package cn.smssdk.entity;

import com.mob.tools.proguard.EverythingKeeper;
import com.mob.tools.utils.Hashon;
import java.io.Serializable;

/* loaded from: classes.dex */
public class BaseEntity implements Serializable, EverythingKeeper {
    public String toJSONString() {
        return new Hashon().fromObject(this);
    }
}
