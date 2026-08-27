package no.nordicsemi.android.support.v18.scanner;

import android.os.ParcelUuid;
import android.util.Log;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public final class ScanRecord {
    private static final int DATA_TYPE_FLAGS = 1;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    private static final int DATA_TYPE_SERVICE_DATA_128_BIT = 33;
    private static final int DATA_TYPE_SERVICE_DATA_16_BIT = 22;
    private static final int DATA_TYPE_SERVICE_DATA_32_BIT = 32;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 10;
    private static final String TAG = "ScanRecord";
    private final int advertiseFlags;
    private final byte[] bytes;
    private final String deviceName;

    @Nullable
    private final SparseArray<byte[]> manufacturerSpecificData;

    @Nullable
    private final Map<ParcelUuid, byte[]> serviceData;

    @Nullable
    private final List<ParcelUuid> serviceUuids;
    private final int txPowerLevel;

    private ScanRecord(@Nullable List<ParcelUuid> list, @Nullable SparseArray<byte[]> sparseArray, @Nullable Map<ParcelUuid, byte[]> map, int i, int i2, String str, byte[] bArr) {
        this.serviceUuids = list;
        this.manufacturerSpecificData = sparseArray;
        this.serviceData = map;
        this.deviceName = str;
        this.advertiseFlags = i;
        this.txPowerLevel = i2;
        this.bytes = bArr;
    }

    private static byte[] extractBytes(@NonNull byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static ScanRecord parseFromBytes(@Nullable byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        int i = 0;
        ArrayList arrayList = null;
        SparseArray sparseArray = null;
        HashMap hashMap = null;
        String str = null;
        int i2 = -1;
        byte b = -2147483648;
        while (i < bArr.length) {
            try {
                int i3 = i + 1;
                int i4 = bArr[i] & 255;
                if (i4 == 0) {
                    return new ScanRecord(arrayList, sparseArray, hashMap, i2, b, str, bArr);
                }
                int i5 = i4 - 1;
                int i6 = i3 + 1;
                int i7 = bArr[i3] & 255;
                int i8 = 16;
                if (i7 != 22) {
                    if (i7 != 255) {
                        switch (i7) {
                            case 1:
                                i2 = bArr[i6] & 255;
                                continue;
                            case 2:
                            case 3:
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                parseServiceUuid(bArr, i6, i5, 2, arrayList);
                                continue;
                            case 4:
                            case 5:
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                parseServiceUuid(bArr, i6, i5, 4, arrayList);
                                continue;
                            case 6:
                            case 7:
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                parseServiceUuid(bArr, i6, i5, 16, arrayList);
                                continue;
                            case 8:
                            case 9:
                                str = new String(extractBytes(bArr, i6, i5));
                                continue;
                            case 10:
                                b = bArr[i6];
                                continue;
                            default:
                                switch (i7) {
                                    case 32:
                                    case 33:
                                        break;
                                    default:
                                        continue;
                                }
                        }
                    } else {
                        int i9 = ((bArr[i6 + 1] & 255) << 8) + (255 & bArr[i6]);
                        byte[] extractBytes = extractBytes(bArr, i6 + 2, i5 - 2);
                        if (sparseArray == null) {
                            sparseArray = new SparseArray();
                        }
                        sparseArray.put(i9, extractBytes);
                    }
                    i = i5 + i6;
                }
                if (i7 == 32) {
                    i8 = 4;
                } else if (i7 != 33) {
                    i8 = 2;
                }
                ParcelUuid parseUuidFrom = BluetoothUuid.parseUuidFrom(extractBytes(bArr, i6, i8));
                byte[] extractBytes2 = extractBytes(bArr, i6 + i8, i5 - i8);
                if (hashMap == null) {
                    hashMap = new HashMap();
                }
                hashMap.put(parseUuidFrom, extractBytes2);
                i = i5 + i6;
            } catch (Exception unused) {
                Log.e(TAG, "unable to parse scan record: " + Arrays.toString(bArr));
                return new ScanRecord(null, null, null, -1, Integer.MIN_VALUE, null, bArr);
            }
        }
        return new ScanRecord(arrayList, sparseArray, hashMap, i2, b, str, bArr);
    }

    private static int parseServiceUuid(@NonNull byte[] bArr, int i, int i2, int i3, @NonNull List<ParcelUuid> list) {
        while (i2 > 0) {
            list.add(BluetoothUuid.parseUuidFrom(extractBytes(bArr, i, i3)));
            i2 -= i3;
            i += i3;
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Arrays.equals(this.bytes, ((ScanRecord) obj).bytes);
    }

    public int getAdvertiseFlags() {
        return this.advertiseFlags;
    }

    @Nullable
    public byte[] getBytes() {
        return this.bytes;
    }

    @Nullable
    public String getDeviceName() {
        return this.deviceName;
    }

    @Nullable
    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.manufacturerSpecificData;
    }

    @Nullable
    public byte[] getManufacturerSpecificData(int i) {
        if (this.manufacturerSpecificData == null) {
            return null;
        }
        return this.manufacturerSpecificData.get(i);
    }

    @Nullable
    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.serviceData;
    }

    @Nullable
    public byte[] getServiceData(@NonNull ParcelUuid parcelUuid) {
        if (parcelUuid == null || this.serviceData == null) {
            return null;
        }
        return this.serviceData.get(parcelUuid);
    }

    @Nullable
    public List<ParcelUuid> getServiceUuids() {
        return this.serviceUuids;
    }

    public int getTxPowerLevel() {
        return this.txPowerLevel;
    }

    public String toString() {
        return "ScanRecord [advertiseFlags=" + this.advertiseFlags + ", serviceUuids=" + this.serviceUuids + ", manufacturerSpecificData=" + BluetoothLeUtils.toString(this.manufacturerSpecificData) + ", serviceData=" + BluetoothLeUtils.toString(this.serviceData) + ", txPowerLevel=" + this.txPowerLevel + ", deviceName=" + this.deviceName + "]";
    }
}
