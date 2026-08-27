package ycble.runchinaup.ota.absimpl.telink;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.ota.absimpl.telink.Command;
import ycble.runchinaup.util.BleUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class Peripheral extends BluetoothGattCallback {
    public static final int CONNECTION_PRIORITY_BALANCED = 0;
    public static final int CONNECTION_PRIORITY_HIGH = 1;
    public static final int CONNECTION_PRIORITY_LOW_POWER = 2;
    private static final int CONN_STATE_CLOSED = 16;
    private static final int CONN_STATE_CONNECTED = 4;
    private static final int CONN_STATE_CONNECTING = 2;
    private static final int CONN_STATE_DISCONNECTING = 8;
    private static final int CONN_STATE_IDLE = 1;
    private static final int RSSI_UPDATE_TIME_INTERVAL = 2000;
    protected BluetoothDevice device;
    protected BluetoothGatt gatt;
    protected final Runnable mCommandDelayRunnable;
    protected final Runnable mCommandTimeoutRunnable;
    protected final Runnable mRssiUpdateRunnable;
    protected List<BluetoothGattService> mServices;
    protected String mac;
    protected byte[] macBytes;
    protected boolean monitorRssi;
    protected String name;
    protected int rssi;
    protected byte[] scanRecord;
    protected int type;
    protected final Queue<CommandContext> mInputCommandQueue = new ConcurrentLinkedQueue();
    protected final Queue<CommandContext> mOutputCommandQueue = new ConcurrentLinkedQueue();
    protected final Map<String, CommandContext> mNotificationCallbacks = new ConcurrentHashMap();
    protected final Handler mTimeoutHandler = new Handler(Looper.getMainLooper());
    protected final Handler mRssiUpdateHandler = new Handler(Looper.getMainLooper());
    protected final Handler mDelayHandler = new Handler(Looper.getMainLooper());
    private final Object mStateLock = new Object();
    private final Object mProcessLock = new Object();
    protected Boolean processing = false;
    protected int updateIntervalMill = 5000;
    protected int commandTimeoutMill = 10000;
    private int mConnState = 1;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class CommandContext {
        public Command.Callback callback;
        public Command command;

        public CommandContext(Command.Callback callback, Command command) {
            this.callback = callback;
            this.command = command;
        }

        public void clear() {
            this.command = null;
            this.callback = null;
        }
    }

    /* loaded from: classes2.dex */
    private final class CommandDelayRunnable implements Runnable {
        private CommandDelayRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (Peripheral.this.mOutputCommandQueue) {
                Peripheral.this.processCommand(Peripheral.this.mOutputCommandQueue.peek());
            }
        }
    }

    /* loaded from: classes2.dex */
    private final class CommandTimeoutRunnable implements Runnable {
        private CommandTimeoutRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (Peripheral.this.mOutputCommandQueue) {
                CommandContext peek = Peripheral.this.mOutputCommandQueue.peek();
                if (peek != null) {
                    Command command = peek.command;
                    Command.Callback callback = peek.callback;
                    if (Peripheral.this.commandTimeout(peek)) {
                        peek.command = command;
                        peek.callback = callback;
                        Peripheral.this.processCommand(peek);
                    } else {
                        Peripheral.this.mOutputCommandQueue.poll();
                        Peripheral.this.commandCompleted();
                    }
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    private final class RssiUpdateRunnable implements Runnable {
        private RssiUpdateRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Peripheral.this.monitorRssi && Peripheral.this.isConnected()) {
                if (Peripheral.this.gatt != null) {
                    Peripheral.this.gatt.readRemoteRssi();
                }
                Peripheral.this.mRssiUpdateHandler.postDelayed(Peripheral.this.mRssiUpdateRunnable, Peripheral.this.updateIntervalMill);
            }
        }
    }

    public Peripheral(BluetoothDevice bluetoothDevice, byte[] bArr, int i) {
        this.mRssiUpdateRunnable = new RssiUpdateRunnable();
        this.mCommandTimeoutRunnable = new CommandTimeoutRunnable();
        this.mCommandDelayRunnable = new CommandDelayRunnable();
        this.device = bluetoothDevice;
        this.scanRecord = bArr;
        this.rssi = i;
        this.name = bluetoothDevice.getName();
        this.mac = bluetoothDevice.getAddress();
        this.type = bluetoothDevice.getType();
    }

    public Peripheral(BleDevice bleDevice) {
        this.mRssiUpdateRunnable = new RssiUpdateRunnable();
        this.mCommandTimeoutRunnable = new CommandTimeoutRunnable();
        this.mCommandDelayRunnable = new CommandDelayRunnable();
        this.scanRecord = bleDevice.getScanBytes();
        this.rssi = bleDevice.getRssi();
        this.name = bleDevice.getName();
        this.mac = bleDevice.getMac();
        this.device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bleDevice.getMac());
        this.type = this.device.getType();
    }

    private void cancelCommandTimeoutTask() {
        this.mTimeoutHandler.removeCallbacksAndMessages(null);
    }

    private void clear() {
        this.processing = false;
        stopMonitoringRssi();
        cancelCommandTimeoutTask();
        this.mInputCommandQueue.clear();
        this.mOutputCommandQueue.clear();
        this.mNotificationCallbacks.clear();
        this.mDelayHandler.removeCallbacksAndMessages(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void commandCompleted() {
        synchronized (this.mProcessLock) {
            if (this.processing.booleanValue()) {
                this.processing = false;
            }
        }
        processCommand();
    }

    private void commandError(String str) {
        commandError(this.mOutputCommandQueue.poll(), str);
    }

    private void commandError(CommandContext commandContext, String str) {
        ycBleLog.d("commandError");
        if (commandContext != null) {
            Command command = commandContext.command;
            Command.Callback callback = commandContext.callback;
            commandContext.clear();
            if (callback != null) {
                callback.error(this, command, str);
            }
        }
    }

    private void commandSuccess(Object obj) {
        commandSuccess(this.mOutputCommandQueue.poll(), obj);
    }

    private void commandSuccess(CommandContext commandContext, Object obj) {
        if (commandContext != null) {
            Command command = commandContext.command;
            Command.Callback callback = commandContext.callback;
            commandContext.clear();
            if (callback != null) {
                callback.success(this, command, obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean commandTimeout(CommandContext commandContext) {
        ycBleLog.d("commandTimeout");
        if (commandContext == null) {
            return false;
        }
        Command command = commandContext.command;
        Command.Callback callback = commandContext.callback;
        commandContext.clear();
        if (callback != null) {
            return callback.timeout(this, command);
        }
        return false;
    }

    private void disableNotification(CommandContext commandContext, UUID uuid, UUID uuid2) {
        String str = "";
        BluetoothGattService service = this.gatt.getService(uuid);
        boolean z = false;
        if (service != null) {
            BluetoothGattCharacteristic findNotifyCharacteristic = findNotifyCharacteristic(service, uuid2);
            if (findNotifyCharacteristic != null) {
                this.mNotificationCallbacks.remove(generateHashKey(uuid, findNotifyCharacteristic));
                if (this.gatt.setCharacteristicNotification(findNotifyCharacteristic, false)) {
                    z = true;
                } else {
                    str = "disable notification error";
                }
            } else {
                str = "no characteristic";
            }
        } else {
            str = "service is not offered by the remote device";
        }
        if (!z) {
            commandError(commandContext, str);
        }
        commandCompleted();
    }

    private void enableNotification(CommandContext commandContext, UUID uuid, UUID uuid2) {
        String str = "";
        BluetoothGattService service = this.gatt.getService(uuid);
        boolean z = false;
        if (service != null) {
            BluetoothGattCharacteristic findNotifyCharacteristic = findNotifyCharacteristic(service, uuid2);
            if (findNotifyCharacteristic == null) {
                str = "no characteristic";
            } else if (this.gatt.setCharacteristicNotification(findNotifyCharacteristic, true)) {
                this.mNotificationCallbacks.put(generateHashKey(uuid, findNotifyCharacteristic), commandContext);
                z = true;
            } else {
                str = "enable notification error";
            }
        } else {
            str = "service is not offered by the remote device";
        }
        if (!z) {
            commandError(commandContext, str);
        }
        commandCompleted();
    }

    private BluetoothGattCharacteristic findNotifyCharacteristic(BluetoothGattService bluetoothGattService, UUID uuid) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic;
        List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
        Iterator<BluetoothGattCharacteristic> it = characteristics.iterator();
        while (true) {
            if (!it.hasNext()) {
                bluetoothGattCharacteristic = null;
                break;
            }
            bluetoothGattCharacteristic = it.next();
            if ((bluetoothGattCharacteristic.getProperties() & 16) != 0 && uuid.equals(bluetoothGattCharacteristic.getUuid())) {
                break;
            }
        }
        if (bluetoothGattCharacteristic != null) {
            return bluetoothGattCharacteristic;
        }
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic2 : characteristics) {
            if ((bluetoothGattCharacteristic2.getProperties() & 32) != 0 && uuid.equals(bluetoothGattCharacteristic2.getUuid())) {
                return bluetoothGattCharacteristic2;
            }
        }
        return bluetoothGattCharacteristic;
    }

    private BluetoothGattCharacteristic findWritableCharacteristic(BluetoothGattService bluetoothGattService, UUID uuid, int i) {
        int i2 = i == 1 ? 4 : 8;
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
            if ((bluetoothGattCharacteristic.getProperties() & i2) != 0 && uuid.equals(bluetoothGattCharacteristic.getUuid())) {
                return bluetoothGattCharacteristic;
            }
        }
        return null;
    }

    private String generateHashKey(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return generateHashKey(bluetoothGattCharacteristic.getService().getUuid(), bluetoothGattCharacteristic);
    }

    private String generateHashKey(UUID uuid, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return String.valueOf(uuid) + "|" + bluetoothGattCharacteristic.getUuid() + "|" + bluetoothGattCharacteristic.getInstanceId();
    }

    private void postCommand(CommandContext commandContext) {
        this.mInputCommandQueue.add(commandContext);
        synchronized (this.mProcessLock) {
            if (!this.processing.booleanValue()) {
                processCommand();
            }
        }
    }

    private void postCommandTimeoutTask() {
        if (this.commandTimeoutMill <= 0) {
            return;
        }
        this.mTimeoutHandler.removeCallbacksAndMessages(null);
        this.mTimeoutHandler.postDelayed(this.mCommandTimeoutRunnable, this.commandTimeoutMill);
    }

    private void processCommand() {
        synchronized (this.mInputCommandQueue) {
            if (this.mInputCommandQueue.isEmpty()) {
                return;
            }
            CommandContext poll = this.mInputCommandQueue.poll();
            if (poll == null) {
                return;
            }
            Command.CommandType commandType = poll.command.type;
            if (commandType != Command.CommandType.ENABLE_NOTIFY && commandType != Command.CommandType.DISABLE_NOTIFY) {
                this.mOutputCommandQueue.add(poll);
                synchronized (this.mProcessLock) {
                    if (!this.processing.booleanValue()) {
                        this.processing = true;
                    }
                }
            }
            int i = poll.command.delay;
            if (i > 0) {
                this.mDelayHandler.postDelayed(this.mCommandDelayRunnable, i);
            } else {
                processCommand(poll);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processCommand(CommandContext commandContext) {
        Command command = commandContext.command;
        switch (command.type) {
            case READ:
                postCommandTimeoutTask();
                readCharacteristic(commandContext, command.serviceUUID, command.characteristicUUID);
                break;
            case WRITE:
                postCommandTimeoutTask();
                writeCharacteristic(commandContext, command.serviceUUID, command.characteristicUUID, 2, command.data);
                break;
            case WRITE_NO_RESPONSE:
                postCommandTimeoutTask();
                writeCharacteristic(commandContext, command.serviceUUID, command.characteristicUUID, 1, command.data);
                break;
            case ENABLE_NOTIFY:
                enableNotification(commandContext, command.serviceUUID, command.characteristicUUID);
                break;
            case DISABLE_NOTIFY:
                disableNotification(commandContext, command.serviceUUID, command.characteristicUUID);
                break;
        }
    }

    private void readCharacteristic(CommandContext commandContext, UUID uuid, UUID uuid2) {
        String str = "";
        BluetoothGattService service = this.gatt.getService(uuid);
        boolean z = false;
        if (service != null) {
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(uuid2);
            if (characteristic == null) {
                str = "read characteristic error";
            } else if (this.gatt.readCharacteristic(characteristic)) {
                z = true;
            } else {
                str = "read characteristic error";
            }
        } else {
            str = "service is not offered by the remote device";
        }
        if (z) {
            return;
        }
        commandError(str);
        commandCompleted();
    }

    private void writeCharacteristic(CommandContext commandContext, UUID uuid, UUID uuid2, int i, byte[] bArr) {
        String str = "";
        BluetoothGattService service = this.gatt.getService(uuid);
        boolean z = false;
        if (service != null) {
            BluetoothGattCharacteristic findWritableCharacteristic = findWritableCharacteristic(service, uuid2, i);
            if (findWritableCharacteristic != null) {
                findWritableCharacteristic.setValue(bArr);
                findWritableCharacteristic.setWriteType(i);
                if (this.gatt.writeCharacteristic(findWritableCharacteristic)) {
                    z = true;
                } else {
                    str = "write characteristic error";
                }
            } else {
                str = "no characteristic";
            }
        } else {
            str = "service is not offered by the remote device";
        }
        if (z) {
            return;
        }
        commandError(str);
        commandCompleted();
    }

    public void connect(Context context) {
        synchronized (this.mStateLock) {
            if (this.mConnState == 1) {
                ycBleLog.d("connect " + getDeviceName() + " -- " + getMacAddress());
                this.mConnState = 2;
                this.gatt = this.device.connectGatt(context, false, this);
                if (this.gatt == null) {
                    disconnect();
                    this.mConnState = 1;
                    onDisconnect();
                }
            }
        }
    }

    public void disconnect() {
        synchronized (this.mStateLock) {
            if (this.mConnState == 2 || this.mConnState == 4) {
                ycBleLog.d("disconnect " + getDeviceName() + " -- " + getMacAddress());
                clear();
                synchronized (this.mStateLock) {
                    if (this.gatt == null) {
                        this.mConnState = 1;
                    } else if (this.mConnState == 4) {
                        this.gatt.disconnect();
                        this.mConnState = 8;
                    } else {
                        this.gatt.disconnect();
                        this.gatt.close();
                        this.mConnState = 16;
                    }
                }
            }
        }
    }

    protected void enableMonitorRssi(boolean z) {
        if (z) {
            this.mRssiUpdateHandler.removeCallbacks(this.mRssiUpdateRunnable);
            this.mRssiUpdateHandler.postDelayed(this.mRssiUpdateRunnable, this.updateIntervalMill);
        } else {
            this.mRssiUpdateHandler.removeCallbacks(this.mRssiUpdateRunnable);
            this.mRssiUpdateHandler.removeCallbacksAndMessages(null);
        }
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public String getDeviceName() {
        return this.name;
    }

    public String getMacAddress() {
        return this.mac;
    }

    public byte[] getMacBytes() {
        if (this.macBytes == null) {
            String[] split = getMacAddress().split(":");
            int length = split.length;
            this.macBytes = new byte[length];
            for (int i = 0; i < length; i++) {
                this.macBytes[i] = (byte) (Integer.parseInt(split[i], 16) & 255);
            }
            BleUtil.reverse(this.macBytes, 0, length - 1);
        }
        return this.macBytes;
    }

    public int getRssi() {
        return this.rssi;
    }

    public List<BluetoothGattService> getServices() {
        return this.mServices;
    }

    public int getType() {
        return this.type;
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.mStateLock) {
            z = this.mConnState == 4;
        }
        return z;
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
        CommandContext commandContext = this.mNotificationCallbacks.get(generateHashKey(bluetoothGattCharacteristic));
        if (commandContext != null) {
            onNotify(bluetoothGattCharacteristic.getValue(), commandContext.command.serviceUUID, commandContext.command.characteristicUUID, commandContext.command.tag);
        }
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        super.onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic, i);
        cancelCommandTimeoutTask();
        if (i == 0) {
            commandSuccess(bluetoothGattCharacteristic.getValue());
        } else {
            commandError("read characteristic failed");
        }
        commandCompleted();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        super.onCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic, i);
        cancelCommandTimeoutTask();
        if (i == 0) {
            commandSuccess(null);
        } else {
            commandError("write characteristic fail");
        }
        commandCompleted();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onConnect() {
        enableMonitorRssi(this.monitorRssi);
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
        ycBleLog.d("onConnectionStateChange  status :" + i + " state : " + i2);
        if (i2 != 2) {
            synchronized (this.mStateLock) {
                ycBleLog.d("Close");
                if (this.gatt != null) {
                    this.gatt.close();
                    this.mConnState = 16;
                }
                clear();
                this.mConnState = 1;
                onDisconnect();
            }
            return;
        }
        synchronized (this.mStateLock) {
            this.mConnState = 4;
        }
        if (this.gatt != null && this.gatt.discoverServices()) {
            onConnect();
            return;
        }
        ycBleLog.d("remote service discovery has been stopped status = " + i2);
        disconnect();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
        super.onDescriptorRead(bluetoothGatt, bluetoothGattDescriptor, i);
        cancelCommandTimeoutTask();
        if (i == 0) {
            commandSuccess(bluetoothGattDescriptor.getValue());
        } else {
            commandError("read description failed");
        }
        commandCompleted();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
        super.onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor, i);
        cancelCommandTimeoutTask();
        if (i == 0) {
            commandSuccess(null);
        } else {
            commandError("write description failed");
        }
        commandCompleted();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDisconnect() {
        enableMonitorRssi(false);
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
        super.onMtuChanged(bluetoothGatt, i, i2);
        ycBleLog.d("mtu changed : " + i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onNotify(byte[] bArr, UUID uuid, UUID uuid2, Object obj) {
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onReadRemoteRssi(BluetoothGatt bluetoothGatt, int i, int i2) {
        super.onReadRemoteRssi(bluetoothGatt, i, i2);
        if (i2 != 0 || i == this.rssi) {
            return;
        }
        this.rssi = i;
        onRssiChanged();
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onReliableWriteCompleted(BluetoothGatt bluetoothGatt, int i) {
        super.onReliableWriteCompleted(bluetoothGatt, i);
    }

    protected void onRssiChanged() {
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
        super.onServicesDiscovered(bluetoothGatt, i);
        if (i != 0) {
            ycBleLog.d("Service discovery failed");
            disconnect();
        } else {
            List<BluetoothGattService> services = bluetoothGatt.getServices();
            this.mServices = services;
            onServicesDiscovered(services);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServicesDiscovered(List<BluetoothGattService> list) {
    }

    public final boolean requestConnectionPriority(int i) {
        return Build.VERSION.SDK_INT >= 21 && this.gatt.requestConnectionPriority(i);
    }

    public boolean sendCommand(Command.Callback callback, Command command) {
        synchronized (this.mStateLock) {
            if (this.mConnState != 4) {
                return false;
            }
            postCommand(new CommandContext(callback, command));
            return true;
        }
    }

    public final void startMonitoringRssi(int i) {
        this.monitorRssi = true;
        if (i <= 0) {
            this.updateIntervalMill = RSSI_UPDATE_TIME_INTERVAL;
        } else {
            this.updateIntervalMill = i;
        }
    }

    public final void stopMonitoringRssi() {
        this.monitorRssi = false;
        this.mRssiUpdateHandler.removeCallbacks(this.mRssiUpdateRunnable);
        this.mRssiUpdateHandler.removeCallbacksAndMessages(null);
    }
}
