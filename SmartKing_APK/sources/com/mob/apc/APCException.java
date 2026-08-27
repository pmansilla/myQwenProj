package com.mob.apc;

/* loaded from: classes.dex */
public class APCException extends Exception {
    public static final int AIDL_ERROR_CODE_SERVICE_BINDER_NULL_OR_TIMEOUT = 1001;
    public static final int AIDL_ERROR_CODE_SERVICE_BINDER_SEND_MESSAGE_REMOTEEXCEPTION = 1004;
    public static final int AIDL_ERROR_CODE_SERVICE_BIND_EXCEPTION = 1002;
    public static final int AIDL_ERROR_CODE_SERVICE_BIND_FAILED = 1003;
    public int errorCode;

    public APCException(int i, String str) {
        super(str);
        this.errorCode = 0;
        this.errorCode = i;
    }

    public APCException(String str) {
        super(str);
        this.errorCode = 0;
    }

    public APCException(Throwable th) {
        super(th);
        this.errorCode = 0;
    }
}
