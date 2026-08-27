package cn.sharesdk.line;

/* loaded from: classes.dex */
public class LineClientNotExistException extends RuntimeException {
    public static final String MESSAGE = "Line for Android is not installed!";
    private static final long serialVersionUID = 4016527937286556356L;

    public LineClientNotExistException() {
        super(MESSAGE);
    }
}
