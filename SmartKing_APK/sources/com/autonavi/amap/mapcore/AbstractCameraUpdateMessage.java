package com.autonavi.amap.mapcore;

import android.graphics.Point;
import com.amap.api.mapcore.util.fr;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLngBounds;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.GLMapState;

/* loaded from: classes.dex */
public abstract class AbstractCameraUpdateMessage {
    public float amount;
    public int anchorX;
    public int anchorY;
    public LatLngBounds bounds;
    public CameraPosition cameraPosition;
    public Point geoPoint;
    public int height;
    public boolean isChangeFinished;
    public AMap.CancelableCallback mCallback;
    public MapConfig mapConfig;
    public int paddingBottom;
    public int paddingLeft;
    public int paddingRight;
    public int paddingTop;
    public int width;
    public float xPixel;
    public float yPixel;
    public Type nowType = Type.none;
    public Point focus = null;
    public float zoom = Float.NaN;
    public float tilt = Float.NaN;
    public float bearing = Float.NaN;
    public boolean isUseAnchor = false;
    public long mDuration = 250;

    /* loaded from: classes.dex */
    public enum Type {
        none,
        zoomIn,
        changeCenter,
        changeTilt,
        changeBearing,
        changeBearingGeoCenter,
        changeGeoCenterZoom,
        zoomOut,
        zoomTo,
        zoomBy,
        scrollBy,
        newCameraPosition,
        newLatLngBounds,
        newLatLngBoundsWithSize,
        changeGeoCenterZoomTiltBearing
    }

    protected void changeCenterByAnchor(GLMapState gLMapState, Point point) {
        changeCenterByAnchor(gLMapState, point, this.anchorX, this.anchorY);
    }

    protected void changeCenterByAnchor(GLMapState gLMapState, Point point, int i, int i2) {
        gLMapState.recalculate();
        Point anchorGeoPoint = getAnchorGeoPoint(gLMapState, i, i2);
        Point mapGeoCenter = gLMapState.getMapGeoCenter();
        gLMapState.setMapGeoCenter((mapGeoCenter.x + point.x) - anchorGeoPoint.x, (mapGeoCenter.y + point.y) - anchorGeoPoint.y);
    }

    public void generateMapAnimation(GLMapEngine gLMapEngine) {
        GLMapState newMapState = gLMapEngine.getNewMapState(1);
        runCameraUpdate(newMapState);
        Point mapGeoCenter = newMapState.getMapGeoCenter();
        gLMapEngine.addGroupAnimation(1, (int) this.mDuration, newMapState.getMapZoomer(), (int) newMapState.getMapAngle(), (int) newMapState.getCameraDegree(), mapGeoCenter.x, mapGeoCenter.y, this.mCallback);
        newMapState.recycle();
    }

    protected Point getAnchorGeoPoint(GLMapState gLMapState, int i, int i2) {
        Point point = new Point();
        gLMapState.screenToP20Point(i, i2, point);
        return point;
    }

    public abstract void mergeCameraUpdateDelegate(AbstractCameraUpdateMessage abstractCameraUpdateMessage);

    /* JADX INFO: Access modifiers changed from: protected */
    public void normalChange(GLMapState gLMapState) {
        this.zoom = Float.isNaN(this.zoom) ? gLMapState.getMapZoomer() : this.zoom;
        this.bearing = Float.isNaN(this.bearing) ? gLMapState.getMapAngle() : this.bearing;
        this.tilt = Float.isNaN(this.tilt) ? gLMapState.getCameraDegree() : this.tilt;
        this.zoom = fr.a(this.mapConfig, this.zoom);
        this.tilt = fr.a(this.tilt, this.zoom);
        double d = this.bearing;
        Double.isNaN(d);
        this.bearing = (float) (((d % 360.0d) + 360.0d) % 360.0d);
        if (this.focus != null && this.geoPoint == null) {
            this.geoPoint = getAnchorGeoPoint(gLMapState, this.focus.x, this.focus.y);
        }
        if (!Float.isNaN(this.zoom)) {
            gLMapState.setMapZoomer(this.zoom);
        }
        if (!Float.isNaN(this.bearing)) {
            gLMapState.setMapAngle(this.bearing);
        }
        if (!Float.isNaN(this.tilt)) {
            gLMapState.setCameraDegree(this.tilt);
        }
        if (this.focus != null) {
            changeCenterByAnchor(gLMapState, this.geoPoint, this.focus.x, this.focus.y);
            return;
        }
        if ((this.geoPoint == null || (this.geoPoint.x == 0 && this.geoPoint.y == 0)) ? false : true) {
            gLMapState.setMapGeoCenter(this.geoPoint.x, this.geoPoint.y);
        }
    }

    public abstract void runCameraUpdate(GLMapState gLMapState);
}
