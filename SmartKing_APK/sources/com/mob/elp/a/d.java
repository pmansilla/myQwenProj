package com.mob.elp.a;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import com.mob.elp.PushMessage;
import com.mob.elp.d.e;
import com.mob.mcl.MCLSDK;
import com.mob.tools.utils.UIHandler;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ELPImpl.java */
/* loaded from: classes.dex */
public class d implements e.b {
    final /* synthetic */ PushMessage a;
    final /* synthetic */ String b;
    final /* synthetic */ com.mob.elp.a.a c;

    /* compiled from: ELPImpl.java */
    /* loaded from: classes.dex */
    class a implements Handler.Callback {
        final /* synthetic */ ArrayList a;

        a(ArrayList arrayList) {
            this.a = arrayList;
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            com.mob.elp.c.a.a().a(d.this.a, this.a);
            com.mob.elp.d.d.a().a(d.this.b + " is create");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(com.mob.elp.a.a aVar, PushMessage pushMessage, String str) {
        this.c = aVar;
        this.a = pushMessage;
        this.b = str;
    }

    @Override // com.mob.elp.d.e.b
    public void a(ArrayList<Bitmap> arrayList) {
        if (arrayList != null) {
            try {
                if ((this.a.unfold.showType == 1 && arrayList.size() >= 4) || (this.a.unfold.showType != 1 && arrayList.size() >= 1)) {
                    UIHandler.sendEmptyMessage(0, new a(arrayList));
                    return;
                }
            } catch (Throwable th) {
                com.mob.elp.d.d.a().a(th);
                com.mob.elp.a.a aVar = this.c;
                String str = this.b;
                if (aVar == null) {
                    throw null;
                }
                MCLSDK.deleteMsg(str);
                return;
            }
        }
        com.mob.elp.a.a aVar2 = this.c;
        String str2 = this.b;
        if (aVar2 == null) {
            throw null;
        }
        MCLSDK.deleteMsg(str2);
    }
}
