package com.oldmen.unsplash_gallery_dagger_tests.di;

import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.SharedPrefHelper;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.splash_screen.SplashPresenter;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.splash_screen.SplashView;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.splash_screen.SplashActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = SplashActivity.class, addsTo = AppModule.class)
public class SplashModule {

    private SplashView mView;

    public SplashModule(SplashView view) {
        mView = view;
    }

    @Provides
    @Singleton
    public SplashView provideView() {
        return mView;
    }

    @Provides
    @Singleton
    public SplashPresenter providePresenter(SplashView view, SharedPrefHelper sharedPrefHelper) {
        return new SplashPresenter(view, sharedPrefHelper);
    }
}
