package com.erapps.newws.ui.top

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        setUpChipGroup()
        adapter = TopListAdapter()
        recyclerView.adapter = adapter

        viewModel.getTopNews()
        launchAndRepeatWithViewLifecycle {
            observeViewModel()
        }
    }

    private fun setUpChipGroup() {
        binding.chipGroupTop.setOnCheckedChangeListener{ _, checkedId ->
            when(checkedId){
                binding.general.id -> {
                    setCategoryValue(binding.general.text.toString())
                }
                binding.sports.id -> {
                    setCategoryValue(binding.sports.text.toString())
                }
                binding.entertainment.id -> {
                    setCategoryValue(binding.entertainment.text.toString())
                }
                binding.tech.id -> {
                    setCategoryValue(binding.tech.text.toString())
                }
                binding.science.id -> {
                    setCategoryValue(binding.science.text.toString())
                }
                binding.health.id -> {
                    setCategoryValue(binding.health.text.toString())
                }
                binding.business.id -> {
                    setCategoryValue(binding.business.text.toString())
                }
            }
        }
    }

    private fun setCategoryValue(query: String){
        recyclerView.scrollToPosition(0)
        viewModel.chipText.apply {
            value = query
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