package com.czw.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.ScrollView;

/* loaded from: classes.dex */
public class ViewUtil {
    static Context mContext;

    public static int dip2px(float f) {
        return dip2px(mContext, f);
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int i = 0;
        for (int i2 = 0; i2 < scrollView.getChildCount(); i2++) {
            i += scrollView.getChildAt(i2).getHeight();
            scrollView.getChildAt(i2).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        Bitmap createBitmap = Bitmap.createBitmap(scrollView.getWidth(), i, Bitmap.Config.RGB_565);
        scrollView.draw(new Canvas(createBitmap));
        return ImageUtil.compressImage(createBitmap);
    }

    public static Bitmap getCacheBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap drawingCache = view.getDrawingCache();
        if (drawingCache == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(drawingCache);
        view.setDrawingCacheEnabled(false);
        return createBitmap;
    }

    public static void load(Context context) {
        mContext = context;
    }

    public static int px2dip(float f) {
        return px2dip(mContext, f);
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
