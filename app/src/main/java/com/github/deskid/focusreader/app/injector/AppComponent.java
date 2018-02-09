package com.github.deskid.focusreader.app.injector;

import com.github.deskid.focusreader.api.Api;
import com.github.deskid.focusreader.app.App;
import com.github.deskid.focusreader.screens.articledaily.ArticleDetailViewModel;
import com.github.deskid.focusreader.screens.penti.duanzi.DuanziViewModel;
import com.github.deskid.focusreader.screens.penti.tugua.TuGuaViewModel;
import com.github.deskid.focusreader.screens.penti.tugua.TuGuaWebViewModel;
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageDetailViewModel;
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageViewModel;
import com.github.deskid.focusreader.screens.readhub.news.NewsViewModel;
import com.github.deskid.focusreader.screens.readhub.technews.TechnewsViewModel;
import com.github.deskid.focusreader.screens.readhub.topic.TopicViewModel;
import com.github.deskid.focusreader.screens.zhihudaily.WebViewModel;
import com.github.deskid.focusreader.screens.zhihudaily.ZhihuViewModel;
import com.github.deskid.focusreader.widget.image.glide.MyGlideModule;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@AppScope
@Component(modules = {
        AppModule.class,
        ApiModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBuilder.class,
        FragmentBuilder.class})
public interface AppComponent extends AndroidInjector<App> {
    Api api();

    void inject(MyGlideModule glideModule);

    void inject(ArticleDetailViewModel viewModel);

    void inject(ZhihuViewModel viewModel);

    void inject(NewsViewModel viewModel);

    void inject(TechnewsViewModel viewModel);

    void inject(TopicViewModel viewModel);

    void inject(WebViewModel viewModel);

    void inject(DuanziViewModel viewModel);

    void inject(TuGuaViewModel viewModel);

    void inject(ZenImageViewModel viewModel);

    void inject(ZenImageDetailViewModel viewModel);

    void inject(TuGuaWebViewModel viewModel);


}
