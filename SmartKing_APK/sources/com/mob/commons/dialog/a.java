package com.mob.commons.dialog;

import com.mob.OperationCallback;
import com.mob.commons.MobProduct;
import com.mob.commons.dialog.entity.InternalPolicyUi;
import com.mob.tools.MobLog;

/* compiled from: AuthDialogManager.java */
/* loaded from: classes.dex */
public class a {
    private static a a;
    private MobProduct b;

    private a() {
    }

    public static a a() {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a();
                }
            }
        }
        return a;
    }

    public void a(MobProduct mobProduct, InternalPolicyUi internalPolicyUi, OperationCallback<Boolean> operationCallback) {
        try {
            MobLog.getInstance().d("canIContinueBusiness()", new Object[0]);
            this.b = mobProduct;
            boolean c = com.mob.commons.a.c();
            MobLog.getInstance().d("====> ppNece: " + c, new Object[0]);
            if (!c) {
                if (operationCallback != null) {
                    operationCallback.onComplete(true);
                    return;
                }
                return;
            }
            boolean d = com.mob.commons.a.d();
            MobLog.getInstance().d("====> ppGrtd: " + d, new Object[0]);
            if (d) {
                if (operationCallback != null) {
                    operationCallback.onComplete(true);
                }
            } else if (operationCallback != null) {
                operationCallback.onComplete(false);
            }
        } catch (Throwable th) {
            MobLog.getInstance().e(th);
            if (operationCallback != null) {
                operationCallback.onFailure(th);
            }
        }
    }
}
