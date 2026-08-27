package com.example.otalib.boads;

import android.os.Handler;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import com.liulishuo.filedownloader.model.FileDownloadStatus;

/* loaded from: classes.dex */
public class msg {
    final int ERR_NO_RESPONSE = Constant.NORESPONSEERROR;
    final int ERR_INVALID_PARAMETER = Constant.INVALIDPARAMETERERROR;
    final int ERR_NOT_CONNECTED = Constant.FILETOBIGERROR;
    final int ERR_ANSWER_TOO_BIG = -1204;
    final int ERR_INVALID_RESPONSE = -1205;
    final int ERR_MEM_ACCESS = -1206;
    final int ERR_FILE_ACCESS = -1207;
    final int ERR_EXEC_FORMAT = Constant.EXECFORMATERROR;
    private final int OPCODE_ERASE = 0;
    private final int OPCODE_READID = 1;
    private final int OPCODE_READ = 2;
    private final int OPCODE_WRITE = 3;
    private final int OPCODE_WRITE_KEY = 4;
    private final int OPCODE_REBOOT = 5;
    private final int OPCODE_WRITE_BOOST = 6;
    private final int OPCODE_SET_BAUDRATE = 7;
    private final int OPCODE_VERIFY = 8;
    private final int OPCODE_LOCK_JTAG = 9;
    private final int OPCODE_PIN = 10;
    private final int OPCODE_READ_PART = 11;
    private final int OPCODE_WRITE_PART = 12;
    private final int OPCODE_READ_REG = 13;
    private final int OPCODE_WRITE_REG = 14;
    private final int OPCODE_RFU_f = 15;
    private final int OPCODE_SECURE_JTAG = 16;
    private final int OPCODE_SECURE_ROP = 17;
    private final int OPCODE_SECURE_ENC = 18;
    private final int OPCODE_SECURE_SIG = 19;
    final int MAX_BLOCK_SIZE = 1024;
    final int MAX_FRAME_SIZE = 1500;
    final int DEFAULT_EXEC_TIME = 2000;
    private byte[] cmdBuffer = new byte[1500];
    private byte[] ansBuffer = new byte[1500];

    private int generic_transaction(TransOverBle transOverBle, int i, byte[] bArr, int i2, byte[] bArr2, int i3) {
        return new msg_ble().ble_transaction(transOverBle, i, bArr, i2, bArr2, i3);
    }

    int AppendByte(byte b, byte[] bArr, int i) {
        int i2 = i + 1;
        bArr[i] = b;
        return i2;
    }

    int AppendLong(int i, byte[] bArr, int i2) {
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >> 16);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i >> 8);
        int i6 = i5 + 1;
        bArr[i5] = (byte) i;
        return i6;
    }

    int AppendWord(short s, byte[] bArr, int i) {
        int i2 = i + 1;
        bArr[i] = (byte) (s >> 8);
        int i3 = i2 + 1;
        bArr[i2] = (byte) s;
        return i3;
    }

    int ExtractByte(byte[] bArr, byte[] bArr2, int i) {
        int i2 = i + 1;
        bArr[0] = bArr2[i];
        return i2;
    }

    int ExtractLong(byte[] bArr, byte[] bArr2, int i) {
        int i2 = i + 1;
        bArr[0] = bArr2[i];
        int i3 = i2 + 1;
        bArr[1] = bArr2[i2];
        int i4 = i3 + 1;
        bArr[2] = bArr2[i3];
        int i5 = i4 + 1;
        bArr[3] = bArr2[i4];
        return i5;
    }

    int msg_enable_sram_part(TransOverBle transOverBle, int i, int i2, int i3) {
        byte[] bArr = new byte[1];
        generic_transaction(transOverBle, AppendLong(i2, this.cmdBuffer, AppendLong(i, this.cmdBuffer, AppendByte((byte) i3, this.cmdBuffer, AppendByte((byte) 12, this.cmdBuffer, 0)))), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        ExtractByte(bArr, this.ansBuffer, 0);
        return bArr[0] != 12 ? -1205 : 0;
    }

    int msg_erase_flash(TransOverBle transOverBle, int i, int i2) {
        byte[] bArr = new byte[1];
        int generic_transaction = generic_transaction(transOverBle, AppendLong(i2, this.cmdBuffer, AppendLong(i, this.cmdBuffer, AppendByte((byte) 0, this.cmdBuffer, 0))), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        byte[] bArr2 = new byte[4];
        ExtractLong(bArr2, this.ansBuffer, ExtractByte(bArr, this.ansBuffer, 0));
        int MSB_Bytearray_2_Int4 = Utils.MSB_Bytearray_2_Int4(bArr2);
        if (bArr[0] != 0) {
            return -1205;
        }
        return MSB_Bytearray_2_Int4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int msg_load_binary(TransOverBle transOverBle, byte[] bArr, int i, int i2, Handler handler) {
        byte[] bArr2 = new byte[1];
        byte[] bArr3 = new byte[4];
        if (bArr == null) {
            return -1;
        }
        long length = bArr.length;
        int generic_transaction = generic_transaction(transOverBle, AppendLong((int) length, this.cmdBuffer, AppendLong(i, this.cmdBuffer, AppendByte((byte) i2, this.cmdBuffer, AppendByte((byte) 12, this.cmdBuffer, 0)))), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        int ExtractByte = ExtractByte(bArr2, this.ansBuffer, 0);
        if (bArr2[0] != 12) {
            return -1205;
        }
        ExtractLong(bArr3, this.ansBuffer, ExtractByte);
        int MSB_Bytearray_2_Int4 = Utils.MSB_Bytearray_2_Int4(bArr3);
        if (MSB_Bytearray_2_Int4 < 0) {
            return MSB_Bytearray_2_Int4;
        }
        int msg_write_from_file = msg_write_from_file(transOverBle, MSB_Bytearray_2_Int4, length, bArr, handler);
        if (length == msg_write_from_file) {
            return 0;
        }
        return msg_write_from_file == -8 ? Constant.EXECFORMATERROR : msg_write_from_file;
    }

    int msg_lock_jtag(TransOverBle transOverBle) {
        byte[] bArr = new byte[1];
        int generic_transaction = generic_transaction(transOverBle, AppendByte((byte) 16, this.cmdBuffer, 0), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        byte[] bArr2 = new byte[4];
        ExtractLong(bArr2, this.ansBuffer, ExtractByte(bArr, this.ansBuffer, 0));
        int MSB_Bytearray_2_Int4 = Utils.MSB_Bytearray_2_Int4(bArr2);
        if (bArr[0] != 16) {
            return -1205;
        }
        return MSB_Bytearray_2_Int4;
    }

    public int msg_read_part(int i, int i2, TransOverBle transOverBle, Handler handler) {
        byte[] bArr = new byte[1];
        byte[] bArr2 = new byte[4];
        byte[] bArr3 = new byte[4];
        int generic_transaction = generic_transaction(transOverBle, AppendByte((byte) i2, this.cmdBuffer, AppendByte(FileDownloadStatus.toFileDownloadService, this.cmdBuffer, 0)), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        int ExtractByte = ExtractByte(bArr, this.ansBuffer, 0);
        if (bArr[0] != 11) {
            return -1205;
        }
        int ExtractLong = ExtractLong(bArr2, this.ansBuffer, ExtractByte);
        int MSB_Bytearray_2_Int4 = Utils.MSB_Bytearray_2_Int4(bArr2);
        ExtractLong(bArr3, this.ansBuffer, ExtractLong);
        int MSB_Bytearray_2_Int42 = Utils.MSB_Bytearray_2_Int4(bArr3);
        Message obtain = Message.obtain();
        obtain.what = Constant.MSG_WHAT_READ_PART;
        obtain.arg1 = MSB_Bytearray_2_Int4;
        obtain.arg2 = MSB_Bytearray_2_Int42;
        obtain.obj = Integer.valueOf(i);
        handler.sendMessage(obtain);
        return 0;
    }

    public int msg_reset_link(TransOverBle transOverBle) {
        return new msg_ble().AsynResetLink(transOverBle);
    }

    int msg_reset_target(TransOverBle transOverBle, int i) {
        byte[] bArr = new byte[1];
        int generic_transaction = generic_transaction(transOverBle, AppendLong(i, this.cmdBuffer, AppendByte((byte) 5, this.cmdBuffer, 0)), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 100);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        byte[] bArr2 = new byte[4];
        ExtractLong(bArr2, this.ansBuffer, ExtractByte(bArr, this.ansBuffer, 0));
        int MSB_Bytearray_2_Int4 = Utils.MSB_Bytearray_2_Int4(bArr2);
        if (bArr[0] != 5) {
            return -1205;
        }
        return MSB_Bytearray_2_Int4;
    }

    int msg_secury_encrpt(TransOverBle transOverBle) {
        byte[] bArr = new byte[1];
        int generic_transaction = generic_transaction(transOverBle, AppendByte((byte) 18, this.cmdBuffer, 0), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        byte[] bArr2 = new byte[4];
        ExtractLong(bArr2, this.ansBuffer, ExtractByte(bArr, this.ansBuffer, 0));
        int MSB_Bytearray_2_Int4 = Utils.MSB_Bytearray_2_Int4(bArr2);
        if (bArr[0] != 18) {
            return -1205;
        }
        return MSB_Bytearray_2_Int4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int msg_set_rop(TransOverBle transOverBle) {
        byte[] bArr = new byte[1];
        int generic_transaction = generic_transaction(transOverBle, AppendByte((byte) 17, this.cmdBuffer, 0), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        byte[] bArr2 = new byte[4];
        ExtractLong(bArr2, this.ansBuffer, ExtractByte(bArr, this.ansBuffer, 0));
        int MSB_Bytearray_2_Int4 = Utils.MSB_Bytearray_2_Int4(bArr2);
        if (bArr[0] != 17) {
            return -1205;
        }
        return MSB_Bytearray_2_Int4;
    }

    int msg_write_from_file(TransOverBle transOverBle, int i, long j, byte[] bArr, Handler handler) {
        byte[] bArr2 = bArr;
        int i2 = 1024;
        byte[] bArr3 = new byte[1024];
        Message message = new Message();
        message.arg1 = 1002;
        long j2 = 0;
        int i3 = 0;
        message.arg2 = (int) ((j / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + (j % PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID == 0 ? 0 : 1));
        handler.sendMessage(message);
        long j3 = j;
        long currentTimeMillis = System.currentTimeMillis();
        int i4 = 0;
        int i5 = 0;
        float f = 0.0f;
        int i6 = 0;
        int i7 = i;
        while (j3 > j2) {
            int length = bArr2.length - i4 > i2 ? 1024 : bArr2.length - i4;
            System.arraycopy(bArr2, i4, bArr3, i3, length);
            int msg_write_mem = msg_write_mem(transOverBle, bArr3, i7, length);
            Log.i("JavaXmodem", "cnt:" + length);
            if (msg_write_mem <= 0) {
                return msg_write_mem;
            }
            i7 += length;
            long j4 = j3 - length;
            i5 += length;
            i4 += length;
            f += length;
            if (System.currentTimeMillis() - currentTimeMillis >= 1000) {
                long currentTimeMillis2 = System.currentTimeMillis();
                Message obtain = Message.obtain();
                obtain.arg1 = 1008;
                obtain.obj = Float.valueOf(f / 1024.0f);
                handler.sendMessage(obtain);
                currentTimeMillis = currentTimeMillis2;
                f = 0.0f;
            }
            int i8 = i6 + 1;
            Message message2 = new Message();
            message2.arg1 = 1003;
            message2.arg2 = i8;
            Log.i("JavaXmodem", "num:" + i8);
            handler.sendMessage(message2);
            if (length < bArr3.length) {
                break;
            }
            i6 = i8;
            j3 = j4;
            bArr2 = bArr;
            i2 = 1024;
            j2 = 0;
            i3 = 0;
        }
        return i5;
    }

    int msg_write_mem(TransOverBle transOverBle, byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[1];
        int AppendLong = AppendLong(i2, this.cmdBuffer, AppendLong(i, this.cmdBuffer, AppendByte((byte) 3, this.cmdBuffer, 0)));
        int i3 = 0;
        while (i3 < i2) {
            this.cmdBuffer[AppendLong] = bArr[i3];
            i3++;
            AppendLong++;
        }
        int generic_transaction = generic_transaction(transOverBle, AppendLong, this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        int ExtractByte = ExtractByte(bArr2, this.ansBuffer, 0);
        if (bArr2[0] != 3) {
            return -1205;
        }
        byte[] bArr3 = new byte[4];
        ExtractLong(bArr3, this.ansBuffer, ExtractByte);
        return Utils.MSB_Bytearray_2_Int4(bArr3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int msg_write_verify(TransOverBle transOverBle, byte b) {
        byte[] bArr = new byte[1];
        int generic_transaction = generic_transaction(transOverBle, AppendByte(b, this.cmdBuffer, AppendByte((byte) 8, this.cmdBuffer, 0)), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        byte[] bArr2 = new byte[4];
        ExtractLong(bArr2, this.ansBuffer, ExtractByte(bArr, this.ansBuffer, 0));
        int MSB_Bytearray_2_Int4 = Utils.MSB_Bytearray_2_Int4(bArr2);
        if (bArr[0] != 8) {
            return -1205;
        }
        return MSB_Bytearray_2_Int4;
    }

    public int write_user_data(TransOverBle transOverBle, byte[] bArr, int i, int i2, Handler handler) {
        int length = bArr.length;
        byte[] bArr2 = new byte[1];
        byte[] bArr3 = new byte[4];
        int generic_transaction = generic_transaction(transOverBle, AppendLong(length, this.cmdBuffer, AppendLong(i, this.cmdBuffer, AppendByte((byte) i2, this.cmdBuffer, AppendByte((byte) 12, this.cmdBuffer, 0)))), this.cmdBuffer, this.ansBuffer.length, this.ansBuffer, 2000);
        if (generic_transaction < 0) {
            return generic_transaction;
        }
        int ExtractByte = ExtractByte(bArr2, this.ansBuffer, 0);
        if (bArr2[0] != 12) {
            return -1205;
        }
        ExtractLong(bArr3, this.ansBuffer, ExtractByte);
        int MSB_Bytearray_2_Int4 = Utils.MSB_Bytearray_2_Int4(bArr3);
        if (MSB_Bytearray_2_Int4 < 0) {
            return MSB_Bytearray_2_Int4;
        }
        int msg_write_from_file = msg_write_from_file(transOverBle, MSB_Bytearray_2_Int4, length, bArr, handler);
        if (bArr.length == msg_write_from_file) {
            return 0;
        }
        return msg_write_from_file == -8 ? Constant.EXECFORMATERROR : msg_write_from_file;
    }
}
