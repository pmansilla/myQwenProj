package com.example.otalib.boads;

import com.alibaba.fastjson.asm.Opcodes;
import com.autonavi.amap.mapcore.AMapEngineUtils;

/* loaded from: classes.dex */
public class msg_ble {
    private int repeats;
    final int DLE = 16;
    final int STX = 2;
    final int ETX = 3;
    final int FRAME_COUNT_FIELD = Opcodes.CHECKCAST;
    final int FRAME_LENGTH_MASK = 7;
    final int FRAME_TYPE_MASK = 56;
    final int FRAME_LNK_TYPE = 0;
    final int FRAME_ATT_TYPE = 8;
    final int FRAME_STD_TYPE = 16;
    final int LNK_RESET = 1;
    final int LNK_ECHO = 2;
    final int LNK_SET_BAUDRATE = 3;
    final int MAX_FRAME_SIZE = 1500;
    final int ASYN_SETUP = -1001;
    final int ASYN_RX_TIMEOUT = -1002;
    final int ASYN_RX_ERROR = -1003;
    final int ASYN_RX_FORMAT = -1004;
    final int ASYN_RX_BCC = -1005;
    final int ASYN_RX_OVERFLOW = -1006;
    final int ASYN_TX_ERROR = -1007;
    final int ERR_NO_RESPONSE = Constant.NORESPONSEERROR;
    final int ERR_INVALID_PARAMETER = Constant.INVALIDPARAMETERERROR;
    final int ERR_NOT_CONNECTED = Constant.FILETOBIGERROR;
    final int ERR_ANSWER_TOO_BIG = -1204;
    final int ERR_INVALID_RESPONSE = -1205;
    final int ERR_MEM_ACCESS = -1206;
    final int ERR_FILE_ACCESS = -1207;
    final int ERR_WORKING_RAM = -1212;
    final int ERR_FIRMWARE_FILE = -1302;
    final int SLIP_END = Opcodes.CHECKCAST;
    final int SLIP_ESC = 219;
    final int SLIP_ESC_END = 220;
    final int SLIP_ESC_ESC = 221;
    final int SLIP_ESC_AVA = AMapEngineUtils.ARROW_LINE_OUTER_TEXTURE_ID;
    private byte[] txFrame = new byte[1500];
    private byte[] rxFrame = new byte[1500];
    private int frameCount = 0;

    private int AsynSendFrame(TransOverBle transOverBle, int i, byte[] bArr) {
        byte[] bArr2 = new byte[1500];
        int i2 = 0;
        bArr2[0] = 16;
        int i3 = 2;
        bArr2[1] = 2;
        byte b = 0;
        while (i != 0) {
            i--;
            int i4 = i2 + 1;
            byte b2 = bArr[i2];
            int i5 = i3 + 1;
            bArr2[i3] = b2;
            b = (byte) (b ^ b2);
            if (b2 == 16) {
                i3 = i5 + 1;
                bArr2[i5] = 16;
                i2 = i4;
            } else {
                i2 = i4;
                i3 = i5;
            }
        }
        int i6 = i3 + 1;
        bArr2[i3] = 16;
        int i7 = i6 + 1;
        bArr2[i6] = 3;
        int i8 = i7 + 1;
        bArr2[i7] = b;
        if (b == 16) {
            bArr2[i8] = 16;
            i8++;
        }
        return transOverBle.send(bArr2, i8);
    }

    private int AsynWaitFrame(TransOverBle transOverBle, int i, byte[] bArr, int i2) {
        while (true) {
            byte[] receive = transOverBle.receive(1, i2);
            if (receive == null) {
                return -1002;
            }
            if (receive != null && receive[0] == 16) {
                byte[] receive2 = transOverBle.receive(1, i2);
                if (receive2 == null) {
                    return -1002;
                }
                if (receive2 != null && receive2[0] == 2) {
                    int i3 = 0;
                    int i4 = 0;
                    byte b = 0;
                    while (true) {
                        byte[] receive3 = transOverBle.receive(1, i2);
                        if (receive3 == null) {
                            return -1;
                        }
                        byte b2 = receive3[0];
                        if (b2 == 16) {
                            byte[] receive4 = transOverBle.receive(1, i2);
                            if (receive4 == null) {
                                return -1;
                            }
                            b2 = receive4[0];
                            if (b2 == 3) {
                                byte[] receive5 = transOverBle.receive(1, i2);
                                if (receive5 == null) {
                                    return -1;
                                }
                                byte b3 = receive5[0];
                                if (b3 == 16) {
                                    byte[] receive6 = transOverBle.receive(1, i2);
                                    if (receive6 == null) {
                                        return -1;
                                    }
                                    b3 = receive6[0];
                                    if (b3 != 16) {
                                        return -1004;
                                    }
                                }
                                if (b3 == b) {
                                    return i4;
                                }
                                return -1005;
                            }
                            if (b2 != 16) {
                                return -1004;
                            }
                        }
                        if (i4 >= i) {
                            return -1006;
                        }
                        b = (byte) (b ^ b2);
                        bArr[i3] = b2;
                        i4++;
                        i3++;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int AsynResetLink(TransOverBle transOverBle) {
        byte[] bArr = {-37, -34};
        int send = transOverBle.send(bArr, bArr.length);
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] bArr2 = this.txFrame;
        bArr2[0] = 0;
        bArr2[1] = 1;
        bArr2[2] = 1;
        if (send == 0) {
            send = AsynSendFrame(transOverBle, 3, this.txFrame);
        }
        return (send == 0 ? AsynWaitFrame(transOverBle, this.rxFrame.length, this.rxFrame, 5000) : 0) == 3 ? 0 : -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int ble_transaction(TransOverBle transOverBle, int i, byte[] bArr, int i2, byte[] bArr2, int i3) {
        int AsynWaitFrame;
        int i4;
        int i5;
        int i6 = 2;
        if (i > this.txFrame.length - 2) {
            return Constant.INVALIDPARAMETERERROR;
        }
        int i7 = i + 2;
        byte[] bArr3 = this.txFrame;
        byte b = (byte) (((byte) ((i >> 8) | 16)) | (this.frameCount << 6));
        this.frameCount++;
        int i8 = 0;
        bArr3[0] = b;
        bArr3[1] = (byte) i;
        int i9 = 2;
        int i10 = 0;
        while (i != 0) {
            bArr3[i9] = bArr[i10];
            i--;
            i9++;
            i10++;
        }
        this.repeats = 0;
        while (this.repeats < 3) {
            transOverBle.flush();
            int AsynSendFrame = AsynSendFrame(transOverBle, i7, this.txFrame);
            this.repeats++;
            if (AsynSendFrame == 0 && (AsynWaitFrame = AsynWaitFrame(transOverBle, this.rxFrame.length, this.rxFrame, i3)) > 0 && AsynWaitFrame - 2 == (i5 = ((this.rxFrame[0] & 7) * 256) + this.rxFrame[1])) {
                if (i4 > i2) {
                    return -1204;
                }
                while (i4 != 0) {
                    bArr2[i8] = this.rxFrame[i6];
                    i4--;
                    i8++;
                    i6++;
                }
                return i5;
            }
        }
        return Constant.NORESPONSEERROR;
    }
}
