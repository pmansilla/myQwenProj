package com.mob.apc;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

/* loaded from: classes.dex */
public class APCMessage {
    public Bundle data;
    public Object obj;
    public int what = -1;
    public int arg1 = -1;
    public int arg2 = -1;
    public int errorCode = -1;

    /* JADX WARN: Removed duplicated region for block: B:7:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.mob.apc.APCMessage readFromParcel(android.os.Parcel r3) {
        /*
            r2 = this;
            int r0 = r3.readInt()
            r2.what = r0
            int r0 = r3.readInt()
            r2.arg1 = r0
            int r0 = r3.readInt()
            r2.arg2 = r0
            int r0 = r3.readInt()
            r1 = 2
            if (r0 != r1) goto L24
            java.io.Serializable r0 = r3.readSerializable()
        L1d:
            r2.obj = r0
            int r0 = r3.readInt()
            goto L32
        L24:
            r1 = 3
            if (r0 != r1) goto L32
            java.lang.Class<com.mob.apc.APCMessage> r0 = com.mob.apc.APCMessage.class
            java.lang.ClassLoader r0 = r0.getClassLoader()
            android.os.Parcelable r0 = r3.readParcelable(r0)
            goto L1d
        L32:
            r1 = 1
            if (r0 != r1) goto L3b
            android.os.Bundle r3 = r3.readBundle()
            r2.data = r3
        L3b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.apc.APCMessage.readFromParcel(android.os.Parcel):com.mob.apc.APCMessage");
    }

    public String toString() {
        return "APCMessage{what=" + this.what + ", arg1=" + this.arg1 + ", arg2=" + this.arg2 + ", obj=" + this.obj + ", data=" + this.data + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.what);
        parcel.writeInt(this.arg1);
        parcel.writeInt(this.arg2);
        Object obj = this.obj;
        if (obj != null) {
            if (obj instanceof Serializable) {
                parcel.writeInt(2);
                parcel.writeSerializable((Serializable) this.obj);
            } else if (obj instanceof Parcelable) {
                parcel.writeInt(3);
                parcel.writeParcelable((Parcelable) this.obj, i);
            }
        }
        if (this.data == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            parcel.writeBundle(this.data);
        }
    }
}
