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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    init {
        //networkManagment
        //update Room Database
    }

    private val _articleList = MutableStateFlow<NewsEvent>(NewsEvent.Success(PagingData.empty()))
    val articleList = _articleList.asStateFlow()

    fun getArticles() = viewModelScope.launch {
        newsRepository.getAllNews().cachedIn(viewModelScope)
            .collectLatest { list ->
            _articleList.value = NewsEvent.Success(list)
        }
    }
}

sealed class NewsEvent{
    data class Success(val articles: PagingData<Article>): NewsEvent()
    data class Empty(val errorMessage: String): NewsEvent()
}