package ar.edu.unsam.phm.grupo5.airphm.repositories

import ar.edu.unsam.phm.grupo5.airphm.cache.LodgmentCache
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LodgmentCacheRepository: CrudRepository<LodgmentCache, String> {
}