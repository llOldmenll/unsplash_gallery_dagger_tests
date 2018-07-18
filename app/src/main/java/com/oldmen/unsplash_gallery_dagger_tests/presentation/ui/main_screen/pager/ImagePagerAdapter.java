package com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oldmen.unsplash_gallery_dagger_tests.domain.ImageUnsplash;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.pager.image.ImageFragment;

import java.util.List;


public class ImagePagerAdapter extends FragmentStatePagerAdapter {
    private List<ImageUnsplash> mImages;

    ImagePagerAdapter(Fragment fragment, List<ImageUnsplash> images) {
        super(fragment.getChildFragmentManager());
        mImages = images;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(mImages.get(position));
    }

    void update(List<ImageUnsplash> images) {
        mImages.clear();
        mImages.addAll(images);
        notifyDataSetChanged();
    }
}
