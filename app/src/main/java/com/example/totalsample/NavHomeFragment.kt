package com.example.totalsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.totalsample.databinding.FragmentNavHomeBinding

class NavHomeFragment : Fragment() {

    private var _binding: FragmentNavHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNavHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnHome.setOnClickListener { findNavController().navigate(NavDetailRoute(id = "user_123")) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
