package com.liulishuo.filedownloader.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import com.liulishuo.filedownloader.R;
import com.liulishuo.filedownloader.util.FileDownloadLog;

@TargetApi(26)
/* loaded from: classes.dex */
public class ForegroundServiceConfig {
    private static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "filedownloader_channel";
    private static final String DEFAULT_NOTIFICATION_CHANNEL_NAME = "Filedownloader";
    private static final int DEFAULT_NOTIFICATION_ID = 17301506;
    private boolean needRecreateChannelId;
    private Notification notification;
    private String notificationChannelId;
    private String notificationChannelName;
    private int notificationId;

    /* loaded from: classes.dex */
    public static class Builder {
        private boolean needRecreateChannelId;
        private Notification notification;
        private String notificationChannelId;
        private String notificationChannelName;
        private int notificationId;

        public ForegroundServiceConfig build() {
            ForegroundServiceConfig foregroundServiceConfig = new ForegroundServiceConfig();
            foregroundServiceConfig.setNotificationChannelId(this.notificationChannelId == null ? ForegroundServiceConfig.DEFAULT_NOTIFICATION_CHANNEL_ID : this.notificationChannelId);
            foregroundServiceConfig.setNotificationChannelName(this.notificationChannelName == null ? ForegroundServiceConfig.DEFAULT_NOTIFICATION_CHANNEL_NAME : this.notificationChannelName);
            foregroundServiceConfig.setNotificationId(this.notificationId == 0 ? 17301506 : this.notificationId);
            foregroundServiceConfig.setNeedRecreateChannelId(this.needRecreateChannelId);
            foregroundServiceConfig.setNotification(this.notification);
            return foregroundServiceConfig;
        }

        public Builder needRecreateChannelId(boolean z) {
            this.needRecreateChannelId = z;
            return this;
        }

        public Builder notification(Notification notification) {
            this.notification = notification;
            return this;
        }

        public Builder notificationChannelId(String str) {
            this.notificationChannelId = str;
            return this;
        }

        public Builder notificationChannelName(String str) {
            this.notificationChannelName = str;
            return this;
        }

        public Builder notificationId(int i) {
            this.notificationId = i;
            return this;
        }
    }

    private ForegroundServiceConfig() {
    }

    private Notification buildDefaultNotification(Context context) {
        String string = context.getString(R.string.default_filedownloader_notification_title);
        String string2 = context.getString(R.string.default_filedownloader_notification_content);
        Notification.Builder builder = new Notification.Builder(context, this.notificationChannelId);
        builder.setContentTitle(string).setContentText(string2).setSmallIcon(17301506);
        return builder.build();
    }

    public Notification getNotification(Context context) {
        if (this.notification == null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "build default notification", new Object[0]);
            }
            this.notification = buildDefaultNotification(context);
        }
        return this.notification;
    }

    public String getNotificationChannelId() {
        return this.notificationChannelId;
    }

    public String getNotificationChannelName() {
        return this.notificationChannelName;
    }

    public int getNotificationId() {
        return this.notificationId;
    }

    public boolean isNeedRecreateChannelId() {
        return this.needRecreateChannelId;
    }

    public void setNeedRecreateChannelId(boolean z) {
        this.needRecreateChannelId = z;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public void setNotificationChannelId(String str) {
        this.notificationChannelId = str;
    }

    public void setNotificationChannelName(String str) {
        this.notificationChannelName = str;
    }

    public void setNotificationId(int i) {
        this.notificationId = i;
    }

    public String toString() {
        return "ForegroundServiceConfig{notificationId=" + this.notificationId + ", notificationChannelId='" + this.notificationChannelId + "', notificationChannelName='" + this.notificationChannelName + "', notification=" + this.notification + ", needRecreateChannelId=" + this.needRecreateChannelId + '}';
    }
}
