package com.csi.palabakosys.app.joborders.create.discount

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.csi.palabakosys.model.EnumDiscountType
import com.csi.palabakosys.model.EnumDiscountApplicable
//import com.csi.palabakosys.model.DiscountTypeEnum
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.util.*

@Parcelize
data class MenuDiscount(
    @ColumnInfo(name = "id")
    val discountRefId: UUID?,

    val name: String,

    val value: Float,

    @ColumnInfo(name = "applicable_to")
    val applicableToIds: String,

    @ColumnInfo(name = "discount_type")
    val discountType: EnumDiscountType,

    @ColumnInfo(name = "deleted_at")
    var deletedAt: Instant?,
) : Parcelable {
    @Ignore
    @IgnoredOnParcel
    var selected: Boolean = false

    private fun applicableTo() : List<EnumDiscountApplicable> {
        return EnumDiscountApplicable.fromIds(applicableToIds)
    }

    fun applicableToStr() : String {
        return "Applicable to: " + applicableTo().joinToString(",") {
            it.value
        }
    }

    private fun compute(amount: Float) : Float {
        return (value / 100) * amount
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

    fun getDiscount(amount: Float, applicable: EnumDiscountApplicable) : Float {
        if(!applicableTo().any{ it == applicable || it == EnumDiscountApplicable.TOTAL_AMOUNT}) return 0f

        return compute(amount)
    }
}