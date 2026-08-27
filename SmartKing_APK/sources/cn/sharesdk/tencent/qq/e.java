package cn.sharesdk.tencent.qq;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.mob.MobSDK;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;
import java.io.File;

/* compiled from: ShareActivity.java */
/* loaded from: classes.dex */
public class e extends FakeActivity {
    private Platform a;
    private String b;
    private PlatformActionListener c;

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int i) {
        String b = b(str, str2, str3, str4, str5, str6, str7, str8, i);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(b));
        try {
            int[] a = a();
            ReceiveActivity.setUriScheme(this.b);
            ReceiveActivity.setPlatformActionListener(this.c);
            if (a.length <= 1 || (a[0] < 4 && a[1] < 6)) {
                intent.putExtra("key_request_code", 0);
            }
            intent.putExtra("pkg_name", this.activity.getPackageName());
            if (Build.VERSION.SDK_INT >= 28) {
                intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            }
            this.activity.startActivityForResult(intent, 256);
        } catch (Throwable th) {
            if (this.c != null) {
                this.c.onError(this.a, 9, th);
            }
            this.activity.finish();
        }
    }

    private int[] a() {
        String str;
        try {
            str = MobSDK.getContext().getPackageManager().getPackageInfo("com.tencent.mobileqq", 0).versionName;
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            str = AmapLoc.RESULT_TYPE_GPS;
        }
        String[] split = str.split("\\.");
        int[] iArr = new int[split.length];
        for (int i = 0; i < iArr.length; i++) {
            try {
                iArr[i] = ResHelper.parseInt(split[i]);
            } catch (Throwable th2) {
                cn.sharesdk.framework.utils.e.b().d(th2);
                iArr[i] = 0;
            }
        }
        return iArr;
    }

    private String b(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int i) {
        String str9;
        String str10 = "mqqapi://share/to_fri?src_type=app&version=1&file_type=news";
        if (!TextUtils.isEmpty(str4)) {
            if (!TextUtils.isEmpty(str5)) {
                str4 = "";
            } else if (str4.startsWith("https")) {
                try {
                    str5 = BitmapHelper.downloadBitmap(this.activity, str4);
                    str4 = "";
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().d(th);
                    str5 = null;
                }
            }
        }
        if (!TextUtils.isEmpty(str4)) {
            str10 = "mqqapi://share/to_fri?src_type=app&version=1&file_type=news&image_url=" + Base64.encodeToString(str4.getBytes(), 2);
        }
        if (!TextUtils.isEmpty(str5)) {
            str10 = str10 + "&file_data=" + Base64.encodeToString(str5.getBytes(), 2);
        }
        if (!TextUtils.isEmpty(str)) {
            str10 = str10 + "&title=" + Base64.encodeToString(str.getBytes(), 2);
        }
        if (!TextUtils.isEmpty(str3)) {
            str10 = str10 + "&description=" + Base64.encodeToString(str3.getBytes(), 2);
        }
        if (!TextUtils.isEmpty(str7)) {
            if (str7.length() > 20) {
                str7 = str7.substring(0, 20) + "...";
            }
            str10 = str10 + "&app_name=" + Base64.encodeToString(str7.getBytes(), 2);
        }
        if (!TextUtils.isEmpty(str8)) {
            str10 = str10 + "&share_id=" + str8;
        }
        if (!TextUtils.isEmpty(str2)) {
            str10 = str10 + "&url=" + Base64.encodeToString(str2.getBytes(), 2);
        }
        if (!TextUtils.isEmpty(str3)) {
            str10 = str10 + "&share_qq_ext_str=" + Base64.encodeToString(str3.getBytes(), 2);
        }
        if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str3) && TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str5)) {
            str9 = str10 + "&req_type=" + Base64.encodeToString(AmapLoc.RESULT_TYPE_SELF_LAT_LON.getBytes(), 2);
        } else if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str3) && TextUtils.isEmpty(str2)) {
            str9 = str10 + "&req_type=" + Base64.encodeToString(AmapLoc.RESULT_TYPE_NO_LONGER_USED.getBytes(), 2);
        } else if (TextUtils.isEmpty(str6)) {
            str9 = str10 + "&req_type=" + Base64.encodeToString(AmapLoc.RESULT_TYPE_WIFI_ONLY.getBytes(), 2);
        } else {
            str9 = (str10 + "&req_type=" + Base64.encodeToString(AmapLoc.RESULT_TYPE_FUSED.getBytes(), 2)) + "&audioUrl=" + Base64.encodeToString(str6.getBytes(), 2);
        }
        return str9 + "&cflag=" + Base64.encodeToString((i == 1 ? "10" : "00").getBytes(), 2);
    }

    public void a(Platform platform, PlatformActionListener platformActionListener) {
        this.a = platform;
        this.c = platformActionListener;
    }

    public void a(String str) {
        this.b = "tencent" + str;
    }

    @Override // com.mob.tools.FakeActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 256 && i2 == 0 && Build.VERSION.SDK_INT < 28) {
            this.c.onCancel(this.a, 9);
        }
        finish();
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [cn.sharesdk.tencent.qq.e$1] */
    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        Bundle extras = this.activity.getIntent().getExtras();
        final String string = extras.getString("title");
        final String string2 = extras.getString("titleUrl");
        final String string3 = extras.getString("summary");
        final String string4 = extras.getString("imageUrl");
        final String string5 = extras.getString("musicUrl");
        final String appName = DeviceHelper.getInstance(this.activity).getAppName();
        final String string6 = extras.getString("appId");
        final int i = extras.getInt("hidden");
        String string7 = extras.getString("imagePath");
        if (TextUtils.isEmpty(string) && TextUtils.isEmpty(string3) && TextUtils.isEmpty(string2) && ((TextUtils.isEmpty(string7) || !new File(string7).exists()) && !TextUtils.isEmpty(string4))) {
            new Thread() { // from class: cn.sharesdk.tencent.qq.e.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    String str;
                    try {
                        try {
                            str = BitmapHelper.downloadBitmap(e.this.activity, string4);
                        } catch (Throwable th) {
                            cn.sharesdk.framework.utils.e.b().d(th);
                            str = null;
                        }
                        e.this.a(string, string2, string3, string4, str, string5, appName, string6, i);
                    } catch (Throwable th2) {
                        cn.sharesdk.framework.utils.e.b().d(th2);
                    }
                }
            }.start();
        } else {
            a(string, string2, string3, string4, string7, string5, appName, string6, i);
        }
    }
}
