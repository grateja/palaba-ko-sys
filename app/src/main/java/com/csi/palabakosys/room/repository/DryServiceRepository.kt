//package com.csi.palabakosys.room.repository
//
//import com.csi.palabakosys.room.dao.DaoDryService
//import com.csi.palabakosys.room.entities.EntityServiceDry
//import javax.inject.Inject
//
//class DryServiceRepository
//@Inject
//constructor (
//    private val daoDryService: DaoDryService,
//) : BaseRepository<EntityServiceDry>(daoDryService) {
//    override suspend fun get(id: String?) : EntityServiceDry? {
//        if(id == null) return null
//        return daoDryService.get(id)
//    }
//
//    suspend fun getAll() : List<EntityServiceDry> {
//        return daoDryService.getAll()
//    }
//}