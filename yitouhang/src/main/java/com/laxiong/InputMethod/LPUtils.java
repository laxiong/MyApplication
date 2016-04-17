/*
 * LPUtils.java
 *
 * Copyright (c) 2006 LightPole, Inc.
 * All rights reserved.
 *
 * No part of this source code may be copied, userd, or modified
 * without the express written consent of LightPole, Inc.
 *
 * Created on November 9, 2006, 3:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.laxiong.InputMethod;



//#define DEBUG

/**
 * Utility class.
 */
public final class LPUtils {

    public static int screenWidth_ = 320;
    public static int screenHeight_ = 480;

    // 界面参考缩放参数
    static float SCALEDATE;

    public LPUtils() {

    }

    public static int getScaledValue(int num) {
        // TODO Auto-generated method stub
        if (SCALEDATE != 0) {
            num = (int) (SCALEDATE * num);
        }
        return num;
    }

    /**
     *
     * @param screenWidth
     *            参照宽度
     */
    public static void setScaledParams(float screenWidth) {
        // TODO Auto-generated method stub
        SCALEDATE = screenWidth;
    }

}
