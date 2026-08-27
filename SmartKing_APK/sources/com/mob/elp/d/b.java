package com.mob.elp.d;

import android.content.Context;
import android.graphics.Bitmap;
import com.mob.elp.d.e;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.util.concurrent.CountDownLatch;

/* compiled from: BitmapTask.java */
/* loaded from: classes.dex */
public class b implements Runnable {
    private CountDownLatch a;
    private Context b;
    private String c;
    private a d;
    private Bitmap e;
    private int f;
    private int g;
    private int h;

    /* compiled from: BitmapTask.java */
    /* loaded from: classes.dex */
    public interface a {
    }

    public b(Context context, CountDownLatch countDownLatch, int i, int i2, int i3, String str, a aVar) {
        this.a = countDownLatch;
        this.g = i;
        this.h = i2;
        this.f = i3;
        this.c = str;
        this.b = context;
        this.d = aVar;
    }

    public static String a(Context context, String str) throws Throwable {
        String str2;
        String cachePath = ResHelper.getCachePath(context, "images");
        String MD5 = Data.MD5(str);
        File file = new File(cachePath);
        if (file.exists() && file.isDirectory()) {
            String[] list = file.list();
            int length = list.length;
            for (int i = 0; i < length; i++) {
                str2 = list[i];
                if (str2.contains(MD5) || str.endsWith(str2)) {
                    break;
                }
            }
        }
        str2 = "";
        if (str2.isEmpty()) {
            return BitmapHelper.downloadBitmap(context, str);
        }
        return cachePath + str2;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            Bitmap a2 = e.a(a(this.b, this.c), this.g, this.h, ResHelper.dipToPx(this.b, 10));
            this.e = a2;
            a aVar = this.d;
            if (aVar != null) {
                ((e.a) aVar).a(this.f, a2);
            }
        } catch (Throwable th) {
            try {
                d.a().a(th);
            } finally {
                a aVar2 = this.d;
                if (aVar2 != null) {
                    ((e.a) aVar2).a(this.f, this.e);
                }
                this.a.countDown();
            }
        }
    }
}
