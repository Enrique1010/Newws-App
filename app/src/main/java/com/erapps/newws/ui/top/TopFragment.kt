package com.erapps.newws.ui.top

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erapps.newws.R
import com.erapps.newws.databinding.FragmentTopBinding
import com.erapps.newws.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TopFragment : Fragment() {
    private var _binding: FragmentTopBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TopListViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TopListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTopBinding.inflate(inflater, container, false)
        setUpView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TopListAdapter()
        recyclerView.adapter = adapter

        viewModel.getTopNews()
        launchAndRepeatWithViewLifecycle {
            observeViewModel()
        }
    }

    private fun CoroutineScope.observeViewModel() {
        launch {
            viewModel.topHeadLines.collectLatest {
                when(it){
                    is TopListEvent.Empty -> adapter.submitData(PagingData.empty())
                    is TopListEvent.Success -> adapter.submitData(it.topArticles)
                }
            }
        }
    }

    private fun setUpView(){
        recyclerView = binding.recyclerTop
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext() ,LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}