package com.czw.smartkit.liveservice;

import android.app.Service;

/* loaded from: classes.dex */
abstract class BaseService extends Service {
    protected abstract void init();

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        init();
    }
}
