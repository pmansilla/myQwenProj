package cn.smssdk.utils;

/* compiled from: Constants.java */
/* loaded from: classes.dex */
public class a {
    public static String a = "http://init.sms.mob.com/v3/sdk/init";
    public static Boolean b = false;
    public static Boolean c = false;
    public static Boolean d = true;
    public static final int e;

    static {
        Boolean.valueOf(true);
        int i = 0;
        for (String str : "3.8.3".split("\\.")) {
            i = (i * 100) + Integer.parseInt(str);
        }
        e = i;
    }
}
