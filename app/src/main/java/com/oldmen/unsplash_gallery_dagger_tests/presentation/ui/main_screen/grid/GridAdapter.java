package com.oldmen.unsplash_gallery_dagger_tests.presentation.ui.main_screen.grid;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.oldmen.unsplash_gallery_dagger_tests.R;
import com.oldmen.unsplash_gallery_dagger_tests.domain.ImageUnsplash;
import com.oldmen.unsplash_gallery_dagger_tests.presentation.mvp.main_screen.grid.GridPresenter;
import com.oldmen.unsplash_gallery_dagger_tests.utils.GlideApp;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridHolder> {
    private Fragment mFragment;
    private List<ImageUnsplash> mImages;
    private GridListener mListener;

    GridAdapter(Fragment fragment, List<ImageUnsplash> imagesInfo) {
        mFragment = fragment;
        mImages = imagesInfo;
    }

    @NonNull
    @Override
    public GridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_grid_item, parent, false);
        GridHolder holder = new GridHolder(view);
        holder.itemView.setOnClickListener(v ->
                mListener.imageClicked(holder.getAdapterPosition(), new WeakReference<>(holder.mImageView)));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GridHolder holder, int position) {
        holder.bindView(mImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public void setGridListener(GridListener listener) {
        mListener = listener;
    }

    void update(int currentPage, List<ImageUnsplash> images) {
        List<ImageUnsplash> imagesList;

        if (currentPage <= 1) {
            mImages.clear();
            mImages.addAll(images);
            notifyDataSetChanged();
        } else if (images.size() > mImages.size()) {
            imagesList = images.subList(mImages.size(), images.size() - 1);
            mImages.addAll(imagesList);
            notifyItemRangeChanged(mImages.size() - 1, imagesList.size());
        }
    }

    class GridHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_grid_item)
        ImageView mImageView;
        @BindView(R.id.author_name_grid_item)
        TextView mAuthor;

        GridHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(ImageUnsplash imgInfo) {
            if (mFragment.getContext() != null)
                GlideApp.with(mFragment.getContext())
                        .load(imgInfo.getSmallImgUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail()
                        .dontAnimate()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                        Target<Drawable> target, boolean isFirstResource) {
                                mFragment.startPostponedEnterTransition();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                                    target, DataSource dataSource, boolean isFirstResource) {
                                mFragment.startPostponedEnterTransition();
                                return false;
                            }
                        })
                        .into(mImageView);

            if (imgInfo.getAuthorName() != null) mAuthor.setText(imgInfo.getAuthorName());
            else mAuthor.clearComposingText();

            ViewCompat.setTransitionName(mImageView, imgInfo.getSmallImgUrl());
        }
    }

    public interface GridListener {
        void imageClicked(int position, WeakReference<ImageView> imageViewWR);
    }
}
