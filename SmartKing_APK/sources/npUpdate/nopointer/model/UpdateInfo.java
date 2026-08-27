package npUpdate.nopointer.model;

import com.litesuits.orm.db.assit.SQLBuilder;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UpdateInfo.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b%\n\u0002\u0010\b\n\u0002\b\u0002\b\u0080\b\u0018\u00002\u00020\u0001BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f¢\u0006\u0002\u0010\u000eJ\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0003HÆ\u0003J\t\u0010)\u001a\u00020\u0006HÆ\u0003J\t\u0010*\u001a\u00020\bHÆ\u0003J\t\u0010+\u001a\u00020\nHÆ\u0003J\t\u0010,\u001a\u00020\fHÆ\u0003J\t\u0010-\u001a\u00020\fHÆ\u0003JO\u0010.\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\fHÆ\u0001J\u0013\u0010/\u001a\u00020\f2\b\u00100\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00101\u001a\u000202HÖ\u0001J\t\u00103\u001a\u00020\u0006HÖ\u0001R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001c\"\u0004\b \u0010\u001eR\u001a\u0010\r\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\"\"\u0004\b&\u0010$¨\u00064"}, d2 = {"LnpUpdate/nopointer/model/UpdateInfo;", "", "updateTitle", "", "updateContent", "apkUrl", "", "config", "LnpUpdate/nopointer/model/UpdateConfig;", "uiConfig", "LnpUpdate/nopointer/model/UiConfig;", "useDefaultTitle", "", "useDefaultContent", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/String;LnpUpdate/nopointer/model/UpdateConfig;LnpUpdate/nopointer/model/UiConfig;ZZ)V", "getApkUrl", "()Ljava/lang/String;", "setApkUrl", "(Ljava/lang/String;)V", "getConfig", "()LnpUpdate/nopointer/model/UpdateConfig;", "setConfig", "(LnpUpdate/nopointer/model/UpdateConfig;)V", "getUiConfig", "()LnpUpdate/nopointer/model/UiConfig;", "setUiConfig", "(LnpUpdate/nopointer/model/UiConfig;)V", "getUpdateContent", "()Ljava/lang/CharSequence;", "setUpdateContent", "(Ljava/lang/CharSequence;)V", "getUpdateTitle", "setUpdateTitle", "getUseDefaultContent", "()Z", "setUseDefaultContent", "(Z)V", "getUseDefaultTitle", "setUseDefaultTitle", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final /* data */ class UpdateInfo {

    @NotNull
    private String apkUrl;

    @NotNull
    private UpdateConfig config;

    @NotNull
    private UiConfig uiConfig;

    @NotNull
    private CharSequence updateContent;

    @NotNull
    private CharSequence updateTitle;
    private boolean useDefaultContent;
    private boolean useDefaultTitle;

    public UpdateInfo() {
        this(null, null, null, null, null, false, false, 127, null);
    }

    public UpdateInfo(@NotNull CharSequence updateTitle, @NotNull CharSequence updateContent, @NotNull String apkUrl, @NotNull UpdateConfig config, @NotNull UiConfig uiConfig, boolean z, boolean z2) {
        Intrinsics.checkParameterIsNotNull(updateTitle, "updateTitle");
        Intrinsics.checkParameterIsNotNull(updateContent, "updateContent");
        Intrinsics.checkParameterIsNotNull(apkUrl, "apkUrl");
        Intrinsics.checkParameterIsNotNull(config, "config");
        Intrinsics.checkParameterIsNotNull(uiConfig, "uiConfig");
        this.updateTitle = updateTitle;
        this.updateContent = updateContent;
        this.apkUrl = apkUrl;
        this.config = config;
        this.uiConfig = uiConfig;
        this.useDefaultTitle = z;
        this.useDefaultContent = z2;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ UpdateInfo(java.lang.CharSequence r30, java.lang.CharSequence r31, java.lang.String r32, npUpdate.nopointer.model.UpdateConfig r33, npUpdate.nopointer.model.UiConfig r34, boolean r35, boolean r36, int r37, kotlin.jvm.internal.DefaultConstructorMarker r38) {
        /*
            Method dump skipped, instructions count: 186
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: npUpdate.nopointer.model.UpdateInfo.<init>(java.lang.CharSequence, java.lang.CharSequence, java.lang.String, npUpdate.nopointer.model.UpdateConfig, npUpdate.nopointer.model.UiConfig, boolean, boolean, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    @NotNull
    public static /* synthetic */ UpdateInfo copy$default(UpdateInfo updateInfo, CharSequence charSequence, CharSequence charSequence2, String str, UpdateConfig updateConfig, UiConfig uiConfig, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            charSequence = updateInfo.updateTitle;
        }
        if ((i & 2) != 0) {
            charSequence2 = updateInfo.updateContent;
        }
        CharSequence charSequence3 = charSequence2;
        if ((i & 4) != 0) {
            str = updateInfo.apkUrl;
        }
        String str2 = str;
        if ((i & 8) != 0) {
            updateConfig = updateInfo.config;
        }
        UpdateConfig updateConfig2 = updateConfig;
        if ((i & 16) != 0) {
            uiConfig = updateInfo.uiConfig;
        }
        UiConfig uiConfig2 = uiConfig;
        if ((i & 32) != 0) {
            z = updateInfo.useDefaultTitle;
        }
        boolean z3 = z;
        if ((i & 64) != 0) {
            z2 = updateInfo.useDefaultContent;
        }
        return updateInfo.copy(charSequence, charSequence3, str2, updateConfig2, uiConfig2, z3, z2);
    }

    @NotNull
    /* renamed from: component1, reason: from getter */
    public final CharSequence getUpdateTitle() {
        return this.updateTitle;
    }

    @NotNull
    /* renamed from: component2, reason: from getter */
    public final CharSequence getUpdateContent() {
        return this.updateContent;
    }

    @NotNull
    /* renamed from: component3, reason: from getter */
    public final String getApkUrl() {
        return this.apkUrl;
    }

    @NotNull
    /* renamed from: component4, reason: from getter */
    public final UpdateConfig getConfig() {
        return this.config;
    }

    @NotNull
    /* renamed from: component5, reason: from getter */
    public final UiConfig getUiConfig() {
        return this.uiConfig;
    }

    /* renamed from: component6, reason: from getter */
    public final boolean getUseDefaultTitle() {
        return this.useDefaultTitle;
    }

    /* renamed from: component7, reason: from getter */
    public final boolean getUseDefaultContent() {
        return this.useDefaultContent;
    }

    @NotNull
    public final UpdateInfo copy(@NotNull CharSequence updateTitle, @NotNull CharSequence updateContent, @NotNull String apkUrl, @NotNull UpdateConfig config, @NotNull UiConfig uiConfig, boolean useDefaultTitle, boolean useDefaultContent) {
        Intrinsics.checkParameterIsNotNull(updateTitle, "updateTitle");
        Intrinsics.checkParameterIsNotNull(updateContent, "updateContent");
        Intrinsics.checkParameterIsNotNull(apkUrl, "apkUrl");
        Intrinsics.checkParameterIsNotNull(config, "config");
        Intrinsics.checkParameterIsNotNull(uiConfig, "uiConfig");
        return new UpdateInfo(updateTitle, updateContent, apkUrl, config, uiConfig, useDefaultTitle, useDefaultContent);
    }

    public boolean equals(@Nullable Object other) {
        if (this != other) {
            if (other instanceof UpdateInfo) {
                UpdateInfo updateInfo = (UpdateInfo) other;
                if (Intrinsics.areEqual(this.updateTitle, updateInfo.updateTitle) && Intrinsics.areEqual(this.updateContent, updateInfo.updateContent) && Intrinsics.areEqual(this.apkUrl, updateInfo.apkUrl) && Intrinsics.areEqual(this.config, updateInfo.config) && Intrinsics.areEqual(this.uiConfig, updateInfo.uiConfig)) {
                    if (this.useDefaultTitle == updateInfo.useDefaultTitle) {
                        if (this.useDefaultContent == updateInfo.useDefaultContent) {
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    @NotNull
    public final String getApkUrl() {
        return this.apkUrl;
    }

    @NotNull
    public final UpdateConfig getConfig() {
        return this.config;
    }

    @NotNull
    public final UiConfig getUiConfig() {
        return this.uiConfig;
    }

    @NotNull
    public final CharSequence getUpdateContent() {
        return this.updateContent;
    }

    @NotNull
    public final CharSequence getUpdateTitle() {
        return this.updateTitle;
    }

    public final boolean getUseDefaultContent() {
        return this.useDefaultContent;
    }

    public final boolean getUseDefaultTitle() {
        return this.useDefaultTitle;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        CharSequence charSequence = this.updateTitle;
        int hashCode = (charSequence != null ? charSequence.hashCode() : 0) * 31;
        CharSequence charSequence2 = this.updateContent;
        int hashCode2 = (hashCode + (charSequence2 != null ? charSequence2.hashCode() : 0)) * 31;
        String str = this.apkUrl;
        int hashCode3 = (hashCode2 + (str != null ? str.hashCode() : 0)) * 31;
        UpdateConfig updateConfig = this.config;
        int hashCode4 = (hashCode3 + (updateConfig != null ? updateConfig.hashCode() : 0)) * 31;
        UiConfig uiConfig = this.uiConfig;
        int hashCode5 = (hashCode4 + (uiConfig != null ? uiConfig.hashCode() : 0)) * 31;
        boolean z = this.useDefaultTitle;
        int i = z;
        if (z != 0) {
            i = 1;
        }
        int i2 = (hashCode5 + i) * 31;
        boolean z2 = this.useDefaultContent;
        int i3 = z2;
        if (z2 != 0) {
            i3 = 1;
        }
        return i2 + i3;
    }

    public final void setApkUrl(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.apkUrl = str;
    }

    public final void setConfig(@NotNull UpdateConfig updateConfig) {
        Intrinsics.checkParameterIsNotNull(updateConfig, "<set-?>");
        this.config = updateConfig;
    }

    public final void setUiConfig(@NotNull UiConfig uiConfig) {
        Intrinsics.checkParameterIsNotNull(uiConfig, "<set-?>");
        this.uiConfig = uiConfig;
    }

    public final void setUpdateContent(@NotNull CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "<set-?>");
        this.updateContent = charSequence;
    }

    public final void setUpdateTitle(@NotNull CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "<set-?>");
        this.updateTitle = charSequence;
    }

    public final void setUseDefaultContent(boolean z) {
        this.useDefaultContent = z;
    }

    public final void setUseDefaultTitle(boolean z) {
        this.useDefaultTitle = z;
    }

    @NotNull
    public String toString() {
        return "UpdateInfo(updateTitle=" + this.updateTitle + ", updateContent=" + this.updateContent + ", apkUrl=" + this.apkUrl + ", config=" + this.config + ", uiConfig=" + this.uiConfig + ", useDefaultTitle=" + this.useDefaultTitle + ", useDefaultContent=" + this.useDefaultContent + SQLBuilder.PARENTHESES_RIGHT;
    }
}
