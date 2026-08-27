package cn.smssdk.utils;

import com.mob.MobSDK;
import com.mob.commons.dialog.entity.InternalPolicyUi;

/* compiled from: GlobalHolder.java */
/* loaded from: classes.dex */
public class b {
    private static b c;
    private boolean a = false;
    private Boolean b;

    private b() {
        MobSDK.getContext();
        new InternalPolicyUi.Builder().setTitleText("服务授权").setContentText("为了给您提供Mobservice相关产品服务，请您详细查看我们的隐私政策，详见<a href=\"http://www.mob.com/about/policy\">http://www.mob.com/about/policy</a>。如您同意我们的隐私政策，请点击“接受”，如您不同意我们的隐私政策，请点击“拒绝”。").build();
    }

    public static b c() {
        if (c == null) {
            synchronized (b.class) {
                if (c == null) {
                    c = new b();
                }
            }
        }
        return c;
    }

    public void a(boolean z) {
        this.b = Boolean.valueOf(z);
        SPHelper.getInstance().setAgree(z);
    }

    public boolean a() {
        if (this.b == null) {
            this.b = Boolean.valueOf(SPHelper.getInstance().isAgree());
        }
        return this.b.booleanValue();
    }

    public void b(boolean z) {
        this.a = z;
    }

    public boolean b() {
        return this.a;
    }

    public void c(boolean z) {
    }
}
