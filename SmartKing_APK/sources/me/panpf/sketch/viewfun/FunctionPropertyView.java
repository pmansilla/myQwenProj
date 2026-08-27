package me.panpf.sketch.viewfun;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.shaper.ImageShaper;
import me.panpf.sketch.zoom.ImageZoomer;

/* loaded from: classes2.dex */
public abstract class FunctionPropertyView extends FunctionCallbackView {
    public FunctionPropertyView(Context context) {
        super(context);
    }

    public FunctionPropertyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FunctionPropertyView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Nullable
    public ImageFrom getImageFrom() {
        if (getFunctions().showImageFromFunction != null) {
            return getFunctions().showImageFromFunction.getImageFrom();
        }
        return null;
    }

    @Nullable
    public ImageZoomer getZoomer() {
        if (getFunctions().zoomFunction != null) {
            return getFunctions().zoomFunction.getZoomer();
        }
        return null;
    }

    public boolean isClickPlayGifEnabled() {
        return getFunctions().clickPlayGifFunction != null;
    }

    public boolean isClickRetryOnDisplayErrorEnabled() {
        return getFunctions().clickRetryFunction != null && getFunctions().clickRetryFunction.isClickRetryOnDisplayErrorEnabled();
    }

    public boolean isClickRetryOnPauseDownloadEnabled() {
        return getFunctions().clickRetryFunction != null && getFunctions().clickRetryFunction.isClickRetryOnPauseDownloadEnabled();
    }

    public boolean isShowDownloadProgressEnabled() {
        return getFunctions().showDownloadProgressFunction != null;
    }

    public boolean isShowGifFlagEnabled() {
        return getFunctions().showGifFlagFunction != null;
    }

    public boolean isShowImageFromEnabled() {
        return getFunctions().showImageFromFunction != null;
    }

    public boolean isShowPressedStatusEnabled() {
        return getFunctions().showPressedFunction != null;
    }

    @Override // me.panpf.sketch.SketchView
    public boolean isZoomEnabled() {
        return getFunctions().zoomFunction != null;
    }

    public void setClickPlayGifEnabled(@DrawableRes int i) {
        setClickPlayGifEnabled(i > 0 ? getResources().getDrawable(i) : null);
    }

    public void setClickPlayGifEnabled(@Nullable Drawable drawable) {
        boolean z = true;
        if (drawable != null) {
            if (getFunctions().clickPlayGifFunction == null) {
                getFunctions().clickPlayGifFunction = new ClickPlayGifFunction(this);
            } else {
                z = false;
            }
            z |= getFunctions().clickPlayGifFunction.setPlayIconDrawable(drawable);
        } else if (getFunctions().clickPlayGifFunction != null) {
            getFunctions().clickPlayGifFunction = null;
        } else {
            z = false;
        }
        if (z) {
            updateClickable();
            invalidate();
        }
    }

    public void setClickRetryOnDisplayErrorEnabled(boolean z) {
        if (isClickRetryOnDisplayErrorEnabled() == z) {
            return;
        }
        if (getFunctions().clickRetryFunction == null) {
            getFunctions().clickRetryFunction = new ClickRetryFunction(this);
        }
        getFunctions().clickRetryFunction.setClickRetryOnDisplayErrorEnabled(z);
        updateClickable();
    }

    public void setClickRetryOnPauseDownloadEnabled(boolean z) {
        if (isClickRetryOnPauseDownloadEnabled() == z) {
            return;
        }
        if (getFunctions().clickRetryFunction == null) {
            getFunctions().clickRetryFunction = new ClickRetryFunction(this);
        }
        getFunctions().clickRetryFunction.setClickRetryOnPauseDownloadEnabled(z);
        updateClickable();
    }

    public void setShowDownloadProgressEnabled(boolean z) {
        setShowDownloadProgressEnabled(z, 570425344, null);
    }

    public void setShowDownloadProgressEnabled(boolean z, @ColorInt int i) {
        setShowDownloadProgressEnabled(z, i, null);
    }

    public void setShowDownloadProgressEnabled(boolean z, @ColorInt int i, @Nullable ImageShaper imageShaper) {
        boolean z2 = true;
        if (z) {
            if (getFunctions().showDownloadProgressFunction == null) {
                getFunctions().showDownloadProgressFunction = new ShowDownloadProgressFunction(this);
            } else {
                z2 = false;
            }
            z2 = getFunctions().showDownloadProgressFunction.setMaskColor(i) | z2 | getFunctions().showDownloadProgressFunction.setMaskShaper(imageShaper);
        } else if (getFunctions().showDownloadProgressFunction != null) {
            getFunctions().showDownloadProgressFunction = null;
        } else {
            z2 = false;
        }
        if (z2) {
            invalidate();
        }
    }

    public void setShowDownloadProgressEnabled(boolean z, @Nullable ImageShaper imageShaper) {
        setShowDownloadProgressEnabled(z, 570425344, imageShaper);
    }

    public void setShowGifFlagEnabled(@DrawableRes int i) {
        setShowGifFlagEnabled(i > 0 ? getResources().getDrawable(i) : null);
    }

    public void setShowGifFlagEnabled(Drawable drawable) {
        boolean z = true;
        if (drawable != null) {
            if (getFunctions().showGifFlagFunction == null) {
                getFunctions().showGifFlagFunction = new ShowGifFlagFunction(this);
            } else {
                z = false;
            }
            z |= getFunctions().showGifFlagFunction.setGifFlagDrawable(drawable);
        } else if (getFunctions().showGifFlagFunction != null) {
            getFunctions().showGifFlagFunction = null;
        } else {
            z = false;
        }
        if (z) {
            invalidate();
        }
    }

    public void setShowImageFromEnabled(boolean z) {
        if (isShowImageFromEnabled() == z) {
            return;
        }
        if (z) {
            getFunctions().showImageFromFunction = new ShowImageFromFunction(this);
            getFunctions().showImageFromFunction.onDrawableChanged("setShowImageFromEnabled", null, getDrawable());
        } else {
            getFunctions().showImageFromFunction = null;
        }
        invalidate();
    }

    public void setShowPressedStatusEnabled(boolean z) {
        setShowPressedStatusEnabled(z, 855638016, null);
    }

    public void setShowPressedStatusEnabled(boolean z, @ColorInt int i) {
        setShowPressedStatusEnabled(z, i, null);
    }

    public void setShowPressedStatusEnabled(boolean z, @ColorInt int i, ImageShaper imageShaper) {
        boolean z2 = true;
        if (z) {
            if (getFunctions().showPressedFunction == null) {
                getFunctions().showPressedFunction = new ShowPressedFunction(this);
            } else {
                z2 = false;
            }
            z2 = getFunctions().showPressedFunction.setMaskColor(i) | z2 | getFunctions().showPressedFunction.setMaskShaper(imageShaper);
        } else if (getFunctions().showPressedFunction != null) {
            getFunctions().showPressedFunction = null;
        } else {
            z2 = false;
        }
        if (z2) {
            invalidate();
        }
    }

    public void setShowPressedStatusEnabled(boolean z, ImageShaper imageShaper) {
        setShowPressedStatusEnabled(z, 855638016, imageShaper);
    }

    public void setZoomEnabled(boolean z) {
        if (z == isZoomEnabled()) {
            return;
        }
        if (!z) {
            getFunctions().zoomFunction.recycle("setZoomEnabled");
            getFunctions().zoomFunction = null;
        } else {
            ImageZoomFunction imageZoomFunction = new ImageZoomFunction(this);
            imageZoomFunction.onDrawableChanged("setZoomEnabled", null, getDrawable());
            getFunctions().zoomFunction = imageZoomFunction;
        }
    }
}
