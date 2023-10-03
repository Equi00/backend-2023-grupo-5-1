package ar.edu.unsam.phm.grupo5.airphm.reserve

import ar.edu.unsam.phm.grupo5.airphm.user.User
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*

@JsonSerialize(using = CustomReserveSerialize::class)
@Entity
class Reserve() : Serializable {
    constructor(
        _user: User,
       // _lodgment: Lodgment,
        _lodgmentId: String,
        _startDate: LocalDate,
        _endDate: LocalDate,
        _cost: Int
    ) : this() {
        user = _user
      //  lodgment = _lodgment
        lodgmentId = _lodgmentId
        startDate = _startDate
        endDate = _endDate
        cost = _cost
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    lateinit var user: User

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    lateinit var lodgment: Lodgment
    @Column
    lateinit var lodgmentId: String

    @Column
    lateinit var startDate: LocalDate

    @Column
    lateinit var endDate: LocalDate

    @Column
    var cost: Int = 0

//    fun getUserComment(): RateData? = lodgment.rate.find { it.userRate == user }
//
//    fun hasLodgmentComment(): Boolean = lodgment.hasCommentOfUser(user)
    fun overlaps(reserves: List<Reserve>)  = reserves.any { (this.startDate in it.startDate.rangeTo(it.endDate) ) || (this.endDate in it.startDate.rangeTo(it.endDate) )  }
}

