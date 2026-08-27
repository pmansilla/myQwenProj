package me.panpf.sketch.util;

import java.text.DecimalFormat;
import kotlin.jvm.internal.LongCompanionObject;
import me.panpf.sketch.SLog;

/* loaded from: classes2.dex */
public class Stopwatch {
    private static Stopwatch instance;
    private StringBuilder builder;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private long decodeCount;
    private long lastTime;
    private String logName;
    private long startTime;
    private long useTimeCount;

    public static Stopwatch with() {
        if (instance == null) {
            synchronized (Stopwatch.class) {
                if (instance == null) {
                    instance = new Stopwatch();
                }
            }
        }
        return instance;
    }

    public void print(String str) {
        if (this.builder != null) {
            long currentTimeMillis = System.currentTimeMillis() - this.startTime;
            if (this.builder.length() > 0) {
                this.builder.append(". ");
            }
            StringBuilder sb = this.builder;
            sb.append("useTime=");
            sb.append(currentTimeMillis);
            sb.append("ms");
            if (LongCompanionObject.MAX_VALUE - this.decodeCount < 1 || LongCompanionObject.MAX_VALUE - this.useTimeCount < currentTimeMillis) {
                this.decodeCount = 0L;
                this.useTimeCount = 0L;
            }
            this.decodeCount++;
            this.useTimeCount += currentTimeMillis;
            if (SLog.isLoggable(262146)) {
                String str2 = this.logName;
                DecimalFormat decimalFormat = this.decimalFormat;
                double d = this.useTimeCount;
                double d2 = this.decodeCount;
                Double.isNaN(d);
                Double.isNaN(d2);
                SLog.d(str2, "%s, average=%sms. %s", this.builder.toString(), decimalFormat.format(d / d2), str);
            }
            this.builder = null;
        }
    }

    public void record(String str) {
        if (this.builder != null) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.lastTime;
            this.lastTime = currentTimeMillis;
            if (this.builder.length() > 0) {
                this.builder.append(", ");
            }
            StringBuilder sb = this.builder;
            sb.append(str);
            sb.append(":");
            sb.append(j);
            sb.append("ms");
        }
    }

    public void start(String str) {
        this.logName = str;
        this.startTime = System.currentTimeMillis();
        this.lastTime = this.startTime;
        this.builder = new StringBuilder();
    }
}
