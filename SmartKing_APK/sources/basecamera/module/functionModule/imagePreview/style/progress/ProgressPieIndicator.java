package basecamera.module.functionModule.imagePreview.style.progress;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import basecamera.module.functionModule.imagePreview.style.IProgressIndicator;
import com.filippudak.ProgressPieView.ProgressPieView;
import java.util.Locale;

/* loaded from: classes.dex */
public class ProgressPieIndicator implements IProgressIndicator {
    private SparseArray<ProgressPieView> progressPieArray = new SparseArray<>();

    private int dip2Px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void attach(int i, FrameLayout frameLayout) {
        Context context = frameLayout.getContext();
        int dip2Px = dip2Px(context, 50.0f);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dip2Px, dip2Px);
        layoutParams.gravity = 17;
        ProgressPieView progressPieView = new ProgressPieView(context);
        progressPieView.setTextSize(13);
        progressPieView.setStrokeWidth(1);
        progressPieView.setTextColor(-1);
        progressPieView.setProgressFillType(0);
        progressPieView.setBackgroundColor(0);
        progressPieView.setProgressColor(Color.parseColor("#BBFFFFFF"));
        progressPieView.setStrokeColor(-1);
        progressPieView.setLayoutParams(layoutParams);
        frameLayout.addView((View) progressPieView, frameLayout.getChildCount());
        this.progressPieArray.put(i, progressPieView);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void hideView(int i) {
        ProgressPieView progressPieView = this.progressPieArray.get(i);
        if (progressPieView != null) {
            progressPieView.setVisibility(8);
        }
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void onFinish(int i) {
        ViewGroup viewGroup;
        View view = (ProgressPieView) this.progressPieArray.get(i);
        if (view == null || (viewGroup = (ViewGroup) view.getParent()) == null) {
            return;
        }
        viewGroup.removeView(view);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void onProgress(int i, int i2) {
        if (i2 < 0 || i2 > 100) {
            return;
        }
        ProgressPieView progressPieView = this.progressPieArray.get(i);
        progressPieView.setProgress(i2);
        progressPieView.setText(String.format(Locale.getDefault(), "%d%%", Integer.valueOf(i2)));
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IProgressIndicator
    public void onStart(int i) {
        ProgressPieView progressPieView = this.progressPieArray.get(i);
        progressPieView.setProgress(0);
        progressPieView.setText(String.format(Locale.getDefault(), "%d%%", 0));
    }
}
