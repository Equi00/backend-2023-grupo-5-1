package ar.edu.unsam.phm.grupo5.airphm.controller

import ar.edu.unsam.phm.grupo5.airphm.dto.RateDataDTO
import ar.edu.unsam.phm.grupo5.airphm.graph.LodgementGraph
import ar.edu.unsam.phm.grupo5.airphm.lodgment.*
import ar.edu.unsam.phm.grupo5.airphm.repositories.RateRepository
import ar.edu.unsam.phm.grupo5.airphm.repositories.UserRepository
import ar.edu.unsam.phm.grupo5.airphm.reserve.Reserve
import ar.edu.unsam.phm.grupo5.airphm.service.AuditService
import ar.edu.unsam.phm.grupo5.airphm.service.LodgmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("api/lodgment")
@CrossOrigin("*")
class LodgmentController(val auditService: AuditService) {

    @Autowired
    lateinit var rateRepository: RateRepository

    @Autowired
    lateinit var lodgmentService: LodgmentService

    @Autowired
    lateinit var userRepository: UserRepository

    @Value("\${api.max-results}")
    private val maxResults: Int = 0

    @GetMapping("/search")
    fun getAll(
        @RequestParam("page") page: Int,
    ) = lodgmentService.getAll(page)

    @GetMapping("/{id}")
    fun getOne(@RequestHeader("X-User-Id") userId: Long, @PathVariable id: String): LodgmentDetailDTO {
        val lodgment = lodgmentService.getById(id)
        val rates = rateRepository.findTop3ByLodgmentRateIdOrderByIdDesc(lodgment.id)

        auditService.registerLodgementDetailAccess(userId, lodgment.name, lodgment.id)

        return lodgment.toDetailDTO(rates)
    }

    @GetMapping("/owner")
    fun getAllByOwnerId(
        @RequestParam("id") ownerId: Optional<Long>
    ): ResponseEntity<List<LodgmentDetailDTO>> {
        val lodgments = lodgmentService.findAllByOwnerId(ownerId.get())
        return ResponseEntity.ok().body(lodgments)
    }

    @GetMapping fun getLodgements(
        @RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") dateFrom: Optional<LocalDate>,
        @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") dateTo: Optional<LocalDate>,
        @RequestParam("country") country: Optional<String>,
        @RequestParam("passengers") passengers: Optional<Int>,
        @RequestParam("min_rate") minRate: Optional<Int>,
        @RequestParam("page") page: Int,
        @RequestHeader("X-User-Id") userId: Long,
        request: HttpServletRequest
    ): ResponseEntity<List<LodgementGraph>> {
        val lodgements = lodgmentService.getLodgements(
            dateFrom, dateTo, country, passengers, minRate, page, userId
        )

        val nextPage = if (lodgements.size < maxResults) null else page + 1
        val headers = HttpHeaders()
        headers.set("X-Next-Page", nextPage.toString())
        headers.set("Access-Control-Expose-Headers", "X-Next-Page")

        return ResponseEntity.ok().headers(headers).body(lodgements) }

    @PostMapping("/{id}/rate")
    fun rate(@PathVariable id: String, @RequestBody rateDataDTO: RateDataDTO): ResponseEntity<String> {
        lodgmentService.rateLodgement(id, rateDataDTO)
        return ResponseEntity.ok("Lodgement successfully rated.")
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<String> {
        val lodgment = lodgmentService.deleteById(id)
        return ResponseEntity.ok("Lodgement eliminado.")
    }

    @PostMapping()
    fun create(@RequestBody lodgment: Lodgment) {
        lodgmentService.createOne(lodgment)
    }

    @PostMapping("/{lodgmentId}/reserve")
    fun saveReserve( @PathVariable lodgmentId: String, @RequestBody reserve: Reserve) = lodgmentService.saveReserve(lodgmentId, reserve)

    @PutMapping()
    fun update(@RequestBody lodgment: Lodgment) = lodgmentService.update(lodgment)
}
