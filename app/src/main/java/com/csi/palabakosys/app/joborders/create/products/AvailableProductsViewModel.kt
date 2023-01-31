package com.csi.palabakosys.app.joborders.create.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.joborders.create.ui.QuantityModel
import com.csi.palabakosys.model.ProductType
import com.csi.palabakosys.util.MeasureUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableProductsViewModel

@Inject constructor()
: ViewModel() {
    val availableProducts = MutableLiveData<List<MenuProductItem>>()
    val dataState = MutableLiveData<DataState>()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            availableProducts.value = listOf(
                MenuProductItem("ariel", "Ariel", 15.0f, MeasureUnit.SACHET, 1f, ProductType.DETERGENT),
                MenuProductItem("breeze", "Breeze", 15.0f, MeasureUnit.SACHET, 1f, ProductType.DETERGENT),
                MenuProductItem("downy", "Downy", 15.0f, MeasureUnit.MILLILITER,  80f, ProductType.FAB_CON),
                MenuProductItem("del", "Del", 15.0f, MeasureUnit.MILLILITER,  80f, ProductType.FAB_CON),
                MenuProductItem("plastic-s", "Plastic (S)", 3.0f, MeasureUnit.PCS,  1f, ProductType.OTHER),
                MenuProductItem("plastic-m", "Plastic (M)", 5.0f, MeasureUnit.PCS,  1f, ProductType.OTHER),
                MenuProductItem("plastic-l", "Plastic (L)", 7.0f, MeasureUnit.PCS,  1f, ProductType.OTHER),
                MenuProductItem("hanger", "Hanger", 25.0f, MeasureUnit.PCS, 1f, ProductType.OTHER),
            )
        }
    }

    fun setPreselectedProducts(products: List<MenuProductItem>?) {
        products?.forEach { mpi ->
            availableProducts.value?.find { mpi.id == it.id }?.apply {
                this.selected = true
                this.quantity = mpi.quantity
            }
        }
    }

    fun putProduct(quantityModel: QuantityModel) {
        val product = availableProducts.value?.find { it.id == quantityModel.id }?.apply {
            selected = true
            quantity = quantityModel.quantity
        }
        dataState.value = DataState.UpdateProduct(product!!)
    }

    fun removeProduct(quantityModel: QuantityModel) {
        val item = availableProducts.value?.find { it.id == quantityModel.id }?.apply {
            this.selected = false
            this.quantity = 0
        }
        dataState.value = DataState.UpdateProduct(item!!)
    }

    fun prepareSubmit() {
        val list = availableProducts.value?.filter { it.selected }
        list?.let {
            dataState.value = DataState.Submit(it)
        }
    }

    fun resetState() {
        dataState.value = DataState.StateLess
    }

    sealed class DataState {
        object StateLess: DataState()
        data class UpdateProduct(val productItem: MenuProductItem) : DataState()
        data class Submit(val productItems: List<MenuProductItem>) : DataState()
    }
}