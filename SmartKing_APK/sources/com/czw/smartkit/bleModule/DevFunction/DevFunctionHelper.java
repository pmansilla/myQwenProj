package com.czw.smartkit.bleModule.DevFunction;

import com.czw.smartkit.bleModule.data.DevFunctionEntity;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class DevFunctionHelper {
    private static final DevFunctionHelper ourInstance = new DevFunctionHelper();
    private DevFunctionEntity devFunctionEntity = null;
    private HashSet<DeviceFunctionCallback> deviceFunctionCallbackHashSet = new HashSet<>();

    /* loaded from: classes.dex */
    public interface DeviceFunctionCallback {
        void onGetFunction(DevFunctionEntity devFunctionEntity);
    }

    private DevFunctionHelper() {
    }

    public static DevFunctionHelper getInstance() {
        return ourInstance;
    }

    public DevFunctionEntity getDevFunctionEntity() {
        if (this.devFunctionEntity == null) {
            this.devFunctionEntity = new DevFunctionEntity();
        }
        return this.devFunctionEntity;
    }

    public void notifyDeviceFunction(DevFunctionEntity devFunctionEntity) {
        this.devFunctionEntity = devFunctionEntity;
        Iterator<DeviceFunctionCallback> it = this.deviceFunctionCallbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onGetFunction(devFunctionEntity);
        }
    }

    public void registerDeviceFunctionCallback(DeviceFunctionCallback deviceFunctionCallback) {
        if (this.deviceFunctionCallbackHashSet.contains(deviceFunctionCallback)) {
            return;
        }
        this.deviceFunctionCallbackHashSet.add(deviceFunctionCallback);
    }

    public void unRegisterDeviceFunctionCallback(DeviceFunctionCallback deviceFunctionCallback) {
        if (this.deviceFunctionCallbackHashSet.contains(deviceFunctionCallback)) {
            this.deviceFunctionCallbackHashSet.remove(deviceFunctionCallback);
        }
    }
}
