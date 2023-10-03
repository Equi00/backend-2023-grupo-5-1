package ar.edu.unsam.phm.grupo5.airphm.domain

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "audit_logs")
data class AuditLog(
    val userId: Long,
    val lodgementName: String,
    val lodgementId: String,
    val action: AuditActionEnum,
    val date: LocalDate = LocalDate.now()
)
