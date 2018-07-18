package com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.pager;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.oldmen.unsplash_gallery_dagger_tests.data.network.RetrofitClient;
import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.LocalFileManager;
import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.SharedPrefHelper;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.base.BasePresenter;
import com.oldmen.unsplash_gallery_dagger_tests.utils.CustomApplication;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PagerPresenter extends BasePresenter {
    private PagerView mView;
    private LocalFileManager mFileManager;

    public PagerPresenter(SharedPrefHelper sharedPrefHelper, PagerView view, LocalFileManager fileManager) {
        super(sharedPrefHelper);
        mView = view;
        mFileManager = fileManager;
    }

    public void getImagesFromDb() {
        CustomApplication.getDbInstance().getImagesDao().getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(imageUnsplashes -> {
                    if (imageUnsplashes.size() != 0) mView.updatePager(imageUnsplashes);
                })
                .subscribe();
    }

    @Override
    public synchronized void loadImagesWithSearch(Gson gson) {
        int currentPage = getCurrentPage();
        addLoadingBaseAction(currentPage,
                RetrofitClient.getApiService(gson).getSearchPhotos(currentPage + 1, getSearchQuery()))
                .doOnError(throwable -> handleError(throwable, mView))
                .subscribe();
    }

    @Override
    public synchronized void loadImagesDefault(Gson gson) {
        int currentPage = getCurrentPage();
        addLoadingBaseAction(currentPage,
                RetrofitClient.getApiService(gson).getAllPhotos(currentPage + 1))
                .doOnError(throwable -> handleError(throwable, mView))
                .subscribe();
    }

    public void changeFooterState() {
        boolean footerState = getFooterState();
        if (footerState) mView.hideFooter();
        else mView.showFooter();
        saveFooterState(!footerState);
    }

    public void saveImage(Bitmap finalBitmap, String name) {
        Observable.fromCallable(() -> mFileManager.saveImage(finalBitmap, name))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(isSaved -> {
                    if (isSaved) mView.showImageLoadingMsg("Image saved");
                    else mView.showImageLoadingMsg("Image wasn't saved");
                })
                .subscribe();
    }
}
