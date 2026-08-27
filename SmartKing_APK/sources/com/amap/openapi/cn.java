package com.amap.openapi;

import android.text.TextUtils;
import com.amap.location.common.model.CellState;
import com.amap.location.common.model.CellStatus;
import com.amap.location.security.Core;

/* compiled from: EncryptUtil.java */
/* loaded from: classes.dex */
public class cn {
    public static long a(long j) {
        try {
            return Core.encMac(com.amap.location.common.util.f.a(j));
        } catch (Throwable unused) {
            return -1L;
        }
    }

    public static long a(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return Core.encMac(str);
            } catch (Throwable unused) {
            }
        }
        return -1L;
    }

    public static String a(CellStatus cellStatus) {
        StringBuilder sb;
        int i;
        if (cellStatus == null || cellStatus.mainCell == null) {
            return "";
        }
        CellState cellState = cellStatus.mainCell;
        if (cellState.type == 2) {
            sb = new StringBuilder();
            sb.append(cellState.sid);
            sb.append(":");
            sb.append(cellState.nid);
            sb.append(":");
            i = cellState.bid;
        } else {
            if (cellState.type == 0 || cellState.mcc == 0 || cellState.mcc == 65535) {
                return "";
            }
            sb = new StringBuilder();
            sb.append(cellState.mcc);
            sb.append(":");
            sb.append(cellState.mnc);
            sb.append(":");
            sb.append(cellState.lac);
            sb.append(":");
            i = cellState.cid;
        }
        sb.append(i);
        return sb.toString();
    }
}
