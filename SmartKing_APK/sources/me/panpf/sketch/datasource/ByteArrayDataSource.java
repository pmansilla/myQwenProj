package me.panpf.sketch.datasource;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.io.ByteArrayInputStream;
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
public class ByteArrayDataSource implements DataSource {
    private byte[] data;
    private ImageFrom imageFrom;

    public ByteArrayDataSource(byte[] bArr, ImageFrom imageFrom) {
        this.data = bArr;
        this.imageFrom = imageFrom;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    public File getFile(File file, String str) throws IOException {
        if (file == null) {
            return null;
        }
        if (!file.exists() && !file.getParentFile().mkdirs()) {
            return null;
        }
        File file2 = !TextUtils.isEmpty(str) ? new File(file, str) : new File(file, SketchUtils.generatorTempFileName(this, String.valueOf(System.currentTimeMillis())));
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
        return this.imageFrom;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.data);
    }

    @Override // me.panpf.sketch.datasource.DataSource
    public long getLength() throws IOException {
        return this.data.length;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public SketchGifDrawable makeGifDrawable(@NonNull String str, @NonNull String str2, @NonNull ImageAttrs imageAttrs, @NonNull BitmapPool bitmapPool) throws IOException, NotFoundGifLibraryException {
        return SketchGifFactory.createGifDrawable(str, str2, imageAttrs, getImageFrom(), bitmapPool, this.data);
    }
}
