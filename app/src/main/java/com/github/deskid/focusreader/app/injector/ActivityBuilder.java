package com.github.deskid.focusreader.app.injector;

import com.github.deskid.focusreader.activity.MainActivity;
import com.github.deskid.focusreader.activity.SplashActivity;
import com.github.deskid.focusreader.screens.WebViewActivity;
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageDetailAct;
import com.github.deskid.focusreader.screens.readhub.InstantViewActivity;
import com.github.deskid.focusreader.screens.zhihudaily.ZhihuWebViewActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract WebViewActivity bindWebViewActivity();

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector
    abstract ZhihuWebViewActivity injectZhihuWebViewActivity();

    @ContributesAndroidInjector
    abstract ZenImageDetailAct injectZenImageDetailAct();

    @ContributesAndroidInjector
    abstract InstantViewActivity injectInstantViewActivity();
}
