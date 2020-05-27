package com.github.deskid.focusreader.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.github.deskid.focusreader.db.dao.*;
import com.github.deskid.focusreader.db.entity.*;

@Database(entities = {
        ArticleEntity.class,
        WebContentEntity.class,
        YituEntity.class,
        ZhihuEntity.class,
        ZhihuDailyPostEntity.class,
        DailyArticleEntity.class,
        InstantViewEntity.class,
        InfoqArticleEntity.class}, version = 3,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "read_main_db.db";

    public abstract ArticleDao articleDao();

    public abstract WebContentDao webContentDao();

    public abstract YituDao yituDao();

    public abstract ZhihuDao zhihuDao();

    public abstract DailyArticleDao dailyArticleDao();

    public abstract InstantViewDao instantContentDao();

    public abstract InfoqArticleDao infoqArticleDao();
}
