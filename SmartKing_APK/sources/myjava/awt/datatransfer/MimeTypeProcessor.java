package myjava.awt.datatransfer;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import kotlin.text.Typography;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes2.dex */
final class MimeTypeProcessor {
    private static MimeTypeProcessor instance;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class MimeType implements Cloneable, Serializable {
        private static final long serialVersionUID = -6693571907475992044L;
        private Hashtable<String, String> parameters;
        private String primaryType;
        private String subType;
        private Hashtable<String, Object> systemParameters;

        MimeType() {
            this.primaryType = null;
            this.subType = null;
            this.parameters = null;
            this.systemParameters = null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public MimeType(String str, String str2) {
            this.primaryType = str;
            this.subType = str2;
            this.parameters = new Hashtable<>();
            this.systemParameters = new Hashtable<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void addParameter(String str, String str2) {
            if (str2 == null) {
                return;
            }
            if (str2.charAt(0) == '\"' && str2.charAt(str2.length() - 1) == '\"') {
                str2 = str2.substring(1, str2.length() - 2);
            }
            if (str2.length() == 0) {
                return;
            }
            this.parameters.put(str, str2);
        }

        void addSystemParameter(String str, Object obj) {
            this.systemParameters.put(str, obj);
        }

        public Object clone() {
            MimeType mimeType = new MimeType(this.primaryType, this.subType);
            mimeType.parameters = (Hashtable) this.parameters.clone();
            mimeType.systemParameters = (Hashtable) this.systemParameters.clone();
            return mimeType;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean equals(MimeType mimeType) {
            if (mimeType == null) {
                return false;
            }
            return getFullType().equals(mimeType.getFullType());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public String getFullType() {
            return String.valueOf(this.primaryType) + FileUriModel.SCHEME + this.subType;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public String getParameter(String str) {
            return this.parameters.get(str);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public String getPrimaryType() {
            return this.primaryType;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public String getSubType() {
            return this.subType;
        }

        Object getSystemParameter(String str) {
            return this.systemParameters.get(str);
        }

        void removeParameter(String str) {
            this.parameters.remove(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class StringPosition {
        int i;

        private StringPosition() {
            this.i = 0;
        }

        /* synthetic */ StringPosition(StringPosition stringPosition) {
            this();
        }
    }

    private MimeTypeProcessor() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String assemble(MimeType mimeType) {
        StringBuilder sb = new StringBuilder();
        sb.append(mimeType.getFullType());
        Enumeration keys = mimeType.parameters.keys();
        while (keys.hasMoreElements()) {
            String str = (String) keys.nextElement();
            String str2 = (String) mimeType.parameters.get(str);
            sb.append("; ");
            sb.append(str);
            sb.append("=\"");
            sb.append(str2);
            sb.append(Typography.quote);
        }
        return sb.toString();
    }

    private static int getNextMeaningfulIndex(String str, int i) {
        while (i < str.length() && !isMeaningfulChar(str.charAt(i))) {
            i++;
        }
        return i;
    }

    private static boolean isMeaningfulChar(char c) {
        return c >= '!' && c <= '~';
    }

    private static boolean isTSpecialChar(char c) {
        return c == '(' || c == ')' || c == '[' || c == ']' || c == '<' || c == '>' || c == '@' || c == ',' || c == ';' || c == ':' || c == '\\' || c == '\"' || c == '/' || c == '?' || c == '=';
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MimeType parse(String str) {
        if (instance == null) {
            instance = new MimeTypeProcessor();
        }
        MimeType mimeType = new MimeType();
        if (str != null) {
            StringPosition stringPosition = new StringPosition(null);
            retrieveType(str, mimeType, stringPosition);
            retrieveParams(str, mimeType, stringPosition);
        }
        return mimeType;
    }

    private static void retrieveParam(String str, MimeType mimeType, StringPosition stringPosition) {
        String lowerCase = retrieveToken(str, stringPosition).toLowerCase();
        stringPosition.i = getNextMeaningfulIndex(str, stringPosition.i);
        if (stringPosition.i >= str.length() || str.charAt(stringPosition.i) != '=') {
            throw new IllegalArgumentException();
        }
        stringPosition.i++;
        stringPosition.i = getNextMeaningfulIndex(str, stringPosition.i);
        if (stringPosition.i >= str.length()) {
            throw new IllegalArgumentException();
        }
        mimeType.parameters.put(lowerCase, str.charAt(stringPosition.i) == '\"' ? retrieveQuoted(str, stringPosition) : retrieveToken(str, stringPosition));
    }

    private static void retrieveParams(String str, MimeType mimeType, StringPosition stringPosition) {
        mimeType.parameters = new Hashtable();
        mimeType.systemParameters = new Hashtable();
        while (true) {
            stringPosition.i = getNextMeaningfulIndex(str, stringPosition.i);
            if (stringPosition.i >= str.length()) {
                return;
            }
            if (str.charAt(stringPosition.i) != ';') {
                throw new IllegalArgumentException();
            }
            stringPosition.i++;
            retrieveParam(str, mimeType, stringPosition);
        }
    }

    private static String retrieveQuoted(String str, StringPosition stringPosition) {
        StringBuilder sb = new StringBuilder();
        stringPosition.i++;
        boolean z = true;
        do {
            if (str.charAt(stringPosition.i) == '\"' && z) {
                stringPosition.i++;
                return sb.toString();
            }
            int i = stringPosition.i;
            stringPosition.i = i + 1;
            char charAt = str.charAt(i);
            if (!z) {
                z = true;
            } else if (charAt == '\\') {
                z = false;
            }
            if (z) {
                sb.append(charAt);
            }
        } while (stringPosition.i != str.length());
        throw new IllegalArgumentException();
    }

    private static String retrieveToken(String str, StringPosition stringPosition) {
        StringBuilder sb = new StringBuilder();
        stringPosition.i = getNextMeaningfulIndex(str, stringPosition.i);
        if (stringPosition.i >= str.length() || isTSpecialChar(str.charAt(stringPosition.i))) {
            throw new IllegalArgumentException();
        }
        do {
            int i = stringPosition.i;
            stringPosition.i = i + 1;
            sb.append(str.charAt(i));
            if (stringPosition.i >= str.length() || !isMeaningfulChar(str.charAt(stringPosition.i))) {
                break;
            }
        } while (!isTSpecialChar(str.charAt(stringPosition.i)));
        return sb.toString();
    }

    private static void retrieveType(String str, MimeType mimeType, StringPosition stringPosition) {
        mimeType.primaryType = retrieveToken(str, stringPosition).toLowerCase();
        stringPosition.i = getNextMeaningfulIndex(str, stringPosition.i);
        if (stringPosition.i >= str.length() || str.charAt(stringPosition.i) != '/') {
            throw new IllegalArgumentException();
        }
        stringPosition.i++;
        mimeType.subType = retrieveToken(str, stringPosition).toLowerCase();
    }
}
