package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Response;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class Status {
    static final String[] standardItems = {"MESSAGES", "RECENT", "UNSEEN", "UIDNEXT", "UIDVALIDITY"};
    public long highestmodseq;
    public Map<String, Long> items;
    public String mbox;
    public int recent;
    public int total;
    public long uidnext;
    public long uidvalidity;
    public int unseen;

    public Status(Response response) throws ParsingException {
        this.mbox = null;
        this.total = -1;
        this.recent = -1;
        this.uidnext = -1L;
        this.uidvalidity = -1L;
        this.unseen = -1;
        this.highestmodseq = -1L;
        this.mbox = response.readAtomString();
        if (!response.supportsUtf8()) {
            this.mbox = BASE64MailboxDecoder.decode(this.mbox);
        }
        StringBuffer stringBuffer = new StringBuffer();
        boolean z = true;
        while (response.peekByte() != 40 && response.peekByte() != 0) {
            char readByte = (char) response.readByte();
            stringBuffer.append(readByte);
            if (readByte != ' ') {
                z = false;
            }
        }
        if (!z) {
            this.mbox = (this.mbox + ((Object) stringBuffer)).trim();
        }
        if (response.readByte() != 40) {
            throw new ParsingException("parse error in STATUS");
        }
        do {
            String readAtom = response.readAtom();
            if (readAtom == null) {
                throw new ParsingException("parse error in STATUS");
            }
            if (readAtom.equalsIgnoreCase("MESSAGES")) {
                this.total = response.readNumber();
            } else if (readAtom.equalsIgnoreCase("RECENT")) {
                this.recent = response.readNumber();
            } else if (readAtom.equalsIgnoreCase("UIDNEXT")) {
                this.uidnext = response.readLong();
            } else if (readAtom.equalsIgnoreCase("UIDVALIDITY")) {
                this.uidvalidity = response.readLong();
            } else if (readAtom.equalsIgnoreCase("UNSEEN")) {
                this.unseen = response.readNumber();
            } else if (readAtom.equalsIgnoreCase("HIGHESTMODSEQ")) {
                this.highestmodseq = response.readLong();
            } else {
                if (this.items == null) {
                    this.items = new HashMap();
                }
                this.items.put(readAtom.toUpperCase(Locale.ENGLISH), Long.valueOf(response.readLong()));
            }
        } while (!response.isNextNonSpace(')'));
    }

    public static void add(Status status, Status status2) {
        if (status2.total != -1) {
            status.total = status2.total;
        }
        if (status2.recent != -1) {
            status.recent = status2.recent;
        }
        if (status2.uidnext != -1) {
            status.uidnext = status2.uidnext;
        }
        if (status2.uidvalidity != -1) {
            status.uidvalidity = status2.uidvalidity;
        }
        if (status2.unseen != -1) {
            status.unseen = status2.unseen;
        }
        if (status2.highestmodseq != -1) {
            status.highestmodseq = status2.highestmodseq;
        }
        if (status.items == null) {
            status.items = status2.items;
        } else if (status2.items != null) {
            status.items.putAll(status2.items);
        }
    }

    public long getItem(String str) {
        Long l;
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        if (this.items != null && (l = this.items.get(upperCase)) != null) {
            return l.longValue();
        }
        if (upperCase.equals("MESSAGES")) {
            return this.total;
        }
        if (upperCase.equals("RECENT")) {
            return this.recent;
        }
        if (upperCase.equals("UIDNEXT")) {
            return this.uidnext;
        }
        if (upperCase.equals("UIDVALIDITY")) {
            return this.uidvalidity;
        }
        if (upperCase.equals("UNSEEN")) {
            return this.unseen;
        }
        if (upperCase.equals("HIGHESTMODSEQ")) {
            return this.highestmodseq;
        }
        return -1L;
    }
}
