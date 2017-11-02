package com.github.deskid.focusreader.app.injector;

import com.github.deskid.focusreader.activity.SplashActivity;
import com.github.deskid.focusreader.api.Api;
import com.github.deskid.focusreader.screens.articledaily.ArticleDetailFragment;
import com.github.deskid.focusreader.screens.penti.PentiFragment;
import com.github.deskid.focusreader.screens.penti.duanzi.DuanziFragment;
import com.github.deskid.focusreader.screens.penti.tugua.TuGuaFragment;
import com.github.deskid.focusreader.screens.penti.tugua.WebViewActivity;
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageDetailAct;
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageDetailFragment;
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageFragment;
import com.github.deskid.focusreader.screens.readhub.technews.NewsFragment;
import com.github.deskid.focusreader.screens.readhub.technews.TechnewsFragment;
import com.github.deskid.focusreader.screens.readhub.topic.TopicFragment;
import com.github.deskid.focusreader.screens.zhihudaily.ZhihuFragment;
import com.github.deskid.focusreader.screens.zhihudaily.ZhihuWebViewActivity;

import dagger.Component;

@AppScope
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    Api api();

    void inject(SplashActivity activity);

    void inject(WebViewActivity activity);

    void inject(ZhihuWebViewActivity activity);

    void inject(ZenImageDetailAct activity);

    void inject(TuGuaFragment fragment);

    void inject(ZenImageFragment fragment);

    void inject(DuanziFragment fragment);

    void inject(PentiFragment fragment);

    void inject(ZenImageDetailFragment fragment);

    void inject(ZhihuFragment fragment);

    void inject(ArticleDetailFragment fragment);

    void inject(TopicFragment fragment);

    void inject(TechnewsFragment fragment);

    void inject(NewsFragment fragment);
}
