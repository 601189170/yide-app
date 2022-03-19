package com.yyide.chatim.activity.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.attendance.fragment.SchoolAttendanceFragment;
import com.yyide.chatim.activity.attendance.fragment.TeacherStudentAttendanceFragment;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityAttendanceBinding;
import com.yyide.chatim.model.AttendanceRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;

/**
 * 校长查看全校考勤
 * update 2021年11月2日
 * author LRZ
 */
public class AttendanceActivity extends BaseActivity {

    private ActivityAttendanceBinding mViewBinding;
    private String TAG = AttendanceActivity.class.getSimpleName();

    public static void start(Context context, AttendanceRsp.DataBean.AttendanceListBean item) {
        Intent intent = new Intent(context, AttendanceActivity.class);
        intent.putExtra("item", item);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        initView();
    }

    private void initView() {
//        String type = getIntent().getStringExtra("type");
        AttendanceRsp.DataBean.AttendanceListBean item = (AttendanceRsp.DataBean.AttendanceListBean) getIntent().getSerializableExtra("item");
        mViewBinding.top.title.setText(R.string.attendance_title);
        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PRESIDENT.equals(SpData.getIdentityInfo())) {
            mViewBinding.top.tvRight.setVisibility(View.GONE);
        } else {
            mViewBinding.top.tvRight.setVisibility(View.VISIBLE);
        }

        mViewBinding.top.tvRight.setText(R.string.statistics_title);
        mViewBinding.top.tvRight.setTextColor(getResources().getColor(R.color.colorPrimary));
        mViewBinding.top.tvRight.setOnClickListener(v -> jupm(AttendanceActivity.this, StatisticsActivity.class));
        mViewBinding.top.backLayout.setOnClickListener(v -> finish());
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PRESIDENT.equals(SpData.getIdentityInfo().status)) {//校长
//            fragmentTransaction.replace(R.id.fl_content, SchoolAttendanceFragment.newInstance(item));
//        } else {
        //教师教职工 考情详情
        fragmentTransaction.replace(R.id.fl_content, TeacherStudentAttendanceFragment.newInstance(item));
//        }
        fragmentTransaction.commit();
    }
}