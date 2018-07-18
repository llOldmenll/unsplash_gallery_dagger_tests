package com.oldmen.unsplash_gallery_dagger_tests.di;

import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.SharedPrefHelper;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.grid.GridPresenter;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.grid.GridView;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.grid.GridFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = GridFragment.class, addsTo = AppModule.class)
public class GridModule {

    private GridView mView;

    public GridModule(GridView view) {
        mView = view;
    }

    @Provides
    @Singleton
    public GridView provideView() {
        return mView;
    }

    @Provides
    @Singleton
    public GridPresenter providePresenter(SharedPrefHelper sharedPrefHelper, GridView view) {
        return new GridPresenter(sharedPrefHelper, view);
    }
}
