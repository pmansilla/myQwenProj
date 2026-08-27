package com.liulishuo.filedownloader.services;

import android.content.Intent;
import android.os.IBinder;

/* loaded from: classes.dex */
interface IFileDownloadServiceHandler {
    IBinder onBind(Intent intent);

    void onDestroy();

    void onStartCommand(Intent intent, int i, int i2);
}
