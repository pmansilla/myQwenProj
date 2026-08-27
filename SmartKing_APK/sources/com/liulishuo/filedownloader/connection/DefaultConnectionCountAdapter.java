package com.liulishuo.filedownloader.connection;

import com.liulishuo.filedownloader.util.FileDownloadHelper;

/* loaded from: classes.dex */
public class DefaultConnectionCountAdapter implements FileDownloadHelper.ConnectionCountAdapter {
    private static final long FOUR_CONNECTION_UPPER_LIMIT = 104857600;
    private static final long ONE_CONNECTION_UPPER_LIMIT = 1048576;
    private static final long THREE_CONNECTION_UPPER_LIMIT = 52428800;
    private static final long TWO_CONNECTION_UPPER_LIMIT = 5242880;

    @Override // com.liulishuo.filedownloader.util.FileDownloadHelper.ConnectionCountAdapter
    public int determineConnectionCount(int i, String str, String str2, long j) {
        if (j < 1048576) {
            return 1;
        }
        if (j < TWO_CONNECTION_UPPER_LIMIT) {
            return 2;
        }
        if (j < THREE_CONNECTION_UPPER_LIMIT) {
            return 3;
        }
        return j < FOUR_CONNECTION_UPPER_LIMIT ? 4 : 5;
    }
}
