package com.mob.elp.b;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import com.mob.elp.PushMessage;
import com.mob.elp.d.d;
import com.mob.elp.d.e;
import com.mob.mcl.MCLSDK;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import java.util.ArrayList;

/* compiled from: InAppMessage.java */
/* loaded from: classes.dex */
public class a {
    private static a b;
    private b a;

    /* compiled from: InAppMessage.java */
    /* renamed from: com.mob.elp.b.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    class C0054a implements e.b {
        final /* synthetic */ PushMessage a;
        final /* synthetic */ String b;
        final /* synthetic */ Activity c;

        /* compiled from: InAppMessage.java */
        /* renamed from: com.mob.elp.b.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class C0055a implements Handler.Callback {
            final /* synthetic */ ArrayList a;

            C0055a(ArrayList arrayList) {
                this.a = arrayList;
            }

            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                if (a.this.a != null && a.this.a.isShowing()) {
                    com.mob.elp.a.a b = com.mob.elp.a.a.b();
                    C0054a c0054a = C0054a.this;
                    b.a(c0054a.b, c0054a.a);
                    return false;
                }
                C0054a c0054a2 = C0054a.this;
                a.this.a = b.a(c0054a2.c, c0054a2.a, this.a);
                a.this.a.show();
                d.a().a(C0054a.this.b + " is show");
                return false;
            }
        }

        C0054a(PushMessage pushMessage, String str, Activity activity) {
            this.a = pushMessage;
            this.b = str;
            this.c = activity;
        }

        @Override // com.mob.elp.d.e.b
        public void a(ArrayList<Bitmap> arrayList) {
            if (arrayList != null) {
                try {
                    if ((this.a.unfold.showType == 1 && arrayList.size() >= 4) || (this.a.unfold.showType != 1 && arrayList.size() >= 1)) {
                        UIHandler.sendEmptyMessage(0, new C0055a(arrayList));
                        return;
                    }
                } catch (Throwable th) {
                    d.a().a(th);
                    com.mob.elp.a.a b = com.mob.elp.a.a.b();
                    String str = this.b;
                    if (b == null) {
                        throw null;
                    }
                    MCLSDK.deleteMsg(str);
                    return;
                }
            }
            com.mob.elp.a.a b2 = com.mob.elp.a.a.b();
            String str2 = this.b;
            if (b2 == null) {
                throw null;
            }
            MCLSDK.deleteMsg(str2);
        }
    }

    public static a b() {
        if (b == null) {
            synchronized (a.class) {
                if (b == null) {
                    b = new a();
                }
            }
        }
        return b;
    }

    public b a() {
        return this.a;
    }

    public void a(Activity activity, PushMessage pushMessage, String str) {
        int i;
        int dipToPx;
        int dipToPx2;
        try {
            e eVar = new e();
            ArrayList<String> arrayList = new ArrayList<>();
            int screenWidth = ResHelper.getScreenWidth(activity);
            int i2 = 1;
            if (pushMessage.unfold.showType == 1) {
                arrayList.addAll(pushMessage.unfold.images);
                i2 = (screenWidth - ResHelper.dipToPx(activity, 54)) / 4;
                i = ResHelper.dipToPx(activity, 60);
            } else {
                if (pushMessage.unfold.showType == 2) {
                    arrayList.add(pushMessage.unfold.image);
                    dipToPx = ResHelper.dipToPx(activity, 90);
                    dipToPx2 = screenWidth - ResHelper.dipToPx(activity, 42);
                } else if (pushMessage.unfold.showType == 3) {
                    arrayList.add(pushMessage.unfold.image);
                    i2 = ResHelper.dipToPx(activity, 45);
                    i = i2;
                } else if (pushMessage.unfold.showType == 4) {
                    arrayList.add(pushMessage.unfold.image);
                    dipToPx = ResHelper.dipToPx(activity, 100);
                    dipToPx2 = screenWidth - ResHelper.dipToPx(activity, 12);
                } else {
                    i = 1;
                }
                int i3 = dipToPx;
                i2 = dipToPx2;
                i = i3;
            }
            eVar.a(arrayList, i2, i, new C0054a(pushMessage, str, activity));
        } catch (Throwable th) {
            d.a().a(th);
            if (com.mob.elp.a.a.b() == null) {
                throw null;
            }
            MCLSDK.deleteMsg(str);
        }
    }
}
