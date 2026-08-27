package com.autonavi.amap.mapcore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import com.amap.api.mapcore.util.ic;
import com.amap.api.maps.MapsInitializer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';

    public static boolean checkCanWrite(File file) {
        if (file == null || !file.canWrite()) {
            return false;
        }
        File file2 = new File(file, "amap.tmp");
        try {
            file2.createNewFile();
            if (!file2.exists()) {
                return false;
            }
            try {
                file2.delete();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static void copy(Context context, String str, File file) throws Exception {
        file.delete();
        InputStream open = context.getAssets().open(str);
        byte[] bArr = new byte[open.available()];
        open.read(bArr);
        open.close();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bArr);
        fileOutputStream.close();
    }

    public static void createNoMediaFileIfNotExist(String str) {
    }

    public static boolean deleteFile(File file) {
        File[] listFiles;
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isFile()) {
                    if (!listFiles[i].delete()) {
                        return false;
                    }
                } else {
                    if (!deleteFile(listFiles[i])) {
                        return false;
                    }
                    listFiles[i].delete();
                }
            }
        }
        file.delete();
        return true;
    }

    public static String getExternalStroragePath(Context context) {
        int i;
        String str;
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 12) {
            try {
                StorageManager storageManager = (StorageManager) context.getSystemService("storage");
                Method method = StorageManager.class.getMethod("getVolumeList", new Class[0]);
                int i3 = 1;
                Method method2 = StorageManager.class.getMethod("getVolumeState", String.class);
                Object[] objArr = (Object[]) method.invoke(storageManager, new Object[0]);
                Boolean.valueOf(false);
                int length = objArr.length;
                String str2 = "";
                String str3 = "";
                int i4 = 0;
                while (true) {
                    if (i4 >= length) {
                        i = 18;
                        str = null;
                        break;
                    }
                    Object obj = objArr[i4];
                    Method method3 = obj.getClass().getMethod("getPath", new Class[0]);
                    Method method4 = obj.getClass().getMethod("isRemovable", new Class[0]);
                    String str4 = (String) method3.invoke(obj, new Object[0]);
                    Object[] objArr2 = new Object[i3];
                    objArr2[0] = method3.invoke(obj, new Object[0]);
                    String str5 = (String) method2.invoke(storageManager, objArr2);
                    Boolean bool = (Boolean) method4.invoke(obj, new Object[0]);
                    if (TextUtils.isEmpty(str4) || !str4.toLowerCase().contains("private")) {
                        if (!bool.booleanValue()) {
                            i3 = 1;
                            str2 = str5;
                            str3 = str4;
                            i4++;
                        } else if (str4 != null && str5 != null && str5.equals("mounted")) {
                            if (i2 > 18) {
                                try {
                                    File[] externalFilesDirs = context.getExternalFilesDirs(null);
                                    if (externalFilesDirs == null) {
                                        str = null;
                                    } else if (externalFilesDirs.length > 1) {
                                        str = externalFilesDirs[1].getAbsolutePath();
                                    }
                                } catch (Exception unused) {
                                }
                                i = 18;
                            }
                            str = str4;
                            i = 18;
                        }
                    }
                    i3 = 1;
                    i4++;
                }
                return i2 <= i ? (str != null || str3 == null || str2 == null || !str2.equals("mounted")) ? str : str3 : (str3 == null || str2 == null || !str2.equals("mounted")) ? str : str3;
            } catch (Throwable unused2) {
            }
        }
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    public static String getMapBaseStorage(Context context) {
        String string;
        if (context == null) {
            return null;
        }
        String str = Build.VERSION.SDK_INT > 18 ? "map_base_path_v44" : "map_base_path";
        SharedPreferences sharedPreferences = context.getSharedPreferences("base_path", 0);
        if (MapsInitializer.sdcardDir == null || MapsInitializer.sdcardDir.trim().length() <= 0) {
            string = sharedPreferences.getString(str, "");
        } else {
            string = MapsInitializer.sdcardDir + File.separatorChar;
        }
        if (string != null && string.length() > 2) {
            File file = new File(string);
            if (!file.exists()) {
                file.mkdir();
            }
            if (file.isDirectory()) {
                if (checkCanWrite(file)) {
                    return string;
                }
                String str2 = context.getCacheDir().toString() + AeUtil.ROOTPATH;
                if (str2 != null && str2.length() > 2) {
                    File file2 = new File(str2);
                    if (!file2.exists()) {
                        file2.mkdir();
                    }
                    if (file2.isDirectory()) {
                        return str2;
                    }
                }
            }
        }
        String str3 = getExternalStroragePath(context) + AeUtil.ROOTPATH;
        if (str3 != null && str3.length() > 2) {
            File file3 = new File(str3);
            if (!file3.exists()) {
                file3.mkdir();
            }
            if (file3.isDirectory() && file3.canWrite()) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(str, str3);
                edit.commit();
                createNoMediaFileIfNotExist(str3);
                return str3;
            }
        }
        String str4 = context.getCacheDir().toString() + AeUtil.ROOTPATH;
        if (str4 != null && str4.length() > 2) {
            File file4 = new File(str4);
            if (!file4.exists()) {
                file4.mkdir();
            }
            if (file4.isDirectory()) {
                return str4;
            }
        }
        return str4;
    }

    public static String getName(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(indexOfLastSeparator(str) + 1);
    }

    public static int indexOfLastSeparator(String str) {
        if (str == null) {
            return -1;
        }
        return Math.max(str.lastIndexOf(47), str.lastIndexOf(92));
    }

    public static byte[] readFileContents(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read == -1) {
                    byteArrayOutputStream.close();
                    fileInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (Throwable th) {
            ic.c(th, TAG, "readFileContents");
            return null;
        }
    }

    public static byte[] readFileContentsFromAssets(Context context, String str) {
        try {
            InputStream open = context.getAssets().open(str);
            int available = open.available();
            if (available == 0) {
                return null;
            }
            byte[] bArr = new byte[available];
            for (int i = 0; i < available; i += open.read(bArr, i, available - i)) {
            }
            open.close();
            return bArr;
        } catch (IOException unused) {
            return null;
        } catch (OutOfMemoryError unused2) {
            return null;
        }
    }

    public static void saveFile(String str, String str2, boolean z) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + FileUriModel.SCHEME + str2);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, z);
            fileOutputStream.write(str.getBytes("utf-8"));
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void writeDatasToFile(String str, byte[] bArr) {
        ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();
        writeLock.lock();
        try {
            if (bArr != null) {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bArr.length != 0) {
                    File file = new File(str);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(bArr);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
        } finally {
            writeLock.unlock();
        }
    }
}
