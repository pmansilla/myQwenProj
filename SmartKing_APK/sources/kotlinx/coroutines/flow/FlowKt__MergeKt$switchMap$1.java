package kotlinx.coroutines.flow;

import com.czw.smartkit.BuildConfig;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: Add missing generic type declarations: [R] */
/* compiled from: Merge.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1", f = "Merge.kt", i = {}, l = {114}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes2.dex */
public final class FlowKt__MergeKt$switchMap$1<R> extends SuspendLambda implements Function2<FlowCollector<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Flow $this_switchMap;
    final /* synthetic */ Function2 $transform;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Merge.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
    @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1", f = "Merge.kt", i = {0}, l = {116}, m = "invokeSuspend", n = {"previousFlow"}, s = {"L$0"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1, reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ FlowCollector $receiver$0;
        Object L$0;
        int label;
        private CoroutineScope p$;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX INFO: Add missing generic type declarations: [T] */
        /* compiled from: Merge.kt */
        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "value", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
        @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1$1", f = "Merge.kt", i = {}, l = {118}, m = "invokeSuspend", n = {}, s = {})
        /* renamed from: kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1$1, reason: invalid class name and collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C00891<T> extends SuspendLambda implements Function2<T, Continuation<? super Unit>, Object> {
            final /* synthetic */ Ref.ObjectRef $previousFlow;
            final /* synthetic */ CoroutineScope $receiver$0;
            Object L$0;
            int label;
            private Object p$0;

            /* JADX INFO: Access modifiers changed from: package-private */
            /* compiled from: Merge.kt */
            @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
            @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1$1$1", f = "Merge.kt", i = {}, l = {BuildConfig.VERSION_CODE, BuildConfig.VERSION_CODE}, m = "invokeSuspend", n = {}, s = {})
            /* renamed from: kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1$1$1, reason: invalid class name and collision with other inner class name */
            /* loaded from: classes2.dex */
            public static final class C00901 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
                final /* synthetic */ Object $value;
                int label;
                private CoroutineScope p$;

                /* JADX INFO: Access modifiers changed from: package-private */
                /* compiled from: Merge.kt */
                @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "innerValue", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
                @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1$1$1$1", f = "Merge.kt", i = {}, l = {122}, m = "invokeSuspend", n = {}, s = {})
                /* renamed from: kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1$1$1$1, reason: invalid class name and collision with other inner class name */
                /* loaded from: classes2.dex */
                public static final class C00911 extends SuspendLambda implements Function2<R, Continuation<? super Unit>, Object> {
                    int label;
                    private Object p$0;

                    C00911(Continuation continuation) {
                        super(2, continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @NotNull
                    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
                        Intrinsics.checkParameterIsNotNull(completion, "completion");
                        C00911 c00911 = new C00911(completion);
                        c00911.p$0 = obj;
                        return c00911;
                    }

                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
                        return ((C00911) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
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
                                FlowCollector flowCollector = AnonymousClass1.this.$receiver$0;
                                this.label = 1;
                                if (flowCollector.emit(obj2, this) == coroutine_suspended) {
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
                C00901(Object obj, Continuation continuation) {
                    super(2, continuation);
                    this.$value = obj;
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                @NotNull
                public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
                    Intrinsics.checkParameterIsNotNull(completion, "completion");
                    C00901 c00901 = new C00901(this.$value, completion);
                    c00901.p$ = (CoroutineScope) obj;
                    return c00901;
                }

                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                    return ((C00901) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
                }

                /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
                /* JADX WARN: Removed duplicated region for block: B:12:0x0045 A[RETURN] */
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
                            case 0: goto L19;
                            case 1: goto L15;
                            case 2: goto L11;
                            default: goto L9;
                        }
                    L9:
                        java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
                        java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                        r4.<init>(r0)
                        throw r4
                    L11:
                        kotlin.ResultKt.throwOnFailure(r4)
                        goto L46
                    L15:
                        kotlin.ResultKt.throwOnFailure(r4)
                        goto L32
                    L19:
                        kotlin.ResultKt.throwOnFailure(r4)
                        kotlinx.coroutines.CoroutineScope r4 = r3.p$
                        kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1$1 r4 = kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1.AnonymousClass1.C00891.this
                        kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1 r4 = kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1.AnonymousClass1.this
                        kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1 r4 = kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1.this
                        kotlin.jvm.functions.Function2 r4 = r4.$transform
                        java.lang.Object r1 = r3.$value
                        r2 = 1
                        r3.label = r2
                        java.lang.Object r4 = r4.invoke(r1, r3)
                        if (r4 != r0) goto L32
                        return r0
                    L32:
                        kotlinx.coroutines.flow.Flow r4 = (kotlinx.coroutines.flow.Flow) r4
                        kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1$1$1$1 r1 = new kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1$1$1$1$1
                        r2 = 0
                        r1.<init>(r2)
                        kotlin.jvm.functions.Function2 r1 = (kotlin.jvm.functions.Function2) r1
                        r2 = 2
                        r3.label = r2
                        java.lang.Object r4 = kotlinx.coroutines.flow.FlowKt.collect(r4, r1, r3)
                        if (r4 != r0) goto L46
                        return r0
                    L46:
                        kotlin.Unit r4 = kotlin.Unit.INSTANCE
                        return r4
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__MergeKt$switchMap$1.AnonymousClass1.C00891.C00901.invokeSuspend(java.lang.Object):java.lang.Object");
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C00891(CoroutineScope coroutineScope, Ref.ObjectRef objectRef, Continuation continuation) {
                super(2, continuation);
                this.$receiver$0 = coroutineScope;
                this.$previousFlow = objectRef;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @NotNull
            public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
                Intrinsics.checkParameterIsNotNull(completion, "completion");
                C00891 c00891 = new C00891(this.$receiver$0, this.$previousFlow, completion);
                c00891.p$0 = obj;
                return c00891;
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
                return ((C00891) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @Nullable
            public final Object invokeSuspend(@NotNull Object obj) {
                Object obj2;
                Object obj3;
                Job launch$default;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        obj2 = this.p$0;
                        Job job = (Job) this.$previousFlow.element;
                        if (job != null) {
                            this.L$0 = obj2;
                            this.label = 1;
                            Object cancelAndJoin = JobKt.cancelAndJoin(job, this);
                            if (cancelAndJoin == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            obj3 = obj2;
                            obj = cancelAndJoin;
                            obj2 = obj3;
                        }
                        Ref.ObjectRef objectRef = this.$previousFlow;
                        launch$default = BuildersKt__Builders_commonKt.launch$default(this.$receiver$0, null, CoroutineStart.UNDISPATCHED, new C00901(obj2, null), 1, null);
                        objectRef.element = (T) launch$default;
                        return Unit.INSTANCE;
                    case 1:
                        obj3 = this.L$0;
                        ResultKt.throwOnFailure(obj);
                        obj2 = obj3;
                        Ref.ObjectRef objectRef2 = this.$previousFlow;
                        launch$default = BuildersKt__Builders_commonKt.launch$default(this.$receiver$0, null, CoroutineStart.UNDISPATCHED, new C00901(obj2, null), 1, null);
                        objectRef2.element = (T) launch$default;
                        return Unit.INSTANCE;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
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

        /* JADX WARN: Type inference failed for: r3v1, types: [T, kotlinx.coroutines.Job] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    CoroutineScope coroutineScope = this.p$;
                    Ref.ObjectRef objectRef = new Ref.ObjectRef();
                    objectRef.element = (Job) 0;
                    Flow flow = FlowKt__MergeKt$switchMap$1.this.$this_switchMap;
                    C00891 c00891 = new C00891(coroutineScope, objectRef, null);
                    this.L$0 = objectRef;
                    this.label = 1;
                    if (FlowKt.collect(flow, c00891, this) == coroutine_suspended) {
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
    public FlowKt__MergeKt$switchMap$1(Flow flow, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_switchMap = flow;
        this.$transform = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__MergeKt$switchMap$1 flowKt__MergeKt$switchMap$1 = new FlowKt__MergeKt$switchMap$1(this.$this_switchMap, this.$transform, completion);
        flowKt__MergeKt$switchMap$1.p$ = (FlowCollector) obj;
        return flowKt__MergeKt$switchMap$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((FlowKt__MergeKt$switchMap$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
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
