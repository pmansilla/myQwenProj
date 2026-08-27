package com.mob.elp.d;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import com.mob.elp.d.b;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/* compiled from: GetBitmaps.java */
/* loaded from: classes.dex */
public class e {
    private Bitmap[] a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GetBitmaps.java */
    /* loaded from: classes.dex */
    public class a implements b.a {
        a() {
        }

        public void a(int i, Bitmap bitmap) {
            e.this.a[i] = bitmap;
        }
    }

    /* compiled from: GetBitmaps.java */
    /* loaded from: classes.dex */
    public interface b {
        void a(ArrayList<Bitmap> arrayList);
    }

    public static Bitmap a(String str, int i, int i2, int i3) {
        Bitmap decodeFile = BitmapFactory.decodeFile(str);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodeFile.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        if (byteArrayOutputStream.toByteArray().length / 1024 > 1024) {
            byteArrayOutputStream.reset();
            decodeFile.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(byteArrayInputStream, null, options);
        int i4 = options.outWidth;
        int i5 = options.outHeight;
        options.inJustDecodeBounds = false;
        int i6 = (i4 <= i5 || i4 <= i) ? (i4 >= i5 || i5 <= i2) ? 1 : options.outHeight / i2 : i4 / i;
        if (i6 <= 0) {
            i6 = 1;
        }
        options.inSampleSize = i6;
        Bitmap decodeStream = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, options);
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        decodeStream.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream2);
        int i7 = 90;
        while (byteArrayOutputStream2.toByteArray().length / 1024 > 100) {
            byteArrayOutputStream2.reset();
            decodeStream.compress(Bitmap.CompressFormat.JPEG, i7, byteArrayOutputStream2);
            i7 -= 10;
        }
        Bitmap decodeStream2 = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream2.toByteArray()), null, null);
        if (decodeStream2 == null) {
            return null;
        }
        float f = i;
        float f2 = i2;
        float height = (1.0f * f2) / decodeStream2.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale((f * 1.0f) / decodeStream2.getWidth(), height);
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(decodeStream2, tileMode, tileMode);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        float f3 = i3;
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, f, f2), f3, f3, paint);
        decodeStream2.recycle();
        return createBitmap;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0075, code lost:
    
        if (r0.isEmpty() != false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00c0, code lost:
    
        r21.a(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00c3, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00bc, code lost:
    
        r21.a(null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00ba, code lost:
    
        if (r0.isEmpty() == false) goto L44;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(java.util.ArrayList<java.lang.String> r18, int r19, int r20, com.mob.elp.d.e.b r21) {
        /*
            r17 = this;
            r1 = r17
            r2 = r21
            boolean r0 = r18.isEmpty()
            if (r0 == 0) goto Lb
            return
        Lb:
            android.content.Context r0 = com.mob.MobSDK.getContext()
            android.content.Context r0 = r0.getApplicationContext()
            java.util.concurrent.CountDownLatch r11 = new java.util.concurrent.CountDownLatch
            int r3 = r18.size()
            r11.<init>(r3)
            int r3 = r18.size()
            android.graphics.Bitmap[] r3 = new android.graphics.Bitmap[r3]
            r1.a = r3
            com.mob.elp.d.e$a r12 = new com.mob.elp.d.e$a
            r12.<init>()
            r14 = 0
        L2a:
            int r3 = r18.size()
            if (r14 >= r3) goto L56
            java.lang.Thread r15 = new java.lang.Thread
            com.mob.elp.d.b r10 = new com.mob.elp.d.b
            r9 = r18
            java.lang.Object r3 = r9.get(r14)
            r16 = r3
            java.lang.String r16 = (java.lang.String) r16
            r3 = r10
            r4 = r0
            r5 = r11
            r6 = r19
            r7 = r20
            r8 = r14
            r9 = r16
            r13 = r10
            r10 = r12
            r3.<init>(r4, r5, r6, r7, r8, r9, r10)
            r15.<init>(r13)
            r15.start()
            int r14 = r14 + 1
            goto L2a
        L56:
            r3 = 0
            r11.await()     // Catch: java.lang.Throwable -> L78 java.lang.InterruptedException -> L9f
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r4 = 0
        L60:
            android.graphics.Bitmap[] r5 = r1.a
            int r6 = r5.length
            if (r4 >= r6) goto L71
            r6 = r5[r4]
            if (r6 == 0) goto L6e
            r5 = r5[r4]
            r0.add(r5)
        L6e:
            int r4 = r4 + 1
            goto L60
        L71:
            boolean r4 = r0.isEmpty()
            if (r4 == 0) goto Lc0
            goto Lbc
        L78:
            r0 = move-exception
            r4 = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r5 = 0
        L80:
            android.graphics.Bitmap[] r6 = r1.a
            int r7 = r6.length
            if (r5 >= r7) goto L91
            r7 = r6[r5]
            if (r7 == 0) goto L8e
            r6 = r6[r5]
            r0.add(r6)
        L8e:
            int r5 = r5 + 1
            goto L80
        L91:
            boolean r5 = r0.isEmpty()
            if (r5 == 0) goto L9b
            r2.a(r3)
            goto L9e
        L9b:
            r2.a(r0)
        L9e:
            throw r4
        L9f:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r4 = 0
        La5:
            android.graphics.Bitmap[] r5 = r1.a
            int r6 = r5.length
            if (r4 >= r6) goto Lb6
            r6 = r5[r4]
            if (r6 == 0) goto Lb3
            r5 = r5[r4]
            r0.add(r5)
        Lb3:
            int r4 = r4 + 1
            goto La5
        Lb6:
            boolean r4 = r0.isEmpty()
            if (r4 == 0) goto Lc0
        Lbc:
            r2.a(r3)
            goto Lc3
        Lc0:
            r2.a(r0)
        Lc3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.elp.d.e.a(java.util.ArrayList, int, int, com.mob.elp.d.e$b):void");
    }
}
