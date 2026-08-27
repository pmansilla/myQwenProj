package cn.sharesdk.framework.b.a;

import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.utils.SharePrefrenceHelper;
import com.tencent.bugly.Bugly;

/* compiled from: SharePrefrenceUtil.java */
/* loaded from: classes.dex */
public class e {
    private static e b;
    private SharePrefrenceHelper a = new SharePrefrenceHelper(MobSDK.getContext());

    private e() {
        this.a.open("share_sdk", 1);
    }

    public static e a() {
        if (b == null) {
            b = new e();
        }
        return b;
    }

    public void a(long j) {
        this.a.putLong("device_time", Long.valueOf(j));
    }

    public void a(String str) {
        this.a.putString("trans_short_link", str);
    }

    public void a(String str, int i) {
        this.a.putInt(str, Integer.valueOf(i));
    }

    public void a(String str, Long l) {
        this.a.putLong(str, l);
    }

    public void a(String str, Object obj) {
        this.a.put(str, obj);
    }

    public void a(boolean z) {
        this.a.putBoolean("connect_server", Boolean.valueOf(z));
    }

    public long b() {
        return this.a.getLong("service_time");
    }

    public void b(long j) {
        this.a.putLong("connect_server_time", Long.valueOf(j));
    }

    public void b(String str) {
        this.a.putString("upload_device_info", str);
    }

    public void b(boolean z) {
        this.a.putBoolean("sns_info_buffered", Boolean.valueOf(z));
    }

    public void c(String str) {
        this.a.putString("upload_user_info", str);
    }

    public boolean c() {
        String string = this.a.getString("upload_device_info");
        if (TextUtils.isEmpty(string)) {
            return true;
        }
        return Boolean.parseBoolean(string);
    }

    public void d(String str) {
        this.a.putString("upload_share_content", str);
    }

    public boolean d() {
        String string = this.a.getString("upload_user_info");
        if (TextUtils.isEmpty(string)) {
            return true;
        }
        return Boolean.parseBoolean(string);
    }

    public void e(String str) {
        this.a.putString("buffered_snsconf_" + MobSDK.getAppkey(), str);
    }

    public boolean e() {
        String string = this.a.getString("trans_short_link");
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        return Boolean.parseBoolean(string);
    }

    public int f() {
        String string = this.a.getString("upload_share_content");
        if ("true".equals(string)) {
            return 1;
        }
        return Bugly.SDK_IS_DEV.equals(string) ? -1 : 0;
    }

    public long f(String str) {
        return this.a.getLong(str);
    }

    public int g(String str) {
        return this.a.getInt(str);
    }

    public String g() {
        return this.a.getString("buffered_snsconf_" + MobSDK.getAppkey());
    }

    public Long h() {
        return Long.valueOf(this.a.getLong("device_time"));
    }

    public Object h(String str) {
        return this.a.get(str);
    }

    public boolean i() {
        return this.a.getBoolean("connect_server");
    }

    public Long j() {
        return Long.valueOf(this.a.getLong("connect_server_time"));
    }

    public boolean k() {
        return this.a.getBoolean("sns_info_buffered");
    }
}
