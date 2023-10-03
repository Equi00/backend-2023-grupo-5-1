package ar.edu.unsam.phm.grupo5.airphm.graph

import ar.edu.unsam.phm.grupo5.airphm.lodgment.Lodgment
import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node("Lodgement")
class LodgementGraph {

    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var lodgementId: String

    lateinit var imageUrl: String

    lateinit var name: String

    lateinit var description: String

    var baseCost: Int? = null

    var capacity: Int? = null

    lateinit var address: String

    lateinit var country: String

    var rateAverage: Float? = null

    var rateCount: Int? = null

    lateinit var type: String

    fun fromLodgement(lodgement: Lodgment, type: String): LodgementGraph {
        this.lodgementId = lodgement.id
        this.imageUrl = lodgement.imageUrl
        this.name = lodgement.name
        this.description = lodgement.description
        this.baseCost = lodgement.baseCost
        this.capacity = lodgement.capacity
        this.address = lodgement.address
        this.country = lodgement.country
        this.rateAverage = lodgement.rateAverage
        this.rateCount = lodgement.rateCount
        this.type = type
        return this
    }
}