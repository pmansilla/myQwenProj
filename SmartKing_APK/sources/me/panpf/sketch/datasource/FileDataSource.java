package me.panpf.sketch.datasource;

import android.support.annotation.NonNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.decode.NotFoundGifLibraryException;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.drawable.SketchGifFactory;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public class FileDataSource implements DataSource {
    private File file;
    private long length = -1;

    public FileDataSource(File file) {
        this.file = file;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    public File getFile(File file, String str) {
        return this.file;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public ImageFrom getImageFrom() {
        return ImageFrom.LOCAL;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override // me.panpf.sketch.datasource.DataSource
    public synchronized long getLength() throws IOException {
        if (this.length >= 0) {
            return this.length;
        }
        this.length = this.file.length();
        return this.length;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public SketchGifDrawable makeGifDrawable(@NonNull String str, @NonNull String str2, @NonNull ImageAttrs imageAttrs, @NonNull BitmapPool bitmapPool) throws IOException, NotFoundGifLibraryException {
        return SketchGifFactory.createGifDrawable(str, str2, imageAttrs, getImageFrom(), bitmapPool, this.file);
    }
}
