package me.panpf.sketch.zoom.block;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.util.KeyCounter;
import me.panpf.sketch.zoom.block.DecodeHandler;

/* loaded from: classes2.dex */
class CallbackHandler extends Handler {
    private static final String NAME = "CallbackHandler";
    private static final int WHAT_DECODE_COMPLETED = 2004;
    private static final int WHAT_DECODE_FAILED = 2005;
    private static final int WHAT_INIT_COMPLETED = 2002;
    private static final int WHAT_INIT_FAILED = 2003;
    private static final int WHAT_RECYCLE_DECODE_THREAD = 2001;
    private BitmapPool bitmapPool;
    private WeakReference<BlockExecutor> executorReference;

    /* loaded from: classes2.dex */
    private static final class DecodeErrorResult {
        public Block block;
        public DecodeHandler.DecodeErrorException exception;

        public DecodeErrorResult(Block block, DecodeHandler.DecodeErrorException decodeErrorException) {
            this.block = block;
            this.exception = decodeErrorException;
        }
    }

    /* loaded from: classes2.dex */
    private static final class DecodeResult {
        public Bitmap bitmap;
        public Block block;
        public int useTime;

        public DecodeResult(Bitmap bitmap, Block block, int i) {
            this.bitmap = bitmap;
            this.block = block;
            this.useTime = i;
        }
    }

    /* loaded from: classes2.dex */
    private static final class InitErrorResult {
        public Exception exception;
        public String imageUrl;
        public KeyCounter keyCounter;

        public InitErrorResult(Exception exc, String str, KeyCounter keyCounter) {
            this.exception = exc;
            this.imageUrl = str;
            this.keyCounter = keyCounter;
        }
    }

    /* loaded from: classes2.dex */
    private static final class InitResult {
        public ImageRegionDecoder imageRegionDecoder;
        public String imageUrl;
        public KeyCounter keyCounter;

        public InitResult(ImageRegionDecoder imageRegionDecoder, String str, KeyCounter keyCounter) {
            this.imageRegionDecoder = imageRegionDecoder;
            this.imageUrl = str;
            this.keyCounter = keyCounter;
        }
    }

    public CallbackHandler(Looper looper, BlockExecutor blockExecutor) {
        super(looper);
        this.executorReference = new WeakReference<>(blockExecutor);
        this.bitmapPool = Sketch.with(blockExecutor.callback.getContext()).getConfiguration().getBitmapPool();
    }

    private void decodeCompleted(int i, Block block, Bitmap bitmap, int i2) {
        BlockExecutor blockExecutor = this.executorReference.get();
        if (blockExecutor == null) {
            SLog.w(NAME, "weak reference break. decodeCompleted. key: %d, block=%s", Integer.valueOf(i), block.getInfo());
            BitmapPoolUtils.freeBitmapToPoolForRegionDecoder(bitmap, this.bitmapPool);
        } else if (!block.isExpired(i)) {
            blockExecutor.callback.onDecodeCompleted(block, bitmap, i2);
        } else {
            BitmapPoolUtils.freeBitmapToPoolForRegionDecoder(bitmap, this.bitmapPool);
            blockExecutor.callback.onDecodeError(block, new DecodeHandler.DecodeErrorException(DecodeHandler.DecodeErrorException.CAUSE_CALLBACK_KEY_EXPIRED));
        }
    }

    private void decodeError(int i, Block block, DecodeHandler.DecodeErrorException decodeErrorException) {
        BlockExecutor blockExecutor = this.executorReference.get();
        if (blockExecutor == null) {
            SLog.w(NAME, "weak reference break. decodeError. key: %d, block=%s", Integer.valueOf(i), block.getInfo());
        } else {
            blockExecutor.callback.onDecodeError(block, decodeErrorException);
        }
    }

    private void initCompleted(ImageRegionDecoder imageRegionDecoder, String str, int i, KeyCounter keyCounter) {
        BlockExecutor blockExecutor = this.executorReference.get();
        if (blockExecutor == null) {
            SLog.w(NAME, "weak reference break. initCompleted. key: %d, imageUri: %s", Integer.valueOf(i), imageRegionDecoder.getImageUri());
            imageRegionDecoder.recycle();
            return;
        }
        int key = keyCounter.getKey();
        if (i == key) {
            blockExecutor.callback.onInitCompleted(str, imageRegionDecoder);
        } else {
            SLog.w(NAME, "init key expired. initCompleted. key: %d. newKey: %d, imageUri: %s", Integer.valueOf(i), Integer.valueOf(key), imageRegionDecoder.getImageUri());
            imageRegionDecoder.recycle();
        }
    }

    private void initError(Exception exc, String str, int i, KeyCounter keyCounter) {
        BlockExecutor blockExecutor = this.executorReference.get();
        if (blockExecutor == null) {
            SLog.w(NAME, "weak reference break. initError. key: %d, imageUri: %s", Integer.valueOf(i), str);
            return;
        }
        int key = keyCounter.getKey();
        if (i != key) {
            SLog.w(NAME, "key expire. initError. key: %d. newKey: %d, imageUri: %s", Integer.valueOf(i), Integer.valueOf(key), str);
        } else {
            blockExecutor.callback.onInitError(str, exc);
        }
    }

    private void recycleDecodeThread() {
        BlockExecutor blockExecutor = this.executorReference.get();
        if (blockExecutor != null) {
            blockExecutor.recycleDecodeThread();
        }
    }

    public void cancelDelayDestroyThread() {
        removeMessages(WHAT_RECYCLE_DECODE_THREAD);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case WHAT_RECYCLE_DECODE_THREAD /* 2001 */:
                recycleDecodeThread();
                return;
            case WHAT_INIT_COMPLETED /* 2002 */:
                InitResult initResult = (InitResult) message.obj;
                initCompleted(initResult.imageRegionDecoder, initResult.imageUrl, message.arg1, initResult.keyCounter);
                return;
            case WHAT_INIT_FAILED /* 2003 */:
                InitErrorResult initErrorResult = (InitErrorResult) message.obj;
                initError(initErrorResult.exception, initErrorResult.imageUrl, message.arg1, initErrorResult.keyCounter);
                return;
            case WHAT_DECODE_COMPLETED /* 2004 */:
                DecodeResult decodeResult = (DecodeResult) message.obj;
                decodeCompleted(message.arg1, decodeResult.block, decodeResult.bitmap, decodeResult.useTime);
                return;
            case WHAT_DECODE_FAILED /* 2005 */:
                DecodeErrorResult decodeErrorResult = (DecodeErrorResult) message.obj;
                decodeError(message.arg1, decodeErrorResult.block, decodeErrorResult.exception);
                return;
            default:
                return;
        }
    }

    public void postDecodeCompleted(int i, Block block, Bitmap bitmap, int i2) {
        Message obtainMessage = obtainMessage(WHAT_DECODE_COMPLETED);
        obtainMessage.arg1 = i;
        obtainMessage.obj = new DecodeResult(bitmap, block, i2);
        obtainMessage.sendToTarget();
    }

    public void postDecodeError(int i, Block block, DecodeHandler.DecodeErrorException decodeErrorException) {
        Message obtainMessage = obtainMessage(WHAT_DECODE_FAILED);
        obtainMessage.arg1 = i;
        obtainMessage.obj = new DecodeErrorResult(block, decodeErrorException);
        obtainMessage.sendToTarget();
    }

    public void postDelayRecycleDecodeThread() {
        cancelDelayDestroyThread();
        sendMessageDelayed(obtainMessage(WHAT_RECYCLE_DECODE_THREAD), 30000L);
    }

    public void postInitCompleted(ImageRegionDecoder imageRegionDecoder, String str, int i, KeyCounter keyCounter) {
        Message obtainMessage = obtainMessage(WHAT_INIT_COMPLETED);
        obtainMessage.arg1 = i;
        obtainMessage.obj = new InitResult(imageRegionDecoder, str, keyCounter);
        obtainMessage.sendToTarget();
    }

    public void postInitError(Exception exc, String str, int i, KeyCounter keyCounter) {
        Message obtainMessage = obtainMessage(WHAT_INIT_FAILED);
        obtainMessage.arg1 = i;
        obtainMessage.obj = new InitErrorResult(exc, str, keyCounter);
        obtainMessage.sendToTarget();
    }
}
