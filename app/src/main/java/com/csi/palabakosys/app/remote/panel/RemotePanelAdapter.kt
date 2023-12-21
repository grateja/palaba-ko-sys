package com.csi.palabakosys.app.remote.panel

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.csi.palabakosys.app.machines.MachineListItem
import com.csi.palabakosys.databinding.RecyclerItemMachineTileBinding
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

class RemotePanelAdapter : RecyclerView.Adapter<RemotePanelAdapter.ViewHolder>() {
    var handler = Handler(Looper.getMainLooper())
    var list = emptyList<MachineListItem>()
    var onItemClick: ((MachineListItem) -> Unit) ? = null
    var onOptionClick: ((MachineListItem) -> Unit) ? = null
//    private val itemRunnables = mutableMapOf<MachineListItem, Runnable>()
    class ViewHolder(private val binding: RecyclerItemMachineTileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MachineListItem) {
            binding.setVariable(BR.viewModel, model)

            val context = binding.root.context

            if(model.machine.serviceActivationId != null) {
                binding.machineTile.strokeColor = context.getColor(R.color.purple_200)
                binding.machineTile.setCardBackgroundColor(context.getColor(R.color.darker_background))
            } else if(model.machine.activationRef?.running() == true) {
                binding.machineTile.strokeColor = context.getColor(R.color.card_selected)
                binding.machineTile.setCardBackgroundColor(context.getColor(R.color.span_background_selected))
            } else {
                binding.machineTile.strokeColor = context.getColor(R.color.white)
                binding.machineTile.setCardBackgroundColor(context.getColor(R.color.white))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemMachineTileBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    private fun startTimer(item: MachineListItem) {
//        itemRunnables[item]?.let { Handler(Looper.getMainLooper()).removeCallbacks(it) }
//
//        val remainingSeconds = item.machine.activationRef?.remainingSeconds() ?: 0
//        val remainder = remainingSeconds % 60
//        if (remainder > 0) {
//            val runnable = Runnable {
//                notifyItemChanged(list.indexOf(item))
//                println("it should refresh now")
//                startTimer(item)
//            }
//
//            itemRunnables[item] = runnable
//
//            Handler(Looper.getMainLooper()).postDelayed(runnable, (remainder * 1000) + 1000)
//            println("remainder $remainder")
//        }
        val remainingSeconds = item.machine.activationRef?.remainingSeconds() ?: 0
        val remainder = remainingSeconds % 60
        if(remainder > 0) {
            handler.postDelayed({
                notifyItemChanged(list.indexOf(item))
                println("it should refresh now")
                startTimer(item)
            }, (remainder * 1000) + 1000)
            println("remainder $remainder")
        }
    }

    fun stopUpdatingTime() {
        // Remove all pending Runnables when the RecyclerView is being destroyed
        handler.removeCallbacksAndMessages(null)
        println("runnables removed")
    }

    fun startUpdatingTime() {
        list.forEach {
            startTimer(it)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        stopUpdatingTime()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = list[position]
        holder.bind(r)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(r)
        }
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            onOptionClick?.invoke(r) != null
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<MachineListItem>) {
        this.list = list
        notifyDataSetChanged()
    }
}