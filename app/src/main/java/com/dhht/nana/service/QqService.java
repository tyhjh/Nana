package com.dhht.nana.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Rect;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.dhht.nana.app.Const;
import com.dhht.nana.data.ChatDataSource;
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


    public static final String AT_ME = "[有人@我]";
    public static final String AT = "@";


    String replyName;
    boolean autoMoney = false;
    boolean autoReply = false;
    boolean newMsg = false;
    boolean hongBaoComing = false;

    ChatDataSource dataSource;

    String currentName;
    String recivedMsg;

    Set<String> nameSet = new HashSet<>();
    String repalyTxt;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        autoMoney = SharedPreferencesUtil.getBoolean(QQ_MONEY, true);
        autoReply = SharedPreferencesUtil.getBoolean(QQ_AUTO_REPALY, false);
        repalyTxt = SharedPreferencesUtil.getString(Const.Msg.WX_AUTO_REPALY_TXT, Const.Msg.AUTO_REPALY_DEFAULT);

        if (event.getPackageName().toString().equals(QQ_PACKAGE_NAME)) {
            try {
                if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                    notifyQq(event);
                } else if (findViewByID(QQ_INPUT_ID) != null && autoMoney && hongBaoComing) {
                    clickHongBaoItems();
                } else if (autoReply && newMsg) {
                    autoReplyMsg();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void autoReplyMsg() throws InterruptedException {
        newMsg = false;
        AccessibilityNodeInfo inputInfo = findViewByID(QQ_INPUT_ID);
        if (inputInfo != null) {
            if (nameSet.contains(currentName)) {
                repalyTxt = dataSource.getReply(recivedMsg, currentName);
            } else {
                nameSet.add(currentName);
            }
            inputText(inputInfo, replyName + repalyTxt);
            Thread.sleep(200);
            newMsg = false;
            AccessibilityNodeInfo sendBtn = findViewByID(QQ_MSG_SEND);
            performViewClick(sendBtn);
            Thread.sleep(2000);
            performGlobalAction(GLOBAL_ACTION_HOME);
        }
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
                    if (!name.contains(AT_ME)) {
                        return;
                    }
                    String myName = text.substring(text.indexOf("@"), text.indexOf(" "));
                    recivedMsg = text.substring(text.indexOf(myName) + myName.length() + 1, text.length());
                    Logger.e("recivedMsg：" + recivedMsg);


                    currentName = name.substring(name.indexOf(AT_ME) + AT_ME.length(), name.indexOf("(")) + " ";
                    replyName = AT + currentName;

                    Logger.e("replyName：" + replyName);
                    newMsg = true;

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
            Thread.sleep(500);
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
        dataSource = new ChatDataSource();
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
