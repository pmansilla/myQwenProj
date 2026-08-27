package com.loc;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

/* loaded from: classes.dex */
final class dg {
    private static dg a = new dg();
    private static ConcurrentMap b;
    private static ConcurrentSkipListSet c;

    private dg() {
        b = new ConcurrentHashMap();
        c = new ConcurrentSkipListSet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static dg a() {
        return a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static dh a(String str) {
        return (dh) b.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(String str, dh dhVar) {
        b.put(str, dhVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b() {
        return b.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(String str) {
        return c.contains(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c() {
        b.clear();
        c.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(String str) {
        c.add(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList d() {
        return new ArrayList(b.keySet());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void d(String str) {
        c.remove(str);
    }
}
