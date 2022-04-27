package com.erapps.newws.ui.favs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erapps.newws.R
import com.erapps.newws.databinding.FragmentFavsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavsFragment : Fragment() {
    private var _binding: FragmentFavsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavsBinding.inflate(inflater, container, false)
        return binding.root
    }
}