package com.czw.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes.dex */
public class AgeUtils {
    public static int getAgeFromBirthTime(long j) {
        return getAgeFromBirthTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(j * 1000)));
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0047, code lost:
    
        return 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0045, code lost:
    
        if (r4 > 0) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0042, code lost:
    
        if (r5 >= 0) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getAgeFromBirthTime(java.lang.String r8) {
        /*
            java.lang.String r8 = r8.trim()
            java.lang.String r0 = "-"
            java.lang.String[] r8 = r8.split(r0)
            r0 = 0
            r1 = r8[r0]
            int r1 = java.lang.Integer.parseInt(r1)
            r2 = 1
            r3 = r8[r2]
            int r3 = java.lang.Integer.parseInt(r3)
            r4 = 2
            r8 = r8[r4]
            int r8 = java.lang.Integer.parseInt(r8)
            java.util.Calendar r5 = java.util.Calendar.getInstance()
            int r6 = r5.get(r2)
            int r4 = r5.get(r4)
            int r4 = r4 + r2
            r7 = 5
            int r5 = r5.get(r7)
            int r1 = r6 - r1
            int r4 = r4 - r3
            int r5 = r5 - r8
            if (r1 >= 0) goto L38
            goto L5e
        L38:
            if (r1 != 0) goto L49
            if (r4 >= 0) goto L3d
            goto L5e
        L3d:
            if (r4 != 0) goto L45
            if (r5 >= 0) goto L42
            goto L5e
        L42:
            if (r5 < 0) goto L5d
            goto L47
        L45:
            if (r4 <= 0) goto L5d
        L47:
            r0 = 1
            goto L5e
        L49:
            if (r1 <= 0) goto L5d
            if (r4 >= 0) goto L4e
            goto L5d
        L4e:
            if (r4 != 0) goto L58
            if (r5 >= 0) goto L53
            goto L5d
        L53:
            if (r5 < 0) goto L5d
            int r0 = r1 + 1
            goto L5e
        L58:
            if (r4 <= 0) goto L5d
            int r0 = r1 + 1
            goto L5e
        L5d:
            r0 = r1
        L5e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.utils.AgeUtils.getAgeFromBirthTime(java.lang.String):int");
    }
}
