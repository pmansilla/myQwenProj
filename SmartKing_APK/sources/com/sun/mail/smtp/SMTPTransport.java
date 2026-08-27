package com.sun.mail.smtp;

import com.litesuits.orm.db.assit.SQLBuilder;
import com.sun.mail.auth.Ntlm;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.MailConnectException;
import com.sun.mail.util.MailLogger;
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.SocketConnectException;
import com.sun.mail.util.SocketFetcher;
import com.sun.mail.util.TraceInputStream;
import com.sun.mail.util.TraceOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.ParseException;
import javax.net.ssl.SSLSocket;

/* loaded from: classes2.dex */
public class SMTPTransport extends Transport {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String UNKNOWN = "UNKNOWN";
    private Address[] addresses;
    private boolean allowutf8;
    private Map<String, Authenticator> authenticators;
    private String authorizationID;
    private int chunkSize;
    private SMTPOutputStream dataStream;
    private boolean debugpassword;
    private boolean debugusername;
    private String defaultAuthenticationMechanisms;
    private int defaultPort;
    private boolean enableSASL;
    private MessagingException exception;
    private Hashtable<String, String> extMap;
    private String host;
    private Address[] invalidAddr;
    private boolean isSSL;
    private int lastReturnCode;
    private String lastServerResponse;
    private LineInputStream lineInputStream;
    private String localHostName;
    private MailLogger logger;
    private MimeMessage message;
    private String name;
    private boolean noauthdebug;
    private boolean noopStrict;
    private boolean notificationDone;
    private String ntlmDomain;
    private boolean quitWait;
    private boolean reportSuccess;
    private boolean requireStartTLS;
    private SaslAuthenticator saslAuthenticator;
    private String[] saslMechanisms;
    private String saslRealm;
    private boolean sendPartiallyFailed;
    private BufferedInputStream serverInput;
    private OutputStream serverOutput;
    private Socket serverSocket;
    private TraceInputStream traceInput;
    private MailLogger traceLogger;
    private TraceOutputStream traceOutput;
    private boolean useCanonicalHostName;
    private boolean useRset;
    private boolean useStartTLS;
    private Address[] validSentAddr;
    private Address[] validUnsentAddr;
    private static final String[] ignoreList = {"Bcc", "Content-Length"};
    private static final byte[] CRLF = {13, 10};
    private static final String[] UNKNOWN_SA = new String[0];
    private static char[] hexchar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public abstract class Authenticator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final boolean enabled;
        private final String mech;
        protected int resp;

        Authenticator(SMTPTransport sMTPTransport, String str) {
            this(str, true);
        }

        Authenticator(String str, boolean z) {
            this.mech = str.toUpperCase(Locale.ENGLISH);
            this.enabled = z;
        }

        boolean authenticate(String str, String str2, String str3, String str4) throws MessagingException {
            try {
                try {
                    String initialResponse = getInitialResponse(str, str2, str3, str4);
                    if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                        SMTPTransport.this.logger.fine("AUTH " + this.mech + " command trace suppressed");
                        SMTPTransport.this.suspendTracing();
                    }
                    if (initialResponse != null) {
                        SMTPTransport sMTPTransport = SMTPTransport.this;
                        StringBuilder sb = new StringBuilder();
                        sb.append("AUTH ");
                        sb.append(this.mech);
                        sb.append(SQLBuilder.BLANK);
                        sb.append(initialResponse.length() == 0 ? "=" : initialResponse);
                        this.resp = sMTPTransport.simpleCommand(sb.toString());
                    } else {
                        this.resp = SMTPTransport.this.simpleCommand("AUTH " + this.mech);
                    }
                    if (this.resp == 530) {
                        SMTPTransport.this.startTLS();
                        if (initialResponse != null) {
                            this.resp = SMTPTransport.this.simpleCommand("AUTH " + this.mech + SQLBuilder.BLANK + initialResponse);
                        } else {
                            this.resp = SMTPTransport.this.simpleCommand("AUTH " + this.mech);
                        }
                    }
                    if (this.resp == 334) {
                        doAuth(str, str2, str3, str4);
                    }
                    if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                        MailLogger mailLogger = SMTPTransport.this.logger;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("AUTH ");
                        sb2.append(this.mech);
                        sb2.append(SQLBuilder.BLANK);
                        sb2.append(this.resp == 235 ? "succeeded" : "failed");
                        mailLogger.fine(sb2.toString());
                    }
                    SMTPTransport.this.resumeTracing();
                    if (this.resp == 235) {
                        return true;
                    }
                    SMTPTransport.this.closeConnection();
                    throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse());
                } catch (IOException e) {
                    SMTPTransport.this.logger.log(Level.FINE, "AUTH " + this.mech + " failed", (Throwable) e);
                    if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                        MailLogger mailLogger2 = SMTPTransport.this.logger;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("AUTH ");
                        sb3.append(this.mech);
                        sb3.append(SQLBuilder.BLANK);
                        sb3.append(this.resp == 235 ? "succeeded" : "failed");
                        mailLogger2.fine(sb3.toString());
                    }
                    SMTPTransport.this.resumeTracing();
                    if (this.resp == 235) {
                        return true;
                    }
                    SMTPTransport.this.closeConnection();
                    throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse());
                } catch (Throwable th) {
                    SMTPTransport.this.logger.log(Level.FINE, "AUTH " + this.mech + " failed", th);
                    if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                        MailLogger mailLogger3 = SMTPTransport.this.logger;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("AUTH ");
                        sb4.append(this.mech);
                        sb4.append(SQLBuilder.BLANK);
                        sb4.append(this.resp == 235 ? "succeeded" : "failed");
                        mailLogger3.fine(sb4.toString());
                    }
                    SMTPTransport.this.resumeTracing();
                    if (this.resp == 235) {
                        return true;
                    }
                    SMTPTransport.this.closeConnection();
                    if (th instanceof Error) {
                        throw ((Error) th);
                    }
                    if (th instanceof Exception) {
                        throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse(), (Exception) th);
                    }
                    throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse());
                }
            } catch (Throwable th2) {
                if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                    MailLogger mailLogger4 = SMTPTransport.this.logger;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("AUTH ");
                    sb5.append(this.mech);
                    sb5.append(SQLBuilder.BLANK);
                    sb5.append(this.resp == 235 ? "succeeded" : "failed");
                    mailLogger4.fine(sb5.toString());
                }
                SMTPTransport.this.resumeTracing();
                if (this.resp == 235) {
                    throw th2;
                }
                SMTPTransport.this.closeConnection();
                throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse());
            }
        }

        abstract void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException;

        boolean enabled() {
            return this.enabled;
        }

        String getInitialResponse(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            return null;
        }

        String getMechanism() {
            return this.mech;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class BDATOutputStream extends SMTPOutputStream {
        public BDATOutputStream(OutputStream outputStream, int i) {
            super(new ChunkedOutputStream(outputStream, i));
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.out.close();
        }
    }

    /* loaded from: classes2.dex */
    private class ChunkedOutputStream extends OutputStream {
        private final byte[] buf;
        private int count = 0;
        private final OutputStream out;

        public ChunkedOutputStream(OutputStream outputStream, int i) {
            this.out = outputStream;
            this.buf = new byte[i];
        }

        private void bdat(byte[] bArr, int i, int i2, boolean z) throws IOException {
            if (i2 > 0 || z) {
                try {
                    if (z) {
                        SMTPTransport.this.sendCommand("BDAT " + i2 + " LAST");
                    } else {
                        SMTPTransport.this.sendCommand("BDAT " + i2);
                    }
                    this.out.write(bArr, i, i2);
                    this.out.flush();
                    if (SMTPTransport.this.readServerResponse() == 250) {
                    } else {
                        throw new IOException(SMTPTransport.this.lastServerResponse);
                    }
                } catch (MessagingException e) {
                    throw new IOException("BDAT write exception", e);
                }
            }
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            bdat(this.buf, 0, this.count, true);
            this.count = 0;
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            bdat(this.buf, 0, this.count, false);
            this.count = 0;
        }

        @Override // java.io.OutputStream
        public void write(int i) throws IOException {
            byte[] bArr = this.buf;
            int i2 = this.count;
            this.count = i2 + 1;
            bArr[i2] = (byte) i;
            if (this.count >= this.buf.length) {
                flush();
            }
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i, int i2) throws IOException {
            while (i2 > 0) {
                int min = Math.min(this.buf.length - this.count, i2);
                if (min == this.buf.length) {
                    bdat(bArr, i, min, false);
                } else {
                    System.arraycopy(bArr, i, this.buf, this.count, min);
                    this.count += min;
                }
                i += min;
                i2 -= min;
                if (this.count >= this.buf.length) {
                    flush();
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    private class DigestMD5Authenticator extends Authenticator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private DigestMD5 md5support;

        DigestMD5Authenticator() {
            super(SMTPTransport.this, "DIGEST-MD5");
        }

        private synchronized DigestMD5 getMD5() {
            if (this.md5support == null) {
                this.md5support = new DigestMD5(SMTPTransport.this.logger);
            }
            return this.md5support;
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            DigestMD5 md5 = getMD5();
            this.resp = SMTPTransport.this.simpleCommand(md5.authClient(str, str3, str4, SMTPTransport.this.getSASLRealm(), SMTPTransport.this.getLastServerResponse()));
            if (this.resp == 334) {
                if (md5.authServer(SMTPTransport.this.getLastServerResponse())) {
                    this.resp = SMTPTransport.this.simpleCommand(new byte[0]);
                } else {
                    this.resp = -1;
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    private class LoginAuthenticator extends Authenticator {
        LoginAuthenticator() {
            super(SMTPTransport.this, "LOGIN");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            this.resp = SMTPTransport.this.simpleCommand(BASE64EncoderStream.encode(str3.getBytes(StandardCharsets.UTF_8)));
            if (this.resp == 334) {
                this.resp = SMTPTransport.this.simpleCommand(BASE64EncoderStream.encode(str4.getBytes(StandardCharsets.UTF_8)));
            }
        }
    }

    /* loaded from: classes2.dex */
    private class NtlmAuthenticator extends Authenticator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private int flags;
        private Ntlm ntlm;

        NtlmAuthenticator() {
            super(SMTPTransport.this, "NTLM");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            this.resp = SMTPTransport.this.simpleCommand(this.ntlm.generateType3Msg(SMTPTransport.this.getLastServerResponse().substring(4).trim()));
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        String getInitialResponse(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            this.ntlm = new Ntlm(SMTPTransport.this.getNTLMDomain(), SMTPTransport.this.getLocalHost(), str3, str4, SMTPTransport.this.logger);
            this.flags = PropUtil.getIntProperty(SMTPTransport.this.session.getProperties(), "mail." + SMTPTransport.this.name + ".auth.ntlm.flags", 0);
            return this.ntlm.generateType1Msg(this.flags);
        }
    }

    /* loaded from: classes2.dex */
    private class OAuth2Authenticator extends Authenticator {
        OAuth2Authenticator() {
            super("XOAUTH2", false);
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            throw new AuthenticationFailedException("OAUTH2 asked for more");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        String getInitialResponse(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            return ASCIIUtility.toString(BASE64EncoderStream.encode(("user=" + str3 + "\u0001auth=Bearer " + str4 + "\u0001\u0001").getBytes(StandardCharsets.UTF_8)));
        }
    }

    /* loaded from: classes2.dex */
    private class PlainAuthenticator extends Authenticator {
        PlainAuthenticator() {
            super(SMTPTransport.this, "PLAIN");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            throw new AuthenticationFailedException("PLAIN asked for more");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        String getInitialResponse(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BASE64EncoderStream bASE64EncoderStream = new BASE64EncoderStream(byteArrayOutputStream, Integer.MAX_VALUE);
            if (str2 != null) {
                bASE64EncoderStream.write(str2.getBytes(StandardCharsets.UTF_8));
            }
            bASE64EncoderStream.write(0);
            bASE64EncoderStream.write(str3.getBytes(StandardCharsets.UTF_8));
            bASE64EncoderStream.write(0);
            bASE64EncoderStream.write(str4.getBytes(StandardCharsets.UTF_8));
            bASE64EncoderStream.flush();
            return ASCIIUtility.toString(byteArrayOutputStream.toByteArray());
        }
    }

    public SMTPTransport(Session session, URLName uRLName) {
        this(session, uRLName, "smtp", false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SMTPTransport(Session session, URLName uRLName, String str, boolean z) {
        super(session, uRLName);
        this.name = "smtp";
        this.defaultPort = 25;
        this.isSSL = false;
        this.sendPartiallyFailed = false;
        this.authenticators = new HashMap();
        this.quitWait = false;
        this.saslRealm = UNKNOWN;
        this.authorizationID = UNKNOWN;
        this.enableSASL = false;
        this.useCanonicalHostName = false;
        this.saslMechanisms = UNKNOWN_SA;
        this.ntlmDomain = UNKNOWN;
        this.noopStrict = true;
        this.noauthdebug = true;
        this.logger = new MailLogger(getClass(), "DEBUG SMTP", session);
        this.traceLogger = this.logger.getSubLogger("protocol", null);
        this.noauthdebug = !PropUtil.getBooleanSessionProperty(session, "mail.debug.auth", false);
        this.debugusername = PropUtil.getBooleanSessionProperty(session, "mail.debug.auth.username", true);
        this.debugpassword = PropUtil.getBooleanSessionProperty(session, "mail.debug.auth.password", false);
        str = uRLName != null ? uRLName.getProtocol() : str;
        this.name = str;
        z = z ? z : PropUtil.getBooleanSessionProperty(session, "mail." + str + ".ssl.enable", false);
        if (z) {
            this.defaultPort = 465;
        } else {
            this.defaultPort = 25;
        }
        this.isSSL = z;
        this.quitWait = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".quitwait", true);
        this.reportSuccess = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".reportsuccess", false);
        this.useStartTLS = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".starttls.enable", false);
        this.requireStartTLS = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".starttls.required", false);
        this.useRset = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".userset", false);
        this.noopStrict = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".noop.strict", true);
        this.enableSASL = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".sasl.enable", false);
        if (this.enableSASL) {
            this.logger.config("enable SASL");
        }
        this.useCanonicalHostName = PropUtil.getBooleanSessionProperty(session, "mail." + str + ".sasl.usecanonicalhostname", false);
        if (this.useCanonicalHostName) {
            this.logger.config("use canonical host name");
        }
        this.allowutf8 = PropUtil.getBooleanSessionProperty(session, "mail.mime.allowutf8", false);
        if (this.allowutf8) {
            this.logger.config("allow UTF-8");
        }
        this.chunkSize = PropUtil.getIntSessionProperty(session, "mail." + str + ".chunksize", -1);
        if (this.chunkSize > 0 && this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("chunk size " + this.chunkSize);
        }
        Authenticator[] authenticatorArr = {new LoginAuthenticator(), new PlainAuthenticator(), new DigestMD5Authenticator(), new NtlmAuthenticator(), new OAuth2Authenticator()};
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < authenticatorArr.length; i++) {
            this.authenticators.put(authenticatorArr[i].getMechanism(), authenticatorArr[i]);
            stringBuffer.append(authenticatorArr[i].getMechanism());
            stringBuffer.append(' ');
        }
        this.defaultAuthenticationMechanisms = stringBuffer.toString();
    }

    private void addressesFailed() {
        if (this.validSentAddr != null) {
            if (this.validUnsentAddr == null) {
                this.validUnsentAddr = this.validSentAddr;
                this.validSentAddr = null;
                return;
            }
            Address[] addressArr = new Address[this.validSentAddr.length + this.validUnsentAddr.length];
            System.arraycopy(this.validSentAddr, 0, addressArr, 0, this.validSentAddr.length);
            System.arraycopy(this.validUnsentAddr, 0, addressArr, this.validSentAddr.length, this.validUnsentAddr.length);
            this.validSentAddr = null;
            this.validUnsentAddr = addressArr;
        }
    }

    private boolean authenticate(String str, String str2) throws MessagingException {
        String property = this.session.getProperty("mail." + this.name + ".auth.mechanisms");
        if (property == null) {
            property = this.defaultAuthenticationMechanisms;
        }
        String authorizationId = getAuthorizationId();
        if (authorizationId == null) {
            authorizationId = str;
        }
        if (this.enableSASL) {
            this.logger.fine("Authenticate with SASL");
            try {
                if (sasllogin(getSASLMechanisms(), getSASLRealm(), authorizationId, str, str2)) {
                    return true;
                }
                this.logger.fine("SASL authentication failed");
                return false;
            } catch (UnsupportedOperationException e) {
                this.logger.log(Level.FINE, "SASL support failed", (Throwable) e);
            }
        }
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("Attempt to authenticate using mechanisms: " + property);
        }
        StringTokenizer stringTokenizer = new StringTokenizer(property);
        while (stringTokenizer.hasMoreTokens()) {
            String upperCase = stringTokenizer.nextToken().toUpperCase(Locale.ENGLISH);
            Authenticator authenticator = this.authenticators.get(upperCase);
            if (authenticator == null) {
                this.logger.log(Level.FINE, "no authenticator for mechanism {0}", upperCase);
            } else {
                if (supportsAuthentication(upperCase)) {
                    if (property == this.defaultAuthenticationMechanisms) {
                        String str3 = "mail." + this.name + ".auth." + upperCase.toLowerCase(Locale.ENGLISH) + ".disable";
                        if (PropUtil.getBooleanSessionProperty(this.session, str3, !authenticator.enabled())) {
                            if (this.logger.isLoggable(Level.FINE)) {
                                this.logger.fine("mechanism " + upperCase + " disabled by property: " + str3);
                            }
                        }
                    }
                    this.logger.log(Level.FINE, "Using mechanism {0}", upperCase);
                    return authenticator.authenticate(this.host, authorizationId, str, str2);
                }
                this.logger.log(Level.FINE, "mechanism {0} not supported by server", upperCase);
            }
        }
        throw new AuthenticationFailedException("No authentication mechanisms supported by both server and client");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void closeConnection() throws MessagingException {
        try {
            try {
                if (this.serverSocket != null) {
                    this.serverSocket.close();
                }
            } catch (IOException e) {
                throw new MessagingException("Server Close Failed", e);
            }
        } finally {
            this.serverSocket = null;
            this.serverOutput = null;
            this.serverInput = null;
            this.lineInputStream = null;
            if (super.isConnected()) {
                super.close();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean convertTo8Bit(MimePart mimePart) {
        InputStream inputStream;
        boolean z = false;
        try {
            if (!mimePart.isMimeType("text/*")) {
                if (!mimePart.isMimeType("multipart/*")) {
                    return false;
                }
                MimeMultipart mimeMultipart = (MimeMultipart) mimePart.getContent();
                int count = mimeMultipart.getCount();
                boolean z2 = false;
                for (int i = 0; i < count; i++) {
                    try {
                        if (convertTo8Bit((MimePart) mimeMultipart.getBodyPart(i))) {
                            z2 = true;
                        }
                    } catch (IOException | MessagingException unused) {
                    }
                }
                return z2;
            }
            String encoding = mimePart.getEncoding();
            if (encoding == null) {
                return false;
            }
            if (!encoding.equalsIgnoreCase("quoted-printable") && !encoding.equalsIgnoreCase("base64")) {
                return false;
            }
            try {
                inputStream = mimePart.getInputStream();
                try {
                    if (is8Bit(inputStream)) {
                        mimePart.setContent(mimePart.getContent(), mimePart.getContentType());
                        mimePart.setHeader("Content-Transfer-Encoding", "8bit");
                        z = true;
                    }
                    if (inputStream == null) {
                        return z;
                    }
                    inputStream.close();
                    return z;
                } catch (Throwable th) {
                    th = th;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException unused2) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = null;
            }
        } catch (IOException | MessagingException unused3) {
            return false;
        }
    }

    private void expandGroups() {
        ArrayList arrayList = null;
        for (int i = 0; i < this.addresses.length; i++) {
            InternetAddress internetAddress = (InternetAddress) this.addresses[i];
            if (internetAddress.isGroup()) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    for (int i2 = 0; i2 < i; i2++) {
                        arrayList.add(this.addresses[i2]);
                    }
                }
                try {
                    InternetAddress[] group = internetAddress.getGroup(true);
                    if (group != null) {
                        for (InternetAddress internetAddress2 : group) {
                            arrayList.add(internetAddress2);
                        }
                    } else {
                        arrayList.add(internetAddress);
                    }
                } catch (ParseException unused) {
                    arrayList.add(internetAddress);
                }
            } else if (arrayList != null) {
                arrayList.add(internetAddress);
            }
        }
        if (arrayList != null) {
            InternetAddress[] internetAddressArr = new InternetAddress[arrayList.size()];
            arrayList.toArray(internetAddressArr);
            this.addresses = internetAddressArr;
        }
    }

    private void initStreams() throws IOException {
        boolean booleanSessionProperty = PropUtil.getBooleanSessionProperty(this.session, "mail.debug.quote", false);
        this.traceInput = new TraceInputStream(this.serverSocket.getInputStream(), this.traceLogger);
        this.traceInput.setQuote(booleanSessionProperty);
        this.traceOutput = new TraceOutputStream(this.serverSocket.getOutputStream(), this.traceLogger);
        this.traceOutput.setQuote(booleanSessionProperty);
        this.serverOutput = new BufferedOutputStream(this.traceOutput);
        this.serverInput = new BufferedInputStream(this.traceInput);
        this.lineInputStream = new LineInputStream(this.serverInput);
    }

    private boolean is8Bit(InputStream inputStream) {
        boolean z = false;
        int i = 0;
        while (true) {
            try {
                int read = inputStream.read();
                if (read < 0) {
                    if (z) {
                        this.logger.fine("found an 8bit part");
                    }
                    return z;
                }
                int i2 = read & 255;
                if (i2 == 13 || i2 == 10) {
                    i = 0;
                } else if (i2 == 0 || (i = i + 1) > 998) {
                    return false;
                }
                if (i2 > 127) {
                    z = true;
                }
            } catch (IOException unused) {
                return false;
            }
        }
    }

    private boolean isNotLastLine(String str) {
        return str != null && str.length() >= 4 && str.charAt(3) == '-';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTracing() {
        return this.traceLogger.isLoggable(Level.FINEST);
    }

    private void issueSendCommand(String str, int i) throws MessagingException {
        sendCommand(str);
        int readServerResponse = readServerResponse();
        if (readServerResponse != i) {
            int length = this.validSentAddr == null ? 0 : this.validSentAddr.length;
            int length2 = this.validUnsentAddr == null ? 0 : this.validUnsentAddr.length;
            Address[] addressArr = new Address[length + length2];
            if (length > 0) {
                System.arraycopy(this.validSentAddr, 0, addressArr, 0, length);
            }
            if (length2 > 0) {
                System.arraycopy(this.validUnsentAddr, 0, addressArr, length, length2);
            }
            this.validSentAddr = null;
            this.validUnsentAddr = addressArr;
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("got response code " + readServerResponse + ", with response: " + this.lastServerResponse);
            }
            String str2 = this.lastServerResponse;
            int i2 = this.lastReturnCode;
            if (this.serverSocket != null) {
                issueCommand("RSET", -1);
            }
            this.lastServerResponse = str2;
            this.lastReturnCode = i2;
            throw new SMTPSendFailedException(str, readServerResponse, this.lastServerResponse, this.exception, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
        }
    }

    private String normalizeAddress(String str) {
        if (str.startsWith("<") || str.endsWith(">")) {
            return str;
        }
        return "<" + str + ">";
    }

    private void openServer() throws MessagingException {
        IOException e;
        int i;
        this.host = UNKNOWN;
        try {
            i = this.serverSocket.getPort();
            try {
                this.host = this.serverSocket.getInetAddress().getHostName();
                if (this.logger.isLoggable(Level.FINE)) {
                    this.logger.fine("starting protocol to host \"" + this.host + "\", port " + i);
                }
                initStreams();
                int readServerResponse = readServerResponse();
                if (readServerResponse == 220) {
                    if (this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("protocol started to host \"" + this.host + "\", port: " + i + "\n");
                        return;
                    }
                    return;
                }
                this.serverSocket.close();
                this.serverSocket = null;
                this.serverOutput = null;
                this.serverInput = null;
                this.lineInputStream = null;
                if (this.logger.isLoggable(Level.FINE)) {
                    this.logger.fine("got bad greeting from host \"" + this.host + "\", port: " + i + ", response: " + readServerResponse + "\n");
                }
                throw new MessagingException("Got bad greeting from SMTP host: " + this.host + ", port: " + i + ", response: " + readServerResponse);
            } catch (IOException e2) {
                e = e2;
                throw new MessagingException("Could not start protocol to SMTP host: " + this.host + ", port: " + i, e);
            }
        } catch (IOException e3) {
            e = e3;
            i = -1;
        }
    }

    private void openServer(String str, int i) throws MessagingException {
        int i2;
        IOException e;
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("trying to connect to host \"" + str + "\", port " + i + ", isSSL " + this.isSSL);
        }
        try {
            try {
                this.serverSocket = SocketFetcher.getSocket(str, i, this.session.getProperties(), "mail." + this.name, this.isSSL);
                i2 = this.serverSocket.getPort();
            } catch (IOException e2) {
                i2 = i;
                e = e2;
            }
            try {
                this.host = str;
                initStreams();
                int readServerResponse = readServerResponse();
                if (readServerResponse == 220) {
                    if (this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("connected to host \"" + str + "\", port: " + i2 + "\n");
                        return;
                    }
                    return;
                }
                this.serverSocket.close();
                this.serverSocket = null;
                this.serverOutput = null;
                this.serverInput = null;
                this.lineInputStream = null;
                if (this.logger.isLoggable(Level.FINE)) {
                    this.logger.fine("could not connect to host \"" + str + "\", port: " + i2 + ", response: " + readServerResponse + "\n");
                }
                throw new MessagingException("Could not connect to SMTP host: " + str + ", port: " + i2 + ", response: " + readServerResponse);
            } catch (IOException e3) {
                e = e3;
                throw new MessagingException("Could not connect to SMTP host: " + str + ", port: " + i2, e);
            }
        } catch (SocketConnectException e4) {
            throw new MailConnectException(e4);
        } catch (UnknownHostException e5) {
            throw new MessagingException("Unknown SMTP host: " + str, e5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resumeTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(true);
            this.traceOutput.setTrace(true);
        }
    }

    private boolean sasllogin(String[] strArr, String str, String str2, String str3, String str4) throws MessagingException {
        ArrayList arrayList;
        String str5;
        String canonicalHostName = this.useCanonicalHostName ? this.serverSocket.getInetAddress().getCanonicalHostName() : this.host;
        if (this.saslAuthenticator == null) {
            try {
                this.saslAuthenticator = (SaslAuthenticator) Class.forName("com.sun.mail.smtp.SMTPSaslAuthenticator").getConstructor(SMTPTransport.class, String.class, Properties.class, MailLogger.class, String.class).newInstance(this, this.name, this.session.getProperties(), this.logger, canonicalHostName);
            } catch (Exception e) {
                this.logger.log(Level.FINE, "Can't load SASL authenticator", (Throwable) e);
                return false;
            }
        }
        if (strArr == null || strArr.length <= 0) {
            arrayList = new ArrayList();
            if (this.extMap != null && (str5 = this.extMap.get("AUTH")) != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(str5);
                while (stringTokenizer.hasMoreTokens()) {
                    arrayList.add(stringTokenizer.nextToken());
                }
            }
        } else {
            arrayList = new ArrayList(strArr.length);
            for (int i = 0; i < strArr.length; i++) {
                if (supportsAuthentication(strArr[i])) {
                    arrayList.add(strArr[i]);
                }
            }
        }
        String[] strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
        try {
            if (this.noauthdebug && isTracing()) {
                this.logger.fine("SASL AUTH command trace suppressed");
                suspendTracing();
            }
            return this.saslAuthenticator.authenticate(strArr2, str, str2, str3, str4);
        } finally {
            resumeTracing();
        }
    }

    private void sendCommand(byte[] bArr) throws MessagingException {
        try {
            this.serverOutput.write(bArr);
            this.serverOutput.write(CRLF);
            this.serverOutput.flush();
        } catch (IOException e) {
            throw new MessagingException("Can't send command to SMTP host", e);
        }
    }

    private void sendMessageEnd() {
    }

    private void sendMessageStart(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void suspendTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(false);
            this.traceOutput.setTrace(false);
        }
    }

    private byte[] toBytes(String str) {
        return this.allowutf8 ? str.getBytes(StandardCharsets.UTF_8) : ASCIIUtility.getBytes(str);
    }

    private String tracePassword(String str) {
        return this.debugpassword ? str : str == null ? "<null>" : "<non-null>";
    }

    private String traceUser(String str) {
        return this.debugusername ? str : "<user name suppressed>";
    }

    protected static String xtext(String str) {
        return xtext(str, false);
    }

    protected static String xtext(String str, boolean z) {
        byte[] bytes = z ? str.getBytes(StandardCharsets.UTF_8) : ASCIIUtility.getBytes(str);
        StringBuffer stringBuffer = null;
        for (int i = 0; i < bytes.length; i++) {
            char c = (char) (bytes[i] & 255);
            if (!z && c >= 128) {
                throw new IllegalArgumentException("Non-ASCII character in SMTP submitter: " + str);
            }
            if (c < '!' || c > '~' || c == '+' || c == '=') {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer(str.length() + 4);
                    stringBuffer.append(str.substring(0, i));
                }
                stringBuffer.append('+');
                stringBuffer.append(hexchar[(c & 240) >> 4]);
                stringBuffer.append(hexchar[c & 15]);
            } else if (stringBuffer != null) {
                stringBuffer.append(c);
            }
        }
        return stringBuffer != null ? stringBuffer.toString() : str;
    }

    protected OutputStream bdat() throws MessagingException {
        this.dataStream = new BDATOutputStream(this.serverOutput, this.chunkSize);
        return this.dataStream;
    }

    protected void checkConnected() {
        if (!super.isConnected()) {
            throw new IllegalStateException("Not connected");
        }
    }

    @Override // javax.mail.Service, java.lang.AutoCloseable
    public synchronized void close() throws MessagingException {
        int readServerResponse;
        if (super.isConnected()) {
            try {
                if (this.serverSocket != null) {
                    sendCommand("QUIT");
                    if (this.quitWait && (readServerResponse = readServerResponse()) != 221 && readServerResponse != -1 && this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("QUIT failed with " + readServerResponse);
                    }
                }
            } finally {
                closeConnection();
            }
        }
    }

    public synchronized void connect(Socket socket) throws MessagingException {
        this.serverSocket = socket;
        super.connect();
    }

    protected OutputStream data() throws MessagingException {
        issueSendCommand("DATA", 354);
        this.dataStream = new SMTPOutputStream(this.serverOutput);
        return this.dataStream;
    }

    protected boolean ehlo(String str) throws MessagingException {
        String str2;
        if (str != null) {
            str2 = "EHLO " + str;
        } else {
            str2 = "EHLO";
        }
        sendCommand(str2);
        int readServerResponse = readServerResponse();
        if (readServerResponse == 250) {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(this.lastServerResponse));
            this.extMap = new Hashtable<>();
            boolean z = true;
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    if (z) {
                        z = false;
                    } else if (readLine.length() >= 5) {
                        String substring = readLine.substring(4);
                        int indexOf = substring.indexOf(32);
                        String str3 = "";
                        if (indexOf > 0) {
                            str3 = substring.substring(indexOf + 1);
                            substring = substring.substring(0, indexOf);
                        }
                        if (this.logger.isLoggable(Level.FINE)) {
                            this.logger.fine("Found extension \"" + substring + "\", arg \"" + str3 + "\"");
                        }
                        this.extMap.put(substring.toUpperCase(Locale.ENGLISH), str3);
                    }
                } catch (IOException unused) {
                }
            }
        }
        return readServerResponse == 250;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javax.mail.Service
    public void finalize() throws Throwable {
        try {
            closeConnection();
        } catch (MessagingException unused) {
        } catch (Throwable th) {
            super.finalize();
            throw th;
        }
        super.finalize();
    }

    protected void finishBdat() throws IOException, MessagingException {
        this.dataStream.ensureAtBOL();
        this.dataStream.close();
    }

    protected void finishData() throws IOException, MessagingException {
        this.dataStream.ensureAtBOL();
        issueSendCommand(".", 250);
    }

    public synchronized String getAuthorizationId() {
        if (this.authorizationID == UNKNOWN) {
            this.authorizationID = this.session.getProperty("mail." + this.name + ".sasl.authorizationid");
        }
        return this.authorizationID;
    }

    public String getExtensionParameter(String str) {
        if (this.extMap == null) {
            return null;
        }
        return this.extMap.get(str.toUpperCase(Locale.ENGLISH));
    }

    public synchronized int getLastReturnCode() {
        return this.lastReturnCode;
    }

    public synchronized String getLastServerResponse() {
        return this.lastServerResponse;
    }

    public synchronized String getLocalHost() {
        if (this.localHostName == null || this.localHostName.length() <= 0) {
            this.localHostName = this.session.getProperty("mail." + this.name + ".localhost");
        }
        if (this.localHostName == null || this.localHostName.length() <= 0) {
            this.localHostName = this.session.getProperty("mail." + this.name + ".localaddress");
        }
        try {
            if (this.localHostName == null || this.localHostName.length() <= 0) {
                InetAddress localHost = InetAddress.getLocalHost();
                this.localHostName = localHost.getCanonicalHostName();
                if (this.localHostName == null) {
                    this.localHostName = "[" + localHost.getHostAddress() + "]";
                }
            }
        } catch (UnknownHostException unused) {
        }
        if ((this.localHostName == null || this.localHostName.length() <= 0) && this.serverSocket != null && this.serverSocket.isBound()) {
            InetAddress localAddress = this.serverSocket.getLocalAddress();
            this.localHostName = localAddress.getCanonicalHostName();
            if (this.localHostName == null) {
                this.localHostName = "[" + localAddress.getHostAddress() + "]";
            }
        }
        return this.localHostName;
    }

    public synchronized String getNTLMDomain() {
        if (this.ntlmDomain == UNKNOWN) {
            this.ntlmDomain = this.session.getProperty("mail." + this.name + ".auth.ntlm.domain");
        }
        return this.ntlmDomain;
    }

    public synchronized boolean getNoopStrict() {
        return this.noopStrict;
    }

    public synchronized boolean getReportSuccess() {
        return this.reportSuccess;
    }

    public synchronized boolean getRequireStartTLS() {
        return this.requireStartTLS;
    }

    public synchronized boolean getSASLEnabled() {
        return this.enableSASL;
    }

    public synchronized String[] getSASLMechanisms() {
        if (this.saslMechanisms == UNKNOWN_SA) {
            ArrayList arrayList = new ArrayList(5);
            String property = this.session.getProperty("mail." + this.name + ".sasl.mechanisms");
            if (property != null && property.length() > 0) {
                if (this.logger.isLoggable(Level.FINE)) {
                    this.logger.fine("SASL mechanisms allowed: " + property);
                }
                StringTokenizer stringTokenizer = new StringTokenizer(property, " ,");
                while (stringTokenizer.hasMoreTokens()) {
                    String nextToken = stringTokenizer.nextToken();
                    if (nextToken.length() > 0) {
                        arrayList.add(nextToken);
                    }
                }
            }
            this.saslMechanisms = new String[arrayList.size()];
            arrayList.toArray(this.saslMechanisms);
        }
        if (this.saslMechanisms == null) {
            return null;
        }
        return (String[]) this.saslMechanisms.clone();
    }

    public synchronized String getSASLRealm() {
        if (this.saslRealm == UNKNOWN) {
            this.saslRealm = this.session.getProperty("mail." + this.name + ".sasl.realm");
            if (this.saslRealm == null) {
                this.saslRealm = this.session.getProperty("mail." + this.name + ".saslrealm");
            }
        }
        return this.saslRealm;
    }

    public synchronized boolean getStartTLS() {
        return this.useStartTLS;
    }

    public synchronized boolean getUseCanonicalHostName() {
        return this.useCanonicalHostName;
    }

    public synchronized boolean getUseRset() {
        return this.useRset;
    }

    protected void helo(String str) throws MessagingException {
        if (str == null) {
            issueCommand("HELO", 250);
            return;
        }
        issueCommand("HELO " + str, 250);
    }

    @Override // javax.mail.Service
    public synchronized boolean isConnected() {
        if (!super.isConnected()) {
            return false;
        }
        try {
            try {
                if (this.useRset) {
                    sendCommand("RSET");
                } else {
                    sendCommand("NOOP");
                }
                int readServerResponse = readServerResponse();
                if (readServerResponse >= 0 && (!this.noopStrict ? readServerResponse == 421 : readServerResponse != 250)) {
                    return true;
                }
                try {
                    closeConnection();
                } catch (MessagingException unused) {
                }
                return false;
            } catch (Exception unused2) {
                closeConnection();
                return false;
            }
        } catch (MessagingException unused3) {
            return false;
        }
    }

    public synchronized boolean isSSL() {
        return this.serverSocket instanceof SSLSocket;
    }

    public synchronized void issueCommand(String str, int i) throws MessagingException {
        sendCommand(str);
        int readServerResponse = readServerResponse();
        if (i != -1 && readServerResponse != i) {
            throw new MessagingException(this.lastServerResponse);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(6:56|57|(2:63|64)|65|66|64) */
    /* JADX WARN: Removed duplicated region for block: B:43:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0174  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void mailFrom() throws javax.mail.MessagingException {
        /*
            Method dump skipped, instructions count: 492
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.smtp.SMTPTransport.mailFrom():void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javax.mail.Transport
    public void notifyTransportListeners(int i, Address[] addressArr, Address[] addressArr2, Address[] addressArr3, Message message) {
        if (this.notificationDone) {
            return;
        }
        super.notifyTransportListeners(i, addressArr, addressArr2, addressArr3, message);
        this.notificationDone = true;
    }

    @Override // javax.mail.Service
    protected synchronized boolean protocolConnect(String str, int i, String str2, String str3) throws MessagingException {
        boolean booleanSessionProperty = PropUtil.getBooleanSessionProperty(this.session, "mail." + this.name + ".auth", false);
        if (booleanSessionProperty && (str2 == null || str3 == null)) {
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("need username and password for authentication");
                this.logger.fine("protocolConnect returning false, host=" + str + ", user=" + traceUser(str2) + ", password=" + tracePassword(str3));
            }
            return false;
        }
        boolean booleanSessionProperty2 = PropUtil.getBooleanSessionProperty(this.session, "mail." + this.name + ".ehlo", true);
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("useEhlo " + booleanSessionProperty2 + ", useAuth " + booleanSessionProperty);
        }
        if (i == -1) {
            i = PropUtil.getIntSessionProperty(this.session, "mail." + this.name + ".port", -1);
        }
        if (i == -1) {
            i = this.defaultPort;
        }
        if (str == null || str.length() == 0) {
            str = "localhost";
        }
        try {
            if (this.serverSocket != null) {
                openServer();
            } else {
                openServer(str, i);
            }
            if (!(booleanSessionProperty2 ? ehlo(getLocalHost()) : false)) {
                helo(getLocalHost());
            }
            if (this.useStartTLS || this.requireStartTLS) {
                if (this.serverSocket instanceof SSLSocket) {
                    this.logger.fine("STARTTLS requested but already using SSL");
                } else if (supportsExtension("STARTTLS")) {
                    startTLS();
                    ehlo(getLocalHost());
                } else if (this.requireStartTLS) {
                    this.logger.fine("STARTTLS required but not supported");
                    throw new MessagingException("STARTTLS is required but host does not support STARTTLS");
                }
            }
            if (this.allowutf8 && !supportsExtension("SMTPUTF8")) {
                this.logger.log(Level.INFO, "mail.mime.allowutf8 set but server doesn't advertise SMTPUTF8 support");
            }
            if ((!booleanSessionProperty && (str2 == null || str3 == null)) || (!supportsExtension("AUTH") && !supportsExtension("AUTH=LOGIN"))) {
                return true;
            }
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("protocolConnect login, host=" + str + ", user=" + traceUser(str2) + ", password=" + tracePassword(str3));
            }
            boolean authenticate = authenticate(str2, str3);
            if (!authenticate) {
            }
            return authenticate;
        } finally {
            try {
                closeConnection();
            } catch (MessagingException unused) {
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:27:0x00e1. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:28:0x00e4. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:29:0x00e7. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:125:0x02d3  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0322 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01a2  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0238  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void rcptTo() throws javax.mail.MessagingException {
        /*
            Method dump skipped, instructions count: 834
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.smtp.SMTPTransport.rcptTo():void");
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x006d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected int readServerResponse() throws javax.mail.MessagingException {
        /*
            r6 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r1 = 100
            r0.<init>(r1)
        L7:
            r1 = 0
            com.sun.mail.util.LineInputStream r2 = r6.lineInputStream     // Catch: java.io.IOException -> L7b
            java.lang.String r2 = r2.readLine()     // Catch: java.io.IOException -> L7b
            r3 = -1
            if (r2 != 0) goto L2b
            java.lang.String r0 = r0.toString()     // Catch: java.io.IOException -> L7b
            int r2 = r0.length()     // Catch: java.io.IOException -> L7b
            if (r2 != 0) goto L1d
            java.lang.String r0 = "[EOF]"
        L1d:
            r6.lastServerResponse = r0     // Catch: java.io.IOException -> L7b
            r6.lastReturnCode = r3     // Catch: java.io.IOException -> L7b
            com.sun.mail.util.MailLogger r2 = r6.logger     // Catch: java.io.IOException -> L7b
            java.util.logging.Level r4 = java.util.logging.Level.FINE     // Catch: java.io.IOException -> L7b
            java.lang.String r5 = "EOF: {0}"
            r2.log(r4, r5, r0)     // Catch: java.io.IOException -> L7b
            return r3
        L2b:
            r0.append(r2)     // Catch: java.io.IOException -> L7b
            java.lang.String r4 = "\n"
            r0.append(r4)     // Catch: java.io.IOException -> L7b
            boolean r2 = r6.isNotLastLine(r2)     // Catch: java.io.IOException -> L7b
            if (r2 != 0) goto L7
            java.lang.String r0 = r0.toString()     // Catch: java.io.IOException -> L7b
            int r2 = r0.length()
            r4 = 3
            if (r2 < r4) goto L6a
            java.lang.String r1 = r0.substring(r1, r4)     // Catch: java.lang.StringIndexOutOfBoundsException -> L4d java.lang.NumberFormatException -> L5c
            int r1 = java.lang.Integer.parseInt(r1)     // Catch: java.lang.StringIndexOutOfBoundsException -> L4d java.lang.NumberFormatException -> L5c
            goto L6b
        L4d:
            r6.close()     // Catch: javax.mail.MessagingException -> L51
            goto L6a
        L51:
            r1 = move-exception
            com.sun.mail.util.MailLogger r2 = r6.logger
            java.util.logging.Level r4 = java.util.logging.Level.FINE
            java.lang.String r5 = "close failed"
            r2.log(r4, r5, r1)
            goto L6a
        L5c:
            r6.close()     // Catch: javax.mail.MessagingException -> L60
            goto L6a
        L60:
            r1 = move-exception
            com.sun.mail.util.MailLogger r2 = r6.logger
            java.util.logging.Level r4 = java.util.logging.Level.FINE
            java.lang.String r5 = "close failed"
            r2.log(r4, r5, r1)
        L6a:
            r1 = -1
        L6b:
            if (r1 != r3) goto L76
            com.sun.mail.util.MailLogger r2 = r6.logger
            java.util.logging.Level r3 = java.util.logging.Level.FINE
            java.lang.String r4 = "bad server response: {0}"
            r2.log(r3, r4, r0)
        L76:
            r6.lastServerResponse = r0
            r6.lastReturnCode = r1
            return r1
        L7b:
            r0 = move-exception
            com.sun.mail.util.MailLogger r2 = r6.logger
            java.util.logging.Level r3 = java.util.logging.Level.FINE
            java.lang.String r4 = "exception reading response"
            r2.log(r3, r4, r0)
            java.lang.String r2 = ""
            r6.lastServerResponse = r2
            r6.lastReturnCode = r1
            javax.mail.MessagingException r1 = new javax.mail.MessagingException
            java.lang.String r2 = "Exception reading response"
            r1.<init>(r2, r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.smtp.SMTPTransport.readServerResponse():int");
    }

    protected void sendCommand(String str) throws MessagingException {
        sendCommand(toBytes(str));
    }

    @Override // javax.mail.Transport
    public synchronized void sendMessage(Message message, Address[] addressArr) throws MessagingException, SendFailedException {
        String subject;
        if (message != null) {
            try {
                subject = message.getSubject();
            } catch (Throwable th) {
                throw th;
            }
        } else {
            subject = "";
        }
        sendMessageStart(subject);
        checkConnected();
        if (!(message instanceof MimeMessage)) {
            this.logger.fine("Can only send RFC822 msgs");
            throw new MessagingException("SMTP can only send RFC822 messages");
        }
        for (int i = 0; i < addressArr.length; i++) {
            if (!(addressArr[i] instanceof InternetAddress)) {
                throw new MessagingException(addressArr[i] + " is not an InternetAddress");
            }
        }
        if (addressArr.length == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        this.message = (MimeMessage) message;
        this.addresses = addressArr;
        this.validUnsentAddr = addressArr;
        expandGroups();
        boolean allow8bitMIME = message instanceof SMTPMessage ? ((SMTPMessage) message).getAllow8bitMIME() : false;
        if (!allow8bitMIME) {
            allow8bitMIME = PropUtil.getBooleanSessionProperty(this.session, "mail." + this.name + ".allow8bitmime", false);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("use8bit " + allow8bitMIME);
        }
        if (allow8bitMIME && supportsExtension("8BITMIME") && convertTo8Bit(this.message)) {
            try {
                this.message.saveChanges();
            } catch (MessagingException unused) {
            }
        }
        try {
            try {
                try {
                    mailFrom();
                    rcptTo();
                    if (this.chunkSize <= 0 || !supportsExtension("CHUNKING")) {
                        this.message.writeTo(data(), ignoreList);
                        finishData();
                    } else {
                        this.message.writeTo(bdat(), ignoreList);
                        finishBdat();
                    }
                    if (this.sendPartiallyFailed) {
                        this.logger.fine("Sending partially failed because of invalid destination addresses");
                        notifyTransportListeners(3, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                        throw new SMTPSendFailedException(".", this.lastReturnCode, this.lastServerResponse, this.exception, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
                    }
                    this.logger.fine("message successfully delivered to mail server");
                    notifyTransportListeners(1, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                    this.invalidAddr = null;
                    this.validUnsentAddr = null;
                    this.validSentAddr = null;
                    this.addresses = null;
                    this.message = null;
                    this.exception = null;
                    this.sendPartiallyFailed = false;
                    this.notificationDone = false;
                    sendMessageEnd();
                } catch (MessagingException e) {
                    this.logger.log(Level.FINE, "MessagingException while sending", (Throwable) e);
                    if (e.getNextException() instanceof IOException) {
                        this.logger.fine("nested IOException, closing");
                        try {
                            closeConnection();
                        } catch (MessagingException unused2) {
                        }
                    }
                    addressesFailed();
                    notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                    throw e;
                }
            } catch (IOException e2) {
                this.logger.log(Level.FINE, "IOException while sending, closing", (Throwable) e2);
                try {
                    closeConnection();
                } catch (MessagingException unused3) {
                }
                addressesFailed();
                notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                throw new MessagingException("IOException while sending message", e2);
            }
        } catch (Throwable th2) {
            this.invalidAddr = null;
            this.validUnsentAddr = null;
            this.validSentAddr = null;
            this.addresses = null;
            this.message = null;
            this.exception = null;
            this.sendPartiallyFailed = false;
            this.notificationDone = false;
            throw th2;
        }
    }

    public synchronized void setAuthorizationID(String str) {
        this.authorizationID = str;
    }

    public synchronized void setLocalHost(String str) {
        this.localHostName = str;
    }

    public synchronized void setNTLMDomain(String str) {
        this.ntlmDomain = str;
    }

    public synchronized void setNoopStrict(boolean z) {
        this.noopStrict = z;
    }

    public synchronized void setReportSuccess(boolean z) {
        this.reportSuccess = z;
    }

    public synchronized void setRequireStartTLS(boolean z) {
        this.requireStartTLS = z;
    }

    public synchronized void setSASLEnabled(boolean z) {
        this.enableSASL = z;
    }

    public synchronized void setSASLMechanisms(String[] strArr) {
        if (strArr != null) {
            try {
                strArr = (String[]) strArr.clone();
            } catch (Throwable th) {
                throw th;
            }
        }
        this.saslMechanisms = strArr;
    }

    public synchronized void setSASLRealm(String str) {
        this.saslRealm = str;
    }

    public synchronized void setStartTLS(boolean z) {
        this.useStartTLS = z;
    }

    public synchronized void setUseCanonicalHostName(boolean z) {
        this.useCanonicalHostName = z;
    }

    public synchronized void setUseRset(boolean z) {
        this.useRset = z;
    }

    public synchronized int simpleCommand(String str) throws MessagingException {
        sendCommand(str);
        return readServerResponse();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int simpleCommand(byte[] bArr) throws MessagingException {
        sendCommand(bArr);
        return readServerResponse();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startTLS() throws MessagingException {
        issueCommand("STARTTLS", 220);
        try {
            this.serverSocket = SocketFetcher.startTLS(this.serverSocket, this.host, this.session.getProperties(), "mail." + this.name);
            initStreams();
        } catch (IOException e) {
            closeConnection();
            throw new MessagingException("Could not convert socket to TLS", e);
        }
    }

    protected boolean supportsAuthentication(String str) {
        String str2;
        if (this.extMap == null || (str2 = this.extMap.get("AUTH")) == null) {
            return false;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str2);
        while (stringTokenizer.hasMoreTokens()) {
            if (stringTokenizer.nextToken().equalsIgnoreCase(str)) {
                return true;
            }
        }
        if (!str.equalsIgnoreCase("LOGIN") || !supportsExtension("AUTH=LOGIN")) {
            return false;
        }
        this.logger.fine("use AUTH=LOGIN hack");
        return true;
    }

    public boolean supportsExtension(String str) {
        return (this.extMap == null || this.extMap.get(str.toUpperCase(Locale.ENGLISH)) == null) ? false : true;
    }
}
