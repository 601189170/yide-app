package com.yyide.chatim.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tencent.mmkv.MMKV;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.dialog.BottomHeadMenuPop;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.presenter.UserPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.TakePicUtil;
import com.yyide.chatim.view.UserView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class UserActivity extends BaseMvpActivity<UserPresenter> implements UserView, OnDateSetListener {
    private static final String TAG = UserActivity.class.getSimpleName();
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.layout1)
    FrameLayout layout1;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.layout2)
    FrameLayout layout2;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.layout3)
    FrameLayout layout3;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.layout4)
    FrameLayout layout4;
    @BindView(R.id.tv_email)
    TextView email;
    @BindView(R.id.layout5)
    FrameLayout layout5;
    @BindView(R.id.face)
    TextView face;
    @BindView(R.id.layout6)
    FrameLayout layout6;
    @BindView(R.id.title)
    TextView title;

    private GetUserSchoolRsp.DataBean userInfo;
    private String classesId;
    private String realname;
    private long depId;
    private String studentId;

    @Override
    public int getContentViewID() {
        return R.layout.activity_user_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
            title.setText("学生信息");
        } else {
            title.setText("我的信息");
        }
        initData();
//        classesId = SpData.getIdentityInfo().classesId;
        if (!SpData.getIdentityInfo().staffIdentity()) {
            final List<GetUserSchoolRsp.DataBean.FormBean> form = SpData.getIdentityInfo().form;
            if (!form.isEmpty()) {
                final GetUserSchoolRsp.DataBean.FormBean formBean = form.get(0);
                try {
                    studentId = formBean.studentId;
                    classesId = formBean.classesId;
                } catch (NumberFormatException exception) {
                    Log.e(TAG, "studentId=" + formBean.studentId);
                }
            }
        }
        realname = SpData.getIdentityInfo().realname;
        depId = SpData.getIdentityInfo().teacherDepId;
        Log.e(TAG, "getFaceData: name=" + realname + ",classId=" + classesId);
    }

    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    private void initData() {
        userInfo = SpData.getIdentityInfo();
        if (userInfo != null) {
            GlideUtil.loadImageHead(this, userInfo.img, img);
            sex.setText(!TextUtils.isEmpty(userInfo.sex) ? ("1".equals(userInfo.sex) ? "男" : "女") : "未设置");
            phone.setText(!TextUtils.isEmpty(userInfo.username) ? userInfo.username : "未设置");
            date.setText(!TextUtils.isEmpty(userInfo.birthdayDate) ? userInfo.birthdayDate : "未设置");
            email.setText(!TextUtils.isEmpty(userInfo.email) ? userInfo.email : "未设置");
            face.setText("未设置");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mvpPresenter.getFaceData(realname, classesId, depId, studentId);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5, R.id.layout6, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout1://头像
                new BottomHeadMenuPop(this);
                break;
            case R.id.layout2://手机号
                //startActivity(new Intent(this, CheckPhoneActivity.class));
                break;
            case R.id.layout3://性别
//                Intent intent1 = new Intent(this, SexActivity.class);
//                String sexStr = sex.getText().toString().trim();
//                intent1.putExtra("sex", !"未设置".equals(sexStr) ? sexStr : "");
//                startActivity(intent1);
                break;
            case R.id.layout4://生日
//                showTime();
                break;
            case R.id.layout5://邮箱Email
                Intent intent = new Intent(this, EmailActivity.class);
                String emailStr = email.getText().toString().trim();
                intent.putExtra("email", !"未设置".equals(emailStr) ? emailStr : "");
                startActivity(intent);
                break;
            case R.id.layout6://设置人脸
                //startActivity(new Intent(this, FaceCaptureActivity.class));
                startActivityForResult(new Intent(this, FaceCaptureActivity.class), 1);
                break;
            case R.id.back_layout:
                finish();
                break;
            default:
                break;
        }
    }

    private TimePickerDialog mDialogAll;

    private void showTime() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        long tenYears2 = 50L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择日期")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis() - tenYears2)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.text_212121))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorPrimary))
                .setWheelItemTextSize(12)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "all");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_USER_EMAIL.equals(messageEvent.getCode())) {
            email.setText(messageEvent.getMessage());
            userInfo.email = messageEvent.getMessage();
            SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(userInfo));
            updateInfo();
        } else if (BaseConstant.TYPE_UPDATE_USER_SEX.equals(messageEvent.getCode())) {
            if ("1".equals(messageEvent.getMessage())) {//男
                sex.setText("男");
                userInfo.sex = "1";
            } else if ("0".equals(messageEvent.getMessage())) {//女
                sex.setText("女");
                userInfo.sex = "0";
            }
            SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(userInfo));
            updateInfo();
        }
    }

    private void updateInfo() {
        if (userInfo != null) {
            mvpPresenter.update(userInfo.userId + "",
                    TextUtils.isEmpty(userInfo.sex) ? "" : userInfo.sex,
                    TextUtils.isEmpty(userInfo.birthdayDate) ? "" : userInfo.birthdayDate,
                    TextUtils.isEmpty(userInfo.email) ? "" : userInfo.email);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File corpFile = TakePicUtil.onActivityResult(this, requestCode, resultCode, data);
        if (corpFile != null) {
            showPicFileByLuban(corpFile);
        }

    }

    private void showPicFileByLuban(@NonNull File file) {
        Luban.with(this)
                .load(file)
                .ignoreBy(100)
                //.putGear(Luban.THIRD_GEAR)//压缩等级
                .setTargetDir(Environment.getExternalStorageDirectory().getAbsolutePath())
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        showLoading();
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        mvpPresenter.uploadFile(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        hideLoading();
                        ToastUtils.showShort("图片压缩失败请重试");
                    }
                }).launch();
    }


    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void updateSuccess(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void updateFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void uploadFileSuccess(String imgUrl) {
        if (userInfo != null) {
            userInfo.img = imgUrl;
            SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(userInfo));
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_IMG, imgUrl));
        }
        GlideUtil.loadImageHead(this, imgUrl, img);
    }

    @Override
    public void uploadFileFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void getFaceDataSuccess(FaceOssBean faceOssBean) {
        Log.e(TAG, "getFaceDataSuccess: " + faceOssBean.toString());
        if (faceOssBean.getCode() == 200) {
            if (faceOssBean.getData() != null) {
                FaceOssBean.DataBean data = faceOssBean.getData();
                final String status = data.getStatus();
                face.setText(status);
            }
        }
    }

    @Override
    public void getFaceDataFail(String msg) {

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        date.setText(DateUtils.stampToDate(millseconds));
        userInfo.birthdayDate = DateUtils.stampToDate(millseconds);
        SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(userInfo));
        updateInfo();
    }
}
