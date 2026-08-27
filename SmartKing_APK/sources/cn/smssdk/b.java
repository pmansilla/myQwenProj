package cn.smssdk;

import android.app.Activity;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.wrapper.MobVerifyWrapper;
import cn.smssdk.wrapper.TokenVerifyException;
import cn.smssdk.wrapper.TokenVerifyResult;
import com.mob.MobSDK;
import com.mob.commons.ForbThrowable;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* compiled from: SMSSDKCore.java */
/* loaded from: classes.dex */
public class b {
    private HashSet<EventHandler> a;
    private cn.smssdk.net.f b;
    private cn.smssdk.d.a c;
    private String d;
    private HashMap<Character, ArrayList<String[]>> e;
    private HashMap<String, String> f;
    private ArrayList<HashMap<String, Object>> g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        final /* synthetic */ Object a;

        a(Object obj) {
            this.a = obj;
        }

        @Override // java.lang.Runnable
        public void run() {
            Object[] objArr = (Object[]) this.a;
            int i = 0;
            String str = (String) objArr[0];
            String str2 = (String) objArr[1];
            String str3 = (String) objArr[2];
            if (str2.startsWith("+")) {
                str2 = str2.substring(1);
            }
            try {
                if (b.this.f == null || b.this.f.size() <= 0) {
                    b.this.l();
                }
                b.this.b.a(str, str2, str3);
                i = -1;
                th = null;
            } catch (Throwable th) {
                th = th;
            }
            b.this.a(8, i, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* renamed from: cn.smssdk.b$b, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class RunnableC0009b implements Runnable {
        RunnableC0009b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            b.this.a(8, 0, b.b(614, (Throwable) null));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class c extends FakeActivity {
        final /* synthetic */ int a;

        /* compiled from: SMSSDKCore.java */
        /* loaded from: classes.dex */
        class a implements Runnable {
            final /* synthetic */ HashMap a;

            a(HashMap hashMap) {
                this.a = hashMap;
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (!"true".equals(String.valueOf(this.a.get("res")))) {
                        SMSLog.getInstance().d(SMSLog.FORMAT, "SMSSDKCore", "showDialog", "AlertPage: FALSE clicked");
                        Iterator it = ((ArrayList) this.a.get("cancelActions")).iterator();
                        while (it.hasNext()) {
                            Runnable runnable = (Runnable) it.next();
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                        return;
                    }
                    SMSLog.getInstance().d(SMSLog.FORMAT, "SMSSDKCore", "showDialog", "AlertPage: TRUE clicked");
                    cn.smssdk.utils.b.c().a(true);
                    Iterator it2 = ((ArrayList) this.a.get("okActions")).iterator();
                    while (it2.hasNext()) {
                        Runnable runnable2 = (Runnable) it2.next();
                        if (runnable2 != null) {
                            runnable2.run();
                        }
                    }
                } catch (Throwable th) {
                    SMSLog.getInstance().d(th);
                    c cVar = c.this;
                    b.this.a(cVar.a, 0, th);
                }
            }
        }

        c(int i) {
            this.a = i;
        }

        @Override // com.mob.tools.FakeActivity
        public void onResult(HashMap<String, Object> hashMap) {
            new Thread(new a(hashMap)).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class d implements Runnable {
        d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                b.f();
                b.this.b.b();
            } catch (Throwable unused) {
                SMSLog.getInstance().d(SMSLog.FORMAT_SIMPLE, "Privacy not granted, stop init token");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class e extends cn.smssdk.wrapper.a<TokenVerifyResult> {
        e() {
        }

        @Override // cn.smssdk.wrapper.a
        public void a(TokenVerifyException tokenVerifyException) {
            b.this.a(9, 0, tokenVerifyException);
        }

        @Override // cn.smssdk.wrapper.a
        public void a(TokenVerifyResult tokenVerifyResult) {
            b.this.a(9, -1, tokenVerifyResult);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class f extends cn.smssdk.net.h.d {
        f() {
        }

        @Override // cn.smssdk.net.h.d
        public void a() {
            b.this.a(10, -1, (Object) null);
        }

        @Override // cn.smssdk.net.h.d
        public void a(Throwable th) {
            b.this.a(10, 0, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class g implements Runnable {
        g(b bVar) {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                b.f();
                cn.smssdk.logger.d.d().b();
            } catch (Throwable unused) {
                SMSLog.getInstance().d(SMSLog.FORMAT_SIMPLE, "Privacy not granted, stop init token");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class h extends Thread {
        final /* synthetic */ int a;
        final /* synthetic */ Object b;

        h(int i, Object obj) {
            this.a = i;
            this.b = obj;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                synchronized (b.this.a) {
                    Iterator it = b.this.a.iterator();
                    while (it.hasNext()) {
                        ((EventHandler) it.next()).beforeEvent(this.a, this.b);
                    }
                }
                b.f();
                b.this.b(this.a, this.b);
            } catch (Throwable th) {
                b.this.a(this.a, 0, th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class i implements Runnable {
        final /* synthetic */ Object a;

        i(Object obj) {
            this.a = obj;
        }

        @Override // java.lang.Runnable
        public void run() {
            int i = 0;
            try {
                if (b.this.f == null || b.this.f.size() <= 0) {
                    b.this.l();
                }
                Object[] objArr = (Object[]) this.a;
                String str = (String) objArr[0];
                String str2 = (String) objArr[1];
                String str3 = (String) objArr[2];
                String str4 = (String) objArr[3];
                if (str.startsWith("+")) {
                    str = str.substring(1);
                }
                OnSendMessageHandler onSendMessageHandler = (OnSendMessageHandler) objArr[4];
                if (onSendMessageHandler != null && onSendMessageHandler.onSendMessage(str, str2)) {
                    throw new UserInterruptException();
                }
                th = Boolean.valueOf(b.this.b.a(str, str2, str3, str4));
                i = -1;
            } catch (Throwable th) {
                th = th;
            }
            b.this.a(2, i, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class j implements Runnable {
        j() {
        }

        @Override // java.lang.Runnable
        public void run() {
            b.this.a(2, 0, b.b(614, (Throwable) null));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class k implements Runnable {
        final /* synthetic */ Object a;

        k(Object obj) {
            this.a = obj;
        }

        @Override // java.lang.Runnable
        public void run() {
            int i = 0;
            try {
                if (b.this.f == null || b.this.f.size() <= 0) {
                    b.this.l();
                }
                String[] strArr = (String[]) this.a;
                String str = strArr[0];
                String str2 = strArr[1];
                String str3 = strArr[2];
                if (str.startsWith("+")) {
                    str = str.substring(1);
                }
                th = b.this.b.b(str3, str, str2);
                i = -1;
            } catch (Throwable th) {
                th = th;
            }
            b.this.a(3, i, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SMSSDKCore.java */
    /* loaded from: classes.dex */
    public class l implements Runnable {
        l() {
        }

        @Override // java.lang.Runnable
        public void run() {
            b.this.a(3, 0, b.b(614, (Throwable) null));
        }
    }

    static {
        Long.valueOf(3000L);
    }

    public b(SMSSDK.InitFlag initFlag) {
        cn.smssdk.a.a();
        this.a = new HashSet<>();
        SMSLog.prepare();
        this.b = cn.smssdk.net.f.d();
        this.c = cn.smssdk.d.a.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:40:0x015f A[Catch: all -> 0x0164, TRY_LEAVE, TryCatch #1 {all -> 0x0164, blocks: (B:27:0x00cf, B:29:0x00da, B:35:0x0118, B:37:0x0133, B:38:0x0159, B:40:0x015f), top: B:26:0x00cf }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(int r17, int r18, java.lang.Object r19) {
        /*
            Method dump skipped, instructions count: 498
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.smssdk.b.a(int, int, java.lang.Object):void");
    }

    private void a(int i2, Runnable runnable, Runnable runnable2) {
        if (!cn.smssdk.utils.b.c().b() || cn.smssdk.utils.b.c().a()) {
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        SMSLog.getInstance().d(SMSLog.FORMAT, "SMSSDKCore", "showDialog", "AlertPage.isShow(): " + cn.smssdk.c.a.c());
        String e2 = cn.smssdk.utils.e.e(ResHelper.getStringRes(MobSDK.getContext(), "smssdk_authorize_msg_sms"));
        if (cn.smssdk.c.a.c()) {
            cn.smssdk.c.a.a(e2);
            cn.smssdk.c.a.a(runnable, runnable2);
        } else {
            cn.smssdk.c.a aVar = new cn.smssdk.c.a();
            cn.smssdk.c.a.a(e2);
            cn.smssdk.c.a.a(runnable, runnable2);
            aVar.showForResult(MobSDK.getContext(), null, new c(i2));
        }
    }

    private void a(Object obj) {
        a(4, 0, b(613, (Throwable) null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Throwable b(int i2, Throwable th) {
        return new Throwable("{\"status\":" + i2 + ",\"detail\":\"" + MobSDK.getContext().getResources().getString(ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_" + i2)) + "\"}", th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i2, Object obj) {
        cn.smssdk.logger.d.d().b(i2);
        switch (i2) {
            case 1:
                k();
                return;
            case 2:
                b(obj);
                return;
            case 3:
                e(obj);
                return;
            case 4:
                a(obj);
                return;
            case 5:
                d(obj);
                return;
            case 6:
                h();
                return;
            case 7:
                j();
                return;
            case 8:
                c(obj);
                return;
            case 9:
                i();
                return;
            default:
                return;
        }
    }

    private void b(Object obj) {
        a(2, new i(obj), new j());
    }

    private void c(Object obj) {
        a(8, new a(obj), new RunnableC0009b());
    }

    private void d(Object obj) {
        a(5, 0, b(613, (Throwable) null));
    }

    private void e(Object obj) {
        a(3, new k(obj), new l());
    }

    public static void f() throws Throwable {
        int i2;
        if (MobSDK.isForb()) {
            throw new ForbThrowable();
        }
        try {
            i2 = MobSDK.isAuth();
        } catch (Throwable unused) {
            i2 = 1;
        }
        try {
            SMSLog.getInstance().d(SMSLog.FORMAT, "SMSSDKCore", "checkBusiness", "isAuth: " + i2);
        } catch (Throwable unused2) {
            SMSLog.getInstance().w(SMSLog.FORMAT, "SMSSDKCore", "checkBusiness", "Not privacy version, do work!");
            if (i2 == 1) {
            } else {
                return;
            }
        }
        if (i2 == 1 && i2 != 2) {
            throw b(612, (Throwable) null);
        }
    }

    private boolean g() {
        try {
            ReflectHelper.importClass("com.mob.mobverify.MobVerify");
            SMSLog.getInstance().d("has mobverify component", new Object[0]);
            return true;
        } catch (Throwable unused) {
            SMSLog.getInstance().d("no mobverify component", new Object[0]);
            return false;
        }
    }

    private void h() {
        a(6, 0, b(613, (Throwable) null));
    }

    private void i() {
        c();
    }

    private void j() {
        a(7, 0, b(613, (Throwable) null));
    }

    private void k() {
        int i2;
        try {
            th = l();
            i2 = -1;
        } catch (Throwable th) {
            th = th;
            i2 = 0;
        }
        a(1, i2, th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<HashMap<String, Object>> l() throws Throwable {
        if (this.g == null || this.b.c()) {
            this.g = this.b.a();
        }
        ArrayList<HashMap<String, Object>> arrayList = this.g;
        if (arrayList != null) {
            Iterator<HashMap<String, Object>> it = arrayList.iterator();
            while (it.hasNext()) {
                HashMap<String, Object> next = it.next();
                String str = (String) next.get("zone");
                String str2 = (String) next.get("rule");
                if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                    if (this.f == null) {
                        this.f = new HashMap<>();
                    }
                    this.f.put(str, str2);
                }
            }
        } else {
            SMSLog.getInstance().w(SMSLog.FORMAT, "SMSSDKCore", "saveCountryRules", "WARNING: Get 'countryData' from server error!");
            this.g = new ArrayList<>();
        }
        return this.g;
    }

    public HashMap<Character, ArrayList<String[]>> a() {
        ArrayList arrayList;
        try {
            f();
            String appLanguage = DeviceHelper.getInstance(MobSDK.getContext()).getAppLanguage();
            SMSLog.getInstance().d("appLanguage:" + appLanguage, new Object[0]);
            if (appLanguage != null && !appLanguage.equals(this.d)) {
                this.d = appLanguage;
                this.e = null;
            }
            HashMap<Character, ArrayList<String[]>> hashMap = this.e;
            if (hashMap != null && hashMap.size() > 0) {
                return this.e;
            }
            LinkedHashMap linkedHashMap = null;
            for (char c2 = 'A'; c2 <= 'Z'; c2 = (char) (c2 + 1)) {
                int stringArrayRes = ResHelper.getStringArrayRes(MobSDK.getContext(), "smssdk_country_group_" + Character.toLowerCase(c2));
                if (stringArrayRes > 0) {
                    String[] stringArray = MobSDK.getContext().getResources().getStringArray(stringArrayRes);
                    if (stringArray != null) {
                        arrayList = null;
                        for (String str : stringArray) {
                            String[] split = str.split(",");
                            if (arrayList == null) {
                                arrayList = new ArrayList();
                            }
                            arrayList.add(split);
                        }
                    } else {
                        arrayList = null;
                    }
                    if (arrayList != null) {
                        if (linkedHashMap == null) {
                            linkedHashMap = new LinkedHashMap();
                        }
                        linkedHashMap.put(Character.valueOf(c2), arrayList);
                    }
                }
            }
            this.e = linkedHashMap;
            return this.e;
        } catch (Throwable th) {
            SMSLog.getInstance().e(th, SMSLog.FORMAT, "SMSSDKCore", "getGroupedCountryList", "Can not do work currently!");
            return new HashMap<>();
        }
    }

    public void a(int i2, Object obj) {
        new h(i2, obj).start();
    }

    public void a(Activity activity, OnDialogListener onDialogListener) {
        new cn.smssdk.c.b(activity, onDialogListener).show();
    }

    public void a(SmsMessage smsMessage, SMSSDK.VerifyCodeReadListener verifyCodeReadListener) {
        try {
            f();
            this.c.a(verifyCodeReadListener);
            this.c.a(smsMessage);
        } catch (Throwable th) {
            SMSLog.getInstance().e(th, SMSLog.FORMAT, "SMSSDKCore", "getGroupedCountryList", "Can not do work currently!");
            if (verifyCodeReadListener != null) {
                verifyCodeReadListener.onReadVerifyCode(null);
            }
        }
    }

    public void a(EventHandler eventHandler) {
        synchronized (this.a) {
            if (eventHandler != null) {
                try {
                    if (!this.a.contains(eventHandler)) {
                        this.a.add(eventHandler);
                        eventHandler.onRegister();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public void a(String str, TokenVerifyResult tokenVerifyResult) {
        new cn.smssdk.net.h.e().a(str, tokenVerifyResult, new f());
    }

    public String[] a(String str) {
        try {
            f();
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            Iterator<Map.Entry<Character, ArrayList<String[]>>> it = a().entrySet().iterator();
            while (it.hasNext()) {
                ArrayList<String[]> value = it.next().getValue();
                int size = value == null ? 0 : value.size();
                for (int i2 = 0; i2 < size; i2++) {
                    String[] strArr = value.get(i2);
                    if (strArr != null && strArr.length > 2 && str.equals(strArr[2])) {
                        return strArr;
                    }
                }
            }
            return null;
        } catch (Throwable th) {
            SMSLog.getInstance().e(th, SMSLog.FORMAT, "SMSSDKCore", "getGroupedCountryList", "Can not do work currently!");
            return null;
        }
    }

    public void b() {
        new Thread(new d()).start();
    }

    public void b(EventHandler eventHandler) {
        synchronized (this.a) {
            if (eventHandler != null) {
                try {
                    if (this.a.contains(eventHandler)) {
                        eventHandler.onUnregister();
                        this.a.remove(eventHandler);
                    }
                } finally {
                }
            }
        }
    }

    public String[] b(String str) {
        SMSLog.getInstance().d(SMSLog.FORMAT, "SMSSDKCore", "getCountryByMCC", "mcc: " + str);
        try {
            f();
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            Iterator<Map.Entry<Character, ArrayList<String[]>>> it = a().entrySet().iterator();
            while (it.hasNext()) {
                ArrayList<String[]> value = it.next().getValue();
                int size = value == null ? 0 : value.size();
                for (int i2 = 0; i2 < size; i2++) {
                    String[] strArr = value.get(i2);
                    if (strArr.length < 4) {
                        SMSLog.getInstance().d("MCC not found in the country: " + strArr[0], new Object[0]);
                    } else {
                        String str2 = strArr[3];
                        if (str2.indexOf("|") >= 0) {
                            for (String str3 : str2.split("\\|")) {
                                if (str3.startsWith(str)) {
                                    return strArr;
                                }
                            }
                        } else if (str2.startsWith(str)) {
                            return strArr;
                        }
                    }
                }
            }
            return null;
        } catch (Throwable th) {
            SMSLog.getInstance().e(th, SMSLog.FORMAT, "SMSSDKCore", "getGroupedCountryList", "Can not do work currently!");
            return null;
        }
    }

    public void c() {
        if (g()) {
            MobVerifyWrapper.a(new e());
        } else {
            a(9, 0, b(617, (Throwable) null));
        }
    }

    public void d() {
        synchronized (this.a) {
            Iterator<EventHandler> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().onUnregister();
            }
            this.a.clear();
        }
    }

    public void e() {
        new Thread(new g(this)).start();
    }
}
