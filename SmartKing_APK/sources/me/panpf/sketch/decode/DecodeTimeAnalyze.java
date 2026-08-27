package me.panpf.sketch.decode;

import java.text.DecimalFormat;
import kotlin.jvm.internal.LongCompanionObject;
import me.panpf.sketch.SLog;

/* loaded from: classes2.dex */
public class DecodeTimeAnalyze {
    private static DecimalFormat decimalFormat;
    private static volatile long decodeCount;
    private static volatile long useTimeCount;

    public synchronized void decodeEnd(long j, String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        if (LongCompanionObject.MAX_VALUE - decodeCount < 1 || LongCompanionObject.MAX_VALUE - useTimeCount < currentTimeMillis) {
            decodeCount = 0L;
            useTimeCount = 0L;
        }
        decodeCount++;
        useTimeCount += currentTimeMillis;
        if (decimalFormat == null) {
            decimalFormat = new DecimalFormat("#.##");
        }
        Object[] objArr = new Object[3];
        objArr[0] = Long.valueOf(currentTimeMillis);
        DecimalFormat decimalFormat2 = decimalFormat;
        double d = useTimeCount;
        double d2 = decodeCount;
        Double.isNaN(d);
        Double.isNaN(d2);
        objArr[1] = decimalFormat2.format(d / d2);
        objArr[2] = str2;
        SLog.d(str, "decode use time %dms, average %sms. %s", objArr);
    }

    public long decodeStart() {
        return System.currentTimeMillis();
    }
}
