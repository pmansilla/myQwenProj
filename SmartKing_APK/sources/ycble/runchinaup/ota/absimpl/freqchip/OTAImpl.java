package ycble.runchinaup.ota.absimpl.freqchip;

import android.os.Handler;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import ycble.runchinaup.core.AbsBleManager;
import ycble.runchinaup.core.BleUnitTask;
import ycble.runchinaup.exception.BleUUIDNullException;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.ota.callback.OTACallback;
import ycble.runchinaup.util.BleUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class OTAImpl extends AbsBleManager implements BleCfg {
    private InputStream input;
    private long leng;
    private OTACallback otaCallback;
    private int recv_data;
    private int writePrecent;
    private String filePath = null;
    private FileInputStream isfile = null;
    private int firstaddr = 0;
    private byte[] baseaddr = null;
    private int sencondaddr = 81920;
    private boolean writeStatus = false;
    private MyHandler myHandler = new MyHandler();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class MyHandler extends Handler {
        private MyHandler() {
        }

        @Override // android.os.Handler
        public void dispatchMessage(Message message) {
            switch (message.what) {
                case 0:
                    if (OTAImpl.this.otaCallback != null) {
                        OTAImpl.this.otaCallback.onSuccess();
                        return;
                    }
                    return;
                case 1:
                    if (OTAImpl.this.otaCallback != null) {
                        OTAImpl.this.otaCallback.onProgress(OTAImpl.this.writePrecent);
                        return;
                    }
                    return;
                case 2:
                    if (OTAImpl.this.otaCallback != null) {
                        OTAImpl.this.otaCallback.onFailure(1, "disconnected");
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public OTAImpl() {
        init(UUID_OTA_SEND_DATA);
    }

    private void hanWithTask() {
        taskSuccess(5);
    }

    private int page_erase(int i, long j) throws BleUUIDNullException {
        long j2 = j / PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
        if (j % PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM != 0) {
            j2++;
        }
        int i2 = i;
        for (int i3 = 0; i3 < j2; i3++) {
            send_data(3, i2, null, 0);
            do {
            } while (getRecv_data() != 1);
            setRecv_data(0);
            i2 += 4096;
        }
        return 0;
    }

    private boolean send_data(int i, int i2, byte[] bArr, int i3) throws BleUUIDNullException {
        byte[] bArr2 = new byte[1];
        byte[] cmd_operation = FreqchipUtils.cmd_operation(i, i3, i2);
        if (i == 1 || i == 3) {
            bArr2 = cmd_operation;
        } else if (i == 9) {
            bArr2[0] = (byte) (i & 255);
        } else {
            bArr2 = FreqchipUtils.byteMerger(cmd_operation, bArr);
        }
        return writeDataWithoutResp(UUID_OTA_SERVICE, UUID_OTA_SEND_DATA, bArr2);
    }

    private boolean verifyFile() {
        byte[] bArr = new byte[4];
        File file = new File(this.filePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                fileInputStream.skip(359L);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                new BufferedInputStream(fileInputStream).read(bArr, 0, 4);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
        }
        System.out.println("buffer: " + ((int) bArr[0]) + SQLBuilder.BLANK + ((int) bArr[1]));
        if (bArr[0] != 82 || bArr[1] != 81 || bArr[2] != 81 || bArr[3] != 82) {
            if (this.otaCallback != null) {
                this.otaCallback.onFailure(40, "文件校验不通过");
            }
            return false;
        }
        if (file.length() >= 100) {
            return true;
        }
        if (this.otaCallback != null) {
            this.otaCallback.onFailure(40, "文件校验不通过");
        }
        return false;
    }

    boolean checkDisconnect() {
        if (isConn()) {
            return false;
        }
        this.myHandler.sendEmptyMessage(2);
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x009e, code lost:
    
        if (ycble.runchinaup.ota.absimpl.freqchip.FreqchipUtils.bytetoint(r10.baseaddr) == (r4 - r6)) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00a4, code lost:
    
        if (checkDisconnect() == false) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00a6, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00a7, code lost:
    
        send_data(9, 0, null, 0);
        r10.myHandler.sendEmptyMessage(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void doSendFileByBluetooth(java.lang.String r11) throws java.io.FileNotFoundException, ycble.runchinaup.exception.BleUUIDNullException {
        /*
            r10 = this;
            r0 = 0
            boolean r1 = r11.equals(r0)
            if (r1 != 0) goto Lb7
            r1 = 235(0xeb, float:3.3E-43)
            r2 = 300(0x12c, float:4.2E-43)
            byte[] r3 = new byte[r1]
            java.io.File r4 = new java.io.File
            r4.<init>(r11)
            java.io.FileInputStream r11 = new java.io.FileInputStream
            r11.<init>(r4)
            r10.isfile = r11
            long r4 = r4.length()
            r10.leng = r4
            long r4 = r10.leng
            long r6 = (long) r2
            long r4 = r4 / r6
            long r4 = r10.leng
            long r4 = r4 % r6
            java.io.BufferedInputStream r11 = new java.io.BufferedInputStream
            java.io.FileInputStream r2 = r10.isfile
            r11.<init>(r2)
            r10.input = r11
            r11 = 0
            r10.setRecv_data(r11)
            r2 = 1
            r10.send_data(r2, r11, r0, r11)
        L37:
            int r4 = r10.getRecv_data()
            if (r4 == r2) goto L44
            boolean r4 = r10.checkDisconnect()
            if (r4 == 0) goto L37
            return
        L44:
            byte[] r4 = r10.baseaddr
            int r4 = ycble.runchinaup.ota.absimpl.freqchip.FreqchipUtils.bytetoint(r4)
            int r5 = r10.firstaddr
            if (r4 != r5) goto L51
            int r4 = r10.sencondaddr
            goto L53
        L51:
            int r4 = r10.firstaddr
        L53:
            r10.setRecv_data(r11)
            long r5 = r10.leng
            r10.page_erase(r4, r5)
            r5 = 0
            r6 = 0
        L5d:
            java.io.InputStream r7 = r10.input     // Catch: java.io.IOException -> Lb2
            int r7 = r7.read(r3, r11, r1)     // Catch: java.io.IOException -> Lb2
            r8 = -1
            if (r7 == r8) goto L96
            r6 = 5
            r10.send_data(r6, r4, r3, r7)     // Catch: java.io.IOException -> Lb2
            int r4 = r4 + r7
            int r5 = r5 + r7
            float r6 = (float) r5     // Catch: java.io.IOException -> Lb2
            long r8 = r10.leng     // Catch: java.io.IOException -> Lb2
            float r8 = (float) r8     // Catch: java.io.IOException -> Lb2
            float r6 = r6 / r8
            r8 = 1120403456(0x42c80000, float:100.0)
            float r6 = r6 * r8
            int r6 = (int) r6     // Catch: java.io.IOException -> Lb2
            r10.writePrecent = r6     // Catch: java.io.IOException -> Lb2
            ycble.runchinaup.ota.absimpl.freqchip.OTAImpl$MyHandler r6 = r10.myHandler     // Catch: java.io.IOException -> Lb2
            r6.sendEmptyMessage(r2)     // Catch: java.io.IOException -> Lb2
        L7d:
            boolean r6 = r10.writeStatus     // Catch: java.io.IOException -> Lb2
            if (r6 != 0) goto L82
            goto L7d
        L82:
            r10.writeStatus = r11     // Catch: java.io.IOException -> Lb2
        L84:
            int r6 = r10.getRecv_data()     // Catch: java.io.IOException -> Lb2
            if (r6 == r2) goto L91
            boolean r6 = r10.checkDisconnect()     // Catch: java.io.IOException -> Lb2
            if (r6 == 0) goto L84
            return
        L91:
            r10.setRecv_data(r11)     // Catch: java.io.IOException -> Lb2
            r6 = r7
            goto L5d
        L96:
            byte[] r1 = r10.baseaddr     // Catch: java.io.IOException -> Lb2
            int r1 = ycble.runchinaup.ota.absimpl.freqchip.FreqchipUtils.bytetoint(r1)     // Catch: java.io.IOException -> Lb2
            int r2 = r4 - r6
            if (r1 == r2) goto La7
            boolean r1 = r10.checkDisconnect()     // Catch: java.io.IOException -> Lb2
            if (r1 == 0) goto L96
            return
        La7:
            r1 = 9
            r10.send_data(r1, r11, r0, r11)     // Catch: java.io.IOException -> Lb2
            ycble.runchinaup.ota.absimpl.freqchip.OTAImpl$MyHandler r0 = r10.myHandler     // Catch: java.io.IOException -> Lb2
            r0.sendEmptyMessage(r11)     // Catch: java.io.IOException -> Lb2
            goto Lbc
        Lb2:
            r11 = move-exception
            r11.printStackTrace()
            goto Lbc
        Lb7:
            java.lang.String r11 = "请选择要发送的文件"
            ycble.runchinaup.log.ycBleLog.e(r11)
        Lbc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ycble.runchinaup.ota.absimpl.freqchip.OTAImpl.doSendFileByBluetooth(java.lang.String):void");
    }

    public int getRecv_data() {
        return this.recv_data;
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void loadCfg() {
        addBleUnitTask(BleUnitTask.createEnableNotify(UUID_OTA_SERVICE, UUID_OTA_RECV_DATA, "打开通知"));
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onConnException() {
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onConnectSuccess() {
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onDataReceive(byte[] bArr, UUID uuid) {
        ycBleLog.e("接收到数据" + BleUtil.byte2HexStr(bArr));
        this.baseaddr = bArr;
        setRecv_data(1);
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onDataWrite(byte[] bArr, boolean z, UUID... uuidArr) {
        super.onDataWrite(bArr, z, uuidArr);
        if (z) {
            this.writeStatus = true;
        }
        hanWithTask();
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onFinishTaskAfterConn() {
        if (verifyFile()) {
            new Thread(new Runnable() { // from class: ycble.runchinaup.ota.absimpl.freqchip.OTAImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        OTAImpl.this.doSendFileByBluetooth(OTAImpl.this.filePath);
                        OTAImpl.this.myHandler.sendEmptyMessage(1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (BleUUIDNullException e2) {
                        e2.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onHandDisConn() {
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public void setOtaCallback(OTACallback oTACallback) {
        this.otaCallback = oTACallback;
    }

    public void setRecv_data(int i) {
        this.recv_data = i;
    }
}
