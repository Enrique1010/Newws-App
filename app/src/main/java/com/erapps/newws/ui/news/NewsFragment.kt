package com.erapps.newws.ui.news

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erapps.newws.R
import com.erapps.newws.databinding.FragmentNewsBinding
import com.erapps.newws.utils.launchAndRepeatWithViewLifecycle
import com.erapps.newws.utils.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
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
        _binding = FragmentNewsBinding.inflate(inflater, container, false).apply {
            viewModel = this@NewsFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setHasOptionsMenu(true)
        setupRV()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsListAdapter()
        recyclerView.adapter = adapter
        //loadStateListener()
        viewModel.getArticles()
        launchAndRepeatWithViewLifecycle {
            verifyInternet()
            observeViewModel()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search_button)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        }

        searchView.onQueryTextChanged {
            if (it.isNotEmpty()) {
                viewModel.searchText.value = it
                scrollToTop()
            }else {
                viewModel.searchText.value = "the"
                scrollToTop()
            }
        }
    }

//    private fun loadStateListener(){
//        adapter.addLoadStateListener { loadState ->
//            if (loadState.source.refresh is LoadState.NotLoading
//                && loadState.append.endOfPaginationReached){
//                viewModel.isEmpty.value = true
//                viewModel.emptyText.value = "No News to Show!"
//            }else{
//                viewModel.isEmpty.value = false
//                viewModel.emptyText.value = ""
//            }
//        }
//    }

    private fun CoroutineScope.observeViewModel(){
        launch {
            //delay(2000)
            viewModel.articleList.collectLatest { uiState ->
                when(uiState){
                    is NewsEvent.Loading -> viewModel.isLoading.value = true
                    is NewsEvent.Empty -> viewModel.isLoading.value = false
                    is NewsEvent.Success -> {
                        viewModel.isLoading.value = false
                        adapter.submitData(uiState.articles)
                    }
                }
            }
        }
    }

    /*private fun showProgress(visible: Boolean){
        if (visible){
            binding.progressBar3.visibility = View.VISIBLE
        }else{
            binding.progressBar3.visibility = View.INVISIBLE
        }
    }*/

    private fun scrollToTop(){
        recyclerView.layoutManager?.scrollToPosition(0)
    }

    private fun setupRV() {
        recyclerView = binding.recyclerNews
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun CoroutineScope.verifyInternet(){
        val snack = Snackbar.make(
            requireContext(),
            requireView(),
            "No internet connection",
            Snackbar.LENGTH_INDEFINITE
        )
        launch {
            viewModel.networkAvailable.collect {
                when(it){
                    true -> snack.dismiss()
                    false -> snack.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}