package com.czw.smartkit.netModule;

import android.widget.Toast;
import com.czw.smartkit.base.BaseActivity;
import ycnet.runchinaup.log.ycNetLog;

/* loaded from: classes.dex */
public class YCNetCodeParserHelper {
    private static BaseActivity mActivity;
    private static YCNetCodeParserHelper netCodeParserHelper = new YCNetCodeParserHelper();
    Toast toast = null;

    private YCNetCodeParserHelper() {
    }

    public static YCNetCodeParserHelper getNetCodeParserHelper() {
        return netCodeParserHelper;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void handErrorCode(int r3) {
        /*
            r2 = this;
            r0 = 1000(0x3e8, float:1.401E-42)
            if (r3 >= r0) goto L5
            goto L49
        L5:
            r0 = 2000(0x7d0, float:2.803E-42)
            if (r3 >= r0) goto L19
            switch(r3) {
                case 1002: goto L49;
                case 1003: goto L49;
                case 1004: goto L49;
                case 1005: goto L49;
                case 1006: goto L49;
                case 1007: goto L49;
                default: goto Lc;
            }
        Lc:
            switch(r3) {
                case 1010: goto L49;
                case 1011: goto L49;
                case 1012: goto L49;
                case 1013: goto L49;
                default: goto Lf;
            }
        Lf:
            switch(r3) {
                case 1020: goto L49;
                case 1021: goto L49;
                default: goto L12;
            }
        L12:
            switch(r3) {
                case 1029: goto L49;
                case 1030: goto L49;
                case 1031: goto L49;
                case 1032: goto L49;
                case 1033: goto L49;
                case 1034: goto L49;
                default: goto L15;
            }
        L15:
            switch(r3) {
                case 1040: goto L49;
                case 1041: goto L49;
                default: goto L18;
            }
        L18:
            goto L49
        L19:
            r0 = 3000(0xbb8, float:4.204E-42)
            if (r3 >= r0) goto L2f
            r0 = 2020(0x7e4, float:2.83E-42)
            if (r3 == r0) goto L49
            r0 = 2040(0x7f8, float:2.859E-42)
            if (r3 == r0) goto L49
            switch(r3) {
                case 2000: goto L49;
                case 2001: goto L49;
                case 2002: goto L49;
                case 2003: goto L49;
                case 2004: goto L49;
                case 2005: goto L49;
                case 2006: goto L49;
                case 2007: goto L49;
                case 2008: goto L49;
                case 2009: goto L49;
                case 2010: goto L49;
                case 2011: goto L49;
                case 2012: goto L49;
                case 2013: goto L49;
                case 2014: goto L49;
                default: goto L28;
            }
        L28:
            switch(r3) {
                case 2022: goto L49;
                case 2023: goto L49;
                case 2024: goto L49;
                case 2025: goto L49;
                default: goto L2b;
            }
        L2b:
            switch(r3) {
                case 2030: goto L49;
                case 2031: goto L49;
                case 2032: goto L49;
                default: goto L2e;
            }
        L2e:
            goto L49
        L2f:
            r0 = 4000(0xfa0, float:5.605E-42)
            if (r3 >= r0) goto L37
            switch(r3) {
                case 3000: goto L49;
                case 3001: goto L49;
                case 3002: goto L49;
                case 3003: goto L49;
                case 3004: goto L49;
                case 3005: goto L49;
                case 3006: goto L49;
                case 3007: goto L49;
                case 3008: goto L49;
                case 3009: goto L49;
                case 3010: goto L49;
                case 3011: goto L49;
                case 3012: goto L49;
                case 3013: goto L49;
                default: goto L36;
            }
        L36:
            goto L49
        L37:
            r0 = 5000(0x1388, float:7.006E-42)
            if (r3 >= r0) goto L3f
            switch(r3) {
                case 4000: goto L49;
                case 4001: goto L49;
                case 4002: goto L3e;
                case 4003: goto L49;
                case 4004: goto L49;
                default: goto L3e;
            }
        L3e:
            goto L49
        L3f:
            r0 = 6000(0x1770, float:8.408E-42)
            if (r3 >= r0) goto L47
            switch(r3) {
                case 5000: goto L49;
                case 5001: goto L49;
                case 5002: goto L49;
                case 5003: goto L49;
                case 5004: goto L49;
                case 5005: goto L49;
                case 5006: goto L49;
                case 5007: goto L49;
                case 5008: goto L49;
                case 5009: goto L49;
                case 5010: goto L49;
                case 5011: goto L49;
                case 5012: goto L49;
                case 5013: goto L49;
                case 5014: goto L49;
                case 5015: goto L49;
                case 5016: goto L49;
                case 5017: goto L49;
                case 5018: goto L49;
                case 5019: goto L49;
                case 5020: goto L49;
                case 5021: goto L49;
                case 5022: goto L49;
                case 5023: goto L49;
                case 5024: goto L49;
                case 5025: goto L49;
                default: goto L46;
            }
        L46:
            goto L49
        L47:
            r1 = 7000(0x1b58, float:9.809E-42)
        L49:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.smartkit.netModule.YCNetCodeParserHelper.handErrorCode(int):void");
    }

    public static void init(BaseActivity baseActivity) {
        mActivity = baseActivity;
    }

    public void onErrorCode(final int i, final String str) {
        ycNetLog.e("debug===code==>" + i);
        if (mActivity != null && 1011 != i) {
            mActivity.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.netModule.YCNetCodeParserHelper.1
                @Override // java.lang.Runnable
                public void run() {
                    if (YCNetCodeParserHelper.this.toast == null) {
                        YCNetCodeParserHelper.this.toast = Toast.makeText(YCNetCodeParserHelper.mActivity, i + ":" + str, 0);
                    } else {
                        YCNetCodeParserHelper.this.toast.setText(i + ":" + str);
                        YCNetCodeParserHelper.this.toast.setDuration(0);
                    }
                    YCNetCodeParserHelper.this.toast.show();
                }
            });
        }
        if (mActivity != null) {
            mActivity.dismissLoadingDialog();
        }
        handErrorCode(i);
    }
}
