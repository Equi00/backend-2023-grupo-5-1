package ar.edu.unsam.phm.grupo5.airphm.repositories

import ar.edu.unsam.phm.grupo5.airphm.lodgment.*
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.time.LocalDate
import java.util.*


@Repository
interface LodgmentRepository : PagingAndSortingRepository<Lodgment, String>, MongoRepository<Lodgment, String> {

    fun findByName(name: String): Lodgment?

    override fun findById(id: String): Optional<Lodgment>

    fun findAllByOwnerId(ownerId: Long): Iterable<Lodgment>

    @Query("{\"capacity\": {\"\$gte\": ?3}, \"rateAverage\": {\"\$gte\": ?4}, \"country\": {\"\$regex\": ?2, \"\$options\": \"i\"}, \"reserves\": {\"\$not\": {\"\$elemMatch\": {\"\$and\": [{\"startDate\": {\"\$lte\": ?1}, \"endDate\": {\"\$gte\": ?0}}]}}}}")
    fun findByDateFromDateToCountryPassengersAndRate(
        dateFrom: Optional<LocalDate>,
        dateTo: Optional<LocalDate>,
        country: String,
        passengers: Int,
        minRate: Float,
        pageable: Pageable
    ): Page<Lodgment>
}
