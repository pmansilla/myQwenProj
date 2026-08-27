package com.amap.api.mapcore.util;

import com.litesuits.orm.db.assit.SQLBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/* compiled from: StatisticsPubDataStrategy.java */
/* loaded from: classes.dex */
public class jp extends jq {
    public jp() {
    }

    public jp(jq jqVar) {
        super(jqVar);
    }

    @Override // com.amap.api.mapcore.util.jq
    protected byte[] a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()));
        stringBuffer.append(SQLBuilder.BLANK);
        stringBuffer.append(UUID.randomUUID().toString());
        stringBuffer.append(SQLBuilder.BLANK);
        if (stringBuffer.length() != 53) {
            return new byte[0];
        }
        byte[] a = hp.a(stringBuffer.toString());
        byte[] bArr2 = new byte[a.length + bArr.length];
        System.arraycopy(a, 0, bArr2, 0, a.length);
        System.arraycopy(bArr, 0, bArr2, a.length, bArr.length);
        return bArr2;
    }
}
