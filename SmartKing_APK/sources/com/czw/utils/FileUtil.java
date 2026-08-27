package com.czw.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class FileUtil {
    static final String folder = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String shareTmpImg = "/tmps/share/";

    public static String saveImage(Bitmap bitmap) {
        File file = new File(folder + shareTmpImg);
        File file2 = new File(file, System.currentTimeMillis() + ".jpg");
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write(ImageUtil.bitmap2Byte(bitmap));
            fileOutputStream.flush();
            fileOutputStream.close();
            return file2.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String saveImage(InputStream inputStream) {
        File file = new File(folder + shareTmpImg);
        File file2 = new File(file, System.currentTimeMillis() + ".jpg");
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return file2.getAbsolutePath();
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
