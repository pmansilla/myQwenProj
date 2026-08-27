package com.sun.mail.imap;

import com.sun.mail.iap.BadCommandException;
import com.sun.mail.iap.CommandFailedException;
import com.sun.mail.iap.ConnectionException;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.iap.ResponseHandler;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.imap.protocol.IMAPReferralException;
import com.sun.mail.imap.protocol.ListInfo;
import com.sun.mail.imap.protocol.Namespaces;
import com.sun.mail.util.MailConnectException;
import com.sun.mail.util.MailLogger;
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.SocketConnectException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Quota;
import javax.mail.QuotaAwareStore;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.URLName;
import kotlin.jvm.internal.CharCompanionObject;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: classes2.dex */
public class IMAPStore extends Store implements QuotaAwareStore, ResponseHandler {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String ID_ADDRESS = "address";
    public static final String ID_ARGUMENTS = "arguments";
    public static final String ID_COMMAND = "command";
    public static final String ID_DATE = "date";
    public static final String ID_ENVIRONMENT = "environment";
    public static final String ID_NAME = "name";
    public static final String ID_OS = "os";
    public static final String ID_OS_VERSION = "os-version";
    public static final String ID_SUPPORT_URL = "support-url";
    public static final String ID_VENDOR = "vendor";
    public static final String ID_VERSION = "version";
    public static final int RESPONSE = 1000;
    private final int appendBufferSize;
    protected String authorizationID;
    private final int blksize;
    private boolean closeFoldersOnStoreFailure;
    private volatile boolean connectionFailed;
    private final Object connectionFailedLock;
    private boolean debugpassword;
    private boolean debugusername;
    protected final int defaultPort;
    private boolean enableCompress;
    private boolean enableImapEvents;
    private boolean enableResponseEvents;
    private boolean enableSASL;
    private boolean enableStartTLS;
    private boolean finalizeCleanClose;
    private volatile Constructor<?> folderConstructor;
    private volatile Constructor<?> folderConstructorLI;
    private volatile boolean forceClose;
    private boolean forcePasswordRefresh;
    private String guid;
    protected String host;
    private boolean ignoreSize;
    protected final boolean isSSL;
    protected MailLogger logger;
    private boolean messageCacheDebug;
    private final int minIdleTime;
    protected final String name;
    private Namespaces namespaces;
    private ResponseHandler nonStoreResponseHandler;
    protected String password;
    private boolean peek;
    private final ConnectionPool pool;
    private volatile int port;
    protected String proxyAuthUser;
    private boolean requireStartTLS;
    private String[] saslMechanisms;
    protected String saslRealm;
    private final int statusCacheTimeout;
    private boolean throwSearchException;
    protected String user;
    private boolean usingSSL;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class ConnectionPool {
        private static final int ABORTING = 2;
        private static final int IDLE = 1;
        private static final int RUNNING = 0;
        private final long clientTimeoutInterval;
        private Vector<IMAPFolder> folders;
        private IMAPProtocol idleProtocol;
        private final MailLogger logger;
        private final int poolSize;
        private final long pruningInterval;
        private final boolean separateStoreConnection;
        private final long serverTimeoutInterval;
        private Vector<IMAPProtocol> authenticatedConnections = new Vector<>();
        private boolean storeConnectionInUse = false;
        private int idleState = 0;
        private long lastTimePruned = System.currentTimeMillis();

        ConnectionPool(String str, MailLogger mailLogger, Session session) {
            this.logger = mailLogger.getSubLogger("connectionpool", "DEBUG IMAP CP", PropUtil.getBooleanSessionProperty(session, "mail." + str + ".connectionpool.debug", false));
            int intSessionProperty = PropUtil.getIntSessionProperty(session, "mail." + str + ".connectionpoolsize", -1);
            if (intSessionProperty > 0) {
                this.poolSize = intSessionProperty;
                if (this.logger.isLoggable(Level.CONFIG)) {
                    this.logger.config("mail.imap.connectionpoolsize: " + this.poolSize);
                }
            } else {
                this.poolSize = 1;
            }
            int intSessionProperty2 = PropUtil.getIntSessionProperty(session, "mail." + str + ".connectionpooltimeout", -1);
            if (intSessionProperty2 > 0) {
                this.clientTimeoutInterval = intSessionProperty2;
                if (this.logger.isLoggable(Level.CONFIG)) {
                    this.logger.config("mail.imap.connectionpooltimeout: " + this.clientTimeoutInterval);
                }
            } else {
                this.clientTimeoutInterval = 45000L;
            }
            int intSessionProperty3 = PropUtil.getIntSessionProperty(session, "mail." + str + ".servertimeout", -1);
            if (intSessionProperty3 > 0) {
                this.serverTimeoutInterval = intSessionProperty3;
                if (this.logger.isLoggable(Level.CONFIG)) {
                    this.logger.config("mail.imap.servertimeout: " + this.serverTimeoutInterval);
                }
            } else {
                this.serverTimeoutInterval = 1800000L;
            }
            int intSessionProperty4 = PropUtil.getIntSessionProperty(session, "mail." + str + ".pruninginterval", -1);
            if (intSessionProperty4 > 0) {
                this.pruningInterval = intSessionProperty4;
                if (this.logger.isLoggable(Level.CONFIG)) {
                    this.logger.config("mail.imap.pruninginterval: " + this.pruningInterval);
                }
            } else {
                this.pruningInterval = DateUtils.MILLIS_PER_MINUTE;
            }
            this.separateStoreConnection = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".separatestoreconnection", false);
            if (this.separateStoreConnection) {
                this.logger.config("dedicate a store connection");
            }
        }
    }

    public IMAPStore(Session session, URLName uRLName) {
        this(session, uRLName, "imap", false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IMAPStore(Session session, URLName uRLName, String str, boolean z) {
        super(session, uRLName);
        Class<?> cls;
        this.port = -1;
        this.enableStartTLS = false;
        this.requireStartTLS = false;
        this.usingSSL = false;
        this.enableSASL = false;
        this.forcePasswordRefresh = false;
        this.enableResponseEvents = false;
        this.enableImapEvents = false;
        this.throwSearchException = false;
        this.peek = false;
        this.closeFoldersOnStoreFailure = true;
        this.enableCompress = false;
        this.finalizeCleanClose = false;
        this.connectionFailed = false;
        this.forceClose = false;
        this.connectionFailedLock = new Object();
        this.folderConstructor = null;
        this.folderConstructorLI = null;
        this.nonStoreResponseHandler = new ResponseHandler() { // from class: com.sun.mail.imap.IMAPStore.1
            @Override // com.sun.mail.iap.ResponseHandler
            public void handleResponse(Response response) {
                if (response.isOK() || response.isNO() || response.isBAD() || response.isBYE()) {
                    IMAPStore.this.handleResponseCode(response);
                }
                if (response.isBYE()) {
                    IMAPStore.this.logger.fine("IMAPStore non-store connection dead");
                }
            }
        };
        str = uRLName != null ? uRLName.getProtocol() : str;
        this.name = str;
        if (!z) {
            z = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".ssl.enable", false);
        }
        if (z) {
            this.defaultPort = 993;
        } else {
            this.defaultPort = 143;
        }
        this.isSSL = z;
        this.debug = session.getDebug();
        this.debugusername = PropUtil.getBooleanSessionProperty(session, "mail.debug.auth.username", true);
        this.debugpassword = PropUtil.getBooleanSessionProperty(session, "mail.debug.auth.password", false);
        this.logger = new MailLogger(getClass(), "DEBUG " + str.toUpperCase(Locale.ENGLISH), session);
        if (PropUtil.getBooleanSessionProperty(session, "mail." + str + ".partialfetch", true)) {
            this.blksize = PropUtil.getIntSessionProperty(session, "mail." + str + ".fetchsize", 16384);
            if (this.logger.isLoggable(Level.CONFIG)) {
                this.logger.config("mail.imap.fetchsize: " + this.blksize);
            }
        } else {
            this.blksize = -1;
            this.logger.config("mail.imap.partialfetch: false");
        }
        this.ignoreSize = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".ignorebodystructuresize", false);
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("mail.imap.ignorebodystructuresize: " + this.ignoreSize);
        }
        this.statusCacheTimeout = PropUtil.getIntSessionProperty(session, "mail." + str + ".statuscachetimeout", 1000);
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("mail.imap.statuscachetimeout: " + this.statusCacheTimeout);
        }
        this.appendBufferSize = PropUtil.getIntSessionProperty(session, "mail." + str + ".appendbuffersize", -1);
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("mail.imap.appendbuffersize: " + this.appendBufferSize);
        }
        this.minIdleTime = PropUtil.getIntSessionProperty(session, "mail." + str + ".minidletime", 10);
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("mail.imap.minidletime: " + this.minIdleTime);
        }
        String property = session.getProperty("mail." + str + ".proxyauth.user");
        if (property != null) {
            this.proxyAuthUser = property;
            if (this.logger.isLoggable(Level.CONFIG)) {
                this.logger.config("mail.imap.proxyauth.user: " + this.proxyAuthUser);
            }
        }
        this.enableStartTLS = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".starttls.enable", false);
        if (this.enableStartTLS) {
            this.logger.config("enable STARTTLS");
        }
        this.requireStartTLS = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".starttls.required", false);
        if (this.requireStartTLS) {
            this.logger.config("require STARTTLS");
        }
        this.enableSASL = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".sasl.enable", false);
        if (this.enableSASL) {
            this.logger.config("enable SASL");
        }
        if (this.enableSASL) {
            String property2 = session.getProperty("mail." + str + ".sasl.mechanisms");
            if (property2 != null && property2.length() > 0) {
                if (this.logger.isLoggable(Level.CONFIG)) {
                    this.logger.config("SASL mechanisms allowed: " + property2);
                }
                ArrayList arrayList = new ArrayList(5);
                StringTokenizer stringTokenizer = new StringTokenizer(property2, " ,");
                while (stringTokenizer.hasMoreTokens()) {
                    String nextToken = stringTokenizer.nextToken();
                    if (nextToken.length() > 0) {
                        arrayList.add(nextToken);
                    }
                }
                this.saslMechanisms = new String[arrayList.size()];
                arrayList.toArray(this.saslMechanisms);
            }
        }
        String property3 = session.getProperty("mail." + str + ".sasl.authorizationid");
        if (property3 != null) {
            this.authorizationID = property3;
            this.logger.log(Level.CONFIG, "mail.imap.sasl.authorizationid: {0}", this.authorizationID);
        }
        String property4 = session.getProperty("mail." + str + ".sasl.realm");
        if (property4 != null) {
            this.saslRealm = property4;
            this.logger.log(Level.CONFIG, "mail.imap.sasl.realm: {0}", this.saslRealm);
        }
        this.forcePasswordRefresh = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".forcepasswordrefresh", false);
        if (this.forcePasswordRefresh) {
            this.logger.config("enable forcePasswordRefresh");
        }
        this.enableResponseEvents = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".enableresponseevents", false);
        if (this.enableResponseEvents) {
            this.logger.config("enable IMAP response events");
        }
        this.enableImapEvents = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".enableimapevents", false);
        if (this.enableImapEvents) {
            this.logger.config("enable IMAP IDLE events");
        }
        this.messageCacheDebug = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".messagecache.debug", false);
        this.guid = session.getProperty("mail." + str + ".yahoo.guid");
        if (this.guid != null) {
            this.logger.log(Level.CONFIG, "mail.imap.yahoo.guid: {0}", this.guid);
        }
        this.throwSearchException = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".throwsearchexception", false);
        if (this.throwSearchException) {
            this.logger.config("throw SearchException");
        }
        this.peek = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".peek", false);
        if (this.peek) {
            this.logger.config("peek");
        }
        this.closeFoldersOnStoreFailure = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".closefoldersonstorefailure", true);
        if (this.closeFoldersOnStoreFailure) {
            this.logger.config("closeFoldersOnStoreFailure");
        }
        this.enableCompress = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".compress.enable", false);
        if (this.enableCompress) {
            this.logger.config("enable COMPRESS");
        }
        this.finalizeCleanClose = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".finalizecleanclose", false);
        if (this.finalizeCleanClose) {
            this.logger.config("close connection cleanly in finalize");
        }
        String property5 = session.getProperty("mail." + str + ".folder.class");
        if (property5 != null) {
            this.logger.log(Level.CONFIG, "IMAP: folder class: {0}", property5);
            try {
                try {
                    cls = Class.forName(property5, false, getClass().getClassLoader());
                } catch (ClassNotFoundException unused) {
                    cls = Class.forName(property5);
                }
                this.folderConstructor = cls.getConstructor(String.class, Character.TYPE, IMAPStore.class, Boolean.class);
                this.folderConstructorLI = cls.getConstructor(ListInfo.class, IMAPStore.class);
            } catch (Exception e) {
                this.logger.log(Level.CONFIG, "IMAP: failed to load folder class", (Throwable) e);
            }
        }
        this.pool = new ConnectionPool(str, this.logger, session);
    }

    private void authenticate(IMAPProtocol iMAPProtocol, String str, String str2, String str3) throws ProtocolException {
        String property = this.session.getProperty("mail." + this.name + ".auth.mechanisms");
        if (property == null) {
            property = "PLAIN LOGIN NTLM XOAUTH2";
        }
        StringTokenizer stringTokenizer = new StringTokenizer(property);
        while (stringTokenizer.hasMoreTokens()) {
            String upperCase = stringTokenizer.nextToken().toUpperCase(Locale.ENGLISH);
            if (property == "PLAIN LOGIN NTLM XOAUTH2") {
                String str4 = "mail." + this.name + ".auth." + upperCase.toLowerCase(Locale.ENGLISH) + ".disable";
                if (PropUtil.getBooleanSessionProperty(this.session, str4, upperCase.equals("XOAUTH2"))) {
                    if (this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("mechanism " + upperCase + " disabled by property: " + str4);
                    }
                }
            }
            if (!iMAPProtocol.hasCapability("AUTH=" + upperCase) && (!upperCase.equals("LOGIN") || !iMAPProtocol.hasCapability("AUTH-LOGIN"))) {
                this.logger.log(Level.FINE, "mechanism {0} not supported by server", upperCase);
            } else {
                if (upperCase.equals("PLAIN")) {
                    iMAPProtocol.authplain(str, str2, str3);
                    return;
                }
                if (upperCase.equals("LOGIN")) {
                    iMAPProtocol.authlogin(str2, str3);
                    return;
                } else if (upperCase.equals("NTLM")) {
                    iMAPProtocol.authntlm(str, str2, str3);
                    return;
                } else {
                    if (upperCase.equals("XOAUTH2")) {
                        iMAPProtocol.authoauth2(str2, str3);
                        return;
                    }
                    this.logger.log(Level.FINE, "no authenticator for mechanism {0}", upperCase);
                }
            }
        }
        if (iMAPProtocol.hasCapability("LOGINDISABLED")) {
            throw new ProtocolException("No login methods supported!");
        }
        iMAPProtocol.login(str2, str3);
    }

    private void checkConnected() {
        if (!super.isConnected()) {
            throw new IllegalStateException("Not connected");
        }
    }

    private synchronized void cleanup() {
        boolean z;
        boolean z2;
        if (!super.isConnected()) {
            this.logger.fine("IMAPStore cleanup, not connected");
            return;
        }
        synchronized (this.connectionFailedLock) {
            z = this.forceClose;
            this.forceClose = false;
            this.connectionFailed = false;
        }
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("IMAPStore cleanup, force " + z);
        }
        if (!z || this.closeFoldersOnStoreFailure) {
            Vector vector = null;
            while (true) {
                synchronized (this.pool) {
                    if (this.pool.folders != null) {
                        vector = this.pool.folders;
                        this.pool.folders = null;
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                }
                if (z2) {
                    break;
                }
                int size = vector.size();
                for (int i = 0; i < size; i++) {
                    IMAPFolder iMAPFolder = (IMAPFolder) vector.get(i);
                    if (z) {
                        try {
                            this.logger.fine("force folder to close");
                            iMAPFolder.forceClose();
                        } catch (IllegalStateException | MessagingException unused) {
                        }
                    } else {
                        this.logger.fine("close folder");
                        iMAPFolder.close(false);
                    }
                }
            }
        }
        synchronized (this.pool) {
            emptyConnectionPool(z);
        }
        try {
            super.close();
        } catch (MessagingException unused2) {
        }
        this.logger.fine("IMAPStore cleanup done");
    }

    private void emptyConnectionPool(boolean z) {
        synchronized (this.pool) {
            for (int size = this.pool.authenticatedConnections.size() - 1; size >= 0; size--) {
                try {
                    IMAPProtocol iMAPProtocol = (IMAPProtocol) this.pool.authenticatedConnections.elementAt(size);
                    iMAPProtocol.removeResponseHandler(this);
                    if (z) {
                        iMAPProtocol.disconnect();
                    } else {
                        iMAPProtocol.logout();
                    }
                } catch (ProtocolException unused) {
                }
            }
            this.pool.authenticatedConnections.removeAllElements();
        }
        this.pool.logger.fine("removed all authenticated connections from pool");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.sun.mail.imap.protocol.Namespaces] */
    /* JADX WARN: Type inference failed for: r1v3 */
    private synchronized Namespaces getNamespaces() throws MessagingException {
        ProtocolException e;
        ConnectionException e2;
        IMAPProtocol iMAPProtocol;
        checkConnected();
        IMAPProtocol iMAPProtocol2 = this.namespaces;
        try {
            if (iMAPProtocol2 == 0) {
                try {
                    iMAPProtocol = getStoreProtocol();
                    try {
                        this.namespaces = iMAPProtocol.namespace();
                    } catch (BadCommandException unused) {
                    } catch (ConnectionException e3) {
                        e2 = e3;
                        throw new StoreClosedException(this, e2.getMessage());
                    } catch (ProtocolException e4) {
                        e = e4;
                        throw new MessagingException(e.getMessage(), e);
                    }
                } catch (BadCommandException unused2) {
                    iMAPProtocol = null;
                } catch (ConnectionException e5) {
                    e2 = e5;
                } catch (ProtocolException e6) {
                    e = e6;
                } catch (Throwable th) {
                    iMAPProtocol2 = 0;
                    th = th;
                    releaseStoreProtocol(iMAPProtocol2);
                    throw th;
                }
                releaseStoreProtocol(iMAPProtocol);
            }
        } catch (Throwable th2) {
            th = th2;
        }
        return this.namespaces;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0043 A[Catch: all -> 0x00f0, TRY_ENTER, TryCatch #1 {, blocks: (B:6:0x0007, B:8:0x0016, B:10:0x0021, B:12:0x0025, B:13:0x0028, B:15:0x0030, B:17:0x0043, B:18:0x00b9, B:25:0x00c1, B:21:0x00ea, B:22:0x00ed, B:20:0x00d8, B:29:0x00c9, B:30:0x00d7, B:33:0x0050, B:34:0x0057, B:40:0x003d, B:45:0x0058, B:47:0x0066, B:48:0x008a, B:50:0x009b, B:52:0x00a7, B:54:0x00af), top: B:5:0x0007, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0050 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.sun.mail.imap.protocol.IMAPProtocol getStoreProtocol() throws com.sun.mail.iap.ProtocolException {
        /*
            Method dump skipped, instructions count: 244
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPStore.getStoreProtocol():com.sun.mail.imap.protocol.IMAPProtocol");
    }

    private void login(IMAPProtocol iMAPProtocol, String str, String str2) throws ProtocolException {
        if ((this.enableStartTLS || this.requireStartTLS) && !iMAPProtocol.isSSL()) {
            if (iMAPProtocol.hasCapability("STARTTLS")) {
                iMAPProtocol.startTLS();
                iMAPProtocol.capability();
            } else if (this.requireStartTLS) {
                this.logger.fine("STARTTLS required but not supported by server");
                throw new ProtocolException("STARTTLS required but not supported by server");
            }
        }
        if (iMAPProtocol.isAuthenticated()) {
            return;
        }
        preLogin(iMAPProtocol);
        if (this.guid != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("GUID", this.guid);
            iMAPProtocol.id(hashMap);
        }
        iMAPProtocol.getCapabilities().put("__PRELOGIN__", "");
        String str3 = this.authorizationID != null ? this.authorizationID : this.proxyAuthUser != null ? this.proxyAuthUser : null;
        if (this.enableSASL) {
            try {
                iMAPProtocol.sasllogin(this.saslMechanisms, this.saslRealm, str3, str, str2);
                if (!iMAPProtocol.isAuthenticated()) {
                    throw new CommandFailedException("SASL authentication failed");
                }
            } catch (UnsupportedOperationException unused) {
            }
        }
        if (!iMAPProtocol.isAuthenticated()) {
            authenticate(iMAPProtocol, str3, str, str2);
        }
        if (this.proxyAuthUser != null) {
            iMAPProtocol.proxyauth(this.proxyAuthUser);
        }
        if (iMAPProtocol.hasCapability("__PRELOGIN__")) {
            try {
                iMAPProtocol.capability();
            } catch (ConnectionException e) {
                throw e;
            } catch (ProtocolException unused2) {
            }
        }
        if (this.enableCompress && iMAPProtocol.hasCapability("COMPRESS=DEFLATE")) {
            iMAPProtocol.compress();
        }
        if (iMAPProtocol.hasCapability("UTF8=ACCEPT") || iMAPProtocol.hasCapability("UTF8=ONLY")) {
            iMAPProtocol.enable("UTF8=ACCEPT");
        }
    }

    private Folder[] namespaceToFolders(Namespaces.Namespace[] namespaceArr, String str) {
        Folder[] folderArr = new Folder[namespaceArr.length];
        for (int i = 0; i < folderArr.length; i++) {
            String str2 = namespaceArr[i].prefix;
            if (str == null) {
                int length = str2.length();
                if (length > 0) {
                    int i2 = length - 1;
                    if (str2.charAt(i2) == namespaceArr[i].delimiter) {
                        str2 = str2.substring(0, i2);
                    }
                }
            } else {
                str2 = str2 + str;
            }
            folderArr[i] = newIMAPFolder(str2, namespaceArr[i].delimiter, Boolean.valueOf(str == null));
        }
        return folderArr;
    }

    private void refreshPassword() {
        InetAddress inetAddress;
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("refresh password, user: " + traceUser(this.user));
        }
        try {
            inetAddress = InetAddress.getByName(this.host);
        } catch (UnknownHostException unused) {
            inetAddress = null;
        }
        PasswordAuthentication requestPasswordAuthentication = this.session.requestPasswordAuthentication(inetAddress, this.port, this.name, null, this.user);
        if (requestPasswordAuthentication != null) {
            this.user = requestPasswordAuthentication.getUserName();
            this.password = requestPasswordAuthentication.getPassword();
        }
    }

    private void releaseStoreProtocol(IMAPProtocol iMAPProtocol) {
        boolean z;
        if (iMAPProtocol == null) {
            cleanup();
            return;
        }
        synchronized (this.connectionFailedLock) {
            z = this.connectionFailed;
            this.connectionFailed = false;
        }
        synchronized (this.pool) {
            this.pool.storeConnectionInUse = false;
            this.pool.notifyAll();
            this.pool.logger.fine("releaseStoreProtocol()");
            timeoutConnections();
        }
        if (z) {
            cleanup();
        }
    }

    private void timeoutConnections() {
        synchronized (this.pool) {
            if (System.currentTimeMillis() - this.pool.lastTimePruned > this.pool.pruningInterval && this.pool.authenticatedConnections.size() > 1) {
                if (this.pool.logger.isLoggable(Level.FINE)) {
                    this.pool.logger.fine("checking for connections to prune: " + (System.currentTimeMillis() - this.pool.lastTimePruned));
                    this.pool.logger.fine("clientTimeoutInterval: " + this.pool.clientTimeoutInterval);
                }
                for (int size = this.pool.authenticatedConnections.size() - 1; size > 0; size--) {
                    IMAPProtocol iMAPProtocol = (IMAPProtocol) this.pool.authenticatedConnections.elementAt(size);
                    if (this.pool.logger.isLoggable(Level.FINE)) {
                        this.pool.logger.fine("protocol last used: " + (System.currentTimeMillis() - iMAPProtocol.getTimestamp()));
                    }
                    if (System.currentTimeMillis() - iMAPProtocol.getTimestamp() > this.pool.clientTimeoutInterval) {
                        this.pool.logger.fine("authenticated connection timed out, logging out the connection");
                        iMAPProtocol.removeResponseHandler(this);
                        this.pool.authenticatedConnections.removeElementAt(size);
                        try {
                            iMAPProtocol.logout();
                        } catch (ProtocolException unused) {
                        }
                    }
                }
                this.pool.lastTimePruned = System.currentTimeMillis();
            }
        }
    }

    private String tracePassword(String str) {
        return this.debugpassword ? str : str == null ? "<null>" : "<non-null>";
    }

    private String traceUser(String str) {
        return this.debugusername ? str : "<user name suppressed>";
    }

    private void waitIfIdle() throws ProtocolException {
        while (this.pool.idleState != 0) {
            if (this.pool.idleState == 1) {
                this.pool.idleProtocol.idleAbort();
                this.pool.idleState = 2;
            }
            try {
                this.pool.wait();
            } catch (InterruptedException e) {
                throw new ProtocolException("Interrupted waitIfIdle", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allowReadOnlySelect() {
        return PropUtil.getBooleanSessionProperty(this.session, "mail." + this.name + ".allowreadonlyselect", false);
    }

    @Override // javax.mail.Service, java.lang.AutoCloseable
    public synchronized void close() throws MessagingException {
        boolean isEmpty;
        if (!super.isConnected()) {
            return;
        }
        IMAPProtocol iMAPProtocol = null;
        try {
            try {
                synchronized (this.pool) {
                    isEmpty = this.pool.authenticatedConnections.isEmpty();
                }
                if (isEmpty) {
                    this.pool.logger.fine("close() - no connections ");
                    cleanup();
                    releaseStoreProtocol(null);
                    return;
                }
                IMAPProtocol storeProtocol = getStoreProtocol();
                try {
                    synchronized (this.pool) {
                        this.pool.authenticatedConnections.removeElement(storeProtocol);
                    }
                    storeProtocol.logout();
                    releaseStoreProtocol(storeProtocol);
                } catch (ProtocolException e) {
                    e = e;
                    throw new MessagingException(e.getMessage(), e);
                } catch (Throwable th) {
                    th = th;
                    iMAPProtocol = storeProtocol;
                    releaseStoreProtocol(iMAPProtocol);
                    throw th;
                }
            } catch (ProtocolException e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javax.mail.Service
    public void finalize() throws Throwable {
        if (!this.finalizeCleanClose) {
            synchronized (this.connectionFailedLock) {
                this.connectionFailed = true;
                this.forceClose = true;
            }
            this.closeFoldersOnStoreFailure = true;
        }
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAppendBufferSize() {
        return this.appendBufferSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MailLogger getConnectionPoolLogger() {
        return this.pool.logger;
    }

    @Override // javax.mail.Store
    public synchronized Folder getDefaultFolder() throws MessagingException {
        checkConnected();
        return new DefaultFolder(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getFetchBlockSize() {
        return this.blksize;
    }

    @Override // javax.mail.Store
    public synchronized Folder getFolder(String str) throws MessagingException {
        checkConnected();
        return newIMAPFolder(str, CharCompanionObject.MAX_VALUE);
    }

    @Override // javax.mail.Store
    public synchronized Folder getFolder(URLName uRLName) throws MessagingException {
        checkConnected();
        return newIMAPFolder(uRLName.getFile(), CharCompanionObject.MAX_VALUE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IMAPProtocol getFolderStoreProtocol() throws ProtocolException {
        IMAPProtocol storeProtocol = getStoreProtocol();
        storeProtocol.removeResponseHandler(this);
        storeProtocol.addResponseHandler(this.nonStoreResponseHandler);
        return storeProtocol;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getMessageCacheDebug() {
        return this.messageCacheDebug;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMinIdleTime() {
        return this.minIdleTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getPeek() {
        return this.peek;
    }

    @Override // javax.mail.Store
    public Folder[] getPersonalNamespaces() throws MessagingException {
        Namespaces namespaces = getNamespaces();
        return (namespaces == null || namespaces.personal == null) ? super.getPersonalNamespaces() : namespaceToFolders(namespaces.personal, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't wrap try/catch for region: R(11:6|7|(10:16|(1:18)|19|20|(2:47|48)|22|(2:28|29)|38|39|(3:41|(1:43)|44))|56|57|58|(1:60)|61|62|63|(3:65|39|(0))(1:66)) */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0144, code lost:
    
        throw new javax.mail.MessagingException("connection failure");
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0111, code lost:
    
        if (r3 != null) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0116, code lost:
    
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0113, code lost:
    
        r3.disconnect();
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0110, code lost:
    
        r3 = r1;
     */
    /* JADX WARN: Removed duplicated region for block: B:41:0x011f A[Catch: all -> 0x0145, TryCatch #5 {, blocks: (B:7:0x0007, B:9:0x0013, B:11:0x0020, B:13:0x0028, B:16:0x0032, B:18:0x003c, B:19:0x005c, B:48:0x0085, B:22:0x00a4, B:24:0x00a8, B:26:0x00b4, B:29:0x00bc, B:31:0x00d7, B:32:0x00df, B:38:0x00e2, B:39:0x011a, B:41:0x011f, B:43:0x0127, B:44:0x0131, B:45:0x013a, B:50:0x0099, B:51:0x00a1, B:56:0x00e6, B:58:0x00ed, B:60:0x00f1, B:61:0x00f4, B:63:0x00fc, B:67:0x013d, B:68:0x0144, B:76:0x0113), top: B:6:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x013d A[EDGE_INSN: B:66:0x013d->B:67:0x013d BREAK  A[LOOP:0: B:2:0x0001->B:35:0x0001, LOOP_LABEL: LOOP:0: B:2:0x0001->B:35:0x0001], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.sun.mail.imap.protocol.IMAPProtocol getProtocol(com.sun.mail.imap.IMAPFolder r9) throws javax.mail.MessagingException {
        /*
            Method dump skipped, instructions count: 329
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPStore.getProtocol(com.sun.mail.imap.IMAPFolder):com.sun.mail.imap.protocol.IMAPProtocol");
    }

    public String getProxyAuthUser() {
        return this.proxyAuthUser;
    }

    @Override // javax.mail.QuotaAwareStore
    public synchronized Quota[] getQuota(String str) throws MessagingException {
        IMAPProtocol storeProtocol;
        Quota[] quotaRoot;
        checkConnected();
        IMAPProtocol iMAPProtocol = null;
        try {
            try {
                storeProtocol = getStoreProtocol();
            } catch (Throwable th) {
                th = th;
            }
        } catch (BadCommandException e) {
            e = e;
        } catch (ConnectionException e2) {
            e = e2;
        } catch (ProtocolException e3) {
            e = e3;
        }
        try {
            quotaRoot = storeProtocol.getQuotaRoot(str);
            releaseStoreProtocol(storeProtocol);
        } catch (BadCommandException e4) {
            e = e4;
            throw new MessagingException("QUOTA not supported", e);
        } catch (ConnectionException e5) {
            e = e5;
            throw new StoreClosedException(this, e.getMessage());
        } catch (ProtocolException e6) {
            e = e6;
            throw new MessagingException(e.getMessage(), e);
        } catch (Throwable th2) {
            th = th2;
            iMAPProtocol = storeProtocol;
            releaseStoreProtocol(iMAPProtocol);
            throw th;
        }
        return quotaRoot;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Session getSession() {
        return this.session;
    }

    @Override // javax.mail.Store
    public Folder[] getSharedNamespaces() throws MessagingException {
        Namespaces namespaces = getNamespaces();
        return (namespaces == null || namespaces.shared == null) ? super.getSharedNamespaces() : namespaceToFolders(namespaces.shared, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getStatusCacheTimeout() {
        return this.statusCacheTimeout;
    }

    @Override // javax.mail.Store
    public Folder[] getUserNamespaces(String str) throws MessagingException {
        Namespaces namespaces = getNamespaces();
        return (namespaces == null || namespaces.otherUsers == null) ? super.getUserNamespaces(str) : namespaceToFolders(namespaces.otherUsers, str);
    }

    @Override // com.sun.mail.iap.ResponseHandler
    public void handleResponse(Response response) {
        if (response.isOK() || response.isNO() || response.isBAD() || response.isBYE()) {
            handleResponseCode(response);
        }
        if (response.isBYE()) {
            this.logger.fine("IMAPStore connection dead");
            synchronized (this.connectionFailedLock) {
                this.connectionFailed = true;
                if (response.isSynthetic()) {
                    this.forceClose = true;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleResponseCode(Response response) {
        if (this.enableResponseEvents) {
            notifyStoreListeners(1000, response.toString());
        }
        String rest = response.getRest();
        boolean z = false;
        if (rest.startsWith("[")) {
            int indexOf = rest.indexOf(93);
            if (indexOf > 0 && rest.substring(0, indexOf + 1).equalsIgnoreCase("[ALERT]")) {
                z = true;
            }
            rest = rest.substring(indexOf + 1).trim();
        }
        if (z) {
            notifyStoreListeners(1, rest);
        } else {
            if (!response.isUnTagged() || rest.length() <= 0) {
                return;
            }
            notifyStoreListeners(2, rest);
        }
    }

    public synchronized boolean hasCapability(String str) throws MessagingException {
        IMAPProtocol storeProtocol;
        boolean hasCapability;
        IMAPProtocol iMAPProtocol = null;
        try {
            try {
                storeProtocol = getStoreProtocol();
            } catch (ProtocolException e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            hasCapability = storeProtocol.hasCapability(str);
            releaseStoreProtocol(storeProtocol);
        } catch (ProtocolException e2) {
            e = e2;
            iMAPProtocol = storeProtocol;
            throw new MessagingException(e.getMessage(), e);
        } catch (Throwable th2) {
            th = th2;
            iMAPProtocol = storeProtocol;
            releaseStoreProtocol(iMAPProtocol);
            throw th;
        }
        return hasCapability;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasSeparateStoreConnection() {
        return this.pool.separateStoreConnection;
    }

    public synchronized Map<String, String> id(Map<String, String> map) throws MessagingException {
        IMAPProtocol storeProtocol;
        Map<String, String> id;
        checkConnected();
        IMAPProtocol iMAPProtocol = null;
        try {
            try {
                storeProtocol = getStoreProtocol();
            } catch (Throwable th) {
                th = th;
            }
        } catch (BadCommandException e) {
            e = e;
        } catch (ConnectionException e2) {
            e = e2;
        } catch (ProtocolException e3) {
            e = e3;
        }
        try {
            id = storeProtocol.id(map);
            releaseStoreProtocol(storeProtocol);
        } catch (BadCommandException e4) {
            e = e4;
            throw new MessagingException("ID not supported", e);
        } catch (ConnectionException e5) {
            e = e5;
            throw new StoreClosedException(this, e.getMessage());
        } catch (ProtocolException e6) {
            e = e6;
            throw new MessagingException(e.getMessage(), e);
        } catch (Throwable th2) {
            th = th2;
            iMAPProtocol = storeProtocol;
            releaseStoreProtocol(iMAPProtocol);
            throw th;
        }
        return id;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0052, code lost:
    
        if (r7.enableImapEvents == false) goto L147;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0058, code lost:
    
        if (r3.isUnTagged() == false) goto L148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x005a, code lost:
    
        notifyStoreListeners(1000, r3.toString());
     */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00e3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void idle() throws javax.mail.MessagingException {
        /*
            Method dump skipped, instructions count: 257
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPStore.idle():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean ignoreBodyStructureSize() {
        return this.ignoreSize;
    }

    @Override // javax.mail.Service
    public synchronized boolean isConnected() {
        IMAPProtocol iMAPProtocol;
        Throwable th;
        if (!super.isConnected()) {
            return false;
        }
        IMAPProtocol iMAPProtocol2 = null;
        try {
            iMAPProtocol = getStoreProtocol();
            try {
                iMAPProtocol.noop();
                releaseStoreProtocol(iMAPProtocol);
            } catch (ProtocolException unused) {
                iMAPProtocol2 = iMAPProtocol;
                releaseStoreProtocol(iMAPProtocol2);
                return super.isConnected();
            } catch (Throwable th2) {
                th = th2;
                releaseStoreProtocol(iMAPProtocol);
                throw th;
            }
        } catch (ProtocolException unused2) {
        } catch (Throwable th3) {
            iMAPProtocol = null;
            th = th3;
        }
        return super.isConnected();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isConnectionPoolFull() {
        boolean z;
        synchronized (this.pool) {
            if (this.pool.logger.isLoggable(Level.FINE)) {
                this.pool.logger.fine("connection pool current size: " + this.pool.authenticatedConnections.size() + "   pool size: " + this.pool.poolSize);
            }
            z = this.pool.authenticatedConnections.size() >= this.pool.poolSize;
        }
        return z;
    }

    public synchronized boolean isSSL() {
        return this.usingSSL;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.sun.mail.imap.IMAPFolder newIMAPFolder(com.sun.mail.imap.protocol.ListInfo r5) {
        /*
            r4 = this;
            java.lang.reflect.Constructor<?> r0 = r4.folderConstructorLI
            if (r0 == 0) goto L20
            r0 = 2
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch: java.lang.Exception -> L16
            r1 = 0
            r0[r1] = r5     // Catch: java.lang.Exception -> L16
            r1 = 1
            r0[r1] = r4     // Catch: java.lang.Exception -> L16
            java.lang.reflect.Constructor<?> r1 = r4.folderConstructorLI     // Catch: java.lang.Exception -> L16
            java.lang.Object r0 = r1.newInstance(r0)     // Catch: java.lang.Exception -> L16
            com.sun.mail.imap.IMAPFolder r0 = (com.sun.mail.imap.IMAPFolder) r0     // Catch: java.lang.Exception -> L16
            goto L21
        L16:
            r0 = move-exception
            com.sun.mail.util.MailLogger r1 = r4.logger
            java.util.logging.Level r2 = java.util.logging.Level.FINE
            java.lang.String r3 = "exception creating IMAPFolder class LI"
            r1.log(r2, r3, r0)
        L20:
            r0 = 0
        L21:
            if (r0 != 0) goto L28
            com.sun.mail.imap.IMAPFolder r0 = new com.sun.mail.imap.IMAPFolder
            r0.<init>(r5, r4)
        L28:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPStore.newIMAPFolder(com.sun.mail.imap.protocol.ListInfo):com.sun.mail.imap.IMAPFolder");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IMAPFolder newIMAPFolder(String str, char c) {
        return newIMAPFolder(str, c, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected com.sun.mail.imap.IMAPFolder newIMAPFolder(java.lang.String r5, char r6, java.lang.Boolean r7) {
        /*
            r4 = this;
            java.lang.reflect.Constructor<?> r0 = r4.folderConstructor
            if (r0 == 0) goto L2a
            r0 = 4
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch: java.lang.Exception -> L20
            r1 = 0
            r0[r1] = r5     // Catch: java.lang.Exception -> L20
            r1 = 1
            java.lang.Character r2 = java.lang.Character.valueOf(r6)     // Catch: java.lang.Exception -> L20
            r0[r1] = r2     // Catch: java.lang.Exception -> L20
            r1 = 2
            r0[r1] = r4     // Catch: java.lang.Exception -> L20
            r1 = 3
            r0[r1] = r7     // Catch: java.lang.Exception -> L20
            java.lang.reflect.Constructor<?> r1 = r4.folderConstructor     // Catch: java.lang.Exception -> L20
            java.lang.Object r0 = r1.newInstance(r0)     // Catch: java.lang.Exception -> L20
            com.sun.mail.imap.IMAPFolder r0 = (com.sun.mail.imap.IMAPFolder) r0     // Catch: java.lang.Exception -> L20
            goto L2b
        L20:
            r0 = move-exception
            com.sun.mail.util.MailLogger r1 = r4.logger
            java.util.logging.Level r2 = java.util.logging.Level.FINE
            java.lang.String r3 = "exception creating IMAPFolder class"
            r1.log(r2, r3, r0)
        L2a:
            r0 = 0
        L2b:
            if (r0 != 0) goto L32
            com.sun.mail.imap.IMAPFolder r0 = new com.sun.mail.imap.IMAPFolder
            r0.<init>(r5, r6, r4, r7)
        L32:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPStore.newIMAPFolder(java.lang.String, char, java.lang.Boolean):com.sun.mail.imap.IMAPFolder");
    }

    protected IMAPProtocol newIMAPProtocol(String str, int i) throws IOException, ProtocolException {
        return new IMAPProtocol(this.name, str, i, this.session.getProperties(), this.isSSL, this.logger);
    }

    protected void preLogin(IMAPProtocol iMAPProtocol) throws ProtocolException {
    }

    @Override // javax.mail.Service
    protected synchronized boolean protocolConnect(String str, int i, String str2, String str3) throws MessagingException {
        IMAPProtocol iMAPProtocol;
        boolean isEmpty;
        if (str == null || str3 == null || str2 == null) {
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("protocolConnect returning false, host=" + str + ", user=" + traceUser(str2) + ", password=" + tracePassword(str3));
            }
            return false;
        }
        if (i != -1) {
            this.port = i;
        } else {
            this.port = PropUtil.getIntSessionProperty(this.session, "mail." + this.name + ".port", this.port);
        }
        if (this.port == -1) {
            this.port = this.defaultPort;
        }
        try {
            try {
                synchronized (this.pool) {
                    isEmpty = this.pool.authenticatedConnections.isEmpty();
                }
                if (isEmpty) {
                    if (this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("trying to connect to host \"" + str + "\", port " + this.port + ", isSSL " + this.isSSL);
                    }
                    iMAPProtocol = newIMAPProtocol(str, this.port);
                    try {
                        if (this.logger.isLoggable(Level.FINE)) {
                            this.logger.fine("protocolConnect login, host=" + str + ", user=" + traceUser(str2) + ", password=" + tracePassword(str3));
                        }
                        iMAPProtocol.addResponseHandler(this.nonStoreResponseHandler);
                        login(iMAPProtocol, str2, str3);
                        iMAPProtocol.removeResponseHandler(this.nonStoreResponseHandler);
                        iMAPProtocol.addResponseHandler(this);
                        this.usingSSL = iMAPProtocol.isSSL();
                        this.host = str;
                        this.user = str2;
                        this.password = str3;
                        synchronized (this.pool) {
                            this.pool.authenticatedConnections.addElement(iMAPProtocol);
                        }
                    } catch (CommandFailedException e) {
                        e = e;
                        if (iMAPProtocol != null) {
                            iMAPProtocol.disconnect();
                        }
                        Response response = e.getResponse();
                        throw new AuthenticationFailedException(response != null ? response.getRest() : e.getMessage());
                    } catch (IMAPReferralException e2) {
                        e = e2;
                        if (iMAPProtocol != null) {
                            iMAPProtocol.disconnect();
                        }
                        throw new ReferralException(e.getUrl(), e.getMessage());
                    } catch (ProtocolException e3) {
                        e = e3;
                        if (iMAPProtocol != null) {
                            iMAPProtocol.disconnect();
                        }
                        throw new MessagingException(e.getMessage(), e);
                    }
                }
                return true;
            } catch (SocketConnectException e4) {
                throw new MailConnectException(e4);
            } catch (IOException e5) {
                throw new MessagingException(e5.getMessage(), e5);
            }
        } catch (CommandFailedException e6) {
            e = e6;
            iMAPProtocol = null;
        } catch (IMAPReferralException e7) {
            e = e7;
            iMAPProtocol = null;
        } catch (ProtocolException e8) {
            e = e8;
            iMAPProtocol = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseFolderStoreProtocol(IMAPProtocol iMAPProtocol) {
        if (iMAPProtocol == null) {
            return;
        }
        iMAPProtocol.removeResponseHandler(this.nonStoreResponseHandler);
        iMAPProtocol.addResponseHandler(this);
        synchronized (this.pool) {
            this.pool.storeConnectionInUse = false;
            this.pool.notifyAll();
            this.pool.logger.fine("releaseFolderStoreProtocol()");
            timeoutConnections();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseProtocol(IMAPFolder iMAPFolder, IMAPProtocol iMAPProtocol) {
        synchronized (this.pool) {
            if (iMAPProtocol != null) {
                if (isConnectionPoolFull()) {
                    this.logger.fine("pool is full, not adding an Authenticated connection");
                    try {
                        iMAPProtocol.logout();
                    } catch (ProtocolException unused) {
                    }
                } else {
                    iMAPProtocol.addResponseHandler(this);
                    this.pool.authenticatedConnections.addElement(iMAPProtocol);
                    if (this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("added an Authenticated connection -- size: " + this.pool.authenticatedConnections.size());
                    }
                }
            }
            if (this.pool.folders != null) {
                this.pool.folders.removeElement(iMAPFolder);
            }
            timeoutConnections();
        }
    }

    public synchronized void setPassword(String str) {
        this.password = str;
    }

    public void setProxyAuthUser(String str) {
        this.proxyAuthUser = str;
    }

    @Override // javax.mail.QuotaAwareStore
    public synchronized void setQuota(Quota quota) throws MessagingException {
        IMAPProtocol storeProtocol;
        checkConnected();
        IMAPProtocol iMAPProtocol = null;
        try {
            try {
                storeProtocol = getStoreProtocol();
            } catch (Throwable th) {
                th = th;
            }
        } catch (BadCommandException e) {
            e = e;
        } catch (ConnectionException e2) {
            e = e2;
        } catch (ProtocolException e3) {
            e = e3;
        }
        try {
            storeProtocol.setQuota(quota);
            releaseStoreProtocol(storeProtocol);
        } catch (BadCommandException e4) {
            e = e4;
            throw new MessagingException("QUOTA not supported", e);
        } catch (ConnectionException e5) {
            e = e5;
            throw new StoreClosedException(this, e.getMessage());
        } catch (ProtocolException e6) {
            e = e6;
            throw new MessagingException(e.getMessage(), e);
        } catch (Throwable th2) {
            th = th2;
            iMAPProtocol = storeProtocol;
            releaseStoreProtocol(iMAPProtocol);
            throw th;
        }
    }

    public synchronized void setUsername(String str) {
        this.user = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean throwSearchException() {
        return this.throwSearchException;
    }
}
