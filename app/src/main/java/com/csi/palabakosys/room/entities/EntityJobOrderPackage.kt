//package com.csi.palabakosys.room.entities
//
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import java.util.UUID
//
//@Entity(tableName = "job_order_packages")
//data class EntityJobOrderPackage(
//    @ColumnInfo(name = "job_order_id")
//    val jobOrderId: UUID,
//
//    @ColumnInfo(name = "package_id")
//    val packageId: UUID,
//
//    @ColumnInfo(name = "quantity")
//    val quantity: Int,
//
//    @ColumnInfo(name = "amount_per_package")
//    val amountPerPackage: Float,
//
//    @ColumnInfo(name = "discount_in_peso")
//    val discountInPeso: Float,
//
//    @PrimaryKey(autoGenerate = false)
//    override var id: UUID,
//) : BaseEntity(id)