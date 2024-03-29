package com.csi.palabakosys.room.repository

import androidx.lifecycle.LiveData
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.customers.list.CustomerListItem
import com.csi.palabakosys.app.customers.list.CustomerQueryResult
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.room.dao.DaoCustomer
import com.csi.palabakosys.room.entities.EntityCustomer
import com.csi.palabakosys.util.EnumSortDirection
import com.csi.palabakosys.util.QueryResult
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository
@Inject
constructor (
    private val daoCustomer: DaoCustomer,
) : BaseRepository<EntityCustomer>(daoCustomer) {
    override suspend fun get(id: UUID?) : EntityCustomer? {
        if(id == null) return null
        return daoCustomer.get(id)
    }

//    suspend fun filter(keyword: String) : List<EntityCustomer> {
//        return daoCustomer.getAll(keyword)
//    }

    suspend fun getNextJONumber() : String {
        val currentCRN = daoCustomer.getLastCRN()?.toInt() ?: 0
        return (currentCRN + 1).toString().padStart(6, '0')
    }

    suspend fun checkName(name: String?) : Boolean {
        return daoCustomer.checkName(name)
    }

    suspend fun getCustomersMinimal(keyword: String?, page: Int, customerId: UUID?): List<CustomerMinimal> {
        val offset = (20 * page) - 20
        return daoCustomer.getCustomersMinimal(keyword, 20, offset, customerId)
    }

    suspend fun getListItems(keyword: String?, orderBy: String?, sortDirection: EnumSortDirection?, page: Int, hideAllWithoutJO: Boolean, dateFilter: DateFilter?): CustomerQueryResult {
        val offset = (20 * page) - 20
        return daoCustomer.getListItem(keyword, orderBy, sortDirection.toString(), offset, hideAllWithoutJO, dateFilter?.dateFrom, dateFilter?.dateTo)
    }

    suspend fun getCustomerMinimalByCRN(crn: String?): EntityCustomer? {
        return daoCustomer.getCustomerMinimalByCRN(crn)
    }

    fun getDashboard(dateFilter: DateFilter) = daoCustomer.getDashboardCustomer(dateFilter.dateFrom, dateFilter.dateTo)

    fun getCustomerAsLiveData(customerId: UUID?) = daoCustomer.getCustomerAsLiveData(customerId)

    fun canCreateJobOrder(customerId: UUID, limit: Int) = daoCustomer.canCreateJobOrder(customerId, limit)
}