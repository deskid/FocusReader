package com.github.deskid.focusreader.api;

import retrofit2.Retrofit;

public class Api {

    private Retrofit mRetrofit;

    public Api(Retrofit retrofit) {
        mRetrofit = retrofit;

    }

    public <T> T getService(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

}
