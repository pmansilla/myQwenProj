package javax.mail.internet;

import com.sun.mail.util.PropUtil;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.mail.Address;
import javax.mail.Session;
import kotlin.text.Typography;

/* loaded from: classes2.dex */
public class InternetAddress extends Address implements Cloneable {
    private static final long serialVersionUID = -7507595530758302903L;
    private static final String specialsNoDot = "()<>,;:\\\"[]@";
    private static final String specialsNoDotNoAt = "()<>,;:\\\"[]";
    protected String address;
    protected String encodedPersonal;
    protected String personal;
    private static final boolean ignoreBogusGroupName = PropUtil.getBooleanSystemProperty("mail.mime.address.ignorebogusgroupname", true);
    private static final boolean useCanonicalHostName = PropUtil.getBooleanSystemProperty("mail.mime.address.usecanonicalhostname", true);
    private static final boolean allowUtf8 = PropUtil.getBooleanSystemProperty("mail.mime.allowutf8", false);
    private static final String rfc822phrase = HeaderTokenizer.RFC822.replace(' ', (char) 0).replace('\t', (char) 0);

    public InternetAddress() {
    }

    public InternetAddress(String str) throws AddressException {
        InternetAddress[] parse = parse(str, true);
        if (parse.length != 1) {
            throw new AddressException("Illegal address", str);
        }
        this.address = parse[0].address;
        this.personal = parse[0].personal;
        this.encodedPersonal = parse[0].encodedPersonal;
    }

    public InternetAddress(String str, String str2) throws UnsupportedEncodingException {
        this(str, str2, null);
    }

    public InternetAddress(String str, String str2, String str3) throws UnsupportedEncodingException {
        this.address = str;
        setPersonal(str2, str3);
    }

    public InternetAddress(String str, boolean z) throws AddressException {
        this(str);
        if (z) {
            if (isGroup()) {
                getGroup(true);
            } else {
                checkAddress(this.address, true, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InternetAddress _getLocalAddress(Session session) throws SecurityException, AddressException, UnknownHostException {
        String property;
        String str;
        String str2;
        if (session == null) {
            str2 = System.getProperty("user.name");
            str = getLocalHostName();
            property = null;
        } else {
            property = session.getProperty("mail.from");
            if (property == null) {
                str2 = session.getProperty("mail.user");
                if (str2 == null || str2.length() == 0) {
                    str2 = session.getProperty("user.name");
                }
                if (str2 == null || str2.length() == 0) {
                    str2 = System.getProperty("user.name");
                }
                str = session.getProperty("mail.host");
                if (str == null || str.length() == 0) {
                    str = getLocalHostName();
                }
            } else {
                str = null;
                str2 = null;
            }
        }
        if (property == null && str2 != null && str2.length() != 0 && str != null && str.length() != 0) {
            property = MimeUtility.quote(str2.trim(), "()<>,;:\\\"[]@\t ") + "@" + str;
        }
        if (property == null) {
            return null;
        }
        return new InternetAddress(property);
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x00e6, code lost:
    
        throw new javax.mail.internet.AddressException("Local address contains control or whitespace", r12);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void checkAddress(java.lang.String r12, boolean r13, boolean r14) throws javax.mail.internet.AddressException {
        /*
            Method dump skipped, instructions count: 409
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.InternetAddress.checkAddress(java.lang.String, boolean, boolean):void");
    }

    public static InternetAddress getLocalAddress(Session session) {
        try {
            return _getLocalAddress(session);
        } catch (SecurityException | UnknownHostException | AddressException unused) {
            return null;
        }
    }

    private static String getLocalHostName() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        if (localHost == null) {
            return null;
        }
        String canonicalHostName = useCanonicalHostName ? localHost.getCanonicalHostName() : null;
        if (canonicalHostName == null) {
            canonicalHostName = localHost.getHostName();
        }
        if (canonicalHostName == null) {
            canonicalHostName = localHost.getHostAddress();
        }
        if (canonicalHostName == null || canonicalHostName.length() <= 0 || !isInetAddressLiteral(canonicalHostName)) {
            return canonicalHostName;
        }
        return '[' + canonicalHostName + ']';
    }

    private static int indexOfAny(String str, String str2) {
        return indexOfAny(str, str2, 0);
    }

    private static int indexOfAny(String str, String str2, int i) {
        try {
            int length = str.length();
            while (i < length) {
                if (str2.indexOf(str.charAt(i)) >= 0) {
                    return i;
                }
                i++;
            }
            return -1;
        } catch (StringIndexOutOfBoundsException unused) {
            return -1;
        }
    }

    private static boolean isInetAddressLiteral(String str) {
        boolean z = false;
        boolean z2 = false;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if ((charAt < '0' || charAt > '9') && charAt != '.') {
                if ((charAt >= 'a' && charAt <= 'z') || (charAt >= 'A' && charAt <= 'Z')) {
                    z = true;
                } else {
                    if (charAt != ':') {
                        return false;
                    }
                    z2 = true;
                }
            }
        }
        return !z || z2;
    }

    private boolean isSimple() {
        return this.address == null || indexOfAny(this.address, specialsNoDotNoAt) < 0;
    }

    private static int lengthOfFirstSegment(String str) {
        int indexOf = str.indexOf("\r\n");
        return indexOf != -1 ? indexOf : str.length();
    }

    private static int lengthOfLastSegment(String str, int i) {
        return str.lastIndexOf("\r\n") != -1 ? (str.length() - r0) - 2 : str.length() + i;
    }

    public static InternetAddress[] parse(String str) throws AddressException {
        return parse(str, true);
    }

    public static InternetAddress[] parse(String str, boolean z) throws AddressException {
        return parse(str, z, false);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0027, code lost:
    
        if (r10 == (-1)) goto L258;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x01c0, code lost:
    
        if (r3 == ';') goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x024a, code lost:
    
        if (r4.trim().length() == 0) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x0374, code lost:
    
        if (r0.trim().length() == 0) goto L270;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0052, code lost:
    
        if (r10 == (-1)) goto L207;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0023. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:102:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x00cb A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x02e8  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x02f3  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x0337  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x0343  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0113  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static javax.mail.internet.InternetAddress[] parse(java.lang.String r24, boolean r25, boolean r26) throws javax.mail.internet.AddressException {
        /*
            Method dump skipped, instructions count: 1062
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.InternetAddress.parse(java.lang.String, boolean, boolean):javax.mail.internet.InternetAddress[]");
    }

    public static InternetAddress[] parseHeader(String str, boolean z) throws AddressException {
        return parse(MimeUtility.unfold(str), z, true);
    }

    private static String quotePhrase(String str) {
        int length = str.length();
        boolean z = false;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt == '\"' || charAt == '\\') {
                StringBuffer stringBuffer = new StringBuffer(length + 3);
                stringBuffer.append(Typography.quote);
                for (int i2 = 0; i2 < length; i2++) {
                    char charAt2 = str.charAt(i2);
                    if (charAt2 == '\"' || charAt2 == '\\') {
                        stringBuffer.append('\\');
                    }
                    stringBuffer.append(charAt2);
                }
                stringBuffer.append(Typography.quote);
                return stringBuffer.toString();
            }
            if ((charAt < ' ' && charAt != '\r' && charAt != '\n' && charAt != '\t') || ((charAt >= 127 && !allowUtf8) || rfc822phrase.indexOf(charAt) >= 0)) {
                z = true;
            }
        }
        if (!z) {
            return str;
        }
        StringBuffer stringBuffer2 = new StringBuffer(length + 2);
        stringBuffer2.append(Typography.quote);
        stringBuffer2.append(str);
        stringBuffer2.append(Typography.quote);
        return stringBuffer2.toString();
    }

    public static String toString(Address[] addressArr) {
        return toString(addressArr, 0);
    }

    public static String toString(Address[] addressArr, int i) {
        if (addressArr == null || addressArr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = i;
        for (int i3 = 0; i3 < addressArr.length; i3++) {
            if (i3 != 0) {
                sb.append(", ");
                i2 += 2;
            }
            String fold = MimeUtility.fold(0, addressArr[i3].toString());
            if (lengthOfFirstSegment(fold) + i2 > 76) {
                int length = sb.length();
                if (length > 0) {
                    int i4 = length - 1;
                    if (sb.charAt(i4) == ' ') {
                        sb.setLength(i4);
                    }
                }
                sb.append("\r\n\t");
                i2 = 8;
            }
            sb.append(fold);
            i2 = lengthOfLastSegment(fold, i2);
        }
        return sb.toString();
    }

    public static String toUnicodeString(Address[] addressArr) {
        return toUnicodeString(addressArr, 0);
    }

    public static String toUnicodeString(Address[] addressArr, int i) {
        String str;
        if (addressArr == null || addressArr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = i;
        int i3 = 0;
        boolean z = false;
        while (i3 < addressArr.length) {
            if (i3 != 0) {
                sb.append(", ");
                i2 += 2;
            }
            String unicodeString = ((InternetAddress) addressArr[i3]).toUnicodeString();
            boolean z2 = true;
            if (MimeUtility.checkAscii(unicodeString) != 1) {
                str = new String(unicodeString.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            } else {
                z2 = z;
                str = unicodeString;
            }
            String fold = MimeUtility.fold(0, str);
            if (lengthOfFirstSegment(fold) + i2 > 76) {
                int length = sb.length();
                if (length > 0) {
                    int i4 = length - 1;
                    if (sb.charAt(i4) == ' ') {
                        sb.setLength(i4);
                    }
                }
                sb.append("\r\n\t");
                i2 = 8;
            }
            sb.append(fold);
            i2 = lengthOfLastSegment(fold, i2);
            i3++;
            z = z2;
        }
        String sb2 = sb.toString();
        return z ? new String(sb2.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8) : sb2;
    }

    private static String unquote(String str) {
        if (!str.startsWith("\"") || !str.endsWith("\"") || str.length() <= 1) {
            return str;
        }
        String substring = str.substring(1, str.length() - 1);
        if (substring.indexOf(92) < 0) {
            return substring;
        }
        StringBuffer stringBuffer = new StringBuffer(substring.length());
        int i = 0;
        while (i < substring.length()) {
            char charAt = substring.charAt(i);
            if (charAt == '\\' && i < substring.length() - 1) {
                i++;
                charAt = substring.charAt(i);
            }
            stringBuffer.append(charAt);
            i++;
        }
        return stringBuffer.toString();
    }

    public Object clone() {
        try {
            return (InternetAddress) super.clone();
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }

    @Override // javax.mail.Address
    public boolean equals(Object obj) {
        if (!(obj instanceof InternetAddress)) {
            return false;
        }
        String address = ((InternetAddress) obj).getAddress();
        if (address == this.address) {
            return true;
        }
        return this.address != null && this.address.equalsIgnoreCase(address);
    }

    public String getAddress() {
        return this.address;
    }

    public InternetAddress[] getGroup(boolean z) throws AddressException {
        int indexOf;
        String address = getAddress();
        if (address != null && address.endsWith(";") && (indexOf = address.indexOf(58)) >= 0) {
            return parseHeader(address.substring(indexOf + 1, address.length() - 1), z);
        }
        return null;
    }

    public String getPersonal() {
        if (this.personal != null) {
            return this.personal;
        }
        if (this.encodedPersonal == null) {
            return null;
        }
        try {
            this.personal = MimeUtility.decodeText(this.encodedPersonal);
            return this.personal;
        } catch (Exception unused) {
            return this.encodedPersonal;
        }
    }

    @Override // javax.mail.Address
    public String getType() {
        return "rfc822";
    }

    public int hashCode() {
        if (this.address == null) {
            return 0;
        }
        return this.address.toLowerCase(Locale.ENGLISH).hashCode();
    }

    public boolean isGroup() {
        return this.address != null && this.address.endsWith(";") && this.address.indexOf(58) > 0;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public void setPersonal(String str) throws UnsupportedEncodingException {
        this.personal = str;
        if (str != null) {
            this.encodedPersonal = MimeUtility.encodeWord(str);
        } else {
            this.encodedPersonal = null;
        }
    }

    public void setPersonal(String str, String str2) throws UnsupportedEncodingException {
        this.personal = str;
        if (str != null) {
            this.encodedPersonal = MimeUtility.encodeWord(str, str2, null);
        } else {
            this.encodedPersonal = null;
        }
    }

    @Override // javax.mail.Address
    public String toString() {
        String str = this.address == null ? "" : this.address;
        if (this.encodedPersonal == null && this.personal != null) {
            try {
                this.encodedPersonal = MimeUtility.encodeWord(this.personal);
            } catch (UnsupportedEncodingException unused) {
            }
        }
        if (this.encodedPersonal != null) {
            return quotePhrase(this.encodedPersonal) + " <" + str + ">";
        }
        if (isGroup() || isSimple()) {
            return str;
        }
        return "<" + str + ">";
    }

    public String toUnicodeString() {
        String personal = getPersonal();
        if (personal != null) {
            return quotePhrase(personal) + " <" + this.address + ">";
        }
        if (isGroup() || isSimple()) {
            return this.address;
        }
        return "<" + this.address + ">";
    }

    public void validate() throws AddressException {
        if (isGroup()) {
            getGroup(true);
        } else {
            checkAddress(getAddress(), true, true);
        }
    }
}
