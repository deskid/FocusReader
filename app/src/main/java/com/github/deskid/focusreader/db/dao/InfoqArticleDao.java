package com.github.deskid.focusreader.db.dao;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.github.deskid.focusreader.db.entity.InfoqArticleEntity;

import java.util.List;

@Dao
public abstract class InfoqArticleDao {
    @Query("select * from infoqArticles ORDER BY score DESC")
    public abstract DataSource.Factory<Integer, InfoqArticleEntity> posts();


    public void insertAll(List<InfoqArticleEntity> list) {
        for (InfoqArticleEntity entity : list) {
            insert(entity);
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(InfoqArticleEntity entity);

}
