package com.czw.modes.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/* loaded from: classes.dex */
public class NotifyManager {

    /* loaded from: classes.dex */
    public static class NotifyBuilder {
        private Context context;
        NotificationCompat.Builder nb;
        private int number = 0;

        private NotifyBuilder(Context context, int i) {
            this.nb = null;
            this.context = null;
            this.context = context;
            this.nb = new NotificationCompat.Builder(context);
            this.nb.setSmallIcon(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Notification build() {
            return this.nb.build();
        }

        public static NotifyBuilder create(int i, Context context) {
            return new NotifyBuilder(context, i);
        }

        public NotifyBuilder setInfo(String str, String str2) {
            this.nb.setContentTitle(str);
            this.nb.setContentInfo(str2);
            return this;
        }

        public NotifyBuilder setNumber(int i) {
            this.number = i;
            return this;
        }
    }

    private NotifyManager() {
    }

    public static void sendNotify(NotifyBuilder notifyBuilder) {
        ((NotificationManager) notifyBuilder.context.getSystemService("notification")).notify(notifyBuilder.number, notifyBuilder.build());
    }
}
