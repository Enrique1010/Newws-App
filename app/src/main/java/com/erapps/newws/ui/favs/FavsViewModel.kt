package com.erapps.newws.ui.favs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erapps.newws.room.entities.Article
import com.erapps.newws.data.source.favs.IFavsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavsViewModel @Inject constructor(
    private val favsRepository: IFavsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<FavsEvents>(FavsEvents.Loading)
    val uiState = _uiState.asStateFlow()

    private val _showText = Channel<FavsEvents>(Channel.CONFLATED)
    val showText = _showText.receiveAsFlow()

    val progressStatus = MutableStateFlow(true)

    fun getFavsArticles() = viewModelScope.launch {
        favsRepository.getFavoritesArticles().cachedIn(viewModelScope).collect {
            _uiState.value = FavsEvents.Success(it)
        }
    }

    fun onArticleSwipe(article: Article) = viewModelScope.launch {
        favsRepository.deleteFavArticle(article)
        _showText.trySend(FavsEvents.ShowDeleteMessage(article))
    }

    fun onUndoArticleSwipe(article: Article) = viewModelScope.launch {
        favsRepository.insertFavoriteArticle(article)
    }
}

sealed class FavsEvents {
    data class Success(val pagingData: PagingData<Article>): FavsEvents()
    data class ShowDeleteMessage(val article: Article): FavsEvents()
    object Loading: FavsEvents()
}