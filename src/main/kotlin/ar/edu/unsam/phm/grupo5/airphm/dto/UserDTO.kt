package ar.edu.unsam.phm.grupo5.airphm.dto

import ar.edu.unsam.phm.grupo5.airphm.lodgment.Lodgment
import ar.edu.unsam.phm.grupo5.airphm.lodgment.LodgmentDetailDTO
import ar.edu.unsam.phm.grupo5.airphm.repositories.UserRepository
import ar.edu.unsam.phm.grupo5.airphm.reserve.Reserve
import ar.edu.unsam.phm.grupo5.airphm.user.User
import java.time.LocalDate

data class UserDTO(
    val id: Long,
    val name: String,
    val surname: String,
    val countryOfResidence: String,
    val balance: Int,
    val birthDate: LocalDate,
    val email: String
)

data class FullUserDTO(
    val id: Long,
    val name: String,
    val surname: String,
    val countryOfResidence: String,
    val balance: Int,
    var birthDate: LocalDate,
    val email: String,
    val reserves: MutableSet<Reserve>,
    val friends: List<FriendDTO>,
)

data class UpdateUserDTO(
    val id: Long,
    val name: String,
    val surname: String,
    val countryOfResidence: String,
    val balance: Int,
    var birthDate: LocalDate,
    val email: String,
    val reserves: MutableSet<Reserve>,
    val friends: MutableList<User>,
)

// Convertion from UpdateUserDTO to a class instance or entity
fun UpdateUserDTO.toEntity(id: Long, userRepository: UserRepository) = User(
).also {
    it.id = id
    it.name = name
    it.surname = surname
    it.countryOfResidence = countryOfResidence
    it.balance = balance
    it.birthDate = birthDate
    it.email = email
    it.reserves = reserves
    it.friends = friends
}

data class FriendDTO(
    val id: Long,
    val name: String,
    val surname: String
)

data class UserResponse(
    val id: Long,
    val name: String
)
