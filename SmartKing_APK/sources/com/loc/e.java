package com.loc;

import android.content.Context;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/* compiled from: FileStorageModel.java */
/* loaded from: classes.dex */
public final class e {
    private static String a(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        try {
            Class<?> cls = Class.forName("android.os.storage.StorageVolume");
            Method method = storageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method method2 = cls.getMethod("getPath", new Class[0]);
            Method method3 = cls.getMethod("isRemovable", new Class[0]);
            Object invoke = method.invoke(storageManager, new Object[0]);
            int length = Array.getLength(invoke);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(invoke, i);
                String str = (String) method2.invoke(obj, new Object[0]);
                if (!((Boolean) method3.invoke(obj, new Object[0])).booleanValue()) {
                    return str;
                }
            }
            return null;
        } catch (Throwable unused) {
            return null;
        }
    }

    public static synchronized String a(Context context, String str) {
        RandomAccessFile randomAccessFile;
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        String str2;
        String[] split;
        synchronized (e.class) {
            String a = a(context);
            if (TextUtils.isEmpty(a)) {
                return "";
            }
            File file = new File(a + File.separator + "backups", ".adiu");
            if (file.exists() && file.canRead()) {
                if (file.length() == 0) {
                    file.delete();
                    return "";
                }
                ByteArrayOutputStream byteArrayOutputStream2 = null;
                try {
                    randomAccessFile = new RandomAccessFile(file, "r");
                } catch (Throwable th2) {
                    th = th2;
                    randomAccessFile = null;
                    byteArrayOutputStream = null;
                }
                try {
                    byte[] bArr = new byte[1024];
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    while (true) {
                        try {
                            int read = randomAccessFile.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            byteArrayOutputStream.write(bArr, 0, read);
                        } catch (Throwable th3) {
                            th = th3;
                            a(byteArrayOutputStream);
                            a(randomAccessFile);
                            throw th;
                        }
                    }
                    str2 = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
                } catch (Throwable th4) {
                    th = th4;
                    byteArrayOutputStream = null;
                    th = th;
                    a(byteArrayOutputStream);
                    a(randomAccessFile);
                    throw th;
                }
                if (TextUtils.isEmpty(str2) || !str2.contains("#") || (split = str2.split("#")) == null || split.length != 2 || !TextUtils.equals(str, split[0])) {
                    a(byteArrayOutputStream);
                    a(randomAccessFile);
                    return "";
                }
                String str3 = split[1];
                a(byteArrayOutputStream);
                a(randomAccessFile);
                return str3;
            }
            return "";
        }
    }

    public static synchronized void a(Context context, String str, String str2) {
        FileChannel fileChannel;
        RandomAccessFile randomAccessFile;
        FileChannel fileChannel2;
        synchronized (e.class) {
            String a = a(context);
            if (TextUtils.isEmpty(a)) {
                return;
            }
            String str3 = str + "#" + str2;
            File file = new File(a + File.separator + "backups");
            File file2 = new File(file, ".adiu");
            FileLock fileLock = null;
            try {
                if (!file.exists() || file.isDirectory()) {
                    file.mkdirs();
                }
                file2.createNewFile();
                randomAccessFile = new RandomAccessFile(file2, "rws");
                try {
                    fileChannel2 = randomAccessFile.getChannel();
                    try {
                        FileLock tryLock = fileChannel2.tryLock();
                        if (tryLock != null) {
                            try {
                                fileChannel2.write(ByteBuffer.wrap(str3.getBytes("UTF-8")));
                            } catch (Throwable th) {
                                th = th;
                                fileLock = tryLock;
                                Throwable th2 = th;
                                fileChannel = fileChannel2;
                                th = th2;
                                if (fileLock != null) {
                                    try {
                                        fileLock.release();
                                    } catch (IOException unused) {
                                    }
                                }
                                if (fileChannel != null) {
                                    try {
                                        fileChannel.close();
                                    } catch (IOException unused2) {
                                    }
                                }
                                a(randomAccessFile);
                                throw th;
                            }
                        }
                        if (tryLock != null) {
                            try {
                                tryLock.release();
                            } catch (IOException unused3) {
                            }
                        }
                        if (fileChannel2 != null) {
                            try {
                                fileChannel2.close();
                            } catch (IOException unused4) {
                            }
                        }
                        a(randomAccessFile);
                    } catch (Throwable unused5) {
                    }
                } catch (Throwable unused6) {
                    fileChannel2 = null;
                }
            } catch (Throwable th3) {
                th = th3;
                fileChannel = null;
                randomAccessFile = null;
            }
        }
    }

    private static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable unused) {
            }
        }
    }
}
