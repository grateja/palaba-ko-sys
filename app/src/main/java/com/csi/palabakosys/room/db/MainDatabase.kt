package com.csi.palabakosys.room.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.csi.palabakosys.room.dao.*
import com.csi.palabakosys.room.db.seeder.DatabaseSeeder
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.migrations.AddColumnPackagePrice
import com.csi.palabakosys.util.converters.*

@Database(entities = [
    EntityUser::class,
    EntityShop::class,
    EntityMachine::class,
    EntityMachineRemarks::class,
    EntityMachineUsage::class,
    EntityService::class,
    EntityExtras::class,
    EntityDeliveryProfile::class,
    EntityProduct::class,
    EntityJobOrder::class,
//    EntityJobOrderPackage::class,
    EntityJobOrderService::class,
    EntityJobOrderProduct::class,
    EntityJobOrderExtras::class,
    EntityJobOrderDeliveryCharge::class,
    EntityJobOrderDiscount::class,
    EntityJobOrderPayment::class,
    EntityPackage::class,
    EntityPackageService::class,
    EntityPackageExtras::class,
    EntityPackageProduct::class,
    EntityCustomer::class,
    EntityExpense::class,
    EntityInventoryLog::class,
    EntityDiscount::class,
//    EntityCashlessProvider::class
], version = 2,
    exportSchema = true,
)
@TypeConverters(
    InstantConverters::class,
    UUIDConverter::class,
    RoleConverter::class,
    WashTypeConverter::class,
    MachineTypeConverter::class,
    PaymentMethodConverter::class,
    DeliveryVehicleConverter::class,
    DeliveryOptionConverter::class,
    ProductTypeConverter::class,
    ServiceTypeConverter::class,
    DiscountApplicableConverter::class,
    DiscountTypeConverter::class,
    ActionPermissionConverter::class,
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun daoUser() : DaoUser
    abstract fun daoShop() : DaoShop
    abstract fun daoMachine() : DaoMachine
    abstract fun daoMachineRemarks() : DaoMachineRemarks
    abstract fun daoMachineUsage() : DaoMachineUsage
    abstract fun daoWashService() : DaoService
    abstract fun daoDeliveryProfile() : DaoDeliveryProfile
    abstract fun daoExtras() : DaoExtras
    abstract fun daoProduct() : DaoProduct
    abstract fun daoJobOrder() : DaoJobOrder
    abstract fun daoJobOrderPackage(): DaoPackage
    abstract fun daoJobOrderPayment() : DaoJobOrderPayment
    abstract fun daoCustomer() : DaoCustomer
    abstract fun daoExpense() : DaoExpense
    abstract fun daoInventoryLog() : DaoInventoryLog
    abstract fun daoDiscount() : DaoDiscount
//    abstract fun daoCashlessProvider() : DaoCashlessProvider
    abstract fun daoJobOrderQueues() : DaoJobOrderQueues
    abstract fun daoRemote() : DaoRemote

    companion object {
        private const val DATABASE_NAME: String = "main_db"

        private var instance: MainDatabase? = null

        fun getInstance(context: Context) : MainDatabase {
            return instance?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, MainDatabase::class.java, DATABASE_NAME)
                    .addCallback(object: Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            DatabaseSeeder(getInstance(context)).run()
                        }
                    })
                    .addMigrations(
                        AddColumnPackagePrice()
//                        AddColumnPaymentVoid(),
//                        CreateTablePackage(),
//                        CreateTablePackageService(),
//                        AddColumnsIsPackage()
                    )
                    .build()
            }
        }
    }
}