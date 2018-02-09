package com.github.deskid.focusreader.app;

import com.github.deskid.focusreader.app.injector.AppComponent;
import com.github.deskid.focusreader.app.injector.AppModule;
import com.github.deskid.focusreader.app.injector.DaggerAppComponent;
import com.github.logutils.DebugUtils;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class App extends DaggerApplication {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInjector().inject(this);
        DebugUtils.setApplicationContext(this);
    }

    @Override
    public AndroidInjector<App> applicationInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        return appComponent;
    }

    public AppComponent appComponent() {
        if (appComponent == null) {
            appComponent = (AppComponent) applicationInjector();
        }
        return appComponent;
    }
}
