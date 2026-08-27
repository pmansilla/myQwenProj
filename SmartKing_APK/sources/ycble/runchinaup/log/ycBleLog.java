package ycble.runchinaup.log;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.panpf.sketch.uri.FileUriModel;
import ycble.runchinaup.util.PhoneDeviceUtil;

/* loaded from: classes.dex */
public class ycBleLog {
    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWriteLogToLocalFile = true;
    public static boolean allowWtf = true;
    static String appName = "ycBleLog";
    public static final String npBleTag = "ycBleTag";
    private static SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private ycBleLog() {
    }

    private static void appendFileHeader(String str, String str2) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile(str2, "rw");
        int length = (int) randomAccessFile.length();
        byte[] bArr = new byte[length];
        randomAccessFile.read(bArr, 0, length);
        randomAccessFile.seek(0L);
        randomAccessFile.write(str.getBytes("utf-8"));
        randomAccessFile.seek(r4.length);
        randomAccessFile.write(bArr);
        randomAccessFile.close();
    }

    public static synchronized void clearLogFile() {
        synchronized (ycBleLog.class) {
            File file = new File(Environment.getExternalStorageDirectory(), "ycBleLogs/" + appName);
            Log.e("===dir", file.getAbsolutePath());
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles == null) {
                    return;
                }
                for (File file2 : listFiles) {
                    if (file2 == null) {
                        return;
                    }
                    Log.e("===file", file2.getAbsolutePath());
                    file2.delete();
                }
            }
        }
    }

    public static void d(String str) {
        if (allowD) {
            Log.d("ycBleTag", str);
        }
    }

    public static void e(String str) {
        if (allowE) {
            Log.e("ycBleTag", str);
        }
        if (allowWriteLogToLocalFile) {
            writeFile(smp.format(new Date()) + "  " + str);
        }
    }

    public static File getBleLogFileDir() {
        return new File(Environment.getExternalStorageDirectory(), "ycBleLogs/" + appName);
    }

    private static String gteAppInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("手机品牌:" + PhoneDeviceUtil.getDeviceBrand());
        sb.append("\n");
        sb.append("手机型号:" + PhoneDeviceUtil.getSystemModel());
        sb.append("\n");
        sb.append("安卓版本:" + PhoneDeviceUtil.getSystemVersion());
        sb.append("\n");
        sb.append("语言环境:" + PhoneDeviceUtil.getSystemLanguage());
        sb.append("\n");
        return sb.toString();
    }

    public static void i(String str) {
        if (allowW) {
            Log.i("ycBleTag", str);
        }
    }

    public static void initLogDirName(String str) {
        Log.e("initBleLogDir", "初始化文件夹名称" + str);
        appName = str;
    }

    public static synchronized void reCreateLogFile(String str) {
        synchronized (ycBleLog.class) {
            File file = new File(Environment.getExternalStorageDirectory(), "ycBleLogs/" + appName);
            Log.e("===dir", file.getAbsolutePath());
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles == null) {
                    return;
                }
                for (File file2 : listFiles) {
                    if (file2 == null) {
                        return;
                    }
                    Log.e("===file", file2.getAbsolutePath());
                    file2.delete();
                }
            }
        }
    }

    public static void w(String str) {
        if (allowW) {
            Log.w("ycBleTag", str);
        }
    }

    public static synchronized void writeFile(String str) {
        synchronized (ycBleLog.class) {
            File file = new File(Environment.getExternalStorageDirectory(), "ycBleLogs/" + appName + FileUriModel.SCHEME);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, appName + ".txt");
            if (!file2.exists()) {
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file2, true));
                    bufferedWriter.write(gteAppInfo());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(file2, true));
                bufferedWriter2.write(str);
                bufferedWriter2.newLine();
                bufferedWriter2.flush();
                bufferedWriter2.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
