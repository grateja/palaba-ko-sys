package com.csi.palabakosys.app.joborders.create.products

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.google.android.material.card.MaterialCardView

class AvailableProductsAdapter : RecyclerView.Adapter<AvailableProductsAdapter.ViewHolder>() {
    private var list: List<MenuProductItem> = emptyList()
    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MenuProductItem) {
            binding.setVariable(BR.viewModel, model)
        }
    }

    var onItemClick: ((MenuProductItem) -> Unit) ? = null

    fun setData(services: List<MenuProductItem>) {
        list = services
        notifyItemRangeChanged(0, list.size - 1)
    }

    fun updateItem(product: MenuProductItem) {
        list.let {
            it.find { s -> s.id == product.id }?.apply {
                selected = product.selected
                quantity = product.quantity
                notifyItemChanged(it.indexOf(this))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_available_product,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = list[position]
        holder.bind(r)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(r)
        }
        holder.itemView.apply {
            findViewById<MaterialCardView>(R.id.jobOrderMenuItem).also {
                if(r.selected) {
                    it.strokeColor = context.getColor(R.color.card_selected)
                    it.setCardBackgroundColor(context.getColor(R.color.span_background_selected))
                } else {
                    it.strokeColor = context.getColor(R.color.border_primary)
                    it.setCardBackgroundColor(context.getColor(R.color.white))
                }
            }
            findViewById<TextView>(R.id.textTitle).setTextAppearance(
                if(r.selected) {
                    R.style.TextItemTitleActive
                } else {
                    R.style.TextItemTitle
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}