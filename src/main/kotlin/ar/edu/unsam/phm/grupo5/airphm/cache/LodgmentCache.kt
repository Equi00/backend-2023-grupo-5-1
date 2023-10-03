package ar.edu.unsam.phm.grupo5.airphm.cache

import ar.edu.unsam.phm.grupo5.airphm.graph.LodgementGraph
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.time.LocalDate
import java.util.*

@RedisHash(value = "LodgmentCache", timeToLive = 60)
class LodgmentCache(val dateFrom: LocalDate?, val dateTo: LocalDate?, val country: String?, val passengers: Int?, val minRate: Int?) {
    @Id
    var id: String

    var lodgments: List<LodgementGraph> = listOf()

    init {
        id = generateCacheKey()
    }

    private fun generateCacheKey(): String =
        "lodgments_${dateFrom}_${dateTo}_${country}_${passengers}_${minRate}"
}
