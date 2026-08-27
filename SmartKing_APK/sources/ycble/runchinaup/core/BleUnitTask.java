package ycble.runchinaup.core;

import java.util.UUID;

/* loaded from: classes2.dex */
public class BleUnitTask {
    public static final int TYPE_DISABLE_NOTIFY_OR_INDICATE = 6;
    public static final int TYPE_ENABLE_INDICATE = 5;
    public static final int TYPE_ENABLE_NOTIFY = 4;
    public static final int TYPE_READ = 1;
    public static final int TYPE_WRITE = 2;
    public static final int TYPE_WRITE_WITHOUT_RESP = 3;
    private UUID U_chara;
    private UUID U_service;
    private byte[] data;
    public String msg;
    private int optionType;

    private BleUnitTask(UUID uuid, UUID uuid2) {
        this.U_service = uuid;
        this.U_chara = uuid2;
    }

    public static BleUnitTask createDisEnableNotifyOrIndicate(UUID uuid, UUID uuid2, String... strArr) {
        BleUnitTask bleUnitTask = new BleUnitTask(uuid, uuid2);
        bleUnitTask.optionType = 6;
        if (strArr == null || strArr.length <= 0) {
            bleUnitTask.msg = String.format("%s{ %s>>>>%s", "notify", uuid.toString(), uuid2.toString());
        } else {
            bleUnitTask.msg = strArr[0];
        }
        return bleUnitTask;
    }

    public static BleUnitTask createEnableIndicate(UUID uuid, UUID uuid2, String... strArr) {
        BleUnitTask bleUnitTask = new BleUnitTask(uuid, uuid2);
        bleUnitTask.optionType = 5;
        if (strArr == null || strArr.length <= 0) {
            bleUnitTask.msg = String.format("%s{ %s>>>>%s", "notify", uuid.toString(), uuid2.toString());
        } else {
            bleUnitTask.msg = strArr[0];
        }
        return bleUnitTask;
    }

    public static BleUnitTask createEnableNotify(UUID uuid, UUID uuid2, String... strArr) {
        BleUnitTask bleUnitTask = new BleUnitTask(uuid, uuid2);
        bleUnitTask.optionType = 4;
        if (strArr == null || strArr.length <= 0) {
            bleUnitTask.msg = String.format("%s{ %s>>>>%s", "notify", uuid.toString(), uuid2.toString());
        } else {
            bleUnitTask.msg = strArr[0];
        }
        return bleUnitTask;
    }

    public static BleUnitTask createRead(UUID uuid, UUID uuid2, String... strArr) {
        BleUnitTask bleUnitTask = new BleUnitTask(uuid, uuid2);
        bleUnitTask.optionType = 1;
        if (strArr == null || strArr.length <= 0) {
            bleUnitTask.msg = String.format("read {%s>>>>%s", uuid.toString(), uuid2.toString());
        } else {
            bleUnitTask.msg = strArr[0];
        }
        return bleUnitTask;
    }

    public static BleUnitTask createWrite(UUID uuid, UUID uuid2, byte[] bArr, String... strArr) {
        BleUnitTask bleUnitTask = new BleUnitTask(uuid, uuid2);
        bleUnitTask.optionType = 2;
        if (strArr == null || strArr.length <= 0) {
            bleUnitTask.msg = String.format("write { %s>>>>%s", uuid.toString(), uuid2.toString());
        } else {
            bleUnitTask.msg = strArr[0];
        }
        bleUnitTask.data = bArr;
        return bleUnitTask;
    }

    public static BleUnitTask createWriteWithOutResp(UUID uuid, UUID uuid2, byte[] bArr, String... strArr) {
        BleUnitTask bleUnitTask = new BleUnitTask(uuid, uuid2);
        bleUnitTask.optionType = 3;
        if (strArr == null || strArr.length <= 0) {
            bleUnitTask.msg = String.format("write withoutResponse{ %s>>>>%s", uuid.toString(), uuid2.toString());
        } else {
            bleUnitTask.msg = strArr[0];
        }
        bleUnitTask.data = bArr;
        return bleUnitTask;
    }

    public byte[] getData() {
        if (this.data == null) {
            if (getOptionType() == 4) {
                return new byte[]{1, 0};
            }
            if (getOptionType() == 5) {
                return new byte[]{2, 0};
            }
            if (getOptionType() == 6) {
                return new byte[]{0, 0};
            }
            this.data = new byte[1];
        }
        return this.data;
    }

    public int getOptionType() {
        return this.optionType;
    }

    public UUID getU_chara() {
        return this.U_chara;
    }

    public UUID getU_service() {
        return this.U_service;
    }

    public void setData(byte[] bArr) {
        this.data = bArr;
    }
}
