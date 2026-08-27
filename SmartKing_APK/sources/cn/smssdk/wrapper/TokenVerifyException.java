package cn.smssdk.wrapper;

/* loaded from: classes.dex */
public class TokenVerifyException extends Exception {
    protected int code;

    public TokenVerifyException(int i, String str) {
        super(str);
        this.code = i;
    }

    public TokenVerifyException(int i, String str, Throwable th) {
        super(str, th);
        this.code = i;
    }

    public TokenVerifyException(Throwable th) {
        super(th);
    }

    public int getCode() {
        return this.code;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "{\"code\": " + this.code + ", \"message\": \"" + getMessage() + "\"}";
    }
}
