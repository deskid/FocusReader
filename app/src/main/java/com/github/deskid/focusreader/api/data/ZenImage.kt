package com.github.deskid.focusreader.api.data

import android.os.Parcel
import android.os.Parcelable
import com.github.deskid.focusreader.db.entity.ArticleEntity

data class ZenImage(
        val title: String,
        var description: String,
        var imgurl: String,
        val author: String?,
        val pubDate: String?,
        var url: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    constructor(entity: ArticleEntity) :
            this(entity.title,
                    entity.description,
                    entity.imgurl,
                    entity.author,
                    entity.pubDate,
                    entity.url)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(imgurl)
        parcel.writeString(author)
        parcel.writeString(pubDate)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ZenImage> {
        override fun createFromParcel(parcel: Parcel): ZenImage {
            return ZenImage(parcel)
        }

        override fun newArray(size: Int): Array<ZenImage?> {
            return arrayOfNulls(size)
        }
    }

}