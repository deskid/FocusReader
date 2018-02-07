package com.github.deskid.focusreader.app;

import com.github.deskid.focusreader.app.injector.AppModule;
import com.github.deskid.focusreader.app.injector.DaggerAppComponent;
import com.github.logutils.DebugUtils;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class App extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInjector().inject(this);
        DebugUtils.setApplicationContext(this);
    }

    @Override
    protected AndroidInjector<App> applicationInjector() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
