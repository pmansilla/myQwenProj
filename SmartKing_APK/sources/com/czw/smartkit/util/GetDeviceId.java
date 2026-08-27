package com.czw.smartkit.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;
import com.amap.location.common.model.AmapLoc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

/* loaded from: classes.dex */
public class GetDeviceId {
    private static final String CACHE_IMAGE_DIR = "aray/cache/devices";
    private static final String DEVICES_FILE_NAME = ".DEVICES";
    private static final String SP_DEVICES_ID = "SP_DEVICES_ID";

    public static String bytesToHex(byte[] bArr, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i];
            if (i2 < 0) {
                i2 += 256;
            }
            if (i2 < 16) {
                stringBuffer.append(AmapLoc.RESULT_TYPE_GPS);
            }
            stringBuffer.append(Integer.toHexString(i2));
        }
        return z ? stringBuffer.toString().toUpperCase() : stringBuffer.toString().toLowerCase();
    }

    private void debug(final Context context) {
        new Thread(new Runnable() { // from class: com.czw.smartkit.util.GetDeviceId.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    String readDeviceID = GetDeviceId.readDeviceID(context);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("uniun_cfg", 0);
                    String string = sharedPreferences.getString(GetDeviceId.SP_DEVICES_ID, readDeviceID);
                    if (string != null && StringUtils.isBlank(readDeviceID) && !string.equals(readDeviceID) && StringUtils.isBlank(readDeviceID) && !StringUtils.isBlank(string)) {
                        GetDeviceId.saveDeviceID(string, context);
                        readDeviceID = string;
                    }
                    if (StringUtils.isBlank(readDeviceID)) {
                        readDeviceID = GetDeviceId.getDeviceId(context);
                    }
                    sharedPreferences.edit().putString(GetDeviceId.SP_DEVICES_ID, readDeviceID).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String getDeviceId(Context context) {
        String readDeviceID = readDeviceID(context);
        StringBuffer stringBuffer = new StringBuffer();
        if (readDeviceID != null && !"".equals(readDeviceID)) {
            return readDeviceID;
        }
        try {
            stringBuffer.append(getIMIEStatus(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stringBuffer.append(getLocalMac(context).replace(":", ""));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (stringBuffer.length() <= 0) {
            stringBuffer.append(UUID.randomUUID().toString().replace("-", ""));
        }
        String md5 = getMD5(stringBuffer.toString(), false);
        if (stringBuffer.length() > 0) {
            saveDeviceID(md5, context);
        }
        return md5;
    }

    private static File getDevicesDir(Context context) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file = new File(Environment.getExternalStorageDirectory(), CACHE_IMAGE_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            return new File(file, DEVICES_FILE_NAME);
        }
        File file2 = new File(context.getFilesDir(), CACHE_IMAGE_DIR);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return new File(file2, DEVICES_FILE_NAME);
    }

    private static String getIMIEStatus(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    private static String getLocalMac(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            NetworkInterface byName = NetworkInterface.getByName("eth1");
            if (byName == null) {
                byName = NetworkInterface.getByName("wlan0");
            }
            if (byName == null) {
                return "";
            }
            for (byte b : byName.getHardwareAddress()) {
                stringBuffer.append(String.format("%02X:", Byte.valueOf(b)));
            }
            if (stringBuffer.length() > 0) {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            }
            return stringBuffer.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMD5(String str, boolean z) {
        try {
            return bytesToHex(MessageDigest.getInstance("MD5").digest(str.getBytes()), z);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String readDeviceID(Context context) {
        File devicesDir = getDevicesDir(context);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(devicesDir), "UTF-8"));
            while (true) {
                int read = bufferedReader.read();
                if (read <= -1) {
                    bufferedReader.close();
                    return stringBuffer.toString();
                }
                stringBuffer.append((char) read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveDeviceID(String str, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(getDevicesDir(context)), "UTF-8");
            outputStreamWriter.write(str);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
