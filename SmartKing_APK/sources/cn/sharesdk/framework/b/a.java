package cn.sharesdk.framework.b;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.framework.b.a.e;
import cn.sharesdk.framework.b.b.f;
import com.autonavi.amap.mapcore.AeUtil;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.luck.picture.lib.config.PictureMimeType;
import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;
import io.reactivex.annotations.SchedulerSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import me.panpf.sketch.uri.HttpUriModel;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: EventManager.java */
/* loaded from: classes.dex */
public class a {
    private static a a;
    private boolean e = true;
    private c b = new c();
    private e d = e.a();
    private DeviceHelper c = DeviceHelper.getInstance(MobSDK.getContext());

    private a() {
    }

    public static a a() {
        if (a == null) {
            a = new a();
        }
        return a;
    }

    private String a(Bitmap bitmap, b bVar) throws Throwable {
        File createTempFile = File.createTempFile("bm_tmp", PictureMimeType.PNG);
        FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return a(createTempFile.getAbsolutePath(), bVar);
    }

    private String a(String str, b bVar) throws Throwable {
        int ceil;
        if (TextUtils.isEmpty(str) || !new File(str).exists()) {
            return null;
        }
        String networkType = this.c.getNetworkType();
        if (SchedulerSupport.NONE.equals(networkType) || TextUtils.isEmpty(networkType)) {
            return null;
        }
        Bitmap.CompressFormat bmpFormat = BitmapHelper.getBmpFormat(str);
        float f = bVar == b.BEFORE_SHARE ? 600.0f : 200.0f;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        int i = options.outWidth;
        int i2 = options.outHeight;
        if (i >= i2 && i2 > f) {
            ceil = (int) Math.ceil(options.outHeight / f);
        } else {
            if (i >= i2 || i <= f) {
                return d(str);
            }
            ceil = (int) Math.ceil(options.outWidth / f);
        }
        if (ceil <= 0) {
            ceil = 1;
        }
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = ceil;
        options2.inPurgeable = true;
        options2.inInputShareable = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options2);
        decodeFile.getHeight();
        decodeFile.getWidth();
        File createTempFile = File.createTempFile("bm_tmp2", "." + bmpFormat.name().toLowerCase());
        FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
        decodeFile.compress(bmpFormat, 80, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return d(createTempFile.getAbsolutePath());
    }

    private String a(String str, String str2, int i, String str3) throws Throwable {
        HashMap<String, Object> a2;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        Pattern compile = Pattern.compile(str2);
        Matcher matcher = compile.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            if (group != null && group.length() > 0) {
                arrayList.add(group);
            }
        }
        if (arrayList.size() == 0 || (a2 = this.b.a(str, arrayList, i, str3)) == null || a2.size() <= 0 || !a2.containsKey(AeUtil.ROOT_DATA_PATH_OLD_NAME)) {
            return str;
        }
        ArrayList arrayList2 = (ArrayList) a2.get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
        HashMap hashMap = new HashMap();
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            HashMap hashMap2 = (HashMap) it.next();
            hashMap.put(String.valueOf(hashMap2.get(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME)), String.valueOf(hashMap2.get("surl")));
        }
        Matcher matcher2 = compile.matcher(str);
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (matcher2.find()) {
            sb.append(str.substring(i2, matcher2.start()));
            sb.append((String) hashMap.get(matcher2.group()));
            i2 = matcher2.end();
        }
        sb.append(str.substring(i2, str.length()));
        String sb2 = sb.toString();
        cn.sharesdk.framework.utils.e.b().i("> SERVER_SHORT_LINK_URL content after replace link ===  %s", sb2);
        return sb2;
    }

    private void a(cn.sharesdk.framework.b.b.b bVar) throws Throwable {
        boolean d = this.d.d();
        String str = bVar.c;
        if (d && !TextUtils.isEmpty(str)) {
            bVar.c = Data.Base64AES(str, bVar.f.substring(0, 16));
        } else {
            bVar.d = null;
            bVar.c = null;
        }
    }

    private void a(f fVar) throws Throwable {
        int f = this.d.f();
        boolean d = this.d.d();
        f.a aVar = fVar.d;
        if (f == 1) {
            int size = (aVar == null || aVar.e == null) ? 0 : aVar.e.size();
            for (int i = 0; i < size; i++) {
                String a2 = a(aVar.e.get(i), b.FINISH_SHARE);
                if (!TextUtils.isEmpty(a2)) {
                    aVar.d.add(a2);
                }
            }
            int size2 = (aVar == null || aVar.f == null) ? 0 : aVar.f.size();
            for (int i2 = 0; i2 < size2; i2++) {
                String a3 = a(aVar.f.get(i2), b.FINISH_SHARE);
                if (!TextUtils.isEmpty(a3)) {
                    aVar.d.add(a3);
                }
            }
        } else {
            fVar.d = null;
        }
        if (d) {
            return;
        }
        fVar.m = null;
    }

    private boolean a(String str, boolean z) throws Throwable {
        return this.b.a(str, z);
    }

    private String d(String str) throws Throwable {
        HashMap<String, Object> d = this.b.d(str);
        if (d != null && d.size() > 0 && d.containsKey("status") && ResHelper.parseInt(String.valueOf(d.get("status"))) == 200 && d.containsKey(FileDownloadModel.URL)) {
            return (String) d.get(FileDownloadModel.URL);
        }
        return null;
    }

    private String e(String str) throws Throwable {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = byteArrayInputStream.read(bArr, 0, 1024);
            if (read == -1) {
                gZIPOutputStream.flush();
                gZIPOutputStream.close();
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
                byteArrayInputStream.close();
                return Base64.encodeToString(byteArray, 2);
            }
            gZIPOutputStream.write(bArr, 0, read);
        }
    }

    public String a(Bitmap bitmap) {
        if (!this.d.i()) {
            return null;
        }
        try {
            return a(bitmap, b.BEFORE_SHARE);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    public String a(String str, int i, boolean z, String str2) {
        String a2;
        try {
            if (this.d.i() && this.d.e()) {
                String networkType = this.c.getNetworkType();
                if (!SchedulerSupport.NONE.equals(networkType) && !TextUtils.isEmpty(networkType)) {
                    if (z && (a2 = a(str, "<a[^>]*?href[\\s]*=[\\s]*[\"']?([^'\">]+?)['\"]?>", i, str2)) != null && !a2.equals(str)) {
                        return a2;
                    }
                    String a3 = a(str, "(http://|https://){1}[\\w\\.\\-/:\\?&%=,;\\[\\]\\{\\}`~!@#\\$\\^\\*\\(\\)_\\+\\\\\\|]+", i, str2);
                    if (a3 != null) {
                        if (!a3.equals(str)) {
                            return a3;
                        }
                    }
                    return str;
                }
                return str;
            }
            return str;
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return str;
        }
    }

    public void a(cn.sharesdk.framework.b.b.c cVar) {
        try {
            if (this.d.i()) {
                if (cVar instanceof cn.sharesdk.framework.b.b.b) {
                    a((cn.sharesdk.framework.b.b.b) cVar);
                } else if (cVar instanceof f) {
                    a((f) cVar);
                }
                if (!this.d.c()) {
                    cVar.l = null;
                }
                long b = this.d.b();
                if (b == 0) {
                    b = this.b.b();
                }
                cVar.e = System.currentTimeMillis() - b;
                this.b.a(cVar);
            }
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
    }

    public void a(String str) {
        if (this.b == null || TextUtils.isEmpty(str)) {
            return;
        }
        this.b.a(str);
    }

    public void a(HashMap<String, Object> hashMap) {
        try {
            this.b.b(hashMap);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
    }

    public String b(String str) {
        if (!this.d.i()) {
            return null;
        }
        try {
            return a(str, b.BEFORE_SHARE);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    public void b() {
        try {
            String networkType = this.c.getNetworkType();
            if (!SchedulerSupport.NONE.equals(networkType) && !TextUtils.isEmpty(networkType)) {
                long longValue = this.d.j().longValue();
                long currentTimeMillis = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(longValue);
                int i = calendar.get(5);
                calendar.setTimeInMillis(currentTimeMillis);
                int i2 = calendar.get(5);
                if (currentTimeMillis - longValue >= DateUtils.MILLIS_PER_DAY || i != i2) {
                    HashMap<String, Object> a2 = this.b.a();
                    this.d.a(a2.containsKey("res") ? "true".equals(String.valueOf(a2.get("res"))) : true);
                    if (a2 == null || a2.size() <= 0) {
                        return;
                    }
                    this.d.b(System.currentTimeMillis());
                }
            }
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
    }

    public HashMap<String, Object> c(String str) {
        try {
            return this.b.e(str);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    public void c() {
        HashMap hashMap;
        HashMap hashMap2;
        try {
            String networkType = this.c.getNetworkType();
            if (SchedulerSupport.NONE.equals(networkType) || TextUtils.isEmpty(networkType) || !this.d.i()) {
                return;
            }
            this.d.a(System.currentTimeMillis());
            HashMap<String, Object> c = this.b.c();
            if (c.containsKey("status") && ResHelper.parseInt(String.valueOf(c.get("status"))) == -200) {
                cn.sharesdk.framework.utils.e.b().d((String) c.get("error"), new Object[0]);
                return;
            }
            if (c.containsKey("timestamp")) {
                this.d.a("service_time", Long.valueOf(System.currentTimeMillis() - ResHelper.parseLong(String.valueOf(c.get("timestamp")))));
            }
            if (c.containsKey("switchs") && (hashMap2 = (HashMap) c.get("switchs")) != null) {
                String valueOf = String.valueOf(hashMap2.get("device"));
                String valueOf2 = String.valueOf(hashMap2.get("share"));
                String valueOf3 = String.valueOf(hashMap2.get("auth"));
                String valueOf4 = String.valueOf(hashMap2.get("backflow"));
                this.d.b(valueOf);
                this.d.d(valueOf2);
                this.d.c(valueOf3);
                this.d.a(valueOf4);
            }
            if (!c.containsKey("serpaths") || (hashMap = (HashMap) c.get("serpaths")) == null) {
                return;
            }
            String valueOf5 = String.valueOf(hashMap.get("defhost"));
            String valueOf6 = String.valueOf(hashMap.get("defport"));
            if (!TextUtils.isEmpty(valueOf5) && !TextUtils.isEmpty(valueOf6)) {
                this.b.c(HttpUriModel.SCHEME + valueOf5 + ":" + valueOf6);
            }
            HashMap<String, String> hashMap3 = new HashMap<>();
            if (hashMap.containsKey("assigns")) {
                HashMap hashMap4 = (HashMap) hashMap.get("assigns");
                if (hashMap4 != null && hashMap4.size() != 0) {
                    for (String str : hashMap4.keySet()) {
                        HashMap hashMap5 = (HashMap) hashMap4.get(str);
                        String valueOf7 = String.valueOf(hashMap5.get("host"));
                        String valueOf8 = String.valueOf(hashMap5.get("port"));
                        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(valueOf7) && !TextUtils.isEmpty(valueOf8)) {
                            hashMap3.put(str, HttpUriModel.SCHEME + valueOf7 + ":" + valueOf8);
                        }
                    }
                    this.b.a(hashMap3);
                    return;
                }
                this.b.a((HashMap<String, String>) null);
            }
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
    }

    public void d() {
        try {
            String networkType = this.c.getNetworkType();
            if (SchedulerSupport.NONE.equals(networkType) || TextUtils.isEmpty(networkType) || !this.d.i()) {
                return;
            }
            ArrayList<cn.sharesdk.framework.b.a.c> e = this.b.e();
            for (int i = 0; i < e.size(); i++) {
                cn.sharesdk.framework.b.a.c cVar = e.get(i);
                if (cVar.b.size() == 1 ? a(cVar.a, false) : a(e(cVar.a), true)) {
                    this.b.a(cVar.b);
                }
            }
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
    }

    public HashMap<String, Object> e() {
        try {
            return this.b.f();
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return new HashMap<>();
        }
    }

    public HashMap<String, Object> f() {
        if (!this.d.i() && this.d.k()) {
            return new HashMap<>();
        }
        try {
            HashMap<String, Object> d = this.b.d();
            this.d.b(true);
            return d;
        } catch (Throwable th) {
            this.d.b(false);
            cn.sharesdk.framework.utils.e.b().d(th);
            return new HashMap<>();
        }
    }
}
