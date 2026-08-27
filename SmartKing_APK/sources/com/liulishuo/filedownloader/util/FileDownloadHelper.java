package com.liulishuo.filedownloader.util;

import android.annotation.SuppressLint;
import android.content.Context;
import com.liulishuo.filedownloader.IThreadPoolMonitor;
import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.exception.PathConflictException;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.message.MessageSnapshotTaker;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.stream.FileDownloadOutputStream;
import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public class FileDownloadHelper {

    @SuppressLint({"StaticFieldLeak"})
    private static Context APP_CONTEXT;

    /* loaded from: classes.dex */
    public interface ConnectionCountAdapter {
        int determineConnectionCount(int i, String str, String str2, long j);
    }

    /* loaded from: classes.dex */
    public interface ConnectionCreator {
        FileDownloadConnection create(String str) throws IOException;
    }

    /* loaded from: classes.dex */
    public interface DatabaseCustomMaker {
        FileDownloadDatabase customMake();
    }

    /* loaded from: classes.dex */
    public interface IdGenerator {
        int generateId(String str, String str2, boolean z);

        int transOldId(int i, String str, String str2, boolean z);
    }

    /* loaded from: classes.dex */
    public interface OutputStreamCreator {
        FileDownloadOutputStream create(File file) throws IOException;

        boolean supportSeek();
    }

    public static Context getAppContext() {
        return APP_CONTEXT;
    }

    public static void holdContext(Context context) {
        APP_CONTEXT = context;
    }

    public static boolean inspectAndInflowConflictPath(int i, long j, String str, String str2, IThreadPoolMonitor iThreadPoolMonitor) {
        int findRunningTaskIdBySameTempPath;
        if (str2 == null || str == null || (findRunningTaskIdBySameTempPath = iThreadPoolMonitor.findRunningTaskIdBySameTempPath(str, i)) == 0) {
            return false;
        }
        MessageSnapshotFlow.getImpl().inflow(MessageSnapshotTaker.catchException(i, j, new PathConflictException(findRunningTaskIdBySameTempPath, str, str2)));
        return true;
    }

    public static boolean inspectAndInflowDownloaded(int i, String str, boolean z, boolean z2) {
        if (!z && str != null) {
            File file = new File(str);
            if (file.exists()) {
                MessageSnapshotFlow.getImpl().inflow(MessageSnapshotTaker.catchCanReusedOldFile(i, file, z2));
                return true;
            }
        }
        return false;
    }

    public static boolean inspectAndInflowDownloading(int i, FileDownloadModel fileDownloadModel, IThreadPoolMonitor iThreadPoolMonitor, boolean z) {
        if (!iThreadPoolMonitor.isDownloading(fileDownloadModel)) {
            return false;
        }
        MessageSnapshotFlow.getImpl().inflow(MessageSnapshotTaker.catchWarn(i, fileDownloadModel.getSoFar(), fileDownloadModel.getTotal(), z));
        return true;
    }
}
