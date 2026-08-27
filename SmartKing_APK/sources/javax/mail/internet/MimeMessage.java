package javax.mail.internet;

import com.litesuits.orm.db.assit.SQLBuilder;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.FolderClosedIOException;
import com.sun.mail.util.LineOutputStream;
import com.sun.mail.util.MessageRemovedIOException;
import com.sun.mail.util.MimeUtil;
import com.sun.mail.util.PropUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.SharedByteArrayInputStream;

/* loaded from: classes2.dex */
public class MimeMessage extends Message implements MimePart {
    private boolean allowutf8;
    protected Object cachedContent;
    protected byte[] content;
    protected InputStream contentStream;
    protected DataHandler dh;
    protected Flags flags;
    protected InternetHeaders headers;
    protected boolean modified;
    protected boolean saved;
    private boolean strict;
    private static final MailDateFormat mailDateFormat = new MailDateFormat();
    private static final Flags answeredFlag = new Flags(Flags.Flag.ANSWERED);

    /* loaded from: classes2.dex */
    public static class RecipientType extends Message.RecipientType {
        public static final RecipientType NEWSGROUPS = new RecipientType("Newsgroups");
        private static final long serialVersionUID = -5468290701714395543L;

        protected RecipientType(String str) {
            super(str);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // javax.mail.Message.RecipientType
        public Object readResolve() throws ObjectStreamException {
            return this.type.equals("Newsgroups") ? NEWSGROUPS : super.readResolve();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MimeMessage(Folder folder, int i) {
        super(folder, i);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.allowutf8 = false;
        this.flags = new Flags();
        this.saved = true;
        initStrict();
    }

    protected MimeMessage(Folder folder, InputStream inputStream, int i) throws MessagingException {
        this(folder, i);
        initStrict();
        parse(inputStream);
    }

    protected MimeMessage(Folder folder, InternetHeaders internetHeaders, byte[] bArr, int i) throws MessagingException {
        this(folder, i);
        this.headers = internetHeaders;
        this.content = bArr;
        initStrict();
    }

    public MimeMessage(Session session) {
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.allowutf8 = false;
        this.modified = true;
        this.headers = new InternetHeaders();
        this.flags = new Flags();
        initStrict();
    }

    public MimeMessage(Session session, InputStream inputStream) throws MessagingException {
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.allowutf8 = false;
        this.flags = new Flags();
        initStrict();
        parse(inputStream);
        this.saved = true;
    }

    public MimeMessage(MimeMessage mimeMessage) throws MessagingException {
        super(mimeMessage.session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.allowutf8 = false;
        this.flags = mimeMessage.getFlags();
        if (this.flags == null) {
            this.flags = new Flags();
        }
        int size = mimeMessage.getSize();
        ByteArrayOutputStream byteArrayOutputStream = size > 0 ? new ByteArrayOutputStream(size) : new ByteArrayOutputStream();
        try {
            this.strict = mimeMessage.strict;
            mimeMessage.writeTo(byteArrayOutputStream);
            byteArrayOutputStream.close();
            SharedByteArrayInputStream sharedByteArrayInputStream = new SharedByteArrayInputStream(byteArrayOutputStream.toByteArray());
            parse(sharedByteArrayInputStream);
            sharedByteArrayInputStream.close();
            this.saved = true;
        } catch (IOException e) {
            throw new MessagingException("IOException while copying message", e);
        }
    }

    private void addAddressHeader(String str, Address[] addressArr) throws MessagingException {
        if (addressArr == null || addressArr.length == 0) {
            return;
        }
        Address[] addressHeader = getAddressHeader(str);
        if (addressHeader != null && addressHeader.length != 0) {
            Address[] addressArr2 = new Address[addressHeader.length + addressArr.length];
            System.arraycopy(addressHeader, 0, addressArr2, 0, addressHeader.length);
            System.arraycopy(addressArr, 0, addressArr2, addressHeader.length, addressArr.length);
            addressArr = addressArr2;
        }
        String unicodeString = this.allowutf8 ? InternetAddress.toUnicodeString(addressArr, str.length() + 2) : InternetAddress.toString(addressArr, str.length() + 2);
        if (unicodeString == null) {
            return;
        }
        setHeader(str, unicodeString);
    }

    private Address[] eliminateDuplicates(List<Address> list, Address[] addressArr) {
        boolean z;
        if (addressArr == null) {
            return null;
        }
        int i = 0;
        for (int i2 = 0; i2 < addressArr.length; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= list.size()) {
                    z = false;
                    break;
                }
                if (((InternetAddress) list.get(i3)).equals(addressArr[i2])) {
                    i++;
                    addressArr[i2] = null;
                    z = true;
                    break;
                }
                i3++;
            }
            if (!z) {
                list.add(addressArr[i2]);
            }
        }
        if (i == 0) {
            return addressArr;
        }
        Address[] addressArr2 = addressArr instanceof InternetAddress[] ? new InternetAddress[addressArr.length - i] : new Address[addressArr.length - i];
        int i4 = 0;
        for (int i5 = 0; i5 < addressArr.length; i5++) {
            if (addressArr[i5] != null) {
                addressArr2[i4] = addressArr[i5];
                i4++;
            }
        }
        return addressArr2;
    }

    private Address[] getAddressHeader(String str) throws MessagingException {
        String header = getHeader(str, ",");
        if (header == null) {
            return null;
        }
        return InternetAddress.parseHeader(header, this.strict);
    }

    private String getHeaderName(Message.RecipientType recipientType) throws MessagingException {
        if (recipientType == Message.RecipientType.TO) {
            return "To";
        }
        if (recipientType == Message.RecipientType.CC) {
            return "Cc";
        }
        if (recipientType == Message.RecipientType.BCC) {
            return "Bcc";
        }
        if (recipientType == RecipientType.NEWSGROUPS) {
            return "Newsgroups";
        }
        throw new MessagingException("Invalid Recipient Type");
    }

    private void initStrict() {
        if (this.session != null) {
            this.strict = PropUtil.getBooleanSessionProperty(this.session, "mail.mime.address.strict", true);
            this.allowutf8 = PropUtil.getBooleanSessionProperty(this.session, "mail.mime.allowutf8", false);
        }
    }

    private void setAddressHeader(String str, Address[] addressArr) throws MessagingException {
        String unicodeString = this.allowutf8 ? InternetAddress.toUnicodeString(addressArr, str.length() + 2) : InternetAddress.toString(addressArr, str.length() + 2);
        if (unicodeString == null) {
            removeHeader(str);
        } else {
            setHeader(str, unicodeString);
        }
    }

    @Override // javax.mail.Message
    public void addFrom(Address[] addressArr) throws MessagingException {
        addAddressHeader("From", addressArr);
    }

    @Override // javax.mail.Part
    public void addHeader(String str, String str2) throws MessagingException {
        this.headers.addHeader(str, str2);
    }

    public void addHeaderLine(String str) throws MessagingException {
        this.headers.addHeaderLine(str);
    }

    public void addRecipients(Message.RecipientType recipientType, String str) throws MessagingException {
        if (recipientType != RecipientType.NEWSGROUPS) {
            addAddressHeader(getHeaderName(recipientType), InternetAddress.parse(str));
        } else {
            if (str == null || str.length() == 0) {
                return;
            }
            addHeader("Newsgroups", str);
        }
    }

    @Override // javax.mail.Message
    public void addRecipients(Message.RecipientType recipientType, Address[] addressArr) throws MessagingException {
        if (recipientType != RecipientType.NEWSGROUPS) {
            addAddressHeader(getHeaderName(recipientType), addressArr);
            return;
        }
        String newsAddress = NewsAddress.toString(addressArr);
        if (newsAddress != null) {
            addHeader("Newsgroups", newsAddress);
        }
    }

    protected InternetHeaders createInternetHeaders(InputStream inputStream) throws MessagingException {
        return new InternetHeaders(inputStream, this.allowutf8);
    }

    protected MimeMessage createMimeMessage(Session session) throws MessagingException {
        return new MimeMessage(session);
    }

    public Enumeration<String> getAllHeaderLines() throws MessagingException {
        return this.headers.getAllHeaderLines();
    }

    @Override // javax.mail.Part
    public Enumeration<Header> getAllHeaders() throws MessagingException {
        return this.headers.getAllHeaders();
    }

    @Override // javax.mail.Message
    public Address[] getAllRecipients() throws MessagingException {
        Address[] allRecipients = super.getAllRecipients();
        Address[] recipients = getRecipients(RecipientType.NEWSGROUPS);
        if (recipients == null) {
            return allRecipients;
        }
        if (allRecipients == null) {
            return recipients;
        }
        Address[] addressArr = new Address[allRecipients.length + recipients.length];
        System.arraycopy(allRecipients, 0, addressArr, 0, allRecipients.length);
        System.arraycopy(recipients, 0, addressArr, allRecipients.length, recipients.length);
        return addressArr;
    }

    @Override // javax.mail.Part
    public Object getContent() throws IOException, MessagingException {
        if (this.cachedContent != null) {
            return this.cachedContent;
        }
        try {
            Object content = getDataHandler().getContent();
            if (MimeBodyPart.cacheMultipart && (((content instanceof Multipart) || (content instanceof Message)) && (this.content != null || this.contentStream != null))) {
                this.cachedContent = content;
                if (content instanceof MimeMultipart) {
                    ((MimeMultipart) content).parse();
                }
            }
            return content;
        } catch (FolderClosedIOException e) {
            throw new FolderClosedException(e.getFolder(), e.getMessage());
        } catch (MessageRemovedIOException e2) {
            throw new MessageRemovedException(e2.getMessage());
        }
    }

    public String getContentID() throws MessagingException {
        return getHeader("Content-Id", null);
    }

    public String[] getContentLanguage() throws MessagingException {
        return MimeBodyPart.getContentLanguage(this);
    }

    public String getContentMD5() throws MessagingException {
        return getHeader("Content-MD5", null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InputStream getContentStream() throws MessagingException {
        if (this.contentStream != null) {
            return ((SharedInputStream) this.contentStream).newStream(0L, -1L);
        }
        if (this.content != null) {
            return new SharedByteArrayInputStream(this.content);
        }
        throw new MessagingException("No MimeMessage content");
    }

    @Override // javax.mail.Part
    public String getContentType() throws MessagingException {
        String cleanContentType = MimeUtil.cleanContentType(this, getHeader("Content-Type", null));
        return cleanContentType == null ? "text/plain" : cleanContentType;
    }

    @Override // javax.mail.Part
    public synchronized DataHandler getDataHandler() throws MessagingException {
        if (this.dh == null) {
            this.dh = new MimeBodyPart.MimePartDataHandler(this);
        }
        return this.dh;
    }

    @Override // javax.mail.Part
    public String getDescription() throws MessagingException {
        return MimeBodyPart.getDescription(this);
    }

    @Override // javax.mail.Part
    public String getDisposition() throws MessagingException {
        return MimeBodyPart.getDisposition(this);
    }

    public String getEncoding() throws MessagingException {
        return MimeBodyPart.getEncoding(this);
    }

    @Override // javax.mail.Part
    public String getFileName() throws MessagingException {
        return MimeBodyPart.getFileName(this);
    }

    @Override // javax.mail.Message
    public synchronized Flags getFlags() throws MessagingException {
        return (Flags) this.flags.clone();
    }

    @Override // javax.mail.Message
    public Address[] getFrom() throws MessagingException {
        Address[] addressHeader = getAddressHeader("From");
        return addressHeader == null ? getAddressHeader("Sender") : addressHeader;
    }

    public String getHeader(String str, String str2) throws MessagingException {
        return this.headers.getHeader(str, str2);
    }

    @Override // javax.mail.Part
    public String[] getHeader(String str) throws MessagingException {
        return this.headers.getHeader(str);
    }

    @Override // javax.mail.Part
    public InputStream getInputStream() throws IOException, MessagingException {
        return getDataHandler().getInputStream();
    }

    @Override // javax.mail.Part
    public int getLineCount() throws MessagingException {
        return -1;
    }

    public Enumeration<String> getMatchingHeaderLines(String[] strArr) throws MessagingException {
        return this.headers.getMatchingHeaderLines(strArr);
    }

    @Override // javax.mail.Part
    public Enumeration<Header> getMatchingHeaders(String[] strArr) throws MessagingException {
        return this.headers.getMatchingHeaders(strArr);
    }

    public String getMessageID() throws MessagingException {
        return getHeader("Message-ID", null);
    }

    public Enumeration<String> getNonMatchingHeaderLines(String[] strArr) throws MessagingException {
        return this.headers.getNonMatchingHeaderLines(strArr);
    }

    @Override // javax.mail.Part
    public Enumeration<Header> getNonMatchingHeaders(String[] strArr) throws MessagingException {
        return this.headers.getNonMatchingHeaders(strArr);
    }

    public InputStream getRawInputStream() throws MessagingException {
        return getContentStream();
    }

    @Override // javax.mail.Message
    public Date getReceivedDate() throws MessagingException {
        return null;
    }

    @Override // javax.mail.Message
    public Address[] getRecipients(Message.RecipientType recipientType) throws MessagingException {
        if (recipientType != RecipientType.NEWSGROUPS) {
            return getAddressHeader(getHeaderName(recipientType));
        }
        String header = getHeader("Newsgroups", ",");
        if (header == null) {
            return null;
        }
        return NewsAddress.parse(header);
    }

    @Override // javax.mail.Message
    public Address[] getReplyTo() throws MessagingException {
        Address[] addressHeader = getAddressHeader("Reply-To");
        return (addressHeader == null || addressHeader.length == 0) ? getFrom() : addressHeader;
    }

    public Address getSender() throws MessagingException {
        Address[] addressHeader = getAddressHeader("Sender");
        if (addressHeader == null || addressHeader.length == 0) {
            return null;
        }
        return addressHeader[0];
    }

    @Override // javax.mail.Message
    public Date getSentDate() throws MessagingException {
        Date parse;
        String header = getHeader("Date", null);
        if (header == null) {
            return null;
        }
        try {
            synchronized (mailDateFormat) {
                parse = mailDateFormat.parse(header);
            }
            return parse;
        } catch (java.text.ParseException unused) {
            return null;
        }
    }

    @Override // javax.mail.Part
    public int getSize() throws MessagingException {
        if (this.content != null) {
            return this.content.length;
        }
        if (this.contentStream == null) {
            return -1;
        }
        try {
            int available = this.contentStream.available();
            if (available > 0) {
                return available;
            }
            return -1;
        } catch (IOException unused) {
            return -1;
        }
    }

    @Override // javax.mail.Message
    public String getSubject() throws MessagingException {
        String header = getHeader("Subject", null);
        if (header == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(header));
        } catch (UnsupportedEncodingException unused) {
            return header;
        }
    }

    @Override // javax.mail.Part
    public boolean isMimeType(String str) throws MessagingException {
        return MimeBodyPart.isMimeType(this, str);
    }

    @Override // javax.mail.Message
    public synchronized boolean isSet(Flags.Flag flag) throws MessagingException {
        return this.flags.contains(flag);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public void parse(InputStream inputStream) throws MessagingException {
        boolean z = inputStream instanceof ByteArrayInputStream;
        InputStream inputStream2 = inputStream;
        if (!z) {
            boolean z2 = inputStream instanceof BufferedInputStream;
            inputStream2 = inputStream;
            if (!z2) {
                boolean z3 = inputStream instanceof SharedInputStream;
                inputStream2 = inputStream;
                if (!z3) {
                    inputStream2 = new BufferedInputStream(inputStream);
                }
            }
        }
        this.headers = createInternetHeaders(inputStream2);
        if (inputStream2 instanceof SharedInputStream) {
            SharedInputStream sharedInputStream = (SharedInputStream) inputStream2;
            this.contentStream = sharedInputStream.newStream(sharedInputStream.getPosition(), -1L);
        } else {
            try {
                this.content = ASCIIUtility.getBytes(inputStream2);
            } catch (IOException e) {
                throw new MessagingException("IOException", e);
            }
        }
        this.modified = false;
    }

    @Override // javax.mail.Part
    public void removeHeader(String str) throws MessagingException {
        this.headers.removeHeader(str);
    }

    @Override // javax.mail.Message
    public Message reply(boolean z) throws MessagingException {
        return reply(z, true);
    }

    public Message reply(boolean z, boolean z2) throws MessagingException {
        MimeMessage createMimeMessage = createMimeMessage(this.session);
        String header = getHeader("Subject", null);
        if (header != null) {
            if (!header.regionMatches(true, 0, "Re: ", 0, 4)) {
                header = "Re: " + header;
            }
            createMimeMessage.setHeader("Subject", header);
        }
        Address[] replyTo = getReplyTo();
        createMimeMessage.setRecipients(Message.RecipientType.TO, replyTo);
        if (z) {
            ArrayList arrayList = new ArrayList();
            InternetAddress localAddress = InternetAddress.getLocalAddress(this.session);
            if (localAddress != null) {
                arrayList.add(localAddress);
            }
            String property = this.session != null ? this.session.getProperty("mail.alternates") : null;
            if (property != null) {
                eliminateDuplicates(arrayList, InternetAddress.parse(property, false));
            }
            boolean booleanSessionProperty = this.session != null ? PropUtil.getBooleanSessionProperty(this.session, "mail.replyallcc", false) : false;
            eliminateDuplicates(arrayList, replyTo);
            Address[] eliminateDuplicates = eliminateDuplicates(arrayList, getRecipients(Message.RecipientType.TO));
            if (eliminateDuplicates != null && eliminateDuplicates.length > 0) {
                if (booleanSessionProperty) {
                    createMimeMessage.addRecipients(Message.RecipientType.CC, eliminateDuplicates);
                } else {
                    createMimeMessage.addRecipients(Message.RecipientType.TO, eliminateDuplicates);
                }
            }
            Address[] eliminateDuplicates2 = eliminateDuplicates(arrayList, getRecipients(Message.RecipientType.CC));
            if (eliminateDuplicates2 != null && eliminateDuplicates2.length > 0) {
                createMimeMessage.addRecipients(Message.RecipientType.CC, eliminateDuplicates2);
            }
            Address[] recipients = getRecipients(RecipientType.NEWSGROUPS);
            if (recipients != null && recipients.length > 0) {
                createMimeMessage.setRecipients(RecipientType.NEWSGROUPS, recipients);
            }
        }
        String header2 = getHeader("Message-Id", null);
        if (header2 != null) {
            createMimeMessage.setHeader("In-Reply-To", header2);
        }
        String header3 = getHeader("References", SQLBuilder.BLANK);
        if (header3 == null) {
            header3 = getHeader("In-Reply-To", SQLBuilder.BLANK);
        }
        if (header2 == null) {
            header2 = header3;
        } else if (header3 != null) {
            header2 = MimeUtility.unfold(header3) + SQLBuilder.BLANK + header2;
        }
        if (header2 != null) {
            createMimeMessage.setHeader("References", MimeUtility.fold(12, header2));
        }
        if (z2) {
            try {
                setFlags(answeredFlag, true);
            } catch (MessagingException unused) {
            }
        }
        return createMimeMessage;
    }

    @Override // javax.mail.Message
    public void saveChanges() throws MessagingException {
        this.modified = true;
        this.saved = true;
        updateHeaders();
    }

    @Override // javax.mail.Part
    public void setContent(Object obj, String str) throws MessagingException {
        if (obj instanceof Multipart) {
            setContent((Multipart) obj);
        } else {
            setDataHandler(new DataHandler(obj, str));
        }
    }

    @Override // javax.mail.Part
    public void setContent(Multipart multipart) throws MessagingException {
        setDataHandler(new DataHandler(multipart, multipart.getContentType()));
        multipart.setParent(this);
    }

    public void setContentID(String str) throws MessagingException {
        if (str == null) {
            removeHeader("Content-ID");
        } else {
            setHeader("Content-ID", str);
        }
    }

    public void setContentLanguage(String[] strArr) throws MessagingException {
        MimeBodyPart.setContentLanguage(this, strArr);
    }

    public void setContentMD5(String str) throws MessagingException {
        setHeader("Content-MD5", str);
    }

    @Override // javax.mail.Part
    public synchronized void setDataHandler(DataHandler dataHandler) throws MessagingException {
        this.dh = dataHandler;
        this.cachedContent = null;
        MimeBodyPart.invalidateContentHeaders(this);
    }

    @Override // javax.mail.Part
    public void setDescription(String str) throws MessagingException {
        setDescription(str, null);
    }

    public void setDescription(String str, String str2) throws MessagingException {
        MimeBodyPart.setDescription(this, str, str2);
    }

    @Override // javax.mail.Part
    public void setDisposition(String str) throws MessagingException {
        MimeBodyPart.setDisposition(this, str);
    }

    @Override // javax.mail.Part
    public void setFileName(String str) throws MessagingException {
        MimeBodyPart.setFileName(this, str);
    }

    @Override // javax.mail.Message
    public synchronized void setFlags(Flags flags, boolean z) throws MessagingException {
        try {
            if (z) {
                this.flags.add(flags);
            } else {
                this.flags.remove(flags);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // javax.mail.Message
    public void setFrom() throws MessagingException {
        try {
            InternetAddress _getLocalAddress = InternetAddress._getLocalAddress(this.session);
            if (_getLocalAddress == null) {
                throw new MessagingException("No From address");
            }
            setFrom(_getLocalAddress);
        } catch (Exception e) {
            throw new MessagingException("No From address", e);
        }
    }

    public void setFrom(String str) throws MessagingException {
        if (str == null) {
            removeHeader("From");
        } else {
            setAddressHeader("From", InternetAddress.parse(str));
        }
    }

    @Override // javax.mail.Message
    public void setFrom(Address address) throws MessagingException {
        if (address == null) {
            removeHeader("From");
        } else {
            setHeader("From", MimeUtility.fold(6, address.toString()));
        }
    }

    @Override // javax.mail.Part
    public void setHeader(String str, String str2) throws MessagingException {
        this.headers.setHeader(str, str2);
    }

    public void setRecipients(Message.RecipientType recipientType, String str) throws MessagingException {
        if (recipientType != RecipientType.NEWSGROUPS) {
            setAddressHeader(getHeaderName(recipientType), str == null ? null : InternetAddress.parse(str));
        } else if (str == null || str.length() == 0) {
            removeHeader("Newsgroups");
        } else {
            setHeader("Newsgroups", str);
        }
    }

    @Override // javax.mail.Message
    public void setRecipients(Message.RecipientType recipientType, Address[] addressArr) throws MessagingException {
        if (recipientType != RecipientType.NEWSGROUPS) {
            setAddressHeader(getHeaderName(recipientType), addressArr);
        } else if (addressArr == null || addressArr.length == 0) {
            removeHeader("Newsgroups");
        } else {
            setHeader("Newsgroups", NewsAddress.toString(addressArr));
        }
    }

    @Override // javax.mail.Message
    public void setReplyTo(Address[] addressArr) throws MessagingException {
        setAddressHeader("Reply-To", addressArr);
    }

    public void setSender(Address address) throws MessagingException {
        if (address == null) {
            removeHeader("Sender");
        } else {
            setHeader("Sender", MimeUtility.fold(8, address.toString()));
        }
    }

    @Override // javax.mail.Message
    public void setSentDate(Date date) throws MessagingException {
        if (date == null) {
            removeHeader("Date");
            return;
        }
        synchronized (mailDateFormat) {
            setHeader("Date", mailDateFormat.format(date));
        }
    }

    @Override // javax.mail.Message
    public void setSubject(String str) throws MessagingException {
        setSubject(str, null);
    }

    public void setSubject(String str, String str2) throws MessagingException {
        if (str == null) {
            removeHeader("Subject");
            return;
        }
        try {
            setHeader("Subject", MimeUtility.fold(9, MimeUtility.encodeText(str, str2, null)));
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Encoding error", e);
        }
    }

    @Override // javax.mail.Part, javax.mail.internet.MimePart
    public void setText(String str) throws MessagingException {
        setText(str, null);
    }

    @Override // javax.mail.internet.MimePart
    public void setText(String str, String str2) throws MessagingException {
        MimeBodyPart.setText(this, str, str2, "plain");
    }

    @Override // javax.mail.internet.MimePart
    public void setText(String str, String str2, String str3) throws MessagingException {
        MimeBodyPart.setText(this, str, str2, str3);
    }

    protected synchronized void updateHeaders() throws MessagingException {
        MimeBodyPart.updateHeaders(this);
        setHeader("MIME-Version", "1.0");
        if (getHeader("Date") == null) {
            setSentDate(new Date());
        }
        updateMessageID();
        if (this.cachedContent != null) {
            this.dh = new DataHandler(this.cachedContent, getContentType());
            this.cachedContent = null;
            this.content = null;
            if (this.contentStream != null) {
                try {
                    this.contentStream.close();
                } catch (IOException unused) {
                }
            }
            this.contentStream = null;
        }
    }

    protected void updateMessageID() throws MessagingException {
        setHeader("Message-ID", "<" + UniqueValue.getUniqueMessageIDValue(this.session) + ">");
    }

    @Override // javax.mail.Part
    public void writeTo(OutputStream outputStream) throws IOException, MessagingException {
        writeTo(outputStream, null);
    }

    public void writeTo(OutputStream outputStream, String[] strArr) throws IOException, MessagingException {
        InputStream inputStream;
        if (!this.saved) {
            saveChanges();
        }
        if (this.modified) {
            MimeBodyPart.writeTo(this, outputStream, strArr);
            return;
        }
        Enumeration<String> nonMatchingHeaderLines = getNonMatchingHeaderLines(strArr);
        LineOutputStream lineOutputStream = new LineOutputStream(outputStream, this.allowutf8);
        while (nonMatchingHeaderLines.hasMoreElements()) {
            lineOutputStream.writeln(nonMatchingHeaderLines.nextElement());
        }
        lineOutputStream.writeln();
        if (this.content == null) {
            byte[] bArr = new byte[8192];
            try {
                inputStream = getContentStream();
                while (true) {
                    try {
                        int read = inputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        } else {
                            outputStream.write(bArr, 0, read);
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th;
                    }
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = null;
            }
        } else {
            outputStream.write(this.content);
        }
        outputStream.flush();
    }
}
