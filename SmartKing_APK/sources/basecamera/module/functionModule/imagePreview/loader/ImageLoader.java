package basecamera.module.functionModule.imagePreview.loader;

import android.graphics.drawable.Drawable;
import android.support.annotation.UiThread;
import android.widget.ImageView;

/* loaded from: classes.dex */
public interface ImageLoader {
    public static final int STATUS_DISPLAY_CANCEL = -1;
    public static final int STATUS_DISPLAY_FAILED = 0;
    public static final int STATUS_DISPLAY_SUCCESS = 1;

    /* loaded from: classes.dex */
    public interface SourceCallback {
        @UiThread
        void onDelivered(int i);

        @UiThread
        void onFinish();

        @UiThread
        void onProgress(int i);

        @UiThread
        void onStart();
    }

    /* loaded from: classes.dex */
    public interface ThumbnailCallback {
        @UiThread
        void onFinish(Drawable drawable);
    }

    void clearCache();

    boolean isLoaded(String str);

    void loadImageAsync(String str, ThumbnailCallback thumbnailCallback);

    Drawable loadImageSync(String str);

    void showImage(String str, ImageView imageView, Drawable drawable, SourceCallback sourceCallback);
}
