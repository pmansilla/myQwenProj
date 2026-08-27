package kotlinx.coroutines.flow;

import com.sun.mail.imap.IMAPStore;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ProduceKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.channels.RendezvousChannel;
import kotlinx.coroutines.selects.SelectBuilder;
import org.jetbrains.annotations.NotNull;

/* compiled from: Zip.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000N\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00032\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0002¢\u0006\u0002\b\u0006\u001a#\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00020\b*\u00020\u00032\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0002¢\u0006\u0002\b\t\u001ah\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0005\"\u0004\b\u0000\u0010\f\"\u0004\b\u0001\u0010\r\"\u0004\b\u0002\u0010\u000b*\b\u0012\u0004\u0012\u0002H\f0\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\r0\u00052(\u0010\u000f\u001a$\b\u0001\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0010ø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001ax\u0010\u0013\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00140\u00152\u0006\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00020\b2\u000e\b\u0004\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00140\u001a23\b\b\u0010\u0013\u001a-\b\u0001\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u001c\u0012\b\b\u001d\u0012\u0004\b\b(\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u001bH\u0082\bø\u0001\u0000¢\u0006\u0004\b\u001f\u0010 \u001ah\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0005\"\u0004\b\u0000\u0010\f\"\u0004\b\u0001\u0010\r\"\u0004\b\u0002\u0010\u000b*\b\u0012\u0004\u0012\u0002H\f0\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\r0\u00052(\u0010\u000f\u001a$\b\u0001\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0010ø\u0001\u0000¢\u0006\u0002\u0010\u0012\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\""}, d2 = {"asChannel", "Lkotlinx/coroutines/channels/ReceiveChannel;", "", "Lkotlinx/coroutines/CoroutineScope;", "flow", "Lkotlinx/coroutines/flow/Flow;", "asChannel$FlowKt__ZipKt", "asFairChannel", "Lkotlinx/coroutines/channels/Channel;", "asFairChannel$FlowKt__ZipKt", "combineLatest", "R", "T1", "T2", "other", "transform", "Lkotlin/Function3;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "onReceive", "", "Lkotlinx/coroutines/selects/SelectBuilder;", "isClosed", "", "channel", "onClosed", "Lkotlin/Function0;", "Lkotlin/Function2;", "Lkotlin/ParameterName;", IMAPStore.ID_NAME, "value", "onReceive$FlowKt__ZipKt", "(Lkotlinx/coroutines/selects/SelectBuilder;ZLkotlinx/coroutines/channels/Channel;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function2;)V", "zip", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes2.dex */
final /* synthetic */ class FlowKt__ZipKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final ReceiveChannel<Object> asChannel$FlowKt__ZipKt(@NotNull CoroutineScope coroutineScope, Flow<?> flow) {
        return ProduceKt.produce$default(coroutineScope, null, 0, new FlowKt__ZipKt$asChannel$1(flow, null), 3, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Channel<Object> asFairChannel$FlowKt__ZipKt(@NotNull CoroutineScope coroutineScope, Flow<?> flow) {
        RendezvousChannel rendezvousChannel = new RendezvousChannel();
        BuildersKt__Builders_commonKt.launch$default(coroutineScope, null, null, new FlowKt__ZipKt$asFairChannel$1(flow, rendezvousChannel, null), 3, null);
        return rendezvousChannel;
    }

    @NotNull
    public static final <T1, T2, R> Flow<R> combineLatest(@NotNull Flow<? extends T1> combineLatest, @NotNull Flow<? extends T2> other, @NotNull Function3<? super T1, ? super T2, ? super Continuation<? super R>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(combineLatest, "$this$combineLatest");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return FlowKt.unsafeFlow(new FlowKt__ZipKt$combineLatest$1(combineLatest, other, transform, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onReceive$FlowKt__ZipKt(@NotNull SelectBuilder<? super Unit> selectBuilder, boolean z, Channel<Object> channel, Function0<Unit> function0, Function2<Object, ? super Continuation<? super Unit>, ? extends Object> function2) {
        if (z) {
            return;
        }
        selectBuilder.invoke(channel.getOnReceiveOrNull(), new FlowKt__ZipKt$onReceive$1(function0, function2, null));
    }

    @NotNull
    public static final <T1, T2, R> Flow<R> zip(@NotNull Flow<? extends T1> zip, @NotNull Flow<? extends T2> other, @NotNull Function3<? super T1, ? super T2, ? super Continuation<? super R>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(zip, "$this$zip");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return FlowKt.unsafeFlow(new FlowKt__ZipKt$zip$1(zip, other, transform, null));
    }
}
