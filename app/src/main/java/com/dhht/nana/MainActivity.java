package com.dhht.nana;

import android.content.Intent;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.dhht.annotation.CheckBoxChange;
import com.dhht.annotation.Click;
import com.dhht.annotation.SwitchChange;
import com.dhht.annotation.ViewById;
import com.dhht.nana.app.BaseActivity;
import com.dhht.nana.service.QqService;
import com.dhht.nana.service.WxService;
import com.dhht.nana.util.AccessbilityUtil;

import util.SharedPreferencesUtil;

public class MainActivity extends BaseActivity {

    @ViewById
    Switch swInstall, swSkip, switchJump, swWx, swQq;


    @ViewById
    CheckBox ckWxMsg, ckWxMoney, ckQqMoney, ckQqMsg;

    @ViewById
    TextView tvBackMsg;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @SwitchChange
    void swInstall(boolean isChecked) {

    }

    @SwitchChange
    void swSkip(boolean isChecked) {

    }


    @Click
    void swWx() {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }


    @Click
    void swQq() {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }


    @CheckBoxChange
    void ckWxMsg(boolean isChecked) {
        SharedPreferencesUtil.save(WxService.WX_AUTO_REPALY, isChecked);
    }


    @CheckBoxChange
    void ckWxMoney(boolean isChecked) {
        SharedPreferencesUtil.save(WxService.WX_MONEY, isChecked);
    }


    @CheckBoxChange
    void ckQqMoney(boolean isChecked) {
        SharedPreferencesUtil.save(QqService.QQ_MONEY, isChecked);
    }


    @CheckBoxChange
    void ckQqMsg(boolean isChecked) {
        SharedPreferencesUtil.save(QqService.QQ_AUTO_REPALY, isChecked);
    }


    @Override
    protected void afterView() {
        swWx.setChecked(AccessbilityUtil.isAccessibilitySettingsOn(this, WxService.class));
        ckWxMoney.setChecked(SharedPreferencesUtil.getBoolean(WxService.WX_MONEY, true));
        ckWxMsg.setChecked(SharedPreferencesUtil.getBoolean(WxService.WX_AUTO_REPALY, false));

        swQq.setChecked(AccessbilityUtil.isAccessibilitySettingsOn(this, QqService.class));
        ckQqMoney.setChecked(SharedPreferencesUtil.getBoolean(QqService.QQ_MONEY, true));
        ckQqMsg.setChecked(SharedPreferencesUtil.getBoolean(QqService.QQ_AUTO_REPALY, false));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        afterView();
    }
}
