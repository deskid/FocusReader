package com.github.deskid.focusreader.api.data

import com.google.gson.annotations.SerializedName

data class Zhihu(val date: String,
                 val stories: ArrayList<Story>,
                 @SerializedName("top_stories")
                 val topStories: List<TopStory>)

data class Story(val images: List<String>,
                 val type: Int,
                 val id: Int,
                 @SerializedName("ga_prefix")
                 val gaPrefix: String,
                 val title: String,
                 val multipic: Boolean)

data class TopStory(val image: String,
                    val type: Int,
                    val id: Int,
                    @SerializedName("ga_prefix")
                    val gaPrefix: String,
                    val title: String)

data class ZhihuDetail(var body: String,
                       val title: String,
                       val image: String)