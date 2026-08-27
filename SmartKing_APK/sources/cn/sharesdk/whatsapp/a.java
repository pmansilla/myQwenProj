package cn.sharesdk.whatsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.e;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.tools.FakeActivity;
import java.util.HashMap;

/* compiled from: ShareActivity.java */
/* loaded from: classes.dex */
public class a extends FakeActivity {
    private Platform a;
    private PlatformActionListener b;

    public void a(Platform platform, PlatformActionListener platformActionListener) {
        this.a = platform;
        this.b = platformActionListener;
    }

    @Override // com.mob.tools.FakeActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        finish();
        if (i == 1) {
            HashMap<String, Object> hashMap = new HashMap<>();
            if (intent != null && intent.getExtras() != null) {
                for (String str : intent.getExtras().keySet()) {
                    hashMap.put(str, intent.getExtras().get(str));
                }
            }
            this.b.onComplete(this.a, 9, hashMap);
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        Bundle extras = this.activity.getIntent().getExtras();
        String string = extras.getString("uri");
        String string2 = extras.getString(FileDownloadModel.PATH);
        String string3 = extras.getString("title");
        String string4 = extras.getString("text");
        String string5 = extras.getString("phone");
        int i = extras.getInt("type");
        if (!TextUtils.isEmpty(string)) {
            try {
                startActivityForResult(new Intent("android.intent.action.SEND", Uri.parse(string)), 1);
                return;
            } catch (Throwable th) {
                e.b().d(th);
                return;
            }
        }
        Intent intent = new Intent("android.intent.action.SEND");
        if (1 == i && (!TextUtils.isEmpty(string4) || !TextUtils.isEmpty(string3))) {
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", string3);
            intent.putExtra("android.intent.extra.TEXT", string4);
        } else if (2 == i && !TextUtils.isEmpty(string2)) {
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", Uri.parse(string2));
        } else if (6 == i && !TextUtils.isEmpty(string2)) {
            intent.setType("video/*");
            intent.putExtra("android.intent.extra.STREAM", Uri.parse(string2));
        } else if (100 == i && !TextUtils.isEmpty(string5)) {
            intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + string5));
        }
        intent.setPackage("com.whatsapp");
        try {
            startActivityForResult(intent, 1);
        } catch (Throwable th2) {
            e.b().d(th2);
        }
    }
}
