package com.mob.mobapm.e;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* loaded from: classes.dex */
public class d {
    public static ByteArrayOutputStream a(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= -1) {
                    byteArrayOutputStream.flush();
                    return byteArrayOutputStream;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (IOException unused) {
            return null;
        }
    }

    public static String b(InputStream inputStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return stringBuffer.toString();
                }
                stringBuffer.append(readLine);
            }
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: input stream content error:" + th, new Object[0]);
            return null;
        }
    }
}
