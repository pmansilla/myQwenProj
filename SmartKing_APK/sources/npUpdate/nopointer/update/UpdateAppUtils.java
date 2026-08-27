package npUpdate.nopointer.update;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import npUpdate.nopointer.extension.AnyKt;
import npUpdate.nopointer.listener.Md5CheckResultListener;
import npUpdate.nopointer.listener.OnBtnClickListener;
import npUpdate.nopointer.listener.OnInitUiListener;
import npUpdate.nopointer.listener.UpdateDownloadListener;
import npUpdate.nopointer.model.UiConfig;
import npUpdate.nopointer.model.UpdateConfig;
import npUpdate.nopointer.model.UpdateInfo;
import npUpdate.nopointer.ui.UpdateAppActivity;
import npUpdate.nopointer.util.GlobalContextProvider;
import npUpdate.nopointer.util.SPUtil;
import npUpdate.nopointer.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UpdateAppUtils.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\"\u001a\u00020\u00002\u0006\u0010\"\u001a\u00020#J\u0006\u0010$\u001a\u00020%J\b\u0010&\u001a\u00020\u0000H\u0007J\u0010\u0010'\u001a\u00020\u00002\b\u0010(\u001a\u0004\u0018\u00010\u0010J\u0010\u0010)\u001a\u00020\u00002\b\u0010(\u001a\u0004\u0018\u00010\nJ\u0010\u0010*\u001a\u00020\u00002\b\u0010(\u001a\u0004\u0018\u00010\u0016J\u0010\u0010+\u001a\u00020\u00002\b\u0010(\u001a\u0004\u0018\u00010\u0010J\u0010\u0010,\u001a\u00020\u00002\b\u0010(\u001a\u0004\u0018\u00010\u0004J\u000e\u0010-\u001a\u00020\u00002\u0006\u0010-\u001a\u00020.J\u0006\u0010/\u001a\u00020%J\u000e\u00100\u001a\u00020\u00002\u0006\u00101\u001a\u000202J\u000e\u00103\u001a\u00020\u00002\u0006\u00104\u001a\u000205J\u000e\u00106\u001a\u00020\u00002\u0006\u00107\u001a\u000205J\u000e\u00108\u001a\u00020\u00002\u0006\u00108\u001a\u000209J\u000e\u0010:\u001a\u00020\u00002\u0006\u0010:\u001a\u000209R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u0010X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0012\"\u0004\b\u001d\u0010\u0014R\u0014\u0010\u001e\u001a\u00020\u001fX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b \u0010!¨\u0006;"}, d2 = {"LnpUpdate/nopointer/update/UpdateAppUtils;", "", "()V", "downloadListener", "LnpUpdate/nopointer/listener/UpdateDownloadListener;", "getDownloadListener$npUpdate_release", "()LnpUpdate/nopointer/listener/UpdateDownloadListener;", "setDownloadListener$npUpdate_release", "(LnpUpdate/nopointer/listener/UpdateDownloadListener;)V", "md5CheckResultListener", "LnpUpdate/nopointer/listener/Md5CheckResultListener;", "getMd5CheckResultListener$npUpdate_release", "()LnpUpdate/nopointer/listener/Md5CheckResultListener;", "setMd5CheckResultListener$npUpdate_release", "(LnpUpdate/nopointer/listener/Md5CheckResultListener;)V", "onCancelBtnClickListener", "LnpUpdate/nopointer/listener/OnBtnClickListener;", "getOnCancelBtnClickListener$npUpdate_release", "()LnpUpdate/nopointer/listener/OnBtnClickListener;", "setOnCancelBtnClickListener$npUpdate_release", "(LnpUpdate/nopointer/listener/OnBtnClickListener;)V", "onInitUiListener", "LnpUpdate/nopointer/listener/OnInitUiListener;", "getOnInitUiListener$npUpdate_release", "()LnpUpdate/nopointer/listener/OnInitUiListener;", "setOnInitUiListener$npUpdate_release", "(LnpUpdate/nopointer/listener/OnInitUiListener;)V", "onUpdateBtnClickListener", "getOnUpdateBtnClickListener$npUpdate_release", "setOnUpdateBtnClickListener$npUpdate_release", "updateInfo", "LnpUpdate/nopointer/model/UpdateInfo;", "getUpdateInfo$npUpdate_release", "()LnpUpdate/nopointer/model/UpdateInfo;", "apkUrl", "", "deleteInstalledApk", "", "getInstance", "setCancelBtnClickListener", "listener", "setMd5CheckResultListener", "setOnInitUiListener", "setUpdateBtnClickListener", "setUpdateDownloadListener", "uiConfig", "LnpUpdate/nopointer/model/UiConfig;", "update", "updateConfig", "config", "LnpUpdate/nopointer/model/UpdateConfig;", "updateContent", "content", "", "updateTitle", "title", "useDefaultContent", "", "useDefaultTitle", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class UpdateAppUtils {
    public static final UpdateAppUtils INSTANCE = new UpdateAppUtils();

    @Nullable
    private static UpdateDownloadListener downloadListener;

    @Nullable
    private static Md5CheckResultListener md5CheckResultListener;

    @Nullable
    private static OnBtnClickListener onCancelBtnClickListener;

    @Nullable
    private static OnInitUiListener onInitUiListener;

    @Nullable
    private static OnBtnClickListener onUpdateBtnClickListener;

    @NotNull
    private static final UpdateInfo updateInfo;

    static {
        GlobalContextProvider.INSTANCE.getGlobalContext();
        updateInfo = new UpdateInfo(null, null, null, null, null, false, false, 127, null);
    }

    private UpdateAppUtils() {
    }

    @JvmStatic
    @NotNull
    public static final UpdateAppUtils getInstance() {
        return INSTANCE;
    }

    @NotNull
    public final UpdateAppUtils apkUrl(@NotNull String apkUrl) {
        Intrinsics.checkParameterIsNotNull(apkUrl, "apkUrl");
        updateInfo.setApkUrl(apkUrl);
        return this;
    }

    public final void deleteInstalledApk() {
        String string = SPUtil.INSTANCE.getString(DownloadAppUtils.KEY_OF_SP_APK_PATH, "");
        int aPPVersionCode = Utils.INSTANCE.getAPPVersionCode();
        int apkVersionCode = Utils.INSTANCE.getApkVersionCode(string);
        AnyKt.log(this, "appVersionCode:" + aPPVersionCode);
        AnyKt.log(this, "apkVersionCode:" + apkVersionCode);
        boolean z = false;
        if ((string.length() > 0) && aPPVersionCode == apkVersionCode && apkVersionCode > 0) {
            z = true;
        }
        if (z) {
            Utils.INSTANCE.deleteFile(string);
        }
    }

    @Nullable
    public final UpdateDownloadListener getDownloadListener$npUpdate_release() {
        return downloadListener;
    }

    @Nullable
    public final Md5CheckResultListener getMd5CheckResultListener$npUpdate_release() {
        return md5CheckResultListener;
    }

    @Nullable
    public final OnBtnClickListener getOnCancelBtnClickListener$npUpdate_release() {
        return onCancelBtnClickListener;
    }

    @Nullable
    public final OnInitUiListener getOnInitUiListener$npUpdate_release() {
        return onInitUiListener;
    }

    @Nullable
    public final OnBtnClickListener getOnUpdateBtnClickListener$npUpdate_release() {
        return onUpdateBtnClickListener;
    }

    @NotNull
    public final UpdateInfo getUpdateInfo$npUpdate_release() {
        return updateInfo;
    }

    @NotNull
    public final UpdateAppUtils setCancelBtnClickListener(@Nullable OnBtnClickListener listener) {
        onCancelBtnClickListener = listener;
        return this;
    }

    public final void setDownloadListener$npUpdate_release(@Nullable UpdateDownloadListener updateDownloadListener) {
        downloadListener = updateDownloadListener;
    }

    @NotNull
    public final UpdateAppUtils setMd5CheckResultListener(@Nullable Md5CheckResultListener listener) {
        md5CheckResultListener = listener;
        return this;
    }

    public final void setMd5CheckResultListener$npUpdate_release(@Nullable Md5CheckResultListener md5CheckResultListener2) {
        md5CheckResultListener = md5CheckResultListener2;
    }

    public final void setOnCancelBtnClickListener$npUpdate_release(@Nullable OnBtnClickListener onBtnClickListener) {
        onCancelBtnClickListener = onBtnClickListener;
    }

    @NotNull
    public final UpdateAppUtils setOnInitUiListener(@Nullable OnInitUiListener listener) {
        onInitUiListener = listener;
        return this;
    }

    public final void setOnInitUiListener$npUpdate_release(@Nullable OnInitUiListener onInitUiListener2) {
        onInitUiListener = onInitUiListener2;
    }

    public final void setOnUpdateBtnClickListener$npUpdate_release(@Nullable OnBtnClickListener onBtnClickListener) {
        onUpdateBtnClickListener = onBtnClickListener;
    }

    @NotNull
    public final UpdateAppUtils setUpdateBtnClickListener(@Nullable OnBtnClickListener listener) {
        onUpdateBtnClickListener = listener;
        return this;
    }

    @NotNull
    public final UpdateAppUtils setUpdateDownloadListener(@Nullable UpdateDownloadListener listener) {
        downloadListener = listener;
        return this;
    }

    @NotNull
    public final UpdateAppUtils uiConfig(@NotNull UiConfig uiConfig) {
        Intrinsics.checkParameterIsNotNull(uiConfig, "uiConfig");
        updateInfo.setUiConfig(uiConfig);
        return this;
    }

    public final void update() {
        String str = GlobalContextProvider.INSTANCE.getGlobalContext().getPackageName() + updateInfo.getConfig().getServerVersionName();
        boolean z = updateInfo.getConfig().getAlwaysShow() || updateInfo.getConfig().getThisTimeShow() || updateInfo.getConfig().getForce();
        if (z) {
            UpdateAppActivity.INSTANCE.launch();
        }
        if (!(z)) {
            if (!(SPUtil.INSTANCE.getBoolean(str, false))) {
                UpdateAppActivity.INSTANCE.launch();
            }
        }
        SPUtil.INSTANCE.putBase(str, true);
    }

    @NotNull
    public final UpdateAppUtils updateConfig(@NotNull UpdateConfig config) {
        Intrinsics.checkParameterIsNotNull(config, "config");
        updateInfo.setConfig(config);
        return this;
    }

    @NotNull
    public final UpdateAppUtils updateContent(@NotNull CharSequence content) {
        Intrinsics.checkParameterIsNotNull(content, "content");
        updateInfo.setUpdateContent(content);
        return this;
    }

    @NotNull
    public final UpdateAppUtils updateTitle(@NotNull CharSequence title) {
        Intrinsics.checkParameterIsNotNull(title, "title");
        updateInfo.setUpdateTitle(title);
        return this;
    }

    @NotNull
    public final UpdateAppUtils useDefaultContent(boolean useDefaultContent) {
        updateInfo.setUseDefaultContent(useDefaultContent);
        return this;
    }

    @NotNull
    public final UpdateAppUtils useDefaultTitle(boolean useDefaultTitle) {
        updateInfo.setUseDefaultTitle(useDefaultTitle);
        return this;
    }
}
