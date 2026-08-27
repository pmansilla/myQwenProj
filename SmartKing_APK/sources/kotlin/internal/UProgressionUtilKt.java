package kotlin.internal;

import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UnsignedKt;

/* compiled from: UProgressionUtil.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012"}, d2 = {"differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class UProgressionUtilKt {
    /* renamed from: differenceModulo-WZ9TVnA, reason: not valid java name */
    private static final int m864differenceModuloWZ9TVnA(int i, int i2, int i3) {
        int m317uintRemainderJ1ME1BU = UnsignedKt.m317uintRemainderJ1ME1BU(i, i3);
        int m317uintRemainderJ1ME1BU2 = UnsignedKt.m317uintRemainderJ1ME1BU(i2, i3);
        return UInt.m118constructorimpl(UnsignedKt.uintCompare(m317uintRemainderJ1ME1BU, m317uintRemainderJ1ME1BU2) >= 0 ? m317uintRemainderJ1ME1BU - m317uintRemainderJ1ME1BU2 : UInt.m118constructorimpl(m317uintRemainderJ1ME1BU - m317uintRemainderJ1ME1BU2) + i3);
    }

    /* renamed from: differenceModulo-sambcqE, reason: not valid java name */
    private static final long m865differenceModulosambcqE(long j, long j2, long j3) {
        long m319ulongRemaindereb3DHEI = UnsignedKt.m319ulongRemaindereb3DHEI(j, j3);
        long m319ulongRemaindereb3DHEI2 = UnsignedKt.m319ulongRemaindereb3DHEI(j2, j3);
        return ULong.m187constructorimpl(UnsignedKt.ulongCompare(m319ulongRemaindereb3DHEI, m319ulongRemaindereb3DHEI2) >= 0 ? m319ulongRemaindereb3DHEI - m319ulongRemaindereb3DHEI2 : ULong.m187constructorimpl(m319ulongRemaindereb3DHEI - m319ulongRemaindereb3DHEI2) + j3);
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    /* renamed from: getProgressionLastElement-7ftBX0g, reason: not valid java name */
    public static final long m866getProgressionLastElement7ftBX0g(long j, long j2, long j3) {
        if (j3 > 0) {
            return UnsignedKt.ulongCompare(j, j2) >= 0 ? j2 : ULong.m187constructorimpl(j2 - m865differenceModulosambcqE(j2, j, ULong.m187constructorimpl(j3)));
        }
        if (j3 < 0) {
            return UnsignedKt.ulongCompare(j, j2) <= 0 ? j2 : ULong.m187constructorimpl(j2 + m865differenceModulosambcqE(j, j2, ULong.m187constructorimpl(-j3)));
        }
        throw new IllegalArgumentException("Step is zero.");
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    /* renamed from: getProgressionLastElement-Nkh28Cs, reason: not valid java name */
    public static final int m867getProgressionLastElementNkh28Cs(int i, int i2, int i3) {
        if (i3 > 0) {
            return UnsignedKt.uintCompare(i, i2) >= 0 ? i2 : UInt.m118constructorimpl(i2 - m864differenceModuloWZ9TVnA(i2, i, UInt.m118constructorimpl(i3)));
        }
        if (i3 < 0) {
            return UnsignedKt.uintCompare(i, i2) <= 0 ? i2 : UInt.m118constructorimpl(i2 + m864differenceModuloWZ9TVnA(i, i2, UInt.m118constructorimpl(-i3)));
        }
        throw new IllegalArgumentException("Step is zero.");
    }
}
