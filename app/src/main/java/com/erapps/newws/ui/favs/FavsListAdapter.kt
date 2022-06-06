package com.erapps.newws.ui.favs

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.erapps.newws.room.entities.Article

class FavsListAdapter: PagingDataAdapter<Article, FavsListViewHolder>(ARTICLE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavsListViewHolder {
        return FavsListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavsListViewHolder, position: Int) {
        val article = getItem(position)

        if(article != null){
            holder.bind(article)
        }
    }

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }
}