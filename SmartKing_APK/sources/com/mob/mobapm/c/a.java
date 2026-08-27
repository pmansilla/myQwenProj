package com.mob.mobapm.c;

import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.mobapm.bean.TransactionType;
import com.mob.mobapm.core.Transaction;
import com.mob.mobapm.core.c;
import com.mob.mobapm.core.d;
import com.mob.mobapm.core.i;
import com.mob.mobapm.core.j;
import com.mob.mobapm.e.f;
import com.mob.tools.utils.DeviceHelper;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;

/* loaded from: classes.dex */
public class a {
    public static void a(Transaction transaction, String str, int i) {
        com.mob.mobapm.d.a.a().i("APM: request end, transaction switch is " + transaction.isCreate(), new Object[0]);
        if (!c.e || transaction == null || !transaction.isCreate() || transaction.getTransStatus() == 2) {
            return;
        }
        try {
            transaction.setTransStatus(2);
            transaction.setResponseTime(System.currentTimeMillis());
            transaction.setRequestDuration(transaction.getResponseTime() - transaction.getRequestTime());
            transaction.setStatus(i);
            if (!TextUtils.isEmpty(str)) {
                transaction.setMethod(str);
            }
            com.mob.mobapm.d.a.a().d("APM: start inserting this transcation:" + transaction, new Object[0]);
            j.d().a(transaction);
            if (TextUtils.isEmpty(transaction.getBody()) && TextUtils.isEmpty(transaction.getQuery())) {
                return;
            }
            i.d().a(transaction);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM: an error occurred while inserting this data:" + th, new Object[0]);
        }
    }

    public static void a(Transaction transaction, String str, String str2, TransactionType transactionType) {
        com.mob.mobapm.d.a.a().i("APM: request prepare, switch is " + c.e, new Object[0]);
        if (!c.e || transaction == null) {
            return;
        }
        if (transactionType != null) {
            try {
                if (!TextUtils.isEmpty(str2)) {
                    transaction.setPath(str2);
                    transaction.setTransType(transactionType);
                    transaction.setImei(DeviceHelper.getInstance(MobSDK.getContext()).getIMEI());
                    transaction.setRequestTime(System.currentTimeMillis());
                    transaction.setMac(DeviceHelper.getInstance(MobSDK.getContext()).getMacAddress());
                    transaction.setHost(str);
                    f.a(transaction);
                    transaction.setDuid(d.e());
                    transaction.setClientTime(System.currentTimeMillis());
                    transaction.setNetworkType(DeviceHelper.getInstance(MobSDK.getContext()).getNetworkType());
                    transaction.setDataNetworkType(String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getDataNtType()));
                    transaction.setTransStatus(1);
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM: request prepare error:" + th, new Object[0]);
                return;
            }
        }
        if (transactionType == null && TextUtils.isEmpty(str2)) {
            transaction.setRequestTime(System.currentTimeMillis());
        }
        transaction.setHost(str);
        f.a(transaction);
        transaction.setDuid(d.e());
        transaction.setClientTime(System.currentTimeMillis());
        transaction.setNetworkType(DeviceHelper.getInstance(MobSDK.getContext()).getNetworkType());
        transaction.setDataNetworkType(String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getDataNtType()));
        transaction.setTransStatus(1);
    }

    public static void a(Transaction transaction, Throwable th) {
        com.mob.mobapm.d.a.a().i("APM: request error! transaction switch is " + transaction.isCreate(), new Object[0]);
        if (c.e && transaction != null && transaction.isCreate()) {
            transaction.setErrMsg(th.getMessage());
            int i = -1;
            if (th instanceof UnknownHostException) {
                i = 901;
            } else if (th instanceof SocketTimeoutException) {
                i = 903;
            } else if (th instanceof ConnectException) {
                i = 902;
            } else if (th instanceof SSLException) {
                i = 908;
            }
            a(transaction, "", i);
        }
    }

    public static void a(Transaction transaction, HttpURLConnection httpURLConnection) {
        com.mob.mobapm.d.a.a().i("APM: request end, transaction switch is " + transaction.isCreate(), new Object[0]);
        if (c.e && transaction != null && transaction.isCreate()) {
            int i = -1;
            try {
                i = httpURLConnection.getResponseCode();
            } catch (Throwable th) {
                if (th instanceof UnknownHostException) {
                    i = 901;
                } else if (th instanceof SocketTimeoutException) {
                    i = 903;
                } else if (th instanceof ConnectException) {
                    i = 902;
                } else if (th instanceof SSLException) {
                    i = 908;
                }
                com.mob.mobapm.d.a.a().i("APM: get response code exception :" + th, new Object[0]);
            }
            if (i >= 300) {
                try {
                    transaction.setErrMsg(com.mob.mobapm.e.d.b(httpURLConnection.getInputStream()));
                } catch (Throwable unused) {
                }
            }
            a(transaction, httpURLConnection.getRequestMethod(), i);
        }
    }

    public static void a(Transaction transaction, HttpURLConnection httpURLConnection, String str) {
        com.mob.mobapm.d.a.a().i("APM: request error! transaction switch is " + transaction.isCreate(), new Object[0]);
        if (c.e && transaction != null && transaction.isCreate()) {
            int i = -1;
            try {
                i = httpURLConnection.getResponseCode();
            } catch (Throwable th) {
                if (th instanceof UnknownHostException) {
                    i = 901;
                } else if (th instanceof SocketTimeoutException) {
                    i = 903;
                } else if (th instanceof ConnectException) {
                    i = 902;
                } else if (th instanceof SSLException) {
                    i = 908;
                }
                com.mob.mobapm.d.a.a().i("APM: get response code exception :" + th, new Object[0]);
            }
            transaction.setErrMsg(str);
            a(transaction, httpURLConnection.getRequestMethod(), i);
        }
    }

    public static void b(Transaction transaction, HttpURLConnection httpURLConnection) {
        com.mob.mobapm.d.a.a().i("APM: request prepare, switch is " + c.e, new Object[0]);
        if (!c.e || transaction == null) {
            return;
        }
        try {
            transaction.setTransType(httpURLConnection.getURL().getProtocol().equals("http") ? TransactionType.http : TransactionType.https);
            transaction.setHost(httpURLConnection.getURL().getHost());
            transaction.setPath(httpURLConnection.getURL().getPath());
            String query = httpURLConnection.getURL().getQuery();
            if (!TextUtils.isEmpty(query) && query.getBytes().length <= c.j) {
                transaction.setQuery(query);
            }
            transaction.setImei(DeviceHelper.getInstance(MobSDK.getContext()).getIMEI());
            transaction.setDuid(d.e());
            transaction.setClientTime(System.currentTimeMillis());
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM: request prepare error:" + th, new Object[0]);
        }
    }

    public static void c(Transaction transaction, HttpURLConnection httpURLConnection) {
        com.mob.mobapm.d.a.a().i("APM: request start", new Object[0]);
        if (!c.e || transaction == null || transaction.getTransStatus() >= 1) {
            return;
        }
        try {
            transaction.setMac(DeviceHelper.getInstance(MobSDK.getContext()).getMacAddress());
            transaction.setNetworkType(DeviceHelper.getInstance(MobSDK.getContext()).getNetworkType());
            transaction.setDataNetworkType(String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getDataNtType()));
            transaction.setRequestTime(System.currentTimeMillis());
            transaction.setTransStatus(1);
            if (httpURLConnection != null) {
                transaction.setMethod(httpURLConnection.getRequestMethod());
            }
            f.a(transaction);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM: request start error:" + th, new Object[0]);
        }
    }
}
