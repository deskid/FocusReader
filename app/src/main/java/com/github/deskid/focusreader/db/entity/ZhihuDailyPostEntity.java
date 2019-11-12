package com.github.deskid.focusreader.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.github.deskid.focusreader.api.data.Story;
import com.github.deskid.focusreader.api.data.Zhihu;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "zhihu_daily",
        indices = {@Index(value = {"id"}, unique = true)})
@TypeConverters(Converter.class)
public class ZhihuDailyPostEntity {
    @NonNull
    public String date;

    public List<String> images;

    @PrimaryKey
    public Integer id;
    public String title;

    public ZhihuDailyPostEntity(@NonNull String date, List<String> images, Integer id, String title) {
        this.date = date;
        this.images = images;
        this.id = id;
        this.title = title;
    }

    public static List<ZhihuDailyPostEntity> zhihuWrap(Zhihu zhihu) {
        ArrayList<ZhihuDailyPostEntity> result = new ArrayList<>();
        for (int i = 0; i < zhihu.getStories().size(); i++) {
            Story story = zhihu.getStories().get(i);
            ZhihuDailyPostEntity entity = new ZhihuDailyPostEntity(zhihu.getDate(), story.getImages(), story.getId(), story.getTitle());
            result.add(entity);
        }
        return result;
    }


}
