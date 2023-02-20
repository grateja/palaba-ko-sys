package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService

@Dao
interface DaoJobOrderQueues {
    @Query("SELECT job_order_id, job_order_number, customer_id, jos.svc_machine_type as machine_type, customer_name FROM job_orders jo JOIN job_order_services jos ON job_order_id = jo.id LEFT JOIN services w ON w.id = service_id WHERE jos.svc_machine_type = :machineType AND jos.quantity > used GROUP BY customer_name, job_order_number, job_order_id")
    suspend fun getByMachineType(machineType: String?) : List<EntityCustomerQueueService>

    @Query("SELECT jo.id, SUM(jo.quantity - used) as available, service_name, svc_minutes as minutes, service_id, job_order_id, svc_wash_type, svc_machine_type FROM job_order_services jo JOIN job_orders on job_orders.id = job_order_id WHERE customer_id = :customerId AND jo.svc_machine_type = :machineType AND (quantity - used) > 0  GROUP BY service_name")
    suspend fun getAvailableWashes(customerId: String?, machineType: String?) : List<EntityAvailableService>
}