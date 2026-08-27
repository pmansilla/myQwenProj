package myjava.awt.datatransfer;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import myjava.awt.datatransfer.MimeTypeProcessor;
import org.apache.harmony.awt.datatransfer.DTK;
import org.apache.harmony.awt.datatransfer.DataProvider;
import org.apache.harmony.awt.internal.nls.Messages;

/* loaded from: classes2.dex */
public class DataFlavor implements Externalizable, Cloneable {
    public static final String javaJVMLocalObjectMimeType = "application/x-java-jvm-local-objectref";
    public static final String javaRemoteObjectMimeType = "application/x-java-remote-object";
    public static final String javaSerializedObjectMimeType = "application/x-java-serialized-object";
    private static final long serialVersionUID = 8367026044764648243L;
    private String humanPresentableName;
    private MimeTypeProcessor.MimeType mimeInfo;
    private Class<?> representationClass;

    @Deprecated
    public static final DataFlavor plainTextFlavor = new DataFlavor("text/plain; charset=unicode; class=java.io.InputStream", "Plain Text");
    public static final DataFlavor stringFlavor = new DataFlavor("application/x-java-serialized-object; class=java.lang.String", "Unicode String");
    public static final DataFlavor javaFileListFlavor = new DataFlavor("application/x-java-file-list; class=java.util.List", "application/x-java-file-list");
    private static final String[] sortedTextFlavors = {"text/sgml", "text/xml", "text/html", "text/rtf", "text/enriched", "text/richtext", DataProvider.TYPE_URILIST, "text/tab-separated-values", "text/t140", "text/rfc822-headers", "text/parityfec", "text/directory", "text/css", "text/calendar", "application/x-java-serialized-object", "text/plain"};
    private static DataFlavor plainUnicodeFlavor = null;

    public DataFlavor() {
        this.mimeInfo = null;
        this.humanPresentableName = null;
        this.representationClass = null;
    }

    public DataFlavor(Class<?> cls, String str) {
        this.mimeInfo = new MimeTypeProcessor.MimeType("application", "x-java-serialized-object");
        if (str != null) {
            this.humanPresentableName = str;
        } else {
            this.humanPresentableName = "application/x-java-serialized-object";
        }
        this.mimeInfo.addParameter("class", cls.getName());
        this.representationClass = cls;
    }

    public DataFlavor(String str) throws ClassNotFoundException {
        init(str, null, null);
    }

    public DataFlavor(String str, String str2) {
        try {
            init(str, str2, null);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(Messages.getString("awt.16C", this.mimeInfo.getParameter("class")), e);
        }
    }

    public DataFlavor(String str, String str2, ClassLoader classLoader) throws ClassNotFoundException {
        init(str, str2, classLoader);
    }

    private static List<DataFlavor> fetchTextFlavors(List<DataFlavor> list, String str) {
        LinkedList linkedList = new LinkedList();
        Iterator<DataFlavor> it = list.iterator();
        while (it.hasNext()) {
            DataFlavor next = it.next();
            if (!next.isFlavorTextType()) {
                it.remove();
            } else if (next.mimeInfo.getFullType().equals(str)) {
                if (!linkedList.contains(next)) {
                    linkedList.add(next);
                }
                it.remove();
            }
        }
        if (linkedList.isEmpty()) {
            return null;
        }
        return linkedList;
    }

    private String getCharset() {
        if (this.mimeInfo == null || isCharsetRedundant()) {
            return "";
        }
        String parameter = this.mimeInfo.getParameter("charset");
        return (isCharsetRequired() && (parameter == null || parameter.length() == 0)) ? DTK.getDTK().getDefaultCharset() : parameter == null ? "" : parameter;
    }

    private static List<DataFlavor> getFlavors(List<DataFlavor> list, Class<?> cls) {
        LinkedList linkedList = new LinkedList();
        for (DataFlavor dataFlavor : list) {
            if (dataFlavor.representationClass.equals(cls)) {
                linkedList.add(dataFlavor);
            }
        }
        if (linkedList.isEmpty()) {
            return null;
        }
        return list;
    }

    private static List<DataFlavor> getFlavors(List<DataFlavor> list, String[] strArr) {
        LinkedList linkedList = new LinkedList();
        Iterator<DataFlavor> it = list.iterator();
        while (it.hasNext()) {
            DataFlavor next = it.next();
            if (isCharsetSupported(next.getCharset())) {
                for (String str : strArr) {
                    if (Charset.forName(str).equals(Charset.forName(next.getCharset()))) {
                        linkedList.add(next);
                    }
                }
            } else {
                it.remove();
            }
        }
        if (linkedList.isEmpty()) {
            return null;
        }
        return list;
    }

    private String getKeyInfo() {
        String str = String.valueOf(this.mimeInfo.getFullType()) + ";class=" + this.representationClass.getName();
        if (!this.mimeInfo.getPrimaryType().equals("text") || isUnicodeFlavor()) {
            return str;
        }
        return String.valueOf(str) + ";charset=" + getCharset().toLowerCase();
    }

    public static final DataFlavor getTextPlainUnicodeFlavor() {
        if (plainUnicodeFlavor == null) {
            plainUnicodeFlavor = new DataFlavor("text/plain; charset=" + DTK.getDTK().getDefaultCharset() + "; class=java.io.InputStream", "Plain Text");
        }
        return plainUnicodeFlavor;
    }

    private void init(String str, String str2, ClassLoader classLoader) throws ClassNotFoundException {
        try {
            this.mimeInfo = MimeTypeProcessor.parse(str);
            if (str2 != null) {
                this.humanPresentableName = str2;
            } else {
                this.humanPresentableName = String.valueOf(this.mimeInfo.getPrimaryType()) + '/' + this.mimeInfo.getSubType();
            }
            String parameter = this.mimeInfo.getParameter("class");
            if (parameter == null) {
                parameter = "java.io.InputStream";
                this.mimeInfo.addParameter("class", "java.io.InputStream");
            }
            this.representationClass = classLoader == null ? Class.forName(parameter) : classLoader.loadClass(parameter);
        } catch (IllegalArgumentException unused) {
            throw new IllegalArgumentException(Messages.getString("awt.16D", str));
        }
    }

    private boolean isByteCodeFlavor() {
        if (this.representationClass != null) {
            return this.representationClass.equals(InputStream.class) || this.representationClass.equals(ByteBuffer.class) || this.representationClass.equals(byte[].class);
        }
        return false;
    }

    private boolean isCharsetRedundant() {
        String fullType = this.mimeInfo.getFullType();
        return fullType.equals("text/rtf") || fullType.equals("text/tab-separated-values") || fullType.equals("text/t140") || fullType.equals("text/rfc822-headers") || fullType.equals("text/parityfec");
    }

    private boolean isCharsetRequired() {
        String fullType = this.mimeInfo.getFullType();
        return fullType.equals("text/sgml") || fullType.equals("text/xml") || fullType.equals("text/html") || fullType.equals("text/enriched") || fullType.equals("text/richtext") || fullType.equals(DataProvider.TYPE_URILIST) || fullType.equals("text/directory") || fullType.equals("text/css") || fullType.equals("text/calendar") || fullType.equals("application/x-java-serialized-object") || fullType.equals("text/plain");
    }

    private static boolean isCharsetSupported(String str) {
        try {
            return Charset.isSupported(str);
        } catch (IllegalCharsetNameException unused) {
            return false;
        }
    }

    private boolean isUnicodeFlavor() {
        if (this.representationClass != null) {
            return this.representationClass.equals(Reader.class) || this.representationClass.equals(String.class) || this.representationClass.equals(CharBuffer.class) || this.representationClass.equals(char[].class);
        }
        return false;
    }

    private static List<DataFlavor> selectBestByAlphabet(List<DataFlavor> list) {
        String[] strArr = new String[list.size()];
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = list.get(i).getCharset();
        }
        Arrays.sort(strArr, String.CASE_INSENSITIVE_ORDER);
        for (DataFlavor dataFlavor : list) {
            if (strArr[0].equalsIgnoreCase(dataFlavor.getCharset())) {
                linkedList.add(dataFlavor);
            }
        }
        if (linkedList.isEmpty()) {
            return null;
        }
        return linkedList;
    }

    private static DataFlavor selectBestByCharset(List<DataFlavor> list) {
        List<DataFlavor> flavors = getFlavors(list, new String[]{"UTF-16", "UTF-8", "UTF-16BE", "UTF-16LE"});
        if (flavors == null && (flavors = getFlavors(list, new String[]{DTK.getDTK().getDefaultCharset()})) == null && (flavors = getFlavors(list, new String[]{"US-ASCII"})) == null) {
            flavors = selectBestByAlphabet(list);
        }
        if (flavors != null) {
            return flavors.size() == 1 ? flavors.get(0) : selectBestFlavorWOCharset(flavors);
        }
        return null;
    }

    private static DataFlavor selectBestFlavorWCharset(List<DataFlavor> list) {
        List<DataFlavor> flavors = getFlavors(list, (Class<?>) Reader.class);
        if (flavors != null) {
            return flavors.get(0);
        }
        List<DataFlavor> flavors2 = getFlavors(list, (Class<?>) String.class);
        if (flavors2 != null) {
            return flavors2.get(0);
        }
        List<DataFlavor> flavors3 = getFlavors(list, (Class<?>) CharBuffer.class);
        if (flavors3 != null) {
            return flavors3.get(0);
        }
        List<DataFlavor> flavors4 = getFlavors(list, (Class<?>) char[].class);
        return flavors4 != null ? flavors4.get(0) : selectBestByCharset(list);
    }

    private static DataFlavor selectBestFlavorWOCharset(List<DataFlavor> list) {
        List<DataFlavor> flavors = getFlavors(list, (Class<?>) InputStream.class);
        if (flavors != null) {
            return flavors.get(0);
        }
        List<DataFlavor> flavors2 = getFlavors(list, (Class<?>) ByteBuffer.class);
        if (flavors2 != null) {
            return flavors2.get(0);
        }
        List<DataFlavor> flavors3 = getFlavors(list, (Class<?>) byte[].class);
        return flavors3 != null ? flavors3.get(0) : list.get(0);
    }

    public static final DataFlavor selectBestTextFlavor(DataFlavor[] dataFlavorArr) {
        if (dataFlavorArr == null) {
            return null;
        }
        List<List<DataFlavor>> sortTextFlavorsByType = sortTextFlavorsByType(new LinkedList(Arrays.asList(dataFlavorArr)));
        if (sortTextFlavorsByType.isEmpty()) {
            return null;
        }
        List<DataFlavor> list = sortTextFlavorsByType.get(0);
        return list.size() == 1 ? list.get(0) : list.get(0).getCharset().length() == 0 ? selectBestFlavorWOCharset(list) : selectBestFlavorWCharset(list);
    }

    private static List<List<DataFlavor>> sortTextFlavorsByType(List<DataFlavor> list) {
        LinkedList linkedList = new LinkedList();
        for (String str : sortedTextFlavors) {
            List<DataFlavor> fetchTextFlavors = fetchTextFlavors(list, str);
            if (fetchTextFlavors != null) {
                linkedList.addLast(fetchTextFlavors);
            }
        }
        if (!list.isEmpty()) {
            linkedList.addLast(list);
        }
        return linkedList;
    }

    protected static final Class<?> tryToLoadClass(String str, ClassLoader classLoader) throws ClassNotFoundException {
        try {
            try {
                return Class.forName(str);
            } catch (ClassNotFoundException unused) {
                return ClassLoader.getSystemClassLoader().loadClass(str);
            }
        } catch (ClassNotFoundException unused2) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                try {
                    return contextClassLoader.loadClass(str);
                } catch (ClassNotFoundException unused3) {
                    return classLoader.loadClass(str);
                }
            }
            return classLoader.loadClass(str);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        DataFlavor dataFlavor = new DataFlavor();
        dataFlavor.humanPresentableName = this.humanPresentableName;
        dataFlavor.representationClass = this.representationClass;
        dataFlavor.mimeInfo = this.mimeInfo != null ? (MimeTypeProcessor.MimeType) this.mimeInfo.clone() : null;
        return dataFlavor;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DataFlavor)) {
            return false;
        }
        return equals((DataFlavor) obj);
    }

    @Deprecated
    public boolean equals(String str) {
        if (str == null) {
            return false;
        }
        return isMimeTypeEqual(str);
    }

    public boolean equals(DataFlavor dataFlavor) {
        if (dataFlavor == this) {
            return true;
        }
        if (dataFlavor == null) {
            return false;
        }
        if (this.mimeInfo == null) {
            return dataFlavor.mimeInfo == null;
        }
        if (!this.mimeInfo.equals(dataFlavor.mimeInfo) || !this.representationClass.equals(dataFlavor.representationClass)) {
            return false;
        }
        if (!this.mimeInfo.getPrimaryType().equals("text") || isUnicodeFlavor()) {
            return true;
        }
        String charset = getCharset();
        String charset2 = dataFlavor.getCharset();
        return (isCharsetSupported(charset) && isCharsetSupported(charset2)) ? Charset.forName(charset).equals(Charset.forName(charset2)) : charset.equalsIgnoreCase(charset2);
    }

    public final Class<?> getDefaultRepresentationClass() {
        return InputStream.class;
    }

    public final String getDefaultRepresentationClassAsString() {
        return getDefaultRepresentationClass().getName();
    }

    public String getHumanPresentableName() {
        return this.humanPresentableName;
    }

    MimeTypeProcessor.MimeType getMimeInfo() {
        return this.mimeInfo;
    }

    public String getMimeType() {
        if (this.mimeInfo != null) {
            return MimeTypeProcessor.assemble(this.mimeInfo);
        }
        return null;
    }

    public String getParameter(String str) {
        String lowerCase = str.toLowerCase();
        if (lowerCase.equals("humanpresentablename")) {
            return this.humanPresentableName;
        }
        if (this.mimeInfo != null) {
            return this.mimeInfo.getParameter(lowerCase);
        }
        return null;
    }

    public String getPrimaryType() {
        if (this.mimeInfo != null) {
            return this.mimeInfo.getPrimaryType();
        }
        return null;
    }

    public Reader getReaderForText(Transferable transferable) throws UnsupportedFlavorException, IOException {
        InputStream byteArrayInputStream;
        Object transferData = transferable.getTransferData(this);
        if (transferData == null) {
            throw new IllegalArgumentException(Messages.getString("awt.16E"));
        }
        if (transferData instanceof Reader) {
            Reader reader = (Reader) transferData;
            reader.reset();
            return reader;
        }
        if (transferData instanceof String) {
            return new StringReader((String) transferData);
        }
        if (transferData instanceof CharBuffer) {
            return new CharArrayReader(((CharBuffer) transferData).array());
        }
        if (transferData instanceof char[]) {
            return new CharArrayReader((char[]) transferData);
        }
        String charset = getCharset();
        if (transferData instanceof InputStream) {
            InputStream inputStream = (InputStream) transferData;
            inputStream.reset();
            byteArrayInputStream = inputStream;
        } else if (transferData instanceof ByteBuffer) {
            byteArrayInputStream = new ByteArrayInputStream(((ByteBuffer) transferData).array());
        } else {
            if (!(transferData instanceof byte[])) {
                throw new IllegalArgumentException(Messages.getString("awt.16F"));
            }
            byteArrayInputStream = new ByteArrayInputStream((byte[]) transferData);
        }
        return charset.length() == 0 ? new InputStreamReader(byteArrayInputStream) : new InputStreamReader(byteArrayInputStream, charset);
    }

    public Class<?> getRepresentationClass() {
        return this.representationClass;
    }

    public String getSubType() {
        if (this.mimeInfo != null) {
            return this.mimeInfo.getSubType();
        }
        return null;
    }

    public int hashCode() {
        return getKeyInfo().hashCode();
    }

    public boolean isFlavorJavaFileListType() {
        return List.class.isAssignableFrom(this.representationClass) && isMimeTypeEqual(javaFileListFlavor);
    }

    public boolean isFlavorRemoteObjectType() {
        return isMimeTypeEqual(javaRemoteObjectMimeType) && isRepresentationClassRemote();
    }

    public boolean isFlavorSerializedObjectType() {
        return isMimeTypeSerializedObject() && isRepresentationClassSerializable();
    }

    public boolean isFlavorTextType() {
        if (equals(stringFlavor) || equals(plainTextFlavor)) {
            return true;
        }
        if (this.mimeInfo != null && !this.mimeInfo.getPrimaryType().equals("text")) {
            return false;
        }
        String charset = getCharset();
        if (!isByteCodeFlavor()) {
            return isUnicodeFlavor();
        }
        if (charset.length() != 0) {
            return isCharsetSupported(charset);
        }
        return true;
    }

    public boolean isMimeTypeEqual(String str) {
        try {
            return this.mimeInfo.equals(MimeTypeProcessor.parse(str));
        } catch (IllegalArgumentException unused) {
            return false;
        }
    }

    public final boolean isMimeTypeEqual(DataFlavor dataFlavor) {
        return this.mimeInfo != null ? this.mimeInfo.equals(dataFlavor.mimeInfo) : dataFlavor.mimeInfo == null;
    }

    public boolean isMimeTypeSerializedObject() {
        return isMimeTypeEqual("application/x-java-serialized-object");
    }

    public boolean isRepresentationClassByteBuffer() {
        return ByteBuffer.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassCharBuffer() {
        return CharBuffer.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassInputStream() {
        return InputStream.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassReader() {
        return Reader.class.isAssignableFrom(this.representationClass);
    }

    public boolean isRepresentationClassRemote() {
        return false;
    }

    public boolean isRepresentationClassSerializable() {
        return Serializable.class.isAssignableFrom(this.representationClass);
    }

    public boolean match(DataFlavor dataFlavor) {
        return equals(dataFlavor);
    }

    @Deprecated
    protected String normalizeMimeType(String str) {
        return str;
    }

    @Deprecated
    protected String normalizeMimeTypeParameter(String str, String str2) {
        return str2;
    }

    @Override // java.io.Externalizable
    public synchronized void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.humanPresentableName = (String) objectInput.readObject();
        this.mimeInfo = (MimeTypeProcessor.MimeType) objectInput.readObject();
        this.representationClass = this.mimeInfo != null ? Class.forName(this.mimeInfo.getParameter("class")) : null;
    }

    public void setHumanPresentableName(String str) {
        this.humanPresentableName = str;
    }

    public String toString() {
        return String.valueOf(getClass().getName()) + "[MimeType=(" + getMimeType() + ");humanPresentableName=" + this.humanPresentableName + "]";
    }

    @Override // java.io.Externalizable
    public synchronized void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.humanPresentableName);
        objectOutput.writeObject(this.mimeInfo);
    }
}
