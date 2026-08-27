package cn.sharesdk.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.DeviceHelper;
import io.reactivex.annotations.SchedulerSupport;
import ycble.runchinaup.ota.absimpl.htx.SampleGattAttributes;

/* compiled from: InviteActivity.java */
/* loaded from: classes.dex */
public class e extends FakeActivity {
    private PlatformActionListener a;
    private Platform b;
    private String c;
    private Platform.ShareParams d;

    public Intent a() {
        Intent intent = new Intent("com.facebook.platform.PLATFORM_ACTIVITY");
        intent.setPackage("com.facebook.katana");
        intent.addCategory("android.intent.category.DEFAULT");
        Bundle bundle = new Bundle();
        bundle.putString("app_link_url", this.d.getUrl());
        bundle.putString("preview_image_url", this.d.getImageUrl());
        intent.putExtra("com.facebook.platform.protocol.PROTOCOL_VERSION", 20160327).putExtra("com.facebook.platform.protocol.PROTOCOL_ACTION", "com.facebook.platform.action.request.APPINVITES_DIALOG").putExtra("com.facebook.platform.extra.APPLICATION_ID", this.c);
        Bundle bundle2 = new Bundle();
        bundle2.putString("action_id", "cf61947c-a8fe-4fa3-aa7c-fbeb7f291352");
        DeviceHelper deviceHelper = DeviceHelper.getInstance(getContext());
        String appName = deviceHelper.getAppName();
        if (!TextUtils.isEmpty(appName) && deviceHelper.getNetworkTypeForStatic().equals(SchedulerSupport.NONE)) {
            bundle2.putString(SampleGattAttributes.SP_APP_NAME, appName);
        }
        intent.putExtra("com.facebook.platform.protocol.BRIDGE_ARGS", bundle2);
        intent.putExtra("com.facebook.platform.protocol.METHOD_ARGS", bundle);
        return intent;
    }

    public void a(PlatformActionListener platformActionListener, Platform platform, Platform.ShareParams shareParams) {
        this.a = platformActionListener;
        this.b = platform;
        this.d = shareParams;
    }

    public void a(String str) {
        this.c = str;
    }

    @Override // com.mob.tools.FakeActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        finish();
        if (intent == null) {
            if (i == 64206 && i2 == 0) {
                this.a.onCancel(this.b, 9);
                return;
            } else {
                this.a.onError(this.b, 9, new Throwable("share error!"));
                return;
            }
        }
        Bundle bundleExtra = intent.getBundleExtra("com.facebook.platform.protocol.BRIDGE_ARGS");
        if (bundleExtra != null) {
            for (String str : bundleExtra.keySet()) {
                if (str.equals("error")) {
                    Bundle bundle = (Bundle) bundleExtra.get(str);
                    if (bundle != null) {
                        String str2 = "";
                        for (String str3 : bundle.keySet()) {
                            str2 = str2 + str3 + ":" + bundle.get(str3) + ", ";
                        }
                        if (str2.indexOf("UserCanceled") > -1) {
                            this.a.onCancel(this.b, 9);
                        }
                        this.a.onError(this.b, 9, new Throwable(str2));
                        return;
                    }
                    return;
                }
            }
        }
        Bundle bundleExtra2 = intent.getBundleExtra("com.facebook.platform.protocol.RESULT_ARGS");
        if (bundleExtra2 != null) {
            boolean z = bundleExtra2.getInt("didComplete") == 1;
            String string = bundleExtra2.getString("completionGesture");
            if (TextUtils.isEmpty(string)) {
                if (z) {
                    this.a.onComplete(this.b, 9, null);
                }
            } else if (string.equalsIgnoreCase("cancel")) {
                this.a.onCancel(this.b, 9);
            }
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        super.onCreate();
        try {
            if (a() != null) {
                this.activity.startActivityForResult(a(), 64206);
            }
        } catch (Throwable th) {
            finish();
            this.a.onError(this.b, 9, th);
        }
    }
}
