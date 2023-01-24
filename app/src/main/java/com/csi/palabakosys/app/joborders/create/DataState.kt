//package com.csi.palabakosys.app.joborders.create
//
//import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
//import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
//import com.csi.palabakosys.app.joborders.create.ui.QuantityModel
//
//sealed class DataState {
//    object StateLess : DataState()
//    data class PutService(val serviceItem: MenuServiceItem) : DataState()
//    data class PutProduct(val product: MenuProductItem) : DataState()
//    data class RequestEditServiceQuantity(val serviceItem: QuantityModel) : DataState()
//    data class RequestEditProductQuantity(val serviceItem: QuantityModel) : DataState()
//    data class RemoveService(val index: Int, val id: String) : DataState()
//    data class RemoveProduct(val index: Int, val id: String) : DataState()
//}