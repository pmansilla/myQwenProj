package com.mob.mobapm.proxy;

import com.mob.mobapm.core.Transaction;
import com.mob.tools.proguard.ClassKeeper;
import java.net.Socket;

/* loaded from: classes.dex */
public class APMSocketInstrumentation implements ClassKeeper {
    private Socket impl;
    private Transaction transaction;

    public static Socket wrapSocket(Socket socket) {
        return new com.mob.mobapm.proxy.e.a(socket);
    }
}
