package me.panpf.sketch.zoom.block;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.concurrent.atomic.AtomicInteger;
import me.panpf.sketch.SLog;
import me.panpf.sketch.util.KeyCounter;
import me.panpf.sketch.zoom.block.DecodeHandler;

/* loaded from: classes2.dex */
public class BlockExecutor {
    private static final String NAME = "BlockExecutor";
    private static final AtomicInteger THREAD_NUMBER = new AtomicInteger();
    Callback callback;
    private DecodeHandler decodeHandler;
    private HandlerThread handlerThread;
    private InitHandler initHandler;
    private final Object handlerThreadLock = new Object();
    CallbackHandler callbackHandler = new CallbackHandler(Looper.getMainLooper(), this);

    /* loaded from: classes2.dex */
    public interface Callback {
        Context getContext();

        void onDecodeCompleted(Block block, Bitmap bitmap, int i);

        void onDecodeError(Block block, DecodeHandler.DecodeErrorException decodeErrorException);

        void onInitCompleted(String str, ImageRegionDecoder imageRegionDecoder);

        void onInitError(String str, Exception exc);
    }

    public BlockExecutor(Callback callback) {
        this.callback = callback;
    }

    private void installHandlerThread() {
        if (this.handlerThread == null) {
            synchronized (this.handlerThreadLock) {
                if (this.handlerThread == null) {
                    if (THREAD_NUMBER.get() >= Integer.MAX_VALUE) {
                        THREAD_NUMBER.set(0);
                    }
                    this.handlerThread = new HandlerThread("ImageRegionDecodeThread" + THREAD_NUMBER.addAndGet(1));
                    this.handlerThread.start();
                    if (SLog.isLoggable(1048578)) {
                        SLog.d(NAME, "image region decode thread %s started", this.handlerThread.getName());
                    }
                    this.decodeHandler = new DecodeHandler(this.handlerThread.getLooper(), this);
                    this.initHandler = new InitHandler(this.handlerThread.getLooper(), this);
                    this.callbackHandler.postDelayRecycleDecodeThread();
                }
            }
        }
    }

    public void cleanDecode(String str) {
        if (this.decodeHandler != null) {
            this.decodeHandler.clean(str);
        }
    }

    public void recycle(String str) {
        if (this.initHandler != null) {
            this.initHandler.clean(str);
        }
        if (this.decodeHandler != null) {
            this.decodeHandler.clean(str);
        }
        recycleDecodeThread();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recycleDecodeThread() {
        if (this.initHandler != null) {
            this.initHandler.clean("recycleDecodeThread");
        }
        if (this.decodeHandler != null) {
            this.decodeHandler.clean("recycleDecodeThread");
        }
        synchronized (this.handlerThreadLock) {
            if (this.handlerThread != null) {
                if (Build.VERSION.SDK_INT >= 18) {
                    this.handlerThread.quitSafely();
                } else {
                    this.handlerThread.quit();
                }
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "image region decode thread %s quit", this.handlerThread.getName());
                }
                this.handlerThread = null;
            }
        }
    }

    public void submitDecodeBlock(int i, Block block) {
        installHandlerThread();
        this.decodeHandler.postDecode(i, block);
    }

    public void submitInit(String str, KeyCounter keyCounter, boolean z) {
        installHandlerThread();
        this.initHandler.postInit(str, z, keyCounter.getKey(), keyCounter);
    }
}
