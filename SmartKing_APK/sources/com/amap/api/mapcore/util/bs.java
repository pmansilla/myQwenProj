package com.amap.api.mapcore.util;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: OfflineMapDataVerify.java */
/* loaded from: classes.dex */
public class bs extends Thread {
    private Context a;
    private ce b;

    public bs(Context context) {
        this.a = context;
        this.b = ce.a(context);
    }

    private bz a(File file) {
        String a = fr.a(file);
        bz bzVar = new bz();
        bzVar.b(a);
        return bzVar;
    }

    private bz a(String str) {
        if (str.equals("quanguo")) {
            str = "quanguogaiyaotu";
        }
        bp a = bp.a(this.a);
        if (a == null) {
            return null;
        }
        String f = a.f(str);
        File[] listFiles = new File(fr.c(this.a)).listFiles();
        if (listFiles == null) {
            return null;
        }
        bz bzVar = null;
        for (File file : listFiles) {
            if (((file.getName().contains(f) || file.getName().contains(str)) && file.getName().endsWith(".zip.tmp.dt")) && (bzVar = a(file)) != null && bzVar.d() != null) {
                return bzVar;
            }
        }
        return bzVar;
    }

    private void a() {
        bz a;
        String c;
        int indexOf;
        boolean z;
        String c2;
        int indexOf2;
        String c3;
        int indexOf3;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<bz> a2 = this.b.a();
        a(arrayList, "vmap/");
        a(arrayList, "map/");
        b(arrayList, "map/");
        ArrayList<String> b = b();
        Iterator<bz> it = a2.iterator();
        while (it.hasNext()) {
            bz next = it.next();
            if (next != null && next.d() != null) {
                if (next.l == 4 || next.l == 7) {
                    boolean contains = arrayList.contains(next.i());
                    if (!contains && (c = cm.c(next.g())) != null && (indexOf = arrayList.indexOf(c)) != -1) {
                        arrayList.set(indexOf, next.i());
                        contains = true;
                    }
                    if (!contains) {
                        this.b.b(next);
                    }
                } else {
                    if (next.l == 0 || next.l == 1) {
                        z = b.contains(next.f()) || b.contains(next.i());
                        if (!z && (c2 = cm.c(next.g())) != null && (indexOf2 = b.indexOf(c2)) != -1) {
                            b.set(indexOf2, next.i());
                            z = true;
                        }
                        if (!z) {
                            this.b.b(next);
                        }
                    } else if (next.l == 3 && next.h() != 0) {
                        z = b.contains(next.f()) || b.contains(next.i());
                        if (!z && (c3 = cm.c(next.g())) != null && (indexOf3 = b.indexOf(c3)) != -1) {
                            b.set(indexOf3, next.i());
                            z = true;
                        }
                        if (!z) {
                            this.b.b(next);
                        }
                    }
                }
            }
        }
        Iterator<String> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            String next2 = it2.next();
            if (!a(next2, a2) && (a = a(next2)) != null) {
                this.b.a(a);
            }
        }
        bp a3 = bp.a(this.a);
        if (a3 != null) {
            a3.a((ArrayList<bz>) null);
        }
    }

    private void a(ArrayList<String> arrayList, String str) {
        File[] listFiles;
        String name;
        int lastIndexOf;
        File file = new File(fr.b(this.a) + str);
        if (file.exists() && (listFiles = file.listFiles()) != null) {
            for (File file2 : listFiles) {
                if (file2.getName().endsWith(".dat") && (lastIndexOf = (name = file2.getName()).lastIndexOf(46)) > -1 && lastIndexOf < name.length()) {
                    String substring = name.substring(0, lastIndexOf);
                    if (!arrayList.contains(substring)) {
                        arrayList.add(substring);
                    }
                }
            }
        }
    }

    private boolean a(String str, ArrayList<bz> arrayList) {
        Iterator<bz> it = arrayList.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().i())) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> b() {
        File[] listFiles;
        String name;
        int lastIndexOf;
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new File(fr.c(this.a));
        if (!file.exists() || (listFiles = file.listFiles()) == null) {
            return arrayList;
        }
        for (File file2 : listFiles) {
            if (file2.getName().endsWith(".zip") && (lastIndexOf = (name = file2.getName()).lastIndexOf(46)) > -1 && lastIndexOf < name.length()) {
                arrayList.add(name.substring(0, lastIndexOf));
            }
        }
        return arrayList;
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x0089, code lost:
    
        if (r9 != false) goto L42;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0092 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void b(java.util.ArrayList<java.lang.String> r13, java.lang.String r14) {
        /*
            r12 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            android.content.Context r1 = r12.a
            java.lang.String r1 = com.amap.api.mapcore.util.fr.a(r1)
            r0.append(r1)
            r0.append(r14)
            java.io.File r14 = new java.io.File
            java.lang.String r0 = r0.toString()
            r14.<init>(r0)
            boolean r0 = r14.exists()
            if (r0 != 0) goto L21
            return
        L21:
            java.io.File[] r14 = r14.listFiles()
            if (r14 != 0) goto L28
            return
        L28:
            int r0 = r14.length
            r1 = 0
            r2 = 0
        L2b:
            if (r2 >= r0) goto L95
            r3 = r14[r2]
            boolean r4 = r3.isDirectory()
            if (r4 == 0) goto L92
            java.lang.String r4 = r3.getName()
            boolean r5 = android.text.TextUtils.isEmpty(r4)
            if (r5 != 0) goto L92
            java.lang.String[] r3 = r3.list()
            if (r3 == 0) goto L92
            int r5 = r3.length
            r6 = 1
            if (r5 >= r6) goto L4a
            goto L92
        L4a:
            boolean r5 = r13.contains(r4)
            if (r5 != 0) goto L92
            java.lang.String r5 = "a0"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L6a
            int r5 = r3.length
            r7 = 0
        L5a:
            if (r7 >= r5) goto L8c
            r8 = r3[r7]
            java.lang.String r9 = "m1.ans"
            boolean r8 = r9.equals(r8)
            if (r8 == 0) goto L67
            goto L8d
        L67:
            int r7 = r7 + 1
            goto L5a
        L6a:
            int r5 = r3.length
            r7 = 0
            r8 = 0
            r9 = 0
        L6e:
            if (r7 >= r5) goto L87
            r10 = r3[r7]
            java.lang.String r11 = "m1.ans"
            boolean r11 = r11.equals(r10)
            if (r11 == 0) goto L7b
            r8 = 1
        L7b:
            java.lang.String r11 = "m3.ans"
            boolean r10 = r11.equals(r10)
            if (r10 == 0) goto L84
            r9 = 1
        L84:
            int r7 = r7 + 1
            goto L6e
        L87:
            if (r8 == 0) goto L8c
            if (r9 == 0) goto L8c
            goto L8d
        L8c:
            r6 = 0
        L8d:
            if (r6 == 0) goto L92
            r13.add(r4)
        L92:
            int r2 = r2 + 1
            goto L2b
        L95:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.bs.b(java.util.ArrayList, java.lang.String):void");
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            a();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
