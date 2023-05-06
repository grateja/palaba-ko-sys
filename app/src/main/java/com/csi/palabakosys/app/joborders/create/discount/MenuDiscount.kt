package com.csi.palabakosys.app.joborders.create.discount

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.csi.palabakosys.model.DiscountApplicable
//import com.csi.palabakosys.model.DiscountTypeEnum
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuDiscount(
    val id: String,
    val name: String,
    val percentage: Float,

//    @ColumnInfo(name = "discount_type")
//    val discountType: DiscountTypeEnum,

    @ColumnInfo(name = "applicable_to")
    val applicableToIds: String,
) : Parcelable {
    @Ignore
    @IgnoredOnParcel
    var selected: Boolean = false

    private fun applicableTo() : List<DiscountApplicable> {
        return DiscountApplicable.fromIds(applicableToIds)
    }

    fun applicableToStr() : String {
        return "Applicable to: " + applicableTo().joinToString(",") {
            it.value
        }
    }

    private fun compute(amount: Float) : Float {
        return (percentage / 100) * amount
//        return if(discountType == DiscountTypeEnum.FIXED) {
//            if(amount - value < 0f) {
//                return 0f
//            } else {
//                return value
//            }
//        } else {
//            (value / 100) * amount
//        }
    }

    fun getDiscount(amount: Float, applicable: DiscountApplicable) : Float {
        if(!applicableTo().any{ it == applicable || it == DiscountApplicable.TOTAL_AMOUNT}) return 0f

        return compute(amount)
    }
}