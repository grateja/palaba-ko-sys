package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.model.EnumWashType
import com.csi.palabakosys.room.db.MainDatabase
import com.csi.palabakosys.room.entities.EntityPackageService
import kotlinx.coroutines.*

class DatabaseSeeder(val db: MainDatabase) {
    fun run() {
        CoroutineScope(Job() + Dispatchers.Main).launch {
            runBlocking {
                UsersSeeder(db.daoUser()).seed()
                MachinesSeeder(db.daoMachine()).seed()
                val washServices = WashServicesSeeder(db.daoWashService()).seed()
                val products = ProductsSeeder(db.daoProduct()).seed()
                val extras = ExtrasSeeder(db.daoExtras()).seed()
                val deliveries = DeliveryProfileSeeder(db.daoDeliveryProfile()).seed()
                val discounts = DiscountsSeeder(db.daoDiscount()).seed()
                val packages = PackageSeeder(db.daoJobOrderPackage()).seed()
                val packageServices: MutableList<EntityPackageService> = mutableListOf()

                packages.forEach {  _package ->
                    if(_package.packageName == "Package 1") {
                        val regularWash = washServices.find {it.name == "Warm Wash" && it.serviceRef.washType == EnumWashType.WARM && it.serviceRef.machineType == EnumMachineType.REGULAR_WASHER}
                        val regularDry = washServices.find {it.name == "Regular Dry" && it.serviceRef.machineType == EnumMachineType.REGULAR_DRYER}
                        if(regularWash == null || regularDry == null) return@forEach
                        packageServices.add(EntityPackageService(_package.id, regularWash.id, 2))
                        packageServices.add(EntityPackageService(_package.id, regularDry.id, 2))
                    } else if(_package.packageName == "Package 2") {
                        val titanWash = washServices.find {it.name == "Warm Wash" && it.serviceRef.washType == EnumWashType.WARM && it.serviceRef.machineType == EnumMachineType.TITAN_WASHER}
                        val titanDry = washServices.find {it.name == "Regular Dry" && it.serviceRef.machineType == EnumMachineType.TITAN_DRYER}
                        val regularWash = washServices.find {it.name == "Warm Wash" && it.serviceRef.washType == EnumWashType.WARM && it.serviceRef.machineType == EnumMachineType.REGULAR_WASHER}
                        val regularDry = washServices.find {it.name == "Regular Dry" && it.serviceRef.machineType == EnumMachineType.REGULAR_DRYER}
                        if(titanWash == null || titanDry == null || regularWash == null || regularDry == null) return@forEach
                        packageServices.add(EntityPackageService(_package.id, titanWash.id, 2))
                        packageServices.add(EntityPackageService(_package.id, titanDry.id, 2))
                        packageServices.add(EntityPackageService(_package.id, regularWash.id, 1))
                        packageServices.add(EntityPackageService(_package.id, regularDry.id, 1))
                    }
                    val fold = extras.find { it.name == "8KG Fold" }
                    val ariel = products.find { it.name == "Ariel" }

                    db.daoJobOrderPackage().syncServices(packageServices)
                }
            }
        }
    }
}