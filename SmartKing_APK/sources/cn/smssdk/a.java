package cn.smssdk;

/* compiled from: InitConfig.java */
/* loaded from: classes.dex */
public class a {
    private static a a;

    private a() {
    }

    public static synchronized a a() {
        a aVar;
        synchronized (a.class) {
            if (a == null) {
                a = new a();
            }
            aVar = a;
        }
        return aVar;
    }
}
