package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/* loaded from: classes.dex */
public final class Downsampler {
    private static final int MARK_POSITION = 5242880;
    private static final String TAG = "Downsampler";
    private final BitmapPool bitmapPool;
    private final ArrayPool byteArrayPool;
    private final DisplayMetrics displayMetrics;
    private final List<ImageHeaderParser> parsers;
    public static final Option<DecodeFormat> DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.DEFAULT);
    public static final Option<DownsampleStrategy> DOWNSAMPLE_STRATEGY = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DownsampleStrategy", DownsampleStrategy.AT_LEAST);
    public static final Option<Boolean> FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", false);
    private static final String WBMP_MIME_TYPE = "image/vnd.wap.wbmp";
    private static final String ICO_MIME_TYPE = "image/x-ico";
    private static final Set<String> NO_DOWNSAMPLE_PRE_N_MIME_TYPES = Collections.unmodifiableSet(new HashSet(Arrays.asList(WBMP_MIME_TYPE, ICO_MIME_TYPE)));
    private static final DecodeCallbacks EMPTY_CALLBACKS = new DecodeCallbacks() { // from class: com.bumptech.glide.load.resource.bitmap.Downsampler.1
        @Override // com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeCallbacks
        public void onDecodeComplete(BitmapPool bitmapPool, Bitmap bitmap) throws IOException {
        }

        @Override // com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeCallbacks
        public void onObtainBounds() {
        }
    };
    private static final Set<ImageHeaderParser.ImageType> TYPES_THAT_USE_POOL_PRE_KITKAT = Collections.unmodifiableSet(EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
    private static final Queue<BitmapFactory.Options> OPTIONS_QUEUE = Util.createQueue(0);

    /* loaded from: classes.dex */
    public interface DecodeCallbacks {
        void onDecodeComplete(BitmapPool bitmapPool, Bitmap bitmap) throws IOException;

        void onObtainBounds();
    }

    public Downsampler(List<ImageHeaderParser> list, DisplayMetrics displayMetrics, BitmapPool bitmapPool, ArrayPool arrayPool) {
        this.parsers = list;
        this.displayMetrics = (DisplayMetrics) Preconditions.checkNotNull(displayMetrics);
        this.bitmapPool = (BitmapPool) Preconditions.checkNotNull(bitmapPool);
        this.byteArrayPool = (ArrayPool) Preconditions.checkNotNull(arrayPool);
    }

    static void calculateScaling(DownsampleStrategy downsampleStrategy, int i, int i2, int i3, int i4, int i5, BitmapFactory.Options options) {
        int i6;
        if (i2 <= 0 || i3 <= 0) {
            return;
        }
        float scaleFactor = (i == 90 || i == 270) ? downsampleStrategy.getScaleFactor(i3, i2, i4, i5) : downsampleStrategy.getScaleFactor(i2, i3, i4, i5);
        if (scaleFactor <= 0.0f) {
            throw new IllegalArgumentException("Cannot scale with factor: " + scaleFactor + " from: " + downsampleStrategy);
        }
        DownsampleStrategy.SampleSizeRounding sampleSizeRounding = downsampleStrategy.getSampleSizeRounding(i2, i3, i4, i5);
        if (sampleSizeRounding == null) {
            throw new IllegalArgumentException("Cannot round with null rounding");
        }
        int i7 = i2 / ((int) ((i2 * scaleFactor) + 0.5f));
        int i8 = i3 / ((int) ((i3 * scaleFactor) + 0.5f));
        int max = sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY ? Math.max(i7, i8) : Math.min(i7, i8);
        if (Build.VERSION.SDK_INT > 23 || !NO_DOWNSAMPLE_PRE_N_MIME_TYPES.contains(options.outMimeType)) {
            int max2 = Math.max(1, Integer.highestOneBit(max));
            i6 = (sampleSizeRounding != DownsampleStrategy.SampleSizeRounding.MEMORY || ((float) max2) >= 1.0f / scaleFactor) ? max2 : max2 << 1;
        } else {
            i6 = 1;
        }
        float f = i6 * scaleFactor;
        options.inSampleSize = i6;
        if (Build.VERSION.SDK_INT >= 19) {
            options.inTargetDensity = (int) ((1000.0f * f) + 0.5f);
            options.inDensity = 1000;
        }
        if (isScaling(options)) {
            options.inScaled = true;
        } else {
            options.inTargetDensity = 0;
            options.inDensity = 0;
        }
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "Calculate scaling, source: [" + i2 + "x" + i3 + "], target: [" + i4 + "x" + i5 + "], exact scale factor: " + scaleFactor + ", power of 2 sample size: " + i6 + ", adjusted scale factor: " + f + ", target density: " + options.inTargetDensity + ", density: " + options.inDensity);
        }
    }

    private Bitmap decodeFromWrappedStreams(InputStream inputStream, BitmapFactory.Options options, DownsampleStrategy downsampleStrategy, DecodeFormat decodeFormat, int i, int i2, boolean z, DecodeCallbacks decodeCallbacks) throws IOException {
        int i3;
        int i4;
        int round;
        int round2;
        int[] dimensions = getDimensions(inputStream, options, decodeCallbacks, this.bitmapPool);
        int i5 = dimensions[0];
        int i6 = dimensions[1];
        String str = options.outMimeType;
        int orientation = ImageHeaderParserUtils.getOrientation(this.parsers, inputStream, this.byteArrayPool);
        int exifOrientationDegrees = TransformationUtils.getExifOrientationDegrees(orientation);
        options.inPreferredConfig = getConfig(inputStream, decodeFormat);
        if (options.inPreferredConfig != Bitmap.Config.ARGB_8888) {
            options.inDither = true;
        }
        if (i == Integer.MIN_VALUE) {
            i3 = i2;
            i4 = i5;
        } else {
            i3 = i2;
            i4 = i;
        }
        int i7 = i3 == Integer.MIN_VALUE ? i6 : i3;
        calculateScaling(downsampleStrategy, exifOrientationDegrees, i5, i6, i4, i7, options);
        boolean z2 = Build.VERSION.SDK_INT >= 19;
        if ((options.inSampleSize == 1 || z2) && shouldUsePool(inputStream)) {
            if (z && z2) {
                round = i4;
                round2 = i7;
            } else {
                float f = isScaling(options) ? options.inTargetDensity / options.inDensity : 1.0f;
                int i8 = options.inSampleSize;
                float f2 = i8;
                int ceil = (int) Math.ceil(i5 / f2);
                int ceil2 = (int) Math.ceil(i6 / f2);
                round = Math.round(ceil * f);
                round2 = Math.round(ceil2 * f);
                if (Log.isLoggable(TAG, 2)) {
                    Log.v(TAG, "Calculated target [" + round + "x" + round2 + "] for source [" + i5 + "x" + i6 + "], sampleSize: " + i8 + ", targetDensity: " + options.inTargetDensity + ", density: " + options.inDensity + ", density multiplier: " + f);
                }
            }
            if (round > 0 && round2 > 0) {
                setInBitmap(options, this.bitmapPool, round, round2);
            }
        }
        Bitmap decodeStream = decodeStream(inputStream, options, decodeCallbacks, this.bitmapPool);
        decodeCallbacks.onDecodeComplete(this.bitmapPool, decodeStream);
        if (Log.isLoggable(TAG, 2)) {
            logDecode(i5, i6, str, options, decodeStream, i, i2);
        }
        Bitmap bitmap = null;
        if (decodeStream != null) {
            decodeStream.setDensity(this.displayMetrics.densityDpi);
            bitmap = TransformationUtils.rotateImageExif(this.bitmapPool, decodeStream, orientation);
            if (!decodeStream.equals(bitmap)) {
                this.bitmapPool.put(decodeStream);
            }
        }
        return bitmap;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:?, code lost:
    
        throw r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.graphics.Bitmap decodeStream(java.io.InputStream r5, android.graphics.BitmapFactory.Options r6, com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeCallbacks r7, com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool r8) throws java.io.IOException {
        /*
            boolean r0 = r6.inJustDecodeBounds
            if (r0 == 0) goto La
            r0 = 5242880(0x500000, float:7.34684E-39)
            r5.mark(r0)
            goto Ld
        La:
            r7.onObtainBounds()
        Ld:
            int r0 = r6.outWidth
            int r1 = r6.outHeight
            java.lang.String r2 = r6.outMimeType
            java.util.concurrent.locks.Lock r3 = com.bumptech.glide.load.resource.bitmap.TransformationUtils.getBitmapDrawableLock()
            r3.lock()
            r3 = 0
            android.graphics.Bitmap r4 = android.graphics.BitmapFactory.decodeStream(r5, r3, r6)     // Catch: java.lang.Throwable -> L2e java.lang.IllegalArgumentException -> L30
            java.util.concurrent.locks.Lock r7 = com.bumptech.glide.load.resource.bitmap.TransformationUtils.getBitmapDrawableLock()
            r7.unlock()
            boolean r6 = r6.inJustDecodeBounds
            if (r6 == 0) goto L2d
            r5.reset()
        L2d:
            return r4
        L2e:
            r5 = move-exception
            goto L61
        L30:
            r4 = move-exception
            java.io.IOException r0 = newIoExceptionForInBitmapAssertion(r4, r0, r1, r2, r6)     // Catch: java.lang.Throwable -> L2e
            java.lang.String r1 = "Downsampler"
            r2 = 3
            boolean r1 = android.util.Log.isLoggable(r1, r2)     // Catch: java.lang.Throwable -> L2e
            if (r1 == 0) goto L45
            java.lang.String r1 = "Downsampler"
            java.lang.String r2 = "Failed to decode with inBitmap, trying again without Bitmap re-use"
            android.util.Log.d(r1, r2, r0)     // Catch: java.lang.Throwable -> L2e
        L45:
            android.graphics.Bitmap r1 = r6.inBitmap     // Catch: java.lang.Throwable -> L2e
            if (r1 == 0) goto L60
            r5.reset()     // Catch: java.lang.Throwable -> L2e java.io.IOException -> L5f
            android.graphics.Bitmap r1 = r6.inBitmap     // Catch: java.lang.Throwable -> L2e java.io.IOException -> L5f
            r8.put(r1)     // Catch: java.lang.Throwable -> L2e java.io.IOException -> L5f
            r6.inBitmap = r3     // Catch: java.lang.Throwable -> L2e java.io.IOException -> L5f
            android.graphics.Bitmap r5 = decodeStream(r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L2e java.io.IOException -> L5f
            java.util.concurrent.locks.Lock r6 = com.bumptech.glide.load.resource.bitmap.TransformationUtils.getBitmapDrawableLock()
            r6.unlock()
            return r5
        L5f:
            throw r0     // Catch: java.lang.Throwable -> L2e
        L60:
            throw r0     // Catch: java.lang.Throwable -> L2e
        L61:
            java.util.concurrent.locks.Lock r6 = com.bumptech.glide.load.resource.bitmap.TransformationUtils.getBitmapDrawableLock()
            r6.unlock()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.Downsampler.decodeStream(java.io.InputStream, android.graphics.BitmapFactory$Options, com.bumptech.glide.load.resource.bitmap.Downsampler$DecodeCallbacks, com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool):android.graphics.Bitmap");
    }

    @TargetApi(19)
    @Nullable
    private static String getBitmapString(Bitmap bitmap) {
        String str;
        if (bitmap == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            str = " (" + bitmap.getAllocationByteCount() + SQLBuilder.PARENTHESES_RIGHT;
        } else {
            str = "";
        }
        return "[" + bitmap.getWidth() + "x" + bitmap.getHeight() + "] " + bitmap.getConfig() + str;
    }

    private Bitmap.Config getConfig(InputStream inputStream, DecodeFormat decodeFormat) throws IOException {
        boolean z;
        if (decodeFormat == DecodeFormat.PREFER_ARGB_8888 || Build.VERSION.SDK_INT == 16) {
            return Bitmap.Config.ARGB_8888;
        }
        try {
            z = ImageHeaderParserUtils.getType(this.parsers, inputStream, this.byteArrayPool).hasAlpha();
        } catch (IOException e) {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Cannot determine whether the image has alpha or not from header, format " + decodeFormat, e);
            }
            z = false;
        }
        return z ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
    }

    private static synchronized BitmapFactory.Options getDefaultOptions() {
        BitmapFactory.Options poll;
        synchronized (Downsampler.class) {
            synchronized (OPTIONS_QUEUE) {
                poll = OPTIONS_QUEUE.poll();
            }
            if (poll == null) {
                poll = new BitmapFactory.Options();
                resetOptions(poll);
            }
        }
        return poll;
    }

    private static int[] getDimensions(InputStream inputStream, BitmapFactory.Options options, DecodeCallbacks decodeCallbacks, BitmapPool bitmapPool) throws IOException {
        options.inJustDecodeBounds = true;
        decodeStream(inputStream, options, decodeCallbacks, bitmapPool);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

    private static String getInBitmapString(BitmapFactory.Options options) {
        return getBitmapString(options.inBitmap);
    }

    private static boolean isScaling(BitmapFactory.Options options) {
        return options.inTargetDensity > 0 && options.inDensity > 0 && options.inTargetDensity != options.inDensity;
    }

    private static void logDecode(int i, int i2, String str, BitmapFactory.Options options, Bitmap bitmap, int i3, int i4) {
        Log.v(TAG, "Decoded " + getBitmapString(bitmap) + " from [" + i + "x" + i2 + "] " + str + " with inBitmap " + getInBitmapString(options) + " for [" + i3 + "x" + i4 + "], sample size: " + options.inSampleSize + ", density: " + options.inDensity + ", target density: " + options.inTargetDensity + ", thread: " + Thread.currentThread().getName());
    }

    private static IOException newIoExceptionForInBitmapAssertion(IllegalArgumentException illegalArgumentException, int i, int i2, String str, BitmapFactory.Options options) {
        return new IOException("Exception decoding bitmap, outWidth: " + i + ", outHeight: " + i2 + ", outMimeType: " + str + ", inBitmap: " + getInBitmapString(options), illegalArgumentException);
    }

    private static void releaseOptions(BitmapFactory.Options options) {
        resetOptions(options);
        synchronized (OPTIONS_QUEUE) {
            OPTIONS_QUEUE.offer(options);
        }
    }

    private static void resetOptions(BitmapFactory.Options options) {
        options.inTempStorage = null;
        options.inDither = false;
        options.inScaled = false;
        options.inSampleSize = 1;
        options.inPreferredConfig = null;
        options.inJustDecodeBounds = false;
        options.inDensity = 0;
        options.inTargetDensity = 0;
        options.outWidth = 0;
        options.outHeight = 0;
        options.outMimeType = null;
        options.inBitmap = null;
        options.inMutable = true;
    }

    private static void setInBitmap(BitmapFactory.Options options, BitmapPool bitmapPool, int i, int i2) {
        options.inBitmap = bitmapPool.getDirty(i, i2, options.inPreferredConfig);
    }

    private boolean shouldUsePool(InputStream inputStream) throws IOException {
        if (Build.VERSION.SDK_INT >= 19) {
            return true;
        }
        try {
            return TYPES_THAT_USE_POOL_PRE_KITKAT.contains(ImageHeaderParserUtils.getType(this.parsers, inputStream, this.byteArrayPool));
        } catch (IOException e) {
            if (!Log.isLoggable(TAG, 3)) {
                return false;
            }
            Log.d(TAG, "Cannot determine the image type from header", e);
            return false;
        }
    }

    public Resource<Bitmap> decode(InputStream inputStream, int i, int i2, Options options) throws IOException {
        return decode(inputStream, i, i2, options, EMPTY_CALLBACKS);
    }

    public Resource<Bitmap> decode(InputStream inputStream, int i, int i2, Options options, DecodeCallbacks decodeCallbacks) throws IOException {
        Preconditions.checkArgument(inputStream.markSupported(), "You must provide an InputStream that supports mark()");
        byte[] bArr = (byte[]) this.byteArrayPool.get(65536, byte[].class);
        BitmapFactory.Options defaultOptions = getDefaultOptions();
        defaultOptions.inTempStorage = bArr;
        try {
            return BitmapResource.obtain(decodeFromWrappedStreams(inputStream, defaultOptions, (DownsampleStrategy) options.get(DOWNSAMPLE_STRATEGY), (DecodeFormat) options.get(DECODE_FORMAT), i, i2, ((Boolean) options.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS)).booleanValue(), decodeCallbacks), this.bitmapPool);
        } finally {
            releaseOptions(defaultOptions);
            this.byteArrayPool.put(bArr, byte[].class);
        }
    }

    public boolean handles(InputStream inputStream) {
        return true;
    }

    public boolean handles(ByteBuffer byteBuffer) {
        return true;
    }
}
