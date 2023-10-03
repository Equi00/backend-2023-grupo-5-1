package ar.edu.unsam.phm.grupo5.airphm.lodgment

import ar.edu.unsam.phm.grupo5.airphm.dto.RateDataComment
import ar.edu.unsam.phm.grupo5.airphm.dto.ReserveDTO
import ar.edu.unsam.phm.grupo5.airphm.exceptions.BadRequestException
import ar.edu.unsam.phm.grupo5.airphm.graph.ReserveGraph
import com.fasterxml.jackson.annotation.*
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import kotlin.math.roundToInt

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "lodgments")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = Cabin::class, name = "Cabin"),
    JsonSubTypes.Type(value = House::class, name = "House"),
    JsonSubTypes.Type(value = Department::class, name = "Department")
)
//@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn("type")
abstract class Lodgment() {

    constructor(
        _ownerId: Long,
        _baseCost: Int,
        _name: String,
        _description: String,
        _capacity: Int,
        _bedrooms: Int,
        _bathrooms: Int,
        _accommodationDetail: String,
        _otherAspects: String,
        _cleaningService: Boolean,
        _address: String,
        _country: String,
        _imageUrl: String,
    ) : this() {
        ownerId = _ownerId
        baseCost = _baseCost
        name = _name
        description = _description
        capacity = _capacity
        bedrooms = _bedrooms
        bathrooms = _bathrooms
        accommodationDetail = _accommodationDetail
        otherAspects = _otherAspects
        cleaningService = _cleaningService
        address = _address
        country = _country
        imageUrl = _imageUrl
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    open var id: Long = 0
//    @ManyToOne
//    @JoinColumn
//    open var owner: User? = null
//    @Column
//    open var baseCost: Int = 0
//    @Column(length = 50)
//    open lateinit var name: String
//    @Column(length = 500)
//    open lateinit var description: String
//    @Column
//    open var capacity: Int = 0
//    @Column
//    open var bedrooms: Int = 0
//    @Column
//    open var bathrooms: Int = 0
//    @Column(length = 300)
//    open lateinit var accommodationDetail: String
//    @Column(length = 300)
//    open lateinit var otherAspects: String
//    @Column
//    open var cleaningService: Boolean = false
//    @Column(length = 100)
//    open lateinit var address: String
//    @Column(length = 50)
//    open lateinit var country: String
//    @Column(length = 300)
//    open lateinit var imageUrl: String
//    @Column(length = 15, insertable = false, updatable = false)
//    lateinit var type: String
//    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
//    @OrderColumn
//    open var rate: MutableList<RateData> = mutableListOf()
//    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
//    @OrderColumn
//    open var reserves: MutableList<Reserve> = mutableListOf()
//    open var commission: Float = 1.05F
//    @Column
//    var rateAverage: Float = 0F
//    @Column
//    var rateCount: Int = 0

    @Id
    open lateinit var id: String

    open var ownerId: Long = 0

    open var baseCost: Int = 0

    open lateinit var name: String

    open lateinit var description: String

    open var capacity: Int = 0

    open var bedrooms: Int = 0

    open var bathrooms: Int = 0

    open lateinit var accommodationDetail: String

    open lateinit var otherAspects: String

    open var cleaningService: Boolean = false

    open lateinit var address: String

    open lateinit var country: String

    open lateinit var imageUrl: String

    open var reserves: MutableList<ReserveDTO> = mutableListOf()

    open var commission: Float = 1.05F

    var rateAverage: Float = 0F

    var rateCount: Int = 0

    abstract fun plus(): Int

    fun totalCost(): Int = ((baseCost + this.plus()) * commission).roundToInt()

    fun addScore(rateData: RateData) {
        if (!rateData.isValid()) { throw BadRequestException("La calificacion es invalida") }
      //  rate.add(rateData)
        this.updateAverageAndCount(rateData.rateScore)
    }

   // fun averageScore(): Float = (rate.sumOf{ it.rateScore }.toFloat() / Math.max(1,rate.count()))

    fun hasOverlappedReserves(): Boolean {
        return if (reserves.count() > 1) reserves.any {
            reserve ->
            val remainingReserves = reserves.filter { it != reserve }
            return remainingReserves.any { reserve.overlaps(remainingReserves) }
        } else false
    }

   // fun hasCommentOfUser(user: User): Boolean = rate.any { it.userRate == user }


    fun isValid(): Boolean = baseCost > 0 &&
            name.isNotBlank() &&
            description.isNotBlank() &&
            capacity > 0 &&
            bedrooms > 0 &&
            bathrooms > 0 &&
            accommodationDetail.isNotBlank() &&
            otherAspects.isNotBlank() &&
            address.isNotBlank() &&
            country.isNotBlank() &&
            imageUrl.isNotBlank() &&
            !this.hasOverlappedReserves()

    fun updateAverageAndCount(score: Int) {
        rateAverage = ((rateAverage * rateCount) + score) / (rateCount + 1)
        rateCount += 1
    }
}

@Document(collection = "lodgments")
@TypeAlias("Cabin")
@JsonTypeName(value = "Cabin")
class Cabin() : Lodgment() {

    constructor(
        _ownerId: Long,
        _baseCost: Int,
        _name: String,
        _description: String,
        _capacity: Int,
        _bedrooms: Int,
        _bathrooms: Int,
        _accommodationDetail: String,
        _otherAspects: String,
        _cleaningService: Boolean,
        _address: String,
        _country: String,
        _imageUrl: String,
    ) : this() {
        ownerId = _ownerId
        baseCost = _baseCost
        name = _name
        description = _description
        capacity = _capacity
        bedrooms = _bedrooms
        bathrooms = _bathrooms
        accommodationDetail = _accommodationDetail
        otherAspects = _otherAspects
        cleaningService = _cleaningService
        address = _address
        country = _country
        imageUrl = _imageUrl
    }
override fun plus(): Int = if(cleaningService) 10000 else 0
}




@Document(collection = "lodgments")
@TypeAlias("House")
@JsonTypeName(value = "House")
class House() : Lodgment() {
    constructor(
        _ownerId: Long,
        _baseCost: Int,
        _name: String,
        _description: String,
        _capacity: Int,
        _bedrooms: Int,
        _bathrooms: Int,
        _accommodationDetail: String,
        _otherAspects: String,
        _cleaningService: Boolean,
        _address: String,
        _country: String,
        _imageUrl: String
    ) : this() {
        ownerId = _ownerId
        baseCost = _baseCost
        name = _name
        description = _description
        capacity = _capacity
        bedrooms = _bedrooms
        bathrooms = _bathrooms
        accommodationDetail = _accommodationDetail
        otherAspects = _otherAspects
        cleaningService = _cleaningService
        address = _address
        country = _country
        imageUrl = _imageUrl
    }
    override fun plus(): Int = capacity * 500
}

@Document(collection = "lodgments")
@TypeAlias("Department")
@JsonTypeName(value = "Department")
class Department() : Lodgment() {
    constructor(
        _ownerId: Long,
        _baseCost: Int,
        _name: String,
        _description: String,
        _capacity: Int,
        _bedrooms: Int,
        _bathrooms: Int,
        _accommodationDetail: String,
        _otherAspects: String,
        _cleaningService: Boolean,
        _address: String,
        _country: String,
        _imageUrl: String
    ) : this() {
        ownerId = _ownerId
        baseCost = _baseCost
        name = _name
        description = _description
        capacity = _capacity
        bedrooms = _bedrooms
        bathrooms = _bathrooms
        accommodationDetail = _accommodationDetail
        otherAspects = _otherAspects
        cleaningService = _cleaningService
        address = _address
        country = _country
        imageUrl = _imageUrl
    }
    override fun plus(): Int = if(bedrooms < 3) (2000*bedrooms) else (1000*bedrooms)
}

data class LodgmentDetailDTO(
    val id: String,
    val ownerId: Long?,
    val name: String,
    val description: String,
    val baseCost: Int,
    val capacity: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val accommodationDetail: String,
    val otherAspects: String,
    val cleaningService: Boolean,
    val address: String,
    val country: String,
    val imageUrl: String,
    val reserves: MutableList<ReserveDTO>,
    val rate: List<RateDataComment>,
    val rateAverage: Float,
    val rateCount: Int,
    val commission: Float,
    val type: String
)

// como ya no hay mas rates, hay que modificar este DTO

fun Lodgment.toDetailDTO(rates: List<RateData>): LodgmentDetailDTO {
 
    return LodgmentDetailDTO(
        id = id,
        ownerId = ownerId,
        name = name,
        description = description,
        baseCost = baseCost,
        capacity = capacity,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        accommodationDetail = accommodationDetail,
        otherAspects = otherAspects,
        cleaningService = cleaningService,
        address = address,
        country = country,
        imageUrl = imageUrl,
        reserves = reserves,
        rate = rates.map { rateData -> RateDataComment(rateData.rateScore, rateData.commentary, rateData.userRate.userToDto(rateData.userRate), rateData.date) },
        rateAverage = rateAverage,
        rateCount = rateCount,
        commission = commission,
        type = this.javaClass.simpleName
    )
}
