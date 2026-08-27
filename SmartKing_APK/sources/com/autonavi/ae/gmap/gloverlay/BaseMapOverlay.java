package com.autonavi.ae.gmap.gloverlay;

import android.content.Context;
import android.graphics.Bitmap;
import com.amap.api.mapcore.util.ad;
import com.autonavi.ae.gmap.gloverlay.GLOverlay;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/* loaded from: classes.dex */
public abstract class BaseMapOverlay<T extends GLOverlay, E> implements Serializable {
    private static final long serialVersionUID = 1;
    protected Context mContext;
    protected int mEngineID;
    protected T mGLOverlay;
    protected Vector<E> mItemList;
    protected int mLastFocusedIndex;
    protected ad mMapView;

    public BaseMapOverlay(int i, Context context, ad adVar) {
        this.mItemList = null;
        this.mEngineID = 1;
        this.mEngineID = i;
        this.mContext = context;
        this.mMapView = adVar;
        this.mItemList = new Vector<>();
        iniGLOverlay();
    }

    public abstract void addItem(E e);

    public boolean clear() {
        try {
            this.mItemList.clear();
            clearFocus();
            if (this.mGLOverlay == null) {
                return true;
            }
            this.mGLOverlay.removeAll();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public void clearFocus() {
        this.mLastFocusedIndex = -1;
        this.mGLOverlay.clearFocus();
    }

    public T getGLOverlay() {
        return this.mGLOverlay;
    }

    public final E getItem(int i) {
        try {
            synchronized (this.mItemList) {
                if (i >= 0) {
                    try {
                        if (i <= this.mItemList.size() - 1) {
                            return this.mItemList.get(i);
                        }
                    } finally {
                    }
                }
                return null;
            }
        } catch (IndexOutOfBoundsException unused) {
            return null;
        }
    }

    public List<E> getItems() {
        return this.mItemList;
    }

    public int getSize() {
        return this.mItemList.size();
    }

    protected abstract void iniGLOverlay();

    public boolean isClickable() {
        if (this.mGLOverlay != null) {
            return this.mGLOverlay.isClickable();
        }
        return false;
    }

    public boolean isVisible() {
        if (this.mGLOverlay != null) {
            return this.mGLOverlay.isVisible();
        }
        return false;
    }

    public void releaseInstance() {
        this.mMapView.queueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.gloverlay.BaseMapOverlay.1
            @Override // java.lang.Runnable
            public void run() {
                if (BaseMapOverlay.this.getGLOverlay() != null) {
                    if (BaseMapOverlay.this.mMapView != null && BaseMapOverlay.this.mMapView.isMaploaded()) {
                        BaseMapOverlay.this.mMapView.removeEngineGLOverlay(BaseMapOverlay.this);
                    }
                    BaseMapOverlay.this.getGLOverlay().releaseInstance();
                    BaseMapOverlay.this.mGLOverlay = null;
                }
            }
        });
    }

    public boolean removeAll() {
        return clear();
    }

    public void removeItem(int i) {
        if (i >= 0) {
            try {
                if (i > this.mItemList.size() - 1) {
                    return;
                }
                if (i == this.mLastFocusedIndex) {
                    this.mLastFocusedIndex = -1;
                    clearFocus();
                }
                this.mItemList.remove(i);
                if (this.mGLOverlay != null) {
                    this.mGLOverlay.removeItem(i);
                }
            } catch (IndexOutOfBoundsException unused) {
            }
        }
    }

    public void removeItem(E e) {
        if (e == null) {
            return;
        }
        try {
            synchronized (this.mItemList) {
                removeItem(this.mItemList.indexOf(e));
            }
        } catch (IndexOutOfBoundsException unused) {
        }
    }

    public abstract void resumeMarker(Bitmap bitmap);

    public void setClickable(boolean z) {
        if (this.mGLOverlay != null) {
            this.mGLOverlay.setClickable(z);
        }
    }

    public void setVisible(boolean z) {
        if (this.mGLOverlay != null) {
            this.mGLOverlay.setVisible(z);
        }
    }
}
