package com.github.deskid.focusreader.app.injector;

import com.github.deskid.focusreader.api.Api;
import com.github.deskid.focusreader.app.App;

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
}
