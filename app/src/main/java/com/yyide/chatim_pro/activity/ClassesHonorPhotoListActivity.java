package com.yyide.chatim_pro.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ClassesHonorPhotoListActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @Override
    public int getContentViewID() {
        return R.layout.activity_classes_honor_photo_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Integer> list = new ArrayList<>();
        title.setText("班级荣誉");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseQuickAdapter<Integer, BaseViewHolder> baseQuickAdapter = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout.item_classes_honor_img) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, Integer index) {
                ImageView img = holder.getView(R.id.iv_img);
                //设置图片圆角角度
                RoundedCorners roundedCorners = new RoundedCorners(6);
                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                Glide.with(ClassesHonorPhotoListActivity.this).load(getResources().getDrawable(index)).apply(options).into(img);
            }
        };
        mRecyclerView.setAdapter(baseQuickAdapter);
        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            PhotoBrowseActivity.startWithElement(ClassesHonorPhotoListActivity.this, list, position, view);
        });

        baseQuickAdapter.setList(list);
    }

    @OnClick(R.id.back_layout)
    void click() {
        finish();
    }

}