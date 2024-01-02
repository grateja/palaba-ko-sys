package com.csi.palabakosys.app.joborders.print

import android.view.LayoutInflater
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.model.PrinterItem
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.RecyclerItemPrintItemBinding
import com.csi.palabakosys.util.spToPx

class PrinterAdapter : RecyclerView.Adapter<PrinterAdapter.ViewHolder>() {
    private var list: List<PrinterItem> = emptyList()
    inner class ViewHolder(private val binding: RecyclerItemPrintItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: PrinterItem) {
            binding.setVariable(BR.viewModel, model)
            when(model::class.java) {
                PrinterItem.HeaderDoubleCenter::class.java -> {
                    binding.text.setTextAppearance(R.style.Print_Double)
                    binding.text.textAlignment = TEXT_ALIGNMENT_CENTER
                }
                PrinterItem.TextCenter::class.java, PrinterItem.TextCenterTall::class.java -> {
                    binding.text.textAlignment = TEXT_ALIGNMENT_CENTER
                }
                PrinterItem.Header::class.java -> {
                    binding.text.setTextAppearance(R.style.Print_Header)
                }
                PrinterItem.ListItemBold::class.java, PrinterItem.DefinitionTermBold::class.java, PrinterItem.Header::class.java -> {
                    binding.text.setTextAppearance(R.style.Print_Header)
                }
                else -> {
                    binding.text.setTextAppearance(R.style.Print)
                }
            }
        }
    }

    fun setData(items: List<PrinterItem>) {
        list = items
        notifyDataSetChanged()
//        notifyItemRangeInserted(0, list.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerItemPrintItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_print_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = list[position]
        holder.bind(r)
    }
    override fun getItemCount(): Int {
        return list.size
    }
}