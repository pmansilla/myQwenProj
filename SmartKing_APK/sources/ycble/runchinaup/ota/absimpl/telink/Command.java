package ycble.runchinaup.ota.absimpl.telink;

import java.util.UUID;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes2.dex */
class Command {
    public UUID characteristicUUID;
    public byte[] data;
    public int delay;
    public UUID descriptorUUID;
    public UUID serviceUUID;
    public Object tag;
    public CommandType type;

    /* loaded from: classes2.dex */
    public interface Callback {
        void error(Peripheral peripheral, Command command, String str);

        void success(Peripheral peripheral, Command command, Object obj);

        boolean timeout(Peripheral peripheral, Command command);
    }

    /* loaded from: classes2.dex */
    public enum CommandType {
        READ,
        READ_DESCRIPTOR,
        WRITE,
        WRITE_NO_RESPONSE,
        ENABLE_NOTIFY,
        DISABLE_NOTIFY
    }

    public Command() {
        this(null, null, CommandType.WRITE);
    }

    public Command(UUID uuid, UUID uuid2, CommandType commandType) {
        this(uuid, uuid2, commandType, null);
    }

    public Command(UUID uuid, UUID uuid2, CommandType commandType, byte[] bArr) {
        this(uuid, uuid2, commandType, bArr, null);
    }

    public Command(UUID uuid, UUID uuid2, CommandType commandType, byte[] bArr, Object obj) {
        this.serviceUUID = uuid;
        this.characteristicUUID = uuid2;
        this.type = commandType;
        this.data = bArr;
        this.tag = obj;
    }

    public static Command newInstance() {
        return new Command();
    }

    public void clear() {
        this.serviceUUID = null;
        this.characteristicUUID = null;
        this.descriptorUUID = null;
        this.data = null;
    }

    public String toString() {
        return "{ tag : " + this.tag + ", type : " + this.type + " CHARACTERISTIC_UUID :" + this.characteristicUUID.toString() + " data: " + (this.data != null ? BleUtil.byte2HexStr(this.data, ",") : "") + " delay :" + this.delay + "}";
    }
}
