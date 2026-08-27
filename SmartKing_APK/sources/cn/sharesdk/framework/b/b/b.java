package cn.sharesdk.framework.b.b;

import android.text.TextUtils;
import android.util.Base64;
import com.mob.tools.utils.Data;

/* compiled from: AuthEvent.java */
/* loaded from: classes.dex */
public class b extends c {
    private static int m;
    private static long n;
    public int a;
    public String b;
    public String c;
    public String d;

    @Override // cn.sharesdk.framework.b.b.c
    protected String a() {
        return "[AUT]";
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void a(long j) {
        n = j;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected int b() {
        return 5000;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected int c() {
        return 5;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected long d() {
        return m;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected long e() {
        return n;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void f() {
        m++;
    }

    @Override // cn.sharesdk.framework.b.b.c
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append('|');
        sb.append(this.a);
        sb.append('|');
        sb.append(this.b);
        sb.append('|');
        if (!TextUtils.isEmpty(this.d)) {
            try {
                String encodeToString = Base64.encodeToString(Data.AES128Encode(this.f.substring(0, 16), this.d), 0);
                if (!TextUtils.isEmpty(encodeToString) && encodeToString.contains("\n")) {
                    encodeToString = encodeToString.replace("\n", "");
                }
                sb.append(encodeToString);
            } catch (Throwable th) {
                cn.sharesdk.framework.utils.e.b().d(th);
            }
        }
        sb.append('|');
        if (!TextUtils.isEmpty(this.l)) {
            sb.append(this.l);
        }
        sb.append('|');
        if (!TextUtils.isEmpty(this.c)) {
            sb.append(this.c);
        }
        return sb.toString();
    }
}
