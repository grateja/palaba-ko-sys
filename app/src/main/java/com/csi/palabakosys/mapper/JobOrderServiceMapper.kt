//package com.csi.palabakosys.mapper
//
//import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
//import com.csi.palabakosys.room.entities.EntityJobOrderService
//import com.csi.palabakosys.util.Mapper
//
//class JobOrderServiceMapper: Mapper<EntityJobOrderService, MenuServiceItem> {
//    override fun mapFromEntity(entity: EntityJobOrderService): MenuServiceItem {
//        return MenuServiceItem(
//            entity.id,
//            entity.serviceName,
//            entity.serviceRef.minutes,
//            entity.price,
//            entity.serviceRef.machineType,
//            entity.serviceRef.washType,
//            entity.quantity
//        )
//    }
//
//    override fun mapToEntity(domain: MenuServiceItem): EntityJobOrderService {
//        TODO("Not yet implemented")
//    }
//}