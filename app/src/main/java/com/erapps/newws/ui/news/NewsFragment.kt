package com.erapps.newws.ui.news

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erapps.newws.R
import com.erapps.newws.data.models.Article
import com.erapps.newws.databinding.FragmentNewsBinding
import com.erapps.newws.utils.launchAndRepeatWithViewLifecycle
import com.erapps.newws.utils.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsListAdapter
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupRV()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsListAdapter()
        recyclerView.adapter = adapter

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

        viewModel.getArticles()
        launchAndRepeatWithViewLifecycle {
            observeViewModel()
            observeSwipeText()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.search_button)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.queryHint = "Search News Here"
        searchView.onQueryTextChanged {
            if (it.isEmpty() || it.isBlank()) viewModel.searchText.value = "all"
            viewModel.searchText.value = it
        }
    }

    private fun CoroutineScope.observeViewModel(){
        launch {
            viewModel.articleList.collectLatest { uiState ->
                when(uiState){
                    is NewsEvent.Empty -> adapter.submitData(PagingData.empty())
                    is NewsEvent.Success -> adapter.submitData(uiState.articles)
                }
            }
        }
    }

    private fun CoroutineScope.observeSwipeText(){
        launch {
            viewModel.insertEvent.collect {
                when(it){
                    is NewsEvent.ShowInsertMessage -> {
                        showSnackBar("Added to Favorites!!", "Undo", it.article)
                    }
                }
            }
        }
    }

    private fun showSnackBar(text: String, actionText: String, article: Article){
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
            .setAction(actionText) {
                viewModel.onUndoAddedToFavs(article)
                adapter.notifyItemInserted(position)
            }.show()
    }

    private fun setupRV() {
        recyclerView = binding.recyclerNews
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}