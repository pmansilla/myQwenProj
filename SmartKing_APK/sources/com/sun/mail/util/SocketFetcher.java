package com.sun.mail.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.PrivilegedAction;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.SocketFactory;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/* loaded from: classes2.dex */
public class SocketFetcher {
    private static MailLogger logger = new MailLogger(SocketFetcher.class, "socket", "DEBUG SocketFetcher", PropUtil.getBooleanSystemProperty("mail.socket.debug", false), System.out);

    private SocketFetcher() {
    }

    private static void checkServerIdentity(String str, SSLSocket sSLSocket) throws IOException {
        try {
            Certificate[] peerCertificates = sSLSocket.getSession().getPeerCertificates();
            if (peerCertificates != null && peerCertificates.length > 0 && (peerCertificates[0] instanceof X509Certificate)) {
                if (matchCert(str, (X509Certificate) peerCertificates[0])) {
                    return;
                }
            }
            sSLSocket.close();
            throw new IOException("Can't verify identity of server: " + str);
        } catch (SSLPeerUnverifiedException e) {
            sSLSocket.close();
            IOException iOException = new IOException("Can't verify identity of server: " + str);
            iOException.initCause(e);
            throw iOException;
        }
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

    private static void configureSSLSocket(Socket socket, String str, Properties properties, String str2, SocketFactory socketFactory) throws IOException {
        if (socket instanceof SSLSocket) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            String property = properties.getProperty(str2 + ".ssl.protocols", null);
            if (property != null) {
                sSLSocket.setEnabledProtocols(stringArray(property));
            } else {
                String[] enabledProtocols = sSLSocket.getEnabledProtocols();
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("SSL enabled protocols before " + Arrays.asList(enabledProtocols));
                }
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < enabledProtocols.length; i++) {
                    if (enabledProtocols[i] != null && !enabledProtocols[i].startsWith("SSL")) {
                        arrayList.add(enabledProtocols[i]);
                    }
                }
                sSLSocket.setEnabledProtocols((String[]) arrayList.toArray(new String[arrayList.size()]));
            }
            String property2 = properties.getProperty(str2 + ".ssl.ciphersuites", null);
            if (property2 != null) {
                sSLSocket.setEnabledCipherSuites(stringArray(property2));
            }
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("SSL enabled protocols after " + Arrays.asList(sSLSocket.getEnabledProtocols()));
                logger.finer("SSL enabled ciphers after " + Arrays.asList(sSLSocket.getEnabledCipherSuites()));
            }
            sSLSocket.startHandshake();
            if (PropUtil.getBooleanProperty(properties, str2 + ".ssl.checkserveridentity", false)) {
                checkServerIdentity(str, sSLSocket);
            }
            if (!(socketFactory instanceof MailSSLSocketFactory) || ((MailSSLSocketFactory) socketFactory).isServerTrusted(str, sSLSocket)) {
                return;
            }
            throw cleanupAndThrow(sSLSocket, new IOException("Server is not trusted: " + str));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x018f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.net.Socket createSocket(java.net.InetAddress r19, int r20, java.lang.String r21, int r22, int r23, int r24, java.util.Properties r25, java.lang.String r26, javax.net.SocketFactory r27, boolean r28) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 782
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.SocketFetcher.createSocket(java.net.InetAddress, int, java.lang.String, int, int, int, java.util.Properties, java.lang.String, javax.net.SocketFactory, boolean):java.net.Socket");
    }

    private static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.mail.util.SocketFetcher.1
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

    public static Socket getSocket(String str, int i, Properties properties, String str2) throws IOException {
        return getSocket(str, i, properties, str2, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x01e8  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0200  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0203  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0228  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x023d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.net.Socket getSocket(java.lang.String r23, int r24, java.util.Properties r25, java.lang.String r26, boolean r27) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 613
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.SocketFetcher.getSocket(java.lang.String, int, java.util.Properties, java.lang.String, boolean):java.net.Socket");
    }

    private static SocketFactory getSocketFactory(String str) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> cls = null;
        if (str == null || str.length() == 0) {
            return null;
        }
        ClassLoader contextClassLoader = getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                cls = Class.forName(str, false, contextClassLoader);
            } catch (ClassNotFoundException unused) {
            }
        }
        if (cls == null) {
            cls = Class.forName(str);
        }
        return (SocketFactory) cls.getMethod("getDefault", new Class[0]).invoke(new Object(), new Object[0]);
    }

    private static boolean isRecoverable(Throwable th) {
        return (th instanceof Exception) || (th instanceof LinkageError);
    }

    private static boolean matchCert(String str, X509Certificate x509Certificate) {
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("matchCert server " + str + ", cert " + x509Certificate);
        }
        try {
            Class<?> cls = Class.forName("sun.security.util.HostnameChecker");
            Object invoke = cls.getMethod("getInstance", Byte.TYPE).invoke(new Object(), (byte) 2);
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("using sun.security.util.HostnameChecker");
            }
            try {
                cls.getMethod("match", String.class, X509Certificate.class).invoke(invoke, str, x509Certificate);
                return true;
            } catch (InvocationTargetException e) {
                logger.log(Level.FINER, "HostnameChecker FAIL", (Throwable) e);
                return false;
            }
        } catch (Exception e2) {
            logger.log(Level.FINER, "NO sun.security.util.HostnameChecker", (Throwable) e2);
            try {
                Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
                if (subjectAlternativeNames != null) {
                    boolean z = false;
                    for (List<?> list : subjectAlternativeNames) {
                        if (((Integer) list.get(0)).intValue() == 2) {
                            String str2 = (String) list.get(1);
                            if (logger.isLoggable(Level.FINER)) {
                                logger.finer("found name: " + str2);
                            }
                            if (matchServer(str, str2)) {
                                return true;
                            }
                            z = true;
                        }
                    }
                    if (z) {
                        return false;
                    }
                }
            } catch (CertificateParsingException unused) {
            }
            Matcher matcher = Pattern.compile("CN=([^,]*)").matcher(x509Certificate.getSubjectX500Principal().getName());
            return matcher.find() && matchServer(str, matcher.group(1).trim());
        }
    }

    private static boolean matchServer(String str, String str2) {
        int length;
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("match server " + str + " with " + str2);
        }
        if (!str2.startsWith("*.")) {
            return str.equalsIgnoreCase(str2);
        }
        String substring = str2.substring(2);
        return substring.length() != 0 && (length = str.length() - substring.length()) >= 1 && str.charAt(length + (-1)) == '.' && str.regionMatches(true, length, substring, 0, substring.length());
    }

    private static void proxyConnect(Socket socket, String str, int i, String str2, int i2, int i3) throws IOException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("connecting through proxy " + str + ":" + i + " to " + str2 + ":" + i2);
        }
        if (i3 >= 0) {
            socket.connect(new InetSocketAddress(str, i), i3);
        } else {
            socket.connect(new InetSocketAddress(str, i));
        }
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.print("CONNECT " + str2 + ":" + i2 + " HTTP/1.0\r\n\r\n");
        printStream.flush();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        boolean z = true;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return;
            }
            logger.finest(readLine);
            if (readLine.length() == 0) {
                return;
            }
            if (z) {
                StringTokenizer stringTokenizer = new StringTokenizer(readLine);
                stringTokenizer.nextToken();
                if (!stringTokenizer.nextToken().equals("200")) {
                    try {
                        socket.close();
                    } catch (IOException unused) {
                    }
                    ConnectException connectException = new ConnectException("connection through proxy " + str + ":" + i + " to " + str2 + ":" + i2 + " failed: " + readLine);
                    logger.log(Level.FINE, "connect failed", (Throwable) connectException);
                    throw connectException;
                }
                z = false;
            }
        }
    }

    @Deprecated
    public static Socket startTLS(Socket socket) throws IOException {
        return startTLS(socket, new Properties(), "socket");
    }

    public static Socket startTLS(Socket socket, String str, Properties properties, String str2) throws IOException {
        Object obj;
        String str3;
        SSLSocketFactory sSLSocketFactory;
        int port = socket.getPort();
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("startTLS host " + str + ", port " + port);
        }
        String str4 = "unknown socket factory";
        try {
            Object obj2 = properties.get(str2 + ".ssl.socketFactory");
            SSLSocketFactory sSLSocketFactory2 = null;
            sSLSocketFactory2 = null;
            if (obj2 instanceof SocketFactory) {
                obj = (SocketFactory) obj2;
                str4 = "SSL socket factory instance " + obj;
            } else {
                obj = null;
            }
            if (obj == null) {
                String property = properties.getProperty(str2 + ".ssl.socketFactory.class");
                str4 = "SSL socket factory class " + property;
                obj = getSocketFactory(property);
            }
            if (obj != null && (obj instanceof SSLSocketFactory)) {
                sSLSocketFactory2 = (SSLSocketFactory) obj;
            }
            if (sSLSocketFactory2 == null) {
                Object obj3 = properties.get(str2 + ".socketFactory");
                if (obj3 instanceof SocketFactory) {
                    obj = (SocketFactory) obj3;
                    str4 = "socket factory instance " + obj;
                }
                if (obj == null) {
                    String property2 = properties.getProperty(str2 + ".socketFactory.class");
                    str4 = "socket factory class " + property2;
                    obj = getSocketFactory(property2);
                }
                if (obj != null && (obj instanceof SSLSocketFactory)) {
                    sSLSocketFactory2 = (SSLSocketFactory) obj;
                }
            }
            SSLSocketFactory sSLSocketFactory3 = sSLSocketFactory2;
            if (sSLSocketFactory2 == null) {
                String property3 = properties.getProperty(str2 + ".ssl.trust");
                if (property3 != null) {
                    try {
                        MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
                        if (property3.equals("*")) {
                            mailSSLSocketFactory.setTrustAllHosts(true);
                        } else {
                            mailSSLSocketFactory.setTrustedHosts(property3.split("\\s+"));
                        }
                        str3 = "mail SSL socket factory";
                        sSLSocketFactory = mailSSLSocketFactory;
                    } catch (GeneralSecurityException e) {
                        IOException iOException = new IOException("Can't create MailSSLSocketFactory");
                        iOException.initCause(e);
                        throw iOException;
                    }
                } else {
                    str3 = "default SSL socket factory";
                    sSLSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                }
                sSLSocketFactory3 = sSLSocketFactory;
            }
            Socket createSocket = sSLSocketFactory3.createSocket(socket, str, port, true);
            configureSSLSocket(createSocket, str, properties, str2, sSLSocketFactory3);
            return createSocket;
        } catch (Exception e2) {
            e = e2;
            if (e instanceof InvocationTargetException) {
                Throwable targetException = ((InvocationTargetException) e).getTargetException();
                if (targetException instanceof Exception) {
                    e = (Exception) targetException;
                }
            }
            if (e instanceof IOException) {
                throw ((IOException) e);
            }
            IOException iOException2 = new IOException("Exception in startTLS using " + str4 + ": host, port: " + str + ", " + port + "; Exception: " + e);
            iOException2.initCause(e);
            throw iOException2;
        }
    }

    @Deprecated
    public static Socket startTLS(Socket socket, Properties properties, String str) throws IOException {
        return startTLS(socket, socket.getInetAddress().getHostName(), properties, str);
    }

    private static String[] stringArray(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        ArrayList arrayList = new ArrayList();
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(stringTokenizer.nextToken());
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }
}
