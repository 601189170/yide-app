package com.yyide.chatim_pro.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions3.Permission;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yyide.chatim_pro.BaseApplication;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.base.BaseActivity;
import com.yyide.chatim_pro.chat.ChatActivity;
import com.yyide.chatim_pro.model.TeacherlistRsp;
import com.yyide.chatim_pro.utils.Constants;
import com.yyide.chatim_pro.utils.GlideUtil;
import com.yyide.chatim_pro.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

public class PersonInfoActivity extends BaseActivity {

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
    @BindView(R.id.ll_address)
    LinearLayout ll_address;
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
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.iv_phone)
    ImageView iv_phone;
    @BindView(R.id.iv_phone_master)
    ImageView iv_phone_master;
    @BindView(R.id.iv_phone_vice)
    ImageView iv_phone_vice;

    @BindView(R.id.iv_head)
    ImageView iv_head;
    int type = 1;
    int type2 = 1;
    int type3 = 1;
    TeacherlistRsp.DataBean.RecordsBean bean;

    @BindView(R.id.send)
    TextView send;

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



        setData(organization);
        send.setOnClickListener(v -> {
            ChatInfo chatInfo = new ChatInfo();
            chatInfo.setType(V2TIMConversation.V2TIM_C2C);
            chatInfo.setId(String.valueOf(bean.userId));
            chatInfo.setChatName(bean.name);

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(Constants.CHAT_INFO, chatInfo);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void setData(String organization) {
        if (bean != null) {
            if ("2".equals(bean.userType) || "student".equals(organization)) {
                ll_email.setVisibility(View.GONE);
                ll_master.setVisibility(View.VISIBLE);
                ll_vice.setVisibility(View.VISIBLE);
                ll_classes.setVisibility(View.VISIBLE);
                ll_subject.setVisibility(View.GONE);
                ll_address.setVisibility(View.VISIBLE);
            } else {
                ll_email.setVisibility(View.VISIBLE);
                ll_subject.setVisibility(View.VISIBLE);
                ll_address.setVisibility(View.GONE);
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

            GlideUtil.loadImageHead(
                    this,
                    bean.avatar,iv_head

            );
            subject.setText(TextUtils.isEmpty(stringBuffer.toString()) ? "暂无科目" : stringBuffer.toString());
            sex.setText(!TextUtils.isEmpty(bean.sex) ? "1".equals(bean.sex) ? "男" : "女" : "无");
            tv_class_name.setText(TextUtils.isEmpty(bean.className) ? "未知班级" : bean.className);
            phone.setText(!TextUtils.isEmpty(bean.phone) ? setMobile(bean.phone) : "暂无手机号码");

            Log.e("TAG", "setData==>phone: "+bean.phone );
            address.setText(!TextUtils.isEmpty(bean.address) ? bean.address : "暂无住址");
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
                rxPermission(bean.phone);
                break;
            case R.id.iv_phone_master:
                rxPermission(bean.primaryGuardianPhone);
                break;
            case R.id.iv_phone_vice:
                rxPermission(bean.deputyGuardianPhone);
                break;
        }
    }

    private void rxPermission(String phone) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(granted -> {
            if (granted) {
                if (!TextUtils.isEmpty(phone)) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + phone);
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    ToastUtils.showShort("空手机号，无法拨打电话");
                }
            } else {
                // 权限被拒绝
                new AlertDialog.Builder(PersonInfoActivity.this)
                        .setTitle("提示")
                        .setMessage(R.string.permission_call_phone)
                        .setPositiveButton("开启", (dialog, which) -> {
                            Intent localIntent = new Intent();
                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                            startActivity(localIntent);
                        })
                        .setNegativeButton("取消", null)
                        .create().show();
            }
        });
    }
}
