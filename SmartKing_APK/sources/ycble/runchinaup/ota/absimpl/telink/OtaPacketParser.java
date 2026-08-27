package ycble.runchinaup.ota.absimpl.telink;

/* loaded from: classes2.dex */
class OtaPacketParser {
    private byte[] data;
    private int index = -1;
    private int progress;
    private int total;

    public void clear() {
        this.progress = 0;
        this.total = 0;
        this.index = -1;
        this.data = null;
    }

    public int crc16(byte[] bArr) {
        int length = bArr.length - 2;
        short[] sArr = {0, -24575};
        int i = 0;
        int i2 = 65535;
        while (i < length) {
            int i3 = bArr[i];
            int i4 = i2;
            for (int i5 = 0; i5 < 8; i5++) {
                i4 = (sArr[(i4 ^ i3) & 1] & 65535) ^ (i4 >> 1);
                i3 >>= 1;
            }
            i++;
            i2 = i4;
        }
        return i2;
    }

    public void fillCrc(byte[] bArr, int i) {
        int length = bArr.length - 2;
        bArr[length] = (byte) (i & 255);
        bArr[length + 1] = (byte) ((i >> 8) & 255);
    }

    public void fillIndex(byte[] bArr, int i) {
        bArr[0] = (byte) (i & 255);
        bArr[1] = (byte) ((i >> 8) & 255);
    }

    public byte[] getCheckPacket() {
        byte[] bArr = new byte[16];
        for (int i = 0; i < 16; i++) {
            bArr[i] = -1;
        }
        fillIndex(bArr, getNextPacketIndex());
        fillCrc(bArr, crc16(bArr));
        return bArr;
    }

    public int getCurIndex() {
        return this.index;
    }

    public byte[] getNextPacket() {
        int nextPacketIndex = getNextPacketIndex();
        byte[] packet = getPacket(nextPacketIndex);
        this.index = nextPacketIndex;
        return packet;
    }

    public int getNextPacketIndex() {
        return this.index + 1;
    }

    public byte[] getPacket(int i) {
        int length = this.data.length;
        if (length > 16) {
            length = i + 1 == this.total ? length - (i * 16) : 16;
        }
        int i2 = length + 4;
        byte[] bArr = new byte[20];
        for (int i3 = 0; i3 < 20; i3++) {
            bArr[i3] = -1;
        }
        System.arraycopy(this.data, i * 16, bArr, 2, i2 - 4);
        fillIndex(bArr, i);
        fillCrc(bArr, crc16(bArr));
        return bArr;
    }

    public int getProgress() {
        return this.progress;
    }

    public boolean hasNextPacket() {
        return this.total > 0 && this.index + 1 < this.total;
    }

    public boolean invalidateProgress() {
        int floor = (int) Math.floor((getNextPacketIndex() / this.total) * 100.0f);
        if (floor == this.progress) {
            return false;
        }
        this.progress = floor;
        return true;
    }

    public boolean isLast() {
        return this.index + 1 == this.total;
    }

    public void set(byte[] bArr) {
        clear();
        this.data = bArr;
        int length = this.data.length;
        if (length % 16 == 0) {
            this.total = length / 16;
        } else {
            this.total = (int) Math.floor((length / 16) + 1);
        }
    }
}
