package ar.edu.unsam.phm.grupo5.airphm.graph

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode
import java.time.LocalDate

@RelationshipProperties
class ReserveGraph {

    @Id @GeneratedValue
    var id: Long? = null

//    var reserveId: Long? = null

    lateinit var startDate: LocalDate

    @TargetNode
    lateinit var lodgement: LodgementGraph


}
