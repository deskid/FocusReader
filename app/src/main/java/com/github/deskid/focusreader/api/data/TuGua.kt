package com.github.deskid.focusreader.api.data

import com.github.deskid.focusreader.db.entity.ArticleEntity

data class TuGua(
        var id: Int,
        var title: String,
        val description: String,
        val imgurl: String,
        val author: String?,
        var pubDate: String?) {

    constructor(entity: ArticleEntity) :
            this(entity.id,
                    entity.title,
                    entity.description,
                    entity.imgurl,
                    entity.author,
                    entity.pubDate)
}


