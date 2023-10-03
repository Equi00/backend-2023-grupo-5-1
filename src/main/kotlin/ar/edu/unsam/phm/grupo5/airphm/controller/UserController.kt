package ar.edu.unsam.phm.grupo5.airphm.controller

import ar.edu.unsam.phm.grupo5.airphm.domain.AuditActionEnum
import ar.edu.unsam.phm.grupo5.airphm.domain.AuditLog
import ar.edu.unsam.phm.grupo5.airphm.dto.*
import ar.edu.unsam.phm.grupo5.airphm.repositories.UserRepository
import ar.edu.unsam.phm.grupo5.airphm.service.AuditService
import ar.edu.unsam.phm.grupo5.airphm.service.UserService
import ar.edu.unsam.phm.grupo5.airphm.user.User
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/user")
@CrossOrigin("*")
class UserController (val auditService: AuditService) {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping
    @Operation(summary = "Devuelve todos los usuarios")
    fun getAll(): List<UserDTO> = userRepository.findAll().toList().map { user -> user.userToDto(user) }

    @GetMapping("/{id}")
    @Operation(summary = "Devuelve un usuario por ID")
    fun getOne(@PathVariable id: Long): FullUserDTO {
        val user = userService.getById(id)
        return user.toUserUpdateDTO()
    }

    @GetMapping("/{username}/{password}")
    fun authenticateUser(@PathVariable username: String, @PathVariable password: String): ResponseEntity<UserResponse> {
        val result = userService.autheticateUser(username, password)
        return ResponseEntity.ok(result.toResponse())
    }

    @GetMapping("/name/{username}")
    fun getOneUser(@PathVariable username: String): FullUserDTO {
        val list = username.split(" ")
        return userRepository.findByNameIgnoreCaseAndSurnameIgnoreCase(list.get(0), list.get(1)).toUserUpdateDTO()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody userRequestUpdate: UpdateUserDTO): ResponseEntity<UserResponse> {
        userRepository.findById(id).get()
        val user = userService.update(id, userRequestUpdate.toEntity(id, userRepository))
        return ResponseEntity.ok(user.toResponse())
    }

    @GetMapping("/search/{toSearch}")
    @Operation(summary = "Devuelve todos los usuarios que coincidan con la busqueda")
    fun search(@PathVariable toSearch: String) = userRepository.findAllByNameOrSurname(toSearch, toSearch)

    @PutMapping("/{id}/{cash}")
    fun rechargeUserBalance(@PathVariable id: Long, @PathVariable cash: Int) = userService.addUserBalance(id, cash)

    @GetMapping("/{id}/logs")
    fun getUserAuditLogs(@PathVariable id: Long, @RequestParam action: AuditActionEnum): ResponseEntity<List<AuditLog>> {
        return ResponseEntity.ok(auditService.getUserAuditLogsOfAction(id, action))
    }
}
