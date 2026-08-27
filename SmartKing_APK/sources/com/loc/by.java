package com.loc;

import com.litesuits.orm.db.assit.SQLBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/* compiled from: StatisticsPubDataStrategy.java */
/* loaded from: classes.dex */
public final class by extends bz {
    public by() {
    }

    public by(bz bzVar) {
        super(bzVar);
    }

    @Override // com.loc.bz
    protected final byte[] a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()));
        stringBuffer.append(SQLBuilder.BLANK);
        stringBuffer.append(UUID.randomUUID().toString());
        stringBuffer.append(SQLBuilder.BLANK);
        if (stringBuffer.length() != 53) {
            return new byte[0];
        }
        byte[] a = ad.a(stringBuffer.toString());
        byte[] bArr2 = new byte[a.length + bArr.length];
        System.arraycopy(a, 0, bArr2, 0, a.length);
        System.arraycopy(bArr, 0, bArr2, a.length, bArr.length);
        return bArr2;
    }
}
