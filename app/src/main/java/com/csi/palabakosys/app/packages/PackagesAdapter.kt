package com.csi.palabakosys.app.packages

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.RecyclerItemPackageWithItemsBinding
import com.csi.palabakosys.room.entities.EntityPackageWithItems

class PackagesAdapter : RecyclerView.Adapter<PackagesAdapter.ViewHolder>() {
    private var list: MutableList<EntityPackageWithItems> = mutableListOf()

    var onItemClick: ((EntityPackageWithItems) -> Unit) ? = null

    inner class ViewHolder(private val binding: RecyclerItemPackageWithItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        val adapter = Adapter<PackageItem>(R.layout.recycler_item_package_item)
        fun bind(model: EntityPackageWithItems) {
            binding.viewModel = model
            binding.recyclerViewItems.adapter = adapter
            binding.buttonEdit.setOnClickListener {
                onItemClick?.invoke(model)
            }
            adapter.setData(model.simpleList())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemPackageWithItemsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
//        holder.itemView.setOnClickListener {
//            onItemClick?.invoke(item)
//        }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<EntityPackageWithItems>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }
}