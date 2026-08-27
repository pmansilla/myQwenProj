package com.amap.openapi;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import java.util.List;

/* compiled from: CellUtil.java */
/* loaded from: classes.dex */
public class ax {
    private static byte a(TelephonyManager telephonyManager) {
        try {
            return (byte) telephonyManager.getNetworkType();
        } catch (Throwable unused) {
            return (byte) 0;
        }
    }

    private static int a(int i) {
        return (i * 2) - 113;
    }

    public static CellInfo a(List<CellInfo> list) {
        if (Build.VERSION.SDK_INT < 17) {
            return null;
        }
        if (list != null) {
            for (CellInfo cellInfo : list) {
                if (((cellInfo instanceof CellInfoLte) || (cellInfo instanceof CellInfoCdma) || (cellInfo instanceof CellInfoGsm)) && cellInfo.isRegistered()) {
                    return cellInfo;
                }
                if (Build.VERSION.SDK_INT >= 18 && (cellInfo instanceof CellInfoWcdma) && cellInfo.isRegistered()) {
                    return cellInfo;
                }
            }
        }
        return null;
    }

    public static void a(@NonNull Context context, @NonNull q qVar, @Nullable CellLocation cellLocation, @Nullable SignalStrength signalStrength, @Nullable List<CellInfo> list) {
        List neighboringCellInfo;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        qVar.a(a(telephonyManager), b(telephonyManager));
        if (cellLocation != null) {
            if (cellLocation instanceof GsmCellLocation) {
                try {
                    neighboringCellInfo = telephonyManager.getNeighboringCellInfo();
                } catch (Throwable unused) {
                }
                a(qVar, cellLocation, signalStrength, neighboringCellInfo);
            }
            neighboringCellInfo = null;
            a(qVar, cellLocation, signalStrength, neighboringCellInfo);
        }
        if (list != null) {
            a(qVar, list);
        }
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [com.amap.openapi.w, T] */
    /* JADX WARN: Type inference failed for: r8v2, types: [T, com.amap.openapi.p] */
    /* JADX WARN: Type inference failed for: r8v3, types: [com.amap.openapi.w, T] */
    private static void a(@NonNull q qVar, @NonNull CellLocation cellLocation, @Nullable SignalStrength signalStrength, @Nullable List<NeighboringCellInfo> list) {
        if (!(cellLocation instanceof GsmCellLocation)) {
            if (cellLocation instanceof CdmaCellLocation) {
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                ?? pVar = new p();
                pVar.d = cdmaCellLocation.getBaseStationLatitude();
                pVar.e = cdmaCellLocation.getBaseStationLongitude();
                pVar.a = cdmaCellLocation.getSystemId();
                pVar.b = cdmaCellLocation.getNetworkId();
                pVar.c = cdmaCellLocation.getBaseStationId();
                if (signalStrength != null) {
                    pVar.f = signalStrength.getCdmaDbm();
                }
                r rVar = new r();
                rVar.a = (byte) 2;
                rVar.f = pVar;
                rVar.b = (byte) 1;
                rVar.c = (byte) 0;
                qVar.c.add(rVar);
                return;
            }
            return;
        }
        GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
        ?? wVar = new w();
        wVar.c = gsmCellLocation.getLac();
        wVar.d = gsmCellLocation.getCid();
        if (Build.VERSION.SDK_INT >= 9) {
            wVar.i = gsmCellLocation.getPsc();
        }
        if (signalStrength != null) {
            int gsmSignalStrength = signalStrength.getGsmSignalStrength();
            wVar.e = gsmSignalStrength == 99 ? Integer.MAX_VALUE : a(gsmSignalStrength);
        }
        r rVar2 = new r();
        rVar2.a = (byte) 1;
        rVar2.f = wVar;
        rVar2.b = (byte) 1;
        rVar2.c = (byte) 0;
        qVar.c.add(rVar2);
        if (list != null) {
            for (NeighboringCellInfo neighboringCellInfo : list) {
                ?? wVar2 = new w();
                wVar2.c = neighboringCellInfo.getLac();
                wVar2.d = neighboringCellInfo.getCid();
                wVar2.e = neighboringCellInfo.getRssi();
                wVar2.i = neighboringCellInfo.getPsc();
                r rVar3 = new r();
                rVar3.a = (byte) 1;
                rVar3.f = wVar2;
                rVar3.b = (byte) 0;
                rVar3.c = (byte) 0;
                qVar.c.add(rVar3);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [T, com.amap.openapi.p] */
    private static void a(@NonNull q qVar, @NonNull List<CellInfo> list) {
        r rVar;
        T t;
        byte b;
        x xVar;
        if (Build.VERSION.SDK_INT >= 17) {
            for (CellInfo cellInfo : list) {
                if (cellInfo instanceof CellInfoCdma) {
                    CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                    CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
                    ?? pVar = new p();
                    pVar.d = cellIdentity.getLatitude();
                    pVar.e = cellIdentity.getLongitude();
                    pVar.a = cellIdentity.getSystemId();
                    pVar.b = cellIdentity.getNetworkId();
                    pVar.c = cellIdentity.getBasestationId();
                    pVar.f = cellInfoCdma.getCellSignalStrength().getCdmaDbm();
                    pVar.g = cellInfoCdma.getCellSignalStrength().getAsuLevel();
                    rVar = new r();
                    rVar.a = (byte) 2;
                    rVar.f = pVar;
                } else {
                    if (cellInfo instanceof CellInfoGsm) {
                        CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                        CellIdentityGsm cellIdentity2 = cellInfoGsm.getCellIdentity();
                        w wVar = new w();
                        wVar.a = cellIdentity2.getMcc();
                        wVar.b = cellIdentity2.getMnc();
                        wVar.c = cellIdentity2.getLac();
                        wVar.d = cellIdentity2.getCid();
                        wVar.e = cellInfoGsm.getCellSignalStrength().getDbm();
                        wVar.h = cellInfoGsm.getCellSignalStrength().getAsuLevel();
                        if (Build.VERSION.SDK_INT >= 24) {
                            wVar.f = cellIdentity2.getArfcn();
                            wVar.g = cellIdentity2.getBsic();
                        }
                        rVar = new r();
                        rVar.a = (byte) 1;
                        t = wVar;
                    } else {
                        if (cellInfo instanceof CellInfoLte) {
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                            CellIdentityLte cellIdentity3 = cellInfoLte.getCellIdentity();
                            x xVar2 = new x();
                            xVar2.a = cellIdentity3.getMcc();
                            xVar2.b = cellIdentity3.getMnc();
                            xVar2.c = cellIdentity3.getTac();
                            xVar2.d = cellIdentity3.getCi();
                            xVar2.e = cellIdentity3.getPci();
                            xVar2.f = cellInfoLte.getCellSignalStrength().getDbm();
                            xVar2.h = cellInfoLte.getCellSignalStrength().getAsuLevel();
                            if (Build.VERSION.SDK_INT >= 24) {
                                xVar2.g = cellIdentity3.getEarfcn();
                            }
                            rVar = new r();
                            b = 3;
                            xVar = xVar2;
                        } else if (Build.VERSION.SDK_INT >= 18 && (cellInfo instanceof CellInfoWcdma)) {
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                            CellIdentityWcdma cellIdentity4 = cellInfoWcdma.getCellIdentity();
                            z zVar = new z();
                            zVar.a = cellIdentity4.getMcc();
                            zVar.b = cellIdentity4.getMnc();
                            zVar.c = cellIdentity4.getLac();
                            zVar.d = cellIdentity4.getCid();
                            zVar.e = cellIdentity4.getPsc();
                            zVar.f = cellInfoWcdma.getCellSignalStrength().getDbm();
                            zVar.h = cellInfoWcdma.getCellSignalStrength().getAsuLevel();
                            if (Build.VERSION.SDK_INT >= 24) {
                                zVar.g = cellIdentity4.getUarfcn();
                            }
                            rVar = new r();
                            b = 4;
                            xVar = zVar;
                        }
                        rVar.a = b;
                        t = xVar;
                    }
                    rVar.f = t;
                }
                rVar.b = cellInfo.isRegistered() ? (byte) 1 : (byte) 0;
                rVar.c = (byte) 1;
                qVar.c.add(rVar);
            }
        }
    }

    public static boolean a(Context context) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            return (Build.VERSION.SDK_INT >= 17 ? Settings.Global.getInt(contentResolver, "airplane_mode_on", 0) : Settings.System.getInt(contentResolver, "airplane_mode_on", 0)) != 0;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean a(CellInfo cellInfo, CellInfo cellInfo2) {
        if (cellInfo == cellInfo2) {
            return true;
        }
        if (cellInfo != null && cellInfo2 != null && Build.VERSION.SDK_INT >= 17) {
            if ((cellInfo instanceof CellInfoGsm) && (cellInfo2 instanceof CellInfoGsm)) {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                CellIdentityGsm cellIdentity2 = ((CellInfoGsm) cellInfo2).getCellIdentity();
                return cellIdentity.getCid() == cellIdentity2.getCid() && cellIdentity.getLac() == cellIdentity2.getLac();
            }
            if ((cellInfo instanceof CellInfoCdma) && (cellInfo2 instanceof CellInfoCdma)) {
                CellIdentityCdma cellIdentity3 = ((CellInfoCdma) cellInfo).getCellIdentity();
                CellIdentityCdma cellIdentity4 = ((CellInfoCdma) cellInfo2).getCellIdentity();
                return cellIdentity3.getBasestationId() == cellIdentity4.getBasestationId() && cellIdentity3.getNetworkId() == cellIdentity4.getNetworkId() && cellIdentity3.getSystemId() == cellIdentity4.getSystemId();
            }
            if ((cellInfo instanceof CellInfoLte) && (cellInfo2 instanceof CellInfoLte)) {
                CellIdentityLte cellIdentity5 = ((CellInfoLte) cellInfo).getCellIdentity();
                CellIdentityLte cellIdentity6 = ((CellInfoLte) cellInfo2).getCellIdentity();
                return cellIdentity5.getCi() == cellIdentity6.getCi() && cellIdentity5.getTac() == cellIdentity6.getTac();
            }
            if (Build.VERSION.SDK_INT >= 18 && (cellInfo instanceof CellInfoWcdma) && (cellInfo2 instanceof CellInfoWcdma)) {
                CellIdentityWcdma cellIdentity7 = ((CellInfoWcdma) cellInfo).getCellIdentity();
                CellIdentityWcdma cellIdentity8 = ((CellInfoWcdma) cellInfo2).getCellIdentity();
                if (cellIdentity7.getCid() == cellIdentity8.getCid() && cellIdentity7.getLac() == cellIdentity8.getLac()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean a(CellLocation cellLocation, CellLocation cellLocation2) {
        if (cellLocation == cellLocation2) {
            return true;
        }
        if (cellLocation != null && cellLocation2 != null) {
            if ((cellLocation instanceof GsmCellLocation) && (cellLocation2 instanceof GsmCellLocation)) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                GsmCellLocation gsmCellLocation2 = (GsmCellLocation) cellLocation2;
                return gsmCellLocation.getCid() == gsmCellLocation2.getCid() && gsmCellLocation.getLac() == gsmCellLocation2.getLac();
            }
            if ((cellLocation instanceof CdmaCellLocation) && (cellLocation2 instanceof CdmaCellLocation)) {
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                CdmaCellLocation cdmaCellLocation2 = (CdmaCellLocation) cellLocation2;
                if (cdmaCellLocation.getBaseStationId() == cdmaCellLocation2.getBaseStationId() && cdmaCellLocation.getNetworkId() == cdmaCellLocation2.getNetworkId() && cdmaCellLocation.getSystemId() == cdmaCellLocation2.getSystemId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String b(TelephonyManager telephonyManager) {
        String str;
        try {
            str = telephonyManager.getNetworkOperator();
        } catch (Throwable unused) {
            str = null;
        }
        return str == null ? "" : str;
    }
}
