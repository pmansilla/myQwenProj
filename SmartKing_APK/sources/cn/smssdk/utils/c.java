package cn.smssdk.utils;

import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* compiled from: LogCatHelper.java */
/* loaded from: classes.dex */
public class c {
    static {
        "0123456789ABCDEF".toCharArray();
    }

    public static String a(int i) {
        switch (i) {
            case 1:
                return "EVENT_GET_SUPPORTED_COUNTRIES";
            case 2:
                return "EVENT_GET_VERIFICATION_CODE";
            case 3:
                return "EVENT_SUBMIT_VERIFICATION_CODE";
            case 4:
                return "EVENT_GET_CONTACTS";
            case 5:
                return "EVENT_SUBMIT_USER_INFO";
            case 6:
                return "EVENT_GET_FRIENDS_IN_APP";
            case 7:
                return "EVENT_GET_NEW_FRIENDS_COUNT";
            case 8:
                return "EVENT_GET_VOICE_VERIFICATION_CODE";
            default:
                return String.valueOf(i);
        }
    }

    public static String a(List list) {
        if (list == null) {
            return "";
        }
        Iterator it = list.iterator();
        String str = "";
        while (it.hasNext()) {
            Object next = it.next();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            if (next == null) {
                next = "";
            }
            sb.append(next);
            str = sb.toString();
            if (it.hasNext()) {
                str = str + " ,";
            }
        }
        return str;
    }

    public static String a(Map map) {
        if (map == null) {
            return "";
        }
        String str = "[";
        for (Map.Entry entry : map.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(SQLBuilder.BLANK);
            sb.append(entry.getKey());
            sb.append(":");
            sb.append(entry.getValue() == null ? "" : entry.getValue());
            str = sb.toString();
        }
        return str + " ]";
    }

    public static String b(int i) {
        return i != -1 ? i != 0 ? String.valueOf(i) : "RESULT_ERROR" : "RESULT_COMPLETE";
    }
}
