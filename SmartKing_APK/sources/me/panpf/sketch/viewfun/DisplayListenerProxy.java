package me.panpf.sketch.viewfun;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.DisplayListener;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.ImageFrom;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class DisplayListenerProxy implements DisplayListener {
    private WeakReference<FunctionCallbackView> viewWeakReference;

    public DisplayListenerProxy(FunctionCallbackView functionCallbackView) {
        this.viewWeakReference = new WeakReference<>(functionCallbackView);
    }

    @Override // me.panpf.sketch.request.Listener
    public void onCanceled(@NonNull CancelCause cancelCause) {
        FunctionCallbackView functionCallbackView = this.viewWeakReference.get();
        if (functionCallbackView == null) {
            return;
        }
        if (functionCallbackView.getFunctions().onDisplayCanceled(cancelCause)) {
            functionCallbackView.invalidate();
        }
        if (functionCallbackView.wrappedDisplayListener != null) {
            functionCallbackView.wrappedDisplayListener.onCanceled(cancelCause);
        }
    }

    @Override // me.panpf.sketch.request.DisplayListener
    public void onCompleted(@NonNull Drawable drawable, @NonNull ImageFrom imageFrom, @NonNull ImageAttrs imageAttrs) {
        FunctionCallbackView functionCallbackView = this.viewWeakReference.get();
        if (functionCallbackView == null) {
            return;
        }
        if (functionCallbackView.getFunctions().onDisplayCompleted(drawable, imageFrom, imageAttrs)) {
            functionCallbackView.invalidate();
        }
        if (functionCallbackView.wrappedDisplayListener != null) {
            functionCallbackView.wrappedDisplayListener.onCompleted(drawable, imageFrom, imageAttrs);
        }
    }

    @Override // me.panpf.sketch.request.Listener
    public void onError(@NonNull ErrorCause errorCause) {
        FunctionCallbackView functionCallbackView = this.viewWeakReference.get();
        if (functionCallbackView == null) {
            return;
        }
        if (functionCallbackView.getFunctions().onDisplayError(errorCause)) {
            functionCallbackView.invalidate();
        }
        if (functionCallbackView.wrappedDisplayListener != null) {
            functionCallbackView.wrappedDisplayListener.onError(errorCause);
        }
    }

    @Override // me.panpf.sketch.request.DisplayListener, me.panpf.sketch.request.Listener
    public void onStarted() {
        FunctionCallbackView functionCallbackView = this.viewWeakReference.get();
        if (functionCallbackView == null) {
            return;
        }
        if (functionCallbackView.getFunctions().onDisplayStarted()) {
            functionCallbackView.invalidate();
        }
        if (functionCallbackView.wrappedDisplayListener != null) {
            functionCallbackView.wrappedDisplayListener.onStarted();
        }
    }
}
