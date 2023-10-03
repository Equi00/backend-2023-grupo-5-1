package ar.edu.unsam.phm.grupo5.airphm.reserve

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class CustomReserveSerialize: JsonSerializer<Reserve>() {

    override fun serialize(value: Reserve?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeStartObject()
        gen?.writeObjectField("userId",value?.user?.id)
        gen?.writeObjectField("lodgment", value?.lodgmentId)
        gen?.writeObjectField("startDate",value?.startDate)
        gen?.writeObjectField("endDate",value?.endDate)
        gen?.writeObjectField("cost",value?.cost)
        gen?.writeEndObject()
    }
}