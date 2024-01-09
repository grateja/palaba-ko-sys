package com.csi.palabakosys.app.main_menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.databinding.ActivitySubMenuBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubMenuActivity : AppCompatActivity() {
    companion object {
        const val SUB_MENU_ITEMS_EXTRA = "subMenuItems"
        const val SUB_MENU_TITLE_EXTRA = "title"
    }
    private val adapter = Adapter<MenuItem>(R.layout.recycler_item_sub_menu)
    private lateinit var binding: ActivitySubMenuBinding

    private val mainViewModel: MainViewModel by viewModels()
    private val authLauncher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_menu)
        binding.lifecycleOwner = this
        binding.recyclerSubMenu.adapter = adapter

        intent.getParcelableArrayListExtra<MenuItem>(SUB_MENU_ITEMS_EXTRA)?.let {
            adapter.setData(it.toList())
        }
        intent.getStringExtra(SUB_MENU_TITLE_EXTRA)?.let {
            title = it
        }

        subscribeEvents()
    }

    private fun subscribeEvents() {
        mainViewModel.navigationState.observe(this, Observer {
            when(it) {
                is MainViewModel.NavigationState.OpenMenu -> {
                    openMenu(it.menuItem)
                    mainViewModel.resetState()
                }
                is MainViewModel.NavigationState.RequestAuthentication -> {
                    requestPermission(it.menuItem)
                    mainViewModel.resetState()
                }
            }
        })
        authLauncher.onOk = {
            when(it.data?.action) {
                AuthActionDialogActivity.AUTH_ACTION -> {
                    it.data?.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)?.let {
                        println("auth passed")
                        mainViewModel.permissionGranted(it)
                    }
                }
            }
        }
        adapter.onItemClick = {
            mainViewModel.openMenu(it)
        }
    }

    private fun requestPermission(menuItem: MenuItem) {
        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
            action = AuthActionDialogActivity.AUTH_ACTION
            menuItem.permissions?.let {
                putParcelableArrayListExtra(AuthActionDialogActivity.PERMISSIONS_EXTRA, ArrayList(it))
            }
//            menuItem.roles?.let {
//                putParcelableArrayListExtra(AuthActionDialogActivity.ROLES_EXTRA, ArrayList(it))
//            }
        }
        authLauncher.launch(intent)
    }

    private fun openMenu(menuItem: MenuItem) {
        val intent = if(menuItem.menuItems != null)
            Intent(this, SubMenuActivity::class.java).apply {
                putParcelableArrayListExtra(SubMenuActivity.SUB_MENU_ITEMS_EXTRA, ArrayList(menuItem.menuItems))
                putExtra(SubMenuActivity.SUB_MENU_TITLE_EXTRA, menuItem.text)
            }
        else
            Intent(this, menuItem.activityClass)

        startActivity(intent)
    }
}