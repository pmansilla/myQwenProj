package com.loc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
final class dn {
    private static Pattern a = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(String str) {
        if (str != null) {
            char[] charArray = str.toCharArray();
            if (charArray.length > 0 && charArray.length <= 255) {
                for (char c : charArray) {
                    if ((c < 'A' || c > 'Z') && ((c < 'a' || c > 'z') && !((c >= '0' && c <= '9') || c == '.' || c == '-'))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(String str) {
        return str != null && str.length() >= 7 && str.length() <= 15 && !str.equals("") && a.matcher(str).matches();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String c(String str) {
        return new BigInteger(1, MessageDigest.getInstance("md5").digest(str.getBytes())).toString(16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String d(String str) {
        return new BigInteger(1, MessageDigest.getInstance("SHA1").digest(str.getBytes())).toString(16);
    }
}
