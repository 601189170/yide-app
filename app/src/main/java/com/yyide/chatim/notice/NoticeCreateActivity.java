package com.yyide.chatim.notice;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.notice.presenter.NoticeCreatePresenter;
import com.yyide.chatim.notice.view.NoticeCreateView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeCreateActivity extends BaseMvpActivity<NoticeCreatePresenter> implements NoticeCreateView {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.cl_timing_time)
    ConstraintLayout cl_timing_time;
    @BindView(R.id.switch1)
    Switch mSwitch;
    @BindView(R.id.tv_parents)
    TextView tv_parents;
    @BindView(R.id.tv_faculty)
    TextView tv_faculty;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_create;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_create_title);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cl_timing_time.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    @OnClick({R.id.tv_parents, R.id.tv_faculty, R.id.cl_timing_time})
    public void click(View view){
        switch (view.getId()){
            case R.id.tv_parents:
                tv_parents.setTextColor(Color.parseColor("#FFFFFF"));
                tv_parents.setBackground(getDrawable(R.drawable.btn_select_bg));
                tv_faculty.setTextColor(Color.parseColor("#666666"));
                tv_faculty.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                break;
            case R.id.tv_faculty:
                tv_parents.setTextColor(Color.parseColor("#666666"));
                tv_parents.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                tv_faculty.setTextColor(Color.parseColor("#FFFFFF"));
                tv_faculty.setBackground(getDrawable(R.drawable.btn_select_bg));
                break;
            case R.id.cl_timing_time://选择发布时间

                break;
        }
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    @OnClick(R.id.constraintLayout_select)
    public void select(){
        jupm(this, NoticeScopeActivity.class);
    }

    @Override
    protected NoticeCreatePresenter createPresenter() {
        return new NoticeCreatePresenter(this);
    }

    @Override
    public void showError() {

    }
}