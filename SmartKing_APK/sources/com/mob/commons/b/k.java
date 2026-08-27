package com.mob.commons.b;

import android.content.Context;
import android.content.Intent;

/* compiled from: Samsung.java */
/* loaded from: classes.dex */
public class k extends f {
    public k(Context context) {
        super(context);
    }

    @Override // com.mob.commons.b.f
    protected Intent a() {
        Intent intent = new Intent();
        intent.setClassName(com.mob.commons.k.a(115), com.mob.commons.k.a(116));
        return intent;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0056  */
    @Override // com.mob.commons.b.f
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected com.mob.commons.b.f.c a(android.os.IBinder r13) {
        /*
            r12 = this;
            r0 = 1
            r1 = 117(0x75, float:1.64E-43)
            r2 = 0
            if (r13 == 0) goto L1a
            java.lang.String r3 = com.mob.commons.k.a(r1)     // Catch: java.lang.Throwable -> L12
            android.os.IInterface r3 = r13.queryLocalInterface(r3)     // Catch: java.lang.Throwable -> L12
            if (r3 == 0) goto L1a
            r3 = 1
            goto L1b
        L12:
            r3 = move-exception
            com.mob.commons.b.c r4 = com.mob.commons.b.c.a()
            r4.a(r3)
        L1a:
            r3 = 0
        L1b:
            com.mob.commons.b.f$c r4 = new com.mob.commons.b.f$c
            r4.<init>()
            r4.a = r3
            java.lang.String r3 = r12.b
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 == 0) goto L36
            android.content.Context r3 = r12.a
            if (r3 == 0) goto L36
            android.content.Context r3 = r12.a
            java.lang.String r3 = r3.getPackageName()
            r12.b = r3
        L36:
            r3 = 69
            java.lang.String r6 = com.mob.commons.k.a(r3)
            java.lang.String r8 = com.mob.commons.k.a(r1)
            r9 = 1
            java.lang.String[] r10 = new java.lang.String[r2]
            r5 = r12
            r7 = r13
            java.lang.String r3 = r5.a(r6, r7, r8, r9, r10)
            r4.b = r3
            java.lang.String r3 = r12.b
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            r5 = 0
            if (r3 == 0) goto L56
            r3 = r5
            goto L6d
        L56:
            r3 = 70
            java.lang.String r7 = com.mob.commons.k.a(r3)
            java.lang.String r9 = com.mob.commons.k.a(r1)
            r10 = 2
            java.lang.String[] r11 = new java.lang.String[r0]
            java.lang.String r3 = r12.b
            r11[r2] = r3
            r6 = r12
            r8 = r13
            java.lang.String r3 = r6.a(r7, r8, r9, r10, r11)
        L6d:
            r4.e = r3
            java.lang.String r3 = r12.b
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 == 0) goto L78
            goto L8f
        L78:
            r3 = 75
            java.lang.String r6 = com.mob.commons.k.a(r3)
            java.lang.String r8 = com.mob.commons.k.a(r1)
            r9 = 3
            java.lang.String[] r10 = new java.lang.String[r0]
            java.lang.String r0 = r12.b
            r10[r2] = r0
            r5 = r12
            r7 = r13
            java.lang.String r5 = r5.a(r6, r7, r8, r9, r10)
        L8f:
            r4.c = r5
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.b.k.a(android.os.IBinder):com.mob.commons.b.f$c");
    }
}
