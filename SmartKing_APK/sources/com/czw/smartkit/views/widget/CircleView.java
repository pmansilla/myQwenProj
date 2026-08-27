package com.czw.smartkit.views.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
public class CircleView extends ImageView {
    private float radiusX;
    private float radiusY;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CircleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void _init_wh_() {
        this.radiusX = getWidth() / 2;
        this.radiusY = getHeight() / 2;
    }

    private void init() {
        this.radiusX = 58.0f;
        this.radiusY = 58.0f;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        _init_wh_();
        Path path = new Path();
        int width = getWidth();
        int height = getHeight();
        if (width > height) {
            width = height;
        }
        float f = width;
        float f2 = width / 2;
        path.addRoundRect(new RectF(0.0f, 0.0f, f, f), f2, f2, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.INTERSECT);
        super.onDraw(canvas);
    }
}
