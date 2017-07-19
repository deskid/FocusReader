package com.github.deskid.focusreader.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tuguas", indices = {@Index(value = {"url"}, unique = true)})
public class WebContentEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int type;//1.tugua  2.yitu
    public String content;
    public String url;

    public WebContentEntity(int id, int type, String content, String url) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.url = url;
    }
}
