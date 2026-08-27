package com.czw.smartkit.views;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes.dex */
public class SquareView extends View {
    protected float height;
    protected Paint paint;
    protected float width;

    public SquareView(Context context) {
        super(context);
        init(context);
    }

    public SquareView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public SquareView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void init(Context context) {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            this.width = getWidth();
            this.height = getHeight();
            if (this.width <= this.height) {
                this.height = this.width;
            } else {
                this.width = this.height;
            }
        }
    }
}
