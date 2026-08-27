package cn.sharesdk.sina.weibo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.utils.Data;
import java.util.List;

/* compiled from: WeiboAppManager.java */
/* loaded from: classes.dex */
public class g {
    private static g b;
    private static final Uri a = Uri.parse("content://com.sina.weibo.sdkProvider/query/package");
    private static a c = null;

    /* compiled from: WeiboAppManager.java */
    /* loaded from: classes.dex */
    public static class a {
        private String a = "com.sina.weibo";
        private String b = "com.sina.weibo.SSOActivity";
        private int c;

        /* JADX INFO: Access modifiers changed from: private */
        public void a(int i) {
            this.c = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(String str) {
            this.a = str;
        }

        public String a() {
            return this.a;
        }

        public int b() {
            return this.c;
        }

        public String toString() {
            return "WeiboInfo: PackageName = " + this.a + ", supportApi = " + this.c;
        }
    }

    private g() {
    }

    public static synchronized g a() {
        g gVar;
        synchronized (g.class) {
            if (b == null) {
                b = new g();
            }
            gVar = b;
        }
        return gVar;
    }

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            return a(MobSDK.getContext().getPackageManager().getPackageInfo(str, 64).signatures, "18da2bf10352443a00a5e046d9fca6bd");
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private static boolean a(Signature[] signatureArr, String str) {
        if (signatureArr == null || str == null) {
            return false;
        }
        for (Signature signature : signatureArr) {
            if (str.equals(Data.MD5(signature.toByteArray()))) {
                cn.sharesdk.framework.utils.e.b().d("check pass", new Object[0]);
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0077, code lost:
    
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x007b, code lost:
    
        r9 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x007c, code lost:
    
        cn.sharesdk.framework.utils.e.b().e(r9.getMessage(), new java.lang.Object[0]);
     */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00cd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private cn.sharesdk.sina.weibo.g.a b(java.lang.String r9) {
        /*
            r8 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r9)
            r1 = 0
            if (r0 == 0) goto L8
            return r1
        L8:
            r0 = 0
            android.content.Context r2 = com.mob.MobSDK.getContext()     // Catch: java.lang.Throwable -> L8e java.lang.Exception -> L91 android.content.pm.PackageManager.NameNotFoundException -> La6
            r3 = 2
            android.content.Context r2 = r2.createPackageContext(r9, r3)     // Catch: java.lang.Throwable -> L8e java.lang.Exception -> L91 android.content.pm.PackageManager.NameNotFoundException -> La6
            r3 = 4096(0x1000, float:5.74E-42)
            byte[] r4 = new byte[r3]     // Catch: java.lang.Throwable -> L8e java.lang.Exception -> L91 android.content.pm.PackageManager.NameNotFoundException -> La6
            android.content.res.AssetManager r2 = r2.getAssets()     // Catch: java.lang.Throwable -> L8e java.lang.Exception -> L91 android.content.pm.PackageManager.NameNotFoundException -> La6
            java.lang.String r5 = "weibo_for_sdk.json"
            java.io.InputStream r2 = r2.open(r5)     // Catch: java.lang.Throwable -> L8e java.lang.Exception -> L91 android.content.pm.PackageManager.NameNotFoundException -> La6
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            r5.<init>()     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
        L25:
            int r6 = r2.read(r4, r0, r3)     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            r7 = -1
            if (r6 == r7) goto L35
            java.lang.String r7 = new java.lang.String     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            r7.<init>(r4, r0, r6)     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            r5.append(r7)     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            goto L25
        L35:
            java.lang.String r3 = r5.toString()     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            if (r3 != 0) goto L75
            boolean r3 = a(r9)     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            if (r3 != 0) goto L46
            goto L75
        L46:
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            java.lang.String r4 = r5.toString()     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            r3.<init>(r4)     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            java.lang.String r4 = "support_api"
            int r3 = r3.optInt(r4, r7)     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            cn.sharesdk.sina.weibo.g$a r4 = new cn.sharesdk.sina.weibo.g$a     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            r4.<init>()     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            cn.sharesdk.sina.weibo.g.a.a(r4, r9)     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            cn.sharesdk.sina.weibo.g.a.a(r4, r3)     // Catch: java.lang.Exception -> L8a android.content.pm.PackageManager.NameNotFoundException -> L8c java.lang.Throwable -> Lca
            if (r2 == 0) goto L74
            r2.close()     // Catch: java.io.IOException -> L66
            goto L74
        L66:
            r9 = move-exception
            com.mob.tools.log.NLog r1 = cn.sharesdk.framework.utils.e.b()
            java.lang.String r9 = r9.getMessage()
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r1.e(r9, r0)
        L74:
            return r4
        L75:
            if (r2 == 0) goto L89
            r2.close()     // Catch: java.io.IOException -> L7b
            goto L89
        L7b:
            r9 = move-exception
            com.mob.tools.log.NLog r2 = cn.sharesdk.framework.utils.e.b()
            java.lang.String r9 = r9.getMessage()
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r2.e(r9, r0)
        L89:
            return r1
        L8a:
            r9 = move-exception
            goto L93
        L8c:
            r9 = move-exception
            goto La8
        L8e:
            r9 = move-exception
            r2 = r1
            goto Lcb
        L91:
            r9 = move-exception
            r2 = r1
        L93:
            com.mob.tools.log.NLog r3 = cn.sharesdk.framework.utils.e.b()     // Catch: java.lang.Throwable -> Lca
            java.lang.String r9 = r9.getMessage()     // Catch: java.lang.Throwable -> Lca
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch: java.lang.Throwable -> Lca
            r3.e(r9, r4)     // Catch: java.lang.Throwable -> Lca
            if (r2 == 0) goto Lc9
            r2.close()     // Catch: java.io.IOException -> Lbb
            goto Lc9
        La6:
            r9 = move-exception
            r2 = r1
        La8:
            com.mob.tools.log.NLog r3 = cn.sharesdk.framework.utils.e.b()     // Catch: java.lang.Throwable -> Lca
            java.lang.String r9 = r9.getMessage()     // Catch: java.lang.Throwable -> Lca
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch: java.lang.Throwable -> Lca
            r3.e(r9, r4)     // Catch: java.lang.Throwable -> Lca
            if (r2 == 0) goto Lc9
            r2.close()     // Catch: java.io.IOException -> Lbb
            goto Lc9
        Lbb:
            r9 = move-exception
            com.mob.tools.log.NLog r2 = cn.sharesdk.framework.utils.e.b()
            java.lang.String r9 = r9.getMessage()
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r2.e(r9, r0)
        Lc9:
            return r1
        Lca:
            r9 = move-exception
        Lcb:
            if (r2 == 0) goto Ldf
            r2.close()     // Catch: java.io.IOException -> Ld1
            goto Ldf
        Ld1:
            r1 = move-exception
            com.mob.tools.log.NLog r2 = cn.sharesdk.framework.utils.e.b()
            java.lang.String r1 = r1.getMessage()
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r2.e(r1, r0)
        Ldf:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.sina.weibo.g.b(java.lang.String):cn.sharesdk.sina.weibo.g$a");
    }

    private a c() {
        a d = d();
        a e = e();
        boolean z = d != null;
        boolean z2 = e != null;
        if (z && z2) {
            return d.b() >= e.b() ? d : e;
        }
        if (z) {
            return d;
        }
        if (z2) {
            return e;
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x006c, code lost:
    
        if (r1 != null) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x008b, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0088, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0086, code lost:
    
        if (r1 == null) goto L37;
     */
    /* JADX WARN: Removed duplicated region for block: B:39:0x008f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private cn.sharesdk.sina.weibo.g.a d() {
        /*
            r8 = this;
            android.content.Context r0 = com.mob.MobSDK.getContext()
            android.content.ContentResolver r1 = r0.getContentResolver()
            r0 = 0
            android.net.Uri r2 = cn.sharesdk.sina.weibo.g.a     // Catch: java.lang.Throwable -> L71 java.lang.Exception -> L76
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L71 java.lang.Exception -> L76
            if (r1 != 0) goto L1b
            if (r1 == 0) goto L1a
            r1.close()
        L1a:
            return r0
        L1b:
            java.lang.String r2 = "support_api"
            int r2 = r1.getColumnIndex(r2)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            java.lang.String r3 = "package"
            int r3 = r1.getColumnIndex(r3)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            java.lang.String r4 = "sso_activity"
            int r4 = r1.getColumnIndex(r4)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            boolean r5 = r1.moveToFirst()     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            if (r5 == 0) goto L6c
            r5 = -1
            java.lang.String r2 = r1.getString(r2)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            int r2 = java.lang.Integer.parseInt(r2)     // Catch: java.lang.NumberFormatException -> L3d java.lang.Exception -> L6f java.lang.Throwable -> L8c
            goto L46
        L3d:
            r2 = move-exception
            com.mob.tools.log.NLog r6 = cn.sharesdk.framework.utils.e.b()     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            r6.d(r2)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            r2 = -1
        L46:
            java.lang.String r3 = r1.getString(r3)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            if (r4 <= 0) goto L4f
            r1.getString(r4)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
        L4f:
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            if (r4 != 0) goto L6c
            boolean r4 = a(r3)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            if (r4 == 0) goto L6c
            cn.sharesdk.sina.weibo.g$a r4 = new cn.sharesdk.sina.weibo.g$a     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            r4.<init>()     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            cn.sharesdk.sina.weibo.g.a.a(r4, r3)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            cn.sharesdk.sina.weibo.g.a.a(r4, r2)     // Catch: java.lang.Exception -> L6f java.lang.Throwable -> L8c
            if (r1 == 0) goto L6b
            r1.close()
        L6b:
            return r4
        L6c:
            if (r1 == 0) goto L8b
            goto L88
        L6f:
            r2 = move-exception
            goto L78
        L71:
            r1 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
            goto L8d
        L76:
            r2 = move-exception
            r1 = r0
        L78:
            com.mob.tools.log.NLog r3 = cn.sharesdk.framework.utils.e.b()     // Catch: java.lang.Throwable -> L8c
            java.lang.String r2 = r2.getMessage()     // Catch: java.lang.Throwable -> L8c
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L8c
            r3.e(r2, r4)     // Catch: java.lang.Throwable -> L8c
            if (r1 == 0) goto L8b
        L88:
            r1.close()
        L8b:
            return r0
        L8c:
            r0 = move-exception
        L8d:
            if (r1 == 0) goto L92
            r1.close()
        L92:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.sina.weibo.g.d():cn.sharesdk.sina.weibo.g$a");
    }

    private a e() {
        a b2;
        Intent intent = new Intent("com.sina.weibo.action.sdkidentity");
        intent.addCategory("android.intent.category.DEFAULT");
        List<ResolveInfo> queryIntentServices = MobSDK.getContext().getPackageManager().queryIntentServices(intent, 0);
        a aVar = null;
        if (queryIntentServices == null || queryIntentServices.isEmpty()) {
            return null;
        }
        for (ResolveInfo resolveInfo : queryIntentServices) {
            if (resolveInfo.serviceInfo != null && resolveInfo.serviceInfo.applicationInfo != null && !TextUtils.isEmpty(resolveInfo.serviceInfo.applicationInfo.packageName) && (b2 = b(resolveInfo.serviceInfo.applicationInfo.packageName)) != null && (aVar == null || aVar.b() < b2.b())) {
                aVar = b2;
            }
        }
        return aVar;
    }

    public synchronized String b() {
        if (c == null) {
            c = c();
        }
        return c.a();
    }
}
