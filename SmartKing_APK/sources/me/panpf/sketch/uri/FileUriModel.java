package me.panpf.sketch.uri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.io.File;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.datasource.FileDataSource;
import me.panpf.sketch.request.DownloadResult;

/* loaded from: classes2.dex */
public class FileUriModel extends UriModel {
    public static final String SCHEME = "/";

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public DataSource getDataSource(@NonNull Context context, @NonNull String str, DownloadResult downloadResult) {
        return new FileDataSource(new File(str));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.UriModel
    public boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
