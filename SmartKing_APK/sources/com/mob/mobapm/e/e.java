package com.mob.mobapm.e;

import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class e {
    public static String a(String str) {
        try {
            return InetAddress.getByName(str).getHostAddress();
        } catch (UnknownHostException e) {
            com.mob.mobapm.d.a.a().i(e);
            return null;
        }
    }
}
