package com.csi.palabakosys.model

enum class DiscountApplicable(val value: String, val id: Int) {
    TOTAL_AMOUNT("Total Amount", 1),
    WASH_DRY_SERVICES("Wash & Dry Services", 2),
    PRODUCTS_CHEMICALS("Products & Chemicals", 3),
    EXTRAS("Extras", 3),
    DELIVERY("Pickup/Delivery", 4);

    override fun toString() : String {
        return value
    }

    companion object {
        private fun fromId(id: Int?) : DiscountApplicable? {
            return values().find {
                it.id == id
            }
        }

        private fun fromIds(ids: List<Int>) : List<DiscountApplicable> {
            return ids.map {
                fromId(it)!!
            }
//            return values().filter { d ->
//                ids.any {d.id == it} ?: false
//            }
        }

        fun fromIds(ids: String?) : List<DiscountApplicable> {
            return ids?.split(",")?.map{ it.toInt() }?.let {
                fromIds(it)
            } ?: listOf()
        }

        fun toIds(discounts: List<DiscountApplicable>) : String {
            return discounts.joinToString(",") { it.id.toString() }
        }
    }
}