package basecamera.module.functionModule.imagePreview.style;

import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public interface IIndexIndicator {
    void attach(FrameLayout frameLayout);

    void onHide();

    void onRemove();

    void onShow(ViewPager viewPager);
}
