package com.csi.palabakosys.app.joborders.create.products

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.BR
import com.csi.palabakosys.R

class JobOrderProductsItemAdapter: RecyclerView.Adapter<JobOrderProductsItemAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MenuProductItem) {
            binding.setVariable(BR.viewModel, model)
        }
    }

    private var list: MutableList<MenuProductItem> = mutableListOf()
//    private fun filtered(): List<MenuProductItem> {
//        return list.filter {it.deletedAt == null}
//    }

    var onItemClick: ((MenuProductItem) -> Unit) ? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_create_job_order_product,
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
            onItemClick?.invoke(r)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(jobOrderProducts: MutableList<MenuProductItem>) {
        list = jobOrderProducts.filter { it.deletedAt == null }.toMutableList()
//        notifyItemRangeChanged(0, jobOrderProducts.size - 1)
        notifyDataSetChanged()
    }
}