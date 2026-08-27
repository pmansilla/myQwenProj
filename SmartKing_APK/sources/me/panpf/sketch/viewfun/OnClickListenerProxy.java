package me.panpf.sketch.viewfun;

import android.view.View;
import java.lang.ref.WeakReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class OnClickListenerProxy implements View.OnClickListener {
    private WeakReference<FunctionCallbackView> viewWeakReference;

    public OnClickListenerProxy(FunctionCallbackView functionCallbackView) {
        this.viewWeakReference = new WeakReference<>(functionCallbackView);
    }

    public boolean isClickable() {
        FunctionCallbackView functionCallbackView = this.viewWeakReference.get();
        if (functionCallbackView == null) {
            return false;
        }
        if (functionCallbackView.getFunctions().clickRetryFunction == null || !functionCallbackView.getFunctions().clickRetryFunction.isClickable()) {
            return (functionCallbackView.getFunctions().clickPlayGifFunction != null && functionCallbackView.getFunctions().clickPlayGifFunction.isClickable()) || functionCallbackView.wrappedClickListener != null;
        }
        return true;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        FunctionCallbackView functionCallbackView = this.viewWeakReference.get();
        if (functionCallbackView == null) {
            return;
        }
        if (functionCallbackView.getFunctions().clickRetryFunction == null || !functionCallbackView.getFunctions().clickRetryFunction.onClick(view)) {
            if ((functionCallbackView.getFunctions().clickPlayGifFunction == null || !functionCallbackView.getFunctions().clickPlayGifFunction.onClick(view)) && functionCallbackView.wrappedClickListener != null) {
                functionCallbackView.wrappedClickListener.onClick(view);
            }
        }
    }
}
