package com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.oldmen.unsplash_gallery_dagger_tests.utils.CustomApplication;

import java.util.List;

import dagger.ObjectGraph;

public abstract class BaseActivity extends AppCompatActivity {

    private ObjectGraph mActivityGraph;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityGraph = ((CustomApplication) getApplication()).createScopedGraph(getModules().toArray());
        mActivityGraph.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityGraph = null;
    }

    protected abstract List<Object> getModules();
}
