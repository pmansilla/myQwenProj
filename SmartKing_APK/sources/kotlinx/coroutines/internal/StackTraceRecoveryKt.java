package kotlinx.coroutines.internal;

import java.util.ArrayDeque;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.InternalCoroutinesApi;
import org.apache.commons.lang.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StackTraceRecovery.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0001\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0007\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a9\u0010\u0004\u001a\u0002H\u0005\"\b\b\u0000\u0010\u0005*\u00020\u00062\u0006\u0010\u0007\u001a\u0002H\u00052\u0006\u0010\b\u001a\u0002H\u00052\u0010\u0010\t\u001a\f\u0012\b\u0012\u00060\u0001j\u0002`\u000b0\nH\u0002¢\u0006\u0002\u0010\f\u001a\u001e\u0010\r\u001a\f\u0012\b\u0012\u00060\u0001j\u0002`\u000b0\n2\n\u0010\u000e\u001a\u00060\u000fj\u0002`\u0010H\u0002\u001a1\u0010\u0011\u001a\u00020\u00122\u0010\u0010\u0013\u001a\f\u0012\b\u0012\u00060\u0001j\u0002`\u000b0\u00142\u0010\u0010\b\u001a\f\u0012\b\u0012\u00060\u0001j\u0002`\u000b0\nH\u0002¢\u0006\u0002\u0010\u0015\u001a\u0019\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0006H\u0080Hø\u0001\u0000¢\u0006\u0002\u0010\u0019\u001a+\u0010\u001a\u001a\u0002H\u0005\"\b\b\u0000\u0010\u0005*\u00020\u00062\u0006\u0010\u0018\u001a\u0002H\u00052\n\u0010\u000e\u001a\u00060\u000fj\u0002`\u0010H\u0002¢\u0006\u0002\u0010\u001b\u001a\u001f\u0010\u001c\u001a\u0002H\u0005\"\b\b\u0000\u0010\u0005*\u00020\u00062\u0006\u0010\u0018\u001a\u0002H\u0005H\u0000¢\u0006\u0002\u0010\u001d\u001a+\u0010\u001c\u001a\u0002H\u0005\"\b\b\u0000\u0010\u0005*\u00020\u00062\u0006\u0010\u0018\u001a\u0002H\u00052\n\u0010\u000e\u001a\u0006\u0012\u0002\b\u00030\u001eH\u0000¢\u0006\u0002\u0010\u001f\u001a\u001f\u0010 \u001a\u00020!\"\b\b\u0000\u0010\u0005*\u00020\u00062\u0006\u0010\u0018\u001a\u0002H\u0005H\u0002¢\u0006\u0002\u0010\"\u001a\u0018\u0010#\u001a\u00060\u0001j\u0002`\u000b2\n\u0010$\u001a\u00060\u0001j\u0002`\u000bH\u0007\u001a\u001f\u0010%\u001a\u0002H\u0005\"\b\b\u0000\u0010\u0005*\u00020\u00062\u0006\u0010\u0018\u001a\u0002H\u0005H\u0000¢\u0006\u0002\u0010\u001d\u001a1\u0010&\u001a\u0018\u0012\u0004\u0012\u0002H\u0005\u0012\u000e\u0012\f\u0012\b\u0012\u00060\u0001j\u0002`\u000b0\u00140'\"\b\b\u0000\u0010\u0005*\u00020\u0006*\u0002H\u0005H\u0002¢\u0006\u0002\u0010(\u001a\u001c\u0010)\u001a\u00020!*\u00060\u0001j\u0002`\u000b2\n\u0010*\u001a\u00060\u0001j\u0002`\u000bH\u0002\u001a#\u0010+\u001a\u00020,*\f\u0012\b\u0012\u00060\u0001j\u0002`\u000b0\u00142\u0006\u0010-\u001a\u00020\u0003H\u0002¢\u0006\u0002\u0010.\u001a\u0010\u0010/\u001a\u00020!*\u00060\u0001j\u0002`\u000bH\u0000\u001a\u001b\u00100\u001a\u0002H\u0005\"\b\b\u0000\u0010\u0005*\u00020\u0006*\u0002H\u0005H\u0002¢\u0006\u0002\u0010\u001d*\f\b\u0000\u00101\"\u00020\u000f2\u00020\u000f*\f\b\u0000\u00102\"\u00020\u00012\u00020\u0001\u0082\u0002\u0004\n\u0002\b\u0019¨\u00063"}, d2 = {"artificialFrame", "Ljava/lang/StackTraceElement;", "message", "", "createFinalException", "E", "", "cause", "result", "resultStackTrace", "Ljava/util/ArrayDeque;", "Lkotlinx/coroutines/internal/StackTraceElement;", "(Ljava/lang/Throwable;Ljava/lang/Throwable;Ljava/util/ArrayDeque;)Ljava/lang/Throwable;", "createStackTrace", "continuation", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "mergeRecoveredTraces", "", "recoveredStacktrace", "", "([Ljava/lang/StackTraceElement;Ljava/util/ArrayDeque;)V", "recoverAndThrow", "", "exception", "(Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recoverFromStackFrame", "(Ljava/lang/Throwable;Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)Ljava/lang/Throwable;", "recoverStackTrace", "(Ljava/lang/Throwable;)Ljava/lang/Throwable;", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Throwable;", "recoveryDisabled", "", "(Ljava/lang/Throwable;)Z", "sanitize", "element", "unwrap", "causeAndStacktrace", "Lkotlin/Pair;", "(Ljava/lang/Throwable;)Lkotlin/Pair;", "elementWiseEquals", "e", "frameIndex", "", "methodName", "([Ljava/lang/StackTraceElement;Ljava/lang/String;)I", "isArtificial", "sanitizeStackTrace", "CoroutineStackFrame", "StackTraceElement", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class StackTraceRecoveryKt {
    public static /* synthetic */ void CoroutineStackFrame$annotations() {
    }

    public static /* synthetic */ void StackTraceElement$annotations() {
    }

    @InternalCoroutinesApi
    @NotNull
    public static final StackTraceElement artificialFrame(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        return new StackTraceElement("\b\b\b(" + message, "\b", "\b", -1);
    }

    private static final <E extends Throwable> Pair<E, StackTraceElement[]> causeAndStacktrace(@NotNull E e) {
        boolean z;
        Throwable cause = e.getCause();
        if (cause == null || !Intrinsics.areEqual(cause.getClass(), e.getClass())) {
            return TuplesKt.to(e, new StackTraceElement[0]);
        }
        StackTraceElement[] currentTrace = e.getStackTrace();
        Intrinsics.checkExpressionValueIsNotNull(currentTrace, "currentTrace");
        int length = currentTrace.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            }
            StackTraceElement it = currentTrace[i];
            Intrinsics.checkExpressionValueIsNotNull(it, "it");
            if (isArtificial(it)) {
                z = true;
                break;
            }
            i++;
        }
        return z ? TuplesKt.to(cause, currentTrace) : TuplesKt.to(e, new StackTraceElement[0]);
    }

    private static final <E extends Throwable> E createFinalException(E e, E e2, ArrayDeque<StackTraceElement> arrayDeque) {
        arrayDeque.addFirst(artificialFrame("Coroutine boundary"));
        StackTraceElement[] causeTrace = e.getStackTrace();
        Intrinsics.checkExpressionValueIsNotNull(causeTrace, "causeTrace");
        int frameIndex = frameIndex(causeTrace, "kotlin.coroutines.jvm.internal.BaseContinuationImpl");
        int i = 0;
        if (frameIndex == -1) {
            ArrayDeque<StackTraceElement> arrayDeque2 = arrayDeque;
            if (arrayDeque2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
            }
            Object[] array = arrayDeque2.toArray(new StackTraceElement[0]);
            if (array == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            e2.setStackTrace((StackTraceElement[]) array);
            return e2;
        }
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[arrayDeque.size() + frameIndex];
        for (int i2 = 0; i2 < frameIndex; i2++) {
            stackTraceElementArr[i2] = causeTrace[i2];
        }
        Iterator<T> it = arrayDeque.iterator();
        while (it.hasNext()) {
            stackTraceElementArr[frameIndex + i] = (StackTraceElement) it.next();
            i++;
        }
        e2.setStackTrace(stackTraceElementArr);
        return e2;
    }

    private static final ArrayDeque<StackTraceElement> createStackTrace(CoroutineStackFrame coroutineStackFrame) {
        ArrayDeque<StackTraceElement> arrayDeque = new ArrayDeque<>();
        StackTraceElement stackTraceElement = coroutineStackFrame.getStackTraceElement();
        if (stackTraceElement != null) {
            arrayDeque.add(sanitize(stackTraceElement));
        }
        while (true) {
            if (!(coroutineStackFrame instanceof CoroutineStackFrame)) {
                coroutineStackFrame = null;
            }
            if (coroutineStackFrame == null || (coroutineStackFrame = coroutineStackFrame.getCallerFrame()) == null) {
                break;
            }
            StackTraceElement stackTraceElement2 = coroutineStackFrame.getStackTraceElement();
            if (stackTraceElement2 != null) {
                arrayDeque.add(sanitize(stackTraceElement2));
            }
        }
        return arrayDeque;
    }

    private static final boolean elementWiseEquals(@NotNull StackTraceElement stackTraceElement, StackTraceElement stackTraceElement2) {
        return stackTraceElement.getLineNumber() == stackTraceElement2.getLineNumber() && Intrinsics.areEqual(stackTraceElement.getMethodName(), stackTraceElement2.getMethodName()) && Intrinsics.areEqual(stackTraceElement.getFileName(), stackTraceElement2.getFileName()) && Intrinsics.areEqual(stackTraceElement.getClassName(), stackTraceElement2.getClassName());
    }

    private static final int frameIndex(@NotNull StackTraceElement[] stackTraceElementArr, String str) {
        int length = stackTraceElementArr.length;
        for (int i = 0; i < length; i++) {
            if (Intrinsics.areEqual(str, stackTraceElementArr[i].getClassName())) {
                return i;
            }
        }
        return -1;
    }

    public static final boolean isArtificial(@NotNull StackTraceElement isArtificial) {
        Intrinsics.checkParameterIsNotNull(isArtificial, "$this$isArtificial");
        String className = isArtificial.getClassName();
        Intrinsics.checkExpressionValueIsNotNull(className, "className");
        return StringsKt.startsWith$default(className, "\b\b\b", false, 2, (Object) null);
    }

    private static final void mergeRecoveredTraces(StackTraceElement[] stackTraceElementArr, ArrayDeque<StackTraceElement> arrayDeque) {
        int length = stackTraceElementArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            } else if (isArtificial(stackTraceElementArr[i])) {
                break;
            } else {
                i++;
            }
        }
        int i2 = i + 1;
        int length2 = stackTraceElementArr.length - 1;
        if (length2 < i2) {
            return;
        }
        while (true) {
            StackTraceElement stackTraceElement = stackTraceElementArr[length2];
            StackTraceElement last = arrayDeque.getLast();
            Intrinsics.checkExpressionValueIsNotNull(last, "result.last");
            if (elementWiseEquals(stackTraceElement, last)) {
                arrayDeque.removeLast();
            }
            arrayDeque.addFirst(stackTraceElementArr[length2]);
            if (length2 == i2) {
                return;
            } else {
                length2--;
            }
        }
    }

    @Nullable
    public static final Object recoverAndThrow(@NotNull Throwable th, @NotNull Continuation<?> continuation) {
        if (recoveryDisabled(th)) {
            throw th;
        }
        if (continuation instanceof CoroutineStackFrame) {
            throw recoverFromStackFrame(th, (CoroutineStackFrame) continuation);
        }
        throw th;
    }

    @Nullable
    private static final Object recoverAndThrow$$forInline(@NotNull Throwable th, @NotNull Continuation continuation) {
        if (recoveryDisabled(th)) {
            throw th;
        }
        InlineMarker.mark(0);
        if (continuation instanceof CoroutineStackFrame) {
            throw recoverFromStackFrame(th, (CoroutineStackFrame) continuation);
        }
        throw th;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <E extends Throwable> E recoverFromStackFrame(E e, CoroutineStackFrame coroutineStackFrame) {
        Pair causeAndStacktrace = causeAndStacktrace(e);
        Throwable th = (Throwable) causeAndStacktrace.component1();
        StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) causeAndStacktrace.component2();
        Throwable tryCopyException = ExceptionsConstuctorKt.tryCopyException(th);
        if (tryCopyException == null) {
            return e;
        }
        ArrayDeque<StackTraceElement> createStackTrace = createStackTrace(coroutineStackFrame);
        if (createStackTrace.isEmpty()) {
            return e;
        }
        if (th != e) {
            mergeRecoveredTraces(stackTraceElementArr, createStackTrace);
        }
        return (E) createFinalException(th, tryCopyException, createStackTrace);
    }

    @NotNull
    public static final <E extends Throwable> E recoverStackTrace(@NotNull E exception) {
        Throwable tryCopyException;
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        return (recoveryDisabled(exception) || (tryCopyException = ExceptionsConstuctorKt.tryCopyException(exception)) == null) ? exception : (E) sanitizeStackTrace(tryCopyException);
    }

    @NotNull
    public static final <E extends Throwable> E recoverStackTrace(@NotNull E exception, @NotNull Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return (recoveryDisabled(exception) || !(continuation instanceof CoroutineStackFrame)) ? exception : (E) recoverFromStackFrame(exception, (CoroutineStackFrame) continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <E extends Throwable> boolean recoveryDisabled(E e) {
        return !DebugKt.RECOVER_STACK_TRACES || (e instanceof NonRecoverableThrowable);
    }

    @InternalCoroutinesApi
    @NotNull
    public static final StackTraceElement sanitize(@NotNull StackTraceElement element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        String className = element.getClassName();
        Intrinsics.checkExpressionValueIsNotNull(className, "element.className");
        if (!StringsKt.contains$default((CharSequence) className, '/', false, 2, (Object) null)) {
            return element;
        }
        String className2 = element.getClassName();
        Intrinsics.checkExpressionValueIsNotNull(className2, "element.className");
        return new StackTraceElement(StringsKt.replace$default(className2, '/', ClassUtils.PACKAGE_SEPARATOR_CHAR, false, 4, (Object) null), element.getMethodName(), element.getFileName(), element.getLineNumber());
    }

    private static final <E extends Throwable> E sanitizeStackTrace(@NotNull E e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        int length = stackTrace.length;
        Intrinsics.checkExpressionValueIsNotNull(stackTrace, "stackTrace");
        int frameIndex = frameIndex(stackTrace, "kotlinx.coroutines.internal.StackTraceRecoveryKt");
        int i = frameIndex + 1;
        int frameIndex2 = frameIndex(stackTrace, "kotlin.coroutines.jvm.internal.BaseContinuationImpl");
        int i2 = 0;
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[(length - frameIndex) - (frameIndex2 == -1 ? 0 : length - frameIndex2)];
        int length2 = stackTraceElementArr.length;
        while (i2 < length2) {
            stackTraceElementArr[i2] = i2 == 0 ? artificialFrame("Coroutine boundary") : stackTrace[(i + i2) - 1];
            i2++;
        }
        e.setStackTrace(stackTraceElementArr);
        return e;
    }

    @NotNull
    public static final <E extends Throwable> E unwrap(@NotNull E exception) {
        E e;
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (!recoveryDisabled(exception) && (e = (E) exception.getCause()) != null) {
            boolean z = true;
            if (!(!Intrinsics.areEqual(e.getClass(), exception.getClass()))) {
                StackTraceElement[] stackTrace = exception.getStackTrace();
                Intrinsics.checkExpressionValueIsNotNull(stackTrace, "exception.stackTrace");
                int length = stackTrace.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = false;
                        break;
                    }
                    StackTraceElement it = stackTrace[i];
                    Intrinsics.checkExpressionValueIsNotNull(it, "it");
                    if (isArtificial(it)) {
                        break;
                    }
                    i++;
                }
                return z ? e : exception;
            }
        }
        return exception;
    }
}
