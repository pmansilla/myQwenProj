package cn.smssdk.net;

import android.util.Base64;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* compiled from: Crypto.java */
/* loaded from: classes.dex */
public class c {
    private static String a;
    private static String b;
    private static int c;

    static {
        new Hashon();
        a = "fa3acdf1b118fc26668bf72a70d60aa024a2667254c5f0bb8f082bc384b38a4e6d3d1b672467a19793c8f770c63f48b409e87f5787371789af40b95eae9867b9";
        b = "1ef570e1013109c50df8f8c2015faed71e4cf7c53ca9195a99c574ca046aeefdf70bc5fd69f04b0eadf63398698f776cf1ef0db5134efddc3aa4825b69aee94b55356a15d2a50a325ef7bd2d9efe15f3ac5d2303e0bdf5147b3d0fb5fa4fd1d5ea07fe1b45912ff9d7fe472136ff49cb1176f039219bc737ec7ccad132a5ce57";
        c = 1024;
    }

    public static Object a(String str, String str2) throws Throwable {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(new String(Data.AES128Decode(Data.rawMD5(str.getBytes()), Base64.decode(str2, 2)), "UTF-8").trim(), 2)));
        String str3 = (String) objectInputStream.readObject();
        objectInputStream.close();
        return str3;
    }

    public static String a(String str) throws Throwable {
        byte[] AES128Decode = Data.AES128Decode(Data.rawMD5("sms.mob.com.sdk.2.0.0".getBytes()), c(str));
        if (AES128Decode == null) {
            return null;
        }
        return new String(AES128Decode, "UTF-8").trim();
    }

    public static String a(String str, Object obj) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return Base64.encodeToString(Data.AES128Encode(Data.rawMD5(str.getBytes()), Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2).getBytes()), 2);
    }

    public static void a(String str, String str2, int i) {
        a = str;
        b = str2;
        c = i;
    }

    public static Object[] a() {
        return new Object[]{a, b, Integer.valueOf(c)};
    }

    public static String b(String str) throws Throwable {
        return Data.byteToHex(Data.AES128Encode(Data.rawMD5("sms.mob.com.sdk.2.0.0".getBytes()), str.getBytes()));
    }

    public static byte[] c(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length % 2 == 1) {
            return null;
        }
        int i = length / 2;
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = i2 * 2;
            try {
                bArr[i2] = (byte) Integer.parseInt(str.substring(i3, i3 + 2), 16);
            } catch (Throwable unused) {
                return null;
            }
        }
        return bArr;
    }
}
