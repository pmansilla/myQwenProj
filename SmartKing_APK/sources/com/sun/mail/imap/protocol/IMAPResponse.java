package com.sun.mail.imap.protocol;

import com.sun.mail.iap.Protocol;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.util.ASCIIUtility;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class IMAPResponse extends Response {
    private String key;
    private int number;

    public IMAPResponse(Protocol protocol) throws IOException, ProtocolException {
        super(protocol);
        init();
    }

    public IMAPResponse(IMAPResponse iMAPResponse) {
        super(iMAPResponse);
        this.key = iMAPResponse.key;
        this.number = iMAPResponse.number;
    }

    public IMAPResponse(String str) throws IOException, ProtocolException {
        this(str, true);
    }

    public IMAPResponse(String str, boolean z) throws IOException, ProtocolException {
        super(str, z);
        init();
    }

    private void init() throws IOException, ProtocolException {
        if (!isUnTagged() || isOK() || isNO() || isBAD() || isBYE()) {
            return;
        }
        this.key = readAtom();
        try {
            this.number = Integer.parseInt(this.key);
            this.key = readAtom();
        } catch (NumberFormatException unused) {
        }
    }

    public String getKey() {
        return this.key;
    }

    public int getNumber() {
        return this.number;
    }

    public boolean keyEquals(String str) {
        return this.key != null && this.key.equalsIgnoreCase(str);
    }

    public String[] readSimpleList() {
        skipSpaces();
        if (this.buffer[this.index] != 40) {
            return null;
        }
        this.index++;
        ArrayList arrayList = new ArrayList();
        int i = this.index;
        while (this.buffer[this.index] != 41) {
            if (this.buffer[this.index] == 32) {
                arrayList.add(ASCIIUtility.toString(this.buffer, i, this.index));
                i = this.index + 1;
            }
            this.index++;
        }
        if (this.index > i) {
            arrayList.add(ASCIIUtility.toString(this.buffer, i, this.index));
        }
        this.index++;
        int size = arrayList.size();
        if (size > 0) {
            return (String[]) arrayList.toArray(new String[size]);
        }
        return null;
    }
}
