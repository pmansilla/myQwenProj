package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.amap.api.mapcore.util.ho;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: SoCrashLogProcessor.java */
/* loaded from: classes.dex */
public class ie {
    private File[] e;
    private static HashSet<String> d = new HashSet<>();
    public static byte[] a = "FDF1F436161AEF5B".getBytes();
    public static byte[] b = "0102030405060708".getBytes();
    public static String c = "SOCRASH";
    private static final String f = c;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SoCrashLogProcessor.java */
    /* loaded from: classes.dex */
    public static class a {
        private String a;
        private String b;
        private String c;
        private String d;
        private String e;

        public a() {
        }

        public a(String str, String str2, String str3, String str4, String str5) {
            this.a = str;
            this.b = str2;
            this.c = str3;
            this.d = str4;
            this.e = str5;
        }

        public static a a(String str) {
            if (TextUtils.isEmpty(str)) {
                return new a();
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                return new a(jSONObject.optString("mk", ""), jSONObject.optString("ak", ""), jSONObject.optString("bk", ""), jSONObject.optString("ik", ""), jSONObject.optString("nk", ""));
            } catch (Throwable unused) {
                return new a();
            }
        }

        public static List<a> b(String str) {
            if (TextUtils.isEmpty(str)) {
                return new ArrayList();
            }
            ArrayList arrayList = new ArrayList();
            try {
                JSONArray jSONArray = new JSONArray(str);
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add(a(jSONArray.getString(i)));
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
            return arrayList;
        }

        public String a() {
            return this.a;
        }

        public String b() {
            return this.b;
        }

        public String c() {
            return this.c;
        }

        public String d() {
            return this.d;
        }

        public String e() {
            return this.e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SoCrashLogProcessor.java */
    /* loaded from: classes.dex */
    public static class b {
        private int a;
        private String b;
    }

    private static String a() {
        return hl.b("SO_DYNAMIC_FILE_KEY");
    }

    private static void a(Context context, a aVar) throws JSONException {
        if (TextUtils.isEmpty(aVar.b()) || TextUtils.isEmpty(aVar.c()) || TextUtils.isEmpty(aVar.d())) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(a(), 0);
        JSONArray jSONArray = new JSONArray(hp.a(hb.b(hp.e(sharedPreferences.getString("SO_ERROR_KEY", "")))));
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            if (jSONObject.opt("mk").equals(aVar.a()) && jSONObject.opt("ak").equals(aVar.b()) && jSONObject.opt("bk").equals(aVar.c()) && jSONObject.opt("ik").equals(aVar.d()) && jSONObject.opt("nk").equals(aVar.e())) {
                return;
            }
        }
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.putOpt("mk", aVar.a());
        jSONObject2.putOpt("ak", aVar.b());
        jSONObject2.putOpt("bk", aVar.c());
        jSONObject2.putOpt("ik", aVar.d());
        jSONObject2.putOpt("nk", aVar.e());
        jSONArray.put(jSONObject2);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("SO_ERROR_KEY", hp.g(hb.a(hp.a(jSONArray.toString()))));
        edit.commit();
    }

    private void a(Context context, byte[] bArr) {
        List<a> b2;
        if (context == null) {
            return;
        }
        try {
            String str = new String(bArr, "ISO-8859-1");
            if (str.indexOf("{\"") > 0 && str.indexOf("\"}") > 0) {
                JSONObject jSONObject = new JSONObject(str.substring(str.indexOf("{\""), str.lastIndexOf("\"}") + 2));
                String optString = jSONObject.optString("ik");
                String optString2 = jSONObject.optString("jk");
                if (TextUtils.isEmpty(optString2) || (b2 = a.b(optString)) == null) {
                    return;
                }
                for (int i = 0; i < b2.size(); i++) {
                    a aVar = b2.get(i);
                    if (optString2.contains(aVar.e())) {
                        a(context, aVar);
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void a(File file) {
        if (file == null) {
            return;
        }
        try {
            file.delete();
        } catch (Exception unused) {
        }
    }

    private boolean a(List<b> list, String str) {
        if (list == null) {
            return false;
        }
        for (b bVar : list) {
            if (bVar.b.equals(str)) {
                bVar.a++;
                return true;
            }
        }
        return false;
    }

    private boolean a(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0 || bArr.length != bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    private byte[] b(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            fileInputStream.close();
            byte[] a2 = hj.a("a1f5886b7153004c5c99559f5261676f".getBytes(), bArr, "nFy1THrhajaZzz8U".getBytes());
            byte[] bArr2 = new byte[16];
            byte[] bArr3 = new byte[a2.length - 16];
            System.arraycopy(a2, 0, bArr2, 0, 16);
            System.arraycopy(a2, 16, bArr3, 0, a2.length - 16);
            return !a(hl.a(bArr3, "MD5"), bArr2) ? new byte[0] : bArr3;
        } catch (Throwable unused) {
            return null;
        }
    }

    private File[] b(Context context) {
        File file = new File(ia.a(context));
        if (file.isDirectory()) {
            return file.listFiles();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Context context) {
        try {
            File[] b2 = b(context);
            if (b2 == null) {
                return;
            }
            this.e = b2;
            ho hoVar = null;
            try {
                hoVar = new ho.a(f, "1.0", "").a(new String[0]).a();
            } catch (Throwable unused) {
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < b2.length && i < 10; i++) {
                File file = b2[i];
                if (file != null && file.exists() && file.isFile()) {
                    byte[] b3 = b(file);
                    if (b3 != null && b3.length != 0 && b3.length <= 100000) {
                        String a2 = hl.a(b3);
                        if (!a(arrayList, a2) && !d.contains(a2)) {
                            a(context, b3);
                            d.add(a2);
                            id.a(hoVar, context, f, hj.b(b3));
                            a(file);
                        }
                        file.delete();
                    }
                    file.delete();
                }
            }
        } catch (Throwable unused2) {
        }
    }
}
