package me.panpf.sketch.zoom.block;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.os.EnvironmentCompat;
import java.lang.ref.WeakReference;
import me.panpf.sketch.Configuration;
import me.panpf.sketch.ErrorTracker;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.decode.ImageDecodeUtils;
import me.panpf.sketch.decode.ImageOrientationCorrector;
import me.panpf.sketch.decode.ImageType;

/* loaded from: classes2.dex */
public class DecodeHandler extends Handler {
    private static final String NAME = "DecodeHandler";
    private static final int WHAT_DECODE = 1001;
    private BitmapPool bitmapPool;
    private boolean disableInBitmap;
    private ErrorTracker errorTracker;
    private ImageOrientationCorrector orientationCorrector;
    private WeakReference<BlockExecutor> reference;

    /* loaded from: classes2.dex */
    public static class DecodeErrorException extends Exception {
        public static final int CAUSE_AFTER_KEY_EXPIRED = 1103;
        public static final int CAUSE_BEFORE_KEY_EXPIRED = 1102;
        public static final int CAUSE_BITMAP_NULL = 1101;
        public static final int CAUSE_BITMAP_RECYCLED = 1100;
        public static final int CAUSE_CALLBACK_KEY_EXPIRED = 1104;
        public static final int CAUSE_DECODER_NULL_OR_NOT_READY = 1106;
        public static final int CAUSE_DECODE_PARAM_EMPTY = 1105;
        public static final int CAUSE_ROTATE_BITMAP_RECYCLED = 1107;
        private int cause;

        public DecodeErrorException(int i) {
            this.cause = i;
        }

        public String getCauseMessage() {
            return this.cause == 1100 ? "bitmap is recycled" : this.cause == 1101 ? "bitmap is null or recycled" : this.cause == 1102 ? "key expired before decode" : this.cause == 1103 ? "key expired after decode" : this.cause == 1104 ? "key expired before callback" : this.cause == 1105 ? "decode param is empty" : this.cause == 1106 ? "decoder is null or not ready" : this.cause == 1107 ? "rotate result bitmap is recycled" : EnvironmentCompat.MEDIA_UNKNOWN;
        }

        public int getErrorCause() {
            return this.cause;
        }
    }

    public DecodeHandler(Looper looper, BlockExecutor blockExecutor) {
        super(looper);
        this.reference = new WeakReference<>(blockExecutor);
        Configuration configuration = Sketch.with(blockExecutor.callback.getContext()).getConfiguration();
        this.bitmapPool = configuration.getBitmapPool();
        this.errorTracker = configuration.getErrorTracker();
        this.orientationCorrector = configuration.getOrientationCorrector();
    }

    private void decode(BlockExecutor blockExecutor, int i, Block block) {
        Bitmap bitmap;
        if (blockExecutor == null) {
            SLog.w(NAME, "weak reference break. key: %d, block=%s", Integer.valueOf(i), block.getInfo());
            return;
        }
        if (block.isExpired(i)) {
            blockExecutor.callbackHandler.postDecodeError(i, block, new DecodeErrorException(DecodeErrorException.CAUSE_BEFORE_KEY_EXPIRED));
            return;
        }
        if (block.isDecodeParamEmpty()) {
            blockExecutor.callbackHandler.postDecodeError(i, block, new DecodeErrorException(DecodeErrorException.CAUSE_DECODE_PARAM_EMPTY));
            return;
        }
        ImageRegionDecoder imageRegionDecoder = block.decoder;
        if (imageRegionDecoder == null || !imageRegionDecoder.isReady()) {
            blockExecutor.callbackHandler.postDecodeError(i, block, new DecodeErrorException(DecodeErrorException.CAUSE_DECODER_NULL_OR_NOT_READY));
            return;
        }
        Rect rect = new Rect(block.srcRect);
        int i2 = block.inSampleSize;
        Point imageSize = imageRegionDecoder.getImageSize();
        this.orientationCorrector.reverseRotate(rect, imageSize.x, imageSize.y, imageRegionDecoder.getExifOrientation());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = i2;
        ImageType imageType = imageRegionDecoder.getImageType();
        if (imageType != null) {
            options.inPreferredConfig = imageType.getConfig(false);
        }
        if (!this.disableInBitmap && BitmapPoolUtils.sdkSupportInBitmapForRegionDecoder()) {
            BitmapPoolUtils.setInBitmapFromPoolForRegionDecoder(options, rect, this.bitmapPool);
        }
        long currentTimeMillis = System.currentTimeMillis();
        try {
            bitmap = imageRegionDecoder.decodeRegion(rect, options);
        } catch (Throwable th) {
            th.printStackTrace();
            if (ImageDecodeUtils.isInBitmapDecodeError(th, options, true)) {
                this.disableInBitmap = true;
                ImageDecodeUtils.recycleInBitmapOnDecodeError(this.errorTracker, this.bitmapPool, imageRegionDecoder.getImageUri(), imageRegionDecoder.getImageSize().x, imageRegionDecoder.getImageSize().y, imageRegionDecoder.getImageType().getMimeType(), th, options, true);
                try {
                    bitmap = imageRegionDecoder.decodeRegion(rect, options);
                } catch (Throwable th2) {
                    th2.printStackTrace();
                    bitmap = null;
                    int currentTimeMillis2 = (int) (System.currentTimeMillis() - currentTimeMillis);
                    if (bitmap != null) {
                    }
                    blockExecutor.callbackHandler.postDecodeError(i, block, new DecodeErrorException(DecodeErrorException.CAUSE_BITMAP_NULL));
                    return;
                }
            } else if (ImageDecodeUtils.isSrcRectDecodeError(th, imageRegionDecoder.getImageSize().x, imageRegionDecoder.getImageSize().y, rect)) {
                this.errorTracker.onDecodeRegionError(imageRegionDecoder.getImageUri(), imageRegionDecoder.getImageSize().x, imageRegionDecoder.getImageSize().y, imageRegionDecoder.getImageType().getMimeType(), th, rect, options.inSampleSize);
            }
            bitmap = null;
        }
        int currentTimeMillis22 = (int) (System.currentTimeMillis() - currentTimeMillis);
        if (bitmap != null || bitmap.isRecycled()) {
            blockExecutor.callbackHandler.postDecodeError(i, block, new DecodeErrorException(DecodeErrorException.CAUSE_BITMAP_NULL));
            return;
        }
        if (block.isExpired(i)) {
            BitmapPoolUtils.freeBitmapToPoolForRegionDecoder(bitmap, Sketch.with(blockExecutor.callback.getContext()).getConfiguration().getBitmapPool());
            blockExecutor.callbackHandler.postDecodeError(i, block, new DecodeErrorException(DecodeErrorException.CAUSE_AFTER_KEY_EXPIRED));
            return;
        }
        Bitmap rotate = this.orientationCorrector.rotate(bitmap, imageRegionDecoder.getExifOrientation(), this.bitmapPool);
        if (rotate != null && rotate != bitmap) {
            if (rotate.isRecycled()) {
                blockExecutor.callbackHandler.postDecodeError(i, block, new DecodeErrorException(DecodeErrorException.CAUSE_ROTATE_BITMAP_RECYCLED));
                return;
            } else {
                BitmapPoolUtils.freeBitmapToPool(bitmap, this.bitmapPool);
                bitmap = rotate;
            }
        }
        if (bitmap.isRecycled()) {
            blockExecutor.callbackHandler.postDecodeError(i, block, new DecodeErrorException(1100));
        } else {
            blockExecutor.callbackHandler.postDecodeCompleted(i, block, bitmap, currentTimeMillis22);
        }
    }

    public void clean(String str) {
        if (SLog.isLoggable(1048578)) {
            SLog.d(NAME, "clean. %s", str);
        }
        removeMessages(1001);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        BlockExecutor blockExecutor = this.reference.get();
        if (blockExecutor != null) {
            blockExecutor.callbackHandler.cancelDelayDestroyThread();
        }
        if (message.what == 1001) {
            decode(blockExecutor, message.arg1, (Block) message.obj);
        }
        if (blockExecutor != null) {
            blockExecutor.callbackHandler.postDelayRecycleDecodeThread();
        }
    }

    public void postDecode(int i, Block block) {
        Message obtainMessage = obtainMessage(1001);
        obtainMessage.arg1 = i;
        obtainMessage.obj = block;
        obtainMessage.sendToTarget();
    }
}
