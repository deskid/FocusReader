package com.github.deskid.focusreader.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.github.deskid.focusreader.db.entity.ZhihuDailyPostEntity;
import com.github.deskid.focusreader.db.entity.ZhihuEntity;

import java.util.List;

@Dao
public abstract class ZhihuDao {
    @Query("select * from zhihu_detail where id = :id")
    public abstract LiveData<List<ZhihuEntity>> query(String id);

    @Query("select * from zhihu_daily ORDER BY date DESC")
    public abstract DataSource.Factory<Integer, ZhihuDailyPostEntity> posts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ZhihuEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<ZhihuDailyPostEntity> entity);
}