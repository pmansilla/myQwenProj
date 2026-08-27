package com.amap.api.maps.offlinemap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.amap.api.mapcore.util.j;

/* loaded from: classes.dex */
public class DownloadProgressView extends View {
    private String a;
    private int b;
    private int c;
    private float d;
    private float e;
    private TextPaint f;
    private TextPaint g;
    private float h;
    private float i;

    public DownloadProgressView(Context context) {
        super(context);
        this.b = SupportMenu.CATEGORY_MASK;
        this.c = SupportMenu.CATEGORY_MASK;
        this.d = 0.0f;
        this.e = 0.6f;
        a(null, 0);
    }

    public DownloadProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.b = SupportMenu.CATEGORY_MASK;
        this.c = SupportMenu.CATEGORY_MASK;
        this.d = 0.0f;
        this.e = 0.6f;
        a(attributeSet, 0);
    }

    public DownloadProgressView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.b = SupportMenu.CATEGORY_MASK;
        this.c = SupportMenu.CATEGORY_MASK;
        this.d = 0.0f;
        this.e = 0.6f;
        a(attributeSet, i);
    }

    private void a() {
        this.f.setTextSize(this.d);
        this.f.setColor(this.b);
        this.g.setColor(this.c);
        this.h = this.f.measureText(this.a);
        this.i = this.f.getFontMetrics().bottom;
    }

    private void a(AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, j.a.a, i, 0);
        this.a = obtainStyledAttributes.getString(0);
        this.b = obtainStyledAttributes.getColor(3, this.b);
        this.d = obtainStyledAttributes.getDimension(1, this.d);
        this.c = obtainStyledAttributes.getColor(2, this.c);
        obtainStyledAttributes.recycle();
        this.f = new TextPaint();
        this.f.setFlags(1);
        this.f.setTextAlign(Paint.Align.RIGHT);
        this.g = new TextPaint();
        this.g.setStyle(Paint.Style.FILL);
        a();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int width = (getWidth() - paddingLeft) - paddingRight;
        int height = (getHeight() - paddingTop) - paddingBottom;
        double d = height * 0.8f;
        float f = width;
        double d2 = this.e * f;
        Double.isNaN(d);
        String str = String.valueOf((int) (this.e * 100.0f)) + "%";
        canvas.drawRect(new Rect(0, (int) d, (int) (f * this.e), height), this.g);
        canvas.drawText(str, (int) d2, (int) (d - 3.0d), this.f);
    }

    public void setProgress(int i) {
        if (i > 100 || i < 0) {
            return;
        }
        this.e = i / 100.0f;
        invalidate();
    }
}
