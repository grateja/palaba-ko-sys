package com.csi.palabakosys.app.joborders.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.databinding.FragmentJobOrderListBinding
import com.csi.palabakosys.viewmodels.ListViewModel
import java.util.*

class JobOrderListFragment : Fragment() {
    private lateinit var binding: FragmentJobOrderListBinding
    private val viewModel: JobOrderListViewModel by activityViewModels()
    private val adapter = Adapter<JobOrderListItem>(R.layout.recycler_item_job_order_list_item)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobOrderListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerJobOrderList.adapter = adapter
        subscribeEvents()
        subscribeListeners()

        return binding.root
    }

    private fun openJobOrder(jobOrderId: UUID) {
        val intent = Intent(context, JobOrderCreateActivity::class.java).apply {
            action = JobOrderCreateActivity.ACTION_LOAD_BY_JOB_ORDER_ID
            putExtra(JobOrderCreateActivity.JOB_ORDER_ID, jobOrderId.toString())
        }
        startActivity(intent)
    }

    private fun subscribeEvents() {
        adapter.onScrollAtTheBottom = {
            viewModel.loadMore()
        }
        adapter.onItemClick = {
            openJobOrder(it.id)
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ListViewModel.DataState.LoadItems -> {
                    if(it.reset) {
                        adapter.setData(it.items)
                    } else {
                        adapter.addItems(it.items)
                    }
                }
            }
        })

//        viewModel.sortDirection.observe(viewLifecycleOwner, Observer {
//            viewModel.filter(true)
//        })
//
//        viewModel.orderBy.observe(viewLifecycleOwner, Observer {
//            viewModel.filter(true)
//        })
//
//        viewModel.paymentStatus.observe(viewLifecycleOwner, Observer {
//            viewModel.filter(true)
//        })
    }
}