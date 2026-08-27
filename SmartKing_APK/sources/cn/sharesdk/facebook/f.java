package cn.sharesdk.facebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import com.mob.MobSDK;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;
import io.reactivex.annotations.SchedulerSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ycble.runchinaup.ota.absimpl.htx.SampleGattAttributes;

/* compiled from: ShareActivity.java */
/* loaded from: classes.dex */
public class f extends FakeActivity {
    private PlatformActionListener a;
    private Platform b;
    private Platform.ShareParams c;
    private String d;

    public Intent a() {
        Intent intent = new Intent("com.facebook.platform.PLATFORM_ACTIVITY");
        intent.setPackage("com.facebook.katana");
        intent.addCategory("android.intent.category.DEFAULT");
        Bundle bundle = new Bundle();
        bundle.putBoolean("DATA_FAILURES_FATAL", false);
        bundle.putString("TITLE", this.c.getTitle());
        if (!TextUtils.isEmpty(this.c.getUrl())) {
            bundle.putString("LINK", this.b.getShortLintk(this.c.getUrl(), false));
            bundle.putString("type", "LINK");
        } else if (!TextUtils.isEmpty(this.c.getFilePath())) {
            bundle.putString("VIDEO", Uri.fromFile(new File(this.c.getFilePath())).toString());
            bundle.putString("type", "VIDEO");
            bundle.putString("DESCRIPTION", this.c.getText());
            bundle.putString("TITLE", this.c.getTitle());
        } else if (this.c.getImageArray() != null && this.c.getImageArray().length > 0) {
            try {
                ArrayList<String> arrayList = new ArrayList<>();
                List arrayList2 = new ArrayList();
                if (this.c.getImageArray() != null) {
                    arrayList2 = Arrays.asList(this.c.getImageArray());
                }
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    File file = new File((String) it.next());
                    if (file.exists()) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            arrayList.add(ResHelper.pathToContentUri(MobSDK.getContext(), file.getAbsolutePath()).toString());
                        } else {
                            arrayList.add(Uri.fromFile(file).toString());
                        }
                    }
                }
                bundle.putStringArrayList("PHOTOS", arrayList);
                bundle.putString("DESCRIPTION", this.c.getText());
                bundle.putString("NAME", this.c.getTitle());
            } catch (Throwable th) {
                cn.sharesdk.framework.utils.e.b().d(th);
            }
        }
        intent.putExtra("com.facebook.platform.protocol.PROTOCOL_VERSION", 20171115).putExtra("com.facebook.platform.protocol.PROTOCOL_ACTION", "com.facebook.platform.action.request.FEED_DIALOG").putExtra("com.facebook.platform.extra.APPLICATION_ID", this.d);
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

    public void a(PlatformActionListener platformActionListener, Platform platform, Platform.ShareParams shareParams, String str) {
        this.a = platformActionListener;
        this.b = platform;
        this.c = shareParams;
        this.d = str;
    }

    @Override // com.mob.tools.FakeActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        finish();
        if (this.a != null) {
            Bundle bundleExtra = intent != null ? intent.getBundleExtra("com.facebook.platform.protocol.RESULT_ARGS") : null;
            if (bundleExtra == null) {
                if (i == 64206 && i2 == 0) {
                    this.a.onComplete(this.b, 9, null);
                    return;
                } else {
                    this.a.onError(this.b, 9, new Throwable("share error!"));
                    return;
                }
            }
            String string = bundleExtra.getString("completionGesture");
            boolean z = bundleExtra.getBoolean("didComplete");
            if (TextUtils.isEmpty(string)) {
                if (z) {
                    this.a.onComplete(this.b, 9, null);
                    return;
                } else {
                    this.a.onCancel(this.b, 9);
                    return;
                }
            }
            if (string.equalsIgnoreCase("cancel")) {
                this.a.onCancel(this.b, 9);
            } else if (string.equalsIgnoreCase("post")) {
                this.a.onComplete(this.b, 9, null);
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
