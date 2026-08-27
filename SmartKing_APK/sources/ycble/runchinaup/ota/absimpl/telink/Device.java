package ycble.runchinaup.ota.absimpl.telink;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import java.util.List;
import java.util.UUID;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.ota.absimpl.telink.Command;
import ycble.runchinaup.util.BleUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class Device extends Peripheral {
    public static final int OTA_END = 65282;
    public static final int OTA_START = 65281;
    public static final int OTA_START_REQ = 65283;
    public static final int OTA_START_RSP = 65284;
    public static final int STATE_FAILURE = 0;
    public static final int STATE_PROGRESS = 2;
    public static final int STATE_SUCCESS = 1;
    public static final String TAG = "Device";
    private static final int TAG_OTA_ENABLE_NOTIFICATION = 9;
    private static final int TAG_OTA_END = 8;
    private static final int TAG_OTA_LAST = 3;
    private static final int TAG_OTA_LAST_READ = 10;
    private static final int TAG_OTA_PRE_READ = 4;
    private static final int TAG_OTA_READ = 2;
    private static final int TAG_OTA_START = 7;
    private static final int TAG_OTA_START_REQ = 5;
    private static final int TAG_OTA_START_RSP = 6;
    private static final int TAG_OTA_WRITE = 1;
    private long delay;
    private boolean isReadSupport;
    private Callback mCallback;
    private final OtaCommandCallback mOtaCallback;
    private final OtaPacketParser mOtaParser;
    public static final UUID SERVICE_UUID = UUID.fromString("00010203-0405-0607-0809-0a0b0c0d1912");
    public static final UUID CHARACTERISTIC_UUID = UUID.fromString("00010203-0405-0607-0809-0a0b0c0d2b12");

    /* loaded from: classes2.dex */
    public interface Callback {
        void onConnected(Device device);

        void onDisconnected(Device device);

        void onOtaStateChanged(Device device, int i);

        void onServicesDiscovered(Device device);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class OtaCommandCallback implements Command.Callback {
        private OtaCommandCallback() {
        }

        @Override // ycble.runchinaup.ota.absimpl.telink.Command.Callback
        public void error(Peripheral peripheral, Command command, String str) {
            ycBleLog.d("error packet : " + command.tag + " errorMsg : " + str);
            if (!command.tag.equals(8)) {
                Device.this.resetOta();
                Device.this.onOtaFailure();
            } else {
                Device.this.resetOta();
                Device.this.setOtaProgressChanged();
                Device.this.onOtaSuccess();
            }
        }

        @Override // ycble.runchinaup.ota.absimpl.telink.Command.Callback
        public void success(Peripheral peripheral, Command command, Object obj) {
            if (command.tag.equals(4)) {
                ycBleLog.d("read =========> " + BleUtil.byte2HexStr((byte[]) obj, "-"));
                return;
            }
            if (command.tag.equals(7)) {
                Device.this.sendNextOtaPacketCommand(0);
                return;
            }
            if (command.tag.equals(8)) {
                Device.this.resetOta();
                Device.this.setOtaProgressChanged();
                Device.this.onOtaSuccess();
                return;
            }
            if (command.tag.equals(3)) {
                Device.this.sendOtaEndCommand();
                return;
            }
            if (command.tag.equals(1)) {
                if (!Device.this.validateOta()) {
                    Device.this.sendNextOtaPacketCommand(0);
                } else if (!Device.this.isReadSupport) {
                    Device.this.sendNextOtaPacketCommand((int) Device.this.delay);
                }
                Device.this.setOtaProgressChanged();
                return;
            }
            if (command.tag.equals(2)) {
                Device.this.sendNextOtaPacketCommand(0);
            } else if (command.tag.equals(10)) {
                Device.this.sendOTALastCommand();
            }
        }

        @Override // ycble.runchinaup.ota.absimpl.telink.Command.Callback
        public boolean timeout(Peripheral peripheral, Command command) {
            ycBleLog.d("timeout : " + BleUtil.byte2HexStr(command.data, ":"));
            if (!command.tag.equals(8)) {
                Device.this.resetOta();
                Device.this.onOtaFailure();
                return false;
            }
            Device.this.resetOta();
            Device.this.setOtaProgressChanged();
            Device.this.onOtaSuccess();
            return false;
        }
    }

    public Device(BluetoothDevice bluetoothDevice, byte[] bArr, int i) {
        super(bluetoothDevice, bArr, i);
        this.mOtaParser = new OtaPacketParser();
        this.mOtaCallback = new OtaCommandCallback();
        this.isReadSupport = true;
        this.delay = 20L;
    }

    public Device(BleDevice bleDevice) {
        super(bleDevice);
        this.mOtaParser = new OtaPacketParser();
        this.mOtaCallback = new OtaCommandCallback();
        this.isReadSupport = true;
        this.delay = 20L;
    }

    private void enableOtaNotification() {
        Command newInstance = Command.newInstance();
        newInstance.serviceUUID = SERVICE_UUID;
        newInstance.characteristicUUID = CHARACTERISTIC_UUID;
        newInstance.type = Command.CommandType.ENABLE_NOTIFY;
        newInstance.tag = 9;
        sendCommand(this.mOtaCallback, newInstance);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetOta() {
        this.mDelayHandler.removeCallbacksAndMessages(null);
        this.mOtaParser.clear();
    }

    private void sendLastReadCommand() {
        Command newInstance = Command.newInstance();
        newInstance.serviceUUID = SERVICE_UUID;
        newInstance.characteristicUUID = CHARACTERISTIC_UUID;
        newInstance.type = Command.CommandType.READ;
        newInstance.tag = 10;
        sendCommand(this.mOtaCallback, newInstance);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendNextOtaPacketCommand(int i) {
        if (!this.mOtaParser.hasNextPacket()) {
            sendLastReadCommand();
            return;
        }
        Command newInstance = Command.newInstance();
        newInstance.serviceUUID = SERVICE_UUID;
        newInstance.characteristicUUID = CHARACTERISTIC_UUID;
        newInstance.type = Command.CommandType.WRITE_NO_RESPONSE;
        newInstance.data = this.mOtaParser.getNextPacket();
        newInstance.tag = 1;
        newInstance.delay = i;
        sendCommand(this.mOtaCallback, newInstance);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOTALastCommand() {
        Command newInstance = Command.newInstance();
        newInstance.serviceUUID = SERVICE_UUID;
        newInstance.characteristicUUID = CHARACTERISTIC_UUID;
        newInstance.type = Command.CommandType.WRITE_NO_RESPONSE;
        newInstance.tag = 3;
        newInstance.delay = 0;
        int curIndex = this.mOtaParser.getCurIndex();
        int i = (curIndex >> 8) & 255;
        newInstance.data = new byte[]{2, -1, (byte) (curIndex & 255), (byte) i, (byte) ((255 - curIndex) & 255), (byte) (255 - i)};
        sendCommand(this.mOtaCallback, newInstance);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOtaEndCommand() {
        Command newInstance = Command.newInstance();
        newInstance.serviceUUID = SERVICE_UUID;
        newInstance.characteristicUUID = CHARACTERISTIC_UUID;
        newInstance.type = Command.CommandType.WRITE_NO_RESPONSE;
        newInstance.tag = 8;
        newInstance.data = new byte[]{2, -1};
        sendCommand(this.mOtaCallback, newInstance);
    }

    private void sendOtaStartCommand() {
        Command newInstance = Command.newInstance();
        newInstance.serviceUUID = SERVICE_UUID;
        newInstance.characteristicUUID = CHARACTERISTIC_UUID;
        newInstance.type = Command.CommandType.WRITE_NO_RESPONSE;
        newInstance.tag = 7;
        newInstance.data = new byte[]{1, -1};
        sendCommand(this.mOtaCallback, newInstance);
    }

    private void sendOtaStartReqCommand() {
        Command newInstance = Command.newInstance();
        newInstance.serviceUUID = SERVICE_UUID;
        newInstance.characteristicUUID = CHARACTERISTIC_UUID;
        newInstance.type = Command.CommandType.WRITE_NO_RESPONSE;
        newInstance.tag = 5;
        newInstance.data = new byte[]{3, -1};
        sendCommand(this.mOtaCallback, newInstance);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOtaProgressChanged() {
        if (this.mOtaParser.invalidateProgress()) {
            onOtaProgress();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateOta() {
        int nextPacketIndex = this.mOtaParser.getNextPacketIndex() * 16;
        if (nextPacketIndex <= 0 || nextPacketIndex % 64 != 0) {
            return false;
        }
        if (!this.isReadSupport) {
            return true;
        }
        Command newInstance = Command.newInstance();
        newInstance.serviceUUID = SERVICE_UUID;
        newInstance.characteristicUUID = CHARACTERISTIC_UUID;
        newInstance.type = Command.CommandType.READ;
        newInstance.tag = 2;
        sendCommand(this.mOtaCallback, newInstance);
        return true;
    }

    @Override // ycble.runchinaup.ota.absimpl.telink.Peripheral
    public void connect(Context context) {
        super.connect(context);
        this.isReadSupport = SharedPreferencesHelper.getReadSupport(context);
        this.delay = SharedPreferencesHelper.getPktDelay(context);
    }

    public int getOtaProgress() {
        return this.mOtaParser.getProgress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ycble.runchinaup.ota.absimpl.telink.Peripheral
    public void onConnect() {
        super.onConnect();
        if (this.mCallback != null) {
            this.mCallback.onConnected(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ycble.runchinaup.ota.absimpl.telink.Peripheral
    public void onDisconnect() {
        super.onDisconnect();
        resetOta();
        if (this.mCallback != null) {
            this.mCallback.onDisconnected(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ycble.runchinaup.ota.absimpl.telink.Peripheral
    public void onNotify(byte[] bArr, UUID uuid, UUID uuid2, Object obj) {
        super.onNotify(bArr, uuid, uuid2, obj);
        ycBleLog.e(" onNotify ==> " + BleUtil.byte2HexStr(bArr, ":"));
    }

    protected void onOtaFailure() {
        if (this.mCallback != null) {
            this.mCallback.onOtaStateChanged(this, 0);
        }
    }

    protected void onOtaProgress() {
        if (this.mCallback != null) {
            this.mCallback.onOtaStateChanged(this, 2);
        }
    }

    protected void onOtaSuccess() {
        if (this.mCallback != null) {
            this.mCallback.onOtaStateChanged(this, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ycble.runchinaup.ota.absimpl.telink.Peripheral
    public void onServicesDiscovered(List<BluetoothGattService> list) {
        super.onServicesDiscovered(list);
        if (this.mCallback != null) {
            this.mCallback.onServicesDiscovered(this);
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void startOta(byte[] bArr) {
        ycBleLog.d("Start OTA");
        resetOta();
        this.mOtaParser.set(bArr);
        sendOtaStartCommand();
    }
}
