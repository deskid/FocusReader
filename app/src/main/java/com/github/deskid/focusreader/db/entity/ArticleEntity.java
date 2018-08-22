package com.github.deskid.focusreader.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.github.deskid.focusreader.api.data.Duanzi;
import com.github.deskid.focusreader.api.data.TuGua;
import com.github.deskid.focusreader.api.data.ZenImage;

@Entity(tableName = "articles", indices = {@Index(value = {"type", "description"}, unique = true)})
public class ArticleEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    // *1:yitu 2:tugua 3:duanzi
    public int type;
    public String title;
    public String author;
    public String pubDate;
    public String description;
    public String imgurl;
    public String url;

    public ArticleEntity(int id, int type, String title, String author, String pubDate, String description, String imgurl, String url) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.author = author;
        this.pubDate = pubDate;
        this.description = description;
        this.imgurl = imgurl;
        this.url = url;
    }

    public static ArticleEntity zenImageWrap(ZenImage zenImage) {
        return new ArticleEntity(0, 1, zenImage.getTitle(), zenImage.getAuthor(), zenImage.getPubDate(), zenImage.getDescription(), zenImage.getImgurl(), zenImage.getUrl());
    }

    public static ArticleEntity tuguaWrap(TuGua tuGua) {
        return new ArticleEntity(0, 2, tuGua.getTitle(), tuGua.getAuthor(), tuGua.getPubDate(), tuGua.getDescription(), tuGua.getImgurl(), "");
    }

    public static ArticleEntity duanziWrap(Duanzi duanzi) {
        return new ArticleEntity(0, 3, duanzi.getTitle(), "", duanzi.getPubDate(), duanzi.getDescription(), "", "");
    }
}