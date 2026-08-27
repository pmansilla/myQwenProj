package cn.droidlover.xrichtext;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import cn.droidlover.xrichtext.XRichText;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class LoaderTask {
    public static final int MSG_POST_RESULT = 100;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = (CPU_COUNT * 2) + 1;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() { // from class: cn.droidlover.xrichtext.LoaderTask.1
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "LoaderTask#" + this.mCount.getAndIncrement());
        }
    };
    private static final long KEEP_ALIVE = 10;
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue(), sThreadFactory);
    private static Handler mainHandler = new Handler(Looper.getMainLooper()) { // from class: cn.droidlover.xrichtext.LoaderTask.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message == null || message.obj == null || !(message.obj instanceof XRichText.ILoad)) {
                return;
            }
            ((XRichText.ILoad) message.obj).afterLoad();
        }
    };

    public static Handler getMainHandler() {
        return mainHandler;
    }

    public static Executor getThreadPoolExecutor() {
        return THREAD_POOL_EXECUTOR;
    }
}
