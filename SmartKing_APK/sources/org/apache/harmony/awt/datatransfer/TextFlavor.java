package org.apache.harmony.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/* loaded from: classes2.dex */
public class TextFlavor {
    public static final Class[] unicodeTextClasses = {String.class, Reader.class, CharBuffer.class, char[].class};
    public static final Class[] charsetTextClasses = {InputStream.class, ByteBuffer.class, byte[].class};

    public static void addCharsetClasses(SystemFlavorMap systemFlavorMap, String str, String str2, String str3) {
        for (int i = 0; i < charsetTextClasses.length; i++) {
            String str4 = "text/" + str2;
            DataFlavor dataFlavor = new DataFlavor(String.valueOf(str4) + (";class=\"" + charsetTextClasses[i].getName() + "\";charset=\"" + str3 + "\""), str4);
            systemFlavorMap.addFlavorForUnencodedNative(str, dataFlavor);
            systemFlavorMap.addUnencodedNativeForFlavor(dataFlavor, str);
        }
    }

    public static void addUnicodeClasses(SystemFlavorMap systemFlavorMap, String str, String str2) {
        for (int i = 0; i < unicodeTextClasses.length; i++) {
            String str3 = "text/" + str2;
            DataFlavor dataFlavor = new DataFlavor(String.valueOf(str3) + (";class=\"" + unicodeTextClasses[i].getName() + "\""), str3);
            systemFlavorMap.addFlavorForUnencodedNative(str, dataFlavor);
            systemFlavorMap.addUnencodedNativeForFlavor(dataFlavor, str);
        }
    }
}
