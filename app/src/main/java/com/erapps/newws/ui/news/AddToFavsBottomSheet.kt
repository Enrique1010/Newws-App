package com.erapps.newws.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.erapps.newws.R
import com.erapps.newws.databinding.FragmentAddToFavsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddToFavsBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentAddToFavsBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddToFavsBSViewModel by viewModels()
    private val args: AddToFavsBottomSheetArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddToFavsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomsheetButton1.setOnClickListener {
            viewModel.insertArticleToFavs(args.article)
            Toast.makeText(requireContext(), "Added to Favs!!", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.bottomsheetButton2.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}