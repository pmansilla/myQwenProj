package com.amap.location.common.model;

import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import java.util.Locale;
import kotlin.jvm.internal.ShortCompanionObject;

/* loaded from: classes.dex */
public class CellState {
    public static final int I_CDMA_T = 2;
    public static final int I_DEF_CGI_T = 0;
    public static final int I_GSM_T = 1;
    public static final int I_LTE_T = 3;
    public static final int I_WCDMA_T = 4;
    public int bid;

    @Deprecated
    public short cellAge;
    public int cid;
    public int lac;

    @Deprecated
    public long lastUpdateTimeMills;
    public long lastUpdateUtcMills;
    public int latitude;
    public int longitude;
    public int mcc;
    public int mnc;
    public boolean newapi;
    public int nid;
    public short pci;
    public boolean registered;
    public int sid;
    public int signalStrength;
    public int type;

    public CellState(int i, boolean z) {
        this.type = 0;
        this.mcc = 0;
        this.mnc = 0;
        this.lac = 0;
        this.cid = 0;
        this.sid = 0;
        this.nid = 0;
        this.bid = 0;
        this.signalStrength = 99;
        this.cellAge = (short) 0;
        this.lastUpdateTimeMills = 0L;
        this.lastUpdateUtcMills = 0L;
        this.newapi = true;
        this.pci = ShortCompanionObject.MAX_VALUE;
        this.type = i;
        this.registered = z;
    }

    public CellState(int i, boolean z, boolean z2) {
        this.type = 0;
        this.mcc = 0;
        this.mnc = 0;
        this.lac = 0;
        this.cid = 0;
        this.sid = 0;
        this.nid = 0;
        this.bid = 0;
        this.signalStrength = 99;
        this.cellAge = (short) 0;
        this.lastUpdateTimeMills = 0L;
        this.lastUpdateUtcMills = 0L;
        this.newapi = true;
        this.pci = ShortCompanionObject.MAX_VALUE;
        this.type = i;
        this.registered = z;
        this.newapi = z2;
    }

    private boolean bidValid(int i) {
        return i >= 0 && i <= 65535;
    }

    private boolean cidValid(int i) {
        return i >= 0 && i <= 268435455;
    }

    private boolean lacValid(int i) {
        return i >= 0 && i <= 65535;
    }

    private boolean nidValid(int i) {
        return i >= 0 && i <= 65535;
    }

    private boolean sidValid(int i) {
        return i > 0 && i <= 32767;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public CellState m14clone() {
        CellState cellState = new CellState(this.type, this.registered, this.newapi);
        cellState.mcc = this.mcc;
        cellState.mnc = this.mnc;
        cellState.lac = this.lac;
        cellState.cid = this.cid;
        cellState.sid = this.sid;
        cellState.nid = this.nid;
        cellState.bid = this.bid;
        cellState.signalStrength = this.signalStrength;
        cellState.latitude = this.latitude;
        cellState.longitude = this.longitude;
        cellState.cellAge = this.cellAge;
        cellState.lastUpdateTimeMills = this.lastUpdateTimeMills;
        cellState.lastUpdateUtcMills = this.lastUpdateUtcMills;
        cellState.pci = this.pci;
        return cellState;
    }

    public String getKey() {
        String keyWithOutInterface = getKeyWithOutInterface();
        if (TextUtils.isEmpty(keyWithOutInterface)) {
            return "";
        }
        return (this.newapi ? 1 : 0) + "#" + keyWithOutInterface;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0004. Please report as an issue. */
    public String getKeyWithOutInterface() {
        StringBuilder sb;
        int i;
        switch (this.type) {
            case 1:
            case 3:
            case 4:
                sb = new StringBuilder();
                sb.append(this.type);
                sb.append("#");
                sb.append(this.mcc);
                sb.append("#");
                sb.append(this.mnc);
                sb.append("#");
                sb.append(this.lac);
                sb.append("#");
                i = this.cid;
                sb.append(i);
                return sb.toString();
            case 2:
                sb = new StringBuilder();
                sb.append(this.type);
                sb.append("#");
                sb.append(this.sid);
                sb.append("#");
                sb.append(this.nid);
                sb.append("#");
                i = this.bid;
                sb.append(i);
                return sb.toString();
            default:
                return "";
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0020 A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean isValid() {
        /*
            r3 = this;
            int r0 = r3.type
            r1 = 1
            r2 = 0
            switch(r0) {
                case 1: goto L22;
                case 2: goto L8;
                case 3: goto L22;
                case 4: goto L22;
                default: goto L7;
            }
        L7:
            goto L33
        L8:
            int r0 = r3.sid
            boolean r0 = r3.sidValid(r0)
            if (r0 == 0) goto L33
            int r0 = r3.nid
            boolean r0 = r3.nidValid(r0)
            if (r0 == 0) goto L33
            int r0 = r3.bid
            boolean r0 = r3.bidValid(r0)
            if (r0 == 0) goto L33
        L20:
            r2 = 1
            goto L33
        L22:
            int r0 = r3.lac
            boolean r0 = r3.lacValid(r0)
            if (r0 == 0) goto L33
            int r0 = r3.cid
            boolean r0 = r3.cidValid(r0)
            if (r0 == 0) goto L33
            goto L20
        L33:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.location.common.model.CellState.isValid():boolean");
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0010. Please report as an issue. */
    public String toString() {
        Locale locale;
        String str;
        Object[] objArr;
        switch (this.type) {
            case 1:
                locale = Locale.CHINA;
                str = "[type=GSM, mcc=%d, mnc=%d, lac=%d, cid=%d, sig=%d, age=%d, reg=%b, new=%b]";
                objArr = new Object[]{Integer.valueOf(this.mcc), Integer.valueOf(this.mnc), Integer.valueOf(this.lac), Integer.valueOf(this.cid), Integer.valueOf(this.signalStrength), Short.valueOf(this.cellAge), Boolean.valueOf(this.registered), Boolean.valueOf(this.newapi)};
                return String.format(locale, str, objArr);
            case 2:
                locale = Locale.CHINA;
                str = "[type=CDMA, mcc=%d, mnc=%d, sid=%d, nid=%d, bid=%d, sig=%d, age=%d, reg=%b, new=%b]";
                objArr = new Object[]{Integer.valueOf(this.mcc), Integer.valueOf(this.mnc), Integer.valueOf(this.sid), Integer.valueOf(this.nid), Integer.valueOf(this.bid), Integer.valueOf(this.signalStrength), Short.valueOf(this.cellAge), Boolean.valueOf(this.registered), Boolean.valueOf(this.newapi)};
                return String.format(locale, str, objArr);
            case 3:
                locale = Locale.CHINA;
                str = "[type=LTE, mcc=%d, mnc=%d, lac=%d, cid=%d, sig=%d, age=%d, reg=%b, new=%b, pci=%d]";
                objArr = new Object[]{Integer.valueOf(this.mcc), Integer.valueOf(this.mnc), Integer.valueOf(this.lac), Integer.valueOf(this.cid), Integer.valueOf(this.signalStrength), Short.valueOf(this.cellAge), Boolean.valueOf(this.registered), Boolean.valueOf(this.newapi), Short.valueOf(this.pci)};
                return String.format(locale, str, objArr);
            case 4:
                locale = Locale.CHINA;
                str = "[type=WCDMA, mcc=%d, mnc=%d, lac=%d, cid=%d, sig=%d, age=%d, reg=%b, new=%b, psc=%d]";
                objArr = new Object[]{Integer.valueOf(this.mcc), Integer.valueOf(this.mnc), Integer.valueOf(this.lac), Integer.valueOf(this.cid), Integer.valueOf(this.signalStrength), Short.valueOf(this.cellAge), Boolean.valueOf(this.registered), Boolean.valueOf(this.newapi), Short.valueOf(this.pci)};
                return String.format(locale, str, objArr);
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }
}
