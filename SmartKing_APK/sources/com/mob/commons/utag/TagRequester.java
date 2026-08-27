package com.mob.commons.utag;

import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.commons.ForbThrowable;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.commons.b;
import com.mob.commons.i;
import com.mob.commons.j;
import com.mob.tools.MobLog;
import com.mob.tools.RxMob;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import me.panpf.sketch.uri.HttpUriModel;

/* loaded from: classes.dex */
public final class TagRequester implements PublicMemberKeeper {
    private static HashMap<String, Object> a;
    private static DeviceHelper b = DeviceHelper.getInstance(MobSDK.getContext());
    private static boolean c;
    private UserTagsResponse d;
    private UserTagError e;

    /* loaded from: classes.dex */
    public interface UserTagsResponse {
        void onResponse(Map<String, Object> map);
    }

    static /* synthetic */ HashMap b() throws Throwable {
        return e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static HashMap<String, Object> b(HashMap<String, Object> hashMap, String str) throws Throwable {
        return (HashMap) new MobCommunicator(1024, "e3e28dce5fe8fc1bb56a25964219d5dc2976edb171b99b1103c2c4f89ad0b66fb58669fe69eb0b5d11e8be990b0715b4de2b4e5a5dcce121f47f18063d5d99f9", "256f461cc45979b52264ac022ff1353ea5f8140d35686ffdae2faee09db2006c3b43c2bb74ce6f4c51698db6384c1c0ceca958208d65c7ed345a04ea6349ca39601818c3d5500565ba49ed49c0f4014b06980d17fc069c95d30092d0cfdaddf783ea96c5f8bdc42b6765d71a5d12192ef74646b41d92f1caeba3123e71938d39").requestSynchronized(hashMap, str, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r1v12, types: [com.mob.commons.utag.TagRequester$3] */
    public static synchronized void d() {
        boolean z;
        synchronized (TagRequester.class) {
            a = i.k();
            if (a != null && a.containsKey("defHost") && a.containsKey("defPort") && a.containsKey("defSSLPort") && a.containsKey("tagExpire")) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(i.j());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(calendar.getTime());
                calendar.setTimeInMillis(System.currentTimeMillis());
                z = !format.equals(simpleDateFormat.format(calendar.getTime()));
                if (z && !c) {
                    c = true;
                    new Thread() { // from class: com.mob.commons.utag.TagRequester.3
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            try {
                                HashMap hashMap = new HashMap();
                                hashMap.put("duid", DeviceAuthorizer.authorize(null));
                                hashMap.put(Dic.MAC, TagRequester.b.getMacAddress());
                                hashMap.put(Dic.IMEI, TagRequester.b.getIMEI());
                                hashMap.put(Dic.SERIAL_NO, TagRequester.b.getSerialno());
                                hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, TagRequester.b.getModel());
                                hashMap.put("appkey", MobSDK.getAppkey());
                                hashMap.put("apppkg", TagRequester.b.getPackageName());
                                hashMap.put("appver", TagRequester.b.getAppVersionName());
                                hashMap.put("plat", 1);
                                i.a((HashMap<String, Object>) TagRequester.b(hashMap, j.c("http://api.utag.mob.com/conf")));
                            } catch (Throwable th) {
                                MobLog.getInstance().w(th);
                            }
                            boolean unused = TagRequester.c = false;
                        }
                    }.start();
                }
            }
            i.a((HashMap<String, Object>) null);
            a = new HashMap<>();
            a.put("defHost", "api.utag.mob.com");
            a.put("defPort", 80);
            a.put("defSSLPort", 443);
            a.put("tagExpire", 86400);
            z = true;
            if (z) {
                c = true;
                new Thread() { // from class: com.mob.commons.utag.TagRequester.3
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        try {
                            HashMap hashMap = new HashMap();
                            hashMap.put("duid", DeviceAuthorizer.authorize(null));
                            hashMap.put(Dic.MAC, TagRequester.b.getMacAddress());
                            hashMap.put(Dic.IMEI, TagRequester.b.getIMEI());
                            hashMap.put(Dic.SERIAL_NO, TagRequester.b.getSerialno());
                            hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, TagRequester.b.getModel());
                            hashMap.put("appkey", MobSDK.getAppkey());
                            hashMap.put("apppkg", TagRequester.b.getPackageName());
                            hashMap.put("appver", TagRequester.b.getAppVersionName());
                            hashMap.put("plat", 1);
                            i.a((HashMap<String, Object>) TagRequester.b(hashMap, j.c("http://api.utag.mob.com/conf")));
                        } catch (Throwable th) {
                            MobLog.getInstance().w(th);
                        }
                        boolean unused = TagRequester.c = false;
                    }
                }.start();
            }
        }
    }

    private static synchronized HashMap<String, Object> e() throws Throwable {
        HashMap<String, Object> l;
        String str;
        synchronized (TagRequester.class) {
            l = i.l();
            if (l == null || l.isEmpty()) {
                Object obj = a.get("defPort");
                if (obj == null || !(obj instanceof Integer)) {
                    str = null;
                } else {
                    int intValue = ((Integer) obj).intValue();
                    if (intValue <= 0) {
                        str = "";
                    } else {
                        str = ":" + intValue;
                    }
                }
                String c2 = j.c(HttpUriModel.SCHEME + a.get("defHost") + str + "/utag");
                HashMap hashMap = new HashMap();
                hashMap.put("duid", DeviceAuthorizer.authorize(null));
                hashMap.put(Dic.MAC, b.getMacAddress());
                hashMap.put(Dic.IMEI, b.getIMEI());
                hashMap.put(Dic.SERIAL_NO, b.getSerialno());
                hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, b.getModel());
                hashMap.put("appkey", MobSDK.getAppkey());
                hashMap.put("apppkg", b.getPackageName());
                hashMap.put("appver", b.getAppVersionName());
                hashMap.put("plat", 1);
                l = b(hashMap, c2);
                int i = 0;
                try {
                    i = Integer.parseInt(String.valueOf(a.get("tagExpire")));
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                }
                i.a(l, i);
            }
        }
        return l;
    }

    public synchronized void request() {
        RxMob.create(new RxMob.QuickSubscribe<HashMap<String, Object>>() { // from class: com.mob.commons.utag.TagRequester.2
            @Override // com.mob.tools.RxMob.QuickSubscribe
            protected void doNext(RxMob.Subscriber<HashMap<String, Object>> subscriber) throws Throwable {
                if (b.ac()) {
                    throw new ForbThrowable();
                }
                TagRequester.d();
                subscriber.onNext(TagRequester.b());
            }
        }).subscribeOnNewThreadAndObserveOnUIThread(new RxMob.Subscriber<HashMap<String, Object>>() { // from class: com.mob.commons.utag.TagRequester.1
            @Override // com.mob.tools.RxMob.Subscriber
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onNext(HashMap<String, Object> hashMap) {
                if (TagRequester.this.d != null) {
                    TagRequester.this.d.onResponse(hashMap);
                }
            }

            @Override // com.mob.tools.RxMob.Subscriber
            public void onError(Throwable th) {
                if (TagRequester.this.e != null) {
                    TagRequester.this.e.onError(th);
                }
            }
        });
    }

    public TagRequester whenError(UserTagError userTagError) {
        this.e = userTagError;
        return this;
    }

    public TagRequester whenSuccess(UserTagsResponse userTagsResponse) {
        this.d = userTagsResponse;
        return this;
    }
}
