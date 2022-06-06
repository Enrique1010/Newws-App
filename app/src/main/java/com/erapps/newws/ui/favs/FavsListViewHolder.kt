package com.erapps.newws.ui.favs

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.erapps.newws.room.entities.Article
import com.erapps.newws.databinding.FragmentFavsItemListBinding

class FavsListViewHolder(
    private val binding: FragmentFavsItemListBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article){
        binding.article = article

        binding.cardFavs.setOnClickListener {
            openInCustomTab(article.url!!)
        }
    }

    private fun openInCustomTab(url: String){
        val builder = CustomTabsIntent.Builder().build()
        builder.launchUrl(binding.root.context, Uri.parse(url))
    }

    companion object {
        fun create(parent: ViewGroup): FavsListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentFavsItemListBinding.inflate(layoutInflater, parent, false)

            return FavsListViewHolder(binding)
        }
    }
}