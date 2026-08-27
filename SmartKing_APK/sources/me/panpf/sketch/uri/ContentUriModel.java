package me.panpf.sketch.uri;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import me.panpf.sketch.datasource.ContentDataSource;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.request.DownloadResult;

/* loaded from: classes2.dex */
public class ContentUriModel extends UriModel {
    public static final String SCHEME = "content://";

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public DataSource getDataSource(@NonNull Context context, @NonNull String str, DownloadResult downloadResult) {
        return new ContentDataSource(context, Uri.parse(str));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.UriModel
    public boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
