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

    var onItemClick: ((MenuProductItem) -> Unit) ? = null
    var onDeleteRequest: ((MenuProductItem) -> Unit) ? = null

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
        holder.itemView.findViewById<ImageButton>(R.id.buttonDelete).setOnClickListener {
            onDeleteRequest?.invoke(r)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(jobOrderProducts: MutableList<MenuProductItem>) {
        list = jobOrderProducts
    }

    fun addItem(service: MenuProductItem) {
        list.let { _list ->
            _list.find { s -> s.id == service.id }?.let {
                notifyItemChanged(_list.indexOf(it))
            }
        }
    }

    fun removeItem(index: Int) {
        notifyItemRemoved(index)
    }
}