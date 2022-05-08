package com.erapps.newws.ui.news

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.erapps.newws.R
import com.erapps.newws.data.models.Article
import com.erapps.newws.databinding.FragmentNewsListItemBinding

class NewsListViewHolder(
    private val binding: FragmentNewsListItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article){
        binding.apply {
            this.article = article
        }

        binding.cardNews.setOnClickListener {
            openInCustomTab(article.url)
        }

        binding.cardNews.setOnLongClickListener {
            val direction = NewsFragmentDirections.actionNewsFragmentToAddToFavsBottomSheet(article)
            binding.root.findNavController().navigate(direction)
            return@setOnLongClickListener true
        }
    }

    private fun openInCustomTab(url: String){
        val builder = CustomTabsIntent.Builder().build()
        builder.launchUrl(binding.root.context, Uri.parse(url))
    }

    companion object {
        fun create(parent: ViewGroup): NewsListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentNewsListItemBinding.inflate(layoutInflater, parent, false)

            return NewsListViewHolder(binding)
        }
    }
}