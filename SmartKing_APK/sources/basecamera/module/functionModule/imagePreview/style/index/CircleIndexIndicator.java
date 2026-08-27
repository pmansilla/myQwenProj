package basecamera.module.functionModule.imagePreview.style.index;

import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import basecamera.module.functionModule.imagePreview.style.IIndexIndicator;
import basecamera.module.functionModule.imagePreview.view.indicator.CircleIndicator;

/* loaded from: classes.dex */
public class CircleIndexIndicator implements IIndexIndicator {
    private CircleIndicator circleIndicator;

    @Override // basecamera.module.functionModule.imagePreview.style.IIndexIndicator
    public void attach(FrameLayout frameLayout) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, 48);
        layoutParams.gravity = 81;
        layoutParams.bottomMargin = 10;
        this.circleIndicator = new CircleIndicator(frameLayout.getContext());
        this.circleIndicator.setGravity(16);
        this.circleIndicator.setLayoutParams(layoutParams);
        frameLayout.addView(this.circleIndicator);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IIndexIndicator
    public void onHide() {
        if (this.circleIndicator == null) {
            return;
        }
        this.circleIndicator.setVisibility(8);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IIndexIndicator
    public void onRemove() {
        ViewGroup viewGroup;
        if (this.circleIndicator == null || (viewGroup = (ViewGroup) this.circleIndicator.getParent()) == null) {
            return;
        }
        viewGroup.removeView(this.circleIndicator);
    }

    @Override // basecamera.module.functionModule.imagePreview.style.IIndexIndicator
    public void onShow(ViewPager viewPager) {
        this.circleIndicator.setVisibility(0);
        this.circleIndicator.setViewPager(viewPager);
    }
}
