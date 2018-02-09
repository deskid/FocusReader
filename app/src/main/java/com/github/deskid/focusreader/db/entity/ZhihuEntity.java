package com.github.deskid.focusreader.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "zhihu_detail", indices = {@Index(value = {"id"}, unique = true)})
public class ZhihuEntity {

    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String body;
    public String image;

    public ZhihuEntity(String id, String title, String body, String image) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.image = image;
    }
}
