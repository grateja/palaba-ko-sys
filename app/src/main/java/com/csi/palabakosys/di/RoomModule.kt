package com.csi.palabakosys.di

import android.content.Context
import com.csi.palabakosys.preferences.AppPreferenceRepository
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

    @Singleton
    @Provides
    fun provideDaoUser(mainDatabase: MainDatabase) : DaoUser {
        return mainDatabase.daoUser()
    }

    @Singleton
    @Provides
    fun provideDaoShop(mainDatabase: MainDatabase) : DaoShop {
        return mainDatabase.daoShop()
    }

    @Singleton
    @Provides
    fun provideDaoMachine(mainDatabase: MainDatabase) : DaoMachine {
        return mainDatabase.daoMachine()
    }

    @Singleton
    @Provides
    fun provideDaoWashService(mainDatabase: MainDatabase) : DaoService {
        return mainDatabase.daoWashService()
    }
//    @Provides
//    fun provideDaoDryService(mainDatabase: MainDatabase) : DaoDryService {
//        return mainDatabase.daoDryService()
//    }
//    @Singleton
//    @Provides
//    fun provideDaoOtherService(mainDatabase: MainDatabase) : DaoOtherService {
//        return mainDatabase.daoOtherService()
//    }
    @Singleton
    @Provides
    fun provideDaoExtras(mainDatabase: MainDatabase) : DaoExtras {
        return mainDatabase.daoExtras()
    }
    @Singleton
    @Provides
    fun provideDaoProduct(mainDatabase: MainDatabase) : DaoProduct {
        return mainDatabase.daoProduct()
    }
    @Singleton
    @Provides
    fun provideDaoDeliveryProfile(mainDatabase: MainDatabase) : DaoDeliveryProfile {
        return mainDatabase.daoDeliveryProfile()
    }
    @Singleton
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
    @Singleton
    @Provides
    fun provideDaoCustomer(mainDatabase: MainDatabase) : DaoCustomer {
        return mainDatabase.daoCustomer()
    }

    @Singleton
    @Provides
    fun provideDaoDiscount(mainDatabase: MainDatabase) : DaoDiscount {
        return mainDatabase.daoDiscount()
    }

    @Singleton
    @Provides
    fun provideDaoCashlessProvider(mainDatabase: MainDatabase) : DaoCashlessProvider {
        return mainDatabase.daoCashlessProvider()
    }

    @Singleton
    @Provides
    fun provideDaoExpense(mainDatabase: MainDatabase) : DaoExpense {
        return mainDatabase.daoExpense()
    }

//    @Provides
//    fun provideDaoLoyaltyPoints(mainDatabase: MainDatabase) : DaoLoyaltyPoints {
//        return mainDatabase.daoLoyaltyPoints()
//    }

    @Singleton
    @Provides
    fun provideDaoJobOrderPayment(mainDatabase: MainDatabase) : DaoJobOrderPayment {
        return mainDatabase.daoJobOrderPayment()
    }

    @Singleton
    @Provides
    fun provideDaoJobOrderQueues(mainDatabase: MainDatabase) : DaoJobOrderQueues {
        return mainDatabase.daoJobOrderQueues()
    }

    @Singleton
    @Provides
    fun provideRemoteDao(mainDatabase: MainDatabase) : DaoRemote {
        return mainDatabase.daoRemote()
    }

//    @Singleton
//    @Provides
//    fun appPreferenceRepository(@ApplicationContext context: Context) : AppPreferenceRepository {
//        return AppPreferenceRepository(context)
//    }
}