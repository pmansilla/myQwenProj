package com.sina.weibo.sdk.api;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import cn.sharesdk.framework.utils.e;
import java.io.ByteArrayOutputStream;

/* loaded from: classes2.dex */
public class WebpageObject implements Parcelable {
    public static final Parcelable.Creator<WebpageObject> CREATOR = new Parcelable.Creator<WebpageObject>() { // from class: com.sina.weibo.sdk.api.WebpageObject.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebpageObject createFromParcel(Parcel parcel) {
            return new WebpageObject(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebpageObject[] newArray(int i) {
            return new WebpageObject[i];
        }
    };
    public static final String EXTRA_KEY_DEFAULTTEXT = "extra_key_defaulttext";
    public String actionUrl;
    public String defaultText;
    public String description;
    public String identify;
    public String schema;
    public byte[] thumbData;
    public String title;

    public WebpageObject() {
    }

    public WebpageObject(Parcel parcel) {
        this.actionUrl = parcel.readString();
        this.schema = parcel.readString();
        this.identify = parcel.readString();
        this.title = parcel.readString();
        this.description = parcel.readString();
        this.thumbData = parcel.createByteArray();
    }

    public boolean checkArgs() {
        if (this.actionUrl == null || this.actionUrl.length() > 512) {
            e.b().d("checkArgs fail, actionUrl is invalid", new Object[0]);
            return false;
        }
        if (this.identify == null || this.identify.length() > 512) {
            e.b().d("checkArgs fail, identify is invalid", new Object[0]);
            return false;
        }
        if (this.thumbData == null || this.thumbData.length > 32768) {
            new Throwable("checkArgs fail, thumbData is invalid,size is " + (this.thumbData != null ? this.thumbData.length : -1) + "! more then 32768.").printStackTrace();
            return false;
        }
        if (this.title == null || this.title.length() > 512) {
            e.b().d("checkArgs fail, title is invalid", new Object[0]);
            return false;
        }
        if (this.description != null && this.description.length() <= 1024) {
            return true;
        }
        e.b().d("checkArgs fail, description is invalid", new Object[0]);
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getObjType() {
        return 5;
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
    }
}
