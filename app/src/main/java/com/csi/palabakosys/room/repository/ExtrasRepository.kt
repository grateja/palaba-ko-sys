package com.csi.palabakosys.room.repository

import androidx.lifecycle.LiveData
import com.csi.palabakosys.app.extras.ExtrasItemFull
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.room.dao.DaoExtras
//import com.csi.palabakosys.room.dao.DaoOtherService
import com.csi.palabakosys.room.entities.EntityExtras
import java.util.UUID
//import com.csi.palabakosys.room.entities.EntityServiceOther
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExtrasRepository
@Inject
constructor (
    private val daoExtras: DaoExtras,
) : BaseRepository<EntityExtras>(daoExtras) {
    override suspend fun get(id: UUID?) : EntityExtras? {
        if(id == null) return null
        return daoExtras.get(id)
    }

    suspend fun getAll(keyword: String) : List<MenuExtrasItem> {
        return daoExtras.getAll(keyword)
    }

    suspend fun filter(keyword: String) : List<ExtrasItemFull> {
        return daoExtras.filter(keyword)
    }

    fun getCategories(): LiveData<List<String>> = daoExtras.getCategories()
}