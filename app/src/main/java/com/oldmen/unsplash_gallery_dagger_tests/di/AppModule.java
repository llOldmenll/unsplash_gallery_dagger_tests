package com.oldmen.unsplash_gallery_dagger_tests.di;

import android.content.Context;

import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.grid.GridFragment;
import com.oldmen.unsplash_gallery_dagger_tests.utils.CustomApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = CustomApplication.class,
        includes = SharedPrefHelperModule.class)
public class AppModule {

    private CustomApplication mApplication;

    public AppModule(CustomApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public CustomApplication provideApplication() {
        return mApplication;
    }

}
