package com.yyide.chatim.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.ToastUtils;
import com.kevin.crop.UCrop;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.utils.DisplayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class FaceCaptureActivity extends BaseActivity {
    private static final String TAG = "FaceCaptureActivity";
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.iv_face)
    ImageView iv_face;

    @BindView(R.id.tv_face_capture_tip)
    TextView tv_face_capture_tip;//显示正在采集中

    @BindView(R.id.btn_start_capture)
    Button btn_start_capture;//已经上传过人脸的，显示重新采集，默认开始采集

    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    public int getContentViewID() {
        return R.layout.activity_face_capture;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("人脸采集");
        mDestinationUri = Uri.fromFile(new File(getCacheDir(), "cropImage.jpeg"));
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
                //去上传人脸图片
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
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
}