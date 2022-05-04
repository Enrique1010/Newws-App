package com.erapps.newws.ui.favs

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.erapps.newws.R
import com.erapps.newws.data.models.Article
import com.erapps.newws.databinding.FragmentFavsItemListBinding

class FavsListViewHolder(
    private val binding: FragmentFavsItemListBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article){
        binding.article = article
        binding.ivFavsItem.load(article.urlToImage) {
            crossfade(true)
            placeholder(R.drawable.ic_error_placeholder)
        }

        binding.cardFavs.setOnClickListener {
            openInCustomTab(article.url)
        }
    }

    private fun openInCustomTab(url: String){
        val builder = CustomTabsIntent.Builder().build()
        builder.launchUrl(binding.root.context, Uri.parse(url))
    }

    /*private fun openInBrowser(url: String){
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        binding.root.context.startActivity(i)
    }*/

    companion object {
        fun create(parent: ViewGroup): FavsListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentFavsItemListBinding.inflate(layoutInflater, parent, false)

            return FavsListViewHolder(binding)
        }
    }
}