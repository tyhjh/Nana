package com.dhht.nana.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.dhht.nana.app.Const;
import com.orhanobut.logger.Logger;
import com.yorhp.recordlibrary.ScreenUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import util.SharedPreferencesUtil;

/**
 * @author Tyhj
 * @date 2019/6/30
 */

public class WxService extends BaseAccessbilityService {

    private static final float pointYScal = 0.646F;

    private static final String WX_PACKAGE_NAME = "com.tencent.mm";


    public static final String WX_AUTO_REPALY = "wx_auto_repaly";
    public static final String WX_MONEY = "wx_auto_money";

    public static final String HONG_BAO = "微信红包";


    Set<String> nameSet = new HashSet<>();

    public static final String wxEditTextId = "com.tencent.mm:id/ami";
    public static final String wxSendButtonId = "com.tencent.mm:id/amp";
    public static final String wxHongBaoId = "com.tencent.mm:id/aql";
    public static final String wxOpenHongBaoId = "com.tencent.mm:id/d02";


    boolean autoRepaly = false;
    boolean autoMoney = false;
    boolean newMsg = false;
    boolean hongBaoComing = false;
    boolean waitingOpen = false;

    String currentName;
    String repalyTxt;


    int screenWidth = ScreenUtil.SCREEN_WIDTH;
    int screenHeight = ScreenUtil.SCREEN_HEIGHT;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        autoRepaly = SharedPreferencesUtil.getBoolean(WX_AUTO_REPALY, false);
        autoMoney = SharedPreferencesUtil.getBoolean(WX_MONEY, true);
        repalyTxt = SharedPreferencesUtil.getString(Const.Msg.WX_AUTO_REPALY_TXT, Const.Msg.AUTO_REPALY_DEFAULT);

        ScreenUtil.getScreenSize(this);

        screenWidth = ScreenUtil.SCREEN_WIDTH;
        screenHeight = ScreenUtil.SCREEN_HEIGHT;

        if (event.getPackageName().toString().equals(WX_PACKAGE_NAME)) {
            notifyWechat(event);
            if (waitingOpen) {
                try {
                    openHongBao();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (hongBaoComing) {
                try {
                    clickHongBaoItem();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                autoRepaly();
            }
        }
    }

    private void openHongBao() throws InterruptedException {
        if (autoMoney) {
            Thread.sleep(600);
            Logger.e("openHongBao：" + ScreenUtil.SCREEN_WIDTH);
            clickOnScreen(screenWidth / 2, screenHeight * pointYScal, 50, null);
            clickOnScreen(screenWidth / 2, screenHeight * pointYScal, 50, null);
            waitingOpen = false;
            Thread.sleep(1500);
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(200);
            back();
        }
    }

    private void clickHongBaoItem() throws InterruptedException {
        if (autoMoney) {
            Thread.sleep(500);
            List<AccessibilityNodeInfo> inputInfos = findViewsByID(wxHongBaoId);
            waitingOpen = true;
            hongBaoComing = false;
            Logger.e("autoMoney");
            if (inputInfos != null && inputInfos.size() > 0) {
                performViewClick(inputInfos.get(inputInfos.size() - 1));
            }
        }
    }

    private void autoRepaly() {
        if (newMsg && autoRepaly) {
            AccessibilityNodeInfo inputInfo = findViewByID(wxEditTextId);
            if (inputInfo != null) {
                if (nameSet.contains(currentName)) {
                    String msg = Const.Msg.msgs[new Random(System.currentTimeMillis()).nextInt(Const.Msg.msgs.length - 1)];
                    repalyTxt = msg + Const.Msg.AUTO_REPALY_DEFAULT_LAST;
                } else {
                    nameSet.add(currentName);
                }
                inputText(inputInfo, repalyTxt);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                newMsg = false;
                AccessibilityNodeInfo sendBtn = findViewByID(wxSendButtonId);
                performViewClick(sendBtn);
                back();
            }
        }
    }

    @Override
    public void onInterrupt() {

    }


    /**
     * 打开通知栏消息
     *
     * @param event
     */
    private void notifyWechat(AccessibilityEvent event) {
        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            String content = notification.tickerText.toString();
            String[] msg = content.split(":");
            String name = msg[0].trim();
            Logger.e(content);
            String text = msg[1].trim();
            if (text.contains(HONG_BAO)) {
                hongBaoComing = true;
                Logger.e("hongBaoComing");
                if (autoMoney) {
                    PendingIntent pendingIntent = notification.contentIntent;
                    try {
                        pendingIntent.send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (autoRepaly) {
                    newMsg = true;
                    currentName = name;
                    PendingIntent pendingIntent = notification.contentIntent;
                    try {
                        pendingIntent.send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void back() {
        try {
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(200);
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(200);
            performGlobalAction(GLOBAL_ACTION_BACK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // 创建唤醒锁
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "WxService:wakeLock");
        // 获得唤醒锁
        wakeLock.acquire();
    }
}
