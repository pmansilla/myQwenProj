package me.panpf.sketch.uri;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import me.panpf.sketch.SLog;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.datasource.DrawableDataSource;
import me.panpf.sketch.request.DownloadResult;

/* loaded from: classes2.dex */
public class DrawableUriModel extends UriModel {
    private static final String NAME = "DrawableUriModel";
    public static final String SCHEME = "drawable://";

    public static String makeUri(@DrawableRes int i) {
        return SCHEME + String.valueOf(i);
    }

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public DataSource getDataSource(@NonNull Context context, @NonNull String str, DownloadResult downloadResult) throws GetDataSourceException {
        try {
            return new DrawableDataSource(context, Integer.valueOf(getUriContent(str)).intValue());
        } catch (NumberFormatException e) {
            String format = String.format("Conversion resId failed. %s", str);
            SLog.e(NAME, e, format);
            throw new GetDataSourceException(format, e);
        }
    }

    public int getResId(String str) {
        return Integer.parseInt(getUriContent(str));
    }

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public String getUriContent(@NonNull String str) {
        return match(str) ? str.substring(SCHEME.length()) : str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.UriModel
    public boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
