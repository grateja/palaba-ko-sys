package com.csi.palabakosys.app.discounts.edit

import com.csi.palabakosys.model.EnumDiscountApplicable

data class DiscountApplicableViewModel(
    val applicable: EnumDiscountApplicable,
    var selected: Boolean
)