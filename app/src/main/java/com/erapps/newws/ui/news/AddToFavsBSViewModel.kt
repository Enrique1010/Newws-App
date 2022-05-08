package com.erapps.newws.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.favs.FavsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToFavsBSViewModel @Inject constructor(
    private val favsRepository: FavsRepository
): ViewModel() {

    fun insertArticleToFavs(article: Article) = viewModelScope.launch{
        favsRepository.insertFavoriteArticle(article)
    }

}