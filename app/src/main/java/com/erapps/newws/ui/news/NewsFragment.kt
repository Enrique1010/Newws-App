package com.erapps.newws.ui.news

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erapps.newws.R
import com.erapps.newws.databinding.FragmentNewsBinding
import com.erapps.newws.utils.launchAndRepeatWithViewLifecycle
import com.erapps.newws.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsListAdapter

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

        viewModel.getArticles()
        launchAndRepeatWithViewLifecycle {
            observeViewModel()
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
            viewModel.articleList.collectLatest {
                when(it){
                    is NewsEvent.Empty -> {
                        adapter.submitData(PagingData.empty())
                    }
                    is NewsEvent.Success -> adapter.submitData(it.articles)
                }
            }
        }
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