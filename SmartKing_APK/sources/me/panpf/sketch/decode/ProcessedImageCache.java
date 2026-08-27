package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.datasource.DiskCacheDataSource;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.request.LoadOptions;
import me.panpf.sketch.request.LoadRequest;
import me.panpf.sketch.util.DiskLruCache;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ProcessedImageCache {
    public boolean canUse(LoadOptions loadOptions) {
        if (!loadOptions.isCacheProcessedImageInDisk()) {
            return false;
        }
        if (loadOptions.getMaxSize() == null && loadOptions.getResize() == null && loadOptions.getProcessor() == null) {
            return (loadOptions.isThumbnailMode() && loadOptions.getResize() != null) || !loadOptions.isCorrectImageOrientationDisabled();
        }
        return true;
    }

    public boolean canUseCacheProcessedImageInDisk(int i) {
        return i >= 8;
    }

    public boolean checkDiskCache(LoadRequest loadRequest) {
        DiskCache diskCache = loadRequest.getConfiguration().getDiskCache();
        String processedDiskCacheKey = loadRequest.getProcessedDiskCacheKey();
        ReentrantLock editLock = diskCache.getEditLock(processedDiskCacheKey);
        editLock.lock();
        try {
            return diskCache.exist(processedDiskCacheKey);
        } finally {
            editLock.unlock();
        }
    }

    @Nullable
    public DiskCacheDataSource getDiskCache(LoadRequest loadRequest) {
        DiskCache diskCache = loadRequest.getConfiguration().getDiskCache();
        String processedDiskCacheKey = loadRequest.getProcessedDiskCacheKey();
        ReentrantLock editLock = diskCache.getEditLock(processedDiskCacheKey);
        editLock.lock();
        try {
            DiskCache.Entry entry = diskCache.get(processedDiskCacheKey);
            if (entry == null) {
                return null;
            }
            return new DiskCacheDataSource(entry, ImageFrom.DISK_CACHE).setFromProcessedCache(true);
        } finally {
            editLock.unlock();
        }
    }

    public void saveToDiskCache(LoadRequest loadRequest, Bitmap bitmap) {
        BufferedOutputStream bufferedOutputStream;
        DiskCache diskCache = loadRequest.getConfiguration().getDiskCache();
        String processedDiskCacheKey = loadRequest.getProcessedDiskCacheKey();
        ReentrantLock editLock = diskCache.getEditLock(processedDiskCacheKey);
        editLock.lock();
        try {
            DiskCache.Entry entry = diskCache.get(processedDiskCacheKey);
            if (entry != null) {
                entry.delete();
            }
            DiskCache.Editor edit = diskCache.edit(processedDiskCacheKey);
            if (edit != null) {
                BufferedOutputStream bufferedOutputStream2 = null;
                try {
                    try {
                        bufferedOutputStream = new BufferedOutputStream(edit.newOutputStream(), 8192);
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (IOException e) {
                    e = e;
                } catch (DiskLruCache.ClosedException e2) {
                    e = e2;
                } catch (DiskLruCache.EditorChangedException e3) {
                    e = e3;
                } catch (DiskLruCache.FileNotExistException e4) {
                    e = e4;
                }
                try {
                    bitmap.compress(SketchUtils.bitmapConfigToCompressFormat(bitmap.getConfig()), 100, bufferedOutputStream);
                    edit.commit();
                    SketchUtils.close(bufferedOutputStream);
                } catch (IOException e5) {
                    e = e5;
                    bufferedOutputStream2 = bufferedOutputStream;
                    e.printStackTrace();
                    edit.abort();
                    SketchUtils.close(bufferedOutputStream2);
                } catch (DiskLruCache.ClosedException e6) {
                    e = e6;
                    bufferedOutputStream2 = bufferedOutputStream;
                    e.printStackTrace();
                    edit.abort();
                    SketchUtils.close(bufferedOutputStream2);
                } catch (DiskLruCache.EditorChangedException e7) {
                    e = e7;
                    bufferedOutputStream2 = bufferedOutputStream;
                    e.printStackTrace();
                    edit.abort();
                    SketchUtils.close(bufferedOutputStream2);
                } catch (DiskLruCache.FileNotExistException e8) {
                    e = e8;
                    bufferedOutputStream2 = bufferedOutputStream;
                    e.printStackTrace();
                    edit.abort();
                    SketchUtils.close(bufferedOutputStream2);
                } catch (Throwable th2) {
                    th = th2;
                    bufferedOutputStream2 = bufferedOutputStream;
                    SketchUtils.close(bufferedOutputStream2);
                    throw th;
                }
            }
        } finally {
            editLock.unlock();
        }
    }

    @NonNull
    public String toString() {
        return "ProcessedImageCache";
    }
}
