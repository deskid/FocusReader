package com.github.deskid.focusreader.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.github.deskid.focusreader.db.entity.ZhihuEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public abstract class ZhihuDao {
    @Query("select * from zhihu_detail where id = :id")
    public abstract Flowable<List<ZhihuEntity>> query(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ZhihuEntity entity);
}