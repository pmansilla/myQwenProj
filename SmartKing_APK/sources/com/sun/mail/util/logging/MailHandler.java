package com.sun.mail.util.logging;

import android.support.v4.app.NotificationCompat;
import com.amap.location.common.model.AmapLoc;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import javax.activation.DataHandler;
import javax.activation.FileTypeMap;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessageContext;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Service;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import no.nordicsemi.android.dfu.DfuBaseService;
import org.apache.commons.lang.ClassUtils;

/* loaded from: classes2.dex */
public class MailHandler extends Handler {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int MIN_HEADER_SIZE = 1024;
    private volatile Filter[] attachmentFilters;
    private Formatter[] attachmentFormatters;
    private Formatter[] attachmentNames;
    private Authenticator auth;
    private int capacity;
    private Comparator<? super LogRecord> comparator;
    private FileTypeMap contentTypes;
    private LogRecord[] data;
    private String encoding;
    private volatile Filter filter;
    private Formatter formatter;
    private boolean isWriting;
    private Properties mailProps;
    private int[] matched;
    private Filter pushFilter;
    private Level pushLevel;
    private volatile boolean sealed;
    private Session session;
    private int size;
    private Formatter subjectFormatter;
    private static final Filter[] EMPTY_FILTERS = new Filter[0];
    private static final Formatter[] EMPTY_FORMATTERS = new Formatter[0];
    private static final int offValue = Level.OFF.intValue();
    private static final PrivilegedAction<Object> MAILHANDLER_LOADER = new GetAndSetContext(MailHandler.class);
    private static final ThreadLocal<Integer> MUTEX = new ThreadLocal<>();
    private static final Integer MUTEX_PUBLISH = -2;
    private static final Integer MUTEX_REPORT = -4;
    private static final Integer MUTEX_LINKAGE = -8;
    private volatile Level logLevel = Level.ALL;
    private volatile ErrorManager errorManager = defaultErrorManager();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class DefaultAuthenticator extends Authenticator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final String pass;

        private DefaultAuthenticator(String str) {
            this.pass = str;
        }

        static Authenticator of(String str) {
            return new DefaultAuthenticator(str);
        }

        @Override // javax.mail.Authenticator
        protected final PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(getDefaultUserName(), this.pass);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class GetAndSetContext implements PrivilegedAction<Object> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public static final Object NOT_MODIFIED = GetAndSetContext.class;
        private final Object source;

        GetAndSetContext(Object obj) {
            this.source = obj;
        }

        @Override // java.security.PrivilegedAction
        public final Object run() {
            Thread currentThread = Thread.currentThread();
            ClassLoader contextClassLoader = currentThread.getContextClassLoader();
            ClassLoader classLoader = this.source == null ? null : this.source instanceof ClassLoader ? (ClassLoader) this.source : this.source instanceof Class ? ((Class) this.source).getClassLoader() : this.source instanceof Thread ? ((Thread) this.source).getContextClassLoader() : this.source.getClass().getClassLoader();
            if (contextClassLoader == classLoader) {
                return NOT_MODIFIED;
            }
            currentThread.setContextClassLoader(classLoader);
            return contextClassLoader;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class TailNameFormatter extends Formatter {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final String name;

        private TailNameFormatter(String str) {
            this.name = str;
        }

        static Formatter of(String str) {
            return new TailNameFormatter(str);
        }

        public final boolean equals(Object obj) {
            if (obj instanceof TailNameFormatter) {
                return this.name.equals(((TailNameFormatter) obj).name);
            }
            return false;
        }

        @Override // java.util.logging.Formatter
        public final String format(LogRecord logRecord) {
            return "";
        }

        @Override // java.util.logging.Formatter
        public final String getTail(Handler handler) {
            return this.name;
        }

        public final int hashCode() {
            return getClass().hashCode() + this.name.hashCode();
        }

        public final String toString() {
            return this.name;
        }
    }

    public MailHandler() {
        init((Properties) null);
        this.sealed = true;
        checkAccess();
    }

    public MailHandler(int i) {
        init((Properties) null);
        this.sealed = true;
        setCapacity0(i);
    }

    public MailHandler(Properties properties) {
        if (properties == null) {
            throw new NullPointerException();
        }
        init(properties);
        this.sealed = true;
        setMailProperties0(properties);
    }

    private boolean alignAttachmentFilters() {
        int length = this.attachmentFormatters.length;
        int length2 = this.attachmentFilters.length;
        if (length2 != length) {
            this.attachmentFilters = (Filter[]) Arrays.copyOf(this.attachmentFilters, length, Filter[].class);
            clearMatches(length2);
            r2 = length2 != 0;
            Filter filter = this.filter;
            if (filter != null) {
                while (length2 < length) {
                    this.attachmentFilters[length2] = filter;
                    length2++;
                }
            }
        }
        if (length == 0) {
            this.attachmentFilters = emptyFilterArray();
        }
        return r2;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0023 A[ADDED_TO_REGION, LOOP:0: B:10:0x0023->B:15:0x003b, LOOP_START, PHI: r2
      0x0023: PHI (r2v1 int) = (r2v0 int), (r2v2 int) binds: [B:6:0x001a, B:15:0x003b] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x001c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean alignAttachmentNames() {
        /*
            r5 = this;
            java.util.logging.Formatter[] r0 = r5.attachmentFormatters
            int r0 = r0.length
            java.util.logging.Formatter[] r1 = r5.attachmentNames
            int r1 = r1.length
            r2 = 0
            if (r1 == r0) goto L19
            java.util.logging.Formatter[] r3 = r5.attachmentNames
            java.lang.Class<java.util.logging.Formatter[]> r4 = java.util.logging.Formatter[].class
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r3, r0, r4)
            java.util.logging.Formatter[] r3 = (java.util.logging.Formatter[]) r3
            r5.attachmentNames = r3
            if (r1 == 0) goto L19
            r1 = 1
            goto L1a
        L19:
            r1 = 0
        L1a:
            if (r0 != 0) goto L23
            java.util.logging.Formatter[] r0 = emptyFormatterArray()
            r5.attachmentNames = r0
            goto L3e
        L23:
            if (r2 >= r0) goto L3e
            java.util.logging.Formatter[] r3 = r5.attachmentNames
            r3 = r3[r2]
            if (r3 != 0) goto L3b
            java.util.logging.Formatter[] r3 = r5.attachmentNames
            java.util.logging.Formatter[] r4 = r5.attachmentFormatters
            r4 = r4[r2]
            java.lang.String r4 = r5.toString(r4)
            java.util.logging.Formatter r4 = com.sun.mail.util.logging.MailHandler.TailNameFormatter.of(r4)
            r3[r2] = r4
        L3b:
            int r2 = r2 + 1
            goto L23
        L3e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.logging.MailHandler.alignAttachmentNames():boolean");
    }

    private boolean allowRestrictedHeaders() {
        return LogManagerProperties.hasLogManager();
    }

    private void appendContentLang(MimePart mimePart, Locale locale) {
        try {
            String languageTag = LogManagerProperties.toLanguageTag(locale);
            if (languageTag.length() != 0) {
                String header = mimePart.getHeader("Content-Language", null);
                if (isEmpty(header)) {
                    mimePart.setHeader("Content-Language", languageTag);
                    return;
                }
                if (header.equalsIgnoreCase(languageTag)) {
                    return;
                }
                String concat = ",".concat(languageTag);
                int i = 0;
                do {
                    i = header.indexOf(concat, i);
                    if (i <= -1 || (i = i + concat.length()) == header.length()) {
                        break;
                    }
                } while (header.charAt(i) != ',');
                if (i < 0) {
                    int lastIndexOf = header.lastIndexOf("\r\n\t");
                    mimePart.setHeader("Content-Language", (lastIndexOf < 0 ? header.length() + 20 : (header.length() - lastIndexOf) + 8) + concat.length() > 76 ? header.concat("\r\n\t".concat(concat)) : header.concat(concat));
                }
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void appendFileName(Part part, String str) {
        if (str == null) {
            reportNullError(5);
        } else if (str.length() > 0) {
            appendFileName0(part, str);
        }
    }

    private void appendFileName0(Part part, String str) {
        try {
            String replaceAll = str.replaceAll("[\\x00-\\x1F\\x7F]+", "");
            String fileName = part.getFileName();
            if (fileName != null) {
                replaceAll = fileName.concat(replaceAll);
            }
            part.setFileName(replaceAll);
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void appendSubject(Message message, String str) {
        if (str == null) {
            reportNullError(5);
        } else if (str.length() > 0) {
            appendSubject0(message, str);
        }
    }

    private void appendSubject0(Message message, String str) {
        try {
            String replaceAll = str.replaceAll("[\\x00-\\x1F\\x7F]+", "");
            String encodingName = getEncodingName();
            String subject = message.getSubject();
            MimeMessage mimeMessage = (MimeMessage) message;
            if (subject != null) {
                replaceAll = subject.concat(replaceAll);
            }
            mimeMessage.setSubject(replaceAll, MimeUtility.mimeCharset(encodingName));
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private static String atIndexMsg(int i) {
        return "At index: " + i + ClassUtils.PACKAGE_SEPARATOR_CHAR;
    }

    private static MessagingException attach(MessagingException messagingException, Exception exc) {
        if (exc != null && !messagingException.setNextException(exc)) {
            if (exc instanceof MessagingException) {
                MessagingException messagingException2 = (MessagingException) exc;
                if (messagingException2.setNextException(messagingException)) {
                    return messagingException2;
                }
            }
            if (exc != messagingException) {
                messagingException.addSuppressed(exc);
            }
        }
        return messagingException;
    }

    private static RuntimeException attachmentMismatch(int i, int i2) {
        return attachmentMismatch("Attachments mismatched, expected " + i + " but given " + i2 + ClassUtils.PACKAGE_SEPARATOR_CHAR);
    }

    private static RuntimeException attachmentMismatch(String str) {
        return new IndexOutOfBoundsException(str);
    }

    private void checkAccess() {
        if (this.sealed) {
            LogManagerProperties.checkLogManagerAccess();
        }
    }

    private void clearMatches(int i) {
        for (int i2 = 0; i2 < this.size; i2++) {
            if (this.matched[i2] >= i) {
                this.matched[i2] = MUTEX_PUBLISH.intValue();
            }
        }
    }

    private String contentWithEncoding(String str, String str2) {
        try {
            ContentType contentType = new ContentType(str);
            contentType.setParameter("charset", MimeUtility.mimeCharset(str2));
            String contentType2 = contentType.toString();
            return !isEmpty(contentType2) ? contentType2 : str;
        } catch (MessagingException e) {
            reportError(str, e, 5);
            return str;
        }
    }

    private MimeBodyPart createBodyPart() throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setDisposition(Part.INLINE);
        mimeBodyPart.setDescription(descriptionFrom(getFormatter(), getFilter(), this.subjectFormatter));
        setAcceptLang(mimeBodyPart);
        return mimeBodyPart;
    }

    private MimeBodyPart createBodyPart(int i) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setDisposition(Part.ATTACHMENT);
        mimeBodyPart.setDescription(descriptionFrom(this.attachmentFormatters[i], this.attachmentFilters[i], this.attachmentNames[i]));
        setAcceptLang(mimeBodyPart);
        return mimeBodyPart;
    }

    private static Formatter createSimpleFormatter() {
        return (Formatter) Formatter.class.cast(new SimpleFormatter());
    }

    private ErrorManager defaultErrorManager() {
        ErrorManager errorManager;
        try {
            errorManager = super.getErrorManager();
        } catch (LinkageError | RuntimeException unused) {
            errorManager = null;
        }
        return errorManager == null ? new ErrorManager() : errorManager;
    }

    private String descriptionFrom(Comparator<?> comparator, Level level, Filter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sorted using ");
        sb.append(comparator == null ? "no comparator" : comparator.getClass().getName());
        sb.append(", pushed when ");
        sb.append(level.getName());
        sb.append(", and ");
        sb.append(filter == null ? "no push filter" : filter.getClass().getName());
        sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        return sb.toString();
    }

    private String descriptionFrom(Formatter formatter, Filter filter, Formatter formatter2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Formatted using ");
        sb.append(getClassId(formatter));
        sb.append(", filtered with ");
        sb.append(filter == null ? "no filter" : filter.getClass().getName());
        sb.append(", and named by ");
        sb.append(getClassId(formatter2));
        sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        return sb.toString();
    }

    private static Filter[] emptyFilterArray() {
        return EMPTY_FILTERS;
    }

    private static Formatter[] emptyFormatterArray() {
        return EMPTY_FORMATTERS;
    }

    private void envelopeFor(Message message, boolean z) {
        setAcceptLang(message);
        setFrom(message);
        if (!setRecipient(message, "mail.to", Message.RecipientType.TO)) {
            setDefaultRecipient(message, Message.RecipientType.TO);
        }
        setRecipient(message, "mail.cc", Message.RecipientType.CC);
        setRecipient(message, "mail.bcc", Message.RecipientType.BCC);
        setReplyTo(message);
        setSender(message);
        setMailer(message);
        setAutoSubmitted(message);
        if (z) {
            setPriority(message);
        }
        try {
            message.setSentDate(new Date());
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private String format(Formatter formatter, LogRecord logRecord) {
        try {
            return formatter.format(logRecord);
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, 5);
            return "";
        }
    }

    private Object getAndSetContextClassLoader(Object obj) {
        if (obj != GetAndSetContext.NOT_MODIFIED) {
            try {
                return AccessController.doPrivileged(obj instanceof PrivilegedAction ? (PrivilegedAction) obj : new GetAndSetContext(obj));
            } catch (SecurityException unused) {
            }
        }
        return GetAndSetContext.NOT_MODIFIED;
    }

    private String getClassId(Formatter formatter) {
        return formatter instanceof TailNameFormatter ? String.class.getName() : formatter.getClass().getName();
    }

    private String getContentType(String str) {
        String contentType = this.contentTypes.getContentType(str);
        if (DfuBaseService.MIME_TYPE_OCTET_STREAM.equalsIgnoreCase(contentType)) {
            return null;
        }
        return contentType;
    }

    private String getEncodingName() {
        String encoding = getEncoding();
        return encoding == null ? MimeUtility.getDefaultJavaCharset() : encoding;
    }

    private String getLocalHost(Service service) {
        try {
            return LogManagerProperties.getLocalHost(service);
        } catch (Exception e) {
            reportError(service.toString(), e, 4);
            return null;
        } catch (LinkageError | NoSuchMethodException | SecurityException unused) {
            return null;
        }
    }

    private int getMatchedPart() {
        Integer num = MUTEX.get();
        if (num == null || num.intValue() >= readOnlyAttachmentFilters().length) {
            num = MUTEX_PUBLISH;
        }
        return num.intValue();
    }

    private Session getSession(Message message) {
        if (message != null) {
            return new MessageContext(message).getSession();
        }
        throw new NullPointerException();
    }

    private void grow() {
        int length = this.data.length;
        int i = (length >> 1) + length + 1;
        if (i > this.capacity || i < length) {
            i = this.capacity;
        }
        this.data = (LogRecord[]) Arrays.copyOf(this.data, i, LogRecord[].class);
        this.matched = Arrays.copyOf(this.matched, i);
    }

    private static boolean hasValue(String str) {
        return (isEmpty(str) || "null".equalsIgnoreCase(str)) ? false : true;
    }

    private String head(Formatter formatter) {
        try {
            return formatter.getHead(this);
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, 5);
            return "";
        }
    }

    private synchronized void init(Properties properties) {
        String name = getClass().getName();
        this.mailProps = new Properties();
        Object andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
        try {
            this.contentTypes = FileTypeMap.getDefaultFileTypeMap();
            getAndSetContextClassLoader(andSetContextClassLoader);
            initErrorManager(name);
            initLevel(name);
            initFilter(name);
            initCapacity(name);
            initAuthenticator(name);
            initEncoding(name);
            initFormatter(name);
            initComparator(name);
            initPushLevel(name);
            initPushFilter(name);
            initSubject(name);
            initAttachmentFormaters(name);
            initAttachmentFilters(name);
            initAttachmentNames(name);
            if (properties == null && LogManagerProperties.fromLogManager(name.concat(".verify")) != null) {
                verifySettings(initSession());
            }
            intern();
        } catch (Throwable th) {
            getAndSetContextClassLoader(andSetContextClassLoader);
            throw th;
        }
    }

    private void initAttachmentFilters(String str) {
        String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".attachment.filters"));
        if (isEmpty(fromLogManager)) {
            this.attachmentFilters = emptyFilterArray();
            alignAttachmentFilters();
            return;
        }
        String[] split = fromLogManager.split(",");
        Filter[] filterArr = new Filter[split.length];
        for (int i = 0; i < filterArr.length; i++) {
            split[i] = split[i].trim();
            if (!"null".equalsIgnoreCase(split[i])) {
                try {
                    filterArr[i] = LogManagerProperties.newFilter(split[i]);
                } catch (SecurityException e) {
                    throw e;
                } catch (Exception e2) {
                    reportError(e2.getMessage(), e2, 4);
                }
            }
        }
        this.attachmentFilters = filterArr;
        if (alignAttachmentFilters()) {
            reportError("Attachment filters.", attachmentMismatch("Length mismatch."), 4);
        }
    }

    private void initAttachmentFormaters(String str) {
        String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".attachment.formatters"));
        if (isEmpty(fromLogManager)) {
            this.attachmentFormatters = emptyFormatterArray();
            return;
        }
        String[] split = fromLogManager.split(",");
        Formatter[] emptyFormatterArray = split.length == 0 ? emptyFormatterArray() : new Formatter[split.length];
        for (int i = 0; i < emptyFormatterArray.length; i++) {
            split[i] = split[i].trim();
            if ("null".equalsIgnoreCase(split[i])) {
                reportError("Attachment formatter.", new NullPointerException(atIndexMsg(i)), 4);
                emptyFormatterArray[i] = createSimpleFormatter();
            } else {
                try {
                    emptyFormatterArray[i] = LogManagerProperties.newFormatter(split[i]);
                    if (emptyFormatterArray[i] instanceof TailNameFormatter) {
                        reportError("Attachment formatter.", new ClassNotFoundException(emptyFormatterArray[i].toString()), 4);
                        emptyFormatterArray[i] = createSimpleFormatter();
                    }
                } catch (SecurityException e) {
                    throw e;
                } catch (Exception e2) {
                    reportError(e2.getMessage(), e2, 4);
                    emptyFormatterArray[i] = createSimpleFormatter();
                }
            }
        }
        this.attachmentFormatters = emptyFormatterArray;
    }

    private void initAttachmentNames(String str) {
        String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".attachment.names"));
        if (isEmpty(fromLogManager)) {
            this.attachmentNames = emptyFormatterArray();
            alignAttachmentNames();
            return;
        }
        String[] split = fromLogManager.split(",");
        Formatter[] formatterArr = new Formatter[split.length];
        for (int i = 0; i < formatterArr.length; i++) {
            split[i] = split[i].trim();
            if ("null".equalsIgnoreCase(split[i])) {
                reportError("Attachment names.", new NullPointerException(atIndexMsg(i)), 4);
            } else {
                try {
                    try {
                        formatterArr[i] = LogManagerProperties.newFormatter(split[i]);
                    } catch (ClassCastException | ClassNotFoundException unused) {
                        formatterArr[i] = TailNameFormatter.of(split[i]);
                    }
                } catch (SecurityException e) {
                    throw e;
                } catch (Exception e2) {
                    reportError(e2.getMessage(), e2, 4);
                }
            }
        }
        this.attachmentNames = formatterArr;
        if (alignAttachmentNames()) {
            reportError("Attachment names.", attachmentMismatch("Length mismatch."), 4);
        }
    }

    private void initAuthenticator(String str) {
        String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".authenticator"));
        if (fromLogManager == null || "null".equalsIgnoreCase(fromLogManager)) {
            return;
        }
        if (fromLogManager.length() == 0) {
            this.auth = DefaultAuthenticator.of(fromLogManager);
            return;
        }
        try {
            this.auth = (Authenticator) LogManagerProperties.newObjectFrom(fromLogManager, Authenticator.class);
        } catch (ClassCastException | ClassNotFoundException unused) {
            this.auth = DefaultAuthenticator.of(fromLogManager);
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initCapacity(String str) {
        try {
            String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".capacity"));
            if (fromLogManager != null) {
                setCapacity0(Integer.parseInt(fromLogManager));
            } else {
                setCapacity0(1000);
            }
        } catch (SecurityException e) {
            throw e;
        } catch (RuntimeException e2) {
            reportError(e2.getMessage(), e2, 4);
        }
        if (this.capacity <= 0) {
            this.capacity = 1000;
        }
        this.data = new LogRecord[1];
        this.matched = new int[this.data.length];
    }

    private void initComparator(String str) {
        try {
            String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".comparator"));
            String fromLogManager2 = LogManagerProperties.fromLogManager(str.concat(".comparator.reverse"));
            if (!hasValue(fromLogManager)) {
                if (!isEmpty(fromLogManager2)) {
                    throw new IllegalArgumentException("No comparator to reverse.");
                }
            } else {
                this.comparator = LogManagerProperties.newComparator(fromLogManager);
                if (Boolean.parseBoolean(fromLogManager2)) {
                    this.comparator = LogManagerProperties.reverseOrder(this.comparator);
                }
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initEncoding(String str) {
        try {
            String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".encoding"));
            if (fromLogManager != null) {
                setEncoding0(fromLogManager);
            }
        } catch (UnsupportedEncodingException | RuntimeException e) {
            reportError(e.getMessage(), e, 4);
        } catch (SecurityException e2) {
            throw e2;
        }
    }

    private void initErrorManager(String str) {
        try {
            String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".errorManager"));
            if (fromLogManager != null) {
                setErrorManager0(LogManagerProperties.newErrorManager(fromLogManager));
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initFilter(String str) {
        try {
            String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".filter"));
            if (hasValue(fromLogManager)) {
                this.filter = LogManagerProperties.newFilter(fromLogManager);
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initFormatter(String str) {
        try {
            String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".formatter"));
            if (hasValue(fromLogManager)) {
                Formatter newFormatter = LogManagerProperties.newFormatter(fromLogManager);
                if (newFormatter instanceof TailNameFormatter) {
                    this.formatter = createSimpleFormatter();
                } else {
                    this.formatter = newFormatter;
                }
            } else {
                this.formatter = createSimpleFormatter();
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
            this.formatter = createSimpleFormatter();
        }
    }

    private void initLevel(String str) {
        try {
            String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".level"));
            if (fromLogManager != null) {
                this.logLevel = Level.parse(fromLogManager);
            } else {
                this.logLevel = Level.WARNING;
            }
        } catch (SecurityException e) {
            throw e;
        } catch (RuntimeException e2) {
            reportError(e2.getMessage(), e2, 4);
            this.logLevel = Level.WARNING;
        }
    }

    private void initPushFilter(String str) {
        try {
            String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".pushFilter"));
            if (hasValue(fromLogManager)) {
                this.pushFilter = LogManagerProperties.newFilter(fromLogManager);
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initPushLevel(String str) {
        try {
            String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".pushLevel"));
            if (fromLogManager != null) {
                this.pushLevel = Level.parse(fromLogManager);
            }
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, 4);
        }
        if (this.pushLevel == null) {
            this.pushLevel = Level.OFF;
        }
    }

    private Session initSession() {
        this.session = Session.getInstance(new LogManagerProperties(this.mailProps, getClass().getName()), this.auth);
        return this.session;
    }

    private void initSubject(String str) {
        String fromLogManager = LogManagerProperties.fromLogManager(str.concat(".subject"));
        if (fromLogManager == null) {
            fromLogManager = "com.sun.mail.util.logging.CollectorFormatter";
        }
        if (!hasValue(fromLogManager)) {
            this.subjectFormatter = TailNameFormatter.of(fromLogManager);
            return;
        }
        try {
            this.subjectFormatter = LogManagerProperties.newFormatter(fromLogManager);
        } catch (ClassCastException | ClassNotFoundException unused) {
            this.subjectFormatter = TailNameFormatter.of(fromLogManager);
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            this.subjectFormatter = TailNameFormatter.of(fromLogManager);
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private Object intern(Map<Object, Object> map, Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Object newInstance = obj.getClass().getName().equals(TailNameFormatter.class.getName()) ? obj : obj.getClass().getConstructor(new Class[0]).newInstance(new Object[0]);
        if (newInstance.getClass() != obj.getClass()) {
            return obj;
        }
        Object obj2 = map.get(newInstance);
        if (obj2 != null) {
            if (obj.getClass() == obj2.getClass()) {
                return obj2;
            }
            reportNonDiscriminating(obj, obj2);
            return obj;
        }
        boolean equals = newInstance.equals(obj);
        boolean equals2 = obj.equals(newInstance);
        if (!equals || !equals2) {
            if (equals == equals2) {
                return obj;
            }
            reportNonSymmetric(obj, newInstance);
            return obj;
        }
        Object put = map.put(obj, obj);
        if (put == null) {
            return obj;
        }
        reportNonDiscriminating(newInstance, put);
        Object remove = map.remove(newInstance);
        if (remove == obj) {
            return obj;
        }
        reportNonDiscriminating(newInstance, remove);
        map.clear();
        return obj;
    }

    private void intern() {
        try {
            Map<Object, Object> hashMap = new HashMap<>();
            try {
                intern(hashMap, this.errorManager);
            } catch (SecurityException e) {
                reportError(e.getMessage(), e, 4);
            }
            try {
                Object obj = this.filter;
                Object intern = intern(hashMap, obj);
                if (intern != obj && (intern instanceof Filter)) {
                    this.filter = (Filter) intern;
                }
                Object obj2 = this.formatter;
                Object intern2 = intern(hashMap, obj2);
                if (intern2 != obj2 && (intern2 instanceof Formatter)) {
                    this.formatter = (Formatter) intern2;
                }
            } catch (SecurityException e2) {
                reportError(e2.getMessage(), e2, 4);
            }
            Object obj3 = this.subjectFormatter;
            Object intern3 = intern(hashMap, obj3);
            if (intern3 != obj3 && (intern3 instanceof Formatter)) {
                this.subjectFormatter = (Formatter) intern3;
            }
            Object obj4 = this.pushFilter;
            Object intern4 = intern(hashMap, obj4);
            if (intern4 != obj4 && (intern4 instanceof Filter)) {
                this.pushFilter = (Filter) intern4;
            }
            for (int i = 0; i < this.attachmentFormatters.length; i++) {
                Object obj5 = this.attachmentFormatters[i];
                Object intern5 = intern(hashMap, obj5);
                if (intern5 != obj5 && (intern5 instanceof Formatter)) {
                    this.attachmentFormatters[i] = (Formatter) intern5;
                }
                Object obj6 = this.attachmentFilters[i];
                Object intern6 = intern(hashMap, obj6);
                if (intern6 != obj6 && (intern6 instanceof Filter)) {
                    this.attachmentFilters[i] = (Filter) intern6;
                }
                Object obj7 = this.attachmentNames[i];
                Object intern7 = intern(hashMap, obj7);
                if (intern7 != obj7 && (intern7 instanceof Formatter)) {
                    this.attachmentNames[i] = (Formatter) intern7;
                }
            }
        } catch (Exception e3) {
            reportError(e3.getMessage(), e3, 4);
        } catch (LinkageError e4) {
            reportError(e4.getMessage(), new InvocationTargetException(e4), 4);
        }
    }

    private boolean isAttachmentLoggable(LogRecord logRecord) {
        Filter[] readOnlyAttachmentFilters = readOnlyAttachmentFilters();
        for (int i = 0; i < readOnlyAttachmentFilters.length; i++) {
            Filter filter = readOnlyAttachmentFilters[i];
            if (filter == null || filter.isLoggable(logRecord)) {
                setMatchedPart(i);
                return true;
            }
        }
        return false;
    }

    private static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    private boolean isPushable(LogRecord logRecord) {
        int intValue = getPushLevel().intValue();
        if (intValue == offValue || logRecord.getLevel().intValue() < intValue) {
            return false;
        }
        Filter pushFilter = getPushFilter();
        if (pushFilter == null) {
            return true;
        }
        int matchedPart = getMatchedPart();
        if (!(matchedPart == -1 && getFilter() == pushFilter) && (matchedPart < 0 || this.attachmentFilters[matchedPart] != pushFilter)) {
            return pushFilter.isLoggable(logRecord);
        }
        return true;
    }

    private Locale localeFor(LogRecord logRecord) {
        ResourceBundle resourceBundle = logRecord.getResourceBundle();
        if (resourceBundle == null) {
            return null;
        }
        Locale locale = resourceBundle.getLocale();
        return (locale == null || isEmpty(locale.getLanguage())) ? Locale.getDefault() : locale;
    }

    private void publish0(LogRecord logRecord) {
        Message message;
        boolean z;
        synchronized (this) {
            if (this.size == this.data.length && this.size < this.capacity) {
                grow();
            }
            message = null;
            if (this.size < this.data.length) {
                this.matched[this.size] = getMatchedPart();
                this.data[this.size] = logRecord;
                this.size++;
                z = isPushable(logRecord);
                if (z || this.size >= this.capacity) {
                    message = writeLogRecords(1);
                }
            } else {
                z = false;
            }
        }
        if (message != null) {
            send(message, z, 1);
        }
    }

    private void push(boolean z, int i) {
        try {
            if (!tryMutex()) {
                reportUnPublishedError(null);
                return;
            }
            try {
                Message writeLogRecords = writeLogRecords(i);
                if (writeLogRecords != null) {
                    send(writeLogRecords, z, i);
                }
            } catch (LinkageError e) {
                reportLinkageError(e, i);
            }
        } finally {
            releaseMutex();
        }
    }

    private Filter[] readOnlyAttachmentFilters() {
        return this.attachmentFilters;
    }

    private void releaseMutex() {
        MUTEX.remove();
    }

    private void reportError(Message message, Exception exc, int i) {
        try {
            try {
                this.errorManager.error(toRawString(message), exc, i);
            } catch (RuntimeException e) {
                reportError(toMsgString(e), exc, i);
            } catch (Exception e2) {
                reportError(toMsgString(e2), exc, i);
            }
        } catch (LinkageError e3) {
            reportLinkageError(e3, i);
        }
    }

    private void reportFilterError(LogRecord logRecord) {
        Formatter createSimpleFormatter = createSimpleFormatter();
        reportError("Log record " + logRecord.getSequenceNumber() + " was filtered from all message parts.  " + head(createSimpleFormatter) + format(createSimpleFormatter, logRecord) + tail(createSimpleFormatter, ""), new IllegalArgumentException(getFilter() + ", " + Arrays.asList(readOnlyAttachmentFilters())), 5);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002e, code lost:
    
        if (r4 != null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0049, code lost:
    
        com.sun.mail.util.logging.MailHandler.MUTEX.remove();
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x004e, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0043, code lost:
    
        com.sun.mail.util.logging.MailHandler.MUTEX.set(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0041, code lost:
    
        if (r4 == null) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void reportLinkageError(java.lang.Throwable r3, int r4) {
        /*
            r2 = this;
            if (r3 == 0) goto L4f
            java.lang.ThreadLocal<java.lang.Integer> r4 = com.sun.mail.util.logging.MailHandler.MUTEX
            java.lang.Object r4 = r4.get()
            java.lang.Integer r4 = (java.lang.Integer) r4
            if (r4 == 0) goto L18
            int r0 = r4.intValue()
            java.lang.Integer r1 = com.sun.mail.util.logging.MailHandler.MUTEX_LINKAGE
            int r1 = r1.intValue()
            if (r0 <= r1) goto L4e
        L18:
            java.lang.ThreadLocal<java.lang.Integer> r0 = com.sun.mail.util.logging.MailHandler.MUTEX
            java.lang.Integer r1 = com.sun.mail.util.logging.MailHandler.MUTEX_LINKAGE
            r0.set(r1)
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L40
            java.lang.Thread$UncaughtExceptionHandler r0 = r0.getUncaughtExceptionHandler()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L40
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L40
            r0.uncaughtException(r1, r3)     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L40
            if (r4 == 0) goto L49
            goto L43
        L31:
            r3 = move-exception
            if (r4 == 0) goto L3a
            java.lang.ThreadLocal<java.lang.Integer> r0 = com.sun.mail.util.logging.MailHandler.MUTEX
            r0.set(r4)
            goto L3f
        L3a:
            java.lang.ThreadLocal<java.lang.Integer> r4 = com.sun.mail.util.logging.MailHandler.MUTEX
            r4.remove()
        L3f:
            throw r3
        L40:
            if (r4 == 0) goto L49
        L43:
            java.lang.ThreadLocal<java.lang.Integer> r3 = com.sun.mail.util.logging.MailHandler.MUTEX
            r3.set(r4)
            goto L4e
        L49:
            java.lang.ThreadLocal<java.lang.Integer> r3 = com.sun.mail.util.logging.MailHandler.MUTEX
            r3.remove()
        L4e:
            return
        L4f:
            java.lang.NullPointerException r3 = new java.lang.NullPointerException
            java.lang.String r4 = java.lang.String.valueOf(r4)
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.logging.MailHandler.reportLinkageError(java.lang.Throwable, int):void");
    }

    private void reportNonDiscriminating(Object obj, Object obj2) {
        reportError("Non discriminating equals implementation.", new IllegalArgumentException(obj.getClass().getName() + " should not be equal to " + obj2.getClass().getName()), 4);
    }

    private void reportNonSymmetric(Object obj, Object obj2) {
        reportError("Non symmetric equals implementation.", new IllegalArgumentException(obj.getClass().getName() + " is not equal to " + obj2.getClass().getName()), 4);
    }

    private void reportNullError(int i) {
        reportError("null", new NullPointerException(), i);
    }

    private void reportUnPublishedError(LogRecord logRecord) {
        String str;
        Integer num = MUTEX.get();
        if (num == null || num.intValue() > MUTEX_REPORT.intValue()) {
            MUTEX.set(MUTEX_REPORT);
            if (logRecord != null) {
                try {
                    Formatter createSimpleFormatter = createSimpleFormatter();
                    str = "Log record " + logRecord.getSequenceNumber() + " was not published. " + head(createSimpleFormatter) + format(createSimpleFormatter, logRecord) + tail(createSimpleFormatter, "");
                } catch (Throwable th) {
                    if (num != null) {
                        MUTEX.set(num);
                    } else {
                        MUTEX.remove();
                    }
                    throw th;
                }
            } else {
                str = null;
            }
            reportError(str, new IllegalStateException("Recursive publish detected by thread " + Thread.currentThread()), 1);
            if (num != null) {
                MUTEX.set(num);
            } else {
                MUTEX.remove();
            }
        }
    }

    private void reportUnexpectedSend(MimeMessage mimeMessage, String str, Exception exc) {
        Exception messagingException = new MessagingException("An empty message was sent.", exc);
        setErrorContent(mimeMessage, str, messagingException);
        reportError(mimeMessage, messagingException, 4);
    }

    private void reset() {
        if (this.size < this.data.length) {
            Arrays.fill(this.data, 0, this.size, (Object) null);
        } else {
            Arrays.fill(this.data, (Object) null);
        }
        this.size = 0;
    }

    private void saveChangesNoContent(Message message, String str) {
        if (message != null) {
            try {
                try {
                    message.saveChanges();
                } catch (NullPointerException e) {
                    try {
                        if (message.getHeader("Content-Transfer-Encoding") != null) {
                            throw e;
                        }
                        message.setHeader("Content-Transfer-Encoding", "base64");
                        message.saveChanges();
                    } catch (RuntimeException | MessagingException e2) {
                        if (e2 != e) {
                            e2.addSuppressed(e);
                        }
                        throw e2;
                    }
                }
            } catch (RuntimeException | MessagingException e3) {
                reportError(str, e3, 5);
            }
        }
    }

    private void send(Message message, boolean z, int i) {
        try {
            envelopeFor(message, z);
            Object andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
            try {
                Transport.send(message);
                getAndSetContextClassLoader(andSetContextClassLoader);
            } catch (Throwable th) {
                getAndSetContextClassLoader(andSetContextClassLoader);
                throw th;
            }
        } catch (RuntimeException e) {
            reportError(message, e, i);
        } catch (Exception e2) {
            reportError(message, e2, i);
        }
    }

    private void setAcceptLang(Part part) {
        try {
            String languageTag = LogManagerProperties.toLanguageTag(Locale.getDefault());
            if (languageTag.length() != 0) {
                part.setHeader("Accept-Language", languageTag);
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void setAuthenticator0(Authenticator authenticator) {
        Session updateSession;
        checkAccess();
        synchronized (this) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.auth = authenticator;
            updateSession = updateSession();
        }
        verifySettings(updateSession);
    }

    private void setAutoSubmitted(Message message) {
        if (allowRestrictedHeaders()) {
            try {
                message.setHeader("auto-submitted", "auto-generated");
            } catch (MessagingException e) {
                reportError(e.getMessage(), e, 5);
            }
        }
    }

    private synchronized void setCapacity0(int i) {
        checkAccess();
        if (i <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        if (this.capacity < 0) {
            this.capacity = -i;
        } else {
            this.capacity = i;
        }
    }

    private void setContent(MimePart mimePart, CharSequence charSequence, String str) throws MessagingException {
        String encodingName = getEncodingName();
        if (str == null || "text/plain".equalsIgnoreCase(str)) {
            mimePart.setText(charSequence.toString(), MimeUtility.mimeCharset(encodingName));
            return;
        }
        try {
            mimePart.setDataHandler(new DataHandler(new ByteArrayDataSource(charSequence.toString(), contentWithEncoding(str, encodingName))));
        } catch (IOException e) {
            reportError(e.getMessage(), e, 5);
            mimePart.setText(charSequence.toString(), encodingName);
        }
    }

    private void setDefaultFrom(Message message) {
        try {
            message.setFrom();
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void setDefaultRecipient(Message message, Message.RecipientType recipientType) {
        try {
            InternetAddress localAddress = InternetAddress.getLocalAddress(getSession(message));
            if (localAddress != null) {
                message.setRecipient(recipientType, localAddress);
                return;
            }
            MimeMessage mimeMessage = new MimeMessage(getSession(message));
            mimeMessage.setFrom();
            Address[] from = mimeMessage.getFrom();
            if (from.length <= 0) {
                throw new MessagingException("No local address.");
            }
            message.setRecipients(recipientType, from);
        } catch (RuntimeException | MessagingException e) {
            reportError("Unable to compute a default recipient.", e, 5);
        }
    }

    private void setEncoding0(String str) throws UnsupportedEncodingException {
        if (str != null) {
            try {
                if (!Charset.isSupported(str)) {
                    throw new UnsupportedEncodingException(str);
                }
            } catch (IllegalCharsetNameException unused) {
                throw new UnsupportedEncodingException(str);
            }
        }
        synchronized (this) {
            this.encoding = str;
        }
    }

    private void setErrorContent(MimeMessage mimeMessage, String str, Throwable th) {
        MimeBodyPart createBodyPart;
        String descriptionFrom;
        String classId;
        try {
            synchronized (this) {
                createBodyPart = createBodyPart();
                descriptionFrom = descriptionFrom(this.comparator, this.pushLevel, this.pushFilter);
                classId = getClassId(this.subjectFormatter);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Formatted using ");
            sb.append(th == null ? Throwable.class.getName() : th.getClass().getName());
            sb.append(", filtered with ");
            sb.append(str);
            sb.append(", and named by ");
            sb.append(classId);
            sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            createBodyPart.setDescription(sb.toString());
            setContent(createBodyPart, toMsgString(th), "text/plain");
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(createBodyPart);
            mimeMessage.setContent(mimeMultipart);
            mimeMessage.setDescription(descriptionFrom);
            setAcceptLang(mimeMessage);
            mimeMessage.saveChanges();
        } catch (RuntimeException | MessagingException e) {
            reportError("Unable to create body.", e, 4);
        }
    }

    private void setErrorManager0(ErrorManager errorManager) {
        if (errorManager == null) {
            throw new NullPointerException();
        }
        try {
            synchronized (this) {
                this.errorManager = errorManager;
                super.setErrorManager(errorManager);
            }
        } catch (LinkageError | RuntimeException unused) {
        }
    }

    private void setFrom(Message message) {
        String property = getSession(message).getProperty("mail.from");
        if (property == null) {
            setDefaultFrom(message);
            return;
        }
        try {
            InternetAddress[] parse = InternetAddress.parse(property, false);
            if (parse.length > 0) {
                if (parse.length == 1) {
                    message.setFrom(parse[0]);
                } else {
                    message.addFrom(parse);
                }
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
            setDefaultFrom(message);
        }
    }

    private void setIncompleteCopy(Message message) {
        try {
            message.setHeader("Incomplete-Copy", "");
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void setMailProperties0(Properties properties) {
        Session updateSession;
        checkAccess();
        Properties properties2 = (Properties) properties.clone();
        synchronized (this) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.mailProps = properties2;
            updateSession = updateSession();
        }
        verifySettings(updateSession);
    }

    private void setMailer(Message message) {
        String replaceAll;
        String fold;
        try {
            Class<?> cls = getClass();
            if (cls == MailHandler.class) {
                fold = MailHandler.class.getName();
            } else {
                try {
                    replaceAll = MimeUtility.encodeText(cls.getName());
                } catch (UnsupportedEncodingException e) {
                    reportError(e.getMessage(), e, 5);
                    replaceAll = cls.getName().replaceAll("[^\\x00-\\x7F]", "\u001a");
                }
                fold = MimeUtility.fold(10, MailHandler.class.getName() + " using the " + replaceAll + " extension.");
            }
            message.setHeader("X-Mailer", fold);
        } catch (MessagingException e2) {
            reportError(e2.getMessage(), e2, 5);
        }
    }

    private void setMatchedPart(int i) {
        if (MUTEX_PUBLISH.equals(MUTEX.get())) {
            MUTEX.set(Integer.valueOf(i));
        }
    }

    private void setPriority(Message message) {
        try {
            message.setHeader("Importance", "High");
            message.setHeader("Priority", "urgent");
            message.setHeader("X-Priority", AmapLoc.RESULT_TYPE_FUSED);
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private boolean setRecipient(Message message, String str, Message.RecipientType recipientType) {
        String property = getSession(message).getProperty(str);
        boolean z = property != null;
        if (!isEmpty(property)) {
            try {
                InternetAddress[] parse = InternetAddress.parse(property, false);
                if (parse.length > 0) {
                    message.setRecipients(recipientType, parse);
                }
            } catch (MessagingException e) {
                reportError(e.getMessage(), e, 5);
            }
        }
        return z;
    }

    private void setReplyTo(Message message) {
        String property = getSession(message).getProperty("mail.reply.to");
        if (isEmpty(property)) {
            return;
        }
        try {
            InternetAddress[] parse = InternetAddress.parse(property, false);
            if (parse.length > 0) {
                message.setReplyTo(parse);
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void setSender(Message message) {
        String property = getSession(message).getProperty("mail.sender");
        if (isEmpty(property)) {
            return;
        }
        try {
            Address[] parse = InternetAddress.parse(property, false);
            if (parse.length > 0) {
                ((MimeMessage) message).setSender(parse[0]);
                if (parse.length > 1) {
                    reportError("Ignoring other senders.", tooManyAddresses(parse, 1), 5);
                }
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void sort() {
        if (this.comparator != null) {
            try {
                if (this.size != 1) {
                    Arrays.sort(this.data, 0, this.size, this.comparator);
                } else if (this.comparator.compare(this.data[0], this.data[0]) != 0) {
                    throw new IllegalArgumentException(this.comparator.getClass().getName());
                }
            } catch (RuntimeException e) {
                reportError(e.getMessage(), e, 5);
            }
        }
    }

    private String tail(Formatter formatter, String str) {
        try {
            return formatter.getTail(this);
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, 5);
            return str;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0049 A[Catch: all -> 0x004d, Throwable -> 0x004f, TryCatch #0 {, blocks: (B:10:0x0018, B:13:0x002a, B:21:0x004c, B:20:0x0049, B:27:0x0045), top: B:9:0x0018, outer: #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0040 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String toMsgString(java.lang.Throwable r9) {
        /*
            r8 = this;
            if (r9 != 0) goto L5
            java.lang.String r9 = "null"
            return r9
        L5:
            java.lang.String r0 = r8.getEncodingName()
            r1 = 32
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
            r3 = 1024(0x400, float:1.435E-42)
            r2.<init>(r3)     // Catch: java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
            java.io.OutputStreamWriter r3 = new java.io.OutputStreamWriter     // Catch: java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
            r3.<init>(r2, r0)     // Catch: java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
            r4 = 0
            java.io.PrintWriter r5 = new java.io.PrintWriter     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            r5.<init>(r3)     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            java.lang.String r6 = r9.getMessage()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L38
            r5.println(r6)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L38
            r9.printStackTrace(r5)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L38
            r5.flush()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L38
            r5.close()     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            r3.close()     // Catch: java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
            java.lang.String r0 = r2.toString(r0)     // Catch: java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
            return r0
        L35:
            r0 = move-exception
            r2 = r4
            goto L3e
        L38:
            r0 = move-exception
            throw r0     // Catch: java.lang.Throwable -> L3a
        L3a:
            r2 = move-exception
            r7 = r2
            r2 = r0
            r0 = r7
        L3e:
            if (r2 == 0) goto L49
            r5.close()     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L4d
            goto L4c
        L44:
            r5 = move-exception
            r2.addSuppressed(r5)     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            goto L4c
        L49:
            r5.close()     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
        L4c:
            throw r0     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
        L4d:
            r0 = move-exception
            goto L52
        L4f:
            r0 = move-exception
            r4 = r0
            throw r4     // Catch: java.lang.Throwable -> L4d
        L52:
            if (r4 == 0) goto L5d
            r3.close()     // Catch: java.lang.Throwable -> L58 java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
            goto L60
        L58:
            r2 = move-exception
            r4.addSuppressed(r2)     // Catch: java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
            goto L60
        L5d:
            r3.close()     // Catch: java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
        L60:
            throw r0     // Catch: java.lang.Exception -> L61 java.lang.RuntimeException -> L7d
        L61:
            r0 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r9 = r9.toString()
            r2.append(r9)
            r2.append(r1)
            java.lang.String r9 = r0.toString()
            r2.append(r9)
            java.lang.String r9 = r2.toString()
            return r9
        L7d:
            r0 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r9 = r9.toString()
            r2.append(r9)
            r2.append(r1)
            java.lang.String r9 = r0.toString()
            r2.append(r9)
            java.lang.String r9 = r2.toString()
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.logging.MailHandler.toMsgString(java.lang.Throwable):java.lang.String");
    }

    private String toRawString(Message message) throws MessagingException, IOException {
        if (message == null) {
            return null;
        }
        Object andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Math.max(message.getSize() + 1024, 1024));
            message.writeTo(byteArrayOutputStream);
            return byteArrayOutputStream.toString("UTF-8");
        } finally {
            getAndSetContextClassLoader(andSetContextClassLoader);
        }
    }

    private String toString(Formatter formatter) {
        String obj = formatter.toString();
        return !isEmpty(obj) ? obj : getClassId(formatter);
    }

    private AddressException tooManyAddresses(Address[] addressArr, int i) {
        return new AddressException(Arrays.asList(addressArr).subList(i, addressArr.length).toString());
    }

    private boolean tryMutex() {
        if (MUTEX.get() != null) {
            return false;
        }
        MUTEX.set(MUTEX_PUBLISH);
        return true;
    }

    private Session updateSession() {
        if (this.mailProps.getProperty("verify") != null) {
            return initSession();
        }
        this.session = null;
        return null;
    }

    private static void verifyAddresses(Address[] addressArr) throws AddressException {
        if (addressArr != null) {
            for (Address address : addressArr) {
                if (address instanceof InternetAddress) {
                    ((InternetAddress) address).validate();
                }
            }
        }
    }

    private static InetAddress verifyHost(String str) throws IOException {
        InetAddress localHost = isEmpty(str) ? InetAddress.getLocalHost() : InetAddress.getByName(str);
        if (localHost.getCanonicalHostName().length() != 0) {
            return localHost;
        }
        throw new UnknownHostException();
    }

    private static void verifyProperties(Session session, String str) {
        session.getProperty("mail.from");
        session.getProperty("mail." + str + ".from");
        session.getProperty("mail.dsn.ret");
        session.getProperty("mail." + str + ".dsn.ret");
        session.getProperty("mail.dsn.notify");
        session.getProperty("mail." + str + ".dsn.notify");
        session.getProperty("mail." + str + ".port");
        session.getProperty("mail.user");
        session.getProperty("mail." + str + ".user");
        session.getProperty("mail." + str + ".localport");
    }

    private void verifySettings(Session session) {
        if (session != null) {
            try {
                Object put = session.getProperties().put("verify", "");
                if (put instanceof String) {
                    String str = (String) put;
                    if (hasValue(str)) {
                        verifySettings0(session, str);
                    }
                } else if (put != null) {
                    verifySettings0(session, put.getClass().toString());
                }
            } catch (LinkageError e) {
                reportLinkageError(e, 4);
            }
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(23:46|47|(10:52|(1:54)(1:138)|55|(1:57)(1:137)|58|(4:126|127|(2:129|(1:131))(1:134)|132)|60|(14:88|89|(1:93)|94|95|96|97|98|2a0|107|(2:110|108)|111|112|113)|62|(6:64|(1:66)|67|(1:(2:84|85))(3:71|(3:74|(1:76)(3:77|78|79)|72)|80)|81|82)(2:86|87))|139|140|141|142|143|144|145|(1:147)|149|150|151|152|153|(1:155)(1:160)|156|(1:158)|60|(0)|62|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x0221, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0242, code lost:
    
        r2 = r0.getInvalidAddresses();
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x0246, code lost:
    
        if (r2 != null) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x024b, code lost:
    
        setErrorContent(r5, r18, r0);
        reportError(r5, r0, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0251, code lost:
    
        r2 = r0.getValidSentAddresses();
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0255, code lost:
    
        if (r2 != null) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x025a, code lost:
    
        reportUnexpectedSend(r5, r18, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x021f, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x0205, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0206, code lost:
    
        r14 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x022d, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x022e, code lost:
    
        r14 = r12;
     */
    /* JADX WARN: Removed duplicated region for block: B:158:0x025f A[Catch: Exception -> 0x0397, RuntimeException -> 0x039f, TryCatch #8 {Exception -> 0x0397, blocks: (B:35:0x00db, B:37:0x00e1, B:40:0x00e4, B:44:0x00ef, B:46:0x00f2, B:47:0x0118, B:49:0x0121, B:52:0x012b, B:54:0x0156, B:55:0x0162, B:57:0x0182, B:58:0x01b6, B:127:0x01be, B:129:0x01cc, B:131:0x01d5, B:134:0x01da, B:60:0x0266, B:89:0x026e, B:91:0x0276, B:93:0x027e, B:94:0x0282, B:96:0x0292, B:113:0x02fb, B:122:0x0308, B:119:0x0303, B:120:0x0306, B:62:0x0313, B:64:0x0316, B:66:0x0325, B:67:0x032b, B:69:0x0335, B:71:0x0338, B:72:0x033c, B:74:0x033f, B:76:0x0347, B:78:0x034a, B:79:0x036a, B:81:0x036d, B:84:0x0375, B:85:0x0381, B:86:0x0382, B:87:0x0389, B:125:0x0287, B:136:0x01e0, B:137:0x019d, B:138:0x015d, B:139:0x01ed, B:150:0x0200, B:153:0x0207, B:155:0x020f, B:158:0x025f, B:160:0x0213, B:173:0x0235, B:175:0x023b, B:162:0x0242, B:164:0x0248, B:166:0x024b, B:167:0x0251, B:169:0x0257, B:171:0x025a, B:183:0x0229, B:186:0x0232, B:197:0x00fe, B:198:0x0108, B:199:0x00e9, B:202:0x010b, B:205:0x0115, B:213:0x0393, B:214:0x0396), top: B:34:0x00db }] */
    /* JADX WARN: Removed duplicated region for block: B:175:0x023b A[Catch: Exception -> 0x0397, RuntimeException -> 0x039f, TryCatch #8 {Exception -> 0x0397, blocks: (B:35:0x00db, B:37:0x00e1, B:40:0x00e4, B:44:0x00ef, B:46:0x00f2, B:47:0x0118, B:49:0x0121, B:52:0x012b, B:54:0x0156, B:55:0x0162, B:57:0x0182, B:58:0x01b6, B:127:0x01be, B:129:0x01cc, B:131:0x01d5, B:134:0x01da, B:60:0x0266, B:89:0x026e, B:91:0x0276, B:93:0x027e, B:94:0x0282, B:96:0x0292, B:113:0x02fb, B:122:0x0308, B:119:0x0303, B:120:0x0306, B:62:0x0313, B:64:0x0316, B:66:0x0325, B:67:0x032b, B:69:0x0335, B:71:0x0338, B:72:0x033c, B:74:0x033f, B:76:0x0347, B:78:0x034a, B:79:0x036a, B:81:0x036d, B:84:0x0375, B:85:0x0381, B:86:0x0382, B:87:0x0389, B:125:0x0287, B:136:0x01e0, B:137:0x019d, B:138:0x015d, B:139:0x01ed, B:150:0x0200, B:153:0x0207, B:155:0x020f, B:158:0x025f, B:160:0x0213, B:173:0x0235, B:175:0x023b, B:162:0x0242, B:164:0x0248, B:166:0x024b, B:167:0x0251, B:169:0x0257, B:171:0x025a, B:183:0x0229, B:186:0x0232, B:197:0x00fe, B:198:0x0108, B:199:0x00e9, B:202:0x010b, B:205:0x0115, B:213:0x0393, B:214:0x0396), top: B:34:0x00db }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0316 A[Catch: Exception -> 0x0397, RuntimeException -> 0x039f, TryCatch #8 {Exception -> 0x0397, blocks: (B:35:0x00db, B:37:0x00e1, B:40:0x00e4, B:44:0x00ef, B:46:0x00f2, B:47:0x0118, B:49:0x0121, B:52:0x012b, B:54:0x0156, B:55:0x0162, B:57:0x0182, B:58:0x01b6, B:127:0x01be, B:129:0x01cc, B:131:0x01d5, B:134:0x01da, B:60:0x0266, B:89:0x026e, B:91:0x0276, B:93:0x027e, B:94:0x0282, B:96:0x0292, B:113:0x02fb, B:122:0x0308, B:119:0x0303, B:120:0x0306, B:62:0x0313, B:64:0x0316, B:66:0x0325, B:67:0x032b, B:69:0x0335, B:71:0x0338, B:72:0x033c, B:74:0x033f, B:76:0x0347, B:78:0x034a, B:79:0x036a, B:81:0x036d, B:84:0x0375, B:85:0x0381, B:86:0x0382, B:87:0x0389, B:125:0x0287, B:136:0x01e0, B:137:0x019d, B:138:0x015d, B:139:0x01ed, B:150:0x0200, B:153:0x0207, B:155:0x020f, B:158:0x025f, B:160:0x0213, B:173:0x0235, B:175:0x023b, B:162:0x0242, B:164:0x0248, B:166:0x024b, B:167:0x0251, B:169:0x0257, B:171:0x025a, B:183:0x0229, B:186:0x0232, B:197:0x00fe, B:198:0x0108, B:199:0x00e9, B:202:0x010b, B:205:0x0115, B:213:0x0393, B:214:0x0396), top: B:34:0x00db }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0382 A[Catch: Exception -> 0x0397, RuntimeException -> 0x039f, TryCatch #8 {Exception -> 0x0397, blocks: (B:35:0x00db, B:37:0x00e1, B:40:0x00e4, B:44:0x00ef, B:46:0x00f2, B:47:0x0118, B:49:0x0121, B:52:0x012b, B:54:0x0156, B:55:0x0162, B:57:0x0182, B:58:0x01b6, B:127:0x01be, B:129:0x01cc, B:131:0x01d5, B:134:0x01da, B:60:0x0266, B:89:0x026e, B:91:0x0276, B:93:0x027e, B:94:0x0282, B:96:0x0292, B:113:0x02fb, B:122:0x0308, B:119:0x0303, B:120:0x0306, B:62:0x0313, B:64:0x0316, B:66:0x0325, B:67:0x032b, B:69:0x0335, B:71:0x0338, B:72:0x033c, B:74:0x033f, B:76:0x0347, B:78:0x034a, B:79:0x036a, B:81:0x036d, B:84:0x0375, B:85:0x0381, B:86:0x0382, B:87:0x0389, B:125:0x0287, B:136:0x01e0, B:137:0x019d, B:138:0x015d, B:139:0x01ed, B:150:0x0200, B:153:0x0207, B:155:0x020f, B:158:0x025f, B:160:0x0213, B:173:0x0235, B:175:0x023b, B:162:0x0242, B:164:0x0248, B:166:0x024b, B:167:0x0251, B:169:0x0257, B:171:0x025a, B:183:0x0229, B:186:0x0232, B:197:0x00fe, B:198:0x0108, B:199:0x00e9, B:202:0x010b, B:205:0x0115, B:213:0x0393, B:214:0x0396), top: B:34:0x00db }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x026e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void verifySettings0(javax.mail.Session r17, java.lang.String r18) {
        /*
            Method dump skipped, instructions count: 938
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.logging.MailHandler.verifySettings0(javax.mail.Session, java.lang.String):void");
    }

    private Message writeLogRecords(int i) {
        try {
            synchronized (this) {
                if (this.size <= 0 || this.isWriting) {
                    return null;
                }
                this.isWriting = true;
                try {
                    return writeLogRecords0();
                } finally {
                    this.isWriting = false;
                    if (this.size > 0) {
                        reset();
                    }
                }
            }
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, i);
            return null;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, i);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Message writeLogRecords0() throws Exception {
        MimeBodyPart createBodyPart;
        int i;
        StringBuilder sb;
        StringBuilder sb2;
        boolean z;
        Filter filter;
        sort();
        if (this.session == null) {
            initSession();
        }
        MimeMessage mimeMessage = new MimeMessage(this.session);
        MimeBodyPart[] mimeBodyPartArr = new MimeBodyPart[this.attachmentFormatters.length];
        StringBuilder[] sbArr = new StringBuilder[mimeBodyPartArr.length];
        if (mimeBodyPartArr.length == 0) {
            mimeMessage.setDescription(descriptionFrom(getFormatter(), getFilter(), this.subjectFormatter));
            createBodyPart = mimeMessage;
        } else {
            mimeMessage.setDescription(descriptionFrom(this.comparator, this.pushLevel, this.pushFilter));
            createBodyPart = createBodyPart();
        }
        appendSubject(mimeMessage, head(this.subjectFormatter));
        Formatter formatter = getFormatter();
        Filter filter2 = getFilter();
        LogRecord logRecord = null;
        StringBuilder sb3 = null;
        Object obj = null;
        int i2 = 0;
        while (i2 < this.size) {
            int i3 = this.matched[i2];
            LogRecord logRecord2 = this.data[i2];
            this.data[i2] = logRecord;
            Locale localeFor = localeFor(logRecord2);
            appendSubject(mimeMessage, format(this.subjectFormatter, logRecord2));
            if (filter2 == null || i3 == -1 || mimeBodyPartArr.length == 0 || (i3 < -1 && filter2.isLoggable(logRecord2))) {
                if (sb3 == null) {
                    sb3 = new StringBuilder();
                    sb3.append(head(formatter));
                }
                sb3.append(format(formatter, logRecord2));
                if (localeFor != null && !localeFor.equals(obj)) {
                    appendContentLang(createBodyPart, localeFor);
                }
                sb2 = sb3;
                z = true;
                filter = filter2;
            } else {
                sb2 = sb3;
                z = false;
                filter = null;
            }
            Filter filter3 = filter2;
            Filter filter4 = filter;
            boolean z2 = z;
            for (int i4 = 0; i4 < mimeBodyPartArr.length; i4++) {
                Filter filter5 = this.attachmentFilters[i4];
                if (filter5 == null || filter4 == filter5 || i3 == i4 || (i3 < i4 && filter5.isLoggable(logRecord2))) {
                    if (filter4 != null || filter5 == null) {
                        filter5 = filter4;
                    }
                    if (mimeBodyPartArr[i4] == null) {
                        mimeBodyPartArr[i4] = createBodyPart(i4);
                        sbArr[i4] = new StringBuilder();
                        sbArr[i4].append(head(this.attachmentFormatters[i4]));
                        appendFileName(mimeBodyPartArr[i4], head(this.attachmentNames[i4]));
                    }
                    appendFileName(mimeBodyPartArr[i4], format(this.attachmentNames[i4], logRecord2));
                    sbArr[i4].append(format(this.attachmentFormatters[i4], logRecord2));
                    if (localeFor != null && !localeFor.equals(obj)) {
                        appendContentLang(mimeBodyPartArr[i4], localeFor);
                    }
                    filter4 = filter5;
                    z2 = true;
                }
            }
            if (!z2) {
                reportFilterError(logRecord2);
            } else if (createBodyPart != mimeMessage && localeFor != null && !localeFor.equals(obj)) {
                appendContentLang(mimeMessage, localeFor);
            }
            i2++;
            sb3 = sb2;
            obj = localeFor;
            filter2 = filter3;
            logRecord = null;
        }
        this.size = 0;
        for (int length = mimeBodyPartArr.length - 1; length >= 0; length--) {
            if (mimeBodyPartArr[length] != null) {
                appendFileName(mimeBodyPartArr[length], tail(this.attachmentNames[length], NotificationCompat.CATEGORY_ERROR));
                sbArr[length].append(tail(this.attachmentFormatters[length], ""));
                if (sbArr[length].length() > 0) {
                    String fileName = mimeBodyPartArr[length].getFileName();
                    if (isEmpty(fileName)) {
                        fileName = toString(this.attachmentFormatters[length]);
                        mimeBodyPartArr[length].setFileName(fileName);
                    }
                    setContent(mimeBodyPartArr[length], sbArr[length], getContentType(fileName));
                    sb = null;
                } else {
                    setIncompleteCopy(mimeMessage);
                    sb = null;
                    mimeBodyPartArr[length] = null;
                }
                sbArr[length] = sb;
            }
        }
        if (sb3 != null) {
            sb3.append(tail(formatter, ""));
            i = 0;
        } else {
            i = 0;
            sb3 = new StringBuilder(0);
        }
        appendSubject(mimeMessage, tail(this.subjectFormatter, ""));
        String contentTypeOf = contentTypeOf(sb3);
        String contentTypeOf2 = contentTypeOf(formatter);
        if (contentTypeOf2 != null) {
            contentTypeOf = contentTypeOf2;
        }
        setContent(createBodyPart, sb3, contentTypeOf);
        if (createBodyPart != mimeMessage) {
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(createBodyPart);
            while (i < mimeBodyPartArr.length) {
                if (mimeBodyPartArr[i] != null) {
                    mimeMultipart.addBodyPart(mimeBodyPartArr[i]);
                }
                i++;
            }
            mimeMessage.setContent(mimeMultipart);
        }
        return mimeMessage;
    }

    @Override // java.util.logging.Handler
    public void close() {
        Message writeLogRecords;
        try {
            checkAccess();
            synchronized (this) {
                try {
                    writeLogRecords = writeLogRecords(3);
                    this.logLevel = Level.OFF;
                    if (this.capacity > 0) {
                        this.capacity = -this.capacity;
                    }
                    if (this.size == 0 && this.data.length != 1) {
                        this.data = new LogRecord[1];
                        this.matched = new int[this.data.length];
                    }
                } catch (Throwable th) {
                    this.logLevel = Level.OFF;
                    if (this.capacity > 0) {
                        this.capacity = -this.capacity;
                    }
                    if (this.size == 0 && this.data.length != 1) {
                        this.data = new LogRecord[1];
                        this.matched = new int[this.data.length];
                    }
                    throw th;
                }
            }
            if (writeLogRecords != null) {
                send(writeLogRecords, false, 3);
            }
        } catch (LinkageError e) {
            reportLinkageError(e, 3);
        }
    }

    final String contentTypeOf(CharSequence charSequence) {
        if (isEmpty(charSequence)) {
            return null;
        }
        if (charSequence.length() > 25) {
            charSequence = charSequence.subSequence(0, 25);
        }
        try {
            return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(charSequence.toString().getBytes(getEncodingName())));
        } catch (IOException e) {
            reportError(e.getMessage(), e, 5);
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0062, code lost:
    
        r7 = r7.getSuperclass();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final java.lang.String contentTypeOf(java.util.logging.Formatter r7) {
        /*
            r6 = this;
            if (r7 == 0) goto L67
            java.lang.Class r0 = r7.getClass()
            java.lang.String r0 = r0.getName()
            java.lang.String r0 = r6.getContentType(r0)
            if (r0 == 0) goto L11
            return r0
        L11:
            java.lang.Class r7 = r7.getClass()
        L15:
            java.lang.Class<java.util.logging.Formatter> r0 = java.util.logging.Formatter.class
            if (r7 == r0) goto L67
            java.lang.String r0 = r7.getSimpleName()     // Catch: java.lang.InternalError -> L1e
            goto L22
        L1e:
            java.lang.String r0 = r7.getName()
        L22:
            java.util.Locale r1 = java.util.Locale.ENGLISH
            java.lang.String r0 = r0.toLowerCase(r1)
            r1 = 36
            int r1 = r0.indexOf(r1)
            r2 = 1
            int r1 = r1 + r2
        L30:
            java.lang.String r3 = "ml"
            int r1 = r0.indexOf(r3, r1)
            r3 = -1
            if (r1 <= r3) goto L62
            if (r1 <= 0) goto L5f
            int r3 = r1 + (-1)
            char r4 = r0.charAt(r3)
            r5 = 120(0x78, float:1.68E-43)
            if (r4 != r5) goto L48
            java.lang.String r7 = "application/xml"
            return r7
        L48:
            if (r1 <= r2) goto L5f
            int r4 = r1 + (-2)
            char r4 = r0.charAt(r4)
            r5 = 104(0x68, float:1.46E-43)
            if (r4 != r5) goto L5f
            char r3 = r0.charAt(r3)
            r4 = 116(0x74, float:1.63E-43)
            if (r3 != r4) goto L5f
            java.lang.String r7 = "text/html"
            return r7
        L5f:
            int r1 = r1 + 2
            goto L30
        L62:
            java.lang.Class r7 = r7.getSuperclass()
            goto L15
        L67:
            r7 = 0
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.logging.MailHandler.contentTypeOf(java.util.logging.Formatter):java.lang.String");
    }

    @Override // java.util.logging.Handler
    public void flush() {
        push(false, 2);
    }

    public final Filter[] getAttachmentFilters() {
        return (Filter[]) readOnlyAttachmentFilters().clone();
    }

    public final Formatter[] getAttachmentFormatters() {
        Formatter[] formatterArr;
        synchronized (this) {
            formatterArr = this.attachmentFormatters;
        }
        return (Formatter[]) formatterArr.clone();
    }

    public final Formatter[] getAttachmentNames() {
        Formatter[] formatterArr;
        synchronized (this) {
            formatterArr = this.attachmentNames;
        }
        return (Formatter[]) formatterArr.clone();
    }

    public final synchronized Authenticator getAuthenticator() {
        checkAccess();
        return this.auth;
    }

    public final synchronized int getCapacity() {
        return Math.abs(this.capacity);
    }

    public final synchronized Comparator<? super LogRecord> getComparator() {
        return this.comparator;
    }

    @Override // java.util.logging.Handler
    public synchronized String getEncoding() {
        return this.encoding;
    }

    @Override // java.util.logging.Handler
    public ErrorManager getErrorManager() {
        checkAccess();
        return this.errorManager;
    }

    @Override // java.util.logging.Handler
    public Filter getFilter() {
        return this.filter;
    }

    @Override // java.util.logging.Handler
    public synchronized Formatter getFormatter() {
        return this.formatter;
    }

    @Override // java.util.logging.Handler
    public Level getLevel() {
        return this.logLevel;
    }

    public final Properties getMailProperties() {
        Properties properties;
        checkAccess();
        synchronized (this) {
            properties = this.mailProps;
        }
        return (Properties) properties.clone();
    }

    public final synchronized Filter getPushFilter() {
        return this.pushFilter;
    }

    public final synchronized Level getPushLevel() {
        return this.pushLevel;
    }

    public final synchronized Formatter getSubject() {
        return this.subjectFormatter;
    }

    @Override // java.util.logging.Handler
    public boolean isLoggable(LogRecord logRecord) {
        int intValue = getLevel().intValue();
        if (logRecord.getLevel().intValue() < intValue || intValue == offValue) {
            return false;
        }
        Filter filter = getFilter();
        if (filter != null && !filter.isLoggable(logRecord)) {
            return isAttachmentLoggable(logRecord);
        }
        setMatchedPart(-1);
        return true;
    }

    final boolean isMissingContent(Message message, Throwable th) {
        Object andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
        try {
            try {
                message.writeTo(new ByteArrayOutputStream(1024));
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                String message2 = e2.getMessage();
                if (!isEmpty(message2)) {
                    int i = 0;
                    while (th != null) {
                        if (e2.getClass() == th.getClass() && message2.equals(th.getMessage())) {
                            getAndSetContextClassLoader(andSetContextClassLoader);
                            return true;
                        }
                        Throwable cause = th.getCause();
                        th = (cause == null && (th instanceof MessagingException)) ? ((MessagingException) th).getNextException() : cause;
                        i++;
                        if (i == 65536) {
                            break;
                        }
                    }
                }
            }
            getAndSetContextClassLoader(andSetContextClassLoader);
            return false;
        } catch (Throwable th2) {
            getAndSetContextClassLoader(andSetContextClassLoader);
            throw th2;
        }
    }

    public void postConstruct() {
    }

    public void preDestroy() {
        push(false, 3);
    }

    @Override // java.util.logging.Handler
    public void publish(LogRecord logRecord) {
        try {
            if (!tryMutex()) {
                reportUnPublishedError(logRecord);
                return;
            }
            try {
                if (isLoggable(logRecord)) {
                    logRecord.getSourceMethodName();
                    publish0(logRecord);
                }
            } catch (LinkageError e) {
                reportLinkageError(e, 1);
            }
        } finally {
            releaseMutex();
        }
    }

    public void push() {
        push(true, 2);
    }

    @Override // java.util.logging.Handler
    protected void reportError(String str, Exception exc, int i) {
        try {
            if (str != null) {
                this.errorManager.error(Level.SEVERE.getName().concat(": ").concat(str), exc, i);
            } else {
                this.errorManager.error(null, exc, i);
            }
        } catch (LinkageError | RuntimeException e) {
            reportLinkageError(e, i);
        }
    }

    public final void setAttachmentFilters(Filter... filterArr) {
        checkAccess();
        Filter[] emptyFilterArray = filterArr.length == 0 ? emptyFilterArray() : (Filter[]) Arrays.copyOf(filterArr, filterArr.length, Filter[].class);
        synchronized (this) {
            if (this.attachmentFormatters.length != emptyFilterArray.length) {
                throw attachmentMismatch(this.attachmentFormatters.length, emptyFilterArray.length);
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            if (this.size != 0) {
                int i = 0;
                while (true) {
                    if (i >= emptyFilterArray.length) {
                        break;
                    }
                    if (emptyFilterArray[i] != this.attachmentFilters[i]) {
                        clearMatches(i);
                        break;
                    }
                    i++;
                }
            }
            this.attachmentFilters = emptyFilterArray;
        }
    }

    public final void setAttachmentFormatters(Formatter... formatterArr) {
        Formatter[] formatterArr2;
        checkAccess();
        if (formatterArr.length == 0) {
            formatterArr2 = emptyFormatterArray();
        } else {
            formatterArr2 = (Formatter[]) Arrays.copyOf(formatterArr, formatterArr.length, Formatter[].class);
            for (int i = 0; i < formatterArr2.length; i++) {
                if (formatterArr2[i] == null) {
                    throw new NullPointerException(atIndexMsg(i));
                }
            }
        }
        synchronized (this) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentFormatters = formatterArr2;
            alignAttachmentFilters();
            alignAttachmentNames();
        }
    }

    public final void setAttachmentNames(String... strArr) {
        checkAccess();
        Formatter[] emptyFormatterArray = strArr.length == 0 ? emptyFormatterArray() : new Formatter[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            if (str == null) {
                throw new NullPointerException(atIndexMsg(i));
            }
            if (str.length() <= 0) {
                throw new IllegalArgumentException(atIndexMsg(i));
            }
            emptyFormatterArray[i] = TailNameFormatter.of(str);
        }
        synchronized (this) {
            if (this.attachmentFormatters.length != strArr.length) {
                throw attachmentMismatch(this.attachmentFormatters.length, strArr.length);
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentNames = emptyFormatterArray;
        }
    }

    public final void setAttachmentNames(Formatter... formatterArr) {
        checkAccess();
        Formatter[] emptyFormatterArray = formatterArr.length == 0 ? emptyFormatterArray() : (Formatter[]) Arrays.copyOf(formatterArr, formatterArr.length, Formatter[].class);
        for (int i = 0; i < emptyFormatterArray.length; i++) {
            if (emptyFormatterArray[i] == null) {
                throw new NullPointerException(atIndexMsg(i));
            }
        }
        synchronized (this) {
            if (this.attachmentFormatters.length != emptyFormatterArray.length) {
                throw attachmentMismatch(this.attachmentFormatters.length, emptyFormatterArray.length);
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentNames = emptyFormatterArray;
        }
    }

    public final void setAuthenticator(Authenticator authenticator) {
        setAuthenticator0(authenticator);
    }

    public final void setAuthenticator(char... cArr) {
        if (cArr == null) {
            setAuthenticator0((Authenticator) null);
        } else {
            setAuthenticator0(DefaultAuthenticator.of(new String(cArr)));
        }
    }

    public final synchronized void setComparator(Comparator<? super LogRecord> comparator) {
        checkAccess();
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.comparator = comparator;
    }

    @Override // java.util.logging.Handler
    public void setEncoding(String str) throws UnsupportedEncodingException {
        checkAccess();
        setEncoding0(str);
    }

    @Override // java.util.logging.Handler
    public void setErrorManager(ErrorManager errorManager) {
        checkAccess();
        setErrorManager0(errorManager);
    }

    @Override // java.util.logging.Handler
    public void setFilter(Filter filter) {
        checkAccess();
        synchronized (this) {
            if (filter != this.filter) {
                clearMatches(-1);
            }
            this.filter = filter;
        }
    }

    @Override // java.util.logging.Handler
    public synchronized void setFormatter(Formatter formatter) throws SecurityException {
        checkAccess();
        if (formatter == null) {
            throw new NullPointerException();
        }
        this.formatter = formatter;
    }

    @Override // java.util.logging.Handler
    public void setLevel(Level level) {
        if (level == null) {
            throw new NullPointerException();
        }
        checkAccess();
        synchronized (this) {
            if (this.capacity > 0) {
                this.logLevel = level;
            }
        }
    }

    public final void setMailProperties(Properties properties) {
        setMailProperties0(properties);
    }

    public final synchronized void setPushFilter(Filter filter) {
        checkAccess();
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.pushFilter = filter;
    }

    public final synchronized void setPushLevel(Level level) {
        checkAccess();
        if (level == null) {
            throw new NullPointerException();
        }
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.pushLevel = level;
    }

    public final void setSubject(String str) {
        if (str != null) {
            setSubject(TailNameFormatter.of(str));
        } else {
            checkAccess();
            throw new NullPointerException();
        }
    }

    public final void setSubject(Formatter formatter) {
        checkAccess();
        if (formatter == null) {
            throw new NullPointerException();
        }
        synchronized (this) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.subjectFormatter = formatter;
        }
    }
}
