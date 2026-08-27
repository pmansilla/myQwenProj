package com.yalantis.ucrop.model;

import java.io.Serializable;

/* loaded from: classes2.dex */
public class CutInfo implements Serializable {
    private String cutPath;
    private int imageHeight;
    private int imageWidth;
    private boolean isCut;
    private int offsetX;
    private int offsetY;
    private String path;
    private float resultAspectRatio;

    public CutInfo() {
    }

    public CutInfo(String str, boolean z) {
        this.path = str;
        this.isCut = z;
    }

    public String getCutPath() {
        return this.cutPath;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public String getPath() {
        return this.path;
    }

    public float getResultAspectRatio() {
        return this.resultAspectRatio;
    }

    public boolean isCut() {
        return this.isCut;
    }

    public void setCut(boolean z) {
        this.isCut = z;
    }

    public void setCutPath(String str) {
        this.cutPath = str;
    }

    public void setImageHeight(int i) {
        this.imageHeight = i;
    }

    public void setImageWidth(int i) {
        this.imageWidth = i;
    }

    public void setOffsetX(int i) {
        this.offsetX = i;
    }

    public void setOffsetY(int i) {
        this.offsetY = i;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public void setResultAspectRatio(float f) {
        this.resultAspectRatio = f;
    }
}
