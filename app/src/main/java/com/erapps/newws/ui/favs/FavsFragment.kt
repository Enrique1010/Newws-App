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
import com.erapps.newws.data.models.Article
import com.erapps.newws.databinding.FragmentFavsBinding
import com.erapps.newws.utils.launchAndRepeatWithViewLifecycle
import com.google.android.material.snackbar.Snackbar
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
    private var position: Int = 0

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
                position = viewHolder.bindingAdapterPosition
                val article = adapter.snapshot().items[position]
                adapter.notifyItemRemoved(position)
                viewModel.onArticleSwipe(article)
            }

        }).attachToRecyclerView(recyclerView)

        viewModel.getFavsArticles()
        launchAndRepeatWithViewLifecycle {
            observeUiState()
            observeSwipeText()
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
                    else -> {}
                }
            }
        }
    }

    private fun CoroutineScope.observeSwipeText(){
        launch {
            viewModel.showText.collect {
                when(it){
                    is FavsEvents.ShowDeleteMessage -> {
                        showSnackBar(it.article)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showSnackBar(article: Article){
        Snackbar.make(requireView(), "Article removed from favorites!!", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                viewModel.onUndoArticleSwipe(article)
                adapter.notifyItemInserted(position)
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}