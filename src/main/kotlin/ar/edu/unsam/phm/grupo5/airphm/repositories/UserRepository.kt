package ar.edu.unsam.phm.grupo5.airphm.repositories

import ar.edu.unsam.phm.grupo5.airphm.user.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, Long> {

    @EntityGraph(attributePaths = ["reserves", "friends"])
    fun findByNameIgnoreCaseAndSurnameIgnoreCase(name: String, surname: String): User

    fun findAllByNameOrSurname(name: String, surname: String): List<User>

    @EntityGraph(attributePaths = ["reserves", "friends"])
    override fun findById(id: Long): Optional<User>
}
