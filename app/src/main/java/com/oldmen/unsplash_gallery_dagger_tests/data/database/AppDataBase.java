package com.oldmen.unsplash_gallery_dagger_tests.data.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.oldmen.unsplash_gallery_dagger_tests.domain.ImageUnsplash;

@Database(entities = {ImageUnsplash.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract ImagesUnsplashDao getImagesDao();

}
