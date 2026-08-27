package com.sina.weibo.sdk.api;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import cn.sharesdk.framework.utils.e;
import java.io.ByteArrayOutputStream;
import java.io.File;

/* loaded from: classes2.dex */
public class ImageObject implements Parcelable {
    public static final Parcelable.Creator<ImageObject> CREATOR = new Parcelable.Creator<ImageObject>() { // from class: com.sina.weibo.sdk.api.ImageObject.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ImageObject createFromParcel(Parcel parcel) {
            return new ImageObject(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ImageObject[] newArray(int i) {
            return new ImageObject[i];
        }
    };
    private static final int DATA_SIZE = 2097152;
    public byte[] imageData;
    public String imagePath;

    public ImageObject() {
    }

    public ImageObject(Parcel parcel) {
        this.imageData = parcel.createByteArray();
        this.imagePath = parcel.readString();
    }

    public boolean checkArgs() {
        if (this.imageData == null && this.imagePath == null) {
            new Throwable("imageData and imagePath are null").printStackTrace();
            return false;
        }
        if (this.imageData != null && this.imageData.length > DATA_SIZE) {
            new Throwable("imageData is too large").printStackTrace();
            return false;
        }
        if (this.imagePath != null && this.imagePath.length() > 512) {
            new Throwable("imagePath is too length").printStackTrace();
            return false;
        }
        if (this.imagePath == null) {
            return true;
        }
        try {
            File file = new File(this.imagePath);
            if (file.exists() && file.length() != 0 && file.length() <= 10485760) {
                return true;
            }
            new Throwable("checkArgs fail, image content is too large or not exists").printStackTrace();
            return false;
        } catch (Throwable unused) {
            new Throwable("checkArgs fail, image content is too large or not exists").printStackTrace();
            return false;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getObjType() {
        return 2;
    }

    public final void setImageObject(Bitmap bitmap) {
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
            this.imageData = byteArrayOutputStream.toByteArray();
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
        parcel.writeByteArray(this.imageData);
        parcel.writeString(this.imagePath);
    }
}
