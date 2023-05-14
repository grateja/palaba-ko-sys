package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityMachineRemarks
import java.util.UUID

@Dao
abstract class DaoMachineRemarks : BaseDao<EntityMachineRemarks> {
    @Query("SELECT * FROM machine_remarks WHERE id = :id")
    abstract suspend fun get(id: UUID) : EntityMachineRemarks?

    @Query("SELECT * FROM machine_remarks WHERE remarks LIKE '%' || :keyword || '%' ORDER BY remarks")
    abstract suspend fun getAll(keyword: String) : List<EntityMachineRemarks>
}