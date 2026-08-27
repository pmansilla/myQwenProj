package com.mob.mobapm.a;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobSDK;
import com.mob.mobapm.bean.CarrierType;
import com.mob.mobapm.core.d;
import com.mob.mobapm.e.e;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class a implements Handler.Callback {
    private static a c;
    private static boolean d;
    private HashMap<String, Object> a;
    private ExecutorService b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.mob.mobapm.a.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class RunnableC0062a implements Runnable {
        final /* synthetic */ HashMap a;
        final /* synthetic */ String b;

        RunnableC0062a(a aVar, HashMap hashMap, String str) {
            this.a = hashMap;
            this.b = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                HashMap hashMap = (HashMap) this.a.get(this.b);
                int intValue = (hashMap == null || !hashMap.containsKey("times")) ? 0 : ((Integer) hashMap.get("times")).intValue();
                long currentTimeMillis = System.currentTimeMillis();
                int i = -1;
                int i2 = intValue;
                boolean z = false;
                do {
                    Object a = d.a((HashMap<String, Object>) hashMap);
                    if (a instanceof HashMap) {
                        HashMap hashMap2 = (HashMap) a;
                        String str = (hashMap2 == null || hashMap2.isEmpty()) ? "-1" : (String) hashMap2.get("code");
                        com.mob.mobapm.d.a.a().i("APM: send radar code: " + str, new Object[0]);
                        i = Integer.valueOf(str).intValue();
                        if ("200".equals(str)) {
                            z = true;
                            i2 = 0;
                        } else {
                            i2--;
                            z = false;
                        }
                    }
                    if (i2 <= 0) {
                        break;
                    }
                } while (!z);
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                HashMap hashMap3 = new HashMap();
                hashMap3.put("taskId", hashMap.get(ConnectionModel.ID));
                hashMap3.put(FileDownloadModel.URL, hashMap.get(FileDownloadModel.URL));
                hashMap3.put("responseIp", e.a(Uri.parse((String) hashMap.get(FileDownloadModel.URL)).getHost()));
                hashMap3.put("carrier", CarrierType.valuesOf(DeviceHelper.getInstance(MobSDK.getContext()).getCarrier()).name);
                hashMap3.put("rt", Long.valueOf(currentTimeMillis2));
                hashMap3.put("responseStatus", Integer.valueOf(i));
                Object c = d.c(hashMap3);
                if ((c instanceof HashMap) && ((Integer) ((HashMap) c).get("code")).intValue() == 200) {
                    com.mob.mobapm.b.a.c(hashMap);
                }
                com.mob.mobapm.d.a.a().i("APM: send radar result: " + c, new Object[0]);
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().i("APM: radar send error: " + th, new Object[0]);
            }
        }
    }

    private a() {
        MobHandlerThread.newHandler(this);
        this.a = new HashMap<>();
        this.b = Executors.newFixedThreadPool(8);
        new Hashon();
    }

    public static synchronized a b() {
        a aVar;
        synchronized (a.class) {
            if (c == null) {
                c = new a();
            }
            aVar = c;
        }
        return aVar;
    }

    public void a() {
        HashMap<String, Object> j;
        if (!d || (j = com.mob.mobapm.b.a.j()) == null || j.isEmpty()) {
            return;
        }
        this.a.putAll(j);
        Iterator<String> it = j.keySet().iterator();
        while (it.hasNext()) {
            this.b.submit(new RunnableC0062a(this, j, it.next()));
        }
    }

    public void a(boolean z) {
        d = z;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        return false;
    }
}
