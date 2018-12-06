package com.github.deskid.focusreader.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.github.deskid.focusreader.db.entity.YituEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public abstract class YituDao {

    @Query("select * from yitus where url = :url")
    public abstract LiveData<List<YituEntity>> findContentByUrl(String url);

    @Query("select * from yitus  order by id desc limit 30 offset (:offset-1)*30")
    public abstract LiveData<List<YituEntity>> queryZenImage(int offset);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(YituEntity entity);
}