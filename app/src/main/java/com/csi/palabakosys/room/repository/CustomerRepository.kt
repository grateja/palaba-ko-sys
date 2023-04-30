package com.csi.palabakosys.room.repository

import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.room.dao.DaoCustomer
import com.csi.palabakosys.room.entities.EntityCustomer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository
@Inject
constructor (
    private val daoCustomer: DaoCustomer,
) : BaseRepository<EntityCustomer>(daoCustomer) {
    override suspend fun get(id: String?) : EntityCustomer? {
        if(id == null) return null
        return daoCustomer.get(id)
    }

    suspend fun getAll(keyword: String) : List<EntityCustomer> {
        return daoCustomer.getAll(keyword)
    }

    suspend fun getNextJONumber() : String {
        val currentCRN = daoCustomer.getLastCRN()?.toInt() ?: 0
        return (currentCRN + 1).toString().padStart(6, '0')
    }

    suspend fun getCustomersMinimal(keyword: String?): List<CustomerMinimal> {
        return daoCustomer.getCustomersMinimal(keyword)
    }
}