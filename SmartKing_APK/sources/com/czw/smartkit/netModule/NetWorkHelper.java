package com.czw.smartkit.netModule;

import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class NetWorkHelper {
    private static final NetWorkHelper ourInstance = new NetWorkHelper();
    private HashSet<OnNetWorkListener> onNetWorkListeners = new HashSet<>();

    /* loaded from: classes.dex */
    public interface OnNetWorkListener {
        void onNetNotAvailable();
    }

    private NetWorkHelper() {
    }

    public static NetWorkHelper getInstance() {
        return ourInstance;
    }

    public void notifyNetNotAvailable() {
        Iterator<OnNetWorkListener> it = this.onNetWorkListeners.iterator();
        while (it.hasNext()) {
            it.next().onNetNotAvailable();
        }
    }

    public void registerListener(OnNetWorkListener onNetWorkListener) {
        if (this.onNetWorkListeners.contains(onNetWorkListener)) {
            return;
        }
        this.onNetWorkListeners.add(onNetWorkListener);
    }

    public void unRegisterListener(OnNetWorkListener onNetWorkListener) {
        if (this.onNetWorkListeners.contains(onNetWorkListener)) {
            this.onNetWorkListeners.remove(onNetWorkListener);
        }
    }
}
