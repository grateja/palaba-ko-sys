package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService

@Dao
interface DaoJobOrderQueues {
    @Query("SELECT job_order_id, job_order_number, customer_name, SUM(jos.quantity - used) as available FROM job_orders jo JOIN job_order_services jos ON job_order_id = jo.id LEFT JOIN wash_services w ON w.id = service_id LEFT JOIN dry_services d ON d.id = service_id WHERE jos.machine_type = :machineType AND jos.quantity > used GROUP BY job_order_id")
    suspend fun getByMachineType(machineType: String?) : List<EntityCustomerQueueService>

    @Query("SELECT jo.id, SUM(jo.quantity - used) as available, service_name, IFNULL(w.minutes, d.minutes) as minutes, service_id, job_order_id, wash_type, jo.machine_type FROM job_order_services jo LEFT JOIN wash_services w ON w.id = jo.service_id LEFT JOIN dry_services d ON d.id = jo.service_id WHERE job_order_id = :jobOrderId AND jo.machine_type = :machineType AND (quantity - used) > 0  GROUP BY service_name, service_id, wash_type")
    suspend fun getAvailableWashes(jobOrderId: String?, machineType: String?) : List<EntityAvailableService>
}