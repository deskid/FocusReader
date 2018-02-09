package com.github.deskid.focusreader.app.injector;

import com.github.deskid.focusreader.screens.articledaily.ArticleDetailFragment;
import com.github.deskid.focusreader.screens.penti.duanzi.DuanziFragment;
import com.github.deskid.focusreader.screens.penti.tugua.TuGuaFragment;
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageDetailFragment;
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageFragment;
import com.github.deskid.focusreader.screens.readhub.news.NewsFragment;
import com.github.deskid.focusreader.screens.readhub.technews.TechnewsFragment;
import com.github.deskid.focusreader.screens.readhub.topic.TopicFragment;
import com.github.deskid.focusreader.screens.zhihudaily.ZhihuFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract TuGuaFragment injectTuGuaFragment();

    @ContributesAndroidInjector
    abstract ZenImageFragment injectZenImageFragment();

    @ContributesAndroidInjector
    abstract DuanziFragment injectDuanziFragment();

    @ContributesAndroidInjector
    abstract ZenImageDetailFragment injectZenImageDetailFragment();

    @ContributesAndroidInjector
    abstract ZhihuFragment injectZhihuFragment();

    @ContributesAndroidInjector
    abstract ArticleDetailFragment injectArticleDetailFragment();

    @ContributesAndroidInjector
    abstract TopicFragment injectTopicFragment();

    @ContributesAndroidInjector
    abstract TechnewsFragment injectTechnewsFragment();

    @ContributesAndroidInjector
    abstract NewsFragment injectNewsFragment();
}

