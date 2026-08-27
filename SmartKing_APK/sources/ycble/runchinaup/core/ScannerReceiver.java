package ycble.runchinaup.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.List;
import me.panpf.sketch.uri.FileUriModel;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes2.dex */
public class ScannerReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) {
            ycBleLog.e("intent.getAction() = null");
            return;
        }
        int intExtra = intent.getIntExtra(BluetoothLeScannerCompat.EXTRA_ERROR_CODE, -1);
        int intExtra2 = intent.getIntExtra(BluetoothLeScannerCompat.EXTRA_CALLBACK_TYPE, -1);
        List list = (List) intent.getSerializableExtra(BluetoothLeScannerCompat.EXTRA_LIST_SCAN_RESULT);
        if (list == null || list.size() < 1) {
            return;
        }
        ycBleLog.i("====MyReceiver==" + intExtra + FileUriModel.SCHEME + intExtra2 + "==>批量==>" + list.size());
    }
}
