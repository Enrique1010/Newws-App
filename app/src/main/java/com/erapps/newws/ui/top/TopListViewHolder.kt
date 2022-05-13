package com.erapps.newws.ui.top

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.erapps.newws.data.models.Article
import com.erapps.newws.databinding.FragmentTopListItemBinding

class TopListViewHolder(
    private val binding: FragmentTopListItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article){
        binding.article = article

        binding.cardTop.setOnClickListener {
            openInCustomTab(article.url!!)
        }

        binding.cardTop.setOnLongClickListener {
            val direction = TopFragmentDirections.actionTopFragmentToAddToFavsBottomSheet(article)
            binding.root.findNavController().navigate(direction)
            return@setOnLongClickListener true
        }
    }

    private fun openInCustomTab(url: String){
        val builder = CustomTabsIntent.Builder().build()
        builder.launchUrl(binding.root.context, Uri.parse(url))
    }

    companion object {
        fun create(parent: ViewGroup): TopListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentTopListItemBinding.inflate(layoutInflater, parent, false)

            return TopListViewHolder(binding)
        }
    }
}