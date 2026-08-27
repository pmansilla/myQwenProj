package com.mob.apc.impl;

import android.os.Parcel;
import com.mob.apc.APCException;
import com.mob.apc.APCMessage;
import com.mob.apc.MobAPC;

/* loaded from: classes.dex */
public class InnerMessage {
    public APCMessage apcMessage;
    public String businessID;
    public long timeout;
    public APCException apcException = null;
    public String pkg = MobAPC.getContext().getPackageName();

    public InnerMessage(APCMessage aPCMessage, String str, long j) {
        this.timeout = -1L;
        this.apcMessage = aPCMessage;
        this.businessID = str;
        this.timeout = j;
    }

    public static InnerMessage createFromParcel(Parcel parcel) {
        InnerMessage innerMessage = new InnerMessage(null, null, parcel.readLong());
        int readInt = parcel.readInt();
        if (readInt == 1) {
            innerMessage.apcMessage = new APCMessage().readFromParcel(parcel);
            readInt = parcel.readInt();
        }
        if (readInt == 2) {
            innerMessage.businessID = parcel.readString();
            readInt = parcel.readInt();
        }
        if (readInt == 3) {
            innerMessage.pkg = parcel.readString();
        }
        return innerMessage;
    }

    public String toString() {
        return "InnerMessage{apcMessage=" + this.apcMessage + ", businessID='" + this.businessID + "', pkg='" + this.pkg + "'}";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timeout);
        if (this.apcMessage != null) {
            parcel.writeInt(1);
            this.apcMessage.writeToParcel(parcel, i);
        }
        if (this.businessID != null) {
            parcel.writeInt(2);
            parcel.writeString(this.businessID);
        }
        this.pkg = MobAPC.getContext().getPackageName();
        parcel.writeInt(3);
        parcel.writeString(this.pkg);
    }
}
