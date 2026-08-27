package com.liulishuo.filedownloader;

import android.os.SystemClock;
import com.liulishuo.filedownloader.IDownloadSpeed;

/* loaded from: classes.dex */
public class DownloadSpeedMonitor implements IDownloadSpeed.Monitor, IDownloadSpeed.Lookup {
    private long mLastRefreshSofarBytes;
    private long mLastRefreshTime;
    private int mMinIntervalUpdateSpeed = 1000;
    private int mSpeed;
    private long mStartSofarBytes;
    private long mStartTime;
    private long mTotalBytes;

    @Override // com.liulishuo.filedownloader.IDownloadSpeed.Monitor
    public void end(long j) {
        if (this.mStartTime <= 0) {
            return;
        }
        long j2 = j - this.mStartSofarBytes;
        this.mLastRefreshTime = 0L;
        long uptimeMillis = SystemClock.uptimeMillis() - this.mStartTime;
        if (uptimeMillis <= 0) {
            this.mSpeed = (int) j2;
        } else {
            this.mSpeed = (int) (j2 / uptimeMillis);
        }
    }

    @Override // com.liulishuo.filedownloader.IDownloadSpeed.Lookup
    public int getSpeed() {
        return this.mSpeed;
    }

    @Override // com.liulishuo.filedownloader.IDownloadSpeed.Monitor
    public void reset() {
        this.mSpeed = 0;
        this.mLastRefreshTime = 0L;
    }

    @Override // com.liulishuo.filedownloader.IDownloadSpeed.Lookup
    public void setMinIntervalUpdateSpeed(int i) {
        this.mMinIntervalUpdateSpeed = i;
    }

    @Override // com.liulishuo.filedownloader.IDownloadSpeed.Monitor
    public void start(long j) {
        this.mStartTime = SystemClock.uptimeMillis();
        this.mStartSofarBytes = j;
    }

    @Override // com.liulishuo.filedownloader.IDownloadSpeed.Monitor
    public void update(long j) {
        if (this.mMinIntervalUpdateSpeed <= 0) {
            return;
        }
        boolean z = true;
        if (this.mLastRefreshTime != 0) {
            long uptimeMillis = SystemClock.uptimeMillis() - this.mLastRefreshTime;
            if (uptimeMillis >= this.mMinIntervalUpdateSpeed || (this.mSpeed == 0 && uptimeMillis > 0)) {
                this.mSpeed = (int) ((j - this.mLastRefreshSofarBytes) / uptimeMillis);
                this.mSpeed = Math.max(0, this.mSpeed);
            } else {
                z = false;
            }
        }
        if (z) {
            this.mLastRefreshSofarBytes = j;
            this.mLastRefreshTime = SystemClock.uptimeMillis();
        }
    }
}
