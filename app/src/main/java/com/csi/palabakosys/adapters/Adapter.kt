package com.csi.palabakosys.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.BR
import android.animation.ObjectAnimator

import android.animation.AnimatorSet

open class Adapter<R>(private val layoutId: Int) : RecyclerView.Adapter<Adapter.ViewHolder<R>>() {
    protected var list: List<R> = emptyList()
    private var on_attach = true
    var DURATION: Long = 50
    //    private var selectedItem: R? = null
    var onItemClick: ((R) -> Unit) ? = null
    var onDataSetChanged: (() -> Unit) ? = null
    var allowSelection = false

    open fun add(item: R) {
        val k = list.toMutableList()
        k.add(item)
        list = k.toList()
        notifyItemInserted(itemCount - 1)
        onDataSetChanged?.invoke()
    }

    open fun removeItem(item: R) {
        val rItem = list.find {
            it == item
        }
        val position = list.indexOf(rItem)
        list = list.toMutableList().apply {
            remove(rItem)
        }
        notifyItemRemoved(position)
        onDataSetChanged?.invoke()
    }

    fun setData(items: List<R>) {
        this.list = items
        notifyDataSetChanged()
        onDataSetChanged?.invoke()
    }

    open fun getItems() : List<R> {
        return list
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<R> {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<R>, position: Int) {
        val r = list[position]
        holder.bind(r)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(r)
        }
        //setFadeAnimation(holder.itemView, position)
    }

    private fun setFadeAnimation(itemView: View, position: Int) {
//        val anim = AlphaAnimation(0.0f, 1.0f)
//        anim.duration = 300
//        itemView.startAnimation(anim)
        var i = position
        if (!on_attach) {
            i = -1
        }
        val isNotFirstItem = i == -1
        i++
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animator.startDelay = if (isNotFirstItem) DURATION / 2 else i * DURATION / 3
        animator.duration = 500
        animatorSet.play(animator)
        animator.start()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                on_attach = false;
            }
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder<T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: T) {
            binding.setVariable(BR.viewModel, model)
        }
    }
}