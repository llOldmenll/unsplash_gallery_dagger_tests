package com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.grid;

import android.widget.ImageView;

import com.google.gson.Gson;
import com.oldmen.unsplash_gallery_dagger_tests.data.network.RetrofitClient;
import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.SharedPrefHelper;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.base.BasePresenter;
import com.oldmen.unsplash_gallery_dagger_tests.utils.CustomApplication;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GridPresenter extends BasePresenter {
    private GridView mView;

    public GridPresenter(SharedPrefHelper sharedPrefHelper, GridView view) {
        super(sharedPrefHelper);
        mView = view;
    }

    public void getImagesFromDb() {
        CustomApplication.getDbInstance().getImagesDao().getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(imageUnsplashes -> {
                    saveCurrentPage(getCurrentPage() + 1);
                    mView.updateRecycler(imageUnsplashes);
                })
                .subscribe();
    }

    @Override
    public void loadImagesWithSearch(Gson gson) {
        int currentPage = getCurrentPage();
        addLoadingBaseAction(currentPage,
                RetrofitClient.getApiService(gson).getSearchPhotos(currentPage + 1, getSearchQuery()))
                .doOnError(throwable -> handleError(throwable, mView))
                .subscribe();
    }

    @Override
    public void loadImagesDefault(Gson gson) {
        int currentPage = getCurrentPage();
        addLoadingBaseAction(currentPage,
                RetrofitClient.getApiService(gson).getAllPhotos(currentPage + 1))
                .doOnError(throwable -> handleError(throwable, mView))
                .subscribe();
    }

    public void startPager(int position, ImageView imgView) {
        saveCurrentPosition(position);
        mView.startPager(position, imgView);
    }

}
