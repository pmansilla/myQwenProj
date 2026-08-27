package com.amap.api.mapcore.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import me.panpf.sketch.uri.FileUriModel;

/* compiled from: ServiceUtils.java */
/* loaded from: classes.dex */
public class gt {
    private static AssetManager b = null;
    private static Resources c = null;
    private static Resources d = null;
    private static boolean e = true;
    private static Context f = null;
    private static String g = "amap_resource";
    private static String h = "1_0_0";
    private static String j = ".jar";
    private static String k = g + h + j;
    private static String i = ".png";
    private static String l = g + h + i;
    private static String m = "";
    private static String n = m + k;
    private static Resources.Theme o = null;
    private static Resources.Theme p = null;
    private static Field q = null;
    private static Field r = null;
    private static Activity s = null;
    public static int a = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ServiceUtils.java */
    /* loaded from: classes.dex */
    public static class a implements FilenameFilter {
        a() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            StringBuilder sb = new StringBuilder();
            sb.append(gt.h);
            sb.append(gt.j);
            return str.startsWith(gt.g) && !str.endsWith(sb.toString());
        }
    }

    private static AssetManager a(String str) {
        AssetManager assetManager;
        try {
            Class<?> cls = Class.forName("android.content.res.AssetManager");
            assetManager = (AssetManager) cls.getConstructor((Class[]) null).newInstance((Object[]) null);
            try {
                cls.getDeclaredMethod("addAssetPath", String.class).invoke(assetManager, str);
            } catch (Throwable th) {
                th = th;
                ic.c(th, "ResourcesUtil", "getAssetManager(String apkPath)");
                return assetManager;
            }
        } catch (Throwable th2) {
            th = th2;
            assetManager = null;
        }
        return assetManager;
    }

    public static Resources a() {
        return c == null ? f.getResources() : c;
    }

    private static Resources a(Context context, AssetManager assetManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.setToDefaults();
        return new Resources(assetManager, displayMetrics, context.getResources().getConfiguration());
    }

    public static View a(Context context, int i2, ViewGroup viewGroup) {
        XmlResourceParser xml = a().getXml(i2);
        try {
            if (!e) {
                return LayoutInflater.from(context).inflate(xml, viewGroup);
            }
            try {
                return LayoutInflater.from(new gs(context, a == -1 ? 0 : a, gt.class.getClassLoader())).inflate(xml, viewGroup);
            } catch (Throwable th) {
                th.printStackTrace();
                ic.c(th, "ResourcesUtil", "selfInflate(Activity activity, int resource, ViewGroup root)");
                xml.close();
                return null;
            }
        } finally {
            xml.close();
        }
    }

    private static OutputStream a(InputStream inputStream) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(m, k));
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read <= 0) {
                return fileOutputStream;
            }
            fileOutputStream.write(bArr, 0, read);
        }
    }

    public static boolean a(Context context) {
        try {
            f = context;
            File b2 = b(f);
            if (b2 != null) {
                m = b2.getAbsolutePath() + FileUriModel.SCHEME;
            }
            n = m + k;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (!e) {
            return true;
        }
        if (!c(context)) {
            return false;
        }
        b = a(n);
        c = a(context, b);
        return true;
    }

    private static File b(Context context) {
        File filesDir;
        File file = null;
        try {
            if (context == null) {
                if (context != null) {
                    context.getFilesDir();
                }
                return null;
            }
            try {
                if (Environment.getExternalStorageState().equals("mounted")) {
                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
                    try {
                        filesDir = !externalStorageDirectory.canWrite() ? context.getFilesDir() : context.getExternalFilesDir("LBS");
                    } catch (Exception e2) {
                        e = e2;
                        file = externalStorageDirectory;
                        e.printStackTrace();
                        return (file != null || context == null) ? file : context.getFilesDir();
                    } catch (Throwable th) {
                        th = th;
                        file = externalStorageDirectory;
                        if (file == null && context != null) {
                            context.getFilesDir();
                        }
                        throw th;
                    }
                } else {
                    filesDir = context.getFilesDir();
                }
                if (filesDir == null && context != null) {
                    context.getFilesDir();
                }
                return filesDir;
            } catch (Exception e3) {
                e = e3;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static boolean b(InputStream inputStream) throws IOException {
        File file = new File(n);
        long length = file.length();
        int available = inputStream.available();
        if (!file.exists() || length != available) {
            return false;
        }
        inputStream.close();
        return true;
    }

    private static boolean c(Context context) {
        InputStream open;
        d(context);
        InputStream inputStream = null;
        try {
            try {
                open = context.getResources().getAssets().open(l);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            if (b(open)) {
                if (open != null) {
                    try {
                        open.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        ic.c(e2, "ResourcesUtil", "copyResourceJarToAppFilesDir(Context ctx)");
                    }
                }
                return true;
            }
            e();
            OutputStream a2 = a(open);
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                    ic.c(e3, "ResourcesUtil", "copyResourceJarToAppFilesDir(Context ctx)");
                }
            }
            if (a2 != null) {
                a2.close();
            }
            return true;
        } catch (Throwable th3) {
            inputStream = open;
            th = th3;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                    ic.c(e4, "ResourcesUtil", "copyResourceJarToAppFilesDir(Context ctx)");
                }
            }
            throw th;
        }
    }

    private static void d(Context context) {
        m = context.getFilesDir().getAbsolutePath();
        n = m + FileUriModel.SCHEME + k;
    }

    private static void e() {
        File[] listFiles = new File(m).listFiles(new a());
        if (listFiles == null || listFiles.length <= 0) {
            return;
        }
        for (File file : listFiles) {
            file.delete();
        }
    }
}
