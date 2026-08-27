package ycble.runchinaup.device;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes2.dex */
public class BleDevice implements Serializable {
    public static final String adv_manufacturer_data = "FF";
    public static final String adv_name_length = "09";
    public static final String adv_name_short = "08";
    public static final String adv_serviceUUID_16bit = "03";
    private String mac;
    private String name;
    private int rssi;
    private byte[] scanBytes;
    private String scanHexStr;

    public BleDevice(String str, String str2) {
        this.name = "null";
        this.name = str;
        this.mac = str2;
    }

    public BleDevice(String str, String str2, byte[] bArr) {
        this.name = "null";
        this.name = str;
        this.mac = str2;
        this.scanBytes = bArr;
    }

    public static BleDevice createADebugDeice(String str) {
        return new BleDevice("debug", str);
    }

    private HashMap<String, String> getAdvData() {
        int i;
        int i2;
        this.scanHexStr = this.scanHexStr.replace(SQLBuilder.BLANK, "");
        if (TextUtils.isEmpty(this.scanHexStr) || this.scanHexStr.startsWith("00") || this.scanHexStr.length() < 1 || this.scanHexStr.length() > 124) {
            return null;
        }
        ycBleLog.w(this.mac + "==>原始广播数据:" + this.scanHexStr);
        int length = this.scanHexStr.length();
        HashMap<String, String> hashMap = new HashMap<>();
        String str = this.scanHexStr;
        int intValue = Integer.valueOf(str.substring(0, 2), 16).intValue();
        if (intValue <= 0 || intValue >= 30 || this.scanHexStr.substring(2).length() < (i = intValue * 2)) {
            return null;
        }
        hashMap.put(String.format("%02X", Integer.valueOf(Integer.valueOf(str.substring(2, 4), 16).intValue())), str.substring(4, (i + 4) - 2));
        int i3 = 0;
        while (i3 < length) {
            int i4 = i3 + 2;
            int intValue2 = Integer.valueOf(str.substring(i3, i4), 16).intValue();
            if (intValue2 == 0 || intValue2 == 0 || intValue2 > 60 || this.scanHexStr.substring(i4).length() < (i2 = intValue2 * 2)) {
                break;
            }
            int i5 = i3 + 4;
            hashMap.put(String.format("%02X", Integer.valueOf(Integer.valueOf(str.substring(i4, i5), 16).intValue())), str.substring(i5, (i5 + i2) - 2));
            i3 += i2 + 2;
        }
        return hashMap;
    }

    private String getAdvName() {
        HashMap<String, String> advData = getAdvData();
        return (advData == null || !advData.containsKey(adv_name_length) || TextUtils.isEmpty(advData.get(adv_name_length))) ? "null" : new String(BleUtil.hexStr2Byte(advData.get(adv_name_length)));
    }

    public static synchronized BleDevice parserFromScanData(BluetoothDevice bluetoothDevice, byte[] bArr, int i) {
        BleDevice bleDevice;
        synchronized (BleDevice.class) {
            bleDevice = new BleDevice(bluetoothDevice.getName(), bluetoothDevice.getAddress(), bArr);
            bleDevice.setRssi(i);
        }
        return bleDevice;
    }

    public String getMac() {
        return this.mac;
    }

    public String getName() {
        return TextUtils.isEmpty(this.name) ? "null" : this.name;
    }

    public int getRssi() {
        return this.rssi;
    }

    public byte[] getScanBytes() {
        return this.scanBytes;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setRssi(int i) {
        this.rssi = i;
    }

    public void setScanBytes(byte[] bArr) {
        this.scanBytes = bArr;
    }

    public String toString() {
        return "BleDevice{mac='" + this.mac + "', name='" + this.name + "', rssi=" + this.rssi + ", scanBytes=" + Arrays.toString(this.scanBytes) + '}';
    }
}
