package com.github.deskid.focusreader.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.github.deskid.focusreader.db.entity.InstantContentEntity;

import java.util.List;

@Dao
public abstract class InstantContentDao {

    @Query("select * from instant where id = :id")
    public abstract LiveData<List<InstantContentEntity>> query(String id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(InstantContentEntity entity);
}
