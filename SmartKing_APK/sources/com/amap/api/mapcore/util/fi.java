package com.amap.api.mapcore.util;

import android.graphics.Bitmap;

/* compiled from: PureScreenCheckTool.java */
/* loaded from: classes.dex */
public class fi {
    private static boolean a = false;
    private static boolean b = false;
    private int c = 0;
    private int d = 20;

    public static boolean a() {
        return a;
    }

    public boolean a(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                try {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int i = -1;
                    for (int i2 = (int) (width / 4.0f); i2 < (width * 3) / 4.0f; i2++) {
                        for (int i3 = (int) (height / 4.0f); i3 < (height * 3) / 4.0f; i3++) {
                            int pixel = bitmap.getPixel(i2, i3);
                            if (i == -1) {
                                i = pixel;
                            }
                            if (pixel != i) {
                                return true;
                            }
                            if (pixel != -16777216) {
                                return true;
                            }
                        }
                    }
                } catch (Throwable th) {
                    ic.c(th, "AMapdelegate", "checkBlackScreen");
                }
            }
            return false;
        } finally {
            b = true;
        }
    }

    public boolean b() {
        return b;
    }

    public void c() {
        this.c++;
    }

    public boolean d() {
        return this.c >= this.d;
    }
}
