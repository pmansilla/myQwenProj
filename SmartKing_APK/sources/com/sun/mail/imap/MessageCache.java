package com.sun.mail.imap;

import com.litesuits.orm.db.assit.SQLBuilder;
import com.sun.mail.util.MailLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import javax.mail.Message;

/* loaded from: classes2.dex */
public class MessageCache {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int SLOP = 64;
    private IMAPFolder folder;
    private MailLogger logger;
    private IMAPMessage[] messages;
    private int[] seqnums;
    private int size;

    MessageCache(int i, boolean z) {
        this.folder = null;
        this.logger = new MailLogger(getClass(), "messagecache", "DEBUG IMAP MC", z, System.out);
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("create DEBUG cache of size " + i);
        }
        ensureCapacity(i, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MessageCache(IMAPFolder iMAPFolder, IMAPStore iMAPStore, int i) {
        this.folder = iMAPFolder;
        this.logger = iMAPFolder.logger.getSubLogger("messagecache", "DEBUG IMAP MC", iMAPStore.getMessageCacheDebug());
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("create cache of size " + i);
        }
        ensureCapacity(i, 1);
    }

    private void ensureCapacity(int i, int i2) {
        if (this.messages == null) {
            this.messages = new IMAPMessage[i + 64];
        } else if (this.messages.length < i) {
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("expand capacity to " + i);
            }
            int i3 = i + 64;
            IMAPMessage[] iMAPMessageArr = new IMAPMessage[i3];
            System.arraycopy(this.messages, 0, iMAPMessageArr, 0, this.messages.length);
            this.messages = iMAPMessageArr;
            if (this.seqnums != null) {
                int[] iArr = new int[i3];
                System.arraycopy(this.seqnums, 0, iArr, 0, this.seqnums.length);
                int i4 = this.size;
                while (i4 < iArr.length) {
                    iArr[i4] = i2;
                    i4++;
                    i2++;
                }
                this.seqnums = iArr;
                if (this.logger.isLoggable(Level.FINE)) {
                    this.logger.fine("message " + i + " has sequence number " + this.seqnums[i - 1]);
                }
            }
        } else if (i < this.size) {
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("shrink capacity to " + i);
            }
            for (int i5 = i + 1; i5 <= this.size; i5++) {
                int i6 = i5 - 1;
                this.messages[i6] = null;
                if (this.seqnums != null) {
                    this.seqnums[i6] = -1;
                }
            }
        }
        this.size = i;
    }

    private int msgnumOf(int i) {
        if (this.seqnums == null) {
            return i;
        }
        if (i < 1) {
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("bad seqnum " + i);
            }
            return -1;
        }
        for (int i2 = i; i2 <= this.size; i2++) {
            int i3 = i2 - 1;
            if (this.seqnums[i3] == i) {
                return i2;
            }
            if (this.seqnums[i3] > i) {
                break;
            }
        }
        return -1;
    }

    private void shrink(int i, int i2) {
        this.size = i - 1;
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("size now " + this.size);
        }
        if (this.size == 0) {
            this.messages = null;
            this.seqnums = null;
            return;
        }
        if (this.size > 64 && this.size < this.messages.length / 2) {
            this.logger.fine("reallocate array");
            IMAPMessage[] iMAPMessageArr = new IMAPMessage[this.size + 64];
            System.arraycopy(this.messages, 0, iMAPMessageArr, 0, this.size);
            this.messages = iMAPMessageArr;
            if (this.seqnums != null) {
                int[] iArr = new int[this.size + 64];
                System.arraycopy(this.seqnums, 0, iArr, 0, this.size);
                this.seqnums = iArr;
                return;
            }
            return;
        }
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("clean " + i + " to " + i2);
        }
        while (i < i2) {
            int i3 = i - 1;
            this.messages[i3] = null;
            if (this.seqnums != null) {
                this.seqnums[i3] = 0;
            }
            i++;
        }
    }

    public void addMessages(int i, int i2) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("add " + i + " messages");
        }
        ensureCapacity(this.size + i, i2);
    }

    public void expungeMessage(int i) {
        int msgnumOf = msgnumOf(i);
        if (msgnumOf < 0) {
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("expunge no seqnum " + i);
                return;
            }
            return;
        }
        int i2 = msgnumOf - 1;
        IMAPMessage iMAPMessage = this.messages[i2];
        if (iMAPMessage != null) {
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("expunge existing " + msgnumOf);
            }
            iMAPMessage.setExpunged(true);
        }
        if (this.seqnums != null) {
            this.seqnums[i2] = 0;
            for (int i3 = msgnumOf + 1; i3 <= this.seqnums.length; i3++) {
                int i4 = i3 - 1;
                if (this.seqnums[i4] > 0) {
                    int[] iArr = this.seqnums;
                    iArr[i4] = iArr[i4] - 1;
                }
            }
            return;
        }
        this.logger.fine("create seqnums array");
        this.seqnums = new int[this.messages.length];
        for (int i5 = 1; i5 < msgnumOf; i5++) {
            this.seqnums[i5 - 1] = i5;
        }
        this.seqnums[i2] = 0;
        for (int i6 = msgnumOf + 1; i6 <= this.seqnums.length; i6++) {
            int i7 = i6 - 1;
            this.seqnums[i7] = i7;
        }
    }

    public IMAPMessage getMessage(int i) {
        if (i < 1 || i > this.size) {
            throw new ArrayIndexOutOfBoundsException("message number (" + i + ") out of bounds (" + this.size + SQLBuilder.PARENTHESES_RIGHT);
        }
        int i2 = i - 1;
        IMAPMessage iMAPMessage = this.messages[i2];
        if (iMAPMessage == null) {
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("create message number " + i);
            }
            iMAPMessage = this.folder.newIMAPMessage(i);
            this.messages[i2] = iMAPMessage;
            if (seqnumOf(i) <= 0) {
                this.logger.fine("it's expunged!");
                iMAPMessage.setExpunged(true);
            }
        }
        return iMAPMessage;
    }

    public IMAPMessage getMessageBySeqnum(int i) {
        int msgnumOf = msgnumOf(i);
        if (msgnumOf >= 0) {
            return getMessage(msgnumOf);
        }
        if (!this.logger.isLoggable(Level.FINE)) {
            return null;
        }
        this.logger.fine("no message seqnum " + i);
        return null;
    }

    public IMAPMessage[] removeExpungedMessages() {
        this.logger.fine("remove expunged messages");
        ArrayList arrayList = new ArrayList();
        int i = 1;
        int i2 = 1;
        while (i <= this.size) {
            if (seqnumOf(i) <= 0) {
                arrayList.add(getMessage(i));
            } else {
                if (i2 != i) {
                    int i3 = i2 - 1;
                    this.messages[i3] = this.messages[i - 1];
                    if (this.messages[i3] != null) {
                        this.messages[i3].setMessageNumber(i2);
                    }
                }
                i2++;
            }
            i++;
        }
        this.seqnums = null;
        shrink(i2, i);
        IMAPMessage[] iMAPMessageArr = new IMAPMessage[arrayList.size()];
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("return " + iMAPMessageArr.length);
        }
        arrayList.toArray(iMAPMessageArr);
        return iMAPMessageArr;
    }

    public IMAPMessage[] removeExpungedMessages(Message[] messageArr) {
        this.logger.fine("remove expunged messages");
        ArrayList arrayList = new ArrayList();
        int[] iArr = new int[messageArr.length];
        for (int i = 0; i < messageArr.length; i++) {
            iArr[i] = messageArr[i].getMessageNumber();
        }
        Arrays.sort(iArr);
        int i2 = 1;
        int i3 = 0;
        boolean z = false;
        int i4 = 1;
        while (i2 <= this.size) {
            if (i3 >= iArr.length || i2 != iArr[i3] || seqnumOf(i2) > 0) {
                if (i4 != i2) {
                    int i5 = i4 - 1;
                    int i6 = i2 - 1;
                    this.messages[i5] = this.messages[i6];
                    if (this.messages[i5] != null) {
                        this.messages[i5].setMessageNumber(i4);
                    }
                    if (this.seqnums != null) {
                        this.seqnums[i5] = this.seqnums[i6];
                    }
                }
                if (this.seqnums != null && this.seqnums[i4 - 1] != i4) {
                    z = true;
                }
                i4++;
            } else {
                arrayList.add(getMessage(i2));
                while (i3 < iArr.length && iArr[i3] <= i2) {
                    i3++;
                }
            }
            i2++;
        }
        if (!z) {
            this.seqnums = null;
        }
        shrink(i4, i2);
        IMAPMessage[] iMAPMessageArr = new IMAPMessage[arrayList.size()];
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("return " + iMAPMessageArr.length);
        }
        arrayList.toArray(iMAPMessageArr);
        return iMAPMessageArr;
    }

    public int seqnumOf(int i) {
        if (this.seqnums == null) {
            return i;
        }
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("msgnum " + i + " is seqnum " + this.seqnums[i - 1]);
        }
        return this.seqnums[i - 1];
    }

    public int size() {
        return this.size;
    }
}
