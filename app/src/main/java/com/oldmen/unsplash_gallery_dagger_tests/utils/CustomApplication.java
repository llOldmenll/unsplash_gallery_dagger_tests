package com.oldmen.unsplash_gallery_dagger_tests.utils;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.oldmen.unsplash_gallery_dagger_tests.data.database.AppDataBase;
import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.SharedPrefHelper;
import com.oldmen.unsplash_gallery_dagger_tests.di.AppModule;


import javax.inject.Inject;

import dagger.ObjectGraph;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.oldmen.unsplash_gallery_dagger_tests.utils.Constants.DATA_BASE_NAME;

public class CustomApplication extends Application {

    private static AppDataBase mDataBase;
    private ObjectGraph mObjectGraph;
    @Inject
    SharedPrefHelper mSharedPrefHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        mObjectGraph = ObjectGraph.create(new AppModule(this));
        mObjectGraph.inject(this);
        mDataBase = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, DATA_BASE_NAME).build();
        dropData();
    }

    public static AppDataBase getDbInstance() {
        return mDataBase;
    }

    private void dropData() {
        mSharedPrefHelper.removeAll();
        Completable.fromAction(() -> mDataBase.getImagesDao().drop())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public ObjectGraph createScopedGraph(Object... modules) {
        return mObjectGraph.plus(modules);
    }
}
