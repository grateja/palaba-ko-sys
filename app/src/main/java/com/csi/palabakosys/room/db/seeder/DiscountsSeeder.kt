package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.EnumDiscountType
import com.csi.palabakosys.model.EnumDiscountApplicable
//import com.csi.palabakosys.model.DiscountTypeEnum
import com.csi.palabakosys.room.dao.DaoDiscount
import com.csi.palabakosys.room.entities.EntityDiscount


class DiscountsSeeder(dao: DaoDiscount): EntitySeederImpl<EntityDiscount>(dao) {
    override fun items(): List<EntityDiscount> {
        return listOf(
            EntityDiscount("Birthday", 5f,  EnumDiscountType.PERCENTAGE,
                listOf(EnumDiscountApplicable.WASH_DRY_SERVICES)
            ),
            EntityDiscount("PWD", 10f, EnumDiscountType.PERCENTAGE,
                listOf(EnumDiscountApplicable.DELIVERY)
            ),
            EntityDiscount("Senior", 5f, EnumDiscountType.PERCENTAGE,
                listOf(EnumDiscountApplicable.TOTAL_AMOUNT)
            ),
            EntityDiscount("Opening", 10f, EnumDiscountType.FIXED,
                listOf(EnumDiscountApplicable.WASH_DRY_SERVICES, EnumDiscountApplicable.PRODUCTS_CHEMICALS)
            ),
        )
    }
}