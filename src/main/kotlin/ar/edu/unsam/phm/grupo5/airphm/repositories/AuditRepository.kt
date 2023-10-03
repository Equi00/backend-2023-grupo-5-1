package ar.edu.unsam.phm.grupo5.airphm.repositories

import ar.edu.unsam.phm.grupo5.airphm.domain.AuditActionEnum
import ar.edu.unsam.phm.grupo5.airphm.domain.AuditLog
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AuditRepository: MongoRepository<AuditLog, String> {

    fun findByUserIdAndAction(userId: Long, action: AuditActionEnum): List<AuditLog>

}
