package ar.edu.unsam.phm.grupo5.airphm.graph

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LodgementGraphRepository: Neo4jRepository<LodgementGraph, Long> {
    @Query("MATCH (u:User {id: \$userId})-[:FRIEND_OF]->(f:User)<-[:RESERVED_BY]-(l:Lodgement) RETURN DISTINCT l")
    fun findSuggestionsByUserId(userId: Long): List<LodgementGraph>

    @Query("MATCH (l:Lodgement {lodgementId: \$lodgementId}) RETURN l")
    fun findByLodgementId(lodgementId: String): Optional<LodgementGraph>
}
