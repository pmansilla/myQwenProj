package com.sun.mail.pop3;

import com.sun.mail.util.MailConnectException;
import com.sun.mail.util.MailLogger;
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.SocketConnectException;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

/* loaded from: classes2.dex */
public class POP3Store extends Store {
    volatile boolean cacheWriteTo;
    private Map<String, String> capabilities;
    private int defaultPort;
    volatile boolean disableTop;
    volatile File fileCacheDir;
    volatile boolean finalizeCleanClose;
    volatile boolean forgetTopHeaders;
    private String host;
    private boolean isSSL;
    volatile boolean keepMessageContent;
    private MailLogger logger;
    volatile Constructor<?> messageConstructor;
    private String name;
    private String passwd;
    private Protocol port;
    private int portNum;
    private POP3Folder portOwner;
    private boolean requireStartTLS;
    volatile boolean rsetBeforeQuit;
    volatile boolean supportsUidl;
    volatile boolean useFileCache;
    private boolean useStartTLS;
    private String user;
    private boolean usingSSL;

    public POP3Store(Session session, URLName uRLName) {
        this(session, uRLName, "pop3", false);
    }

    public POP3Store(Session session, URLName uRLName, String str, boolean z) {
        super(session, uRLName);
        Class<?> cls;
        this.name = "pop3";
        this.defaultPort = 110;
        this.isSSL = false;
        this.port = null;
        this.portOwner = null;
        this.host = null;
        this.portNum = -1;
        this.user = null;
        this.passwd = null;
        this.useStartTLS = false;
        this.requireStartTLS = false;
        this.usingSSL = false;
        this.messageConstructor = null;
        this.rsetBeforeQuit = false;
        this.disableTop = false;
        this.forgetTopHeaders = false;
        this.supportsUidl = true;
        this.cacheWriteTo = false;
        this.useFileCache = false;
        this.fileCacheDir = null;
        this.keepMessageContent = false;
        this.finalizeCleanClose = false;
        str = uRLName != null ? uRLName.getProtocol() : str;
        this.name = str;
        this.logger = new MailLogger(getClass(), "DEBUG POP3", session);
        if (!z) {
            z = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".ssl.enable", false);
        }
        if (z) {
            this.defaultPort = 995;
        } else {
            this.defaultPort = 110;
        }
        this.isSSL = z;
        this.rsetBeforeQuit = getBoolProp("rsetbeforequit");
        this.disableTop = getBoolProp("disabletop");
        this.forgetTopHeaders = getBoolProp("forgettopheaders");
        this.cacheWriteTo = getBoolProp("cachewriteto");
        this.useFileCache = getBoolProp("filecache.enable");
        String property = session.getProperty("mail." + str + ".filecache.dir");
        if (property != null && this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("mail." + str + ".filecache.dir: " + property);
        }
        if (property != null) {
            this.fileCacheDir = new File(property);
        }
        this.keepMessageContent = getBoolProp("keepmessagecontent");
        this.useStartTLS = getBoolProp("starttls.enable");
        this.requireStartTLS = getBoolProp("starttls.required");
        this.finalizeCleanClose = getBoolProp("finalizecleanclose");
        String property2 = session.getProperty("mail." + str + ".message.class");
        if (property2 != null) {
            this.logger.log(Level.CONFIG, "message class: {0}", property2);
            try {
                try {
                    cls = Class.forName(property2, false, getClass().getClassLoader());
                } catch (ClassNotFoundException unused) {
                    cls = Class.forName(property2);
                }
                this.messageConstructor = cls.getConstructor(Folder.class, Integer.TYPE);
            } catch (Exception e) {
                this.logger.log(Level.CONFIG, "failed to load message class", (Throwable) e);
            }
        }
    }

    private void checkConnected() throws MessagingException {
        if (!super.isConnected()) {
            throw new MessagingException("Not connected");
        }
    }

    private static IOException cleanupAndThrow(Protocol protocol, IOException iOException) {
        try {
            protocol.quit();
        } catch (Throwable th) {
            if (!isRecoverable(th)) {
                th.addSuppressed(iOException);
                if (th instanceof Error) {
                    throw ((Error) th);
                }
                if (th instanceof RuntimeException) {
                    throw ((RuntimeException) th);
                }
                throw new RuntimeException("unexpected exception", th);
            }
            iOException.addSuppressed(th);
        }
        return iOException;
    }

    private final synchronized boolean getBoolProp(String str) {
        boolean booleanSessionProperty;
        String str2 = "mail." + this.name + "." + str;
        booleanSessionProperty = PropUtil.getBooleanSessionProperty(this.session, str2, false);
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config(str2 + ": " + booleanSessionProperty);
        }
        return booleanSessionProperty;
    }

    private static boolean isRecoverable(Throwable th) {
        return (th instanceof Exception) || (th instanceof LinkageError);
    }

    public Map<String, String> capabilities() throws MessagingException {
        Map<String, String> map;
        synchronized (this) {
            map = this.capabilities;
        }
        return map != null ? Collections.unmodifiableMap(map) : Collections.emptyMap();
    }

    @Override // javax.mail.Service, java.lang.AutoCloseable
    public synchronized void close() throws MessagingException {
        close(false);
    }

    synchronized void close(boolean z) throws MessagingException {
        try {
            try {
                if (this.port != null) {
                    if (z) {
                        this.port.close();
                    } else {
                        this.port.quit();
                    }
                }
                this.port = null;
            } catch (Throwable th) {
                this.port = null;
                super.close();
                throw th;
            }
        } catch (IOException unused) {
            this.port = null;
        }
        super.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void closePort(POP3Folder pOP3Folder) {
        if (this.portOwner == pOP3Folder) {
            this.port = null;
            this.portOwner = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javax.mail.Service
    public void finalize() throws Throwable {
        try {
            if (this.port != null) {
                close(!this.finalizeCleanClose);
            }
        } finally {
            super.finalize();
        }
    }

    @Override // javax.mail.Store
    public Folder getDefaultFolder() throws MessagingException {
        checkConnected();
        return new DefaultFolder(this);
    }

    @Override // javax.mail.Store
    public Folder getFolder(String str) throws MessagingException {
        checkConnected();
        return new POP3Folder(this, str);
    }

    @Override // javax.mail.Store
    public Folder getFolder(URLName uRLName) throws MessagingException {
        checkConnected();
        return new POP3Folder(this, uRLName.getFile());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Protocol getPort(POP3Folder pOP3Folder) throws IOException {
        if (this.port != null && this.portOwner == null) {
            this.portOwner = pOP3Folder;
            return this.port;
        }
        Protocol protocol = new Protocol(this.host, this.portNum, this.logger, this.session.getProperties(), "mail." + this.name, this.isSSL);
        if (this.useStartTLS || this.requireStartTLS) {
            if (protocol.hasCapability("STLS")) {
                if (protocol.stls()) {
                    protocol.setCapabilities(protocol.capa());
                } else if (this.requireStartTLS) {
                    this.logger.fine("STLS required but failed");
                    throw cleanupAndThrow(protocol, new EOFException("STLS required but failed"));
                }
            } else if (this.requireStartTLS) {
                this.logger.fine("STLS required but not supported");
                throw cleanupAndThrow(protocol, new EOFException("STLS required but not supported"));
            }
        }
        this.capabilities = protocol.getCapabilities();
        this.usingSSL = protocol.isSSL();
        boolean z = true;
        if (!this.disableTop && this.capabilities != null && !this.capabilities.containsKey("TOP")) {
            this.disableTop = true;
            this.logger.fine("server doesn't support TOP, disabling it");
        }
        if (this.capabilities != null && !this.capabilities.containsKey("UIDL")) {
            z = false;
        }
        this.supportsUidl = z;
        String login = protocol.login(this.user, this.passwd);
        if (login != null) {
            throw cleanupAndThrow(protocol, new EOFException(login));
        }
        if (this.port == null && pOP3Folder != null) {
            this.port = protocol;
            this.portOwner = pOP3Folder;
        }
        if (this.portOwner == null) {
            this.portOwner = pOP3Folder;
        }
        return protocol;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Session getSession() {
        return this.session;
    }

    @Override // javax.mail.Service
    public synchronized boolean isConnected() {
        if (!super.isConnected()) {
            return false;
        }
        try {
            try {
                if (this.port == null) {
                    this.port = getPort(null);
                } else if (!this.port.noop()) {
                    throw new IOException("NOOP failed");
                }
                return true;
            } catch (IOException unused) {
                super.close();
                return false;
            }
        } catch (MessagingException unused2) {
            return false;
        }
    }

    public synchronized boolean isSSL() {
        return this.usingSSL;
    }

    @Override // javax.mail.Service
    protected synchronized boolean protocolConnect(String str, int i, String str2, String str3) throws MessagingException {
        if (str == null || str3 == null || str2 == null) {
            return false;
        }
        if (i == -1) {
            try {
                i = PropUtil.getIntSessionProperty(this.session, "mail." + this.name + ".port", -1);
            } catch (Throwable th) {
                throw th;
            }
        }
        if (i == -1) {
            i = this.defaultPort;
        }
        this.host = str;
        this.portNum = i;
        this.user = str2;
        this.passwd = str3;
        try {
            this.port = getPort(null);
            return true;
        } catch (SocketConnectException e) {
            throw new MailConnectException(e);
        } catch (EOFException e2) {
            throw new AuthenticationFailedException(e2.getMessage());
        } catch (IOException e3) {
            throw new MessagingException("Connect failed", e3);
        }
    }
}
