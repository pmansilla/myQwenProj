package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MailcapFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class MailcapCommandMap extends CommandMap {
    private static final int PROG = 0;
    private static MailcapFile defDB;
    private MailcapFile[] DB;

    public MailcapCommandMap() {
        ArrayList arrayList = new ArrayList(5);
        arrayList.add(null);
        LogSupport.log("MailcapCommandMap: load HOME");
        try {
            String property = System.getProperty("user.home");
            if (property != null) {
                MailcapFile loadFile = loadFile(String.valueOf(property) + File.separator + ".mailcap");
                if (loadFile != null) {
                    arrayList.add(loadFile);
                }
            }
        } catch (SecurityException unused) {
        }
        LogSupport.log("MailcapCommandMap: load SYS");
        try {
            MailcapFile loadFile2 = loadFile(String.valueOf(System.getProperty("java.home")) + File.separator + "lib" + File.separator + "mailcap");
            if (loadFile2 != null) {
                arrayList.add(loadFile2);
            }
        } catch (SecurityException unused2) {
        }
        LogSupport.log("MailcapCommandMap: load JAR");
        loadAllResources(arrayList, "mailcap");
        LogSupport.log("MailcapCommandMap: load DEF");
        synchronized (MailcapCommandMap.class) {
            if (defDB == null) {
                defDB = loadResource("mailcap.default");
            }
        }
        if (defDB != null) {
            arrayList.add(defDB);
        }
        this.DB = new MailcapFile[arrayList.size()];
        this.DB = (MailcapFile[]) arrayList.toArray(this.DB);
    }

    public MailcapCommandMap(InputStream inputStream) {
        this();
        LogSupport.log("MailcapCommandMap: load PROG");
        if (this.DB[0] == null) {
            try {
                this.DB[0] = new MailcapFile(inputStream);
            } catch (IOException unused) {
            }
        }
    }

    public MailcapCommandMap(String str) throws IOException {
        this();
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: load PROG from " + str);
        }
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile(str);
        }
    }

    private void appendCmdsToList(Map map, List list) {
        for (String str : map.keySet()) {
            Iterator it = ((List) map.get(str)).iterator();
            while (it.hasNext()) {
                list.add(new CommandInfo(str, (String) it.next()));
            }
        }
    }

    private void appendPrefCmdsToList(Map map, List list) {
        for (String str : map.keySet()) {
            if (!checkForVerb(list, str)) {
                list.add(new CommandInfo(str, (String) ((List) map.get(str)).get(0)));
            }
        }
    }

    private boolean checkForVerb(List list, String str) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((CommandInfo) it.next()).getCommandName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private DataContentHandler getDataContentHandler(String str) {
        Class<?> cls;
        if (LogSupport.isLoggable()) {
            LogSupport.log("    got content-handler");
        }
        if (LogSupport.isLoggable()) {
            LogSupport.log("      class " + str);
        }
        try {
            ClassLoader contextClassLoader = SecuritySupport.getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = getClass().getClassLoader();
            }
            try {
                cls = contextClassLoader.loadClass(str);
            } catch (Exception unused) {
                cls = Class.forName(str);
            }
            if (cls != null) {
                return (DataContentHandler) cls.newInstance();
            }
            return null;
        } catch (ClassNotFoundException e) {
            if (!LogSupport.isLoggable()) {
                return null;
            }
            LogSupport.log("Can't load DCH " + str, e);
            return null;
        } catch (IllegalAccessException e2) {
            if (!LogSupport.isLoggable()) {
                return null;
            }
            LogSupport.log("Can't load DCH " + str, e2);
            return null;
        } catch (InstantiationException e3) {
            if (!LogSupport.isLoggable()) {
                return null;
            }
            LogSupport.log("Can't load DCH " + str, e3);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:94:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void loadAllResources(java.util.List r9, java.lang.String r10) {
        /*
            Method dump skipped, instructions count: 293
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.activation.MailcapCommandMap.loadAllResources(java.util.List, java.lang.String):void");
    }

    private MailcapFile loadFile(String str) {
        try {
            return new MailcapFile(str);
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
    private com.sun.activation.registries.MailcapFile loadResource(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 0
            java.lang.Class r1 = r5.getClass()     // Catch: java.lang.Throwable -> L4e java.lang.SecurityException -> L51 java.io.IOException -> L6d
            java.io.InputStream r1 = javax.activation.SecuritySupport.getResourceAsStream(r1, r6)     // Catch: java.lang.Throwable -> L4e java.lang.SecurityException -> L51 java.io.IOException -> L6d
            if (r1 == 0) goto L31
            com.sun.activation.registries.MailcapFile r2 = new com.sun.activation.registries.MailcapFile     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            r2.<init>(r1)     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            boolean r3 = com.sun.activation.registries.LogSupport.isLoggable()     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            if (r3 == 0) goto L27
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.SecurityException -> L2d java.io.IOException -> L2f java.lang.Throwable -> L8a
            java.lang.String r4 = "MailcapCommandMap: successfully loaded mailcap file: "
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
            java.lang.String r3 = "MailcapCommandMap: not loading mailcap file: "
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
            java.lang.String r4 = "MailcapCommandMap: can't load "
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
            java.lang.String r4 = "MailcapCommandMap: can't load "
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
        throw new UnsupportedOperationException("Method not decompiled: javax.activation.MailcapCommandMap.loadResource(java.lang.String):com.sun.activation.registries.MailcapFile");
    }

    public synchronized void addMailcap(String str) {
        LogSupport.log("MailcapCommandMap: add to PROG");
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile();
        }
        this.DB[0].appendToMailcap(str);
    }

    @Override // javax.activation.CommandMap
    public synchronized DataContentHandler createDataContentHandler(String str) {
        List list;
        DataContentHandler dataContentHandler;
        List list2;
        DataContentHandler dataContentHandler2;
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: createDataContentHandler for " + str);
        }
        if (str != null) {
            str = str.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("  search DB #" + i);
                }
                Map mailcapList = this.DB[i].getMailcapList(str);
                if (mailcapList != null && (list2 = (List) mailcapList.get("content-handler")) != null && (dataContentHandler2 = getDataContentHandler((String) list2.get(0))) != null) {
                    return dataContentHandler2;
                }
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("  search fallback DB #" + i2);
                }
                Map mailcapFallbackList = this.DB[i2].getMailcapFallbackList(str);
                if (mailcapFallbackList != null && (list = (List) mailcapFallbackList.get("content-handler")) != null && (dataContentHandler = getDataContentHandler((String) list.get(0))) != null) {
                    return dataContentHandler;
                }
            }
        }
        return null;
    }

    @Override // javax.activation.CommandMap
    public synchronized CommandInfo[] getAllCommands(String str) {
        ArrayList arrayList;
        Map mailcapFallbackList;
        Map mailcapList;
        arrayList = new ArrayList();
        if (str != null) {
            str = str.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (mailcapList = this.DB[i].getMailcapList(str)) != null) {
                appendCmdsToList(mailcapList, arrayList);
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (mailcapFallbackList = this.DB[i2].getMailcapFallbackList(str)) != null) {
                appendCmdsToList(mailcapFallbackList, arrayList);
            }
        }
        return (CommandInfo[]) arrayList.toArray(new CommandInfo[arrayList.size()]);
    }

    @Override // javax.activation.CommandMap
    public synchronized CommandInfo getCommand(String str, String str2) {
        Map mailcapFallbackList;
        List list;
        String str3;
        Map mailcapList;
        List list2;
        String str4;
        if (str != null) {
            try {
                str = str.toLowerCase(Locale.ENGLISH);
            } catch (Throwable th) {
                throw th;
            }
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (mailcapList = this.DB[i].getMailcapList(str)) != null && (list2 = (List) mailcapList.get(str2)) != null && (str4 = (String) list2.get(0)) != null) {
                return new CommandInfo(str2, str4);
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (mailcapFallbackList = this.DB[i2].getMailcapFallbackList(str)) != null && (list = (List) mailcapFallbackList.get(str2)) != null && (str3 = (String) list.get(0)) != null) {
                return new CommandInfo(str2, str3);
            }
        }
        return null;
    }

    @Override // javax.activation.CommandMap
    public synchronized String[] getMimeTypes() {
        ArrayList arrayList;
        String[] mimeTypes;
        arrayList = new ArrayList();
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (mimeTypes = this.DB[i].getMimeTypes()) != null) {
                for (int i2 = 0; i2 < mimeTypes.length; i2++) {
                    if (!arrayList.contains(mimeTypes[i2])) {
                        arrayList.add(mimeTypes[i2]);
                    }
                }
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public synchronized String[] getNativeCommands(String str) {
        ArrayList arrayList;
        String[] nativeCommands;
        arrayList = new ArrayList();
        if (str != null) {
            str = str.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (nativeCommands = this.DB[i].getNativeCommands(str)) != null) {
                for (int i2 = 0; i2 < nativeCommands.length; i2++) {
                    if (!arrayList.contains(nativeCommands[i2])) {
                        arrayList.add(nativeCommands[i2]);
                    }
                }
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    @Override // javax.activation.CommandMap
    public synchronized CommandInfo[] getPreferredCommands(String str) {
        ArrayList arrayList;
        Map mailcapFallbackList;
        Map mailcapList;
        arrayList = new ArrayList();
        if (str != null) {
            str = str.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (mailcapList = this.DB[i].getMailcapList(str)) != null) {
                appendPrefCmdsToList(mailcapList, arrayList);
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (mailcapFallbackList = this.DB[i2].getMailcapFallbackList(str)) != null) {
                appendPrefCmdsToList(mailcapFallbackList, arrayList);
            }
        }
        return (CommandInfo[]) arrayList.toArray(new CommandInfo[arrayList.size()]);
    }
}
