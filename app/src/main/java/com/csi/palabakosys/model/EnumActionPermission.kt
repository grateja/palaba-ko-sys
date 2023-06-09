package com.csi.palabakosys.model

enum class EnumActionPermission(val id: Int, val description: String) {
    ALL(0, "Full access"),
    BASIC(1, "All basic operations"),
    VIEW_DAILY_REPORTS(2, "View sales reports"),
    CANCEL_JOB_ORDERS(3, "Void or Cancel Job Orders"),
    MODIFY_JOB_ORDERS(4, "Remove or Update Items in the job orders"),
    MODIFY_INVENTORY(5, "Update stocks"),
    MODIFY_SERVICES(6, "Update services price"),
    VIEW_MACHINES(7, "View machine configurations");

    override fun toString() : String {
        return description
    }

    companion object {
        private fun fromId(id: Int?) : EnumActionPermission? {
            return values().find {
                it.id == id
            }
        }

        private fun fromIds(ids: List<Int>) : List<EnumActionPermission> {
            return ids.map {
                fromId(it)!!
            }
        }

        fun fromIds(ids: String?) : List<EnumActionPermission> {
            return ids?.split(",")?.map{ it.toInt() }?.let {
                fromIds(it)
            } ?: listOf()
        }

        fun toIds(permissions: List<EnumActionPermission>) : String {
            return permissions.joinToString(",") { it.id.toString() }
        }
    }
}