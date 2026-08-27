package ycnet.runchinaup.core.ycimpl.data;

import java.io.Serializable;

/* loaded from: classes2.dex */
public class YCResp implements Serializable {
    private static final long serialVersionUID = -2254162270181981878L;
    private int errorCode;
    private String message;

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String toString() {
        return "YCResp{errorCode=" + this.errorCode + ", message='" + this.message + "'}";
    }
}
