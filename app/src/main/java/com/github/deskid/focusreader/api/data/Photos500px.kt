package com.github.deskid.focusreader.api.data

data class Photos500px(val photos: ArrayList<Photo>)

data class Photo(val id: Int,
                 val image_url: ArrayList<String>)