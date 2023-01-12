package com.csi.palabakosys.di

import android.content.Context
import com.csi.palabakosys.room.dao.*
import com.csi.palabakosys.room.db.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideMainDatabase(@ApplicationContext context: Context) : MainDatabase {
//        return Room.databaseBuilder(context, MainDatabase::class.java, MainDatabase.DATABASE_NAME)
//            .fallbackToDestructiveMigration()
//            .build()
        return MainDatabase.getInstance(context)
    }

//    @Singleton
    @Provides
    fun provideDaoUser(mainDatabase: MainDatabase) : DaoUser {
        return mainDatabase.daoUser()
    }

//    @Singleton
    @Provides
    fun provideDaoShop(mainDatabase: MainDatabase) : DaoShop {
        return mainDatabase.daoShop()
    }

//    @Singleton
    @Provides
    fun provideDaoMachine(mainDatabase: MainDatabase) : DaoMachine {
        return mainDatabase.daoMachine()
    }

    //    @Singleton
    @Provides
    fun provideDaoWashService(mainDatabase: MainDatabase) : DaoWashService {
        return mainDatabase.daoWashService()
    }
    @Provides
    fun provideDaoDryService(mainDatabase: MainDatabase) : DaoDryService {
        return mainDatabase.daoDryService()
    }
    @Provides
    fun provideDaoOtherService(mainDatabase: MainDatabase) : DaoOtherService {
        return mainDatabase.daoOtherService()
    }
    @Provides
    fun provideDaoProduct(mainDatabase: MainDatabase) : DaoProduct {
        return mainDatabase.daoProduct()
    }
    @Provides
    fun provideDaoJobOrder(mainDatabase: MainDatabase) : DaoJobOrder {
        return mainDatabase.daoJobOrder()
    }
//    @Provides
//    fun provideDaoJobOrderService(mainDatabase: MainDatabase) : DaoJobOrderService {
//        return mainDatabase.daoJobOrderService()
//    }
//    @Provides
//    fun provideDaoJobOrderProduct(mainDatabase: MainDatabase) : DaoJobOrderProduct {
//        return mainDatabase.daoJobOrderProduct()
//    }
    @Provides
    fun provideDaoCustomer(mainDatabase: MainDatabase) : DaoCustomer {
        return mainDatabase.daoCustomer()
    }

    @Provides
    fun provideDaoDiscount(mainDatabase: MainDatabase) : DaoDiscount {
        return mainDatabase.daoDiscount()
    }

    @Provides
    fun provideDaoCashlessProvider(mainDatabase: MainDatabase) : DaoCashlessProvider {
        return mainDatabase.daoCashlessProvider()
    }

    @Provides
    fun provideDaoExpense(mainDatabase: MainDatabase) : DaoExpense {
        return mainDatabase.daoExpense()
    }

//    @Provides
//    fun provideDaoLoyaltyPoints(mainDatabase: MainDatabase) : DaoLoyaltyPoints {
//        return mainDatabase.daoLoyaltyPoints()
//    }

    @Provides
    fun provideDaoJobOrderPayment(mainDatabase: MainDatabase) : DaoJobOrderPayment {
        return mainDatabase.daoJobOrderPayment()
    }

    @Provides
    fun provideDaoJobOrderQueues(mainDatabase: MainDatabase) : DaoJobOrderQueues {
        return mainDatabase.daoJobOrderQueues()
    }

    @Provides
    fun provideRemoteDao(mainDatabase: MainDatabase) : DaoRemote {
        return mainDatabase.daoRemote()
    }
}