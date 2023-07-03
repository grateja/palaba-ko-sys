package com.csi.palabakosys.app.packages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.packages.preview.PackagesPreviewActivity
import com.csi.palabakosys.databinding.ActivityPackagesBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.CrudActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PackagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPackagesBinding
    private val viewModel: PackagesViewModel by viewModels()
    private val adapter = PackagesAdapter()
    private val launcher = ActivityLauncher(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packages)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerPackages.adapter = adapter

        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeEvents() {
        adapter.onItemClick = {
            openAddEdit(it.prePackage.id)
        }

        binding.buttonCreateNew.setOnClickListener {
            openAddEdit(null)
        }
    }

    private fun subscribeListeners() {
        viewModel.packages.observe(this, Observer {
            adapter.setData(it)
        })
    }

    private fun openAddEdit(packageId: UUID?) {
        val intent = Intent(this, PackagesPreviewActivity::class.java).apply {
            putExtra(CrudActivity.ENTITY_ID, packageId.toString())
        }
        launcher.launch(intent)
    }
}