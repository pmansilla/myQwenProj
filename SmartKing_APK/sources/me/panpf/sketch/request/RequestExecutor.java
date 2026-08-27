package me.panpf.sketch.request;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class RequestExecutor {
    public static final int DEFAULT_LOCAL_THREAD_POOL_SIZE = 3;
    public static final int DEFAULT_NET_THREAD_POOL_SIZE = 3;
    private static final String KEY = "RequestExecutor";
    private Handler dispatchHandler;
    private DispatchThread dispatchThread;
    private ExecutorService localTaskExecutor;
    private int localThreadPoolSize;
    private ExecutorService netTaskExecutor;
    private int netThreadPoolSize;
    private boolean shutdown;

    /* loaded from: classes2.dex */
    private static class DefaultThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadNumber;

        private DefaultThreadFactory(String str) {
            this.threadNumber = new AtomicInteger(1);
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = str;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.group, runnable, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != 5) {
                thread.setPriority(5);
            }
            return thread;
        }
    }

    /* loaded from: classes2.dex */
    private static final class DispatchCallback implements Handler.Callback {
        private DispatchCallback() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            ((Runnable) message.obj).run();
            return true;
        }
    }

    /* loaded from: classes2.dex */
    private static final class DispatchThread extends HandlerThread {
        public DispatchThread(String str) {
            super(str, 10);
        }
    }

    public RequestExecutor() {
        this(3, 3);
    }

    public RequestExecutor(int i, int i2) {
        this.localThreadPoolSize = i;
        this.netThreadPoolSize = i2;
    }

    public boolean isShutdown() {
        return this.shutdown;
    }

    public void setLocalTaskExecutor(ExecutorService executorService) {
        if (this.shutdown) {
            return;
        }
        this.localTaskExecutor = executorService;
    }

    public void setNetTaskExecutor(ExecutorService executorService) {
        if (this.shutdown) {
            return;
        }
        this.netTaskExecutor = executorService;
    }

    public void shutdown() {
        if (this.dispatchHandler != null) {
            this.dispatchHandler = null;
        }
        if (this.dispatchThread != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                this.dispatchThread.quitSafely();
            } else {
                this.dispatchThread.quit();
            }
            this.dispatchThread = null;
        }
        if (this.netTaskExecutor != null) {
            this.netTaskExecutor.shutdown();
            this.netTaskExecutor = null;
        }
        if (this.localTaskExecutor != null) {
            this.localTaskExecutor.shutdown();
            this.localTaskExecutor = null;
        }
        this.shutdown = true;
    }

    public void submitDispatch(Runnable runnable) {
        if (this.shutdown) {
            return;
        }
        if (this.dispatchHandler == null || this.dispatchThread == null) {
            synchronized (this) {
                if (this.dispatchHandler == null) {
                    this.dispatchThread = new DispatchThread("DispatchThread");
                    this.dispatchThread.start();
                    this.dispatchHandler = new Handler(this.dispatchThread.getLooper(), new DispatchCallback());
                }
            }
        }
        this.dispatchHandler.obtainMessage(0, runnable).sendToTarget();
    }

    public void submitDownload(Runnable runnable) {
        if (this.shutdown) {
            return;
        }
        if (this.netTaskExecutor == null) {
            synchronized (this) {
                if (this.netTaskExecutor == null) {
                    this.netTaskExecutor = new ThreadPoolExecutor(this.netThreadPoolSize, this.netThreadPoolSize, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(200), new DefaultThreadFactory("DownloadThread"), new ThreadPoolExecutor.DiscardOldestPolicy());
                }
            }
        }
        this.netTaskExecutor.execute(runnable);
    }

    public void submitLoad(Runnable runnable) {
        if (this.shutdown) {
            return;
        }
        if (this.localTaskExecutor == null) {
            synchronized (this) {
                if (this.localTaskExecutor == null) {
                    this.localTaskExecutor = new ThreadPoolExecutor(this.localThreadPoolSize, this.localThreadPoolSize, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(200), new DefaultThreadFactory("LoadThread"), new ThreadPoolExecutor.DiscardOldestPolicy());
                }
            }
        }
        this.localTaskExecutor.execute(runnable);
    }

    @NonNull
    public String toString() {
        Object[] objArr = new Object[2];
        objArr[0] = KEY;
        objArr[1] = this.shutdown ? "shutdown" : "running)";
        return String.format("%s(%s", objArr);
    }
}
