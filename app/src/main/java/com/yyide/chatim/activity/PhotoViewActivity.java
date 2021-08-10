package com.yyide.chatim.activity;

import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityClassPhotoViewBinding;

public class PhotoViewActivity extends BaseActivity {
    private static final String TAG = "PhotoViewActivity";
    private ActivityClassPhotoViewBinding viewBinding;

    @Override
    public int getContentViewID() {
        return R.layout.activity_class_photo_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String path = getIntent().getStringExtra("path");
        Log.e(TAG, "显示图片: " + path);
        viewBinding = ActivityClassPhotoViewBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewBinding.top.title.setText(R.string.look_big_image);
        viewBinding.top.backLayout.setOnClickListener((v) -> {
            finish();
        });
        Glide.with(this).load(path).into(viewBinding.photoView);

    }
}