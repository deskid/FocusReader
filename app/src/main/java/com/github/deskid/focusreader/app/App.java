package com.github.deskid.focusreader.app;

import android.app.Application;

import com.github.deskid.focusreader.app.injector.AppComponent;
import com.github.deskid.focusreader.app.injector.AppModule;
import com.github.deskid.focusreader.app.injector.DaggerAppComponent;

public class App extends Application {
    AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
