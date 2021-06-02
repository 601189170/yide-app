package com.yyide.chatim.activity.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.attendance.fragment.TeacherStudentAttendanceFragment;
import com.yyide.chatim.activity.attendance.fragment.FamilyStudentAttendanceFragment;
import com.yyide.chatim.activity.attendance.fragment.SchoolTeacherAttendanceFragment;
import com.yyide.chatim.activity.attendance.fragment.SchoolStudentAttendanceFragment;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityAttendanceBinding;
import com.yyide.chatim.model.GetUserSchoolRsp;

public class AttendanceActivity extends BaseActivity {

    private ActivityAttendanceBinding mViewBinding;
    private String TAG = AttendanceActivity.class.getSimpleName();

    public static void start(Context context, String type) {
        Intent intent = new Intent(context, AttendanceActivity.class);
        intent.putExtra("type", type);
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
        String type = getIntent().getStringExtra("type");
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
            if ("N".equals(type)) {
                fragmentTransaction.add(R.id.fl_content, SchoolTeacherAttendanceFragment.newInstance());
            } else {
                fragmentTransaction.add(R.id.fl_content, SchoolStudentAttendanceFragment.newInstance());
            }
        } else if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
            //家长考情
            fragmentTransaction.add(R.id.fl_content, FamilyStudentAttendanceFragment.newInstance());
        } else {
            //教师教职工 考情详情
            fragmentTransaction.add(R.id.fl_content, TeacherStudentAttendanceFragment.newInstance());
        }
        fragmentTransaction.commit();
    }
}