package com.csi.palabakosys.app.joborders.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.shared_ui.BottomSheetDateRangePickerFragment
import com.csi.palabakosys.databinding.FragmentJobOrderListAdvancedFilterBinding
import com.csi.palabakosys.fragments.ModalFragment
import com.csi.palabakosys.model.JobOrderAdvancedFilter
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.viewmodels.AdvancedFilterViewModel

class JobOrderListAdvancedFilterFragment : ModalFragment<JobOrderAdvancedFilter>() {
    private lateinit var binding: FragmentJobOrderListAdvancedFilterBinding
    private val viewModel: JobOrderListAdvancedFilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobOrderListAdvancedFilterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        closeOnTouchOutside = true

        subscribeEvents()
        subscribeListeners()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<JobOrderAdvancedFilter>(PAYLOAD).let {
            viewModel.setInitialFilters(it)
            println("date range now")
            println(it)
        }
    }

    private fun showDatePicker(dateFilter: DateFilter?) {
        val dateRangeDialog = BottomSheetDateRangePickerFragment.getInstance(dateFilter)
        dateRangeDialog.show(parentFragmentManager, null)
        dateRangeDialog.onOk = {
            viewModel.setDateRange(it)
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is DataState.Submit -> {
                    onOk?.invoke(it.data as JobOrderAdvancedFilter)
                    dismiss()
                    viewModel.clearState()
                }
            }
        })
        viewModel.navigationState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is AdvancedFilterViewModel.NavigationState.ShowDateRangePicker -> {
                    showDatePicker(it.dateFilter)
                    viewModel.clearNavigation()
                }
            }
        })
    }

    private fun subscribeEvents() {
        binding.buttonClose.setOnClickListener {
            dismiss()
        }
        binding.buttonApply.setOnClickListener {
            viewModel.submit()
        }
        binding.buttonSelectDateRange.setOnClickListener {
            viewModel.showDateFilter()
        }
        binding.buttonClearDateFilter.setOnClickListener {
            viewModel.setDateRange(null)
        }
    }

    companion object {
        private var instance: JobOrderListAdvancedFilterFragment? = null
        fun getInstance(model: JobOrderAdvancedFilter): JobOrderListAdvancedFilterFragment {
            if(instance == null) {
                instance = JobOrderListAdvancedFilterFragment()
            }
            instance?.arguments = Bundle().apply {
                putParcelable(PAYLOAD, model)
            }
            return instance as JobOrderListAdvancedFilterFragment
        }
    }
}