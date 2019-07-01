package com.dhht.nana.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Rect;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.orhanobut.logger.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.SharedPreferencesUtil;

/**
 * @author Tyhj
 * @date 2019/6/30
 */

public class QqService extends BaseAccessbilityService {

    private static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";

    public static final String QQ_AUTO_REPALY = "qq_auto_repaly";
    public static final String QQ_MONEY = "qq_auto_money";

    public static final String HONG_BAO = "红包";

    public static final String QQ_HONG_BAO = "QQ红包";
    public static final String QQ_WORD_HONG_BAO = "QQ文字口令红包";


    public static final String QQ_INPUT_ID = "com.tencent.mobileqq:id/input";

    public static final String QQ_MSG_SEND = "com.tencent.mobileqq:id/fun_btn";


    boolean autoMoney = false;
    boolean autoReply = false;
    boolean newMsg = false;
    boolean hongBaoComing = false;

    String currentName;

    Set<String> nameSet = new HashSet<>();


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        autoMoney = SharedPreferencesUtil.getBoolean(QQ_MONEY, true);
        autoReply = SharedPreferencesUtil.getBoolean(QQ_AUTO_REPALY, false);

        if (event.getPackageName().toString().equals(QQ_PACKAGE_NAME)) {
            try {
                if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                    notifyQq(event);
                }else if (findViewByID(QQ_INPUT_ID) != null && autoMoney && hongBaoComing) {
                    clickHongBaoItems();
                } else if (autoReply && newMsg) {
                    autoReplyMsg();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void autoReplyMsg() {
        performGlobalAction(GLOBAL_ACTION_HOME);
    }

    private void notifyQq(AccessibilityEvent event) {
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
                if (autoReply) {
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

    private void clickHongBaoItems() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<AccessibilityNodeInfo> inputInfos = getRootInActiveWindow().findAccessibilityNodeInfosByText(HONG_BAO);
        while (inputInfos.size() == 0 && (System.currentTimeMillis() - startTime) < 1000) {
            Thread.sleep(300);
            inputInfos = getRootInActiveWindow().findAccessibilityNodeInfosByText(HONG_BAO);
        }
        if (inputInfos == null || inputInfos.size() == 0) {
            return;
        }
        for (int i = inputInfos.size() - 1; i >= 0; i--) {
            AccessibilityNodeInfo info = inputInfos.get(i);
            Rect rect = new Rect();
            info.getBoundsInScreen(rect);
            if (rect.bottom < 800) {
                continue;
            }
            if (info.getText().toString().equals(QQ_HONG_BAO)) {
                clickOnScreen(rect.right, rect.top - 50, 10, null);
                Thread.sleep(800);
                hongBaoComing = false;
                performGlobalAction(GLOBAL_ACTION_HOME);
                return;
            } else if (info.getText().toString().equals(QQ_WORD_HONG_BAO)) {
                info.getBoundsInScreen(rect);
                clickOnScreen(rect.right, rect.top - 50, 10, null);
                Thread.sleep(200);
                clickOnScreen(rect.left, rect.bottom - 10, 10, null);
                Thread.sleep(200);
                AccessibilityNodeInfo nodeInfo = findViewByID(QQ_MSG_SEND);
                performViewClick(nodeInfo);
                Thread.sleep(800);
                hongBaoComing = false;
                performGlobalAction(GLOBAL_ACTION_HOME);
                return;
            }
        }

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // 创建唤醒锁
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "QqService:wakeLock");
        // 获得唤醒锁
        wakeLock.acquire();
        performGlobalAction(GLOBAL_ACTION_BACK);
        performGlobalAction(GLOBAL_ACTION_BACK);
        performGlobalAction(GLOBAL_ACTION_BACK);
    }


}
