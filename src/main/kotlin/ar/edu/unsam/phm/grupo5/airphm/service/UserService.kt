package ar.edu.unsam.phm.grupo5.airphm.service

import ar.edu.unsam.phm.grupo5.airphm.exceptions.BadRequestException
import ar.edu.unsam.phm.grupo5.airphm.exceptions.ElementNotFoundException
import ar.edu.unsam.phm.grupo5.airphm.exceptions.UnauthorizedUserException
import ar.edu.unsam.phm.grupo5.airphm.repositories.UserRepository
import ar.edu.unsam.phm.grupo5.airphm.reserve.Reserve
import ar.edu.unsam.phm.grupo5.airphm.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getById(userId: Long) = userRepository.findById(userId).orElseThrow {
        ElementNotFoundException("User with id <$userId> not found.")
    }

    @Transactional(Transactional.TxType.NEVER)
    fun update(id: Long, user: User) =
        if (user.id == id) userRepository.save(user)
        else throw ElementNotFoundException("No se encontro el usuario especificado")

    fun autheticateUser(username: String, password: String): User{
        val list = username.split(" ")
        val user = userRepository.findByNameIgnoreCaseAndSurnameIgnoreCase(list.get(0), list.get(1))
        return if(user != null && user.password == password) user
        else throw UnauthorizedUserException("Los datos no pertenecen a ningun usuario")
    }

    @Transactional(Transactional.TxType.NEVER)
    fun addUserBalance(id: Long, cash: Int){
        if (cash < 0){
            throw BadRequestException("No se puede agregar saldo negativo")
        }
        val user = userRepository.findById(id).get()
        user.rechargeBalance(cash)
        update(id, user)
    }

    @Transactional(Transactional.TxType.NEVER)
    fun saveReserve(
        userId: Long,
        reserve: Reserve
    ) {
        val user = this.getById(userId)

        if (user == null) throw ElementNotFoundException("No se encontro el usuario")
        else {
            if (reserve.overlaps(user.reserves.toList())) {
                throw BadRequestException("Ya hay reserva durante la fecha especificada")
            } else {
                user.reserves.add(reserve)
                this.update(userId, user)
            }
        }
    }
}
