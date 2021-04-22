package com.yyide.chatim.activity.notice;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.jzxiang.pickerview.TimePickerDialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.activity.notice.presenter.NoticeCreatePresenter;
import com.yyide.chatim.activity.notice.view.NoticeCreateView;
import com.yyide.chatim.model.AddUserAnnouncementBody;
import com.yyide.chatim.model.AddUserAnnouncementResponse;
import com.yyide.chatim.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeCreateActivity extends BaseMvpActivity<NoticeCreatePresenter> implements NoticeCreateView, OnDateSetListener {
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
    @BindView(R.id.tv_student)
    TextView tv_student;
    @BindView(R.id.et_input_title)
    EditText et_input_title;//标题
    @BindView(R.id.et_input_before_class)
    EditText et_input_content;//内容
   @BindView(R.id.tv_show_ids)
    TextView tv_show_ids;//内容
    @BindView(R.id.tv_show_timed_time)
    TextView tv_show_timed_time;//显示定时的时间

    private static int REQUEST_CODE = 100;

    private int sendObj = 0;//发送对象 ： 1家长 2学生 3部门
    private String timingTime = "";
    List<String> departmentIds = new ArrayList();
    List<String> classCardIds = new ArrayList();
    List<String> classesIds = new ArrayList();
    private static final String TAG = "NoticeCreateActivity";

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

        Intent intent = getIntent();
        boolean template = intent.getBooleanExtra("template", false);
        if (template){
            //表示从模板过来
            String name = intent.getStringExtra("name");
            String content = intent.getStringExtra("content");
            et_input_title.setText(name);
            et_input_content.setText(Html.fromHtml(content));
        }
    }

    @OnClick({R.id.tv_parents, R.id.tv_faculty,R.id.tv_student, R.id.cl_timing_time, R.id.ll_bottom})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_parents:
                sendObj = 1;
               showCheckedNumber(classesIds);
                tv_parents.setTextColor(Color.parseColor("#FFFFFF"));
                tv_parents.setBackground(getDrawable(R.drawable.btn_select_bg));
                tv_faculty.setTextColor(Color.parseColor("#666666"));
                tv_faculty.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                tv_student.setTextColor(Color.parseColor("#666666"));
                tv_student.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                break;
            case R.id.tv_faculty:
                sendObj = 3;
                showCheckedNumber(departmentIds);
                tv_parents.setTextColor(Color.parseColor("#666666"));
                tv_parents.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                tv_student.setTextColor(Color.parseColor("#666666"));
                tv_student.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                tv_faculty.setTextColor(Color.parseColor("#FFFFFF"));
                tv_faculty.setBackground(getDrawable(R.drawable.btn_select_bg));
                break;
            case R.id.tv_student:
                sendObj = 2;
                showCheckedNumber(classesIds);
                tv_parents.setTextColor(Color.parseColor("#666666"));
                tv_parents.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                tv_faculty.setTextColor(Color.parseColor("#666666"));
                tv_faculty.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                tv_student.setTextColor(Color.parseColor("#FFFFFF"));
                tv_student.setBackground(getDrawable(R.drawable.btn_select_bg));
                break;
            case R.id.cl_timing_time://选择发布时间
                showTime();
                break;
            case R.id.ll_bottom://提交公告
                commit();
                break;
            default:
                break;
        }
    }

    /**
     * type : 3
     * title : 这是标题
     * content : 内容1 内容2
     * equipmentType : 1
     * sendTarget : 1
     * departmentIds : ["430"]
     * classesIds : []
     * classCardIds : []
     * isTiming : false
     * timingTime :
     */
    private void commit() {
        String title = et_input_title.getText().toString();
        if (TextUtils.isEmpty(title)){
            ToastUtils.showShort("标题不能为空，请输入标题！");
            return;
        }
        String content = et_input_content.getText().toString();
        if (TextUtils.isEmpty(content)){
            ToastUtils.showShort("通知内容不能为空，请输入内容！");
            return;
        }
        boolean isTiming = mSwitch.isChecked();
        if (isTiming && TextUtils.isEmpty(timingTime)){
            ToastUtils.showShort("请选择发布时间");
            return;
        }
        AddUserAnnouncementBody body = new AddUserAnnouncementBody();
        body.setType("3");
        body.setTitle(title);
        body.setContent(content);
        body.setEquipmentType("1");
        body.setSendTarget(""+sendObj);
        body.setDepartmentIds(departmentIds);
        body.setClassCardIds(classCardIds);
        body.setClassesIds(classesIds);
        body.setIsTiming(isTiming);
        int schoolId = SpData.getIdentityInfo().schoolId;
        body.setSchoolId(schoolId);
        if (isTiming){
            body.setTimingTime(timingTime);
        }
        if (classesIds.isEmpty() && departmentIds.isEmpty() && classCardIds.isEmpty()){
            ToastUtils.showShort("请选择通知范围");
            return;
        }
        String jsonString = JSON.toJSONString(body);
        Log.e(TAG, "commit: " + jsonString);
        mvpPresenter.addUserAnnouncement(jsonString);
    }

    private TimePickerDialog mDialogAll;

    private void showTime() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择日期时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.text_212121))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorPrimary))
                .setWheelItemTextSize(12)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "all");
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    @OnClick(R.id.constraintLayout_select)
    public void select() {
        if (sendObj == 0){
            ToastUtils.showShort("请选择通知对象！");
            return;
        }
        Intent intent = new Intent(this,NoticeScopeActivity.class);
        intent.putExtra("sendObj",sendObj);
        startActivityForResult(intent,REQUEST_CODE);
        //jupm(this, NoticeScopeActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode:"+requestCode+", resultCode:"+resultCode );
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (data == null){
                return;
            }
            ArrayList<String> ids = data.getStringArrayListExtra("ids");
            Log.e(TAG, "onActivityResult: "+ids );
            departmentIds.clear();
            classCardIds.clear();
            classesIds.clear();
            if (sendObj == 1) {
                classesIds.addAll(ids);
            } else if (sendObj == 2) {
                classCardIds.addAll(ids);
            } else {
                departmentIds.addAll(ids);
            }
            if (ids.isEmpty()){
                tv_show_ids.setText("未选择");
            }else {
                tv_show_ids.setText("已选择("+ids.size()+")");
            }
        }
    }

    private void showCheckedNumber(List<String> ids){
        if (ids.isEmpty()){
            tv_show_ids.setText("未选择");
        }else {
            tv_show_ids.setText("已选择("+ids.size()+")");
        }
    }

    private String list2String(ArrayList<String> list){
        StringBuffer buffer = new StringBuffer();
        for (String s : list) {
            buffer.append(s+" ");
        }
        return buffer.toString();
    }

    @Override
    protected NoticeCreatePresenter createPresenter() {
        return new NoticeCreatePresenter(this);
    }

    @Override
    public void showError() {

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = Long.valueOf(millseconds);
        timingTime = simpleDateFormat.format(new Date(time));
        tv_show_timed_time.setText(timingTime);
    }

    @Override
    public void addUserAnnouncement(AddUserAnnouncementResponse addUserAnnouncementResponse) {
        if (addUserAnnouncementResponse.getCode() == 200) {
            //String success = addUserAnnouncementResponse.getMsg();
            ToastUtils.showShort("发送成功");
            finish();
            return;
        }
        ToastUtils.showShort(addUserAnnouncementResponse.getMsg());
    }

    @Override
    public void addUserAnnouncementFail(String msg) {
        ToastUtils.showShort(msg);
    }
}