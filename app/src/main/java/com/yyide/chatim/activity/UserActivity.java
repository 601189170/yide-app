package com.yyide.chatim.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.dialog.BottomHeadMenuPop;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.presenter.UserPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.UserView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class UserActivity extends BaseMvpActivity<UserPresenter> implements UserView, OnDateSetListener {

    @BindView(R.id.back)
    TextView back;
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

    @Override
    public int getContentViewID() {
        return R.layout.activity_user_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        title.setText("我的信息");
        initData();
    }

    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    private void initData() {
        userInfo = SpData.getIdentityInfo();
        if (userInfo != null) {
            GlideUtil.loadImage(this, userInfo.img, img);
            sex.setText(!TextUtils.isEmpty(userInfo.sex) ? ("0".equals(userInfo.sex) ? "男" : "女") : "未设置");
            phone.setText(!TextUtils.isEmpty(userInfo.username) ? userInfo.username : "未设置");
            date.setText(!TextUtils.isEmpty(userInfo.birthdayDate) ? userInfo.birthdayDate : "未设置");
            email.setText(!TextUtils.isEmpty(userInfo.email) ? userInfo.email : "未设置");
            face.setText("未设置");
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
                Intent intent1 = new Intent(this, SexActivity.class);
                String sexStr = sex.getText().toString().trim();
                intent1.putExtra("sex", !"未设置".equals(sexStr) ? sexStr : "");
                startActivity(intent1);
                break;
            case R.id.layout4://生日
                showTime();
                break;
            case R.id.layout5://邮箱Email
                Intent intent = new Intent(this, EmailActivity.class);
                String emailStr = email.getText().toString().trim();
                intent.putExtra("email", !"未设置".equals(emailStr) ? emailStr : "");
                startActivity(intent);
                break;
            case R.id.layout6://设置人脸
                break;
            case R.id.back_layout:
                finish();
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
            if ("0".equals(messageEvent.getMessage())) {//男
                sex.setText("男");
                userInfo.sex = "0";
            } else if ("1".equals(messageEvent.getMessage())) {//女
                sex.setText("女");
                userInfo.sex = "1";
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
        switch (requestCode) {
            case BaseConstant.SELECT_ORIGINAL_PIC:
                if (resultCode == RESULT_OK) {//从相册选择照片不裁切
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();

                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
//                        Log.e("TAG", "onActivityResult==>: " + JSON.toJSONString(bitmap));
                        img.setImageBitmap(bitmap);
                        mvpPresenter.uploadFile(picturePath);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
        }
        if (requestCode == BaseConstant.REQ_CODE && resultCode == RESULT_OK) {
            /*缩略图信息是储存在返回的intent中的Bundle中的，
             * 对应Bundle中的键为data，因此从Intent中取出
             * Bundle再根据data取出来Bitmap即可*/
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(bitmap);
//            Log.e(TAG, "img==》: " + JSON.toJSONString(extras));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showError() {

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
        GlideUtil.loadImage(this, imgUrl, img);
        if (userInfo != null) {
            userInfo.img = imgUrl;
            SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(userInfo));
        }
    }

    @Override
    public void uploadFileFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        date.setText(DateUtils.stampToDate(millseconds));
        userInfo.birthdayDate = DateUtils.stampToDate(millseconds);
        SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(userInfo));
        updateInfo();
    }
}
