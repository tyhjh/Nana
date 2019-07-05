package com.dhht.nana;

import android.Manifest;
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
import com.dhht.nana.app.Const;
import com.dhht.nana.jump.Jump;
import com.dhht.nana.service.JumpService;
import com.dhht.nana.service.QqService;
import com.dhht.nana.service.WxService;
import com.dhht.nana.util.AccessbilityUtil;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import permison.FloatWindowManager;
import permison.PermissonUtil;
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
    void switchJump() {
        if (FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this)) {
            ScreenRecordUtil.getInstance().screenShot(MainActivity.this, null);
            Jump.setStart_model(Const.RUN_MODEL_QUICK_JUMP);
            JumpService.isOpenScreenCut=true;
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }
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

        switchJump.setChecked(AccessbilityUtil.isAccessibilitySettingsOn(this, JumpService.class));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PermissonUtil.checkPermission(this, null, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        afterView();
    }
}
