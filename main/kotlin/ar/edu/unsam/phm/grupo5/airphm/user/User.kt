package ar.edu.unsam. phm.grupo5.airphm.user

import ar.edu.unsam.phm.grupo5.airphm.dto.*
import ar.edu.unsam.phm.grupo5.airphm.exceptions.BadRequestException
import ar.edu.unsam.phm.grupo5.airphm.graph.ReserveGraph
import ar.edu.unsam.phm.grupo5.airphm.lodgment.Lodgment
import ar.edu.unsam.phm.grupo5.airphm.lodgment.RateData
import ar.edu.unsam.phm.grupo5.airphm.reserve.Reserve
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.persistence.*

@Entity
@Table(name = "client")
class User() {
    constructor(
        _name: String,
        _surname: String,
        _country: String,
        _balance: Int,
        _birthDate: LocalDate
    ) : this() {
        name = _name
        surname = _surname
        countryOfResidence = _country
        balance = _balance
        birthDate = _birthDate
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @Column(length = 50)
    lateinit var name: String

    @Column(length = 50)
    lateinit var surname: String

    @Column(length = 50)
    lateinit var countryOfResidence: String

    @Column
    var balance: Int = 0

    @Column
    var birthDate: LocalDate = LocalDate.now()

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var reserves: MutableSet<Reserve> = mutableSetOf()

    @JoinColumn
    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn
    var friends: MutableList<User> = mutableListOf()

    @Column(length = 75)
    var email: String = ""

    @JsonIgnore
    var password: String = ""

    fun lodgmentReserve(lodgmentId: String, startDate: LocalDate, endDate: LocalDate, totalCost: Int){
        if (this.canReserve(lodgmentId, totalCost)) {
            val reserve = Reserve(this, lodgmentId, startDate, endDate, totalCost) // Faltan las Querys
            reserves.add(reserve)
            balance -= reserve.cost
        }
        else { throw BadRequestException("El usuario no puede reservar este alojamiento!") }
    }

    fun rateLodgment(lodgment: Lodgment, score: Int, commentary: String){
        if (!this.canRateLodgment(lodgment.id)) {
            throw BadRequestException("El usuario no puede puntuar un hospedaje que no reservo!") }
        lodgment.addScore(RateData(score, commentary, this, lodgment.id))
    }

    fun rechargeBalance(cash: Int){
        balance += cash
    }

    fun addFriend(user: User) { friends.add(user) }

    fun removeFriend(user: User){ friends.remove(user) }

    fun isFriend(user: User): Boolean = friends.contains(user)

    fun age(): Int = ChronoUnit.YEARS.between(birthDate,LocalDate.now()).toInt()

    fun isValid(): Boolean = name.isNotBlank() &&
            surname.isNotBlank() &&
            countryOfResidence.isNotBlank() &&
            balance >= 0F &&
            this.age() >= 18

    fun userToDto(user: User): UserDTO {

        return UserDTO(
            id = id,
            name = name,
            surname = surname,
            countryOfResidence = countryOfResidence,
            balance = balance,
            birthDate = birthDate,
            email = email
        )
    }

    fun toUserUpdateDTO() = FullUserDTO(
        id = id,
        name = name,
        surname = surname,
        countryOfResidence = countryOfResidence,
        balance = balance,
        birthDate = birthDate,
        email = email,
        reserves = reserves,
        friends = friends.map { it.toFriendDTO() },
    )

    fun toFriendDTO() = FriendDTO(
        id = id,
        name = name,
        surname = surname
    )

    private fun canReserve(lodgmentId: String, totalCost: Int): Boolean = (balance >= totalCost) &&
                !canRateLodgment(lodgmentId)

    private fun canRateLodgment(lodgmentId: String): Boolean = reserves.any { it.lodgmentId == lodgmentId }

    fun toResponse() = UserResponse(
        id = id,
        name = name
    )

    fun hasOverlappedReserves(): Boolean {
        return if (reserves.count() > 1) reserves.any {
            reserve -> val remainingReserves = reserves.filter { it != reserve }
            return remainingReserves.any { reserve.overlaps(remainingReserves) }
        } else { false }
    }
}
