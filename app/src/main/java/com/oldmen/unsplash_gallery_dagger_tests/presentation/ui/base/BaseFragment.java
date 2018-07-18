package com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.oldmen.unsplash_gallery_dagger_tests.utils.CustomApplication;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.ObjectGraph;

public abstract class BaseFragment extends Fragment {

    @Inject
    CustomApplication mApplication;
    private ObjectGraph mFragmentGraph;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentGraph = ((CustomApplication) Objects.requireNonNull(getActivity()).getApplication())
                .createScopedGraph(getModules().toArray());
        mFragmentGraph.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragmentGraph = null;
    }

    protected abstract List<Object> getModules();

}
