package com.github.deskid.focusreader.api.data

import com.github.deskid.focusreader.db.entity.ArticleEntity

data class Duanzi(var title: String,
                  var description: String,
                  var pubDate: String) {

    constructor(articleEntity: ArticleEntity) :
            this(articleEntity.title,
                    articleEntity.description,
                    articleEntity.pubDate)
}