package com.csi.palabakosys.app.joborders.create.delicate

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.FragmentMenuDelicateBinding

class MenuDelicateFragment : Fragment(R.layout.fragment_menu_delicate), MenuProvider {
    private var searchBar: SearchView? = null
    private lateinit var binding: FragmentMenuDelicateBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMenuDelicateBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchBar = menu.findItem(R.id.menu_search)?.actionView as SearchView
        searchBar?.apply {
            maxWidth = Integer.MAX_VALUE
            queryHint = "Search Name or Category"
            val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
            setOnQueryTextFocusChangeListener { view, b ->
                if(b) {
                    toolbar.setBackgroundColor(context.getColor(R.color.white))
                } else {
                    toolbar.setBackgroundColor(context.getColor(R.color.teal_700))
                }
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}