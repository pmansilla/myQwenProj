package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.sun.mail.imap.IMAPStore;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import me.panpf.sketch.uri.FileUriModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: Utility.java */
/* loaded from: classes.dex */
public class cm {
    public static long a() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return 0L;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return statFs.getFreeBlocks() * statFs.getBlockSize();
    }

    public static long a(File file) {
        if (!file.isDirectory()) {
            return file.length();
        }
        long j = 0;
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return 0L;
        }
        for (File file2 : listFiles) {
            j += file2.isDirectory() ? a(file2) : file2.length();
        }
        return j;
    }

    public static OfflineMapProvince a(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return null;
        }
        OfflineMapProvince offlineMapProvince = new OfflineMapProvince();
        offlineMapProvince.setUrl(a(jSONObject, FileDownloadModel.URL));
        offlineMapProvince.setProvinceName(a(jSONObject, IMAPStore.ID_NAME));
        offlineMapProvince.setJianpin(a(jSONObject, "jianpin"));
        offlineMapProvince.setPinyin(a(jSONObject, "pinyin"));
        offlineMapProvince.setProvinceCode(d(a(jSONObject, "adcode")));
        offlineMapProvince.setVersion(a(jSONObject, IMAPStore.ID_VERSION));
        offlineMapProvince.setSize(Long.parseLong(a(jSONObject, "size")));
        offlineMapProvince.setCityList(b(jSONObject));
        return offlineMapProvince;
    }

    public static String a(Context context, String str) {
        try {
            return fr.a(fl.a(context).open(str));
        } catch (Throwable th) {
            ic.c(th, "MapDownloadManager", "readOfflineAsset");
            th.printStackTrace();
            return null;
        }
    }

    public static String a(JSONObject jSONObject, String str) throws JSONException {
        return (jSONObject == null || !jSONObject.has(str) || "[]".equals(jSONObject.getString(str))) ? "" : jSONObject.optString(str).trim();
    }

    public static List<OfflineMapProvince> a(String str, Context context) throws JSONException {
        return (str == null || "".equals(str)) ? new ArrayList() : a(new JSONObject(str), context);
    }

    public static List<OfflineMapProvince> a(JSONObject jSONObject, Context context) throws JSONException {
        JSONObject optJSONObject;
        JSONObject optJSONObject2;
        ArrayList arrayList = new ArrayList();
        if (jSONObject.has("result")) {
            optJSONObject = jSONObject.optJSONObject("result");
        } else {
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("result", new JSONObject().put("offlinemap_with_province_vfour", jSONObject));
                c(jSONObject2.toString(), context);
                optJSONObject = jSONObject2.optJSONObject("result");
            } catch (JSONException e) {
                JSONObject optJSONObject3 = jSONObject.optJSONObject("result");
                ic.c(e, "Utility", "parseJson");
                e.printStackTrace();
                optJSONObject = optJSONObject3;
            }
        }
        if (optJSONObject != null) {
            JSONObject optJSONObject4 = optJSONObject.optJSONObject("offlinemap_with_province_vfour");
            if (optJSONObject4 == null) {
                return arrayList;
            }
            optJSONObject2 = optJSONObject4.optJSONObject("offlinemapinfo_with_province");
        } else {
            optJSONObject2 = jSONObject.optJSONObject("offlinemapinfo_with_province");
        }
        if (optJSONObject2 == null) {
            return arrayList;
        }
        if (optJSONObject2.has(IMAPStore.ID_VERSION)) {
            bp.d = a(optJSONObject2, IMAPStore.ID_VERSION);
        }
        JSONArray optJSONArray = optJSONObject2.optJSONArray("provinces");
        if (optJSONArray == null) {
            return arrayList;
        }
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject optJSONObject5 = optJSONArray.optJSONObject(i);
            if (optJSONObject5 != null) {
                arrayList.add(a(optJSONObject5));
            }
        }
        JSONArray optJSONArray2 = optJSONObject2.optJSONArray("others");
        JSONObject jSONObject3 = null;
        if (optJSONArray2 != null && optJSONArray2.length() > 0) {
            jSONObject3 = optJSONArray2.getJSONObject(0);
        }
        if (jSONObject3 == null) {
            return arrayList;
        }
        arrayList.add(a(jSONObject3));
        return arrayList;
    }

    public static void a(String str) {
    }

    public static ArrayList<OfflineMapCity> b(JSONObject jSONObject) throws JSONException {
        JSONArray optJSONArray = jSONObject.optJSONArray("cities");
        ArrayList<OfflineMapCity> arrayList = new ArrayList<>();
        if (optJSONArray == null) {
            return arrayList;
        }
        if (optJSONArray.length() == 0) {
            arrayList.add(c(jSONObject));
        }
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject optJSONObject = optJSONArray.optJSONObject(i);
            if (optJSONObject != null) {
                arrayList.add(c(optJSONObject));
            }
        }
        return arrayList;
    }

    public static void b(String str) {
        File[] listFiles;
        File file = new File(str);
        if (file.exists() && file.isDirectory() && (listFiles = file.listFiles()) != null) {
            for (File file2 : listFiles) {
                if (file2.exists() && file2.isDirectory()) {
                    String[] list = file2.list();
                    if (list == null) {
                        file2.delete();
                    } else if (list.length == 0) {
                        file2.delete();
                    }
                }
            }
        }
    }

    public static void b(String str, Context context) throws IOException, Exception {
        File[] listFiles = new File(fr.c(context)).listFiles();
        if (listFiles == null) {
            return;
        }
        for (File file : listFiles) {
            if (file.exists() && file.getName().contains(str)) {
                b(file);
            }
        }
        b(fr.c(context));
    }

    public static boolean b(File file) throws IOException, Exception {
        if (file == null || !file.exists()) {
            return false;
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isFile()) {
                    if (!listFiles[i].delete()) {
                        return false;
                    }
                } else if (!b(listFiles[i])) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static OfflineMapCity c(JSONObject jSONObject) throws JSONException {
        OfflineMapCity offlineMapCity = new OfflineMapCity();
        offlineMapCity.setAdcode(d(a(jSONObject, "adcode")));
        offlineMapCity.setUrl(a(jSONObject, FileDownloadModel.URL));
        offlineMapCity.setCity(a(jSONObject, IMAPStore.ID_NAME));
        offlineMapCity.setCode(a(jSONObject, "citycode"));
        offlineMapCity.setPinyin(a(jSONObject, "pinyin"));
        offlineMapCity.setJianpin(a(jSONObject, "jianpin"));
        offlineMapCity.setVersion(a(jSONObject, IMAPStore.ID_VERSION));
        offlineMapCity.setSize(Long.parseLong(a(jSONObject, "size")));
        return offlineMapCity;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r5v12 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v5, types: [java.io.BufferedReader] */
    public static String c(File file) {
        FileInputStream fileInputStream;
        BufferedReader bufferedReader;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            try {
                try {
                    fileInputStream = new FileInputStream((File) file);
                } catch (Throwable th) {
                    th = th;
                }
            } catch (FileNotFoundException e) {
                e = e;
                bufferedReader = null;
                fileInputStream = null;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = null;
                fileInputStream = null;
            } catch (Throwable th2) {
                th = th2;
                file = 0;
                fileInputStream = null;
            }
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "utf-8"));
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        stringBuffer.append(readLine);
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        ic.c(e, "MapDownloadManager", "readOfflineSD filenotfound");
                        e.printStackTrace();
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return null;
                    } catch (IOException e5) {
                        e = e5;
                        ic.c(e, "MapDownloadManager", "readOfflineSD io");
                        e.printStackTrace();
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e6) {
                                e6.printStackTrace();
                            }
                        }
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return null;
                    }
                }
                String stringBuffer2 = stringBuffer.toString();
                try {
                    bufferedReader.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
                try {
                    fileInputStream.close();
                } catch (IOException e8) {
                    e8.printStackTrace();
                }
                return stringBuffer2;
            } catch (FileNotFoundException e9) {
                e = e9;
                bufferedReader = null;
            } catch (IOException e10) {
                e = e10;
                bufferedReader = null;
            } catch (Throwable th3) {
                th = th3;
                file = 0;
                if (file != 0) {
                    try {
                        file.close();
                    } catch (IOException e11) {
                        e11.printStackTrace();
                    }
                }
                if (fileInputStream == null) {
                    throw th;
                }
                try {
                    fileInputStream.close();
                    throw th;
                } catch (IOException e12) {
                    e12.printStackTrace();
                    throw th;
                }
            }
        } catch (IOException e13) {
            e13.printStackTrace();
        }
    }

    public static String c(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            return str.substring(str.lastIndexOf(FileUriModel.SCHEME) + 1, str.indexOf(".zip"));
        } catch (Throwable th) {
            ic.c(th, "Utility", "getZipFileNameFromUrl");
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v19, types: [java.lang.String] */
    public static void c(String str, Context context) {
        FileOutputStream fileOutputStream;
        if ("".equals(fr.c(context))) {
            return;
        }
        File file = new File(fr.c(context) + "offlinemapv4.png");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                ic.c(e, "OfflineUpdateCityHandlerAbstract", "writeSD dirCreate");
                e.printStackTrace();
            }
        }
        if (a() > PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            FileOutputStream fileOutputStream2 = null;
            fileOutputStream2 = null;
            fileOutputStream2 = null;
            fileOutputStream2 = null;
            try {
                try {
                    try {
                        fileOutputStream = new FileOutputStream(file);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                } catch (FileNotFoundException e3) {
                    e = e3;
                } catch (IOException e4) {
                    e = e4;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                ?? r6 = "utf-8";
                fileOutputStream.write(str.getBytes("utf-8"));
                fileOutputStream.close();
                fileOutputStream2 = r6;
            } catch (FileNotFoundException e5) {
                e = e5;
                fileOutputStream2 = fileOutputStream;
                ic.c(e, "OfflineUpdateCityHandlerAbstract", "writeSD filenotfound");
                e.printStackTrace();
                if (fileOutputStream2 != null) {
                    fileOutputStream2.close();
                    fileOutputStream2 = fileOutputStream2;
                }
            } catch (IOException e6) {
                e = e6;
                fileOutputStream2 = fileOutputStream;
                ic.c(e, "OfflineUpdateCityHandlerAbstract", "writeSD io");
                e.printStackTrace();
                if (fileOutputStream2 != null) {
                    fileOutputStream2.close();
                    fileOutputStream2 = fileOutputStream2;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream2 = fileOutputStream;
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e7) {
                        e7.printStackTrace();
                    }
                }
                throw th;
            }
        }
    }

    private static String d(String str) {
        return "000001".equals(str) ? "100000" : str;
    }
}
