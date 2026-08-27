package npUpdate.nopointer.util;

import java.io.InputStream;
import java.io.OutputStream;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: FileDownloadUtil.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u001a2\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\t0\b¨\u0006\n"}, d2 = {"copyToWithProgress", "", "Ljava/io/InputStream;", "out", "Ljava/io/OutputStream;", "bufferSize", "", "currentByte", "Lkotlin/Function1;", "", "npUpdate_release"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class FileDownloadUtilKt {
    public static final long copyToWithProgress(@NotNull InputStream copyToWithProgress, @NotNull OutputStream out, int i, @NotNull Function1<? super Long, Unit> currentByte) {
        Intrinsics.checkParameterIsNotNull(copyToWithProgress, "$this$copyToWithProgress");
        Intrinsics.checkParameterIsNotNull(out, "out");
        Intrinsics.checkParameterIsNotNull(currentByte, "currentByte");
        byte[] bArr = new byte[i];
        int read = copyToWithProgress.read(bArr);
        long j = 0;
        while (read >= 0) {
            out.write(bArr, 0, read);
            j += read;
            read = copyToWithProgress.read(bArr);
            currentByte.invoke(Long.valueOf(j));
        }
        return j;
    }

    public static /* synthetic */ long copyToWithProgress$default(InputStream inputStream, OutputStream outputStream, int i, Function1 function1, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 8192;
        }
        if ((i2 & 4) != 0) {
            function1 = new Function1<Long, Unit>() { // from class: npUpdate.nopointer.util.FileDownloadUtilKt$copyToWithProgress$1
                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Unit invoke(Long l) {
                    invoke(l.longValue());
                    return Unit.INSTANCE;
                }

                public final void invoke(long j) {
                }
            };
        }
        return copyToWithProgress(inputStream, outputStream, i, function1);
    }
}
