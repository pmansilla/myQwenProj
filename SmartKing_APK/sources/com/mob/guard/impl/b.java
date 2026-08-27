package com.mob.guard.impl;

import com.mob.MobSDK;

/* loaded from: classes.dex */
public class b {

    /* loaded from: classes.dex */
    static class a extends g {
        a() {
        }

        @Override // com.mob.guard.impl.g
        protected void a() throws Throwable {
            if (b.b() && !MobSDK.isForb()) {
                e.b();
                c.e().g();
            }
        }
    }

    public static void a() {
        new a().start();
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0030, code lost:
    
        if (r2.processName == null) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0040, code lost:
    
        if (r2.processName.equalsIgnoreCase(com.mob.MobSDK.getContext().getPackageName()) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0042, code lost:
    
        r1 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean b() {
        /*
            r0 = 0
            android.content.Context r1 = com.mob.MobSDK.getContext()     // Catch: java.lang.Throwable -> L44
            java.lang.String r2 = "activity"
            java.lang.Object r1 = r1.getSystemService(r2)     // Catch: java.lang.Throwable -> L44
            android.app.ActivityManager r1 = (android.app.ActivityManager) r1     // Catch: java.lang.Throwable -> L44
            java.util.List r1 = r1.getRunningAppProcesses()     // Catch: java.lang.Throwable -> L44
            if (r1 != 0) goto L14
            return r0
        L14:
            java.util.Iterator r1 = r1.iterator()     // Catch: java.lang.Throwable -> L44
        L18:
            boolean r2 = r1.hasNext()     // Catch: java.lang.Throwable -> L44
            if (r2 == 0) goto L4c
            java.lang.Object r2 = r1.next()     // Catch: java.lang.Throwable -> L44
            android.app.ActivityManager$RunningAppProcessInfo r2 = (android.app.ActivityManager.RunningAppProcessInfo) r2     // Catch: java.lang.Throwable -> L44
            if (r2 == 0) goto L18
            int r3 = r2.pid     // Catch: java.lang.Throwable -> L44
            int r4 = android.os.Process.myPid()     // Catch: java.lang.Throwable -> L44
            if (r3 != r4) goto L18
            java.lang.String r1 = r2.processName     // Catch: java.lang.Throwable -> L44
            if (r1 == 0) goto L4c
            java.lang.String r1 = r2.processName     // Catch: java.lang.Throwable -> L44
            android.content.Context r2 = com.mob.MobSDK.getContext()     // Catch: java.lang.Throwable -> L44
            java.lang.String r2 = r2.getPackageName()     // Catch: java.lang.Throwable -> L44
            boolean r1 = r1.equalsIgnoreCase(r2)     // Catch: java.lang.Throwable -> L44
            if (r1 == 0) goto L4c
            r1 = 1
            goto L4d
        L44:
            r1 = move-exception
            com.mob.tools.log.NLog r2 = com.mob.guard.impl.e.a()
            r2.e(r1)
        L4c:
            r1 = 0
        L4d:
            com.mob.tools.log.NLog r2 = com.mob.guard.impl.e.a()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "[MobGuard] isInMainProcess:"
            r3.append(r4)
            r3.append(r1)
            java.lang.String r3 = r3.toString()
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r2.d(r3, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.guard.impl.b.b():boolean");
    }
}
