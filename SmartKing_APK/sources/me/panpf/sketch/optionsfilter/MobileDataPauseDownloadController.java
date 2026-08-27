package me.panpf.sketch.optionsfilter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import java.lang.ref.WeakReference;
import me.panpf.sketch.Configuration;

/* loaded from: classes2.dex */
public class MobileDataPauseDownloadController {
    private Configuration configuration;
    private boolean opened;
    private NetworkChangedBroadcastReceiver receiver;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class NetworkChangedBroadcastReceiver extends BroadcastReceiver {
        private Context context;
        private WeakReference<MobileDataPauseDownloadController> weakReference;

        public NetworkChangedBroadcastReceiver(Context context, MobileDataPauseDownloadController mobileDataPauseDownloadController) {
            this.context = context.getApplicationContext();
            this.weakReference = new WeakReference<>(mobileDataPauseDownloadController);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register() {
            try {
                this.context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregister() {
            try {
                this.context.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            MobileDataPauseDownloadController mobileDataPauseDownloadController;
            if (!intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") || (mobileDataPauseDownloadController = this.weakReference.get()) == null) {
                return;
            }
            mobileDataPauseDownloadController.updateStatus(context);
        }
    }

    public MobileDataPauseDownloadController(Configuration configuration) {
        this.receiver = new NetworkChangedBroadcastReceiver(configuration.getContext(), this);
        this.configuration = configuration;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean z = true;
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable() || (activeNetworkInfo.getType() != 0 && (activeNetworkInfo.getType() != 1 || Build.VERSION.SDK_INT < 16 || !connectivityManager.isActiveNetworkMetered()))) {
            z = false;
        }
        this.configuration.setPauseDownloadEnabled(z);
    }

    public boolean isOpened() {
        return this.opened;
    }

    public void setOpened(boolean z) {
        if (this.opened == z) {
            return;
        }
        this.opened = z;
        if (this.opened) {
            updateStatus(this.receiver.context);
            this.receiver.register();
        } else {
            this.configuration.setPauseDownloadEnabled(false);
            this.receiver.unregister();
        }
    }
}
