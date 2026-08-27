package me.panpf.sketch.zoom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.OverScroller;
import android.widget.Scroller;

/* loaded from: classes2.dex */
public abstract class ScrollerProxy {

    @TargetApi(9)
    /* loaded from: classes2.dex */
    public static class GingerScroller extends ScrollerProxy {
        private boolean mFirstScroll = false;
        protected final OverScroller mScroller;

        public GingerScroller(Context context) {
            this.mScroller = new OverScroller(context);
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public boolean computeScrollOffset() {
            if (this.mFirstScroll) {
                this.mScroller.computeScrollOffset();
                this.mFirstScroll = false;
            }
            return this.mScroller.computeScrollOffset();
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            this.mScroller.fling(i, i2, i3, i4, i5, i6, i7, i8, i9, i10);
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public void forceFinished(boolean z) {
            this.mScroller.forceFinished(z);
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public int getCurrX() {
            return this.mScroller.getCurrX();
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public int getCurrY() {
            return this.mScroller.getCurrY();
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public boolean isFinished() {
            return this.mScroller.isFinished();
        }
    }

    @TargetApi(14)
    /* loaded from: classes2.dex */
    public static class IcsScroller extends GingerScroller {
        public IcsScroller(Context context) {
            super(context);
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy.GingerScroller, me.panpf.sketch.zoom.ScrollerProxy
        public boolean computeScrollOffset() {
            return this.mScroller.computeScrollOffset();
        }
    }

    /* loaded from: classes2.dex */
    public static class PreGingerScroller extends ScrollerProxy {
        private final Scroller mScroller;

        public PreGingerScroller(Context context) {
            this.mScroller = new Scroller(context);
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public boolean computeScrollOffset() {
            return this.mScroller.computeScrollOffset();
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            this.mScroller.fling(i, i2, i3, i4, i5, i6, i7, i8);
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public void forceFinished(boolean z) {
            this.mScroller.forceFinished(z);
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public int getCurrX() {
            return this.mScroller.getCurrX();
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public int getCurrY() {
            return this.mScroller.getCurrY();
        }

        @Override // me.panpf.sketch.zoom.ScrollerProxy
        public boolean isFinished() {
            return this.mScroller.isFinished();
        }
    }

    @SuppressLint({"ObsoleteSdkInt"})
    public static ScrollerProxy getScroller(Context context) {
        return Build.VERSION.SDK_INT >= 14 ? new IcsScroller(context) : Build.VERSION.SDK_INT >= 9 ? new GingerScroller(context) : new PreGingerScroller(context);
    }

    public abstract boolean computeScrollOffset();

    public abstract void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    public abstract void forceFinished(boolean z);

    public abstract int getCurrX();

    public abstract int getCurrY();

    public abstract boolean isFinished();
}
