package ar.edu.unsam.phm.grupo5.airphm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["ar.edu.unsam.phm.*"])
class AirphmApplication

fun main(args: Array<String>) {
    runApplication<AirphmApplication>(*args)
}