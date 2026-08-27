package me.panpf.sketch.uri;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import me.panpf.sketch.datasource.ContentDataSource;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.request.DownloadResult;

/* loaded from: classes2.dex */
public class AndroidResUriModel extends UriModel {
    public static final String SCHEME = "android.resource://";

    public static String makeUriById(@NonNull String str, int i) {
        return SCHEME + str + FileUriModel.SCHEME + String.valueOf(i);
    }

    public static String makeUriByName(@NonNull String str, @NonNull String str2, @NonNull String str3) {
        return SCHEME + str + FileUriModel.SCHEME + str2 + FileUriModel.SCHEME + str3;
    }

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public DataSource getDataSource(@NonNull Context context, @NonNull String str, @Nullable DownloadResult downloadResult) {
        return new ContentDataSource(context, Uri.parse(str));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.UriModel
    public boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
