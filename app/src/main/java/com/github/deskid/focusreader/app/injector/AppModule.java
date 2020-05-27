package com.github.deskid.focusreader.app.injector;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.github.deskid.focusreader.db.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;

    public AppModule(final Context context) {
        this.context = context;
    }

    @AppScope
    @Provides
    public Context provideContext() {
        return context;
    }

    @AppScope
    @Singleton
    @Provides
    public AppDatabase provideAppDatabase() {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

}
