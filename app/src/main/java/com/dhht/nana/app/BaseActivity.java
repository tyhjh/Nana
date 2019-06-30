package com.dhht.nana.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dhht.annotationlibrary.ViewInjector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import toast.ToastUtil;

/**
 * @author HanPei
 * @date 2019/3/22  上午9:08
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    public static List<Activity> sActivityList = new ArrayList<>();

    T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sActivityList.add(0, this);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        //初始化注解
        ViewInjector.injectView(this);
        //初始化Presenter
        mPresenter = setPresenter();
        //执行afterView方法
        afterView();
    }

    /**
     * 获取布局
     *
     * @return
     */
    protected abstract @LayoutRes
    int getLayoutId();



    /**
     * 设置Presenter
     *
     * @return
     */
    protected T setPresenter() {
        return null;
    }


    public T getPresenter() {
        return mPresenter;
    }

    /**
     * 控件初始化后的操作
     */
    protected abstract void afterView();


    public static void finishAll() {
        for (int i = 0; i < sActivityList.size(); i++) {
            sActivityList.get(i).finish();
        }
    }


    public void showMsg(String msg) {
        ToastUtil.showShort(msg);
    }



    @Override
    protected void onDestroy() {
        sActivityList.remove(this);
        super.onDestroy();
    }

    /**
     * 延迟关闭页面
     *
     * @param delay
     */
    public void finish(int delay) {
        Observable.timer(delay, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        finish();
                    }
                });
    }


}
