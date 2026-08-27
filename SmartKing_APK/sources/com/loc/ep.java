package com.loc;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import com.litesuits.orm.db.assit.SQLBuilder;
import org.json.JSONObject;

/* compiled from: Parser.java */
/* loaded from: classes.dex */
public final class ep {
    private StringBuilder a = new StringBuilder();
    private AMapLocationClientOption b = new AMapLocationClientOption();

    private void a(AMapLocationServer aMapLocationServer, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        StringBuilder sb;
        StringBuilder sb2 = new StringBuilder();
        if (!TextUtils.isEmpty(str)) {
            sb2.append(str);
            sb2.append(SQLBuilder.BLANK);
        }
        if (!TextUtils.isEmpty(str2) && (this.b.getGeoLanguage() != AMapLocationClientOption.GeoLanguage.EN ? !str.contains("市") || !str.equals(str2) : !str2.equals(str))) {
            sb2.append(str2);
            sb2.append(SQLBuilder.BLANK);
        }
        if (!TextUtils.isEmpty(str3)) {
            sb2.append(str3);
            sb2.append(SQLBuilder.BLANK);
        }
        if (!TextUtils.isEmpty(str4)) {
            sb2.append(str4);
            sb2.append(SQLBuilder.BLANK);
        }
        if (!TextUtils.isEmpty(str5)) {
            sb2.append(str5);
            sb2.append(SQLBuilder.BLANK);
        }
        if (!TextUtils.isEmpty(str6)) {
            if (TextUtils.isEmpty(str7) || this.b.getGeoLanguage() == AMapLocationClientOption.GeoLanguage.EN) {
                sb2.append("Near " + str6);
                sb = new StringBuilder("Near ");
                sb.append(str6);
            } else {
                sb2.append("靠近");
                sb2.append(str6);
                sb2.append(SQLBuilder.BLANK);
                sb = new StringBuilder("在");
                sb.append(str6);
                sb.append("附近");
            }
            aMapLocationServer.setDescription(sb.toString());
        }
        Bundle bundle = new Bundle();
        bundle.putString("citycode", aMapLocationServer.getCityCode());
        bundle.putString("desc", sb2.toString());
        bundle.putString("adcode", aMapLocationServer.getAdCode());
        aMapLocationServer.setExtras(bundle);
        aMapLocationServer.g(sb2.toString());
        String adCode = aMapLocationServer.getAdCode();
        aMapLocationServer.setAddress((adCode == null || adCode.trim().length() <= 0 || this.b.getGeoLanguage() == AMapLocationClientOption.GeoLanguage.EN) ? sb2.toString() : sb2.toString().replace(SQLBuilder.BLANK, ""));
    }

    private static String b(String str) {
        return "[]".equals(str) ? "" : str;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(44:21|(2:22|23)|24|(2:25|26)|(2:28|29)|30|31|32|34|35|36|37|38|39|40|41|42|43|44|46|47|48|49|(4:50|51|52|53)|54|55|56|57|59|60|61|62|63|64|65|66|67|69|70|71|72|(2:73|74)|75|76) */
    /* JADX WARN: Can't wrap try/catch for region: R(45:21|22|23|24|(2:25|26)|(2:28|29)|30|31|32|34|35|36|37|38|39|40|41|42|43|44|46|47|48|49|(4:50|51|52|53)|54|55|56|57|59|60|61|62|63|64|65|66|67|69|70|71|72|(2:73|74)|75|76) */
    /* JADX WARN: Code restructure failed: missing block: B:100:0x00e9, code lost:
    
        r8 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x017b, code lost:
    
        r2 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x014d, code lost:
    
        r2 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x011b, code lost:
    
        r2 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0100, code lost:
    
        r2 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x00e8, code lost:
    
        r2 = r8;
     */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01b6 A[Catch: all -> 0x0271, Throwable -> 0x0273, TryCatch #15 {all -> 0x0271, blocks: (B:9:0x0033, B:11:0x0039, B:15:0x004d, B:17:0x005b, B:19:0x006b, B:21:0x0097, B:23:0x00b0, B:24:0x00ba, B:26:0x00c5, B:29:0x00cc, B:30:0x00d2, B:32:0x00dd, B:35:0x00e4, B:36:0x00ea, B:38:0x00f5, B:40:0x00fc, B:42:0x0102, B:44:0x010d, B:47:0x0114, B:49:0x011e, B:51:0x0129, B:53:0x0130, B:55:0x0137, B:57:0x0142, B:60:0x0149, B:62:0x0150, B:64:0x015b, B:65:0x0165, B:67:0x0170, B:70:0x0177, B:72:0x017e, B:74:0x0189, B:76:0x01a2, B:108:0x01a5, B:110:0x01b6, B:111:0x01bf, B:113:0x01c5, B:115:0x01d0, B:116:0x01da, B:118:0x01e5, B:123:0x01ef, B:125:0x01f5, B:126:0x01fe, B:128:0x0204, B:129:0x020b, B:131:0x0211, B:133:0x0216, B:135:0x0219, B:138:0x0227, B:140:0x022d, B:141:0x0232, B:143:0x0245, B:148:0x025d, B:149:0x0268), top: B:8:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x01c5 A[Catch: all -> 0x0271, Throwable -> 0x0273, TRY_LEAVE, TryCatch #15 {all -> 0x0271, blocks: (B:9:0x0033, B:11:0x0039, B:15:0x004d, B:17:0x005b, B:19:0x006b, B:21:0x0097, B:23:0x00b0, B:24:0x00ba, B:26:0x00c5, B:29:0x00cc, B:30:0x00d2, B:32:0x00dd, B:35:0x00e4, B:36:0x00ea, B:38:0x00f5, B:40:0x00fc, B:42:0x0102, B:44:0x010d, B:47:0x0114, B:49:0x011e, B:51:0x0129, B:53:0x0130, B:55:0x0137, B:57:0x0142, B:60:0x0149, B:62:0x0150, B:64:0x015b, B:65:0x0165, B:67:0x0170, B:70:0x0177, B:72:0x017e, B:74:0x0189, B:76:0x01a2, B:108:0x01a5, B:110:0x01b6, B:111:0x01bf, B:113:0x01c5, B:115:0x01d0, B:116:0x01da, B:118:0x01e5, B:123:0x01ef, B:125:0x01f5, B:126:0x01fe, B:128:0x0204, B:129:0x020b, B:131:0x0211, B:133:0x0216, B:135:0x0219, B:138:0x0227, B:140:0x022d, B:141:0x0232, B:143:0x0245, B:148:0x025d, B:149:0x0268), top: B:8:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x01f5 A[Catch: all -> 0x0271, Throwable -> 0x0273, TryCatch #15 {all -> 0x0271, blocks: (B:9:0x0033, B:11:0x0039, B:15:0x004d, B:17:0x005b, B:19:0x006b, B:21:0x0097, B:23:0x00b0, B:24:0x00ba, B:26:0x00c5, B:29:0x00cc, B:30:0x00d2, B:32:0x00dd, B:35:0x00e4, B:36:0x00ea, B:38:0x00f5, B:40:0x00fc, B:42:0x0102, B:44:0x010d, B:47:0x0114, B:49:0x011e, B:51:0x0129, B:53:0x0130, B:55:0x0137, B:57:0x0142, B:60:0x0149, B:62:0x0150, B:64:0x015b, B:65:0x0165, B:67:0x0170, B:70:0x0177, B:72:0x017e, B:74:0x0189, B:76:0x01a2, B:108:0x01a5, B:110:0x01b6, B:111:0x01bf, B:113:0x01c5, B:115:0x01d0, B:116:0x01da, B:118:0x01e5, B:123:0x01ef, B:125:0x01f5, B:126:0x01fe, B:128:0x0204, B:129:0x020b, B:131:0x0211, B:133:0x0216, B:135:0x0219, B:138:0x0227, B:140:0x022d, B:141:0x0232, B:143:0x0245, B:148:0x025d, B:149:0x0268), top: B:8:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0204 A[Catch: all -> 0x0271, Throwable -> 0x0273, TryCatch #15 {all -> 0x0271, blocks: (B:9:0x0033, B:11:0x0039, B:15:0x004d, B:17:0x005b, B:19:0x006b, B:21:0x0097, B:23:0x00b0, B:24:0x00ba, B:26:0x00c5, B:29:0x00cc, B:30:0x00d2, B:32:0x00dd, B:35:0x00e4, B:36:0x00ea, B:38:0x00f5, B:40:0x00fc, B:42:0x0102, B:44:0x010d, B:47:0x0114, B:49:0x011e, B:51:0x0129, B:53:0x0130, B:55:0x0137, B:57:0x0142, B:60:0x0149, B:62:0x0150, B:64:0x015b, B:65:0x0165, B:67:0x0170, B:70:0x0177, B:72:0x017e, B:74:0x0189, B:76:0x01a2, B:108:0x01a5, B:110:0x01b6, B:111:0x01bf, B:113:0x01c5, B:115:0x01d0, B:116:0x01da, B:118:0x01e5, B:123:0x01ef, B:125:0x01f5, B:126:0x01fe, B:128:0x0204, B:129:0x020b, B:131:0x0211, B:133:0x0216, B:135:0x0219, B:138:0x0227, B:140:0x022d, B:141:0x0232, B:143:0x0245, B:148:0x025d, B:149:0x0268), top: B:8:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0211 A[Catch: all -> 0x0271, Throwable -> 0x0273, TRY_LEAVE, TryCatch #15 {all -> 0x0271, blocks: (B:9:0x0033, B:11:0x0039, B:15:0x004d, B:17:0x005b, B:19:0x006b, B:21:0x0097, B:23:0x00b0, B:24:0x00ba, B:26:0x00c5, B:29:0x00cc, B:30:0x00d2, B:32:0x00dd, B:35:0x00e4, B:36:0x00ea, B:38:0x00f5, B:40:0x00fc, B:42:0x0102, B:44:0x010d, B:47:0x0114, B:49:0x011e, B:51:0x0129, B:53:0x0130, B:55:0x0137, B:57:0x0142, B:60:0x0149, B:62:0x0150, B:64:0x015b, B:65:0x0165, B:67:0x0170, B:70:0x0177, B:72:0x017e, B:74:0x0189, B:76:0x01a2, B:108:0x01a5, B:110:0x01b6, B:111:0x01bf, B:113:0x01c5, B:115:0x01d0, B:116:0x01da, B:118:0x01e5, B:123:0x01ef, B:125:0x01f5, B:126:0x01fe, B:128:0x0204, B:129:0x020b, B:131:0x0211, B:133:0x0216, B:135:0x0219, B:138:0x0227, B:140:0x022d, B:141:0x0232, B:143:0x0245, B:148:0x025d, B:149:0x0268), top: B:8:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:140:0x022d A[Catch: all -> 0x0271, Throwable -> 0x0273, TryCatch #15 {all -> 0x0271, blocks: (B:9:0x0033, B:11:0x0039, B:15:0x004d, B:17:0x005b, B:19:0x006b, B:21:0x0097, B:23:0x00b0, B:24:0x00ba, B:26:0x00c5, B:29:0x00cc, B:30:0x00d2, B:32:0x00dd, B:35:0x00e4, B:36:0x00ea, B:38:0x00f5, B:40:0x00fc, B:42:0x0102, B:44:0x010d, B:47:0x0114, B:49:0x011e, B:51:0x0129, B:53:0x0130, B:55:0x0137, B:57:0x0142, B:60:0x0149, B:62:0x0150, B:64:0x015b, B:65:0x0165, B:67:0x0170, B:70:0x0177, B:72:0x017e, B:74:0x0189, B:76:0x01a2, B:108:0x01a5, B:110:0x01b6, B:111:0x01bf, B:113:0x01c5, B:115:0x01d0, B:116:0x01da, B:118:0x01e5, B:123:0x01ef, B:125:0x01f5, B:126:0x01fe, B:128:0x0204, B:129:0x020b, B:131:0x0211, B:133:0x0216, B:135:0x0219, B:138:0x0227, B:140:0x022d, B:141:0x0232, B:143:0x0245, B:148:0x025d, B:149:0x0268), top: B:8:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0245 A[Catch: all -> 0x0271, Throwable -> 0x0273, TryCatch #15 {all -> 0x0271, blocks: (B:9:0x0033, B:11:0x0039, B:15:0x004d, B:17:0x005b, B:19:0x006b, B:21:0x0097, B:23:0x00b0, B:24:0x00ba, B:26:0x00c5, B:29:0x00cc, B:30:0x00d2, B:32:0x00dd, B:35:0x00e4, B:36:0x00ea, B:38:0x00f5, B:40:0x00fc, B:42:0x0102, B:44:0x010d, B:47:0x0114, B:49:0x011e, B:51:0x0129, B:53:0x0130, B:55:0x0137, B:57:0x0142, B:60:0x0149, B:62:0x0150, B:64:0x015b, B:65:0x0165, B:67:0x0170, B:70:0x0177, B:72:0x017e, B:74:0x0189, B:76:0x01a2, B:108:0x01a5, B:110:0x01b6, B:111:0x01bf, B:113:0x01c5, B:115:0x01d0, B:116:0x01da, B:118:0x01e5, B:123:0x01ef, B:125:0x01f5, B:126:0x01fe, B:128:0x0204, B:129:0x020b, B:131:0x0211, B:133:0x0216, B:135:0x0219, B:138:0x0227, B:140:0x022d, B:141:0x0232, B:143:0x0245, B:148:0x025d, B:149:0x0268), top: B:8:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:156:0x026d  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x02b8  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x02c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.autonavi.aps.amapapi.model.AMapLocationServer a(com.autonavi.aps.amapapi.model.AMapLocationServer r21, byte[] r22) {
        /*
            Method dump skipped, instructions count: 716
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ep.a(com.autonavi.aps.amapapi.model.AMapLocationServer, byte[]):com.autonavi.aps.amapapi.model.AMapLocationServer");
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0075, code lost:
    
        if (r3.length() <= 0) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0077, code lost:
    
        r10.setCity(r3);
        r2 = r3;
     */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0083 A[Catch: Throwable -> 0x010b, TryCatch #0 {Throwable -> 0x010b, blocks: (B:3:0x0001, B:5:0x004e, B:7:0x0056, B:9:0x005e, B:12:0x0067, B:13:0x007d, B:15:0x0083, B:16:0x0089, B:18:0x00d3, B:19:0x00e7, B:21:0x00f3, B:22:0x0104, B:29:0x0071, B:31:0x0077), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00d3 A[Catch: Throwable -> 0x010b, TryCatch #0 {Throwable -> 0x010b, blocks: (B:3:0x0001, B:5:0x004e, B:7:0x0056, B:9:0x005e, B:12:0x0067, B:13:0x007d, B:15:0x0083, B:16:0x0089, B:18:0x00d3, B:19:0x00e7, B:21:0x00f3, B:22:0x0104, B:29:0x0071, B:31:0x0077), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00f3 A[Catch: Throwable -> 0x010b, TryCatch #0 {Throwable -> 0x010b, blocks: (B:3:0x0001, B:5:0x004e, B:7:0x0056, B:9:0x005e, B:12:0x0067, B:13:0x007d, B:15:0x0083, B:16:0x0089, B:18:0x00d3, B:19:0x00e7, B:21:0x00f3, B:22:0x0104, B:29:0x0071, B:31:0x0077), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0088  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.autonavi.aps.amapapi.model.AMapLocationServer a(java.lang.String r13) {
        /*
            r12 = this;
            r0 = 0
            com.autonavi.aps.amapapi.model.AMapLocationServer r10 = new com.autonavi.aps.amapapi.model.AMapLocationServer     // Catch: java.lang.Throwable -> L10b
            java.lang.String r1 = ""
            r10.<init>(r1)     // Catch: java.lang.Throwable -> L10b
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch: java.lang.Throwable -> L10b
            r1.<init>(r13)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r13 = "regeocode"
            org.json.JSONObject r13 = r1.optJSONObject(r13)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r1 = "addressComponent"
            org.json.JSONObject r1 = r13.optJSONObject(r1)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = "country"
            java.lang.String r2 = r1.optString(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = b(r2)     // Catch: java.lang.Throwable -> L10b
            r10.setCountry(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = "province"
            java.lang.String r2 = r1.optString(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r3 = b(r2)     // Catch: java.lang.Throwable -> L10b
            r10.setProvince(r3)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = "citycode"
            java.lang.String r2 = r1.optString(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = b(r2)     // Catch: java.lang.Throwable -> L10b
            r10.setCityCode(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r4 = "city"
            java.lang.String r4 = r1.optString(r4)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r5 = "010"
            boolean r5 = r2.endsWith(r5)     // Catch: java.lang.Throwable -> L10b
            if (r5 != 0) goto L6f
            java.lang.String r5 = "021"
            boolean r5 = r2.endsWith(r5)     // Catch: java.lang.Throwable -> L10b
            if (r5 != 0) goto L6f
            java.lang.String r5 = "022"
            boolean r5 = r2.endsWith(r5)     // Catch: java.lang.Throwable -> L10b
            if (r5 != 0) goto L6f
            java.lang.String r5 = "023"
            boolean r2 = r2.endsWith(r5)     // Catch: java.lang.Throwable -> L10b
            if (r2 == 0) goto L67
            goto L6f
        L67:
            java.lang.String r2 = b(r4)     // Catch: java.lang.Throwable -> L10b
            r10.setCity(r2)     // Catch: java.lang.Throwable -> L10b
            goto L7d
        L6f:
            if (r3 == 0) goto L7c
            int r2 = r3.length()     // Catch: java.lang.Throwable -> L10b
            if (r2 <= 0) goto L7c
            r10.setCity(r3)     // Catch: java.lang.Throwable -> L10b
            r2 = r3
            goto L7d
        L7c:
            r2 = r4
        L7d:
            boolean r4 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Throwable -> L10b
            if (r4 == 0) goto L88
            r10.setCity(r3)     // Catch: java.lang.Throwable -> L10b
            r4 = r3
            goto L89
        L88:
            r4 = r2
        L89:
            java.lang.String r2 = "district"
            java.lang.String r2 = r1.optString(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r5 = b(r2)     // Catch: java.lang.Throwable -> L10b
            r10.setDistrict(r5)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = "adcode"
            java.lang.String r2 = r1.optString(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r9 = b(r2)     // Catch: java.lang.Throwable -> L10b
            r10.setAdCode(r9)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = "streetNumber"
            org.json.JSONObject r1 = r1.optJSONObject(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = "street"
            java.lang.String r2 = r1.optString(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r6 = b(r2)     // Catch: java.lang.Throwable -> L10b
            r10.setStreet(r6)     // Catch: java.lang.Throwable -> L10b
            r10.setRoad(r6)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = "number"
            java.lang.String r1 = r1.optString(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r7 = b(r1)     // Catch: java.lang.Throwable -> L10b
            r10.setNumber(r7)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r1 = "pois"
            org.json.JSONArray r1 = r13.optJSONArray(r1)     // Catch: java.lang.Throwable -> L10b
            int r2 = r1.length()     // Catch: java.lang.Throwable -> L10b
            r8 = 0
            if (r2 <= 0) goto Le6
            org.json.JSONObject r1 = r1.getJSONObject(r8)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r2 = "name"
            java.lang.String r1 = r1.optString(r2)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r1 = b(r1)     // Catch: java.lang.Throwable -> L10b
            r10.setPoiName(r1)     // Catch: java.lang.Throwable -> L10b
            r11 = r1
            goto Le7
        Le6:
            r11 = r0
        Le7:
            java.lang.String r1 = "aois"
            org.json.JSONArray r13 = r13.optJSONArray(r1)     // Catch: java.lang.Throwable -> L10b
            int r1 = r13.length()     // Catch: java.lang.Throwable -> L10b
            if (r1 <= 0) goto L104
            org.json.JSONObject r13 = r13.getJSONObject(r8)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r1 = "name"
            java.lang.String r13 = r13.optString(r1)     // Catch: java.lang.Throwable -> L10b
            java.lang.String r13 = b(r13)     // Catch: java.lang.Throwable -> L10b
            r10.setAoiName(r13)     // Catch: java.lang.Throwable -> L10b
        L104:
            r1 = r12
            r2 = r10
            r8 = r11
            r1.a(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L10b
            return r10
        L10b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ep.a(java.lang.String):com.autonavi.aps.amapapi.model.AMapLocationServer");
    }

    public final AMapLocationServer a(String str, Context context, bk bkVar) {
        AMapLocationServer aMapLocationServer = new AMapLocationServer("");
        aMapLocationServer.setErrorCode(7);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("#SHA1AndPackage#");
            stringBuffer.append(u.e(context));
            String str2 = bkVar.b.get("gsid").get(0);
            if (!TextUtils.isEmpty(str2)) {
                stringBuffer.append("#gsid#");
                stringBuffer.append(str2);
            }
            String str3 = bkVar.c;
            if (!TextUtils.isEmpty(str3)) {
                stringBuffer.append("#csid#" + str3);
            }
        } catch (Throwable unused) {
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status") || !jSONObject.has("info")) {
                StringBuilder sb = this.a;
                sb.append("json is error:");
                sb.append(str);
                sb.append(stringBuffer);
                sb.append("#0702");
            }
            String string = jSONObject.getString("status");
            String string2 = jSONObject.getString("info");
            String string3 = jSONObject.getString("infocode");
            if (AmapLoc.RESULT_TYPE_GPS.equals(string)) {
                StringBuilder sb2 = this.a;
                sb2.append("auth fail:");
                sb2.append(string2);
                sb2.append(stringBuffer);
                sb2.append("#0701");
                ey.a(bkVar.d, string3, string2);
            }
        } catch (Throwable th) {
            StringBuilder sb3 = this.a;
            sb3.append("json exception error:");
            sb3.append(th.getMessage());
            sb3.append(stringBuffer);
            sb3.append("#0703");
            es.a(th, "parser", "paseAuthFailurJson");
        }
        aMapLocationServer.setLocationDetail(this.a.toString());
        if (this.a.length() > 0) {
            this.a.delete(0, this.a.length());
        }
        return aMapLocationServer;
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        if (aMapLocationClientOption == null) {
            this.b = new AMapLocationClientOption();
        } else {
            this.b = aMapLocationClientOption;
        }
    }
}
