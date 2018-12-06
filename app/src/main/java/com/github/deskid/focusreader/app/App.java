package com.github.deskid.focusreader.app;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import android.webkit.WebSettings;

import com.github.deskid.focusreader.app.injector.AppComponent;
import com.github.deskid.focusreader.app.injector.AppModule;
import com.github.deskid.focusreader.app.injector.DaggerAppComponent;
import com.github.logutils.DebugUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class App extends DaggerApplication {

    private AppComponent appComponent;

    private static void preloadWebView(final Context context) {
        final HandlerThread thread = new HandlerThread("WebViewPreload");
        thread.start();
        new Handler(thread.getLooper()).post(new Runnable() {
            @Override
            public void run() {
                final long t0 = SystemClock.uptimeMillis();

                try {
                    WebSettings.getDefaultUserAgent(context);
                    Log.i("", "Preload webview complete: " + (SystemClock.uptimeMillis() - t0) + " ms");
                } catch (final Throwable e) {
                    Log.e("", "Preload webview failed", e);
                } finally {
                    try {
                        thread.quit();
                    } catch (final Throwable e) {
                        Log.e("", "Quit looper of thread `WebViewPreload` failed");
                    }
                }
            }
        });
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

    private void enableSystemTrace() {

        try {
            Class<?> trace = Class.forName("android.os.Trace");
            Method setAppTracingAllowed = trace.getDeclaredMethod("setAppTracingAllowed", boolean.class);
            setAppTracingAllowed.invoke(null, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInjector().inject(this);
        DebugUtils.setApplicationContext(this);
        preloadWebView(this);
        enableSystemTrace();
    }
}
