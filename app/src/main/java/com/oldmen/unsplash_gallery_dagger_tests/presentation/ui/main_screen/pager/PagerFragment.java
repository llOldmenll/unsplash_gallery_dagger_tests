package com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.pager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.oldmen.unsplash_gallery_dagger_tests.R;
import com.oldmen.unsplash_gallery_dagger_tests.di.PagerModule;
import com.oldmen.unsplash_gallery_dagger_tests.domain.ImageUnsplash;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.pager.PagerPresenter;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.pager.PagerView;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.base.BaseFragment;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.pager.image.ImageFragment;
import com.oldmen.unsplash_gallery_dagger_tests.utils.GlideApp;
import com.oldmen.unsplash_gallery_dagger_tests.utils.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.oldmen.unsplash_gallery_dagger_tests.utils.Constants.EXTRA_IMAGES_INFO_LIST;

public class PagerFragment extends BaseFragment implements PagerView, ImageFragment.ImagePagerListener,
        PopupMenu.OnMenuItemClickListener {
    @Inject
    PagerPresenter mPresenter;
    @BindView(R.id.viewpager_pager)
    ViewPager mPager;
    @BindView(R.id.author_avatar_pager)
    ImageView mAuthorAvatar;
    @BindView(R.id.author_name_pager)
    TextView mAuthorName;
    @BindView(R.id.btn_download_pager)
    ImageButton mBtnDownload;
    @BindView(R.id.btn_share_pager)
    ImageButton mBtnShare;
    @BindView(R.id.footer_pager)
    ConstraintLayout mFooter;

    private Unbinder unbinder;
    private ArrayList<ImageUnsplash> mImages;
    private ImagePagerAdapter mPagerAdapter;
    private boolean mIsDownloading;
    private Context mContext;

    public static PagerFragment newInstance(Bundle args) {
        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(EXTRA_IMAGES_INFO_LIST))
                mImages = getArguments().getParcelableArrayList(EXTRA_IMAGES_INFO_LIST);
        }

    }

    @Override
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new PagerModule(this));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPager();
        initFooter();
        prepareSharedElementTransition();
        if (savedInstanceState == null) postponeEnterTransition();
        mPresenter.getImagesFromDb();
        updateAuthorInfo(mPresenter.getCurrentPosition());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initPager() {
        mPagerAdapter = new ImagePagerAdapter(this, mImages);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(5);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setCurrentItem(mPresenter.getCurrentPosition());
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mPresenter.saveCurrentPosition(position);
                updateAuthorInfo(position);
                if (mImages.size() - position <= 4 && !mIsDownloading) {
                    mIsDownloading = true;
                    mPresenter.loadImages();
                }
            }
        });

        mPager.setOnClickListener(v -> {
            if (mAuthorAvatar.getY() == 0F)
                hideFooter();
            else
                showFooter();
        });
    }

    private void initFooter() {
        mBtnShare.setOnClickListener(v ->
                shareLink(mImages.get(mPresenter.getCurrentPosition()).getShareLink()));
        mBtnDownload.setOnClickListener(v -> {
            ImageUnsplash img = mImages.get(mPresenter.getCurrentPosition());
            showLoadPopUp(img.getWidth(), img.getHeight(), v);
        });
    }

    private void prepareSharedElementTransition() {
        Transition transition =
                TransitionInflater.from(mContext)
                        .inflateTransition(R.transition.image_shared_element_transition);
        setSharedElementEnterTransition(transition);
        setEnterSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        Fragment currentFragment = (Fragment) mPagerAdapter
                                .instantiateItem(mPager, mPresenter.getCurrentPosition());
                        View view = currentFragment.getView();
                        if (view == null) return;
                        sharedElements.put(names.get(0), view.findViewById(R.id.image));
                    }
                });
    }

    private void updateAuthorInfo(int position) {
        ImageUnsplash image = mImages.get(position);
        mAuthorName.setText(image.getAuthorName());
        GlideApp.with(this)
                .load(image.getAuthorAvatar())
                .circleCrop()
                .into(mAuthorAvatar);
    }

    private void shareLink(String link) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.shareLink)));
    }

    private void showLoadPopUp(int width, int height, View view) {
        if (mContext != null) {
            Menu menu;
            PopupMenu loadPopUp = new PopupMenu(mContext, view);
            loadPopUp.setOnMenuItemClickListener(this);
            loadPopUp.inflate(R.menu.pop_up_img_load_menu);
            menu = loadPopUp.getMenu();
            menu.findItem(R.id.load_400).setTitle(String.format(getString(R.string.size_400_format),
                    (int) ((double) 400 / width * height)));
            menu.findItem(R.id.load_1080).setTitle(String.format(getString(R.string.size_1080_format),
                    (int) ((double) 1080 / width * height)));
            menu.findItem(R.id.load_original).setTitle(String.format(getString(R.string.original_size_format),
                    width, height));
            loadPopUp.show();
        }
    }

    private void loadImageWithGlide(String url, String name) {
        GlideApp.with(mContext)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        checkPermision(resource, name);
                    }
                });
    }

    private void checkPermision(Bitmap finalBitmap, String name) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (getActivity() != null)
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            mPresenter.saveImage(finalBitmap, name);
        }
    }

    @Override
    public void showNoInternetMsg(DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setMessage(getString(R.string.no_internet_msg));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), listener);
        alertDialog.show();
        mIsDownloading = false;
    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void updatePager(List<ImageUnsplash> images) {
        mPagerAdapter.update(images);
        mIsDownloading = false;
    }

    @Override
    public void hideFooter() {
        mFooter.animate()
                .translationY(mFooter.getHeight())
                .setDuration(200);
    }

    @Override
    public void showFooter() {
        mFooter.animate().translationY(0).setDuration(200);
    }

    @Override
    public void showImageLoadingMsg(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void imageClicked() {
        mPresenter.changeFooterState();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int position = mPresenter.getCurrentPosition();
        ImageUnsplash image = mImages.get(position);
        switch (item.getItemId()) {
            case R.id.load_400:
                loadImageWithGlide(image.getSmallImgUrl(), image.getImgId() + "_400");
                return true;
            case R.id.load_1080:
                loadImageWithGlide(image.getRegularImgUrl(), image.getImgId() + "_1080");
                return true;
            case R.id.load_original:
                loadImageWithGlide(image.getFullImgUrl(), image.getImgId());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) mBtnDownload.callOnClick();
    }
}
