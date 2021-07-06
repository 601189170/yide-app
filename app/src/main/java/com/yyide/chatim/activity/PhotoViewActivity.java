package com.yyide.chatim.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityPhotoViewBinding;

public class PhotoViewActivity extends BaseActivity {
    private static final String TAG = "PhotoViewActivity";
    private ActivityPhotoViewBinding viewBinding;

    @Override
    public int getContentViewID() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String path = getIntent().getStringExtra("path");
        Log.e(TAG, "显示图片: " + path);
        viewBinding = ActivityPhotoViewBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewBinding.top.title.setText(R.string.look_big_image);
        viewBinding.top.backLayout.setOnClickListener((v)->{
            finish();
        });
        Glide.with(this).load(path).into(viewBinding.photoView);

    }
}