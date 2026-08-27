package basecamera.module.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import basecamera.module.activity.model.PhotoItem;
import com.luck.picture.lib.config.PictureMimeType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class FileUtils {
    private static String BASE_PATH;
    private static String STICKER_BASE_PATH;
    private static FileUtils mInstance;

    private FileUtils(Context context) {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/stickercamera/";
        } else {
            BASE_PATH = context.getCacheDir().getAbsolutePath();
        }
        STICKER_BASE_PATH = BASE_PATH + "/stickers/";
    }

    private boolean copyAssetFileToFiles(Context context, String str, File file) {
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        InputStream inputStream2 = null;
        try {
            inputStream = context.getAssets().open(str);
            try {
                createFile(file);
                fileOutputStream = new FileOutputStream(file);
            } catch (IOException e) {
                e = e;
                fileOutputStream = null;
            } catch (Throwable th) {
                th = th;
                fileOutputStream = null;
            }
        } catch (IOException e2) {
            e = e2;
            fileOutputStream = null;
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            fileOutputStream = null;
        }
        try {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    fileOutputStream.flush();
                    IOUtil.closeStream(inputStream);
                    IOUtil.closeStream(fileOutputStream);
                    return true;
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (IOException e3) {
            e = e3;
            inputStream2 = inputStream;
            try {
                e.printStackTrace();
                IOUtil.closeStream(inputStream2);
                IOUtil.closeStream(fileOutputStream);
                return false;
            } catch (Throwable th3) {
                th = th3;
                inputStream = inputStream2;
                IOUtil.closeStream(inputStream);
                IOUtil.closeStream(fileOutputStream);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            IOUtil.closeStream(inputStream);
            IOUtil.closeStream(fileOutputStream);
            throw th;
        }
    }

    private String getImageFilePath(int i, String str) {
        return getBasePath(i) + MD5Util.getMD5(str).replace("-", "mm");
    }

    public static FileUtils getInst(Context context) {
        if (mInstance == null) {
            synchronized (FileUtils.class) {
                if (mInstance == null) {
                    mInstance = new FileUtils(context);
                }
            }
        }
        return mInstance;
    }

    public boolean copyAssetDirToFiles(Context context, String str) {
        try {
            AssetManager assets = context.getAssets();
            for (String str2 : assets.list(str)) {
                String str3 = str + '/' + str2;
                if (assets.list(str3).length == 0) {
                    copyAssetFileToFiles(context, str3);
                } else {
                    copyAssetDirToFiles(context, str3);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean copyAssetFileToFiles(Context context, String str) {
        return copyAssetFileToFiles(context, str, getExtFile(FileUriModel.SCHEME + str));
    }

    public void copyFile(String str, String str2) {
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream = null;
        try {
            if (new File(str).exists()) {
                FileInputStream fileInputStream2 = new FileInputStream(str);
                try {
                    fileOutputStream = new FileOutputStream(str2);
                } catch (Exception e) {
                    e = e;
                    fileOutputStream = null;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = null;
                }
                try {
                    byte[] bArr = new byte[1444];
                    int i = 0;
                    while (true) {
                        int read = fileInputStream2.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        i += read;
                        System.out.println(i);
                        fileOutputStream.write(bArr, 0, read);
                    }
                    fileInputStream = fileInputStream2;
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    try {
                        System.out.println("复制单个文件操作出错");
                        e.printStackTrace();
                        IOUtil.closeStream(fileInputStream);
                        IOUtil.closeStream(fileOutputStream);
                    } catch (Throwable th2) {
                        th = th2;
                        IOUtil.closeStream(fileInputStream);
                        IOUtil.closeStream(fileOutputStream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = fileInputStream2;
                    IOUtil.closeStream(fileInputStream);
                    IOUtil.closeStream(fileOutputStream);
                    throw th;
                }
            } else {
                fileOutputStream = null;
            }
        } catch (Exception e3) {
            e = e3;
            fileOutputStream = null;
        } catch (Throwable th4) {
            th = th4;
            fileOutputStream = null;
        }
        IOUtil.closeStream(fileInputStream);
        IOUtil.closeStream(fileOutputStream);
    }

    public boolean createFile(File file) {
        try {
            if (!file.getParentFile().exists()) {
                mkdir(file.getParentFile());
            }
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                file.delete();
                return;
            }
            for (File file2 : listFiles) {
                delete(file2);
            }
            file.delete();
        }
    }

    public ArrayList<PhotoItem> findPicsInDir(String str) {
        ArrayList<PhotoItem> arrayList = new ArrayList<>();
        File file = new File(str);
        if (file.exists() && file.isDirectory()) {
            for (File file2 : file.listFiles(new FileFilter() { // from class: basecamera.module.utils.FileUtils.1
                @Override // java.io.FileFilter
                public boolean accept(File file3) {
                    String absolutePath = file3.getAbsolutePath();
                    return absolutePath.endsWith(PictureMimeType.PNG) || absolutePath.endsWith(".jpg") || absolutePath.endsWith(".jepg");
                }
            })) {
                arrayList.add(new PhotoItem(file2.getAbsolutePath(), file2.lastModified()));
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public String getBasePath(int i) {
        return STICKER_BASE_PATH + i + FileUriModel.SCHEME;
    }

    public File getExtFile(String str) {
        return new File(BASE_PATH + str);
    }

    public long getFolderSize(File file) {
        try {
            if (!file.exists()) {
                return 0L;
            }
            if (!file.isDirectory()) {
                return file.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
            }
            File[] listFiles = file.listFiles();
            long j = 0;
            for (int i = 0; i < listFiles.length; i++) {
                j = listFiles[i].isDirectory() ? j + getFolderSize(listFiles[i]) : j + listFiles[i].length();
            }
            return j / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        } catch (Exception unused) {
            return 0L;
        }
    }

    public String getPhotoSavedPath() {
        return BASE_PATH + "stickercamera";
    }

    public String getPhotoTempPath() {
        return BASE_PATH + "stickercamera";
    }

    public String getSystemPhotoPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM";
    }

    public boolean mkdir(File file) {
        while (!file.getParentFile().exists()) {
            mkdir(file.getParentFile());
        }
        return file.mkdir();
    }

    public String readFromAsset(Context context, String str) {
        BufferedReader bufferedReader;
        Throwable th;
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(str);
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            } catch (Exception unused) {
                bufferedReader = null;
            } catch (Throwable th2) {
                th = th2;
                bufferedReader = null;
            }
            try {
                String str2 = "";
                for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                    str2 = str2 + readLine;
                }
                IOUtil.closeStream(bufferedReader);
                IOUtil.closeStream(inputStream);
                return str2;
            } catch (Exception unused2) {
                IOUtil.closeStream(bufferedReader);
                IOUtil.closeStream(inputStream);
                return null;
            } catch (Throwable th3) {
                th = th3;
                IOUtil.closeStream(bufferedReader);
                IOUtil.closeStream(inputStream);
                throw th;
            }
        } catch (Exception unused3) {
            inputStream = null;
            bufferedReader = null;
        } catch (Throwable th4) {
            bufferedReader = null;
            th = th4;
            inputStream = null;
        }
    }

    public String readSimpleString(File file) {
        BufferedReader bufferedReader;
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader2 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            String readLine = bufferedReader.readLine();
            if (StringUtils.isNotEmpty(readLine)) {
                stringBuffer.append(readLine.trim());
                bufferedReader.readLine();
            }
            IOUtil.closeStream(bufferedReader);
            return stringBuffer.toString();
        } catch (Throwable th3) {
            th = th3;
            bufferedReader2 = bufferedReader;
            IOUtil.closeStream(bufferedReader2);
            throw th;
        }
    }

    public void removeAddonFolder(int i) {
        File file = new File(getBasePath(i));
        if (file.exists()) {
            delete(file);
        }
    }

    public boolean renameDir(String str, String str2) {
        File file = new File(str);
        File file2 = new File(str2);
        return file.exists() && !file2.exists() && file.renameTo(file2);
    }

    public boolean writeSimpleString(File file, String str) {
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            fileOutputStream.write(str.getBytes());
            IOUtil.closeStream(fileOutputStream);
            return true;
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream2 = fileOutputStream;
            th.printStackTrace();
            IOUtil.closeStream(fileOutputStream2);
            return false;
        }
    }
}
