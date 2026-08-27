package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;

/* loaded from: classes.dex */
public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    private static final String TAG = "BitmapEncoder";
    public static final Option<Integer> COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", 90);
    public static final Option<Bitmap.CompressFormat> COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");

    private Bitmap.CompressFormat getFormat(Bitmap bitmap, Options options) {
        Bitmap.CompressFormat compressFormat = (Bitmap.CompressFormat) options.get(COMPRESSION_FORMAT);
        return compressFormat != null ? compressFormat : bitmap.hasAlpha() ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x007f A[Catch: all -> 0x00d1, TRY_LEAVE, TryCatch #0 {all -> 0x00d1, blocks: (B:3:0x0036, B:11:0x0054, B:13:0x0076, B:15:0x007f, B:34:0x00cd, B:32:0x00d0, B:28:0x0073), top: B:2:0x0036 }] */
    @Override // com.bumptech.glide.load.Encoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean encode(com.bumptech.glide.load.engine.Resource<android.graphics.Bitmap> r8, java.io.File r9, com.bumptech.glide.load.Options r10) {
        /*
            r7 = this;
            java.lang.Object r8 = r8.get()
            android.graphics.Bitmap r8 = (android.graphics.Bitmap) r8
            android.graphics.Bitmap$CompressFormat r0 = r7.getFormat(r8, r10)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "encode: ["
            r1.append(r2)
            int r2 = r8.getWidth()
            r1.append(r2)
            java.lang.String r2 = "x"
            r1.append(r2)
            int r2 = r8.getHeight()
            r1.append(r2)
            java.lang.String r2 = "] "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            android.support.v4.os.TraceCompat.beginSection(r1)
            long r1 = com.bumptech.glide.util.LogTime.getLogTime()     // Catch: java.lang.Throwable -> Ld1
            com.bumptech.glide.load.Option<java.lang.Integer> r3 = com.bumptech.glide.load.resource.bitmap.BitmapEncoder.COMPRESSION_QUALITY     // Catch: java.lang.Throwable -> Ld1
            java.lang.Object r3 = r10.get(r3)     // Catch: java.lang.Throwable -> Ld1
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch: java.lang.Throwable -> Ld1
            int r3 = r3.intValue()     // Catch: java.lang.Throwable -> Ld1
            r4 = 0
            r5 = 0
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L5e java.io.IOException -> L60
            r6.<init>(r9)     // Catch: java.lang.Throwable -> L5e java.io.IOException -> L60
            r8.compress(r0, r3, r6)     // Catch: java.lang.Throwable -> L58 java.io.IOException -> L5b
            r6.close()     // Catch: java.lang.Throwable -> L58 java.io.IOException -> L5b
            r4 = 1
            r6.close()     // Catch: java.io.IOException -> L76 java.lang.Throwable -> Ld1
            goto L76
        L58:
            r8 = move-exception
            r5 = r6
            goto Lcb
        L5b:
            r9 = move-exception
            r5 = r6
            goto L61
        L5e:
            r8 = move-exception
            goto Lcb
        L60:
            r9 = move-exception
        L61:
            java.lang.String r3 = "BitmapEncoder"
            r6 = 3
            boolean r3 = android.util.Log.isLoggable(r3, r6)     // Catch: java.lang.Throwable -> L5e
            if (r3 == 0) goto L71
            java.lang.String r3 = "BitmapEncoder"
            java.lang.String r6 = "Failed to encode Bitmap"
            android.util.Log.d(r3, r6, r9)     // Catch: java.lang.Throwable -> L5e
        L71:
            if (r5 == 0) goto L76
            r5.close()     // Catch: java.io.IOException -> L76 java.lang.Throwable -> Ld1
        L76:
            java.lang.String r9 = "BitmapEncoder"
            r3 = 2
            boolean r9 = android.util.Log.isLoggable(r9, r3)     // Catch: java.lang.Throwable -> Ld1
            if (r9 == 0) goto Lc7
            java.lang.String r9 = "BitmapEncoder"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld1
            r3.<init>()     // Catch: java.lang.Throwable -> Ld1
            java.lang.String r5 = "Compressed with type: "
            r3.append(r5)     // Catch: java.lang.Throwable -> Ld1
            r3.append(r0)     // Catch: java.lang.Throwable -> Ld1
            java.lang.String r0 = " of size "
            r3.append(r0)     // Catch: java.lang.Throwable -> Ld1
            int r0 = com.bumptech.glide.util.Util.getBitmapByteSize(r8)     // Catch: java.lang.Throwable -> Ld1
            r3.append(r0)     // Catch: java.lang.Throwable -> Ld1
            java.lang.String r0 = " in "
            r3.append(r0)     // Catch: java.lang.Throwable -> Ld1
            double r0 = com.bumptech.glide.util.LogTime.getElapsedMillis(r1)     // Catch: java.lang.Throwable -> Ld1
            r3.append(r0)     // Catch: java.lang.Throwable -> Ld1
            java.lang.String r0 = ", options format: "
            r3.append(r0)     // Catch: java.lang.Throwable -> Ld1
            com.bumptech.glide.load.Option<android.graphics.Bitmap$CompressFormat> r0 = com.bumptech.glide.load.resource.bitmap.BitmapEncoder.COMPRESSION_FORMAT     // Catch: java.lang.Throwable -> Ld1
            java.lang.Object r10 = r10.get(r0)     // Catch: java.lang.Throwable -> Ld1
            r3.append(r10)     // Catch: java.lang.Throwable -> Ld1
            java.lang.String r10 = ", hasAlpha: "
            r3.append(r10)     // Catch: java.lang.Throwable -> Ld1
            boolean r8 = r8.hasAlpha()     // Catch: java.lang.Throwable -> Ld1
            r3.append(r8)     // Catch: java.lang.Throwable -> Ld1
            java.lang.String r8 = r3.toString()     // Catch: java.lang.Throwable -> Ld1
            android.util.Log.v(r9, r8)     // Catch: java.lang.Throwable -> Ld1
        Lc7:
            android.support.v4.os.TraceCompat.endSection()
            return r4
        Lcb:
            if (r5 == 0) goto Ld0
            r5.close()     // Catch: java.io.IOException -> Ld0 java.lang.Throwable -> Ld1
        Ld0:
            throw r8     // Catch: java.lang.Throwable -> Ld1
        Ld1:
            r8 = move-exception
            android.support.v4.os.TraceCompat.endSection()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.BitmapEncoder.encode(com.bumptech.glide.load.engine.Resource, java.io.File, com.bumptech.glide.load.Options):boolean");
    }

    @Override // com.bumptech.glide.load.ResourceEncoder
    public EncodeStrategy getEncodeStrategy(Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}
