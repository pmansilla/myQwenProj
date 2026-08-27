package npUpdate.nopointer.model;

import com.litesuits.orm.db.assit.SQLBuilder;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UpdateConfig.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\bC\b\u0086\b\u0018\u00002\u00020\u0001B¥\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\f\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\t\u0012\b\b\u0002\u0010\u0014\u001a\u00020\f¢\u0006\u0002\u0010\u0015J\t\u0010:\u001a\u00020\u0003HÆ\u0003J\t\u0010;\u001a\u00020\u0003HÆ\u0003J\t\u0010<\u001a\u00020\u0003HÆ\u0003J\t\u0010=\u001a\u00020\fHÆ\u0003J\t\u0010>\u001a\u00020\u0003HÆ\u0003J\t\u0010?\u001a\u00020\u0003HÆ\u0003J\t\u0010@\u001a\u00020\tHÆ\u0003J\t\u0010A\u001a\u00020\fHÆ\u0003J\t\u0010B\u001a\u00020\u0003HÆ\u0003J\t\u0010C\u001a\u00020\u0003HÆ\u0003J\t\u0010D\u001a\u00020\u0003HÆ\u0003J\t\u0010E\u001a\u00020\u0003HÆ\u0003J\t\u0010F\u001a\u00020\tHÆ\u0003J\t\u0010G\u001a\u00020\tHÆ\u0003J\t\u0010H\u001a\u00020\fHÆ\u0003J\t\u0010I\u001a\u00020\u0003HÆ\u0003J©\u0001\u0010J\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\f2\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\t2\b\b\u0002\u0010\u0014\u001a\u00020\fHÆ\u0001J\u0013\u0010K\u001a\u00020\u00032\b\u0010L\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010M\u001a\u00020\fHÖ\u0001J\t\u0010N\u001a\u00020\tHÖ\u0001R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0017\"\u0004\b\u001b\u0010\u0019R\u001a\u0010\n\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u001d\"\u0004\b!\u0010\u001fR\u001a\u0010\u000e\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0017\"\u0004\b#\u0010\u0019R\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0017\"\u0004\b)\u0010\u0019R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0002\u0010\u0017\"\u0004\b*\u0010\u0019R\u001a\u0010\u000f\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0017\"\u0004\b+\u0010\u0019R\u001a\u0010\r\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0017\"\u0004\b-\u0010\u0019R\u001a\u0010\u0011\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0017\"\u0004\b/\u0010\u0019R\u001a\u0010\u0010\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u0010%\"\u0004\b1\u0010'R\u001a\u0010\u0014\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u0010%\"\u0004\b3\u0010'R\u001a\u0010\u0013\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u001d\"\u0004\b5\u0010\u001fR\u001a\u0010\u0012\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u0017\"\u0004\b7\u0010\u0019R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u0017\"\u0004\b9\u0010\u0019¨\u0006O"}, d2 = {"LnpUpdate/nopointer/model/UpdateConfig;", "", "isDebug", "", "alwaysShow", "thisTimeShow", "alwaysShowDownLoadDialog", "force", "apkSavePath", "", "apkSaveName", "downloadBy", "", "justDownload", "checkWifi", "isShowNotification", "notifyImgRes", "needCheckMd5", "showDownloadingToast", "serverVersionName", "serverVersionCode", "(ZZZZZLjava/lang/String;Ljava/lang/String;IZZZIZZLjava/lang/String;I)V", "getAlwaysShow", "()Z", "setAlwaysShow", "(Z)V", "getAlwaysShowDownLoadDialog", "setAlwaysShowDownLoadDialog", "getApkSaveName", "()Ljava/lang/String;", "setApkSaveName", "(Ljava/lang/String;)V", "getApkSavePath", "setApkSavePath", "getCheckWifi", "setCheckWifi", "getDownloadBy", "()I", "setDownloadBy", "(I)V", "getForce", "setForce", "setDebug", "setShowNotification", "getJustDownload", "setJustDownload", "getNeedCheckMd5", "setNeedCheckMd5", "getNotifyImgRes", "setNotifyImgRes", "getServerVersionCode", "setServerVersionCode", "getServerVersionName", "setServerVersionName", "getShowDownloadingToast", "setShowDownloadingToast", "getThisTimeShow", "setThisTimeShow", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final /* data */ class UpdateConfig {
    private boolean alwaysShow;
    private boolean alwaysShowDownLoadDialog;

    @NotNull
    private String apkSaveName;

    @NotNull
    private String apkSavePath;
    private boolean checkWifi;
    private int downloadBy;
    private boolean force;
    private boolean isDebug;
    private boolean isShowNotification;
    private boolean justDownload;
    private boolean needCheckMd5;
    private int notifyImgRes;
    private int serverVersionCode;

    @NotNull
    private String serverVersionName;
    private boolean showDownloadingToast;
    private boolean thisTimeShow;

    public UpdateConfig() {
        this(false, false, false, false, false, null, null, 0, false, false, false, 0, false, false, null, 0, 65535, null);
    }

    public UpdateConfig(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, @NotNull String apkSavePath, @NotNull String apkSaveName, int i, boolean z6, boolean z7, boolean z8, int i2, boolean z9, boolean z10, @NotNull String serverVersionName, int i3) {
        Intrinsics.checkParameterIsNotNull(apkSavePath, "apkSavePath");
        Intrinsics.checkParameterIsNotNull(apkSaveName, "apkSaveName");
        Intrinsics.checkParameterIsNotNull(serverVersionName, "serverVersionName");
        this.isDebug = z;
        this.alwaysShow = z2;
        this.thisTimeShow = z3;
        this.alwaysShowDownLoadDialog = z4;
        this.force = z5;
        this.apkSavePath = apkSavePath;
        this.apkSaveName = apkSaveName;
        this.downloadBy = i;
        this.justDownload = z6;
        this.checkWifi = z7;
        this.isShowNotification = z8;
        this.notifyImgRes = i2;
        this.needCheckMd5 = z9;
        this.showDownloadingToast = z10;
        this.serverVersionName = serverVersionName;
        this.serverVersionCode = i3;
    }

    public /* synthetic */ UpdateConfig(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, String str, String str2, int i, boolean z6, boolean z7, boolean z8, int i2, boolean z9, boolean z10, String str3, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this((i4 & 1) != 0 ? true : z, (i4 & 2) != 0 ? true : z2, (i4 & 4) != 0 ? false : z3, (i4 & 8) != 0 ? false : z4, (i4 & 16) != 0 ? false : z5, (i4 & 32) != 0 ? "" : str, (i4 & 64) != 0 ? "" : str2, (i4 & 128) != 0 ? 257 : i, (i4 & 256) != 0 ? false : z6, (i4 & 512) != 0 ? false : z7, (i4 & 1024) != 0 ? true : z8, (i4 & 2048) != 0 ? 0 : i2, (i4 & 4096) != 0 ? false : z9, (i4 & 8192) != 0 ? true : z10, (i4 & 16384) != 0 ? "" : str3, (i4 & 32768) == 0 ? i3 : 0);
    }

    /* renamed from: component1, reason: from getter */
    public final boolean getIsDebug() {
        return this.isDebug;
    }

    /* renamed from: component10, reason: from getter */
    public final boolean getCheckWifi() {
        return this.checkWifi;
    }

    /* renamed from: component11, reason: from getter */
    public final boolean getIsShowNotification() {
        return this.isShowNotification;
    }

    /* renamed from: component12, reason: from getter */
    public final int getNotifyImgRes() {
        return this.notifyImgRes;
    }

    /* renamed from: component13, reason: from getter */
    public final boolean getNeedCheckMd5() {
        return this.needCheckMd5;
    }

    /* renamed from: component14, reason: from getter */
    public final boolean getShowDownloadingToast() {
        return this.showDownloadingToast;
    }

    @NotNull
    /* renamed from: component15, reason: from getter */
    public final String getServerVersionName() {
        return this.serverVersionName;
    }

    /* renamed from: component16, reason: from getter */
    public final int getServerVersionCode() {
        return this.serverVersionCode;
    }

    /* renamed from: component2, reason: from getter */
    public final boolean getAlwaysShow() {
        return this.alwaysShow;
    }

    /* renamed from: component3, reason: from getter */
    public final boolean getThisTimeShow() {
        return this.thisTimeShow;
    }

    /* renamed from: component4, reason: from getter */
    public final boolean getAlwaysShowDownLoadDialog() {
        return this.alwaysShowDownLoadDialog;
    }

    /* renamed from: component5, reason: from getter */
    public final boolean getForce() {
        return this.force;
    }

    @NotNull
    /* renamed from: component6, reason: from getter */
    public final String getApkSavePath() {
        return this.apkSavePath;
    }

    @NotNull
    /* renamed from: component7, reason: from getter */
    public final String getApkSaveName() {
        return this.apkSaveName;
    }

    /* renamed from: component8, reason: from getter */
    public final int getDownloadBy() {
        return this.downloadBy;
    }

    /* renamed from: component9, reason: from getter */
    public final boolean getJustDownload() {
        return this.justDownload;
    }

    @NotNull
    public final UpdateConfig copy(boolean isDebug, boolean alwaysShow, boolean thisTimeShow, boolean alwaysShowDownLoadDialog, boolean force, @NotNull String apkSavePath, @NotNull String apkSaveName, int downloadBy, boolean justDownload, boolean checkWifi, boolean isShowNotification, int notifyImgRes, boolean needCheckMd5, boolean showDownloadingToast, @NotNull String serverVersionName, int serverVersionCode) {
        Intrinsics.checkParameterIsNotNull(apkSavePath, "apkSavePath");
        Intrinsics.checkParameterIsNotNull(apkSaveName, "apkSaveName");
        Intrinsics.checkParameterIsNotNull(serverVersionName, "serverVersionName");
        return new UpdateConfig(isDebug, alwaysShow, thisTimeShow, alwaysShowDownLoadDialog, force, apkSavePath, apkSaveName, downloadBy, justDownload, checkWifi, isShowNotification, notifyImgRes, needCheckMd5, showDownloadingToast, serverVersionName, serverVersionCode);
    }

    public boolean equals(@Nullable Object other) {
        if (this != other) {
            if (other instanceof UpdateConfig) {
                UpdateConfig updateConfig = (UpdateConfig) other;
                if (this.isDebug == updateConfig.isDebug) {
                    if (this.alwaysShow == updateConfig.alwaysShow) {
                        if (this.thisTimeShow == updateConfig.thisTimeShow) {
                            if (this.alwaysShowDownLoadDialog == updateConfig.alwaysShowDownLoadDialog) {
                                if ((this.force == updateConfig.force) && Intrinsics.areEqual(this.apkSavePath, updateConfig.apkSavePath) && Intrinsics.areEqual(this.apkSaveName, updateConfig.apkSaveName)) {
                                    if (this.downloadBy == updateConfig.downloadBy) {
                                        if (this.justDownload == updateConfig.justDownload) {
                                            if (this.checkWifi == updateConfig.checkWifi) {
                                                if (this.isShowNotification == updateConfig.isShowNotification) {
                                                    if (this.notifyImgRes == updateConfig.notifyImgRes) {
                                                        if (this.needCheckMd5 == updateConfig.needCheckMd5) {
                                                            if ((this.showDownloadingToast == updateConfig.showDownloadingToast) && Intrinsics.areEqual(this.serverVersionName, updateConfig.serverVersionName)) {
                                                                if (this.serverVersionCode == updateConfig.serverVersionCode) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public final boolean getAlwaysShow() {
        return this.alwaysShow;
    }

    public final boolean getAlwaysShowDownLoadDialog() {
        return this.alwaysShowDownLoadDialog;
    }

    @NotNull
    public final String getApkSaveName() {
        return this.apkSaveName;
    }

    @NotNull
    public final String getApkSavePath() {
        return this.apkSavePath;
    }

    public final boolean getCheckWifi() {
        return this.checkWifi;
    }

    public final int getDownloadBy() {
        return this.downloadBy;
    }

    public final boolean getForce() {
        return this.force;
    }

    public final boolean getJustDownload() {
        return this.justDownload;
    }

    public final boolean getNeedCheckMd5() {
        return this.needCheckMd5;
    }

    public final int getNotifyImgRes() {
        return this.notifyImgRes;
    }

    public final int getServerVersionCode() {
        return this.serverVersionCode;
    }

    @NotNull
    public final String getServerVersionName() {
        return this.serverVersionName;
    }

    public final boolean getShowDownloadingToast() {
        return this.showDownloadingToast;
    }

    public final boolean getThisTimeShow() {
        return this.thisTimeShow;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    /* JADX WARN: Type inference failed for: r0v32 */
    /* JADX WARN: Type inference failed for: r0v33 */
    /* JADX WARN: Type inference failed for: r2v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v15, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v17, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v19, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v22, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v4, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v6, types: [boolean] */
    public int hashCode() {
        boolean z = this.isDebug;
        ?? r0 = z;
        if (z) {
            r0 = 1;
        }
        int i = r0 * 31;
        ?? r2 = this.alwaysShow;
        int i2 = r2;
        if (r2 != 0) {
            i2 = 1;
        }
        int i3 = (i + i2) * 31;
        ?? r22 = this.thisTimeShow;
        int i4 = r22;
        if (r22 != 0) {
            i4 = 1;
        }
        int i5 = (i3 + i4) * 31;
        ?? r23 = this.alwaysShowDownLoadDialog;
        int i6 = r23;
        if (r23 != 0) {
            i6 = 1;
        }
        int i7 = (i5 + i6) * 31;
        ?? r24 = this.force;
        int i8 = r24;
        if (r24 != 0) {
            i8 = 1;
        }
        int i9 = (i7 + i8) * 31;
        String str = this.apkSavePath;
        int hashCode = (i9 + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.apkSaveName;
        int hashCode2 = (((hashCode + (str2 != null ? str2.hashCode() : 0)) * 31) + this.downloadBy) * 31;
        ?? r25 = this.justDownload;
        int i10 = r25;
        if (r25 != 0) {
            i10 = 1;
        }
        int i11 = (hashCode2 + i10) * 31;
        ?? r26 = this.checkWifi;
        int i12 = r26;
        if (r26 != 0) {
            i12 = 1;
        }
        int i13 = (i11 + i12) * 31;
        ?? r27 = this.isShowNotification;
        int i14 = r27;
        if (r27 != 0) {
            i14 = 1;
        }
        int i15 = (((i13 + i14) * 31) + this.notifyImgRes) * 31;
        ?? r28 = this.needCheckMd5;
        int i16 = r28;
        if (r28 != 0) {
            i16 = 1;
        }
        int i17 = (i15 + i16) * 31;
        boolean z2 = this.showDownloadingToast;
        int i18 = (i17 + (z2 ? 1 : z2 ? 1 : 0)) * 31;
        String str3 = this.serverVersionName;
        return ((i18 + (str3 != null ? str3.hashCode() : 0)) * 31) + this.serverVersionCode;
    }

    public final boolean isDebug() {
        return this.isDebug;
    }

    public final boolean isShowNotification() {
        return this.isShowNotification;
    }

    public final void setAlwaysShow(boolean z) {
        this.alwaysShow = z;
    }

    public final void setAlwaysShowDownLoadDialog(boolean z) {
        this.alwaysShowDownLoadDialog = z;
    }

    public final void setApkSaveName(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.apkSaveName = str;
    }

    public final void setApkSavePath(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.apkSavePath = str;
    }

    public final void setCheckWifi(boolean z) {
        this.checkWifi = z;
    }

    public final void setDebug(boolean z) {
        this.isDebug = z;
    }

    public final void setDownloadBy(int i) {
        this.downloadBy = i;
    }

    public final void setForce(boolean z) {
        this.force = z;
    }

    public final void setJustDownload(boolean z) {
        this.justDownload = z;
    }

    public final void setNeedCheckMd5(boolean z) {
        this.needCheckMd5 = z;
    }

    public final void setNotifyImgRes(int i) {
        this.notifyImgRes = i;
    }

    public final void setServerVersionCode(int i) {
        this.serverVersionCode = i;
    }

    public final void setServerVersionName(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.serverVersionName = str;
    }

    public final void setShowDownloadingToast(boolean z) {
        this.showDownloadingToast = z;
    }

    public final void setShowNotification(boolean z) {
        this.isShowNotification = z;
    }

    public final void setThisTimeShow(boolean z) {
        this.thisTimeShow = z;
    }

    @NotNull
    public String toString() {
        return "UpdateConfig(isDebug=" + this.isDebug + ", alwaysShow=" + this.alwaysShow + ", thisTimeShow=" + this.thisTimeShow + ", alwaysShowDownLoadDialog=" + this.alwaysShowDownLoadDialog + ", force=" + this.force + ", apkSavePath=" + this.apkSavePath + ", apkSaveName=" + this.apkSaveName + ", downloadBy=" + this.downloadBy + ", justDownload=" + this.justDownload + ", checkWifi=" + this.checkWifi + ", isShowNotification=" + this.isShowNotification + ", notifyImgRes=" + this.notifyImgRes + ", needCheckMd5=" + this.needCheckMd5 + ", showDownloadingToast=" + this.showDownloadingToast + ", serverVersionName=" + this.serverVersionName + ", serverVersionCode=" + this.serverVersionCode + SQLBuilder.PARENTHESES_RIGHT;
    }
}
