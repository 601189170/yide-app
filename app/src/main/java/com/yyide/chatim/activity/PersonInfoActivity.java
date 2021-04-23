package com.yyide.chatim.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonInfoActivity extends BaseActivity {

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
    @BindView(R.id.set_master)
    TextView set_master;
    @BindView(R.id.set_vice)
    TextView set_vice;
    @BindView(R.id.ll_master)
    LinearLayout ll_master;
    @BindView(R.id.ll_vice)
    LinearLayout ll_vice;
    @BindView(R.id.ll_classes)
    LinearLayout ll_classes;
    @BindView(R.id.ll_email)
    LinearLayout ll_email;
    @BindView(R.id.ll_subject)
    LinearLayout ll_subject;
    @BindView(R.id.tv_master_phone)
    TextView tv_master_phone;
    @BindView(R.id.tv_vice_phone)
    TextView tv_vice_phone;
    @BindView(R.id.tv_class_name)
    TextView tv_class_name;
    @BindView(R.id.tv_name_title)
    TextView tv_name_title;
    @BindView(R.id.iv_phone)
    ImageView iv_phone;
    @BindView(R.id.iv_phone_master)
    ImageView iv_phone_master;
    @BindView(R.id.iv_phone_vice)
    ImageView iv_phone_vice;
    int type = 1;
    int type2 = 1;
    int type3 = 1;

    @Override
    public int getContentViewID() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data = getIntent().getStringExtra("data");
        String organization = getIntent().getStringExtra("organization");
        title.setText("个人信息");
        bean = JSON.parseObject(data, TeacherlistRsp.DataBean.RecordsBean.class);
        Log.e("TAG", "PersonInfoActivity: " + JSON.toJSONString(bean));
        if (bean != null) {
            if ((!TextUtils.isEmpty(bean.userType) && !"2".equals(bean.userType)) || !"staff".equals(organization)) {
                ll_email.setVisibility(View.GONE);
                ll_master.setVisibility(View.VISIBLE);
                ll_vice.setVisibility(View.VISIBLE);
                ll_classes.setVisibility(View.VISIBLE);
                ll_subject.setVisibility(View.GONE);
            } else {
                ll_email.setVisibility(View.VISIBLE);
                ll_subject.setVisibility(View.VISIBLE);
                ll_master.setVisibility(View.GONE);
                ll_vice.setVisibility(View.GONE);
                ll_classes.setVisibility(View.GONE);
            }
            tv_name_title.setText(StringUtils.subString(bean.name, 2));
            topname.setText(bean.name);
            name.setText(bean.name);
            adress.setText(TextUtils.isEmpty(bean.address) ? "暂无邮箱" : bean.address);
            StringBuffer stringBuffer = new StringBuffer();
            if (bean.subjects != null && bean.subjects.size() > 0) {
                for (int i = 0; i < bean.subjects.size(); i++) {
                    if (bean.subjects.get(i) != null) {
                        if (i == (bean.subjects.size() - 1)) {
                            stringBuffer.append(bean.subjects.get(i).subjectName);
                        } else {
                            stringBuffer.append(bean.subjects.get(i).subjectName + "、");
                        }
                    }
                }
            }
            subject.setText(TextUtils.isEmpty(stringBuffer.toString()) ? "暂无科目" : stringBuffer.toString());
            sex.setText(!TextUtils.isEmpty(bean.sex) ? "0".equals(bean.sex) ? "男" : "女" : "无");
            tv_class_name.setText(TextUtils.isEmpty(bean.classesName) ? "未知班级" : bean.classesName);
            phone.setText(!TextUtils.isEmpty(bean.phone) ? setMobile(bean.phone) : "暂无手机号码");
            if (TextUtils.isEmpty(bean.phone)) {
                iv_phone.setVisibility(View.GONE);
                set.setVisibility(View.GONE);
            }
            tv_master_phone.setText(!TextUtils.isEmpty(bean.primaryGuardianPhone) ? setMobile(bean.primaryGuardianPhone) : "暂无手机号码");//主监护人
            if (TextUtils.isEmpty(bean.primaryGuardianPhone)) {
                set_master.setVisibility(View.GONE);
                iv_phone_master.setVisibility(View.GONE);
            }
            tv_vice_phone.setText(!TextUtils.isEmpty(bean.deputyGuardianPhone) ? setMobile(bean.deputyGuardianPhone) : "暂无手机号码");//副监护人
            if (TextUtils.isEmpty(bean.deputyGuardianPhone)) {
                set_vice.setVisibility(View.GONE);
                iv_phone_vice.setVisibility(View.GONE);
            }
        }
        set.setText("显示");
        set.setOnClickListener(v -> {
            if (type == 1) {
                type = 2;
                set.setText("隐藏");
                phone.setText(bean.phone);
            } else {
                type = 1;
                set.setText("显示");
                phone.setText(setMobile(bean.phone));
            }
        });

        set_master.setOnClickListener(v -> {
            if (type2 == 1) {
                type2 = 2;
                set_master.setText("隐藏");
                tv_master_phone.setText(bean.primaryGuardianPhone);
            } else {
                type2 = 1;
                set_master.setText("显示");
                tv_master_phone.setText(setMobile(bean.primaryGuardianPhone));
            }
        });

        set_vice.setOnClickListener(v -> {
            if (type3 == 1) {
                type3 = 2;
                set_vice.setText("隐藏");
                tv_vice_phone.setText(bean.deputyGuardianPhone);
            } else {
                type3 = 1;
                set_vice.setText("显示");
                tv_vice_phone.setText(setMobile(bean.deputyGuardianPhone));
            }
        });
    }

    public static String setMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile)) {
            if (mobile.length() >= 11)
                return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        } else {
            return "";
        }
        return mobile;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.back_layout, R.id.iv_phone, R.id.iv_phone_master, R.id.iv_phone_vice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.iv_phone:
                if (!TextUtils.isEmpty(bean.phone)) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + bean.phone);
                    intent.setData(data);
                    startActivity(intent);
                }
                break;
            case R.id.iv_phone_master:
                if (!TextUtils.isEmpty(bean.primaryGuardianPhone)) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + bean.phone);
                    intent.setData(data);
                    startActivity(intent);
                }
                break;
            case R.id.iv_phone_vice:
                if (!TextUtils.isEmpty(bean.deputyGuardianPhone)) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + bean.phone);
                    intent.setData(data);
                    startActivity(intent);
                }
                break;
        }
    }
}
