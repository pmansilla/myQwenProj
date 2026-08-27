package javax.mail.internet;

import javax.mail.internet.HeaderTokenizer;

/* loaded from: classes2.dex */
public class ContentType {
    private ParameterList list;
    private String primaryType;
    private String subType;

    public ContentType() {
    }

    public ContentType(String str) throws ParseException {
        HeaderTokenizer headerTokenizer = new HeaderTokenizer(str, HeaderTokenizer.MIME);
        HeaderTokenizer.Token next = headerTokenizer.next();
        if (next.getType() != -1) {
            throw new ParseException("In Content-Type string <" + str + ">, expected MIME type, got " + next.getValue());
        }
        this.primaryType = next.getValue();
        HeaderTokenizer.Token next2 = headerTokenizer.next();
        if (((char) next2.getType()) != '/') {
            throw new ParseException("In Content-Type string <" + str + ">, expected '/', got " + next2.getValue());
        }
        HeaderTokenizer.Token next3 = headerTokenizer.next();
        if (next3.getType() == -1) {
            this.subType = next3.getValue();
            String remainder = headerTokenizer.getRemainder();
            if (remainder != null) {
                this.list = new ParameterList(remainder);
                return;
            }
            return;
        }
        throw new ParseException("In Content-Type string <" + str + ">, expected MIME subtype, got " + next3.getValue());
    }

    public ContentType(String str, String str2, ParameterList parameterList) {
        this.primaryType = str;
        this.subType = str2;
        this.list = parameterList;
    }

    public String getBaseType() {
        if (this.primaryType == null || this.subType == null) {
            return "";
        }
        return this.primaryType + '/' + this.subType;
    }

    public String getParameter(String str) {
        if (this.list == null) {
            return null;
        }
        return this.list.get(str);
    }

    public ParameterList getParameterList() {
        return this.list;
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public String getSubType() {
        return this.subType;
    }

    public boolean match(String str) {
        try {
            return match(new ContentType(str));
        } catch (ParseException unused) {
            return false;
        }
    }

    public boolean match(ContentType contentType) {
        if (!(this.primaryType == null && contentType.getPrimaryType() == null) && (this.primaryType == null || !this.primaryType.equalsIgnoreCase(contentType.getPrimaryType()))) {
            return false;
        }
        String subType = contentType.getSubType();
        if ((this.subType == null || !this.subType.startsWith("*")) && (subType == null || !subType.startsWith("*"))) {
            return (this.subType == null && subType == null) || (this.subType != null && this.subType.equalsIgnoreCase(subType));
        }
        return true;
    }

    public void setParameter(String str, String str2) {
        if (this.list == null) {
            this.list = new ParameterList();
        }
        this.list.set(str, str2);
    }

    public void setParameterList(ParameterList parameterList) {
        this.list = parameterList;
    }

    public void setPrimaryType(String str) {
        this.primaryType = str;
    }

    public void setSubType(String str) {
        this.subType = str;
    }

    public String toString() {
        if (this.primaryType == null || this.subType == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.primaryType);
        stringBuffer.append('/');
        stringBuffer.append(this.subType);
        if (this.list != null) {
            stringBuffer.append(this.list.toString(stringBuffer.length() + 14));
        }
        return stringBuffer.toString();
    }
}
