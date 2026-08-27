package cn.smssdk.logger;

import java.util.List;

/* loaded from: classes.dex */
public interface ILoggerEngine {
    void deleteAllLogItems();

    void deleteLogItem(long j);

    List<c> getLogList();

    void insertOneRequestLog(int i, long j, String str);
}
