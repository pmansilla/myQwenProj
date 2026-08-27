package me.panpf.sketch;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.DisplayHelper;
import me.panpf.sketch.request.DisplayRequest;
import me.panpf.sketch.request.DownloadHelper;
import me.panpf.sketch.request.DownloadListener;
import me.panpf.sketch.request.LoadHelper;
import me.panpf.sketch.request.LoadListener;
import me.panpf.sketch.uri.AssetUriModel;
import me.panpf.sketch.uri.DrawableUriModel;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class Sketch {
    public static final String META_DATA_KEY_INITIALIZER = "SKETCH_INITIALIZER";
    private static volatile Sketch instance;
    private Configuration configuration;

    private Sketch(@NonNull Context context) {
        this.configuration = new Configuration(context);
    }

    public static boolean cancel(@NonNull SketchView sketchView) {
        DisplayRequest findDisplayRequest = SketchUtils.findDisplayRequest(sketchView);
        if (findDisplayRequest == null || findDisplayRequest.isFinished()) {
            return false;
        }
        findDisplayRequest.cancel(CancelCause.BE_CANCELLED);
        return true;
    }

    @NonNull
    public static Sketch with(@NonNull Context context) {
        if (instance == null) {
            synchronized (Sketch.class) {
                if (instance == null) {
                    Sketch sketch = new Sketch(context);
                    SLog.i(null, "Version %s %s(%d) -> %s", "release", BuildConfig.VERSION_NAME, Integer.valueOf(BuildConfig.VERSION_CODE), sketch.configuration.toString());
                    Initializer findInitializer = SketchUtils.findInitializer(context);
                    if (findInitializer != null) {
                        findInitializer.onInitialize(context.getApplicationContext(), sketch.configuration);
                    }
                    instance = sketch;
                }
            }
        }
        return instance;
    }

    @NonNull
    public DisplayHelper display(@NonNull String str, @NonNull SketchView sketchView) {
        return this.configuration.getHelperFactory().getDisplayHelper(this, str, sketchView);
    }

    @NonNull
    public DisplayHelper displayFromAsset(@NonNull String str, @NonNull SketchView sketchView) {
        return this.configuration.getHelperFactory().getDisplayHelper(this, AssetUriModel.makeUri(str), sketchView);
    }

    @NonNull
    public DisplayHelper displayFromContent(@NonNull String str, @NonNull SketchView sketchView) {
        return this.configuration.getHelperFactory().getDisplayHelper(this, str, sketchView);
    }

    @NonNull
    public DisplayHelper displayFromResource(@DrawableRes int i, @NonNull SketchView sketchView) {
        return this.configuration.getHelperFactory().getDisplayHelper(this, DrawableUriModel.makeUri(i), sketchView);
    }

    @NonNull
    public DownloadHelper download(@NonNull String str, @Nullable DownloadListener downloadListener) {
        return this.configuration.getHelperFactory().getDownloadHelper(this, str, downloadListener);
    }

    @NonNull
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @NonNull
    public LoadHelper load(@NonNull String str, @Nullable LoadListener loadListener) {
        return this.configuration.getHelperFactory().getLoadHelper(this, str, loadListener);
    }

    @NonNull
    public LoadHelper loadFromAsset(@NonNull String str, @Nullable LoadListener loadListener) {
        return this.configuration.getHelperFactory().getLoadHelper(this, AssetUriModel.makeUri(str), loadListener);
    }

    @NonNull
    public LoadHelper loadFromContent(@NonNull String str, @Nullable LoadListener loadListener) {
        return this.configuration.getHelperFactory().getLoadHelper(this, str, loadListener);
    }

    @NonNull
    public LoadHelper loadFromResource(@DrawableRes int i, @Nullable LoadListener loadListener) {
        return this.configuration.getHelperFactory().getLoadHelper(this, DrawableUriModel.makeUri(i), loadListener);
    }

    @Keep
    public void onLowMemory() {
        if (Build.VERSION.SDK_INT < 14 || SketchUtils.invokeIn(new Exception().getStackTrace(), Application.class, "onLowMemory")) {
            SLog.w(null, "Memory is very low, clean memory cache and bitmap pool");
            this.configuration.getMemoryCache().clear();
            this.configuration.getBitmapPool().clear();
        }
    }

    @Keep
    public void onTrimMemory(int i) {
        if (Build.VERSION.SDK_INT < 14 || SketchUtils.invokeIn(new Exception().getStackTrace(), Application.class, "onTrimMemory")) {
            SLog.w((String) null, "Trim of memory, level= %s", SketchUtils.getTrimLevelName(i));
            this.configuration.getMemoryCache().trimMemory(i);
            this.configuration.getBitmapPool().trimMemory(i);
        }
    }
}
