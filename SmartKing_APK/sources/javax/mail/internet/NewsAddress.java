package javax.mail.internet;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.mail.Address;

/* loaded from: classes2.dex */
public class NewsAddress extends Address {
    private static final long serialVersionUID = -4203797299824684143L;
    protected String host;
    protected String newsgroup;

    public NewsAddress() {
    }

    public NewsAddress(String str) {
        this(str, null);
    }

    public NewsAddress(String str, String str2) {
        this.newsgroup = str.replaceAll("\\s+", "");
        this.host = str2;
    }

    public static NewsAddress[] parse(String str) throws AddressException {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        ArrayList arrayList = new ArrayList();
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(new NewsAddress(stringTokenizer.nextToken()));
        }
        return (NewsAddress[]) arrayList.toArray(new NewsAddress[arrayList.size()]);
    }

    public static String toString(Address[] addressArr) {
        if (addressArr == null || addressArr.length == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(((NewsAddress) addressArr[0]).toString());
        int length = stringBuffer.length();
        for (int i = 1; i < addressArr.length; i++) {
            stringBuffer.append(",");
            int i2 = length + 1;
            String newsAddress = ((NewsAddress) addressArr[i]).toString();
            if (newsAddress.length() + i2 > 76) {
                stringBuffer.append("\r\n\t");
                i2 = 8;
            }
            stringBuffer.append(newsAddress);
            length = i2 + newsAddress.length();
        }
        return stringBuffer.toString();
    }

    @Override // javax.mail.Address
    public boolean equals(Object obj) {
        if (!(obj instanceof NewsAddress)) {
            return false;
        }
        NewsAddress newsAddress = (NewsAddress) obj;
        if (!(this.newsgroup == null && newsAddress.newsgroup == null) && (this.newsgroup == null || !this.newsgroup.equals(newsAddress.newsgroup))) {
            return false;
        }
        return (this.host == null && newsAddress.host == null) || !(this.host == null || newsAddress.host == null || !this.host.equalsIgnoreCase(newsAddress.host));
    }

    public String getHost() {
        return this.host;
    }

    public String getNewsgroup() {
        return this.newsgroup;
    }

    @Override // javax.mail.Address
    public String getType() {
        return "news";
    }

    public int hashCode() {
        int hashCode = this.newsgroup != null ? 0 + this.newsgroup.hashCode() : 0;
        return this.host != null ? hashCode + this.host.toLowerCase(Locale.ENGLISH).hashCode() : hashCode;
    }

    public void setHost(String str) {
        this.host = str;
    }

    public void setNewsgroup(String str) {
        this.newsgroup = str;
    }

    @Override // javax.mail.Address
    public String toString() {
        return this.newsgroup;
    }
}
