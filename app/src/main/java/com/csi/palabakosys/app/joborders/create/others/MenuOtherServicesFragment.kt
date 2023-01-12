package com.csi.palabakosys.app.joborders.create.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.FragmentMenuOtherServicesBinding

class MenuOtherServicesFragment : Fragment(R.layout.fragment_menu_other_services) {
    private lateinit var binding: FragmentMenuOtherServicesBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMenuOtherServicesBinding.bind(view)
    }
}