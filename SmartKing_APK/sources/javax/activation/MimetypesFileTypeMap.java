package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MimeTypeFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/* loaded from: classes2.dex */
public class MimetypesFileTypeMap extends FileTypeMap {
    private static final int PROG = 0;
    private static MimeTypeFile defDB = null;
    private static String defaultType = "application/octet-stream";
    private MimeTypeFile[] DB;

    public MimetypesFileTypeMap() {
        Vector vector = new Vector(5);
        vector.addElement(null);
        LogSupport.log("MimetypesFileTypeMap: load HOME");
        try {
            String property = System.getProperty("user.home");
            if (property != null) {
                MimeTypeFile loadFile = loadFile(String.valueOf(property) + File.separator + ".mime.types");
                if (loadFile != null) {
                    vector.addElement(loadFile);
                }
            }
        } catch (SecurityException unused) {
        }
        LogSupport.log("MimetypesFileTypeMap: load SYS");
        try {
            MimeTypeFile loadFile2 = loadFile(String.valueOf(System.getProperty("java.home")) + File.separator + "lib" + File.separator + "mime.types");
            if (loadFile2 != null) {
                vector.addElement(loadFile2);
            }
        } catch (SecurityException unused2) {
        }
        LogSupport.log("MimetypesFileTypeMap: load JAR");
        loadAllResources(vector, "mime.types");
        LogSupport.log("MimetypesFileTypeMap: load DEF");
        synchronized (MimetypesFileTypeMap.class) {
            if (defDB == null) {
                defDB = loadResource("/mimetypes.default");
            }
        }
        if (defDB != null) {
            vector.addElement(defDB);
        }
        this.DB = new MimeTypeFile[vector.size()];
        vector.copyInto(this.DB);
    }

    public MimetypesFileTypeMap(InputStream inputStream) {
        this();
        try {
            this.DB[0] = new MimeTypeFile(inputStream);
        } catch (IOException unused) {
        }
    }

    public MimetypesFileTypeMap(String str) throws IOException {
        this();
        this.DB[0] = new MimeTypeFile(str);
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:91:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void loadAllResources(java.util.Vector r9, java.lang.String r10) {
        /*
            Method dump skipped, instructions count: 287
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.activation.MimetypesFileTypeMap.loadAllResources(java.util.Vector, java.lang.String):void");
    }

    private MimeTypeFile loadFile(String str) {
        try {
            return new MimeTypeFile(str);
        } catch (IOException unused) {
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0048, code lost:
    
        if (r1 != null) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0086, code lost:
    
        if (r1 != null) goto L45;
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x008d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.sun.activation.registries.MimeTypeFile loadResource(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 0
            java.lang.Class r1 = r5.getClass()     // Catch: java.lang.Throwable -> L4e java.lang.SecurityException -> L51 java.io.IOException -> L6d
            java.io.InputStream r1 = javax.activation.SecuritySupport.getResourceAsStream(r1, r6)     // Catch: java.lang.Throwable -> L4e java.lang.SecurityException -> L51 java.io.IOException -> L6d
            if (r1 == 0) goto L31
            com.sun.activation.registries.MimeTypeFile r2 = new com.sun.activation.registries.MimeTypeFile     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            r2.<init>(r1)     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            boolean r3 = com.sun.activation.registries.LogSupport.isLoggable()     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            if (r3 == 0) goto L27
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            java.lang.String r4 = "MimetypesFileTypeMap: successfully loaded mime types file: "
            r3.<init>(r4)     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            r3.append(r6)     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            java.lang.String r3 = r3.toString()     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            com.sun.activation.registries.LogSupport.log(r3)     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
        L27:
            if (r1 == 0) goto L2c
            r1.close()     // Catch: java.io.IOException -> L2c
        L2c:
            return r2
        L2d:
            r2 = move-exception
            goto L53
        L2f:
            r2 = move-exception
            goto L6f
        L31:
            boolean r2 = com.sun.activation.registries.LogSupport.isLoggable()     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            if (r2 == 0) goto L48
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            java.lang.String r3 = "MimetypesFileTypeMap: not loading mime types file: "
            r2.<init>(r3)     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            r2.append(r6)     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            java.lang.String r2 = r2.toString()     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            com.sun.activation.registries.LogSupport.log(r2)     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
        L48:
            if (r1 == 0) goto L89
        L4a:
            r1.close()     // Catch: java.io.IOException -> L89
            goto L89
        L4e:
            r6 = move-exception
            r1 = r0
            goto L8b
        L51:
            r2 = move-exception
            r1 = r0
        L53:
            boolean r3 = com.sun.activation.registries.LogSupport.isLoggable()     // Catch: java.lang.Throwable -> L8a
            if (r3 == 0) goto L6a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L8a
            java.lang.String r4 = "MimetypesFileTypeMap: can't load "
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L8a
            r3.append(r6)     // Catch: java.lang.Throwable -> L8a
            java.lang.String r6 = r3.toString()     // Catch: java.lang.Throwable -> L8a
            com.sun.activation.registries.LogSupport.log(r6, r2)     // Catch: java.lang.Throwable -> L8a
        L6a:
            if (r1 == 0) goto L89
            goto L4a
        L6d:
            r2 = move-exception
            r1 = r0
        L6f:
            boolean r3 = com.sun.activation.registries.LogSupport.isLoggable()     // Catch: java.lang.Throwable -> L8a
            if (r3 == 0) goto L86
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L8a
            java.lang.String r4 = "MimetypesFileTypeMap: can't load "
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L8a
            r3.append(r6)     // Catch: java.lang.Throwable -> L8a
            java.lang.String r6 = r3.toString()     // Catch: java.lang.Throwable -> L8a
            com.sun.activation.registries.LogSupport.log(r6, r2)     // Catch: java.lang.Throwable -> L8a
        L86:
            if (r1 == 0) goto L89
            goto L4a
        L89:
            return r0
        L8a:
            r6 = move-exception
        L8b:
            if (r1 == 0) goto L90
            r1.close()     // Catch: java.io.IOException -> L90
        L90:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.activation.MimetypesFileTypeMap.loadResource(java.lang.String):com.sun.activation.registries.MimeTypeFile");
    }

    public synchronized void addMimeTypes(String str) {
        if (this.DB[0] == null) {
            this.DB[0] = new MimeTypeFile();
        }
        this.DB[0].appendToRegistry(str);
    }

    @Override // javax.activation.FileTypeMap
    public String getContentType(File file) {
        return getContentType(file.getName());
    }

    @Override // javax.activation.FileTypeMap
    public synchronized String getContentType(String str) {
        String mIMETypeString;
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf < 0) {
            return defaultType;
        }
        String substring = str.substring(lastIndexOf + 1);
        if (substring.length() == 0) {
            return defaultType;
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (mIMETypeString = this.DB[i].getMIMETypeString(substring)) != null) {
                return mIMETypeString;
            }
        }
        return defaultType;
    }
}
