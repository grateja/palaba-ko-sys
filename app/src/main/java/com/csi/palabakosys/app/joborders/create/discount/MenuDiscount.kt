package com.csi.palabakosys.app.joborders.create.discount

import android.os.Parcelable
import com.csi.palabakosys.model.DiscountApplicable
import com.csi.palabakosys.model.DiscountTypeEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuDiscount(
    val id: String,
    val name: String,
    val value: Float,
    val discountType: DiscountTypeEnum,
    val applicableTo: List<DiscountApplicable>,
    var selected: Boolean = false,
) : Parcelable {
    fun applicableToStr() : String {
        return "Applicable to: " + applicableTo.joinToString(",") {
            it.value
        }
    }

    private fun compute(amount: Float) : Float {
        return if(discountType == DiscountTypeEnum.FIXED) {
            if(amount - value < 0f) {
                return 0f
            } else {
                return value
            }
        } else {
            (value / 100) * amount
        }
    }

    fun getDiscount(amount: Float, applicable: DiscountApplicable) : Float {
        if(!applicableTo.any{ it == applicable || it == DiscountApplicable.TOTAL_AMOUNT}) return 0f

        return compute(amount)
    }
}