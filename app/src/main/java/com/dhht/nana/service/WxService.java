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

    public static final String HONG_BAO = "[微信红包]";
    public static final String ALREADY_GET = "已领取";

    public static final String ALREADY_GET_ID = "com.tencent.mm:id/aqk";


    Set<String> nameSet = new HashSet<>();

    public static final String wxEditTextId = "com.tencent.mm:id/ami";
    public static final String wxSendButtonId = "com.tencent.mm:id/amp";
    public static final String wxHongBaoId = "com.tencent.mm:id/aqh";

    public static final String WX_OPEN_HONG_BAO_ID = "com.tencent.mm:id/czs";

    public static final String WX_HOME_ID = "com.tencent.mm:id/rq";


    public static final String HOME_MSG_ID = "com.tencent.mm:id/b6g";

    public static final String TALK_MSG_INPUT_ID = "com.tencent.mm:id/ami";


    boolean autoReply = false;
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
        autoReply = SharedPreferencesUtil.getBoolean(WX_AUTO_REPALY, false);
        autoMoney = SharedPreferencesUtil.getBoolean(WX_MONEY, true);
        repalyTxt = SharedPreferencesUtil.getString(Const.Msg.WX_AUTO_REPALY_TXT, Const.Msg.AUTO_REPALY_DEFAULT);

        ScreenUtil.getScreenSize(this);

        Logger.e("event.getClassName()：" + event.getClassName().toString());

        screenWidth = ScreenUtil.SCREEN_WIDTH;
        screenHeight = ScreenUtil.SCREEN_HEIGHT;

        if (event.getPackageName().toString().equals(WX_PACKAGE_NAME)) {
            if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                notifyWechat(event);
            } else if (findViewByID(WX_HOME_ID) != null) {
                List<AccessibilityNodeInfo> nodeInfos = findViewsByID(HOME_MSG_ID);
                for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
                    if (nodeInfo.getText().toString().contains(HONG_BAO)) {
                        performViewClick(nodeInfo);
                        return;
                    }
                }
            } else if (findViewByID(TALK_MSG_INPUT_ID) != null) {
                try {
                    if (hongBaoComing && autoMoney) {
                        clickHongBaoItems();
                    } else if (newMsg && autoReply) {
                        autoRepaly();
                    } else if (autoMoney) {
                        clickHongBaoItems();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (waitingOpen && autoMoney) {
                try {
                    openHongBao();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clickHongBaoItems() throws InterruptedException {
        hongBaoComing = false;
        long startTime=System.currentTimeMillis();
        List<AccessibilityNodeInfo> inputInfos = findViewsByID(wxHongBaoId);
        while (inputInfos.size() == 0 && (System.currentTimeMillis() - startTime) < 1000) {
            Thread.sleep(100);
            inputInfos = findViewsByID(wxHongBaoId);
        }
        if (inputInfos == null || inputInfos.size() == 0) {
            return;
        }
        for (AccessibilityNodeInfo info : inputInfos) {
            if (info.getChildCount() == 1 || !ALREADY_GET_ID.equals(info.getChild(1).getViewIdResourceName())) {
                waitingOpen = true;
                performViewClick(info);
                return;
            }
        }

    }

    private void openHongBao() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1500) {
            Thread.sleep(100);
            clickOnScreen(screenWidth / 2, screenHeight * pointYScal, 10, null);
        }
        waitingOpen = false;
        Logger.e("使用时间为：" + (System.currentTimeMillis() - startTime));
        Thread.sleep(200);
        performGlobalAction(GLOBAL_ACTION_BACK);
    }


    private void autoRepaly() {
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

    @Override
    public void onInterrupt() {

    }


    long startTime = 0;

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
                startTime = System.currentTimeMillis();
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


    private void back() {
        performGlobalAction(GLOBAL_ACTION_HOME);
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // 创建唤醒锁
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "WxService:wakeLock");
        // 获得唤醒锁
        wakeLock.acquire();
        performGlobalAction(GLOBAL_ACTION_BACK);
        performGlobalAction(GLOBAL_ACTION_BACK);
        performGlobalAction(GLOBAL_ACTION_BACK);
    }
}
