package com.sina.weibo.sdk.api;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import cn.sharesdk.framework.utils.e;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MultiImageObject implements Parcelable {
    public static final Parcelable.Creator<MultiImageObject> CREATOR = new Parcelable.Creator<MultiImageObject>() { // from class: com.sina.weibo.sdk.api.MultiImageObject.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MultiImageObject createFromParcel(Parcel parcel) {
            return new MultiImageObject(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MultiImageObject[] newArray(int i) {
            return new MultiImageObject[i];
        }
    };
    public String actionUrl;
    public String defaultText;
    public String description;
    public String identify;
    public ArrayList<Uri> imageList;
    public String schema;
    public byte[] thumbData;
    public String title;

    public MultiImageObject() {
    }

    protected MultiImageObject(Parcel parcel) {
        this.actionUrl = parcel.readString();
        this.schema = parcel.readString();
        this.identify = parcel.readString();
        this.title = parcel.readString();
        this.description = parcel.readString();
        this.thumbData = parcel.createByteArray();
        this.imageList = parcel.createTypedArrayList(Uri.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ArrayList<Uri> getImageList() {
        return this.imageList;
    }

    public int getObjType() {
        return 0;
    }

    public void setImageList(ArrayList<Uri> arrayList) {
        this.imageList = arrayList;
    }

    public final void setThumbImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            try {
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                } catch (Throwable th) {
                    e.b().d(th);
                    return;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
            this.thumbData = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        } catch (Throwable th4) {
            th = th4;
            byteArrayOutputStream2 = byteArrayOutputStream;
            if (byteArrayOutputStream2 != null) {
                try {
                    byteArrayOutputStream2.close();
                } catch (Throwable th5) {
                    e.b().d(th5);
                }
            }
            throw th;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.actionUrl);
        parcel.writeString(this.schema);
        parcel.writeString(this.identify);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeByteArray(this.thumbData);
        parcel.writeTypedList(this.imageList);
    }
}
