package com.czw.smartkit.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.amap.location.common.model.AmapLoc;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/* loaded from: classes.dex */
public class YCAppUtils {
    private Context mContext;

    public YCAppUtils(Context context) {
        this.mContext = context;
    }

    public static boolean isChainess() {
        return Locale.getDefault().toString().contains("zh_");
    }

    public static boolean isChinaCountry() {
        return Locale.getDefault().getCountry().equalsIgnoreCase("cn");
    }

    public String getAndroidID() {
        return Settings.Secure.getString(this.mContext.getContentResolver(), "android_id");
    }

    public String getBTMACAddress() {
        return BluetoothAdapter.getDefaultAdapter().getAddress();
    }

    public String getIMEI() {
        return ((TelephonyManager) this.mContext.getSystemService("phone")).getDeviceId();
    }

    public String getPesudoUniqueID() {
        return "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.DISPLAY.length() % 10) + (Build.HOST.length() % 10) + (Build.ID.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10) + (Build.TAGS.length() % 10) + (Build.TYPE.length() % 10) + (Build.USER.length() % 10);
    }

    public String getUniqueID() {
        MessageDigest messageDigest;
        String str = getPesudoUniqueID() + getWLANMACAddress() + getBTMACAddress();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            messageDigest = null;
        }
        messageDigest.update(str.getBytes(), 0, str.length());
        byte[] digest = messageDigest.digest();
        String str2 = new String();
        for (byte b : digest) {
            int i = b & 255;
            if (i <= 15) {
                str2 = str2 + AmapLoc.RESULT_TYPE_GPS;
            }
            str2 = str2 + Integer.toHexString(i);
        }
        return str2.toUpperCase();
    }

    public String getWLANMACAddress() {
        return ((WifiManager) this.mContext.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }
}
