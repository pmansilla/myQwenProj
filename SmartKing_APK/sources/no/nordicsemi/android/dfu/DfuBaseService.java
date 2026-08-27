package no.nordicsemi.android.dfu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Locale;
import no.nordicsemi.android.dfu.DfuProgressInfo;
import no.nordicsemi.android.dfu.internal.ArchiveInputStream;
import no.nordicsemi.android.dfu.internal.HexInputStream;

/* loaded from: classes2.dex */
public abstract class DfuBaseService extends IntentService implements DfuProgressInfo.ProgressListener {
    public static final int ACTION_ABORT = 2;
    public static final int ACTION_PAUSE = 0;
    public static final int ACTION_RESUME = 1;
    public static final String BROADCAST_ACTION = "no.nordicsemi.android.dfu.broadcast.BROADCAST_ACTION";
    public static final String BROADCAST_ERROR = "no.nordicsemi.android.dfu.broadcast.BROADCAST_ERROR";
    public static final String BROADCAST_LOG = "no.nordicsemi.android.dfu.broadcast.BROADCAST_LOG";
    public static final String BROADCAST_PROGRESS = "no.nordicsemi.android.dfu.broadcast.BROADCAST_PROGRESS";
    static boolean DEBUG = false;
    public static final int ERROR_BLUETOOTH_DISABLED = 4106;
    public static final int ERROR_CONNECTION_MASK = 16384;
    public static final int ERROR_CONNECTION_STATE_MASK = 32768;
    public static final int ERROR_CRC_ERROR = 4109;
    public static final int ERROR_DEVICE_DISCONNECTED = 4096;
    public static final int ERROR_DEVICE_NOT_BONDED = 4110;
    public static final int ERROR_FILE_ERROR = 4098;
    public static final int ERROR_FILE_INVALID = 4099;
    public static final int ERROR_FILE_IO_EXCEPTION = 4100;
    public static final int ERROR_FILE_NOT_FOUND = 4097;
    public static final int ERROR_FILE_SIZE_INVALID = 4108;
    public static final int ERROR_FILE_TYPE_UNSUPPORTED = 4105;
    public static final int ERROR_INIT_PACKET_REQUIRED = 4107;
    public static final int ERROR_INVALID_RESPONSE = 4104;
    public static final int ERROR_MASK = 4096;
    public static final int ERROR_REMOTE_MASK = 8192;
    public static final int ERROR_REMOTE_TYPE_LEGACY = 256;
    public static final int ERROR_REMOTE_TYPE_SECURE = 512;
    public static final int ERROR_REMOTE_TYPE_SECURE_BUTTONLESS = 2048;
    public static final int ERROR_REMOTE_TYPE_SECURE_EXTENDED = 1024;
    public static final int ERROR_SERVICE_DISCOVERY_NOT_STARTED = 4101;
    public static final int ERROR_SERVICE_NOT_FOUND = 4102;
    public static final int ERROR_TYPE_COMMUNICATION = 2;
    public static final int ERROR_TYPE_COMMUNICATION_STATE = 1;
    public static final int ERROR_TYPE_DFU_REMOTE = 3;
    public static final int ERROR_TYPE_OTHER = 0;
    public static final String EXTRA_ACTION = "no.nordicsemi.android.dfu.extra.EXTRA_ACTION";
    private static final String EXTRA_ATTEMPT = "no.nordicsemi.android.dfu.extra.EXTRA_ATTEMPT";
    public static final String EXTRA_AVG_SPEED_B_PER_MS = "no.nordicsemi.android.dfu.extra.EXTRA_AVG_SPEED_B_PER_MS";
    public static final String EXTRA_CUSTOM_UUIDS_FOR_BUTTONLESS_DFU_WITHOUT_BOND_SHARING = "no.nordicsemi.android.dfu.extra.EXTRA_CUSTOM_UUIDS_FOR_BUTTONLESS_DFU_WITHOUT_BOND_SHARING";
    public static final String EXTRA_CUSTOM_UUIDS_FOR_BUTTONLESS_DFU_WITH_BOND_SHARING = "no.nordicsemi.android.dfu.extra.EXTRA_CUSTOM_UUIDS_FOR_BUTTONLESS_DFU_WITH_BOND_SHARING";
    public static final String EXTRA_CUSTOM_UUIDS_FOR_EXPERIMENTAL_BUTTONLESS_DFU = "no.nordicsemi.android.dfu.extra.EXTRA_CUSTOM_UUIDS_FOR_EXPERIMENTAL_BUTTONLESS_DFU";
    public static final String EXTRA_CUSTOM_UUIDS_FOR_LEGACY_DFU = "no.nordicsemi.android.dfu.extra.EXTRA_CUSTOM_UUIDS_FOR_LEGACY_DFU";
    public static final String EXTRA_CUSTOM_UUIDS_FOR_SECURE_DFU = "no.nordicsemi.android.dfu.extra.EXTRA_CUSTOM_UUIDS_FOR_SECURE_DFU";
    public static final String EXTRA_DATA = "no.nordicsemi.android.dfu.extra.EXTRA_DATA";
    public static final String EXTRA_DEVICE_ADDRESS = "no.nordicsemi.android.dfu.extra.EXTRA_DEVICE_ADDRESS";
    public static final String EXTRA_DEVICE_NAME = "no.nordicsemi.android.dfu.extra.EXTRA_DEVICE_NAME";
    public static final String EXTRA_DISABLE_NOTIFICATION = "no.nordicsemi.android.dfu.extra.EXTRA_DISABLE_NOTIFICATION";
    public static final String EXTRA_ERROR_TYPE = "no.nordicsemi.android.dfu.extra.EXTRA_ERROR_TYPE";
    public static final String EXTRA_FILE_MIME_TYPE = "no.nordicsemi.android.dfu.extra.EXTRA_MIME_TYPE";
    public static final String EXTRA_FILE_PATH = "no.nordicsemi.android.dfu.extra.EXTRA_FILE_PATH";
    public static final String EXTRA_FILE_RES_ID = "no.nordicsemi.android.dfu.extra.EXTRA_FILE_RES_ID";
    public static final String EXTRA_FILE_TYPE = "no.nordicsemi.android.dfu.extra.EXTRA_FILE_TYPE";
    public static final String EXTRA_FILE_URI = "no.nordicsemi.android.dfu.extra.EXTRA_FILE_URI";
    public static final String EXTRA_FORCE_DFU = "no.nordicsemi.android.dfu.extra.EXTRA_FORCE_DFU";
    public static final String EXTRA_FOREGROUND_SERVICE = "no.nordicsemi.android.dfu.extra.EXTRA_FOREGROUND_SERVICE";
    public static final String EXTRA_INIT_FILE_PATH = "no.nordicsemi.android.dfu.extra.EXTRA_INIT_FILE_PATH";
    public static final String EXTRA_INIT_FILE_RES_ID = "no.nordicsemi.android.dfu.extra.EXTRA_INIT_FILE_RES_ID";
    public static final String EXTRA_INIT_FILE_URI = "no.nordicsemi.android.dfu.extra.EXTRA_INIT_FILE_URI";
    public static final String EXTRA_KEEP_BOND = "no.nordicsemi.android.dfu.extra.EXTRA_KEEP_BOND";
    public static final String EXTRA_LOG_LEVEL = "no.nordicsemi.android.dfu.extra.EXTRA_LOG_LEVEL";
    public static final String EXTRA_LOG_MESSAGE = "no.nordicsemi.android.dfu.extra.EXTRA_LOG_INFO";
    public static final String EXTRA_MTU = "no.nordicsemi.android.dfu.extra.EXTRA_MTU";
    public static final String EXTRA_PACKET_RECEIPT_NOTIFICATIONS_ENABLED = "no.nordicsemi.android.dfu.extra.EXTRA_PRN_ENABLED";
    public static final String EXTRA_PACKET_RECEIPT_NOTIFICATIONS_VALUE = "no.nordicsemi.android.dfu.extra.EXTRA_PRN_VALUE";
    public static final String EXTRA_PARTS_TOTAL = "no.nordicsemi.android.dfu.extra.EXTRA_PARTS_TOTAL";
    public static final String EXTRA_PART_CURRENT = "no.nordicsemi.android.dfu.extra.EXTRA_PART_CURRENT";
    public static final String EXTRA_PROGRESS = "no.nordicsemi.android.dfu.extra.EXTRA_PROGRESS";
    public static final String EXTRA_RESTORE_BOND = "no.nordicsemi.android.dfu.extra.EXTRA_RESTORE_BOND";
    public static final String EXTRA_SPEED_B_PER_MS = "no.nordicsemi.android.dfu.extra.EXTRA_SPEED_B_PER_MS";
    public static final String EXTRA_UNSAFE_EXPERIMENTAL_BUTTONLESS_DFU = "no.nordicsemi.android.dfu.extra.EXTRA_UNSAFE_EXPERIMENTAL_BUTTONLESS_DFU";
    public static final int LOG_LEVEL_APPLICATION = 10;
    public static final int LOG_LEVEL_DEBUG = 0;
    public static final int LOG_LEVEL_ERROR = 20;
    public static final int LOG_LEVEL_INFO = 5;
    public static final int LOG_LEVEL_VERBOSE = 1;
    public static final int LOG_LEVEL_WARNING = 15;
    public static final String MIME_TYPE_OCTET_STREAM = "application/octet-stream";
    public static final String MIME_TYPE_ZIP = "application/zip";
    public static final String NOTIFICATION_CHANNEL_DFU = "dfu";
    public static final int NOTIFICATION_ID = 283;
    public static final int PROGRESS_ABORTED = -7;
    public static final int PROGRESS_COMPLETED = -6;
    public static final int PROGRESS_CONNECTING = -1;
    public static final int PROGRESS_DISCONNECTING = -5;
    public static final int PROGRESS_ENABLING_DFU_MODE = -3;
    public static final int PROGRESS_STARTING = -2;
    public static final int PROGRESS_VALIDATING = -4;
    protected static final int STATE_CLOSED = -5;
    protected static final int STATE_CONNECTED = -2;
    protected static final int STATE_CONNECTED_AND_READY = -3;
    protected static final int STATE_CONNECTING = -1;
    protected static final int STATE_DISCONNECTED = 0;
    protected static final int STATE_DISCONNECTING = -4;
    private static final String TAG = "DfuBaseService";
    public static final int TYPE_APPLICATION = 4;
    public static final int TYPE_AUTO = 0;
    public static final int TYPE_BOOTLOADER = 2;
    public static final int TYPE_SOFT_DEVICE = 1;
    private boolean mAborted;
    private BluetoothAdapter mBluetoothAdapter;
    private final BroadcastReceiver mBondStateBroadcastReceiver;
    protected int mConnectionState;
    private final BroadcastReceiver mConnectionStateBroadcastReceiver;
    private String mDeviceAddress;
    private String mDeviceName;
    private final BroadcastReceiver mDfuActionReceiver;
    private DfuCallback mDfuServiceImpl;
    private boolean mDisableNotification;
    private int mError;
    private InputStream mFirmwareInputStream;
    private final BluetoothGattCallback mGattCallback;
    private InputStream mInitFileInputStream;
    private long mLastNotificationTime;
    private int mLastProgress;
    private final Object mLock;
    DfuProgressInfo mProgressInfo;

    public DfuBaseService() {
        super(TAG);
        this.mLock = new Object();
        this.mLastProgress = -1;
        this.mDfuActionReceiver = new BroadcastReceiver() { // from class: no.nordicsemi.android.dfu.DfuBaseService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                int intExtra = intent.getIntExtra(DfuBaseService.EXTRA_ACTION, 0);
                DfuBaseService.this.logi("User action received: " + intExtra);
                switch (intExtra) {
                    case 0:
                        DfuBaseService.this.sendLogBroadcast(15, "[Broadcast] Pause action received");
                        if (DfuBaseService.this.mDfuServiceImpl != null) {
                            DfuBaseService.this.mDfuServiceImpl.pause();
                            return;
                        }
                        return;
                    case 1:
                        DfuBaseService.this.sendLogBroadcast(15, "[Broadcast] Resume action received");
                        if (DfuBaseService.this.mDfuServiceImpl != null) {
                            DfuBaseService.this.mDfuServiceImpl.resume();
                            return;
                        }
                        return;
                    case 2:
                        DfuBaseService.this.sendLogBroadcast(15, "[Broadcast] Abort action received");
                        DfuBaseService.this.mAborted = true;
                        if (DfuBaseService.this.mDfuServiceImpl != null) {
                            DfuBaseService.this.mDfuServiceImpl.abort();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mBondStateBroadcastReceiver = new BroadcastReceiver() { // from class: no.nordicsemi.android.dfu.DfuBaseService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                int intExtra;
                if (!((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress().equals(DfuBaseService.this.mDeviceAddress) || (intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", -1)) == 11 || DfuBaseService.this.mDfuServiceImpl == null) {
                    return;
                }
                DfuBaseService.this.mDfuServiceImpl.onBondStateChanged(intExtra);
            }
        };
        this.mConnectionStateBroadcastReceiver = new BroadcastReceiver() { // from class: no.nordicsemi.android.dfu.DfuBaseService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress().equals(DfuBaseService.this.mDeviceAddress)) {
                    String action = intent.getAction();
                    DfuBaseService.this.logi("Action received: " + action);
                    DfuBaseService.this.sendLogBroadcast(0, "[Broadcast] Action received: " + action);
                }
            }
        };
        this.mGattCallback = new BluetoothGattCallback() { // from class: no.nordicsemi.android.dfu.DfuBaseService.4
            @Override // android.bluetooth.BluetoothGattCallback
            public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                if (DfuBaseService.this.mDfuServiceImpl != null) {
                    DfuBaseService.this.mDfuServiceImpl.getGattCallback().onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
                if (DfuBaseService.this.mDfuServiceImpl != null) {
                    DfuBaseService.this.mDfuServiceImpl.getGattCallback().onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic, i);
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
                if (DfuBaseService.this.mDfuServiceImpl != null) {
                    DfuBaseService.this.mDfuServiceImpl.getGattCallback().onCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic, i);
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
                if (i != 0) {
                    if (i == 8 || i == 19) {
                        DfuBaseService.this.logw("Target device disconnected with status: " + i);
                    } else {
                        DfuBaseService.this.loge("Connection state change error: " + i + " newState: " + i2);
                    }
                    if (i2 == 0) {
                        DfuBaseService.this.mConnectionState = 0;
                        if (DfuBaseService.this.mDfuServiceImpl != null) {
                            DfuBaseService.this.mDfuServiceImpl.getGattCallback().onDisconnected();
                        }
                    }
                    DfuBaseService.this.mError = i | 32768;
                } else if (i2 == 2) {
                    DfuBaseService.this.logi("Connected to GATT server");
                    DfuBaseService.this.sendLogBroadcast(5, "Connected to " + DfuBaseService.this.mDeviceAddress);
                    DfuBaseService.this.mConnectionState = -2;
                    if (bluetoothGatt.getDevice().getBondState() == 12) {
                        DfuBaseService.this.logi("Waiting 1600 ms for a possible Service Changed indication...");
                        DfuBaseService.this.waitFor(1600);
                    }
                    DfuBaseService.this.sendLogBroadcast(1, "Discovering services...");
                    DfuBaseService.this.sendLogBroadcast(0, "gatt.discoverServices()");
                    boolean discoverServices = bluetoothGatt.discoverServices();
                    DfuBaseService dfuBaseService = DfuBaseService.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Attempting to start service discovery... ");
                    sb.append(discoverServices ? "succeed" : "failed");
                    dfuBaseService.logi(sb.toString());
                    if (discoverServices) {
                        return;
                    } else {
                        DfuBaseService.this.mError = DfuBaseService.ERROR_SERVICE_DISCOVERY_NOT_STARTED;
                    }
                } else if (i2 == 0) {
                    DfuBaseService.this.logi("Disconnected from GATT server");
                    DfuBaseService.this.mConnectionState = 0;
                    if (DfuBaseService.this.mDfuServiceImpl != null) {
                        DfuBaseService.this.mDfuServiceImpl.getGattCallback().onDisconnected();
                    }
                }
                synchronized (DfuBaseService.this.mLock) {
                    DfuBaseService.this.mLock.notifyAll();
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
                if (DfuBaseService.this.mDfuServiceImpl != null) {
                    DfuBaseService.this.mDfuServiceImpl.getGattCallback().onDescriptorRead(bluetoothGatt, bluetoothGattDescriptor, i);
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
                if (DfuBaseService.this.mDfuServiceImpl != null) {
                    DfuBaseService.this.mDfuServiceImpl.getGattCallback().onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor, i);
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            @SuppressLint({"NewApi"})
            public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
                if (DfuBaseService.this.mDfuServiceImpl != null) {
                    DfuBaseService.this.mDfuServiceImpl.getGattCallback().onMtuChanged(bluetoothGatt, i, i2);
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            @SuppressLint({"NewApi"})
            public void onPhyUpdate(BluetoothGatt bluetoothGatt, int i, int i2, int i3) {
                if (DfuBaseService.this.mDfuServiceImpl != null) {
                    DfuBaseService.this.mDfuServiceImpl.getGattCallback().onPhyUpdate(bluetoothGatt, i, i2, i3);
                }
            }

            @Override // android.bluetooth.BluetoothGattCallback
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
                if (i == 0) {
                    DfuBaseService.this.logi("Services discovered");
                    DfuBaseService.this.mConnectionState = -3;
                } else {
                    DfuBaseService.this.loge("Service discovery error: " + i);
                    DfuBaseService.this.mError = i | 16384;
                }
                synchronized (DfuBaseService.this.mLock) {
                    DfuBaseService.this.mLock.notifyAll();
                }
            }
        };
    }

    private boolean initialize() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService("bluetooth");
        if (bluetoothManager == null) {
            loge("Unable to initialize BluetoothManager.");
            return false;
        }
        this.mBluetoothAdapter = bluetoothManager.getAdapter();
        if (this.mBluetoothAdapter != null) {
            return true;
        }
        loge("Unable to obtain a BluetoothAdapter.");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Log.e(TAG, str);
    }

    private void loge(String str, Throwable th) {
        Log.e(TAG, str, th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String str) {
        if (DEBUG) {
            Log.i(TAG, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logw(String str) {
        if (DEBUG) {
            Log.w(TAG, str);
        }
    }

    private static IntentFilter makeDfuActionIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        return intentFilter;
    }

    private InputStream openInputStream(int i, String str, int i2, int i3) throws IOException {
        InputStream openRawResource = getResources().openRawResource(i);
        if (MIME_TYPE_ZIP.equals(str)) {
            return new ArchiveInputStream(openRawResource, i2, i3);
        }
        openRawResource.mark(2);
        int read = openRawResource.read();
        openRawResource.reset();
        return read == 58 ? new HexInputStream(openRawResource, i2) : openRawResource;
    }

    private InputStream openInputStream(Uri uri, String str, int i, int i2) throws IOException {
        InputStream openInputStream = getContentResolver().openInputStream(uri);
        if (MIME_TYPE_ZIP.equals(str)) {
            return new ArchiveInputStream(openInputStream, i, i2);
        }
        Cursor query = getContentResolver().query(uri, new String[]{"_display_name"}, null, null, null);
        try {
            return (query.moveToNext() && query.getString(0).toLowerCase(Locale.US).endsWith("hex")) ? new HexInputStream(openInputStream, i) : openInputStream;
        } finally {
            query.close();
        }
    }

    private InputStream openInputStream(String str, String str2, int i, int i2) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(str);
        return MIME_TYPE_ZIP.equals(str2) ? new ArchiveInputStream(fileInputStream, i, i2) : str.toLowerCase(Locale.US).endsWith("hex") ? new HexInputStream(fileInputStream, i) : fileInputStream;
    }

    private void report(int i) {
        sendErrorBroadcast(i);
        if (this.mDisableNotification) {
            return;
        }
        String str = this.mDeviceAddress;
        String string = this.mDeviceName != null ? this.mDeviceName : getString(R.string.dfu_unknown_name);
        NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_DFU).setSmallIcon(android.R.drawable.stat_sys_upload).setOnlyAlertOnce(true).setColor(SupportMenu.CATEGORY_MASK).setOngoing(false).setContentTitle(getString(R.string.dfu_status_error)).setSmallIcon(android.R.drawable.stat_sys_upload_done).setContentText(getString(R.string.dfu_status_error_msg)).setAutoCancel(true);
        Intent intent = new Intent(this, getNotificationTarget());
        intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
        intent.putExtra(EXTRA_DEVICE_ADDRESS, str);
        intent.putExtra(EXTRA_DEVICE_NAME, string);
        intent.putExtra(EXTRA_PROGRESS, i);
        autoCancel.setContentIntent(PendingIntent.getActivity(this, 0, intent, AMapEngineUtils.HALF_MAX_P20_WIDTH));
        updateErrorNotification(autoCancel);
        ((NotificationManager) getSystemService("notification")).notify(NOTIFICATION_ID, autoCancel.build());
    }

    private void sendErrorBroadcast(int i) {
        Intent intent = new Intent(BROADCAST_ERROR);
        if ((i & 16384) > 0) {
            intent.putExtra(EXTRA_DATA, i & (-16385));
            intent.putExtra(EXTRA_ERROR_TYPE, 2);
        } else if ((32768 & i) > 0) {
            intent.putExtra(EXTRA_DATA, i & (-32769));
            intent.putExtra(EXTRA_ERROR_TYPE, 1);
        } else if ((i & 8192) > 0) {
            intent.putExtra(EXTRA_DATA, i & (-8193));
            intent.putExtra(EXTRA_ERROR_TYPE, 3);
        } else {
            intent.putExtra(EXTRA_DATA, i);
            intent.putExtra(EXTRA_ERROR_TYPE, 0);
        }
        intent.putExtra(EXTRA_DEVICE_ADDRESS, this.mDeviceAddress);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendProgressBroadcast(DfuProgressInfo dfuProgressInfo) {
        Intent intent = new Intent(BROADCAST_PROGRESS);
        intent.putExtra(EXTRA_DATA, dfuProgressInfo.getProgress());
        intent.putExtra(EXTRA_DEVICE_ADDRESS, this.mDeviceAddress);
        intent.putExtra(EXTRA_PART_CURRENT, dfuProgressInfo.getCurrentPart());
        intent.putExtra(EXTRA_PARTS_TOTAL, dfuProgressInfo.getTotalParts());
        intent.putExtra(EXTRA_SPEED_B_PER_MS, dfuProgressInfo.getSpeed());
        intent.putExtra(EXTRA_AVG_SPEED_B_PER_MS, dfuProgressInfo.getAverageSpeed());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void startForeground() {
        NotificationCompat.Builder ongoing = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_DFU).setSmallIcon(android.R.drawable.stat_sys_upload).setContentTitle(getString(R.string.dfu_status_foreground_title)).setContentText(getString(R.string.dfu_status_foreground_content)).setColor(-7829368).setPriority(-1).setOngoing(true);
        Class<? extends Activity> notificationTarget = getNotificationTarget();
        if (notificationTarget != null) {
            Intent intent = new Intent(this, notificationTarget);
            intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.putExtra(EXTRA_DEVICE_ADDRESS, this.mDeviceAddress);
            intent.putExtra(EXTRA_DEVICE_NAME, this.mDeviceName);
            ongoing.setContentIntent(PendingIntent.getActivity(this, 0, intent, AMapEngineUtils.HALF_MAX_P20_WIDTH));
        } else {
            logw("getNotificationTarget() should not return null if the service is to be started as a foreground service");
        }
        updateForegroundNotification(ongoing);
        startForeground(NOTIFICATION_ID, ongoing.build());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void close(BluetoothGatt bluetoothGatt) {
        logi("Cleaning up...");
        sendLogBroadcast(0, "gatt.close()");
        bluetoothGatt.close();
        this.mConnectionState = -5;
    }

    protected BluetoothGatt connect(String str) {
        if (!this.mBluetoothAdapter.isEnabled()) {
            return null;
        }
        this.mConnectionState = -1;
        logi("Connecting to the device...");
        BluetoothDevice remoteDevice = this.mBluetoothAdapter.getRemoteDevice(str);
        sendLogBroadcast(0, "gatt = device.connectGatt(autoConnect = false)");
        BluetoothGatt connectGatt = remoteDevice.connectGatt(this, false, this.mGattCallback);
        try {
            synchronized (this.mLock) {
                while (true) {
                    if ((this.mConnectionState == -1 || this.mConnectionState == -2) && this.mError == 0) {
                        this.mLock.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
        return connectGatt;
    }

    protected void disconnect(BluetoothGatt bluetoothGatt) {
        if (this.mConnectionState == 0) {
            return;
        }
        sendLogBroadcast(1, "Disconnecting...");
        this.mProgressInfo.setProgress(-5);
        this.mConnectionState = -4;
        logi("Disconnecting from the device...");
        sendLogBroadcast(0, "gatt.disconnect()");
        bluetoothGatt.disconnect();
        waitUntilDisconnected();
        sendLogBroadcast(5, "Disconnected");
    }

    protected abstract Class<? extends Activity> getNotificationTarget();

    protected boolean isDebug() {
        return false;
    }

    @Override // android.app.IntentService, android.app.Service
    public void onCreate() {
        super.onCreate();
        DEBUG = isDebug();
        logi("DFU service created. Version: 1.7.0");
        initialize();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter makeDfuActionIntentFilter = makeDfuActionIntentFilter();
        localBroadcastManager.registerReceiver(this.mDfuActionReceiver, makeDfuActionIntentFilter);
        registerReceiver(this.mDfuActionReceiver, makeDfuActionIntentFilter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        registerReceiver(this.mConnectionStateBroadcastReceiver, intentFilter);
        registerReceiver(this.mBondStateBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
    }

    @Override // android.app.IntentService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mDfuActionReceiver);
        unregisterReceiver(this.mDfuActionReceiver);
        unregisterReceiver(this.mConnectionStateBroadcastReceiver);
        unregisterReceiver(this.mBondStateBroadcastReceiver);
        try {
            if (this.mFirmwareInputStream != null) {
                this.mFirmwareInputStream.close();
            }
            if (this.mInitFileInputStream != null) {
                this.mInitFileInputStream.close();
            }
        } catch (IOException unused) {
        } catch (Throwable th) {
            this.mFirmwareInputStream = null;
            this.mInitFileInputStream = null;
            throw th;
        }
        this.mFirmwareInputStream = null;
        this.mInitFileInputStream = null;
        logi("DFU service destroyed");
    }

    /* JADX WARN: Code restructure failed: missing block: B:169:0x046d, code lost:
    
        if (r4 != null) goto L226;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0450, code lost:
    
        r4.release();
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x044e, code lost:
    
        if (r4 != null) goto L226;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:162:0x048f  */
    /* JADX WARN: Removed duplicated region for block: B:164:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0402 A[Catch: all -> 0x0470, TryCatch #8 {all -> 0x0470, blocks: (B:191:0x039b, B:173:0x03fa, B:175:0x0402, B:176:0x0440, B:178:0x0421, B:168:0x0457), top: B:146:0x038c }] */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0421 A[Catch: all -> 0x0470, TryCatch #8 {all -> 0x0470, blocks: (B:191:0x039b, B:173:0x03fa, B:175:0x0402, B:176:0x0440, B:178:0x0421, B:168:0x0457), top: B:146:0x038c }] */
    /* JADX WARN: Removed duplicated region for block: B:183:0x048a A[Catch: all -> 0x0532, TRY_ENTER, TRY_LEAVE, TryCatch #12 {all -> 0x0532, blocks: (B:47:0x0101, B:228:0x010e, B:230:0x0116, B:232:0x012d, B:234:0x014c, B:53:0x0176, B:55:0x017e, B:57:0x0183, B:58:0x018c, B:60:0x0190, B:63:0x0199, B:64:0x01a0, B:65:0x01a1, B:67:0x01a5, B:70:0x01ae, B:71:0x01b5, B:72:0x01b6, B:74:0x01ba, B:77:0x01c3, B:78:0x01ca, B:81:0x01ce, B:83:0x01d4, B:85:0x01f5, B:88:0x0203, B:89:0x0209, B:91:0x0215, B:96:0x022c, B:98:0x0240, B:103:0x0257, B:105:0x025b, B:107:0x0262, B:108:0x028f, B:113:0x0283, B:114:0x029a, B:116:0x02a4, B:118:0x02a9, B:119:0x0315, B:121:0x0320, B:123:0x032b, B:124:0x032e, B:129:0x0353, B:134:0x02df, B:136:0x0361, B:138:0x0365, B:143:0x0380, B:193:0x03b0, B:159:0x03d1, B:187:0x0495, B:188:0x0498, B:170:0x0450, B:183:0x048a, B:222:0x01e0, B:224:0x01e6, B:225:0x0188, B:237:0x0156, B:238:0x015d, B:240:0x0139, B:242:0x0141, B:245:0x011d, B:247:0x0124, B:279:0x0499, B:251:0x04c3, B:272:0x04ed, B:265:0x0504, B:258:0x051b), top: B:46:0x0101, inners: #16, #19, #21, #18 }] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0495 A[Catch: all -> 0x0532, TRY_ENTER, TryCatch #12 {all -> 0x0532, blocks: (B:47:0x0101, B:228:0x010e, B:230:0x0116, B:232:0x012d, B:234:0x014c, B:53:0x0176, B:55:0x017e, B:57:0x0183, B:58:0x018c, B:60:0x0190, B:63:0x0199, B:64:0x01a0, B:65:0x01a1, B:67:0x01a5, B:70:0x01ae, B:71:0x01b5, B:72:0x01b6, B:74:0x01ba, B:77:0x01c3, B:78:0x01ca, B:81:0x01ce, B:83:0x01d4, B:85:0x01f5, B:88:0x0203, B:89:0x0209, B:91:0x0215, B:96:0x022c, B:98:0x0240, B:103:0x0257, B:105:0x025b, B:107:0x0262, B:108:0x028f, B:113:0x0283, B:114:0x029a, B:116:0x02a4, B:118:0x02a9, B:119:0x0315, B:121:0x0320, B:123:0x032b, B:124:0x032e, B:129:0x0353, B:134:0x02df, B:136:0x0361, B:138:0x0365, B:143:0x0380, B:193:0x03b0, B:159:0x03d1, B:187:0x0495, B:188:0x0498, B:170:0x0450, B:183:0x048a, B:222:0x01e0, B:224:0x01e6, B:225:0x0188, B:237:0x0156, B:238:0x015d, B:240:0x0139, B:242:0x0141, B:245:0x011d, B:247:0x0124, B:279:0x0499, B:251:0x04c3, B:272:0x04ed, B:265:0x0504, B:258:0x051b), top: B:46:0x0101, inners: #16, #19, #21, #18 }] */
    /* JADX WARN: Removed duplicated region for block: B:189:? A[Catch: all -> 0x0532, SYNTHETIC, TryCatch #12 {all -> 0x0532, blocks: (B:47:0x0101, B:228:0x010e, B:230:0x0116, B:232:0x012d, B:234:0x014c, B:53:0x0176, B:55:0x017e, B:57:0x0183, B:58:0x018c, B:60:0x0190, B:63:0x0199, B:64:0x01a0, B:65:0x01a1, B:67:0x01a5, B:70:0x01ae, B:71:0x01b5, B:72:0x01b6, B:74:0x01ba, B:77:0x01c3, B:78:0x01ca, B:81:0x01ce, B:83:0x01d4, B:85:0x01f5, B:88:0x0203, B:89:0x0209, B:91:0x0215, B:96:0x022c, B:98:0x0240, B:103:0x0257, B:105:0x025b, B:107:0x0262, B:108:0x028f, B:113:0x0283, B:114:0x029a, B:116:0x02a4, B:118:0x02a9, B:119:0x0315, B:121:0x0320, B:123:0x032b, B:124:0x032e, B:129:0x0353, B:134:0x02df, B:136:0x0361, B:138:0x0365, B:143:0x0380, B:193:0x03b0, B:159:0x03d1, B:187:0x0495, B:188:0x0498, B:170:0x0450, B:183:0x048a, B:222:0x01e0, B:224:0x01e6, B:225:0x0188, B:237:0x0156, B:238:0x015d, B:240:0x0139, B:242:0x0141, B:245:0x011d, B:247:0x0124, B:279:0x0499, B:251:0x04c3, B:272:0x04ed, B:265:0x0504, B:258:0x051b), top: B:46:0x0101, inners: #16, #19, #21, #18 }] */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0154  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0156 A[Catch: Exception -> 0x015e, IOException -> 0x0162, SizeValidationException -> 0x0166, FileNotFoundException -> 0x016a, SecurityException -> 0x016e, all -> 0x0532, TryCatch #16 {IOException -> 0x0162, blocks: (B:228:0x010e, B:230:0x0116, B:232:0x012d, B:234:0x014c, B:53:0x0176, B:55:0x017e, B:57:0x0183, B:58:0x018c, B:60:0x0190, B:63:0x0199, B:64:0x01a0, B:65:0x01a1, B:67:0x01a5, B:70:0x01ae, B:71:0x01b5, B:72:0x01b6, B:74:0x01ba, B:77:0x01c3, B:78:0x01ca, B:81:0x01ce, B:83:0x01d4, B:85:0x01f5, B:222:0x01e0, B:224:0x01e6, B:225:0x0188, B:237:0x0156, B:238:0x015d, B:240:0x0139, B:242:0x0141, B:245:0x011d, B:247:0x0124), top: B:227:0x010e, outer: #12 }] */
    /* JADX WARN: Type inference failed for: r20v0, types: [android.content.Context, no.nordicsemi.android.dfu.DfuProgressInfo$ProgressListener, no.nordicsemi.android.dfu.DfuBaseService] */
    /* JADX WARN: Type inference failed for: r2v116 */
    /* JADX WARN: Type inference failed for: r2v48 */
    /* JADX WARN: Type inference failed for: r2v49 */
    /* JADX WARN: Type inference failed for: r2v54, types: [no.nordicsemi.android.dfu.DfuService] */
    /* JADX WARN: Type inference failed for: r2v60 */
    /* JADX WARN: Type inference failed for: r2v61 */
    /* JADX WARN: Type inference failed for: r2v62 */
    /* JADX WARN: Type inference failed for: r2v70, types: [boolean] */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v15 */
    /* JADX WARN: Type inference failed for: r4v16 */
    /* JADX WARN: Type inference failed for: r4v22, types: [no.nordicsemi.android.dfu.DfuCallback, no.nordicsemi.android.dfu.DfuService] */
    /* JADX WARN: Type inference failed for: r4v9, types: [int] */
    @Override // android.app.IntentService
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onHandleIntent(android.content.Intent r21) {
        /*
            Method dump skipped, instructions count: 1356
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: no.nordicsemi.android.dfu.DfuBaseService.onHandleIntent(android.content.Intent):void");
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        ((NotificationManager) getSystemService("notification")).cancel(NOTIFICATION_ID);
        stopSelf();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void refreshDeviceCache(BluetoothGatt bluetoothGatt, boolean z) {
        if (z || bluetoothGatt.getDevice().getBondState() == 10) {
            sendLogBroadcast(0, "gatt.refresh() (hidden)");
            try {
                Method method = bluetoothGatt.getClass().getMethod("refresh", new Class[0]);
                if (method != null) {
                    logi("Refreshing result: " + ((Boolean) method.invoke(bluetoothGatt, new Object[0])).booleanValue());
                }
            } catch (Exception e) {
                loge("An exception occurred while refreshing device", e);
                sendLogBroadcast(15, "Refreshing failed");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendLogBroadcast(int i, String str) {
        Intent intent = new Intent(BROADCAST_LOG);
        intent.putExtra(EXTRA_LOG_MESSAGE, "[DFU] " + str);
        intent.putExtra(EXTRA_LOG_LEVEL, i);
        intent.putExtra(EXTRA_DEVICE_ADDRESS, this.mDeviceAddress);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void terminateConnection(BluetoothGatt bluetoothGatt, int i) {
        if (this.mConnectionState != 0) {
            disconnect(bluetoothGatt);
        }
        refreshDeviceCache(bluetoothGatt, false);
        close(bluetoothGatt);
        waitFor(600);
        if (i != 0) {
            report(i);
        }
    }

    protected void updateErrorNotification(NotificationCompat.Builder builder) {
    }

    protected void updateForegroundNotification(NotificationCompat.Builder builder) {
    }

    @Override // no.nordicsemi.android.dfu.DfuProgressInfo.ProgressListener
    public void updateProgressNotification() {
        DfuProgressInfo dfuProgressInfo = this.mProgressInfo;
        int progress = dfuProgressInfo.getProgress();
        if (this.mLastProgress == progress) {
            return;
        }
        this.mLastProgress = progress;
        sendProgressBroadcast(dfuProgressInfo);
        if (this.mDisableNotification) {
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime - this.mLastNotificationTime >= 250 || -6 == progress || -7 == progress) {
            this.mLastNotificationTime = elapsedRealtime;
            String str = this.mDeviceAddress;
            String string = this.mDeviceName != null ? this.mDeviceName : getString(R.string.dfu_unknown_name);
            NotificationCompat.Builder onlyAlertOnce = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_DFU).setSmallIcon(android.R.drawable.stat_sys_upload).setOnlyAlertOnce(true);
            onlyAlertOnce.setColor(-7829368);
            switch (progress) {
                case -7:
                    onlyAlertOnce.setOngoing(false).setContentTitle(getString(R.string.dfu_status_aborted)).setSmallIcon(android.R.drawable.stat_sys_upload_done).setContentText(getString(R.string.dfu_status_aborted_msg)).setAutoCancel(true);
                    break;
                case -6:
                    onlyAlertOnce.setOngoing(false).setContentTitle(getString(R.string.dfu_status_completed)).setSmallIcon(android.R.drawable.stat_sys_upload_done).setContentText(getString(R.string.dfu_status_completed_msg)).setAutoCancel(true).setColor(-16730086);
                    break;
                case -5:
                    onlyAlertOnce.setOngoing(true).setContentTitle(getString(R.string.dfu_status_disconnecting)).setContentText(getString(R.string.dfu_status_disconnecting_msg, new Object[]{string})).setProgress(100, 0, true);
                    break;
                case -4:
                    onlyAlertOnce.setOngoing(true).setContentTitle(getString(R.string.dfu_status_validating)).setContentText(getString(R.string.dfu_status_validating_msg)).setProgress(100, 0, true);
                    break;
                case -3:
                    onlyAlertOnce.setOngoing(true).setContentTitle(getString(R.string.dfu_status_switching_to_dfu)).setContentText(getString(R.string.dfu_status_switching_to_dfu_msg)).setProgress(100, 0, true);
                    break;
                case -2:
                    onlyAlertOnce.setOngoing(true).setContentTitle(getString(R.string.dfu_status_starting)).setContentText(getString(R.string.dfu_status_starting_msg)).setProgress(100, 0, true);
                    break;
                case -1:
                    onlyAlertOnce.setOngoing(true).setContentTitle(getString(R.string.dfu_status_connecting)).setContentText(getString(R.string.dfu_status_connecting_msg, new Object[]{string})).setProgress(100, 0, true);
                    break;
                default:
                    onlyAlertOnce.setOngoing(true).setContentTitle(dfuProgressInfo.getTotalParts() == 1 ? getString(R.string.dfu_status_uploading) : getString(R.string.dfu_status_uploading_part, new Object[]{Integer.valueOf(dfuProgressInfo.getCurrentPart()), Integer.valueOf(dfuProgressInfo.getTotalParts())})).setContentText(getString(R.string.dfu_status_uploading_msg, new Object[]{string})).setProgress(100, progress, false);
                    break;
            }
            Intent intent = new Intent(this, getNotificationTarget());
            intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.putExtra(EXTRA_DEVICE_ADDRESS, str);
            intent.putExtra(EXTRA_DEVICE_NAME, string);
            intent.putExtra(EXTRA_PROGRESS, progress);
            onlyAlertOnce.setContentIntent(PendingIntent.getActivity(this, 0, intent, AMapEngineUtils.HALF_MAX_P20_WIDTH));
            updateProgressNotification(onlyAlertOnce, progress);
            ((NotificationManager) getSystemService("notification")).notify(NOTIFICATION_ID, onlyAlertOnce.build());
        }
    }

    protected void updateProgressNotification(NotificationCompat.Builder builder, int i) {
        if (i == -7 || i == -6) {
            return;
        }
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(EXTRA_ACTION, 2);
        builder.addAction(R.drawable.ic_action_notify_cancel, getString(R.string.dfu_action_abort), PendingIntent.getBroadcast(this, 1, intent, AMapEngineUtils.HALF_MAX_P20_WIDTH));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void waitFor(int i) {
        synchronized (this.mLock) {
            try {
                sendLogBroadcast(0, "wait(" + i + SQLBuilder.PARENTHESES_RIGHT);
                this.mLock.wait((long) i);
            } catch (InterruptedException e) {
                loge("Sleeping interrupted", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void waitUntilDisconnected() {
        try {
            synchronized (this.mLock) {
                while (this.mConnectionState != 0 && this.mError == 0) {
                    this.mLock.wait();
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
    }
}
