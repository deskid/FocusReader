package com.github.deskid.focusreader.api.data

import com.github.deskid.focusreader.db.entity.ArticleEntity

data class ZenImage(
        val title: String,
        val description: String,
        var imgurl: String,
        val author: String?,
        val pubDate: String?) {

    constructor(entity: ArticleEntity) :
            this(entity.title,
                    entity.description,
                    entity.imgurl,
                    entity.author,
                    entity.pubDate)

}