package me.panpf.sketch.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import me.panpf.sketch.util.ObjectPool;

/* loaded from: classes2.dex */
public class SketchMD5Utils {
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static ObjectPool<MessageDigest> digestObjectPool = new ObjectPool<>(new ObjectPool.ObjectFactory<MessageDigest>() { // from class: me.panpf.sketch.util.SketchMD5Utils.1
        @Override // me.panpf.sketch.util.ObjectPool.ObjectFactory
        public MessageDigest newObject() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
    }, 3);

    private static void appendHexPair(byte b, StringBuffer stringBuffer) {
        char c = hexDigits[(b & 240) >> 4];
        char c2 = hexDigits[b & 15];
        stringBuffer.append(c);
        stringBuffer.append(c2);
    }

    private static String bufferToHex(byte[] bArr, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer(i2 * 2);
        int i3 = i2 + i;
        while (i < i3) {
            appendHexPair(bArr[i], stringBuffer);
            i++;
        }
        return stringBuffer.toString();
    }

    public static String md5(File file) throws IOException {
        FileInputStream fileInputStream;
        MessageDigest messageDigest = digestObjectPool.get();
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[8192];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read == -1) {
                        byte[] digest = messageDigest.digest();
                        String bufferToHex = bufferToHex(digest, 0, digest.length);
                        SketchUtils.close(fileInputStream);
                        messageDigest.reset();
                        digestObjectPool.put(messageDigest);
                        return bufferToHex;
                    }
                    messageDigest.update(bArr, 0, read);
                }
            } catch (Throwable th) {
                th = th;
                SketchUtils.close(fileInputStream);
                messageDigest.reset();
                digestObjectPool.put(messageDigest);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
        }
    }

    public static String md5(String str) {
        MessageDigest messageDigest = digestObjectPool.get();
        messageDigest.update(str.getBytes());
        byte[] digest = messageDigest.digest();
        char[] cArr = new char[digest.length * 2];
        int i = 0;
        for (byte b : digest) {
            int i2 = i + 1;
            cArr[i] = hexDigits[(b >>> 4) & 15];
            i = i2 + 1;
            cArr[i2] = hexDigits[b & 15];
        }
        String str2 = new String(cArr);
        messageDigest.reset();
        digestObjectPool.put(messageDigest);
        return str2;
    }
}
