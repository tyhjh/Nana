package com.dhht.nana.jump;

/**
 * @author Tyhj
 * @date 2019/6/25
 */

public interface OnJump {

    /**
     * 点击进行事件
     *
     * @param x
     * @param y
     * @param duration
     */
    void jumpStart(int x, int y, int duration);


    /**
     * 显示、隐藏星星
     *
     * @param show
     */
    void showBtn(boolean show);
}
