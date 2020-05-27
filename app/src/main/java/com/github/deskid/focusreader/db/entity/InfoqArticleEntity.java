package com.github.deskid.focusreader.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.github.deskid.focusreader.api.data.ArticleData;
import com.github.deskid.focusreader.api.data.InfoqArticle;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "infoqArticles", indices = {@Index(value = {"aid"}, unique = true)})
public class InfoqArticleEntity {
    @PrimaryKey
    @NonNull
    public String aid;
    public String articleCover;
    public String title;
    public String link;
    public String summary;
    public long score;

    public InfoqArticleEntity(String aid, String articleCover, String title, String link, String summary, long score) {
        this.aid = aid;
        this.articleCover = articleCover;
        this.title = title;
        this.link = link;
        this.summary = summary;
        this.score = score;
    }

    public static List<InfoqArticleEntity> wrap(InfoqArticle article) {
        ArrayList<InfoqArticleEntity> result = new ArrayList<>();
        for (int i = 0; i < article.getData().size(); i++) {
            ArticleData articleData = article.getData().get(i);
            // 只过滤文章类型
            if (articleData.getType() == 1) {
                String link = "https://www.infoq.cn/article/" + articleData.getUuid();
                InfoqArticleEntity entity = new InfoqArticleEntity(articleData.getAid(), articleData.getArticleCover(), articleData.getTitle(), link, articleData.getSummary(), articleData.getScore());
                result.add(entity);
            }

        }
        return result;
    }
}
