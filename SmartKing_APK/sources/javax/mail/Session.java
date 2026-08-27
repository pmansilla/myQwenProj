package javax.mail;

import android.support.v4.app.NotificationCompat;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.MailLogger;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import javax.mail.Provider;
import me.panpf.sketch.SLog;

/* loaded from: classes2.dex */
public final class Session {
    private static final String confDir;
    private static Session defaultSession;
    private final Authenticator authenticator;
    private boolean debug;
    private MailLogger logger;
    private PrintStream out;
    private final Properties props;
    private final EventQueue q;
    private final Hashtable<URLName, PasswordAuthentication> authTable = new Hashtable<>();
    private final Vector<Provider> providers = new Vector<>();
    private final Hashtable<String, Provider> providersByProtocol = new Hashtable<>();
    private final Hashtable<String, Provider> providersByClassName = new Hashtable<>();
    private final Properties addressMap = new Properties();

    static {
        String str;
        try {
            str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.mail.Session.1
                @Override // java.security.PrivilegedAction
                public String run() {
                    String property = System.getProperty("java.home");
                    String str2 = property + File.separator + "conf";
                    if (new File(str2).exists()) {
                        return str2 + File.separator;
                    }
                    return property + File.separator + "lib" + File.separator;
                }
            });
        } catch (Exception unused) {
            str = null;
        }
        confDir = str;
    }

    private Session(Properties properties, Authenticator authenticator) {
        this.debug = false;
        this.props = properties;
        this.authenticator = authenticator;
        if (Boolean.valueOf(properties.getProperty("mail.debug")).booleanValue()) {
            this.debug = true;
        }
        initLogger();
        this.logger.log(Level.CONFIG, "JavaMail version {0}", Version.version);
        Class<?> cls = authenticator != null ? authenticator.getClass() : getClass();
        loadProviders(cls);
        loadAddressMap(cls);
        this.q = new EventQueue((Executor) properties.get("mail.event.executor"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: javax.mail.Session.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                try {
                    return Thread.currentThread().getContextClassLoader();
                } catch (SecurityException unused) {
                    return null;
                }
            }
        });
    }

    public static Session getDefaultInstance(Properties properties) {
        return getDefaultInstance(properties, null);
    }

    public static synchronized Session getDefaultInstance(Properties properties, Authenticator authenticator) {
        Session session;
        synchronized (Session.class) {
            if (defaultSession == null) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkSetFactory();
                }
                defaultSession = new Session(properties, authenticator);
            } else if (defaultSession.authenticator != authenticator && (defaultSession.authenticator == null || authenticator == null || defaultSession.authenticator.getClass().getClassLoader() != authenticator.getClass().getClassLoader())) {
                throw new SecurityException("Access to default session denied");
            }
            session = defaultSession;
        }
        return session;
    }

    public static Session getInstance(Properties properties) {
        return new Session(properties, null);
    }

    public static Session getInstance(Properties properties, Authenticator authenticator) {
        return new Session(properties, authenticator);
    }

    private static InputStream getResourceAsStream(final Class<?> cls, final String str) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() { // from class: javax.mail.Session.5
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public InputStream run() throws IOException {
                    try {
                        return cls.getResourceAsStream(str);
                    } catch (RuntimeException e) {
                        IOException iOException = new IOException("ClassLoader.getResourceAsStream failed");
                        iOException.initCause(e);
                        throw iOException;
                    }
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    private static URL[] getResources(final ClassLoader classLoader, final String str) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction<URL[]>() { // from class: javax.mail.Session.6
            @Override // java.security.PrivilegedAction
            public URL[] run() {
                try {
                    ArrayList list = Collections.list(classLoader.getResources(str));
                    if (list.isEmpty()) {
                        return null;
                    }
                    URL[] urlArr = new URL[list.size()];
                    try {
                        list.toArray(urlArr);
                    } catch (IOException | SecurityException unused) {
                    }
                    return urlArr;
                } catch (IOException | SecurityException unused2) {
                    return null;
                }
            }
        });
    }

    private <T extends Service> T getService(Provider provider, URLName uRLName, Class<T> cls) throws NoSuchProviderException {
        if (provider == null) {
            throw new NoSuchProviderException("null");
        }
        if (uRLName == null) {
            uRLName = new URLName(provider.getProtocol(), null, -1, null, null, null);
        }
        ClassLoader classLoader = this.authenticator != null ? this.authenticator.getClass().getClassLoader() : getClass().getClassLoader();
        Class<?> cls2 = null;
        try {
            try {
                ClassLoader contextClassLoader = getContextClassLoader();
                if (contextClassLoader != null) {
                    try {
                        cls2 = Class.forName(provider.getClassName(), false, contextClassLoader);
                    } catch (ClassNotFoundException unused) {
                    }
                }
                if (cls2 == null || !cls.isAssignableFrom(cls2)) {
                    cls2 = Class.forName(provider.getClassName(), false, classLoader);
                }
            } catch (Exception e) {
                this.logger.log(Level.FINE, "Exception loading provider", (Throwable) e);
                throw new NoSuchProviderException(provider.getProtocol());
            }
        } catch (Exception unused2) {
            cls2 = Class.forName(provider.getClassName());
            if (!cls.isAssignableFrom(cls2)) {
                throw new ClassCastException(cls.getName() + SQLBuilder.BLANK + cls2.getName());
            }
        }
        if (cls.isAssignableFrom(cls2)) {
            try {
                return cls.cast(cls2.getConstructor(Session.class, URLName.class).newInstance(this, uRLName));
            } catch (Exception e2) {
                this.logger.log(Level.FINE, "Exception loading provider", (Throwable) e2);
                throw new NoSuchProviderException(provider.getProtocol());
            }
        }
        throw new ClassCastException(cls.getName() + SQLBuilder.BLANK + cls2.getName());
    }

    private Store getStore(Provider provider, URLName uRLName) throws NoSuchProviderException {
        if (provider == null || provider.getType() != Provider.Type.STORE) {
            throw new NoSuchProviderException("invalid provider");
        }
        return (Store) getService(provider, uRLName, Store.class);
    }

    private static URL[] getSystemResources(final String str) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction<URL[]>() { // from class: javax.mail.Session.7
            @Override // java.security.PrivilegedAction
            public URL[] run() {
                try {
                    ArrayList list = Collections.list(ClassLoader.getSystemResources(str));
                    if (list.isEmpty()) {
                        return null;
                    }
                    URL[] urlArr = new URL[list.size()];
                    try {
                        list.toArray(urlArr);
                    } catch (IOException | SecurityException unused) {
                    }
                    return urlArr;
                } catch (IOException | SecurityException unused2) {
                    return null;
                }
            }
        });
    }

    private Transport getTransport(Provider provider, URLName uRLName) throws NoSuchProviderException {
        if (provider == null || provider.getType() != Provider.Type.TRANSPORT) {
            throw new NoSuchProviderException("invalid provider");
        }
        return (Transport) getService(provider, uRLName, Transport.class);
    }

    private final synchronized void initLogger() {
        this.logger = new MailLogger(getClass(), SLog.LEVEL_NAME_DEBUG, this.debug, getDebugOut());
    }

    private void loadAddressMap(Class<?> cls) {
        StreamLoader streamLoader = new StreamLoader() { // from class: javax.mail.Session.3
            @Override // javax.mail.StreamLoader
            public void load(InputStream inputStream) throws IOException {
                Session.this.addressMap.load(inputStream);
            }
        };
        loadResource("/META-INF/javamail.default.address.map", cls, streamLoader, true);
        loadAllResources("META-INF/javamail.address.map", cls, streamLoader);
        try {
            if (confDir != null) {
                loadFile(confDir + "javamail.address.map", streamLoader);
            }
        } catch (SecurityException unused) {
        }
        if (this.addressMap.isEmpty()) {
            this.logger.config("failed to load address map, using defaults");
            this.addressMap.put("rfc822", "smtp");
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(8:34|35|36|(2:41|23)|38|39|40|23) */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:74:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void loadAllResources(java.lang.String r10, java.lang.Class<?> r11, javax.mail.StreamLoader r12) {
        /*
            Method dump skipped, instructions count: 184
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Session.loadAllResources(java.lang.String, java.lang.Class, javax.mail.StreamLoader):void");
    }

    private void loadFile(String str, StreamLoader streamLoader) {
        BufferedInputStream bufferedInputStream;
        BufferedInputStream bufferedInputStream2 = null;
        try {
            try {
                try {
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(str));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (FileNotFoundException unused) {
                bufferedInputStream = null;
            } catch (IOException e) {
                e = e;
            } catch (SecurityException e2) {
                e = e2;
            }
            try {
                streamLoader.load(bufferedInputStream);
                this.logger.log(Level.CONFIG, "successfully loaded file: {0}", str);
            } catch (FileNotFoundException unused2) {
                if (bufferedInputStream == null) {
                    return;
                }
                bufferedInputStream.close();
            } catch (IOException e3) {
                e = e3;
                bufferedInputStream2 = bufferedInputStream;
                if (this.logger.isLoggable(Level.CONFIG)) {
                    this.logger.log(Level.CONFIG, "not loading file: " + str, (Throwable) e);
                }
                if (bufferedInputStream2 == null) {
                    return;
                }
                bufferedInputStream2.close();
                return;
            } catch (SecurityException e4) {
                e = e4;
                bufferedInputStream2 = bufferedInputStream;
                if (this.logger.isLoggable(Level.CONFIG)) {
                    this.logger.log(Level.CONFIG, "not loading file: " + str, (Throwable) e);
                }
                if (bufferedInputStream2 == null) {
                    return;
                }
                bufferedInputStream2.close();
                return;
            } catch (Throwable th2) {
                th = th2;
                bufferedInputStream2 = bufferedInputStream;
                if (bufferedInputStream2 != null) {
                    try {
                        bufferedInputStream2.close();
                    } catch (IOException unused3) {
                    }
                }
                throw th;
            }
            bufferedInputStream.close();
        } catch (IOException unused4) {
        }
    }

    private void loadProviders(Class<?> cls) {
        StreamLoader streamLoader = new StreamLoader() { // from class: javax.mail.Session.2
            @Override // javax.mail.StreamLoader
            public void load(InputStream inputStream) throws IOException {
                Session.this.loadProvidersFromStream(inputStream);
            }
        };
        try {
            if (confDir != null) {
                loadFile(confDir + "javamail.providers", streamLoader);
            }
        } catch (SecurityException unused) {
        }
        loadAllResources("META-INF/javamail.providers", cls, streamLoader);
        loadResource("/META-INF/javamail.default.providers", cls, streamLoader, true);
        if (this.providers.size() == 0) {
            this.logger.config("failed to load any providers, using defaults");
            addProvider(new Provider(Provider.Type.STORE, "imap", "com.sun.mail.imap.IMAPStore", "Oracle", Version.version));
            addProvider(new Provider(Provider.Type.STORE, "imaps", "com.sun.mail.imap.IMAPSSLStore", "Oracle", Version.version));
            addProvider(new Provider(Provider.Type.STORE, "pop3", "com.sun.mail.pop3.POP3Store", "Oracle", Version.version));
            addProvider(new Provider(Provider.Type.STORE, "pop3s", "com.sun.mail.pop3.POP3SSLStore", "Oracle", Version.version));
            addProvider(new Provider(Provider.Type.TRANSPORT, "smtp", "com.sun.mail.smtp.SMTPTransport", "Oracle", Version.version));
            addProvider(new Provider(Provider.Type.TRANSPORT, "smtps", "com.sun.mail.smtp.SMTPSSLTransport", "Oracle", Version.version));
        }
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("Tables of loaded providers");
            this.logger.config("Providers Listed By Class Name: " + this.providersByClassName.toString());
            this.logger.config("Providers Listed By Protocol: " + this.providersByProtocol.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadProvidersFromStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        LineInputStream lineInputStream = new LineInputStream(inputStream);
        while (true) {
            String readLine = lineInputStream.readLine();
            if (readLine == null) {
                return;
            }
            if (!readLine.startsWith("#") && readLine.trim().length() != 0) {
                StringTokenizer stringTokenizer = new StringTokenizer(readLine, ";");
                Provider.Type type = null;
                String str = null;
                String str2 = null;
                String str3 = null;
                String str4 = null;
                while (stringTokenizer.hasMoreTokens()) {
                    String trim = stringTokenizer.nextToken().trim();
                    int indexOf = trim.indexOf("=");
                    if (trim.startsWith("protocol=")) {
                        str = trim.substring(indexOf + 1);
                    } else if (trim.startsWith("type=")) {
                        String substring = trim.substring(indexOf + 1);
                        if (substring.equalsIgnoreCase("store")) {
                            type = Provider.Type.STORE;
                        } else if (substring.equalsIgnoreCase(NotificationCompat.CATEGORY_TRANSPORT)) {
                            type = Provider.Type.TRANSPORT;
                        }
                    } else if (trim.startsWith("class=")) {
                        str2 = trim.substring(indexOf + 1);
                    } else if (trim.startsWith("vendor=")) {
                        str3 = trim.substring(indexOf + 1);
                    } else if (trim.startsWith("version=")) {
                        str4 = trim.substring(indexOf + 1);
                    }
                }
                if (type == null || str == null || str2 == null || str.length() <= 0 || str2.length() <= 0) {
                    this.logger.log(Level.CONFIG, "Bad provider entry: {0}", readLine);
                } else {
                    addProvider(new Provider(type, str, str2, str3, str4));
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x004a, code lost:
    
        if (r0 == null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x003c, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x003a, code lost:
    
        if (r0 == null) goto L39;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0029 A[Catch: IOException -> 0x004d, TRY_ENTER, TRY_LEAVE, TryCatch #5 {IOException -> 0x004d, blocks: (B:10:0x0029, B:24:0x003c), top: B:3:0x0001 }] */
    /* JADX WARN: Type inference failed for: r0v11, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.String] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void loadResource(java.lang.String r2, java.lang.Class<?> r3, javax.mail.StreamLoader r4, boolean r5) {
        /*
            r1 = this;
            r0 = 0
            java.io.InputStream r3 = getResourceAsStream(r3, r2)     // Catch: java.lang.Throwable -> L2d java.lang.SecurityException -> L30 java.io.IOException -> L40
            if (r3 == 0) goto L1c
            r4.load(r3)     // Catch: java.lang.Throwable -> L14 java.lang.SecurityException -> L16 java.io.IOException -> L19
            com.sun.mail.util.MailLogger r4 = r1.logger     // Catch: java.lang.Throwable -> L14 java.lang.SecurityException -> L16 java.io.IOException -> L19
            java.util.logging.Level r5 = java.util.logging.Level.CONFIG     // Catch: java.lang.Throwable -> L14 java.lang.SecurityException -> L16 java.io.IOException -> L19
            java.lang.String r0 = "successfully loaded resource: {0}"
            r4.log(r5, r0, r2)     // Catch: java.lang.Throwable -> L14 java.lang.SecurityException -> L16 java.io.IOException -> L19
            goto L27
        L14:
            r2 = move-exception
            goto L4e
        L16:
            r2 = move-exception
            r0 = r3
            goto L31
        L19:
            r2 = move-exception
            r0 = r3
            goto L41
        L1c:
            if (r5 == 0) goto L27
            com.sun.mail.util.MailLogger r4 = r1.logger     // Catch: java.lang.Throwable -> L14 java.lang.SecurityException -> L16 java.io.IOException -> L19
            java.util.logging.Level r5 = java.util.logging.Level.WARNING     // Catch: java.lang.Throwable -> L14 java.lang.SecurityException -> L16 java.io.IOException -> L19
            java.lang.String r0 = "expected resource not found: {0}"
            r4.log(r5, r0, r2)     // Catch: java.lang.Throwable -> L14 java.lang.SecurityException -> L16 java.io.IOException -> L19
        L27:
            if (r3 == 0) goto L4d
            r3.close()     // Catch: java.io.IOException -> L4d
            goto L4d
        L2d:
            r2 = move-exception
            r3 = r0
            goto L4e
        L30:
            r2 = move-exception
        L31:
            com.sun.mail.util.MailLogger r3 = r1.logger     // Catch: java.lang.Throwable -> L2d
            java.util.logging.Level r4 = java.util.logging.Level.CONFIG     // Catch: java.lang.Throwable -> L2d
            java.lang.String r5 = "Exception loading resource"
            r3.log(r4, r5, r2)     // Catch: java.lang.Throwable -> L2d
            if (r0 == 0) goto L4d
        L3c:
            r0.close()     // Catch: java.io.IOException -> L4d
            goto L4d
        L40:
            r2 = move-exception
        L41:
            com.sun.mail.util.MailLogger r3 = r1.logger     // Catch: java.lang.Throwable -> L2d
            java.util.logging.Level r4 = java.util.logging.Level.CONFIG     // Catch: java.lang.Throwable -> L2d
            java.lang.String r5 = "Exception loading resource"
            r3.log(r4, r5, r2)     // Catch: java.lang.Throwable -> L2d
            if (r0 == 0) goto L4d
            goto L3c
        L4d:
            return
        L4e:
            if (r3 == 0) goto L53
            r3.close()     // Catch: java.io.IOException -> L53
        L53:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Session.loadResource(java.lang.String, java.lang.Class, javax.mail.StreamLoader, boolean):void");
    }

    private static InputStream openStream(final URL url) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() { // from class: javax.mail.Session.8
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public InputStream run() throws IOException {
                    return url.openStream();
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    public synchronized void addProvider(Provider provider) {
        this.providers.addElement(provider);
        this.providersByClassName.put(provider.getClassName(), provider);
        if (!this.providersByProtocol.containsKey(provider.getProtocol())) {
            this.providersByProtocol.put(provider.getProtocol(), provider);
        }
    }

    public synchronized boolean getDebug() {
        return this.debug;
    }

    public synchronized PrintStream getDebugOut() {
        if (this.out == null) {
            return System.out;
        }
        return this.out;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventQueue getEventQueue() {
        return this.q;
    }

    public Folder getFolder(URLName uRLName) throws MessagingException {
        Store store = getStore(uRLName);
        store.connect();
        return store.getFolder(uRLName);
    }

    public PasswordAuthentication getPasswordAuthentication(URLName uRLName) {
        return this.authTable.get(uRLName);
    }

    public Properties getProperties() {
        return this.props;
    }

    public String getProperty(String str) {
        return this.props.getProperty(str);
    }

    public synchronized Provider getProvider(String str) throws NoSuchProviderException {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    Provider provider = null;
                    String property = this.props.getProperty("mail." + str + ".class");
                    if (property != null) {
                        if (this.logger.isLoggable(Level.FINE)) {
                            this.logger.fine("mail." + str + ".class property exists and points to " + property);
                        }
                        provider = this.providersByClassName.get(property);
                    }
                    if (provider != null) {
                        return provider;
                    }
                    Provider provider2 = this.providersByProtocol.get(str);
                    if (provider2 == null) {
                        throw new NoSuchProviderException("No provider for " + str);
                    }
                    if (this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("getProvider() returning " + provider2.toString());
                    }
                    return provider2;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        throw new NoSuchProviderException("Invalid protocol: null");
    }

    public synchronized Provider[] getProviders() {
        Provider[] providerArr;
        providerArr = new Provider[this.providers.size()];
        this.providers.copyInto(providerArr);
        return providerArr;
    }

    public Store getStore() throws NoSuchProviderException {
        return getStore(getProperty("mail.store.protocol"));
    }

    public Store getStore(String str) throws NoSuchProviderException {
        return getStore(new URLName(str, null, -1, null, null, null));
    }

    public Store getStore(Provider provider) throws NoSuchProviderException {
        return getStore(provider, null);
    }

    public Store getStore(URLName uRLName) throws NoSuchProviderException {
        return getStore(getProvider(uRLName.getProtocol()), uRLName);
    }

    public Transport getTransport() throws NoSuchProviderException {
        String property = getProperty("mail.transport.protocol");
        if (property != null) {
            return getTransport(property);
        }
        String str = (String) this.addressMap.get("rfc822");
        return str != null ? getTransport(str) : getTransport("smtp");
    }

    public Transport getTransport(String str) throws NoSuchProviderException {
        return getTransport(new URLName(str, null, -1, null, null, null));
    }

    public Transport getTransport(Address address) throws NoSuchProviderException {
        String property = getProperty("mail.transport.protocol." + address.getType());
        if (property != null) {
            return getTransport(property);
        }
        String str = (String) this.addressMap.get(address.getType());
        if (str != null) {
            return getTransport(str);
        }
        throw new NoSuchProviderException("No provider for Address type: " + address.getType());
    }

    public Transport getTransport(Provider provider) throws NoSuchProviderException {
        return getTransport(provider, null);
    }

    public Transport getTransport(URLName uRLName) throws NoSuchProviderException {
        return getTransport(getProvider(uRLName.getProtocol()), uRLName);
    }

    public PasswordAuthentication requestPasswordAuthentication(InetAddress inetAddress, int i, String str, String str2, String str3) {
        if (this.authenticator != null) {
            return this.authenticator.requestPasswordAuthentication(inetAddress, i, str, str2, str3);
        }
        return null;
    }

    public synchronized void setDebug(boolean z) {
        this.debug = z;
        initLogger();
        this.logger.log(Level.CONFIG, "setDebug: JavaMail version {0}", Version.version);
    }

    public synchronized void setDebugOut(PrintStream printStream) {
        this.out = printStream;
        initLogger();
    }

    public void setPasswordAuthentication(URLName uRLName, PasswordAuthentication passwordAuthentication) {
        if (passwordAuthentication == null) {
            this.authTable.remove(uRLName);
        } else {
            this.authTable.put(uRLName, passwordAuthentication);
        }
    }

    public synchronized void setProtocolForAddress(String str, String str2) {
        try {
            if (str2 == null) {
                this.addressMap.remove(str);
            } else {
                this.addressMap.put(str, str2);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void setProvider(Provider provider) throws NoSuchProviderException {
        if (provider == null) {
            throw new NoSuchProviderException("Can't set null provider");
        }
        this.providersByProtocol.put(provider.getProtocol(), provider);
        this.props.put("mail." + provider.getProtocol() + ".class", provider.getClassName());
    }
}
