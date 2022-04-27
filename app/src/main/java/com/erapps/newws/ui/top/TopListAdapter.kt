package com.erapps.newws.ui.top

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.erapps.newws.data.models.Article

class TopListAdapter: PagingDataAdapter<Article, TopListViewHolder>(ARTICLE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopListViewHolder {
        return TopListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TopListViewHolder, position: Int) {
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