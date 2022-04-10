package com.yyide.chatim.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tencent.mmkv.MMKV;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.face.FaceCaptureActivity;
import com.yyide.chatim.activity.user.EmailActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.base.MMKVConstant;
import com.yyide.chatim.dialog.BottomHeadMenuPop;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.FaceProtocolBean;
import com.yyide.chatim.model.UserInfo;
import com.yyide.chatim.presenter.UserPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.TakePicUtil;
import com.yyide.chatim.view.DialogUtil;
import com.yyide.chatim.view.UserView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @BindView(R.id.email_line)
    View email_line;
    @BindView(R.id.fl_patriarch)
    FrameLayout fl_patriarch;
    @BindView(R.id.patriarchImg)
    ImageView patriarchImg;
    @BindView(R.id.tv_head_desc)
    TextView tvHeadDesc;
    private UserInfo userInfo;
    private String realname;
    private MMKV mmkv;
    private String userId;

    @Override
    public int getContentViewID() {
        return R.layout.activity_user_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mmkv = MMKV.defaultMMKV();
        userId = SpData.getUserId();
        title.setText("我的信息");
        mvpPresenter.getUserInfo();
        realname = SpData.getIdentityInfo().getIdentityName();
        Log.e(TAG, "getFaceData: name=" + realname);
        if (!SpData.getIdentityInfo().staffIdentity()) {
            layout6.setVisibility(View.GONE);
        }
    }

    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    private void setUserInfo(UserInfo data) {
        if (data != null) {
            userInfo = data;
            GlideUtil.loadImageHead(this, userInfo.getAvatar(), img);
            sex.setText(!TextUtils.isEmpty(userInfo.getGender()) ? ("1".equals(userInfo.getGender()) ? "男" : "女") : "未设置");
            phone.setText(!TextUtils.isEmpty(userInfo.getUsername()) ? userInfo.getUsername() : "未设置");
//            date.setText(!TextUtils.isEmpty(data.get) ? data.birthdayDate : "未设置");
            date.setText("未设置");
            email.setText(!TextUtils.isEmpty(userInfo.getEmail()) ? userInfo.getEmail() : "未设置");
            face.setText("未设置");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mvpPresenter.getFaceData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5, R.id.layout6, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout1://头像
                rxPermission();
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
                //startActivityForResult(new Intent(this, FaceCaptureActivity.class), 1);
                if (getUserAgreedProtocol()) {
                    startActivityForResult(new Intent(this, FaceCaptureActivity.class), 1);
                } else {
                    DialogUtil.showFaceProtocolDialog(this, new DialogUtil.OnClickListener() {
                        @Override
                        public void onCancel(View view) {

                        }

                        @Override
                        public void onEnsure(View view) {
                            saveUserAgreedFaceProtocol();
                            startActivityForResult(new Intent(UserActivity.this, FaceCaptureActivity.class), 1);
                        }
                    });
                }
                break;
            case R.id.back_layout:
                finish();
            default:
                break;
        }
    }

    /**
     * 保存当前用户同意人脸采集协议
     */
    private void saveUserAgreedFaceProtocol() {
        final String decodeString = mmkv.decodeString(MMKVConstant.YD_FACE_PROTOCOL_AGREED_STATUS);
        if (!TextUtils.isEmpty(decodeString)) {
            final List<FaceProtocolBean> faceProtocolBeans = JSON.parseArray(decodeString, FaceProtocolBean.class);
            faceProtocolBeans.add(new FaceProtocolBean(userId, true));
            mmkv.encode(MMKVConstant.YD_FACE_PROTOCOL_AGREED_STATUS, JSON.toJSONString(faceProtocolBeans));
            return;
        }
        final List<FaceProtocolBean> faceProtocolBeans = new ArrayList<>();
        faceProtocolBeans.add(new FaceProtocolBean(userId, true));
        mmkv.encode(MMKVConstant.YD_FACE_PROTOCOL_AGREED_STATUS, JSON.toJSONString(faceProtocolBeans));
    }

    /**
     * 判断当前用户是否同意过人脸采集协议
     *
     * @return
     */
    private boolean getUserAgreedProtocol() {
        if (TextUtils.isEmpty(userId)) {
            return false;
        }
        final String decodeString = mmkv.decodeString(MMKVConstant.YD_FACE_PROTOCOL_AGREED_STATUS);
        if (!TextUtils.isEmpty(decodeString)) {
            final List<FaceProtocolBean> faceProtocolBeans = JSON.parseArray(decodeString, FaceProtocolBean.class);
            for (FaceProtocolBean faceProtocolBean : faceProtocolBeans) {
                if (Objects.equals(faceProtocolBean.getUserId(), userId)) {
                    return faceProtocolBean.getAgreed();
                }
            }
        }
        return false;
    }

    private void rxPermission() {
        new BottomHeadMenuPop(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_USER_EMAIL.equals(messageEvent.getCode())) {
            email.setText(messageEvent.getMessage());
            userInfo.setEmail(messageEvent.getMessage());
            updateInfo();
        } else if (BaseConstant.TYPE_UPDATE_USER_SEX.equals(messageEvent.getCode())) {
            if ("1".equals(messageEvent.getMessage())) {//男
                sex.setText("男");
            } else if ("0".equals(messageEvent.getMessage())) {//女
                sex.setText("女");
            }
            updateInfo();
        }
    }

    private void updateInfo() {
        if (userInfo != null) {
            mvpPresenter.update(userInfo.getId() + "",
                    TextUtils.isEmpty(userInfo.getAvatar()) ? "" : userInfo.getAvatar(),
                    TextUtils.isEmpty(userInfo.getEmail()) ? "" : userInfo.getEmail());
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
                        showLoading();
                    }

                    @Override
                    public void onSuccess(File file) {
                        mvpPresenter.uploadFile(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        ToastUtils.showShort("图片压缩失败请重试");
                    }
                }).
                launch();
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

    private boolean isStudent = false;

    @Override
    public void uploadFileSuccess(String imgUrl) {
        GlideUtil.loadImageHead(this, imgUrl, img);
        userInfo.setAvatar(imgUrl);
        //保存到本地
        SPUtils.getInstance().put(SpData.USER, JSON.toJSONString(userInfo));
        updateInfo();
    }

    @Override
    public void uploadFileFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void getFaceDataSuccess(FaceOssBean faceOssBean) {
        Log.e(TAG, "getFaceDataSuccess: " + faceOssBean.toString());
        if (faceOssBean.getCode() == BaseConstant.REQUEST_SUCCESS2) {
            if (faceOssBean.getData() != null) {
                FaceOssBean.DataBean data = faceOssBean.getData();
//                final String status = data.getType();
//                face.setText(status);
//                if (!"采集成功".equals(status) && !"采集中".equals(status)) {
//                    final Drawable drawable = ContextCompat.getDrawable(this, R.drawable.face_capture_fail_icon);
//                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                    face.setCompoundDrawables(drawable, null, null, null);
//                } else {
//                    face.setCompoundDrawables(null, null, null, null);
//                }
            }
        }
    }

    @Override
    public void getFaceDataFail(String msg) {

    }

    @Override
    public void getUserInfoSuccess(UserInfo userInfo) {
        setUserInfo(userInfo);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        date.setText(DateUtils.stampToDate(millseconds));
//        userInfo.birthdayDate = DateUtils.stampToDate(millseconds);
        SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(userInfo));
        updateInfo();
    }
}
