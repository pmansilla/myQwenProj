package com.sun.mail.pop3;

import com.litesuits.orm.db.assit.SQLBuilder;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.MailLogger;
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.SocketFetcher;
import com.sun.mail.util.TraceInputStream;
import com.sun.mail.util.TraceOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.net.ssl.SSLSocket;

/* loaded from: classes2.dex */
class Protocol {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String CRLF = "\r\n";
    private static final int POP3_PORT = 110;
    private static final int SLOP = 128;
    private static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private String apopChallenge;
    private Map<String, String> capabilities = null;
    private String host;
    private BufferedReader input;
    private MailLogger logger;
    private boolean noauthdebug;
    private PrintWriter output;
    private boolean pipelining;
    private String prefix;
    private Properties props;
    private Socket socket;
    private TraceInputStream traceInput;
    private MailLogger traceLogger;
    private TraceOutputStream traceOutput;
    private boolean traceSuspended;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Protocol(String str, int i, MailLogger mailLogger, Properties properties, String str2, boolean z) throws IOException {
        this.apopChallenge = null;
        boolean z2 = true;
        this.noauthdebug = true;
        this.host = str;
        this.props = properties;
        this.prefix = str2;
        this.logger = mailLogger;
        this.traceLogger = mailLogger.getSubLogger("protocol", null);
        this.noauthdebug = !PropUtil.getBooleanProperty(properties, "mail.debug.auth", false);
        boolean boolProp = getBoolProp(properties, str2 + ".apop.enable");
        boolean boolProp2 = getBoolProp(properties, str2 + ".disablecapa");
        i = i == -1 ? 110 : i;
        try {
            if (mailLogger.isLoggable(Level.FINE)) {
                mailLogger.fine("connecting to host \"" + str + "\", port " + i + ", isSSL " + z);
            }
            this.socket = SocketFetcher.getSocket(str, i, properties, str2, z);
            initStreams();
            Response simpleCommand = simpleCommand(null);
            if (!simpleCommand.ok) {
                throw cleanupAndThrow(this.socket, new IOException("Connect failed"));
            }
            if (boolProp && simpleCommand.data != null) {
                int indexOf = simpleCommand.data.indexOf(60);
                int indexOf2 = simpleCommand.data.indexOf(62, indexOf);
                if (indexOf != -1 && indexOf2 != -1) {
                    this.apopChallenge = simpleCommand.data.substring(indexOf, indexOf2 + 1);
                }
                mailLogger.log(Level.FINE, "APOP challenge: {0}", this.apopChallenge);
            }
            if (!boolProp2) {
                setCapabilities(capa());
            }
            if (!hasCapability("PIPELINING")) {
                if (!PropUtil.getBooleanProperty(properties, str2 + ".pipelining", false)) {
                    z2 = false;
                }
            }
            this.pipelining = z2;
            if (this.pipelining) {
                mailLogger.config("PIPELINING enabled");
            }
        } catch (IOException e) {
            throw cleanupAndThrow(this.socket, e);
        }
    }

    private void batchCommandContinue(String str) {
    }

    private void batchCommandEnd() {
    }

    private void batchCommandStart(String str) {
    }

    private static IOException cleanupAndThrow(Socket socket, IOException iOException) {
        try {
            socket.close();
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

    private final synchronized boolean getBoolProp(Properties properties, String str) {
        boolean booleanProperty;
        booleanProperty = PropUtil.getBooleanProperty(properties, str, false);
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config(str + ": " + booleanProperty);
        }
        return booleanProperty;
    }

    private String getDigest(String str) {
        try {
            return toHex(MessageDigest.getInstance("MD5").digest((this.apopChallenge + str).getBytes("iso-8859-1")));
        } catch (UnsupportedEncodingException unused) {
            return null;
        } catch (NoSuchAlgorithmException unused2) {
            return null;
        }
    }

    private void initStreams() throws IOException {
        boolean booleanProperty = PropUtil.getBooleanProperty(this.props, "mail.debug.quote", false);
        this.traceInput = new TraceInputStream(this.socket.getInputStream(), this.traceLogger);
        this.traceInput.setQuote(booleanProperty);
        this.traceOutput = new TraceOutputStream(this.socket.getOutputStream(), this.traceLogger);
        this.traceOutput.setQuote(booleanProperty);
        this.input = new BufferedReader(new InputStreamReader(this.traceInput, "iso-8859-1"));
        this.output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.traceOutput, "iso-8859-1")));
    }

    private static boolean isRecoverable(Throwable th) {
        return (th instanceof Exception) || (th instanceof LinkageError);
    }

    private void issueCommand(String str) throws IOException {
        if (this.socket == null) {
            throw new IOException("Folder is closed");
        }
        if (str != null) {
            this.output.print(str + CRLF);
            this.output.flush();
        }
    }

    private Response multilineCommand(String str, int i) throws IOException {
        multilineCommandStart(str);
        issueCommand(str);
        Response readResponse = readResponse();
        if (!readResponse.ok) {
            multilineCommandEnd();
            return readResponse;
        }
        readResponse.bytes = readMultilineResponse(i);
        multilineCommandEnd();
        return readResponse;
    }

    private void multilineCommandEnd() {
    }

    private void multilineCommandStart(String str) {
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0021, code lost:
    
        r2 = r3.input.read();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.io.InputStream readMultilineResponse(int r4) throws java.io.IOException {
        /*
            r3 = this;
            com.sun.mail.util.SharedByteArrayOutputStream r0 = new com.sun.mail.util.SharedByteArrayOutputStream
            r0.<init>(r4)
            r4 = 10
            r1 = 10
        L9:
            java.io.BufferedReader r2 = r3.input     // Catch: java.io.InterruptedIOException -> L3c
            int r2 = r2.read()     // Catch: java.io.InterruptedIOException -> L3c
            if (r2 < 0) goto L2d
            if (r1 != r4) goto L28
            r1 = 46
            if (r2 != r1) goto L28
            java.io.BufferedReader r1 = r3.input     // Catch: java.io.InterruptedIOException -> L3c
            int r1 = r1.read()     // Catch: java.io.InterruptedIOException -> L3c
            r2 = 13
            if (r1 != r2) goto L29
            java.io.BufferedReader r4 = r3.input     // Catch: java.io.InterruptedIOException -> L3c
            int r2 = r4.read()     // Catch: java.io.InterruptedIOException -> L3c
            goto L2d
        L28:
            r1 = r2
        L29:
            r0.write(r1)     // Catch: java.io.InterruptedIOException -> L3c
            goto L9
        L2d:
            if (r2 < 0) goto L34
            java.io.InputStream r4 = r0.toStream()
            return r4
        L34:
            java.io.EOFException r4 = new java.io.EOFException
            java.lang.String r0 = "EOF on socket"
            r4.<init>(r0)
            throw r4
        L3c:
            r4 = move-exception
            java.net.Socket r0 = r3.socket     // Catch: java.io.IOException -> L42
            r0.close()     // Catch: java.io.IOException -> L42
        L42:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.pop3.Protocol.readMultilineResponse(int):java.io.InputStream");
    }

    private Response readResponse() throws IOException {
        try {
            String readLine = this.input.readLine();
            if (readLine == null) {
                this.traceLogger.finest("<EOF>");
                throw new EOFException("EOF on socket");
            }
            Response response = new Response();
            if (readLine.startsWith("+OK")) {
                response.ok = true;
            } else {
                if (!readLine.startsWith("-ERR")) {
                    throw new IOException("Unexpected response: " + readLine);
                }
                response.ok = false;
            }
            int indexOf = readLine.indexOf(32);
            if (indexOf >= 0) {
                response.data = readLine.substring(indexOf + 1);
            }
            return response;
        } catch (InterruptedIOException e) {
            try {
                this.socket.close();
            } catch (IOException unused) {
            }
            throw new EOFException(e.getMessage());
        } catch (SocketException e2) {
            try {
                this.socket.close();
            } catch (IOException unused2) {
            }
            throw new EOFException(e2.getMessage());
        }
    }

    private void resumeTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(true);
            this.traceOutput.setTrace(true);
        }
    }

    private Response simpleCommand(String str) throws IOException {
        simpleCommandStart(str);
        issueCommand(str);
        Response readResponse = readResponse();
        simpleCommandEnd();
        return readResponse;
    }

    private void simpleCommandEnd() {
    }

    private void simpleCommandStart(String str) {
    }

    private void suspendTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(false);
            this.traceOutput.setTrace(false);
        }
    }

    private static String toHex(byte[] bArr) {
        char[] cArr = new char[bArr.length * 2];
        int i = 0;
        for (byte b : bArr) {
            int i2 = b & 255;
            int i3 = i + 1;
            cArr[i] = digits[i2 >> 4];
            i = i3 + 1;
            cArr[i3] = digits[i2 & 15];
        }
        return new String(cArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized InputStream capa() throws IOException {
        Response multilineCommand = multilineCommand("CAPA", 128);
        if (!multilineCommand.ok) {
            return null;
        }
        return multilineCommand.bytes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        try {
            this.socket.close();
        } catch (IOException unused) {
        } catch (Throwable th) {
            this.socket = null;
            this.input = null;
            this.output = null;
            throw th;
        }
        this.socket = null;
        this.input = null;
        this.output = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean dele(int i) throws IOException {
        return simpleCommand("DELE " + i).ok;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.socket != null) {
                quit();
            }
        } finally {
            super.finalize();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Map<String, String> getCapabilities() {
        return this.capabilities;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean hasCapability(String str) {
        boolean z;
        if (this.capabilities != null) {
            z = this.capabilities.containsKey(str.toUpperCase(Locale.ENGLISH));
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isSSL() {
        return this.socket instanceof SSLSocket;
    }

    protected boolean isTracing() {
        return this.traceLogger.isLoggable(Level.FINEST);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int list(int i) throws IOException {
        int i2;
        Response simpleCommand = simpleCommand("LIST " + i);
        if (simpleCommand.ok && simpleCommand.data != null) {
            try {
                StringTokenizer stringTokenizer = new StringTokenizer(simpleCommand.data);
                stringTokenizer.nextToken();
                i2 = Integer.parseInt(stringTokenizer.nextToken());
            } catch (RuntimeException unused) {
            }
        }
        i2 = -1;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized InputStream list() throws IOException {
        return multilineCommand("LIST", 128).bytes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String login(String str, String str2) throws IOException {
        Response simpleCommand;
        boolean z = this.pipelining && (this.socket instanceof SSLSocket);
        try {
            if (this.noauthdebug && isTracing()) {
                this.logger.fine("authentication command trace suppressed");
                suspendTracing();
            }
            String digest = this.apopChallenge != null ? getDigest(str2) : null;
            if (this.apopChallenge != null && digest != null) {
                simpleCommand = simpleCommand("APOP " + str + SQLBuilder.BLANK + digest);
            } else if (z) {
                String str3 = "USER " + str;
                batchCommandStart(str3);
                issueCommand(str3);
                String str4 = "PASS " + str2;
                batchCommandContinue(str4);
                issueCommand(str4);
                Response readResponse = readResponse();
                if (!readResponse.ok) {
                    String str5 = readResponse.data != null ? readResponse.data : "USER command failed";
                    readResponse();
                    batchCommandEnd();
                    return str5;
                }
                simpleCommand = readResponse();
                batchCommandEnd();
            } else {
                Response simpleCommand2 = simpleCommand("USER " + str);
                if (!simpleCommand2.ok) {
                    return simpleCommand2.data != null ? simpleCommand2.data : "USER command failed";
                }
                simpleCommand = simpleCommand("PASS " + str2);
            }
            if (this.noauthdebug && isTracing()) {
                this.logger.log(Level.FINE, "authentication command {0}", simpleCommand.ok ? "succeeded" : "failed");
            }
            if (simpleCommand.ok) {
                return null;
            }
            return simpleCommand.data != null ? simpleCommand.data : "login failed";
        } finally {
            resumeTracing();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean noop() throws IOException {
        return simpleCommand("NOOP").ok;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean quit() throws IOException {
        try {
        } finally {
            close();
        }
        return simpleCommand("QUIT").ok;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:40:0x009d A[Catch: all -> 0x000a, TryCatch #3 {all -> 0x000a, blocks: (B:68:0x0004, B:7:0x0012, B:9:0x0048, B:12:0x004c, B:16:0x0063, B:18:0x006d, B:26:0x0088, B:28:0x0090, B:29:0x0098, B:30:0x0116, B:33:0x011c, B:35:0x0126, B:36:0x0142, B:40:0x009d, B:42:0x00bc, B:47:0x00c4, B:50:0x00c8, B:52:0x00df, B:56:0x00e8, B:58:0x00f2, B:66:0x010d), top: B:67:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0012 A[Catch: all -> 0x000a, TryCatch #3 {all -> 0x000a, blocks: (B:68:0x0004, B:7:0x0012, B:9:0x0048, B:12:0x004c, B:16:0x0063, B:18:0x006d, B:26:0x0088, B:28:0x0090, B:29:0x0098, B:30:0x0116, B:33:0x011c, B:35:0x0126, B:36:0x0142, B:40:0x009d, B:42:0x00bc, B:47:0x00c4, B:50:0x00c8, B:52:0x00df, B:56:0x00e8, B:58:0x00f2, B:66:0x010d), top: B:67:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized java.io.InputStream retr(int r6, int r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 328
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.pop3.Protocol.retr(int, int):java.io.InputStream");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0044, code lost:
    
        r2 = r5.input.read();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized boolean retr(int r6, java.io.OutputStream r7) throws java.io.IOException {
        /*
            r5 = this;
            monitor-enter(r5)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L90
            r0.<init>()     // Catch: java.lang.Throwable -> L90
            java.lang.String r1 = "RETR "
            r0.append(r1)     // Catch: java.lang.Throwable -> L90
            r0.append(r6)     // Catch: java.lang.Throwable -> L90
            java.lang.String r6 = r0.toString()     // Catch: java.lang.Throwable -> L90
            r5.multilineCommandStart(r6)     // Catch: java.lang.Throwable -> L90
            r5.issueCommand(r6)     // Catch: java.lang.Throwable -> L90
            com.sun.mail.pop3.Response r6 = r5.readResponse()     // Catch: java.lang.Throwable -> L90
            boolean r6 = r6.ok     // Catch: java.lang.Throwable -> L90
            if (r6 != 0) goto L26
            r5.multilineCommandEnd()     // Catch: java.lang.Throwable -> L90
            r6 = 0
            monitor-exit(r5)
            return r6
        L26:
            r6 = 0
            r0 = 10
            r1 = r6
            r6 = 10
        L2c:
            java.io.BufferedReader r2 = r5.input     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            int r2 = r2.read()     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            if (r2 < 0) goto L68
            if (r6 != r0) goto L4b
            r6 = 46
            if (r2 != r6) goto L4b
            java.io.BufferedReader r6 = r5.input     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            int r6 = r6.read()     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            r2 = 13
            if (r6 != r2) goto L4c
            java.io.BufferedReader r6 = r5.input     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            int r2 = r6.read()     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            goto L68
        L4b:
            r6 = r2
        L4c:
            if (r1 != 0) goto L2c
            r7.write(r6)     // Catch: java.lang.RuntimeException -> L52 java.io.IOException -> L5d java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            goto L2c
        L52:
            r1 = move-exception
            com.sun.mail.util.MailLogger r2 = r5.logger     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            java.util.logging.Level r3 = java.util.logging.Level.FINE     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            java.lang.String r4 = "exception while streaming"
            r2.log(r3, r4, r1)     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            goto L2c
        L5d:
            r1 = move-exception
            com.sun.mail.util.MailLogger r2 = r5.logger     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            java.util.logging.Level r3 = java.util.logging.Level.FINE     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            java.lang.String r4 = "exception while streaming"
            r2.log(r3, r4, r1)     // Catch: java.io.InterruptedIOException -> L89 java.lang.Throwable -> L90
            goto L2c
        L68:
            if (r2 < 0) goto L81
            if (r1 == 0) goto L7b
            boolean r6 = r1 instanceof java.io.IOException     // Catch: java.lang.Throwable -> L90
            if (r6 != 0) goto L78
            boolean r6 = r1 instanceof java.lang.RuntimeException     // Catch: java.lang.Throwable -> L90
            if (r6 != 0) goto L75
            goto L7b
        L75:
            java.lang.RuntimeException r1 = (java.lang.RuntimeException) r1     // Catch: java.lang.Throwable -> L90
            throw r1     // Catch: java.lang.Throwable -> L90
        L78:
            java.io.IOException r1 = (java.io.IOException) r1     // Catch: java.lang.Throwable -> L90
            throw r1     // Catch: java.lang.Throwable -> L90
        L7b:
            r5.multilineCommandEnd()     // Catch: java.lang.Throwable -> L90
            r6 = 1
            monitor-exit(r5)
            return r6
        L81:
            java.io.EOFException r6 = new java.io.EOFException     // Catch: java.lang.Throwable -> L90
            java.lang.String r7 = "EOF on socket"
            r6.<init>(r7)     // Catch: java.lang.Throwable -> L90
            throw r6     // Catch: java.lang.Throwable -> L90
        L89:
            r6 = move-exception
            java.net.Socket r7 = r5.socket     // Catch: java.io.IOException -> L8f java.lang.Throwable -> L90
            r7.close()     // Catch: java.io.IOException -> L8f java.lang.Throwable -> L90
        L8f:
            throw r6     // Catch: java.lang.Throwable -> L90
        L90:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.pop3.Protocol.retr(int, java.io.OutputStream):boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean rset() throws IOException {
        return simpleCommand("RSET").ok;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setCapabilities(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        if (inputStream == null) {
            this.capabilities = null;
        } else {
            this.capabilities = new HashMap(10);
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "us-ascii"));
            } catch (UnsupportedEncodingException unused) {
            }
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        int indexOf = readLine.indexOf(32);
                        this.capabilities.put((indexOf > 0 ? readLine.substring(0, indexOf) : readLine).toUpperCase(Locale.ENGLISH), readLine);
                    }
                } catch (IOException unused2) {
                } catch (Throwable th) {
                    try {
                        inputStream.close();
                    } catch (IOException unused3) {
                    }
                    throw th;
                }
                try {
                    break;
                } catch (IOException unused4) {
                    return;
                }
            }
            inputStream.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Status stat() throws IOException {
        Status status;
        Response simpleCommand = simpleCommand("STAT");
        status = new Status();
        if (!simpleCommand.ok) {
            throw new IOException("STAT command failed: " + simpleCommand.data);
        }
        if (simpleCommand.data != null) {
            try {
                StringTokenizer stringTokenizer = new StringTokenizer(simpleCommand.data);
                status.total = Integer.parseInt(stringTokenizer.nextToken());
                status.size = Integer.parseInt(stringTokenizer.nextToken());
            } catch (RuntimeException unused) {
            }
        }
        return status;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean stls() throws IOException {
        if (this.socket instanceof SSLSocket) {
            return true;
        }
        Response simpleCommand = simpleCommand("STLS");
        if (simpleCommand.ok) {
            try {
                this.socket = SocketFetcher.startTLS(this.socket, this.host, this.props, this.prefix);
                initStreams();
            } catch (IOException e) {
                try {
                    this.socket.close();
                    this.socket = null;
                    this.input = null;
                    this.output = null;
                    IOException iOException = new IOException("Could not convert socket to TLS");
                    iOException.initCause(e);
                    throw iOException;
                } catch (Throwable th) {
                    this.socket = null;
                    this.input = null;
                    this.output = null;
                    throw th;
                }
            }
        }
        return simpleCommand.ok;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized InputStream top(int i, int i2) throws IOException {
        return multilineCommand("TOP " + i + SQLBuilder.BLANK + i2, 0).bytes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String uidl(int i) throws IOException {
        Response simpleCommand = simpleCommand("UIDL " + i);
        if (!simpleCommand.ok) {
            return null;
        }
        int indexOf = simpleCommand.data.indexOf(32);
        if (indexOf <= 0) {
            return null;
        }
        return simpleCommand.data.substring(indexOf + 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean uidl(String[] strArr) throws IOException {
        int parseInt;
        Response multilineCommand = multilineCommand("UIDL", strArr.length * 15);
        if (!multilineCommand.ok) {
            return false;
        }
        LineInputStream lineInputStream = new LineInputStream(multilineCommand.bytes);
        while (true) {
            String readLine = lineInputStream.readLine();
            if (readLine != null) {
                int indexOf = readLine.indexOf(32);
                if (indexOf >= 1 && indexOf < readLine.length() && (parseInt = Integer.parseInt(readLine.substring(0, indexOf))) > 0 && parseInt <= strArr.length) {
                    strArr[parseInt - 1] = readLine.substring(indexOf + 1);
                }
            } else {
                try {
                    break;
                } catch (IOException unused) {
                }
            }
        }
        multilineCommand.bytes.close();
        return true;
    }
}
