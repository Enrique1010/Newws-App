package com.erapps.newws.ui.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.topheadlines.TopHeadLinesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopListViewModel @Inject constructor(
    private val topHeadLinesRepository: TopHeadLinesRepository
): ViewModel(){
    private val _topHeadLines = MutableStateFlow<TopListEvent>(TopListEvent.Success(PagingData.empty()))
    val topHeadLines = _topHeadLines.asStateFlow()

    val chipText = MutableStateFlow("")
    private val searchFlow = chipText.flatMapLatest {
        topHeadLinesRepository.getTopNewsByCategory(it).cachedIn(viewModelScope)
    }

    init {
        //TODO("networking")
    }

    fun getTopNews() = viewModelScope.launch {
        searchFlow.collect {
            _topHeadLines.value = TopListEvent.Success(it)
        }
    }
}

sealed class TopListEvent{
    data class Success(val topArticles: PagingData<Article>): TopListEvent()
    data class Empty(val message: String): TopListEvent()
}