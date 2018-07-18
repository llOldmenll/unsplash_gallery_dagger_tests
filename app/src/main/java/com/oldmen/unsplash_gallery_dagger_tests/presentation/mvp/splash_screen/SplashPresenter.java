package com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.splash_screen;

import com.google.gson.Gson;
import com.oldmen.unsplash_gallery_dagger_tests.data.network.RetrofitClient;
import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.SharedPrefHelper;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.base.BasePresenter;

public class SplashPresenter extends BasePresenter {

    private SplashView mView;

    public SplashPresenter(SplashView view, SharedPrefHelper sharedPrefHelper) {
        super(sharedPrefHelper);
        mView = view;
    }

    @Override
    public void loadImagesWithSearch(Gson gson) {

    }

    @Override
    public void loadImagesDefault(Gson gson) {
        int currentPage = getCurrentPage();
        addLoadingBaseAction(currentPage, RetrofitClient.getApiService(gson).getAllPhotos(currentPage + 1))
                .doOnError(throwable -> handleError(throwable, mView))
                .doOnNext(response -> mView.startMainActivity())
                .subscribe();
    }

}
