package com.github.deskid.focusreader.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.github.deskid.focusreader.db.entity.DailyArticleEntity;

import java.util.List;

@Dao
public abstract class DailyArticleDao {

    @Query("select * from dayilyArticles where type = :type  order by id desc limit 30 offset (:offset-1)*30")
    public abstract LiveData<List<DailyArticleEntity>> findArticleByType(String type, int offset);

    public void insertAll(List<DailyArticleEntity> list) {
        for (DailyArticleEntity entity : list) {
            insert(entity);
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(DailyArticleEntity entity);

}
