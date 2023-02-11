package com.csi.palabakosys.app.joborders.create.products

import android.os.Parcelable
import com.csi.palabakosys.model.ProductType
import com.csi.palabakosys.util.MeasureUnit
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class MenuProductItem(
    val id: UUID,
    val name: String,
    val price: Float,
    val measureUnit: MeasureUnit,
    val unitPerServe: Float,
    val productType: ProductType,
    var quantity: Int = 1,
) : Parcelable {
    @IgnoredOnParcel
    var selected = false
    fun serving() : String {

        return if(unitPerServe == 1f) { "" } else {
            "$unitPerServe"
        } + "$measureUnit"
    }

    fun quantityStr() : String {
        return "*$quantity " + serving()
    }
}

// ariel sachet
// P15.0 / 1 sachet
// quantity - 5 sachets in the cart

// ariel gallon
// 15.0 / 80 mL
// quantity - 5x 80 mL