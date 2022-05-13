package com.erapps.newws.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.news.ILocalNewsRepository
import com.erapps.newws.data.source.news.INewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: INewsRepository,
    private val localRepository: ILocalNewsRepository
): ViewModel() {

    private val _articleList = MutableStateFlow<NewsEvent>(NewsEvent.Loading)
    val articleList = _articleList.asStateFlow()

    val searchText = MutableStateFlow("the")

    init {
        //networkManagement
        //update Room Database
    }

    private val searchFlow = searchText.flatMapLatest { query ->
        newsRepository.getByUserQuery(query).cachedIn(viewModelScope)
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
    object Loading: NewsEvent()
}