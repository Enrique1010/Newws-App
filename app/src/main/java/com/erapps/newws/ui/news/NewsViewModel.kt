package com.erapps.newws.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.news.INewsRepository
import com.erapps.newws.utils.NetworkManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: INewsRepository,
    networkManagement: NetworkManagement
): ViewModel() {

    private val _articleList = MutableStateFlow<NewsEvent>(NewsEvent.Loading)
    val articleList = _articleList.asStateFlow()

    val isLoading = MutableStateFlow(true)
    val networkAvailable = MutableStateFlow(true)
    val isEmpty = MutableStateFlow(false)
    val emptyText = MutableStateFlow("")
    val searchText = MutableStateFlow("the")

    init {
        viewModelScope.launch {
            networkManagement.isNetworkAvailable.collect{
                if (it){
                    networkAvailable.value = it
                    //getArticles()
                }
                networkAvailable.value = it
            }
        }
    }

    private val searchFlow = searchText.flatMapLatest { query ->
        newsRepository.getByUserQuery(query).cachedIn(viewModelScope)
    }

    fun getArticles() = viewModelScope.launch {
        searchFlow.collectLatest {
            _articleList.value = NewsEvent.Success(it)
        }
        _articleList.value = NewsEvent.Loading
    }
}

sealed class NewsEvent{
    data class Success(val articles: PagingData<Article>): NewsEvent()
    data class Empty(val errorMessage: String): NewsEvent()
    object Loading: NewsEvent()
}