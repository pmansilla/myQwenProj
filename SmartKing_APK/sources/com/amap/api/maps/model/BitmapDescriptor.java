package com.amap.api.maps.model;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class BitmapDescriptor implements Parcelable, Cloneable {
    public static final BitmapDescriptorCreator CREATOR = new BitmapDescriptorCreator();
    int a;
    int b;
    Bitmap c;
    private String mId;

    private BitmapDescriptor(Bitmap bitmap, int i, int i2, String str) {
        this.a = 0;
        this.b = 0;
        this.a = i;
        this.b = i2;
        this.c = bitmap;
        this.mId = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BitmapDescriptor(Bitmap bitmap, String str) {
        this.a = 0;
        this.b = 0;
        if (bitmap != null) {
            this.a = bitmap.getWidth();
            this.b = bitmap.getHeight();
            if (bitmap.getConfig() == null) {
                this.c = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            } else {
                this.c = bitmap.copy(bitmap.getConfig(), true);
            }
            this.mId = str;
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public BitmapDescriptor m12clone() {
        try {
            return new BitmapDescriptor(this.c.copy(this.c.getConfig(), true), this.a, this.b, this.mId);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this.c == null || this.c.isRecycled() || obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BitmapDescriptor bitmapDescriptor = (BitmapDescriptor) obj;
        if (bitmapDescriptor.c == null || bitmapDescriptor.c.isRecycled() || this.a != bitmapDescriptor.getWidth() || this.b != bitmapDescriptor.getHeight()) {
            return false;
        }
        try {
            return this.c.sameAs(bitmapDescriptor.c);
        } catch (Throwable unused) {
            return false;
        }
    }

    public Bitmap getBitmap() {
        return this.c;
    }

    public int getHeight() {
        return this.b;
    }

    public String getId() {
        return this.mId;
    }

    public int getWidth() {
        return this.a;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public void recycle() {
        if (this.c == null || this.c.isRecycled()) {
            return;
        }
        if (Build.VERSION.SDK_INT <= 10) {
            this.c.recycle();
        }
        this.c = null;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mId);
        parcel.writeParcelable(this.c, i);
        parcel.writeInt(this.a);
        parcel.writeInt(this.b);
    }
}
