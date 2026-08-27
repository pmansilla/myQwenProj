package com.example.otalib.boads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;

/* loaded from: classes.dex */
public class JavaXmodem {
    private static final int ACK = 6;
    private static final int CAN = 24;
    private static final int CTRLZ = 26;
    private static final int EOT = 4;
    private static final int NAK = 21;
    private static final int SOH = 1;
    private static final int SOH_BUF_SZ = 128;
    private static final int STX = 2;
    private final int MAXRETRANS = 10;
    private boolean TRANSMIT_XMODEM_1K = true;

    private int check(int i, byte[] bArr, int i2, int i3) {
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i2, bArr2, 0, i3);
        if (i != 0) {
            byte[] crc16_ccitt = LoaderCrc.crc16_ccitt(bArr2, 0, i3);
            int i4 = i3 + i2;
            if (((crc16_ccitt[1] << 8) | crc16_ccitt[0]) == (bArr[i4 + 1] | (bArr[i4] << 8))) {
                return 1;
            }
        } else {
            byte b = 0;
            for (int i5 = 0; i5 < i3; i5++) {
                b = (byte) (b + bArr2[i5]);
            }
            if (b == bArr2[i3 + i2]) {
                return 1;
            }
        }
        return 0;
    }

    private void memset(byte[] bArr, int i, byte b, int i2) {
        if (bArr.length < i + i2) {
            return;
        }
        while (i < i2) {
            bArr[i] = 0;
            i++;
        }
    }

    public byte[] xmodem_receive(int i, TransOverBle transOverBle) {
        char c;
        byte[] bArr = new byte[i];
        byte[] bArr2 = new byte[GLMapStaticValue.MAP_PARAMETERNAME_SCENIC];
        int i2 = 1;
        byte[] bArr3 = new byte[1];
        byte[] bArr4 = {67};
        char c2 = 'C';
        byte b = 0;
        int i3 = 0;
        int i4 = 0;
        byte b2 = 1;
        int i5 = 0;
        int i6 = 10;
        while (true) {
            byte b3 = b;
            int i7 = i3;
            int i8 = 0;
            boolean z = false;
            while (i8 < 16) {
                if (c2 != 0) {
                    transOverBle.send(bArr4, i2);
                }
                byte[] receive = transOverBle.receive(i2);
                if (receive != null && (b3 = receive[0]) >= 0) {
                    int i9 = b3 & 255;
                    if (i9 == 4) {
                        transOverBle.flush();
                        bArr3[0] = 6;
                        transOverBle.send(bArr3, 1);
                        return bArr;
                    }
                    if (i9 != 24) {
                        switch (i9) {
                            case 1:
                                i7 = 128;
                                break;
                            case 2:
                                i7 = 1024;
                                break;
                            default:
                                i2 = 1;
                                break;
                        }
                        i2 = 1;
                        i8 = 16;
                        z = true;
                    } else {
                        i2 = 1;
                        byte[] receive2 = transOverBle.receive(1);
                        if (receive2 != null && (b3 = receive2[0]) > 0 && b3 == 24) {
                            transOverBle.flush();
                            bArr3[0] = 6;
                            transOverBle.send(bArr3, 1);
                            return null;
                        }
                    }
                }
                i8 += i2;
            }
            if (z) {
                if (c2 == 'C') {
                    i4 = 1;
                }
                bArr2[0] = b3;
                i3 = i7;
                int i10 = i3 + i4 + 3;
                byte[] receive3 = transOverBle.receive(i10);
                if (!(receive3 == null)) {
                    System.arraycopy(receive3, 0, bArr2, 1, i10);
                    if (bArr2[1] == (bArr2[2] ^ (-1)) && (bArr2[1] == b2 || bArr2[1] == b2 - 1)) {
                        if (check(i4, bArr2, 3, i3) != 0) {
                            if (bArr2[1] == b2) {
                                int i11 = i - i5;
                                if (i11 > i3) {
                                    i11 = i3;
                                }
                                if (i11 > 0) {
                                    System.arraycopy(bArr2, 3, bArr, i5, i11);
                                    i5 += i11;
                                }
                                i6 = 11;
                                b2 = (byte) (b2 + 1);
                            }
                            i6--;
                            if (i6 <= 0) {
                                transOverBle.flush();
                                bArr3[0] = 24;
                                transOverBle.send(bArr3, 1);
                                transOverBle.send(bArr3, 1);
                                transOverBle.send(bArr3, 1);
                                return null;
                            }
                            i2 = 1;
                            bArr3[0] = 6;
                            transOverBle.send(bArr3, 1);
                            b = b3;
                            c2 = 0;
                        } else {
                            i2 = 1;
                            c = 0;
                            transOverBle.flush();
                            bArr3[c] = 21;
                            transOverBle.send(bArr3, i2);
                            b = b3;
                            c2 = 0;
                        }
                    }
                }
                i2 = 1;
                c = 0;
                transOverBle.flush();
                bArr3[c] = 21;
                transOverBle.send(bArr3, i2);
                b = b3;
                c2 = 0;
            } else {
                if (c2 != 'C') {
                    transOverBle.flush();
                    bArr3[0] = 24;
                    transOverBle.send(bArr3, i2);
                    transOverBle.send(bArr3, i2);
                    transOverBle.send(bArr3, i2);
                    return null;
                }
                b = b3;
                i3 = i7;
                c2 = 21;
            }
        }
    }

    public int xmodem_transmit(byte[] bArr, int i, TransOverBle transOverBle) {
        int i2;
        byte b;
        int i3;
        int i4;
        byte[] receive;
        new Message();
        byte[] bArr2 = new byte[GLMapStaticValue.MAP_PARAMETERNAME_SCENIC];
        byte b2 = 1;
        byte[] bArr3 = new byte[1];
        boolean z = this.TRANSMIT_XMODEM_1K;
        byte b3 = 0;
        int i5 = 0;
        boolean z2 = false;
        byte b4 = 0;
        char c = 65535;
        while (i5 < 16) {
            byte[] receive2 = transOverBle.receive(1);
            if (receive2 != null && (b4 = receive2[0]) >= 0) {
                int i6 = b4 & 255;
                if (i6 != 21) {
                    if (i6 == 24) {
                        byte[] receive3 = transOverBle.receive(1);
                        if (receive3 != null) {
                            byte b5 = receive3[0];
                            if (b5 == 24) {
                                bArr3[0] = 6;
                                transOverBle.send(bArr3, 1);
                                transOverBle.flush();
                                return -1;
                            }
                            b4 = b5;
                        }
                    } else if (i6 == 67) {
                        i5 = 16;
                        z2 = true;
                        c = 1;
                    }
                    z2 = false;
                } else {
                    i5 = 16;
                    z2 = true;
                    c = 0;
                }
            }
            i5++;
        }
        if (!z2) {
            bArr3[0] = 24;
            transOverBle.send(bArr3, 1);
            transOverBle.send(bArr3, 1);
            transOverBle.send(bArr3, 1);
            transOverBle.flush();
            return -2;
        }
        boolean z3 = z2;
        byte b6 = b4;
        byte b7 = 1;
        int i7 = 0;
        int i8 = 0;
        while (true) {
            if (this.TRANSMIT_XMODEM_1K) {
                bArr2[b3] = 2;
                i2 = 1024;
            } else {
                bArr2[b3] = b2;
                i2 = 128;
            }
            bArr2[b2] = b7;
            bArr2[2] = (byte) (b7 ^ (-1));
            int i9 = i - i7;
            if (i9 > i2) {
                i9 = i2;
            }
            if (i9 <= 0) {
                int i10 = 0;
                while (true) {
                    if (i10 >= 10) {
                        b = 6;
                        break;
                    }
                    bArr3[0] = 4;
                    transOverBle.send(bArr3, 1);
                    byte[] receive4 = transOverBle.receive(1);
                    if (receive4 != null && (b6 = receive4[0]) > 0) {
                        b = 6;
                        if (b6 == 6) {
                            break;
                        }
                    }
                    i10++;
                }
                transOverBle.flush();
                if (b6 == b) {
                    return i7;
                }
                return -5;
            }
            int i11 = 3;
            memset(bArr2, 3, b3, i9);
            System.arraycopy(bArr, i8, bArr2, 3, i9);
            i8 += i9;
            if (i9 < i2) {
                bArr2[i9 + 3] = 26;
            }
            if (c != 0) {
                byte[] crc16_ccitt = LoaderCrc.crc16_ccitt(bArr2, 3, i2);
                bArr2[i2 + 3] = crc16_ccitt[b2];
                bArr2[i2 + 4] = crc16_ccitt[0];
            } else {
                byte b8 = 0;
                while (true) {
                    i3 = i2 + 3;
                    if (i11 >= i3) {
                        break;
                    }
                    b8 = (byte) (b8 + bArr2[i11]);
                    i11++;
                }
                bArr2[i3] = b8;
            }
            byte b9 = b7;
            int i12 = 0;
            while (i12 < 10) {
                try {
                    transOverBle.m_recv_cnt.acquire(transOverBle.m_recv_data.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                transOverBle.m_recv_data.clear();
                transOverBle.send(bArr2, i2 + 4 + (c != 0 ? 1 : 0));
                byte[] receive5 = transOverBle.receive(1);
                if (receive5 == null || (b6 = receive5[0]) < 0) {
                    i4 = 1;
                } else {
                    int i13 = b6 & 255;
                    if (i13 == 6) {
                        b9 = (byte) (b9 + 1);
                        i7 += i2;
                        i12 = 10;
                        i4 = 1;
                        z3 = true;
                    } else {
                        if (i13 == 24 && (receive = transOverBle.receive(1)) != null && (b6 = receive[0]) > 0 && b6 == 24) {
                            bArr3[0] = 6;
                            transOverBle.send(bArr3, 1);
                            transOverBle.flush();
                            return -1;
                        }
                        i4 = 1;
                        z3 = false;
                    }
                }
                i12 += i4;
            }
            if (!z3) {
                bArr3[0] = 24;
                transOverBle.send(bArr3, 1);
                transOverBle.send(bArr3, 1);
                transOverBle.send(bArr3, 1);
                transOverBle.flush();
                return -4;
            }
            b7 = b9;
            b2 = 1;
            b3 = 0;
        }
    }

    public int xmodem_transmit(byte[] bArr, int i, TransOverBle transOverBle, Handler handler) {
        int i2;
        byte b;
        byte b2;
        int i3;
        int i4;
        byte[] receive;
        byte[] bArr2 = new byte[GLMapStaticValue.MAP_PARAMETERNAME_SCENIC];
        byte b3 = 1;
        byte[] bArr3 = new byte[1];
        char c = 0;
        if (this.TRANSMIT_XMODEM_1K) {
            Message message = new Message();
            message.arg1 = 1002;
            message.arg2 = (i / 1024) + (i % 1024 == 0 ? 0 : 1);
            handler.sendMessage(message);
        } else {
            Message message2 = new Message();
            message2.arg1 = 1002;
            message2.arg2 = (i / 128) + (i % 128 == 0 ? 0 : 1);
            handler.sendMessage(message2);
        }
        int i5 = 0;
        boolean z = false;
        byte b4 = 0;
        char c2 = 65535;
        while (i5 < 16) {
            byte[] receive2 = transOverBle.receive(1);
            if (receive2 != null && (b4 = receive2[0]) >= 0) {
                int i6 = b4 & 255;
                if (i6 != 21) {
                    if (i6 == 24) {
                        byte[] receive3 = transOverBle.receive(1);
                        if (receive3 != null) {
                            byte b5 = receive3[0];
                            if (b5 == 24) {
                                bArr3[0] = 6;
                                transOverBle.send(bArr3, 1);
                                transOverBle.flush();
                                return -1;
                            }
                            b4 = b5;
                        }
                    } else if (i6 == 67) {
                        i5 = 16;
                        z = true;
                        c2 = 1;
                    }
                    z = false;
                } else {
                    i5 = 16;
                    z = true;
                    c2 = 0;
                }
            }
            i5++;
        }
        if (!z) {
            bArr3[0] = 24;
            transOverBle.send(bArr3, 1);
            transOverBle.send(bArr3, 1);
            transOverBle.send(bArr3, 1);
            transOverBle.flush();
            return -2;
        }
        boolean z2 = z;
        byte b6 = b4;
        byte b7 = 1;
        int i7 = 0;
        int i8 = 1;
        int i9 = 0;
        while (true) {
            if (this.TRANSMIT_XMODEM_1K) {
                bArr2[c] = 2;
                i2 = 1024;
            } else {
                bArr2[c] = b3;
                i2 = 128;
            }
            bArr2[b3] = b7;
            bArr2[2] = (byte) (b7 ^ (-1));
            int i10 = i - i7;
            Message message3 = new Message();
            message3.arg1 = 1003;
            message3.arg2 = i8;
            StringBuilder sb = new StringBuilder();
            byte b8 = b7;
            sb.append("num:");
            sb.append(i8);
            Log.i("JavaXmodem", sb.toString());
            handler.sendMessage(message3);
            if (i10 > i2) {
                i10 = i2;
            }
            if (i10 <= 0) {
                int i11 = 0;
                while (true) {
                    if (i11 >= 10) {
                        b = 6;
                        b2 = b6;
                        break;
                    }
                    Log.i("JavaXmodem", "send EOT");
                    bArr3[0] = 4;
                    transOverBle.send(bArr3, 1);
                    byte[] receive4 = transOverBle.receive(1);
                    if (receive4 != null) {
                        b2 = receive4[0];
                        if (b2 > 0) {
                            b = 6;
                            if (b2 == 6) {
                                break;
                            }
                        }
                        b6 = b2;
                    }
                    i11++;
                }
                transOverBle.flush();
                if (b2 == b) {
                    return i7;
                }
                return -5;
            }
            int i12 = 3;
            memset(bArr2, 3, (byte) 0, i10);
            System.arraycopy(bArr, i9, bArr2, 3, i10);
            i9 += i10;
            if (i10 < i2) {
                bArr2[i10 + 3] = 26;
            }
            if (c2 != 0) {
                byte[] crc16_ccitt = LoaderCrc.crc16_ccitt(bArr2, 3, i2);
                bArr2[i2 + 3] = crc16_ccitt[1];
                bArr2[i2 + 4] = crc16_ccitt[0];
            } else {
                byte b9 = 0;
                while (true) {
                    i3 = i2 + 3;
                    if (i12 >= i3) {
                        break;
                    }
                    b9 = (byte) (b9 + bArr2[i12]);
                    i12++;
                }
                bArr2[i3] = b9;
            }
            int i13 = i8;
            int i14 = i7;
            int i15 = 0;
            for (int i16 = 10; i15 < i16; i16 = 10) {
                try {
                    transOverBle.m_recv_cnt.acquire(transOverBle.m_recv_data.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                transOverBle.m_recv_data.clear();
                transOverBle.send(bArr2, i2 + 4 + (c2 != 0 ? 1 : 0));
                byte[] receive5 = transOverBle.receive(1);
                if (receive5 != null) {
                    byte b10 = receive5[0];
                    if (b10 >= 0) {
                        int i17 = b10 & 255;
                        if (i17 != 6) {
                            if (i17 == 21) {
                                Log.i("JavaXmodem", "recv NAK");
                            } else if (i17 == 24 && (receive = transOverBle.receive(1)) != null) {
                                b10 = receive[0];
                                if (b10 > 0 && b10 == 24) {
                                    bArr3[0] = 6;
                                    transOverBle.send(bArr3, 1);
                                    transOverBle.flush();
                                    return -1;
                                }
                            }
                            b6 = b10;
                            i4 = 1;
                            z2 = false;
                        } else {
                            i13++;
                            i14 += i2;
                            b6 = b10;
                            b8 = (byte) (b8 + 1);
                            i4 = 1;
                            i15 = 10;
                            z2 = true;
                        }
                        i15 += i4;
                    } else {
                        b6 = b10;
                    }
                }
                i4 = 1;
                i15 += i4;
            }
            b3 = 1;
            if (!z2) {
                bArr3[0] = 24;
                transOverBle.send(bArr3, 1);
                transOverBle.send(bArr3, 1);
                transOverBle.send(bArr3, 1);
                transOverBle.flush();
                return -4;
            }
            i7 = i14;
            i8 = i13;
            b7 = b8;
            c = 0;
        }
    }
}
