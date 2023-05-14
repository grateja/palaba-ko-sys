package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.EnumProductType
import com.csi.palabakosys.room.dao.DaoProduct
import com.csi.palabakosys.room.entities.EntityProduct
import com.csi.palabakosys.util.MeasureUnit


class ProductsSeeder(dao: DaoProduct): EntitySeederImpl<EntityProduct>(dao) {
    override fun items(): List<EntityProduct> {
        return listOf(
            EntityProduct("Ariel",15f, 100, MeasureUnit.SACHET, 1f, EnumProductType.DETERGENT),
            EntityProduct("Downy",15f, 100, MeasureUnit.SACHET, 1f, EnumProductType.FAB_CON),
            EntityProduct("Hanger",35f, 100, MeasureUnit.PCS, 1f, EnumProductType.OTHER),
            EntityProduct("Plastic",5f, 100, MeasureUnit.PCS, 1f, EnumProductType.DETERGENT),
        )
    }
}