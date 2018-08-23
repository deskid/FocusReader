package com.github.deskid.focusreader.api.data

import com.github.deskid.focusreader.db.entity.DailyArticleEntity

data class Article(val data: Data) {
    constructor(entity: DailyArticleEntity) :
            this(Data(entity.author,
                    entity.content,
                    entity.digest,
                    entity.title
            ))
}

data class Data(val author: String,
                var content: String,
                val digest: String,
                val title: String)



