package com.csi.palabakosys.app.joborders.create.customer

import android.view.ViewGroup
import android.widget.Button
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.google.android.material.card.MaterialCardView
import java.util.UUID

class CustomersAdapterMinimal : Adapter<CustomerMinimal>(R.layout.recycler_item_customer_minimal) {
    var onEdit: ((CustomerMinimal) -> Unit) ? = null
    fun setSelectedCustomerId(customerId: UUID) {
        list.find {
            it.id == customerId
        }?.let {
            it.selected = true
            notifyItemChanged(list.indexOf(it))
        }
    }
    override fun onBindViewHolder(holder: ViewHolder<CustomerMinimal>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItems()[position]
        holder.itemView.findViewById<MaterialCardView>(R.id.buttonEdit)?.setOnClickListener {
            onEdit?.invoke(item)
        }
    }
}