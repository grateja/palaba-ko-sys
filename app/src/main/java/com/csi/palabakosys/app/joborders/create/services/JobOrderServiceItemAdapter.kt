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
    var onDeleteRequest: ((MenuServiceItem) -> Unit) ? = null

    fun setData(services: MutableList<MenuServiceItem>) {
        list = services
        notifyItemRangeChanged(0, services.size -1)
    }
    fun addItem(service: MenuServiceItem) {
        list.let { _list ->
            _list.find { s -> s.id == service.id }?.let {
                notifyItemChanged(_list.indexOf(it))
            }
        }
    }
    fun removeItem(index: Int) {
        println("remove item size")
        println(list.size)
        notifyItemRemoved(index)
//        list.let { _list ->
//            println("find service")
//            _list.find { s -> s.id == service.id }?.let {
//                println("you wish")
//                notifyItemRemoved(_list.indexOf(it))
//            }
//        }
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
        holder.itemView.findViewById<ImageButton>(R.id.buttonDelete).setOnClickListener {
            onDeleteRequest?.invoke(r)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}