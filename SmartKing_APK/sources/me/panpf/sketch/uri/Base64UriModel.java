package me.panpf.sketch.uri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class Base64UriModel extends AbsStreamDiskCacheUriModel {
    public static final String SCHEME = "data:image/";

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // me.panpf.sketch.uri.AbsDiskCacheUriModel
    @NonNull
    public InputStream getContent(@NonNull Context context, @NonNull String str) throws GetDataSourceException {
        return new ByteArrayInputStream(Base64.decode(getUriContent(str), 0));
    }

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public String getDiskCacheKey(@NonNull String str) {
        return getUriContent(str);
    }

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public String getUriContent(@NonNull String str) {
        return !TextUtils.isEmpty(str) ? str.substring(str.indexOf(";") + ";base64,".length()) : str;
    }

    @Override // me.panpf.sketch.uri.UriModel
    public boolean isConvertShortUriForKey() {
        return true;
    }

    @Override // me.panpf.sketch.uri.UriModel
    protected boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
