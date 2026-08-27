package me.panpf.sketch.decode;

import me.panpf.sketch.request.ErrorCause;

/* loaded from: classes2.dex */
public class DecodeException extends Exception {
    private ErrorCause errorCause;

    public DecodeException(String str, Throwable th, ErrorCause errorCause) {
        super(str, th);
        this.errorCause = errorCause;
    }

    public DecodeException(String str, ErrorCause errorCause) {
        super(str);
        this.errorCause = errorCause;
    }

    public DecodeException(Throwable th, ErrorCause errorCause) {
        super(th);
        this.errorCause = errorCause;
    }

    public ErrorCause getErrorCause() {
        return this.errorCause;
    }
}
