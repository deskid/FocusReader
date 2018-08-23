package com.github.deskid.focusreader.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.github.deskid.focusreader.api.data.InstantView;

@Entity(tableName = "instant", indices = {@Index(value = {"url"}, unique = true)})
public class InstantViewEntity {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String id;
    public String content;
    public String url;
    public String title;
    public String siteName;
    public String siteSlug;

    public InstantViewEntity(String id, String content, String url, String title, String siteName, String siteSlug) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.title = title;
        this.siteName = siteName;
        this.siteSlug = siteSlug;
    }

    public InstantViewEntity(String id, InstantView instantView) {
        this.id = id;
        this.content = instantView.getContent();
        this.url = instantView.getUrl();
        this.title = instantView.getTitle();
        this.siteName = instantView.getSiteName();
        this.siteSlug = instantView.getSiteSlug();
    }

}
