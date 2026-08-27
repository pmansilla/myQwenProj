package cn.sharesdk.whatsapp;

import android.content.Intent;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.f;
import cn.sharesdk.framework.utils.e;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobSDK;

/* compiled from: WhatsAppHelper.java */
/* loaded from: classes.dex */
public class b extends cn.sharesdk.framework.b {
    public b(Platform platform) {
        super(platform);
    }

    public void a(int i, String str, PlatformActionListener platformActionListener) {
        Intent intent = new Intent();
        intent.putExtra("type", i);
        intent.putExtra(FileDownloadModel.PATH, str);
        a aVar = new a();
        aVar.a(this.a, platformActionListener);
        aVar.show(MobSDK.getContext(), intent);
    }

    public void a(String str, PlatformActionListener platformActionListener) {
        Intent intent = new Intent();
        intent.putExtra("type", 100);
        intent.putExtra("phone", str);
        a aVar = new a();
        aVar.a(this.a, platformActionListener);
        aVar.show(MobSDK.getContext(), intent);
    }

    public void a(String str, String str2, PlatformActionListener platformActionListener) {
        Intent intent = new Intent();
        intent.putExtra("type", 1);
        intent.putExtra("text", str);
        intent.putExtra("title", str2);
        a aVar = new a();
        aVar.a(this.a, platformActionListener);
        aVar.show(MobSDK.getContext(), intent);
    }

    public boolean a() {
        Boolean bool;
        Boolean.valueOf(false);
        try {
            bool = Boolean.valueOf(MobSDK.getContext().getPackageManager().getPackageInfo("com.whatsapp", 0) != null);
        } catch (Exception e) {
            e.b().d("Exception", e.toString());
            bool = false;
        }
        return bool.booleanValue();
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getAuthorizeUrl() {
        return null;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.b getAuthorizeWebviewClient(f fVar) {
        return null;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getRedirectUri() {
        return null;
    }
}
