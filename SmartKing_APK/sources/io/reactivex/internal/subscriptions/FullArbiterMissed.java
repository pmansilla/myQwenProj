package io.reactivex.internal.subscriptions;

import java.util.concurrent.atomic.AtomicLong;

/* compiled from: FullArbiter.java */
/* loaded from: classes2.dex */
class FullArbiterMissed extends FullArbiterPad1 {
    final AtomicLong missedRequested = new AtomicLong();
}
