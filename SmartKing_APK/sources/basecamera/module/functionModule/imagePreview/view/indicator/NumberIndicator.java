package basecamera.module.functionModule.imagePreview.view.indicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.Locale;

/* loaded from: classes.dex */
public class NumberIndicator extends TextView {
    private static final String STR_NUM_FORMAT = "%s/%s";
    private final ViewPager.OnPageChangeListener mInternalPageChangeListener;
    private ViewPager mViewPager;

    public NumberIndicator(Context context) {
        this(context, null);
    }

    public NumberIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NumberIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mInternalPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: basecamera.module.functionModule.imagePreview.view.indicator.NumberIndicator.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i3) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                if (NumberIndicator.this.mViewPager.getAdapter() == null || NumberIndicator.this.mViewPager.getAdapter().getCount() <= 0) {
                    return;
                }
                NumberIndicator.this.setText(String.format(Locale.getDefault(), NumberIndicator.STR_NUM_FORMAT, Integer.valueOf(i2 + 1), Integer.valueOf(NumberIndicator.this.mViewPager.getAdapter().getCount())));
            }
        };
        initNumberIndicator();
    }

    private void initNumberIndicator() {
        setTextColor(-1);
        setTextSize(18.0f);
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager == null || viewPager.getAdapter() == null) {
            return;
        }
        this.mViewPager = viewPager;
        this.mViewPager.removeOnPageChangeListener(this.mInternalPageChangeListener);
        this.mViewPager.addOnPageChangeListener(this.mInternalPageChangeListener);
        this.mInternalPageChangeListener.onPageSelected(this.mViewPager.getCurrentItem());
    }
}
