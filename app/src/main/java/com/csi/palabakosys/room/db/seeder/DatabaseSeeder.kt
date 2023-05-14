package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.room.db.MainDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DatabaseSeeder(val db: MainDatabase) {
    fun run() {
        CoroutineScope(Job() + Dispatchers.Main).launch {
            WashServicesSeeder(db.daoWashService()).seed()
            ExtrasSeeder(db.daoExtras()).seed()
            MachinesSeeder(db.daoMachine()).seed()
            ProductsSeeder(db.daoProduct()).seed()
            DeliveryProfileSeeder(db.daoDeliveryProfile()).seed()
            DiscountsSeeder(db.daoDiscount()).seed()
            UsersSeeder(db.daoUser()).seed()
        }
    }
}