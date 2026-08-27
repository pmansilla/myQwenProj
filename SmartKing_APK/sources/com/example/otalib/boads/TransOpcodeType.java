package com.example.otalib.boads;

/* loaded from: classes.dex */
public class TransOpcodeType {
    public static final byte Erase = 0;
    public static final byte Read_Flash = 2;
    public static final byte Read_ID = 1;
    public static final byte Reset_MCU = 5;
    public static final byte Set_Key = 4;
    public static final byte Transfer_Image = 6;
    public static final byte Write_Flash = 3;
    public byte opcode;

    public TransOpcodeType(byte b) {
        this.opcode = b;
    }
}
