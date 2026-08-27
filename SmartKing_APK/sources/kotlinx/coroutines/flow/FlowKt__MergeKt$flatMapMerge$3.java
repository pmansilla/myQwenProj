package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: Add missing generic type declarations: [R] */
/* compiled from: Merge.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3", f = "Merge.kt", i = {}, l = {48}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes2.dex */
public final class FlowKt__MergeKt$flatMapMerge$3<R> extends SuspendLambda implements Function2<FlowCollector<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ int $bufferSize;
    final /* synthetic */ int $concurrency;
    final /* synthetic */ Flow $this_flatMapMerge;
    final /* synthetic */ Function2 $transform;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Merge.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
    @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1", f = "Merge.kt", i = {0, 0}, l = {51}, m = "invokeSuspend", n = {"semaphore", "flatMap"}, s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1, reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ FlowCollector $receiver$0;
        Object L$0;
        Object L$1;
        int label;
        private CoroutineScope p$;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX INFO: Add missing generic type declarations: [T] */
        /* compiled from: Merge.kt */
        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "outerValue", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
        @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1", f = "Merge.kt", i = {}, l = {53, 54}, m = "invokeSuspend", n = {}, s = {})
        /* renamed from: kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1, reason: invalid class name and collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C00851<T> extends SuspendLambda implements Function2<T, Continuation<? super Unit>, Object> {
            final /* synthetic */ SerializingFlatMapCollector $flatMap;
            final /* synthetic */ CoroutineScope $receiver$0;
            final /* synthetic */ Channel $semaphore;
            Object L$0;
            int label;
            private Object p$0;

            /* JADX INFO: Access modifiers changed from: package-private */
            /* compiled from: Merge.kt */
            @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
            @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1$1", f = "Merge.kt", i = {}, l = {57, 61, 61}, m = "invokeSuspend", n = {}, s = {})
            /* renamed from: kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1$1, reason: invalid class name and collision with other inner class name */
            /* loaded from: classes2.dex */
            public static final class C00861 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
                final /* synthetic */ Flow $inner;
                Object L$0;
                int label;
                private CoroutineScope p$;

                /* JADX INFO: Access modifiers changed from: package-private */
                /* compiled from: Merge.kt */
                @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "value", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
                @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1$1$1", f = "Merge.kt", i = {}, l = {58}, m = "invokeSuspend", n = {}, s = {})
                /* renamed from: kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1$1$1, reason: invalid class name and collision with other inner class name */
                /* loaded from: classes2.dex */
                public static final class C00871 extends SuspendLambda implements Function2<R, Continuation<? super Unit>, Object> {
                    int label;
                    private Object p$0;

                    C00871(Continuation continuation) {
                        super(2, continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @NotNull
                    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
                        Intrinsics.checkParameterIsNotNull(completion, "completion");
                        C00871 c00871 = new C00871(completion);
                        c00871.p$0 = obj;
                        return c00871;
                    }

                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
                        return ((C00871) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch (this.label) {
                            case 0:
                                ResultKt.throwOnFailure(obj);
                                Object obj2 = this.p$0;
                                SerializingFlatMapCollector serializingFlatMapCollector = C00851.this.$flatMap;
                                this.label = 1;
                                if (serializingFlatMapCollector.emit(obj2, this) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                break;
                            case 1:
                                ResultKt.throwOnFailure(obj);
                                break;
                            default:
                                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        return Unit.INSTANCE;
                    }
                }

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                C00861(Flow flow, Continuation continuation) {
                    super(2, continuation);
                    this.$inner = flow;
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                @NotNull
                public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
                    Intrinsics.checkParameterIsNotNull(completion, "completion");
                    C00861 c00861 = new C00861(this.$inner, completion);
                    c00861.p$ = (CoroutineScope) obj;
                    return c00861;
                }

                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                    return ((C00861) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
                }

                /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0006. Please report as an issue. */
                /* JADX WARN: Removed duplicated region for block: B:15:0x004a A[RETURN] */
                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                @org.jetbrains.annotations.Nullable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r4) {
                    /*
                        r3 = this;
                        java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                        int r1 = r3.label
                        switch(r1) {
                            case 0: goto L24;
                            case 1: goto L1e;
                            case 2: goto L1a;
                            case 3: goto L11;
                            default: goto L9;
                        }
                    L9:
                        java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
                        java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                        r4.<init>(r0)
                        throw r4
                    L11:
                        java.lang.Object r0 = r3.L$0
                        java.lang.Throwable r0 = (java.lang.Throwable) r0
                        kotlin.ResultKt.throwOnFailure(r4)
                        r4 = r0
                        goto L5e
                    L1a:
                        kotlin.ResultKt.throwOnFailure(r4)
                        goto L4b
                    L1e:
                        kotlin.ResultKt.throwOnFailure(r4)     // Catch: java.lang.Throwable -> L22
                        goto L3d
                    L22:
                        r4 = move-exception
                        goto L4e
                    L24:
                        kotlin.ResultKt.throwOnFailure(r4)
                        kotlinx.coroutines.CoroutineScope r4 = r3.p$
                        kotlinx.coroutines.flow.Flow r4 = r3.$inner     // Catch: java.lang.Throwable -> L22
                        kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1$1$1 r1 = new kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1$1$1     // Catch: java.lang.Throwable -> L22
                        r2 = 0
                        r1.<init>(r2)     // Catch: java.lang.Throwable -> L22
                        kotlin.jvm.functions.Function2 r1 = (kotlin.jvm.functions.Function2) r1     // Catch: java.lang.Throwable -> L22
                        r2 = 1
                        r3.label = r2     // Catch: java.lang.Throwable -> L22
                        java.lang.Object r4 = kotlinx.coroutines.flow.FlowKt.collect(r4, r1, r3)     // Catch: java.lang.Throwable -> L22
                        if (r4 != r0) goto L3d
                        return r0
                    L3d:
                        kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1 r4 = kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3.AnonymousClass1.C00851.this
                        kotlinx.coroutines.channels.Channel r4 = r4.$semaphore
                        r1 = 2
                        r3.label = r1
                        java.lang.Object r4 = r4.receive(r3)
                        if (r4 != r0) goto L4b
                        return r0
                    L4b:
                        kotlin.Unit r4 = kotlin.Unit.INSTANCE
                        return r4
                    L4e:
                        kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1 r1 = kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3.AnonymousClass1.C00851.this
                        kotlinx.coroutines.channels.Channel r1 = r1.$semaphore
                        r3.L$0 = r4
                        r2 = 3
                        r3.label = r2
                        java.lang.Object r1 = r1.receive(r3)
                        if (r1 != r0) goto L5e
                        return r0
                    L5e:
                        throw r4
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3.AnonymousClass1.C00851.C00861.invokeSuspend(java.lang.Object):java.lang.Object");
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C00851(CoroutineScope coroutineScope, Channel channel, SerializingFlatMapCollector serializingFlatMapCollector, Continuation continuation) {
                super(2, continuation);
                this.$receiver$0 = coroutineScope;
                this.$semaphore = channel;
                this.$flatMap = serializingFlatMapCollector;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @NotNull
            public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
                Intrinsics.checkParameterIsNotNull(completion, "completion");
                C00851 c00851 = new C00851(this.$receiver$0, this.$semaphore, this.$flatMap, completion);
                c00851.p$0 = obj;
                return c00851;
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
                return ((C00851) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
            /* JADX WARN: Removed duplicated region for block: B:12:0x003f A[RETURN] */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @org.jetbrains.annotations.Nullable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r7) {
                /*
                    r6 = this;
                    java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                    int r1 = r6.label
                    switch(r1) {
                        case 0: goto L1b;
                        case 1: goto L15;
                        case 2: goto L11;
                        default: goto L9;
                    }
                L9:
                    java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
                    java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                    r7.<init>(r0)
                    throw r7
                L11:
                    kotlin.ResultKt.throwOnFailure(r7)
                    goto L40
                L15:
                    java.lang.Object r1 = r6.L$0
                    kotlin.ResultKt.throwOnFailure(r7)
                    goto L30
                L1b:
                    kotlin.ResultKt.throwOnFailure(r7)
                    java.lang.Object r1 = r6.p$0
                    kotlinx.coroutines.channels.Channel r7 = r6.$semaphore
                    kotlin.Unit r2 = kotlin.Unit.INSTANCE
                    r6.L$0 = r1
                    r3 = 1
                    r6.label = r3
                    java.lang.Object r7 = r7.send(r2, r6)
                    if (r7 != r0) goto L30
                    return r0
                L30:
                    kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1 r7 = kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3.AnonymousClass1.this
                    kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3 r7 = kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3.this
                    kotlin.jvm.functions.Function2 r7 = r7.$transform
                    r2 = 2
                    r6.label = r2
                    java.lang.Object r7 = r7.invoke(r1, r6)
                    if (r7 != r0) goto L40
                    return r0
                L40:
                    kotlinx.coroutines.flow.Flow r7 = (kotlinx.coroutines.flow.Flow) r7
                    kotlinx.coroutines.CoroutineScope r0 = r6.$receiver$0
                    r1 = 0
                    r2 = 0
                    kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1$1 r3 = new kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3$1$1$1
                    r4 = 0
                    r3.<init>(r7, r4)
                    kotlin.jvm.functions.Function2 r3 = (kotlin.jvm.functions.Function2) r3
                    r4 = 3
                    r5 = 0
                    kotlinx.coroutines.BuildersKt.launch$default(r0, r1, r2, r3, r4, r5)
                    kotlin.Unit r7 = kotlin.Unit.INSTANCE
                    return r7
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__MergeKt$flatMapMerge$3.AnonymousClass1.C00851.invokeSuspend(java.lang.Object):java.lang.Object");
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(FlowCollector flowCollector, Continuation continuation) {
            super(2, continuation);
            this.$receiver$0 = flowCollector;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
            Intrinsics.checkParameterIsNotNull(completion, "completion");
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.$receiver$0, completion);
            anonymousClass1.p$ = (CoroutineScope) obj;
            return anonymousClass1;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    CoroutineScope coroutineScope = this.p$;
                    Channel Channel = ChannelKt.Channel(FlowKt__MergeKt$flatMapMerge$3.this.$concurrency);
                    SerializingFlatMapCollector serializingFlatMapCollector = new SerializingFlatMapCollector(this.$receiver$0, FlowKt__MergeKt$flatMapMerge$3.this.$bufferSize);
                    Flow flow = FlowKt__MergeKt$flatMapMerge$3.this.$this_flatMapMerge;
                    C00851 c00851 = new C00851(coroutineScope, Channel, serializingFlatMapCollector, null);
                    this.L$0 = Channel;
                    this.L$1 = serializingFlatMapCollector;
                    this.label = 1;
                    if (FlowKt.collect(flow, c00851, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure(obj);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__MergeKt$flatMapMerge$3(Flow flow, int i, int i2, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_flatMapMerge = flow;
        this.$concurrency = i;
        this.$bufferSize = i2;
        this.$transform = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__MergeKt$flatMapMerge$3 flowKt__MergeKt$flatMapMerge$3 = new FlowKt__MergeKt$flatMapMerge$3(this.$this_flatMapMerge, this.$concurrency, this.$bufferSize, this.$transform, completion);
        flowKt__MergeKt$flatMapMerge$3.p$ = (FlowCollector) obj;
        return flowKt__MergeKt$flatMapMerge$3;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((FlowKt__MergeKt$flatMapMerge$3) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.p$, null);
                this.label = 1;
                if (CoroutineScopeKt.coroutineScope(anonymousClass1, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
