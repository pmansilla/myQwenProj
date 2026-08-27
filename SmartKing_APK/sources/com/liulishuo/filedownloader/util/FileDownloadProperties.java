package com.liulishuo.filedownloader.util;

/* loaded from: classes.dex */
public class FileDownloadProperties {
    private static final String FALSE_STRING = "false";
    private static final String KEY_BROADCAST_COMPLETED = "broadcast.completed";
    private static final String KEY_DOWNLOAD_MAX_NETWORK_THREAD_COUNT = "download.max-network-thread-count";
    private static final String KEY_DOWNLOAD_MIN_PROGRESS_STEP = "download.min-progress-step";
    private static final String KEY_DOWNLOAD_MIN_PROGRESS_TIME = "download.min-progress-time";
    private static final String KEY_FILE_NON_PRE_ALLOCATION = "file.non-pre-allocation";
    private static final String KEY_HTTP_LENIENT = "http.lenient";
    private static final String KEY_PROCESS_NON_SEPARATE = "process.non-separate";
    private static final String KEY_TRIAL_CONNECTION_HEAD_METHOD = "download.trial-connection-head-method";
    private static final String TRUE_STRING = "true";
    public final boolean broadcastCompleted;
    public final int downloadMaxNetworkThreadCount;
    public final int downloadMinProgressStep;
    public final long downloadMinProgressTime;
    public final boolean fileNonPreAllocation;
    public final boolean httpLenient;
    public final boolean processNonSeparate;
    public final boolean trialConnectionHeadMethod;

    /* loaded from: classes.dex */
    public static class HolderClass {
        private static final FileDownloadProperties INSTANCE = new FileDownloadProperties();
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x0098 A[Catch: all -> 0x02ae, TryCatch #5 {all -> 0x02ae, blocks: (B:76:0x0026, B:79:0x002f, B:82:0x0035, B:85:0x003b, B:88:0x0041, B:91:0x0047, B:94:0x004d, B:97:0x0053, B:101:0x0094, B:103:0x0098, B:105:0x009c, B:113:0x00a6), top: B:75:0x0026 }] */
    /* JADX WARN: Removed duplicated region for block: B:108:0x00ab A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x00a6 A[Catch: all -> 0x02ae, TRY_LEAVE, TryCatch #5 {all -> 0x02ae, blocks: (B:76:0x0026, B:79:0x002f, B:82:0x0035, B:85:0x003b, B:88:0x0041, B:91:0x0047, B:94:0x004d, B:97:0x0053, B:101:0x0094, B:103:0x0098, B:105:0x009c, B:113:0x00a6), top: B:75:0x0026 }] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x012e  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x01aa  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:61:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01a6  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x016d  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x013d  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00f2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private FileDownloadProperties() {
        /*
            Method dump skipped, instructions count: 708
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.liulishuo.filedownloader.util.FileDownloadProperties.<init>():void");
    }

    public static FileDownloadProperties getImpl() {
        return HolderClass.INSTANCE;
    }

    public static int getValidNetworkThreadCount(int i) {
        if (i > 12) {
            FileDownloadLog.w(FileDownloadProperties.class, "require the count of network thread  is %d, what is more than the max valid count(%d), so adjust to %d auto", Integer.valueOf(i), 12, 12);
            return 12;
        }
        if (i >= 1) {
            return i;
        }
        FileDownloadLog.w(FileDownloadProperties.class, "require the count of network thread  is %d, what is less than the min valid count(%d), so adjust to %d auto", Integer.valueOf(i), 1, 1);
        return 1;
    }
}
