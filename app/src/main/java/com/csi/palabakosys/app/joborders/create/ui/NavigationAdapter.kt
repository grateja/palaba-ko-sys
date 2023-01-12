package com.csi.palabakosys.app.joborders.create.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.google.android.material.card.MaterialCardView

class NavigationAdapter : RecyclerView.Adapter<NavigationAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: NavigationModel) {
            binding.setVariable(BR.viewModel, model)
        }
    }

    private var list = listOf(
        NavigationModel("SERVICES", R.id.menuServicesFragment),
        NavigationModel("PRODUCTS", R.id.menuProductsFragment),
        NavigationModel("DELIVERY", R.id.menuDeliveryFragment),
        NavigationModel("DISCOUNT", R.layout.fragment_menu_products),
        NavigationModel("PAYMENT", R.layout.fragment_menu_products),
    )

    var onItemClick: ((NavigationModel) -> Unit) ? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_create_job_order_navigation,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val r = list[position]
        holder.bind(r)

        holder.itemView.setOnClickListener {
            list.apply {
                this.forEach {
                    if(it.selected) {
                        it.selected = false
                        notifyItemChanged(this.indexOf(it))
                    }
                }
                r.selected = true
                notifyItemChanged(this.indexOf(r))
            }
            onItemClick?.invoke(r)
        }
        holder.itemView.apply {
            findViewById<MaterialCardView>(R.id.cardNavigation).also {
                if(r.selected) {
                    it.strokeColor = context.getColor(R.color.card_selected)
                    it.strokeWidth = 1
                    it.setCardBackgroundColor(context.getColor(R.color.span_background_selected))
                } else {
                    it.strokeColor = context.getColor(R.color.border_primary)
                    it.strokeWidth = 0
                    it.setCardBackgroundColor(context.getColor(R.color.white))
                }
            }
            findViewById<TextView>(R.id.textLabel).setTextAppearance(
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