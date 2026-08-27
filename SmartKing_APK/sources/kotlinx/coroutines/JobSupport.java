package kotlinx.coroutines;

import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.ConcurrentKt;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause0;
import kotlinx.coroutines.selects.SelectInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
@Deprecated(level = DeprecationLevel.ERROR, message = "This is internal API and may be removed in the future releases")
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000æ\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0001\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0017\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0006©\u0001ª\u0001«\u0001B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J$\u00102\u001a\u00020\u00062\u0006\u00103\u001a\u00020\n2\u0006\u00104\u001a\u0002052\n\u00106\u001a\u0006\u0012\u0002\b\u000307H\u0002J\u001e\u00108\u001a\u0002092\u0006\u0010:\u001a\u00020\u00132\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00130<H\u0002J\u001a\u0010=\u001a\u0002092\b\u0010)\u001a\u0004\u0018\u00010\n2\u0006\u0010>\u001a\u00020?H\u0014J\u000e\u0010@\u001a\u00020(2\u0006\u0010A\u001a\u00020\u0002J\u0015\u0010B\u001a\u0004\u0018\u00010\nH\u0080@ø\u0001\u0000¢\u0006\u0004\bC\u0010DJ\u0013\u0010E\u001a\u0004\u0018\u00010\nH\u0082@ø\u0001\u0000¢\u0006\u0002\u0010DJ\u0012\u0010F\u001a\u00020\u00062\b\u0010G\u001a\u0004\u0018\u00010\u0013H\u0017J\u0018\u0010F\u001a\u0002092\u000e\u0010G\u001a\n\u0018\u00010Hj\u0004\u0018\u0001`IH\u0016J\u0010\u0010J\u001a\u00020\u00062\b\u0010G\u001a\u0004\u0018\u00010\u0013J\u0012\u0010K\u001a\u00020\u00062\b\u0010G\u001a\u0004\u0018\u00010\nH\u0002J\u0012\u0010L\u001a\u00020\u00062\b\u0010G\u001a\u0004\u0018\u00010\u0013H\u0016J\u0012\u0010M\u001a\u00020\u00062\b\u0010G\u001a\u0004\u0018\u00010\nH\u0002J\u0010\u0010N\u001a\u00020\u00062\u0006\u0010G\u001a\u00020\u0013H\u0002J\u0010\u0010O\u001a\u00020\u00062\u0006\u0010G\u001a\u00020\u0013H\u0016J\"\u0010P\u001a\u0002092\u0006\u0010)\u001a\u0002002\b\u0010Q\u001a\u0004\u0018\u00010\n2\u0006\u0010>\u001a\u00020?H\u0002J\"\u0010R\u001a\u0002092\u0006\u0010)\u001a\u00020S2\u0006\u0010T\u001a\u00020U2\b\u0010V\u001a\u0004\u0018\u00010\nH\u0002J\u0012\u0010W\u001a\u00020\u00132\b\u0010G\u001a\u0004\u0018\u00010\nH\u0002J\b\u0010X\u001a\u00020YH\u0002J\u0012\u0010Z\u001a\u0004\u0018\u00010U2\u0006\u0010)\u001a\u000200H\u0002J\n\u0010[\u001a\u00060Hj\u0002`IJ\b\u0010\\\u001a\u00020\u0013H\u0016J\u000f\u0010]\u001a\u0004\u0018\u00010\nH\u0000¢\u0006\u0002\b^J\b\u0010_\u001a\u0004\u0018\u00010\u0013J \u0010`\u001a\u0004\u0018\u00010\u00132\u0006\u0010)\u001a\u00020S2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00130<H\u0002J\u0012\u0010a\u001a\u0004\u0018\u0001052\u0006\u0010)\u001a\u000200H\u0002J\u0010\u0010b\u001a\u00020\u00062\u0006\u0010c\u001a\u00020\u0013H\u0014J\u0015\u0010d\u001a\u0002092\u0006\u0010c\u001a\u00020\u0013H\u0010¢\u0006\u0002\beJ\u0017\u0010f\u001a\u0002092\b\u0010g\u001a\u0004\u0018\u00010\u0001H\u0000¢\u0006\u0002\bhJ?\u0010i\u001a\u00020j2\u0006\u0010k\u001a\u00020\u00062\u0006\u0010l\u001a\u00020\u00062'\u0010m\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0013¢\u0006\f\bo\u0012\b\bp\u0012\u0004\b\b(G\u0012\u0004\u0012\u0002090nj\u0002`qJ/\u0010i\u001a\u00020j2'\u0010m\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0013¢\u0006\f\bo\u0012\b\bp\u0012\u0004\b\b(G\u0012\u0004\u0012\u0002090nj\u0002`qJ\u0011\u0010r\u001a\u000209H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010DJ\b\u0010s\u001a\u00020\u0006H\u0002J\u0011\u0010t\u001a\u000209H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010DJ\u001f\u0010u\u001a\u00020v2\u0014\u0010w\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\n\u0012\u0004\u0012\u0002090nH\u0082\bJ\u0012\u0010x\u001a\u00020\u00062\b\u0010G\u001a\u0004\u0018\u00010\nH\u0002J\u0017\u0010y\u001a\u00020\u00062\b\u0010V\u001a\u0004\u0018\u00010\nH\u0000¢\u0006\u0002\bzJ\u001f\u0010{\u001a\u00020\u00062\b\u0010V\u001a\u0004\u0018\u00010\n2\u0006\u0010>\u001a\u00020?H\u0000¢\u0006\u0002\b|J=\u0010}\u001a\u0006\u0012\u0002\b\u0003072'\u0010m\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0013¢\u0006\f\bo\u0012\b\bp\u0012\u0004\b\b(G\u0012\u0004\u0012\u0002090nj\u0002`q2\u0006\u0010k\u001a\u00020\u0006H\u0002J\u000e\u0010~\u001a\u00020\u007fH\u0010¢\u0006\u0003\b\u0080\u0001J\u0019\u0010\u0081\u0001\u001a\u0002092\u0006\u00104\u001a\u0002052\u0006\u0010G\u001a\u00020\u0013H\u0002J-\u0010\u0082\u0001\u001a\u000209\"\u000f\b\u0000\u0010\u0083\u0001\u0018\u0001*\u0006\u0012\u0002\b\u0003072\u0006\u00104\u001a\u0002052\b\u0010G\u001a\u0004\u0018\u00010\u0013H\u0082\bJ\u0012\u0010k\u001a\u0002092\b\u0010G\u001a\u0004\u0018\u00010\u0013H\u0014J\u0013\u0010\u0084\u0001\u001a\u0002092\b\u0010)\u001a\u0004\u0018\u00010\nH\u0014J\u000f\u0010\u0085\u0001\u001a\u000209H\u0010¢\u0006\u0003\b\u0086\u0001J\u0010\u0010\u0087\u0001\u001a\u0002092\u0007\u0010\u0088\u0001\u001a\u00020\u0003J\u0012\u0010\u0089\u0001\u001a\u0002092\u0007\u0010)\u001a\u00030\u008a\u0001H\u0002J\u0015\u0010\u008b\u0001\u001a\u0002092\n\u0010)\u001a\u0006\u0012\u0002\b\u000307H\u0002JH\u0010\u008c\u0001\u001a\u000209\"\u0005\b\u0000\u0010\u008d\u00012\u000f\u0010\u008e\u0001\u001a\n\u0012\u0005\u0012\u0003H\u008d\u00010\u008f\u00012\u001e\u0010w\u001a\u001a\b\u0001\u0012\f\u0012\n\u0012\u0005\u0012\u0003H\u008d\u00010\u0090\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n0nø\u0001\u0000¢\u0006\u0003\u0010\u0091\u0001J\\\u0010\u0092\u0001\u001a\u000209\"\u0005\b\u0000\u0010\u0083\u0001\"\u0005\b\u0001\u0010\u008d\u00012\u000f\u0010\u008e\u0001\u001a\n\u0012\u0005\u0012\u0003H\u008d\u00010\u008f\u00012&\u0010w\u001a\"\b\u0001\u0012\u0005\u0012\u0003H\u0083\u0001\u0012\f\u0012\n\u0012\u0005\u0012\u0003H\u008d\u00010\u0090\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0093\u0001H\u0000ø\u0001\u0000¢\u0006\u0006\b\u0094\u0001\u0010\u0095\u0001J\u001b\u0010\u0096\u0001\u001a\u0002092\n\u00106\u001a\u0006\u0012\u0002\b\u000307H\u0000¢\u0006\u0003\b\u0097\u0001J\\\u0010\u0098\u0001\u001a\u000209\"\u0005\b\u0000\u0010\u0083\u0001\"\u0005\b\u0001\u0010\u008d\u00012\u000f\u0010\u008e\u0001\u001a\n\u0012\u0005\u0012\u0003H\u008d\u00010\u008f\u00012&\u0010w\u001a\"\b\u0001\u0012\u0005\u0012\u0003H\u0083\u0001\u0012\f\u0012\n\u0012\u0005\u0012\u0003H\u008d\u00010\u0090\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0093\u0001H\u0000ø\u0001\u0000¢\u0006\u0006\b\u0099\u0001\u0010\u0095\u0001J\u0007\u0010\u009a\u0001\u001a\u00020\u0006J\u0013\u0010\u009b\u0001\u001a\u00020?2\b\u0010)\u001a\u0004\u0018\u00010\nH\u0002J\u0013\u0010\u009c\u0001\u001a\u00020\u007f2\b\u0010)\u001a\u0004\u0018\u00010\nH\u0002J\t\u0010\u009d\u0001\u001a\u00020\u007fH\u0007J\t\u0010\u009e\u0001\u001a\u00020\u007fH\u0016J#\u0010\u009f\u0001\u001a\u00020\u00062\u0006\u0010)\u001a\u00020S2\b\u0010V\u001a\u0004\u0018\u00010\n2\u0006\u0010>\u001a\u00020?H\u0002J#\u0010 \u0001\u001a\u00020\u00062\u0006\u0010)\u001a\u0002002\b\u0010Q\u001a\u0004\u0018\u00010\n2\u0006\u0010>\u001a\u00020?H\u0002J\u0019\u0010¡\u0001\u001a\u00020\u00062\u0006\u0010)\u001a\u0002002\u0006\u0010:\u001a\u00020\u0013H\u0002J%\u0010¢\u0001\u001a\u00020?2\b\u0010)\u001a\u0004\u0018\u00010\n2\b\u0010V\u001a\u0004\u0018\u00010\n2\u0006\u0010>\u001a\u00020?H\u0002J$\u0010£\u0001\u001a\u00020\u00062\u0006\u0010)\u001a\u00020S2\u0006\u0010A\u001a\u00020U2\b\u0010V\u001a\u0004\u0018\u00010\nH\u0082\u0010J\u0010\u0010¤\u0001\u001a\u0004\u0018\u00010U*\u00030¥\u0001H\u0002J\u0017\u0010¦\u0001\u001a\u000209*\u0002052\b\u0010G\u001a\u0004\u0018\u00010\u0013H\u0002J\u001e\u0010§\u0001\u001a\u00060Hj\u0002`I*\u00020\u00132\u000b\b\u0002\u0010¨\u0001\u001a\u0004\u0018\u00010\u007fH\u0004R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\u00068TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0012\u001a\u0004\u0018\u00010\u00138DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u00068DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\rR\u0014\u0010\u0018\u001a\u00020\u00068TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\rR\u0014\u0010\u001a\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\rR\u0011\u0010\u001b\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\rR\u0011\u0010\u001c\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u001c\u0010\rR\u0011\u0010\u001d\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u001d\u0010\rR\u0015\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u001f8F¢\u0006\u0006\u001a\u0004\b \u0010!R\u0014\u0010\"\u001a\u00020\u00068PX\u0090\u0004¢\u0006\u0006\u001a\u0004\b#\u0010\rR\u0011\u0010$\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b%\u0010&R\u0010\u0010'\u001a\u0004\u0018\u00010(X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010)\u001a\u0004\u0018\u00010\n8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b*\u0010+R\u001c\u0010,\u001a\u0004\u0018\u00010\u0013*\u0004\u0018\u00010\n8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b-\u0010.R\u0018\u0010/\u001a\u00020\u0006*\u0002008BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b/\u00101\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006¬\u0001"}, d2 = {"Lkotlinx/coroutines/JobSupport;", "Lkotlinx/coroutines/Job;", "Lkotlinx/coroutines/ChildJob;", "Lkotlinx/coroutines/ParentJob;", "Lkotlinx/coroutines/selects/SelectClause0;", "active", "", "(Z)V", "_state", "Lkotlinx/atomicfu/AtomicRef;", "", "cancelsParent", "getCancelsParent", "()Z", "children", "Lkotlin/sequences/Sequence;", "getChildren", "()Lkotlin/sequences/Sequence;", "completionCause", "", "getCompletionCause", "()Ljava/lang/Throwable;", "completionCauseHandled", "getCompletionCauseHandled", "handlesException", "getHandlesException", "isActive", "isCancelled", "isCompleted", "isCompletedExceptionally", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/CoroutineContext$Key;", "onCancelComplete", "getOnCancelComplete$kotlinx_coroutines_core", "onJoin", "getOnJoin", "()Lkotlinx/coroutines/selects/SelectClause0;", "parentHandle", "Lkotlinx/coroutines/ChildHandle;", "state", "getState$kotlinx_coroutines_core", "()Ljava/lang/Object;", "exceptionOrNull", "getExceptionOrNull", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "isCancelling", "Lkotlinx/coroutines/Incomplete;", "(Lkotlinx/coroutines/Incomplete;)Z", "addLastAtomic", "expect", "list", "Lkotlinx/coroutines/NodeList;", "node", "Lkotlinx/coroutines/JobNode;", "addSuppressedExceptions", "", "rootCause", "exceptions", "", "afterCompletionInternal", "mode", "", "attachChild", "child", "awaitInternal", "awaitInternal$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "awaitSuspend", "cancel", "cause", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "cancelCoroutine", "cancelImpl", "cancelInternal", "cancelMakeCompleting", "cancelParent", "childCancelled", "completeStateFinalization", "update", "continueCompleting", "Lkotlinx/coroutines/JobSupport$Finishing;", "lastChild", "Lkotlinx/coroutines/ChildHandleNode;", "proposedUpdate", "createCauseException", "createJobCancellationException", "Lkotlinx/coroutines/JobCancellationException;", "firstChild", "getCancellationException", "getChildJobCancellationCause", "getCompletedInternal", "getCompletedInternal$kotlinx_coroutines_core", "getCompletionExceptionOrNull", "getFinalRootCause", "getOrPromoteCancellingList", "handleJobException", "exception", "handleOnCompletionException", "handleOnCompletionException$kotlinx_coroutines_core", "initParentJobInternal", "parent", "initParentJobInternal$kotlinx_coroutines_core", "invokeOnCompletion", "Lkotlinx/coroutines/DisposableHandle;", "onCancelling", "invokeImmediately", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", IMAPStore.ID_NAME, "Lkotlinx/coroutines/CompletionHandler;", "join", "joinInternal", "joinSuspend", "loopOnState", "", "block", "makeCancelling", "makeCompleting", "makeCompleting$kotlinx_coroutines_core", "makeCompletingOnce", "makeCompletingOnce$kotlinx_coroutines_core", "makeNode", "nameString", "", "nameString$kotlinx_coroutines_core", "notifyCancelling", "notifyHandlers", "T", "onCompletionInternal", "onStartInternal", "onStartInternal$kotlinx_coroutines_core", "parentCancelled", "parentJob", "promoteEmptyToNodeList", "Lkotlinx/coroutines/Empty;", "promoteSingleToNodeList", "registerSelectClause0", "R", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "registerSelectClause1Internal", "Lkotlin/Function2;", "registerSelectClause1Internal$kotlinx_coroutines_core", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "removeNode", "removeNode$kotlinx_coroutines_core", "selectAwaitCompletion", "selectAwaitCompletion$kotlinx_coroutines_core", "start", "startInternal", "stateString", "toDebugString", "toString", "tryFinalizeFinishingState", "tryFinalizeSimpleState", "tryMakeCancelling", "tryMakeCompleting", "tryWaitForChild", "nextChild", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "notifyCompletion", "toCancellationException", "message", "AwaitContinuation", "ChildCompletion", "Finishing", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes.dex */
public class JobSupport implements Job, ChildJob, ParentJob, SelectClause0 {
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
    private volatile Object _state;
    private volatile ChildHandle parentHandle;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JobSupport.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/JobSupport$AwaitContinuation;", "T", "Lkotlinx/coroutines/CancellableContinuationImpl;", "delegate", "Lkotlin/coroutines/Continuation;", "job", "Lkotlinx/coroutines/JobSupport;", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/JobSupport;)V", "getContinuationCancellationCause", "", "parent", "Lkotlinx/coroutines/Job;", "nameString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
    /* loaded from: classes2.dex */
    public static final class AwaitContinuation<T> extends CancellableContinuationImpl<T> {
        private final JobSupport job;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AwaitContinuation(@NotNull Continuation<? super T> delegate, @NotNull JobSupport job) {
            super(delegate, 1);
            Intrinsics.checkParameterIsNotNull(delegate, "delegate");
            Intrinsics.checkParameterIsNotNull(job, "job");
            this.job = job;
        }

        @Override // kotlinx.coroutines.CancellableContinuationImpl
        @NotNull
        public Throwable getContinuationCancellationCause(@NotNull Job parent) {
            Throwable th;
            Intrinsics.checkParameterIsNotNull(parent, "parent");
            Object state$kotlinx_coroutines_core = this.job.getState$kotlinx_coroutines_core();
            return (!(state$kotlinx_coroutines_core instanceof Finishing) || (th = ((Finishing) state$kotlinx_coroutines_core).rootCause) == null) ? state$kotlinx_coroutines_core instanceof CompletedExceptionally ? ((CompletedExceptionally) state$kotlinx_coroutines_core).cause : parent.getCancellationException() : th;
        }

        @Override // kotlinx.coroutines.CancellableContinuationImpl
        @NotNull
        protected String nameString() {
            return "AwaitContinuation";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JobSupport.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B'\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bJ\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/JobSupport$ChildCompletion;", "Lkotlinx/coroutines/JobNode;", "Lkotlinx/coroutines/Job;", "parent", "Lkotlinx/coroutines/JobSupport;", "state", "Lkotlinx/coroutines/JobSupport$Finishing;", "child", "Lkotlinx/coroutines/ChildHandleNode;", "proposedUpdate", "", "(Lkotlinx/coroutines/JobSupport;Lkotlinx/coroutines/JobSupport$Finishing;Lkotlinx/coroutines/ChildHandleNode;Ljava/lang/Object;)V", "invoke", "", "cause", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
    /* loaded from: classes2.dex */
    public static final class ChildCompletion extends JobNode<Job> {
        private final ChildHandleNode child;
        private final JobSupport parent;
        private final Object proposedUpdate;
        private final Finishing state;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ChildCompletion(@NotNull JobSupport parent, @NotNull Finishing state, @NotNull ChildHandleNode child, @Nullable Object obj) {
            super(child.childJob);
            Intrinsics.checkParameterIsNotNull(parent, "parent");
            Intrinsics.checkParameterIsNotNull(state, "state");
            Intrinsics.checkParameterIsNotNull(child, "child");
            this.parent = parent;
            this.state = state;
            this.child = child;
            this.proposedUpdate = obj;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            invoke2(th);
            return Unit.INSTANCE;
        }

        @Override // kotlinx.coroutines.CompletionHandlerBase
        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public void invoke2(@Nullable Throwable cause) {
            this.parent.continueCompleting(this.state, this.child, this.proposedUpdate);
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        @NotNull
        public String toString() {
            return "ChildCompletion[" + this.child + ", " + this.proposedUpdate + ']';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JobSupport.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u00022\u00020\u0003B\u001f\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\nJ\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\tJ\u0018\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\t0\u0016j\b\u0012\u0004\u0012\u00020\t`\u0017H\u0002J\u0016\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\t0\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\tJ\b\u0010\u001b\u001a\u00020\u001cH\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0001X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\rR\u0012\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\rR\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\b\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lkotlinx/coroutines/JobSupport$Finishing;", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "Lkotlinx/coroutines/Incomplete;", "list", "Lkotlinx/coroutines/NodeList;", "isCompleting", "", "rootCause", "", "(Lkotlinx/coroutines/NodeList;ZLjava/lang/Throwable;)V", "_exceptionsHolder", "isActive", "()Z", "isCancelling", "isSealed", "getList", "()Lkotlinx/coroutines/NodeList;", "addExceptionLocked", "", "exception", "allocateList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "sealLocked", "", "proposedException", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
    /* loaded from: classes2.dex */
    public static final class Finishing implements Incomplete {

        /* renamed from: _exceptionsHolder, reason: from kotlin metadata and from toString */
        private volatile Object exceptions;

        /* renamed from: isCompleting, reason: from kotlin metadata and from toString */
        @JvmField
        public volatile boolean completing;

        @NotNull
        private final NodeList list;

        @JvmField
        @Nullable
        public volatile Throwable rootCause;

        public Finishing(@NotNull NodeList list, boolean z, @Nullable Throwable th) {
            Intrinsics.checkParameterIsNotNull(list, "list");
            this.list = list;
            this.completing = z;
            this.rootCause = th;
        }

        private final ArrayList<Throwable> allocateList() {
            return new ArrayList<>(4);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final void addExceptionLocked(@NotNull Throwable exception) {
            Intrinsics.checkParameterIsNotNull(exception, "exception");
            Throwable th = this.rootCause;
            if (th == null) {
                this.rootCause = exception;
                return;
            }
            if (exception == th) {
                return;
            }
            Object obj = this.exceptions;
            if (obj == null) {
                this.exceptions = exception;
                return;
            }
            if (obj instanceof Throwable) {
                if (exception == obj) {
                    return;
                }
                ArrayList<Throwable> allocateList = allocateList();
                allocateList.add(obj);
                allocateList.add(exception);
                this.exceptions = allocateList;
                return;
            }
            if (obj instanceof ArrayList) {
                ((ArrayList) obj).add(exception);
                return;
            }
            throw new IllegalStateException(("State is " + obj).toString());
        }

        @Override // kotlinx.coroutines.Incomplete
        @NotNull
        public NodeList getList() {
            return this.list;
        }

        @Override // kotlinx.coroutines.Incomplete
        /* renamed from: isActive */
        public boolean getIsActive() {
            return this.rootCause == null;
        }

        public final boolean isCancelling() {
            return this.rootCause != null;
        }

        public final boolean isSealed() {
            Symbol symbol;
            Object obj = this.exceptions;
            symbol = JobSupportKt.SEALED;
            return obj == symbol;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @NotNull
        public final List<Throwable> sealLocked(@Nullable Throwable proposedException) {
            ArrayList<Throwable> arrayList;
            Symbol symbol;
            Object obj = this.exceptions;
            if (obj == null) {
                arrayList = allocateList();
            } else if (obj instanceof Throwable) {
                ArrayList<Throwable> allocateList = allocateList();
                allocateList.add(obj);
                arrayList = allocateList;
            } else {
                if (!(obj instanceof ArrayList)) {
                    throw new IllegalStateException(("State is " + obj).toString());
                }
                if (obj == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.ArrayList<kotlin.Throwable> /* = java.util.ArrayList<kotlin.Throwable> */");
                }
                arrayList = (ArrayList) obj;
            }
            Throwable th = this.rootCause;
            if (th != null) {
                arrayList.add(0, th);
            }
            if (proposedException != null && (!Intrinsics.areEqual(proposedException, th))) {
                arrayList.add(proposedException);
            }
            symbol = JobSupportKt.SEALED;
            this.exceptions = symbol;
            return arrayList;
        }

        @NotNull
        public String toString() {
            return "Finishing[cancelling=" + isCancelling() + ", completing=" + this.completing + ", rootCause=" + this.rootCause + ", exceptions=" + this.exceptions + ", list=" + getList() + ']';
        }
    }

    public JobSupport(boolean z) {
        this._state = z ? JobSupportKt.EMPTY_ACTIVE : JobSupportKt.EMPTY_NEW;
    }

    private final boolean addLastAtomic(final Object expect, NodeList list, JobNode<?> node) {
        final JobNode<?> jobNode = node;
        LockFreeLinkedListNode.CondAddOp condAddOp = new LockFreeLinkedListNode.CondAddOp(jobNode) { // from class: kotlinx.coroutines.JobSupport$addLastAtomic$$inlined$addLastIf$1
            @Override // kotlinx.coroutines.internal.AtomicOp
            @Nullable
            public Object prepare(@NotNull LockFreeLinkedListNode affected) {
                Intrinsics.checkParameterIsNotNull(affected, "affected");
                if (this.getState$kotlinx_coroutines_core() == expect) {
                    return null;
                }
                return LockFreeLinkedListKt.getCONDITION_FALSE();
            }
        };
        while (true) {
            Object prev = list.getPrev();
            if (prev == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
            switch (((LockFreeLinkedListNode) prev).tryCondAddNext(jobNode, list, condAddOp)) {
                case 1:
                    return true;
                case 2:
                    return false;
            }
        }
    }

    private final void addSuppressedExceptions(Throwable rootCause, List<? extends Throwable> exceptions) {
        if (exceptions.size() <= 1) {
            return;
        }
        Set identitySet = ConcurrentKt.identitySet(exceptions.size());
        Throwable unwrap = StackTraceRecoveryKt.unwrap(rootCause);
        Iterator<? extends Throwable> it = exceptions.iterator();
        while (it.hasNext()) {
            Throwable unwrap2 = StackTraceRecoveryKt.unwrap(it.next());
            if (unwrap2 != rootCause && unwrap2 != unwrap && !(unwrap2 instanceof CancellationException) && identitySet.add(unwrap2)) {
                kotlin.ExceptionsKt.addSuppressed(rootCause, unwrap2);
            }
        }
    }

    private final boolean cancelImpl(Object cause) {
        if (getOnCancelComplete$kotlinx_coroutines_core() && cancelMakeCompleting(cause)) {
            return true;
        }
        return makeCancelling(cause);
    }

    private final boolean cancelMakeCompleting(Object cause) {
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if ((state$kotlinx_coroutines_core instanceof Incomplete) && (!(state$kotlinx_coroutines_core instanceof Finishing) || !((Finishing) state$kotlinx_coroutines_core).completing)) {
                switch (tryMakeCompleting(state$kotlinx_coroutines_core, new CompletedExceptionally(createCauseException(cause), false, 2, null), 0)) {
                    case 0:
                        return false;
                    case 1:
                    case 2:
                        return true;
                    case 3:
                    default:
                        throw new IllegalStateException("unexpected result".toString());
                }
            }
        }
        return false;
    }

    private final boolean cancelParent(Throwable cause) {
        ChildHandle childHandle;
        if (cause instanceof CancellationException) {
            return true;
        }
        return getCancelsParent() && (childHandle = this.parentHandle) != null && childHandle.childCancelled(cause);
    }

    private final void completeStateFinalization(Incomplete state, Object update, int mode) {
        ChildHandle childHandle = this.parentHandle;
        if (childHandle != null) {
            childHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(update instanceof CompletedExceptionally) ? null : update);
        Throwable th = completedExceptionally != null ? completedExceptionally.cause : null;
        if (state instanceof JobNode) {
            try {
                ((JobNode) state).invoke(th);
            } catch (Throwable th2) {
                handleOnCompletionException$kotlinx_coroutines_core(new CompletionHandlerException("Exception in completion handler " + state + " for " + this, th2));
            }
        } else {
            NodeList list = state.getList();
            if (list != null) {
                notifyCompletion(list, th);
            }
        }
        afterCompletionInternal(update, mode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void continueCompleting(Finishing state, ChildHandleNode lastChild, Object proposedUpdate) {
        if (!(getState$kotlinx_coroutines_core() == state)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        ChildHandleNode nextChild = nextChild(lastChild);
        if ((nextChild == null || !tryWaitForChild(state, nextChild, proposedUpdate)) && tryFinalizeFinishingState(state, proposedUpdate, 0)) {
        }
    }

    private final Throwable createCauseException(Object cause) {
        if (cause != null ? cause instanceof Throwable : true) {
            if (cause == null) {
                cause = createJobCancellationException();
            }
            return (Throwable) cause;
        }
        if (cause != null) {
            return ((ParentJob) cause).getChildJobCancellationCause();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
    }

    private final JobCancellationException createJobCancellationException() {
        return new JobCancellationException("Job was cancelled", null, this);
    }

    private final ChildHandleNode firstChild(Incomplete state) {
        ChildHandleNode childHandleNode = (ChildHandleNode) (!(state instanceof ChildHandleNode) ? null : state);
        if (childHandleNode != null) {
            return childHandleNode;
        }
        NodeList list = state.getList();
        if (list != null) {
            return nextChild(list);
        }
        return null;
    }

    private final Throwable getExceptionOrNull(@Nullable Object obj) {
        if (!(obj instanceof CompletedExceptionally)) {
            obj = null;
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }

    private final Throwable getFinalRootCause(Finishing state, List<? extends Throwable> exceptions) {
        Object obj;
        if (exceptions.isEmpty()) {
            if (state.isCancelling()) {
                return createJobCancellationException();
            }
            return null;
        }
        Iterator<T> it = exceptions.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (!(((Throwable) obj) instanceof CancellationException)) {
                break;
            }
        }
        Throwable th = (Throwable) obj;
        return th != null ? th : exceptions.get(0);
    }

    private final NodeList getOrPromoteCancellingList(Incomplete state) {
        NodeList list = state.getList();
        if (list != null) {
            return list;
        }
        if (state instanceof Empty) {
            return new NodeList();
        }
        if (state instanceof JobNode) {
            promoteSingleToNodeList((JobNode) state);
            return null;
        }
        throw new IllegalStateException(("State should have list: " + state).toString());
    }

    private final boolean isCancelling(@NotNull Incomplete incomplete) {
        return (incomplete instanceof Finishing) && ((Finishing) incomplete).isCancelling();
    }

    private final boolean joinInternal() {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                return false;
            }
        } while (startInternal(state$kotlinx_coroutines_core) < 0);
        return true;
    }

    private final Void loopOnState(Function1<Object, Unit> block) {
        while (true) {
            block.invoke(getState$kotlinx_coroutines_core());
        }
    }

    private final boolean makeCancelling(Object cause) {
        Throwable th = (Throwable) null;
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Finishing) {
                synchronized (state$kotlinx_coroutines_core) {
                    if (((Finishing) state$kotlinx_coroutines_core).isSealed()) {
                        return false;
                    }
                    boolean isCancelling = ((Finishing) state$kotlinx_coroutines_core).isCancelling();
                    if (cause != null || !isCancelling) {
                        if (th == null) {
                            th = createCauseException(cause);
                        }
                        ((Finishing) state$kotlinx_coroutines_core).addExceptionLocked(th);
                    }
                    Throwable th2 = ((Finishing) state$kotlinx_coroutines_core).rootCause;
                    if (!(!isCancelling)) {
                        th2 = null;
                    }
                    if (th2 != null) {
                        notifyCancelling(((Finishing) state$kotlinx_coroutines_core).getList(), th2);
                    }
                    return true;
                }
            }
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                return false;
            }
            if (th == null) {
                th = createCauseException(cause);
            }
            Incomplete incomplete = (Incomplete) state$kotlinx_coroutines_core;
            if (!incomplete.getIsActive()) {
                switch (tryMakeCompleting(state$kotlinx_coroutines_core, new CompletedExceptionally(th, false, 2, null), 0)) {
                    case 0:
                        throw new IllegalStateException(("Cannot happen in " + state$kotlinx_coroutines_core).toString());
                    case 1:
                    case 2:
                        return true;
                    case 3:
                        break;
                    default:
                        throw new IllegalStateException("unexpected result".toString());
                }
            } else if (tryMakeCancelling(incomplete, th)) {
                return true;
            }
        }
    }

    private final JobNode<?> makeNode(Function1<? super Throwable, Unit> handler, boolean onCancelling) {
        if (onCancelling) {
            JobCancellingNode jobCancellingNode = (JobCancellingNode) (handler instanceof JobCancellingNode ? handler : null);
            if (jobCancellingNode != null) {
                if (!(jobCancellingNode.job == this)) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                if (jobCancellingNode != null) {
                    return jobCancellingNode;
                }
            }
            return new InvokeOnCancelling(this, handler);
        }
        JobNode<?> jobNode = (JobNode) (handler instanceof JobNode ? handler : null);
        if (jobNode != null) {
            if (jobNode.job == this && !(jobNode instanceof JobCancellingNode)) {
                r0 = true;
            }
            if (!r0) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (jobNode != null) {
                return jobNode;
            }
        }
        return new InvokeOnCompletion(this, handler);
    }

    private final ChildHandleNode nextChild(@NotNull LockFreeLinkedListNode lockFreeLinkedListNode) {
        while (lockFreeLinkedListNode.isRemoved()) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getPrevNode();
        }
        while (true) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode();
            if (!lockFreeLinkedListNode.isRemoved()) {
                if (lockFreeLinkedListNode instanceof ChildHandleNode) {
                    return (ChildHandleNode) lockFreeLinkedListNode;
                }
                if (lockFreeLinkedListNode instanceof NodeList) {
                    return null;
                }
            }
        }
    }

    private final void notifyCancelling(NodeList list, Throwable cause) {
        onCancelling(cause);
        Throwable th = (Throwable) null;
        Object next = list.getNext();
        if (next == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
        }
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual(lockFreeLinkedListNode, list); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobCancellingNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(cause);
                } catch (Throwable th2) {
                    if (th != null) {
                        kotlin.ExceptionsKt.addSuppressed(th, th2);
                        if (th != null) {
                        }
                    }
                    CompletionHandlerException completionHandlerException = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th2);
                    Unit unit = Unit.INSTANCE;
                    th = completionHandlerException;
                }
            }
        }
        if (th != null) {
            handleOnCompletionException$kotlinx_coroutines_core(th);
        }
        cancelParent(cause);
    }

    private final void notifyCompletion(@NotNull NodeList nodeList, Throwable th) {
        Throwable th2 = (Throwable) null;
        Object next = nodeList.getNext();
        if (next == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
        }
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual(lockFreeLinkedListNode, nodeList); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(th);
                } catch (Throwable th3) {
                    if (th2 != null) {
                        kotlin.ExceptionsKt.addSuppressed(th2, th3);
                        if (th2 != null) {
                        }
                    }
                    CompletionHandlerException completionHandlerException = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th3);
                    Unit unit = Unit.INSTANCE;
                    th2 = completionHandlerException;
                }
            }
        }
        if (th2 != null) {
            handleOnCompletionException$kotlinx_coroutines_core(th2);
        }
    }

    private final <T extends JobNode<?>> void notifyHandlers(NodeList list, Throwable cause) {
        Throwable th = (Throwable) null;
        Object next = list.getNext();
        if (next == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
        }
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual(lockFreeLinkedListNode, list); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            Intrinsics.reifiedOperationMarker(3, "T");
            if (lockFreeLinkedListNode instanceof LockFreeLinkedListNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(cause);
                } catch (Throwable th2) {
                    if (th != null) {
                        kotlin.ExceptionsKt.addSuppressed(th, th2);
                        if (th != null) {
                        }
                    }
                    CompletionHandlerException completionHandlerException = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th2);
                    Unit unit = Unit.INSTANCE;
                    th = completionHandlerException;
                }
            }
        }
        if (th != null) {
            handleOnCompletionException$kotlinx_coroutines_core(th);
        }
    }

    private final void promoteEmptyToNodeList(Empty state) {
        NodeList nodeList = new NodeList();
        _state$FU.compareAndSet(this, state, state.getIsActive() ? nodeList : new InactiveNodeList(nodeList));
    }

    private final void promoteSingleToNodeList(JobNode<?> state) {
        state.addOneIfEmpty(new NodeList());
        _state$FU.compareAndSet(this, state, state.getNextNode());
    }

    private final int startInternal(Object state) {
        Empty empty;
        if (!(state instanceof Empty)) {
            if (!(state instanceof InactiveNodeList)) {
                return 0;
            }
            if (!_state$FU.compareAndSet(this, state, ((InactiveNodeList) state).getList())) {
                return -1;
            }
            onStartInternal$kotlinx_coroutines_core();
            return 1;
        }
        if (((Empty) state).getIsActive()) {
            return 0;
        }
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _state$FU;
        empty = JobSupportKt.EMPTY_ACTIVE;
        if (!atomicReferenceFieldUpdater.compareAndSet(this, state, empty)) {
            return -1;
        }
        onStartInternal$kotlinx_coroutines_core();
        return 1;
    }

    private final String stateString(Object state) {
        if (!(state instanceof Finishing)) {
            return state instanceof Incomplete ? ((Incomplete) state).getIsActive() ? "Active" : "New" : state instanceof CompletedExceptionally ? "Cancelled" : "Completed";
        }
        Finishing finishing = (Finishing) state;
        return finishing.isCancelling() ? "Cancelling" : finishing.completing ? "Completing" : "Active";
    }

    @NotNull
    public static /* synthetic */ CancellationException toCancellationException$default(JobSupport jobSupport, Throwable th, String str, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: toCancellationException");
        }
        if ((i & 1) != 0) {
            str = (String) null;
        }
        return jobSupport.toCancellationException(th, str);
    }

    private final boolean tryFinalizeFinishingState(Finishing state, Object proposedUpdate, int mode) {
        boolean isCancelling;
        Throwable finalRootCause;
        Object boxIncomplete;
        if (!(getState$kotlinx_coroutines_core() == state)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!(!state.isSealed())) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!state.completing) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(proposedUpdate instanceof CompletedExceptionally) ? null : proposedUpdate);
        Throwable th = completedExceptionally != null ? completedExceptionally.cause : null;
        synchronized (state) {
            isCancelling = state.isCancelling();
            List<Throwable> sealLocked = state.sealLocked(th);
            finalRootCause = getFinalRootCause(state, sealLocked);
            if (finalRootCause != null) {
                addSuppressedExceptions(finalRootCause, sealLocked);
            }
        }
        if (finalRootCause != null && finalRootCause != th) {
            proposedUpdate = new CompletedExceptionally(finalRootCause, false, 2, null);
        }
        if (finalRootCause != null) {
            if (cancelParent(finalRootCause) || handleJobException(finalRootCause)) {
                if (proposedUpdate == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.CompletedExceptionally");
                }
                ((CompletedExceptionally) proposedUpdate).makeHandled();
            }
        }
        if (!isCancelling) {
            onCancelling(finalRootCause);
        }
        onCompletionInternal(proposedUpdate);
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _state$FU;
        boxIncomplete = JobSupportKt.boxIncomplete(proposedUpdate);
        if (atomicReferenceFieldUpdater.compareAndSet(this, state, boxIncomplete)) {
            completeStateFinalization(state, proposedUpdate, mode);
            return true;
        }
        throw new IllegalArgumentException(("Unexpected state: " + this._state + ", expected: " + state + ", update: " + proposedUpdate).toString());
    }

    private final boolean tryFinalizeSimpleState(Incomplete state, Object update, int mode) {
        Object boxIncomplete;
        if (!((state instanceof Empty) || (state instanceof JobNode))) {
            throw new IllegalStateException("Check failed.".toString());
        }
        if (!(!(update instanceof CompletedExceptionally))) {
            throw new IllegalStateException("Check failed.".toString());
        }
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _state$FU;
        boxIncomplete = JobSupportKt.boxIncomplete(update);
        if (!atomicReferenceFieldUpdater.compareAndSet(this, state, boxIncomplete)) {
            return false;
        }
        onCancelling(null);
        onCompletionInternal(update);
        completeStateFinalization(state, update, mode);
        return true;
    }

    private final boolean tryMakeCancelling(Incomplete state, Throwable rootCause) {
        if (!(!(state instanceof Finishing))) {
            throw new IllegalStateException("Check failed.".toString());
        }
        if (!state.getIsActive()) {
            throw new IllegalStateException("Check failed.".toString());
        }
        NodeList orPromoteCancellingList = getOrPromoteCancellingList(state);
        if (orPromoteCancellingList == null) {
            return false;
        }
        if (!_state$FU.compareAndSet(this, state, new Finishing(orPromoteCancellingList, false, rootCause))) {
            return false;
        }
        notifyCancelling(orPromoteCancellingList, rootCause);
        return true;
    }

    private final int tryMakeCompleting(Object state, Object proposedUpdate, int mode) {
        if (!(state instanceof Incomplete)) {
            return 0;
        }
        if (((state instanceof Empty) || (state instanceof JobNode)) && !(state instanceof ChildHandleNode) && !(proposedUpdate instanceof CompletedExceptionally)) {
            return !tryFinalizeSimpleState((Incomplete) state, proposedUpdate, mode) ? 3 : 1;
        }
        Incomplete incomplete = (Incomplete) state;
        NodeList orPromoteCancellingList = getOrPromoteCancellingList(incomplete);
        if (orPromoteCancellingList == null) {
            return 3;
        }
        Finishing finishing = (Finishing) (!(state instanceof Finishing) ? null : state);
        if (finishing == null) {
            finishing = new Finishing(orPromoteCancellingList, false, null);
        }
        synchronized (finishing) {
            if (finishing.completing) {
                return 0;
            }
            finishing.completing = true;
            if (finishing != state && !_state$FU.compareAndSet(this, state, finishing)) {
                return 3;
            }
            if (!(!finishing.isSealed())) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            boolean isCancelling = finishing.isCancelling();
            CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(proposedUpdate instanceof CompletedExceptionally) ? null : proposedUpdate);
            if (completedExceptionally != null) {
                finishing.addExceptionLocked(completedExceptionally.cause);
            }
            Throwable th = finishing.rootCause;
            if (!(!isCancelling)) {
                th = null;
            }
            Unit unit = Unit.INSTANCE;
            if (th != null) {
                notifyCancelling(orPromoteCancellingList, th);
            }
            ChildHandleNode firstChild = firstChild(incomplete);
            if (firstChild == null || !tryWaitForChild(finishing, firstChild, proposedUpdate)) {
                return tryFinalizeFinishingState(finishing, proposedUpdate, mode) ? 1 : 3;
            }
            return 2;
        }
    }

    private final boolean tryWaitForChild(Finishing state, ChildHandleNode child, Object proposedUpdate) {
        while (Job.DefaultImpls.invokeOnCompletion$default(child.childJob, false, false, new ChildCompletion(this, state, child, proposedUpdate), 1, null) == NonDisposableHandle.INSTANCE) {
            child = nextChild(child);
            if (child == null) {
                return false;
            }
        }
        return true;
    }

    protected void afterCompletionInternal(@Nullable Object state, int mode) {
    }

    @Override // kotlinx.coroutines.Job
    @NotNull
    public final ChildHandle attachChild(@NotNull ChildJob child) {
        Intrinsics.checkParameterIsNotNull(child, "child");
        DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(this, true, false, new ChildHandleNode(this, child), 2, null);
        if (invokeOnCompletion$default != null) {
            return (ChildHandle) invokeOnCompletion$default;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ChildHandle");
    }

    @Nullable
    public final Object awaitInternal$kotlinx_coroutines_core(@NotNull Continuation<Object> continuation) {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                    return JobSupportKt.unboxState(state$kotlinx_coroutines_core);
                }
                Throwable th = ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
                if (StackTraceRecoveryKt.recoveryDisabled(th)) {
                    throw th;
                }
                InlineMarker.mark(0);
                if (continuation instanceof CoroutineStackFrame) {
                    throw StackTraceRecoveryKt.recoverFromStackFrame(th, (CoroutineStackFrame) continuation);
                }
                throw th;
            }
        } while (startInternal(state$kotlinx_coroutines_core) < 0);
        return awaitSuspend(continuation);
    }

    @Nullable
    final /* synthetic */ Object awaitSuspend(@NotNull Continuation<Object> continuation) {
        AwaitContinuation awaitContinuation = new AwaitContinuation(IntrinsicsKt.intercepted(continuation), this);
        CancellableContinuationKt.disposeOnCancellation(awaitContinuation, invokeOnCompletion(new ResumeAwaitOnCompletion(this, awaitContinuation)));
        Object result = awaitContinuation.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    @Override // kotlinx.coroutines.Job
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public /* synthetic */ void cancel() {
        cancel((CancellationException) null);
    }

    @Override // kotlinx.coroutines.Job
    public void cancel(@Nullable CancellationException cause) {
        cancel(cause);
    }

    public final boolean cancelCoroutine(@Nullable Throwable cause) {
        return cancelImpl(cause);
    }

    @Override // kotlinx.coroutines.Job
    /* renamed from: cancelInternal, reason: merged with bridge method [inline-methods] */
    public boolean cancel(@Nullable Throwable cause) {
        return cancelImpl(cause) && getHandlesException();
    }

    public boolean childCancelled(@NotNull Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        return cancelImpl(cause) && getHandlesException();
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return (R) Job.DefaultImpls.fold(this, r, operation);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    @Nullable
    public <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return (E) Job.DefaultImpls.get(this, key);
    }

    @Override // kotlinx.coroutines.Job
    @NotNull
    public final CancellationException getCancellationException() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (!(state$kotlinx_coroutines_core instanceof Finishing)) {
            if (state$kotlinx_coroutines_core instanceof Incomplete) {
                throw new IllegalStateException(("Job is still new or active: " + this).toString());
            }
            if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
                return toCancellationException$default(this, ((CompletedExceptionally) state$kotlinx_coroutines_core).cause, null, 1, null);
            }
            return new JobCancellationException(DebugKt.getClassSimpleName(this) + " has completed normally", null, this);
        }
        Throwable th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
        if (th != null) {
            CancellationException cancellationException = toCancellationException(th, DebugKt.getClassSimpleName(this) + " is cancelling");
            if (cancellationException != null) {
                return cancellationException;
            }
        }
        throw new IllegalStateException(("Job is still new or active: " + this).toString());
    }

    protected boolean getCancelsParent() {
        return true;
    }

    @Override // kotlinx.coroutines.ParentJob
    @NotNull
    public Throwable getChildJobCancellationCause() {
        Throwable th;
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
        } else {
            if (state$kotlinx_coroutines_core instanceof Incomplete) {
                throw new IllegalStateException(("Cannot be cancelling child in this state: " + state$kotlinx_coroutines_core).toString());
            }
            th = state$kotlinx_coroutines_core instanceof CompletedExceptionally ? ((CompletedExceptionally) state$kotlinx_coroutines_core).cause : null;
        }
        if (th != null && (!getHandlesException() || (th instanceof CancellationException))) {
            return th;
        }
        return new JobCancellationException("Parent job is " + stateString(state$kotlinx_coroutines_core), th, this);
    }

    @Override // kotlinx.coroutines.Job
    @NotNull
    public final Sequence<Job> getChildren() {
        return SequencesKt.sequence(new JobSupport$children$1(this, null));
    }

    @Nullable
    public final Object getCompletedInternal$kotlinx_coroutines_core() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (!(!(state$kotlinx_coroutines_core instanceof Incomplete))) {
            throw new IllegalStateException("This job has not completed yet".toString());
        }
        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            throw ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
        }
        return JobSupportKt.unboxState(state$kotlinx_coroutines_core);
    }

    @Nullable
    protected final Throwable getCompletionCause() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            Throwable th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
            if (th != null) {
                return th;
            }
            throw new IllegalStateException(("Job is still new or active: " + this).toString());
        }
        if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
            if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
                return ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
            }
            return null;
        }
        throw new IllegalStateException(("Job is still new or active: " + this).toString());
    }

    protected final boolean getCompletionCauseHandled() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof CompletedExceptionally) && ((CompletedExceptionally) state$kotlinx_coroutines_core).getHandled();
    }

    @Nullable
    public final Throwable getCompletionExceptionOrNull() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
            return getExceptionOrNull(state$kotlinx_coroutines_core);
        }
        throw new IllegalStateException("This job has not completed yet".toString());
    }

    protected boolean getHandlesException() {
        return true;
    }

    @Override // kotlin.coroutines.CoroutineContext.Element
    @NotNull
    public final CoroutineContext.Key<?> getKey() {
        return Job.INSTANCE;
    }

    public boolean getOnCancelComplete$kotlinx_coroutines_core() {
        return false;
    }

    @Override // kotlinx.coroutines.Job
    @NotNull
    public final SelectClause0 getOnJoin() {
        return this;
    }

    @Nullable
    public final Object getState$kotlinx_coroutines_core() {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    protected boolean handleJobException(@NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        return false;
    }

    public void handleOnCompletionException$kotlinx_coroutines_core(@NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        throw exception;
    }

    public final void initParentJobInternal$kotlinx_coroutines_core(@Nullable Job parent) {
        if (!(this.parentHandle == null)) {
            throw new IllegalStateException("Check failed.".toString());
        }
        if (parent == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
            return;
        }
        parent.start();
        ChildHandle attachChild = parent.attachChild(this);
        this.parentHandle = attachChild;
        if (isCompleted()) {
            attachChild.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }

    @Override // kotlinx.coroutines.Job
    @NotNull
    public final DisposableHandle invokeOnCompletion(@NotNull Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        return invokeOnCompletion(false, true, handler);
    }

    @Override // kotlinx.coroutines.Job
    @NotNull
    public final DisposableHandle invokeOnCompletion(boolean onCancelling, boolean invokeImmediately, @NotNull Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        JobNode<?> jobNode = (JobNode) null;
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Empty) {
                Empty empty = (Empty) state$kotlinx_coroutines_core;
                if (empty.getIsActive()) {
                    if (jobNode == null) {
                        jobNode = makeNode(handler, onCancelling);
                    }
                    if (_state$FU.compareAndSet(this, state$kotlinx_coroutines_core, jobNode)) {
                        return jobNode;
                    }
                } else {
                    promoteEmptyToNodeList(empty);
                }
            } else {
                if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                    if (invokeImmediately) {
                        if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                            state$kotlinx_coroutines_core = null;
                        }
                        CompletedExceptionally completedExceptionally = (CompletedExceptionally) state$kotlinx_coroutines_core;
                        handler.invoke(completedExceptionally != null ? completedExceptionally.cause : null);
                    }
                    return NonDisposableHandle.INSTANCE;
                }
                NodeList list = ((Incomplete) state$kotlinx_coroutines_core).getList();
                if (list != null) {
                    Throwable th = (Throwable) null;
                    JobNode<?> jobNode2 = NonDisposableHandle.INSTANCE;
                    if (onCancelling && (state$kotlinx_coroutines_core instanceof Finishing)) {
                        synchronized (state$kotlinx_coroutines_core) {
                            th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
                            if (th == null || ((handler instanceof ChildHandleNode) && !((Finishing) state$kotlinx_coroutines_core).completing)) {
                                if (jobNode == null) {
                                    jobNode = makeNode(handler, onCancelling);
                                }
                                if (addLastAtomic(state$kotlinx_coroutines_core, list, jobNode)) {
                                    if (th == null) {
                                        return jobNode;
                                    }
                                    jobNode2 = jobNode;
                                }
                            }
                            Unit unit = Unit.INSTANCE;
                        }
                    }
                    if (th != null) {
                        if (invokeImmediately) {
                            handler.invoke(th);
                        }
                        return jobNode2;
                    }
                    if (jobNode == null) {
                        jobNode = makeNode(handler, onCancelling);
                    }
                    if (addLastAtomic(state$kotlinx_coroutines_core, list, jobNode)) {
                        return jobNode;
                    }
                } else {
                    if (state$kotlinx_coroutines_core == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.JobNode<*>");
                    }
                    promoteSingleToNodeList((JobNode) state$kotlinx_coroutines_core);
                }
            }
        }
    }

    @Override // kotlinx.coroutines.Job
    public boolean isActive() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof Incomplete) && ((Incomplete) state$kotlinx_coroutines_core).getIsActive();
    }

    @Override // kotlinx.coroutines.Job
    public final boolean isCancelled() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof CompletedExceptionally) || ((state$kotlinx_coroutines_core instanceof Finishing) && ((Finishing) state$kotlinx_coroutines_core).isCancelling());
    }

    @Override // kotlinx.coroutines.Job
    public final boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof Incomplete);
    }

    public final boolean isCompletedExceptionally() {
        return getState$kotlinx_coroutines_core() instanceof CompletedExceptionally;
    }

    @Override // kotlinx.coroutines.Job
    @Nullable
    public final Object join(@NotNull Continuation<? super Unit> continuation) {
        if (joinInternal()) {
            return joinSuspend(continuation);
        }
        YieldKt.checkCompletion(continuation.getContext());
        return Unit.INSTANCE;
    }

    @Nullable
    final /* synthetic */ Object joinSuspend(@NotNull Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        CancellableContinuationImpl cancellableContinuationImpl2 = cancellableContinuationImpl;
        CancellableContinuationKt.disposeOnCancellation(cancellableContinuationImpl2, invokeOnCompletion(new ResumeOnCompletion(this, cancellableContinuationImpl2)));
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    public final boolean makeCompleting$kotlinx_coroutines_core(@Nullable Object proposedUpdate) {
        while (true) {
            switch (tryMakeCompleting(getState$kotlinx_coroutines_core(), proposedUpdate, 0)) {
                case 0:
                    return false;
                case 1:
                case 2:
                    return true;
                case 3:
                default:
                    throw new IllegalStateException("unexpected result".toString());
            }
        }
    }

    public final boolean makeCompletingOnce$kotlinx_coroutines_core(@Nullable Object proposedUpdate, int mode) {
        while (true) {
            switch (tryMakeCompleting(getState$kotlinx_coroutines_core(), proposedUpdate, mode)) {
                case 0:
                    throw new IllegalStateException("Job " + this + " is already complete or completing, but is being completed with " + proposedUpdate, getExceptionOrNull(proposedUpdate));
                case 1:
                    return true;
                case 2:
                    return false;
                case 3:
                default:
                    throw new IllegalStateException("unexpected result".toString());
            }
        }
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    @NotNull
    public CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return Job.DefaultImpls.minusKey(this, key);
    }

    @NotNull
    public String nameString$kotlinx_coroutines_core() {
        return DebugKt.getClassSimpleName(this);
    }

    protected void onCancelling(@Nullable Throwable cause) {
    }

    protected void onCompletionInternal(@Nullable Object state) {
    }

    public void onStartInternal$kotlinx_coroutines_core() {
    }

    @Override // kotlinx.coroutines.ChildJob
    public final void parentCancelled(@NotNull ParentJob parentJob) {
        Intrinsics.checkParameterIsNotNull(parentJob, "parentJob");
        cancelImpl(parentJob);
    }

    @Override // kotlin.coroutines.CoroutineContext
    @NotNull
    public CoroutineContext plus(@NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return Job.DefaultImpls.plus(this, context);
    }

    @Override // kotlinx.coroutines.Job
    @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
    @NotNull
    public Job plus(@NotNull Job other) {
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Job.DefaultImpls.plus((Job) this, other);
    }

    @Override // kotlinx.coroutines.selects.SelectClause0
    public final <R> void registerSelectClause0(@NotNull SelectInstance<? super R> select, @NotNull Function1<? super Continuation<? super R>, ? extends Object> block) {
        Object state$kotlinx_coroutines_core;
        Intrinsics.checkParameterIsNotNull(select, "select");
        Intrinsics.checkParameterIsNotNull(block, "block");
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (select.isSelected()) {
                return;
            }
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                if (select.trySelect(null)) {
                    YieldKt.checkCompletion(select.getCompletion().getContext());
                    UndispatchedKt.startCoroutineUnintercepted(block, select.getCompletion());
                    return;
                }
                return;
            }
        } while (startInternal(state$kotlinx_coroutines_core) != 0);
        select.disposeOnSelect(invokeOnCompletion(new SelectJoinOnCompletion(this, select, block)));
    }

    public final <T, R> void registerSelectClause1Internal$kotlinx_coroutines_core(@NotNull SelectInstance<? super R> select, @NotNull Function2<? super T, ? super Continuation<? super R>, ? extends Object> block) {
        Object state$kotlinx_coroutines_core;
        Intrinsics.checkParameterIsNotNull(select, "select");
        Intrinsics.checkParameterIsNotNull(block, "block");
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (select.isSelected()) {
                return;
            }
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                if (select.trySelect(null)) {
                    if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
                        select.resumeSelectCancellableWithException(((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
                        return;
                    } else {
                        UndispatchedKt.startCoroutineUnintercepted(block, JobSupportKt.unboxState(state$kotlinx_coroutines_core), select.getCompletion());
                        return;
                    }
                }
                return;
            }
        } while (startInternal(state$kotlinx_coroutines_core) != 0);
        select.disposeOnSelect(invokeOnCompletion(new SelectAwaitOnCompletion(this, select, block)));
    }

    public final void removeNode$kotlinx_coroutines_core(@NotNull JobNode<?> node) {
        Object state$kotlinx_coroutines_core;
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater;
        Empty empty;
        Intrinsics.checkParameterIsNotNull(node, "node");
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof JobNode)) {
                if (!(state$kotlinx_coroutines_core instanceof Incomplete) || ((Incomplete) state$kotlinx_coroutines_core).getList() == null) {
                    return;
                }
                node.remove();
                return;
            }
            if (state$kotlinx_coroutines_core != node) {
                return;
            }
            atomicReferenceFieldUpdater = _state$FU;
            empty = JobSupportKt.EMPTY_ACTIVE;
        } while (!atomicReferenceFieldUpdater.compareAndSet(this, state$kotlinx_coroutines_core, empty));
    }

    public final <T, R> void selectAwaitCompletion$kotlinx_coroutines_core(@NotNull SelectInstance<? super R> select, @NotNull Function2<? super T, ? super Continuation<? super R>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(select, "select");
        Intrinsics.checkParameterIsNotNull(block, "block");
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            select.resumeSelectCancellableWithException(((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
        } else {
            CancellableKt.startCoroutineCancellable(block, JobSupportKt.unboxState(state$kotlinx_coroutines_core), select.getCompletion());
        }
    }

    @Override // kotlinx.coroutines.Job
    public final boolean start() {
        while (true) {
            switch (startInternal(getState$kotlinx_coroutines_core())) {
                case 0:
                    return false;
                case 1:
                    return true;
            }
        }
    }

    @NotNull
    protected final CancellationException toCancellationException(@NotNull Throwable toCancellationException, @Nullable String str) {
        Intrinsics.checkParameterIsNotNull(toCancellationException, "$this$toCancellationException");
        CancellationException cancellationException = (CancellationException) (!(toCancellationException instanceof CancellationException) ? null : toCancellationException);
        if (cancellationException != null) {
            return cancellationException;
        }
        if (str == null) {
            str = DebugKt.getClassSimpleName(toCancellationException) + " was cancelled";
        }
        return new JobCancellationException(str, toCancellationException, this);
    }

    @InternalCoroutinesApi
    @NotNull
    public final String toDebugString() {
        return nameString$kotlinx_coroutines_core() + '{' + stateString(getState$kotlinx_coroutines_core()) + '}';
    }

    @NotNull
    public String toString() {
        return toDebugString() + '@' + DebugKt.getHexAddress(this);
    }
}
