package com.sun.mail.imap;

import com.litesuits.orm.db.assit.SQLBuilder;
import com.sun.mail.iap.BadCommandException;
import com.sun.mail.iap.CommandFailedException;
import com.sun.mail.iap.ConnectionException;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.iap.ResponseHandler;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.protocol.FLAGS;
import com.sun.mail.imap.protocol.FetchItem;
import com.sun.mail.imap.protocol.FetchResponse;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.imap.protocol.IMAPResponse;
import com.sun.mail.imap.protocol.Item;
import com.sun.mail.imap.protocol.ListInfo;
import com.sun.mail.imap.protocol.MODSEQ;
import com.sun.mail.imap.protocol.MailboxInfo;
import com.sun.mail.imap.protocol.MessageSet;
import com.sun.mail.imap.protocol.Status;
import com.sun.mail.imap.protocol.UID;
import com.sun.mail.imap.protocol.UIDSet;
import com.sun.mail.util.MailLogger;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Quota;
import javax.mail.ReadOnlyFolderException;
import javax.mail.StoreClosedException;
import javax.mail.UIDFolder;
import javax.mail.event.MailEvent;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchException;
import javax.mail.search.SearchTerm;

/* loaded from: classes2.dex */
public class IMAPFolder extends Folder implements UIDFolder, ResponseHandler {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ABORTING = 2;
    private static final int IDLE = 1;
    private static final int RUNNING = 0;
    protected static final char UNKNOWN_SEPARATOR = 65535;
    protected volatile String[] attributes;
    protected Flags availableFlags;
    private Status cachedStatus;
    private long cachedStatusTime;
    private MailLogger connectionPoolLogger;
    private boolean doExpungeNotification;
    protected volatile boolean exists;
    protected volatile String fullName;
    private boolean hasMessageCountListener;
    private volatile long highestmodseq;
    private IdleManager idleManager;
    private int idleState;
    protected boolean isNamespace;
    protected MailLogger logger;
    protected MessageCache messageCache;
    protected final Object messageCacheLock;
    protected String name;
    private volatile boolean opened;
    protected Flags permanentFlags;
    protected volatile IMAPProtocol protocol;
    private int realTotal;
    private boolean reallyClosed;
    private volatile int recent;
    protected char separator;
    private volatile int total;
    protected int type;
    private boolean uidNotSticky;
    protected Hashtable<Long, IMAPMessage> uidTable;
    private long uidnext;
    private long uidvalidity;

    /* loaded from: classes2.dex */
    public static class FetchProfileItem extends FetchProfile.Item {
        public static final FetchProfileItem HEADERS = new FetchProfileItem("HEADERS");

        @Deprecated
        public static final FetchProfileItem SIZE = new FetchProfileItem("SIZE");
        public static final FetchProfileItem MESSAGE = new FetchProfileItem("MESSAGE");
        public static final FetchProfileItem INTERNALDATE = new FetchProfileItem("INTERNALDATE");

        protected FetchProfileItem(String str) {
            super(str);
        }
    }

    /* loaded from: classes2.dex */
    public interface ProtocolCommand {
        Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IMAPFolder(ListInfo listInfo, IMAPStore iMAPStore) {
        this(listInfo.name, listInfo.separator, iMAPStore, null);
        if (listInfo.hasInferiors) {
            this.type |= 2;
        }
        if (listInfo.canOpen) {
            this.type |= 1;
        }
        this.exists = true;
        this.attributes = listInfo.attrs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IMAPFolder(String str, char c, IMAPStore iMAPStore, Boolean bool) {
        super(iMAPStore);
        int indexOf;
        this.isNamespace = false;
        this.messageCacheLock = new Object();
        this.opened = false;
        this.reallyClosed = true;
        this.idleState = 0;
        this.total = -1;
        this.recent = -1;
        this.realTotal = -1;
        this.uidvalidity = -1L;
        this.uidnext = -1L;
        this.uidNotSticky = false;
        this.highestmodseq = -1L;
        this.doExpungeNotification = true;
        this.cachedStatus = null;
        this.cachedStatusTime = 0L;
        this.hasMessageCountListener = false;
        if (str == null) {
            throw new NullPointerException("Folder name is null");
        }
        this.fullName = str;
        this.separator = c;
        this.logger = new MailLogger(getClass(), "DEBUG IMAP", iMAPStore.getSession());
        this.connectionPoolLogger = iMAPStore.getConnectionPoolLogger();
        this.isNamespace = false;
        if (c != 65535 && c != 0 && (indexOf = this.fullName.indexOf(c)) > 0 && indexOf == this.fullName.length() - 1) {
            this.fullName = this.fullName.substring(0, indexOf);
            this.isNamespace = true;
        }
        if (bool != null) {
            this.isNamespace = bool.booleanValue();
        }
    }

    private void addSuppressed(Throwable th, Throwable th2) {
        if (isRecoverable(th2)) {
            th.addSuppressed(th2);
            return;
        }
        th2.addSuppressed(th);
        if (th2 instanceof Error) {
            throw ((Error) th2);
        }
        if (!(th2 instanceof RuntimeException)) {
            throw new RuntimeException("unexpected exception", th2);
        }
        throw ((RuntimeException) th2);
    }

    private void checkFlags(Flags flags) throws MessagingException {
        if (this.mode == 2) {
            return;
        }
        throw new IllegalStateException("Cannot change flags on READ_ONLY folder: " + this.fullName);
    }

    private void cleanup(boolean z) {
        releaseProtocol(z);
        this.messageCache = null;
        this.uidTable = null;
        this.exists = false;
        this.attributes = null;
        this.opened = false;
        this.idleState = 0;
        this.messageCacheLock.notifyAll();
        notifyConnectionListeners(3);
    }

    private MessagingException cleanupAndThrow(MessagingException messagingException) {
        try {
            try {
                this.protocol.close();
                releaseProtocol(true);
            } catch (ProtocolException e) {
                try {
                    addSuppressed(messagingException, logoutAndThrow(e.getMessage(), e));
                    releaseProtocol(false);
                } catch (Throwable th) {
                    releaseProtocol(false);
                    throw th;
                }
            }
        } catch (Throwable th2) {
            addSuppressed(messagingException, th2);
        }
        return messagingException;
    }

    private void close(boolean z, boolean z2) throws MessagingException {
        boolean z3;
        synchronized (this.messageCacheLock) {
            if (!this.opened && this.reallyClosed) {
                throw new IllegalStateException("This operation is not allowed on a closed folder");
            }
            boolean z4 = true;
            this.reallyClosed = true;
            try {
                if (this.opened) {
                    try {
                        waitIfIdle();
                        if (z2) {
                            this.logger.log(Level.FINE, "forcing folder {0} to close", this.fullName);
                            if (this.protocol != null) {
                                this.protocol.disconnect();
                            }
                        } else if (((IMAPStore) this.store).isConnectionPoolFull()) {
                            this.logger.fine("pool is full, not adding an Authenticated connection");
                            if (z && this.protocol != null) {
                                this.protocol.close();
                            }
                            if (this.protocol != null) {
                                this.protocol.logout();
                            }
                        } else if (!z && this.mode == 2) {
                            try {
                                if (this.protocol != null && this.protocol.hasCapability("UNSELECT")) {
                                    this.protocol.unselect();
                                } else if (this.protocol != null) {
                                    try {
                                        this.protocol.examine(this.fullName);
                                        z3 = true;
                                    } catch (CommandFailedException unused) {
                                        z3 = false;
                                    }
                                    if (z3 && this.protocol != null) {
                                        this.protocol.close();
                                    }
                                }
                            } catch (ProtocolException unused2) {
                                z4 = false;
                            }
                        } else if (this.protocol != null) {
                            this.protocol.close();
                        }
                    } catch (ProtocolException e) {
                        throw new MessagingException(e.getMessage(), e);
                    }
                }
            } finally {
                if (this.opened) {
                    cleanup(true);
                }
            }
        }
    }

    private synchronized void copymoveMessages(Message[] messageArr, Folder folder, boolean z) throws MessagingException {
        checkOpened();
        if (messageArr.length == 0) {
            return;
        }
        if (folder.getStore() == this.store) {
            synchronized (this.messageCacheLock) {
                try {
                    try {
                        IMAPProtocol protocol = getProtocol();
                        MessageSet[] messageSet = Utility.toMessageSet(messageArr, null);
                        if (messageSet == null) {
                            throw new MessageRemovedException("Messages have been removed");
                        }
                        if (z) {
                            protocol.move(messageSet, folder.getFullName());
                        } else {
                            protocol.copy(messageSet, folder.getFullName());
                        }
                    } catch (CommandFailedException e) {
                        if (e.getMessage().indexOf("TRYCREATE") == -1) {
                            throw new MessagingException(e.getMessage(), e);
                        }
                        throw new FolderNotFoundException(folder, folder.getFullName() + " does not exist");
                    }
                } catch (ConnectionException e2) {
                    throw new FolderClosedException(this, e2.getMessage());
                } catch (ProtocolException e3) {
                    throw new MessagingException(e3.getMessage(), e3);
                }
            }
        } else {
            if (z) {
                throw new MessagingException("Move between stores not supported");
            }
            super.copyMessages(messageArr, folder);
        }
    }

    private synchronized AppendUID[] copymoveUIDMessages(Message[] messageArr, Folder folder, boolean z) throws MessagingException {
        AppendUID[] appendUIDArr;
        checkOpened();
        if (messageArr.length == 0) {
            return null;
        }
        if (folder.getStore() != this.store) {
            throw new MessagingException(z ? "can't moveUIDMessages to a different store" : "can't copyUIDMessages to a different store");
        }
        FetchProfile fetchProfile = new FetchProfile();
        fetchProfile.add(UIDFolder.FetchProfileItem.UID);
        fetch(messageArr, fetchProfile);
        synchronized (this.messageCacheLock) {
            try {
                try {
                    IMAPProtocol protocol = getProtocol();
                    MessageSet[] messageSet = Utility.toMessageSet(messageArr, null);
                    if (messageSet == null) {
                        throw new MessageRemovedException("Messages have been removed");
                    }
                    CopyUID moveuid = z ? protocol.moveuid(messageSet, folder.getFullName()) : protocol.copyuid(messageSet, folder.getFullName());
                    long[] array = UIDSet.toArray(moveuid.src);
                    long[] array2 = UIDSet.toArray(moveuid.dst);
                    Message[] messagesByUID = getMessagesByUID(array);
                    appendUIDArr = new AppendUID[messageArr.length];
                    for (int i = 0; i < messageArr.length; i++) {
                        int i2 = i;
                        while (true) {
                            if (messageArr[i] == messagesByUID[i2]) {
                                appendUIDArr[i] = new AppendUID(moveuid.uidvalidity, array2[i2]);
                                break;
                            }
                            i2++;
                            if (i2 >= messagesByUID.length) {
                                i2 = 0;
                            }
                            if (i2 == i) {
                                break;
                            }
                        }
                    }
                } catch (ConnectionException e) {
                    throw new FolderClosedException(this, e.getMessage());
                }
            } catch (CommandFailedException e2) {
                if (e2.getMessage().indexOf("TRYCREATE") == -1) {
                    throw new MessagingException(e2.getMessage(), e2);
                }
                throw new FolderNotFoundException(folder, folder.getFullName() + " does not exist");
            } catch (ProtocolException e3) {
                throw new MessagingException(e3.getMessage(), e3);
            }
        }
        return appendUIDArr;
    }

    private String createHeaderCommand(String[] strArr, boolean z) {
        StringBuffer stringBuffer = z ? new StringBuffer("BODY.PEEK[HEADER.FIELDS (") : new StringBuffer("RFC822.HEADER.LINES (");
        for (int i = 0; i < strArr.length; i++) {
            if (i > 0) {
                stringBuffer.append(SQLBuilder.BLANK);
            }
            stringBuffer.append(strArr[i]);
        }
        if (z) {
            stringBuffer.append(")]");
        } else {
            stringBuffer.append(SQLBuilder.PARENTHESES_RIGHT);
        }
        return stringBuffer.toString();
    }

    private Message[] createMessagesForUIDs(long[] jArr) {
        IMAPMessage[] iMAPMessageArr = new IMAPMessage[jArr.length];
        for (int i = 0; i < jArr.length; i = i + 1 + 1) {
            IMAPMessage iMAPMessage = this.uidTable != null ? this.uidTable.get(Long.valueOf(jArr[i])) : null;
            if (iMAPMessage == null) {
                iMAPMessage = newIMAPMessage(-1);
                iMAPMessage.setUID(jArr[i]);
                iMAPMessage.setExpunged(true);
            }
            iMAPMessageArr[i] = iMAPMessage;
        }
        return iMAPMessageArr;
    }

    private synchronized Folder[] doList(final String str, final boolean z) throws MessagingException {
        checkExists();
        int i = 0;
        if (this.attributes != null && !isDirectory()) {
            return new Folder[0];
        }
        final char separator = getSeparator();
        ListInfo[] listInfoArr = (ListInfo[]) doCommandIgnoreFailure(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.2
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                if (z) {
                    return iMAPProtocol.lsub("", IMAPFolder.this.fullName + separator + str);
                }
                return iMAPProtocol.list("", IMAPFolder.this.fullName + separator + str);
            }
        });
        if (listInfoArr == null) {
            return new Folder[0];
        }
        if (listInfoArr.length > 0) {
            if (listInfoArr[0].name.equals(this.fullName + separator)) {
                i = 1;
            }
        }
        IMAPFolder[] iMAPFolderArr = new IMAPFolder[listInfoArr.length - i];
        IMAPStore iMAPStore = (IMAPStore) this.store;
        for (int i2 = i; i2 < listInfoArr.length; i2++) {
            iMAPFolderArr[i2 - i] = iMAPStore.newIMAPFolder(listInfoArr[i2]);
        }
        return iMAPFolderArr;
    }

    private int findName(ListInfo[] listInfoArr, String str) {
        int i = 0;
        while (i < listInfoArr.length && !listInfoArr[i].name.equals(str)) {
            i++;
        }
        if (i >= listInfoArr.length) {
            return 0;
        }
        return i;
    }

    private Status getStatus() throws ProtocolException {
        IMAPProtocol iMAPProtocol;
        int statusCacheTimeout = ((IMAPStore) this.store).getStatusCacheTimeout();
        if (statusCacheTimeout > 0 && this.cachedStatus != null && System.currentTimeMillis() - this.cachedStatusTime < statusCacheTimeout) {
            return this.cachedStatus;
        }
        try {
            iMAPProtocol = getStoreProtocol();
            try {
                Status status = iMAPProtocol.status(this.fullName, null);
                if (statusCacheTimeout > 0) {
                    this.cachedStatus = status;
                    this.cachedStatusTime = System.currentTimeMillis();
                }
                releaseStoreProtocol(iMAPProtocol);
                return status;
            } catch (Throwable th) {
                th = th;
                releaseStoreProtocol(iMAPProtocol);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            iMAPProtocol = null;
        }
    }

    private boolean isDirectory() {
        return (this.type & 2) != 0;
    }

    private boolean isRecoverable(Throwable th) {
        return (th instanceof Exception) || (th instanceof LinkageError);
    }

    private MessagingException logoutAndThrow(String str, ProtocolException protocolException) {
        MessagingException messagingException = new MessagingException(str, protocolException);
        try {
            this.protocol.logout();
        } catch (Throwable th) {
            addSuppressed(messagingException, th);
        }
        return messagingException;
    }

    private Message processFetchResponse(FetchResponse fetchResponse) {
        IMAPMessage messageBySeqNumber = getMessageBySeqNumber(fetchResponse.getNumber());
        if (messageBySeqNumber == null) {
            return messageBySeqNumber;
        }
        boolean z = false;
        UID uid = (UID) fetchResponse.getItem(UID.class);
        if (uid != null && messageBySeqNumber.getUID() != uid.uid) {
            messageBySeqNumber.setUID(uid.uid);
            if (this.uidTable == null) {
                this.uidTable = new Hashtable<>();
            }
            this.uidTable.put(Long.valueOf(uid.uid), messageBySeqNumber);
            z = true;
        }
        MODSEQ modseq = (MODSEQ) fetchResponse.getItem(MODSEQ.class);
        if (modseq != null && messageBySeqNumber._getModSeq() != modseq.modseq) {
            messageBySeqNumber.setModSeq(modseq.modseq);
            z = true;
        }
        FLAGS flags = (FLAGS) fetchResponse.getItem(FLAGS.class);
        if (flags != null) {
            messageBySeqNumber._setFlags(flags);
            z = true;
        }
        messageBySeqNumber.handleExtensionFetchItems(fetchResponse.getExtensionItems());
        if (z) {
            return messageBySeqNumber;
        }
        return null;
    }

    private void setACL(final ACL acl, final char c) throws MessagingException {
        doOptionalCommand("ACL not supported", new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.18
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                iMAPProtocol.setACL(IMAPFolder.this.fullName, c, acl);
                return null;
            }
        });
    }

    public void addACL(ACL acl) throws MessagingException {
        setACL(acl, (char) 0);
    }

    @Override // javax.mail.Folder
    public synchronized void addMessageCountListener(MessageCountListener messageCountListener) {
        super.addMessageCountListener(messageCountListener);
        this.hasMessageCountListener = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized Message[] addMessages(Message[] messageArr) throws MessagingException {
        MimeMessage[] mimeMessageArr;
        checkOpened();
        mimeMessageArr = new MimeMessage[messageArr.length];
        AppendUID[] appendUIDMessages = appendUIDMessages(messageArr);
        for (int i = 0; i < appendUIDMessages.length; i++) {
            AppendUID appendUID = appendUIDMessages[i];
            if (appendUID != null && appendUID.uidvalidity == this.uidvalidity) {
                try {
                    mimeMessageArr[i] = getMessageByUID(appendUID.uid);
                } catch (MessagingException unused) {
                }
            }
        }
        return mimeMessageArr;
    }

    public void addRights(ACL acl) throws MessagingException {
        setACL(acl, '+');
    }

    @Override // javax.mail.Folder
    public synchronized void appendMessages(Message[] messageArr) throws MessagingException {
        checkExists();
        int appendBufferSize = ((IMAPStore) this.store).getAppendBufferSize();
        for (Message message : messageArr) {
            final Date receivedDate = message.getReceivedDate();
            if (receivedDate == null) {
                receivedDate = message.getSentDate();
            }
            final Flags flags = message.getFlags();
            try {
                final MessageLiteral messageLiteral = new MessageLiteral(message, message.getSize() > appendBufferSize ? 0 : appendBufferSize);
                doCommand(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.10
                    @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
                    public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                        iMAPProtocol.append(IMAPFolder.this.fullName, flags, receivedDate, messageLiteral);
                        return null;
                    }
                });
            } catch (IOException e) {
                throw new MessagingException("IOException while appending messages", e);
            } catch (MessageRemovedException unused) {
            }
        }
    }

    public synchronized AppendUID[] appendUIDMessages(Message[] messageArr) throws MessagingException {
        AppendUID[] appendUIDArr;
        checkExists();
        int appendBufferSize = ((IMAPStore) this.store).getAppendBufferSize();
        appendUIDArr = new AppendUID[messageArr.length];
        for (int i = 0; i < messageArr.length; i++) {
            Message message = messageArr[i];
            try {
                final MessageLiteral messageLiteral = new MessageLiteral(message, message.getSize() > appendBufferSize ? 0 : appendBufferSize);
                final Date receivedDate = message.getReceivedDate();
                if (receivedDate == null) {
                    receivedDate = message.getSentDate();
                }
                final Flags flags = message.getFlags();
                appendUIDArr[i] = (AppendUID) doCommand(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.11
                    @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
                    public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                        return iMAPProtocol.appenduid(IMAPFolder.this.fullName, flags, receivedDate, messageLiteral);
                    }
                });
            } catch (IOException e) {
                throw new MessagingException("IOException while appending messages", e);
            } catch (MessageRemovedException unused) {
            }
        }
        return appendUIDArr;
    }

    protected void checkClosed() {
        if (this.opened) {
            throw new IllegalStateException("This operation is not allowed on an open folder");
        }
    }

    protected void checkExists() throws MessagingException {
        if (this.exists || exists()) {
            return;
        }
        throw new FolderNotFoundException(this, this.fullName + " not found");
    }

    protected void checkOpened() throws FolderClosedException {
        if (this.opened) {
            return;
        }
        if (!this.reallyClosed) {
            throw new FolderClosedException(this, "Lost folder connection to server");
        }
        throw new IllegalStateException("This operation is not allowed on a closed folder");
    }

    protected void checkRange(int i) throws MessagingException {
        if (i < 1) {
            throw new IndexOutOfBoundsException("message number < 1");
        }
        if (i <= this.total) {
            return;
        }
        synchronized (this.messageCacheLock) {
            try {
                try {
                    keepConnectionAlive(false);
                } catch (ConnectionException e) {
                    throw new FolderClosedException(this, e.getMessage());
                }
            } catch (ProtocolException e2) {
                throw new MessagingException(e2.getMessage(), e2);
            }
        }
        if (i <= this.total) {
            return;
        }
        throw new IndexOutOfBoundsException(i + " > " + this.total);
    }

    @Override // javax.mail.Folder
    public synchronized void close(boolean z) throws MessagingException {
        close(z, false);
    }

    @Override // javax.mail.Folder
    public synchronized void copyMessages(Message[] messageArr, Folder folder) throws MessagingException {
        copymoveMessages(messageArr, folder, false);
    }

    public synchronized AppendUID[] copyUIDMessages(Message[] messageArr, Folder folder) throws MessagingException {
        return copymoveUIDMessages(messageArr, folder, false);
    }

    @Override // javax.mail.Folder
    public synchronized boolean create(final int i) throws MessagingException {
        final char separator;
        if ((i & 1) == 0) {
            try {
                separator = getSeparator();
            } catch (Throwable th) {
                throw th;
            }
        } else {
            separator = 0;
        }
        if (doCommandIgnoreFailure(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.6
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                ListInfo[] list;
                if ((i & 1) == 0) {
                    iMAPProtocol.create(IMAPFolder.this.fullName + separator);
                } else {
                    iMAPProtocol.create(IMAPFolder.this.fullName);
                    if ((i & 2) != 0 && (list = iMAPProtocol.list("", IMAPFolder.this.fullName)) != null && !list[0].hasInferiors) {
                        iMAPProtocol.delete(IMAPFolder.this.fullName);
                        throw new ProtocolException("Unsupported type");
                    }
                }
                return Boolean.TRUE;
            }
        }) == null) {
            return false;
        }
        boolean exists = exists();
        if (exists) {
            notifyFolderListeners(1);
        }
        return exists;
    }

    @Override // javax.mail.Folder
    public synchronized boolean delete(boolean z) throws MessagingException {
        checkClosed();
        if (z) {
            for (Folder folder : list()) {
                folder.delete(z);
            }
        }
        if (doCommandIgnoreFailure(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.8
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                iMAPProtocol.delete(IMAPFolder.this.fullName);
                return Boolean.TRUE;
            }
        }) == null) {
            return false;
        }
        this.exists = false;
        this.attributes = null;
        notifyFolderListeners(2);
        return true;
    }

    public Object doCommand(ProtocolCommand protocolCommand) throws MessagingException {
        try {
            return doProtocolCommand(protocolCommand);
        } catch (ConnectionException e) {
            throwClosedException(e);
            return null;
        } catch (ProtocolException e2) {
            throw new MessagingException(e2.getMessage(), e2);
        }
    }

    public Object doCommandIgnoreFailure(ProtocolCommand protocolCommand) throws MessagingException {
        try {
            return doProtocolCommand(protocolCommand);
        } catch (CommandFailedException unused) {
            return null;
        } catch (ConnectionException e) {
            throwClosedException(e);
            return null;
        } catch (ProtocolException e2) {
            throw new MessagingException(e2.getMessage(), e2);
        }
    }

    public Object doOptionalCommand(String str, ProtocolCommand protocolCommand) throws MessagingException {
        try {
            return doProtocolCommand(protocolCommand);
        } catch (BadCommandException e) {
            throw new MessagingException(str, e);
        } catch (ConnectionException e2) {
            throwClosedException(e2);
            return null;
        } catch (ProtocolException e3) {
            throw new MessagingException(e3.getMessage(), e3);
        }
    }

    protected synchronized Object doProtocolCommand(ProtocolCommand protocolCommand) throws ProtocolException {
        Object doCommand;
        if (this.protocol != null) {
            synchronized (this.messageCacheLock) {
                doCommand = protocolCommand.doCommand(getProtocol());
            }
            return doCommand;
        }
        IMAPProtocol iMAPProtocol = null;
        try {
            IMAPProtocol storeProtocol = getStoreProtocol();
            try {
                Object doCommand2 = protocolCommand.doCommand(storeProtocol);
                releaseStoreProtocol(storeProtocol);
                return doCommand2;
            } catch (Throwable th) {
                th = th;
                iMAPProtocol = storeProtocol;
                releaseStoreProtocol(iMAPProtocol);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // javax.mail.Folder
    public synchronized boolean exists() throws MessagingException {
        final String str;
        if (!this.isNamespace || this.separator == 0) {
            str = this.fullName;
        } else {
            str = this.fullName + this.separator;
        }
        ListInfo[] listInfoArr = (ListInfo[]) doCommand(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.1
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                return iMAPProtocol.list("", str);
            }
        });
        if (listInfoArr != null) {
            int findName = findName(listInfoArr, str);
            this.fullName = listInfoArr[findName].name;
            this.separator = listInfoArr[findName].separator;
            int length = this.fullName.length();
            if (this.separator != 0 && length > 0) {
                int i = length - 1;
                if (this.fullName.charAt(i) == this.separator) {
                    this.fullName = this.fullName.substring(0, i);
                }
            }
            this.type = 0;
            if (listInfoArr[findName].hasInferiors) {
                this.type |= 2;
            }
            if (listInfoArr[findName].canOpen) {
                this.type |= 1;
            }
            this.exists = true;
            this.attributes = listInfoArr[findName].attrs;
        } else {
            this.exists = this.opened;
            this.attributes = null;
        }
        return this.exists;
    }

    @Override // javax.mail.Folder
    public synchronized Message[] expunge() throws MessagingException {
        return expunge(null);
    }

    public synchronized Message[] expunge(Message[] messageArr) throws MessagingException {
        IMAPMessage[] removeExpungedMessages;
        checkOpened();
        if (messageArr != null) {
            FetchProfile fetchProfile = new FetchProfile();
            fetchProfile.add(UIDFolder.FetchProfileItem.UID);
            fetch(messageArr, fetchProfile);
        }
        synchronized (this.messageCacheLock) {
            this.doExpungeNotification = false;
            try {
                try {
                    try {
                        IMAPProtocol protocol = getProtocol();
                        if (messageArr != null) {
                            protocol.uidexpunge(Utility.toUIDSet(messageArr));
                        } else {
                            protocol.expunge();
                        }
                        removeExpungedMessages = messageArr != null ? this.messageCache.removeExpungedMessages(messageArr) : this.messageCache.removeExpungedMessages();
                        if (this.uidTable != null) {
                            for (IMAPMessage iMAPMessage : removeExpungedMessages) {
                                long uid = iMAPMessage.getUID();
                                if (uid != -1) {
                                    this.uidTable.remove(Long.valueOf(uid));
                                }
                            }
                        }
                        this.total = this.messageCache.size();
                    } catch (ConnectionException e) {
                        throw new FolderClosedException(this, e.getMessage());
                    }
                } catch (CommandFailedException e2) {
                    if (this.mode == 2) {
                        throw new MessagingException(e2.getMessage(), e2);
                    }
                    throw new IllegalStateException("Cannot expunge READ_ONLY folder: " + this.fullName);
                } catch (ProtocolException e3) {
                    throw new MessagingException(e3.getMessage(), e3);
                }
            } finally {
                this.doExpungeNotification = true;
            }
        }
        if (removeExpungedMessages.length > 0) {
            notifyMessageRemovedListeners(true, removeExpungedMessages);
        }
        return removeExpungedMessages;
    }

    @Override // javax.mail.Folder
    public synchronized void fetch(Message[] messageArr, FetchProfile fetchProfile) throws MessagingException {
        boolean isREV1;
        FetchItem[] fetchItems;
        boolean z;
        boolean z2;
        String[] strArr;
        Response[] responseArr;
        synchronized (this.messageCacheLock) {
            checkOpened();
            isREV1 = this.protocol.isREV1();
            fetchItems = this.protocol.getFetchItems();
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (fetchProfile.contains(FetchProfile.Item.ENVELOPE)) {
            stringBuffer.append(getEnvelopeCommand());
            z = false;
        } else {
            z = true;
        }
        if (fetchProfile.contains(FetchProfile.Item.FLAGS)) {
            stringBuffer.append(z ? "FLAGS" : " FLAGS");
            z = false;
        }
        if (fetchProfile.contains(FetchProfile.Item.CONTENT_INFO)) {
            stringBuffer.append(z ? "BODYSTRUCTURE" : " BODYSTRUCTURE");
            z = false;
        }
        if (fetchProfile.contains(UIDFolder.FetchProfileItem.UID)) {
            stringBuffer.append(z ? "UID" : " UID");
            z = false;
        }
        if (fetchProfile.contains(FetchProfileItem.HEADERS)) {
            if (isREV1) {
                stringBuffer.append(z ? "BODY.PEEK[HEADER]" : " BODY.PEEK[HEADER]");
            } else {
                stringBuffer.append(z ? "RFC822.HEADER" : " RFC822.HEADER");
            }
            z = false;
            z2 = true;
        } else {
            z2 = false;
        }
        if (fetchProfile.contains(FetchProfileItem.MESSAGE)) {
            if (isREV1) {
                stringBuffer.append(z ? "BODY.PEEK[]" : " BODY.PEEK[]");
            } else {
                stringBuffer.append(z ? "RFC822" : " RFC822");
            }
            z = false;
            z2 = true;
        }
        if (fetchProfile.contains(FetchProfile.Item.SIZE) || fetchProfile.contains(FetchProfileItem.SIZE)) {
            stringBuffer.append(z ? "RFC822.SIZE" : " RFC822.SIZE");
            z = false;
        }
        if (fetchProfile.contains(FetchProfileItem.INTERNALDATE)) {
            stringBuffer.append(z ? "INTERNALDATE" : " INTERNALDATE");
            z = false;
        }
        if (z2) {
            strArr = null;
        } else {
            strArr = fetchProfile.getHeaderNames();
            if (strArr.length > 0) {
                if (!z) {
                    stringBuffer.append(SQLBuilder.BLANK);
                }
                stringBuffer.append(createHeaderCommand(strArr, isREV1));
            }
        }
        for (int i = 0; i < fetchItems.length; i++) {
            if (fetchProfile.contains(fetchItems[i].getFetchProfileItem())) {
                if (stringBuffer.length() != 0) {
                    stringBuffer.append(SQLBuilder.BLANK);
                }
                stringBuffer.append(fetchItems[i].getName());
            }
        }
        IMAPMessage.FetchProfileCondition fetchProfileCondition = new IMAPMessage.FetchProfileCondition(fetchProfile, fetchItems);
        synchronized (this.messageCacheLock) {
            checkOpened();
            MessageSet[] messageSetSorted = Utility.toMessageSetSorted(messageArr, fetchProfileCondition);
            if (messageSetSorted == null) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            try {
                try {
                    responseArr = getProtocol().fetch(messageSetSorted, stringBuffer.toString());
                } catch (ProtocolException e) {
                    throw new MessagingException(e.getMessage(), e);
                }
            } catch (CommandFailedException unused) {
                responseArr = null;
            } catch (ConnectionException e2) {
                throw new FolderClosedException(this, e2.getMessage());
            }
            if (responseArr == null) {
                return;
            }
            for (int i2 = 0; i2 < responseArr.length; i2++) {
                if (responseArr[i2] != null) {
                    if (responseArr[i2] instanceof FetchResponse) {
                        FetchResponse fetchResponse = (FetchResponse) responseArr[i2];
                        IMAPMessage messageBySeqNumber = getMessageBySeqNumber(fetchResponse.getNumber());
                        int itemCount = fetchResponse.getItemCount();
                        boolean z3 = false;
                        for (int i3 = 0; i3 < itemCount; i3++) {
                            Item item = fetchResponse.getItem(i3);
                            if ((item instanceof Flags) && (!fetchProfile.contains(FetchProfile.Item.FLAGS) || messageBySeqNumber == null)) {
                                z3 = true;
                            } else if (messageBySeqNumber != null) {
                                messageBySeqNumber.handleFetchItem(item, strArr, z2);
                            }
                        }
                        if (messageBySeqNumber != null) {
                            messageBySeqNumber.handleExtensionFetchItems(fetchResponse.getExtensionItems());
                        }
                        if (z3) {
                            arrayList.add(fetchResponse);
                        }
                    } else {
                        arrayList.add(responseArr[i2]);
                    }
                }
            }
            if (!arrayList.isEmpty()) {
                Response[] responseArr2 = new Response[arrayList.size()];
                arrayList.toArray(responseArr2);
                handleResponses(responseArr2);
            }
        }
    }

    public synchronized void forceClose() throws MessagingException {
        close(false, true);
    }

    public ACL[] getACL() throws MessagingException {
        return (ACL[]) doOptionalCommand("ACL not supported", new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.14
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                return iMAPProtocol.getACL(IMAPFolder.this.fullName);
            }
        });
    }

    public synchronized String[] getAttributes() throws MessagingException {
        checkExists();
        if (this.attributes == null) {
            exists();
        }
        return this.attributes == null ? new String[0] : (String[]) this.attributes.clone();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SocketChannel getChannel() {
        if (this.protocol != null) {
            return this.protocol.getChannel();
        }
        return null;
    }

    @Override // javax.mail.Folder
    public synchronized int getDeletedMessageCount() throws MessagingException {
        int length;
        if (!this.opened) {
            checkExists();
            return -1;
        }
        Flags flags = new Flags();
        flags.add(Flags.Flag.DELETED);
        try {
            synchronized (this.messageCacheLock) {
                length = getProtocol().search(new FlagTerm(flags, true)).length;
            }
            return length;
        } catch (ConnectionException e) {
            throw new FolderClosedException(this, e.getMessage());
        } catch (ProtocolException e2) {
            throw new MessagingException(e2.getMessage(), e2);
        }
    }

    protected String getEnvelopeCommand() {
        return "ENVELOPE INTERNALDATE RFC822.SIZE";
    }

    @Override // javax.mail.Folder
    public synchronized Folder getFolder(String str) throws MessagingException {
        char separator;
        if (this.attributes != null && !isDirectory()) {
            throw new MessagingException("Cannot contain subfolders");
        }
        separator = getSeparator();
        return ((IMAPStore) this.store).newIMAPFolder(this.fullName + separator + str, separator);
    }

    @Override // javax.mail.Folder
    public String getFullName() {
        return this.fullName;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0054 A[Catch: all -> 0x0070, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0005, B:16:0x0022, B:19:0x0054, B:22:0x0058, B:23:0x005f, B:37:0x006c, B:38:0x006f, B:31:0x004f), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0058 A[Catch: all -> 0x0070, TRY_ENTER, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0005, B:16:0x0022, B:19:0x0054, B:22:0x0058, B:23:0x005f, B:37:0x006c, B:38:0x006f, B:31:0x004f), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized long getHighestModSeq() throws javax.mail.MessagingException {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.opened     // Catch: java.lang.Throwable -> L70
            if (r0 == 0) goto L9
            long r0 = r5.highestmodseq     // Catch: java.lang.Throwable -> L70
            monitor-exit(r5)
            return r0
        L9:
            r0 = 0
            com.sun.mail.imap.protocol.IMAPProtocol r1 = r5.getStoreProtocol()     // Catch: java.lang.Throwable -> L37 com.sun.mail.iap.ProtocolException -> L3c com.sun.mail.iap.ConnectionException -> L4a com.sun.mail.iap.BadCommandException -> L60
            java.lang.String r2 = "CONDSTORE"
            boolean r2 = r1.hasCapability(r2)     // Catch: java.lang.Throwable -> L2f com.sun.mail.iap.ProtocolException -> L31 com.sun.mail.iap.ConnectionException -> L33 com.sun.mail.iap.BadCommandException -> L35
            if (r2 == 0) goto L27
            java.lang.String r2 = "HIGHESTMODSEQ"
            java.lang.String[] r2 = new java.lang.String[]{r2}     // Catch: java.lang.Throwable -> L2f com.sun.mail.iap.ProtocolException -> L31 com.sun.mail.iap.ConnectionException -> L33 com.sun.mail.iap.BadCommandException -> L35
            java.lang.String r3 = r5.fullName     // Catch: java.lang.Throwable -> L2f com.sun.mail.iap.ProtocolException -> L31 com.sun.mail.iap.ConnectionException -> L33 com.sun.mail.iap.BadCommandException -> L35
            com.sun.mail.imap.protocol.Status r2 = r1.status(r3, r2)     // Catch: java.lang.Throwable -> L2f com.sun.mail.iap.ProtocolException -> L31 com.sun.mail.iap.ConnectionException -> L33 com.sun.mail.iap.BadCommandException -> L35
            r5.releaseStoreProtocol(r1)     // Catch: java.lang.Throwable -> L70
            r0 = r2
            goto L52
        L27:
            com.sun.mail.iap.BadCommandException r2 = new com.sun.mail.iap.BadCommandException     // Catch: java.lang.Throwable -> L2f com.sun.mail.iap.ProtocolException -> L31 com.sun.mail.iap.ConnectionException -> L33 com.sun.mail.iap.BadCommandException -> L35
            java.lang.String r3 = "CONDSTORE not supported"
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L2f com.sun.mail.iap.ProtocolException -> L31 com.sun.mail.iap.ConnectionException -> L33 com.sun.mail.iap.BadCommandException -> L35
            throw r2     // Catch: java.lang.Throwable -> L2f com.sun.mail.iap.ProtocolException -> L31 com.sun.mail.iap.ConnectionException -> L33 com.sun.mail.iap.BadCommandException -> L35
        L2f:
            r0 = move-exception
            goto L6c
        L31:
            r0 = move-exception
            goto L40
        L33:
            r2 = move-exception
            goto L4c
        L35:
            r0 = move-exception
            goto L64
        L37:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L6c
        L3c:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L40:
            javax.mail.MessagingException r2 = new javax.mail.MessagingException     // Catch: java.lang.Throwable -> L2f
            java.lang.String r3 = r0.getMessage()     // Catch: java.lang.Throwable -> L2f
            r2.<init>(r3, r0)     // Catch: java.lang.Throwable -> L2f
            throw r2     // Catch: java.lang.Throwable -> L2f
        L4a:
            r2 = move-exception
            r1 = r0
        L4c:
            r5.throwClosedException(r2)     // Catch: java.lang.Throwable -> L2f
            r5.releaseStoreProtocol(r1)     // Catch: java.lang.Throwable -> L70
        L52:
            if (r0 == 0) goto L58
            long r0 = r0.highestmodseq     // Catch: java.lang.Throwable -> L70
            monitor-exit(r5)
            return r0
        L58:
            javax.mail.MessagingException r0 = new javax.mail.MessagingException     // Catch: java.lang.Throwable -> L70
            java.lang.String r1 = "Cannot obtain HIGHESTMODSEQ"
            r0.<init>(r1)     // Catch: java.lang.Throwable -> L70
            throw r0     // Catch: java.lang.Throwable -> L70
        L60:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L64:
            javax.mail.MessagingException r2 = new javax.mail.MessagingException     // Catch: java.lang.Throwable -> L2f
            java.lang.String r3 = "Cannot obtain HIGHESTMODSEQ"
            r2.<init>(r3, r0)     // Catch: java.lang.Throwable -> L2f
            throw r2     // Catch: java.lang.Throwable -> L2f
        L6c:
            r5.releaseStoreProtocol(r1)     // Catch: java.lang.Throwable -> L70
            throw r0     // Catch: java.lang.Throwable -> L70
        L70:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPFolder.getHighestModSeq():long");
    }

    @Override // javax.mail.Folder
    public synchronized Message getMessage(int i) throws MessagingException {
        checkOpened();
        checkRange(i);
        return this.messageCache.getMessage(i);
    }

    protected IMAPMessage getMessageBySeqNumber(int i) {
        if (i <= this.messageCache.size()) {
            return this.messageCache.getMessageBySeqnum(i);
        }
        if (!this.logger.isLoggable(Level.FINE)) {
            return null;
        }
        this.logger.fine("ignoring message number " + i + " outside range " + this.messageCache.size());
        return null;
    }

    @Override // javax.mail.UIDFolder
    public synchronized Message getMessageByUID(long j) throws MessagingException {
        checkOpened();
        IMAPMessage iMAPMessage = null;
        try {
            try {
                synchronized (this.messageCacheLock) {
                    Long valueOf = Long.valueOf(j);
                    if (this.uidTable != null) {
                        iMAPMessage = this.uidTable.get(valueOf);
                        if (iMAPMessage != null) {
                            return iMAPMessage;
                        }
                    } else {
                        this.uidTable = new Hashtable<>();
                    }
                    getProtocol().fetchSequenceNumber(j);
                    return (this.uidTable == null || (iMAPMessage = this.uidTable.get(valueOf)) == null) ? iMAPMessage : iMAPMessage;
                }
            } catch (ConnectionException e) {
                throw new FolderClosedException(this, e.getMessage());
            }
        } catch (ProtocolException e2) {
            throw new MessagingException(e2.getMessage(), e2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v5 */
    @Override // javax.mail.Folder
    public synchronized int getMessageCount() throws MessagingException {
        Throwable th;
        ProtocolException e;
        synchronized (this.messageCacheLock) {
            IMAPProtocol iMAPProtocol = this.opened;
            if (iMAPProtocol != 0) {
                try {
                    keepConnectionAlive(true);
                    return this.total;
                } catch (ConnectionException e2) {
                    throw new FolderClosedException(this, e2.getMessage());
                } catch (ProtocolException e3) {
                    throw new MessagingException(e3.getMessage(), e3);
                }
            }
            checkExists();
            try {
                return getStatus().total;
            } catch (BadCommandException unused) {
                try {
                    try {
                        IMAPProtocol storeProtocol = getStoreProtocol();
                        try {
                            MailboxInfo examine = storeProtocol.examine(this.fullName);
                            storeProtocol.close();
                            int i = examine.total;
                            releaseStoreProtocol(storeProtocol);
                            return i;
                        } catch (ProtocolException e4) {
                            e = e4;
                            throw new MessagingException(e.getMessage(), e);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        releaseStoreProtocol(iMAPProtocol);
                        throw th;
                    }
                } catch (ProtocolException e5) {
                    e = e5;
                } catch (Throwable th3) {
                    iMAPProtocol = 0;
                    th = th3;
                    releaseStoreProtocol(iMAPProtocol);
                    throw th;
                }
            } catch (ConnectionException e6) {
                throw new StoreClosedException(this.store, e6.getMessage());
            } catch (ProtocolException e7) {
                throw new MessagingException(e7.getMessage(), e7);
            }
        }
    }

    @Override // javax.mail.Folder
    public synchronized Message[] getMessages() throws MessagingException {
        Message[] messageArr;
        checkOpened();
        int messageCount = getMessageCount();
        messageArr = new Message[messageCount];
        for (int i = 1; i <= messageCount; i++) {
            messageArr[i - 1] = this.messageCache.getMessage(i);
        }
        return messageArr;
    }

    protected IMAPMessage[] getMessagesBySeqNumbers(int[] iArr) {
        IMAPMessage[] iMAPMessageArr = new IMAPMessage[iArr.length];
        int i = 0;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iMAPMessageArr[i2] = getMessageBySeqNumber(iArr[i2]);
            if (iMAPMessageArr[i2] == null) {
                i++;
            }
        }
        if (i <= 0) {
            return iMAPMessageArr;
        }
        IMAPMessage[] iMAPMessageArr2 = new IMAPMessage[iArr.length - i];
        int i3 = 0;
        for (int i4 = 0; i4 < iMAPMessageArr.length; i4++) {
            if (iMAPMessageArr[i4] != null) {
                iMAPMessageArr2[i3] = iMAPMessageArr[i4];
                i3++;
            }
        }
        return iMAPMessageArr2;
    }

    @Override // javax.mail.UIDFolder
    public synchronized Message[] getMessagesByUID(long j, long j2) throws MessagingException {
        Message[] messageArr;
        checkOpened();
        try {
            try {
                synchronized (this.messageCacheLock) {
                    if (this.uidTable == null) {
                        this.uidTable = new Hashtable<>();
                    }
                    long[] fetchSequenceNumbers = getProtocol().fetchSequenceNumbers(j, j2);
                    ArrayList arrayList = new ArrayList();
                    for (long j3 : fetchSequenceNumbers) {
                        IMAPMessage iMAPMessage = this.uidTable.get(Long.valueOf(j3));
                        if (iMAPMessage != null) {
                            arrayList.add(iMAPMessage);
                        }
                    }
                    messageArr = (Message[]) arrayList.toArray(new Message[arrayList.size()]);
                }
            } catch (ConnectionException e) {
                throw new FolderClosedException(this, e.getMessage());
            }
        } catch (ProtocolException e2) {
            throw new MessagingException(e2.getMessage(), e2);
        }
        return messageArr;
    }

    @Override // javax.mail.UIDFolder
    public synchronized Message[] getMessagesByUID(long[] jArr) throws MessagingException {
        long[] jArr2;
        Message[] messageArr;
        checkOpened();
        try {
            synchronized (this.messageCacheLock) {
                if (this.uidTable != null) {
                    ArrayList arrayList = new ArrayList();
                    for (long j : jArr) {
                        if (!this.uidTable.containsKey(Long.valueOf(j))) {
                            arrayList.add(Long.valueOf(j));
                        }
                    }
                    int size = arrayList.size();
                    jArr2 = new long[size];
                    for (int i = 0; i < size; i++) {
                        jArr2[i] = ((Long) arrayList.get(i)).longValue();
                    }
                } else {
                    this.uidTable = new Hashtable<>();
                    jArr2 = jArr;
                }
                if (jArr2.length > 0) {
                    getProtocol().fetchSequenceNumbers(jArr2);
                }
                messageArr = new Message[jArr.length];
                for (int i2 = 0; i2 < jArr.length; i2++) {
                    messageArr[i2] = this.uidTable.get(Long.valueOf(jArr[i2]));
                }
            }
        } catch (ConnectionException e) {
            throw new FolderClosedException(this, e.getMessage());
        } catch (ProtocolException e2) {
            throw new MessagingException(e2.getMessage(), e2);
        }
        return messageArr;
    }

    public synchronized Message[] getMessagesByUIDChangedSince(long j, long j2, long j3) throws MessagingException {
        IMAPMessage[] messagesBySeqNumbers;
        checkOpened();
        try {
            synchronized (this.messageCacheLock) {
                IMAPProtocol protocol = getProtocol();
                if (!protocol.hasCapability("CONDSTORE")) {
                    throw new BadCommandException("CONDSTORE not supported");
                }
                messagesBySeqNumbers = getMessagesBySeqNumbers(protocol.uidfetchChangedSince(j, j2, j3));
            }
        } catch (ConnectionException e) {
            throw new FolderClosedException(this, e.getMessage());
        } catch (ProtocolException e2) {
            throw new MessagingException(e2.getMessage(), e2);
        }
        return messagesBySeqNumbers;
    }

    @Override // javax.mail.Folder
    public synchronized String getName() {
        if (this.name == null) {
            try {
                this.name = this.fullName.substring(this.fullName.lastIndexOf(getSeparator()) + 1);
            } catch (MessagingException unused) {
            }
        }
        return this.name;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v5 */
    @Override // javax.mail.Folder
    public synchronized int getNewMessageCount() throws MessagingException {
        Throwable th;
        ProtocolException e;
        synchronized (this.messageCacheLock) {
            IMAPProtocol iMAPProtocol = this.opened;
            if (iMAPProtocol != 0) {
                try {
                    keepConnectionAlive(true);
                    return this.recent;
                } catch (ConnectionException e2) {
                    throw new FolderClosedException(this, e2.getMessage());
                } catch (ProtocolException e3) {
                    throw new MessagingException(e3.getMessage(), e3);
                }
            }
            checkExists();
            try {
                return getStatus().recent;
            } catch (BadCommandException unused) {
                try {
                    try {
                        IMAPProtocol storeProtocol = getStoreProtocol();
                        try {
                            MailboxInfo examine = storeProtocol.examine(this.fullName);
                            storeProtocol.close();
                            int i = examine.recent;
                            releaseStoreProtocol(storeProtocol);
                            return i;
                        } catch (ProtocolException e4) {
                            e = e4;
                            throw new MessagingException(e.getMessage(), e);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        releaseStoreProtocol(iMAPProtocol);
                        throw th;
                    }
                } catch (ProtocolException e5) {
                    e = e5;
                } catch (Throwable th3) {
                    iMAPProtocol = 0;
                    th = th3;
                    releaseStoreProtocol(iMAPProtocol);
                    throw th;
                }
            } catch (ConnectionException e6) {
                throw new StoreClosedException(this.store, e6.getMessage());
            } catch (ProtocolException e7) {
                throw new MessagingException(e7.getMessage(), e7);
            }
        }
    }

    @Override // javax.mail.Folder
    public synchronized Folder getParent() throws MessagingException {
        char separator = getSeparator();
        int lastIndexOf = this.fullName.lastIndexOf(separator);
        if (lastIndexOf != -1) {
            return ((IMAPStore) this.store).newIMAPFolder(this.fullName.substring(0, lastIndexOf), separator);
        }
        return new DefaultFolder((IMAPStore) this.store);
    }

    @Override // javax.mail.Folder
    public synchronized Flags getPermanentFlags() {
        if (this.permanentFlags == null) {
            return null;
        }
        return (Flags) this.permanentFlags.clone();
    }

    protected IMAPProtocol getProtocol() throws ProtocolException {
        waitIfIdle();
        if (this.protocol != null) {
            return this.protocol;
        }
        throw new ConnectionException("Connection closed");
    }

    public Quota[] getQuota() throws MessagingException {
        return (Quota[]) doOptionalCommand("QUOTA not supported", new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.12
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                return iMAPProtocol.getQuotaRoot(IMAPFolder.this.fullName);
            }
        });
    }

    @Override // javax.mail.Folder
    public synchronized char getSeparator() throws MessagingException {
        if (this.separator == 65535) {
            ListInfo[] listInfoArr = (ListInfo[]) doCommand(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.3
                @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
                public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                    return iMAPProtocol.isREV1() ? iMAPProtocol.list(IMAPFolder.this.fullName, "") : iMAPProtocol.list("", IMAPFolder.this.fullName);
                }
            });
            if (listInfoArr != null) {
                this.separator = listInfoArr[0].separator;
            } else {
                this.separator = '/';
            }
        }
        return this.separator;
    }

    public synchronized Message[] getSortedMessages(SortTerm[] sortTermArr) throws MessagingException {
        return getSortedMessages(sortTermArr, null);
    }

    public synchronized Message[] getSortedMessages(SortTerm[] sortTermArr, SearchTerm searchTerm) throws MessagingException {
        IMAPMessage[] messagesBySeqNumbers;
        checkOpened();
        try {
            try {
                try {
                    synchronized (this.messageCacheLock) {
                        int[] sort = getProtocol().sort(sortTermArr, searchTerm);
                        messagesBySeqNumbers = sort != null ? getMessagesBySeqNumbers(sort) : null;
                    }
                } catch (CommandFailedException e) {
                    throw new MessagingException(e.getMessage(), e);
                }
            } catch (SearchException e2) {
                throw new MessagingException(e2.getMessage(), e2);
            }
        } catch (ConnectionException e3) {
            throw new FolderClosedException(this, e3.getMessage());
        } catch (ProtocolException e4) {
            throw new MessagingException(e4.getMessage(), e4);
        }
        return messagesBySeqNumbers;
    }

    public synchronized long getStatusItem(String str) throws MessagingException {
        IMAPProtocol iMAPProtocol;
        if (this.opened) {
            return -1L;
        }
        checkExists();
        IMAPProtocol iMAPProtocol2 = null;
        try {
            try {
                iMAPProtocol = getStoreProtocol();
            } catch (Throwable th) {
                th = th;
            }
        } catch (BadCommandException unused) {
            iMAPProtocol = null;
        } catch (ConnectionException e) {
            e = e;
        } catch (ProtocolException e2) {
            e = e2;
        }
        try {
            Status status = iMAPProtocol.status(this.fullName, new String[]{str});
            long item = status != null ? status.getItem(str) : -1L;
            releaseStoreProtocol(iMAPProtocol);
            return item;
        } catch (BadCommandException unused2) {
            releaseStoreProtocol(iMAPProtocol);
            return -1L;
        } catch (ConnectionException e3) {
            e = e3;
            throw new StoreClosedException(this.store, e.getMessage());
        } catch (ProtocolException e4) {
            e = e4;
            throw new MessagingException(e.getMessage(), e);
        } catch (Throwable th2) {
            th = th2;
            iMAPProtocol2 = iMAPProtocol;
            releaseStoreProtocol(iMAPProtocol2);
            throw th;
        }
    }

    protected synchronized IMAPProtocol getStoreProtocol() throws ProtocolException {
        this.connectionPoolLogger.fine("getStoreProtocol() borrowing a connection");
        return ((IMAPStore) this.store).getFolderStoreProtocol();
    }

    @Override // javax.mail.Folder
    public synchronized int getType() throws MessagingException {
        if (!this.opened) {
            checkExists();
        } else if (this.attributes == null) {
            exists();
        }
        return this.type;
    }

    @Override // javax.mail.UIDFolder
    public synchronized long getUID(Message message) throws MessagingException {
        if (message.getFolder() != this) {
            throw new NoSuchElementException("Message does not belong to this folder");
        }
        checkOpened();
        if (!(message instanceof IMAPMessage)) {
            throw new MessagingException("message is not an IMAPMessage");
        }
        IMAPMessage iMAPMessage = (IMAPMessage) message;
        long uid = iMAPMessage.getUID();
        if (uid != -1) {
            return uid;
        }
        synchronized (this.messageCacheLock) {
            try {
                IMAPProtocol protocol = getProtocol();
                iMAPMessage.checkExpunged();
                UID fetchUID = protocol.fetchUID(iMAPMessage.getSequenceNumber());
                if (fetchUID != null) {
                    uid = fetchUID.uid;
                    iMAPMessage.setUID(uid);
                    if (this.uidTable == null) {
                        this.uidTable = new Hashtable<>();
                    }
                    this.uidTable.put(Long.valueOf(uid), iMAPMessage);
                }
            } catch (ConnectionException e) {
                throw new FolderClosedException(this, e.getMessage());
            } catch (ProtocolException e2) {
                throw new MessagingException(e2.getMessage(), e2);
            }
        }
        return uid;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0042 A[Catch: all -> 0x005f, TRY_LEAVE, TryCatch #8 {, blocks: (B:3:0x0001, B:5:0x0005, B:14:0x001a, B:17:0x0042, B:20:0x0046, B:21:0x004d, B:28:0x003d, B:34:0x005b, B:35:0x005e), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0046 A[Catch: all -> 0x005f, TRY_ENTER, TryCatch #8 {, blocks: (B:3:0x0001, B:5:0x0005, B:14:0x001a, B:17:0x0042, B:20:0x0046, B:21:0x004d, B:28:0x003d, B:34:0x005b, B:35:0x005e), top: B:2:0x0001 }] */
    @Override // javax.mail.UIDFolder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized long getUIDNext() throws javax.mail.MessagingException {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.opened     // Catch: java.lang.Throwable -> L5f
            if (r0 == 0) goto L9
            long r0 = r5.uidnext     // Catch: java.lang.Throwable -> L5f
            monitor-exit(r5)
            return r0
        L9:
            r0 = 0
            com.sun.mail.imap.protocol.IMAPProtocol r1 = r5.getStoreProtocol()     // Catch: java.lang.Throwable -> L25 com.sun.mail.iap.ProtocolException -> L2a com.sun.mail.iap.ConnectionException -> L38 com.sun.mail.iap.BadCommandException -> L4e
            java.lang.String r2 = "UIDNEXT"
            java.lang.String[] r2 = new java.lang.String[]{r2}     // Catch: com.sun.mail.iap.ProtocolException -> L1f com.sun.mail.iap.ConnectionException -> L21 com.sun.mail.iap.BadCommandException -> L23 java.lang.Throwable -> L5a
            java.lang.String r3 = r5.fullName     // Catch: com.sun.mail.iap.ProtocolException -> L1f com.sun.mail.iap.ConnectionException -> L21 com.sun.mail.iap.BadCommandException -> L23 java.lang.Throwable -> L5a
            com.sun.mail.imap.protocol.Status r2 = r1.status(r3, r2)     // Catch: com.sun.mail.iap.ProtocolException -> L1f com.sun.mail.iap.ConnectionException -> L21 com.sun.mail.iap.BadCommandException -> L23 java.lang.Throwable -> L5a
            r5.releaseStoreProtocol(r1)     // Catch: java.lang.Throwable -> L5f
            r0 = r2
            goto L40
        L1f:
            r0 = move-exception
            goto L2e
        L21:
            r2 = move-exception
            goto L3a
        L23:
            r0 = move-exception
            goto L52
        L25:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L5b
        L2a:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L2e:
            javax.mail.MessagingException r2 = new javax.mail.MessagingException     // Catch: java.lang.Throwable -> L5a
            java.lang.String r3 = r0.getMessage()     // Catch: java.lang.Throwable -> L5a
            r2.<init>(r3, r0)     // Catch: java.lang.Throwable -> L5a
            throw r2     // Catch: java.lang.Throwable -> L5a
        L38:
            r2 = move-exception
            r1 = r0
        L3a:
            r5.throwClosedException(r2)     // Catch: java.lang.Throwable -> L5a
            r5.releaseStoreProtocol(r1)     // Catch: java.lang.Throwable -> L5f
        L40:
            if (r0 == 0) goto L46
            long r0 = r0.uidnext     // Catch: java.lang.Throwable -> L5f
            monitor-exit(r5)
            return r0
        L46:
            javax.mail.MessagingException r0 = new javax.mail.MessagingException     // Catch: java.lang.Throwable -> L5f
            java.lang.String r1 = "Cannot obtain UIDNext"
            r0.<init>(r1)     // Catch: java.lang.Throwable -> L5f
            throw r0     // Catch: java.lang.Throwable -> L5f
        L4e:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L52:
            javax.mail.MessagingException r2 = new javax.mail.MessagingException     // Catch: java.lang.Throwable -> L5a
            java.lang.String r3 = "Cannot obtain UIDNext"
            r2.<init>(r3, r0)     // Catch: java.lang.Throwable -> L5a
            throw r2     // Catch: java.lang.Throwable -> L5a
        L5a:
            r0 = move-exception
        L5b:
            r5.releaseStoreProtocol(r1)     // Catch: java.lang.Throwable -> L5f
            throw r0     // Catch: java.lang.Throwable -> L5f
        L5f:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPFolder.getUIDNext():long");
    }

    public synchronized boolean getUIDNotSticky() throws MessagingException {
        checkOpened();
        return this.uidNotSticky;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0042 A[Catch: all -> 0x005f, TRY_LEAVE, TryCatch #8 {, blocks: (B:3:0x0001, B:5:0x0005, B:14:0x001a, B:17:0x0042, B:20:0x0046, B:21:0x004d, B:28:0x003d, B:34:0x005b, B:35:0x005e), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0046 A[Catch: all -> 0x005f, TRY_ENTER, TryCatch #8 {, blocks: (B:3:0x0001, B:5:0x0005, B:14:0x001a, B:17:0x0042, B:20:0x0046, B:21:0x004d, B:28:0x003d, B:34:0x005b, B:35:0x005e), top: B:2:0x0001 }] */
    @Override // javax.mail.UIDFolder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized long getUIDValidity() throws javax.mail.MessagingException {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.opened     // Catch: java.lang.Throwable -> L5f
            if (r0 == 0) goto L9
            long r0 = r5.uidvalidity     // Catch: java.lang.Throwable -> L5f
            monitor-exit(r5)
            return r0
        L9:
            r0 = 0
            com.sun.mail.imap.protocol.IMAPProtocol r1 = r5.getStoreProtocol()     // Catch: java.lang.Throwable -> L25 com.sun.mail.iap.ProtocolException -> L2a com.sun.mail.iap.ConnectionException -> L38 com.sun.mail.iap.BadCommandException -> L4e
            java.lang.String r2 = "UIDVALIDITY"
            java.lang.String[] r2 = new java.lang.String[]{r2}     // Catch: com.sun.mail.iap.ProtocolException -> L1f com.sun.mail.iap.ConnectionException -> L21 com.sun.mail.iap.BadCommandException -> L23 java.lang.Throwable -> L5a
            java.lang.String r3 = r5.fullName     // Catch: com.sun.mail.iap.ProtocolException -> L1f com.sun.mail.iap.ConnectionException -> L21 com.sun.mail.iap.BadCommandException -> L23 java.lang.Throwable -> L5a
            com.sun.mail.imap.protocol.Status r2 = r1.status(r3, r2)     // Catch: com.sun.mail.iap.ProtocolException -> L1f com.sun.mail.iap.ConnectionException -> L21 com.sun.mail.iap.BadCommandException -> L23 java.lang.Throwable -> L5a
            r5.releaseStoreProtocol(r1)     // Catch: java.lang.Throwable -> L5f
            r0 = r2
            goto L40
        L1f:
            r0 = move-exception
            goto L2e
        L21:
            r2 = move-exception
            goto L3a
        L23:
            r0 = move-exception
            goto L52
        L25:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L5b
        L2a:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L2e:
            javax.mail.MessagingException r2 = new javax.mail.MessagingException     // Catch: java.lang.Throwable -> L5a
            java.lang.String r3 = r0.getMessage()     // Catch: java.lang.Throwable -> L5a
            r2.<init>(r3, r0)     // Catch: java.lang.Throwable -> L5a
            throw r2     // Catch: java.lang.Throwable -> L5a
        L38:
            r2 = move-exception
            r1 = r0
        L3a:
            r5.throwClosedException(r2)     // Catch: java.lang.Throwable -> L5a
            r5.releaseStoreProtocol(r1)     // Catch: java.lang.Throwable -> L5f
        L40:
            if (r0 == 0) goto L46
            long r0 = r0.uidvalidity     // Catch: java.lang.Throwable -> L5f
            monitor-exit(r5)
            return r0
        L46:
            javax.mail.MessagingException r0 = new javax.mail.MessagingException     // Catch: java.lang.Throwable -> L5f
            java.lang.String r1 = "Cannot obtain UIDValidity"
            r0.<init>(r1)     // Catch: java.lang.Throwable -> L5f
            throw r0     // Catch: java.lang.Throwable -> L5f
        L4e:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L52:
            javax.mail.MessagingException r2 = new javax.mail.MessagingException     // Catch: java.lang.Throwable -> L5a
            java.lang.String r3 = "Cannot obtain UIDValidity"
            r2.<init>(r3, r0)     // Catch: java.lang.Throwable -> L5a
            throw r2     // Catch: java.lang.Throwable -> L5a
        L5a:
            r0 = move-exception
        L5b:
            r5.releaseStoreProtocol(r1)     // Catch: java.lang.Throwable -> L5f
            throw r0     // Catch: java.lang.Throwable -> L5f
        L5f:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPFolder.getUIDValidity():long");
    }

    @Override // javax.mail.Folder
    public synchronized int getUnreadMessageCount() throws MessagingException {
        int length;
        if (!this.opened) {
            checkExists();
            try {
                try {
                    return getStatus().unseen;
                } catch (ConnectionException e) {
                    throw new StoreClosedException(this.store, e.getMessage());
                }
            } catch (BadCommandException unused) {
                return -1;
            } catch (ProtocolException e2) {
                throw new MessagingException(e2.getMessage(), e2);
            }
        }
        Flags flags = new Flags();
        flags.add(Flags.Flag.SEEN);
        try {
            synchronized (this.messageCacheLock) {
                length = getProtocol().search(new FlagTerm(flags, false)).length;
            }
            return length;
        } catch (ConnectionException e3) {
            throw new FolderClosedException(this, e3.getMessage());
        } catch (ProtocolException e4) {
            throw new MessagingException(e4.getMessage(), e4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleIdle(boolean z) throws MessagingException {
        while (true) {
            Response readIdleResponse = this.protocol.readIdleResponse();
            try {
                synchronized (this.messageCacheLock) {
                    try {
                        if (readIdleResponse.isBYE() && readIdleResponse.isSynthetic() && this.idleState == 1) {
                            Exception exception = readIdleResponse.getException();
                            if ((exception instanceof InterruptedIOException) && ((InterruptedIOException) exception).bytesTransferred == 0) {
                                if (exception instanceof SocketTimeoutException) {
                                    this.logger.finest("handleIdle: ignoring socket timeout");
                                    readIdleResponse = null;
                                } else {
                                    this.logger.finest("handleIdle: interrupting IDLE");
                                    IdleManager idleManager = this.idleManager;
                                    if (idleManager != null) {
                                        this.logger.finest("handleIdle: request IdleManager to abort");
                                        idleManager.requestAbort(this);
                                    } else {
                                        this.logger.finest("handleIdle: abort IDLE");
                                        this.protocol.idleAbort();
                                        this.idleState = 2;
                                    }
                                }
                                if (readIdleResponse == null && !this.protocol.hasResponse()) {
                                    return true;
                                }
                            }
                        }
                        if (this.protocol == null || !this.protocol.processIdleResponse(readIdleResponse)) {
                            break;
                        }
                        if (z && this.idleState == 1) {
                            try {
                                this.protocol.idleAbort();
                            } catch (Exception unused) {
                            }
                            this.idleState = 2;
                        }
                        if (readIdleResponse == null) {
                        }
                    } finally {
                        this.logger.finest("handleIdle: set to RUNNING");
                        this.idleState = 0;
                        this.idleManager = null;
                        this.messageCacheLock.notifyAll();
                    }
                }
            } catch (ConnectionException e) {
                throw new FolderClosedException(this, e.getMessage());
            } catch (ProtocolException e2) {
                throw new MessagingException(e2.getMessage(), e2);
            }
        }
        return false;
    }

    @Override // com.sun.mail.iap.ResponseHandler
    public void handleResponse(Response response) {
        if (response.isOK() || response.isNO() || response.isBAD() || response.isBYE()) {
            ((IMAPStore) this.store).handleResponseCode(response);
        }
        int i = 0;
        if (response.isBYE()) {
            if (this.opened) {
                cleanup(false);
                return;
            }
            return;
        }
        if (response.isOK()) {
            response.skipSpaces();
            if (response.readByte() == 91 && response.readAtom().equalsIgnoreCase("HIGHESTMODSEQ")) {
                this.highestmodseq = response.readLong();
            }
            response.reset();
            return;
        }
        if (response.isUnTagged()) {
            if (!(response instanceof IMAPResponse)) {
                this.logger.fine("UNEXPECTED RESPONSE : " + response.toString());
                return;
            }
            IMAPResponse iMAPResponse = (IMAPResponse) response;
            if (iMAPResponse.keyEquals("EXISTS")) {
                int number = iMAPResponse.getNumber();
                if (number <= this.realTotal) {
                    return;
                }
                int i2 = number - this.realTotal;
                Message[] messageArr = new Message[i2];
                this.messageCache.addMessages(i2, this.realTotal + 1);
                int i3 = this.total;
                this.realTotal += i2;
                this.total += i2;
                if (this.hasMessageCountListener) {
                    while (i < i2) {
                        i3++;
                        messageArr[i] = this.messageCache.getMessage(i3);
                        i++;
                    }
                    notifyMessageAddedListeners(messageArr);
                    return;
                }
                return;
            }
            if (iMAPResponse.keyEquals("EXPUNGE")) {
                int number2 = iMAPResponse.getNumber();
                Message[] messageArr2 = null;
                if (this.doExpungeNotification && this.hasMessageCountListener) {
                    Message[] messageArr3 = {getMessageBySeqNumber(number2)};
                    if (messageArr3[0] != null) {
                        messageArr2 = messageArr3;
                    }
                }
                this.messageCache.expungeMessage(number2);
                this.realTotal--;
                if (messageArr2 != null) {
                    notifyMessageRemovedListeners(false, messageArr2);
                    return;
                }
                return;
            }
            if (!iMAPResponse.keyEquals("VANISHED")) {
                if (!iMAPResponse.keyEquals("FETCH")) {
                    if (iMAPResponse.keyEquals("RECENT")) {
                        this.recent = iMAPResponse.getNumber();
                        return;
                    }
                    return;
                } else {
                    Message processFetchResponse = processFetchResponse((FetchResponse) iMAPResponse);
                    if (processFetchResponse != null) {
                        notifyMessageChangedListeners(1, processFetchResponse);
                        return;
                    }
                    return;
                }
            }
            if (iMAPResponse.readAtomStringList() == null) {
                UIDSet[] parseUIDSets = UIDSet.parseUIDSets(iMAPResponse.readAtom());
                this.realTotal = (int) (this.realTotal - UIDSet.size(parseUIDSets));
                Message[] createMessagesForUIDs = createMessagesForUIDs(UIDSet.toArray(parseUIDSets));
                int length = createMessagesForUIDs.length;
                while (i < length) {
                    Message message = createMessagesForUIDs[i];
                    if (message.getMessageNumber() > 0) {
                        this.messageCache.expungeMessage(message.getMessageNumber());
                    }
                    i++;
                }
                if (this.doExpungeNotification && this.hasMessageCountListener) {
                    notifyMessageRemovedListeners(true, createMessagesForUIDs);
                }
            }
        }
    }

    void handleResponses(Response[] responseArr) {
        for (int i = 0; i < responseArr.length; i++) {
            if (responseArr[i] != null) {
                handleResponse(responseArr[i]);
            }
        }
    }

    @Override // javax.mail.Folder
    public synchronized boolean hasNewMessages() throws MessagingException {
        final String str;
        synchronized (this.messageCacheLock) {
            if (this.opened) {
                try {
                    keepConnectionAlive(true);
                    return this.recent > 0;
                } catch (ConnectionException e) {
                    throw new FolderClosedException(this, e.getMessage());
                } catch (ProtocolException e2) {
                    throw new MessagingException(e2.getMessage(), e2);
                }
            }
            if (!this.isNamespace || this.separator == 0) {
                str = this.fullName;
            } else {
                str = this.fullName + this.separator;
            }
            ListInfo[] listInfoArr = (ListInfo[]) doCommandIgnoreFailure(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.7
                @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
                public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                    return iMAPProtocol.list("", str);
                }
            });
            if (listInfoArr == null) {
                throw new FolderNotFoundException(this, this.fullName + " not found");
            }
            int findName = findName(listInfoArr, str);
            if (listInfoArr[findName].changeState == 1) {
                return true;
            }
            if (listInfoArr[findName].changeState == 2) {
                return false;
            }
            try {
                try {
                    return getStatus().recent > 0;
                } catch (ConnectionException e3) {
                    throw new StoreClosedException(this.store, e3.getMessage());
                }
            } catch (BadCommandException unused) {
                return false;
            } catch (ProtocolException e4) {
                throw new MessagingException(e4.getMessage(), e4);
            }
        }
    }

    public Map<String, String> id(final Map<String, String> map) throws MessagingException {
        checkOpened();
        return (Map) doOptionalCommand("ID not supported", new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.20
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                return iMAPProtocol.id(map);
            }
        });
    }

    public void idle() throws MessagingException {
        idle(false);
    }

    public void idle(boolean z) throws MessagingException {
        synchronized (this) {
            if (this.protocol != null && this.protocol.getChannel() != null) {
                throw new MessagingException("idle method not supported with SocketChannels");
            }
        }
        if (!startIdle(null)) {
            return;
        }
        do {
        } while (handleIdle(z));
        int minIdleTime = ((IMAPStore) this.store).getMinIdleTime();
        if (minIdleTime > 0) {
            try {
                Thread.sleep(minIdleTime);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void idleAbort() {
        synchronized (this.messageCacheLock) {
            if (this.idleState == 1 && this.protocol != null) {
                this.protocol.idleAbort();
                this.idleState = 2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void idleAbortWait() {
        synchronized (this.messageCacheLock) {
            if (this.idleState == 1 && this.protocol != null) {
                this.protocol.idleAbort();
                this.idleState = 2;
                do {
                    try {
                    } catch (Exception e) {
                        this.logger.log(Level.FINEST, "Exception in idleAbortWait", (Throwable) e);
                    }
                } while (handleIdle(false));
                this.logger.finest("IDLE aborted");
            }
        }
    }

    @Override // javax.mail.Folder
    public synchronized boolean isOpen() {
        synchronized (this.messageCacheLock) {
            if (this.opened) {
                try {
                    keepConnectionAlive(false);
                } catch (ProtocolException unused) {
                }
            }
        }
        return this.opened;
    }

    @Override // javax.mail.Folder
    public synchronized boolean isSubscribed() {
        final String str;
        ListInfo[] listInfoArr = null;
        if (!this.isNamespace || this.separator == 0) {
            str = this.fullName;
        } else {
            str = this.fullName + this.separator;
        }
        try {
            listInfoArr = (ListInfo[]) doProtocolCommand(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.4
                @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
                public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                    return iMAPProtocol.lsub("", str);
                }
            });
        } catch (ProtocolException unused) {
        }
        if (listInfoArr == null) {
            return false;
        }
        return listInfoArr[findName(listInfoArr, str)].canOpen;
    }

    protected void keepConnectionAlive(boolean z) throws ProtocolException {
        IMAPProtocol iMAPProtocol;
        Throwable th;
        if (this.protocol == null) {
            return;
        }
        if (System.currentTimeMillis() - this.protocol.getTimestamp() > 1000) {
            waitIfIdle();
            if (this.protocol != null) {
                this.protocol.noop();
            }
        }
        if (!z || !((IMAPStore) this.store).hasSeparateStoreConnection()) {
            return;
        }
        try {
            iMAPProtocol = ((IMAPStore) this.store).getFolderStoreProtocol();
            try {
                if (System.currentTimeMillis() - iMAPProtocol.getTimestamp() > 1000) {
                    iMAPProtocol.noop();
                }
                ((IMAPStore) this.store).releaseFolderStoreProtocol(iMAPProtocol);
            } catch (Throwable th2) {
                th = th2;
                ((IMAPStore) this.store).releaseFolderStoreProtocol(iMAPProtocol);
                throw th;
            }
        } catch (Throwable th3) {
            iMAPProtocol = null;
            th = th3;
        }
    }

    @Override // javax.mail.Folder
    public Folder[] list(String str) throws MessagingException {
        return doList(str, false);
    }

    public Rights[] listRights(final String str) throws MessagingException {
        return (Rights[]) doOptionalCommand("ACL not supported", new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.16
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                return iMAPProtocol.listRights(IMAPFolder.this.fullName, str);
            }
        });
    }

    @Override // javax.mail.Folder
    public Folder[] listSubscribed(String str) throws MessagingException {
        return doList(str, true);
    }

    public synchronized void moveMessages(Message[] messageArr, Folder folder) throws MessagingException {
        copymoveMessages(messageArr, folder, true);
    }

    public synchronized AppendUID[] moveUIDMessages(Message[] messageArr, Folder folder) throws MessagingException {
        return copymoveUIDMessages(messageArr, folder, true);
    }

    public Rights myRights() throws MessagingException {
        return (Rights) doOptionalCommand("ACL not supported", new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.17
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                return iMAPProtocol.myRights(IMAPFolder.this.fullName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IMAPMessage newIMAPMessage(int i) {
        return new IMAPMessage(this, i);
    }

    public synchronized List<MailEvent> open(int i, ResyncData resyncData) throws MessagingException {
        ArrayList arrayList;
        long[] array;
        Message processFetchResponse;
        checkClosed();
        this.protocol = ((IMAPStore) this.store).getProtocol(this);
        synchronized (this.messageCacheLock) {
            this.protocol.addResponseHandler(this);
            if (resyncData != null) {
                try {
                    if (resyncData == ResyncData.CONDSTORE) {
                        if (!this.protocol.isEnabled("CONDSTORE") && !this.protocol.isEnabled("QRESYNC")) {
                            if (this.protocol.hasCapability("CONDSTORE")) {
                                this.protocol.enable("CONDSTORE");
                            } else {
                                this.protocol.enable("QRESYNC");
                            }
                        }
                    } else if (!this.protocol.isEnabled("QRESYNC")) {
                        this.protocol.enable("QRESYNC");
                    }
                } catch (CommandFailedException e) {
                    try {
                        checkExists();
                        if ((this.type & 1) == 0) {
                            throw new MessagingException("folder cannot contain messages");
                        }
                        throw new MessagingException(e.getMessage(), e);
                    } catch (Throwable th) {
                        this.exists = false;
                        this.attributes = null;
                        this.type = 0;
                        releaseProtocol(true);
                        throw th;
                    }
                } catch (ProtocolException e2) {
                    try {
                        throw logoutAndThrow(e2.getMessage(), e2);
                    } catch (Throwable th2) {
                        releaseProtocol(false);
                        throw th2;
                    }
                }
            }
            MailboxInfo examine = i == 1 ? this.protocol.examine(this.fullName, resyncData) : this.protocol.select(this.fullName, resyncData);
            if (examine.mode != i && (i != 2 || examine.mode != 1 || !((IMAPStore) this.store).allowReadOnlySelect())) {
                throw cleanupAndThrow(new ReadOnlyFolderException(this, "Cannot open in desired mode"));
            }
            this.opened = true;
            this.reallyClosed = false;
            this.mode = examine.mode;
            this.availableFlags = examine.availableFlags;
            this.permanentFlags = examine.permanentFlags;
            int i2 = examine.total;
            this.realTotal = i2;
            this.total = i2;
            this.recent = examine.recent;
            this.uidvalidity = examine.uidvalidity;
            this.uidnext = examine.uidnext;
            this.uidNotSticky = examine.uidNotSticky;
            this.highestmodseq = examine.highestmodseq;
            this.messageCache = new MessageCache(this, (IMAPStore) this.store, this.total);
            if (examine.responses != null) {
                arrayList = new ArrayList();
                for (IMAPResponse iMAPResponse : examine.responses) {
                    if (iMAPResponse.keyEquals("VANISHED")) {
                        String[] readAtomStringList = iMAPResponse.readAtomStringList();
                        if (readAtomStringList != null && readAtomStringList.length == 1 && readAtomStringList[0].equalsIgnoreCase("EARLIER") && (array = UIDSet.toArray(UIDSet.parseUIDSets(iMAPResponse.readAtom()), this.uidnext)) != null && array.length > 0) {
                            arrayList.add(new MessageVanishedEvent(this, array));
                        }
                    } else if (iMAPResponse.keyEquals("FETCH") && (processFetchResponse = processFetchResponse((FetchResponse) iMAPResponse)) != null) {
                        arrayList.add(new MessageChangedEvent(this, 1, processFetchResponse));
                    }
                }
            } else {
                arrayList = null;
            }
        }
        this.exists = true;
        this.attributes = null;
        this.type = 1;
        notifyConnectionListeners(1);
        return arrayList;
    }

    @Override // javax.mail.Folder
    public synchronized void open(int i) throws MessagingException {
        open(i, null);
    }

    protected void releaseProtocol(boolean z) {
        if (this.protocol != null) {
            this.protocol.removeResponseHandler(this);
            if (z) {
                ((IMAPStore) this.store).releaseProtocol(this, this.protocol);
            } else {
                this.protocol.disconnect();
                ((IMAPStore) this.store).releaseProtocol(this, null);
            }
            this.protocol = null;
        }
    }

    protected synchronized void releaseStoreProtocol(IMAPProtocol iMAPProtocol) {
        if (iMAPProtocol != this.protocol) {
            ((IMAPStore) this.store).releaseFolderStoreProtocol(iMAPProtocol);
        } else {
            this.logger.fine("releasing our protocol as store protocol?");
        }
    }

    public void removeACL(final String str) throws MessagingException {
        doOptionalCommand("ACL not supported", new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.15
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                iMAPProtocol.deleteACL(IMAPFolder.this.fullName, str);
                return null;
            }
        });
    }

    public void removeRights(ACL acl) throws MessagingException {
        setACL(acl, '-');
    }

    @Override // javax.mail.Folder
    public synchronized boolean renameTo(final Folder folder) throws MessagingException {
        checkClosed();
        checkExists();
        if (folder.getStore() != this.store) {
            throw new MessagingException("Can't rename across Stores");
        }
        if (doCommandIgnoreFailure(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.9
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                iMAPProtocol.rename(IMAPFolder.this.fullName, folder.getFullName());
                return Boolean.TRUE;
            }
        }) == null) {
            return false;
        }
        this.exists = false;
        this.attributes = null;
        notifyFolderRenamedListeners(folder);
        return true;
    }

    @Override // javax.mail.Folder
    public synchronized Message[] search(SearchTerm searchTerm) throws MessagingException {
        IMAPMessage[] messagesBySeqNumbers;
        checkOpened();
        try {
            try {
                synchronized (this.messageCacheLock) {
                    int[] search = getProtocol().search(searchTerm);
                    messagesBySeqNumbers = search != null ? getMessagesBySeqNumbers(search) : null;
                }
            } catch (CommandFailedException unused) {
                return super.search(searchTerm);
            } catch (ConnectionException e) {
                throw new FolderClosedException(this, e.getMessage());
            }
        } catch (ProtocolException e2) {
            throw new MessagingException(e2.getMessage(), e2);
        } catch (SearchException e3) {
            if (((IMAPStore) this.store).throwSearchException()) {
                throw e3;
            }
            return super.search(searchTerm);
        }
        return messagesBySeqNumbers;
    }

    @Override // javax.mail.Folder
    public synchronized Message[] search(SearchTerm searchTerm, Message[] messageArr) throws MessagingException {
        IMAPMessage[] messagesBySeqNumbers;
        checkOpened();
        if (messageArr.length == 0) {
            return messageArr;
        }
        try {
            try {
                try {
                    try {
                        synchronized (this.messageCacheLock) {
                            IMAPProtocol protocol = getProtocol();
                            MessageSet[] messageSetSorted = Utility.toMessageSetSorted(messageArr, null);
                            if (messageSetSorted == null) {
                                throw new MessageRemovedException("Messages have been removed");
                            }
                            int[] search = protocol.search(messageSetSorted, searchTerm);
                            messagesBySeqNumbers = search != null ? getMessagesBySeqNumbers(search) : null;
                        }
                        return messagesBySeqNumbers;
                    } catch (CommandFailedException unused) {
                        return super.search(searchTerm, messageArr);
                    }
                } catch (ProtocolException e) {
                    throw new MessagingException(e.getMessage(), e);
                }
            } catch (SearchException unused2) {
                return super.search(searchTerm, messageArr);
            }
        } catch (ConnectionException e2) {
            throw new FolderClosedException(this, e2.getMessage());
        }
    }

    @Override // javax.mail.Folder
    public synchronized void setFlags(int i, int i2, Flags flags, boolean z) throws MessagingException {
        checkOpened();
        Message[] messageArr = new Message[(i2 - i) + 1];
        int i3 = 0;
        while (i <= i2) {
            messageArr[i3] = getMessage(i);
            i++;
            i3++;
        }
        setFlags(messageArr, flags, z);
    }

    @Override // javax.mail.Folder
    public synchronized void setFlags(int[] iArr, Flags flags, boolean z) throws MessagingException {
        checkOpened();
        Message[] messageArr = new Message[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            messageArr[i] = getMessage(iArr[i]);
        }
        setFlags(messageArr, flags, z);
    }

    @Override // javax.mail.Folder
    public synchronized void setFlags(Message[] messageArr, Flags flags, boolean z) throws MessagingException {
        checkOpened();
        checkFlags(flags);
        if (messageArr.length == 0) {
            return;
        }
        synchronized (this.messageCacheLock) {
            try {
                IMAPProtocol protocol = getProtocol();
                MessageSet[] messageSetSorted = Utility.toMessageSetSorted(messageArr, null);
                if (messageSetSorted == null) {
                    throw new MessageRemovedException("Messages have been removed");
                }
                protocol.storeFlags(messageSetSorted, flags, z);
            } catch (ConnectionException e) {
                throw new FolderClosedException(this, e.getMessage());
            } catch (ProtocolException e2) {
                throw new MessagingException(e2.getMessage(), e2);
            }
        }
    }

    public void setQuota(final Quota quota) throws MessagingException {
        doOptionalCommand("QUOTA not supported", new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.13
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                iMAPProtocol.setQuota(quota);
                return null;
            }
        });
    }

    @Override // javax.mail.Folder
    public synchronized void setSubscribed(final boolean z) throws MessagingException {
        doCommandIgnoreFailure(new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.5
            @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
            public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                if (z) {
                    iMAPProtocol.subscribe(IMAPFolder.this.fullName);
                    return null;
                }
                iMAPProtocol.unsubscribe(IMAPFolder.this.fullName);
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startIdle(final IdleManager idleManager) throws MessagingException {
        boolean booleanValue;
        synchronized (this) {
            checkOpened();
            if (idleManager != null && this.idleManager != null && idleManager != this.idleManager) {
                throw new MessagingException("Folder already being watched by another IdleManager");
            }
            Boolean bool = (Boolean) doOptionalCommand("IDLE not supported", new ProtocolCommand() { // from class: com.sun.mail.imap.IMAPFolder.19
                @Override // com.sun.mail.imap.IMAPFolder.ProtocolCommand
                public Object doCommand(IMAPProtocol iMAPProtocol) throws ProtocolException {
                    if (IMAPFolder.this.idleState == 1 && idleManager != null && idleManager == IMAPFolder.this.idleManager) {
                        return Boolean.TRUE;
                    }
                    if (IMAPFolder.this.idleState != 0) {
                        try {
                            IMAPFolder.this.messageCacheLock.wait();
                        } catch (InterruptedException unused) {
                            Thread.currentThread().interrupt();
                        }
                        return Boolean.FALSE;
                    }
                    iMAPProtocol.idleStart();
                    IMAPFolder.this.logger.finest("startIdle: set to IDLE");
                    IMAPFolder.this.idleState = 1;
                    IMAPFolder.this.idleManager = idleManager;
                    return Boolean.TRUE;
                }
            });
            this.logger.log(Level.FINEST, "startIdle: return {0}", bool);
            booleanValue = bool.booleanValue();
        }
        return booleanValue;
    }

    protected synchronized void throwClosedException(ConnectionException connectionException) throws FolderClosedException, StoreClosedException {
        if ((this.protocol != null && connectionException.getProtocol() == this.protocol) || (this.protocol == null && !this.reallyClosed)) {
            throw new FolderClosedException(this, connectionException.getMessage());
        }
        throw new StoreClosedException(this.store, connectionException.getMessage());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void waitIfIdle() throws ProtocolException {
        while (this.idleState != 0) {
            if (this.idleState == 1) {
                IdleManager idleManager = this.idleManager;
                if (idleManager != null) {
                    this.logger.finest("waitIfIdle: request IdleManager to abort");
                    idleManager.requestAbort(this);
                } else {
                    this.logger.finest("waitIfIdle: abort IDLE");
                    this.protocol.idleAbort();
                    this.idleState = 2;
                }
            } else {
                this.logger.log(Level.FINEST, "waitIfIdle: idleState {0}", Integer.valueOf(this.idleState));
            }
            try {
                if (this.logger.isLoggable(Level.FINEST)) {
                    this.logger.finest("waitIfIdle: wait to be not idle: " + Thread.currentThread());
                }
                this.messageCacheLock.wait();
                if (this.logger.isLoggable(Level.FINEST)) {
                    this.logger.finest("waitIfIdle: wait done, idleState " + this.idleState + ": " + Thread.currentThread());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ProtocolException("Interrupted waitIfIdle", e);
            }
        }
    }
}
