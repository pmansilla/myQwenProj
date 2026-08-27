package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import me.panpf.sketch.SLog;
import org.jetbrains.annotations.NotNull;

/* compiled from: Debug.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u000f\u001a\u00020\u0003*\u0006\u0012\u0002\b\u00030\u0010H\u0000\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0086T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003X\u0086T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0003X\u0086T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0003X\u0086T¢\u0006\u0002\n\u0000\"\u0010\u0010\u0007\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0003X\u0080T¢\u0006\u0002\n\u0000\"\u0018\u0010\t\u001a\u00020\u0003*\u00020\n8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f\"\u0018\u0010\r\u001a\u00020\u0003*\u00020\n8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f¨\u0006\u0011"}, d2 = {SLog.LEVEL_NAME_DEBUG, "", "DEBUG_PROPERTY_NAME", "", "DEBUG_PROPERTY_VALUE_AUTO", "DEBUG_PROPERTY_VALUE_OFF", "DEBUG_PROPERTY_VALUE_ON", "RECOVER_STACK_TRACES", "STACKTRACE_RECOVERY_PROPERTY_NAME", "classSimpleName", "", "getClassSimpleName", "(Ljava/lang/Object;)Ljava/lang/String;", "hexAddress", "getHexAddress", "toDebugString", "Lkotlin/coroutines/Continuation;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class DebugKt {

    @JvmField
    public static final boolean DEBUG;

    @NotNull
    public static final String DEBUG_PROPERTY_NAME = "kotlinx.coroutines.debug";

    @NotNull
    public static final String DEBUG_PROPERTY_VALUE_AUTO = "auto";

    @NotNull
    public static final String DEBUG_PROPERTY_VALUE_OFF = "off";

    @NotNull
    public static final String DEBUG_PROPERTY_VALUE_ON = "on";

    @JvmField
    public static final boolean RECOVER_STACK_TRACES;

    @NotNull
    public static final String STACKTRACE_RECOVERY_PROPERTY_NAME = "kotlinx.coroutines.stacktrace.recovery";

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0025, code lost:
    
        if (r0.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_AUTO) != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x003e, code lost:
    
        if (r0.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON) != false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0049, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0047, code lost:
    
        if (r0.equals("") != false) goto L24;
     */
    static {
        /*
            java.lang.String r0 = "kotlinx.coroutines.debug"
            java.lang.String r0 = kotlinx.coroutines.internal.SystemPropsKt.systemProp(r0)
            r1 = 0
            r2 = 1
            if (r0 != 0) goto Lb
            goto L27
        Lb:
            int r3 = r0.hashCode()
            if (r3 == 0) goto L41
            r4 = 3551(0xddf, float:4.976E-42)
            if (r3 == r4) goto L38
            r4 = 109935(0x1ad6f, float:1.54052E-40)
            if (r3 == r4) goto L2e
            r4 = 3005871(0x2dddaf, float:4.212122E-39)
            if (r3 != r4) goto L5c
            java.lang.String r3 = "auto"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L5c
        L27:
            java.lang.Class<kotlinx.coroutines.CoroutineId> r0 = kotlinx.coroutines.CoroutineId.class
            boolean r0 = r0.desiredAssertionStatus()
            goto L4a
        L2e:
            java.lang.String r3 = "off"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L5c
            r0 = 0
            goto L4a
        L38:
            java.lang.String r3 = "on"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L5c
            goto L49
        L41:
            java.lang.String r3 = ""
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L5c
        L49:
            r0 = 1
        L4a:
            kotlinx.coroutines.DebugKt.DEBUG = r0
            boolean r0 = kotlinx.coroutines.DebugKt.DEBUG
            if (r0 == 0) goto L59
            java.lang.String r0 = "kotlinx.coroutines.stacktrace.recovery"
            boolean r0 = kotlinx.coroutines.internal.SystemPropsKt.systemProp(r0, r2)
            if (r0 == 0) goto L59
            r1 = 1
        L59:
            kotlinx.coroutines.DebugKt.RECOVER_STACK_TRACES = r1
            return
        L5c:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "System property 'kotlinx.coroutines.debug' has unrecognized value '"
            r1.append(r2)
            r1.append(r0)
            r0 = 39
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.String r0 = r0.toString()
            r1.<init>(r0)
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.DebugKt.<clinit>():void");
    }

    @NotNull
    public static final String getClassSimpleName(@NotNull Object classSimpleName) {
        Intrinsics.checkParameterIsNotNull(classSimpleName, "$this$classSimpleName");
        String simpleName = classSimpleName.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this::class.java.simpleName");
        return simpleName;
    }

    @NotNull
    public static final String getHexAddress(@NotNull Object hexAddress) {
        Intrinsics.checkParameterIsNotNull(hexAddress, "$this$hexAddress");
        String hexString = Integer.toHexString(System.identityHashCode(hexAddress));
        Intrinsics.checkExpressionValueIsNotNull(hexString, "Integer.toHexString(System.identityHashCode(this))");
        return hexString;
    }

    @NotNull
    public static final String toDebugString(@NotNull Continuation<?> toDebugString) {
        Object m33constructorimpl;
        Intrinsics.checkParameterIsNotNull(toDebugString, "$this$toDebugString");
        if (toDebugString instanceof DispatchedContinuation) {
            return toDebugString.toString();
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            m33constructorimpl = Result.m33constructorimpl(toDebugString + '@' + getHexAddress(toDebugString));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            m33constructorimpl = Result.m33constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m36exceptionOrNullimpl(m33constructorimpl) != null) {
            m33constructorimpl = toDebugString.getClass().getName() + '@' + getHexAddress(toDebugString);
        }
        return (String) m33constructorimpl;
    }
}
