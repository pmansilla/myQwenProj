package org.apache.commons.lang.exception;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class NestableDelegate implements Serializable {
    private static final transient String MUST_BE_THROWABLE = "The Nestable implementation passed to the NestableDelegate(Nestable) constructor must extend java.lang.Throwable";
    static /* synthetic */ Class class$org$apache$commons$lang$exception$Nestable = null;
    public static boolean matchSubclasses = true;
    private static final long serialVersionUID = 1;
    public static boolean topDown = true;
    public static boolean trimStackFrames = true;
    private Throwable nestable;

    /* JADX WARN: Multi-variable type inference failed */
    public NestableDelegate(Nestable nestable) {
        this.nestable = null;
        if (!(nestable instanceof Throwable)) {
            throw new IllegalArgumentException(MUST_BE_THROWABLE);
        }
        this.nestable = (Throwable) nestable;
    }

    static /* synthetic */ Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String getMessage(int i) {
        Class cls;
        Throwable throwable = getThrowable(i);
        if (class$org$apache$commons$lang$exception$Nestable == null) {
            Class class$ = class$("org.apache.commons.lang.exception.Nestable");
            class$org$apache$commons$lang$exception$Nestable = class$;
            cls = class$;
        } else {
            cls = class$org$apache$commons$lang$exception$Nestable;
        }
        return cls.isInstance(throwable) ? ((Nestable) throwable).getMessage(0) : throwable.getMessage();
    }

    public String getMessage(String str) {
        Throwable cause = ExceptionUtils.getCause(this.nestable);
        String message = cause == null ? null : cause.getMessage();
        if (cause == null || message == null) {
            return str;
        }
        if (str == null) {
            return message;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(message);
        return stringBuffer.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String[] getMessages() {
        Class cls;
        Throwable[] throwables = getThrowables();
        String[] strArr = new String[throwables.length];
        for (int i = 0; i < throwables.length; i++) {
            if (class$org$apache$commons$lang$exception$Nestable == null) {
                Class class$ = class$("org.apache.commons.lang.exception.Nestable");
                class$org$apache$commons$lang$exception$Nestable = class$;
                cls = class$;
            } else {
                cls = class$org$apache$commons$lang$exception$Nestable;
            }
            strArr[i] = cls.isInstance(throwables[i]) ? ((Nestable) throwables[i]).getMessage(0) : throwables[i].getMessage();
        }
        return strArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected String[] getStackFrames(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter((Writer) stringWriter, true);
        if (th instanceof Nestable) {
            ((Nestable) th).printPartialStackTrace(printWriter);
        } else {
            th.printStackTrace(printWriter);
        }
        return ExceptionUtils.getStackFrames(stringWriter.getBuffer().toString());
    }

    public Throwable getThrowable(int i) {
        return i == 0 ? this.nestable : getThrowables()[i];
    }

    public int getThrowableCount() {
        return ExceptionUtils.getThrowableCount(this.nestable);
    }

    public Throwable[] getThrowables() {
        return ExceptionUtils.getThrowables(this.nestable);
    }

    public int indexOfThrowable(Class cls, int i) {
        if (cls == null) {
            return -1;
        }
        if (i < 0) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("The start index was out of bounds: ");
            stringBuffer.append(i);
            throw new IndexOutOfBoundsException(stringBuffer.toString());
        }
        Throwable[] throwables = ExceptionUtils.getThrowables(this.nestable);
        if (i >= throwables.length) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("The start index was out of bounds: ");
            stringBuffer2.append(i);
            stringBuffer2.append(" >= ");
            stringBuffer2.append(throwables.length);
            throw new IndexOutOfBoundsException(stringBuffer2.toString());
        }
        if (matchSubclasses) {
            while (i < throwables.length) {
                if (cls.isAssignableFrom(throwables[i].getClass())) {
                    return i;
                }
                i++;
            }
        } else {
            while (i < throwables.length) {
                if (cls.equals(throwables[i].getClass())) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream printStream) {
        synchronized (printStream) {
            PrintWriter printWriter = new PrintWriter((OutputStream) printStream, false);
            printStackTrace(printWriter);
            printWriter.flush();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void printStackTrace(PrintWriter printWriter) {
        Throwable th = this.nestable;
        if (ExceptionUtils.isThrowableNested()) {
            if (th instanceof Nestable) {
                ((Nestable) th).printPartialStackTrace(printWriter);
                return;
            } else {
                th.printStackTrace(printWriter);
                return;
            }
        }
        ArrayList arrayList = new ArrayList();
        for (Throwable th2 = th; th2 != null; th2 = ExceptionUtils.getCause(th2)) {
            arrayList.add(getStackFrames(th2));
        }
        String str = "Caused by: ";
        if (!topDown) {
            str = "Rethrown as: ";
            Collections.reverse(arrayList);
        }
        if (trimStackFrames) {
            trimStackFrames(arrayList);
        }
        synchronized (printWriter) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                for (String str2 : (String[]) it.next()) {
                    printWriter.println(str2);
                }
                if (it.hasNext()) {
                    printWriter.print(str);
                }
            }
        }
    }

    protected void trimStackFrames(List list) {
        for (int size = list.size() - 1; size > 0; size--) {
            String[] strArr = (String[]) list.get(size);
            String[] strArr2 = (String[]) list.get(size - 1);
            ArrayList arrayList = new ArrayList(Arrays.asList(strArr));
            ExceptionUtils.removeCommonFrames(arrayList, new ArrayList(Arrays.asList(strArr2)));
            int length = strArr.length - arrayList.size();
            if (length > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("\t... ");
                stringBuffer.append(length);
                stringBuffer.append(" more");
                arrayList.add(stringBuffer.toString());
                list.set(size, arrayList.toArray(new String[arrayList.size()]));
            }
        }
    }
}
