package javax.mail;

import java.util.EventListener;
import java.util.Vector;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.MailEvent;

/* loaded from: classes2.dex */
public abstract class Service implements AutoCloseable {
    private boolean connected = false;
    private final Vector<ConnectionListener> connectionListeners = new Vector<>();
    protected boolean debug;
    private final EventQueue q;
    protected Session session;
    protected volatile URLName url;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00d5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public Service(javax.mail.Session r10, javax.mail.URLName r11) {
        /*
            Method dump skipped, instructions count: 236
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Service.<init>(javax.mail.Session, javax.mail.URLName):void");
    }

    public void addConnectionListener(ConnectionListener connectionListener) {
        this.connectionListeners.addElement(connectionListener);
    }

    @Override // java.lang.AutoCloseable
    public synchronized void close() throws MessagingException {
        setConnected(false);
        notifyConnectionListeners(3);
    }

    public void connect() throws MessagingException {
        connect(null, null, null);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:6|(5:8|(1:10)(1:88)|11|(1:13)(1:87)|(4:15|(2:17|18)|82|18)(3:(2:86|18)|82|18))(1:89)|(2:(1:21)|(1:23))|(1:25)|(1:27)|(3:77|78|79)|29|(1:76)(2:33|(1:(1:36)(2:72|(1:74)))(7:75|38|39|40|41|(5:61|62|63|64|(1:66))|(1:(1:(1:(2:47|48)(2:50|51))(2:52|53))(1:54))(4:55|(1:57)|58|59)))|37|38|39|40|41|(0)|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0110, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0111, code lost:
    
        r14 = r0;
        r0 = false;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:44:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x015b A[Catch: all -> 0x018a, TryCatch #1 {, blocks: (B:4:0x0005, B:6:0x000b, B:8:0x000f, B:10:0x0017, B:13:0x0025, B:15:0x002f, B:17:0x0037, B:18:0x0055, B:21:0x0069, B:23:0x0087, B:25:0x00a5, B:27:0x00af, B:78:0x00b9, B:31:0x00c3, B:33:0x00c7, B:36:0x00e9, B:40:0x010a, B:62:0x0115, B:64:0x011c, B:66:0x0128, B:47:0x0142, B:48:0x0149, B:50:0x014a, B:51:0x0151, B:52:0x0152, B:53:0x0159, B:54:0x015a, B:55:0x015b, B:57:0x016c, B:58:0x017a, B:72:0x00f2, B:74:0x00fc, B:84:0x0040, B:86:0x004c, B:90:0x0182, B:91:0x0189), top: B:3:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0115 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r15v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r15v3 */
    /* JADX WARN: Type inference failed for: r15v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void connect(java.lang.String r19, int r20, java.lang.String r21, java.lang.String r22) throws javax.mail.MessagingException {
        /*
            Method dump skipped, instructions count: 397
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Service.connect(java.lang.String, int, java.lang.String, java.lang.String):void");
    }

    public void connect(String str, String str2) throws MessagingException {
        connect(null, str, str2);
    }

    public void connect(String str, String str2, String str3) throws MessagingException {
        connect(str, -1, str2, str3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            this.q.terminateQueue();
        } finally {
            super.finalize();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventQueue getEventQueue() {
        return this.q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Session getSession() {
        return this.session;
    }

    public URLName getURLName() {
        URLName uRLName = this.url;
        return (uRLName == null || (uRLName.getPassword() == null && uRLName.getFile() == null)) ? uRLName : new URLName(uRLName.getProtocol(), uRLName.getHost(), uRLName.getPort(), null, uRLName.getUsername(), null);
    }

    public synchronized boolean isConnected() {
        return this.connected;
    }

    protected void notifyConnectionListeners(int i) {
        if (this.connectionListeners.size() > 0) {
            queueEvent(new ConnectionEvent(this, i), this.connectionListeners);
        }
        if (i == 3) {
            this.q.terminateQueue();
        }
    }

    protected boolean protocolConnect(String str, int i, String str2, String str3) throws MessagingException {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void queueEvent(MailEvent mailEvent, Vector<? extends EventListener> vector) {
        this.q.enqueue(mailEvent, (Vector) vector.clone());
    }

    public void removeConnectionListener(ConnectionListener connectionListener) {
        this.connectionListeners.removeElement(connectionListener);
    }

    protected synchronized void setConnected(boolean z) {
        this.connected = z;
    }

    protected void setURLName(URLName uRLName) {
        this.url = uRLName;
    }

    public String toString() {
        URLName uRLName = getURLName();
        return uRLName != null ? uRLName.toString() : super.toString();
    }
}
