package com.github.deskid.focusreader.app.injector;

import android.content.Context;

import com.github.deskid.focusreader.api.Api;
import com.github.deskid.focusreader.api.service.IAppService;
import com.github.deskid.focusreader.utils.LiveDataCallAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    private static final String BASE_URL = "http://appb.dapenti.com/";

    @AppScope
    @Provides
    public IAppService provideAppService(Api api) {
        return api.getService(IAppService.class);
    }

    @AppScope
    @Provides
    public Api provideApi(Retrofit retrofit) {
        return new Api(retrofit);
    }

    @AppScope
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okhttpclient, GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .client(okhttpclient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @AppScope
    @Provides
    OkHttpClient provideHttpClient(HttpLoggingInterceptor logger, Cache cache) {
        return new OkHttpClient().newBuilder()
                .addInterceptor(logger)
                .cache(cache)
                .build();

    }

    @AppScope
    @Provides
    HttpLoggingInterceptor provideInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @AppScope
    @Provides
    Cache provideCache(File file) {
        return new Cache(file, 10 * 10 * 1000);
    }

    @AppScope
    @Provides
    File provideCacheFile(Context context) {
        return context.getFilesDir();
    }

    @AppScope
    @Provides
    RxJava2CallAdapterFactory provideRxAdapter() {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    @Provides
    @AppScope
    GsonConverterFactory provideGsonClient(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @AppScope
    Gson provideGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }

}
