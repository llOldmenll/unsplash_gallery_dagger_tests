package com.oldmen.unsplash_gallery_dagger_tests.data.network;

import com.oldmen.unsplash_gallery_dagger_tests.domain.ImageUnsplash;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("photos")
    Observable<Response<List<ImageUnsplash>>> getAllPhotos(@Query("page") int page);

    @GET("search/photos")
    Observable<Response<List<ImageUnsplash>>> getSearchPhotos(@Query("page") int page, @Query("query") String searchMsg);

}
