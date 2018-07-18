package com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.oldmen.unsplash_gallery_dagger_tests.data.database.ImagesUnsplashDao;
import com.oldmen.unsplash_gallery_dagger_tests.data.network.ImageInfoDeserializer;
import com.oldmen.unsplash_gallery_dagger_tests.data.repositories.SharedPrefHelper;
import com.oldmen.unsplash_gallery_dagger_tests.domain.ImageUnsplash;
import com.oldmen.unsplash_gallery_dagger_tests.utils.CustomApplication;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public abstract class BasePresenter {
    private final SharedPrefHelper mSharedPrefHelper;

    public BasePresenter(SharedPrefHelper sharedPrefHelper) {
        mSharedPrefHelper = sharedPrefHelper;
    }

    public abstract void loadImagesWithSearch(Gson gson);

    public abstract void loadImagesDefault(Gson gson);

    public void saveCurrentPosition(int position) {
        mSharedPrefHelper.saveCurrentPosition(position);
    }

    public int getCurrentPosition() {
        return mSharedPrefHelper.getCurrentPosition();
    }

    public void saveCurrentPage(int page) {
        mSharedPrefHelper.saveCurrentPage(page);
    }

    public int getCurrentPage() {
        return mSharedPrefHelper.getCurrentPage();
    }

    private void savePagesNumber(int pagesNumber) {
        mSharedPrefHelper.savePagesNumber(pagesNumber);
    }

    public int getPagesNumber() {
        return mSharedPrefHelper.getPagesNumber();
    }

    public void loadImages() {
        Gson gson = new GsonBuilder().registerTypeAdapter(
                new TypeToken<List<ImageUnsplash>>() {
                }.getType(),
                new ImageInfoDeserializer()).create();

        if (getSearchQuery().isEmpty())
            loadImagesDefault(gson);
        else
            loadImagesWithSearch(gson);
    }

    private void appendImagesTable(List<ImageUnsplash> images) {
        CustomApplication.getDbInstance().getImagesDao().insertAll(images);
    }

    private void refreshImagesTable(List<ImageUnsplash> images) {
        ImagesUnsplashDao dao = CustomApplication.getDbInstance().getImagesDao();
        dao.drop();
        dao.insertAll(images);
    }

    protected String getSearchQuery() {
        return mSharedPrefHelper.getSearchQuery();
    }

    public void saveSearchQuery(String query) {
        mSharedPrefHelper.saveSearchQuery(query.trim());
    }

    protected void handleError(Throwable e, BaseView view) {
        if (e instanceof IOException) view.showNoInternetMsg(null);
        else view.showErrorMsg(e.getMessage());
    }

    private void updateImagesTable(int currentPage, Response<List<ImageUnsplash>> response) {
        List<ImageUnsplash> imagesInfo = response.body();
        if (imagesInfo != null) {
            if (currentPage == 0) refreshImagesTable(imagesInfo);
            else appendImagesTable(imagesInfo);
        }
    }

    private void saveSumPagesNumber(Response response) {
        int imgsPerPage = 0;
        int imgsTotal = 0;

        if (response.headers().get("X-Per-Page") != null)
            imgsPerPage = Integer.parseInt(response.headers().get("X-Per-Page"));
        if (response.headers().get("X-Total") != null)
            imgsTotal = Integer.parseInt(response.headers().get("X-Total"));

        savePagesNumber((int) Math.ceil((double) imgsTotal / (double) imgsPerPage));
    }

    protected boolean getFooterState() {
        return mSharedPrefHelper.getFooterState();
    }

    protected void saveFooterState(boolean state) {
        mSharedPrefHelper.saveFooterState(state);
    }

    protected Observable<Response<List<ImageUnsplash>>> addLoadingBaseAction(int currentPage, Observable<Response<List<ImageUnsplash>>> observable) {
        return observable.doOnNext(response -> updateImagesTable(currentPage, response))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                })
                .doOnNext(response -> {
                    if (currentPage == 0) saveSumPagesNumber(response);
                });
    }
}
