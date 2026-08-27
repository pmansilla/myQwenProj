package com.amap.api.mapcore.util;

import android.graphics.Point;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.autonavi.amap.mapcore.AbstractCameraUpdateMessage;
import com.autonavi.amap.mapcore.VirtualEarthProjection;

/* compiled from: CameraUpdateFactoryDelegate.java */
/* loaded from: classes.dex */
public class aw {
    public static AbstractCameraUpdateMessage a() {
        av avVar = new av();
        avVar.nowType = AbstractCameraUpdateMessage.Type.zoomBy;
        avVar.amount = 1.0f;
        return avVar;
    }

    public static AbstractCameraUpdateMessage a(float f) {
        at atVar = new at();
        atVar.nowType = AbstractCameraUpdateMessage.Type.newCameraPosition;
        atVar.zoom = f;
        return atVar;
    }

    public static AbstractCameraUpdateMessage a(float f, float f2) {
        au auVar = new au();
        auVar.nowType = AbstractCameraUpdateMessage.Type.scrollBy;
        auVar.xPixel = f;
        auVar.yPixel = f2;
        return auVar;
    }

    public static AbstractCameraUpdateMessage a(float f, Point point) {
        av avVar = new av();
        avVar.nowType = AbstractCameraUpdateMessage.Type.zoomBy;
        avVar.amount = f;
        avVar.focus = point;
        return avVar;
    }

    public static AbstractCameraUpdateMessage a(Point point) {
        at atVar = new at();
        atVar.nowType = AbstractCameraUpdateMessage.Type.newCameraPosition;
        atVar.geoPoint = point;
        return atVar;
    }

    public static AbstractCameraUpdateMessage a(CameraPosition cameraPosition) {
        at atVar = new at();
        atVar.nowType = AbstractCameraUpdateMessage.Type.newCameraPosition;
        if (cameraPosition == null || cameraPosition.target == null) {
            return atVar;
        }
        atVar.geoPoint = VirtualEarthProjection.latLongToPixels(cameraPosition.target.latitude, cameraPosition.target.longitude, 20);
        atVar.zoom = cameraPosition.zoom;
        atVar.bearing = cameraPosition.bearing;
        atVar.tilt = cameraPosition.tilt;
        atVar.cameraPosition = cameraPosition;
        return atVar;
    }

    public static AbstractCameraUpdateMessage a(LatLng latLng) {
        return a(CameraPosition.builder().target(latLng).zoom(Float.NaN).bearing(Float.NaN).tilt(Float.NaN).build());
    }

    public static AbstractCameraUpdateMessage a(LatLng latLng, float f) {
        return a(CameraPosition.builder().target(latLng).zoom(f).bearing(Float.NaN).tilt(Float.NaN).build());
    }

    public static AbstractCameraUpdateMessage a(LatLngBounds latLngBounds, int i) {
        as asVar = new as();
        asVar.nowType = AbstractCameraUpdateMessage.Type.newLatLngBounds;
        asVar.bounds = latLngBounds;
        asVar.paddingLeft = i;
        asVar.paddingRight = i;
        asVar.paddingTop = i;
        asVar.paddingBottom = i;
        return asVar;
    }

    public static AbstractCameraUpdateMessage a(LatLngBounds latLngBounds, int i, int i2, int i3) {
        as asVar = new as();
        asVar.nowType = AbstractCameraUpdateMessage.Type.newLatLngBoundsWithSize;
        asVar.bounds = latLngBounds;
        asVar.paddingLeft = i3;
        asVar.paddingRight = i3;
        asVar.paddingTop = i3;
        asVar.paddingBottom = i3;
        asVar.width = i;
        asVar.height = i2;
        return asVar;
    }

    public static AbstractCameraUpdateMessage a(LatLngBounds latLngBounds, int i, int i2, int i3, int i4) {
        as asVar = new as();
        asVar.nowType = AbstractCameraUpdateMessage.Type.newLatLngBounds;
        asVar.bounds = latLngBounds;
        asVar.paddingLeft = i;
        asVar.paddingRight = i2;
        asVar.paddingTop = i3;
        asVar.paddingBottom = i4;
        return asVar;
    }

    public static AbstractCameraUpdateMessage b() {
        av avVar = new av();
        avVar.nowType = AbstractCameraUpdateMessage.Type.zoomBy;
        avVar.amount = -1.0f;
        return avVar;
    }

    public static AbstractCameraUpdateMessage b(float f) {
        return a(f, (Point) null);
    }

    public static AbstractCameraUpdateMessage b(float f, Point point) {
        at atVar = new at();
        atVar.nowType = AbstractCameraUpdateMessage.Type.newCameraPosition;
        atVar.geoPoint = point;
        atVar.bearing = f;
        return atVar;
    }

    public static AbstractCameraUpdateMessage c() {
        return new at();
    }

    public static AbstractCameraUpdateMessage c(float f) {
        at atVar = new at();
        atVar.nowType = AbstractCameraUpdateMessage.Type.newCameraPosition;
        atVar.tilt = f;
        return atVar;
    }

    public static AbstractCameraUpdateMessage d(float f) {
        at atVar = new at();
        atVar.nowType = AbstractCameraUpdateMessage.Type.newCameraPosition;
        atVar.bearing = f;
        return atVar;
    }
}
