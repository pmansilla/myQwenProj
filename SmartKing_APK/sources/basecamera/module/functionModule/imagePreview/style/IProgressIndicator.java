package basecamera.module.functionModule.imagePreview.style;

import android.widget.FrameLayout;

/* loaded from: classes.dex */
public interface IProgressIndicator {
    void attach(int i, FrameLayout frameLayout);

    void hideView(int i);

    void onFinish(int i);

    void onProgress(int i, int i2);

    void onStart(int i);
}
