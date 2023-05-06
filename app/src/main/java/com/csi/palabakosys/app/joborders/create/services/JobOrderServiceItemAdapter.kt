package com.csi.palabakosys.app.joborders.create.services

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.BR
import com.csi.palabakosys.R

class JobOrderServiceItemAdapter: RecyclerView.Adapter<JobOrderServiceItemAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MenuServiceItem) {
            binding.setVariable(BR.viewModel, model)
        }
    }

    private var list: MutableList<MenuServiceItem> = mutableListOf()

    var onItemClick: ((MenuServiceItem) -> Unit) ? = null
//    var onDeleteRequest: ((MenuServiceItem) -> Unit) ? = null

    fun setData(services: MutableList<MenuServiceItem>) {
        list = services.filter { it.deletedAt == null }.toMutableList()
        notifyDataSetChanged()
//        notifyItemRangeChanged(0, services.size -1)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_create_job_order_service,
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

    //        holder.itemView.findViewById<ImageButton>(R.id.buttonDelete).setOnClickListener {
//            onDeleteRequest?.invoke(r)
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}