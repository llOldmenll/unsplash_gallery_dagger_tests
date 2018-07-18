package com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.base;

import android.content.DialogInterface;

public interface BaseView {

    void showNoInternetMsg(DialogInterface.OnClickListener listener);

    void showErrorMsg(String msg);

}
