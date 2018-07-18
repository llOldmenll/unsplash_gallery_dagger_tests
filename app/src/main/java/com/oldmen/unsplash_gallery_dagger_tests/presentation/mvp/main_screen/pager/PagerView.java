package com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.pager;


import com.oldmen.unsplash_gallery_dagger_tests.domain.ImageUnsplash;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.base.BaseView;

import java.util.List;

public interface PagerView extends BaseView {

    void updatePager(List<ImageUnsplash> images);

    void hideFooter();

    void showFooter();

    void showImageLoadingMsg(String msg);
}
