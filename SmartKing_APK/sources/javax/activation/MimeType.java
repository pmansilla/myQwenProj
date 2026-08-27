package javax.activation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Locale;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes2.dex */
public class MimeType implements Externalizable {
    private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
    private MimeTypeParameterList parameters;
    private String primaryType;
    private String subType;

    public MimeType() {
        this.primaryType = "application";
        this.subType = "*";
        this.parameters = new MimeTypeParameterList();
    }

    public MimeType(String str) throws MimeTypeParseException {
        parse(str);
    }

    public MimeType(String str, String str2) throws MimeTypeParseException {
        if (!isValidToken(str)) {
            throw new MimeTypeParseException("Primary type is invalid.");
        }
        this.primaryType = str.toLowerCase(Locale.ENGLISH);
        if (!isValidToken(str2)) {
            throw new MimeTypeParseException("Sub type is invalid.");
        }
        this.subType = str2.toLowerCase(Locale.ENGLISH);
        this.parameters = new MimeTypeParameterList();
    }

    private static boolean isTokenChar(char c) {
        return c > ' ' && c < 127 && TSPECIALS.indexOf(c) < 0;
    }

    private boolean isValidToken(String str) {
        int length = str.length();
        if (length <= 0) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!isTokenChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private void parse(String str) throws MimeTypeParseException {
        int indexOf = str.indexOf(47);
        int indexOf2 = str.indexOf(59);
        if (indexOf < 0 && indexOf2 < 0) {
            throw new MimeTypeParseException("Unable to find a sub type.");
        }
        if (indexOf < 0 && indexOf2 >= 0) {
            throw new MimeTypeParseException("Unable to find a sub type.");
        }
        if (indexOf >= 0 && indexOf2 < 0) {
            this.primaryType = str.substring(0, indexOf).trim().toLowerCase(Locale.ENGLISH);
            this.subType = str.substring(indexOf + 1).trim().toLowerCase(Locale.ENGLISH);
            this.parameters = new MimeTypeParameterList();
        } else {
            if (indexOf >= indexOf2) {
                throw new MimeTypeParseException("Unable to find a sub type.");
            }
            this.primaryType = str.substring(0, indexOf).trim().toLowerCase(Locale.ENGLISH);
            this.subType = str.substring(indexOf + 1, indexOf2).trim().toLowerCase(Locale.ENGLISH);
            this.parameters = new MimeTypeParameterList(str.substring(indexOf2));
        }
        if (!isValidToken(this.primaryType)) {
            throw new MimeTypeParseException("Primary type is invalid.");
        }
        if (!isValidToken(this.subType)) {
            throw new MimeTypeParseException("Sub type is invalid.");
        }
    }

    public String getBaseType() {
        return String.valueOf(this.primaryType) + FileUriModel.SCHEME + this.subType;
    }

    public String getParameter(String str) {
        return this.parameters.get(str);
    }

    public MimeTypeParameterList getParameters() {
        return this.parameters;
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public String getSubType() {
        return this.subType;
    }

    public boolean match(String str) throws MimeTypeParseException {
        return match(new MimeType(str));
    }

    public boolean match(MimeType mimeType) {
        if (this.primaryType.equals(mimeType.getPrimaryType())) {
            return this.subType.equals("*") || mimeType.getSubType().equals("*") || this.subType.equals(mimeType.getSubType());
        }
        return false;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        try {
            parse(objectInput.readUTF());
        } catch (MimeTypeParseException e) {
            throw new IOException(e.toString());
        }
    }

    public void removeParameter(String str) {
        this.parameters.remove(str);
    }

    public void setParameter(String str, String str2) {
        this.parameters.set(str, str2);
    }

    public void setPrimaryType(String str) throws MimeTypeParseException {
        if (!isValidToken(this.primaryType)) {
            throw new MimeTypeParseException("Primary type is invalid.");
        }
        this.primaryType = str.toLowerCase(Locale.ENGLISH);
    }

    public void setSubType(String str) throws MimeTypeParseException {
        if (!isValidToken(this.subType)) {
            throw new MimeTypeParseException("Sub type is invalid.");
        }
        this.subType = str.toLowerCase(Locale.ENGLISH);
    }

    public String toString() {
        return String.valueOf(getBaseType()) + this.parameters.toString();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeUTF(toString());
        objectOutput.flush();
    }
}
