/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dimon.ganwumei.ui.newsfeed.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.ui.base.BaseActivity;
import com.dimon.ganwumei.util.ToastUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片预览Activity
 */
public class PictureActivity extends BaseActivity {

    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    public static final String TRANSIT_PIC = "picture";

    @Bind(R.id.picture) ImageView mImageView;

    PhotoViewAttacher mPhotoViewAttacher;
    String mImageUrl, mImageTitle;

    public static Intent newIntent(Context context, String url, String desc) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_URL, url);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_TITLE, desc);
        return intent;
    }


    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        mImageTitle = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        parseIntent();
        // init image view
        ViewCompat.setTransitionName(mImageView, TRANSIT_PIC);
        Picasso.with(this).load(mImageUrl).into(mImageView);
        // set up app bar
        setTitle(mImageTitle);
        setupPhotoAttacher();
    }
    private void setupPhotoAttacher() {
        mPhotoViewAttacher = new PhotoViewAttacher(mImageView);
        mPhotoViewAttacher.setOnViewTapListener((view, v, v1) -> onBackPressed());
        mPhotoViewAttacher.setOnLongClickListener(v -> {
            new AlertDialog.Builder(PictureActivity.this)
                    .setMessage(getString(R.string.ask_saving_picture))
                    .setNegativeButton(android.R.string.cancel,
                            (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(android.R.string.ok,
                            (dialog, which) -> {
                                saveImageToGallery();
//                                TODO：保存图片逻辑
                                dialog.dismiss();
                            })
                    .show();
            return true;
        });
    }
    private void saveImageToGallery() {
        ToastUtils.showShort(R.string.saved);
    }
    @Override public void onResume() {
        super.onResume();
    }


    @Override public void onPause() {
        super.onPause();
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
    }
}
