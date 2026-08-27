package com.autonavi.amap.mapcore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import com.amap.api.mapcore.util.hc;
import com.amap.api.mapcore.util.ic;
import com.amap.api.mapcore.util.jy;
import com.amap.api.mapcore.util.jz;
import com.autonavi.ae.gmap.GLMapEngine;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import me.panpf.sketch.uri.FileUriModel;

@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public class AeUtil {
    private static final int BUFFER = 1024;
    public static final String CONFIGNAME = "GNaviConfig.xml";
    public static final boolean IS_AE = true;
    public static final String RESZIPNAME = "res.zip";
    public static final String ROOTPATH = "/amap/";
    public static final String ROOT_DATA_PATH_NAME = "data_v6";
    public static final String ROOT_DATA_PATH_OLD_NAME = "data";
    public static final String SO_FILENAME = "AMapSDK_MAP_v6_9_3";
    public static final String SO_FILENAME_NAVI = "AMapSDK_NAVI_v6_5_0";

    /* loaded from: classes.dex */
    public static class UnZipFileBrake {
        public boolean mIsAborted = false;
    }

    /* loaded from: classes.dex */
    public interface ZipCompressProgressListener {
        void onFinishProgress(long j);
    }

    private static boolean checkEngineRes(File file) {
        File[] listFiles = file.listFiles();
        return listFiles != null && listFiles.length >= 2;
    }

    private static void decompress(File file, File file2, ZipInputStream zipInputStream, long j, ZipCompressProgressListener zipCompressProgressListener, UnZipFileBrake unZipFileBrake) throws Exception {
        boolean z = false;
        int i = 0;
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                break;
            }
            if (unZipFileBrake != null && unZipFileBrake.mIsAborted) {
                zipInputStream.closeEntry();
                return;
            }
            String name = nextEntry.getName();
            if (TextUtils.isEmpty(name) || name.contains("../")) {
                break;
            }
            File file3 = new File(file2.getPath() + File.separator + name);
            fileProber(file3);
            if (nextEntry.isDirectory()) {
                file3.mkdirs();
            } else {
                i += decompressFile(file3, zipInputStream, i, j, zipCompressProgressListener, unZipFileBrake);
            }
            zipInputStream.closeEntry();
        }
        z = true;
        if (!z || file == null) {
            return;
        }
        try {
            file.delete();
        } catch (Exception unused) {
        }
    }

    public static void decompress(InputStream inputStream, String str) throws Exception {
        decompress(inputStream, str, 0L, null);
    }

    private static void decompress(InputStream inputStream, String str, long j, ZipCompressProgressListener zipCompressProgressListener) throws Exception {
        CheckedInputStream checkedInputStream = new CheckedInputStream(inputStream, new CRC32());
        ZipInputStream zipInputStream = new ZipInputStream(checkedInputStream);
        decompress(null, new File(str), zipInputStream, j, zipCompressProgressListener, null);
        zipInputStream.close();
        checkedInputStream.close();
    }

    private static int decompressFile(File file, ZipInputStream zipInputStream, long j, long j2, ZipCompressProgressListener zipCompressProgressListener, UnZipFileBrake unZipFileBrake) throws Exception {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        byte[] bArr = new byte[1024];
        int i = 0;
        while (true) {
            int read = zipInputStream.read(bArr, 0, 1024);
            if (read == -1) {
                bufferedOutputStream.close();
                return i;
            }
            if (unZipFileBrake != null && unZipFileBrake.mIsAborted) {
                bufferedOutputStream.close();
                return i;
            }
            bufferedOutputStream.write(bArr, 0, read);
            i += read;
            if (j2 > 0 && zipCompressProgressListener != null) {
                long j3 = ((i + j) * 100) / j2;
                if (unZipFileBrake == null || !unZipFileBrake.mIsAborted) {
                    zipCompressProgressListener.onFinishProgress(j3);
                }
            }
        }
    }

    private static void fileProber(File file) {
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            return;
        }
        fileProber(parentFile);
        parentFile.mkdir();
    }

    public static GLMapEngine.InitParam initResource(final Context context) {
        final String mapBaseStorage = FileUtil.getMapBaseStorage(context);
        String str = mapBaseStorage + FileUriModel.SCHEME + ROOT_DATA_PATH_NAME + FileUriModel.SCHEME;
        File file = new File(mapBaseStorage);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.mkdir();
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            try {
                jy.a(1).a(new jz() { // from class: com.autonavi.amap.mapcore.AeUtil.1
                    @Override // com.amap.api.mapcore.util.jz
                    public void runTask() {
                        AeUtil.loadEngineRes(mapBaseStorage, context);
                    }
                });
            } catch (hc e) {
                e.printStackTrace();
            }
        } else {
            loadEngineRes(mapBaseStorage, context);
        }
        GLMapEngine.InitParam initParam = new GLMapEngine.InitParam();
        byte[] readFileContentsFromAssets = FileUtil.readFileContentsFromAssets(context, "ae/GNaviConfig.xml");
        initParam.mRootPath = mapBaseStorage;
        if (readFileContentsFromAssets != null) {
            try {
                initParam.mConfigContent = new String(readFileContentsFromAssets, "utf-8");
                if (!initParam.mConfigContent.contains(ROOT_DATA_PATH_NAME)) {
                    throw new Exception("GNaviConfig.xml 和数据目录data_v6不匹配");
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        initParam.mOfflineDataPath = str + FileUriModel.SCHEME + "map/";
        initParam.mP3dCrossPath = str;
        return initParam;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v18, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v21 */
    /* JADX WARN: Type inference failed for: r3v22 */
    /* JADX WARN: Type inference failed for: r3v23 */
    /* JADX WARN: Type inference failed for: r3v24 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.io.IOException] */
    /* JADX WARN: Type inference failed for: r3v8, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v9, types: [java.io.InputStream] */
    public static void loadEngineRes(String str, Context context) {
        InputStream open;
        File file = new File(str, "res");
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        if (checkEngineRes(file)) {
            return;
        }
        ?? e = 0;
        e = 0;
        e = 0;
        try {
            try {
                try {
                    open = context.getAssets().open("ae/res.zip");
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                }
            } catch (Exception e3) {
                e = e3;
            } catch (OutOfMemoryError e4) {
                e = e4;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            e = file.getAbsolutePath();
            decompress(open, e);
        } catch (Exception e5) {
            e = e5;
            e = open;
            e.printStackTrace();
            if (e != 0) {
                e.close();
                e = e;
            }
        } catch (OutOfMemoryError e6) {
            e = e6;
            e = open;
            e.printStackTrace();
            if (e != 0) {
                e.close();
                e = e;
            }
        } catch (Throwable th2) {
            th = th2;
            e = open;
            if (e != 0) {
                try {
                    e.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
            }
            throw th;
        }
        if (open != null) {
            open.close();
            e = e;
        }
    }

    public static void loadLib(Context context) {
        try {
            System.loadLibrary(SO_FILENAME);
        } catch (Throwable th) {
            ic.c(th, "AeUtil", "loadLib");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x0080 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0076 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:66:0x0044 -> B:22:0x006f). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void readAssetsFileAndSave(java.lang.String r4, java.lang.String r5, android.content.Context r6) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 == 0) goto L7
            return
        L7:
            r0 = 0
            android.content.res.AssetManager r6 = r6.getAssets()     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L5b
            java.io.InputStream r4 = r6.open(r4)     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L5b
            java.io.File r6 = new java.io.File     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L53
            r6.<init>(r5)     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L53
            boolean r5 = r6.exists()     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L53
            if (r5 == 0) goto L1e
            r6.delete()     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L53
        L1e:
            r6.createNewFile()     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L53
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L53
            r5.<init>(r6)     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L53
            r6 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r6]     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4c
        L2a:
            r1 = 0
            int r2 = r4.read(r0, r1, r6)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4c
            if (r2 <= 0) goto L35
            r5.write(r0, r1, r2)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4c
            goto L2a
        L35:
            if (r4 == 0) goto L3f
            r4.close()     // Catch: java.io.IOException -> L3b
            goto L3f
        L3b:
            r4 = move-exception
            r4.printStackTrace()
        L3f:
            r5.close()     // Catch: java.io.IOException -> L43
            goto L6f
        L43:
            r4 = move-exception
            r4.printStackTrace()
            goto L6f
        L48:
            r6 = move-exception
            r0 = r5
            r5 = r6
            goto L74
        L4c:
            r6 = move-exception
            r0 = r4
            r4 = r5
            r5 = r6
            goto L5d
        L51:
            r5 = move-exception
            goto L74
        L53:
            r5 = move-exception
            r3 = r0
            r0 = r4
            r4 = r3
            goto L5d
        L58:
            r5 = move-exception
            r4 = r0
            goto L74
        L5b:
            r5 = move-exception
            r4 = r0
        L5d:
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L70
            if (r0 == 0) goto L6a
            r0.close()     // Catch: java.io.IOException -> L66
            goto L6a
        L66:
            r5 = move-exception
            r5.printStackTrace()
        L6a:
            if (r4 == 0) goto L6f
            r4.close()     // Catch: java.io.IOException -> L43
        L6f:
            return
        L70:
            r5 = move-exception
            r3 = r0
            r0 = r4
            r4 = r3
        L74:
            if (r4 == 0) goto L7e
            r4.close()     // Catch: java.io.IOException -> L7a
            goto L7e
        L7a:
            r4 = move-exception
            r4.printStackTrace()
        L7e:
            if (r0 == 0) goto L88
            r0.close()     // Catch: java.io.IOException -> L84
            goto L88
        L84:
            r4 = move-exception
            r4.printStackTrace()
        L88:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autonavi.amap.mapcore.AeUtil.readAssetsFileAndSave(java.lang.String, java.lang.String, android.content.Context):void");
    }
}
