package ar.edu.unsam.phm.grupo5.airphm.lodgment

import ar.edu.unsam.phm.grupo5.airphm.repositories.BaseData
import ar.edu.unsam.phm.grupo5.airphm.user.User
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*

@Entity
class RateData() : Serializable {
    constructor(
        _rateScore: Int,
        _commentary: String,
        _userRate: User,
        _lodgmentRateId: String
    ) : this() {
        rateScore = _rateScore
        commentary = _commentary
        userRate = _userRate
        lodgmentRateId = _lodgmentRateId
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @OneToOne
    lateinit var userRate: User

    @Column
    lateinit var lodgmentRateId: String

    @Column
    var rateScore: Int = 0

    @Column
    lateinit var commentary: String

    @Column
    var date: LocalDate = LocalDate.now()
    fun isValid(): Boolean = rateScore in 1..5

    //TODO: Que RateData reciba una Reserve (y con la Reserve obtenes que User lo hizo y a que Lodgment)
}
