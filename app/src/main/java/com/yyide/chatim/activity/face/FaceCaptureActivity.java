package com.yyide.chatim.activity.face;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kevin.crop.UCrop;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.dialog.BottomHeadMenuPop;
import com.yyide.chatim.dialog.DeptSelectPop;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.presenter.FaceUploadPresenter;
import com.yyide.chatim.utils.TakePicUtil;
import com.yyide.chatim.utils.YDToastUtil;
import com.yyide.chatim.view.FaceUploadView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class FaceCaptureActivity extends BaseMvpActivity<FaceUploadPresenter> implements FaceUploadView {
    private static final String TAG = "FaceCaptureActivity";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_face)
    ImageView iv_face;
    @BindView(R.id.tv_face_capture_tip)
    TextView tv_face_capture_tip;//显示正在采集中
    @BindView(R.id.btn_start_capture)
    Button btn_start_capture;//已经上传过人脸的，显示重新采集，默认开始采集
    @BindView(R.id.tv_student_name)
    TextView tvStudentName;//现在学生名字
    @BindView(R.id.btn_commit)
    Button btn_commit;

    private List<LeaveDeptRsp.DataBean> classList = new ArrayList<>();
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String classesId;
    private String studentId;
    private String depId;
    private String realname;

    @Override
    public int getContentViewID() {
        return R.layout.activity_face_capture;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.get_face_data);
        mDestinationUri = Uri.fromFile(new File(getCacheDir(), "cropImage.png"));
        studentId = getIntent().getStringExtra("studentId");
        realname = SpData.getIdentityInfo().getIdentityName();
        Log.e("FaceUploadPresenter", "getFaceData: name=" + realname + ",classId=" + classesId + ",depId=" + depId);
        //人名或者id为空则不能上传人脸
        parmsNull();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void parmsNull() {
        if (!SpData.getIdentityInfo().staffIdentity()) {
            tvStudentName.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(studentId)) {
                warnTip(getString(R.string.face_classid_null_tip));
            } else {
                //initClassView();
                mvpPresenter.getFaceData();
            }
        } else {
            tvStudentName.setVisibility(View.GONE);
            if (TextUtils.isEmpty(realname)) {
                warnTip(getString(R.string.face_username_null_tip));
            } else {
                mvpPresenter.getFaceData();
            }
        }
    }

    private void warnTip(String msg) {
        ToastUtils.showShort(msg);
        tv_face_capture_tip.setVisibility(View.VISIBLE);
        tv_face_capture_tip.setText(msg);
        //tv_face_capture_tip.setTextColor(getResources().getColor(R.color.face_warn));
        tv_face_capture_tip.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.icon_tips), null, null, null);
        tv_face_capture_tip.setCompoundDrawablePadding(10);
        btn_start_capture.setClickable(false);
        btn_start_capture.setAlpha(0.2f);
    }

    @Override
    protected FaceUploadPresenter createPresenter() {
        return new FaceUploadPresenter(this);
    }

    String currentPhotoPath;

    // 剪切后图像文件
    private Uri mDestinationUri;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * 去相机界面
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.yyide.chatim.FileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
            return;
        }
        ToastUtils.showShort(R.string.no_install_camera_tip);
    }

    private void rxPermission() {
        new BottomHeadMenuPop(this, "人脸采集");
    }

    @OnClick({R.id.back_layout, R.id.btn_start_capture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.btn_start_capture:
                //去系统相机界面
                //dispatchTakePictureIntent();
                rxPermission();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode:" + requestCode + ",resultCode:" + resultCode);
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
//                        showLoading();
                    }

                    @Override
                    public void onSuccess(File file) {
//                        hideLoading();
                        iv_face.setImageURI(TakePicUtil.getUriForFile(FaceCaptureActivity.this, file));
                        btn_start_capture.setBackground(getResources().getDrawable(R.drawable.btn_face));
                        btn_start_capture.setText(R.string.again_collect_tip);
                        btn_start_capture.setTextColor(getResources().getColor(R.color.colorAccent));
                        btn_start_capture.setClickable(true);
                        btn_start_capture.setAlpha(0.5f);
                        btn_commit.setVisibility(View.VISIBLE);
                        btn_commit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //图片上传
                                tv_face_capture_tip.setVisibility(View.VISIBLE);
                                tv_face_capture_tip.setText(R.string.face_uploading_tip);
                                mvpPresenter.uploadFile(file);
                                btn_commit.setClickable(false);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        ToastUtils.showShort("图片压缩失败请重试");
                    }
                }).
                launch();
    }

    private void galleryAddPic(String url) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(url);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * 开始剪切图片
     *
     * @param uri
     */
    private void startCropActivity(Uri uri) {
        UCrop.of(uri, mDestinationUri)
                .withTargetActivity(FaceCropActivity.class)
                .withAspectRatio(1, 1)
                //.withMaxResultSize(dip2px, dip2px)
                .start(this);
    }

    private void setPic(ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void faceUploadSuccess(String url) {
        Log.e(TAG, "faceUploadSuccess: " + url);
        //tv_face_capture_tip.setText(R.string.face_upload_success_tip);
        mvpPresenter.updateFace(url);
    }

    @Override
    public void faceUploadFail(String msg) {
        Log.e(TAG, "faceUploadFail: " + msg);
        ToastUtils.showShort("提交失败：" + msg);
        //tv_face_capture_tip.setText(R.string.face_upload_fail_tip);
        btn_commit.setClickable(true);
        tv_face_capture_tip.setVisibility(View.GONE);
    }

    @Override
    public void updateSuccess() {
        YDToastUtil.showMessage("采集人脸成功");
        tv_face_capture_tip.setVisibility(View.GONE);
        tv_face_capture_tip.setText("");
        mvpPresenter.getFaceData();
    }

    @Override
    public void updateFail(String msg) {
        hideLoading();
        Log.e(TAG, "updateFail: " + msg);
        YDToastUtil.showMessage("抱歉，采集失败，请重试");
        tv_face_capture_tip.setVisibility(View.GONE);
    }

    @Override
    public void getFaceDataSuccess(FaceOssBean faceOssBean) {
        hideLoading();
        Log.e(TAG, "getFaceDataSuccess: " + faceOssBean.toString());
        if (faceOssBean.getCode() == BaseConstant.REQUEST_SUCCESS2) {
            if (faceOssBean.getData() != null) {
                FaceOssBean.DataBean data = faceOssBean.getData();
                String path = data.getImgPath();
                if (!TextUtils.isEmpty(path)) {
                    tv_face_capture_tip.setVisibility(View.GONE);
                    btn_commit.setVisibility(View.GONE);
                    btn_start_capture.setText(R.string.again_collect_tip);
                    btn_start_capture.setBackground(getResources().getDrawable(R.drawable.face_capture_blue_btn_shape));
                    btn_start_capture.setTextColor(getResources().getColor(R.color.white));
                    btn_start_capture.setClickable(true);
                    //加载图片
                    Glide.with(this)
                            .load(path)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(iv_face);
                } else {
                    iv_face.setImageResource(R.drawable.icon_face_capture_box);
                }
            }
        }

        btn_start_capture.setClickable(true);
        btn_start_capture.setAlpha(1f);
    }


    @Override
    public void getFaceDataFail(String msg) {
        Log.e(TAG, "getFaceDataFail: " + msg);
        ToastUtils.showShort("" + msg);
        btn_start_capture.setClickable(true);
        btn_start_capture.setAlpha(1f);
        tv_face_capture_tip.setVisibility(View.GONE);
    }

    @Override
    public void showError() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initClassView() {
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (classOptional.isPresent()) {
            final LeaveDeptRsp.DataBean clazzBean = classOptional.get();
            tvStudentName.setText(clazzBean.getDeptName());
            studentId = clazzBean.getDeptId();
            if (classList.size() <= 1) {
                tvStudentName.setCompoundDrawables(null, null, null, null);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.icon_arrow_down);
                //设置图片大小，必须设置
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvStudentName.setCompoundDrawables(null, null, drawable, null);
                tvStudentName.setOnClickListener(v -> {
                            final DeptSelectPop deptSelectPop = new DeptSelectPop(this, 4, classList);
                            deptSelectPop.setOnCheckedListener((id, dept) -> {
                                Log.e(TAG, "班级选择: id=" + id + ", dept=" + dept);
                                tvStudentName.setText(dept);
                                if (TextUtils.isEmpty(id) || "0".equals(id)) {
                                    warnTip(getString(R.string.face_classid_null_tip));
                                }
                                studentId = id;
                                mvpPresenter.getFaceData();
                            });
                        }
                );
            }
        }
    }
}