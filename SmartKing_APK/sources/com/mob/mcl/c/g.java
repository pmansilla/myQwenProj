package com.mob.mcl.c;

import java.io.Serializable;

/* compiled from: Message.java */
/* loaded from: classes.dex */
public class g implements Serializable {
    public final int a;
    public final int b;
    public long c;
    public String d;

    g(int i, int i2, long j, String str) {
        this.a = i;
        this.b = i2;
        this.c = j;
        this.d = str;
    }

    public g(int i, String str) {
        this(str != null ? str.length() : 0, i, 0L, str);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x004e A[LOOP:0: B:2:0x0005->B:17:0x004e, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0052 A[EDGE_INSN: B:18:0x0052->B:19:0x0052 BREAK  A[LOOP:0: B:2:0x0005->B:17:0x004e], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<com.mob.mcl.c.g> a(java.nio.ByteBuffer r10) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
        L5:
            int r1 = r10.remaining()
            r2 = 17
            if (r1 < r2) goto L52
            byte r1 = r10.get()
            r2 = 1
            r3 = 0
            if (r1 == r2) goto L16
            goto L22
        L16:
            int r5 = r10.getInt()
            int r6 = r10.getInt()
            r1 = 9999(0x270f, float:1.4012E-41)
            if (r6 <= r1) goto L24
        L22:
            r1 = r3
            goto L2f
        L24:
            long r7 = r10.getLong()
            com.mob.mcl.c.g r1 = new com.mob.mcl.c.g
            r9 = 0
            r4 = r1
            r4.<init>(r5, r6, r7, r9)
        L2f:
            if (r1 == 0) goto L4b
            int r2 = r1.a
            if (r2 <= 0) goto L4b
            int r4 = r10.remaining()
            if (r2 <= r4) goto L3d
            r1 = r3
            goto L4b
        L3d:
            int r2 = r1.a
            byte[] r2 = new byte[r2]
            r10.get(r2)
            java.lang.String r3 = new java.lang.String
            r3.<init>(r2)
            r1.d = r3
        L4b:
            if (r1 != 0) goto L4e
            goto L52
        L4e:
            r0.add(r1)
            goto L5
        L52:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.mcl.c.g.a(java.nio.ByteBuffer):java.util.List");
    }
}
