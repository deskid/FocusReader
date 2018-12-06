package com.github.deskid.focusreader.api.data

import com.github.deskid.focusreader.db.entity.ArticleEntity

data class Duanzi(var id: Int,
                  val link: String,
                  var title: String,
                  var description: String,
                  var pubDate: String) {

    constructor(articleEntity: ArticleEntity) :
            this(articleEntity.id,
                    articleEntity.url,
                    articleEntity.title,
                    articleEntity.description,
                    articleEntity.pubDate)
}