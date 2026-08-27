package me.panpf.sketch.uri;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class UriModelManager {
    private List<UriModel> uriModelList = new LinkedList();

    public UriModelManager() {
        this.uriModelList.add(new HttpUriModel());
        this.uriModelList.add(new HttpsUriModel());
        this.uriModelList.add(new FileUriModel());
        this.uriModelList.add(new FileVariantUriModel());
        this.uriModelList.add(new AssetUriModel());
        this.uriModelList.add(new DrawableUriModel());
        this.uriModelList.add(new ContentUriModel());
        this.uriModelList.add(new AndroidResUriModel());
        this.uriModelList.add(new ApkIconUriModel());
        this.uriModelList.add(new AppIconUriModel());
        this.uriModelList.add(new Base64UriModel());
        this.uriModelList.add(new Base64VariantUriModel());
    }

    @NonNull
    public UriModelManager add(int i, @NonNull UriModel uriModel) {
        if (uriModel != null) {
            this.uriModelList.add(i, uriModel);
        }
        return this;
    }

    @NonNull
    public UriModelManager add(@NonNull UriModel uriModel) {
        if (uriModel != null) {
            this.uriModelList.add(uriModel);
        }
        return this;
    }

    @Nullable
    public UriModel match(@NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (UriModel uriModel : this.uriModelList) {
            if (uriModel.match(str)) {
                return uriModel;
            }
        }
        return null;
    }

    public boolean remove(@NonNull UriModel uriModel) {
        return uriModel != null && this.uriModelList.remove(uriModel);
    }

    @NonNull
    public String toString() {
        return "UriModelManager";
    }
}
