package com.github.deskid.focusreader.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.github.deskid.focusreader.db.dao.ArticleDao;
import com.github.deskid.focusreader.db.dao.DailyArticleDao;
import com.github.deskid.focusreader.db.dao.InstantViewDao;
import com.github.deskid.focusreader.db.dao.WebContentDao;
import com.github.deskid.focusreader.db.dao.YituDao;
import com.github.deskid.focusreader.db.dao.ZhihuDao;
import com.github.deskid.focusreader.db.entity.ArticleEntity;
import com.github.deskid.focusreader.db.entity.DailyArticleEntity;
import com.github.deskid.focusreader.db.entity.InstantViewEntity;
import com.github.deskid.focusreader.db.entity.WebContentEntity;
import com.github.deskid.focusreader.db.entity.YituEntity;
import com.github.deskid.focusreader.db.entity.ZhihuDailyPostEntity;
import com.github.deskid.focusreader.db.entity.ZhihuEntity;

@Database(entities = {
        ArticleEntity.class,
        WebContentEntity.class,
        YituEntity.class,
        ZhihuEntity.class,
        ZhihuDailyPostEntity.class,
        DailyArticleEntity.class,
        InstantViewEntity.class}, version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "read_main_db.db";

    public abstract ArticleDao articleDao();

    public abstract WebContentDao webContentDao();

    public abstract YituDao yituDao();

    public abstract ZhihuDao zhihuDao();

    public abstract DailyArticleDao dailyArticleDao();

    public abstract InstantViewDao instantContentDao();

}
