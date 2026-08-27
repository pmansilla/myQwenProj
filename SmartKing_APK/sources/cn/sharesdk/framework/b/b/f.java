package cn.sharesdk.framework.b.b;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: ShareEvent.java */
/* loaded from: classes.dex */
public class f extends c {
    private static int o;
    private static long p;
    public int a;
    public String b;
    public String c;
    public a d = new a();
    public String m;
    public String[] n;

    /* compiled from: ShareEvent.java */
    /* loaded from: classes.dex */
    public static class a {
        public String b;
        public HashMap<String, Object> g;
        public String a = "";
        public ArrayList<String> c = new ArrayList<>();
        public ArrayList<String> d = new ArrayList<>();
        public ArrayList<String> e = new ArrayList<>();
        public ArrayList<Bitmap> f = new ArrayList<>();

        public String toString() {
            HashMap hashMap = new HashMap();
            if (!TextUtils.isEmpty(this.b)) {
                this.b = this.b.trim().replaceAll("\r", "");
                this.b = this.b.trim().replaceAll("\n", "");
                this.b = this.b.trim().replaceAll("\r\n", "");
            }
            hashMap.put("text", this.b);
            hashMap.put(FileDownloadModel.URL, this.c);
            if (this.d != null && this.d.size() > 0) {
                hashMap.put("imgs", this.d);
            }
            if (this.g != null) {
                hashMap.put("attch", new Hashon().fromHashMap(this.g));
            }
            return new Hashon().fromHashMap(hashMap);
        }
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected String a() {
        return "[SHR]";
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void a(long j) {
        p = j;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected int b() {
        return 5000;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected int c() {
        return 30;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected long d() {
        return o;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected long e() {
        return p;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void f() {
        o++;
    }

    @Override // cn.sharesdk.framework.b.b.c
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append('|');
        sb.append(this.a);
        sb.append('|');
        sb.append(this.b);
        sb.append('|');
        sb.append(TextUtils.isEmpty(this.c) ? "" : this.c);
        String str = "";
        if (this.n != null && this.n.length > 0) {
            str = "[\"" + TextUtils.join("\",\"", this.n) + "\"]";
        }
        sb.append('|');
        sb.append(str);
        sb.append('|');
        if (this.d != null) {
            try {
                String encodeToString = Base64.encodeToString(Data.AES128Encode(this.f.substring(0, 16), this.d.toString()), 0);
                if (encodeToString.contains("\n")) {
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
        if (!TextUtils.isEmpty(this.m)) {
            try {
                String encodeToString2 = Base64.encodeToString(Data.AES128Encode(this.f.substring(0, 16), this.m), 0);
                if (!TextUtils.isEmpty(encodeToString2) && encodeToString2.contains("\n")) {
                    encodeToString2 = encodeToString2.replace("\n", "");
                }
                sb.append(encodeToString2);
            } catch (Throwable th2) {
                cn.sharesdk.framework.utils.e.b().w(th2);
            }
        }
        return sb.toString();
    }
}
