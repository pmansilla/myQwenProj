package cn.smssdk.net.h;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/* compiled from: BaseUtils.java */
/* loaded from: classes.dex */
public class b {
    public static boolean a(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof String) && obj.equals("")) {
            return true;
        }
        return obj instanceof Collection ? ((Collection) obj).isEmpty() : obj instanceof Map ? ((Map) obj).isEmpty() : obj.getClass().isArray() && Array.getLength(obj) == 0;
    }
}
