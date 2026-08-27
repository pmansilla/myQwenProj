package npUpdate.nopointer.util;

import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.sun.mail.imap.IMAPStore;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FileDownloadUtil.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0090\u0001\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\b\u0010\b\u001a\u0004\u0018\u00010\u00062\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\n28\b\u0002\u0010\u000b\u001a2\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u00020\u00040\f2\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00040\n2\u0014\b\u0002\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00040\u0014¨\u0006\u0016"}, d2 = {"LnpUpdate/nopointer/util/FileDownloadUtil;", "", "()V", "download", "", FileDownloadModel.URL, "", "fileSavePath", "fileName", "onStart", "Lkotlin/Function0;", "onProgress", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", IMAPStore.ID_NAME, "current", FileDownloadModel.TOTAL, "onComplete", "onError", "Lkotlin/Function1;", "", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class FileDownloadUtil {
    public static final FileDownloadUtil INSTANCE = new FileDownloadUtil();

    private FileDownloadUtil() {
    }

    public final void download(@NotNull String url, @NotNull String fileSavePath, @Nullable String fileName, @NotNull Function0<Unit> onStart, @NotNull Function2<? super Long, ? super Long, Unit> onProgress, @NotNull Function0<Unit> onComplete, @NotNull Function1<? super Throwable, Unit> onError) {
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(fileSavePath, "fileSavePath");
        Intrinsics.checkParameterIsNotNull(onStart, "onStart");
        Intrinsics.checkParameterIsNotNull(onProgress, "onProgress");
        Intrinsics.checkParameterIsNotNull(onComplete, "onComplete");
        Intrinsics.checkParameterIsNotNull(onError, "onError");
        BuildersKt__Builders_commonKt.launch$default(GlobalScope.INSTANCE, Dispatchers.getIO(), null, new FileDownloadUtil$download$5(onStart, url, fileSavePath, fileName, onProgress, onComplete, onError, null), 2, null);
    }
}
