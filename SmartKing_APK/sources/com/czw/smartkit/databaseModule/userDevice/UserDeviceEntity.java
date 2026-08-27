package com.czw.smartkit.databaseModule.userDevice;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;
import java.io.Serializable;

/* loaded from: classes.dex */
public class UserDeviceEntity implements Serializable {
    private String mac;
    private String name;

    @PrimaryKey(AssignType.BY_MYSELF)
    private String uid;

    public String getMac() {
        return this.mac;
    }

    public String getName() {
        return this.name;
    }

    public String getUid() {
        return this.uid;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    public String toString() {
        return "UserDeviceEntity{uid='" + this.uid + "', mac='" + this.mac + "', name='" + this.name + "'}";
    }
}
