package com.mob.mobapm.e;

import android.text.TextUtils;
import com.mob.mobapm.core.Transaction;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class f {
    private static final Pattern a = Pattern.compile(".*?(gif|jpeg|png|jpg|bmp|tiff|webp|JPG|GIF|JPEG|PNG|BMP|TIFF|WEBP)");

    public static void a(Transaction transaction) {
        if (transaction == null || TextUtils.isEmpty(transaction.getHost()) || com.mob.mobapm.core.d.d() == null || com.mob.mobapm.core.d.d().isEmpty()) {
            return;
        }
        try {
            if (transaction.getHost().endsWith(".mob.com")) {
                return;
            }
            List list = (List) com.mob.mobapm.core.d.d().get("rules");
            if (list != null && list.size() > 0) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    if (((HashMap) it.next()).get("host").equals(transaction.getHost())) {
                        c(transaction);
                    }
                }
                if (transaction.isCreate() || !com.mob.mobapm.core.c.i) {
                }
                b(transaction);
                return;
            }
            c(transaction);
            if (transaction.isCreate()) {
            }
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: check host error: " + th, new Object[0]);
        }
    }

    public static void b(Transaction transaction) {
        if (com.mob.mobapm.core.d.d().containsKey("dnsWhiteMap")) {
            if (com.mob.mobapm.core.d.d().containsKey("dnsWhiteMap") && com.mob.mobapm.core.d.d().get("dnsWhiteMap") == null) {
                return;
            }
            try {
                HashMap hashMap = (HashMap) com.mob.mobapm.core.d.d().get("dnsWhiteMap");
                if (hashMap.containsKey(transaction.getHost())) {
                    String hostAddress = InetAddress.getByName(transaction.getHost()).getHostAddress();
                    transaction.setIp(hostAddress);
                    Object obj = hashMap.get(transaction.getHost());
                    if (obj instanceof Collection) {
                        Iterator it = ((List) obj).iterator();
                        while (it.hasNext()) {
                            if (((String) it.next()).equals(hostAddress)) {
                                transaction.setHijacked(0);
                                return;
                            }
                        }
                    }
                    transaction.setHijacked(1);
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM: prase di error:" + th, new Object[0]);
            }
        }
    }

    public static void c(Transaction transaction) {
        if (com.mob.mobapm.core.d.d() == null || com.mob.mobapm.core.d.d().isEmpty()) {
            return;
        }
        HashMap<String, Object> d = com.mob.mobapm.core.d.d();
        int i = 0;
        int intValue = (!d.containsKey("samplingRate") || d.get("samplingRate") == null) ? 0 : ((Integer) d.get("samplingRate")).intValue();
        if (d.containsKey("picSamplingRate") && d.get("picSamplingRate") != null) {
            i = ((Integer) d.get("picSamplingRate")).intValue();
        }
        int nextInt = new Random().nextInt(100);
        if (nextInt > 0 && !TextUtils.isEmpty(transaction.getPath()) && a.matcher(transaction.getPath()).matches() && nextInt <= i) {
            transaction.setCreate(true);
        } else {
            if (nextInt <= 0 || a.matcher(transaction.getPath()).matches() || nextInt > intValue) {
                return;
            }
            transaction.setCreate(true);
        }
    }
}
