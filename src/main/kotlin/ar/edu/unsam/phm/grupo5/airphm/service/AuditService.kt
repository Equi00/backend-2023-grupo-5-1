package ar.edu.unsam.phm.grupo5.airphm.service

import ar.edu.unsam.phm.grupo5.airphm.domain.AuditActionEnum
import ar.edu.unsam.phm.grupo5.airphm.domain.AuditLog
import ar.edu.unsam.phm.grupo5.airphm.repositories.AuditRepository
import org.springframework.stereotype.Service

@Service
class AuditService (val auditRepository: AuditRepository, val userService: UserService)  {

    fun registerLodgementDetailAccess(user: Long, lodgementName: String, lodgementId: String) {

        userService.getById(user)

        auditRepository.save(
            AuditLog(
                user,
                lodgementName,
                lodgementId,
                AuditActionEnum.LODGEMENT_DETAIL_ACCESS
            )
        )
    }

    fun getUserAuditLogsOfAction(user: Long, action: AuditActionEnum): List<AuditLog> {
        return auditRepository.findByUserIdAndAction(user, action)
    }
}
