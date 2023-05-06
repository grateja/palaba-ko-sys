package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.DiscountApplicable
//import com.csi.palabakosys.model.DiscountTypeEnum
import com.csi.palabakosys.room.dao.DaoDiscount
import com.csi.palabakosys.room.entities.EntityDiscount


class DiscountsSeeder(dao: DaoDiscount): EntitySeederImpl<EntityDiscount>(dao) {
    override fun items(): List<EntityDiscount> {
        return listOf(
            EntityDiscount("Birthday", 5f,  /*DiscountTypeEnum.PERCENTAGE,*/
                DiscountApplicable.toIds(listOf(DiscountApplicable.WASH_DRY_SERVICES))
            ),
            EntityDiscount("PWD", 10f, /*DiscountTypeEnum.PERCENTAGE,*/
                DiscountApplicable.toIds(listOf(DiscountApplicable.DELIVERY))
            ),
            EntityDiscount("Senior", 5f, /*DiscountTypeEnum.PERCENTAGE,*/
                DiscountApplicable.toIds(listOf(DiscountApplicable.TOTAL_AMOUNT))
            ),
            EntityDiscount("Opening", 10f, /*DiscountTypeEnum.FIXED,*/
                DiscountApplicable.toIds(listOf(DiscountApplicable.WASH_DRY_SERVICES, DiscountApplicable.PRODUCTS_CHEMICALS))
            ),
        )
    }
}