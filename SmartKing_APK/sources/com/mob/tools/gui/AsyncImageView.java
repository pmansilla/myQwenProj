package com.mob.tools.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mob.tools.MobLog;
import com.mob.tools.gui.BitmapProcessor;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.UIHandler;
import com.wx.wheelview.common.WheelConstants;
import java.lang.ref.WeakReference;
import java.util.Random;

/* loaded from: classes2.dex */
public class AsyncImageView extends ImageView implements BitmapProcessor.BitmapCallback, Handler.Callback {
    private static final int MSG_IMG_GOT = 1;
    private static final Random RND = new Random();
    private Bitmap defaultBm;
    private int defaultRes;
    private int desiredHeight;
    private int desiredWidth;
    private long diskCacheTime;
    private Bitmap errorBm;
    private int errorRes;
    private boolean lastReqIsOk;
    private long maxBytes;
    private Path path;
    private int quality;
    private float[] rect;
    private WeakReference<AsyncImageView> refAiv;
    private Bitmap result;
    private boolean scaleToCrop;
    private String url;
    private boolean useDiskCache;
    private boolean useRamCache;

    public AsyncImageView(Context context) {
        super(context);
        this.errorRes = 0;
        this.errorBm = null;
        this.refAiv = null;
        this.useRamCache = true;
        this.useDiskCache = true;
        this.diskCacheTime = 0L;
        this.desiredWidth = 0;
        this.desiredHeight = 0;
        this.maxBytes = 0L;
        this.quality = 0;
        init(context);
    }

    public AsyncImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.errorRes = 0;
        this.errorBm = null;
        this.refAiv = null;
        this.useRamCache = true;
        this.useDiskCache = true;
        this.diskCacheTime = 0L;
        this.desiredWidth = 0;
        this.desiredHeight = 0;
        this.maxBytes = 0L;
        this.quality = 0;
        init(context);
    }

    public AsyncImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.errorRes = 0;
        this.errorBm = null;
        this.refAiv = null;
        this.useRamCache = true;
        this.useDiskCache = true;
        this.diskCacheTime = 0L;
        this.desiredWidth = 0;
        this.desiredHeight = 0;
        this.maxBytes = 0L;
        this.quality = 0;
        init(context);
    }

    private BitmapProcessor.BitmapDesiredOptions getBitmapDesiredOptions() {
        if ((this.desiredWidth <= 1 || this.desiredHeight <= 1) && this.maxBytes < 10240 && this.quality <= 0) {
            return null;
        }
        BitmapProcessor.BitmapDesiredOptions bitmapDesiredOptions = new BitmapProcessor.BitmapDesiredOptions();
        bitmapDesiredOptions.desiredWidth = this.desiredWidth;
        bitmapDesiredOptions.desiredHeight = this.desiredHeight;
        bitmapDesiredOptions.maxBytes = this.maxBytes;
        bitmapDesiredOptions.quality = this.quality;
        return bitmapDesiredOptions;
    }

    private int[] getSize() {
        ViewGroup.LayoutParams layoutParams;
        int width = getWidth();
        int height = getHeight();
        if ((width == 0 || height == 0) && (layoutParams = getLayoutParams()) != null) {
            width = layoutParams.width;
            height = layoutParams.height;
        }
        if (width == 0 || height == 0) {
            measure(0, 0);
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }
        return new int[]{width, height};
    }

    private Bitmap goCrop(Bitmap bitmap) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (width == 0.0f || height == 0.0f) {
            return bitmap;
        }
        int[] size = getSize();
        if (size[0] == 0 || size[1] == 0) {
            return bitmap;
        }
        float f = (size[1] * width) / size[0];
        if (f == height) {
            return bitmap;
        }
        int[] iArr = new int[4];
        if (f < height) {
            iArr[1] = (int) ((height - f) / 2.0f);
            iArr[3] = iArr[1];
        } else {
            iArr[0] = (int) ((width - ((size[0] * height) / size[1])) / 2.0f);
            iArr[2] = iArr[0];
        }
        try {
            return BitmapHelper.cropBitmap(bitmap, iArr[0], iArr[1], iArr[2], iArr[3]);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return bitmap;
        }
    }

    private void init(Context context) {
        if (isInEditMode()) {
            setBackgroundColor(-16777216);
        } else {
            BitmapProcessor.prepare(context);
        }
    }

    private void myClip(Canvas canvas) {
        if (this.rect != null) {
            if (this.path == null) {
                int width = getWidth();
                int height = getHeight();
                this.path = new Path();
                this.path.addRoundRect(new RectF(0.0f, 0.0f, width, height), this.rect, Path.Direction.CW);
            }
            canvas.clipPath(this.path);
        }
    }

    public void deleteCachedFile(String str) {
        BitmapProcessor.deleteCachedFile(str, getBitmapDesiredOptions());
    }

    public void execute(String str, int i) {
        execute(str, i, 0);
    }

    public void execute(String str, int i, int i2) {
        Bitmap bitmapFromCache;
        if ((this.useRamCache || this.useDiskCache) && this.lastReqIsOk && !TextUtils.isEmpty(str) && str.equals(this.url)) {
            return;
        }
        this.lastReqIsOk = false;
        this.url = str;
        this.result = null;
        this.defaultRes = i;
        this.errorRes = i2;
        if (TextUtils.isEmpty(str)) {
            if (i2 != 0) {
                i = i2;
            }
            setImageResource(i);
            return;
        }
        BitmapProcessor.BitmapDesiredOptions bitmapDesiredOptions = getBitmapDesiredOptions();
        if (this.useRamCache && (bitmapFromCache = BitmapProcessor.getBitmapFromCache(str, bitmapDesiredOptions)) != null && !bitmapFromCache.isRecycled()) {
            setBitmap(bitmapFromCache);
            this.lastReqIsOk = true;
            return;
        }
        if (i > 0) {
            setImageResource(i);
        }
        if (this.refAiv == null || this.refAiv.get() == null) {
            this.refAiv = new WeakReference<>(this);
        }
        BitmapProcessor.process(str, bitmapDesiredOptions, this.useRamCache, this.useDiskCache, this.diskCacheTime, this);
    }

    public void execute(String str, Bitmap bitmap) {
        execute(str, bitmap, (Bitmap) null);
    }

    public void execute(String str, Bitmap bitmap, Bitmap bitmap2) {
        Bitmap bitmapFromCache;
        if ((this.useRamCache || this.useDiskCache) && this.lastReqIsOk && !TextUtils.isEmpty(str) && str.equals(this.url)) {
            return;
        }
        this.lastReqIsOk = false;
        this.url = str;
        this.result = null;
        this.defaultBm = bitmap;
        this.errorBm = bitmap2;
        if (TextUtils.isEmpty(str)) {
            if (bitmap2 != null) {
                bitmap = bitmap2;
            }
            setImageBitmap(bitmap);
            return;
        }
        BitmapProcessor.BitmapDesiredOptions bitmapDesiredOptions = getBitmapDesiredOptions();
        if (this.useRamCache && (bitmapFromCache = BitmapProcessor.getBitmapFromCache(str, bitmapDesiredOptions)) != null && !bitmapFromCache.isRecycled()) {
            setBitmap(bitmapFromCache);
            this.lastReqIsOk = true;
            return;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            setImageBitmap(bitmap);
        }
        if (this.refAiv == null || this.refAiv.get() == null) {
            this.refAiv = new WeakReference<>(this);
        }
        BitmapProcessor.process(str, bitmapDesiredOptions, this.useRamCache, this.useDiskCache, this.diskCacheTime, this);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what != 1 || this.refAiv == null || this.refAiv.get() == null) {
            return false;
        }
        try {
            Object obj = ((Object[]) message.obj)[0];
            Object obj2 = ((Object[]) message.obj)[1];
            if (obj2 != null && obj != null && obj.equals(this.url)) {
                this.result = (Bitmap) obj2;
                this.refAiv.get().setImageBitmap(this.result);
                this.lastReqIsOk = true;
            } else if (this.errorRes > 0) {
                this.refAiv.get().setImageResource(this.errorRes);
            } else if (this.errorBm != null && !this.errorBm.isRecycled()) {
                this.refAiv.get().setImageBitmap(this.errorBm);
            } else if (this.defaultBm == null || this.defaultBm.isRecycled()) {
                this.refAiv.get().setImageResource(this.defaultRes);
            } else {
                this.refAiv.get().setImageBitmap(this.defaultBm);
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return false;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0 || getDrawable().getIntrinsicHeight() == 0) {
            return;
        }
        Matrix imageMatrix = getImageMatrix();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int saveCount = canvas.getSaveCount();
        canvas.save();
        if (this.result != null) {
            myClip(canvas);
            canvas.drawBitmap(this.result, imageMatrix, new Paint(6));
        } else {
            if (imageMatrix != null || paddingLeft != 0 || paddingTop != 0) {
                if (Build.VERSION.SDK_INT >= 16 && getCropToPadding()) {
                    int scrollX = getScrollX();
                    int scrollY = getScrollY();
                    canvas.clipRect(scrollX + paddingLeft, scrollY + paddingTop, ((scrollX + getRight()) - getLeft()) - getPaddingRight(), ((scrollY + getBottom()) - getTop()) - getPaddingBottom());
                }
                canvas.translate(paddingLeft, paddingTop);
                if (imageMatrix != null) {
                    canvas.concat(imageMatrix);
                }
            }
            getDrawable().draw(canvas);
        }
        canvas.restoreToCount(saveCount);
    }

    public void onImageGot(String str, Bitmap bitmap) {
        if (this.refAiv == null || this.refAiv.get() == null) {
            return;
        }
        if (str == null || str.trim().length() <= 0 || !str.equals(this.url)) {
            bitmap = null;
        }
        if (bitmap != null && this.scaleToCrop) {
            bitmap = goCrop(bitmap);
        }
        Message message = new Message();
        message.what = 1;
        message.obj = new Object[]{str, bitmap};
        UIHandler.sendMessageDelayed(message, RND.nextInt(WheelConstants.WHEEL_SCROLL_DELAY_DURATION), this);
    }

    public void removeRamCache(String str) {
        if (this.useRamCache) {
            BitmapProcessor.removeBitmapFromRamCache(str, getBitmapDesiredOptions());
        }
    }

    public void setBitmap(Bitmap bitmap) {
        if (this.scaleToCrop) {
            bitmap = goCrop(bitmap);
        }
        setImageBitmap(bitmap);
        this.result = bitmap;
    }

    public void setCompressOptions(int i, int i2, int i3, long j) {
        this.desiredWidth = i;
        this.desiredHeight = i2;
        this.quality = i3;
        this.maxBytes = j;
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        MobLog.getInstance().w(new RuntimeException("Not Support"));
    }

    public void setRound(float f) {
        setRound(f, f, f, f);
    }

    public void setRound(float f, float f2, float f3, float f4) {
        this.rect = new float[]{f, f, f2, f2, f3, f3, f4, f4};
    }

    public void setScaleToCropCenter(boolean z) {
        this.scaleToCrop = z;
    }

    public void setUseCacheOption(boolean z, boolean z2) {
        setUseCacheOption(z, z2, 0L);
    }

    public void setUseCacheOption(boolean z, boolean z2, long j) {
        this.useRamCache = z;
        this.useDiskCache = z2;
        if (z2) {
            this.diskCacheTime = j;
        }
    }
}
