package basecamera.module.functionModule.imagePreview.style.index;

import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import basecamera.module.functionModule.imagePreview.style.IIndexIndicator;
import basecamera.module.functionModule.imagePreview.view.indicator.NumberIndicator;

/* loaded from: classes.dex */
public class NumberIndexIndicator implements IIndexIndicator {
    private NumberIndicator numberIndicator;

    @Override // basecamera.module.functionModule.imagePreview.style.IIndexIndicator
    public void attach(FrameLayout frameLayout) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 49;
        layoutParams.topMargin = 30;
        this.numberIndicator = new NumberIndicator(frameLayout.getContext());
        this.numberIndicator.setTextColor(-6908266);
        this.numberIndicator.setLayoutParams(layoutParams);
        frameLayout.addView(this.numberIndicator);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IIndexIndicator
    public void onHide() {
        if (this.numberIndicator == null) {
            return;
        }
        this.numberIndicator.setVisibility(8);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IIndexIndicator
    public void onRemove() {
        ViewGroup viewGroup;
        if (this.numberIndicator == null || (viewGroup = (ViewGroup) this.numberIndicator.getParent()) == null) {
            return;
        }
        viewGroup.removeView(this.numberIndicator);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IIndexIndicator
    public void onShow(ViewPager viewPager) {
        this.numberIndicator.setVisibility(0);
        this.numberIndicator.setViewPager(viewPager);
    }
}
