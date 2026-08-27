package me.panpf.sketch.request;

import java.lang.ref.WeakReference;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class RequestAndViewBinder {
    private DisplayRequest displayRequest;
    private WeakReference<SketchView> imageViewReference;

    public RequestAndViewBinder(SketchView sketchView) {
        this.imageViewReference = new WeakReference<>(sketchView);
    }

    public SketchView getView() {
        SketchView sketchView = this.imageViewReference.get();
        if (this.displayRequest == null) {
            return sketchView;
        }
        DisplayRequest findDisplayRequest = SketchUtils.findDisplayRequest(sketchView);
        if (findDisplayRequest == null || findDisplayRequest != this.displayRequest) {
            return null;
        }
        return sketchView;
    }

    public boolean isBroken() {
        return getView() == null;
    }

    public void setDisplayRequest(DisplayRequest displayRequest) {
        this.displayRequest = displayRequest;
    }
}
