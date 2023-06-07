package com.csi.palabakosys.app.joborders.create.packages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.joborders.create.shared_ui.ModifyQuantityModalFragment
import com.csi.palabakosys.app.joborders.create.shared_ui.QuantityModel
import com.csi.palabakosys.databinding.ActivityJoSelectPackageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JOSelectPackageActivity : AppCompatActivity() {
    companion object {
        const val SERVICES = "services"
        const val PRODUCTS = "products"
        const val EXTRAS = "extras"
    }

    private lateinit var binding: ActivityJoSelectPackageBinding

    private val viewModel: AvailablePackageViewModel by viewModels()

    private lateinit var modifyQuantityDialog: ModifyQuantityModalFragment

    private val adapter = Adapter<MenuJobOrderPackage>(R.layout.recycler_item_available_package)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_jo_select_package)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerAvailablePackages.adapter = adapter

        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeEvents() {
        adapter.onItemClick = { itemClick(it) }
        binding.buttonOk.setOnClickListener {
            viewModel.prepareSubmit()
        }
        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun subscribeListeners() {
        viewModel.availablePackages.observe(this, Observer {
            adapter.setData(it)
//            viewModel.setPreselectedPackages(
//                intent.getParcelableArrayListExtra<MenuJobOrderPackage>("packages")?.toList()
//            )
        })
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is AvailablePackageViewModel.DataState.UpdatePackage -> {
                    adapter.updateItem(it.packageItem)
                    viewModel.resetState()
                }
                is AvailablePackageViewModel.DataState.Submit -> {
//                    println("services size")
//                    println(it.services?.size)
                    submit(it)
                    viewModel.resetState()
                }
            }
        })
    }

    private fun itemClick(item: MenuJobOrderPackage) {
        requestModifyQuantity(
            QuantityModel(item.packageRefId, item.packageName, item.quantity, QuantityModel.TYPE_PACKAGE)
        )
    }

    private fun requestModifyQuantity(quantityModel: QuantityModel) {
        modifyQuantityDialog = ModifyQuantityModalFragment.getInstance(quantityModel).apply {
            onOk = {
                viewModel.putPackage(it)
            }
            onItemRemove = {
                viewModel.removePackage(it)
            }
        }
        modifyQuantityDialog.show(supportFragmentManager, this.toString())
    }

    private fun submit(data: AvailablePackageViewModel.DataState.Submit) {
        setResult(RESULT_OK, Intent().apply {
            putParcelableArrayListExtra(SERVICES, data.services?.let { ArrayList(it) })
            putParcelableArrayListExtra(EXTRAS, data.extras?.let { ArrayList(it) })
            putParcelableArrayListExtra(PRODUCTS, data.products?.let { ArrayList(it) })
        })
        finish()
    }
}