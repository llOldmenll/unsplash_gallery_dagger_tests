package com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.grid;

import android.widget.ImageView;

import com.oldmen.unsplash_gallery_dagger_tests.domain.ImageUnsplash;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.base.BaseView;

import java.util.List;


public interface GridView extends BaseView {

    void updateRecycler(List<ImageUnsplash> images);

    void startPager(int position, ImageView imgView);

}
