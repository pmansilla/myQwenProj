package javax.mail.internet;

import javax.mail.internet.HeaderTokenizer;

/* loaded from: classes2.dex */
public class ContentDisposition {
    private String disposition;
    private ParameterList list;

    public ContentDisposition() {
    }

    public ContentDisposition(String str) throws ParseException {
        HeaderTokenizer headerTokenizer = new HeaderTokenizer(str, HeaderTokenizer.MIME);
        HeaderTokenizer.Token next = headerTokenizer.next();
        if (next.getType() != -1) {
            throw new ParseException("Expected disposition, got " + next.getValue());
        }
        this.disposition = next.getValue();
        String remainder = headerTokenizer.getRemainder();
        if (remainder != null) {
            this.list = new ParameterList(remainder);
        }
    }

    public ContentDisposition(String str, ParameterList parameterList) {
        this.disposition = str;
        this.list = parameterList;
    }

    public String getDisposition() {
        return this.disposition;
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

    public void setDisposition(String str) {
        this.disposition = str;
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

    public String toString() {
        if (this.disposition == null) {
            return "";
        }
        if (this.list == null) {
            return this.disposition;
        }
        StringBuffer stringBuffer = new StringBuffer(this.disposition);
        stringBuffer.append(this.list.toString(stringBuffer.length() + 21));
        return stringBuffer.toString();
    }
}
