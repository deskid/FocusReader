package com.github.deskid.focusreader.api.data

import com.github.deskid.focusreader.db.entity.InstantViewEntity
import com.google.gson.annotations.SerializedName

data class Topics(val data: ArrayList<Topic>,
                  val pageSize: Int,
                  val totalItems: Int,
                  val totalPages: Int)

data class Topic(val id: String,
                 val createdAt: String,
                 val newsArray: ArrayList<NewsItem>,
                 val order: Long,
                 val publishDate: String,
                 val summary: String,
                 val title: String,
                 val updatedAt: String,
                 val timeline: Any,
                 val extra: Extra,
                 val entityTopics: Any,
                 val entityEventTopics: Any,
                 val entityRelatedTopics: ArrayList<EntityTopic>,
                 val nelData: NelData,
                 var readMore: Boolean)

data class NewsItem(val id: String,
                    val url: String,
                    val title: String,
                    val groupId: Int,
                    val siteName: String,
                    val siteSlug: String,
                    val mobileUrl: String,
                    val authorName: String,
                    val duplicateId: Int,
                    val publishDate: String)

data class EntityTopic(val entityId: String,
                       val entityName: String,
                       val eventType: Int,
                       val eventTypeLabel: String,
                       val data: ArrayList<Entity>)

data class Entity(val id: String,
                  val title: String,
                  val url: String,
                  val mobileUrl: String,
                  val sources: ArrayList<Source>,
                  val createdAt: String)

data class Source(val name: String,
                  val url: String,
                  val mobileUrl: String)

data class NelData(val result: ArrayList<EntityTopic>)

data class Extra(val instantView: Boolean)

data class InstantView(val url: String,
                       val title: String,
                       var content: String,
                       val siteName: String,
                       val siteSlug: String?) {
    constructor(entity: InstantViewEntity) : this(
            entity.url,
            entity.title,
            entity.content,
            entity.siteName,
            entity.siteSlug
    )
}

data class Technews(val data: ArrayList<SimpleTopic>,
                    val pageSize: Int,
                    val totalItems: Int,
                    val totalPages: Int)

data class SimpleTopic(val id: String,
                       val siteName: String,
                       val authorName: String,
                       val url: String,
                       val publishDate: String,
                       @SerializedName("summary")
                       private val _summary: String?,
                       val title: String) {
    val summary: String
        get() = _summary ?: ""
}

data class News(val data: ArrayList<SimpleTopic>,
                val pageSize: Int,
                val totalItems: Int,
                val totalPages: Int)