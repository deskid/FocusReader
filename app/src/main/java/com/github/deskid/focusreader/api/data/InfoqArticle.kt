package com.github.deskid.focusreader.api.data

import com.google.gson.annotations.SerializedName

data class InfoqArticle(val data: List<ArticleData>)

data class ArticleData(
        val aid: String,
        @SerializedName("article_cover")
        val articleCover: String,
        @SerializedName("article_summary")
        val summary: String,
        @SerializedName("article_title")
        val title: String,
        val type: Int,
        val uuid: String,
        val score: Long)


data class InfoRequest(
        val score: Long?,
        val type: Int = 1,
        val size: Int = 30,
        val id: Int = 16)
