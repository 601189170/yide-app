package com.yyide.chatim.activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.dialog.BottomHeadMenuPop;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.presenter.UserPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.UserView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class UserActivity extends BaseMvpActivity<UserPresenter> implements UserView {

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
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
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
        if(userInfo != null){
            GlideUtil.loadImage(this, userInfo.img, img);
            sex.setText(!TextUtils.isEmpty(userInfo.sex) ? userInfo.sex : "未设置");
            phone.setText(!TextUtils.isEmpty(userInfo.username) ? userInfo.username : "未设置");
            date.setText(!TextUtils.isEmpty(userInfo.birthdayDate) ? userInfo.birthdayDate : "未设置");
            email.setText(!TextUtils.isEmpty(userInfo.email) ? userInfo.email : "未设置");
            face.setText(!TextUtils.isEmpty(userInfo.email) ? userInfo.email : "未设置");
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
            case R.id.layout1:
                new BottomHeadMenuPop(this);
                break;
            case R.id.layout2://头像
                startActivity(new Intent(this, CheckPhoneActivity.class));
                break;
            case R.id.layout3://性别
                startActivity(new Intent(this, SexActivity.class));
                break;
            case R.id.layout4://生日
                showDatePickerDialog();
                break;
            case R.id.layout5://邮箱Email
                startActivity(new Intent(this, EmailActivity.class));
                break;
            case R.id.layout6://设置人脸
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_USER_EMAIL.equals(messageEvent.getCode())) {
            email.setText(messageEvent.getMessage());
        } else if (BaseConstant.TYPE_UPDATE_USER_SEX.equals(messageEvent.getCode())) {
            if ("0".equals(messageEvent.getMessage())) {//男
                sex.setText("男");
            } else if("1".equals(messageEvent.getMessage())){//女
                sex.setText("女");
            }
        } else if (BaseConstant.TYPE_UPDATE_USER_PHONE.equals(messageEvent.getCode())) {

        }
//        ToastUtils.showShort(messageEvent.getMessage());
        if (!TextUtils.isEmpty(messageEvent.getMessage())) {
            //mvpPresenter.update(userInfo.);
        }
    }

    private void showDatePickerDialog() {
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CHINA);
                String time = sdf.format(myCalendar.getTime());
                date.setText(time);
            }

        };

        new DatePickerDialog(this, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
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
    public void updateSuccess() {

    }

    @Override
    public void updateFail(String msg) {

    }
}
