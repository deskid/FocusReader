package com.github.deskid.focusreader.api;

import android.os.Trace;

import retrofit2.Retrofit;

public class Api {

    private Retrofit mRetrofit;

    public Api(Retrofit retrofit) {
        mRetrofit = retrofit;

    }

    public <T> T getService(Class<T> clazz) {
        Trace.beginSection("mRetrofit");
        T clzz = mRetrofit.create(clazz);
        Trace.endSection();
        return clzz;

    }

}
