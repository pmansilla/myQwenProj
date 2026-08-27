package com.mob.commons;

import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.PrivacyPolicy;
import com.mob.tools.MobLog;
import com.mob.tools.log.NLog;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.tencent.bugly.BuglyStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/* compiled from: PolicyFetcher.java */
/* loaded from: classes.dex */
public class h {
    private static final String a = j.a() + "/privacy/policy";
    private int b = i.w();
    private String c = i.v();
    private int d = i.y();
    private String e = i.x();
    private String f = i.z();

    private void a(int i, String str, String str2) {
        PrivacyPolicy privacyPolicy = new PrivacyPolicy(str2);
        if (i == 1) {
            this.e = str2;
            this.d = privacyPolicy.getPpVersion();
            i.l(this.e);
            i.b(this.d);
        } else if (i == 2) {
            this.c = str2;
            this.b = privacyPolicy.getPpVersion();
            i.k(this.c);
            i.a(this.b);
        }
        this.f = str;
        i.m(this.f);
    }

    private boolean c(int i, Locale locale) {
        if (i == 1) {
            if (!TextUtils.isEmpty(this.e) && this.d >= a.h()) {
                return locale == null || locale.toString().equals(this.f);
            }
            return false;
        }
        if (i != 2 || TextUtils.isEmpty(this.c) || this.b < a.h()) {
            return false;
        }
        return locale == null || locale.toString().equals(this.f);
    }

    public PrivacyPolicy a(int i, Locale locale) throws Throwable {
        if (i != 1 && i != 2) {
            throw new IllegalArgumentException("Parameter 'type' should be either 1 or 2");
        }
        if (locale == null) {
            if (Build.VERSION.SDK_INT >= 24) {
                LocaleList locales = MobSDK.getContext().getResources().getConfiguration().getLocales();
                if (locales != null && !locales.isEmpty()) {
                    locale = locales.get(0);
                }
            } else {
                locale = MobSDK.getContext().getResources().getConfiguration().locale;
            }
        }
        return c(i, locale) ? i == 1 ? new PrivacyPolicy(this.e) : new PrivacyPolicy(this.c) : b(i, locale);
    }

    public PrivacyPolicy b(int i, Locale locale) throws Throwable {
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        String appkey = MobSDK.getAppkey();
        String packageName = deviceHelper.getPackageName();
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("type", String.valueOf(i)));
        arrayList.add(new KVPair<>("appkey", appkey));
        arrayList.add(new KVPair<>("apppkg", packageName));
        arrayList.add(new KVPair<>("ppVersion", String.valueOf(i == 1 ? i.y() : i.w())));
        arrayList.add(new KVPair<>("language", locale.toString()));
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        networkTimeOut.connectionTimeout = 10000;
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
        MobLog.getInstance().d("Request: " + a + "\nHeaders: " + arrayList2 + "\nValues: " + arrayList, new Object[0]);
        String httpGet = new NetworkHelper().httpGet(a, arrayList, arrayList2, networkTimeOut);
        NLog mobLog = MobLog.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("Response: ");
        sb.append(httpGet);
        mobLog.d(sb.toString(), new Object[0]);
        Hashon hashon = new Hashon();
        HashMap fromJson = hashon.fromJson(httpGet);
        if (fromJson == null) {
            throw new Throwable("Response is illegal: " + httpGet);
        }
        if (!"200".equals(String.valueOf(fromJson.get("code")))) {
            throw new Throwable("Response code is not 200: " + httpGet);
        }
        Object obj = fromJson.get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
        if (obj == null) {
            throw new Throwable("Response is illegal: " + httpGet);
        }
        String fromObject = hashon.fromObject(obj);
        if (!TextUtils.isEmpty(fromObject)) {
            a(i, locale.toString(), fromObject);
            return new PrivacyPolicy(fromObject);
        }
        throw new Throwable("Response is illegal: " + httpGet);
    }
}
