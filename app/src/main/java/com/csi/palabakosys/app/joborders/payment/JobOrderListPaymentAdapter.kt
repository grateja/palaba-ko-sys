package com.csi.palabakosys.app.joborders.payment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.databinding.RecyclerItemJobOrderMinimalBinding

class JobOrderListPaymentAdapter : RecyclerView.Adapter<JobOrderListPaymentAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: RecyclerItemJobOrderMinimalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(jobOrder: JobOrderPaymentMinimal) {
            binding.viewModel = jobOrder
            binding.checkboxSelected.setOnClickListener {
                onSelectionChange?.invoke(jobOrder)
            }
            binding.card.setOnClickListener {
                onItemClick?.invoke(jobOrder)
            }
        }
    }

    var onSelectionChange: ((JobOrderPaymentMinimal) -> Unit) ? = null
    var onItemClick: ((JobOrderPaymentMinimal) -> Unit) ? = null

    private var list: MutableList<JobOrderPaymentMinimal> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: List<JobOrderPaymentMinimal>) {
        list = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemJobOrderMinimalBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun selectedItems() : List<JobOrderPaymentMinimal> {
        return list.filter { it.selected }
    }
}