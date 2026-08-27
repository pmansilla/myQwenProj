package cn.sharesdk.wechat.utils;

import android.os.Bundle;

/* compiled from: WXLaunchMiniProgram.java */
/* loaded from: classes.dex */
public class h {

    /* compiled from: WXLaunchMiniProgram.java */
    /* loaded from: classes.dex */
    public static final class a extends l {
        public String a;
        public String b = "";
        public int c = 0;

        @Override // cn.sharesdk.wechat.utils.l
        public final int a() {
            return 19;
        }

        @Override // cn.sharesdk.wechat.utils.l
        public final void a(Bundle bundle) {
            super.a(bundle);
            bundle.putString("_launch_wxminiprogram_username", this.a);
            bundle.putString("_launch_wxminiprogram_path", this.b);
            bundle.putInt("_launch_wxminiprogram_type", this.c);
        }

        @Override // cn.sharesdk.wechat.utils.l
        public final boolean a(boolean z) {
            if (this.a == null || this.a.length() == 0 || this.a.length() > 10240) {
                cn.sharesdk.framework.utils.e.b().d("checkArgs fail, userName is invalid", new Object[0]);
                return false;
            }
            if (this.c >= 0 && this.c <= 2) {
                return true;
            }
            cn.sharesdk.framework.utils.e.b().d("checkArgs fail", "miniprogram type should between MINIPTOGRAM_TYPE_RELEASE and MINIPROGRAM_TYPE_PREVIEW");
            return false;
        }
    }
}
