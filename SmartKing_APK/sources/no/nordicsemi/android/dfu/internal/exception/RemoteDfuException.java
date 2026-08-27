package no.nordicsemi.android.dfu.internal.exception;

import com.litesuits.orm.db.assit.SQLBuilder;

/* loaded from: classes2.dex */
public class RemoteDfuException extends Exception {
    private static final long serialVersionUID = -6901728550661937942L;
    private final int mState;

    public RemoteDfuException(String str, int i) {
        super(str);
        this.mState = i;
    }

    public int getErrorNumber() {
        return this.mState;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return super.getMessage() + " (error " + this.mState + SQLBuilder.PARENTHESES_RIGHT;
    }
}
