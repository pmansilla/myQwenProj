package npUpdate.nopointer.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationManagerCompat;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import java.io.File;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import kotlin.text.StringsKt;
import me.panpf.sketch.uri.FileUriModel;
import npUpdate.nopointer.extension.AnyKt;
import npUpdate.nopointer.listener.Md5CheckResultListener;
import npUpdate.nopointer.listener.UpdateDownloadListener;
import npUpdate.nopointer.model.UpdateInfo;
import npUpdate.nopointer.util.FileDownloadUtil;
import npUpdate.nopointer.util.GlobalContextProvider;
import npUpdate.nopointer.util.SPUtil;
import npUpdate.nopointer.util.SignMd5Util;
import npUpdate.nopointer.util.Utils;
import org.jetbrains.annotations.NotNull;

/* compiled from: DownloadAppUtils.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010+\u001a\u00020\u00172\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0006\u0010,\u001a\u00020\u0017J\u001a\u0010-\u001a\u00020\u00172\u0006\u0010.\u001a\u00020\u00042\b\u0010/\u001a\u0004\u0018\u00010\u0004H\u0002J\b\u00100\u001a\u00020\u0017H\u0002J\u0010\u00101\u001a\u00020\u00172\u0006\u00102\u001a\u000203H\u0002J\u000e\u00104\u001a\u00020\u00172\u0006\u00105\u001a\u00020\u0004J\b\u00106\u001a\u00020\u0017H\u0002J\u0018\u00107\u001a\u00020\u00172\u0006\u00108\u001a\u0002092\u0006\u0010:\u001a\u000209H\u0002J\u0006\u0010;\u001a\u00020\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\u000b\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0012\"\u0004\b\u0013\u0010\u0014R \u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR&\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u00170\u001dX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R \u0010#\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0019\"\u0004\b%\u0010\u001bR\u001b\u0010&\u001a\u00020'8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b*\u0010\n\u001a\u0004\b(\u0010)¨\u0006<"}, d2 = {"LnpUpdate/nopointer/update/DownloadAppUtils;", "", "()V", DownloadAppUtils.KEY_OF_SP_APK_PATH, "", "context", "Landroid/content/Context;", "getContext", "()Landroid/content/Context;", "context$delegate", "Lkotlin/Lazy;", "downloadUpdateApkFilePath", "getDownloadUpdateApkFilePath", "()Ljava/lang/String;", "setDownloadUpdateApkFilePath", "(Ljava/lang/String;)V", "isDownloading", "", "()Z", "setDownloading", "(Z)V", "onError", "Lkotlin/Function0;", "", "getOnError", "()Lkotlin/jvm/functions/Function0;", "setOnError", "(Lkotlin/jvm/functions/Function0;)V", "onProgress", "Lkotlin/Function1;", "", "getOnProgress", "()Lkotlin/jvm/functions/Function1;", "setOnProgress", "(Lkotlin/jvm/functions/Function1;)V", "onReDownload", "getOnReDownload", "setOnReDownload", "updateInfo", "LnpUpdate/nopointer/model/UpdateInfo;", "getUpdateInfo", "()LnpUpdate/nopointer/model/UpdateInfo;", "updateInfo$delegate", "checkMd5", "download", "downloadByHttpUrlConnection", "filePath", "apkName", "downloadComplete", "downloadError", "e", "", "downloadForWebView", FileDownloadModel.URL, "downloadStart", "downloading", "soFarBytes", "", "totalBytes", "reDownload", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class DownloadAppUtils {

    @NotNull
    public static final String KEY_OF_SP_APK_PATH = "KEY_OF_SP_APK_PATH";
    private static boolean isDownloading;
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(DownloadAppUtils.class), "updateInfo", "getUpdateInfo()LnpUpdate/nopointer/model/UpdateInfo;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(DownloadAppUtils.class), "context", "getContext()Landroid/content/Context;"))};
    public static final DownloadAppUtils INSTANCE = new DownloadAppUtils();

    @NotNull
    private static String downloadUpdateApkFilePath = "";

    /* renamed from: updateInfo$delegate, reason: from kotlin metadata */
    private static final Lazy updateInfo = LazyKt.lazy(new Function0<UpdateInfo>() { // from class: npUpdate.nopointer.update.DownloadAppUtils$updateInfo$2
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final UpdateInfo invoke() {
            return UpdateAppUtils.INSTANCE.getUpdateInfo$npUpdate_release();
        }
    });

    /* renamed from: context$delegate, reason: from kotlin metadata */
    private static final Lazy context = LazyKt.lazy(new Function0<Context>() { // from class: npUpdate.nopointer.update.DownloadAppUtils$context$2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final Context invoke() {
            return GlobalContextProvider.INSTANCE.getGlobalContext();
        }
    });

    @NotNull
    private static Function1<? super Integer, Unit> onProgress = new Function1<Integer, Unit>() { // from class: npUpdate.nopointer.update.DownloadAppUtils$onProgress$1
        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Integer num) {
            invoke(num.intValue());
            return Unit.INSTANCE;
        }

        public final void invoke(int i) {
        }
    };

    @NotNull
    private static Function0<Unit> onError = new Function0<Unit>() { // from class: npUpdate.nopointer.update.DownloadAppUtils$onError$1
        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() {
            invoke2();
            return Unit.INSTANCE;
        }

        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() {
        }
    };

    @NotNull
    private static Function0<Unit> onReDownload = new Function0<Unit>() { // from class: npUpdate.nopointer.update.DownloadAppUtils$onReDownload$1
        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() {
            invoke2();
            return Unit.INSTANCE;
        }

        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() {
        }
    };

    private DownloadAppUtils() {
    }

    private final void checkMd5(Context context2) {
        String appSignatureMD5 = SignMd5Util.INSTANCE.getAppSignatureMD5();
        String signMD5FromApk = SignMd5Util.INSTANCE.getSignMD5FromApk(new File(downloadUpdateApkFilePath));
        AnyKt.log(this, "当前应用签名md5：" + appSignatureMD5);
        AnyKt.log(this, "下载apk签名md5：" + signMD5FromApk);
        Md5CheckResultListener md5CheckResultListener$npUpdate_release = UpdateAppUtils.INSTANCE.getMd5CheckResultListener$npUpdate_release();
        if (md5CheckResultListener$npUpdate_release != null) {
            md5CheckResultListener$npUpdate_release.onResult(StringsKt.equals(appSignatureMD5, signMD5FromApk, true));
        }
        boolean equals = StringsKt.equals(appSignatureMD5, signMD5FromApk, true);
        if (equals) {
            AnyKt.log(INSTANCE, "md5校验成功");
            UpdateAppReceiver.INSTANCE.send(context2, 100);
        }
        if (!(equals)) {
            AnyKt.log(INSTANCE, "md5校验失败");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void downloadByHttpUrlConnection(String filePath, String apkName) {
        FileDownloadUtil.INSTANCE.download(getUpdateInfo().getApkUrl(), filePath, apkName + ".apk", new Function0<Unit>() { // from class: npUpdate.nopointer.update.DownloadAppUtils$downloadByHttpUrlConnection$1
            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                DownloadAppUtils.INSTANCE.downloadStart();
            }
        }, new Function2<Long, Long, Unit>() { // from class: npUpdate.nopointer.update.DownloadAppUtils$downloadByHttpUrlConnection$2
            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ Unit invoke(Long l, Long l2) {
                invoke(l.longValue(), l2.longValue());
                return Unit.INSTANCE;
            }

            public final void invoke(long j, long j2) {
                DownloadAppUtils.INSTANCE.downloading(j, j2);
            }
        }, new Function0<Unit>() { // from class: npUpdate.nopointer.update.DownloadAppUtils$downloadByHttpUrlConnection$3
            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                DownloadAppUtils.INSTANCE.downloadComplete();
            }
        }, new Function1<Throwable, Unit>() { // from class: npUpdate.nopointer.update.DownloadAppUtils$downloadByHttpUrlConnection$4
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke2(th);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull Throwable it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                DownloadAppUtils.INSTANCE.downloadError(it);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void downloadComplete() {
        isDownloading = false;
        AnyKt.log(this, "completed");
        onProgress.invoke(100);
        UpdateDownloadListener downloadListener$npUpdate_release = UpdateAppUtils.INSTANCE.getDownloadListener$npUpdate_release();
        if (downloadListener$npUpdate_release != null) {
            downloadListener$npUpdate_release.onFinish();
        }
        boolean needCheckMd5 = getUpdateInfo().getConfig().getNeedCheckMd5();
        if (needCheckMd5) {
            INSTANCE.checkMd5(INSTANCE.getContext());
        }
        if (!(needCheckMd5)) {
            UpdateAppReceiver.INSTANCE.send(INSTANCE.getContext(), 100);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void downloadError(Throwable e) {
        isDownloading = false;
        AnyKt.log(this, "error:" + e.getMessage());
        Utils.INSTANCE.deleteFile(downloadUpdateApkFilePath);
        onError.invoke();
        UpdateDownloadListener downloadListener$npUpdate_release = UpdateAppUtils.INSTANCE.getDownloadListener$npUpdate_release();
        if (downloadListener$npUpdate_release != null) {
            downloadListener$npUpdate_release.onError(e);
        }
        UpdateAppReceiver.INSTANCE.send(getContext(), NotificationManagerCompat.IMPORTANCE_UNSPECIFIED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void downloadStart() {
        isDownloading = true;
        UpdateDownloadListener downloadListener$npUpdate_release = UpdateAppUtils.INSTANCE.getDownloadListener$npUpdate_release();
        if (downloadListener$npUpdate_release != null) {
            downloadListener$npUpdate_release.onStart();
        }
        UpdateAppReceiver.INSTANCE.send(getContext(), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void downloading(long soFarBytes, long totalBytes) {
        isDownloading = true;
        double d = soFarBytes;
        Double.isNaN(d);
        double d2 = totalBytes;
        Double.isNaN(d2);
        int i = (int) ((d * 100.0d) / d2);
        if (i < 0) {
            i = 0;
        }
        AnyKt.log(this, "progress:" + i);
        UpdateAppReceiver.INSTANCE.send(getContext(), i);
        onProgress.invoke(Integer.valueOf(i));
        UpdateDownloadListener downloadListener$npUpdate_release = UpdateAppUtils.INSTANCE.getDownloadListener$npUpdate_release();
        if (downloadListener$npUpdate_release != null) {
            downloadListener$npUpdate_release.onDownload(i);
        }
    }

    private final Context getContext() {
        Lazy lazy = context;
        KProperty kProperty = $$delegatedProperties[1];
        return (Context) lazy.getValue();
    }

    private final UpdateInfo getUpdateInfo() {
        Lazy lazy = updateInfo;
        KProperty kProperty = $$delegatedProperties[0];
        return (UpdateInfo) lazy.getValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v30, types: [T, java.lang.String] */
    /* JADX WARN: Type inference failed for: r4v11, types: [T, java.lang.String] */
    public final void download() {
        if (!(Intrinsics.areEqual(Environment.getExternalStorageState(), "mounted"))) {
            AnyKt.log(INSTANCE, "没有SD卡");
            onError.invoke();
            return;
        }
        final Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = "";
        boolean z = getUpdateInfo().getConfig().getApkSavePath().length() > 0;
        if (z) {
            objectRef.element = INSTANCE.getUpdateInfo().getConfig().getApkSavePath();
        }
        if (!(z)) {
            String packageName = INSTANCE.getContext().getPackageName();
            StringBuilder sb = new StringBuilder();
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            Intrinsics.checkExpressionValueIsNotNull(externalStorageDirectory, "Environment.getExternalStorageDirectory()");
            sb.append(externalStorageDirectory.getAbsolutePath());
            sb.append(FileUriModel.SCHEME);
            sb.append(packageName);
            objectRef.element = sb.toString();
        }
        final String apkSaveName = getUpdateInfo().getConfig().getApkSaveName().length() > 0 ? getUpdateInfo().getConfig().getApkSaveName() : Utils.INSTANCE.getAppName(getContext());
        String str = ((String) objectRef.element) + '/' + apkSaveName + ".apk";
        downloadUpdateApkFilePath = str;
        SPUtil.INSTANCE.putBase(KEY_OF_SP_APK_PATH, downloadUpdateApkFilePath);
        FileDownloader.setup(getContext());
        final BaseDownloadTask path = FileDownloader.getImpl().create(getUpdateInfo().getApkUrl()).setPath(str);
        path.addHeader("Accept-Encoding", "identity").addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36").setListener(new FileDownloadLargeFileListener() { // from class: npUpdate.nopointer.update.DownloadAppUtils$download$4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.liulishuo.filedownloader.FileDownloadListener
            public void completed(@NotNull BaseDownloadTask task) {
                Intrinsics.checkParameterIsNotNull(task, "task");
                DownloadAppUtils.INSTANCE.downloadComplete();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.liulishuo.filedownloader.FileDownloadListener
            public void error(@NotNull BaseDownloadTask task, @NotNull Throwable e) {
                Intrinsics.checkParameterIsNotNull(task, "task");
                Intrinsics.checkParameterIsNotNull(e, "e");
                AnyKt.log(this, "下载出错，尝试HTTPURLConnection下载");
                Utils.INSTANCE.deleteFile(DownloadAppUtils.INSTANCE.getDownloadUpdateApkFilePath());
                Utils.INSTANCE.deleteFile(DownloadAppUtils.INSTANCE.getDownloadUpdateApkFilePath() + ".temp");
                DownloadAppUtils.INSTANCE.downloadByHttpUrlConnection((String) objectRef.element, apkSaveName);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.liulishuo.filedownloader.FileDownloadLargeFileListener
            public void paused(@NotNull BaseDownloadTask task, long soFarBytes, long totalBytes) {
                Intrinsics.checkParameterIsNotNull(task, "task");
                AnyKt.log(this, "获取文件总长度失败出错，尝试HTTPURLConnection下载");
                Utils.INSTANCE.deleteFile(DownloadAppUtils.INSTANCE.getDownloadUpdateApkFilePath());
                Utils.INSTANCE.deleteFile(DownloadAppUtils.INSTANCE.getDownloadUpdateApkFilePath() + ".temp");
                DownloadAppUtils.INSTANCE.downloadByHttpUrlConnection((String) objectRef.element, apkSaveName);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.liulishuo.filedownloader.FileDownloadLargeFileListener
            public void pending(@NotNull BaseDownloadTask task, long soFarBytes, long totalBytes) {
                Intrinsics.checkParameterIsNotNull(task, "task");
                AnyKt.log(this, "----使用FileDownloader下载-------");
                AnyKt.log(this, "pending:soFarBytes(" + soFarBytes + "),totalBytes(" + totalBytes + ')');
                DownloadAppUtils.INSTANCE.downloadStart();
                if (totalBytes < 0) {
                    BaseDownloadTask.this.pause();
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.liulishuo.filedownloader.FileDownloadLargeFileListener
            public void progress(@NotNull BaseDownloadTask task, long soFarBytes, long totalBytes) {
                Intrinsics.checkParameterIsNotNull(task, "task");
                DownloadAppUtils.INSTANCE.downloading(soFarBytes, totalBytes);
                if (totalBytes < 0) {
                    BaseDownloadTask.this.pause();
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.liulishuo.filedownloader.FileDownloadListener
            public void warn(@NotNull BaseDownloadTask task) {
                Intrinsics.checkParameterIsNotNull(task, "task");
            }
        }).start();
    }

    public final void downloadForWebView(@NotNull String url) {
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
        getContext().startActivity(intent);
    }

    @NotNull
    public final String getDownloadUpdateApkFilePath() {
        return downloadUpdateApkFilePath;
    }

    @NotNull
    public final Function0<Unit> getOnError() {
        return onError;
    }

    @NotNull
    public final Function1<Integer, Unit> getOnProgress() {
        return onProgress;
    }

    @NotNull
    public final Function0<Unit> getOnReDownload() {
        return onReDownload;
    }

    public final boolean isDownloading() {
        return isDownloading;
    }

    public final void reDownload() {
        onReDownload.invoke();
        download();
    }

    public final void setDownloadUpdateApkFilePath(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        downloadUpdateApkFilePath = str;
    }

    public final void setDownloading(boolean z) {
        isDownloading = z;
    }

    public final void setOnError(@NotNull Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "<set-?>");
        onError = function0;
    }

    public final void setOnProgress(@NotNull Function1<? super Integer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "<set-?>");
        onProgress = function1;
    }

    public final void setOnReDownload(@NotNull Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "<set-?>");
        onReDownload = function0;
    }
}
