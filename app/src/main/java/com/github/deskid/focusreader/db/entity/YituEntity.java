package com.github.deskid.focusreader.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "yitus", indices = {@Index(value = {"url"}, unique = true)})
public class YituEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String content;
    public String url;
    public String imgurl;
    public String title;
    public String description;
    public String author;
    public String pubDate;

    public YituEntity(int id, String content, String url, String imgurl, String title, String description, String author, String pubDate) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.imgurl = imgurl;
        this.title = title;
        this.description = description;
        this.author = author;
        this.pubDate = pubDate;
    }
}
