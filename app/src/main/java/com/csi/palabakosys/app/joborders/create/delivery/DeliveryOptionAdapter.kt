package com.csi.palabakosys.app.joborders.create.delivery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.model.DeliveryOption
import com.google.android.material.card.MaterialCardView

class DeliveryOptionAdapter : RecyclerView.Adapter<DeliveryOptionAdapter.ViewHolder>() {
    private var list = DeliveryOption.values()
    private var selectedDeliveryOption: DeliveryOption? = null
    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DeliveryOption) {
            binding.setVariable(BR.viewModel, model)
        }
    }

    var onSelect: ((DeliveryOption) -> Unit) ? = null

//    fun setData(services: List<MenuDeliveryProfile>) {
//        list = services
//        notifyItemRangeChanged(0, list.size - 1)
//        println("List size")
//        println(list.size)
//    }

//    fun notifySelection(service: MenuDeliveryProfile?) {
//        deselectAll()
//        service?.let { _service ->
//            list.let {
//                it.find { s -> s.vehicle.id == _service.vehicle.id }?.apply {
//                    selected = true
//                    notifyItemChanged(it.indexOf(this))
//                }
//            }
//        }
//    }

    private fun deselectAll() {
        list.let {
            it.forEach { vehicle ->
                if(vehicle.selected) {
                    vehicle.selected = false
                    notifyItemChanged(it.indexOf(vehicle))
                }
            }
        }
    }

    fun selectDeliveryOption(deliveryOption: DeliveryOption?) {
        val preSelect = list.find { it.value == deliveryOption?.value && !it.selected }
        preSelect?.let { _preSelect ->
            list.forEach {
                if(it == _preSelect) {
                    it.selected = true
                    onSelect?.invoke(it)
                } else {
                    it.selected = false
                }
            }
//            notifyItemChanged(list.indexOf(_preSelect))
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_delivery_option,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = list[position]
        holder.bind(r)
        holder.itemView.setOnClickListener {
            selectDeliveryOption(r)
        }
        holder.itemView.apply {
            val selected = r.selected
            findViewById<MaterialCardView>(R.id.cardDeliveryOption).also {
                if(selected) {
                    it.strokeColor = context.getColor(R.color.card_selected)
                    it.setCardBackgroundColor(context.getColor(R.color.span_background_selected))
                } else {
                    it.strokeColor = context.getColor(R.color.border_primary)
                    it.setCardBackgroundColor(context.getColor(R.color.white))
                }
            }
//            println("selected" + r.value)
//            println(selected)
//            findViewById<TextView>(R.id.textTitle).setTextAppearance(
//                if(selected) {
//                    R.style.TextItemTitleActive
//                } else {
//                    R.style.TextItemTitle
//                }
//            )
//            println("selected" + r.value)
//            println(selected)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}