package com.github.deskid.focusreader.app;

import android.app.Application;

import com.github.deskid.focusreader.app.injector.AppComponent;
import com.github.deskid.focusreader.app.injector.AppModule;
import com.github.deskid.focusreader.app.injector.DaggerAppComponent;
import com.github.logutils.DebugUtils;

public class App extends Application {
    AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        DebugUtils.setApplicationContext(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
