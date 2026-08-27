package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Response;
import com.sun.mail.util.PropUtil;
import java.util.ArrayList;
import javax.mail.internet.ParameterList;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes2.dex */
public class BODYSTRUCTURE implements Item {
    public String attachment;
    public BODYSTRUCTURE[] bodies;
    public ParameterList cParams;
    public ParameterList dParams;
    public String description;
    public String disposition;
    public String encoding;
    public ENVELOPE envelope;
    public String id;
    public String[] language;
    public int lines;
    public String md5;
    public int msgno;
    private int processedType;
    public int size;
    public String subtype;
    public String type;
    static final char[] name = {'B', 'O', 'D', 'Y', 'S', 'T', 'R', 'U', 'C', 'T', 'U', 'R', 'E'};
    private static int SINGLE = 1;
    private static int MULTI = 2;
    private static int NESTED = 3;
    private static final boolean parseDebug = PropUtil.getBooleanSystemProperty("mail.imap.parse.debug", false);

    public BODYSTRUCTURE(FetchResponse fetchResponse) throws ParsingException {
        this.lines = -1;
        this.size = -1;
        if (parseDebug) {
            System.out.println("DEBUG IMAP: parsing BODYSTRUCTURE");
        }
        this.msgno = fetchResponse.getNumber();
        if (parseDebug) {
            System.out.println("DEBUG IMAP: msgno " + this.msgno);
        }
        fetchResponse.skipSpaces();
        if (fetchResponse.readByte() != 40) {
            throw new ParsingException("BODYSTRUCTURE parse error: missing ``('' at start");
        }
        if (fetchResponse.peekByte() == 40) {
            if (parseDebug) {
                System.out.println("DEBUG IMAP: parsing multipart");
            }
            this.type = "multipart";
            this.processedType = MULTI;
            ArrayList arrayList = new ArrayList(1);
            do {
                arrayList.add(new BODYSTRUCTURE(fetchResponse));
                fetchResponse.skipSpaces();
            } while (fetchResponse.peekByte() == 40);
            this.bodies = (BODYSTRUCTURE[]) arrayList.toArray(new BODYSTRUCTURE[arrayList.size()]);
            this.subtype = fetchResponse.readString();
            if (parseDebug) {
                System.out.println("DEBUG IMAP: subtype " + this.subtype);
            }
            if (fetchResponse.isNextNonSpace(')')) {
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: parse DONE");
                    return;
                }
                return;
            }
            if (parseDebug) {
                System.out.println("DEBUG IMAP: parsing extension data");
            }
            this.cParams = parseParameters(fetchResponse);
            if (fetchResponse.isNextNonSpace(')')) {
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: body parameters DONE");
                    return;
                }
                return;
            }
            byte readByte = fetchResponse.readByte();
            if (readByte == 40) {
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: parse disposition");
                }
                this.disposition = fetchResponse.readString();
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: disposition " + this.disposition);
                }
                this.dParams = parseParameters(fetchResponse);
                if (!fetchResponse.isNextNonSpace(')')) {
                    throw new ParsingException("BODYSTRUCTURE parse error: missing ``)'' at end of disposition in multipart");
                }
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: disposition DONE");
                }
            } else {
                if (readByte != 78 && readByte != 110) {
                    throw new ParsingException("BODYSTRUCTURE parse error: " + this.type + FileUriModel.SCHEME + this.subtype + ": bad multipart disposition, b " + ((int) readByte));
                }
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: disposition NIL");
                }
                fetchResponse.skip(2);
            }
            if (fetchResponse.isNextNonSpace(')')) {
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: no body-fld-lang");
                    return;
                }
                return;
            }
            if (fetchResponse.peekByte() == 40) {
                this.language = fetchResponse.readStringList();
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: language len " + this.language.length);
                }
            } else {
                String readString = fetchResponse.readString();
                if (readString != null) {
                    this.language = new String[]{readString};
                    if (parseDebug) {
                        System.out.println("DEBUG IMAP: language " + readString);
                    }
                }
            }
            while (fetchResponse.readByte() == 32) {
                parseBodyExtension(fetchResponse);
            }
            return;
        }
        if (fetchResponse.peekByte() == 41) {
            throw new ParsingException("BODYSTRUCTURE parse error: missing body content");
        }
        if (parseDebug) {
            System.out.println("DEBUG IMAP: single part");
        }
        this.type = fetchResponse.readString();
        if (parseDebug) {
            System.out.println("DEBUG IMAP: type " + this.type);
        }
        this.processedType = SINGLE;
        this.subtype = fetchResponse.readString();
        if (parseDebug) {
            System.out.println("DEBUG IMAP: subtype " + this.subtype);
        }
        if (this.type == null) {
            this.type = "application";
            this.subtype = "octet-stream";
        }
        this.cParams = parseParameters(fetchResponse);
        if (parseDebug) {
            System.out.println("DEBUG IMAP: cParams " + this.cParams);
        }
        this.id = fetchResponse.readString();
        if (parseDebug) {
            System.out.println("DEBUG IMAP: id " + this.id);
        }
        this.description = fetchResponse.readString();
        if (parseDebug) {
            System.out.println("DEBUG IMAP: description " + this.description);
        }
        this.encoding = fetchResponse.readAtomString();
        if (this.encoding != null && this.encoding.equalsIgnoreCase("NIL")) {
            this.encoding = null;
        }
        if (parseDebug) {
            System.out.println("DEBUG IMAP: encoding " + this.encoding);
        }
        this.size = fetchResponse.readNumber();
        if (parseDebug) {
            System.out.println("DEBUG IMAP: size " + this.size);
        }
        if (this.size < 0) {
            throw new ParsingException("BODYSTRUCTURE parse error: bad ``size'' element");
        }
        if (this.type.equalsIgnoreCase("text")) {
            this.lines = fetchResponse.readNumber();
            if (parseDebug) {
                System.out.println("DEBUG IMAP: lines " + this.lines);
            }
            if (this.lines < 0) {
                throw new ParsingException("BODYSTRUCTURE parse error: bad ``lines'' element");
            }
        } else if (this.type.equalsIgnoreCase("message") && this.subtype.equalsIgnoreCase("rfc822")) {
            this.processedType = NESTED;
            fetchResponse.skipSpaces();
            if (fetchResponse.peekByte() == 40) {
                this.envelope = new ENVELOPE(fetchResponse);
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: got envelope of nested message");
                }
                this.bodies = new BODYSTRUCTURE[]{new BODYSTRUCTURE(fetchResponse)};
                this.lines = fetchResponse.readNumber();
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: lines " + this.lines);
                }
                if (this.lines < 0) {
                    throw new ParsingException("BODYSTRUCTURE parse error: bad ``lines'' element");
                }
            } else if (parseDebug) {
                System.out.println("DEBUG IMAP: missing envelope and body of nested message");
            }
        } else {
            fetchResponse.skipSpaces();
            if (Character.isDigit((char) fetchResponse.peekByte())) {
                throw new ParsingException("BODYSTRUCTURE parse error: server erroneously included ``lines'' element with type " + this.type + FileUriModel.SCHEME + this.subtype);
            }
        }
        if (fetchResponse.isNextNonSpace(')')) {
            if (parseDebug) {
                System.out.println("DEBUG IMAP: parse DONE");
                return;
            }
            return;
        }
        this.md5 = fetchResponse.readString();
        if (fetchResponse.isNextNonSpace(')')) {
            if (parseDebug) {
                System.out.println("DEBUG IMAP: no MD5 DONE");
                return;
            }
            return;
        }
        byte readByte2 = fetchResponse.readByte();
        if (readByte2 == 40) {
            this.disposition = fetchResponse.readString();
            if (parseDebug) {
                System.out.println("DEBUG IMAP: disposition " + this.disposition);
            }
            this.dParams = parseParameters(fetchResponse);
            if (parseDebug) {
                System.out.println("DEBUG IMAP: dParams " + this.dParams);
            }
            if (!fetchResponse.isNextNonSpace(')')) {
                throw new ParsingException("BODYSTRUCTURE parse error: missing ``)'' at end of disposition");
            }
        } else {
            if (readByte2 != 78 && readByte2 != 110) {
                throw new ParsingException("BODYSTRUCTURE parse error: " + this.type + FileUriModel.SCHEME + this.subtype + ": bad single part disposition, b " + ((int) readByte2));
            }
            if (parseDebug) {
                System.out.println("DEBUG IMAP: disposition NIL");
            }
            fetchResponse.skip(2);
        }
        if (fetchResponse.isNextNonSpace(')')) {
            if (parseDebug) {
                System.out.println("DEBUG IMAP: disposition DONE");
                return;
            }
            return;
        }
        if (fetchResponse.peekByte() == 40) {
            this.language = fetchResponse.readStringList();
            if (parseDebug) {
                System.out.println("DEBUG IMAP: language len " + this.language.length);
            }
        } else {
            String readString2 = fetchResponse.readString();
            if (readString2 != null) {
                this.language = new String[]{readString2};
                if (parseDebug) {
                    System.out.println("DEBUG IMAP: language " + readString2);
                }
            }
        }
        while (fetchResponse.readByte() == 32) {
            parseBodyExtension(fetchResponse);
        }
        if (parseDebug) {
            System.out.println("DEBUG IMAP: all DONE");
        }
    }

    private void parseBodyExtension(Response response) throws ParsingException {
        response.skipSpaces();
        byte peekByte = response.peekByte();
        if (peekByte == 40) {
            response.skip(1);
            do {
                parseBodyExtension(response);
            } while (!response.isNextNonSpace(')'));
        } else if (Character.isDigit((char) peekByte)) {
            response.readNumber();
        } else {
            response.readString();
        }
    }

    private ParameterList parseParameters(Response response) throws ParsingException {
        response.skipSpaces();
        byte readByte = response.readByte();
        if (readByte != 40) {
            if (readByte != 78 && readByte != 110) {
                throw new ParsingException("Parameter list parse error");
            }
            if (parseDebug) {
                System.out.println("DEBUG IMAP: parameter list NIL");
            }
            response.skip(2);
            return null;
        }
        ParameterList parameterList = new ParameterList();
        do {
            String readString = response.readString();
            if (parseDebug) {
                System.out.println("DEBUG IMAP: parameter name " + readString);
            }
            if (readString == null) {
                throw new ParsingException("BODYSTRUCTURE parse error: " + this.type + FileUriModel.SCHEME + this.subtype + ": null name in parameter list");
            }
            String readString2 = response.readString();
            if (parseDebug) {
                System.out.println("DEBUG IMAP: parameter value " + readString2);
            }
            if (readString2 == null) {
                readString2 = "";
            }
            parameterList.set(readString, readString2);
        } while (!response.isNextNonSpace(')'));
        parameterList.combineSegments();
        return parameterList;
    }

    public boolean isMulti() {
        return this.processedType == MULTI;
    }

    public boolean isNested() {
        return this.processedType == NESTED;
    }

    public boolean isSingle() {
        return this.processedType == SINGLE;
    }
}
