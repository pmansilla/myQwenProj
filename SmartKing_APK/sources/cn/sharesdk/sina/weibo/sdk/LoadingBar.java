package cn.sharesdk.sina.weibo.sdk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes.dex */
public class LoadingBar extends TextView {
    private int a;
    private int b;
    private Paint c;
    private Handler d;
    private Runnable e;

    public LoadingBar(Context context) {
        super(context);
        this.e = new Runnable() { // from class: cn.sharesdk.sina.weibo.sdk.LoadingBar.1
            @Override // java.lang.Runnable
            public void run() {
                LoadingBar.this.a++;
                LoadingBar.this.a(LoadingBar.this.a);
            }
        };
        a(context);
    }

    public LoadingBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a(context);
    }

    public LoadingBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        a(context);
    }

    private void a(Context context) {
        this.d = new Handler();
        this.c = new Paint();
        a();
    }

    private Rect b() {
        int left = getLeft();
        int top = getTop();
        return new Rect(0, 0, (getLeft() + (((getRight() - getLeft()) * this.a) / 100)) - left, getBottom() - top);
    }

    public void a() {
        this.b = -11693826;
    }

    public void a(int i) {
        if (i < 7) {
            this.d.postDelayed(this.e, 70L);
        } else {
            this.d.removeCallbacks(this.e);
            this.a = i;
        }
        invalidate();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.c.setColor(this.b);
        canvas.drawRect(b(), this.c);
    }
}
