package com.erapps.newws.ui.favs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erapps.newws.databinding.FragmentFavsBinding
import com.erapps.newws.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavsFragment : Fragment() {
    private var _binding: FragmentFavsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavsViewModel by viewModels()

    private lateinit var adapter: FavsListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
        }
        setUpRecycler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavsListAdapter()
        binding.recyclerFavs.adapter = adapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val article = adapter.snapshot().items[viewHolder.bindingAdapterPosition]
                viewModel.onArticleSwipe(article)
            }

        }).attachToRecyclerView(recyclerView)

        viewModel.getFavsArticles()
        launchAndRepeatWithViewLifecycle {
            observeUiState()
        }

    }

    private fun setUpRecycler(){
        recyclerView = binding.recyclerFavs
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun CoroutineScope.observeUiState(){
        launch {
            viewModel.uiState.collect {
                when(it){
                    FavsEvents.Loading -> viewModel.progressStatus.value = true
                    is FavsEvents.Success -> {
                        viewModel.progressStatus.value = false
                        adapter.submitData(it.pagingData)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}