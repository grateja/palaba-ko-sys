package com.csi.palabakosys.app.joborders.create.extras

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.google.android.material.card.MaterialCardView

class AvailableExtrasAdapter : RecyclerView.Adapter<AvailableExtrasAdapter.ViewHolder>() {
    private var list: List<MenuExtrasItem> = emptyList()
    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MenuExtrasItem) {
            binding.setVariable(BR.viewModel, model)
        }
    }

    var onItemClick: ((MenuExtrasItem) -> Unit) ? = null
//    var onDelete: ((MenuServiceItem) -> Unit) ? = null

    fun setData(services: List<MenuExtrasItem>) {
        list = services
        notifyItemRangeChanged(0, list.size - 1)
    }

    fun select(service: MenuExtrasItem) {
        list.let {
            it.find { s -> s.id == service.id }?.apply {
                selected = true
                notifyItemChanged(it.indexOf(this))
            }
        }
    }

    fun deselect(id: String) {
        list.let {
            it.find { s -> s.id == id }?.apply {
                selected = false
                quantity = 0
                notifyItemChanged(it.indexOf(this))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_available_extras,
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
//        holder.itemView.findViewById<MaterialCardView>(R.id.cardButtonDelete).setOnClickListener {
//            onDelete?.invoke(r)
//        }

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