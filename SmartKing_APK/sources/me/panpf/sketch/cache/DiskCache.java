package me.panpf.sketch.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;
import me.panpf.sketch.util.DiskLruCache;

/* loaded from: classes2.dex */
public interface DiskCache {
    public static final String DISK_CACHE_DIR_NAME = "sketch";
    public static final int DISK_CACHE_MAX_SIZE = 104857600;
    public static final int DISK_CACHE_RESERVED_SPACE_SIZE = 209715200;

    /* loaded from: classes2.dex */
    public interface Editor {
        void abort();

        void commit() throws IOException, DiskLruCache.EditorChangedException, DiskLruCache.ClosedException, DiskLruCache.FileNotExistException;

        OutputStream newOutputStream() throws IOException;
    }

    /* loaded from: classes2.dex */
    public interface Entry {
        boolean delete();

        @NonNull
        File getFile();

        @NonNull
        String getKey();

        @NonNull
        InputStream newInputStream() throws IOException;
    }

    void clear();

    void close();

    @Nullable
    Editor edit(@NonNull String str);

    boolean exist(@NonNull String str);

    @Nullable
    Entry get(@NonNull String str);

    @NonNull
    File getCacheDir();

    @NonNull
    ReentrantLock getEditLock(@NonNull String str);

    long getMaxSize();

    long getSize();

    boolean isClosed();

    boolean isDisabled();

    @NonNull
    String keyEncode(@NonNull String str);

    void setDisabled(boolean z);
}
