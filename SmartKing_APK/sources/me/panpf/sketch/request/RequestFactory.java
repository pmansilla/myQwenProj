package me.panpf.sketch.request;

import android.support.annotation.NonNull;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public class RequestFactory {
    private static final String KEY = "RequestFactory";

    public DisplayRequest newDisplayRequest(Sketch sketch, String str, UriModel uriModel, String str2, DisplayOptions displayOptions, ViewInfo viewInfo, RequestAndViewBinder requestAndViewBinder, DisplayListener displayListener, DownloadProgressListener downloadProgressListener) {
        return new FreeRideDisplayRequest(sketch, str, uriModel, str2, new DisplayOptions(displayOptions), new ViewInfo(viewInfo), requestAndViewBinder, displayListener, downloadProgressListener);
    }

    public DownloadRequest newDownloadRequest(Sketch sketch, String str, UriModel uriModel, String str2, DownloadOptions downloadOptions, DownloadListener downloadListener, DownloadProgressListener downloadProgressListener) {
        return new FreeRideDownloadRequest(sketch, str, uriModel, str2, downloadOptions, downloadListener, downloadProgressListener);
    }

    public LoadRequest newLoadRequest(Sketch sketch, String str, UriModel uriModel, String str2, LoadOptions loadOptions, LoadListener loadListener, DownloadProgressListener downloadProgressListener) {
        return new LoadRequest(sketch, str, uriModel, str2, loadOptions, loadListener, downloadProgressListener);
    }

    @NonNull
    public String toString() {
        return KEY;
    }
}
