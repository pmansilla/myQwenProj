package com.czw.smartkit.liveservice;

import android.os.RemoteException;
import com.czw.smartkit.liveservice.RunServiceInterface;

/* loaded from: classes.dex */
public class InnerBinder extends RunServiceInterface.Stub {
    private String name;

    public InnerBinder(String str) {
        this.name = str;
    }

    @Override // com.czw.smartkit.liveservice.RunServiceInterface
    public String getServiceName() throws RemoteException {
        return this.name;
    }
}
