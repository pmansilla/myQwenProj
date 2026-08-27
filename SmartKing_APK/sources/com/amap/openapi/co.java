package com.amap.openapi;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.CellState;
import com.amap.location.common.model.FPS;
import com.amap.location.common.model.WiFi;
import java.util.ArrayList;

/* compiled from: ProviderUtil.java */
/* loaded from: classes.dex */
public class co {
    private static final String[] a = {"ENABLE", "LAT,", "LNG", "RADIUS", "TYPE"};

    /* compiled from: ProviderUtil.java */
    /* loaded from: classes.dex */
    public static class a {
        public boolean a;
        public AmapLoc b;
    }

    public static Cursor a(boolean z, AmapLoc amapLoc) {
        MatrixCursor matrixCursor = new MatrixCursor(a);
        Object[] objArr = new Object[a.length];
        objArr[matrixCursor.getColumnIndex(a[0])] = Integer.valueOf(z ? 1 : 0);
        if (z && amapLoc != null) {
            objArr[matrixCursor.getColumnIndex(a[1])] = Double.valueOf(amapLoc.getLat());
            objArr[matrixCursor.getColumnIndex(a[2])] = Double.valueOf(amapLoc.getLon());
            objArr[matrixCursor.getColumnIndex(a[3])] = Integer.valueOf((int) amapLoc.getAccuracy());
            objArr[matrixCursor.getColumnIndex(a[4])] = amapLoc.getType();
        }
        matrixCursor.addRow(objArr);
        return matrixCursor;
    }

    public static AmapLoc a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            String[] split = str.split(",");
            double parseDouble = Double.parseDouble(split[0]);
            double parseDouble2 = Double.parseDouble(split[1]);
            float parseFloat = Float.parseFloat(split[2]);
            String str2 = split[3];
            AmapLoc amapLoc = new AmapLoc();
            try {
                amapLoc.setRetype(str2);
                amapLoc.setLat(parseDouble);
                amapLoc.setLon(parseDouble2);
                amapLoc.setAccuracy(parseFloat);
            } catch (Exception unused) {
            }
            return amapLoc;
        } catch (Exception unused2) {
            return null;
        }
    }

    public static FPS a(String str, String str2) {
        FPS fps = new FPS();
        int i = 0;
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split(":");
            CellState cellState = null;
            try {
                if (split.length == 3) {
                    cellState = new CellState(2, true);
                    cellState.sid = Integer.parseInt(split[0]);
                    cellState.nid = Integer.parseInt(split[1]);
                    cellState.bid = Integer.parseInt(split[2]);
                } else if (split.length == 4) {
                    cellState = new CellState(1, true);
                    cellState.mcc = Integer.parseInt(split[0]);
                    cellState.mnc = Integer.parseInt(split[1]);
                    cellState.lac = Integer.parseInt(split[2]);
                    cellState.cid = Integer.parseInt(split[3]);
                }
                fps.cellStatus.mainCell = cellState;
            } catch (Exception unused) {
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            ArrayList arrayList = new ArrayList();
            try {
                int length = str2.length();
                do {
                    int indexOf = str2.indexOf(44, i);
                    if (indexOf == -1) {
                        break;
                    }
                    long parseLong = Long.parseLong(str2.substring(i, indexOf));
                    int i2 = indexOf + 1;
                    if (i2 < length) {
                        int indexOf2 = str2.indexOf(59, i2);
                        if (indexOf2 == -1) {
                            indexOf2 = length;
                        }
                        arrayList.add(new WiFi(parseLong, null, Integer.parseInt(str2.substring(i2, indexOf2)), 0, 0L));
                        i = indexOf2 + 1;
                    } else {
                        i = i2;
                    }
                } while (i < length);
                fps.wifiStatus.setWifiList(arrayList);
            } catch (Exception unused2) {
            }
        }
        return fps;
    }

    public static a a(Cursor cursor) {
        a aVar = new a();
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(a[0]);
            if (columnIndex != -1) {
                aVar.a = cursor.getInt(columnIndex) == 1;
            }
            if (aVar.a) {
                int columnIndex2 = cursor.getColumnIndex(a[1]);
                double d = columnIndex2 != -1 ? cursor.getDouble(columnIndex2) : 0.0d;
                int columnIndex3 = cursor.getColumnIndex(a[2]);
                double d2 = columnIndex3 != -1 ? cursor.getDouble(columnIndex3) : 0.0d;
                int columnIndex4 = cursor.getColumnIndex(a[3]);
                int i = columnIndex4 != -1 ? cursor.getInt(columnIndex4) : 0;
                int columnIndex5 = cursor.getColumnIndex(a[4]);
                String string = columnIndex5 != -1 ? cursor.getString(columnIndex5) : "";
                AmapLoc amapLoc = new AmapLoc();
                amapLoc.setTime(System.currentTimeMillis());
                amapLoc.setCoord(0);
                amapLoc.setType(string);
                amapLoc.setLat(d);
                amapLoc.setLon(d2);
                amapLoc.setAccuracy(i);
                if (amapLoc.isLocationCorrect()) {
                    aVar.b = amapLoc;
                }
            }
        }
        return aVar;
    }

    public static String[] a(String str, FPS fps, AmapLoc amapLoc, int i) {
        String[] strArr = new String[5];
        strArr[0] = str;
        if (fps != null) {
            strArr[1] = cn.a(fps.cellStatus);
            if (fps.wifiStatus != null && fps.wifiStatus.numWiFis() > 0) {
                StringBuilder sb = new StringBuilder();
                int numWiFis = fps.wifiStatus.numWiFis();
                for (int i2 = 0; i2 < numWiFis; i2++) {
                    WiFi wiFi = fps.wifiStatus.getWiFi(i2);
                    sb.append(wiFi.mac);
                    sb.append(",");
                    sb.append(wiFi.rssi);
                    sb.append(";");
                }
                sb.deleteCharAt(sb.length() - 1);
                strArr[2] = sb.toString();
            }
        }
        if (amapLoc != null && amapLoc.isLocationCorrect()) {
            strArr[3] = amapLoc.getLat() + "," + amapLoc.getLon() + "," + amapLoc.getAccuracy() + "," + amapLoc.getRetype();
        }
        strArr[4] = String.valueOf(i);
        return strArr;
    }
}
