package com.csi.palabakosys.model

import com.csi.palabakosys.room.entities.EntityCustomer
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine

class MachineRemoteModel {
    var customer: EntityCustomer? = null
    var machine: EntityMachine? = null
    var jobOrderService: EntityJobOrderService? = null
}