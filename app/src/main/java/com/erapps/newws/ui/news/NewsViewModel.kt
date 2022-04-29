package com.erapps.newws.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.news.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _articleList = MutableStateFlow<NewsEvent>(NewsEvent.Success(PagingData.empty()))
    val articleList = _articleList.asStateFlow()

    val searchText = MutableStateFlow("all")
    private val searchFlow = searchText.flatMapLatest {
        newsRepository.getByUserQuery(it).cachedIn(viewModelScope)
    }

    init {
        //networkManagment
        //update Room Database
    }

    fun getArticles() = viewModelScope.launch {
        searchFlow.collectLatest {
            _articleList.value = NewsEvent.Success(it)
        }
    }
}

sealed class NewsEvent{
    data class Success(val articles: PagingData<Article>): NewsEvent()
    data class Empty(val errorMessage: String): NewsEvent()
}