package com.dhht.nana.service;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;

import com.dhht.nana.R;
import com.dhht.nana.app.Const;
import com.dhht.nana.jump.Jump;
import com.dhht.nana.jump.OnJump;

import java.util.List;

import toast.ToastUtil;


public class JumpService extends BaseAccessbilityService {

    private static final String JUMP_NAME_ID = "com.tencent.mm:id/cx";
    private static final String JUMP_NAME = "跳一跳";
    private static final Point START_POINT = new Point(540, 1465);
    public static boolean isOpenScreenCut;

    private boolean isFirstComingWx = true;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (isFirstComingWx && isOpenScreenCut) {
            List<AccessibilityNodeInfo> nodeInfoList = findViewsByID(JUMP_NAME_ID);
            for (AccessibilityNodeInfo info : nodeInfoList) {
                if (info.getText().toString().equals(JUMP_NAME)) {
                    performViewClick(info);
                    isFirstComingWx = false;
                    try {
                        Thread.sleep(6000);
                        clickOnScreen(START_POINT.x, START_POINT.y, 100, null);
                        prepareJump();
                        Thread.sleep(3000);
                        clickListener.onClick(btnView);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        try {
            windowManager.removeView(btnView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void prepareJump() {
        jump.setOnJump(new OnJump() {
            @Override
            public void jumpStart(int x, int y, int duration) {
                clickOnScreen(x, y, duration, null);
            }
        });
        createWindowView();
    }


    WindowManager.LayoutParams params;
    WindowManager windowManager;
    ImageView btnView;
    public static final int FLAG_LAYOUT_INSET_DECOR = 0x00000200;

    private void createWindowView() {
        btnView = new ImageView(getApplicationContext());
        btnView.setImageResource(R.drawable.ic_star);
        windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        // 设置Window Type
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        // 设置悬浮框不可触摸
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | FLAG_LAYOUT_INSET_DECOR;
        // 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应
        params.format = PixelFormat.RGBA_8888;
        // 设置悬浮框的宽高
        params.width = 200;
        params.height = 200;
        params.gravity = Gravity.TOP;
        params.x = 300;
        params.y = 200;

        btnView.setOnTouchListener(new View.OnTouchListener() {

            //保存悬浮框最后位置的变量
            int lastX, lastY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        windowManager.updateViewLayout(btnView, params);
                        break;
                }
                return false;
            }
        });
        btnView.setOnClickListener(clickListener);
        windowManager.addView(btnView, params);
    }


    Jump jump = new Jump();

    View.OnClickListener clickListener = v -> {
        if (Jump.start_model >= Const.RUN_MODEL_TEST_PIC) {
            jump.start();
            return;
        }
        if (!Jump.start) {
            ToastUtil.showLong("点击星星暂停");
            Jump.start = true;
            jump.start();
        } else {
            ToastUtil.showShort("已暂停");
            Jump.start = false;
        }
    };

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        if (!isOpenScreenCut) {
            ToastUtil.showShort("必须从App入口打开辅助");
            stopSelf();
            return;
        }
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
