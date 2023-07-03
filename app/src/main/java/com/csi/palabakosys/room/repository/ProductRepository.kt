package com.csi.palabakosys.room.repository

import androidx.lifecycle.LiveData
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.products.ProductItemFull
import com.csi.palabakosys.room.dao.DaoProduct
import com.csi.palabakosys.room.entities.EntityProduct
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository
@Inject
constructor (
    private val daoProduct: DaoProduct,
) : BaseRepository<EntityProduct> (daoProduct) {
    override suspend fun get(id: UUID?) : EntityProduct? {
        if(id == null) return null

        return daoProduct.get(id)
    }

    suspend fun getAll() : List<EntityProduct> {
        return daoProduct.getAll()
    }

    suspend fun menuItems() : List<MenuProductItem> {
        return daoProduct.menuItems()
    }

    suspend fun filter(keyword: String) : List<ProductItemFull> {
        return daoProduct.filter(keyword)
    }

    suspend fun checkAll(products: List<MenuProductItem>) : String? {
        return daoProduct.checkAll(products)
    }
}