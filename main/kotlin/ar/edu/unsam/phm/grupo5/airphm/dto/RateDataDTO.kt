package ar.edu.unsam.phm.grupo5.airphm.dto

import java.time.LocalDate

data class RateDataDTO(val score: Int, val commentary: String, val userId: Long)
data class RateDataComment(val score: Int, val commentary: String, val user: UserDTO , val date: LocalDate)
