package com.victor.crash.library;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: CrashActivity.java
 * Author: Victor
 * Date: 2018/12/24 15:55
 * Description:
 * -----------------------------------------------------------------
 */

public class CrashActivity extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {

    public static final String CRASH_MODEL = "crash_model";
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private CrashMessageInfo model;

    private ImageView mIvMore;
    private TextView mTvPkgName,mTvMessage,mTvClsName,mTvMethodName,mTvlineNum,
            mTvExceptionType,mTvFullException,mTvTime,mTvModel,mTvBrand,mTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        initialize();
        initData();
    }

    private void initialize () {
        mIvMore = findViewById(R.id.iv_more);
        mTvPkgName = findViewById(R.id.tv_pkgname);
        mTvMessage = findViewById(R.id.tv_message);
        mTvClsName = findViewById(R.id.tv_class_name);
        mTvMethodName = findViewById(R.id.tv_method_name);
        mTvlineNum = findViewById(R.id.tv_line_num);
        mTvExceptionType = findViewById(R.id.tv_exception_type);
        mTvFullException = findViewById(R.id.tv_full_exception);
        mTvTime = findViewById(R.id.tv_time);
        mTvModel = findViewById(R.id.tv_model);
        mTvBrand = findViewById(R.id.tv_brand);
        mTvVersion = findViewById(R.id.tv_version);

        mIvMore.setOnClickListener(this);
    }

    private void initData () {
        model = getIntent().getParcelableExtra(CRASH_MODEL);
        if (model == null) {
            return;
        }

        mTvPkgName.setText(model.getClassName());
        mTvMessage.setText(model.getExceptionMsg());
        mTvClsName.setText(model.getFileName());
        mTvMethodName.setText(model.getMethodName());
        mTvlineNum.setText(String.valueOf(model.getLineNumber()));
        mTvExceptionType.setText(model.getExceptionType());
        mTvFullException.setText(model.getFullException());
        mTvTime.setText(df.format(model.getTime()));
        mTvModel.setText(model.getDevice().getModel());
        mTvBrand.setText(model.getDevice().getBrand());
        mTvVersion.setText(model.getDevice().getVersion());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_cope_txt) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                ClipData mClipData = ClipData.newPlainText("crash", getShareText(model));
                cm.setPrimaryClip(mClipData);
                showToast("拷贝成功");
            }
            return true;
        } else if (itemId == R.id.action_share_txt) {
            shareText(getShareText(model));
            return true;
        } else if (itemId == R.id.action_share_img) {
            if (ContextCompat.checkSelfPermission(CrashActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(CrashActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                shareImage();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getShareText(CrashMessageInfo model) {
        StringBuilder builder = new StringBuilder();

        builder.append("崩溃信息:")
                .append("\n")
                .append(model.getExceptionMsg())
                .append("\n");
        builder.append("\n");

        builder.append("类名:")
                .append(model.getFileName()).append("\n");
        builder.append("\n");

        builder.append("方法:").append(model.getMethodName()).append("\n");
        builder.append("\n");

        builder.append("行数:").append(model.getLineNumber()).append("\n");
        builder.append("\n");

        builder.append("类型:").append(model.getExceptionType()).append("\n");
        builder.append("\n");

        builder.append("时间").append(df.format(model.getTime())).append("\n");
        builder.append("\n");

        builder.append("设备名称:").append(model.getDevice().getModel()).append("\n");
        builder.append("\n");

        builder.append("设备厂商:").append(model.getDevice().getBrand()).append("\n");
        builder.append("\n");

        builder.append("系统版本:").append(model.getDevice().getVersion()).append("\n");
        builder.append("\n");

        builder.append("全部信息:")
                .append("\n")
                .append(model.getFullException()).append("\n");

        return builder.toString();
    }

    private void shareText(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "崩溃信息：");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享到"));
    }

    private static final int REQUEST_CODE = 110;

    private void requestPermission(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //判断请求码，确定当前申请的权限
        if (requestCode == REQUEST_CODE) {
            //判断权限是否申请通过
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //授权成功
                shareImage();
            } else {
                //授权失败
                showToast("请授予SD卡权限才能分享图片");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public Bitmap getBitmapByView(ScrollView view) {
        if (view == null) return null;
        int height = 0;
        for (int i = 0; i < view.getChildCount(); i++) {
            height += view.getChildAt(i).getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRGB(255, 255, 255);
        view.draw(canvas);
        return bitmap;
    }

    private File BitmapToFile(Bitmap bitmap) {
        if (bitmap == null) return null;
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath();
        File imageFile = new File(path, "spiderMan-" + df.format(model.getTime()) + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private void shareImage() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showToast("未插入sd卡");
            return;
        }
        File file = BitmapToFile(getBitmapByView((ScrollView) findViewById(R.id.scrollView)));
        if (file == null || !file.exists()) {
            showToast("图片文件不存在");
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                    getApplicationContext().getPackageName() + ".spidermanfileprovider", file);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "分享图片"));
    }

    private void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_more) {
            PopupMenu menu = new PopupMenu(CrashActivity.this, v);
            menu.inflate(R.menu.menu_crash);
            menu.show();
            menu.setOnMenuItemClickListener(this);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_cope_txt) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                ClipData mClipData = ClipData.newPlainText("crash", getShareText(model));
                cm.setPrimaryClip(mClipData);
                showToast("拷贝成功");
            }
            return true;
        } else if (itemId == R.id.action_share_txt) {
            shareText(getShareText(model));
            return true;
        } else if (itemId == R.id.action_share_img) {
            if (ContextCompat.checkSelfPermission(CrashActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(CrashActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                shareImage();
            }
            return true;
        }
        return false;
    }
}
