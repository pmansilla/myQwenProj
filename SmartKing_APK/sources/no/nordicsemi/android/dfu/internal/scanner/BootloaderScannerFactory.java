package no.nordicsemi.android.dfu.internal.scanner;

import android.os.Build;

/* loaded from: classes2.dex */
public class BootloaderScannerFactory {
    public static BootloaderScanner getScanner() {
        return Build.VERSION.SDK_INT >= 21 ? new BootloaderScannerLollipop() : new BootloaderScannerJB();
    }
}
