package ar.edu.unsam.phm.grupo5.airphm.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ElementNotFoundException(notFoundItem: String): RuntimeException("$notFoundItem not found")