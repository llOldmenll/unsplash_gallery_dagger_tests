package com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.oldmen.unsplash_gallery_dagger_tests.R;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.grid.GridFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_main, new GridFragment(), GridFragment.class.getSimpleName())
                .commit();
    }
}
