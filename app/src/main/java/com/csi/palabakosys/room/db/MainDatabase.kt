package com.csi.palabakosys.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.csi.palabakosys.room.dao.*
import com.csi.palabakosys.room.db.seeder.DatabaseSeeder
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.util.*

@Database(entities = [
    EntityUser::class,
    EntityShop::class,
    EntityMachine::class,
    EntityMachineRemarks::class,
    EntityMachineUsage::class,
    EntityService::class,
//    EntityServiceDry::class,
    EntityExtras::class,
    EntityDeliveryProfile::class,
//    EntityServiceOther::class,
    EntityProduct::class,
    EntityJobOrder::class,
    EntityJobOrderService::class,
    EntityJobOrderProduct::class,
    EntityJobOrderExtras::class,
    EntityJobOrderDeliveryCharge::class,
    EntityJobOrderDiscount::class,
    EntityJobOrderPayment::class,
    EntityCustomer::class,
    EntityExpense::class,
    EntityInventoryLog::class,
    EntityDiscount::class,
    EntityCashlessProvider::class
], version = 5)
@TypeConverters(
    InstantConverters::class,
    UUIDConverter::class,
    RoleConverter::class,
    WashTypeConverter::class,
    MachineTypeConverter::class,
    PaymentMethodConverter::class,
    DeliveryVehicleConverter::class,
    DeliveryOptionConverter::class,
//    DiscountApplicableConverter::class,
//    DiscountTypeConverter::class,
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun daoUser() : DaoUser
    abstract fun daoShop() : DaoShop
    abstract fun daoMachine() : DaoMachine
    abstract fun daoMachineRemarks() : DaoMachineRemarks
    abstract fun daoMachineUsage() : DaoMachineUsage
    abstract fun daoWashService() : DaoService
    abstract fun daoDeliveryProfile() : DaoDeliveryProfile
//    abstract fun daoDryService() : DaoDryService
    abstract fun daoExtras() : DaoExtras
//    abstract fun daoOtherService() : DaoOtherService
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
        private const val DATABASE_NAME: String = "main_db"

        private var instance: MainDatabase? = null

        fun getInstance(context: Context) : MainDatabase {
            return instance?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, MainDatabase::class.java, DATABASE_NAME)
//                    .fallbackToDestructiveMigration()
                    .addCallback(object: Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            DatabaseSeeder(getInstance(context)).run()
                        }
                    })
                    .build()
            }
//            return Room.databaseBuilder(context, MainDatabase::class.java, DATABASE_NAME)
//                .fallbackToDestructiveMigration()
//                .build()
        }
    }
}