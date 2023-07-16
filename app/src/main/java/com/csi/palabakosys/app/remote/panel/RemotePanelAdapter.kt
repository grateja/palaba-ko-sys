package com.csi.palabakosys.app.remote.panel

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.csi.palabakosys.app.machines.MachineListItem
import com.csi.palabakosys.databinding.RecyclerItemMachineTileBinding

class RemotePanelAdapter : RecyclerView.Adapter<RemotePanelAdapter.ViewHolder>() {
    var list = emptyList<MachineListItem>()
    var onItemClick: ((MachineListItem) -> Unit) ? = null
    class ViewHolder(private val binding: RecyclerItemMachineTileBinding) : RecyclerView.ViewHolder(binding.root) {
        var ended: (() -> Unit)? = null

        private var countDownTimer: CountDownTimer? = null

        fun end() {
            countDownTimer?.cancel()
        }

        fun bind(model: MachineListItem) {
            countDownTimer?.cancel()

            binding.setVariable(BR.viewModel, model)

            val context = binding.root.context

            if(model.machine.serviceActivationId != null) {
                binding.machineTile.strokeColor = context.getColor(R.color.purple_200)
                binding.machineTile.setCardBackgroundColor(context.getColor(R.color.darker_background))
            } else if(model.machine.activationRef?.running() == true) {
                binding.machineTile.strokeColor = context.getColor(R.color.card_selected)
                binding.machineTile.setCardBackgroundColor(context.getColor(R.color.span_background_selected))

                model.machine.activationRef?.delayInMillis() ?.let {
                    countDownTimer = object : CountDownTimer(it, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            println("Remaining time $millisUntilFinished -- ${model.machine.machineName()}")
                        }

                        override fun onFinish() {
                            println("finished")
                            ended?.invoke()
                        }
                    }
                    countDownTimer?.start()
                }
            } else {
                binding.machineTile.strokeColor = context.getColor(R.color.white)
                binding.machineTile.setCardBackgroundColor(context.getColor(R.color.white))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding: ViewDataBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(parent.context),
//            R.layout.recycler_item_machine_tile,
//            parent,
//            false
//        )
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemMachineTileBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = list[position]
        holder.bind(r)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(r)
        }
        holder.end()
        holder.ended = {
            notifyItemChanged(position)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.end()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<MachineListItem>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        for (i in 0 until recyclerView.childCount) {
            val childView = recyclerView.getChildAt(i)
            val viewHolder = recyclerView.getChildViewHolder(childView) as? ViewHolder
            viewHolder?.end()
        }
    }

//    fun setConnection(connecting: Boolean, machineId: UUID?, workerId: UUID?) {
//        list.find {it.entityMachine.id == machineId || it.entityMachine.serviceActivationId == workerId}?.apply {
//            this.connecting = connecting
//            notifyItemChanged(list.indexOf(this))
//        }
//    }

//    fun initiateConnection(workerId: UUID) {
//        list.find {it.entityMachine.workerId == workerId}?.apply {
//            this.connecting = true
//            notifyItemChanged(list.indexOf(this))
//        }
//    }
//
//    fun finishConnection(machineId: UUID) {
//        list.find {it.entityMachine.id == machineId}?.apply {
//            this.connecting = false
//            notifyItemChanged(list.indexOf(this))
//        }
//    }

//    fun updateStatus(workers: List<WorkInfo>) {
//        workers.forEach {
//            list.find { _machine -> _machine.entityMachine.workerId == it.id }?.apply {
//                if(!it.state.isFinished) {
//                    println("connecting")
//                    this.status.value = MachineStatus.CONNECTING
//                    notifyItemChanged(list.indexOf(this))
//                }
//            }
//        }
//    }
}