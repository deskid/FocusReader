package com.github.deskid.focusreader.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.github.deskid.focusreader.db.entity.ArticleEntity;

import java.util.List;

@Dao
public abstract class ArticleDao {

    @Query("select * from articles where type = :type  order by id asc limit 30 offset (:offset-1)*30")
    public abstract LiveData<List<ArticleEntity>> findArticleByType(int type, int offset);

//    @Query("select * from articles where type = 1  order by id asc limit 30 offset (:offset-1)*30")
//    public abstract Flowable<List<ArticleEntity>> queryZenImage(int offset);
//
//    @Query("select * from articles where type = :type  and url = :url limit 1")
//    public abstract Flowable<ArticleEntity> findArticleByUrl(int type, String url);

    public void insertAll(List<ArticleEntity> list) {
        for (ArticleEntity entity : list) {
            insert(entity);
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(ArticleEntity entity);

}
