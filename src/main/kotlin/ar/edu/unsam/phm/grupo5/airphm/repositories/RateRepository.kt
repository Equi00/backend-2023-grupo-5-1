package ar.edu.unsam.phm.grupo5.airphm.repositories

import ar.edu.unsam.phm.grupo5.airphm.lodgment.Lodgment
import ar.edu.unsam.phm.grupo5.airphm.lodgment.RateData
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RateRepository: CrudRepository<RateData, Long> {
    fun getAllByLodgmentRateId(lodgmentRateId: String): List<RateData>
    fun findTop3ByLodgmentRateIdOrderByIdDesc(lodgmentId: String): List<RateData>
}