package com.dhht.nana.util;

import android.graphics.Color;

import com.dhht.nana.util.color.HsvColorLike;
import com.dhht.nana.util.color.LabColorLike;
import com.dhht.nana.util.color.LikeColor;
import com.dhht.nana.util.color.RgbColorLike;


/**
 * 作者：Tyhj on 2018/10/21 21:05
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class ColorUtil {


    HsvColorLike hsvColorLike = new HsvColorLike();
    LabColorLike labColorLike = new LabColorLike();
    RgbColorLike rgbColorLike = new RgbColorLike();


    //棋子的颜色
    public static int chessColor = Color.parseColor("#2e2d41");

    public static int chessColorTop = Color.parseColor("#383845");

    public static int whiteCenterColor=Color.parseColor("#f5f5f5");

    public static int whiteTableColor=Color.parseColor("#fafafa");

    public static int buleOverColor=Color.parseColor("#0198ff");

    public static int greenOverColor=Color.parseColor("#00c776");

    public static int blackColor=Color.parseColor("#333333");
    public static int grayColor=Color.parseColor("#757575");
    public static int buleColor=Color.parseColor("#3cc81f");




    //棋子的可允许色差
    public static int ABERRATION_CHESS_RGB = 10;
    public static int ABERRATION_CHESS_LAB= 20;

    //背景颜色的色容差
    public static int ABERRATION_BG_RGB = 5;
    public static int ABERRATION_BG_LAB = 10;


    public static int ABERRATION_BG_LAB2 = 10;

    //杂色板块的色差
    public static int ABERRATION_MOTLEY_HSV = 12;
    public static int ABERRATION_MOTLEY_LAB = 160;

    //略过棋子的色容差
    public static int ABERRATION_CHESS_HSV = 7;
    public static int ABERRATION_CHESSTOP_LAB= 30;


    /**
     * 对比颜色
     *
     * @param color1
     * @param color2
     * @param aberration
     * @return
     */
    public static boolean colorLike(int color1, int color2, int aberration, LikeColor labLike) {
        return labLike.isLike(color1, color2, aberration);
    }


}
