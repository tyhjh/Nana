package com.dhht.nana.app;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.dhht.annotationlibrary.view.AvoidShake;
import com.dhht.nana.jump.Jump;
import com.dhht.nana.network.Retrofite;
import com.dhht.nana.util.FileUitl;
import com.dhht.nana.util.SaveErroToSDCard;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.yorhp.crashlibrary.CrashUtil;
import com.yorhp.crashlibrary.saveErro.ISaveErro;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import java.io.File;

import toast.ToastUtil;
import util.SharedPreferencesUtil;


/**
 * @author HanPei
 * @date 2019/6/26  下午3:07
 */
public class MyApplication extends Application {


    public static String rootDir, savePointDir, saveChessDir, gradeDir, crashDir;


    @Override
    public void onCreate() {
        super.onCreate();
        initDir();
        //设置点击时间间隔
        AvoidShake.setClickIntervalTime(400);
        Retrofite.init();
        ToastUtil.init(this);
        SharedPreferencesUtil.init(this);
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return true;
            }
        });
        CrashUtil.getInstance().init(this);
        CrashUtil.getInstance().setmSaveErro(new ISaveErro() {
            @Override
            public void saveErroMsg(Throwable throwable) {
                new SaveErroToSDCard(crashDir).saveErroMsg(throwable);
                Bitmap bitmap = ScreenRecordUtil.getInstance().getScreenShot();
                FileUitl.bitmapToPath(bitmap, MyApplication.savePointDir + "crash" + System.currentTimeMillis() + ".png");
                for (Bitmap bitmap1 : Jump.bitmapList) {
                    FileUitl.bitmapToPath(bitmap1, MyApplication.savePointDir + "crash_urgent_save" + System.currentTimeMillis() + ".png");
                }
            }
        });
    }


    //文件夹初始化
    public void initDir() {
        rootDir = Environment.getExternalStorageDirectory() + "/ANana/";
        File f1 = new File(rootDir);
        if (!f1.exists()) {
            f1.mkdirs();
        }

        saveChessDir = rootDir + "chess/";
        File f5 = new File(saveChessDir);
        if (!f5.exists()) {
            f5.mkdirs();
        }

        savePointDir = rootDir + "check/";
        File f6 = new File(savePointDir);
        if (!f6.exists()) {
            f6.mkdirs();
        }

        gradeDir = rootDir + "grade/";
        File f7 = new File(gradeDir);
        if (!f7.exists()) {
            f7.mkdirs();
        }

        crashDir = rootDir + "crash/";
        File f8 = new File(crashDir);
        if (!f8.exists()) {
            f8.mkdirs();
        }

    }

}
