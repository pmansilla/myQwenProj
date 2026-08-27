package me.panpf.sketch.datasource;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.decode.NotFoundGifLibraryException;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.drawable.SketchGifFactory;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class DrawableDataSource implements DataSource {
    private Context context;
    private int drawableId;
    private long length = -1;

    public DrawableDataSource(Context context, int i) {
        this.context = context;
        this.drawableId = i;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    public File getFile(File file, String str) throws IOException {
        if (file == null) {
            return null;
        }
        if (!file.exists() && !file.getParentFile().mkdirs()) {
            return null;
        }
        File file2 = !TextUtils.isEmpty(str) ? new File(file, str) : new File(file, SketchUtils.generatorTempFileName(this, String.valueOf(this.drawableId)));
        InputStream inputStream = getInputStream();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            while (true) {
                try {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        return file2;
                    }
                    fileOutputStream.write(bArr, 0, read);
                } finally {
                    SketchUtils.close(fileOutputStream);
                    SketchUtils.close(inputStream);
                }
            }
        } catch (IOException e) {
            SketchUtils.close(inputStream);
            throw e;
        }
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public ImageFrom getImageFrom() {
        return ImageFrom.LOCAL;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public InputStream getInputStream() throws IOException {
        return this.context.getResources().openRawResource(this.drawableId);
    }

    @Override // me.panpf.sketch.datasource.DataSource
    public long getLength() throws IOException {
        long j = 0;
        if (this.length >= 0) {
            return this.length;
        }
        AssetFileDescriptor assetFileDescriptor = null;
        try {
            AssetFileDescriptor openRawResourceFd = this.context.getResources().openRawResourceFd(this.drawableId);
            if (openRawResourceFd != null) {
                try {
                    j = openRawResourceFd.getLength();
                } catch (Throwable th) {
                    th = th;
                    assetFileDescriptor = openRawResourceFd;
                    SketchUtils.close(assetFileDescriptor);
                    throw th;
                }
            }
            this.length = j;
            SketchUtils.close(openRawResourceFd);
            return this.length;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public SketchGifDrawable makeGifDrawable(@NonNull String str, @NonNull String str2, @NonNull ImageAttrs imageAttrs, @NonNull BitmapPool bitmapPool) throws IOException, NotFoundGifLibraryException {
        return SketchGifFactory.createGifDrawable(str, str2, imageAttrs, getImageFrom(), bitmapPool, this.context.getResources(), this.drawableId);
    }
}
