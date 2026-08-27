package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Protocol;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.util.ASCIIUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ClassUtils;

/* loaded from: classes2.dex */
public class FetchResponse extends IMAPResponse {
    private static final char[] HEADER = {ClassUtils.PACKAGE_SEPARATOR_CHAR, 'H', 'E', 'A', 'D', 'E', 'R'};
    private static final char[] TEXT = {ClassUtils.PACKAGE_SEPARATOR_CHAR, 'T', 'E', 'X', 'T'};
    private Map<String, Object> extensionItems;
    private final FetchItem[] fitems;
    private Item[] items;

    public FetchResponse(Protocol protocol) throws IOException, ProtocolException {
        super(protocol);
        this.fitems = null;
        parse();
    }

    public FetchResponse(IMAPResponse iMAPResponse) throws IOException, ProtocolException {
        this(iMAPResponse, null);
    }

    public FetchResponse(IMAPResponse iMAPResponse, FetchItem[] fetchItemArr) throws IOException, ProtocolException {
        super(iMAPResponse);
        this.fitems = fetchItemArr;
        parse();
    }

    public static <T extends Item> T getItem(Response[] responseArr, int i, Class<T> cls) {
        if (responseArr == null) {
            return null;
        }
        for (int i2 = 0; i2 < responseArr.length; i2++) {
            if (responseArr[i2] != null && (responseArr[i2] instanceof FetchResponse) && ((FetchResponse) responseArr[i2]).getNumber() == i) {
                FetchResponse fetchResponse = (FetchResponse) responseArr[i2];
                for (int i3 = 0; i3 < fetchResponse.items.length; i3++) {
                    if (cls.isInstance(fetchResponse.items[i3])) {
                        return cls.cast(fetchResponse.items[i3]);
                    }
                }
            }
        }
        return null;
    }

    public static <T extends Item> List<T> getItems(Response[] responseArr, int i, Class<T> cls) {
        ArrayList arrayList = new ArrayList();
        if (responseArr == null) {
            return arrayList;
        }
        for (int i2 = 0; i2 < responseArr.length; i2++) {
            if (responseArr[i2] != null && (responseArr[i2] instanceof FetchResponse) && ((FetchResponse) responseArr[i2]).getNumber() == i) {
                FetchResponse fetchResponse = (FetchResponse) responseArr[i2];
                for (int i3 = 0; i3 < fetchResponse.items.length; i3++) {
                    if (cls.isInstance(fetchResponse.items[i3])) {
                        arrayList.add(cls.cast(fetchResponse.items[i3]));
                    }
                }
            }
        }
        return arrayList;
    }

    private boolean match(String str) {
        int length = str.length();
        int i = this.index;
        int i2 = 0;
        while (i2 < length) {
            int i3 = i + 1;
            int i4 = i2 + 1;
            if (Character.toUpperCase((char) this.buffer[i]) != str.charAt(i2)) {
                return false;
            }
            i2 = i4;
            i = i3;
        }
        this.index += length;
        return true;
    }

    private boolean match(char[] cArr) {
        int length = cArr.length;
        int i = this.index;
        int i2 = 0;
        while (i2 < length) {
            int i3 = i + 1;
            int i4 = i2 + 1;
            if (Character.toUpperCase((char) this.buffer[i]) != cArr[i2]) {
                return false;
            }
            i2 = i4;
            i = i3;
        }
        this.index += length;
        return true;
    }

    private String next20() {
        if (this.index + 20 > this.size) {
            return ASCIIUtility.toString(this.buffer, this.index, this.size);
        }
        return ASCIIUtility.toString(this.buffer, this.index, this.index + 20) + "...";
    }

    private void parse() throws ParsingException {
        if (!isNextNonSpace('(')) {
            throw new ParsingException("error in FETCH parsing, missing '(' at index " + this.index);
        }
        ArrayList arrayList = new ArrayList();
        skipSpaces();
        while (this.index < this.size) {
            Item parseItem = parseItem();
            if (parseItem != null) {
                arrayList.add(parseItem);
            } else if (!parseExtensionItem()) {
                throw new ParsingException("error in FETCH parsing, unrecognized item at index " + this.index + ", starts with \"" + next20() + "\"");
            }
            if (isNextNonSpace(')')) {
                this.items = (Item[]) arrayList.toArray(new Item[arrayList.size()]);
                return;
            }
        }
        throw new ParsingException("error in FETCH parsing, ran off end of buffer, size " + this.size);
    }

    private boolean parseExtensionItem() throws ParsingException {
        if (this.fitems == null) {
            return false;
        }
        for (int i = 0; i < this.fitems.length; i++) {
            if (match(this.fitems[i].getName())) {
                if (this.extensionItems == null) {
                    this.extensionItems = new HashMap();
                }
                this.extensionItems.put(this.fitems[i].getName(), this.fitems[i].parseItem(this));
                return true;
            }
        }
        return false;
    }

    private Item parseItem() throws ParsingException {
        switch (this.buffer[this.index]) {
            case 66:
            case 98:
                if (match(BODYSTRUCTURE.name)) {
                    return new BODYSTRUCTURE(this);
                }
                if (match(BODY.name)) {
                    return this.buffer[this.index] == 91 ? new BODY(this) : new BODYSTRUCTURE(this);
                }
                return null;
            case 69:
            case 101:
                if (match(ENVELOPE.name)) {
                    return new ENVELOPE(this);
                }
                return null;
            case 70:
            case 102:
                if (match(FLAGS.name)) {
                    return new FLAGS(this);
                }
                return null;
            case 73:
            case 105:
                if (match(INTERNALDATE.name)) {
                    return new INTERNALDATE(this);
                }
                return null;
            case 77:
            case 109:
                if (match(MODSEQ.name)) {
                    return new MODSEQ(this);
                }
                return null;
            case 82:
            case 114:
                if (match(RFC822SIZE.name)) {
                    return new RFC822SIZE(this);
                }
                if (!match(RFC822DATA.name)) {
                    return null;
                }
                boolean z = false;
                if (match(HEADER)) {
                    z = true;
                } else {
                    match(TEXT);
                }
                return new RFC822DATA(this, z);
            case 85:
            case 117:
                if (match(UID.name)) {
                    return new UID(this);
                }
                return null;
            default:
                return null;
        }
    }

    public Map<String, Object> getExtensionItems() {
        return this.extensionItems;
    }

    public Item getItem(int i) {
        return this.items[i];
    }

    public <T extends Item> T getItem(Class<T> cls) {
        for (int i = 0; i < this.items.length; i++) {
            if (cls.isInstance(this.items[i])) {
                return cls.cast(this.items[i]);
            }
        }
        return null;
    }

    public int getItemCount() {
        return this.items.length;
    }
}
