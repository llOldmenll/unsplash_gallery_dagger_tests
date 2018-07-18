package com.oldmen.unsplash_gallery_dagger_tests.di;

import android.content.Context;

import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.SharedPrefHelper;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.base.BasePresenter;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.base.BaseActivity;
import com.oldmen.unsplash_gallery_dagger_tests.utils.CustomApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class SharedPrefHelperModule {

    @Singleton
    @Provides
    public SharedPrefHelper provideSharedPrefHelper(Context context) {
        return new SharedPrefHelper(context);
    }
}
