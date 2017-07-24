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

    public YituEntity(int id, String content, String url) {
        this.id = id;
        this.content = content;
        this.url = url;
    }
}
