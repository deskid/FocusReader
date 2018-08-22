package com.github.deskid.focusreader.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "dayilyArticles", indices = {@Index(value = {"title", "author"}, unique = true)})
public class DailyArticleEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String type;
    public String title;
    public String author;
    public String content;
    public String digest;

    public DailyArticleEntity(int id, String type, String title, String author, String content, String digest) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.author = author;
        this.content = content;
        this.digest = digest;
    }
}