package com.mob.tools.utils;

import android.os.SystemClock;
import com.mob.tools.MobLog;
import java.io.FileOutputStream;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

/* loaded from: classes2.dex */
public class FileLocker {
    private FileOutputStream fos;
    private FileLock lock;

    private boolean getLock(boolean z) throws Throwable {
        if (z) {
            this.lock = this.fos.getChannel().lock();
        } else {
            this.lock = this.fos.getChannel().tryLock();
        }
        return this.lock != null;
    }

    public synchronized void lock(Runnable runnable, boolean z) {
        if (lock(z) && runnable != null) {
            runnable.run();
        }
    }

    public synchronized boolean lock(boolean z) {
        return lock(z, z ? 1000L : 500L, 16L);
    }

    public synchronized boolean lock(boolean z, long j, long j2) {
        boolean z2;
        if (this.fos == null) {
            return false;
        }
        try {
            return getLock(z);
        } catch (Throwable th) {
            if (j <= 0 || !(th instanceof OverlappingFileLockException)) {
                MobLog.getInstance().w(th);
            } else {
                long elapsedRealtime = SystemClock.elapsedRealtime() + j;
                while (true) {
                    if (j <= 0) {
                        z2 = false;
                        break;
                    }
                    try {
                        Thread.sleep(j2);
                    } catch (Throwable unused) {
                    }
                    try {
                        j = elapsedRealtime - SystemClock.elapsedRealtime();
                        z2 = getLock(z);
                        break;
                    } catch (Throwable th2) {
                        if (!(th2 instanceof OverlappingFileLockException)) {
                            MobLog.getInstance().w(th);
                            j = -1;
                        } else if (j <= 0) {
                            MobLog.getInstance().w("OverlappingFileLockException and timeout");
                        }
                    }
                }
                if (j > 0) {
                    return z2;
                }
            }
            if (this.lock != null) {
                try {
                    this.lock.release();
                } catch (Throwable unused2) {
                }
                this.lock = null;
            }
            if (this.fos != null) {
                try {
                    this.fos.close();
                } catch (Throwable unused3) {
                }
                this.fos = null;
            }
            return false;
        }
    }

    public synchronized void release() {
        if (this.fos == null) {
            return;
        }
        unlock();
        try {
            this.fos.close();
        } catch (Throwable unused) {
        }
        this.fos = null;
    }

    public synchronized void setLockFile(String str) {
        try {
            this.fos = new FileOutputStream(str);
        } catch (Throwable unused) {
            if (this.fos != null) {
                try {
                    this.fos.close();
                } catch (Throwable unused2) {
                }
                this.fos = null;
            }
        }
    }

    public synchronized void unlock() {
        if (this.lock == null) {
            return;
        }
        try {
            this.lock.release();
        } catch (Throwable unused) {
        }
        this.lock = null;
    }
}
