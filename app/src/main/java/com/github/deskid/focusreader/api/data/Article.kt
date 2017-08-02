package com.github.deskid.focusreader.api.data

data class Article(val data: Data)

data class Data(val author: String,
                var content: String,
                val date: Date,
                val digest: String,
                val title: String)

data class Date(val curr: String,
                val next: String,
                val prev: String)