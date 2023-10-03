package ar.edu.unsam.phm.grupo5.airphm.repositories

import ar.edu.unsam.phm.grupo5.airphm.reserve.Reserve
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ReserveRepository: CrudRepository<Reserve, Long> {
}