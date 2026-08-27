package me.panpf.sketch.util;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;

/* loaded from: classes2.dex */
public class DrawableWrapper extends Drawable implements Drawable.Callback {
    private Drawable wrappedDrawable;

    public DrawableWrapper(Drawable drawable) {
        setWrappedDrawable(drawable);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.wrappedDrawable != null) {
            this.wrappedDrawable.draw(canvas);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return this.wrappedDrawable != null ? this.wrappedDrawable.getChangingConfigurations() : super.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable getCurrent() {
        return this.wrappedDrawable != null ? this.wrappedDrawable.getCurrent() : super.getCurrent();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.wrappedDrawable != null ? this.wrappedDrawable.getIntrinsicHeight() : super.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.wrappedDrawable != null ? this.wrappedDrawable.getIntrinsicWidth() : super.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return this.wrappedDrawable != null ? this.wrappedDrawable.getMinimumHeight() : super.getMinimumHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return this.wrappedDrawable != null ? this.wrappedDrawable.getMinimumWidth() : super.getMinimumWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        if (this.wrappedDrawable != null) {
            return this.wrappedDrawable.getOpacity();
        }
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        return this.wrappedDrawable != null ? this.wrappedDrawable.getPadding(rect) : super.getPadding(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public int[] getState() {
        return this.wrappedDrawable != null ? this.wrappedDrawable.getState() : super.getState();
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        return this.wrappedDrawable != null ? this.wrappedDrawable.getTransparentRegion() : super.getTransparentRegion();
    }

    public Drawable getWrappedDrawable() {
        return this.wrappedDrawable;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return Build.VERSION.SDK_INT >= 19 && this.wrappedDrawable != null && this.wrappedDrawable.isAutoMirrored();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return this.wrappedDrawable != null ? this.wrappedDrawable.isStateful() : super.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        if (Build.VERSION.SDK_INT < 11 || this.wrappedDrawable == null) {
            return;
        }
        this.wrappedDrawable.jumpToCurrentState();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        if (this.wrappedDrawable != null) {
            this.wrappedDrawable.setBounds(rect);
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i) {
        return this.wrappedDrawable != null ? this.wrappedDrawable.setLevel(i) : super.onLevelChange(i);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        scheduleSelf(runnable, j);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (this.wrappedDrawable != null) {
            this.wrappedDrawable.setAlpha(i);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean z) {
        if (Build.VERSION.SDK_INT < 19 || this.wrappedDrawable == null) {
            return;
        }
        this.wrappedDrawable.setAutoMirrored(z);
    }

    @Override // android.graphics.drawable.Drawable
    public void setChangingConfigurations(int i) {
        if (this.wrappedDrawable != null) {
            this.wrappedDrawable.setChangingConfigurations(i);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        if (this.wrappedDrawable != null) {
            this.wrappedDrawable.setColorFilter(colorFilter);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z) {
        if (this.wrappedDrawable != null) {
            this.wrappedDrawable.setDither(z);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean z) {
        if (this.wrappedDrawable != null) {
            this.wrappedDrawable.setFilterBitmap(z);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float f, float f2) {
        if (Build.VERSION.SDK_INT < 21 || this.wrappedDrawable == null) {
            return;
        }
        this.wrappedDrawable.setHotspot(f, f2);
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspotBounds(int i, int i2, int i3, int i4) {
        if (Build.VERSION.SDK_INT < 21 || this.wrappedDrawable == null) {
            return;
        }
        this.wrappedDrawable.setHotspotBounds(i, i2, i3, i4);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setState(int[] iArr) {
        return this.wrappedDrawable != null ? this.wrappedDrawable.setState(iArr) : super.setState(iArr);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTint(int i) {
        if (Build.VERSION.SDK_INT < 21 || this.wrappedDrawable == null) {
            return;
        }
        this.wrappedDrawable.setTint(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        if (Build.VERSION.SDK_INT < 21 || this.wrappedDrawable == null) {
            return;
        }
        this.wrappedDrawable.setTintList(colorStateList);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        if (Build.VERSION.SDK_INT < 21 || this.wrappedDrawable == null) {
            return;
        }
        this.wrappedDrawable.setTintMode(mode);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z, boolean z2) {
        return super.setVisible(z, z2) || (this.wrappedDrawable != null && this.wrappedDrawable.setVisible(z, z2));
    }

    public void setWrappedDrawable(Drawable drawable) {
        if (drawable == this) {
            return;
        }
        if (this.wrappedDrawable != null) {
            this.wrappedDrawable.setCallback(null);
        }
        this.wrappedDrawable = drawable;
        if (this.wrappedDrawable != null) {
            this.wrappedDrawable.setCallback(this);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }
}
