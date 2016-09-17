package com.pingl.test.dialogtest.utils;

import android.content.res.Resources;

/**
 * Created by pingL on 16/9/16,上午2:18.
 */
public class Utils {
    public Utils() {
        super();
    }

    public static float dp2px (Resources resources, float dp) {
        float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5F;
    }

    public static float sp2dx (Resources resources, float sp) {
        float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
