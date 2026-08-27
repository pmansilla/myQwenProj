package no.nordicsemi.android.support.v18.scanner;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/* loaded from: classes2.dex */
public class ScannerService extends Service {
    static final String EXTRA_FILTERS = "no.nordicsemi.android.support.v18.EXTRA_FILTERS";
    static final String EXTRA_PENDING_INTENT = "no.nordicsemi.android.support.v18.EXTRA_PENDING_INTENT";
    static final String EXTRA_SETTINGS = "no.nordicsemi.android.support.v18.EXTRA_SETTINGS";
    static final String EXTRA_START = "no.nordicsemi.android.support.v18.EXTRA_START";
    private static final String TAG = "ScannerService";

    @NonNull
    private final Object LOCK = new Object();
    private HashMap<PendingIntent, ScanCallback> callbacks;
    private Handler handler;

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    private void startScan(@NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull PendingIntent pendingIntent) {
        PendingIntentExecutor pendingIntentExecutor = new PendingIntentExecutor(pendingIntent, scanSettings, this);
        synchronized (this.LOCK) {
            this.callbacks.put(pendingIntent, pendingIntentExecutor);
        }
        try {
            BluetoothLeScannerCompat.getScanner().startScanInternal(list, scanSettings, pendingIntentExecutor, this.handler);
        } catch (Exception e) {
            Log.e(TAG, "Starting scanning failed", e);
        }
    }

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    private void stopScan(@NonNull PendingIntent pendingIntent) {
        ScanCallback remove;
        boolean isEmpty;
        synchronized (this.LOCK) {
            remove = this.callbacks.remove(pendingIntent);
            isEmpty = this.callbacks.isEmpty();
        }
        if (remove == null) {
            return;
        }
        try {
            BluetoothLeScannerCompat.getScanner().stopScan(remove);
        } catch (Exception e) {
            Log.e(TAG, "Stopping scanning failed", e);
        }
        if (isEmpty) {
            stopSelf();
        }
    }

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.callbacks = new HashMap<>();
        this.handler = new Handler();
    }

    @Override // android.app.Service
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public void onDestroy() {
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        Iterator<ScanCallback> it = this.callbacks.values().iterator();
        while (it.hasNext()) {
            try {
                scanner.stopScan(it.next());
            } catch (Exception unused) {
            }
        }
        this.callbacks.clear();
        this.callbacks = null;
        this.handler = null;
        super.onDestroy();
    }

    @Override // android.app.Service
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public int onStartCommand(Intent intent, int i, int i2) {
        boolean containsKey;
        boolean isEmpty;
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra(EXTRA_PENDING_INTENT);
        boolean booleanExtra = intent.getBooleanExtra(EXTRA_START, false);
        boolean z = !booleanExtra;
        if (pendingIntent == null) {
            synchronized (this.LOCK) {
                isEmpty = this.callbacks.isEmpty();
            }
            if (isEmpty) {
                stopSelf();
            }
            return 2;
        }
        synchronized (this.LOCK) {
            containsKey = this.callbacks.containsKey(pendingIntent);
        }
        if (booleanExtra && !containsKey) {
            List<ScanFilter> parcelableArrayListExtra = intent.getParcelableArrayListExtra(EXTRA_FILTERS);
            ScanSettings scanSettings = (ScanSettings) intent.getParcelableExtra(EXTRA_SETTINGS);
            if (parcelableArrayListExtra == null) {
                parcelableArrayListExtra = Collections.emptyList();
            }
            if (scanSettings == null) {
                scanSettings = new ScanSettings.Builder().build();
            }
            startScan(parcelableArrayListExtra, scanSettings, pendingIntent);
        } else if (z && containsKey) {
            stopScan(pendingIntent);
        }
        return 2;
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
    }
}
