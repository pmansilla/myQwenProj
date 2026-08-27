package com.amap.location.common.model;

/* loaded from: classes.dex */
public class FPS {
    public double accuracy;
    public double latitude;
    public double longitude;
    public byte provider;
    public CellStatus cellStatus = new CellStatus();
    public WifiStatus wifiStatus = new WifiStatus();

    private String toStr(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("FPS[");
        sb.append(this.cellStatus != null ? z ? this.cellStatus.toStringSimple() : this.cellStatus.toString() : "cellStatus:null");
        sb.append(";");
        sb.append(this.wifiStatus != null ? z ? this.wifiStatus.toStringSimple() : this.wifiStatus.toString() : "wifiScan:null");
        sb.append("]");
        return sb.toString();
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public FPS m17clone() {
        FPS fps = new FPS();
        if (this.cellStatus != null) {
            fps.cellStatus = this.cellStatus.m15clone();
        }
        if (this.wifiStatus != null) {
            fps.wifiStatus = this.wifiStatus.m19clone();
        }
        return fps;
    }

    public String toString() {
        return super.toString();
    }

    public String toStringSimple() {
        return toStr(true);
    }
}
