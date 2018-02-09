package com.github.deskid.focusreader.widget.image.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.github.deskid.focusreader.app.App;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

@GlideModule
public class MyGlideModule extends AppGlideModule {

    @Inject
    OkHttpClient mOkHttpClient;

    private static final int MAX_CACHE_SIZE = 1024 * 1024 * 512;
    private static final String CACHE_FILE_NAME = "IMG_CACHE";

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        int memoryCacheSize = 1024 * 1024 * 48;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, CACHE_FILE_NAME, MAX_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        ((App) context.getApplicationContext()).appComponent().inject(this);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(mOkHttpClient));
    }
}