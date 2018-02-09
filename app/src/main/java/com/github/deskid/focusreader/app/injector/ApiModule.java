package com.github.deskid.focusreader.app.injector;

import android.content.Context;

import com.github.deskid.focusreader.api.Api;
import com.github.deskid.focusreader.api.service.IAppService;
import com.github.logutils.DebugUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @AppScope
    @Provides
    OkHttpClient provideHttpClient(HttpLoggingInterceptor logger, Cache cache) {
        SSLSocketFactory sslSocketFactory;
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{trustManager};

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .addInterceptor(logger)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .cache(cache);

        if (DebugUtils.isDebug()) {
            builder.sslSocketFactory(sslSocketFactory, trustManager);
        }

        return builder.build();
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
                .setLenient()
                .setPrettyPrinting()
                .create();
    }

}
