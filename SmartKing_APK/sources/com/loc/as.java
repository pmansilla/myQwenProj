package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.loc.ac;
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
public final class as {
    private File[] e;
    private static HashSet<String> d = new HashSet<>();
    public static byte[] a = "FDF1F436161AEF5B".getBytes();
    public static byte[] b = "0102030405060708".getBytes();
    public static String c = "SOCRASH";
    private static final String f = "SOCRASH";

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

        private a(String str, String str2, String str3, String str4, String str5) {
            this.a = str;
            this.b = str2;
            this.c = str3;
            this.d = str4;
            this.e = str5;
        }

        public static List<a> a(String str) {
            if (TextUtils.isEmpty(str)) {
                return new ArrayList();
            }
            ArrayList arrayList = new ArrayList();
            try {
                JSONArray jSONArray = new JSONArray(str);
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add(b(jSONArray.getString(i)));
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
            return arrayList;
        }

        private static a b(String str) {
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

        public final String a() {
            return this.a;
        }

        public final String b() {
            return this.b;
        }

        public final String c() {
            return this.c;
        }

        public final String d() {
            return this.d;
        }

        public final String e() {
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

    private static void a(Context context, a aVar) throws JSONException {
        if (TextUtils.isEmpty(aVar.b()) || TextUtils.isEmpty(aVar.c()) || TextUtils.isEmpty(aVar.d())) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(aa.b("SO_DYNAMIC_FILE_KEY"), 0);
        JSONArray jSONArray = new JSONArray(ad.a(ae.b(ad.e(sharedPreferences.getString("SO_ERROR_KEY", "")))));
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
        edit.putString("SO_ERROR_KEY", ad.g(ae.a(ad.a(jSONArray.toString()))));
        edit.commit();
    }

    private static void a(Context context, byte[] bArr) {
        if (context == null) {
            return;
        }
        try {
            String str = new String(bArr, "ISO-8859-1");
            if (str.indexOf("{\"") > 0 && str.indexOf("\"}") > 0) {
                JSONObject jSONObject = new JSONObject(str.substring(str.indexOf("{\""), str.lastIndexOf("\"}") + 2));
                String optString = jSONObject.optString("ik");
                String optString2 = jSONObject.optString("jk");
                if (TextUtils.isEmpty(optString2)) {
                    return;
                }
                List<a> a2 = a.a(optString);
                for (int i = 0; i < a2.size(); i++) {
                    a aVar = a2.get(i);
                    if (optString2.contains(aVar.e())) {
                        a(context, aVar);
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static boolean a(List<b> list, String str) {
        for (b bVar : list) {
            if (bVar.b.equals(str)) {
                bVar.a++;
                return true;
            }
        }
        return false;
    }

    private static boolean a(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || bArr.length != 16) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    private static byte[] a(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            fileInputStream.close();
            byte[] a2 = y.a("a1f5886b7153004c5c99559f5261676f".getBytes(), bArr, "nFy1THrhajaZzz8U".getBytes());
            byte[] bArr2 = new byte[16];
            byte[] bArr3 = new byte[a2.length - 16];
            System.arraycopy(a2, 0, bArr2, 0, 16);
            System.arraycopy(a2, 16, bArr3, 0, a2.length - 16);
            return !a(aa.a(bArr3, "MD5"), bArr2) ? new byte[0] : bArr3;
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(Context context) {
        try {
            File file = new File(ao.a(context));
            ac acVar = null;
            File[] listFiles = !file.isDirectory() ? null : file.listFiles();
            if (listFiles == null) {
                return;
            }
            this.e = listFiles;
            try {
                acVar = new ac.a(f, "1.0", "").a(new String[0]).a();
            } catch (Throwable unused) {
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < listFiles.length && i < 10; i++) {
                File file2 = listFiles[i];
                if (file2 != null && file2.exists() && file2.isFile()) {
                    byte[] a2 = a(file2);
                    if (a2 != null && a2.length != 0 && a2.length <= 100000) {
                        String a3 = aa.a(a2);
                        if (!a(arrayList, a3) && !d.contains(a3)) {
                            a(context, a2);
                            d.add(a3);
                            ar.a(acVar, context, f, y.b(a2));
                            if (file2 != null) {
                                try {
                                    file2.delete();
                                } catch (Exception unused2) {
                                }
                            }
                        }
                    }
                    file2.delete();
                }
            }
        } catch (Throwable unused3) {
        }
    }
}
