package me.panpf.sketch.uri;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class Base64VariantUriModel extends Base64UriModel {
    public static final String SCHEME = "data:img/";

    @Override // me.panpf.sketch.uri.Base64UriModel, me.panpf.sketch.uri.UriModel
    @NonNull
    public String getUriContent(@NonNull String str) {
        return super.getUriContent(str);
    }

    @Override // me.panpf.sketch.uri.Base64UriModel, me.panpf.sketch.uri.UriModel
    protected boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
