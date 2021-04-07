package com.yyide.chatim.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.model.TeacherlistRsp;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonInfoActivity extends BaseActivity {


    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.adress)
    TextView adress;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.subject)
    TextView subject;
    TeacherlistRsp.DataBean.RecordsBean bean;
    @BindView(R.id.topname)
    TextView topname;
    @BindView(R.id.set)
    TextView set;
    int type=1;
    @Override
    public int getContentViewID() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data = getIntent().getStringExtra("data");
        bean = JSON.parseObject(data, TeacherlistRsp.DataBean.RecordsBean.class);
        Log.e("TAG", "PersonInfoActivity: " + JSON.toJSONString(bean));
        title.setText("个人信息");
        if (bean != null) {
            topname.setText(bean.name);
            name.setText(bean.name);
            adress.setText(bean.address);
            StringBuffer stringBuffer = new StringBuffer();
            for (TeacherlistRsp.DataBean.RecordsBean.SubjectsBean subjectsBean : bean.subjects) {
                stringBuffer.append(subjectsBean.id);
            }
            subject.setText(stringBuffer.toString());
            phone.setText(bean.phone);
            sex.setText(bean.sex);
        }
        set.setText("显示");
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type==1){
                    type=2;
                    set.setText("隐藏");
                }else {
                    type=1;
                    set.setText("显示");
                }
            }
        });
    }
    void setData(int type){
        if (type==1){
            String dh=bean.phone;
            String p = dh.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
            phone.setText(p);
        }else {
            String dh=bean.phone;
            phone.setText(dh);
        }

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }
}
