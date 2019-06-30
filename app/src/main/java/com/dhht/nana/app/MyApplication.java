package com.dhht.nana.app;

import android.app.Application;
import android.support.annotation.Nullable;

import com.dhht.annotationlibrary.view.AvoidShake;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import toast.ToastUtil;
import util.SharedPreferencesUtil;


/**
 * @author HanPei
 * @date 2019/6/26  下午3:07
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置点击时间间隔
        AvoidShake.setClickIntervalTime(400);
        ToastUtil.init(this);
        SharedPreferencesUtil.init(this);
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return true;
            }
        });
    }
}
