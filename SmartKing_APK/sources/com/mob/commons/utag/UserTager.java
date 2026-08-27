package com.mob.commons.utag;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import com.autonavi.amap.mapcore.AeUtil;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.commons.b;
import com.mob.commons.j;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.SQLiteHelper;
import io.reactivex.annotations.SchedulerSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class UserTager implements PublicMemberKeeper {
    private static Handler a;
    private static SQLiteHelper.SingleTableDB b = SQLiteHelper.getDatabase(MobSDK.getContext(), "UserTag_1");
    private static Hashon c;
    private static DeviceHelper d;
    private HashMap<String, Object> e = new HashMap<>();
    private UserTagError f;

    /* loaded from: classes.dex */
    public class CustomTag implements PublicMemberKeeper {
        private UserTager b;
        private String c;

        private CustomTag(UserTager userTager, String str) {
            this.b = userTager;
            this.c = str;
        }

        private UserTager a(Object obj) {
            this.b.a(this.c, obj);
            return this.b;
        }

        public UserTager withValue(Boolean bool) {
            return a(bool);
        }

        public UserTager withValue(Number number) {
            return a(number);
        }

        public UserTager withValue(String str) {
            return a(str);
        }
    }

    static {
        b.addField("time", "text", true);
        b.addField(AeUtil.ROOT_DATA_PATH_OLD_NAME, "text", true);
        c = new Hashon();
        d = DeviceHelper.getInstance(MobSDK.getContext());
        a = MobHandlerThread.newHandler("t", new Handler.Callback() { // from class: com.mob.commons.utag.UserTager.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                if (!b.ac()) {
                    switch (message.what) {
                        case 1:
                            if (!UserTager.b()) {
                                UserTager.c();
                                break;
                            }
                            break;
                        case 2:
                            Object[] objArr = (Object[]) message.obj;
                            UserTager.b((HashMap) objArr[0], (UserTagError) objArr[1]);
                            UserTager.c();
                            break;
                    }
                }
                return false;
            }
        });
        c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, Object obj) {
        this.e.put(str, obj);
    }

    private static boolean a(ArrayList<HashMap<String, Object>> arrayList) {
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("plat", 1);
            hashMap.put(Dic.MAC, d.getMacAddress());
            hashMap.put("duid", DeviceAuthorizer.authorize(null));
            hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, d.getModel());
            hashMap.put(Dic.IMEI, d.getIMEI());
            hashMap.put(Dic.SERIAL_NO, d.getSerialno());
            hashMap.put("appkey", MobSDK.getAppkey());
            hashMap.put("apppkg", d.getPackageName());
            hashMap.put("appver", d.getAppVersionName());
            hashMap.put("datas", arrayList);
            new MobCommunicator(1024, "e3e28dce5fe8fc1bb56a25964219d5dc2976edb171b99b1103c2c4f89ad0b66fb58669fe69eb0b5d11e8be990b0715b4de2b4e5a5dcce121f47f18063d5d99f9", "256f461cc45979b52264ac022ff1353ea5f8140d35686ffdae2faee09db2006c3b43c2bb74ce6f4c51698db6384c1c0ceca958208d65c7ed345a04ea6349ca39601818c3d5500565ba49ed49c0f4014b06980d17fc069c95d30092d0cfdaddf783ea96c5f8bdc42b6765d71a5d12192ef74646b41d92f1caeba3123e71938d39").requestSynchronized(hashMap, j.c("http://api.utag.mob.com/bdata"), false);
            return true;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return false;
        }
    }

    private static void b(ArrayList<HashMap<String, Object>> arrayList) {
        try {
            StringBuilder sb = new StringBuilder();
            Iterator<HashMap<String, Object>> it = arrayList.iterator();
            while (it.hasNext()) {
                HashMap<String, Object> next = it.next();
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append('\'');
                sb.append(next.get("___datetime"));
                sb.append('\'');
            }
            SQLiteHelper.delete(b, "time in (" + sb.toString() + SQLBuilder.PARENTHESES_RIGHT, null);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0057, code lost:
    
        r0 = r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void b(java.util.HashMap<java.lang.String, java.lang.Object> r5, com.mob.commons.utag.UserTagError r6) {
        /*
            java.util.Set r0 = r5.entrySet()
            java.util.Iterator r0 = r0.iterator()
        L8:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L98
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            java.lang.Object r2 = r1.getKey()
            java.lang.String r2 = (java.lang.String) r2
            if (r2 != 0) goto L25
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "found a key of null"
            r0.<init>(r1)
            goto L99
        L25:
            int r3 = r2.length()
            r4 = 30
            if (r3 <= r4) goto L59
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "key '"
            r0.append(r1)
            r0.append(r2)
            java.lang.String r1 = "' is too long: "
            r0.append(r1)
            int r1 = r2.length()
            r0.append(r1)
            java.lang.String r1 = " > "
            r0.append(r1)
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            r1.<init>(r0)
        L57:
            r0 = r1
            goto L99
        L59:
            java.lang.Object r1 = r1.getValue()
            if (r1 == 0) goto L8
            boolean r2 = r1 instanceof java.lang.String
            if (r2 == 0) goto L8
            java.lang.String r1 = (java.lang.String) r1
            int r2 = r1.length()
            r3 = 255(0xff, float:3.57E-43)
            if (r2 <= r3) goto L8
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "value '"
            r0.append(r2)
            r0.append(r1)
            java.lang.String r2 = "' is too long: "
            r0.append(r2)
            int r1 = r1.length()
            r0.append(r1)
            java.lang.String r1 = " > "
            r0.append(r1)
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            r1.<init>(r0)
            goto L57
        L98:
            r0 = 0
        L99:
            if (r0 == 0) goto La1
            if (r6 == 0) goto La0
            r6.onError(r0)
        La0:
            return
        La1:
            long r0 = com.mob.commons.b.a()     // Catch: java.lang.Throwable -> Lcd
            java.lang.String r2 = "___datetime"
            java.lang.Long r3 = java.lang.Long.valueOf(r0)     // Catch: java.lang.Throwable -> Lcd
            r5.put(r2, r3)     // Catch: java.lang.Throwable -> Lcd
            android.content.ContentValues r2 = new android.content.ContentValues     // Catch: java.lang.Throwable -> Lcd
            r2.<init>()     // Catch: java.lang.Throwable -> Lcd
            java.lang.String r3 = "time"
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch: java.lang.Throwable -> Lcd
            r2.put(r3, r0)     // Catch: java.lang.Throwable -> Lcd
            java.lang.String r0 = "data"
            com.mob.tools.utils.Hashon r1 = com.mob.commons.utag.UserTager.c     // Catch: java.lang.Throwable -> Lcd
            java.lang.String r5 = r1.fromHashMap(r5)     // Catch: java.lang.Throwable -> Lcd
            r2.put(r0, r5)     // Catch: java.lang.Throwable -> Lcd
            com.mob.tools.utils.SQLiteHelper$SingleTableDB r5 = com.mob.commons.utag.UserTager.b     // Catch: java.lang.Throwable -> Lcd
            com.mob.tools.utils.SQLiteHelper.insert(r5, r2)     // Catch: java.lang.Throwable -> Lcd
            goto Ld3
        Lcd:
            r5 = move-exception
            if (r6 == 0) goto Ld3
            r6.onError(r5)
        Ld3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.utag.UserTager.b(java.util.HashMap, com.mob.commons.utag.UserTagError):void");
    }

    static /* synthetic */ boolean b() {
        return d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void c() {
        a.removeMessages(1);
        String networkType = d.getNetworkType();
        a.sendEmptyMessageDelayed(1, (networkType == null || SchedulerSupport.NONE.equals(networkType)) ? 600000L : 10000L);
    }

    private static boolean d() {
        String networkType = d.getNetworkType();
        if (networkType == null || SchedulerSupport.NONE.equals(networkType) || b.L()) {
            return false;
        }
        ArrayList<HashMap<String, Object>> e = e();
        if (e == null || e.size() <= 0) {
            return true;
        }
        if (!a(e)) {
            return false;
        }
        b(e);
        if (e.size() != 50) {
            return true;
        }
        d();
        return true;
    }

    private static ArrayList<HashMap<String, Object>> e() {
        try {
            Cursor query = SQLiteHelper.query(b, new String[]{AeUtil.ROOT_DATA_PATH_OLD_NAME}, null, null, null);
            if (query == null) {
                return null;
            }
            if (!query.moveToFirst()) {
                query.close();
                return null;
            }
            ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
            do {
                arrayList.add(c.fromJson(query.getString(0)));
                if (arrayList.size() > 50) {
                    break;
                }
            } while (query.moveToNext());
            query.close();
            return arrayList;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    public void commit() {
        HashMap hashMap = new HashMap();
        hashMap.putAll(this.e);
        Message message = new Message();
        message.what = 2;
        message.obj = new Object[]{hashMap, this.f};
        a.sendMessage(message);
    }

    public CustomTag set(String str) {
        return new CustomTag(this, str);
    }

    public UserTager whenError(UserTagError userTagError) {
        this.f = userTagError;
        return this;
    }
}
