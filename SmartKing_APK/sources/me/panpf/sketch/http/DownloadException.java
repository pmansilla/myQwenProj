package me.panpf.sketch.http;

import me.panpf.sketch.request.ErrorCause;

/* loaded from: classes2.dex */
public class DownloadException extends Exception {
    private ErrorCause errorCause;

    public DownloadException(String str, Throwable th, ErrorCause errorCause) {
        super(str, th);
        this.errorCause = errorCause;
    }

    public DownloadException(String str, ErrorCause errorCause) {
        super(str);
        this.errorCause = errorCause;
    }

    public ErrorCause getErrorCause() {
        return this.errorCause;
    }
}
