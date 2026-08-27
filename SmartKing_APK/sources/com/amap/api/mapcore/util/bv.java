package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;

/* compiled from: OfflineMapRemoveTask.java */
/* loaded from: classes.dex */
public class bv {
    private Context a;

    public bv(Context context) {
        this.a = context;
    }

    private boolean a(String str, Context context, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String b = fr.b(context);
        try {
            File file = new File(b + str2 + str + ".dat");
            if (file.exists() && !cm.b(file)) {
                cm.a("deleteDownload delete some thing wrong!");
                return false;
            }
            try {
                cm.b(b + str2);
                cm.b(str, context);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e2) {
                e2.printStackTrace();
                return false;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            return false;
        } catch (Exception e4) {
            e4.printStackTrace();
            return false;
        }
    }

    private boolean b(bo boVar) {
        if (boVar == null) {
            return false;
        }
        String pinyin = boVar.getPinyin();
        boolean a = a(pinyin, this.a, "vmap/");
        if (pinyin.equals("quanguogaiyaotu")) {
            pinyin = "quanguo";
        }
        boolean z = true;
        boolean z2 = a(pinyin, this.a, "map/") || a;
        if (!b(cm.c(boVar.getUrl()), this.a, "map/") && !z2) {
            z = false;
        }
        if (z) {
            boVar.i();
            return z;
        }
        boVar.h();
        return false;
    }

    private boolean b(String str, Context context, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String a = fr.a(context);
        try {
            File file = new File(a + str2 + str);
            if (file.exists() && !cm.b(file)) {
                cm.a("deleteDownload delete some thing wrong!");
                return false;
            }
            try {
                cm.b(a + str2);
                cm.b(str, context);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e2) {
                e2.printStackTrace();
                return false;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            return false;
        } catch (Exception e4) {
            e4.printStackTrace();
            return false;
        }
    }

    public void a(bo boVar) {
        b(boVar);
    }
}
