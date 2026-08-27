package org.apache.harmony.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class DataSnapshot implements DataProvider {
    private final String[] fileList;
    private final String html;
    private final String[] nativeFormats;
    private final RawBitmap rawBitmap;
    private final Map<Class<?>, byte[]> serializedObjects = Collections.synchronizedMap(new HashMap());
    private final String text;
    private final String url;

    public DataSnapshot(DataProvider dataProvider) {
        Class<?> representationClass;
        byte[] serializedObject;
        this.nativeFormats = dataProvider.getNativeFormats();
        this.text = dataProvider.getText();
        this.fileList = dataProvider.getFileList();
        this.url = dataProvider.getURL();
        this.html = dataProvider.getHTML();
        this.rawBitmap = dataProvider.getRawBitmap();
        for (int i = 0; i < this.nativeFormats.length; i++) {
            DataFlavor dataFlavor = null;
            try {
                dataFlavor = SystemFlavorMap.decodeDataFlavor(this.nativeFormats[i]);
            } catch (ClassNotFoundException unused) {
            }
            if (dataFlavor != null && (serializedObject = dataProvider.getSerializedObject((representationClass = dataFlavor.getRepresentationClass()))) != null) {
                this.serializedObjects.put(representationClass, serializedObject);
            }
        }
    }

    @Override // org.apache.harmony.awt.datatransfer.DataProvider
    public String[] getFileList() {
        return this.fileList;
    }

    @Override // org.apache.harmony.awt.datatransfer.DataProvider
    public String getHTML() {
        return this.html;
    }

    @Override // org.apache.harmony.awt.datatransfer.DataProvider
    public String[] getNativeFormats() {
        return this.nativeFormats;
    }

    @Override // org.apache.harmony.awt.datatransfer.DataProvider
    public RawBitmap getRawBitmap() {
        return this.rawBitmap;
    }

    public short[] getRawBitmapBuffer16() {
        if (this.rawBitmap == null || !(this.rawBitmap.buffer instanceof short[])) {
            return null;
        }
        return (short[]) this.rawBitmap.buffer;
    }

    public int[] getRawBitmapBuffer32() {
        if (this.rawBitmap == null || !(this.rawBitmap.buffer instanceof int[])) {
            return null;
        }
        return (int[]) this.rawBitmap.buffer;
    }

    public byte[] getRawBitmapBuffer8() {
        if (this.rawBitmap == null || !(this.rawBitmap.buffer instanceof byte[])) {
            return null;
        }
        return (byte[]) this.rawBitmap.buffer;
    }

    public int[] getRawBitmapHeader() {
        if (this.rawBitmap != null) {
            return this.rawBitmap.getHeader();
        }
        return null;
    }

    @Override // org.apache.harmony.awt.datatransfer.DataProvider
    public byte[] getSerializedObject(Class<?> cls) {
        return this.serializedObjects.get(cls);
    }

    public byte[] getSerializedObject(String str) {
        try {
            return getSerializedObject(SystemFlavorMap.decodeDataFlavor(str).getRepresentationClass());
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // org.apache.harmony.awt.datatransfer.DataProvider
    public String getText() {
        return this.text;
    }

    @Override // org.apache.harmony.awt.datatransfer.DataProvider
    public String getURL() {
        return this.url;
    }

    @Override // org.apache.harmony.awt.datatransfer.DataProvider
    public boolean isNativeFormatAvailable(String str) {
        if (str == null) {
            return false;
        }
        if (str.equals("text/plain")) {
            return this.text != null;
        }
        if (str.equals("application/x-java-file-list")) {
            return this.fileList != null;
        }
        if (str.equals("application/x-java-url")) {
            return this.url != null;
        }
        if (str.equals("text/html")) {
            return this.html != null;
        }
        if (str.equals("image/x-java-image")) {
            return this.rawBitmap != null;
        }
        try {
            return this.serializedObjects.containsKey(SystemFlavorMap.decodeDataFlavor(str).getRepresentationClass());
        } catch (Exception unused) {
            return false;
        }
    }
}
