package com.yyide.chatim.leave;


import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.OnClick;

public class LeaveActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.line1)
    TextView line1;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.line2)
    TextView line2;
    @BindView(R.id.content)
    FrameLayout content;

    @Override
    public int getContentViewID() {
        return R.layout.activity_leave_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("请假");
        setTab(0);

//        startActivity(new Intent(this, NoticeDetailActivity.class));
    }

    void setTab(int position) {
        tab1.setChecked(false);
        tab2.setChecked(false);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fg1 = fm.findFragmentByTag(String.valueOf(tab1.getId()));
        Fragment fg2 = fm.findFragmentByTag(String.valueOf(tab2.getId()));

        if (fg1 != null) ft.hide(fg1);
        if (fg2 != null) ft.hide(fg2);

        switch (position) {
            case 0:
                if (fg1 == null) {
                    fg1 = new MyLeaveFragmentByStudent();
                    ft.add(R.id.content, fg1, String.valueOf(tab1.getId()));
                } else
                    ft.show(fg1);
                tab1.setChecked(true);
                line1.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (fg2 == null) {
                    fg2 = new LeaveListFragment();
                    ft.add(R.id.content, fg2, String.valueOf(tab2.getId()));
                } else
                    ft.show(fg2);
                tab2.setChecked(true);
                line2.setVisibility(View.VISIBLE);
                break;
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back_layout, R.id.tab1, R.id.tab2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.tab1:
                setTab(0);
                break;
            case R.id.tab2:
                setTab(1);
                break;
        }
    }
}
