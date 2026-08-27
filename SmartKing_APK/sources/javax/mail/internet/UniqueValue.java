package javax.mail.internet;

import java.util.concurrent.atomic.AtomicInteger;
import javax.mail.Session;
import org.apache.commons.lang.ClassUtils;

/* loaded from: classes2.dex */
class UniqueValue {
    private static AtomicInteger id = new AtomicInteger();

    UniqueValue() {
    }

    public static String getUniqueBoundaryValue() {
        StringBuffer stringBuffer = new StringBuffer();
        long hashCode = stringBuffer.hashCode();
        stringBuffer.append("----=_Part_");
        stringBuffer.append(id.getAndIncrement());
        stringBuffer.append("_");
        stringBuffer.append(hashCode);
        stringBuffer.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        stringBuffer.append(System.currentTimeMillis());
        return stringBuffer.toString();
    }

    public static String getUniqueMessageIDValue(Session session) {
        InternetAddress localAddress = InternetAddress.getLocalAddress(session);
        String address = localAddress != null ? localAddress.getAddress() : "javamailuser@localhost";
        int lastIndexOf = address.lastIndexOf(64);
        if (lastIndexOf >= 0) {
            address = address.substring(lastIndexOf);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(stringBuffer.hashCode());
        stringBuffer.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        stringBuffer.append(id.getAndIncrement());
        stringBuffer.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        stringBuffer.append(System.currentTimeMillis());
        stringBuffer.append(address);
        return stringBuffer.toString();
    }
}
