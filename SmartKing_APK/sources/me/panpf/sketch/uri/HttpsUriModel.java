package me.panpf.sketch.uri;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class HttpsUriModel extends HttpUriModel {
    public static final String SCHEME = "https://";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.HttpUriModel, me.panpf.sketch.uri.UriModel
    public boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
