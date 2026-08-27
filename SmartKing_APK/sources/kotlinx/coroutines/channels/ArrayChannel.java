package kotlinx.coroutines.channels;

import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.AbstractSendChannel;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ArrayChannel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u001b\u001a\u00020\u001cH\u0014J\u0015\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u001fJ!\u0010 \u001a\u00020\b2\u0006\u0010\u001e\u001a\u00028\u00002\n\u0010!\u001a\u0006\u0012\u0002\b\u00030\"H\u0014¢\u0006\u0002\u0010#J\n\u0010$\u001a\u0004\u0018\u00010\bH\u0014J\u0016\u0010%\u001a\u0004\u0018\u00010\b2\n\u0010!\u001a\u0006\u0012\u0002\b\u00030\"H\u0014R\u0018\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0007X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\tR\u0014\u0010\n\u001a\u00020\u000b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0013R\u0014\u0010\u0015\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0013R\u0014\u0010\u0016\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0013R\u0012\u0010\u0017\u001a\u00060\u0018j\u0002`\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lkotlinx/coroutines/channels/ArrayChannel;", "E", "Lkotlinx/coroutines/channels/AbstractChannel;", "capacity", "", "(I)V", "buffer", "", "", "[Ljava/lang/Object;", "bufferDebugString", "", "getBufferDebugString", "()Ljava/lang/String;", "getCapacity", "()I", "head", "isBufferAlwaysEmpty", "", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "size", "cleanupSendQueueOnCancel", "", "offerInternal", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "pollInternal", "pollSelectInternal", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public class ArrayChannel<E> extends AbstractChannel<E> {
    private final Object[] buffer;
    private final int capacity;
    private int head;
    private final ReentrantLock lock;
    private volatile int size;

    public ArrayChannel(int i) {
        this.capacity = i;
        if (this.capacity >= 1) {
            this.lock = new ReentrantLock();
            this.buffer = new Object[this.capacity];
        } else {
            throw new IllegalArgumentException(("ArrayChannel capacity must be at least 1, but " + this.capacity + " was specified").toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public void cleanupSendQueueOnCancel() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i = this.size;
            for (int i2 = 0; i2 < i; i2++) {
                this.buffer[this.head] = 0;
                this.head = (this.head + 1) % this.capacity;
            }
            this.size = 0;
            Unit unit = Unit.INSTANCE;
            reentrantLock.unlock();
            super.cleanupSendQueueOnCancel();
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    @NotNull
    protected String getBufferDebugString() {
        return "(buffer:capacity=" + this.buffer.length + ",size=" + this.size + ')';
    }

    public final int getCapacity() {
        return this.capacity;
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected final boolean isBufferAlwaysEmpty() {
        return false;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected final boolean isBufferAlwaysFull() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferEmpty() {
        return this.size == 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferFull() {
        return this.size == this.capacity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x001f, code lost:
    
        if (r2 == 0) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0021, code lost:
    
        r3 = takeFirstReceiveOrPeekClosed();
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0025, code lost:
    
        if (r3 == null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0029, code lost:
    
        if ((r3 instanceof kotlinx.coroutines.channels.Closed) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0036, code lost:
    
        if (r3 != null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0038, code lost:
    
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x003b, code lost:
    
        r4 = r3.tryResumeReceive(r6, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x003f, code lost:
    
        if (r4 == null) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0041, code lost:
    
        r5.size = r2;
        r6 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0048, code lost:
    
        if (r3 != null) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x004a, code lost:
    
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x004d, code lost:
    
        r3.completeResumeReceive(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0050, code lost:
    
        if (r3 != null) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0052, code lost:
    
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0059, code lost:
    
        return r3.getOfferResult();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x002b, code lost:
    
        r5.size = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x002d, code lost:
    
        if (r3 != null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x002f, code lost:
    
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0035, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x005a, code lost:
    
        r5.buffer[(r5.head + r2) % r5.capacity] = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0069, code lost:
    
        return kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS;
     */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    @org.jetbrains.annotations.NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object offerInternal(E r6) {
        /*
            r5 = this;
            r0 = 0
            r1 = r0
            kotlinx.coroutines.channels.ReceiveOrClosed r1 = (kotlinx.coroutines.channels.ReceiveOrClosed) r1
            java.util.concurrent.locks.ReentrantLock r1 = r5.lock
            java.util.concurrent.locks.Lock r1 = (java.util.concurrent.locks.Lock) r1
            r1.lock()
            int r2 = r5.size     // Catch: java.lang.Throwable -> L70
            kotlinx.coroutines.channels.Closed r3 = r5.getClosedForSend()     // Catch: java.lang.Throwable -> L70
            if (r3 == 0) goto L17
            r1.unlock()
            return r3
        L17:
            int r3 = r5.capacity     // Catch: java.lang.Throwable -> L70
            if (r2 >= r3) goto L6a
            int r3 = r2 + 1
            r5.size = r3     // Catch: java.lang.Throwable -> L70
            if (r2 != 0) goto L5a
        L21:
            kotlinx.coroutines.channels.ReceiveOrClosed r3 = r5.takeFirstReceiveOrPeekClosed()     // Catch: java.lang.Throwable -> L70
            if (r3 == 0) goto L5a
            boolean r4 = r3 instanceof kotlinx.coroutines.channels.Closed     // Catch: java.lang.Throwable -> L70
            if (r4 == 0) goto L36
            r5.size = r2     // Catch: java.lang.Throwable -> L70
            if (r3 != 0) goto L32
            kotlin.jvm.internal.Intrinsics.throwNpe()     // Catch: java.lang.Throwable -> L70
        L32:
            r1.unlock()
            return r3
        L36:
            if (r3 != 0) goto L3b
            kotlin.jvm.internal.Intrinsics.throwNpe()     // Catch: java.lang.Throwable -> L70
        L3b:
            java.lang.Object r4 = r3.tryResumeReceive(r6, r0)     // Catch: java.lang.Throwable -> L70
            if (r4 == 0) goto L21
            r5.size = r2     // Catch: java.lang.Throwable -> L70
            kotlin.Unit r6 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L70
            r1.unlock()
            if (r3 != 0) goto L4d
            kotlin.jvm.internal.Intrinsics.throwNpe()
        L4d:
            r3.completeResumeReceive(r4)
            if (r3 != 0) goto L55
            kotlin.jvm.internal.Intrinsics.throwNpe()
        L55:
            java.lang.Object r6 = r3.getOfferResult()
            return r6
        L5a:
            java.lang.Object[] r0 = r5.buffer     // Catch: java.lang.Throwable -> L70
            int r3 = r5.head     // Catch: java.lang.Throwable -> L70
            int r3 = r3 + r2
            int r2 = r5.capacity     // Catch: java.lang.Throwable -> L70
            int r3 = r3 % r2
            r0[r3] = r6     // Catch: java.lang.Throwable -> L70
            java.lang.Object r6 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS     // Catch: java.lang.Throwable -> L70
            r1.unlock()
            return r6
        L6a:
            java.lang.Object r6 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_FAILED     // Catch: java.lang.Throwable -> L70
            r1.unlock()
            return r6
        L70:
            r6 = move-exception
            r1.unlock()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ArrayChannel.offerInternal(java.lang.Object):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    @NotNull
    public Object offerSelectInternal(E element, @NotNull SelectInstance<?> select) {
        Intrinsics.checkParameterIsNotNull(select, "select");
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i = this.size;
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend != null) {
                return closedForSend;
            }
            if (i >= this.capacity) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            this.size = i + 1;
            if (i == 0) {
                AbstractSendChannel.TryOfferDesc<E> describeTryOffer = describeTryOffer(element);
                Object performAtomicTrySelect = select.performAtomicTrySelect(describeTryOffer);
                if (performAtomicTrySelect == null) {
                    this.size = i;
                    ReceiveOrClosed<? super E> result = describeTryOffer.getResult();
                    Object obj = describeTryOffer.resumeToken;
                    if (!(obj != null)) {
                        throw new IllegalStateException("Check failed.".toString());
                    }
                    Unit unit = Unit.INSTANCE;
                    if (result == null) {
                        Intrinsics.throwNpe();
                    }
                    if (obj == null) {
                        Intrinsics.throwNpe();
                    }
                    result.completeResumeReceive(obj);
                    if (result == null) {
                        Intrinsics.throwNpe();
                    }
                    return result.getOfferResult();
                }
                if (performAtomicTrySelect != AbstractChannelKt.OFFER_FAILED) {
                    if (performAtomicTrySelect != SelectKt.getALREADY_SELECTED() && !(performAtomicTrySelect instanceof Closed)) {
                        throw new IllegalStateException(("performAtomicTrySelect(describeTryOffer) returned " + performAtomicTrySelect).toString());
                    }
                    this.size = i;
                    return performAtomicTrySelect;
                }
            }
            if (select.trySelect(null)) {
                this.buffer[(this.head + i) % this.capacity] = element;
                return AbstractChannelKt.OFFER_SUCCESS;
            }
            this.size = i;
            return SelectKt.getALREADY_SELECTED();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    @Nullable
    protected Object pollInternal() {
        Send send;
        Object obj;
        Send send2 = (Send) null;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i = this.size;
            if (i == 0) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            Object obj2 = this.buffer[this.head];
            this.buffer[this.head] = null;
            this.size = i - 1;
            Object obj3 = AbstractChannelKt.POLL_FAILED;
            if (i == this.capacity) {
                send = send2;
                obj = null;
                while (true) {
                    Send takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
                    if (takeFirstSendOrPeekClosed == null) {
                        break;
                    }
                    if (takeFirstSendOrPeekClosed == null) {
                        Intrinsics.throwNpe();
                    }
                    obj = takeFirstSendOrPeekClosed.tryResumeSend(null);
                    if (obj != null) {
                        if (takeFirstSendOrPeekClosed == null) {
                            Intrinsics.throwNpe();
                        }
                        obj3 = takeFirstSendOrPeekClosed.getPollResult();
                        send = takeFirstSendOrPeekClosed;
                    } else {
                        send = takeFirstSendOrPeekClosed;
                    }
                }
            } else {
                send = send2;
                obj = null;
            }
            if (obj3 != AbstractChannelKt.POLL_FAILED && !(obj3 instanceof Closed)) {
                this.size = i;
                this.buffer[(this.head + i) % this.capacity] = obj3;
            }
            this.head = (this.head + 1) % this.capacity;
            Unit unit = Unit.INSTANCE;
            if (obj != null) {
                if (send == null) {
                    Intrinsics.throwNpe();
                }
                send.completeResumeSend(obj);
            }
            return obj2;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00d3 A[Catch: all -> 0x00fb, TRY_LEAVE, TryCatch #0 {all -> 0x00fb, blocks: (B:3:0x0010, B:5:0x0014, B:11:0x001b, B:12:0x0021, B:14:0x0038, B:16:0x0045, B:21:0x0056, B:22:0x0059, B:23:0x00b8, B:25:0x00bc, B:27:0x00c0, B:28:0x00e3, B:35:0x00cd, B:37:0x00d3, B:40:0x0062, B:41:0x006f, B:43:0x0070, B:46:0x0075, B:48:0x007b, B:51:0x0087, B:53:0x008b, B:54:0x0099, B:55:0x00b5), top: B:2:0x0010 }] */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected java.lang.Object pollSelectInternal(@org.jetbrains.annotations.NotNull kotlinx.coroutines.selects.SelectInstance<?> r11) {
        /*
            Method dump skipped, instructions count: 256
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ArrayChannel.pollSelectInternal(kotlinx.coroutines.selects.SelectInstance):java.lang.Object");
    }
}
