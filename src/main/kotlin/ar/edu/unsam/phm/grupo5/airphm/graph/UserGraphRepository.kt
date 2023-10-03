package ar.edu.unsam.phm.grupo5.airphm.graph

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface UserGraphRepository: Neo4jRepository<UserGraph, Long> {
    @Query(" MATCH (u: User), (l: Lodgement) WHERE u.id = \$userId AND l.lodgementId = \$lodgmentId CREATE (l)-[:RESERVED_BY { startDate: date(\$startDate)}]->(u) ")
    fun createReservedByRelationship(userId: Long, lodgmentId: String, startDate: LocalDate)
}
