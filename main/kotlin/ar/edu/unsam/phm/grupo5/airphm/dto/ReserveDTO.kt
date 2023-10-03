package ar.edu.unsam.phm.grupo5.airphm.dto

import ar.edu.unsam.phm.grupo5.airphm.reserve.Reserve
import java.time.LocalDate

class ReserveDTO(var startDate: LocalDate, var endDate: LocalDate){
    fun overlaps(reserves: List<ReserveDTO>)  = reserves.any { (this.startDate in it.startDate.rangeTo(it.endDate) ) || (this.endDate in it.startDate.rangeTo(it.endDate) )  }
}