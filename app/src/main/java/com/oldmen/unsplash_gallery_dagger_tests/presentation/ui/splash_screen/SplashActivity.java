package com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.splash_screen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.oldmen.unsplash_gallery_dagger_tests.R;
import com.oldmen.unsplash_gallery_dagger_tests.di.SplashModule;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.splash_screen.SplashPresenter;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.splash_screen.SplashView;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.base.BaseActivity;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.MainActivity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashView {

    @Inject
    SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPresenter.loadImages();
    }

    @Override
    protected List<Object> getModules() {
        return Collections.singletonList(new SplashModule(this));
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showNoInternetMsg(DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(R.string.no_internet_msg));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), listener);
        alertDialog.show();
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
