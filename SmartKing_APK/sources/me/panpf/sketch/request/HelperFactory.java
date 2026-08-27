package me.panpf.sketch.request;

import android.support.annotation.NonNull;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.SketchView;

/* loaded from: classes2.dex */
public class HelperFactory {
    private static final String KEY = "HelperFactory";
    private DisplayHelper cacheDisplayHelper;

    public DisplayHelper getDisplayHelper(@NonNull Sketch sketch, String str, SketchView sketchView) {
        if (this.cacheDisplayHelper == null) {
            this.cacheDisplayHelper = new DisplayHelper();
        }
        DisplayHelper displayHelper = this.cacheDisplayHelper;
        this.cacheDisplayHelper = null;
        displayHelper.init(sketch, str, sketchView);
        return displayHelper;
    }

    public DownloadHelper getDownloadHelper(@NonNull Sketch sketch, String str, DownloadListener downloadListener) {
        return new DownloadHelper(sketch, str, downloadListener);
    }

    public LoadHelper getLoadHelper(@NonNull Sketch sketch, String str, LoadListener loadListener) {
        return new LoadHelper(sketch, str, loadListener);
    }

    public void recycleDisplayHelper(@NonNull DisplayHelper displayHelper) {
        displayHelper.reset();
        if (this.cacheDisplayHelper == null) {
            this.cacheDisplayHelper = displayHelper;
        }
    }

    @NonNull
    public String toString() {
        return KEY;
    }
}
