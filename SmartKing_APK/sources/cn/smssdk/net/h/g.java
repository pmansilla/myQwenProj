package cn.smssdk.net.h;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/* compiled from: SignUtil.java */
/* loaded from: classes.dex */
public class g {
    private static String a = "utf8";

    public static String a(HashMap<String, Object> hashMap, String str) {
        if (hashMap == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        TreeMap treeMap = new TreeMap();
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry entry2 : treeMap.entrySet()) {
            if (!"sign".equals(entry2.getKey()) && !b.a(entry2.getValue())) {
                sb.append(String.format("%s=%s&", entry2.getKey(), entry2.getValue()));
            }
        }
        return f.a(sb.substring(0, sb.length() - 1) + str, a);
    }
}
