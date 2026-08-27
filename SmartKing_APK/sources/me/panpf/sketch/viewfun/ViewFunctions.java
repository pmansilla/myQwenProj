package me.panpf.sketch.viewfun;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.uri.UriModel;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ViewFunctions {
    ClickPlayGifFunction clickPlayGifFunction;
    ClickRetryFunction clickRetryFunction;
    RecyclerCompatFunction recyclerCompatFunction;
    RequestFunction requestFunction;
    ShowDownloadProgressFunction showDownloadProgressFunction;
    ShowGifFlagFunction showGifFlagFunction;
    ShowImageFromFunction showImageFromFunction;
    ShowPressedFunction showPressedFunction;
    ImageZoomFunction zoomFunction;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewFunctions(FunctionCallbackView functionCallbackView) {
        this.requestFunction = new RequestFunction(functionCallbackView);
        this.recyclerCompatFunction = new RecyclerCompatFunction(functionCallbackView);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAttachedToWindow() {
        if (this.requestFunction != null) {
            this.requestFunction.onAttachedToWindow();
        }
        if (this.recyclerCompatFunction != null) {
            this.recyclerCompatFunction.onAttachedToWindow();
        }
        if (this.showPressedFunction != null) {
            this.showPressedFunction.onAttachedToWindow();
        }
        if (this.showDownloadProgressFunction != null) {
            this.showDownloadProgressFunction.onAttachedToWindow();
        }
        if (this.showGifFlagFunction != null) {
            this.showGifFlagFunction.onAttachedToWindow();
        }
        if (this.showImageFromFunction != null) {
            this.showImageFromFunction.onAttachedToWindow();
        }
        if (this.clickRetryFunction != null) {
            this.clickRetryFunction.onAttachedToWindow();
        }
        if (this.zoomFunction != null) {
            this.zoomFunction.onAttachedToWindow();
        }
        if (this.clickPlayGifFunction != null) {
            this.clickPlayGifFunction.onAttachedToWindow();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onDetachedFromWindow() {
        boolean onDetachedFromWindow = this.requestFunction != null ? false | this.requestFunction.onDetachedFromWindow() : false;
        if (this.recyclerCompatFunction != null) {
            onDetachedFromWindow |= this.recyclerCompatFunction.onDetachedFromWindow();
        }
        if (this.showPressedFunction != null) {
            onDetachedFromWindow |= this.showPressedFunction.onDetachedFromWindow();
        }
        if (this.showDownloadProgressFunction != null) {
            onDetachedFromWindow |= this.showDownloadProgressFunction.onDetachedFromWindow();
        }
        if (this.showGifFlagFunction != null) {
            onDetachedFromWindow |= this.showGifFlagFunction.onDetachedFromWindow();
        }
        if (this.showImageFromFunction != null) {
            onDetachedFromWindow |= this.showImageFromFunction.onDetachedFromWindow();
        }
        if (this.clickRetryFunction != null) {
            onDetachedFromWindow |= this.clickRetryFunction.onDetachedFromWindow();
        }
        if (this.zoomFunction != null) {
            onDetachedFromWindow |= this.zoomFunction.onDetachedFromWindow();
        }
        return this.clickPlayGifFunction != null ? onDetachedFromWindow | this.clickPlayGifFunction.onDetachedFromWindow() : onDetachedFromWindow;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onDisplayCanceled(@NonNull CancelCause cancelCause) {
        boolean onDisplayCanceled = this.showImageFromFunction != null ? false | this.showImageFromFunction.onDisplayCanceled(cancelCause) : false;
        if (this.showDownloadProgressFunction != null) {
            onDisplayCanceled |= this.showDownloadProgressFunction.onDisplayCanceled(cancelCause);
        }
        if (this.showGifFlagFunction != null) {
            onDisplayCanceled |= this.showGifFlagFunction.onDisplayCanceled(cancelCause);
        }
        if (this.showPressedFunction != null) {
            onDisplayCanceled |= this.showPressedFunction.onDisplayCanceled(cancelCause);
        }
        if (this.clickRetryFunction != null) {
            onDisplayCanceled |= this.clickRetryFunction.onDisplayCanceled(cancelCause);
        }
        if (this.requestFunction != null) {
            onDisplayCanceled |= this.requestFunction.onDisplayCanceled(cancelCause);
        }
        if (this.recyclerCompatFunction != null) {
            onDisplayCanceled |= this.recyclerCompatFunction.onDisplayCanceled(cancelCause);
        }
        if (this.zoomFunction != null) {
            onDisplayCanceled |= this.zoomFunction.onDisplayCanceled(cancelCause);
        }
        return this.clickPlayGifFunction != null ? onDisplayCanceled | this.clickPlayGifFunction.onDisplayCanceled(cancelCause) : onDisplayCanceled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onDisplayCompleted(@NonNull Drawable drawable, @NonNull ImageFrom imageFrom, @NonNull ImageAttrs imageAttrs) {
        boolean onDisplayCompleted = this.showImageFromFunction != null ? false | this.showImageFromFunction.onDisplayCompleted(drawable, imageFrom, imageAttrs) : false;
        if (this.showDownloadProgressFunction != null) {
            onDisplayCompleted |= this.showDownloadProgressFunction.onDisplayCompleted(drawable, imageFrom, imageAttrs);
        }
        if (this.showGifFlagFunction != null) {
            onDisplayCompleted |= this.showGifFlagFunction.onDisplayCompleted(drawable, imageFrom, imageAttrs);
        }
        if (this.showPressedFunction != null) {
            onDisplayCompleted |= this.showPressedFunction.onDisplayCompleted(drawable, imageFrom, imageAttrs);
        }
        if (this.clickRetryFunction != null) {
            onDisplayCompleted |= this.clickRetryFunction.onDisplayCompleted(drawable, imageFrom, imageAttrs);
        }
        if (this.requestFunction != null) {
            onDisplayCompleted |= this.requestFunction.onDisplayCompleted(drawable, imageFrom, imageAttrs);
        }
        if (this.recyclerCompatFunction != null) {
            onDisplayCompleted |= this.recyclerCompatFunction.onDisplayCompleted(drawable, imageFrom, imageAttrs);
        }
        if (this.zoomFunction != null) {
            onDisplayCompleted |= this.zoomFunction.onDisplayCompleted(drawable, imageFrom, imageAttrs);
        }
        return this.clickPlayGifFunction != null ? onDisplayCompleted | this.clickPlayGifFunction.onDisplayCompleted(drawable, imageFrom, imageAttrs) : onDisplayCompleted;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onDisplayError(@NonNull ErrorCause errorCause) {
        boolean onDisplayError = this.showImageFromFunction != null ? false | this.showImageFromFunction.onDisplayError(errorCause) : false;
        if (this.showDownloadProgressFunction != null) {
            onDisplayError |= this.showDownloadProgressFunction.onDisplayError(errorCause);
        }
        if (this.showGifFlagFunction != null) {
            onDisplayError |= this.showGifFlagFunction.onDisplayError(errorCause);
        }
        if (this.showPressedFunction != null) {
            onDisplayError |= this.showPressedFunction.onDisplayError(errorCause);
        }
        if (this.clickRetryFunction != null) {
            onDisplayError |= this.clickRetryFunction.onDisplayError(errorCause);
        }
        if (this.requestFunction != null) {
            onDisplayError |= this.requestFunction.onDisplayError(errorCause);
        }
        if (this.recyclerCompatFunction != null) {
            onDisplayError |= this.recyclerCompatFunction.onDisplayError(errorCause);
        }
        if (this.zoomFunction != null) {
            onDisplayError |= this.zoomFunction.onDisplayError(errorCause);
        }
        return this.clickPlayGifFunction != null ? onDisplayError | this.clickPlayGifFunction.onDisplayError(errorCause) : onDisplayError;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onDisplayStarted() {
        boolean onDisplayStarted = this.showImageFromFunction != null ? false | this.showImageFromFunction.onDisplayStarted() : false;
        if (this.showDownloadProgressFunction != null) {
            onDisplayStarted |= this.showDownloadProgressFunction.onDisplayStarted();
        }
        if (this.showGifFlagFunction != null) {
            onDisplayStarted |= this.showGifFlagFunction.onDisplayStarted();
        }
        if (this.showPressedFunction != null) {
            onDisplayStarted |= this.showPressedFunction.onDisplayStarted();
        }
        if (this.clickRetryFunction != null) {
            onDisplayStarted |= this.clickRetryFunction.onDisplayStarted();
        }
        if (this.requestFunction != null) {
            onDisplayStarted |= this.requestFunction.onDisplayStarted();
        }
        if (this.recyclerCompatFunction != null) {
            onDisplayStarted |= this.recyclerCompatFunction.onDisplayStarted();
        }
        if (this.zoomFunction != null) {
            onDisplayStarted |= this.zoomFunction.onDisplayStarted();
        }
        return this.clickPlayGifFunction != null ? onDisplayStarted | this.clickPlayGifFunction.onDisplayStarted() : onDisplayStarted;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDraw(Canvas canvas) {
        if (this.zoomFunction != null) {
            this.zoomFunction.onDraw(canvas);
        }
        if (this.showPressedFunction != null) {
            this.showPressedFunction.onDraw(canvas);
        }
        if (this.showDownloadProgressFunction != null) {
            this.showDownloadProgressFunction.onDraw(canvas);
        }
        if (this.showImageFromFunction != null) {
            this.showImageFromFunction.onDraw(canvas);
        }
        if (this.showGifFlagFunction != null) {
            this.showGifFlagFunction.onDraw(canvas);
        }
        if (this.clickRetryFunction != null) {
            this.clickRetryFunction.onDraw(canvas);
        }
        if (this.requestFunction != null) {
            this.requestFunction.onDraw(canvas);
        }
        if (this.recyclerCompatFunction != null) {
            this.recyclerCompatFunction.onDraw(canvas);
        }
        if (this.clickPlayGifFunction != null) {
            this.clickPlayGifFunction.onDraw(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onDrawableChanged(String str, Drawable drawable, Drawable drawable2) {
        boolean onDrawableChanged = this.requestFunction != null ? false | this.requestFunction.onDrawableChanged(str, drawable, drawable2) : false;
        if (this.showGifFlagFunction != null) {
            onDrawableChanged |= this.showGifFlagFunction.onDrawableChanged(str, drawable, drawable2);
        }
        if (this.showImageFromFunction != null) {
            onDrawableChanged |= this.showImageFromFunction.onDrawableChanged(str, drawable, drawable2);
        }
        if (this.showPressedFunction != null) {
            onDrawableChanged |= this.showPressedFunction.onDrawableChanged(str, drawable, drawable2);
        }
        if (this.showDownloadProgressFunction != null) {
            onDrawableChanged |= this.showDownloadProgressFunction.onDrawableChanged(str, drawable, drawable2);
        }
        if (this.clickRetryFunction != null) {
            onDrawableChanged |= this.clickRetryFunction.onDrawableChanged(str, drawable, drawable2);
        }
        if (this.recyclerCompatFunction != null) {
            onDrawableChanged |= this.recyclerCompatFunction.onDrawableChanged(str, drawable, drawable2);
        }
        if (this.zoomFunction != null) {
            onDrawableChanged |= this.zoomFunction.onDrawableChanged(str, drawable, drawable2);
        }
        return this.clickPlayGifFunction != null ? onDrawableChanged | this.clickPlayGifFunction.onDrawableChanged(str, drawable, drawable2) : onDrawableChanged;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.showImageFromFunction != null) {
            this.showImageFromFunction.onLayout(z, i, i2, i3, i4);
        }
        if (this.showDownloadProgressFunction != null) {
            this.showDownloadProgressFunction.onLayout(z, i, i2, i3, i4);
        }
        if (this.showGifFlagFunction != null) {
            this.showGifFlagFunction.onLayout(z, i, i2, i3, i4);
        }
        if (this.showPressedFunction != null) {
            this.showPressedFunction.onLayout(z, i, i2, i3, i4);
        }
        if (this.clickRetryFunction != null) {
            this.clickRetryFunction.onLayout(z, i, i2, i3, i4);
        }
        if (this.requestFunction != null) {
            this.requestFunction.onLayout(z, i, i2, i3, i4);
        }
        if (this.recyclerCompatFunction != null) {
            this.recyclerCompatFunction.onLayout(z, i, i2, i3, i4);
        }
        if (this.zoomFunction != null) {
            this.zoomFunction.onLayout(z, i, i2, i3, i4);
        }
        if (this.clickPlayGifFunction != null) {
            this.clickPlayGifFunction.onLayout(z, i, i2, i3, i4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onReadyDisplay(@Nullable UriModel uriModel) {
        boolean onReadyDisplay = this.requestFunction != null ? false | this.requestFunction.onReadyDisplay(uriModel) : false;
        if (this.recyclerCompatFunction != null) {
            onReadyDisplay |= this.recyclerCompatFunction.onReadyDisplay(uriModel);
        }
        if (this.showPressedFunction != null) {
            onReadyDisplay |= this.showPressedFunction.onReadyDisplay(uriModel);
        }
        if (this.showDownloadProgressFunction != null) {
            onReadyDisplay |= this.showDownloadProgressFunction.onReadyDisplay(uriModel);
        }
        if (this.showGifFlagFunction != null) {
            onReadyDisplay |= this.showGifFlagFunction.onReadyDisplay(uriModel);
        }
        if (this.showImageFromFunction != null) {
            onReadyDisplay |= this.showImageFromFunction.onReadyDisplay(uriModel);
        }
        if (this.clickRetryFunction != null) {
            onReadyDisplay |= this.clickRetryFunction.onReadyDisplay(uriModel);
        }
        if (this.zoomFunction != null) {
            onReadyDisplay |= this.zoomFunction.onReadyDisplay(uriModel);
        }
        return this.clickPlayGifFunction != null ? onReadyDisplay | this.clickPlayGifFunction.onReadyDisplay(uriModel) : onReadyDisplay;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (this.requestFunction != null) {
            this.requestFunction.onSizeChanged(i, i2, i3, i4);
        }
        if (this.recyclerCompatFunction != null) {
            this.recyclerCompatFunction.onSizeChanged(i, i2, i3, i4);
        }
        if (this.showPressedFunction != null) {
            this.showPressedFunction.onSizeChanged(i, i2, i3, i4);
        }
        if (this.showDownloadProgressFunction != null) {
            this.showDownloadProgressFunction.onSizeChanged(i, i2, i3, i4);
        }
        if (this.showGifFlagFunction != null) {
            this.showGifFlagFunction.onSizeChanged(i, i2, i3, i4);
        }
        if (this.showImageFromFunction != null) {
            this.showImageFromFunction.onSizeChanged(i, i2, i3, i4);
        }
        if (this.clickRetryFunction != null) {
            this.clickRetryFunction.onSizeChanged(i, i2, i3, i4);
        }
        if (this.zoomFunction != null) {
            this.zoomFunction.onSizeChanged(i, i2, i3, i4);
        }
        if (this.clickPlayGifFunction != null) {
            this.clickPlayGifFunction.onSizeChanged(i, i2, i3, i4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.showPressedFunction != null && this.showPressedFunction.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.showDownloadProgressFunction != null && this.showDownloadProgressFunction.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.showImageFromFunction != null && this.showImageFromFunction.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.showGifFlagFunction != null && this.showGifFlagFunction.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.clickRetryFunction != null && this.clickRetryFunction.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.requestFunction != null && this.requestFunction.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.recyclerCompatFunction != null && this.recyclerCompatFunction.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.clickPlayGifFunction == null || !this.clickPlayGifFunction.onTouchEvent(motionEvent)) {
            return this.zoomFunction != null && this.zoomFunction.onTouchEvent(motionEvent);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onUpdateDownloadProgress(int i, int i2) {
        boolean onUpdateDownloadProgress = this.showImageFromFunction != null ? false | this.showImageFromFunction.onUpdateDownloadProgress(i, i2) : false;
        if (this.showDownloadProgressFunction != null) {
            onUpdateDownloadProgress |= this.showDownloadProgressFunction.onUpdateDownloadProgress(i, i2);
        }
        if (this.showPressedFunction != null) {
            onUpdateDownloadProgress |= this.showPressedFunction.onUpdateDownloadProgress(i, i2);
        }
        if (this.showGifFlagFunction != null) {
            onUpdateDownloadProgress |= this.showGifFlagFunction.onUpdateDownloadProgress(i, i2);
        }
        if (this.clickRetryFunction != null) {
            onUpdateDownloadProgress |= this.clickRetryFunction.onUpdateDownloadProgress(i, i2);
        }
        if (this.requestFunction != null) {
            onUpdateDownloadProgress |= this.requestFunction.onUpdateDownloadProgress(i, i2);
        }
        if (this.recyclerCompatFunction != null) {
            onUpdateDownloadProgress |= this.recyclerCompatFunction.onUpdateDownloadProgress(i, i2);
        }
        if (this.zoomFunction != null) {
            onUpdateDownloadProgress |= this.zoomFunction.onUpdateDownloadProgress(i, i2);
        }
        return this.clickPlayGifFunction != null ? onUpdateDownloadProgress | this.clickPlayGifFunction.onUpdateDownloadProgress(i, i2) : onUpdateDownloadProgress;
    }
}
