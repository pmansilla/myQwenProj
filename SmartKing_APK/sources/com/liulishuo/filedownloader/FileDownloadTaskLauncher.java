package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.ITaskHunter;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FileDownloadTaskLauncher {
    private final LaunchTaskPool mLaunchTaskPool = new LaunchTaskPool();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class HolderClass {
        private static final FileDownloadTaskLauncher INSTANCE = new FileDownloadTaskLauncher();

        static {
            MessageSnapshotFlow.getImpl().setReceiver(new MessageSnapshotGate());
        }

        private HolderClass() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LaunchTaskPool {
        private ThreadPoolExecutor mPool;
        private LinkedBlockingQueue<Runnable> mWorkQueue;

        LaunchTaskPool() {
            init();
        }

        private void init() {
            this.mWorkQueue = new LinkedBlockingQueue<>();
            this.mPool = FileDownloadExecutors.newDefaultThreadPool(3, this.mWorkQueue, "LauncherTask");
        }

        public void asyncExecute(ITaskHunter.IStarter iStarter) {
            this.mPool.execute(new LaunchTaskRunnable(iStarter));
        }

        public void expire(FileDownloadListener fileDownloadListener) {
            if (fileDownloadListener == null) {
                FileDownloadLog.w(this, "want to expire by listener, but the listener provided is null", new Object[0]);
                return;
            }
            ArrayList arrayList = new ArrayList();
            Iterator<Runnable> it = this.mWorkQueue.iterator();
            while (it.hasNext()) {
                Runnable next = it.next();
                LaunchTaskRunnable launchTaskRunnable = (LaunchTaskRunnable) next;
                if (launchTaskRunnable.isSameListener(fileDownloadListener)) {
                    launchTaskRunnable.expire();
                    arrayList.add(next);
                }
            }
            if (arrayList.isEmpty()) {
                return;
            }
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "expire %d tasks with listener[%s]", Integer.valueOf(arrayList.size()), fileDownloadListener);
            }
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                this.mPool.remove((Runnable) it2.next());
            }
        }

        public void expire(ITaskHunter.IStarter iStarter) {
            this.mWorkQueue.remove(iStarter);
        }

        public void expireAll() {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "expire %d tasks", Integer.valueOf(this.mWorkQueue.size()));
            }
            this.mPool.shutdownNow();
            init();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LaunchTaskRunnable implements Runnable {
        private boolean mExpired = false;
        private final ITaskHunter.IStarter mTaskStarter;

        LaunchTaskRunnable(ITaskHunter.IStarter iStarter) {
            this.mTaskStarter = iStarter;
        }

        public boolean equals(Object obj) {
            return super.equals(obj) || obj == this.mTaskStarter;
        }

        public void expire() {
            this.mExpired = true;
        }

        public boolean isSameListener(FileDownloadListener fileDownloadListener) {
            return this.mTaskStarter != null && this.mTaskStarter.equalListener(fileDownloadListener);
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mExpired) {
                return;
            }
            this.mTaskStarter.start();
        }
    }

    FileDownloadTaskLauncher() {
    }

    public static FileDownloadTaskLauncher getImpl() {
        return HolderClass.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void expire(FileDownloadListener fileDownloadListener) {
        this.mLaunchTaskPool.expire(fileDownloadListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void expire(ITaskHunter.IStarter iStarter) {
        this.mLaunchTaskPool.expire(iStarter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void expireAll() {
        this.mLaunchTaskPool.expireAll();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void launch(ITaskHunter.IStarter iStarter) {
        this.mLaunchTaskPool.asyncExecute(iStarter);
    }
}
