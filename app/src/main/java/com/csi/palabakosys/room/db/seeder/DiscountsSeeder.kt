package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.DiscountTypeEnum
import com.csi.palabakosys.model.EnumDiscountApplicable
//import com.csi.palabakosys.model.DiscountTypeEnum
import com.csi.palabakosys.room.dao.DaoDiscount
import com.csi.palabakosys.room.entities.EntityDiscount


class DiscountsSeeder(dao: DaoDiscount): EntitySeederImpl<EntityDiscount>(dao) {
    override fun items(): List<EntityDiscount> {
        return listOf(
            EntityDiscount("Birthday", 5f,  DiscountTypeEnum.PERCENTAGE,
                listOf(EnumDiscountApplicable.WASH_DRY_SERVICES)
            ),
            EntityDiscount("PWD", 10f, DiscountTypeEnum.PERCENTAGE,
                listOf(EnumDiscountApplicable.DELIVERY)
            ),
            EntityDiscount("Senior", 5f, DiscountTypeEnum.PERCENTAGE,
                listOf(EnumDiscountApplicable.TOTAL_AMOUNT)
            ),
            EntityDiscount("Opening", 10f, DiscountTypeEnum.FIXED,
                listOf(EnumDiscountApplicable.WASH_DRY_SERVICES, EnumDiscountApplicable.PRODUCTS_CHEMICALS)
            ),
        )
    }
}