package com.erapps.newws.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erapps.newws.room.entities.Article
import com.erapps.newws.data.source.favs.IFavsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToFavsBSViewModel @Inject constructor(
    private val favsRepository: IFavsRepository
): ViewModel() {

    fun insertArticleToFavs(article: Article) = viewModelScope.launch{
        favsRepository.insertFavoriteArticle(article)
    }

}