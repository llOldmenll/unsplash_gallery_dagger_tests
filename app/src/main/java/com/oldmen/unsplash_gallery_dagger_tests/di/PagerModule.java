package com.oldmen.unsplash_gallery_dagger_tests.di;

import android.content.Context;

import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.LocalFileManager;
import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.SharedPrefHelper;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.pager.PagerPresenter;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.pager.PagerView;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.pager.PagerFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = PagerFragment.class, addsTo = AppModule.class)
public class PagerModule {

    private PagerView mView;

    public PagerModule(PagerView view) {
        mView = view;
    }

    @Provides
    @Singleton
    public PagerView provideView() {
        return mView;
    }

    @Provides
    @Singleton
    public LocalFileManager provideLocalFileManager(Context context) {
        return new LocalFileManager(context);
    }

    @Provides
    @Singleton
    public PagerPresenter providePresenter(SharedPrefHelper sharedPrefHelper, PagerView view,
                                           LocalFileManager fileManager) {
        return new PagerPresenter(sharedPrefHelper, view, fileManager);
    }
}
