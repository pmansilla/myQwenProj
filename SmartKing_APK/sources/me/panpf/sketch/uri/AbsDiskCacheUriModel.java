package me.panpf.sketch.uri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.datasource.ByteArrayDataSource;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.datasource.DiskCacheDataSource;
import me.panpf.sketch.request.DownloadResult;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.util.DiskLruCache;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public abstract class AbsDiskCacheUriModel<Content> extends UriModel {
    private static final String NAME = "AbsDiskCacheUriModel";

    @NonNull
    private DataSource readContent(Context context, String str, String str2) throws GetDataSourceException {
        OutputStream bufferedOutputStream;
        GetDataSourceException getDataSourceException;
        Content content = getContent(context, str);
        DiskCache diskCache = Sketch.with(context).getConfiguration().getDiskCache();
        DiskCache.Editor edit = diskCache.edit(str2);
        if (edit != null) {
            try {
                bufferedOutputStream = new BufferedOutputStream(edit.newOutputStream(), 8192);
            } catch (IOException e) {
                edit.abort();
                closeContent(content, context);
                String format = String.format("Open output stream exception. %s", str);
                SLog.e(NAME, e, format);
                throw new GetDataSourceException(format, e);
            }
        } else {
            bufferedOutputStream = new ByteArrayOutputStream();
        }
        try {
            try {
                outContent(content, bufferedOutputStream);
                if (edit != null) {
                    try {
                        edit.commit();
                    } catch (IOException | DiskLruCache.ClosedException | DiskLruCache.EditorChangedException | DiskLruCache.FileNotExistException e2) {
                        edit.abort();
                        String format2 = String.format("Commit disk cache exception. %s", str);
                        SLog.e(NAME, e2, format2);
                        throw new GetDataSourceException(format2, e2);
                    }
                }
                if (edit == null) {
                    return new ByteArrayDataSource(((ByteArrayOutputStream) bufferedOutputStream).toByteArray(), ImageFrom.LOCAL);
                }
                DiskCache.Entry entry = diskCache.get(str2);
                if (entry != null) {
                    return new DiskCacheDataSource(entry, ImageFrom.LOCAL);
                }
                String format3 = String.format("Not found disk cache after save. %s", str);
                SLog.e(NAME, format3);
                throw new GetDataSourceException(format3);
            } finally {
                SketchUtils.close(bufferedOutputStream);
                closeContent(content, context);
            }
        } finally {
        }
    }

    protected abstract void closeContent(@NonNull Content content, @NonNull Context context);

    @NonNull
    protected abstract Content getContent(@NonNull Context context, @NonNull String str) throws GetDataSourceException;

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public final DataSource getDataSource(@NonNull Context context, @NonNull String str, @Nullable DownloadResult downloadResult) throws GetDataSourceException {
        DiskCache diskCache = Sketch.with(context).getConfiguration().getDiskCache();
        String diskCacheKey = getDiskCacheKey(str);
        DiskCache.Entry entry = diskCache.get(diskCacheKey);
        if (entry != null) {
            return new DiskCacheDataSource(entry, ImageFrom.DISK_CACHE);
        }
        ReentrantLock editLock = diskCache.getEditLock(diskCacheKey);
        editLock.lock();
        try {
            DiskCache.Entry entry2 = diskCache.get(diskCacheKey);
            return entry2 != null ? new DiskCacheDataSource(entry2, ImageFrom.DISK_CACHE) : readContent(context, str, diskCacheKey);
        } finally {
            editLock.unlock();
        }
    }

    protected abstract void outContent(@NonNull Content content, @NonNull OutputStream outputStream) throws Exception;
}
