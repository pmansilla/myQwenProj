package com.czw.smartkit.views;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
abstract class BaseView extends View {
    private boolean hasMeasure;
    protected int height;
    protected Paint paint;
    protected float textUnit;
    protected float viewUnit;
    protected int width;

    public BaseView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.viewUnit = 1.0f;
        this.textUnit = 1.0f;
        this.paint = new Paint();
        this.hasMeasure = false;
    }

    public void afterMeasure() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void init(Context context) {
        this.viewUnit = context.getResources().getDimension(R.dimen.DIMEN_2PX);
        this.textUnit = context.getResources().getDimensionPixelSize(R.dimen.DIMEN_2PX);
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.viewUnit * 1.5f);
        this.paint.setAntiAlias(true);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.width = getMeasuredWidth();
        this.height = getMeasuredHeight();
        if (!this.hasMeasure) {
            init(getContext());
            this.hasMeasure = true;
        }
        afterMeasure();
    }
}
