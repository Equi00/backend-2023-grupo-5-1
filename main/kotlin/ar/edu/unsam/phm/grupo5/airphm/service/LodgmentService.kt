package ar.edu.unsam.phm.grupo5.airphm.service


import ar.edu.unsam.phm.grupo5.airphm.cache.LodgmentCache
import ar.edu.unsam.phm.grupo5.airphm.dto.RateDataDTO
import ar.edu.unsam.phm.grupo5.airphm.dto.ReserveDTO
import ar.edu.unsam.phm.grupo5.airphm.exceptions.BadRequestException
import ar.edu.unsam.phm.grupo5.airphm.exceptions.ElementNotFoundException
import ar.edu.unsam.phm.grupo5.airphm.graph.*
import ar.edu.unsam.phm.grupo5.airphm.lodgment.*
import ar.edu.unsam.phm.grupo5.airphm.repositories.*
import ar.edu.unsam.phm.grupo5.airphm.reserve.Reserve
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional


@Service
class LodgmentService () {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var lodgmentRepository: LodgmentRepository

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var userGraphRepository: UserGraphRepository

    @Autowired
    lateinit var reserveRepository: ReserveRepository
    
    @Autowired
    lateinit var rateRepository: RateRepository

    @Autowired
    lateinit var lodgmentCacheRepository: LodgmentCacheRepository

    @Autowired
    lateinit var lodgementGraphRepository: LodgementGraphRepository

    @Autowired
    lateinit var reserveGraphRepository: ReserveGraphRepository
    
    @Value("\${api.max-results}")
    private val maxResults: Int = 0

    @Transactional
    fun getById(lodgmentId: String): Lodgment {
        return lodgmentRepository.findById(lodgmentId)
            .orElseThrow { ElementNotFoundException("Lodgement with id <$lodgmentId> not found.") }
    }

    @Transactional
    fun getAll(page: Int): List<LodgementGraph> {
        return lodgementsFromPage(
            lodgmentRepository.findAll(PageRequest.of(page, maxResults))
        )

    }

    @Transactional
    fun findAllByOwnerId(ownerId: Long) =
        lodgmentRepository.findAllByOwnerId(ownerId).map { ldg -> ldg.toDetailDTO(
            rateRepository.getAllByLodgmentRateId(ldg.id)
        ) }

    @Transactional
    fun createOne(newLodgment: Lodgment) {
        if (!newLodgment.isValid()) {
            throw BadRequestException("El/los datos del alojamiento no son validos")
        }
        lodgmentRepository.save(newLodgment)

    }

    @Transactional
    fun createMany(lodgments: List<Lodgment>) {
        lodgmentRepository.saveAll(lodgments)
    }

    @Transactional
    fun update(lodgmentId: Long, lodgment: Lodgment) {
        if (lodgment.id != lodgmentId.toString()) {
            throw BadRequestException("Id en URL distinto del id que viene en el body")
        }
        lodgmentRepository.save(lodgment)
    }

    fun update(lodgment: Lodgment) {
        this.update(lodgment.id.toLong(), lodgment)
    }


    fun getLodgements(
        dateFrom: Optional<LocalDate>, dateTo: Optional<LocalDate>,
        country: Optional<String>, passengers: Optional<Int>, minRate: Optional<Int>, page: Int,
        userId: Long
    ): List<LodgementGraph> {
        if(dateFrom.isEmpty && dateTo.isEmpty && country.isEmpty && passengers.isEmpty && minRate.isEmpty) {
            //Ensure that user exists.
            userService.getById(userId)

            return lodgementGraphRepository.findSuggestionsByUserId(userId)
        }

        val criteria = Criteria()

        dateTo.ifPresent { criteria.and("dateFrom").lte(it) }
        dateFrom.ifPresent { criteria.and("dateTo").gte(it) }
        country.ifPresent { criteria.and("country").`is`(it) }
        passengers.ifPresent { criteria.and("passengers").gte(it) }
        minRate.ifPresent { criteria.and("rateAverage").gte(it.toFloat()) }

        val pageable = PageRequest.of(page, maxResults)

        val newLodgmentCache = LodgmentCache(dateFrom.orElse(null), dateTo.orElse(null), country.orElse(null),
            passengers.orElse(null), minRate.orElse(null))

        val lodgmentCache: LodgmentCache? = lodgmentCacheRepository.findById(newLodgmentCache.id).orElse(null)

        if (lodgmentCache != null) {
            return lodgmentCache.lodgments
        }

        val returnLodgment = lodgementsFromPage(
            lodgmentRepository.findByDateFromDateToCountryPassengersAndRate(
                dateFrom,
                dateTo,
                country.orElse(""),
                passengers.orElse(0),
                minRate.map { it.toFloat() }.orElse(0f),
                pageable
            )
        )

        newLodgmentCache.lodgments = returnLodgment
        lodgmentCacheRepository.save(newLodgmentCache)

        return returnLodgment
    }

    @Transactional
    fun saveReserve(
        lodgmentId: String,
        reserve: Reserve
    ) {
        val lodgment = this.getById(lodgmentId)
        val user = userRepository.findById(reserve.user.id).get()
        val lodgmentReserves: List<Reserve> = lodgment.reserves.map { Reserve(user, lodgmentId, it.startDate, it.endDate, lodgment.totalCost()) }

        if (reserve.overlaps(lodgmentReserves) || reserve.overlaps(user.reserves.toList())) {
            throw BadRequestException("Ya hay reserva durante estas fechas")
        } else {
            lodgment.reserves.add(ReserveDTO(reserve.startDate, reserve.endDate))
            lodgmentRepository.save(lodgment)
            user.reserves.add(reserve)
            userRepository.save(user)

            var userGraph = userGraphRepository.findById(user.id)

            var lodgementGraph = lodgementGraphRepository.findByLodgementId(lodgment.id)

            if(lodgementGraph.isEmpty) {
               lodgementGraph = Optional.of(lodgementGraphRepository.save(LodgementGraph().fromLodgement(lodgment, lodgment.javaClass.simpleName)))
            }

            if (userGraph.isEmpty) {
                userGraph = Optional.of(userGraphRepository.save(UserGraph(user.id)))
            }

            userGraphRepository.createReservedByRelationship(
                userId = userGraph.get().id!!,
                lodgmentId = lodgementGraph.get().lodgementId,
                startDate = reserve.startDate
            )
        }
    }

    fun deleteById(id: String): Lodgment {
        val lodgement = this.getById(id)
        lodgmentRepository.delete(lodgement)
        return lodgement
    }

    @Transactional
    fun rateLodgement(lodgementId: String, rateDataDTO: RateDataDTO) {
      val lodgement = this.getById(lodgementId)
        val rateData = RateData(rateDataDTO.score, rateDataDTO.commentary, userService.getById(rateDataDTO.userId), lodgementId.toString())

        if (!rateData.isValid()) { throw BadRequestException("La calificacion es invalida") }
        lodgement.updateAverageAndCount(rateData.rateScore)
        lodgmentRepository.save(lodgement)
        rateRepository.save(rateData)
    }


    private fun lodgementsFromPage(lodgements: Page<Lodgment>): List<LodgementGraph> {
        if (lodgements.isEmpty) throw ElementNotFoundException("No lodgements found.")
        return lodgements.content.map { LodgementGraph().fromLodgement(it, it.javaClass.simpleName) }
    }

}
