package cn.sharesdk.onekeyshare.themes.classic;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class RotateImageView extends ImageView {
    private float rotation;

    public RotateImageView(Context context) {
        super(context);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.rotate(this.rotation, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public void setRotation(float f) {
        this.rotation = f;
        invalidate();
    }
}
