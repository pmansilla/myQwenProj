package com.liulishuo.filedownloader.download;

import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.liulishuo.filedownloader.services.ForegroundServiceConfig;
import com.liulishuo.filedownloader.stream.FileDownloadOutputStream;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public class CustomComponentHolder {
    private FileDownloadHelper.ConnectionCountAdapter connectionCountAdapter;
    private FileDownloadHelper.ConnectionCreator connectionCreator;
    private FileDownloadDatabase database;
    private ForegroundServiceConfig foregroundServiceConfig;
    private FileDownloadHelper.IdGenerator idGenerator;
    private DownloadMgrInitialParams initialParams;
    private FileDownloadHelper.OutputStreamCreator outputStreamCreator;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class LazyLoader {
        private static final CustomComponentHolder INSTANCE = new CustomComponentHolder();

        private LazyLoader() {
        }
    }

    private FileDownloadHelper.ConnectionCountAdapter getConnectionCountAdapter() {
        if (this.connectionCountAdapter != null) {
            return this.connectionCountAdapter;
        }
        synchronized (this) {
            if (this.connectionCountAdapter == null) {
                this.connectionCountAdapter = getDownloadMgrInitialParams().createConnectionCountAdapter();
            }
        }
        return this.connectionCountAdapter;
    }

    private FileDownloadHelper.ConnectionCreator getConnectionCreator() {
        if (this.connectionCreator != null) {
            return this.connectionCreator;
        }
        synchronized (this) {
            if (this.connectionCreator == null) {
                this.connectionCreator = getDownloadMgrInitialParams().createConnectionCreator();
            }
        }
        return this.connectionCreator;
    }

    private DownloadMgrInitialParams getDownloadMgrInitialParams() {
        if (this.initialParams != null) {
            return this.initialParams;
        }
        synchronized (this) {
            if (this.initialParams == null) {
                this.initialParams = new DownloadMgrInitialParams();
            }
        }
        return this.initialParams;
    }

    public static CustomComponentHolder getImpl() {
        return LazyLoader.INSTANCE;
    }

    private FileDownloadHelper.OutputStreamCreator getOutputStreamCreator() {
        if (this.outputStreamCreator != null) {
            return this.outputStreamCreator;
        }
        synchronized (this) {
            if (this.outputStreamCreator == null) {
                this.outputStreamCreator = getDownloadMgrInitialParams().createOutputStreamCreator();
            }
        }
        return this.outputStreamCreator;
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x00d1, code lost:
    
        if (r5.getSoFar() > 0) goto L44;
     */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00c9 A[Catch: all -> 0x00f9, TryCatch #0 {all -> 0x00f9, blocks: (B:32:0x00ee, B:21:0x00fc, B:23:0x0112, B:25:0x0116, B:26:0x012e, B:27:0x0136, B:56:0x00a1, B:57:0x00c2, B:59:0x00c9, B:62:0x00d7, B:65:0x00e2), top: B:31:0x00ee }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00d5  */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void maintainDatabase(com.liulishuo.filedownloader.database.FileDownloadDatabase.Maintainer r25) {
        /*
            Method dump skipped, instructions count: 445
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.liulishuo.filedownloader.download.CustomComponentHolder.maintainDatabase(com.liulishuo.filedownloader.database.FileDownloadDatabase$Maintainer):void");
    }

    public FileDownloadConnection createConnection(String str) throws IOException {
        return getConnectionCreator().create(str);
    }

    public FileDownloadOutputStream createOutputStream(File file) throws IOException {
        return getOutputStreamCreator().create(file);
    }

    public int determineConnectionCount(int i, String str, String str2, long j) {
        return getConnectionCountAdapter().determineConnectionCount(i, str, str2, j);
    }

    public FileDownloadDatabase getDatabaseInstance() {
        if (this.database != null) {
            return this.database;
        }
        synchronized (this) {
            if (this.database == null) {
                this.database = getDownloadMgrInitialParams().createDatabase();
                maintainDatabase(this.database.maintainer());
            }
        }
        return this.database;
    }

    public ForegroundServiceConfig getForegroundConfigInstance() {
        if (this.foregroundServiceConfig != null) {
            return this.foregroundServiceConfig;
        }
        synchronized (this) {
            if (this.foregroundServiceConfig == null) {
                this.foregroundServiceConfig = getDownloadMgrInitialParams().createForegroundServiceConfig();
            }
        }
        return this.foregroundServiceConfig;
    }

    public FileDownloadHelper.IdGenerator getIdGeneratorInstance() {
        if (this.idGenerator != null) {
            return this.idGenerator;
        }
        synchronized (this) {
            if (this.idGenerator == null) {
                this.idGenerator = getDownloadMgrInitialParams().createIdGenerator();
            }
        }
        return this.idGenerator;
    }

    public int getMaxNetworkThreadCount() {
        return getDownloadMgrInitialParams().getMaxNetworkThreadCount();
    }

    public boolean isSupportSeek() {
        return getOutputStreamCreator().supportSeek();
    }

    public void setInitCustomMaker(DownloadMgrInitialParams.InitCustomMaker initCustomMaker) {
        synchronized (this) {
            this.initialParams = new DownloadMgrInitialParams(initCustomMaker);
            this.connectionCreator = null;
            this.outputStreamCreator = null;
            this.database = null;
            this.idGenerator = null;
        }
    }
}
