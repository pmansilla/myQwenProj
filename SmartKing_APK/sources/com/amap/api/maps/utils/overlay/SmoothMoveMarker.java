package com.amap.api.maps.utils.overlay;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapProjection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class SmoothMoveMarker {
    public static final float MIN_OFFSET_DISTANCE = 5.0f;
    private BitmapDescriptor descriptor;
    private AMap mAMap;
    private MoveListener moveListener;
    private long pauseMillis;
    private long duration = 10000;
    private long mStepDuration = 20;
    private LinkedList<LatLng> points = new LinkedList<>();
    private LinkedList<Double> eachDistance = new LinkedList<>();
    private double totalDistance = 0.0d;
    private double remainDistance = 0.0d;
    private Object mLock = new Object();
    private Marker marker = null;
    private int index = 0;
    private boolean useDefaultDescriptor = false;
    AtomicBoolean exitFlag = new AtomicBoolean(false);
    private a moveStatus = a.ACTION_UNKNOWN;
    private long mAnimationBeginTime = System.currentTimeMillis();
    private ExecutorService mThreadPools = new ThreadPoolExecutor(1, 2, 5, TimeUnit.SECONDS, new SynchronousQueue(), new b());

    /* loaded from: classes.dex */
    public interface MoveListener {
        void move(double d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum a {
        ACTION_UNKNOWN,
        ACTION_START,
        ACTION_RUNNING,
        ACTION_PAUSE,
        ACTION_STOP
    }

    /* loaded from: classes.dex */
    private static class b implements ThreadFactory {
        private b() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "MoveSmoothThread");
        }
    }

    /* loaded from: classes.dex */
    private class c implements Runnable {
        private c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                SmoothMoveMarker.this.mAnimationBeginTime = System.currentTimeMillis();
                SmoothMoveMarker.this.moveStatus = a.ACTION_START;
                SmoothMoveMarker.this.exitFlag.set(false);
                while (!SmoothMoveMarker.this.exitFlag.get() && SmoothMoveMarker.this.index <= SmoothMoveMarker.this.points.size() - 1) {
                    synchronized (SmoothMoveMarker.this.mLock) {
                        if (SmoothMoveMarker.this.exitFlag.get()) {
                            return;
                        }
                        if (SmoothMoveMarker.this.moveStatus != a.ACTION_PAUSE) {
                            IPoint curPosition = SmoothMoveMarker.this.getCurPosition(System.currentTimeMillis() - SmoothMoveMarker.this.mAnimationBeginTime);
                            if (SmoothMoveMarker.this.marker != null) {
                                SmoothMoveMarker.this.marker.setGeoPoint(curPosition);
                            }
                            SmoothMoveMarker.this.moveStatus = a.ACTION_RUNNING;
                        }
                    }
                    Thread.sleep(SmoothMoveMarker.this.mStepDuration);
                }
                SmoothMoveMarker.this.moveStatus = a.ACTION_STOP;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public SmoothMoveMarker(AMap aMap) {
        this.mAMap = aMap;
    }

    private void checkMarkerIcon() {
        if (this.useDefaultDescriptor) {
            if (this.descriptor == null) {
                this.useDefaultDescriptor = true;
            } else {
                this.marker.setIcon(this.descriptor);
                this.useDefaultDescriptor = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IPoint getCurPosition(long j) {
        CameraPosition cameraPosition;
        if (j > this.duration) {
            this.exitFlag.set(true);
            IPoint iPoint = new IPoint();
            this.index = this.points.size() - 1;
            LatLng latLng = this.points.get(this.index);
            this.index--;
            this.index = Math.max(this.index, 0);
            this.remainDistance = 0.0d;
            MapProjection.lonlat2Geo(latLng.longitude, latLng.latitude, iPoint);
            if (this.moveListener != null) {
                this.moveListener.move(this.remainDistance);
            }
            return iPoint;
        }
        double d = j;
        double d2 = this.totalDistance;
        Double.isNaN(d);
        double d3 = d * d2;
        double d4 = this.duration;
        Double.isNaN(d4);
        double d5 = d3 / d4;
        this.remainDistance = this.totalDistance - d5;
        double d6 = 1.0d;
        double d7 = d5;
        int i = 0;
        while (true) {
            if (i >= this.eachDistance.size()) {
                i = 0;
                break;
            }
            double doubleValue = this.eachDistance.get(i).doubleValue();
            if (d7 > doubleValue) {
                d7 -= doubleValue;
                i++;
            } else if (doubleValue > 0.0d) {
                d6 = d7 / doubleValue;
            }
        }
        if (i != this.index && this.moveListener != null) {
            this.moveListener.move(this.remainDistance);
        }
        this.index = i;
        LatLng latLng2 = this.points.get(i);
        LatLng latLng3 = this.points.get(i + 1);
        IPoint iPoint2 = new IPoint();
        MapProjection.lonlat2Geo(latLng2.longitude, latLng2.latitude, iPoint2);
        IPoint iPoint3 = new IPoint();
        MapProjection.lonlat2Geo(latLng3.longitude, latLng3.latitude, iPoint3);
        int i2 = iPoint3.x - iPoint2.x;
        int i3 = iPoint3.y - iPoint2.y;
        if (AMapUtils.calculateLineDistance(latLng2, latLng3) > 5.0f) {
            float rotate = getRotate(iPoint2, iPoint3);
            if (this.mAMap != null && (cameraPosition = this.mAMap.getCameraPosition()) != null) {
                this.marker.setRotateAngle((360.0f - rotate) + cameraPosition.bearing);
            }
        }
        double d8 = iPoint2.x;
        double d9 = i2;
        Double.isNaN(d9);
        Double.isNaN(d8);
        double d10 = iPoint2.y;
        double d11 = i3;
        Double.isNaN(d11);
        Double.isNaN(d10);
        return new IPoint((int) (d8 + (d9 * d6)), (int) (d10 + (d11 * d6)));
    }

    private float getRotate(IPoint iPoint, IPoint iPoint2) {
        if (iPoint == null || iPoint2 == null) {
            return 0.0f;
        }
        double d = iPoint2.y;
        double d2 = iPoint.y;
        double d3 = iPoint.x;
        double d4 = iPoint2.x;
        Double.isNaN(d4);
        Double.isNaN(d3);
        Double.isNaN(d2);
        Double.isNaN(d);
        return (float) ((Math.atan2(d4 - d3, d2 - d) / 3.141592653589793d) * 180.0d);
    }

    private void reset() {
        try {
            if (this.moveStatus == a.ACTION_RUNNING || this.moveStatus == a.ACTION_PAUSE) {
                this.exitFlag.set(true);
                this.mThreadPools.awaitTermination(this.mStepDuration + 20, TimeUnit.MILLISECONDS);
                if (this.marker != null) {
                    this.marker.setAnimation(null);
                }
                this.moveStatus = a.ACTION_UNKNOWN;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            reset();
            this.mThreadPools.shutdownNow();
            if (this.descriptor != null) {
                this.descriptor.recycle();
            }
            if (this.marker != null) {
                this.marker.destroy();
                this.marker = null;
            }
            synchronized (this.mLock) {
                this.points.clear();
                this.eachDistance.clear();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public int getIndex() {
        return this.index;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public LatLng getPosition() {
        if (this.marker == null) {
            return null;
        }
        return this.marker.getPosition();
    }

    public void removeMarker() {
        if (this.marker != null) {
            this.marker.remove();
            this.marker = null;
        }
        this.points.clear();
        this.eachDistance.clear();
    }

    public void resetIndex() {
        this.index = 0;
    }

    public void setDescriptor(BitmapDescriptor bitmapDescriptor) {
        if (this.descriptor != null) {
            this.descriptor.recycle();
        }
        this.descriptor = bitmapDescriptor;
        if (this.marker != null) {
            this.marker.setIcon(bitmapDescriptor);
        }
    }

    public void setMoveListener(MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public void setPoints(List<LatLng> list) {
        synchronized (this.mLock) {
            if (list != null) {
                try {
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                if (list.size() >= 2) {
                    stopMove();
                    this.points.clear();
                    for (LatLng latLng : list) {
                        if (latLng != null) {
                            this.points.add(latLng);
                        }
                    }
                    this.eachDistance.clear();
                    this.totalDistance = 0.0d;
                    int i = 0;
                    while (i < this.points.size() - 1) {
                        LatLng latLng2 = this.points.get(i);
                        i++;
                        double calculateLineDistance = AMapUtils.calculateLineDistance(latLng2, this.points.get(i));
                        this.eachDistance.add(Double.valueOf(calculateLineDistance));
                        double d = this.totalDistance;
                        Double.isNaN(calculateLineDistance);
                        this.totalDistance = d + calculateLineDistance;
                    }
                    this.remainDistance = this.totalDistance;
                    LatLng latLng3 = this.points.get(0);
                    if (this.marker != null) {
                        this.marker.setPosition(latLng3);
                        checkMarkerIcon();
                    } else {
                        if (this.descriptor == null) {
                            this.useDefaultDescriptor = true;
                        }
                        this.marker = this.mAMap.addMarker(new MarkerOptions().belowMaskLayer(true).position(latLng3).icon(this.descriptor).title("").anchor(0.5f, 0.5f));
                    }
                    reset();
                }
            }
        }
    }

    public void setPosition(LatLng latLng) {
        if (this.marker != null) {
            this.marker.setPosition(latLng);
            checkMarkerIcon();
        } else {
            if (this.descriptor == null) {
                this.useDefaultDescriptor = true;
            }
            this.marker = this.mAMap.addMarker(new MarkerOptions().belowMaskLayer(true).position(latLng).icon(this.descriptor).title("").anchor(0.5f, 0.5f));
        }
    }

    public void setRotate(float f) {
        CameraPosition cameraPosition;
        if (this.marker == null || this.mAMap == null || this.mAMap == null || (cameraPosition = this.mAMap.getCameraPosition()) == null) {
            return;
        }
        this.marker.setRotateAngle((360.0f - f) + cameraPosition.bearing);
    }

    public void setTotalDuration(int i) {
        this.duration = i * 1000;
    }

    public void setVisible(boolean z) {
        if (this.marker != null) {
            this.marker.setVisible(z);
        }
    }

    public void startSmoothMove() {
        if (this.moveStatus == a.ACTION_PAUSE) {
            this.moveStatus = a.ACTION_RUNNING;
            this.mAnimationBeginTime += System.currentTimeMillis() - this.pauseMillis;
        } else if ((this.moveStatus == a.ACTION_UNKNOWN || this.moveStatus == a.ACTION_STOP) && this.points.size() >= 1) {
            this.index = 0;
            try {
                this.mThreadPools.execute(new c());
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void stopMove() {
        if (this.moveStatus == a.ACTION_RUNNING) {
            this.moveStatus = a.ACTION_PAUSE;
            this.pauseMillis = System.currentTimeMillis();
        }
    }
}
