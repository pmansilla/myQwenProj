package com.mob.mcl.a;

import android.content.Context;
import android.os.Bundle;
import com.autonavi.amap.mapcore.AeUtil;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobSDK;
import com.mob.apc.APCMessage;
import com.mob.apc.MobAPC;
import com.mob.mcl.MCLSDK;
import com.mob.mcl.c.i;
import com.mob.tools.network.HttpResponseCallback;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.network.StringPart;
import com.mob.tools.utils.Hashon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

/* compiled from: ApcHelper.java */
/* loaded from: classes.dex */
public class b {
    private static volatile b c;
    private Set<String> a;
    private String b;

    private b() {
    }

    public static b a() {
        if (c == null) {
            synchronized (b.class) {
                if (c == null) {
                    c = new b();
                }
            }
        }
        return c;
    }

    public APCMessage a(int i, Bundle bundle, String str, int i2) {
        try {
            APCMessage aPCMessage = new APCMessage();
            aPCMessage.what = i;
            aPCMessage.data = bundle;
            com.mob.mcl.d.b.a().a("apc fw mg ： " + i + SQLBuilder.BLANK + aPCMessage.toString() + " to ->" + str);
            return MobAPC.sendMessage(1, str, this.b, aPCMessage, i2);
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
            return null;
        }
    }

    public HttpResponseCallback a(APCMessage aPCMessage) {
        return new a(this, aPCMessage);
    }

    public String a(String str, String str2, ArrayList<KVPair<String>> arrayList, StringPart stringPart, int i, NetworkHelper.NetworkTimeOut networkTimeOut) throws Throwable {
        APCMessage sendMessage;
        if (!b()) {
            com.mob.mcl.d.b.a().b("apc list is null");
            return null;
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.addAll(this.a);
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            String str3 = (String) it.next();
            APCMessage aPCMessage = new APCMessage();
            aPCMessage.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("type", str);
            bundle.putString(FileDownloadModel.URL, str2);
            HashMap hashMap = new HashMap();
            if (arrayList != null) {
                Iterator<KVPair<String>> it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    KVPair<String> next = it2.next();
                    hashMap.put(next.name, next.value);
                }
            }
            bundle.putString("headers", new Hashon().fromHashMap(hashMap));
            bundle.putInt("chunkLength", i);
            if (stringPart != null) {
                bundle.putString("body", stringPart.toString());
            }
            bundle.putInt("readTimout", networkTimeOut.readTimout);
            bundle.putInt("connectionTimeout", networkTimeOut.connectionTimeout);
            aPCMessage.data = bundle;
            try {
                com.mob.mcl.d.b.a().a("apc sd mg ： " + aPCMessage.toString() + " to ->" + str3);
                sendMessage = MobAPC.sendMessage(1, str3, this.b, aPCMessage, (long) networkTimeOut.readTimout);
            } catch (Throwable th) {
                com.mob.mcl.d.b.a().a(th);
            }
            if (sendMessage != null && sendMessage.what == 2 && sendMessage.data != null) {
                Bundle bundle2 = sendMessage.data;
                com.mob.mcl.d.b.a().a("apc receive rp mg ： " + bundle2.getString(AeUtil.ROOT_DATA_PATH_OLD_NAME));
                return bundle2.getString(AeUtil.ROOT_DATA_PATH_OLD_NAME);
            }
            com.mob.mcl.d.b.a().a("apc receive rp : " + sendMessage);
        }
        return null;
    }

    public void a(Context context, MobAPC.MobAPCMessageListener mobAPCMessageListener) {
        this.b = MCLSDK.SDK_TAG;
        MobAPC.init(context);
        if (com.mob.mcl.b.a.e()) {
            MobAPC.addAPCMessageListener(this.b, mobAPCMessageListener);
        }
    }

    public boolean b() {
        Set<String> set = this.a;
        return set != null && set.size() > 0;
    }

    public void c() {
        if (i.c().i) {
            ArrayList arrayList = new ArrayList();
            if (!com.mob.mcl.b.a.e()) {
                arrayList.add(MobSDK.getContext().getPackageName());
            }
            arrayList.addAll(MobAPC.getMAPCServiceList());
            com.mob.mcl.d.b.a().a("qy : " + arrayList.toString());
            this.a = new LinkedHashSet();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                APCMessage aPCMessage = new APCMessage();
                aPCMessage.what = 1;
                try {
                    com.mob.mcl.d.b.a().a("sd apc mg ： " + aPCMessage.toString() + " to ->" + str);
                    APCMessage sendMessage = MobAPC.sendMessage(1, str, this.b, aPCMessage, BootloaderScanner.TIMEOUT);
                    if (sendMessage != null && sendMessage.data != null && sendMessage.what == 1 && sendMessage.data.getBoolean("isTcpAvailable")) {
                        this.a.add(str);
                    }
                } catch (Throwable th) {
                    com.mob.mcl.d.b.a().a("query tcp exp : " + th.getMessage());
                }
            }
            com.mob.mcl.d.b.a().a("apc available pg : " + this.a.toString());
        }
    }
}
