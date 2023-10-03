package ar.edu.unsam.phm.grupo5.airphm.graph

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node("User")
class UserGraph (@Id var id: Long? = null) {
    @Relationship(type = "FRIEND_OF", direction = Relationship.Direction.OUTGOING)
    var friends: MutableList<UserGraph> = mutableListOf()

    @Relationship(type = "RESERVED_BY", direction = Relationship.Direction.INCOMING)
    var reserves: MutableList<LodgementGraph> = mutableListOf()

}
