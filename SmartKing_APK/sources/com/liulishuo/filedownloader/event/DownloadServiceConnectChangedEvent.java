package com.liulishuo.filedownloader.event;

/* loaded from: classes.dex */
public class DownloadServiceConnectChangedEvent extends IDownloadEvent {
    public static final String ID = "event.service.connect.changed";
    private final Class<?> serviceClass;
    private final ConnectStatus status;

    /* loaded from: classes.dex */
    public enum ConnectStatus {
        connected,
        disconnected,
        lost
    }

    public DownloadServiceConnectChangedEvent(ConnectStatus connectStatus, Class<?> cls) {
        super(ID);
        this.status = connectStatus;
        this.serviceClass = cls;
    }

    public ConnectStatus getStatus() {
        return this.status;
    }

    public boolean isSuchService(Class<?> cls) {
        return this.serviceClass != null && this.serviceClass.getName().equals(cls.getName());
    }
}
