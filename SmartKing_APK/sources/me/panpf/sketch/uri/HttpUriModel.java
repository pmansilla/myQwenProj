package me.panpf.sketch.uri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.datasource.ByteArrayDataSource;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.datasource.DiskCacheDataSource;
import me.panpf.sketch.request.DownloadResult;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public class HttpUriModel extends UriModel {
    private static final String NAME = "HttpUriModel";
    public static final String SCHEME = "http://";

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public DataSource getDataSource(@NonNull Context context, @NonNull String str, DownloadResult downloadResult) throws GetDataSourceException {
        if (downloadResult == null) {
            DiskCache.Entry entry = Sketch.with(context).getConfiguration().getDiskCache().get(getDiskCacheKey(str));
            if (entry != null) {
                return new DiskCacheDataSource(entry, ImageFrom.DISK_CACHE);
            }
            String format = String.format("Not found disk cache. %s", str);
            SLog.e(NAME, format);
            throw new GetDataSourceException(format);
        }
        DiskCache.Entry diskCacheEntry = downloadResult.getDiskCacheEntry();
        if (diskCacheEntry != null) {
            return new DiskCacheDataSource(diskCacheEntry, downloadResult.getImageFrom());
        }
        byte[] imageData = downloadResult.getImageData();
        if (imageData != null && imageData.length > 0) {
            return new ByteArrayDataSource(imageData, downloadResult.getImageFrom());
        }
        String format2 = String.format("Not found data from download result. %s", str);
        SLog.e(NAME, format2);
        throw new GetDataSourceException(format2);
    }

    @Override // me.panpf.sketch.uri.UriModel
    public boolean isFromNet() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.UriModel
    public boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
