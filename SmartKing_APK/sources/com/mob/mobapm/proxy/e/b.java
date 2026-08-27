package com.mob.mobapm.proxy.e;

import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.mobapm.bean.SocketTransaction;
import com.mob.mobapm.bean.TransactionType;
import com.mob.mobapm.core.c;
import com.mob.mobapm.core.d;
import com.mob.mobapm.core.h;
import com.mob.tools.utils.DeviceHelper;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;

/* loaded from: classes.dex */
public class b extends com.mob.mobapm.c.a {
    public static void a(SocketTransaction socketTransaction) {
        com.mob.mobapm.d.a.a().i("APM: socket connect end，transaction: " + socketTransaction, new Object[0]);
        if (!c.h || socketTransaction == null || TextUtils.isEmpty(socketTransaction.getHost())) {
            return;
        }
        try {
            if (socketTransaction.getTransStatus() != 2) {
                try {
                    socketTransaction.setTransStatus(2);
                    socketTransaction.setConnectEndTime(System.currentTimeMillis());
                    socketTransaction.setConnectDuration(socketTransaction.getConnectEndTime() - socketTransaction.getConnectBeginTime());
                    socketTransaction.setStatus(200);
                    com.mob.mobapm.d.a.a().d("APM: start inserting this socket transcation:" + socketTransaction, new Object[0]);
                    h.d().a(socketTransaction);
                } catch (Throwable th) {
                    com.mob.mobapm.d.a.a().d("APM: an error occurred while inserting this socket data:" + th, new Object[0]);
                }
            }
        } catch (Throwable th2) {
            com.mob.mobapm.d.a.a().i("APM: socket request end error： " + th2, new Object[0]);
        }
    }

    public static void a(SocketTransaction socketTransaction, Throwable th) {
        com.mob.mobapm.d.a.a().i("APM: socket connect error!", new Object[0]);
        if (!c.h || socketTransaction == null) {
            return;
        }
        int i = 200;
        try {
            if (th instanceof UnknownHostException) {
                i = 901;
            } else if (th instanceof SocketTimeoutException) {
                i = 903;
            } else if (th instanceof ConnectException) {
                i = 902;
            } else if (th instanceof SSLException) {
                i = 908;
            }
            if (socketTransaction.getTransStatus() != 2) {
                try {
                    socketTransaction.setTransStatus(2);
                    socketTransaction.setConnectEndTime(System.currentTimeMillis());
                    socketTransaction.setConnectDuration(socketTransaction.getConnectEndTime() - socketTransaction.getConnectBeginTime());
                    socketTransaction.setStatus(i);
                    com.mob.mobapm.d.a.a().d("APM: start inserting this socket transcation:" + socketTransaction, new Object[0]);
                    h.d().a(socketTransaction);
                } catch (Throwable th2) {
                    com.mob.mobapm.d.a.a().d("APM: an error occurred while inserting this socket data:" + th2, new Object[0]);
                }
            }
        } catch (Throwable th3) {
            com.mob.mobapm.d.a.a().i("APM: socket connect end error： " + th3, new Object[0]);
        }
    }

    public static void a(SocketTransaction socketTransaction, InetSocketAddress inetSocketAddress) {
        com.mob.mobapm.d.a.a().i("APM: socket start, switch is " + c.h, new Object[0]);
        if (!c.h || socketTransaction == null || inetSocketAddress == null) {
            return;
        }
        try {
            socketTransaction.setHost(inetSocketAddress.getHostName());
            socketTransaction.setPort(inetSocketAddress.getPort());
            socketTransaction.setTransType(TransactionType.socket);
            socketTransaction.setConnectBeginTime(System.currentTimeMillis());
            socketTransaction.setDuid(d.e());
            socketTransaction.setClientTime(System.currentTimeMillis());
            socketTransaction.setNetworkType(DeviceHelper.getInstance(MobSDK.getContext()).getNetworkType());
            socketTransaction.setDataNetworkType(String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getDataNtType()));
            socketTransaction.setTransStatus(1);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: socket connect start error: " + th, new Object[0]);
        }
    }
}
