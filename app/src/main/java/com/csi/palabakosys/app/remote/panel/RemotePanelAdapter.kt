package com.csi.palabakosys.app.remote.panel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.csi.palabakosys.BR
import com.csi.palabakosys.R
import com.csi.palabakosys.app.remote.shared_ui.MachineStatus
import com.csi.palabakosys.room.entities.EntityMachine
import java.util.*

class RemotePanelAdapter : RecyclerView.Adapter<RemotePanelAdapter.ViewHolder>() {
    var list = emptyList<MachineTile>()
    var onItemClick: ((MachineTile) -> Unit) ? = null
    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MachineTile) {
            binding.setVariable(BR.viewModel, model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item_machine_tile,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = list[position]
        holder.bind(r)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(r)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<EntityMachine>) {
        this.list = list.map {
            MachineTile(it)
        }
        notifyDataSetChanged()
    }

    fun update(machine: EntityMachine, workInfo: WorkInfo?) {
        list.find {it.entityMachine.id == machine.id}?.apply {
            this.connecting = workInfo?.state?.isFinished != true
//            if(workInfo?.state?.isFinished == true) {
//                this.status.value = MachineStatus.CONNECTING
//            } else {
//                this.status.value = MachineStatus.IDLE
//            }
            notifyItemChanged(list.indexOf(this))
        }
    }

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