package com.csi.palabakosys.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.csi.palabakosys.room.dao.*
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.util.*

@Database(entities = [
    EntityUser::class,
    EntityShop::class,
    EntityMachine::class,
    EntityMachineRemarks::class,
    EntityMachineUsage::class,
    EntityServiceWash::class,
    EntityServiceDry::class,
    EntityServiceOther::class,
    EntityProduct::class,
    EntityJobOrder::class,
    EntityJobOrderService::class,
    EntityJobOrderProduct::class,
    EntityJobOrderPayment::class,
    EntityCustomer::class,
    EntityExpense::class,
    EntityInventoryLog::class,
    EntityDiscount::class,
    EntityCashlessProvider::class
], version = 1)
@TypeConverters(
    InstantConverters::class,
    UUIDConverter::class,
    RoleConverter::class,
    WashTypeConverter::class,
    MachineTypeConverter::class,
    PaymentMethodConverter::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun daoUser() : DaoUser
    abstract fun daoShop() : DaoShop
    abstract fun daoMachine() : DaoMachine
    abstract fun daoMachineRemarks() : DaoMachineRemarks
    abstract fun daoMachineUsage() : DaoMachineUsage
    abstract fun daoWashService() : DaoWashService
    abstract fun daoDryService() : DaoDryService
    abstract fun daoOtherService() : DaoOtherService
    abstract fun daoProduct() : DaoProduct
    abstract fun daoJobOrder() : DaoJobOrder
    abstract fun daoJobOrderPayment() : DaoJobOrderPayment
    abstract fun daoCustomer() : DaoCustomer
    abstract fun daoExpense() : DaoExpense
    abstract fun daoInventoryLog() : DaoInventoryLog
    abstract fun daoDiscount() : DaoDiscount
    abstract fun daoCashlessProvider() : DaoCashlessProvider

    abstract fun daoJobOrderQueues() : DaoJobOrderQueues
    abstract fun daoRemote() : DaoRemote

    companion object {
        val DATABASE_NAME: String = "main_db"

        private var instance: MainDatabase? = null

        fun getInstance(context: Context) : MainDatabase {
            return instance?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, MainDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }
//            return Room.databaseBuilder(context, MainDatabase::class.java, DATABASE_NAME)
//                .fallbackToDestructiveMigration()
//                .build()
        }
    }
}