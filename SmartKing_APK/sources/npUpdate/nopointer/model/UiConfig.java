package npUpdate.nopointer.model;

import com.litesuits.orm.db.assit.SQLBuilder;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UiConfig.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\r\n\u0002\bR\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001Bé\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0011¢\u0006\u0002\u0010\u001aJ\t\u0010M\u001a\u00020\u0003HÆ\u0003J\u0010\u0010N\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJ\u0010\u0010O\u001a\u0004\u0018\u00010\bHÆ\u0003¢\u0006\u0002\u0010)J\t\u0010P\u001a\u00020\u0011HÆ\u0003J\u0010\u0010Q\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJ\u0010\u0010R\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJ\u0010\u0010S\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJ\u0010\u0010T\u001a\u0004\u0018\u00010\bHÆ\u0003¢\u0006\u0002\u0010)J\t\u0010U\u001a\u00020\u0011HÆ\u0003J\t\u0010V\u001a\u00020\u0011HÆ\u0003J\t\u0010W\u001a\u00020\u0011HÆ\u0003J\u0010\u0010X\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJ\t\u0010Y\u001a\u00020\u0011HÆ\u0003J\u0010\u0010Z\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJ\u0010\u0010[\u001a\u0004\u0018\u00010\bHÆ\u0003¢\u0006\u0002\u0010)J\u0010\u0010\\\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJ\u0010\u0010]\u001a\u0004\u0018\u00010\bHÆ\u0003¢\u0006\u0002\u0010)J\u0010\u0010^\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJ\u0010\u0010_\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJ\u0010\u0010`\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u001cJò\u0001\u0010a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\u0016\u001a\u00020\u00112\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0018\u001a\u00020\u00112\b\b\u0002\u0010\u0019\u001a\u00020\u0011HÆ\u0001¢\u0006\u0002\u0010bJ\u0013\u0010c\u001a\u00020d2\b\u0010e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010f\u001a\u00020\u0005HÖ\u0001J\t\u0010g\u001a\u00020\u0003HÖ\u0001R\u001e\u0010\u0012\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001e\u0010\u0013\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b \u0010\u001c\"\u0004\b!\u0010\u001eR\u001a\u0010\u0016\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001e\u0010\u0014\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b&\u0010\u001c\"\u0004\b'\u0010\u001eR\u001e\u0010\u0015\u001a\u0004\u0018\u00010\bX\u0086\u000e¢\u0006\u0010\n\u0002\u0010,\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u001e\u0010\u000b\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b-\u0010\u001c\"\u0004\b.\u0010\u001eR\u001e\u0010\n\u001a\u0004\u0018\u00010\bX\u0086\u000e¢\u0006\u0010\n\u0002\u0010,\u001a\u0004\b/\u0010)\"\u0004\b0\u0010+R\u001e\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b1\u0010\u001c\"\u0004\b2\u0010\u001eR\u001a\u0010\u0019\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u0010#\"\u0004\b4\u0010%R\u001a\u0010\u0018\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b5\u0010#\"\u0004\b6\u0010%R\u001a\u0010\u0017\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b7\u0010#\"\u0004\b8\u0010%R\u001e\u0010\t\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b9\u0010\u001c\"\u0004\b:\u0010\u001eR\u001e\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0086\u000e¢\u0006\u0010\n\u0002\u0010,\u001a\u0004\b;\u0010)\"\u0004\b<\u0010+R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u0010>\"\u0004\b?\u0010@R\u001e\u0010\f\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\bA\u0010\u001c\"\u0004\bB\u0010\u001eR\u001e\u0010\r\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\bC\u0010\u001c\"\u0004\bD\u0010\u001eR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bE\u0010#\"\u0004\bF\u0010%R\u001e\u0010\u000e\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\bG\u0010\u001c\"\u0004\bH\u0010\u001eR\u001e\u0010\u000f\u001a\u0004\u0018\u00010\bX\u0086\u000e¢\u0006\u0010\n\u0002\u0010,\u001a\u0004\bI\u0010)\"\u0004\bJ\u0010+R\u001e\u0010\u0006\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\bK\u0010\u001c\"\u0004\bL\u0010\u001e¨\u0006h"}, d2 = {"LnpUpdate/nopointer/model/UiConfig;", "", "uiType", "", "customLayoutId", "", "updateLogoImgRes", "titleTextSize", "", "titleTextColor", "contentTextSize", "contentTextColor", "updateBtnBgColor", "updateBtnBgRes", "updateBtnTextColor", "updateBtnTextSize", "updateBtnText", "", "cancelBtnBgColor", "cancelBtnBgRes", "cancelBtnTextColor", "cancelBtnTextSize", "cancelBtnText", "downloadingToastText", "downloadingBtnText", "downloadFailText", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/CharSequence;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;)V", "getCancelBtnBgColor", "()Ljava/lang/Integer;", "setCancelBtnBgColor", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getCancelBtnBgRes", "setCancelBtnBgRes", "getCancelBtnText", "()Ljava/lang/CharSequence;", "setCancelBtnText", "(Ljava/lang/CharSequence;)V", "getCancelBtnTextColor", "setCancelBtnTextColor", "getCancelBtnTextSize", "()Ljava/lang/Float;", "setCancelBtnTextSize", "(Ljava/lang/Float;)V", "Ljava/lang/Float;", "getContentTextColor", "setContentTextColor", "getContentTextSize", "setContentTextSize", "getCustomLayoutId", "setCustomLayoutId", "getDownloadFailText", "setDownloadFailText", "getDownloadingBtnText", "setDownloadingBtnText", "getDownloadingToastText", "setDownloadingToastText", "getTitleTextColor", "setTitleTextColor", "getTitleTextSize", "setTitleTextSize", "getUiType", "()Ljava/lang/String;", "setUiType", "(Ljava/lang/String;)V", "getUpdateBtnBgColor", "setUpdateBtnBgColor", "getUpdateBtnBgRes", "setUpdateBtnBgRes", "getUpdateBtnText", "setUpdateBtnText", "getUpdateBtnTextColor", "setUpdateBtnTextColor", "getUpdateBtnTextSize", "setUpdateBtnTextSize", "getUpdateLogoImgRes", "setUpdateLogoImgRes", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/CharSequence;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;)LnpUpdate/nopointer/model/UiConfig;", "equals", "", "other", "hashCode", "toString", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final /* data */ class UiConfig {

    @Nullable
    private Integer cancelBtnBgColor;

    @Nullable
    private Integer cancelBtnBgRes;

    @NotNull
    private CharSequence cancelBtnText;

    @Nullable
    private Integer cancelBtnTextColor;

    @Nullable
    private Float cancelBtnTextSize;

    @Nullable
    private Integer contentTextColor;

    @Nullable
    private Float contentTextSize;

    @Nullable
    private Integer customLayoutId;

    @NotNull
    private CharSequence downloadFailText;

    @NotNull
    private CharSequence downloadingBtnText;

    @NotNull
    private CharSequence downloadingToastText;

    @Nullable
    private Integer titleTextColor;

    @Nullable
    private Float titleTextSize;

    @NotNull
    private String uiType;

    @Nullable
    private Integer updateBtnBgColor;

    @Nullable
    private Integer updateBtnBgRes;

    @NotNull
    private CharSequence updateBtnText;

    @Nullable
    private Integer updateBtnTextColor;

    @Nullable
    private Float updateBtnTextSize;

    @Nullable
    private Integer updateLogoImgRes;

    public UiConfig() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1048575, null);
    }

    public UiConfig(@NotNull String uiType, @Nullable Integer num, @Nullable Integer num2, @Nullable Float f, @Nullable Integer num3, @Nullable Float f2, @Nullable Integer num4, @Nullable Integer num5, @Nullable Integer num6, @Nullable Integer num7, @Nullable Float f3, @NotNull CharSequence updateBtnText, @Nullable Integer num8, @Nullable Integer num9, @Nullable Integer num10, @Nullable Float f4, @NotNull CharSequence cancelBtnText, @NotNull CharSequence downloadingToastText, @NotNull CharSequence downloadingBtnText, @NotNull CharSequence downloadFailText) {
        Intrinsics.checkParameterIsNotNull(uiType, "uiType");
        Intrinsics.checkParameterIsNotNull(updateBtnText, "updateBtnText");
        Intrinsics.checkParameterIsNotNull(cancelBtnText, "cancelBtnText");
        Intrinsics.checkParameterIsNotNull(downloadingToastText, "downloadingToastText");
        Intrinsics.checkParameterIsNotNull(downloadingBtnText, "downloadingBtnText");
        Intrinsics.checkParameterIsNotNull(downloadFailText, "downloadFailText");
        this.uiType = uiType;
        this.customLayoutId = num;
        this.updateLogoImgRes = num2;
        this.titleTextSize = f;
        this.titleTextColor = num3;
        this.contentTextSize = f2;
        this.contentTextColor = num4;
        this.updateBtnBgColor = num5;
        this.updateBtnBgRes = num6;
        this.updateBtnTextColor = num7;
        this.updateBtnTextSize = f3;
        this.updateBtnText = updateBtnText;
        this.cancelBtnBgColor = num8;
        this.cancelBtnBgRes = num9;
        this.cancelBtnTextColor = num10;
        this.cancelBtnTextSize = f4;
        this.cancelBtnText = cancelBtnText;
        this.downloadingToastText = downloadingToastText;
        this.downloadingBtnText = downloadingBtnText;
        this.downloadFailText = downloadFailText;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ UiConfig(java.lang.String r23, java.lang.Integer r24, java.lang.Integer r25, java.lang.Float r26, java.lang.Integer r27, java.lang.Float r28, java.lang.Integer r29, java.lang.Integer r30, java.lang.Integer r31, java.lang.Integer r32, java.lang.Float r33, java.lang.CharSequence r34, java.lang.Integer r35, java.lang.Integer r36, java.lang.Integer r37, java.lang.Float r38, java.lang.CharSequence r39, java.lang.CharSequence r40, java.lang.CharSequence r41, java.lang.CharSequence r42, int r43, kotlin.jvm.internal.DefaultConstructorMarker r44) {
        /*
            Method dump skipped, instructions count: 353
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: npUpdate.nopointer.model.UiConfig.<init>(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Float, java.lang.Integer, java.lang.Float, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Float, java.lang.CharSequence, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Float, java.lang.CharSequence, java.lang.CharSequence, java.lang.CharSequence, java.lang.CharSequence, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    @NotNull
    public static /* synthetic */ UiConfig copy$default(UiConfig uiConfig, String str, Integer num, Integer num2, Float f, Integer num3, Float f2, Integer num4, Integer num5, Integer num6, Integer num7, Float f3, CharSequence charSequence, Integer num8, Integer num9, Integer num10, Float f4, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, CharSequence charSequence5, int i, Object obj) {
        Integer num11;
        Float f5;
        Float f6;
        CharSequence charSequence6;
        CharSequence charSequence7;
        CharSequence charSequence8;
        CharSequence charSequence9;
        CharSequence charSequence10;
        String str2 = (i & 1) != 0 ? uiConfig.uiType : str;
        Integer num12 = (i & 2) != 0 ? uiConfig.customLayoutId : num;
        Integer num13 = (i & 4) != 0 ? uiConfig.updateLogoImgRes : num2;
        Float f7 = (i & 8) != 0 ? uiConfig.titleTextSize : f;
        Integer num14 = (i & 16) != 0 ? uiConfig.titleTextColor : num3;
        Float f8 = (i & 32) != 0 ? uiConfig.contentTextSize : f2;
        Integer num15 = (i & 64) != 0 ? uiConfig.contentTextColor : num4;
        Integer num16 = (i & 128) != 0 ? uiConfig.updateBtnBgColor : num5;
        Integer num17 = (i & 256) != 0 ? uiConfig.updateBtnBgRes : num6;
        Integer num18 = (i & 512) != 0 ? uiConfig.updateBtnTextColor : num7;
        Float f9 = (i & 1024) != 0 ? uiConfig.updateBtnTextSize : f3;
        CharSequence charSequence11 = (i & 2048) != 0 ? uiConfig.updateBtnText : charSequence;
        Integer num19 = (i & 4096) != 0 ? uiConfig.cancelBtnBgColor : num8;
        Integer num20 = (i & 8192) != 0 ? uiConfig.cancelBtnBgRes : num9;
        Integer num21 = (i & 16384) != 0 ? uiConfig.cancelBtnTextColor : num10;
        if ((i & 32768) != 0) {
            num11 = num21;
            f5 = uiConfig.cancelBtnTextSize;
        } else {
            num11 = num21;
            f5 = f4;
        }
        if ((i & 65536) != 0) {
            f6 = f5;
            charSequence6 = uiConfig.cancelBtnText;
        } else {
            f6 = f5;
            charSequence6 = charSequence2;
        }
        if ((i & 131072) != 0) {
            charSequence7 = charSequence6;
            charSequence8 = uiConfig.downloadingToastText;
        } else {
            charSequence7 = charSequence6;
            charSequence8 = charSequence3;
        }
        if ((i & 262144) != 0) {
            charSequence9 = charSequence8;
            charSequence10 = uiConfig.downloadingBtnText;
        } else {
            charSequence9 = charSequence8;
            charSequence10 = charSequence4;
        }
        return uiConfig.copy(str2, num12, num13, f7, num14, f8, num15, num16, num17, num18, f9, charSequence11, num19, num20, num11, f6, charSequence7, charSequence9, charSequence10, (i & 524288) != 0 ? uiConfig.downloadFailText : charSequence5);
    }

    @NotNull
    /* renamed from: component1, reason: from getter */
    public final String getUiType() {
        return this.uiType;
    }

    @Nullable
    /* renamed from: component10, reason: from getter */
    public final Integer getUpdateBtnTextColor() {
        return this.updateBtnTextColor;
    }

    @Nullable
    /* renamed from: component11, reason: from getter */
    public final Float getUpdateBtnTextSize() {
        return this.updateBtnTextSize;
    }

    @NotNull
    /* renamed from: component12, reason: from getter */
    public final CharSequence getUpdateBtnText() {
        return this.updateBtnText;
    }

    @Nullable
    /* renamed from: component13, reason: from getter */
    public final Integer getCancelBtnBgColor() {
        return this.cancelBtnBgColor;
    }

    @Nullable
    /* renamed from: component14, reason: from getter */
    public final Integer getCancelBtnBgRes() {
        return this.cancelBtnBgRes;
    }

    @Nullable
    /* renamed from: component15, reason: from getter */
    public final Integer getCancelBtnTextColor() {
        return this.cancelBtnTextColor;
    }

    @Nullable
    /* renamed from: component16, reason: from getter */
    public final Float getCancelBtnTextSize() {
        return this.cancelBtnTextSize;
    }

    @NotNull
    /* renamed from: component17, reason: from getter */
    public final CharSequence getCancelBtnText() {
        return this.cancelBtnText;
    }

    @NotNull
    /* renamed from: component18, reason: from getter */
    public final CharSequence getDownloadingToastText() {
        return this.downloadingToastText;
    }

    @NotNull
    /* renamed from: component19, reason: from getter */
    public final CharSequence getDownloadingBtnText() {
        return this.downloadingBtnText;
    }

    @Nullable
    /* renamed from: component2, reason: from getter */
    public final Integer getCustomLayoutId() {
        return this.customLayoutId;
    }

    @NotNull
    /* renamed from: component20, reason: from getter */
    public final CharSequence getDownloadFailText() {
        return this.downloadFailText;
    }

    @Nullable
    /* renamed from: component3, reason: from getter */
    public final Integer getUpdateLogoImgRes() {
        return this.updateLogoImgRes;
    }

    @Nullable
    /* renamed from: component4, reason: from getter */
    public final Float getTitleTextSize() {
        return this.titleTextSize;
    }

    @Nullable
    /* renamed from: component5, reason: from getter */
    public final Integer getTitleTextColor() {
        return this.titleTextColor;
    }

    @Nullable
    /* renamed from: component6, reason: from getter */
    public final Float getContentTextSize() {
        return this.contentTextSize;
    }

    @Nullable
    /* renamed from: component7, reason: from getter */
    public final Integer getContentTextColor() {
        return this.contentTextColor;
    }

    @Nullable
    /* renamed from: component8, reason: from getter */
    public final Integer getUpdateBtnBgColor() {
        return this.updateBtnBgColor;
    }

    @Nullable
    /* renamed from: component9, reason: from getter */
    public final Integer getUpdateBtnBgRes() {
        return this.updateBtnBgRes;
    }

    @NotNull
    public final UiConfig copy(@NotNull String uiType, @Nullable Integer customLayoutId, @Nullable Integer updateLogoImgRes, @Nullable Float titleTextSize, @Nullable Integer titleTextColor, @Nullable Float contentTextSize, @Nullable Integer contentTextColor, @Nullable Integer updateBtnBgColor, @Nullable Integer updateBtnBgRes, @Nullable Integer updateBtnTextColor, @Nullable Float updateBtnTextSize, @NotNull CharSequence updateBtnText, @Nullable Integer cancelBtnBgColor, @Nullable Integer cancelBtnBgRes, @Nullable Integer cancelBtnTextColor, @Nullable Float cancelBtnTextSize, @NotNull CharSequence cancelBtnText, @NotNull CharSequence downloadingToastText, @NotNull CharSequence downloadingBtnText, @NotNull CharSequence downloadFailText) {
        Intrinsics.checkParameterIsNotNull(uiType, "uiType");
        Intrinsics.checkParameterIsNotNull(updateBtnText, "updateBtnText");
        Intrinsics.checkParameterIsNotNull(cancelBtnText, "cancelBtnText");
        Intrinsics.checkParameterIsNotNull(downloadingToastText, "downloadingToastText");
        Intrinsics.checkParameterIsNotNull(downloadingBtnText, "downloadingBtnText");
        Intrinsics.checkParameterIsNotNull(downloadFailText, "downloadFailText");
        return new UiConfig(uiType, customLayoutId, updateLogoImgRes, titleTextSize, titleTextColor, contentTextSize, contentTextColor, updateBtnBgColor, updateBtnBgRes, updateBtnTextColor, updateBtnTextSize, updateBtnText, cancelBtnBgColor, cancelBtnBgRes, cancelBtnTextColor, cancelBtnTextSize, cancelBtnText, downloadingToastText, downloadingBtnText, downloadFailText);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UiConfig)) {
            return false;
        }
        UiConfig uiConfig = (UiConfig) other;
        return Intrinsics.areEqual(this.uiType, uiConfig.uiType) && Intrinsics.areEqual(this.customLayoutId, uiConfig.customLayoutId) && Intrinsics.areEqual(this.updateLogoImgRes, uiConfig.updateLogoImgRes) && Intrinsics.areEqual((Object) this.titleTextSize, (Object) uiConfig.titleTextSize) && Intrinsics.areEqual(this.titleTextColor, uiConfig.titleTextColor) && Intrinsics.areEqual((Object) this.contentTextSize, (Object) uiConfig.contentTextSize) && Intrinsics.areEqual(this.contentTextColor, uiConfig.contentTextColor) && Intrinsics.areEqual(this.updateBtnBgColor, uiConfig.updateBtnBgColor) && Intrinsics.areEqual(this.updateBtnBgRes, uiConfig.updateBtnBgRes) && Intrinsics.areEqual(this.updateBtnTextColor, uiConfig.updateBtnTextColor) && Intrinsics.areEqual((Object) this.updateBtnTextSize, (Object) uiConfig.updateBtnTextSize) && Intrinsics.areEqual(this.updateBtnText, uiConfig.updateBtnText) && Intrinsics.areEqual(this.cancelBtnBgColor, uiConfig.cancelBtnBgColor) && Intrinsics.areEqual(this.cancelBtnBgRes, uiConfig.cancelBtnBgRes) && Intrinsics.areEqual(this.cancelBtnTextColor, uiConfig.cancelBtnTextColor) && Intrinsics.areEqual((Object) this.cancelBtnTextSize, (Object) uiConfig.cancelBtnTextSize) && Intrinsics.areEqual(this.cancelBtnText, uiConfig.cancelBtnText) && Intrinsics.areEqual(this.downloadingToastText, uiConfig.downloadingToastText) && Intrinsics.areEqual(this.downloadingBtnText, uiConfig.downloadingBtnText) && Intrinsics.areEqual(this.downloadFailText, uiConfig.downloadFailText);
    }

    @Nullable
    public final Integer getCancelBtnBgColor() {
        return this.cancelBtnBgColor;
    }

    @Nullable
    public final Integer getCancelBtnBgRes() {
        return this.cancelBtnBgRes;
    }

    @NotNull
    public final CharSequence getCancelBtnText() {
        return this.cancelBtnText;
    }

    @Nullable
    public final Integer getCancelBtnTextColor() {
        return this.cancelBtnTextColor;
    }

    @Nullable
    public final Float getCancelBtnTextSize() {
        return this.cancelBtnTextSize;
    }

    @Nullable
    public final Integer getContentTextColor() {
        return this.contentTextColor;
    }

    @Nullable
    public final Float getContentTextSize() {
        return this.contentTextSize;
    }

    @Nullable
    public final Integer getCustomLayoutId() {
        return this.customLayoutId;
    }

    @NotNull
    public final CharSequence getDownloadFailText() {
        return this.downloadFailText;
    }

    @NotNull
    public final CharSequence getDownloadingBtnText() {
        return this.downloadingBtnText;
    }

    @NotNull
    public final CharSequence getDownloadingToastText() {
        return this.downloadingToastText;
    }

    @Nullable
    public final Integer getTitleTextColor() {
        return this.titleTextColor;
    }

    @Nullable
    public final Float getTitleTextSize() {
        return this.titleTextSize;
    }

    @NotNull
    public final String getUiType() {
        return this.uiType;
    }

    @Nullable
    public final Integer getUpdateBtnBgColor() {
        return this.updateBtnBgColor;
    }

    @Nullable
    public final Integer getUpdateBtnBgRes() {
        return this.updateBtnBgRes;
    }

    @NotNull
    public final CharSequence getUpdateBtnText() {
        return this.updateBtnText;
    }

    @Nullable
    public final Integer getUpdateBtnTextColor() {
        return this.updateBtnTextColor;
    }

    @Nullable
    public final Float getUpdateBtnTextSize() {
        return this.updateBtnTextSize;
    }

    @Nullable
    public final Integer getUpdateLogoImgRes() {
        return this.updateLogoImgRes;
    }

    public int hashCode() {
        String str = this.uiType;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        Integer num = this.customLayoutId;
        int hashCode2 = (hashCode + (num != null ? num.hashCode() : 0)) * 31;
        Integer num2 = this.updateLogoImgRes;
        int hashCode3 = (hashCode2 + (num2 != null ? num2.hashCode() : 0)) * 31;
        Float f = this.titleTextSize;
        int hashCode4 = (hashCode3 + (f != null ? f.hashCode() : 0)) * 31;
        Integer num3 = this.titleTextColor;
        int hashCode5 = (hashCode4 + (num3 != null ? num3.hashCode() : 0)) * 31;
        Float f2 = this.contentTextSize;
        int hashCode6 = (hashCode5 + (f2 != null ? f2.hashCode() : 0)) * 31;
        Integer num4 = this.contentTextColor;
        int hashCode7 = (hashCode6 + (num4 != null ? num4.hashCode() : 0)) * 31;
        Integer num5 = this.updateBtnBgColor;
        int hashCode8 = (hashCode7 + (num5 != null ? num5.hashCode() : 0)) * 31;
        Integer num6 = this.updateBtnBgRes;
        int hashCode9 = (hashCode8 + (num6 != null ? num6.hashCode() : 0)) * 31;
        Integer num7 = this.updateBtnTextColor;
        int hashCode10 = (hashCode9 + (num7 != null ? num7.hashCode() : 0)) * 31;
        Float f3 = this.updateBtnTextSize;
        int hashCode11 = (hashCode10 + (f3 != null ? f3.hashCode() : 0)) * 31;
        CharSequence charSequence = this.updateBtnText;
        int hashCode12 = (hashCode11 + (charSequence != null ? charSequence.hashCode() : 0)) * 31;
        Integer num8 = this.cancelBtnBgColor;
        int hashCode13 = (hashCode12 + (num8 != null ? num8.hashCode() : 0)) * 31;
        Integer num9 = this.cancelBtnBgRes;
        int hashCode14 = (hashCode13 + (num9 != null ? num9.hashCode() : 0)) * 31;
        Integer num10 = this.cancelBtnTextColor;
        int hashCode15 = (hashCode14 + (num10 != null ? num10.hashCode() : 0)) * 31;
        Float f4 = this.cancelBtnTextSize;
        int hashCode16 = (hashCode15 + (f4 != null ? f4.hashCode() : 0)) * 31;
        CharSequence charSequence2 = this.cancelBtnText;
        int hashCode17 = (hashCode16 + (charSequence2 != null ? charSequence2.hashCode() : 0)) * 31;
        CharSequence charSequence3 = this.downloadingToastText;
        int hashCode18 = (hashCode17 + (charSequence3 != null ? charSequence3.hashCode() : 0)) * 31;
        CharSequence charSequence4 = this.downloadingBtnText;
        int hashCode19 = (hashCode18 + (charSequence4 != null ? charSequence4.hashCode() : 0)) * 31;
        CharSequence charSequence5 = this.downloadFailText;
        return hashCode19 + (charSequence5 != null ? charSequence5.hashCode() : 0);
    }

    public final void setCancelBtnBgColor(@Nullable Integer num) {
        this.cancelBtnBgColor = num;
    }

    public final void setCancelBtnBgRes(@Nullable Integer num) {
        this.cancelBtnBgRes = num;
    }

    public final void setCancelBtnText(@NotNull CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "<set-?>");
        this.cancelBtnText = charSequence;
    }

    public final void setCancelBtnTextColor(@Nullable Integer num) {
        this.cancelBtnTextColor = num;
    }

    public final void setCancelBtnTextSize(@Nullable Float f) {
        this.cancelBtnTextSize = f;
    }

    public final void setContentTextColor(@Nullable Integer num) {
        this.contentTextColor = num;
    }

    public final void setContentTextSize(@Nullable Float f) {
        this.contentTextSize = f;
    }

    public final void setCustomLayoutId(@Nullable Integer num) {
        this.customLayoutId = num;
    }

    public final void setDownloadFailText(@NotNull CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "<set-?>");
        this.downloadFailText = charSequence;
    }

    public final void setDownloadingBtnText(@NotNull CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "<set-?>");
        this.downloadingBtnText = charSequence;
    }

    public final void setDownloadingToastText(@NotNull CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "<set-?>");
        this.downloadingToastText = charSequence;
    }

    public final void setTitleTextColor(@Nullable Integer num) {
        this.titleTextColor = num;
    }

    public final void setTitleTextSize(@Nullable Float f) {
        this.titleTextSize = f;
    }

    public final void setUiType(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.uiType = str;
    }

    public final void setUpdateBtnBgColor(@Nullable Integer num) {
        this.updateBtnBgColor = num;
    }

    public final void setUpdateBtnBgRes(@Nullable Integer num) {
        this.updateBtnBgRes = num;
    }

    public final void setUpdateBtnText(@NotNull CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "<set-?>");
        this.updateBtnText = charSequence;
    }

    public final void setUpdateBtnTextColor(@Nullable Integer num) {
        this.updateBtnTextColor = num;
    }

    public final void setUpdateBtnTextSize(@Nullable Float f) {
        this.updateBtnTextSize = f;
    }

    public final void setUpdateLogoImgRes(@Nullable Integer num) {
        this.updateLogoImgRes = num;
    }

    @NotNull
    public String toString() {
        return "UiConfig(uiType=" + this.uiType + ", customLayoutId=" + this.customLayoutId + ", updateLogoImgRes=" + this.updateLogoImgRes + ", titleTextSize=" + this.titleTextSize + ", titleTextColor=" + this.titleTextColor + ", contentTextSize=" + this.contentTextSize + ", contentTextColor=" + this.contentTextColor + ", updateBtnBgColor=" + this.updateBtnBgColor + ", updateBtnBgRes=" + this.updateBtnBgRes + ", updateBtnTextColor=" + this.updateBtnTextColor + ", updateBtnTextSize=" + this.updateBtnTextSize + ", updateBtnText=" + this.updateBtnText + ", cancelBtnBgColor=" + this.cancelBtnBgColor + ", cancelBtnBgRes=" + this.cancelBtnBgRes + ", cancelBtnTextColor=" + this.cancelBtnTextColor + ", cancelBtnTextSize=" + this.cancelBtnTextSize + ", cancelBtnText=" + this.cancelBtnText + ", downloadingToastText=" + this.downloadingToastText + ", downloadingBtnText=" + this.downloadingBtnText + ", downloadFailText=" + this.downloadFailText + SQLBuilder.PARENTHESES_RIGHT;
    }
}
