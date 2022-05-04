package com.erapps.newws.room

import androidx.room.TypeConverter
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.models.Source
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson: Gson = Gson()

    @TypeConverter
    fun articlesToGson(article: Article): String? {
        return gson.toJson(article)
    }

    @TypeConverter
    fun gsonToArticle(json: String?): Article? {
        return gson.fromJson(json, Article::class.java)
    }

    @TypeConverter
    fun sourceToGson(source: Source): String? {
        return gson.toJson(source)
    }

    @TypeConverter
    fun gsonToSource(json: String?): Source? {
        return gson.fromJson(json, Source::class.java)
    }

    @TypeConverter
    fun listOfArticlesToGson(articles: List<Article>): String? {
        return gson.toJson(articles)
    }

    @TypeConverter
    fun gsonToListOfArticles(json: String): List<Article> {
        if (json.isEmpty()) return emptyList()

        val listType = object: TypeToken<List<Article>>() {}.type
        return gson.fromJson(json, listType)
    }
}