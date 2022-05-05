package com.erapps.newws.ui.favs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.favs.FavsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavsViewModel @Inject constructor(
    private val favsRepository: FavsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<FavsEvents>(FavsEvents.Loading)
    val uiState = _uiState.asStateFlow()

    val progressStatus = MutableStateFlow(true)

    fun getFavsArticles() = viewModelScope.launch {
        favsRepository.getFavoritesArticles().cachedIn(viewModelScope).collect {
            _uiState.value = FavsEvents.Success(it)
        }
    }

    fun onArticleSwipe(article: Article) = viewModelScope.launch {
        favsRepository.deleteFavArticle(article)
    }

    fun onUndoArticleSwipe(article: Article) = viewModelScope.launch {
        favsRepository.insertFavoriteArticle(article)
    }
}

sealed class FavsEvents {
    data class Success(val pagingData: PagingData<Article>): FavsEvents()
    object Loading: FavsEvents()
}