package com.amap.location.common.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FileUtil {
    private static final String BASE_PATH = "amaplocation";
    private static volatile String sAppSDCardDirPath;

    @Deprecated
    public static boolean appendToFile(String str, File file) {
        return writeToFile(str, file, true);
    }

    public static boolean deleteFileOrDir(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(file);
        while (arrayList2.size() > 0) {
            File file2 = (File) arrayList2.remove(0);
            arrayList.add(file2);
            File[] listFiles = file2.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file3 : listFiles) {
                    if (file3.isDirectory()) {
                        arrayList2.add(file3);
                    } else if (file3.exists() && !file3.delete()) {
                        return false;
                    }
                }
            }
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            File file4 = (File) arrayList.get(size);
            if (file4.exists() && !file4.delete()) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    private static boolean deleteFileOrDirDep(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (!deleteFileOrDir(file2)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static String getAppSDCardFileDir(Context context) {
        if (sAppSDCardDirPath != null) {
            return sAppSDCardDirPath;
        }
        if (isExternalStorageWritable()) {
            try {
                File file = new File(context.getExternalFilesDir(null), BASE_PATH);
                if (file.exists() || file.mkdirs()) {
                    sAppSDCardDirPath = file.getAbsolutePath();
                }
            } catch (Exception unused) {
                return null;
            }
        }
        return sAppSDCardDirPath;
    }

    private static long getAvailspceInner(File file) {
        try {
            StatFs statFs = new StatFs(file.getAbsolutePath());
            if (Build.VERSION.SDK_INT >= 18) {
                return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
            }
            return statFs.getBlockSize() * statFs.getAvailableBlocks();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static long getSdcardAvailableSpace(Context context) {
        if (isExternalStorageWritable()) {
            return getAvailspceInner(context.getExternalFilesDir(null));
        }
        return 0L;
    }

    public static long getSystemAvailableSpace(Context context) {
        return getAvailspceInner(context.getFilesDir());
    }

    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    /* JADX WARN: Not initialized variable reg: 1, insn: 0x001c: MOVE (r0 I:??[OBJECT, ARRAY]) = (r1 I:??[OBJECT, ARRAY]), block:B:16:0x001c */
    public static byte[] readBytes(File file) {
        FileInputStream fileInputStream;
        Closeable closeable;
        Closeable closeable2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    byte[] a = e.a((InputStream) fileInputStream);
                    e.a((Closeable) fileInputStream);
                    return a;
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    e.a((Closeable) fileInputStream);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                closeable2 = closeable;
                e.a(closeable2);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            e.a(closeable2);
            throw th;
        }
    }

    public static List<String> readLines(File file) {
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader = null;
        if (file != null) {
            try {
                if (file.exists()) {
                    BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
                    while (true) {
                        try {
                            String readLine = bufferedReader2.readLine();
                            if (readLine == null) {
                                break;
                            }
                            arrayList.add(readLine);
                        } catch (IOException unused) {
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader = bufferedReader2;
                            e.a(bufferedReader);
                            throw th;
                        }
                    }
                    bufferedReader = bufferedReader2;
                }
            } catch (IOException unused2) {
            } catch (Throwable th2) {
                th = th2;
            }
        }
        e.a(bufferedReader);
        return arrayList;
    }

    /* JADX WARN: Not initialized variable reg: 1, insn: 0x001c: MOVE (r0 I:??[OBJECT, ARRAY]) = (r1 I:??[OBJECT, ARRAY]), block:B:16:0x001c */
    public static String readString(File file) {
        FileInputStream fileInputStream;
        Closeable closeable;
        Closeable closeable2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    String b = e.b(fileInputStream);
                    e.a((Closeable) fileInputStream);
                    return b;
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    e.a((Closeable) fileInputStream);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                closeable2 = closeable;
                e.a(closeable2);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            e.a(closeable2);
            throw th;
        }
    }

    public static boolean setAppSDCardFileDir(File file) {
        if (!file.exists() && !file.mkdirs()) {
            return false;
        }
        sAppSDCardDirPath = file.getAbsolutePath();
        return true;
    }

    public static boolean writeToFile(String str, File file, boolean z) {
        FileWriter fileWriter;
        if (file == null) {
            return false;
        }
        FileWriter fileWriter2 = null;
        try {
            try {
                fileWriter = new FileWriter(file.getAbsolutePath(), z);
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            fileWriter.write(str);
            e.a(fileWriter);
            return true;
        } catch (Exception e2) {
            e = e2;
            fileWriter2 = fileWriter;
            e.printStackTrace();
            e.a(fileWriter2);
            return false;
        } catch (Throwable th2) {
            th = th2;
            fileWriter2 = fileWriter;
            e.a(fileWriter2);
            throw th;
        }
    }

    public static boolean writeToFile(byte[] bArr, File file, boolean z) {
        FileOutputStream fileOutputStream;
        if (file == null) {
            return false;
        }
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(file.getAbsolutePath(), z);
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            fileOutputStream.write(bArr);
            e.a(fileOutputStream);
            return true;
        } catch (Exception e2) {
            e = e2;
            fileOutputStream2 = fileOutputStream;
            e.printStackTrace();
            e.a(fileOutputStream2);
            return false;
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream2 = fileOutputStream;
            e.a(fileOutputStream2);
            throw th;
        }
    }
}
