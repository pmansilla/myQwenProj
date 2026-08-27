package com.liulishuo.filedownloader;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.liulishuo.filedownloader.i.IFileDownloadIPCService;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public class PauseAllMarker implements Handler.Callback {
    private static final String MAKER_FILE_NAME = ".filedownloader_pause_all_marker.b";
    private static final Long PAUSE_ALL_CHECKER_PERIOD = 1000L;
    private static final int PAUSE_ALL_CHECKER_WHAT = 0;
    private static File markerFile;
    private HandlerThread pauseAllChecker;
    private Handler pauseAllHandler;
    private final IFileDownloadIPCService serviceHandler;

    public PauseAllMarker(IFileDownloadIPCService iFileDownloadIPCService) {
        this.serviceHandler = iFileDownloadIPCService;
    }

    public static void clearMarker() {
        File markerFile2 = markerFile();
        if (markerFile2.exists()) {
            FileDownloadLog.d(PauseAllMarker.class, "delete marker file " + markerFile2.delete(), new Object[0]);
        }
    }

    public static void createMarker() {
        File markerFile2 = markerFile();
        if (!markerFile2.getParentFile().exists()) {
            markerFile2.getParentFile().mkdirs();
        }
        if (markerFile2.exists()) {
            FileDownloadLog.w(PauseAllMarker.class, "marker file " + markerFile2.getAbsolutePath() + " exists", new Object[0]);
            return;
        }
        try {
            FileDownloadLog.d(PauseAllMarker.class, "create marker file" + markerFile2.getAbsolutePath() + SQLBuilder.BLANK + markerFile2.createNewFile(), new Object[0]);
        } catch (IOException e) {
            FileDownloadLog.e(PauseAllMarker.class, "create marker file failed", e);
        }
    }

    private static boolean isMarked() {
        return markerFile().exists();
    }

    private static File markerFile() {
        if (markerFile == null) {
            markerFile = new File(FileDownloadHelper.getAppContext().getCacheDir() + File.separator + MAKER_FILE_NAME);
        }
        return markerFile;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        try {
            if (isMarked()) {
                try {
                    this.serviceHandler.pauseAllTasks();
                } catch (RemoteException e) {
                    FileDownloadLog.e(this, e, "pause all failed", new Object[0]);
                }
            }
            this.pauseAllHandler.sendEmptyMessageDelayed(0, PAUSE_ALL_CHECKER_PERIOD.longValue());
            return true;
        } finally {
            clearMarker();
        }
    }

    public void startPauseAllLooperCheck() {
        this.pauseAllChecker = new HandlerThread("PauseAllChecker");
        this.pauseAllChecker.start();
        this.pauseAllHandler = new Handler(this.pauseAllChecker.getLooper(), this);
        this.pauseAllHandler.sendEmptyMessageDelayed(0, PAUSE_ALL_CHECKER_PERIOD.longValue());
    }

    public void stopPauseAllLooperCheck() {
        this.pauseAllHandler.removeMessages(0);
        this.pauseAllChecker.quit();
    }
}
