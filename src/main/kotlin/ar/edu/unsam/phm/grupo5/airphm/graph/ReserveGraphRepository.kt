package ar.edu.unsam.phm.grupo5.airphm.graph

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReserveGraphRepository: Neo4jRepository<ReserveGraph, Long> {
//    fun findByReserveId(reserveId: Long): Optional<ReserveGraph>
}
