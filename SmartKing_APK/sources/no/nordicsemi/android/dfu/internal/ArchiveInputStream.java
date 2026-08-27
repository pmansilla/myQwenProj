package no.nordicsemi.android.dfu.internal;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import no.nordicsemi.android.dfu.internal.manifest.Manifest;
import no.nordicsemi.android.dfu.internal.manifest.ManifestFile;

/* loaded from: classes2.dex */
public class ArchiveInputStream extends ZipInputStream {
    private static final String APPLICATION_BIN = "application.bin";
    private static final String APPLICATION_HEX = "application.hex";
    private static final String APPLICATION_INIT = "application.dat";
    private static final String BOOTLOADER_BIN = "bootloader.bin";
    private static final String BOOTLOADER_HEX = "bootloader.hex";
    private static final String MANIFEST = "manifest.json";
    private static final String SOFTDEVICE_BIN = "softdevice.bin";
    private static final String SOFTDEVICE_HEX = "softdevice.hex";
    private static final String SYSTEM_INIT = "system.dat";
    private static final String TAG = "DfuArchiveInputStream";
    private byte[] applicationBytes;
    private byte[] applicationInitBytes;
    private int applicationSize;
    private byte[] bootloaderBytes;
    private int bootloaderSize;
    private int bytesRead;
    private int bytesReadFromCurrentSource;
    private int bytesReadFromMarkedSource;
    private CRC32 crc32;
    private byte[] currentSource;
    private Map<String, byte[]> entries;
    private Manifest manifest;
    private byte[] markedSource;
    private byte[] softDeviceAndBootloaderBytes;
    private byte[] softDeviceBytes;
    private int softDeviceSize;
    private byte[] systemInitBytes;
    private int type;

    /* JADX WARN: Removed duplicated region for block: B:75:0x028e A[Catch: all -> 0x0296, TRY_ENTER, TryCatch #0 {all -> 0x0296, blocks: (B:3:0x0016, B:5:0x001e, B:8:0x0028, B:10:0x002c, B:12:0x0052, B:13:0x007e, B:16:0x0088, B:18:0x008c, B:20:0x0090, B:22:0x00b6, B:23:0x00c1, B:24:0x00e0, B:26:0x00e1, B:27:0x00e8, B:28:0x00e9, B:31:0x00f3, B:33:0x00f7, B:35:0x011d, B:36:0x0128, B:37:0x0147, B:38:0x0148, B:41:0x0152, B:43:0x0156, B:45:0x015a, B:47:0x015e, B:49:0x0184, B:50:0x0196, B:51:0x01b5, B:52:0x01b6, B:53:0x01bd, B:56:0x0281, B:59:0x01c2, B:60:0x01c9, B:61:0x005d, B:62:0x007c, B:65:0x01cc, B:69:0x020a, B:72:0x0246, B:75:0x028e, B:76:0x0295, B:77:0x0249, B:79:0x0259, B:80:0x0265, B:82:0x0269, B:83:0x020e, B:85:0x021e, B:86:0x022a, B:88:0x022e, B:89:0x01d0, B:91:0x01e0, B:92:0x01ec, B:94:0x01f0), top: B:2:0x0016 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0259 A[Catch: all -> 0x0296, TryCatch #0 {all -> 0x0296, blocks: (B:3:0x0016, B:5:0x001e, B:8:0x0028, B:10:0x002c, B:12:0x0052, B:13:0x007e, B:16:0x0088, B:18:0x008c, B:20:0x0090, B:22:0x00b6, B:23:0x00c1, B:24:0x00e0, B:26:0x00e1, B:27:0x00e8, B:28:0x00e9, B:31:0x00f3, B:33:0x00f7, B:35:0x011d, B:36:0x0128, B:37:0x0147, B:38:0x0148, B:41:0x0152, B:43:0x0156, B:45:0x015a, B:47:0x015e, B:49:0x0184, B:50:0x0196, B:51:0x01b5, B:52:0x01b6, B:53:0x01bd, B:56:0x0281, B:59:0x01c2, B:60:0x01c9, B:61:0x005d, B:62:0x007c, B:65:0x01cc, B:69:0x020a, B:72:0x0246, B:75:0x028e, B:76:0x0295, B:77:0x0249, B:79:0x0259, B:80:0x0265, B:82:0x0269, B:83:0x020e, B:85:0x021e, B:86:0x022a, B:88:0x022e, B:89:0x01d0, B:91:0x01e0, B:92:0x01ec, B:94:0x01f0), top: B:2:0x0016 }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0269 A[Catch: all -> 0x0296, TryCatch #0 {all -> 0x0296, blocks: (B:3:0x0016, B:5:0x001e, B:8:0x0028, B:10:0x002c, B:12:0x0052, B:13:0x007e, B:16:0x0088, B:18:0x008c, B:20:0x0090, B:22:0x00b6, B:23:0x00c1, B:24:0x00e0, B:26:0x00e1, B:27:0x00e8, B:28:0x00e9, B:31:0x00f3, B:33:0x00f7, B:35:0x011d, B:36:0x0128, B:37:0x0147, B:38:0x0148, B:41:0x0152, B:43:0x0156, B:45:0x015a, B:47:0x015e, B:49:0x0184, B:50:0x0196, B:51:0x01b5, B:52:0x01b6, B:53:0x01bd, B:56:0x0281, B:59:0x01c2, B:60:0x01c9, B:61:0x005d, B:62:0x007c, B:65:0x01cc, B:69:0x020a, B:72:0x0246, B:75:0x028e, B:76:0x0295, B:77:0x0249, B:79:0x0259, B:80:0x0265, B:82:0x0269, B:83:0x020e, B:85:0x021e, B:86:0x022a, B:88:0x022e, B:89:0x01d0, B:91:0x01e0, B:92:0x01ec, B:94:0x01f0), top: B:2:0x0016 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x021e A[Catch: all -> 0x0296, TryCatch #0 {all -> 0x0296, blocks: (B:3:0x0016, B:5:0x001e, B:8:0x0028, B:10:0x002c, B:12:0x0052, B:13:0x007e, B:16:0x0088, B:18:0x008c, B:20:0x0090, B:22:0x00b6, B:23:0x00c1, B:24:0x00e0, B:26:0x00e1, B:27:0x00e8, B:28:0x00e9, B:31:0x00f3, B:33:0x00f7, B:35:0x011d, B:36:0x0128, B:37:0x0147, B:38:0x0148, B:41:0x0152, B:43:0x0156, B:45:0x015a, B:47:0x015e, B:49:0x0184, B:50:0x0196, B:51:0x01b5, B:52:0x01b6, B:53:0x01bd, B:56:0x0281, B:59:0x01c2, B:60:0x01c9, B:61:0x005d, B:62:0x007c, B:65:0x01cc, B:69:0x020a, B:72:0x0246, B:75:0x028e, B:76:0x0295, B:77:0x0249, B:79:0x0259, B:80:0x0265, B:82:0x0269, B:83:0x020e, B:85:0x021e, B:86:0x022a, B:88:0x022e, B:89:0x01d0, B:91:0x01e0, B:92:0x01ec, B:94:0x01f0), top: B:2:0x0016 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x022e A[Catch: all -> 0x0296, TryCatch #0 {all -> 0x0296, blocks: (B:3:0x0016, B:5:0x001e, B:8:0x0028, B:10:0x002c, B:12:0x0052, B:13:0x007e, B:16:0x0088, B:18:0x008c, B:20:0x0090, B:22:0x00b6, B:23:0x00c1, B:24:0x00e0, B:26:0x00e1, B:27:0x00e8, B:28:0x00e9, B:31:0x00f3, B:33:0x00f7, B:35:0x011d, B:36:0x0128, B:37:0x0147, B:38:0x0148, B:41:0x0152, B:43:0x0156, B:45:0x015a, B:47:0x015e, B:49:0x0184, B:50:0x0196, B:51:0x01b5, B:52:0x01b6, B:53:0x01bd, B:56:0x0281, B:59:0x01c2, B:60:0x01c9, B:61:0x005d, B:62:0x007c, B:65:0x01cc, B:69:0x020a, B:72:0x0246, B:75:0x028e, B:76:0x0295, B:77:0x0249, B:79:0x0259, B:80:0x0265, B:82:0x0269, B:83:0x020e, B:85:0x021e, B:86:0x022a, B:88:0x022e, B:89:0x01d0, B:91:0x01e0, B:92:0x01ec, B:94:0x01f0), top: B:2:0x0016 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ArchiveInputStream(java.io.InputStream r4, int r5, int r6) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 673
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: no.nordicsemi.android.dfu.internal.ArchiveInputStream.<init>(java.io.InputStream, int, int):void");
    }

    private void parseZip(int i) throws IOException {
        byte[] bArr = new byte[1024];
        String str = null;
        while (true) {
            ZipEntry nextEntry = getNextEntry();
            if (nextEntry == null) {
                break;
            }
            String name = nextEntry.getName();
            if (nextEntry.isDirectory()) {
                Log.w(TAG, "A directory found in the ZIP: " + name + "!");
            } else {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int read = super.read(bArr);
                    if (read == -1) {
                        break;
                    } else {
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                if (name.toLowerCase(Locale.US).endsWith("hex")) {
                    HexInputStream hexInputStream = new HexInputStream(byteArray, i);
                    byteArray = new byte[hexInputStream.available()];
                    hexInputStream.read(byteArray);
                    hexInputStream.close();
                }
                if (MANIFEST.equals(name)) {
                    str = new String(byteArray, "UTF-8");
                } else {
                    this.entries.put(name, byteArray);
                }
            }
        }
        if (this.entries.isEmpty()) {
            throw new FileNotFoundException("No files found in the ZIP. Check if the URI provided is valid and the ZIP contains required files on root level, not in a directory.");
        }
        if (str == null) {
            Log.w(TAG, "Manifest not found in the ZIP. It is recommended to use a distribution file created with: https://github.com/NordicSemiconductor/pc-nrfutil/ (for Legacy DFU use version 0.5.x)");
            return;
        }
        this.manifest = ((ManifestFile) new Gson().fromJson(str, ManifestFile.class)).getManifest();
        if (this.manifest == null) {
            Log.w(TAG, "Manifest failed to be parsed. Did you add \n-keep class no.nordicsemi.android.dfu.** { *; }\nto your proguard rules?");
        }
    }

    private byte[] startNextFile() {
        byte[] bArr;
        if (this.currentSource == this.softDeviceBytes && this.bootloaderBytes != null && (this.type & 2) > 0) {
            bArr = this.bootloaderBytes;
            this.currentSource = bArr;
        } else if (this.currentSource == this.applicationBytes || this.applicationBytes == null || (this.type & 4) <= 0) {
            bArr = null;
            this.currentSource = null;
        } else {
            bArr = this.applicationBytes;
            this.currentSource = bArr;
        }
        this.bytesReadFromCurrentSource = 0;
        return bArr;
    }

    public int applicationImageSize() {
        if ((this.type & 4) > 0) {
            return this.applicationSize;
        }
        return 0;
    }

    @Override // java.util.zip.ZipInputStream, java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public int available() {
        return (this.softDeviceAndBootloaderBytes == null || this.softDeviceSize != 0 || this.bootloaderSize != 0 || (this.type & 3) <= 0) ? ((softDeviceImageSize() + bootloaderImageSize()) + applicationImageSize()) - this.bytesRead : (this.softDeviceAndBootloaderBytes.length + applicationImageSize()) - this.bytesRead;
    }

    public int bootloaderImageSize() {
        if ((this.type & 2) > 0) {
            return this.bootloaderSize;
        }
        return 0;
    }

    @Override // java.util.zip.ZipInputStream, java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.softDeviceBytes = null;
        this.bootloaderBytes = null;
        this.softDeviceBytes = null;
        this.softDeviceAndBootloaderBytes = null;
        this.applicationSize = 0;
        this.bootloaderSize = 0;
        this.softDeviceSize = 0;
        this.currentSource = null;
        this.bytesReadFromCurrentSource = 0;
        this.bytesRead = 0;
        super.close();
    }

    public byte[] getApplicationInit() {
        return this.applicationInitBytes;
    }

    public int getBytesRead() {
        return this.bytesRead;
    }

    public int getContentType() {
        this.type = 0;
        if (this.softDeviceAndBootloaderBytes != null) {
            this.type |= 3;
        }
        if (this.softDeviceSize > 0) {
            this.type |= 1;
        }
        if (this.bootloaderSize > 0) {
            this.type |= 2;
        }
        if (this.applicationSize > 0) {
            this.type |= 4;
        }
        return this.type;
    }

    public long getCrc32() {
        return this.crc32.getValue();
    }

    public byte[] getSystemInit() {
        return this.systemInitBytes;
    }

    public boolean isSecureDfuRequired() {
        return this.manifest != null && this.manifest.isSecureDfuRequired();
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public void mark(int i) {
        this.markedSource = this.currentSource;
        this.bytesReadFromMarkedSource = this.bytesReadFromCurrentSource;
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(@NonNull byte[] bArr) throws IOException {
        int length = this.currentSource.length - this.bytesReadFromCurrentSource;
        if (bArr.length <= length) {
            length = bArr.length;
        }
        System.arraycopy(this.currentSource, this.bytesReadFromCurrentSource, bArr, 0, length);
        this.bytesReadFromCurrentSource += length;
        if (bArr.length > length) {
            if (startNextFile() == null) {
                this.bytesRead += length;
                this.crc32.update(bArr, 0, length);
                return length;
            }
            int length2 = this.currentSource.length;
            if (bArr.length - length <= length2) {
                length2 = bArr.length - length;
            }
            System.arraycopy(this.currentSource, 0, bArr, length, length2);
            this.bytesReadFromCurrentSource += length2;
            length += length2;
        }
        this.bytesRead += length;
        this.crc32.update(bArr, 0, length);
        return length;
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        this.currentSource = this.markedSource;
        int i = this.bytesReadFromMarkedSource;
        this.bytesReadFromCurrentSource = i;
        this.bytesRead = i;
        this.crc32.reset();
        if (this.currentSource == this.bootloaderBytes && this.softDeviceBytes != null) {
            this.crc32.update(this.softDeviceBytes);
            this.bytesRead += this.softDeviceSize;
        }
        this.crc32.update(this.currentSource, 0, this.bytesReadFromCurrentSource);
    }

    public int setContentType(int i) {
        this.type = i;
        int i2 = i & 4;
        if (i2 > 0 && this.applicationBytes == null) {
            this.type &= -5;
        }
        int i3 = i & 3;
        if (i3 == 3) {
            if (this.softDeviceBytes == null && this.softDeviceAndBootloaderBytes == null) {
                this.type &= -2;
            }
            if (this.bootloaderBytes == null && this.softDeviceAndBootloaderBytes == null) {
                this.type &= -2;
            }
        } else if (this.softDeviceAndBootloaderBytes != null) {
            this.type &= -4;
        }
        if (i3 > 0 && this.softDeviceAndBootloaderBytes != null) {
            this.currentSource = this.softDeviceAndBootloaderBytes;
        } else if ((i & 1) > 0) {
            this.currentSource = this.softDeviceBytes;
        } else if ((i & 2) > 0) {
            this.currentSource = this.bootloaderBytes;
        } else if (i2 > 0) {
            this.currentSource = this.applicationBytes;
        }
        this.bytesReadFromCurrentSource = 0;
        try {
            mark(0);
            reset();
        } catch (IOException unused) {
        }
        return this.type;
    }

    public int softDeviceImageSize() {
        if ((this.type & 1) > 0) {
            return this.softDeviceSize;
        }
        return 0;
    }
}
