package com.sun.mail.iap;

import com.litesuits.orm.db.assit.SQLBuilder;
import com.sun.mail.util.MailLogger;
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.SocketFetcher;
import com.sun.mail.util.TraceInputStream;
import com.sun.mail.util.TraceOutputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import javax.net.ssl.SSLSocket;
import me.panpf.sketch.SLog;

/* loaded from: classes2.dex */
public class Protocol {
    private static final byte[] CRLF = {13, 10};
    private final List<ResponseHandler> handlers;
    protected String host;
    private volatile ResponseInputStream input;
    private String localHostName;
    protected MailLogger logger;
    private volatile DataOutputStream output;
    protected String prefix;
    protected Properties props;
    protected boolean quote;
    private Socket socket;
    private int tagCounter;
    private volatile long timestamp;
    private TraceInputStream traceInput;
    protected MailLogger traceLogger;
    private TraceOutputStream traceOutput;

    public Protocol(InputStream inputStream, PrintStream printStream, Properties properties, boolean z) throws IOException {
        this.tagCounter = 0;
        this.handlers = new CopyOnWriteArrayList();
        this.host = "localhost";
        this.props = properties;
        this.quote = false;
        this.logger = new MailLogger(getClass(), SLog.LEVEL_NAME_DEBUG, z, System.out);
        this.traceLogger = this.logger.getSubLogger("protocol", null);
        this.traceInput = new TraceInputStream(inputStream, this.traceLogger);
        this.traceInput.setQuote(this.quote);
        this.input = new ResponseInputStream(this.traceInput);
        this.traceOutput = new TraceOutputStream(printStream, this.traceLogger);
        this.traceOutput.setQuote(this.quote);
        this.output = new DataOutputStream(new BufferedOutputStream(this.traceOutput));
        this.timestamp = System.currentTimeMillis();
    }

    public Protocol(String str, int i, Properties properties, String str2, boolean z, MailLogger mailLogger) throws IOException, ProtocolException {
        this.tagCounter = 0;
        this.handlers = new CopyOnWriteArrayList();
        try {
            this.host = str;
            this.props = properties;
            this.prefix = str2;
            this.logger = mailLogger;
            this.traceLogger = mailLogger.getSubLogger("protocol", null);
            this.socket = SocketFetcher.getSocket(str, i, properties, str2, z);
            this.quote = PropUtil.getBooleanProperty(properties, "mail.debug.quote", false);
            initStreams();
            processGreeting(readResponse());
            this.timestamp = System.currentTimeMillis();
        } catch (Throwable th) {
            disconnect();
            throw th;
        }
    }

    private void commandEnd() {
    }

    private void commandStart(String str) {
    }

    private void initStreams() throws IOException {
        this.traceInput = new TraceInputStream(this.socket.getInputStream(), this.traceLogger);
        this.traceInput.setQuote(this.quote);
        this.input = new ResponseInputStream(this.traceInput);
        this.traceOutput = new TraceOutputStream(this.socket.getOutputStream(), this.traceLogger);
        this.traceOutput.setQuote(this.quote);
        this.output = new DataOutputStream(new BufferedOutputStream(this.traceOutput));
    }

    public void addResponseHandler(ResponseHandler responseHandler) {
        this.handlers.add(responseHandler);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0026 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x005b A[Catch: all -> 0x0072, TryCatch #0 {, blocks: (B:3:0x0001, B:6:0x000c, B:11:0x0026, B:14:0x002a, B:17:0x0032, B:20:0x003b, B:36:0x005b, B:37:0x005e, B:43:0x0047, B:34:0x0054, B:51:0x0012, B:47:0x001b), top: B:2:0x0001, inners: #1, #2, #3, #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized com.sun.mail.iap.Response[] command(java.lang.String r8, com.sun.mail.iap.Argument r9) {
        /*
            r7 = this;
            monitor-enter(r7)
            r7.commandStart(r8)     // Catch: java.lang.Throwable -> L72
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L72
            r0.<init>()     // Catch: java.lang.Throwable -> L72
            r1 = 0
            r2 = 0
            r3 = 1
            java.lang.String r8 = r7.writeCommand(r8, r9)     // Catch: java.lang.Exception -> L11 com.sun.mail.iap.LiteralException -> L1a java.lang.Throwable -> L72
            goto L24
        L11:
            r8 = move-exception
            com.sun.mail.iap.Response r8 = com.sun.mail.iap.Response.byeResponse(r8)     // Catch: java.lang.Throwable -> L72
            r0.add(r8)     // Catch: java.lang.Throwable -> L72
            goto L22
        L1a:
            r8 = move-exception
            com.sun.mail.iap.Response r8 = r8.getResponse()     // Catch: java.lang.Throwable -> L72
            r0.add(r8)     // Catch: java.lang.Throwable -> L72
        L22:
            r8 = r2
        L23:
            r1 = 1
        L24:
            if (r1 != 0) goto L59
            com.sun.mail.iap.Response r9 = r7.readResponse()     // Catch: com.sun.mail.iap.ProtocolException -> L46 java.io.IOException -> L51 java.lang.Throwable -> L72
            boolean r4 = r9.isBYE()     // Catch: java.lang.Throwable -> L72
            if (r4 == 0) goto L32
            r2 = r9
            goto L24
        L32:
            r0.add(r9)     // Catch: java.lang.Throwable -> L72
            boolean r4 = r9.isTagged()     // Catch: java.lang.Throwable -> L72
            if (r4 == 0) goto L24
            java.lang.String r9 = r9.getTag()     // Catch: java.lang.Throwable -> L72
            boolean r9 = r9.equals(r8)     // Catch: java.lang.Throwable -> L72
            if (r9 == 0) goto L24
            goto L23
        L46:
            r9 = move-exception
            com.sun.mail.util.MailLogger r4 = r7.logger     // Catch: java.lang.Throwable -> L72
            java.util.logging.Level r5 = java.util.logging.Level.FINE     // Catch: java.lang.Throwable -> L72
            java.lang.String r6 = "ignoring bad response"
            r4.log(r5, r6, r9)     // Catch: java.lang.Throwable -> L72
            goto L24
        L51:
            r8 = move-exception
            if (r2 != 0) goto L59
            com.sun.mail.iap.Response r8 = com.sun.mail.iap.Response.byeResponse(r8)     // Catch: java.lang.Throwable -> L72
            r2 = r8
        L59:
            if (r2 == 0) goto L5e
            r0.add(r2)     // Catch: java.lang.Throwable -> L72
        L5e:
            int r8 = r0.size()     // Catch: java.lang.Throwable -> L72
            com.sun.mail.iap.Response[] r8 = new com.sun.mail.iap.Response[r8]     // Catch: java.lang.Throwable -> L72
            r0.toArray(r8)     // Catch: java.lang.Throwable -> L72
            long r0 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L72
            r7.timestamp = r0     // Catch: java.lang.Throwable -> L72
            r7.commandEnd()     // Catch: java.lang.Throwable -> L72
            monitor-exit(r7)
            return r8
        L72:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.iap.Protocol.command(java.lang.String, com.sun.mail.iap.Argument):com.sun.mail.iap.Response[]");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void disconnect() {
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException unused) {
            }
            this.socket = null;
        }
    }

    protected void finalize() throws Throwable {
        try {
            disconnect();
        } finally {
            super.finalize();
        }
    }

    public SocketChannel getChannel() {
        SocketChannel channel = this.socket.getChannel();
        if (channel != null || !(this.socket instanceof SSLSocket)) {
            return channel;
        }
        try {
            Field declaredField = this.socket.getClass().getDeclaredField("socket");
            declaredField.setAccessible(true);
            return ((Socket) declaredField.get(this.socket)).getChannel();
        } catch (Exception unused) {
            return channel;
        }
    }

    public InetAddress getInetAddress() {
        return this.socket.getInetAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ResponseInputStream getInputStream() {
        return this.input;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized String getLocalHost() {
        if (this.localHostName == null || this.localHostName.length() <= 0) {
            this.localHostName = this.props.getProperty(this.prefix + ".localhost");
        }
        if (this.localHostName == null || this.localHostName.length() <= 0) {
            this.localHostName = this.props.getProperty(this.prefix + ".localaddress");
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
        if ((this.localHostName == null || this.localHostName.length() <= 0) && this.socket != null && this.socket.isBound()) {
            InetAddress localAddress = this.socket.getLocalAddress();
            this.localHostName = localAddress.getCanonicalHostName();
            if (this.localHostName == null) {
                this.localHostName = "[" + localAddress.getHostAddress() + "]";
            }
        }
        return this.localHostName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public OutputStream getOutputStream() {
        return this.output;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ByteArray getResponseBuffer() {
        return null;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void handleResult(Response response) throws ProtocolException {
        if (response.isOK()) {
            return;
        }
        if (response.isNO()) {
            throw new CommandFailedException(response);
        }
        if (response.isBAD()) {
            throw new BadCommandException(response);
        }
        if (response.isBYE()) {
            disconnect();
            throw new ConnectionException(this, response);
        }
    }

    public boolean hasResponse() {
        try {
            return this.input.available() > 0;
        } catch (IOException unused) {
            return false;
        }
    }

    public boolean isSSL() {
        return this.socket instanceof SSLSocket;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isTracing() {
        return this.traceLogger.isLoggable(Level.FINEST);
    }

    public void notifyResponseHandlers(Response[] responseArr) {
        if (this.handlers.isEmpty()) {
            return;
        }
        for (Response response : responseArr) {
            if (response != null) {
                for (ResponseHandler responseHandler : this.handlers) {
                    if (responseHandler != null) {
                        responseHandler.handleResponse(response);
                    }
                }
            }
        }
    }

    protected void processGreeting(Response response) throws ProtocolException {
        if (response.isBYE()) {
            throw new ConnectionException(this, response);
        }
    }

    public Response readResponse() throws IOException, ProtocolException {
        return new Response(this);
    }

    public void removeResponseHandler(ResponseHandler responseHandler) {
        this.handlers.remove(responseHandler);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void resumeTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(true);
            this.traceOutput.setTrace(true);
        }
    }

    public void simpleCommand(String str, Argument argument) throws ProtocolException {
        Response[] command = command(str, argument);
        notifyResponseHandlers(command);
        handleResult(command[command.length - 1]);
    }

    public synchronized void startCompression(String str) throws IOException, ProtocolException {
        try {
            Constructor constructor = DeflaterOutputStream.class.getConstructor(OutputStream.class, Deflater.class, Boolean.TYPE);
            simpleCommand(str, null);
            this.traceInput = new TraceInputStream(new InflaterInputStream(this.socket.getInputStream(), new Inflater(true)), this.traceLogger);
            this.traceInput.setQuote(this.quote);
            this.input = new ResponseInputStream(this.traceInput);
            int intProperty = PropUtil.getIntProperty(this.props, this.prefix + ".compress.level", -1);
            int intProperty2 = PropUtil.getIntProperty(this.props, this.prefix + ".compress.strategy", 0);
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.log(Level.FINE, "Creating Deflater with compression level {0} and strategy {1}", Integer.valueOf(intProperty), Integer.valueOf(intProperty2));
            }
            Deflater deflater = new Deflater(-1, true);
            try {
                deflater.setLevel(intProperty);
            } catch (IllegalArgumentException e) {
                this.logger.log(Level.FINE, "Ignoring bad compression level", (Throwable) e);
            }
            try {
                deflater.setStrategy(intProperty2);
            } catch (IllegalArgumentException e2) {
                this.logger.log(Level.FINE, "Ignoring bad compression strategy", (Throwable) e2);
            }
            try {
                this.traceOutput = new TraceOutputStream((OutputStream) constructor.newInstance(this.socket.getOutputStream(), deflater, true), this.traceLogger);
                this.traceOutput.setQuote(this.quote);
                this.output = new DataOutputStream(new BufferedOutputStream(this.traceOutput));
            } catch (Exception e3) {
                throw new ProtocolException("can't create deflater", e3);
            }
        } catch (NoSuchMethodException unused) {
            this.logger.fine("Ignoring COMPRESS; missing JDK 1.7 DeflaterOutputStream constructor");
        }
    }

    public synchronized void startTLS(String str) throws IOException, ProtocolException {
        if (this.socket instanceof SSLSocket) {
            return;
        }
        simpleCommand(str, null);
        this.socket = SocketFetcher.startTLS(this.socket, this.host, this.props, this.prefix);
        initStreams();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized boolean supportsNonSyncLiterals() {
        return false;
    }

    public boolean supportsUtf8() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void suspendTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(false);
            this.traceOutput.setTrace(false);
        }
    }

    public String writeCommand(String str, Argument argument) throws IOException, ProtocolException {
        StringBuilder sb = new StringBuilder();
        sb.append("A");
        int i = this.tagCounter;
        this.tagCounter = i + 1;
        sb.append(Integer.toString(i, 10));
        String sb2 = sb.toString();
        this.output.writeBytes(sb2 + SQLBuilder.BLANK + str);
        if (argument != null) {
            this.output.write(32);
            argument.write(this);
        }
        this.output.write(CRLF);
        this.output.flush();
        return sb2;
    }
}
