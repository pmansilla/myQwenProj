package com.example.otalib.boads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class TransOverBle {
    private byte[] mdhkey;
    public Handler ota_frag_handler;
    public Semaphore m_recv_cnt = new Semaphore(0);
    public Semaphore serial_recv_lock = new Semaphore(1);
    public BlockingQueue<Byte> m_recv_data = new LinkedBlockingQueue();
    private String tag = "TransOverBle";
    private boolean mIsEncrypt = false;

    /* loaded from: classes.dex */
    public static class flash_cmd_cntrl_param_t {
        int data_len;
        int flash_addr;
        byte[] key = new byte[16];
    }

    public TransOverBle() {
    }

    public TransOverBle(Context context, Handler handler) {
        this.ota_frag_handler = handler;
    }

    private byte[] hs_recv_data(int i, int i2) {
        boolean z;
        byte[] bArr = new byte[i];
        try {
            z = this.m_recv_cnt.tryAcquire(i, i2, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            z = false;
        }
        if (!z) {
            Log.i(this.tag, "receive timeout");
            return null;
        }
        for (int i3 = 0; i3 < i; i3++) {
            try {
                this.serial_recv_lock.acquire(1);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            if (this.m_recv_data.isEmpty()) {
                return null;
            }
            bArr[i3] = this.m_recv_data.poll().byteValue();
            this.serial_recv_lock.release(1);
        }
        return bArr;
    }

    private int hs_send_data(byte[] bArr, int i) {
        if (bArr == null) {
            Log.i(this.tag, "send data is null");
            return 0;
        }
        if (this.mIsEncrypt) {
            OTAEncrypt.otas_encrypt(bArr, bArr, i, this.mdhkey);
        }
        Message message = new Message();
        message.arg1 = 1004;
        message.arg2 = i;
        message.obj = bArr;
        this.ota_frag_handler.sendMessage(message);
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x001e. Please report as an issue. */
    public int flash_cntrl(TransOpcodeType transOpcodeType, flash_cmd_cntrl_param_t flash_cmd_cntrl_param_tVar) {
        byte[] bArr = new byte[1024];
        bArr[0] = 0;
        bArr[1] = transOpcodeType.opcode;
        bArr[2] = 0;
        int i = 25;
        switch (transOpcodeType.opcode) {
            case 0:
            case 2:
            case 3:
                if (flash_cmd_cntrl_param_tVar == null) {
                    return -1;
                }
                if (flash_cmd_cntrl_param_tVar.data_len == 0) {
                    return 0;
                }
                bArr[5] = 8;
                bArr[6] = 0;
                bArr[7] = 1;
                bArr[8] = 0;
                bArr[9] = (byte) (flash_cmd_cntrl_param_tVar.flash_addr & 255);
                bArr[10] = (byte) ((flash_cmd_cntrl_param_tVar.flash_addr >> 8) & 255);
                bArr[11] = (byte) ((flash_cmd_cntrl_param_tVar.flash_addr >> 16) & 255);
                bArr[12] = (byte) ((flash_cmd_cntrl_param_tVar.flash_addr >> 24) & 255);
                bArr[13] = 8;
                bArr[14] = 0;
                bArr[15] = 2;
                bArr[16] = 0;
                bArr[17] = (byte) (flash_cmd_cntrl_param_tVar.data_len & 255);
                bArr[18] = (byte) ((flash_cmd_cntrl_param_tVar.data_len >> 8) & 255);
                bArr[19] = (byte) ((flash_cmd_cntrl_param_tVar.data_len >> 16) & 255);
                i = 21;
                bArr[20] = (byte) ((flash_cmd_cntrl_param_tVar.data_len >> 24) & 255);
                bArr[3] = (byte) 21;
                bArr[4] = (byte) 0;
                bArr[2] = hs_check_sum(bArr, 21);
                flush();
                return send(bArr, i);
            case 1:
            case 5:
            case 6:
                bArr[3] = (byte) 5;
                bArr[4] = (byte) 0;
                bArr[2] = hs_check_sum(bArr, 5);
                i = 5;
                flush();
                return send(bArr, i);
            case 4:
                bArr[5] = 20;
                bArr[6] = 0;
                bArr[7] = 4;
                bArr[8] = 0;
                System.arraycopy(flash_cmd_cntrl_param_tVar.key, 0, bArr, 9, flash_cmd_cntrl_param_tVar.key.length);
                bArr[3] = (byte) 25;
                bArr[4] = (byte) 0;
                bArr[2] = hs_check_sum(bArr, 25);
                flush();
                return send(bArr, i);
            default:
                i = 5;
                flush();
                return send(bArr, i);
        }
    }

    public void flush() {
        this.m_recv_data.clear();
    }

    boolean get_and_check_flash_rsp_pkt(TransOpcodeType transOpcodeType) {
        byte[] hs_recv_data = hs_recv_data(5, 4000);
        if (hs_recv_data == null) {
            try {
                this.m_recv_cnt.acquire(this.m_recv_data.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.m_recv_data.clear();
            return false;
        }
        if (hs_recv_data[0] != 1 || hs_recv_data[1] != transOpcodeType.opcode) {
            try {
                this.m_recv_cnt.acquire(this.m_recv_data.size());
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            this.m_recv_data.clear();
            return false;
        }
        byte[] hs_recv_data2 = hs_recv_data((short) (((short) (hs_recv_data[3] | (hs_recv_data[4] << 8))) - 5), 4000);
        if (hs_recv_data2 == null) {
            try {
                this.m_recv_cnt.acquire(this.m_recv_data.size());
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            this.m_recv_data.clear();
            return false;
        }
        byte[] bArr = new byte[hs_recv_data.length + hs_recv_data2.length];
        System.arraycopy(hs_recv_data, 0, bArr, 0, hs_recv_data.length);
        System.arraycopy(hs_recv_data2, 0, bArr, hs_recv_data.length, hs_recv_data2.length);
        if (!hs_is_check_sum_ok(bArr)) {
            try {
                this.m_recv_cnt.acquire(this.m_recv_data.size());
            } catch (InterruptedException e4) {
                e4.printStackTrace();
            }
            this.m_recv_data.clear();
            return false;
        }
        switch (transOpcodeType.opcode) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
                if (bArr.length >= 10) {
                    return bArr[9] == 0;
                }
                break;
            case 1:
                if (bArr.length >= 10) {
                    return ((short) (bArr[5] | (bArr[6] << 8))) == 8;
                }
                break;
        }
        try {
            this.m_recv_cnt.acquire(this.m_recv_data.size());
        } catch (InterruptedException e5) {
            e5.printStackTrace();
        }
        this.m_recv_data.clear();
        return false;
    }

    public byte hs_check_sum(byte[] bArr, int i) {
        if (bArr.length == 0) {
            return (byte) 0;
        }
        byte b = 0;
        for (int i2 = 0; i2 < i; i2++) {
            b = (byte) (b + bArr[i2]);
        }
        return b;
    }

    public boolean hs_is_check_sum_ok(byte[] bArr) {
        byte b = (byte) (bArr[0] + bArr[1]);
        for (int i = 3; i < bArr.length; i++) {
            b = (byte) (b + bArr[i]);
        }
        return b == bArr[2];
    }

    public void hs_send_isp_data(byte[] bArr) {
        if (bArr != null) {
            Message message = new Message();
            message.arg1 = 1000;
            message.obj = bArr;
            this.ota_frag_handler.sendMessage(message);
        }
    }

    public byte[] receive(int i) {
        return hs_recv_data(i, 10000);
    }

    public byte[] receive(int i, int i2) {
        return hs_recv_data(i, i2);
    }

    public int send(byte[] bArr, int i) {
        return hs_send_data(bArr, i);
    }

    public void setEncryptkey(byte[] bArr, boolean z) {
        this.mdhkey = bArr;
        this.mIsEncrypt = z;
    }

    public byte[] start_xmodem_receive_file(int i) {
        return new JavaXmodem().xmodem_receive(i, this);
    }

    public int start_xmodem_trans_file(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return -1;
        }
        return new JavaXmodem().xmodem_transmit(bArr, bArr.length, this);
    }

    public int start_xmodem_trans_file(byte[] bArr, Handler handler) {
        if (bArr == null || bArr.length == 0) {
            return -1;
        }
        return new JavaXmodem().xmodem_transmit(bArr, bArr.length, this, handler);
    }
}
