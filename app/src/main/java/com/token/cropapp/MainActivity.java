package com.token.cropapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.tokentm.sdk.crop.UCrop;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.actiondialog.BottomPicSelectDialog;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

//import com.tokentm.sdk.crop.UCrop;

/**
 * @author lqx
 */
public class MainActivity extends AppCompatActivity {

    private TextView tv_show_content;
    private ImageView show_image;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_show_content = findViewById(R.id.tv_show_content);
        show_image = findViewById(R.id.show_image);
        //选择图片
        findViewById(R.id.select_pictures).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                XXF.requestPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                       // .compose(XXF.<Boolean>bindToErrorNotice())
//                        .subscribe(new Consumer<Boolean>() {
//                            @Override
//                            public void accept(Boolean aBoolean) throws Exception {
//
//                            }
//                        });

                new BottomPicSelectDialog(MainActivity.this, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        startUCrop(MainActivity.this, s, UCrop.REQUEST_CROP, 16, 9);
                    }
                }).show();
            }
        });
    }

    /**
     * 启动裁剪
     *
     * @param activity       上下文
     * @param sourceFilePath 需要裁剪图片的绝对路径
     * @param requestCode    比如：UCrop.REQUEST_CROP
     * @param aspectRatioX   裁剪图片宽高比
     * @param aspectRatioY   裁剪图片宽高比
     * @return //
     */
    @SuppressLint("CheckResult")
    public void startUCrop(final FragmentActivity activity, String sourceFilePath,
                           int requestCode, float aspectRatioX, float aspectRatioY) {
        XXF.startActivityForResult(activity, UCropUtils.getUcropLauncher(activity, sourceFilePath, aspectRatioX, aspectRatioY), requestCode)
                .filter(new Predicate<ActivityResult>() {
            @Override
            public boolean test(ActivityResult activityResult) throws Exception {
                return activityResult.isOk();
            }
        }).subscribe(new Consumer<ActivityResult>() {
            @Override
            public void accept(ActivityResult activityResult) throws Exception {
                final Uri resultUri = UCrop.getOutput(activityResult.getData());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(resultUri.getPath(), options);
//                        TessBaseAPI tessBaseAPI = new TessBaseAPI();
////                        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
////                        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789"); // 识别白名单
////                        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);//设置识别模式
//                        tessBaseAPI.init("/storage/emulated/0/", "eng");//参数后面有说明。
//                        tessBaseAPI.setImage(bitmap);
//                        String result = tessBaseAPI.getUTF8Text();
//                        tessBaseAPI.end();
                show_image.setImageBitmap(bitmap);
//                        tv_show_content.setText(result);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ToastUtils.showToast(throwable.getMessage());
            }
        });
    }

}

