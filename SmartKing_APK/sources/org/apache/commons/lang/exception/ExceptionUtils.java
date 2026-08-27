package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

/* loaded from: classes2.dex */
public class ExceptionUtils {
    private static String[] CAUSE_METHOD_NAMES = {"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
    private static final Method THROWABLE_CAUSE_METHOD;
    private static final Method THROWABLE_INITCAUSE_METHOD;
    static final String WRAPPED_MARKER = " [wrapped] ";
    static /* synthetic */ Class class$java$lang$Throwable;

    static {
        Method method;
        Class cls;
        Class<?> cls2;
        Class cls3;
        Method method2 = null;
        try {
            if (class$java$lang$Throwable == null) {
                cls3 = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls3;
            } else {
                cls3 = class$java$lang$Throwable;
            }
            method = cls3.getMethod("getCause", null);
        } catch (Exception unused) {
            method = null;
        }
        THROWABLE_CAUSE_METHOD = method;
        try {
            if (class$java$lang$Throwable == null) {
                cls = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls;
            } else {
                cls = class$java$lang$Throwable;
            }
            Class<?>[] clsArr = new Class[1];
            if (class$java$lang$Throwable == null) {
                cls2 = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls2;
            } else {
                cls2 = class$java$lang$Throwable;
            }
            clsArr[0] = cls2;
            method2 = cls.getMethod("initCause", clsArr);
        } catch (Exception unused2) {
        }
        THROWABLE_INITCAUSE_METHOD = method2;
    }

    public static void addCauseMethodName(String str) {
        if (!StringUtils.isNotEmpty(str) || isCauseMethodName(str)) {
            return;
        }
        ArrayList causeMethodNameList = getCauseMethodNameList();
        if (causeMethodNameList.add(str)) {
            synchronized (CAUSE_METHOD_NAMES) {
                CAUSE_METHOD_NAMES = toArray(causeMethodNameList);
            }
        }
    }

    static /* synthetic */ Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public static Throwable getCause(Throwable th) {
        Throwable cause;
        synchronized (CAUSE_METHOD_NAMES) {
            cause = getCause(th, CAUSE_METHOD_NAMES);
        }
        return cause;
    }

    public static Throwable getCause(Throwable th, String[] strArr) {
        String str;
        if (th == null) {
            return null;
        }
        Throwable causeUsingWellKnownTypes = getCauseUsingWellKnownTypes(th);
        if (causeUsingWellKnownTypes != null) {
            return causeUsingWellKnownTypes;
        }
        if (strArr == null) {
            synchronized (CAUSE_METHOD_NAMES) {
                strArr = CAUSE_METHOD_NAMES;
            }
        }
        for (int i = 0; i < strArr.length && ((str = strArr[i]) == null || (causeUsingWellKnownTypes = getCauseUsingMethodName(th, str)) == null); i++) {
        }
        return causeUsingWellKnownTypes == null ? getCauseUsingFieldName(th, "detail") : causeUsingWellKnownTypes;
    }

    private static ArrayList getCauseMethodNameList() {
        ArrayList arrayList;
        synchronized (CAUSE_METHOD_NAMES) {
            arrayList = new ArrayList(Arrays.asList(CAUSE_METHOD_NAMES));
        }
        return arrayList;
    }

    private static Throwable getCauseUsingFieldName(Throwable th, String str) {
        Field field;
        Class cls;
        try {
            field = th.getClass().getField(str);
        } catch (NoSuchFieldException | SecurityException unused) {
            field = null;
        }
        if (field != null) {
            if (class$java$lang$Throwable == null) {
                cls = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls;
            } else {
                cls = class$java$lang$Throwable;
            }
            if (cls.isAssignableFrom(field.getType())) {
                try {
                    return (Throwable) field.get(th);
                } catch (IllegalAccessException | IllegalArgumentException unused2) {
                }
            }
        }
        return null;
    }

    private static Throwable getCauseUsingMethodName(Throwable th, String str) {
        Method method;
        Class cls;
        try {
            method = th.getClass().getMethod(str, null);
        } catch (NoSuchMethodException | SecurityException unused) {
            method = null;
        }
        if (method != null) {
            if (class$java$lang$Throwable == null) {
                cls = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls;
            } else {
                cls = class$java$lang$Throwable;
            }
            if (cls.isAssignableFrom(method.getReturnType())) {
                try {
                    return (Throwable) method.invoke(th, ArrayUtils.EMPTY_OBJECT_ARRAY);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException unused2) {
                }
            }
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Throwable getCauseUsingWellKnownTypes(Throwable th) {
        if (th instanceof Nestable) {
            return ((Nestable) th).getCause();
        }
        if (th instanceof SQLException) {
            return ((SQLException) th).getNextException();
        }
        if (th instanceof InvocationTargetException) {
            return ((InvocationTargetException) th).getTargetException();
        }
        return null;
    }

    public static String getFullStackTrace(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter((Writer) stringWriter, true);
        Throwable[] throwables = getThrowables(th);
        for (int i = 0; i < throwables.length; i++) {
            throwables[i].printStackTrace(printWriter);
            if (isNestedThrowable(throwables[i])) {
                break;
            }
        }
        return stringWriter.getBuffer().toString();
    }

    public static String getMessage(Throwable th) {
        if (th == null) {
            return "";
        }
        String shortClassName = ClassUtils.getShortClassName(th, null);
        String message = th.getMessage();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(shortClassName);
        stringBuffer.append(": ");
        stringBuffer.append(StringUtils.defaultString(message));
        return stringBuffer.toString();
    }

    public static Throwable getRootCause(Throwable th) {
        List throwableList = getThrowableList(th);
        if (throwableList.size() < 2) {
            return null;
        }
        return (Throwable) throwableList.get(throwableList.size() - 1);
    }

    public static String getRootCauseMessage(Throwable th) {
        Throwable rootCause = getRootCause(th);
        if (rootCause != null) {
            th = rootCause;
        }
        return getMessage(th);
    }

    public static String[] getRootCauseStackTrace(Throwable th) {
        List list;
        if (th == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        Throwable[] throwables = getThrowables(th);
        int length = throwables.length;
        ArrayList arrayList = new ArrayList();
        int i = length - 1;
        List stackFrameList = getStackFrameList(throwables[i]);
        while (true) {
            length--;
            if (length < 0) {
                return (String[]) arrayList.toArray(new String[0]);
            }
            if (length != 0) {
                list = getStackFrameList(throwables[length - 1]);
                removeCommonFrames(stackFrameList, list);
            } else {
                list = stackFrameList;
            }
            if (length == i) {
                arrayList.add(throwables[length].toString());
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(WRAPPED_MARKER);
                stringBuffer.append(throwables[length].toString());
                arrayList.add(stringBuffer.toString());
            }
            for (int i2 = 0; i2 < stackFrameList.size(); i2++) {
                arrayList.add(stackFrameList.get(i2));
            }
            stackFrameList = list;
        }
    }

    static List getStackFrameList(Throwable th) {
        StringTokenizer stringTokenizer = new StringTokenizer(getStackTrace(th), SystemUtils.LINE_SEPARATOR);
        ArrayList arrayList = new ArrayList();
        boolean z = false;
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            int indexOf = nextToken.indexOf("at");
            if (indexOf != -1 && nextToken.substring(0, indexOf).trim().length() == 0) {
                z = true;
                arrayList.add(nextToken);
            } else if (z) {
                break;
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] getStackFrames(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, SystemUtils.LINE_SEPARATOR);
        ArrayList arrayList = new ArrayList();
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(stringTokenizer.nextToken());
        }
        return toArray(arrayList);
    }

    public static String[] getStackFrames(Throwable th) {
        return th == null ? ArrayUtils.EMPTY_STRING_ARRAY : getStackFrames(getStackTrace(th));
    }

    public static String getStackTrace(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter((Writer) stringWriter, true));
        return stringWriter.getBuffer().toString();
    }

    public static int getThrowableCount(Throwable th) {
        return getThrowableList(th).size();
    }

    public static List getThrowableList(Throwable th) {
        ArrayList arrayList = new ArrayList();
        while (th != null && !arrayList.contains(th)) {
            arrayList.add(th);
            th = getCause(th);
        }
        return arrayList;
    }

    public static Throwable[] getThrowables(Throwable th) {
        List throwableList = getThrowableList(th);
        return (Throwable[]) throwableList.toArray(new Throwable[throwableList.size()]);
    }

    private static int indexOf(Throwable th, Class cls, int i, boolean z) {
        if (th == null || cls == null) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        Throwable[] throwables = getThrowables(th);
        if (i >= throwables.length) {
            return -1;
        }
        if (z) {
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

    public static int indexOfThrowable(Throwable th, Class cls) {
        return indexOf(th, cls, 0, false);
    }

    public static int indexOfThrowable(Throwable th, Class cls, int i) {
        return indexOf(th, cls, i, false);
    }

    public static int indexOfType(Throwable th, Class cls) {
        return indexOf(th, cls, 0, true);
    }

    public static int indexOfType(Throwable th, Class cls, int i) {
        return indexOf(th, cls, i, true);
    }

    public static boolean isCauseMethodName(String str) {
        boolean z;
        synchronized (CAUSE_METHOD_NAMES) {
            z = ArrayUtils.indexOf(CAUSE_METHOD_NAMES, str) >= 0;
        }
        return z;
    }

    public static boolean isNestedThrowable(Throwable th) {
        Class cls;
        if (th == null) {
            return false;
        }
        if ((th instanceof Nestable) || (th instanceof SQLException) || (th instanceof InvocationTargetException) || isThrowableNested()) {
            return true;
        }
        Class<?> cls2 = th.getClass();
        synchronized (CAUSE_METHOD_NAMES) {
            int length = CAUSE_METHOD_NAMES.length;
            for (int i = 0; i < length; i++) {
                try {
                    Method method = cls2.getMethod(CAUSE_METHOD_NAMES[i], null);
                    if (method == null) {
                        continue;
                    } else {
                        if (class$java$lang$Throwable == null) {
                            cls = class$("java.lang.Throwable");
                            class$java$lang$Throwable = cls;
                        } else {
                            cls = class$java$lang$Throwable;
                        }
                        if (cls.isAssignableFrom(method.getReturnType())) {
                            return true;
                        }
                    }
                } catch (NoSuchMethodException | SecurityException unused) {
                }
            }
            return cls2.getField("detail") != null;
        }
    }

    public static boolean isThrowableNested() {
        return THROWABLE_CAUSE_METHOD != null;
    }

    public static void printRootCauseStackTrace(Throwable th) {
        printRootCauseStackTrace(th, System.err);
    }

    public static void printRootCauseStackTrace(Throwable th, PrintStream printStream) {
        if (th == null) {
            return;
        }
        if (printStream == null) {
            throw new IllegalArgumentException("The PrintStream must not be null");
        }
        for (String str : getRootCauseStackTrace(th)) {
            printStream.println(str);
        }
        printStream.flush();
    }

    public static void printRootCauseStackTrace(Throwable th, PrintWriter printWriter) {
        if (th == null) {
            return;
        }
        if (printWriter == null) {
            throw new IllegalArgumentException("The PrintWriter must not be null");
        }
        for (String str : getRootCauseStackTrace(th)) {
            printWriter.println(str);
        }
        printWriter.flush();
    }

    public static void removeCauseMethodName(String str) {
        if (StringUtils.isNotEmpty(str)) {
            ArrayList causeMethodNameList = getCauseMethodNameList();
            if (causeMethodNameList.remove(str)) {
                synchronized (CAUSE_METHOD_NAMES) {
                    CAUSE_METHOD_NAMES = toArray(causeMethodNameList);
                }
            }
        }
    }

    public static void removeCommonFrames(List list, List list2) {
        if (list == null || list2 == null) {
            throw new IllegalArgumentException("The List must not be null");
        }
        int size = list.size() - 1;
        for (int size2 = list2.size() - 1; size >= 0 && size2 >= 0; size2--) {
            if (((String) list.get(size)).equals((String) list2.get(size2))) {
                list.remove(size);
            }
            size--;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0029 A[Catch: IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0035, TryCatch #1 {IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0035, blocks: (B:6:0x0014, B:8:0x0020, B:9:0x002b, B:13:0x0029), top: B:5:0x0014 }] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0020 A[Catch: IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0035, TryCatch #1 {IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0035, blocks: (B:6:0x0014, B:8:0x0020, B:9:0x002b, B:13:0x0029), top: B:5:0x0014 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean setCause(java.lang.Throwable r7, java.lang.Throwable r8) {
        /*
            if (r7 == 0) goto L36
            r0 = 1
            java.lang.Object[] r1 = new java.lang.Object[r0]
            r2 = 0
            r1[r2] = r8
            java.lang.reflect.Method r8 = org.apache.commons.lang.exception.ExceptionUtils.THROWABLE_INITCAUSE_METHOD
            if (r8 == 0) goto L13
            java.lang.reflect.Method r8 = org.apache.commons.lang.exception.ExceptionUtils.THROWABLE_INITCAUSE_METHOD     // Catch: java.lang.Throwable -> L13
            r8.invoke(r7, r1)     // Catch: java.lang.Throwable -> L13
            r8 = 1
            goto L14
        L13:
            r8 = 0
        L14:
            java.lang.Class r3 = r7.getClass()     // Catch: java.lang.Throwable -> L35
            java.lang.String r4 = "setCause"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch: java.lang.Throwable -> L35
            java.lang.Class r6 = org.apache.commons.lang.exception.ExceptionUtils.class$java$lang$Throwable     // Catch: java.lang.Throwable -> L35
            if (r6 != 0) goto L29
            java.lang.String r6 = "java.lang.Throwable"
            java.lang.Class r6 = class$(r6)     // Catch: java.lang.Throwable -> L35
            org.apache.commons.lang.exception.ExceptionUtils.class$java$lang$Throwable = r6     // Catch: java.lang.Throwable -> L35
            goto L2b
        L29:
            java.lang.Class r6 = org.apache.commons.lang.exception.ExceptionUtils.class$java$lang$Throwable     // Catch: java.lang.Throwable -> L35
        L2b:
            r5[r2] = r6     // Catch: java.lang.Throwable -> L35
            java.lang.reflect.Method r2 = r3.getMethod(r4, r5)     // Catch: java.lang.Throwable -> L35
            r2.invoke(r7, r1)     // Catch: java.lang.Throwable -> L35
            r8 = 1
        L35:
            return r8
        L36:
            org.apache.commons.lang.NullArgumentException r7 = new org.apache.commons.lang.NullArgumentException
            java.lang.String r8 = "target"
            r7.<init>(r8)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.exception.ExceptionUtils.setCause(java.lang.Throwable, java.lang.Throwable):boolean");
    }

    private static String[] toArray(List list) {
        return (String[]) list.toArray(new String[list.size()]);
    }
}
