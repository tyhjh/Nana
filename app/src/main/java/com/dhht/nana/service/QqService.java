package com.dhht.nana.service;

import android.view.accessibility.AccessibilityEvent;

/**
 * @author Tyhj
 * @date 2019/6/30
 */

public class QqService extends BaseAccessbilityService {

    private static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";



    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName().toString().equals(QQ_PACKAGE_NAME)) {

        }
    }

    @Override
    public void onInterrupt() {

    }
}
