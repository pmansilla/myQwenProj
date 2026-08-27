package me.panpf.sketch.process;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import java.lang.reflect.Array;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.request.Resize;

/* loaded from: classes2.dex */
public class GaussianBlurImageProcessor extends WrappedImageProcessor {
    private static final int DEFAULT_RADIUS = 15;
    private static final int NO_LAYER_COLOR = -1;
    private int maskColor;
    private int radius;

    private GaussianBlurImageProcessor(int i, int i2, WrappedImageProcessor wrappedImageProcessor) {
        super(wrappedImageProcessor);
        this.radius = i;
        this.maskColor = i2;
    }

    public static Bitmap fastGaussianBlur(Bitmap bitmap, int i, boolean z) {
        Bitmap bitmap2;
        Bitmap bitmap3;
        int[] iArr;
        int i2 = i;
        Bitmap copy = z ? bitmap : bitmap.copy(bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888, true);
        if (i2 < 1) {
            return null;
        }
        try {
            int width = copy.getWidth();
            int height = copy.getHeight();
            int i3 = width * height;
            int[] iArr2 = new int[i3];
            copy.getPixels(iArr2, 0, width, 0, 0, width, height);
            int i4 = width - 1;
            int i5 = height - 1;
            int i6 = i2 + i2 + 1;
            int[] iArr3 = new int[i3];
            int[] iArr4 = new int[i3];
            int[] iArr5 = new int[i3];
            int[] iArr6 = new int[Math.max(width, height)];
            int i7 = (i6 + 1) >> 1;
            int i8 = i7 * i7;
            int i9 = i8 * 256;
            int[] iArr7 = new int[i9];
            for (int i10 = 0; i10 < i9; i10++) {
                iArr7[i10] = i10 / i8;
            }
            int[][] iArr8 = (int[][]) Array.newInstance((Class<?>) int.class, i6, 3);
            int i11 = i2 + 1;
            int i12 = 0;
            int i13 = 0;
            int i14 = 0;
            while (i12 < height) {
                int i15 = -i2;
                int i16 = 0;
                int i17 = 0;
                int i18 = 0;
                int i19 = 0;
                int i20 = 0;
                int i21 = 0;
                int i22 = 0;
                int i23 = 0;
                int i24 = 0;
                while (i15 <= i2) {
                    bitmap2 = copy;
                    int i25 = height;
                    try {
                        int i26 = iArr2[i13 + Math.min(i4, Math.max(i15, 0))];
                        int[] iArr9 = iArr8[i15 + i2];
                        iArr9[0] = (i26 & 16711680) >> 16;
                        iArr9[1] = (i26 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                        iArr9[2] = i26 & 255;
                        int abs = i11 - Math.abs(i15);
                        i16 += iArr9[0] * abs;
                        i17 += iArr9[1] * abs;
                        i18 += iArr9[2] * abs;
                        if (i15 > 0) {
                            i19 += iArr9[0];
                            i21 += iArr9[1];
                            i23 += iArr9[2];
                        } else {
                            i20 += iArr9[0];
                            i22 += iArr9[1];
                            i24 += iArr9[2];
                        }
                        i15++;
                        copy = bitmap2;
                        height = i25;
                    } catch (Throwable th) {
                        th = th;
                        th.printStackTrace();
                        if (bitmap2 == null || (bitmap3 = bitmap2) == bitmap) {
                            return null;
                        }
                        bitmap3.recycle();
                        return null;
                    }
                }
                Bitmap bitmap4 = copy;
                int i27 = height;
                int i28 = i2;
                int i29 = 0;
                while (i29 < width) {
                    iArr3[i13] = iArr7[i16];
                    iArr4[i13] = iArr7[i17];
                    iArr5[i13] = iArr7[i18];
                    int i30 = i16 - i20;
                    int i31 = i17 - i22;
                    int i32 = i18 - i24;
                    int[] iArr10 = iArr8[((i28 - i2) + i6) % i6];
                    int i33 = i20 - iArr10[0];
                    int i34 = i22 - iArr10[1];
                    int i35 = i24 - iArr10[2];
                    if (i12 == 0) {
                        iArr = iArr7;
                        iArr6[i29] = Math.min(i29 + i2 + 1, i4);
                    } else {
                        iArr = iArr7;
                    }
                    int i36 = iArr2[i14 + iArr6[i29]];
                    iArr10[0] = (i36 & 16711680) >> 16;
                    iArr10[1] = (i36 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                    iArr10[2] = i36 & 255;
                    int i37 = i19 + iArr10[0];
                    int i38 = i21 + iArr10[1];
                    int i39 = i23 + iArr10[2];
                    i16 = i30 + i37;
                    i17 = i31 + i38;
                    i18 = i32 + i39;
                    i28 = (i28 + 1) % i6;
                    int[] iArr11 = iArr8[i28 % i6];
                    i20 = i33 + iArr11[0];
                    i22 = i34 + iArr11[1];
                    i24 = i35 + iArr11[2];
                    i19 = i37 - iArr11[0];
                    i21 = i38 - iArr11[1];
                    i23 = i39 - iArr11[2];
                    i13++;
                    i29++;
                    iArr7 = iArr;
                }
                i14 += width;
                i12++;
                copy = bitmap4;
                height = i27;
            }
            bitmap2 = copy;
            int[] iArr12 = iArr7;
            int i40 = height;
            int i41 = 0;
            while (i41 < width) {
                int i42 = -i2;
                int i43 = i42 * width;
                int i44 = 0;
                int i45 = 0;
                int i46 = 0;
                int i47 = 0;
                int i48 = 0;
                int i49 = 0;
                int i50 = 0;
                int i51 = 0;
                int i52 = 0;
                while (i42 <= i2) {
                    int[] iArr13 = iArr6;
                    int max = Math.max(0, i43) + i41;
                    int[] iArr14 = iArr8[i42 + i2];
                    iArr14[0] = iArr3[max];
                    iArr14[1] = iArr4[max];
                    iArr14[2] = iArr5[max];
                    int abs2 = i11 - Math.abs(i42);
                    i44 += iArr3[max] * abs2;
                    i45 += iArr4[max] * abs2;
                    i46 += iArr5[max] * abs2;
                    if (i42 > 0) {
                        i47 += iArr14[0];
                        i49 += iArr14[1];
                        i51 += iArr14[2];
                    } else {
                        i48 += iArr14[0];
                        i50 += iArr14[1];
                        i52 += iArr14[2];
                    }
                    if (i42 < i5) {
                        i43 += width;
                    }
                    i42++;
                    iArr6 = iArr13;
                }
                int[] iArr15 = iArr6;
                int i53 = i41;
                int i54 = i51;
                int i55 = i40;
                int i56 = 0;
                int i57 = i49;
                int i58 = i47;
                int i59 = i2;
                while (i56 < i55) {
                    iArr2[i53] = (iArr2[i53] & (-16777216)) | (iArr12[i44] << 16) | (iArr12[i45] << 8) | iArr12[i46];
                    int i60 = i44 - i48;
                    int i61 = i45 - i50;
                    int i62 = i46 - i52;
                    int[] iArr16 = iArr8[((i59 - i2) + i6) % i6];
                    int i63 = i48 - iArr16[0];
                    int i64 = i50 - iArr16[1];
                    int i65 = i52 - iArr16[2];
                    if (i41 == 0) {
                        iArr15[i56] = Math.min(i56 + i11, i5) * width;
                    }
                    int i66 = iArr15[i56] + i41;
                    iArr16[0] = iArr3[i66];
                    iArr16[1] = iArr4[i66];
                    iArr16[2] = iArr5[i66];
                    int i67 = i58 + iArr16[0];
                    int i68 = i57 + iArr16[1];
                    int i69 = i54 + iArr16[2];
                    i44 = i60 + i67;
                    i45 = i61 + i68;
                    i46 = i62 + i69;
                    i59 = (i59 + 1) % i6;
                    int[] iArr17 = iArr8[i59];
                    i48 = i63 + iArr17[0];
                    i50 = i64 + iArr17[1];
                    i52 = i65 + iArr17[2];
                    i58 = i67 - iArr17[0];
                    i57 = i68 - iArr17[1];
                    i54 = i69 - iArr17[2];
                    i53 += width;
                    i56++;
                    i2 = i;
                }
                i41++;
                i40 = i55;
                iArr6 = iArr15;
                i2 = i;
            }
            bitmap2.setPixels(iArr2, 0, width, 0, 0, width, i40);
            return bitmap2;
        } catch (Throwable th2) {
            th = th2;
            bitmap2 = copy;
        }
    }

    public static GaussianBlurImageProcessor make() {
        return new GaussianBlurImageProcessor(15, -1, null);
    }

    public static GaussianBlurImageProcessor make(int i, int i2) {
        return new GaussianBlurImageProcessor(i, i2, null);
    }

    public static GaussianBlurImageProcessor make(int i, int i2, WrappedImageProcessor wrappedImageProcessor) {
        return new GaussianBlurImageProcessor(i, i2, wrappedImageProcessor);
    }

    public static GaussianBlurImageProcessor make(WrappedImageProcessor wrappedImageProcessor) {
        return new GaussianBlurImageProcessor(15, -1, wrappedImageProcessor);
    }

    public static GaussianBlurImageProcessor makeLayerColor(int i) {
        return new GaussianBlurImageProcessor(15, i, null);
    }

    public static GaussianBlurImageProcessor makeLayerColor(int i, WrappedImageProcessor wrappedImageProcessor) {
        return new GaussianBlurImageProcessor(15, i, wrappedImageProcessor);
    }

    public static GaussianBlurImageProcessor makeRadius(int i) {
        return new GaussianBlurImageProcessor(i, -1, null);
    }

    public static GaussianBlurImageProcessor makeRadius(int i, WrappedImageProcessor wrappedImageProcessor) {
        return new GaussianBlurImageProcessor(i, -1, wrappedImageProcessor);
    }

    public int getMaskColor() {
        return this.maskColor;
    }

    public int getRadius() {
        return this.radius;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    public String onGetKey() {
        return String.format("%s(radius=%d,maskColor=%d)", "GaussianBlur", Integer.valueOf(this.radius), Integer.valueOf(this.maskColor));
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public Bitmap onProcess(@NonNull Sketch sketch, @NonNull Bitmap bitmap, @Nullable Resize resize, boolean z) {
        if (bitmap.isRecycled()) {
            return bitmap;
        }
        Bitmap fastGaussianBlur = fastGaussianBlur(bitmap, this.radius, bitmap.getConfig() != null && bitmap.isMutable());
        if (fastGaussianBlur == null) {
            return bitmap;
        }
        if (this.maskColor != -1) {
            new Canvas(fastGaussianBlur).drawColor(this.maskColor);
        }
        return fastGaussianBlur;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public String onToString() {
        return String.format("%s(radius=%d,maskColor=%d)", "GaussianBlurImageProcessor", Integer.valueOf(this.radius), Integer.valueOf(this.maskColor));
    }
}
