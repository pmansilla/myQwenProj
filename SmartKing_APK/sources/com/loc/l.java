package com.loc;

import android.os.Bundle;
import com.amap.api.fence.DistrictItem;
import com.amap.api.fence.GeoFence;
import com.amap.api.fence.PoiItem;
import com.amap.api.location.DPoint;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: GeoFenceSearchResultParser.java */
/* loaded from: classes.dex */
public final class l {
    private static long a;

    public static int a(String str, List<GeoFence> list, Bundle bundle) {
        JSONArray optJSONArray;
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("status", 0);
            int optInt2 = jSONObject.optInt("infocode", 0);
            if (optInt != 1 || (optJSONArray = jSONObject.optJSONArray("pois")) == null) {
                return optInt2;
            }
            for (int i = 0; i < optJSONArray.length(); i++) {
                GeoFence geoFence = new GeoFence();
                PoiItem poiItem = new PoiItem();
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                poiItem.setPoiId(jSONObject2.optString(ConnectionModel.ID));
                poiItem.setPoiName(jSONObject2.optString(IMAPStore.ID_NAME));
                poiItem.setPoiType(jSONObject2.optString("type"));
                poiItem.setTypeCode(jSONObject2.optString("typecode"));
                poiItem.setAddress(jSONObject2.optString(IMAPStore.ID_ADDRESS));
                String optString = jSONObject2.optString("location");
                if (optString != null) {
                    String[] split = optString.split(",");
                    poiItem.setLongitude(Double.parseDouble(split[0]));
                    poiItem.setLatitude(Double.parseDouble(split[1]));
                    List<List<DPoint>> arrayList = new ArrayList<>();
                    ArrayList arrayList2 = new ArrayList();
                    DPoint dPoint = new DPoint(poiItem.getLatitude(), poiItem.getLongitude());
                    arrayList2.add(dPoint);
                    arrayList.add(arrayList2);
                    geoFence.setPointList(arrayList);
                    geoFence.setCenter(dPoint);
                }
                poiItem.setTel(jSONObject2.optString("tel"));
                poiItem.setProvince(jSONObject2.optString("pname"));
                poiItem.setCity(jSONObject2.optString("cityname"));
                poiItem.setAdname(jSONObject2.optString("adname"));
                geoFence.setPoiItem(poiItem);
                StringBuilder sb = new StringBuilder();
                sb.append(a());
                geoFence.setFenceId(sb.toString());
                geoFence.setCustomId(bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID));
                geoFence.setPendingIntentAction(bundle.getString("pendingIntentAction"));
                geoFence.setType(2);
                geoFence.setRadius(bundle.getFloat("fenceRadius"));
                geoFence.setExpiration(bundle.getLong("expiration"));
                geoFence.setActivatesAction(bundle.getInt("activatesAction", 1));
                list.add(geoFence);
            }
            return optInt2;
        } catch (Throwable unused) {
            return 5;
        }
    }

    public static synchronized long a() {
        long j;
        synchronized (l.class) {
            long c = fa.c();
            if (c > a) {
                a = c;
            } else {
                a++;
            }
            j = a;
        }
        return j;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00e9 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<com.amap.api.location.DPoint> a(java.util.List<com.amap.api.location.DPoint> r30, float r31) {
        /*
            Method dump skipped, instructions count: 306
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.l.a(java.util.List, float):java.util.List");
    }

    public final int b(String str, List<GeoFence> list, Bundle bundle) {
        JSONArray optJSONArray;
        int i;
        ArrayList arrayList;
        String str2;
        int i2;
        String str3;
        String str4;
        float f;
        long j;
        long j2;
        GeoFence geoFence;
        float f2;
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("status", 0);
            int optInt2 = jSONObject.optInt("infocode", 0);
            String string = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
            String string2 = bundle.getString("pendingIntentAction");
            float f3 = bundle.getFloat("fenceRadius");
            long j3 = bundle.getLong("expiration");
            int i3 = bundle.getInt("activatesAction", 1);
            if (optInt == 1 && (optJSONArray = jSONObject.optJSONArray("districts")) != null) {
                int i4 = 0;
                while (i4 < optJSONArray.length()) {
                    ArrayList arrayList2 = new ArrayList();
                    ArrayList arrayList3 = new ArrayList();
                    GeoFence geoFence2 = new GeoFence();
                    JSONObject jSONObject2 = optJSONArray.getJSONObject(i4);
                    String optString = jSONObject2.optString("citycode");
                    String optString2 = jSONObject2.optString("adcode");
                    String optString3 = jSONObject2.optString(IMAPStore.ID_NAME);
                    JSONArray jSONArray = optJSONArray;
                    String string3 = jSONObject2.getString("center");
                    int i5 = optInt2;
                    DPoint dPoint = new DPoint();
                    if (string3 != null) {
                        i = i4;
                        String[] split = string3.split(",");
                        arrayList = arrayList2;
                        str2 = optString3;
                        dPoint.setLatitude(Double.parseDouble(split[1]));
                        dPoint.setLongitude(Double.parseDouble(split[0]));
                        geoFence2.setCenter(dPoint);
                    } else {
                        i = i4;
                        arrayList = arrayList2;
                        str2 = optString3;
                    }
                    geoFence2.setCustomId(string);
                    geoFence2.setPendingIntentAction(string2);
                    geoFence2.setType(3);
                    geoFence2.setRadius(f3);
                    geoFence2.setExpiration(j3);
                    geoFence2.setActivatesAction(i3);
                    StringBuilder sb = new StringBuilder();
                    sb.append(a());
                    geoFence2.setFenceId(sb.toString());
                    String optString4 = jSONObject2.optString("polyline");
                    if (optString4 != null) {
                        String[] split2 = optString4.split("\\|");
                        int length = split2.length;
                        int i6 = 0;
                        float f4 = Float.MIN_VALUE;
                        float f5 = Float.MAX_VALUE;
                        while (i6 < length) {
                            int i7 = i3;
                            String str5 = split2[i6];
                            String[] strArr = split2;
                            DistrictItem districtItem = new DistrictItem();
                            String str6 = string;
                            List<DPoint> arrayList4 = new ArrayList<>();
                            districtItem.setCitycode(optString);
                            districtItem.setAdcode(optString2);
                            String str7 = optString2;
                            String str8 = str2;
                            districtItem.setDistrictName(str8);
                            String[] split3 = str5.split(";");
                            String str9 = string2;
                            int i8 = 0;
                            while (i8 < split3.length) {
                                String[] strArr2 = split3;
                                String[] split4 = split3[i8].split(",");
                                float f6 = f3;
                                if (split4.length > 1) {
                                    j2 = j3;
                                    geoFence = geoFence2;
                                    f2 = f5;
                                    arrayList4.add(new DPoint(Double.parseDouble(split4[1]), Double.parseDouble(split4[0])));
                                } else {
                                    j2 = j3;
                                    geoFence = geoFence2;
                                    f2 = f5;
                                }
                                i8++;
                                split3 = strArr2;
                                f3 = f6;
                                j3 = j2;
                                geoFence2 = geoFence;
                                f5 = f2;
                            }
                            float f7 = f3;
                            long j4 = j3;
                            GeoFence geoFence3 = geoFence2;
                            float f8 = f5;
                            if (arrayList4.size() > 100.0f) {
                                try {
                                    arrayList4 = a(arrayList4, 100.0f);
                                } catch (Throwable unused) {
                                    return 5;
                                }
                            }
                            arrayList3.add(arrayList4);
                            districtItem.setPolyline(arrayList4);
                            ArrayList arrayList5 = arrayList;
                            arrayList5.add(districtItem);
                            f4 = Math.max(f4, j.b(dPoint, arrayList4));
                            f5 = Math.min(f8, j.a(dPoint, arrayList4));
                            i6++;
                            arrayList = arrayList5;
                            i3 = i7;
                            split2 = strArr;
                            string = str6;
                            optString2 = str7;
                            str2 = str8;
                            string2 = str9;
                            f3 = f7;
                            j3 = j4;
                            geoFence2 = geoFence3;
                        }
                        i2 = i3;
                        str3 = string;
                        str4 = string2;
                        f = f3;
                        j = j3;
                        GeoFence geoFence4 = geoFence2;
                        geoFence4.setMaxDis2Center(f4);
                        geoFence4.setMinDis2Center(f5);
                        geoFence4.setDistrictItemList(arrayList);
                        geoFence4.setPointList(arrayList3);
                        list.add(geoFence4);
                    } else {
                        i2 = i3;
                        str3 = string;
                        str4 = string2;
                        f = f3;
                        j = j3;
                    }
                    i4 = i + 1;
                    optJSONArray = jSONArray;
                    optInt2 = i5;
                    i3 = i2;
                    string = str3;
                    string2 = str4;
                    f3 = f;
                    j3 = j;
                }
            }
            return optInt2;
        } catch (Throwable unused2) {
        }
    }
}
