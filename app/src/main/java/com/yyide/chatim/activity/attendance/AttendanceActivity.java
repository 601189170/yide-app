package com.yyide.chatim.activity.attendance;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.attendance.fragment.AttendanceTeachingFragment;
import com.yyide.chatim.activity.attendance.fragment.FamilyHeadFragment;
import com.yyide.chatim.activity.attendance.fragment.SchoolMasterAttendanceFragment;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityAttendanceBinding;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;

public class AttendanceActivity extends BaseActivity {

    private ActivityAttendanceBinding mViewBinding;
    private String TAG = AttendanceActivity.class.getSimpleName();

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
        mViewBinding.top.title.setText(R.string.attendance_title);
        mViewBinding.top.tvRight.setVisibility(View.GONE);
        mViewBinding.top.tvRight.setText(R.string.statistics_title);
        mViewBinding.top.tvRight.setTextColor(getResources().getColor(R.color.colorPrimary));
        mViewBinding.top.tvRight.setOnClickListener(v -> jupm(AttendanceActivity.this, StatisticsActivity.class));
        mViewBinding.top.backLayout.setOnClickListener(v -> finish());
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PRESIDENT.equals(SpData.getIdentityInfo().status)) {//校长
            //校长考情展示
            fragmentTransaction.add(R.id.fl_content, SchoolMasterAttendanceFragment.newInstance());
        } else if(SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
            fragmentTransaction.add(R.id.fl_content, FamilyHeadFragment.newInstance());
        } else {
            //教师家长教职工 考情详情
            fragmentTransaction.add(R.id.fl_content, AttendanceTeachingFragment.newInstance());
        }
        fragmentTransaction.commit();
    }
}