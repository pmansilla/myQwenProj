package me.panpf.sketch.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import java.util.List;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.decode.ImageType;
import me.panpf.sketch.drawable.SketchDrawable;
import me.panpf.sketch.drawable.SketchLoadingDrawable;
import me.panpf.sketch.util.SketchUtils;
import me.panpf.sketch.viewfun.FunctionPropertyView;
import me.panpf.sketch.zoom.block.Block;
import me.panpf.sketch.zoom.block.BlockDecoder;
import me.panpf.sketch.zoom.block.BlockExecutor;
import me.panpf.sketch.zoom.block.BlockManager;
import me.panpf.sketch.zoom.block.DecodeHandler;
import me.panpf.sketch.zoom.block.ImageRegionDecoder;

/* loaded from: classes2.dex */
public class BlockDisplayer {
    private static final String NAME = "BlockDisplayer";
    private BlockDecoder blockDecoder;
    private BlockExecutor blockExecutor;
    private BlockManager blockManager;
    private Context context;
    private Paint drawBlockPaint;
    private Paint drawBlockRectPaint;
    private Paint drawLoadingBlockRectPaint;
    private String imageUri;
    private ImageZoomer imageZoomer;
    private float lastZoomScale;
    private Matrix matrix;
    private OnBlockChangedListener onBlockChangedListener;
    private boolean paused;
    private boolean running;
    private boolean showBlockBounds;
    private Matrix tempDrawMatrix;
    private Rect tempVisibleRect;
    private float zoomScale;

    /* loaded from: classes2.dex */
    private class ExecutorCallback implements BlockExecutor.Callback {
        private ExecutorCallback() {
        }

        @Override // me.panpf.sketch.zoom.block.BlockExecutor.Callback
        public Context getContext() {
            return BlockDisplayer.this.context;
        }

        @Override // me.panpf.sketch.zoom.block.BlockExecutor.Callback
        public void onDecodeCompleted(Block block, Bitmap bitmap, int i) {
            if (BlockDisplayer.this.running) {
                BlockDisplayer.this.blockManager.decodeCompleted(block, bitmap, i);
            } else {
                SLog.w(BlockDisplayer.NAME, "stop running. decodeCompleted. block=%s", block.getInfo());
                BitmapPoolUtils.freeBitmapToPoolForRegionDecoder(bitmap, Sketch.with(BlockDisplayer.this.context).getConfiguration().getBitmapPool());
            }
        }

        @Override // me.panpf.sketch.zoom.block.BlockExecutor.Callback
        public void onDecodeError(Block block, DecodeHandler.DecodeErrorException decodeErrorException) {
            if (BlockDisplayer.this.running) {
                BlockDisplayer.this.blockManager.decodeError(block, decodeErrorException);
            } else {
                SLog.w(BlockDisplayer.NAME, "stop running. decodeError. block=%s", block.getInfo());
            }
        }

        @Override // me.panpf.sketch.zoom.block.BlockExecutor.Callback
        public void onInitCompleted(String str, ImageRegionDecoder imageRegionDecoder) {
            if (!BlockDisplayer.this.running) {
                SLog.w(BlockDisplayer.NAME, "stop running. initCompleted. %s", str);
            } else {
                BlockDisplayer.this.blockDecoder.initCompleted(str, imageRegionDecoder);
                BlockDisplayer.this.onMatrixChanged();
            }
        }

        @Override // me.panpf.sketch.zoom.block.BlockExecutor.Callback
        public void onInitError(String str, Exception exc) {
            if (BlockDisplayer.this.running) {
                BlockDisplayer.this.blockDecoder.initError(str, exc);
            } else {
                SLog.w(BlockDisplayer.NAME, "stop running. initError. %s", str);
            }
        }
    }

    /* loaded from: classes2.dex */
    public interface OnBlockChangedListener {
        void onBlockChanged(BlockDisplayer blockDisplayer);
    }

    public BlockDisplayer(Context context, ImageZoomer imageZoomer) {
        Context applicationContext = context.getApplicationContext();
        this.context = applicationContext;
        this.imageZoomer = imageZoomer;
        this.blockExecutor = new BlockExecutor(new ExecutorCallback());
        this.blockManager = new BlockManager(applicationContext, this);
        this.blockDecoder = new BlockDecoder(this);
        this.matrix = new Matrix();
        this.drawBlockPaint = new Paint();
        if (SketchUtils.sdkSupportBitmapRegionDecoder()) {
            return;
        }
        SLog.e(NAME, "BlockDisplayer minimum support to GINGERBREAD_MR1");
    }

    private void clean(String str) {
        if (SketchUtils.sdkSupportBitmapRegionDecoder()) {
            this.blockExecutor.cleanDecode(str);
            this.matrix.reset();
            this.lastZoomScale = 0.0f;
            this.zoomScale = 0.0f;
            this.blockManager.clean(str);
            invalidateView();
        }
    }

    public long getAllocationByteCount() {
        return this.blockManager.getAllocationByteCount();
    }

    public int getBlockBaseNumber() {
        return this.blockManager.blockBaseNumber;
    }

    public BlockDecoder getBlockDecoder() {
        return this.blockDecoder;
    }

    public BlockExecutor getBlockExecutor() {
        return this.blockExecutor;
    }

    public List<Block> getBlockList() {
        return this.blockManager.blockList;
    }

    public int getBlockSize() {
        return this.blockManager.blockList.size();
    }

    public Rect getDecodeRect() {
        return this.blockManager.decodeRect;
    }

    public Rect getDecodeSrcRect() {
        return this.blockManager.decodeSrcRect;
    }

    public Rect getDrawRect() {
        return this.blockManager.drawRect;
    }

    public Rect getDrawSrcRect() {
        return this.blockManager.drawSrcRect;
    }

    public Point getImageSize() {
        if (this.blockDecoder.isReady()) {
            return this.blockDecoder.getDecoder().getImageSize();
        }
        return null;
    }

    public ImageType getImageType() {
        if (this.blockDecoder.isReady()) {
            return this.blockDecoder.getDecoder().getImageType();
        }
        return null;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public float getLastZoomScale() {
        return this.lastZoomScale;
    }

    public OnBlockChangedListener getOnBlockChangedListener() {
        return this.onBlockChangedListener;
    }

    public float getZoomScale() {
        return this.zoomScale;
    }

    public void invalidateView() {
        this.imageZoomer.getImageView().invalidate();
    }

    public boolean isInitializing() {
        return this.running && this.blockDecoder.isInitializing();
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isReady() {
        return this.running && this.blockDecoder.isReady();
    }

    public boolean isShowBlockBounds() {
        return this.showBlockBounds;
    }

    public boolean isWorking() {
        return !TextUtils.isEmpty(this.imageUri);
    }

    public void onDraw(Canvas canvas) {
        if (!SketchUtils.sdkSupportBitmapRegionDecoder() || !isReady() || this.blockManager.blockList == null || this.blockManager.blockList.size() <= 0) {
            return;
        }
        int save = canvas.save();
        canvas.concat(this.matrix);
        for (Block block : this.blockManager.blockList) {
            if (!block.isEmpty()) {
                canvas.drawBitmap(block.bitmap, block.bitmapDrawSrcRect, block.drawRect, this.drawBlockPaint);
                if (this.showBlockBounds) {
                    if (this.drawBlockRectPaint == null) {
                        this.drawBlockRectPaint = new Paint();
                        this.drawBlockRectPaint.setColor(Color.parseColor("#88FF0000"));
                    }
                    canvas.drawRect(block.drawRect, this.drawBlockRectPaint);
                }
            } else if (!block.isDecodeParamEmpty() && this.showBlockBounds) {
                if (this.drawLoadingBlockRectPaint == null) {
                    this.drawLoadingBlockRectPaint = new Paint();
                    this.drawLoadingBlockRectPaint.setColor(Color.parseColor("#880000FF"));
                }
                canvas.drawRect(block.drawRect, this.drawLoadingBlockRectPaint);
            }
        }
        canvas.restoreToCount(save);
    }

    public void onMatrixChanged() {
        if (SketchUtils.sdkSupportBitmapRegionDecoder()) {
            if (!isReady() && !isInitializing()) {
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "BlockDisplayer not available. onMatrixChanged. %s", this.imageUri);
                    return;
                }
                return;
            }
            if (this.imageZoomer.getRotateDegrees() % 90 != 0) {
                SLog.w(NAME, "rotate degrees must be in multiples of 90. %s", this.imageUri);
                return;
            }
            if (this.tempDrawMatrix == null) {
                this.tempDrawMatrix = new Matrix();
                this.tempVisibleRect = new Rect();
            }
            this.tempDrawMatrix.reset();
            this.tempVisibleRect.setEmpty();
            this.imageZoomer.getDrawMatrix(this.tempDrawMatrix);
            this.imageZoomer.getVisibleRect(this.tempVisibleRect);
            Matrix matrix = this.tempDrawMatrix;
            Rect rect = this.tempVisibleRect;
            Size drawableSize = this.imageZoomer.getDrawableSize();
            Size viewSize = this.imageZoomer.getViewSize();
            boolean isZooming = this.imageZoomer.isZooming();
            if (!isReady()) {
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "not ready. %s", this.imageUri);
                    return;
                }
                return;
            }
            if (this.paused) {
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "paused. %s", this.imageUri);
                    return;
                }
                return;
            }
            if (rect.isEmpty() || drawableSize.isEmpty() || viewSize.isEmpty()) {
                SLog.w(NAME, "update params is empty. update. newVisibleRect=%s, drawableSize=%s, viewSize=%s. %s", rect.toShortString(), drawableSize.toString(), viewSize.toString(), this.imageUri);
                clean("update param is empty");
                return;
            }
            if (rect.width() == drawableSize.getWidth() && rect.height() == drawableSize.getHeight()) {
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "full display. update. newVisibleRect=%s. %s", rect.toShortString(), this.imageUri);
                }
                clean("full display");
            } else {
                this.lastZoomScale = this.zoomScale;
                this.matrix.set(matrix);
                this.zoomScale = SketchUtils.formatFloat(SketchUtils.getMatrixScale(this.matrix), 2);
                invalidateView();
                this.blockManager.update(rect, drawableSize, viewSize, getImageSize(), isZooming);
            }
        }
    }

    public void recycle(String str) {
        if (SketchUtils.sdkSupportBitmapRegionDecoder()) {
            this.running = false;
            clean(str);
            this.blockExecutor.recycle(str);
            this.blockManager.recycle(str);
            this.blockDecoder.recycle(str);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void reset() {
        SketchDrawable sketchDrawable;
        boolean z;
        if (SketchUtils.sdkSupportBitmapRegionDecoder()) {
            ImageView imageView = this.imageZoomer.getImageView();
            Drawable lastDrawable = SketchUtils.getLastDrawable(this.imageZoomer.getImageView().getDrawable());
            if (lastDrawable == 0 || !(lastDrawable instanceof SketchDrawable) || (lastDrawable instanceof SketchLoadingDrawable)) {
                sketchDrawable = null;
                z = false;
            } else {
                sketchDrawable = (SketchDrawable) lastDrawable;
                int intrinsicWidth = lastDrawable.getIntrinsicWidth();
                int intrinsicHeight = lastDrawable.getIntrinsicHeight();
                int originWidth = sketchDrawable.getOriginWidth();
                int originHeight = sketchDrawable.getOriginHeight();
                z = (intrinsicWidth < originWidth || intrinsicHeight < originHeight) & SketchUtils.sdkSupportBitmapRegionDecoder() & SketchUtils.formatSupportBitmapRegionDecoder(ImageType.valueOfMimeType(sketchDrawable.getMimeType()));
                if (z) {
                    if (SLog.isLoggable(1048578)) {
                        SLog.d(NAME, "Use BlockDisplayer. previewDrawableSize: %dx%d, imageSize: %dx%d, mimeType: %s. %s", Integer.valueOf(intrinsicWidth), Integer.valueOf(intrinsicHeight), Integer.valueOf(originWidth), Integer.valueOf(originHeight), sketchDrawable.getMimeType(), sketchDrawable.getKey());
                    }
                } else if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "Don't need to use BlockDisplayer. previewDrawableSize: %dx%d, imageSize: %dx%d, mimeType: %s. %s", Integer.valueOf(intrinsicWidth), Integer.valueOf(intrinsicHeight), Integer.valueOf(originWidth), Integer.valueOf(originHeight), sketchDrawable.getMimeType(), sketchDrawable.getKey());
                }
            }
            boolean z2 = !(imageView instanceof FunctionPropertyView) || ((FunctionPropertyView) imageView).getOptions().isCorrectImageOrientationDisabled();
            if (z) {
                clean("setImage");
                this.imageUri = sketchDrawable.getUri();
                this.running = !TextUtils.isEmpty(this.imageUri);
                this.blockDecoder.setImage(this.imageUri, z2);
                return;
            }
            clean("setImage");
            this.imageUri = null;
            this.running = false;
            this.blockDecoder.setImage(null, z2);
        }
    }

    public void setOnBlockChangedListener(OnBlockChangedListener onBlockChangedListener) {
        this.onBlockChangedListener = onBlockChangedListener;
    }

    public void setPause(boolean z) {
        if (SketchUtils.sdkSupportBitmapRegionDecoder() && z != this.paused) {
            this.paused = z;
            if (this.paused) {
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "pause. %s", this.imageUri);
                }
                if (this.running) {
                    clean("pause");
                    return;
                }
                return;
            }
            if (SLog.isLoggable(1048578)) {
                SLog.d(NAME, "resume. %s", this.imageUri);
            }
            if (this.running) {
                onMatrixChanged();
            }
        }
    }

    public void setShowBlockBounds(boolean z) {
        this.showBlockBounds = z;
        invalidateView();
    }
}
