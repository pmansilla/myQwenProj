package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import org.json.JSONObject;

/* compiled from: MapParser.java */
/* loaded from: classes.dex */
public final class kl {
    private StringBuilder a = new StringBuilder();

    public final ki a(String str, Context context, iz izVar) {
        ki kiVar = new ki("");
        kiVar.setErrorCode(7);
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status") || !jSONObject.has("info")) {
                this.a.append("json is error " + str);
            }
            String string = jSONObject.getString("status");
            String string2 = jSONObject.getString("info");
            if (string.equals(AmapLoc.RESULT_TYPE_GPS)) {
                this.a.append("auth fail:" + string2);
            }
        } catch (Throwable th) {
            this.a.append("json exception error:" + th.getMessage());
            kw.a(th, "MapParser", "paseAuthFailurJson");
        }
        try {
            StringBuilder sb = this.a;
            sb.append("#SHA1AndPackage#");
            sb.append(hd.e(context));
            String str2 = izVar.b.get("gsid").get(0);
            if (!TextUtils.isEmpty(str2)) {
                StringBuilder sb2 = this.a;
                sb2.append(" #gsid#");
                sb2.append(str2);
            }
            String str3 = izVar.c;
            if (!TextUtils.isEmpty(str3)) {
                this.a.append(" #csid#" + str3);
            }
        } catch (Throwable unused) {
        }
        kiVar.setLocationDetail(this.a.toString());
        if (this.a.length() > 0) {
            this.a.delete(0, this.a.length());
        }
        return kiVar;
    }

    /* JADX WARN: Code restructure failed: missing block: B:149:0x02ac, code lost:
    
        if (r15 == null) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x02ae, code lost:
    
        r15.clear();
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x02ef, code lost:
    
        if (r14.a.length() <= 0) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x02f1, code lost:
    
        r14.a.delete(0, r14.a.length());
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x02fc, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x02e6, code lost:
    
        if (r15 == null) goto L115;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0300  */
    /* JADX WARN: Type inference failed for: r15v0, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r15v4 */
    /* JADX WARN: Type inference failed for: r15v5, types: [java.nio.ByteBuffer] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.amap.api.mapcore.util.ki a(byte[] r15) {
        /*
            Method dump skipped, instructions count: 772
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.kl.a(byte[]):com.amap.api.mapcore.util.ki");
    }
}
