package com.erapps.newws.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.favs.FavsRepository
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
    private val localRepository: ILocalNewsRepository,
    private val favsRepository: FavsRepository
): ViewModel() {

    private val _articleList = MutableStateFlow<NewsEvent>(NewsEvent.Success(PagingData.empty()))
    val articleList = _articleList.asStateFlow()

    private val _insertEvent = Channel<NewsEvent>(Channel.CONFLATED)
    val insertEvent = _insertEvent.receiveAsFlow()

    val searchText = MutableStateFlow("all")

    private val searchFlow = searchText.flatMapLatest { query ->
        localRepository.getByUserQuery(query).cachedIn(viewModelScope).let {
            newsRepository.getByUserQuery(query).cachedIn(viewModelScope)
        }
        //newsRepository.getByUserQuery(it).cachedIn(viewModelScope)
    }

    init {
        //networkManagement
        //update Room Database
    }

    fun getArticles() = viewModelScope.launch {
        searchFlow.collectLatest {
            //localRepository.clearDatabase()
            it.map { article ->
                val list = mutableListOf<Article>()
                list.add(article)
                localRepository.insertUpdatedList(list)
            }
            _articleList.value = NewsEvent.Success(it)
        }
    }

    fun onArticleSwipe(article: Article) = viewModelScope.launch {
        favsRepository.insertFavoriteArticle(article)
        _insertEvent.send(NewsEvent.ShowInsertMessage(article))
    }

    fun onUndoAddedToFavs(article: Article) = viewModelScope.launch {
        favsRepository.deleteFavArticle(article)
    }
}

sealed class NewsEvent{
    data class Success(val articles: PagingData<Article>): NewsEvent()
    data class ShowInsertMessage(val article: Article): NewsEvent()
    data class Empty(val errorMessage: String): NewsEvent()
}