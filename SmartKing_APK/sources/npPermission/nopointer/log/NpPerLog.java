package npPermission.nopointer.log;

import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes2.dex */
public class NpPerLog {
    public static boolean enableLog = true;
    private static NpPerLogPrinter mNpPerLogPrinter;

    /* loaded from: classes2.dex */
    public interface NpPerLogPrinter {
        String initTag();

        void onLogPrint(String str);

        void onLogPrint(String str, String str2);
    }

    public static void log(String str) {
        if (enableLog) {
            if (mNpPerLogPrinter != null) {
                log(mNpPerLogPrinter.initTag(), str);
            } else {
                log("NpPerLog", str);
            }
        }
    }

    public static void log(String str, String str2) {
        if (enableLog) {
            if (TextUtils.isEmpty(str)) {
                str = "NpPerLog";
            }
            if (mNpPerLogPrinter == null) {
                Log.e(str, str2);
            } else {
                mNpPerLogPrinter.onLogPrint(str, str2);
            }
        }
    }

    public static void setNpPerLogPrinter(NpPerLogPrinter npPerLogPrinter) {
        mNpPerLogPrinter = npPerLogPrinter;
    }
}
