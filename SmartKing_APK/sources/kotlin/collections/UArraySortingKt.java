package kotlin.collections;

import com.amap.location.common.model.Adjacent;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: UArraySorting.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0012\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0013\u0010\u0014\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010\u001a\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\bH\u0001ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001f\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000bH\u0001ø\u0001\u0000¢\u0006\u0004\b \u0010!\u001a\u001a\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\"\u0010#\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006$"}, d2 = {"partition", "", "array", "Lkotlin/UByteArray;", Adjacent.LEFT, Adjacent.RIGHT, "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "sortArray-GBYM_sE", "([B)V", "sortArray--ajY-9A", "([I)V", "sortArray-QwZRm1k", "([J)V", "sortArray-rL5Bavg", "([S)V", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class UArraySortingKt {
    @ExperimentalUnsignedTypes
    /* renamed from: partition--nroSd4, reason: not valid java name */
    private static final int m324partitionnroSd4(long[] jArr, int i, int i2) {
        long m238getimpl = ULongArray.m238getimpl(jArr, (i + i2) / 2);
        while (i <= i2) {
            while (UnsignedKt.ulongCompare(ULongArray.m238getimpl(jArr, i), m238getimpl) < 0) {
                i++;
            }
            while (UnsignedKt.ulongCompare(ULongArray.m238getimpl(jArr, i2), m238getimpl) > 0) {
                i2--;
            }
            if (i <= i2) {
                long m238getimpl2 = ULongArray.m238getimpl(jArr, i);
                ULongArray.m243setk8EXiF4(jArr, i, ULongArray.m238getimpl(jArr, i2));
                ULongArray.m243setk8EXiF4(jArr, i2, m238getimpl2);
                i++;
                i2--;
            }
        }
        return i;
    }

    @ExperimentalUnsignedTypes
    /* renamed from: partition-4UcCI2c, reason: not valid java name */
    private static final int m325partition4UcCI2c(byte[] bArr, int i, int i2) {
        int i3;
        byte m100getimpl = UByteArray.m100getimpl(bArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                i3 = m100getimpl & 255;
                if (Intrinsics.compare(UByteArray.m100getimpl(bArr, i) & 255, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UByteArray.m100getimpl(bArr, i2) & 255, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                byte m100getimpl2 = UByteArray.m100getimpl(bArr, i);
                UByteArray.m105setVurrAj0(bArr, i, UByteArray.m100getimpl(bArr, i2));
                UByteArray.m105setVurrAj0(bArr, i2, m100getimpl2);
                i++;
                i2--;
            }
        }
        return i;
    }

    @ExperimentalUnsignedTypes
    /* renamed from: partition-Aa5vz7o, reason: not valid java name */
    private static final int m326partitionAa5vz7o(short[] sArr, int i, int i2) {
        int i3;
        short m305getimpl = UShortArray.m305getimpl(sArr, (i + i2) / 2);
        while (i <= i2) {
            while (true) {
                i3 = m305getimpl & 65535;
                if (Intrinsics.compare(UShortArray.m305getimpl(sArr, i) & 65535, i3) >= 0) {
                    break;
                }
                i++;
            }
            while (Intrinsics.compare(UShortArray.m305getimpl(sArr, i2) & 65535, i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                short m305getimpl2 = UShortArray.m305getimpl(sArr, i);
                UShortArray.m310set01HTLdE(sArr, i, UShortArray.m305getimpl(sArr, i2));
                UShortArray.m310set01HTLdE(sArr, i2, m305getimpl2);
                i++;
                i2--;
            }
        }
        return i;
    }

    @ExperimentalUnsignedTypes
    /* renamed from: partition-oBK06Vg, reason: not valid java name */
    private static final int m327partitionoBK06Vg(int[] iArr, int i, int i2) {
        int m169getimpl = UIntArray.m169getimpl(iArr, (i + i2) / 2);
        while (i <= i2) {
            while (UnsignedKt.uintCompare(UIntArray.m169getimpl(iArr, i), m169getimpl) < 0) {
                i++;
            }
            while (UnsignedKt.uintCompare(UIntArray.m169getimpl(iArr, i2), m169getimpl) > 0) {
                i2--;
            }
            if (i <= i2) {
                int m169getimpl2 = UIntArray.m169getimpl(iArr, i);
                UIntArray.m174setVXSXFK8(iArr, i, UIntArray.m169getimpl(iArr, i2));
                UIntArray.m174setVXSXFK8(iArr, i2, m169getimpl2);
                i++;
                i2--;
            }
        }
        return i;
    }

    @ExperimentalUnsignedTypes
    /* renamed from: quickSort--nroSd4, reason: not valid java name */
    private static final void m328quickSortnroSd4(long[] jArr, int i, int i2) {
        int m324partitionnroSd4 = m324partitionnroSd4(jArr, i, i2);
        int i3 = m324partitionnroSd4 - 1;
        if (i < i3) {
            m328quickSortnroSd4(jArr, i, i3);
        }
        if (m324partitionnroSd4 < i2) {
            m328quickSortnroSd4(jArr, m324partitionnroSd4, i2);
        }
    }

    @ExperimentalUnsignedTypes
    /* renamed from: quickSort-4UcCI2c, reason: not valid java name */
    private static final void m329quickSort4UcCI2c(byte[] bArr, int i, int i2) {
        int m325partition4UcCI2c = m325partition4UcCI2c(bArr, i, i2);
        int i3 = m325partition4UcCI2c - 1;
        if (i < i3) {
            m329quickSort4UcCI2c(bArr, i, i3);
        }
        if (m325partition4UcCI2c < i2) {
            m329quickSort4UcCI2c(bArr, m325partition4UcCI2c, i2);
        }
    }

    @ExperimentalUnsignedTypes
    /* renamed from: quickSort-Aa5vz7o, reason: not valid java name */
    private static final void m330quickSortAa5vz7o(short[] sArr, int i, int i2) {
        int m326partitionAa5vz7o = m326partitionAa5vz7o(sArr, i, i2);
        int i3 = m326partitionAa5vz7o - 1;
        if (i < i3) {
            m330quickSortAa5vz7o(sArr, i, i3);
        }
        if (m326partitionAa5vz7o < i2) {
            m330quickSortAa5vz7o(sArr, m326partitionAa5vz7o, i2);
        }
    }

    @ExperimentalUnsignedTypes
    /* renamed from: quickSort-oBK06Vg, reason: not valid java name */
    private static final void m331quickSortoBK06Vg(int[] iArr, int i, int i2) {
        int m327partitionoBK06Vg = m327partitionoBK06Vg(iArr, i, i2);
        int i3 = m327partitionoBK06Vg - 1;
        if (i < i3) {
            m331quickSortoBK06Vg(iArr, i, i3);
        }
        if (m327partitionoBK06Vg < i2) {
            m331quickSortoBK06Vg(iArr, m327partitionoBK06Vg, i2);
        }
    }

    @ExperimentalUnsignedTypes
    /* renamed from: sortArray--ajY-9A, reason: not valid java name */
    public static final void m332sortArrayajY9A(@NotNull int[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        m331quickSortoBK06Vg(array, 0, UIntArray.m170getSizeimpl(array) - 1);
    }

    @ExperimentalUnsignedTypes
    /* renamed from: sortArray-GBYM_sE, reason: not valid java name */
    public static final void m333sortArrayGBYM_sE(@NotNull byte[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        m329quickSort4UcCI2c(array, 0, UByteArray.m101getSizeimpl(array) - 1);
    }

    @ExperimentalUnsignedTypes
    /* renamed from: sortArray-QwZRm1k, reason: not valid java name */
    public static final void m334sortArrayQwZRm1k(@NotNull long[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        m328quickSortnroSd4(array, 0, ULongArray.m239getSizeimpl(array) - 1);
    }

    @ExperimentalUnsignedTypes
    /* renamed from: sortArray-rL5Bavg, reason: not valid java name */
    public static final void m335sortArrayrL5Bavg(@NotNull short[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        m330quickSortAa5vz7o(array, 0, UShortArray.m306getSizeimpl(array) - 1);
    }
}
