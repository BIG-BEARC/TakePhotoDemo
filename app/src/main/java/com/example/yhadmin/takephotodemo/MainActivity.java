package com.example.yhadmin.takephotodemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener
{
    public static final int TAKE_PHOTO = 1;//启动相机标识
    public static final int SELECT_PHOTO = 2;//启动相册标识
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 3;
    // 获取SDcard路径
    public static String SDCARD = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath();

    // 定义SDcard存放数据库的文件夹
    public static final String CR_URL_DBFILE = "/yihua";
    public static final String CR_PATH = SDCARD + CR_URL_DBFILE;

    private File      outputImagepath;//存储拍完照后的图片
    private Bitmap    orc_bitmap;//拍照和相册获取图片的Bitmap

    private Button    mTakePhotoBtn;
    private ImageView mImageView;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTakePhotoBtn = findViewById(R.id.take_photo_btn);
        mImageView = findViewById(R.id.img);
        mTakePhotoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo_btn:
                applyWritePermission();
                 break;
            default:
                 break;
        }
    }

    public void applyWritePermission() {

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                useCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION);
            }
        } else {
            useCamera();
        }
    }

    private void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFile = new File(CR_PATH + "/" + System.currentTimeMillis() + ".jpg");
        mFile.getParentFile().mkdirs();
        Uri uri = CameraUtile.getUriForFile(mFile, this);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_EXTERNAL_STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            useCamera();
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.e("TAG", "---------" + FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", mFile));
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mFile.getAbsolutePath()));
        }
    }
}
