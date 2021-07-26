package com.yyide.chatim.activity;

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

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kevin.crop.UCrop;
import com.tencent.mmkv.MMKV;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.dialog.DeptSelectPop;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.presenter.FaceUploadPresenter;
import com.yyide.chatim.utils.DisplayUtils;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.FaceUploadView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;

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
    private List<LeaveDeptRsp.DataBean> classList = new ArrayList<>();
    private static final int REQUEST_TAKE_PHOTO = 1;
    private long classesId;
    private long studentId;
    private long depId;
    private String realname;

    @Override
    public int getContentViewID() {
        return R.layout.activity_face_capture;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("人脸采集");
        mDestinationUri = Uri.fromFile(new File(getCacheDir(), "cropImage.png"));
        classesId = SpData.getIdentityInfo().classesId;
        //depId = SpData.getIdentityInfo().teacherDepId;
        realname = SpData.getIdentityInfo().realname;
        Log.e("FaceUploadPresenter", "getFaceData: name="+realname +",classId="+classesId+",depId="+depId);
        //人名或者id为空则不能上传人脸
        parmsNull();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void parmsNull() {
            if (!SpData.getIdentityInfo().staffIdentity()) {
                tvStudentName.setVisibility(View.VISIBLE);
                initClassData();
                if (studentId == 0) {
                    warnTip("当前账号学生Id为空不能采集人脸");
                } else {
                    initClassView();
                    mvpPresenter.getFaceData(realname,classesId , depId,studentId);
                }
            } else {
                tvStudentName.setVisibility(View.GONE);
                if (TextUtils.isEmpty(realname)) {
                    warnTip("当前账号姓名为空不能采集人脸");
                } else {
                    mvpPresenter.getFaceData(realname, classesId, depId, studentId);
                }
            }
    }

    private void warnTip(String msg) {
        ToastUtils.showShort(msg);
        tv_face_capture_tip.setVisibility(View.VISIBLE);
        tv_face_capture_tip.setText(msg);
        //tv_face_capture_tip.setTextColor(getResources().getColor(R.color.face_warn));
        tv_face_capture_tip.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.icon_tips),null,null,null);
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
        ToastUtils.showShort("您的手机没有安装相机，人脸采集功能暂时不能使用！");
    }

    @OnClick({R.id.back_layout, R.id.btn_start_capture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.btn_start_capture:
                //去系统相机界面
                dispatchTakePictureIntent();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode:"+requestCode+",resultCode:"+resultCode );
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.e(TAG, "显示当前照片: " + currentPhotoPath);
            File temp = new File(currentPhotoPath);
            startCropActivity(Uri.fromFile(temp));
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
                if (data ==  null){
                    //取消并重新获取图片
                    dispatchTakePictureIntent();
                    return;
                }
                handleCropResult(data);
        } else {
        }
    }
    /**
     * 处理剪切后的返回值
     * @param result
     */
    private void handleCropResult(Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            String filePath = resultUri.getEncodedPath();
            String imagePath = Uri.decode(filePath);
            Log.e(TAG,"imagePath:"+imagePath);
            Bitmap bmp;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                iv_face.setImageBitmap(bmp);
                tv_face_capture_tip.setVisibility(View.VISIBLE);
                tv_face_capture_tip.setText("人脸上传中...");
                btn_start_capture.setText("重新采集");
                btn_start_capture.setClickable(false);
                btn_start_capture.setAlpha(0.5f);
                //去上传人脸图片
            } catch (FileNotFoundException e) {
            } catch (IOException e) {

            }
            //图片上传
            mvpPresenter.updateFaceData(realname,classesId,depId,studentId,imagePath);

            File file = new File(currentPhotoPath);
            if (file.exists()) {
                file.delete();
            }
        } else {
            Toast.makeText(this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
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
    public void faceUploadSuccess(BaseRsp baseRsp) {
        Log.e(TAG, "faceUploadSuccess: " + baseRsp.toString());
        ToastUtils.showShort(""+baseRsp.getMsg());
        if (baseRsp.getCode() == 200){
            tv_face_capture_tip.setText("人脸上传成功");
        }else {
            tv_face_capture_tip.setText("人脸上传失败");
        }
        mvpPresenter.getFaceData(realname,classesId,depId,studentId);
    }

    @Override
    public void faceUploadFail(String msg) {
        Log.e(TAG, "faceUploadFail: " + msg);
        ToastUtils.showShort("提交失败："+msg);
        tv_face_capture_tip.setText("人脸上传失败");
        mvpPresenter.getFaceData(realname,classesId,depId,studentId);
    }

    @Override
    public void getFaceDataSuccess(FaceOssBean faceOssBean) {
        Log.e(TAG, "getFaceDataSuccess: "+faceOssBean.toString() );
        if (faceOssBean.getCode() == 200) {
            if (faceOssBean.getData() != null) {
                FaceOssBean.DataBean data = faceOssBean.getData();
                final String status = data.getStatus();
                tv_face_capture_tip.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(status)) {
                    tv_face_capture_tip.setText(status);
                }
                String path = data.getPath();
                if (!TextUtils.isEmpty(path)) {
                    btn_start_capture.setText("重新采集");
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
        Log.e(TAG, "getFaceDataFail: "+msg );
        ToastUtils.showShort(""+msg);
        btn_start_capture.setClickable(true);
        btn_start_capture.setAlpha(1f);
    }

    @Override
    public void showError() {

    }

    private void initClassData() {
        final List<GetUserSchoolRsp.DataBean.FormBean> form = SpData.getIdentityInfo().form;
        final GetUserSchoolRsp.DataBean.FormBean classInfo = SpData.getClassInfo();
        classList.clear();
        for (GetUserSchoolRsp.DataBean.FormBean formBean : form) {
            final String studentName = formBean.studentName;
            final String studentId = formBean.studentId;
            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
            try {
                dataBean.setDeptId(Long.parseLong(studentId));
            } catch (NumberFormatException exception) {
                Log.e(TAG, "studentId="+formBean.studentId );
                dataBean.setDeptId(0);
            }
            dataBean.setDeptName(studentName);
            dataBean.setIsDefault(0);
            classList.add(dataBean);
        }
        if (!classList.isEmpty()) {
            final LeaveDeptRsp.DataBean dataBean = classList.get(0);
            dataBean.setIsDefault(1);
            studentId = dataBean.getDeptId();
        } else {
            warnTip("当前账号学生Id为空不能采集人脸");
            tvStudentName.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initClassView() {
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (classOptional.isPresent()){
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
                            final DeptSelectPop deptSelectPop = new DeptSelectPop(this, 2, classList);
                            deptSelectPop.setOnCheckedListener((id, dept) -> {
                                Log.e(TAG, "班级选择: id=" + id + ", dept=" + dept);
                                tvStudentName.setText(dept);
                                if (id == 0){
                                    warnTip("当前账号学生Id为空不能采集人脸");
                                }
                                studentId = id;
                                mvpPresenter.getFaceData(realname,classesId , depId,studentId);
                            });
                        }
                );
            }
        }
    }
}