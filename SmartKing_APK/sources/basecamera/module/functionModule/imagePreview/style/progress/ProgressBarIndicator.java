package basecamera.module.functionModule.imagePreview.style.progress;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import basecamera.module.functionModule.imagePreview.style.IProgressIndicator;

/* loaded from: classes.dex */
public class ProgressBarIndicator implements IProgressIndicator {
    private SparseArray<ProgressBar> progressBarArray = new SparseArray<>();

    private int dip2Px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void attach(int i, FrameLayout frameLayout) {
        Context context = frameLayout.getContext();
        int dip2Px = dip2Px(context, 50.0f);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dip2Px, dip2Px);
        layoutParams.gravity = 17;
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(layoutParams);
        frameLayout.addView(progressBar, frameLayout.getChildCount());
        this.progressBarArray.put(i, progressBar);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void hideView(int i) {
        ProgressBar progressBar = this.progressBarArray.get(i);
        if (progressBar != null) {
            progressBar.setVisibility(8);
        }
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void onFinish(int i) {
        ViewGroup viewGroup;
        ProgressBar progressBar = this.progressBarArray.get(i);
        if (progressBar == null || (viewGroup = (ViewGroup) progressBar.getParent()) == null) {
            return;
        }
        viewGroup.removeView(progressBar);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void onProgress(int i, int i2) {
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void onStart(int i) {
    }
}
