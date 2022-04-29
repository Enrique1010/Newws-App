package com.erapps.newws.ui.news

import android.icu.text.SimpleDateFormat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.erapps.newws.R
import com.erapps.newws.data.models.Article
import com.erapps.newws.databinding.FragmentNewsListItemBinding
import java.util.*

class NewsListViewHolder(
    private val binding: FragmentNewsListItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article){
        binding.apply {
            this.article = article
            ivNewsItem.load(article.urlToImage) {
                error(R.drawable.ic_error_placeholder)
                crossfade(true)
                placeholder(R.drawable.ic_error_placeholder)
            }
        }
        /*val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault())
        val time = sdf.parse(article.publishedAt).time
        val now = System.currentTimeMillis()
        val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)

        binding.tvNewsItemDate.text = ago.toString()*/

        binding.root.setOnClickListener {

        }
    }

    companion object {
        fun create(parent: ViewGroup): NewsListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentNewsListItemBinding.inflate(layoutInflater, parent, false)

            return NewsListViewHolder(binding)
        }
    }
}