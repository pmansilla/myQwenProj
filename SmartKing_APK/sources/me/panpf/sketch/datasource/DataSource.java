package me.panpf.sketch.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.decode.NotFoundGifLibraryException;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public interface DataSource {
    @Nullable
    File getFile(@Nullable File file, @Nullable String str) throws IOException;

    @NonNull
    ImageFrom getImageFrom();

    @NonNull
    InputStream getInputStream() throws IOException;

    long getLength() throws IOException;

    @NonNull
    SketchGifDrawable makeGifDrawable(@NonNull String str, @NonNull String str2, @NonNull ImageAttrs imageAttrs, @NonNull BitmapPool bitmapPool) throws IOException, NotFoundGifLibraryException;
}
