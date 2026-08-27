package kotlinx.coroutines.flow;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelKt;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Merge.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u001b\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00028\u0000H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u0011\u0010\u0010\u001a\u00020\rH\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0011R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/flow/SerializingFlatMapCollector;", "T", "", "downstream", "Lkotlinx/coroutines/flow/FlowCollector;", "bufferSize", "", "(Lkotlinx/coroutines/flow/FlowCollector;I)V", "channel", "Lkotlinx/coroutines/channels/Channel;", "inProgressLock", "Lkotlinx/atomicfu/AtomicBoolean;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "helpEmit", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class SerializingFlatMapCollector<T> {
    private static final AtomicIntegerFieldUpdater inProgressLock$FU = AtomicIntegerFieldUpdater.newUpdater(SerializingFlatMapCollector.class, "inProgressLock");
    private final Channel<Object> channel;
    private final FlowCollector<T> downstream;
    private volatile int inProgressLock;

    /* JADX WARN: Multi-variable type inference failed */
    public SerializingFlatMapCollector(@NotNull FlowCollector<? super T> downstream, int i) {
        Intrinsics.checkParameterIsNotNull(downstream, "downstream");
        this.downstream = downstream;
        this.channel = ChannelKt.Channel(i);
        this.inProgressLock = 0;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0023. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:11:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x00b1 A[PHI: r7
      0x00b1: PHI (r7v13 java.lang.Object) = (r7v12 java.lang.Object), (r7v1 java.lang.Object) binds: [B:15:0x00ae, B:11:0x002e] A[DONT_GENERATE, DONT_INLINE], RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00b0 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0026  */
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object emit(T r6, @org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<? super kotlin.Unit> r7) {
        /*
            r5 = this;
            boolean r0 = r7 instanceof kotlinx.coroutines.flow.SerializingFlatMapCollector$emit$1
            if (r0 == 0) goto L14
            r0 = r7
            kotlinx.coroutines.flow.SerializingFlatMapCollector$emit$1 r0 = (kotlinx.coroutines.flow.SerializingFlatMapCollector$emit$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r7 = r0.label
            int r7 = r7 - r2
            r0.label = r7
            goto L19
        L14:
            kotlinx.coroutines.flow.SerializingFlatMapCollector$emit$1 r0 = new kotlinx.coroutines.flow.SerializingFlatMapCollector$emit$1
            r0.<init>(r5, r7)
        L19:
            java.lang.Object r7 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 0
            r4 = 1
            switch(r2) {
                case 0: goto L57;
                case 1: goto L4d;
                case 2: goto L43;
                case 3: goto L39;
                case 4: goto L2e;
                default: goto L26;
            }
        L26:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L2e:
            java.lang.Object r6 = r0.L$1
            java.lang.Object r6 = r0.L$0
            kotlinx.coroutines.flow.SerializingFlatMapCollector r6 = (kotlinx.coroutines.flow.SerializingFlatMapCollector) r6
            kotlin.ResultKt.throwOnFailure(r7)
            goto Lb1
        L39:
            java.lang.Object r6 = r0.L$1
            java.lang.Object r2 = r0.L$0
            kotlinx.coroutines.flow.SerializingFlatMapCollector r2 = (kotlinx.coroutines.flow.SerializingFlatMapCollector) r2
            kotlin.ResultKt.throwOnFailure(r7)
            goto La3
        L43:
            java.lang.Object r6 = r0.L$1
            java.lang.Object r6 = r0.L$0
            kotlinx.coroutines.flow.SerializingFlatMapCollector r6 = (kotlinx.coroutines.flow.SerializingFlatMapCollector) r6
            kotlin.ResultKt.throwOnFailure(r7)
            goto L8e
        L4d:
            java.lang.Object r6 = r0.L$1
            java.lang.Object r2 = r0.L$0
            kotlinx.coroutines.flow.SerializingFlatMapCollector r2 = (kotlinx.coroutines.flow.SerializingFlatMapCollector) r2
            kotlin.ResultKt.throwOnFailure(r7)
            goto L78
        L57:
            kotlin.ResultKt.throwOnFailure(r7)
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r7 = kotlinx.coroutines.flow.SerializingFlatMapCollector.inProgressLock$FU
            boolean r7 = r7.compareAndSet(r5, r3, r4)
            if (r7 != 0) goto L92
            kotlinx.coroutines.channels.Channel<java.lang.Object> r7 = r5.channel
            if (r6 == 0) goto L68
            r2 = r6
            goto L6a
        L68:
            kotlinx.coroutines.flow.internal.NullSurrogate r2 = kotlinx.coroutines.flow.internal.NullSurrogate.INSTANCE
        L6a:
            r0.L$0 = r5
            r0.L$1 = r6
            r0.label = r4
            java.lang.Object r7 = r7.send(r2, r0)
            if (r7 != r1) goto L77
            return r1
        L77:
            r2 = r5
        L78:
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r7 = kotlinx.coroutines.flow.SerializingFlatMapCollector.inProgressLock$FU
            boolean r7 = r7.compareAndSet(r2, r3, r4)
            if (r7 == 0) goto L8f
            r0.L$0 = r2
            r0.L$1 = r6
            r6 = 2
            r0.label = r6
            java.lang.Object r7 = r2.helpEmit(r0)
            if (r7 != r1) goto L8e
            return r1
        L8e:
            return r7
        L8f:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        L92:
            kotlinx.coroutines.flow.FlowCollector<T> r7 = r5.downstream
            r0.L$0 = r5
            r0.L$1 = r6
            r2 = 3
            r0.label = r2
            java.lang.Object r7 = r7.emit(r6, r0)
            if (r7 != r1) goto La2
            return r1
        La2:
            r2 = r5
        La3:
            r0.L$0 = r2
            r0.L$1 = r6
            r6 = 4
            r0.label = r6
            java.lang.Object r7 = r2.helpEmit(r0)
            if (r7 != r1) goto Lb1
            return r1
        Lb1:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.SerializingFlatMapCollector.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0021. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0056 A[RETURN] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x0054 -> B:12:0x0057). Please report as a decompilation issue!!! */
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final /* synthetic */ java.lang.Object helpEmit(@org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<? super kotlin.Unit> r7) {
        /*
            r6 = this;
            boolean r0 = r7 instanceof kotlinx.coroutines.flow.SerializingFlatMapCollector$helpEmit$1
            if (r0 == 0) goto L14
            r0 = r7
            kotlinx.coroutines.flow.SerializingFlatMapCollector$helpEmit$1 r0 = (kotlinx.coroutines.flow.SerializingFlatMapCollector$helpEmit$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r7 = r0.label
            int r7 = r7 - r2
            r0.label = r7
            goto L19
        L14:
            kotlinx.coroutines.flow.SerializingFlatMapCollector$helpEmit$1 r0 = new kotlinx.coroutines.flow.SerializingFlatMapCollector$helpEmit$1
            r0.<init>(r6, r7)
        L19:
            java.lang.Object r7 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            switch(r2) {
                case 0: goto L37;
                case 1: goto L2c;
                default: goto L24;
            }
        L24:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>(r0)
            throw r7
        L2c:
            java.lang.Object r2 = r0.L$1
            java.lang.Object r2 = r0.L$0
            kotlinx.coroutines.flow.SerializingFlatMapCollector r2 = (kotlinx.coroutines.flow.SerializingFlatMapCollector) r2
            kotlin.ResultKt.throwOnFailure(r7)
            r7 = r2
            goto L57
        L37:
            kotlin.ResultKt.throwOnFailure(r7)
            r7 = r6
        L3b:
            kotlinx.coroutines.channels.Channel<java.lang.Object> r2 = r7.channel
            java.lang.Object r2 = r2.poll()
        L41:
            r3 = 1
            if (r2 == 0) goto L5e
            kotlinx.coroutines.flow.FlowCollector<T> r4 = r7.downstream
            java.lang.Object r5 = kotlinx.coroutines.flow.internal.NullSurrogate.unbox$kotlinx_coroutines_core(r2)
            r0.L$0 = r7
            r0.L$1 = r2
            r0.label = r3
            java.lang.Object r2 = r4.emit(r5, r0)
            if (r2 != r1) goto L57
            return r1
        L57:
            kotlinx.coroutines.channels.Channel<java.lang.Object> r2 = r7.channel
            java.lang.Object r2 = r2.poll()
            goto L41
        L5e:
            r2 = 0
            r7.inProgressLock = r2
            kotlinx.coroutines.channels.Channel<java.lang.Object> r4 = r7.channel
            boolean r4 = r4.isEmpty()
            if (r4 != 0) goto L71
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r4 = kotlinx.coroutines.flow.SerializingFlatMapCollector.inProgressLock$FU
            boolean r2 = r4.compareAndSet(r7, r2, r3)
            if (r2 != 0) goto L3b
        L71:
            kotlin.Unit r7 = kotlin.Unit.INSTANCE
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.SerializingFlatMapCollector.helpEmit(kotlin.coroutines.Continuation):java.lang.Object");
    }
}
