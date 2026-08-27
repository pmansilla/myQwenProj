package com.mob.commons.logcollector;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobSDK;
import com.mob.commons.LockAction;
import com.mob.commons.MobProductCollector;
import com.mob.commons.j;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.FileLocker;
import com.mob.tools.utils.Hashon;
import io.reactivex.annotations.SchedulerSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

/* compiled from: LogsManager.java */
/* loaded from: classes.dex */
public class c {
    private static c b;
    private static String c = j.b("api.exc.mob.com");
    private File f;
    private NetworkHelper e = new NetworkHelper();
    private HashMap<String, Integer> d = new HashMap<>();
    protected final Handler a = MobHandlerThread.newHandler("l", new Handler.Callback() { // from class: com.mob.commons.logcollector.c.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            com.mob.commons.a.b();
            if (com.mob.commons.b.ac()) {
                return false;
            }
            c.this.a(message);
            return false;
        }
    });

    private c() {
    }

    public static synchronized c a() {
        c cVar;
        synchronized (c.class) {
            if (b == null) {
                b = new c();
            }
            cVar = b;
        }
        return cVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) throws Throwable {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = byteArrayInputStream.read(bArr, 0, 1024);
            if (read != -1) {
                gZIPOutputStream.write(bArr, 0, read);
            } else {
                try {
                    break;
                } catch (Throwable unused) {
                }
            }
        }
        gZIPOutputStream.flush();
        gZIPOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        byteArrayInputStream.close();
        return Base64.encodeToString(byteArray, 2);
    }

    private void a(final int i, final String str, final String[] strArr) {
        try {
            if (SchedulerSupport.NONE.equals(DeviceHelper.getInstance(MobSDK.getContext()).getDetailNetworkTypeForStatic())) {
                throw new IllegalStateException("network is disconnected!");
            }
            b();
            com.mob.commons.e.a(this.f, new LockAction() { // from class: com.mob.commons.logcollector.c.3
                @Override // com.mob.commons.LockAction
                public boolean run(FileLocker fileLocker) {
                    try {
                        ArrayList<d> a = e.a(strArr);
                        for (int i2 = 0; i2 < a.size(); i2++) {
                            d dVar = a.get(i2);
                            HashMap b2 = c.this.b(i, str);
                            b2.put("errmsg", dVar.a);
                            if (c.this.a(c.this.a(new Hashon().fromHashMap(b2)), true)) {
                                e.a(dVar.b);
                            }
                        }
                    } catch (Throwable th) {
                        MobLog.getInstance().i(th);
                    }
                    return false;
                }
            });
        } catch (Throwable th) {
            MobLog.getInstance().i(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(String str, boolean z) throws Throwable {
        if (!com.mob.commons.b.aq() || com.mob.commons.b.L()) {
            return false;
        }
        try {
            if (SchedulerSupport.NONE.equals(DeviceHelper.getInstance(MobSDK.getContext()).getDetailNetworkTypeForStatic())) {
                throw new IllegalStateException("network is disconnected!");
            }
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("m", str));
            ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
            arrayList2.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = 10000;
            networkTimeOut.connectionTimeout = 10000;
            this.e.httpPost(c(), arrayList, (KVPair<String>) null, arrayList2, networkTimeOut);
            return true;
        } catch (Throwable th) {
            MobLog.getInstance().i(th);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HashMap<String, Object> b(int i, String str) throws Throwable {
        HashMap<String, Object> hashMap = new HashMap<>();
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        hashMap.put("key", MobSDK.getAppkey());
        hashMap.put("plat", Integer.valueOf(deviceHelper.getPlatformCode()));
        hashMap.put("sdk", str);
        hashMap.put("sdkver", Integer.valueOf(i));
        hashMap.put("appname", deviceHelper.getAppName());
        hashMap.put("apppkg", deviceHelper.getPackageName());
        hashMap.put("appver", String.valueOf(deviceHelper.getAppVersion()));
        hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, deviceHelper.getModel());
        if (com.mob.commons.b.b()) {
            hashMap.put("deviceid", deviceHelper.getDeviceKey());
            hashMap.put(Dic.MAC, deviceHelper.getMacAddress());
            hashMap.put("udid", deviceHelper.getDeviceId());
        }
        hashMap.put("sysver", String.valueOf(deviceHelper.getOSVersionInt()));
        hashMap.put("networktype", deviceHelper.getDetailNetworkTypeForStatic());
        return hashMap;
    }

    private void b() {
        if (this.f == null) {
            this.f = new File(MobSDK.getContext().getFilesDir(), ".lock");
        }
        if (this.f.exists()) {
            return;
        }
        try {
            this.f.createNewFile();
        } catch (Exception e) {
            MobLog.getInstance().w(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Message message) {
        this.a.sendMessageDelayed(message, 1000L);
    }

    private String c() {
        return c + "/errlog";
    }

    private void c(Message message) {
        try {
            int i = message.arg1;
            String str = (String) message.obj;
            boolean ai = com.mob.commons.b.ai();
            boolean aj = com.mob.commons.b.aj();
            if (ai) {
                a(i, str, new String[]{String.valueOf(1)});
            } else if (aj) {
                a(i, str, new String[]{String.valueOf(2)});
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    private void d(final Message message) {
        int i;
        try {
            int i2 = message.arg1;
            Object[] objArr = (Object[]) message.obj;
            String str = (String) objArr[0];
            final String str2 = (String) objArr[1];
            if (message.arg2 == 3) {
                i = 2;
            } else {
                int i3 = message.arg2;
                i = 1;
            }
            boolean ai = com.mob.commons.b.ai();
            boolean aj = com.mob.commons.b.aj();
            if (1 != i || ai) {
                if (2 != i || aj) {
                    final String MD5 = Data.MD5(str2);
                    b();
                    final int i4 = i;
                    if (com.mob.commons.e.a(this.f, new LockAction() { // from class: com.mob.commons.logcollector.c.2
                        @Override // com.mob.commons.LockAction
                        public boolean run(FileLocker fileLocker) {
                            try {
                                e.a(com.mob.commons.b.a(), str2, i4, MD5);
                            } catch (Throwable th) {
                                int intValue = (c.this.d.containsKey(MD5) ? ((Integer) c.this.d.get(MD5)).intValue() : 0) + 1;
                                c.this.d.put(MD5, Integer.valueOf(intValue));
                                if (intValue < 3) {
                                    c.this.b(message);
                                } else {
                                    c.this.d.remove(MD5);
                                    MobLog.getInstance().w(th);
                                }
                            }
                            return false;
                        }
                    })) {
                        this.d.remove(MD5);
                        if (1 == i && ai) {
                            a(i2, str, new String[]{String.valueOf(1)});
                        } else if (2 == i && aj) {
                            a(i2, str, new String[]{String.valueOf(2)});
                        }
                    }
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    public void a(int i, int i2, String str, String str2) {
        Message message = new Message();
        message.what = 101;
        message.arg1 = i;
        message.arg2 = i2;
        message.obj = new Object[]{str, str2};
        this.a.sendMessage(message);
    }

    public void a(int i, String str) {
        Message message = new Message();
        message.what = 100;
        message.arg1 = i;
        message.obj = str;
        this.a.sendMessage(message);
    }

    protected void a(Message message) {
        switch (message.what) {
            case 100:
                c(message);
                return;
            case 101:
                d(message);
                return;
            default:
                return;
        }
    }

    public void b(int i, int i2, String str, String str2) {
        a(i, i2, str, str2);
        try {
            this.a.wait();
        } catch (Throwable unused) {
        }
    }
}
