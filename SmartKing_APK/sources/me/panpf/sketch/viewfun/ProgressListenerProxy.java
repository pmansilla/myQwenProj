package me.panpf.sketch.viewfun;

import java.lang.ref.WeakReference;
import me.panpf.sketch.request.DownloadProgressListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ProgressListenerProxy implements DownloadProgressListener {
    private WeakReference<FunctionCallbackView> viewWeakReference;

    public ProgressListenerProxy(FunctionCallbackView functionCallbackView) {
        this.viewWeakReference = new WeakReference<>(functionCallbackView);
    }

    @Override // me.panpf.sketch.request.DownloadProgressListener
    public void onUpdateDownloadProgress(int i, int i2) {
        FunctionCallbackView functionCallbackView = this.viewWeakReference.get();
        if (functionCallbackView == null) {
            return;
        }
        if (functionCallbackView.getFunctions().onUpdateDownloadProgress(i, i2)) {
            functionCallbackView.invalidate();
        }
        if (functionCallbackView.wrappedProgressListener != null) {
            functionCallbackView.wrappedProgressListener.onUpdateDownloadProgress(i, i2);
        }
    }
}
