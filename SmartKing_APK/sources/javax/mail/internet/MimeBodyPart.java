package javax.mail.internet;

import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.FolderClosedIOException;
import com.sun.mail.util.LineOutputStream;
import com.sun.mail.util.MessageRemovedIOException;
import com.sun.mail.util.MimeUtil;
import com.sun.mail.util.PropUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.EncodingAware;
import javax.mail.FolderClosedException;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.HeaderTokenizer;

/* loaded from: classes2.dex */
public class MimeBodyPart extends BodyPart implements MimePart {
    protected Object cachedContent;
    protected byte[] content;
    protected InputStream contentStream;
    protected DataHandler dh;
    protected InternetHeaders headers;
    private static final boolean setDefaultTextCharset = PropUtil.getBooleanSystemProperty("mail.mime.setdefaulttextcharset", true);
    private static final boolean setContentTypeFileName = PropUtil.getBooleanSystemProperty("mail.mime.setcontenttypefilename", true);
    private static final boolean encodeFileName = PropUtil.getBooleanSystemProperty("mail.mime.encodefilename", false);
    private static final boolean decodeFileName = PropUtil.getBooleanSystemProperty("mail.mime.decodefilename", false);
    private static final boolean ignoreMultipartEncoding = PropUtil.getBooleanSystemProperty("mail.mime.ignoremultipartencoding", true);
    private static final boolean allowutf8 = PropUtil.getBooleanSystemProperty("mail.mime.allowutf8", true);
    static final boolean cacheMultipart = PropUtil.getBooleanSystemProperty("mail.mime.cachemultipart", true);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class EncodedFileDataSource extends FileDataSource implements EncodingAware {
        private String contentType;
        private String encoding;

        public EncodedFileDataSource(File file, String str, String str2) {
            super(file);
            this.contentType = str;
            this.encoding = str2;
        }

        @Override // javax.activation.FileDataSource, javax.activation.DataSource
        public String getContentType() {
            return this.contentType != null ? this.contentType : super.getContentType();
        }

        @Override // javax.mail.EncodingAware
        public String getEncoding() {
            return this.encoding;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class MimePartDataHandler extends DataHandler {
        MimePart part;

        public MimePartDataHandler(MimePart mimePart) {
            super(new MimePartDataSource(mimePart));
            this.part = mimePart;
        }

        InputStream getContentStream() throws MessagingException {
            if (this.part instanceof MimeBodyPart) {
                return ((MimeBodyPart) this.part).getContentStream();
            }
            if (this.part instanceof MimeMessage) {
                return ((MimeMessage) this.part).getContentStream();
            }
            return null;
        }

        MimePart getPart() {
            return this.part;
        }
    }

    public MimeBodyPart() {
        this.headers = new InternetHeaders();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MimeBodyPart(InputStream inputStream) throws MessagingException {
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
        this.headers = new InternetHeaders(inputStream2);
        if (inputStream2 instanceof SharedInputStream) {
            SharedInputStream sharedInputStream = (SharedInputStream) inputStream2;
            this.contentStream = sharedInputStream.newStream(sharedInputStream.getPosition(), -1L);
        } else {
            try {
                this.content = ASCIIUtility.getBytes(inputStream2);
            } catch (IOException e) {
                throw new MessagingException("Error reading input stream", e);
            }
        }
    }

    public MimeBodyPart(InternetHeaders internetHeaders, byte[] bArr) throws MessagingException {
        this.headers = internetHeaders;
        this.content = bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] getContentLanguage(MimePart mimePart) throws MessagingException {
        String header = mimePart.getHeader("Content-Language", null);
        if (header == null) {
            return null;
        }
        HeaderTokenizer headerTokenizer = new HeaderTokenizer(header, HeaderTokenizer.MIME);
        ArrayList arrayList = new ArrayList();
        while (true) {
            HeaderTokenizer.Token next = headerTokenizer.next();
            int type = next.getType();
            if (type == -4) {
                break;
            }
            if (type == -1) {
                arrayList.add(next.getValue());
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getDescription(MimePart mimePart) throws MessagingException {
        String header = mimePart.getHeader("Content-Description", null);
        if (header == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(header));
        } catch (UnsupportedEncodingException unused) {
            return header;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getDisposition(MimePart mimePart) throws MessagingException {
        String header = mimePart.getHeader("Content-Disposition", null);
        if (header == null) {
            return null;
        }
        return new ContentDisposition(header).getDisposition();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getEncoding(MimePart mimePart) throws MessagingException {
        HeaderTokenizer.Token next;
        int type;
        String header = mimePart.getHeader("Content-Transfer-Encoding", null);
        if (header == null) {
            return null;
        }
        String trim = header.trim();
        if (trim.equalsIgnoreCase("7bit") || trim.equalsIgnoreCase("8bit") || trim.equalsIgnoreCase("quoted-printable") || trim.equalsIgnoreCase("binary") || trim.equalsIgnoreCase("base64")) {
            return trim;
        }
        HeaderTokenizer headerTokenizer = new HeaderTokenizer(trim, HeaderTokenizer.MIME);
        do {
            next = headerTokenizer.next();
            type = next.getType();
            if (type == -4) {
                return trim;
            }
        } while (type != -1);
        return next.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getFileName(MimePart mimePart) throws MessagingException {
        String str;
        String cleanContentType;
        String header = mimePart.getHeader("Content-Disposition", null);
        String parameter = header != null ? new ContentDisposition(header).getParameter(FileDownloadModel.FILENAME) : null;
        if (parameter == null && (cleanContentType = MimeUtil.cleanContentType(mimePart, mimePart.getHeader("Content-Type", null))) != null) {
            try {
                str = new ContentType(cleanContentType).getParameter(IMAPStore.ID_NAME);
            } catch (ParseException unused) {
            }
            if (!decodeFileName && str != null) {
                try {
                    return MimeUtility.decodeText(str);
                } catch (UnsupportedEncodingException e) {
                    throw new MessagingException("Can't decode filename", e);
                }
            }
        }
        str = parameter;
        return !decodeFileName ? str : str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invalidateContentHeaders(MimePart mimePart) throws MessagingException {
        mimePart.removeHeader("Content-Type");
        mimePart.removeHeader("Content-Transfer-Encoding");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isMimeType(MimePart mimePart, String str) throws MessagingException {
        String contentType = mimePart.getContentType();
        try {
            return new ContentType(contentType).match(str);
        } catch (ParseException unused) {
            try {
                int indexOf = contentType.indexOf(59);
                if (indexOf > 0) {
                    return new ContentType(contentType.substring(0, indexOf)).match(str);
                }
            } catch (ParseException unused2) {
            }
            return contentType.equalsIgnoreCase(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String restrictEncoding(MimePart mimePart, String str) throws MessagingException {
        String contentType;
        ContentType contentType2;
        if (!ignoreMultipartEncoding || str == null || str.equalsIgnoreCase("7bit") || str.equalsIgnoreCase("8bit") || str.equalsIgnoreCase("binary") || (contentType = mimePart.getContentType()) == null) {
            return str;
        }
        try {
            contentType2 = new ContentType(contentType);
        } catch (ParseException unused) {
        }
        if (contentType2.match("multipart/*")) {
            return null;
        }
        if (contentType2.match("message/*")) {
            if (!PropUtil.getBooleanSystemProperty("mail.mime.allowencodedmessages", false)) {
                return null;
            }
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setContentLanguage(MimePart mimePart, String[] strArr) throws MessagingException {
        StringBuffer stringBuffer = new StringBuffer(strArr[0]);
        int length = "Content-Language".length() + 2 + strArr[0].length();
        for (int i = 1; i < strArr.length; i++) {
            stringBuffer.append(',');
            int i2 = length + 1;
            if (i2 > 76) {
                stringBuffer.append("\r\n\t");
                i2 = 8;
            }
            stringBuffer.append(strArr[i]);
            length = i2 + strArr[i].length();
        }
        mimePart.setHeader("Content-Language", stringBuffer.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setDescription(MimePart mimePart, String str, String str2) throws MessagingException {
        if (str == null) {
            mimePart.removeHeader("Content-Description");
            return;
        }
        try {
            mimePart.setHeader("Content-Description", MimeUtility.fold(21, MimeUtility.encodeText(str, str2, null)));
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Encoding error", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setDisposition(MimePart mimePart, String str) throws MessagingException {
        if (str == null) {
            mimePart.removeHeader("Content-Disposition");
            return;
        }
        String header = mimePart.getHeader("Content-Disposition", null);
        if (header != null) {
            ContentDisposition contentDisposition = new ContentDisposition(header);
            contentDisposition.setDisposition(str);
            str = contentDisposition.toString();
        }
        mimePart.setHeader("Content-Disposition", str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setEncoding(MimePart mimePart, String str) throws MessagingException {
        mimePart.setHeader("Content-Transfer-Encoding", str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setFileName(MimePart mimePart, String str) throws MessagingException {
        String cleanContentType;
        if (encodeFileName && str != null) {
            try {
                str = MimeUtility.encodeText(str);
            } catch (UnsupportedEncodingException e) {
                throw new MessagingException("Can't encode filename", e);
            }
        }
        String header = mimePart.getHeader("Content-Disposition", null);
        if (header == null) {
            header = Part.ATTACHMENT;
        }
        ContentDisposition contentDisposition = new ContentDisposition(header);
        String defaultMIMECharset = MimeUtility.getDefaultMIMECharset();
        ParameterList parameterList = contentDisposition.getParameterList();
        if (parameterList == null) {
            parameterList = new ParameterList();
            contentDisposition.setParameterList(parameterList);
        }
        if (encodeFileName) {
            parameterList.setLiteral(FileDownloadModel.FILENAME, str);
        } else {
            parameterList.set(FileDownloadModel.FILENAME, str, defaultMIMECharset);
        }
        mimePart.setHeader("Content-Disposition", contentDisposition.toString());
        if (!setContentTypeFileName || (cleanContentType = MimeUtil.cleanContentType(mimePart, mimePart.getHeader("Content-Type", null))) == null) {
            return;
        }
        try {
            ContentType contentType = new ContentType(cleanContentType);
            ParameterList parameterList2 = contentType.getParameterList();
            if (parameterList2 == null) {
                parameterList2 = new ParameterList();
                contentType.setParameterList(parameterList2);
            }
            if (encodeFileName) {
                parameterList2.setLiteral(IMAPStore.ID_NAME, str);
            } else {
                parameterList2.set(IMAPStore.ID_NAME, str, defaultMIMECharset);
            }
            mimePart.setHeader("Content-Type", contentType.toString());
        } catch (ParseException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setText(MimePart mimePart, String str, String str2, String str3) throws MessagingException {
        if (str2 == null) {
            str2 = MimeUtility.checkAscii(str) != 1 ? MimeUtility.getDefaultMIMECharset() : "us-ascii";
        }
        mimePart.setContent(str, "text/" + str3 + "; charset=" + MimeUtility.quote(str2, HeaderTokenizer.MIME));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void updateHeaders(MimePart mimePart) throws MessagingException {
        String header;
        String parameter;
        Object content;
        DataHandler dataHandler = mimePart.getDataHandler();
        if (dataHandler == null) {
            return;
        }
        try {
            String contentType = dataHandler.getContentType();
            boolean z = true;
            boolean z2 = mimePart.getHeader("Content-Type") == null;
            ContentType contentType2 = new ContentType(contentType);
            if (contentType2.match("multipart/*")) {
                if (mimePart instanceof MimeBodyPart) {
                    MimeBodyPart mimeBodyPart = (MimeBodyPart) mimePart;
                    content = mimeBodyPart.cachedContent != null ? mimeBodyPart.cachedContent : dataHandler.getContent();
                } else if (mimePart instanceof MimeMessage) {
                    MimeMessage mimeMessage = (MimeMessage) mimePart;
                    content = mimeMessage.cachedContent != null ? mimeMessage.cachedContent : dataHandler.getContent();
                } else {
                    content = dataHandler.getContent();
                }
                if (!(content instanceof MimeMultipart)) {
                    throw new MessagingException("MIME part of type \"" + contentType + "\" contains object of type " + content.getClass().getName() + " instead of MimeMultipart");
                }
                ((MimeMultipart) content).updateHeaders();
            } else if (!contentType2.match("message/rfc822")) {
                z = false;
            }
            if (dataHandler instanceof MimePartDataHandler) {
                MimePart part = ((MimePartDataHandler) dataHandler).getPart();
                if (part == mimePart) {
                    return;
                }
                if (z2) {
                    mimePart.setHeader("Content-Type", part.getContentType());
                }
                String encoding = part.getEncoding();
                if (encoding != null) {
                    setEncoding(mimePart, encoding);
                    return;
                }
            }
            if (!z) {
                if (mimePart.getHeader("Content-Transfer-Encoding") == null) {
                    setEncoding(mimePart, MimeUtility.getEncoding(dataHandler));
                }
                if (z2 && setDefaultTextCharset && contentType2.match("text/*") && contentType2.getParameter("charset") == null) {
                    String encoding2 = mimePart.getEncoding();
                    contentType2.setParameter("charset", (encoding2 == null || !encoding2.equalsIgnoreCase("7bit")) ? MimeUtility.getDefaultMIMECharset() : "us-ascii");
                    contentType = contentType2.toString();
                }
            }
            if (z2) {
                if (setContentTypeFileName && (header = mimePart.getHeader("Content-Disposition", null)) != null && (parameter = new ContentDisposition(header).getParameter(FileDownloadModel.FILENAME)) != null) {
                    ParameterList parameterList = contentType2.getParameterList();
                    if (parameterList == null) {
                        parameterList = new ParameterList();
                        contentType2.setParameterList(parameterList);
                    }
                    if (encodeFileName) {
                        parameterList.setLiteral(IMAPStore.ID_NAME, MimeUtility.encodeText(parameter));
                    } else {
                        parameterList.set(IMAPStore.ID_NAME, parameter, MimeUtility.getDefaultMIMECharset());
                    }
                    contentType = contentType2.toString();
                }
                mimePart.setHeader("Content-Type", contentType);
            }
        } catch (IOException e) {
            throw new MessagingException("IOException updating headers", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void writeTo(MimePart mimePart, OutputStream outputStream, String[] strArr) throws IOException, MessagingException {
        LineOutputStream lineOutputStream = outputStream instanceof LineOutputStream ? (LineOutputStream) outputStream : new LineOutputStream(outputStream, allowutf8);
        Enumeration<String> nonMatchingHeaderLines = mimePart.getNonMatchingHeaderLines(strArr);
        while (nonMatchingHeaderLines.hasMoreElements()) {
            lineOutputStream.writeln(nonMatchingHeaderLines.nextElement());
        }
        lineOutputStream.writeln();
        InputStream inputStream = null;
        try {
            DataHandler dataHandler = mimePart.getDataHandler();
            if (dataHandler instanceof MimePartDataHandler) {
                MimePartDataHandler mimePartDataHandler = (MimePartDataHandler) dataHandler;
                if (mimePartDataHandler.getPart().getEncoding() != null) {
                    inputStream = mimePartDataHandler.getContentStream();
                }
            }
            if (inputStream != null) {
                byte[] bArr = new byte[8192];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    } else {
                        outputStream.write(bArr, 0, read);
                    }
                }
            } else {
                outputStream = MimeUtility.encode(outputStream, restrictEncoding(mimePart, mimePart.getEncoding()));
                mimePart.getDataHandler().writeTo(outputStream);
            }
            outputStream.flush();
        } finally {
            if (0 != 0) {
                inputStream.close();
            }
        }
    }

    @Override // javax.mail.Part
    public void addHeader(String str, String str2) throws MessagingException {
        this.headers.addHeader(str, str2);
    }

    public void addHeaderLine(String str) throws MessagingException {
        this.headers.addHeaderLine(str);
    }

    public void attachFile(File file) throws IOException, MessagingException {
        FileDataSource fileDataSource = new FileDataSource(file);
        setDataHandler(new DataHandler(fileDataSource));
        setFileName(fileDataSource.getName());
        setDisposition(Part.ATTACHMENT);
    }

    public void attachFile(File file, String str, String str2) throws IOException, MessagingException {
        EncodedFileDataSource encodedFileDataSource = new EncodedFileDataSource(file, str, str2);
        setDataHandler(new DataHandler(encodedFileDataSource));
        setFileName(encodedFileDataSource.getName());
        setDisposition(Part.ATTACHMENT);
    }

    public void attachFile(String str) throws IOException, MessagingException {
        attachFile(new File(str));
    }

    public void attachFile(String str, String str2, String str3) throws IOException, MessagingException {
        attachFile(new File(str), str2, str3);
    }

    public Enumeration<String> getAllHeaderLines() throws MessagingException {
        return this.headers.getAllHeaderLines();
    }

    @Override // javax.mail.Part
    public Enumeration<Header> getAllHeaders() throws MessagingException {
        return this.headers.getAllHeaders();
    }

    @Override // javax.mail.Part
    public Object getContent() throws IOException, MessagingException {
        if (this.cachedContent != null) {
            return this.cachedContent;
        }
        try {
            Object content = getDataHandler().getContent();
            if (cacheMultipart && (((content instanceof Multipart) || (content instanceof Message)) && (this.content != null || this.contentStream != null))) {
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

    @Override // javax.mail.internet.MimePart
    public String[] getContentLanguage() throws MessagingException {
        return getContentLanguage(this);
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
            return new ByteArrayInputStream(this.content);
        }
        throw new MessagingException("No MimeBodyPart content");
    }

    @Override // javax.mail.Part
    public String getContentType() throws MessagingException {
        String cleanContentType = MimeUtil.cleanContentType(this, getHeader("Content-Type", null));
        return cleanContentType == null ? "text/plain" : cleanContentType;
    }

    @Override // javax.mail.Part
    public DataHandler getDataHandler() throws MessagingException {
        if (this.dh == null) {
            this.dh = new MimePartDataHandler(this);
        }
        return this.dh;
    }

    @Override // javax.mail.Part
    public String getDescription() throws MessagingException {
        return getDescription(this);
    }

    @Override // javax.mail.Part
    public String getDisposition() throws MessagingException {
        return getDisposition(this);
    }

    public String getEncoding() throws MessagingException {
        return getEncoding(this);
    }

    @Override // javax.mail.Part
    public String getFileName() throws MessagingException {
        return getFileName(this);
    }

    @Override // javax.mail.internet.MimePart
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

    @Override // javax.mail.Part
    public boolean isMimeType(String str) throws MessagingException {
        return isMimeType(this, str);
    }

    @Override // javax.mail.Part
    public void removeHeader(String str) throws MessagingException {
        this.headers.removeHeader(str);
    }

    public void saveFile(File file) throws IOException, MessagingException {
        BufferedOutputStream bufferedOutputStream;
        InputStream inputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            try {
                InputStream inputStream2 = getInputStream();
                try {
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int read = inputStream2.read(bArr);
                        if (read <= 0) {
                            break;
                        } else {
                            bufferedOutputStream.write(bArr, 0, read);
                        }
                    }
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (IOException unused) {
                        }
                    }
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException unused2) {
                    }
                } catch (Throwable th) {
                    inputStream = inputStream2;
                    th = th;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException unused3) {
                        }
                    }
                    if (bufferedOutputStream == null) {
                        throw th;
                    }
                    try {
                        bufferedOutputStream.close();
                        throw th;
                    } catch (IOException unused4) {
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedOutputStream = null;
        }
    }

    public void saveFile(String str) throws IOException, MessagingException {
        saveFile(new File(str));
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

    @Override // javax.mail.internet.MimePart
    public void setContentLanguage(String[] strArr) throws MessagingException {
        setContentLanguage(this, strArr);
    }

    public void setContentMD5(String str) throws MessagingException {
        setHeader("Content-MD5", str);
    }

    @Override // javax.mail.Part
    public void setDataHandler(DataHandler dataHandler) throws MessagingException {
        this.dh = dataHandler;
        this.cachedContent = null;
        invalidateContentHeaders(this);
    }

    @Override // javax.mail.Part
    public void setDescription(String str) throws MessagingException {
        setDescription(str, null);
    }

    public void setDescription(String str, String str2) throws MessagingException {
        setDescription(this, str, str2);
    }

    @Override // javax.mail.Part
    public void setDisposition(String str) throws MessagingException {
        setDisposition(this, str);
    }

    @Override // javax.mail.Part
    public void setFileName(String str) throws MessagingException {
        setFileName(this, str);
    }

    @Override // javax.mail.Part
    public void setHeader(String str, String str2) throws MessagingException {
        this.headers.setHeader(str, str2);
    }

    @Override // javax.mail.Part, javax.mail.internet.MimePart
    public void setText(String str) throws MessagingException {
        setText(str, null);
    }

    @Override // javax.mail.internet.MimePart
    public void setText(String str, String str2) throws MessagingException {
        setText(this, str, str2, "plain");
    }

    @Override // javax.mail.internet.MimePart
    public void setText(String str, String str2, String str3) throws MessagingException {
        setText(this, str, str2, str3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateHeaders() throws MessagingException {
        updateHeaders(this);
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

    @Override // javax.mail.Part
    public void writeTo(OutputStream outputStream) throws IOException, MessagingException {
        writeTo(this, outputStream, null);
    }
}
