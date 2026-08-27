package com.example.otalib.boads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.jni.ecc256;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class WorkOnBoads {
    private static final int PART_TYPE_APP = 96;
    private static final int PART_TYPE_CFG = 98;
    private static final int PART_TYPE_PATCH = 97;
    private static final int PART_TYPE_USER = 4;
    private static final int ROM_MODE = 1;
    private Context context;
    private Semaphore m_ota_cmd_rx_cnt;
    private BlockingQueue<Byte> m_ota_cmd_rx_data;
    private TransOverBle m_trans;
    private byte[] mlocal_key_x;
    private byte[] mlocal_key_y;
    private Handler ota_frag_handler;
    private byte[] private_key;
    private Semaphore send_cmd_cnt;
    private byte[] remote_key_x = new byte[32];
    private byte[] remote_key_y = new byte[32];
    private byte[] mdhkey = new byte[32];
    private boolean isEncrypt = false;
    byte[] OTAS_CMD_EXCHANGE_KEY = {1, 0, 0, 0};
    byte[] CONNECT_INTERVAL_DATA = {2, 0, 8, 0, 6, 0, 6, 0, 0, 0, -56, 0};
    private boolean mConnectionInterval = false;
    private boolean mIsResetLink = false;
    private boolean mIsVerify = false;
    private boolean mIntoROP = false;
    private BootInformation boot_info = new BootInformation();
    private msg m_msg = new msg();

    public WorkOnBoads(Context context, Handler handler) {
        this.context = context;
        this.ota_frag_handler = handler;
        this.m_trans = new TransOverBle(context, handler);
    }

    private int hs_load_binary(byte[] bArr, int i, int i2) {
        return this.m_msg.msg_load_binary(this.m_trans, bArr, i, i2, this.ota_frag_handler);
    }

    private int hs_reset_link() {
        return this.m_msg.msg_reset_link(this.m_trans);
    }

    private int hs_write_ROP() {
        return this.m_msg.msg_set_rop(this.m_trans);
    }

    private int hs_write_verify() {
        return this.m_msg.msg_write_verify(this.m_trans, (byte) 1);
    }

    public void EntryIspMoudle(int i) {
        byte[] bArr = {99, 0, 0, 0};
        byte[] bArr2 = {100, 0, 0, 0};
        Message obtain = Message.obtain();
        if (i == 1006) {
            obtain.arg1 = 1000;
            obtain.arg2 = bArr.length;
            obtain.obj = bArr;
            this.ota_frag_handler.sendMessage(obtain);
            return;
        }
        if (i == 1007) {
            obtain.arg1 = 1000;
            obtain.arg2 = bArr2.length;
            obtain.obj = bArr2;
            this.ota_frag_handler.sendMessage(obtain);
        }
    }

    public int LoadBinary(byte[] bArr, int i) {
        int i2;
        if (bArr == null && bArr.length <= 0) {
            return -1;
        }
        if (!this.mConnectionInterval) {
            Message obtain = Message.obtain();
            obtain.arg1 = 1000;
            obtain.arg2 = this.CONNECT_INTERVAL_DATA.length;
            obtain.obj = this.CONNECT_INTERVAL_DATA;
            this.ota_frag_handler.sendMessage(obtain);
            this.mConnectionInterval = true;
        }
        if (this.mIsResetLink) {
            i2 = -1;
        } else {
            i2 = hs_reset_link();
            if (i2 < 0) {
                this.mIsResetLink = false;
                return i2;
            }
            this.mIsResetLink = true;
        }
        if (!this.mIsVerify) {
            i2 = hs_write_verify();
            if (i2 < 0) {
                this.mIsVerify = false;
                return i2;
            }
            this.mIsVerify = true;
        }
        try {
            this.send_cmd_cnt.acquire();
            switch (i) {
                case 0:
                    i2 = hs_load_binary(bArr, 0, 96);
                    break;
                case 1:
                    i2 = hs_load_binary(bArr, 0, 98);
                    break;
                case 2:
                    i2 = hs_load_binary(bArr, 0, 97);
                    break;
            }
            if (this.m_ota_cmd_rx_data != null) {
                this.m_ota_cmd_rx_data.clear();
                this.m_ota_cmd_rx_data = null;
            }
            if (this.m_ota_cmd_rx_cnt != null) {
                this.m_ota_cmd_rx_cnt = null;
            }
            if (this.send_cmd_cnt != null) {
                this.send_cmd_cnt = null;
            }
            return i2;
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.send_cmd_cnt.release();
            return -1;
        }
    }

    public int ReadPart(int i) {
        if (!this.mIsResetLink) {
            int hs_reset_link = hs_reset_link();
            if (hs_reset_link < 0) {
                this.mIsResetLink = false;
                return hs_reset_link;
            }
            this.mIsResetLink = true;
        }
        switch (i) {
            case 0:
                return this.m_msg.msg_read_part(0, 96, this.m_trans, this.ota_frag_handler);
            case 1:
                return this.m_msg.msg_read_part(1, 98, this.m_trans, this.ota_frag_handler);
            case 2:
                return this.m_msg.msg_read_part(2, 97, this.m_trans, this.ota_frag_handler);
            default:
                return this.m_msg.msg_read_part(0, i, this.m_trans, this.ota_frag_handler);
        }
    }

    public void ResetTarget() {
        this.mIsResetLink = false;
        this.mIsVerify = false;
        this.mConnectionInterval = false;
        if (this.m_ota_cmd_rx_data != null) {
            this.m_ota_cmd_rx_data = null;
        }
        if (this.m_ota_cmd_rx_cnt != null) {
            this.m_ota_cmd_rx_cnt = null;
        }
        if (this.send_cmd_cnt != null) {
            this.send_cmd_cnt = null;
        }
        byte[] bArr = {0, 0, 0, 0};
        Message obtain = Message.obtain();
        obtain.arg1 = 1000;
        obtain.arg2 = bArr.length;
        obtain.obj = bArr;
        this.ota_frag_handler.sendMessage(obtain);
    }

    public int WriteUserData(byte[] bArr, String str) {
        if (bArr == null && bArr.length <= 0) {
            return -1;
        }
        if (!this.mConnectionInterval) {
            Message obtain = Message.obtain();
            obtain.arg1 = 1000;
            obtain.arg2 = this.CONNECT_INTERVAL_DATA.length;
            obtain.obj = this.CONNECT_INTERVAL_DATA;
            this.ota_frag_handler.sendMessage(obtain);
            this.mConnectionInterval = true;
        }
        if (!Pattern.matches("[a-f0-9A-F]{1,12}", str)) {
            return Constant.USERADDRERROR;
        }
        int parseInt = Integer.parseInt(str, 16);
        if (!this.mIsResetLink) {
            int hs_reset_link = hs_reset_link();
            if (hs_reset_link < 0) {
                this.mIsResetLink = false;
                return hs_reset_link;
            }
            this.mIsResetLink = true;
        }
        if (!this.mIsVerify) {
            int hs_write_verify = hs_write_verify();
            if (hs_write_verify < 0) {
                this.mIsVerify = false;
                return hs_write_verify;
            }
            this.mIsVerify = true;
        }
        try {
            this.send_cmd_cnt.acquire();
            int write_user_data = this.m_msg.write_user_data(this.m_trans, bArr, parseInt, 4, this.ota_frag_handler);
            this.send_cmd_cnt.release();
            return write_user_data;
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.send_cmd_cnt.release();
            return -1;
        }
    }

    public void setBluetoothNotifyData(byte[] bArr, int i) {
        int i2 = 0;
        switch (i) {
            case 0:
                break;
            case 1:
                if (this.isEncrypt) {
                    OTAEncrypt.otas_decrypt(bArr, bArr, bArr.length, this.mdhkey);
                }
                while (i2 < bArr.length) {
                    try {
                        this.m_trans.m_recv_data.put(Byte.valueOf(bArr[i2]));
                        this.m_trans.m_recv_cnt.release(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i2++;
                }
                return;
            default:
                return;
        }
        while (i2 < bArr.length) {
            try {
                this.m_ota_cmd_rx_data.put(Byte.valueOf(bArr[i2]));
                this.m_ota_cmd_rx_cnt.release(1);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            i2++;
        }
    }

    public void setEncrypt(boolean z) {
        boolean z2;
        this.m_ota_cmd_rx_data = new LinkedBlockingQueue();
        this.m_ota_cmd_rx_cnt = new Semaphore(0);
        this.send_cmd_cnt = new Semaphore(1);
        if (z) {
            this.isEncrypt = z;
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < 32; i++) {
                stringBuffer.append(Integer.toHexString(new Random().nextInt(16)) + Integer.toHexString(new Random().nextInt(16)));
            }
            this.private_key = Utils.toByteArray(stringBuffer.toString());
            OTAEncrypt.setTx_dat_count(0);
            OTAEncrypt.setRx_dat_count(0);
            ecc256 ecc256Var = new ecc256();
            this.mlocal_key_x = new byte[32];
            this.mlocal_key_y = new byte[32];
            ecc256Var.ecc_generate_public_key(this.private_key, this.mlocal_key_x, this.mlocal_key_y);
            byte[] bArr = new byte[this.OTAS_CMD_EXCHANGE_KEY.length + this.mlocal_key_x.length + this.mlocal_key_y.length];
            int length = this.mlocal_key_x.length + this.mlocal_key_y.length;
            this.OTAS_CMD_EXCHANGE_KEY[2] = (byte) (length & 255);
            this.OTAS_CMD_EXCHANGE_KEY[3] = (byte) ((length >> 8) & 255);
            System.arraycopy(this.OTAS_CMD_EXCHANGE_KEY, 0, bArr, 0, this.OTAS_CMD_EXCHANGE_KEY.length);
            System.arraycopy(this.mlocal_key_x, 0, bArr, this.OTAS_CMD_EXCHANGE_KEY.length, this.mlocal_key_x.length);
            System.arraycopy(this.mlocal_key_y, 0, bArr, this.mlocal_key_x.length + this.OTAS_CMD_EXCHANGE_KEY.length, this.mlocal_key_y.length);
            Message obtain = Message.obtain();
            obtain.arg1 = 1000;
            obtain.arg2 = bArr.length;
            obtain.obj = bArr;
            this.ota_frag_handler.sendMessage(obtain);
            try {
                z2 = this.m_ota_cmd_rx_cnt.tryAcquire((this.remote_key_x.length * 2) + 4, 10000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                z2 = false;
            }
            if (!z2) {
                if (this.m_ota_cmd_rx_data != null) {
                    this.m_ota_cmd_rx_data.clear();
                    this.m_ota_cmd_rx_data = null;
                }
                if (this.m_ota_cmd_rx_cnt != null) {
                    this.m_ota_cmd_rx_cnt = null;
                }
                if (this.send_cmd_cnt != null) {
                    this.send_cmd_cnt = null;
                }
                this.isEncrypt = false;
                Message obtain2 = Message.obtain();
                obtain2.arg1 = 1001;
                this.ota_frag_handler.sendMessage(obtain2);
                return;
            }
            for (int i2 = 0; i2 < (this.remote_key_x.length * 2) + 4; i2++) {
                if (i2 < 4) {
                    this.m_ota_cmd_rx_data.poll();
                } else if (i2 >= 4 && i2 < this.remote_key_x.length + 4) {
                    Byte poll = this.m_ota_cmd_rx_data.poll();
                    if (poll == null) {
                        this.isEncrypt = false;
                        Message message = new Message();
                        message.arg1 = 1001;
                        this.ota_frag_handler.sendMessage(message);
                    }
                    this.remote_key_x[i2 - 4] = poll.byteValue();
                } else if (i2 >= this.remote_key_x.length + 4 && i2 < (this.remote_key_x.length * 2) + 4) {
                    Byte poll2 = this.m_ota_cmd_rx_data.poll();
                    if (poll2 == null) {
                        this.isEncrypt = false;
                        Message message2 = new Message();
                        message2.arg1 = 1001;
                        this.ota_frag_handler.sendMessage(message2);
                    }
                    this.remote_key_y[(i2 - 4) - this.remote_key_x.length] = poll2.byteValue();
                }
            }
            ecc256Var.ecc_generate_dhkey(this.private_key, this.remote_key_x, this.remote_key_y, this.mdhkey);
            this.m_trans.setEncryptkey(this.mdhkey, this.isEncrypt);
        }
    }
}
