package com.erapps.newws.ui.top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.erapps.newws.R
import com.erapps.newws.data.models.Article
import com.erapps.newws.databinding.FragmentTopListItemBinding

class TopListViewHolder(
    private val binding: FragmentTopListItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article){
        binding.article = article
        binding.ivTopItem.load(article.urlToImage) {
            crossfade(true)
            placeholder(R.drawable.ic_news)
        }

        binding.root.setOnClickListener {

        }
    }

    companion object {
        fun create(parent: ViewGroup): TopListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentTopListItemBinding.inflate(layoutInflater, parent, false)

            return TopListViewHolder(binding)
        }
    }
}