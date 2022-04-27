package com.erapps.newws.ui.favs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erapps.newws.data.models.Article
import com.erapps.newws.databinding.FragmentFavsItemListBinding

class FavsListViewHolder(
    private val binding: FragmentFavsItemListBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article){
        binding.article = article
        /*binding.tvNewsItemTitle.text = article.title.toString()
        binding.tvNewsItemSource.text = article.source?.name.toString()
        binding.tvNewsItemContent.text = article.content.toString()
        binding.tvNewsItemAuthor.text = article.author.toString()
        binding.tvNewsItemDate.text = article.publishedAt.toString()*/

        binding.root.setOnClickListener {

        }
    }

    companion object {
        fun create(parent: ViewGroup): FavsListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentFavsItemListBinding.inflate(layoutInflater, parent, false)

            return FavsListViewHolder(binding)
        }
    }
}