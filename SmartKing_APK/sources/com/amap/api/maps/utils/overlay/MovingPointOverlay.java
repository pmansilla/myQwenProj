package com.amap.api.maps.utils.overlay;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BasePointOverlay;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
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
public class MovingPointOverlay {
    private BasePointOverlay baseOverlay;
    private AMap mAMap;
    private ExecutorService mThreadPools;
    private MoveListener moveListener;
    private long pauseMillis;
    private long duration = 10000;
    private long mStepDuration = 20;
    private LinkedList<LatLng> points = new LinkedList<>();
    private LinkedList<Double> eachDistance = new LinkedList<>();
    private double totalDistance = 0.0d;
    private double remainDistance = 0.0d;
    private Object mLock = new Object();
    private int index = 0;
    private boolean useDefaultDescriptor = false;
    AtomicBoolean exitFlag = new AtomicBoolean(false);
    private a STATUS = a.ACTION_UNKNOWN;
    private long mAnimationBeginTime = System.currentTimeMillis();

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
    private class b implements ThreadFactory {
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
                MovingPointOverlay.this.mAnimationBeginTime = System.currentTimeMillis();
                MovingPointOverlay.this.STATUS = a.ACTION_START;
                MovingPointOverlay.this.exitFlag.set(false);
                while (!MovingPointOverlay.this.exitFlag.get() && MovingPointOverlay.this.index <= MovingPointOverlay.this.points.size() - 1) {
                    synchronized (MovingPointOverlay.this.mLock) {
                        if (MovingPointOverlay.this.exitFlag.get()) {
                            return;
                        }
                        if (MovingPointOverlay.this.STATUS != a.ACTION_PAUSE) {
                            MovingPointOverlay.this.baseOverlay.setGeoPoint(MovingPointOverlay.this.getCurPosition(System.currentTimeMillis() - MovingPointOverlay.this.mAnimationBeginTime));
                            MovingPointOverlay.this.STATUS = a.ACTION_RUNNING;
                        }
                    }
                    Thread.sleep(MovingPointOverlay.this.mStepDuration);
                }
                MovingPointOverlay.this.STATUS = a.ACTION_STOP;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public MovingPointOverlay(AMap aMap, BasePointOverlay basePointOverlay) {
        this.baseOverlay = null;
        if (aMap == null || basePointOverlay == null) {
            return;
        }
        this.mAMap = aMap;
        this.mThreadPools = new ThreadPoolExecutor(1, 2, 5L, TimeUnit.SECONDS, new SynchronousQueue(), new b());
        this.baseOverlay = basePointOverlay;
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
        if (AMapUtils.calculateLineDistance(latLng2, latLng3) > 1.0f) {
            float rotate = getRotate(iPoint2, iPoint3);
            if (this.mAMap != null && (cameraPosition = this.mAMap.getCameraPosition()) != null) {
                this.baseOverlay.setRotateAngle((360.0f - rotate) + cameraPosition.bearing);
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
            if (this.STATUS == a.ACTION_RUNNING || this.STATUS == a.ACTION_PAUSE) {
                this.exitFlag.set(true);
                this.mThreadPools.awaitTermination(this.mStepDuration + 20, TimeUnit.MILLISECONDS);
                this.baseOverlay.setAnimation(null);
                this.STATUS = a.ACTION_UNKNOWN;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            reset();
            this.mThreadPools.shutdownNow();
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

    public BasePointOverlay getObject() {
        return this.baseOverlay;
    }

    public LatLng getPosition() {
        if (this.baseOverlay != null) {
            return this.baseOverlay.getPosition();
        }
        return null;
    }

    public void removeMarker() {
        try {
            if (this.baseOverlay != null) {
                this.baseOverlay.remove();
                this.baseOverlay = null;
            }
            this.points.clear();
            this.eachDistance.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetIndex() {
        this.index = 0;
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
                    this.baseOverlay.setPosition(this.points.get(0));
                    reset();
                }
            }
        }
    }

    public void setPosition(LatLng latLng) {
        try {
            if (this.baseOverlay != null) {
                this.baseOverlay.setPosition(latLng);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRotate(float f) {
        CameraPosition cameraPosition;
        try {
            if (this.baseOverlay == null || this.mAMap == null || (cameraPosition = this.mAMap.getCameraPosition()) == null) {
                return;
            }
            this.baseOverlay.setRotateAngle((360.0f - f) + cameraPosition.bearing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTotalDuration(int i) {
        this.duration = i * 1000;
    }

    public void setVisible(boolean z) {
        try {
            if (this.baseOverlay != null) {
                this.baseOverlay.setVisible(z);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSmoothMove() {
        if (this.STATUS == a.ACTION_PAUSE) {
            this.STATUS = a.ACTION_RUNNING;
            this.mAnimationBeginTime += System.currentTimeMillis() - this.pauseMillis;
        } else if ((this.STATUS == a.ACTION_UNKNOWN || this.STATUS == a.ACTION_STOP) && this.points.size() >= 1) {
            this.index = 0;
            try {
                this.mThreadPools.execute(new c());
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void stopMove() {
        if (this.STATUS == a.ACTION_RUNNING) {
            this.STATUS = a.ACTION_PAUSE;
            this.pauseMillis = System.currentTimeMillis();
        }
    }
}
