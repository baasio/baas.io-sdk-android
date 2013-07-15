
package com.kth.common.utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.Window;

public class DisplayUnitConvertUtil {

    private static int[] mLocation = new int[2];

    private static Rect mStatusBarRect = new Rect();

    private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;

    public static float DPFromPixel(Context context, int pixel) {
        float scale = context.getResources().getDisplayMetrics().density;

        return (pixel / DEFAULT_HDIP_DENSITY_SCALE * scale);
    }

    public static int PixelFromDP(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;

        return (int)(dip / scale * DEFAULT_HDIP_DENSITY_SCALE);
    }

    public static Point getContentLocation(View view) {
        Point point = getScreenLocation(view);
        point.y -= getContentOffsetFromTop(view);
        return point;
    }

    public static Point getScreenLocation(View view) {
        view.getLocationOnScreen(mLocation);
        return new Point(mLocation[0], mLocation[1]);
    }

    public static int getContentOffsetFromTop(View view) {
        int offset = view.getRootView().findViewById(Window.ID_ANDROID_CONTENT).getTop();

        if (offset == 0) {
            view.getWindowVisibleDisplayFrame(mStatusBarRect);
            offset = mStatusBarRect.top;
        }

        return offset;
    }

}
