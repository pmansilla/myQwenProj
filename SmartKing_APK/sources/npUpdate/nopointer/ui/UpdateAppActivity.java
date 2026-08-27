package npUpdate.nopointer.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import java.util.HashMap;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import npUpdate.nopointer.R;
import npUpdate.nopointer.constacne.UiType;
import npUpdate.nopointer.extension.ViewKt;
import npUpdate.nopointer.listener.OnBtnClickListener;
import npUpdate.nopointer.listener.OnInitUiListener;
import npUpdate.nopointer.model.UiConfig;
import npUpdate.nopointer.model.UpdateConfig;
import npUpdate.nopointer.model.UpdateInfo;
import npUpdate.nopointer.update.DownloadAppUtils;
import npUpdate.nopointer.update.UpdateAppUtils;
import npUpdate.nopointer.util.AlertDialogUtil;
import npUpdate.nopointer.util.GlobalContextProvider;
import npUpdate.nopointer.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UpdateAppActivity.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0007\b\u0000\u0018\u0000 02\u00020\u0001:\u00010B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\u001bH\u0016J\b\u0010\u001d\u001a\u00020\u001bH\u0002J\b\u0010\u001e\u001a\u00020\u001bH\u0003J\b\u0010\u001f\u001a\u00020\u001bH\u0016J\u0012\u0010 \u001a\u00020\u001b2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0014J+\u0010#\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020%2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020(0'2\u0006\u0010)\u001a\u00020*H\u0016¢\u0006\u0002\u0010+J\b\u0010,\u001a\u00020\u001bH\u0014J\b\u0010-\u001a\u00020\u001bH\u0002J\b\u0010.\u001a\u00020\u001bH\u0003J\u0006\u0010/\u001a\u00020\u001bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\rR\u001b\u0010\u0010\u001a\u00020\u00118BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0014\u0010\u000f\u001a\u0004\b\u0012\u0010\u0013R\u001b\u0010\u0015\u001a\u00020\u00168BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0019\u0010\u000f\u001a\u0004\b\u0017\u0010\u0018¨\u00061"}, d2 = {"LnpUpdate/nopointer/ui/UpdateAppActivity;", "Landroid/support/v7/app/AppCompatActivity;", "()V", "cancelBtn", "Landroid/widget/TextView;", "ivLogo", "Landroid/widget/ImageView;", "sureBtn", "tvContent", "tvTitle", "uiConfig", "LnpUpdate/nopointer/model/UiConfig;", "getUiConfig", "()LnpUpdate/nopointer/model/UiConfig;", "uiConfig$delegate", "Lkotlin/Lazy;", "updateConfig", "LnpUpdate/nopointer/model/UpdateConfig;", "getUpdateConfig", "()LnpUpdate/nopointer/model/UpdateConfig;", "updateConfig$delegate", "updateInfo", "LnpUpdate/nopointer/model/UpdateInfo;", "getUpdateInfo", "()LnpUpdate/nopointer/model/UpdateInfo;", "updateInfo$delegate", "download", "", "finish", "initUi", "initView", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onRequestPermissionsResult", "requestCode", "", "permissions", "", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "preDownLoad", "realDownload", "refreshUI", "Companion", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class UpdateAppActivity extends AppCompatActivity {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(UpdateAppActivity.class), "updateInfo", "getUpdateInfo()LnpUpdate/nopointer/model/UpdateInfo;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(UpdateAppActivity.class), "updateConfig", "getUpdateConfig()LnpUpdate/nopointer/model/UpdateConfig;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(UpdateAppActivity.class), "uiConfig", "getUiConfig()LnpUpdate/nopointer/model/UiConfig;"))};

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int PERMISSION_CODE = 1001;
    private static final String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
    private HashMap _$_findViewCache;
    private TextView cancelBtn;
    private ImageView ivLogo;
    private TextView sureBtn;
    private TextView tvContent;
    private TextView tvTitle;

    /* renamed from: updateInfo$delegate, reason: from kotlin metadata */
    private final Lazy updateInfo = LazyKt.lazy(new Function0<UpdateInfo>() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$updateInfo$2
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final UpdateInfo invoke() {
            return UpdateAppUtils.INSTANCE.getUpdateInfo$npUpdate_release();
        }
    });

    /* renamed from: updateConfig$delegate, reason: from kotlin metadata */
    private final Lazy updateConfig = LazyKt.lazy(new Function0<UpdateConfig>() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$updateConfig$2
        /* JADX INFO: Access modifiers changed from: package-private */
        {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final UpdateConfig invoke() {
            UpdateInfo updateInfo;
            updateInfo = UpdateAppActivity.this.getUpdateInfo();
            return updateInfo.getConfig();
        }
    });

    /* renamed from: uiConfig$delegate, reason: from kotlin metadata */
    private final Lazy uiConfig = LazyKt.lazy(new Function0<UiConfig>() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$uiConfig$2
        /* JADX INFO: Access modifiers changed from: package-private */
        {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final UiConfig invoke() {
            UpdateInfo updateInfo;
            updateInfo = UpdateAppActivity.this.getUpdateInfo();
            return updateInfo.getUiConfig();
        }
    });

    /* compiled from: UpdateAppActivity.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"LnpUpdate/nopointer/ui/UpdateAppActivity$Companion;", "", "()V", "PERMISSION_CODE", "", "permission", "", "launch", "", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
    /* loaded from: classes2.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void launch() {
            Context applicationContext = GlobalContextProvider.INSTANCE.getGlobalContext().getApplicationContext();
            Intent intent = new Intent(applicationContext, (Class<?>) UpdateAppActivity.class);
            intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            applicationContext.startActivity(intent);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void download() {
        /*
            r14 = this;
            android.content.Intent r0 = new android.content.Intent
            r1 = r14
            android.content.Context r1 = (android.content.Context) r1
            java.lang.Class<npUpdate.nopointer.update.UpdateAppService> r2 = npUpdate.nopointer.update.UpdateAppService.class
            r0.<init>(r1, r2)
            r14.startService(r0)
            npUpdate.nopointer.model.UpdateConfig r0 = r14.getUpdateConfig()
            int r0 = r0.getDownloadBy()
            switch(r0) {
                case 257: goto L27;
                case 258: goto L19;
                default: goto L18;
            }
        L18:
            goto L75
        L19:
            npUpdate.nopointer.update.DownloadAppUtils r0 = npUpdate.nopointer.update.DownloadAppUtils.INSTANCE
            npUpdate.nopointer.model.UpdateInfo r1 = r14.getUpdateInfo()
            java.lang.String r1 = r1.getApkUrl()
            r0.downloadForWebView(r1)
            goto L75
        L27:
            npUpdate.nopointer.model.UpdateConfig r0 = r14.getUpdateConfig()
            boolean r0 = r0.getCheckWifi()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L46
            npUpdate.nopointer.util.Utils r0 = npUpdate.nopointer.util.Utils.INSTANCE
            android.content.Context r3 = r14.getApplicationContext()
            java.lang.String r4 = "applicationContext"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r3, r4)
            boolean r0 = r0.isWifiConnected(r3)
            if (r0 != 0) goto L46
            r0 = 1
            goto L47
        L46:
            r0 = 0
        L47:
            if (r0 != r2) goto L6b
            npUpdate.nopointer.util.AlertDialogUtil r3 = npUpdate.nopointer.util.AlertDialogUtil.INSTANCE
            r4 = r14
            android.app.Activity r4 = (android.app.Activity) r4
            int r5 = npUpdate.nopointer.R.string.check_wifi_notice
            java.lang.String r5 = r14.getString(r5)
            java.lang.String r6 = "getString(R.string.check_wifi_notice)"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r5, r6)
            r6 = 0
            npUpdate.nopointer.ui.UpdateAppActivity$download$$inlined$yes$lambda$1 r7 = new npUpdate.nopointer.ui.UpdateAppActivity$download$$inlined$yes$lambda$1
            r7.<init>()
            kotlin.jvm.functions.Function0 r7 = (kotlin.jvm.functions.Function0) r7
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = 0
            r12 = 244(0xf4, float:3.42E-43)
            r13 = 0
            npUpdate.nopointer.util.AlertDialogUtil.show$default(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
        L6b:
            if (r0 != r2) goto L6e
            r1 = 1
        L6e:
            r0 = r1 ^ 1
            if (r0 == 0) goto L75
            r14.realDownload()
        L75:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: npUpdate.nopointer.ui.UpdateAppActivity.download():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final UiConfig getUiConfig() {
        Lazy lazy = this.uiConfig;
        KProperty kProperty = $$delegatedProperties[2];
        return (UiConfig) lazy.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final UpdateConfig getUpdateConfig() {
        Lazy lazy = this.updateConfig;
        KProperty kProperty = $$delegatedProperties[1];
        return (UpdateConfig) lazy.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final UpdateInfo getUpdateInfo() {
        Lazy lazy = this.updateInfo;
        KProperty kProperty = $$delegatedProperties[0];
        return (UpdateInfo) lazy.getValue();
    }

    private final void initUi() {
        UiConfig uiConfig = getUiConfig();
        Integer updateLogoImgRes = uiConfig.getUpdateLogoImgRes();
        if (updateLogoImgRes != null) {
            int intValue = updateLogoImgRes.intValue();
            ImageView imageView = this.ivLogo;
            if (imageView != null) {
                imageView.setImageResource(intValue);
            }
        }
        Integer titleTextColor = uiConfig.getTitleTextColor();
        if (titleTextColor != null) {
            int intValue2 = titleTextColor.intValue();
            TextView textView = this.tvTitle;
            if (textView != null) {
                textView.setTextColor(intValue2);
            }
        }
        Float titleTextSize = uiConfig.getTitleTextSize();
        if (titleTextSize != null) {
            float floatValue = titleTextSize.floatValue();
            TextView textView2 = this.tvTitle;
            if (textView2 != null) {
                textView2.setTextSize(floatValue);
            }
        }
        Integer contentTextColor = uiConfig.getContentTextColor();
        if (contentTextColor != null) {
            int intValue3 = contentTextColor.intValue();
            TextView textView3 = this.tvContent;
            if (textView3 != null) {
                textView3.setTextColor(intValue3);
            }
        }
        Float contentTextSize = uiConfig.getContentTextSize();
        if (contentTextSize != null) {
            float floatValue2 = contentTextSize.floatValue();
            TextView textView4 = this.tvContent;
            if (textView4 != null) {
                textView4.setTextSize(floatValue2);
            }
        }
        Integer updateBtnBgColor = uiConfig.getUpdateBtnBgColor();
        if (updateBtnBgColor != null) {
            int intValue4 = updateBtnBgColor.intValue();
            TextView textView5 = this.sureBtn;
            if (textView5 != null) {
                textView5.setBackgroundColor(intValue4);
            }
        }
        Integer updateBtnBgRes = uiConfig.getUpdateBtnBgRes();
        if (updateBtnBgRes != null) {
            int intValue5 = updateBtnBgRes.intValue();
            TextView textView6 = this.sureBtn;
            if (textView6 != null) {
                textView6.setBackgroundResource(intValue5);
            }
        }
        if (this.sureBtn instanceof TextView) {
            Integer updateBtnTextColor = uiConfig.getUpdateBtnTextColor();
            if (updateBtnTextColor != null) {
                int intValue6 = updateBtnTextColor.intValue();
                TextView textView7 = this.sureBtn;
                if (!(textView7 instanceof TextView)) {
                    textView7 = null;
                }
                if (textView7 != null) {
                    textView7.setTextColor(intValue6);
                }
            }
            Float updateBtnTextSize = uiConfig.getUpdateBtnTextSize();
            if (updateBtnTextSize != null) {
                float floatValue3 = updateBtnTextSize.floatValue();
                TextView textView8 = this.sureBtn;
                if (!(textView8 instanceof TextView)) {
                    textView8 = null;
                }
                if (textView8 != null) {
                    textView8.setTextSize(floatValue3);
                }
            }
            TextView textView9 = this.sureBtn;
            if (!(textView9 instanceof TextView)) {
                textView9 = null;
            }
            if (textView9 != null) {
                textView9.setText(uiConfig.getUpdateBtnText());
            }
        }
        Integer cancelBtnBgColor = uiConfig.getCancelBtnBgColor();
        if (cancelBtnBgColor != null) {
            int intValue7 = cancelBtnBgColor.intValue();
            TextView textView10 = this.cancelBtn;
            if (textView10 != null) {
                textView10.setBackgroundColor(intValue7);
            }
        }
        Integer cancelBtnBgRes = uiConfig.getCancelBtnBgRes();
        if (cancelBtnBgRes != null) {
            int intValue8 = cancelBtnBgRes.intValue();
            TextView textView11 = this.cancelBtn;
            if (textView11 != null) {
                textView11.setBackgroundResource(intValue8);
            }
        }
        if (this.cancelBtn instanceof TextView) {
            Integer cancelBtnTextColor = uiConfig.getCancelBtnTextColor();
            if (cancelBtnTextColor != null) {
                int intValue9 = cancelBtnTextColor.intValue();
                TextView textView12 = this.cancelBtn;
                if (!(textView12 instanceof TextView)) {
                    textView12 = null;
                }
                if (textView12 != null) {
                    textView12.setTextColor(intValue9);
                }
            }
            Float cancelBtnTextSize = uiConfig.getCancelBtnTextSize();
            if (cancelBtnTextSize != null) {
                float floatValue4 = cancelBtnTextSize.floatValue();
                TextView textView13 = this.cancelBtn;
                if (!(textView13 instanceof TextView)) {
                    textView13 = null;
                }
                if (textView13 != null) {
                    textView13.setTextSize(floatValue4);
                }
            }
            TextView textView14 = this.cancelBtn;
            if (!(textView14 instanceof TextView)) {
                textView14 = null;
            }
            if (textView14 != null) {
                textView14.setText(uiConfig.getCancelBtnText());
            }
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private final void initView() {
        this.tvTitle = (TextView) findViewById(R.id.tv_update_title);
        this.tvContent = (TextView) findViewById(R.id.tv_update_content);
        this.cancelBtn = (TextView) findViewById(R.id.btn_update_cancel);
        this.sureBtn = (TextView) findViewById(R.id.btn_update_sure);
        this.ivLogo = (ImageView) findViewById(R.id.iv_update_logo);
        if (getUpdateInfo().getUseDefaultTitle()) {
            UpdateInfo updateInfo = getUpdateInfo();
            String string = GlobalContextProvider.INSTANCE.getGlobalContext().getString(R.string.update_title);
            Intrinsics.checkExpressionValueIsNotNull(string, "GlobalContextProvider.ge…ng(R.string.update_title)");
            updateInfo.setUpdateTitle(string);
        }
        TextView textView = this.tvTitle;
        if (textView != null) {
            textView.setText(getUpdateInfo().getUpdateTitle());
        }
        if (getUpdateInfo().getUseDefaultContent()) {
            UpdateInfo updateInfo2 = getUpdateInfo();
            String string2 = GlobalContextProvider.INSTANCE.getGlobalContext().getString(R.string.update_content);
            Intrinsics.checkExpressionValueIsNotNull(string2, "GlobalContextProvider.ge…(R.string.update_content)");
            updateInfo2.setUpdateContent(string2);
        }
        TextView textView2 = this.tvContent;
        if (textView2 != null) {
            textView2.setText(getUpdateInfo().getUpdateContent());
        }
        UiConfig uiConfig = getUiConfig();
        CharSequence text = getResources().getText(R.string.update_cancel);
        Intrinsics.checkExpressionValueIsNotNull(text, "resources.getText(R.string.update_cancel)");
        uiConfig.setCancelBtnText(text);
        TextView textView3 = this.cancelBtn;
        if (textView3 != null) {
            textView3.setOnClickListener(new View.OnClickListener() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$initView$1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    UpdateConfig updateConfig;
                    updateConfig = UpdateAppActivity.this.getUpdateConfig();
                    boolean force = updateConfig.getForce();
                    if (force) {
                        Utils.INSTANCE.exitApp();
                    }
                    if (!(force)) {
                        UpdateAppActivity.this.finish();
                    }
                }
            });
        }
        UiConfig uiConfig2 = getUiConfig();
        CharSequence text2 = getResources().getText(R.string.update_now);
        Intrinsics.checkExpressionValueIsNotNull(text2, "resources.getText(R.string.update_now)");
        uiConfig2.setUpdateBtnText(text2);
        TextView textView4 = this.sureBtn;
        if (textView4 != null) {
            textView4.setOnClickListener(new View.OnClickListener() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$initView$2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TextView textView5;
                    TextView textView6;
                    UiConfig uiConfig3;
                    if (!(DownloadAppUtils.INSTANCE.isDownloading())) {
                        textView5 = UpdateAppActivity.this.sureBtn;
                        if (textView5 instanceof TextView) {
                            textView6 = UpdateAppActivity.this.sureBtn;
                            if (!(textView6 instanceof TextView)) {
                                textView6 = null;
                            }
                            if (textView6 != null) {
                                uiConfig3 = UpdateAppActivity.this.getUiConfig();
                                textView6.setText(uiConfig3.getUpdateBtnText());
                            }
                        }
                        UpdateAppActivity.this.preDownLoad();
                    }
                }
            });
        }
        TextView textView5 = this.cancelBtn;
        if (textView5 != null) {
            ViewKt.visibleOrGone(textView5, !getUpdateConfig().getForce());
        }
        View findViewById = findViewById(R.id.view_line);
        if (findViewById != null) {
            ViewKt.visibleOrGone(findViewById, !getUpdateConfig().getForce());
        }
        TextView textView6 = this.cancelBtn;
        if (textView6 != null) {
            textView6.setOnTouchListener(new View.OnTouchListener() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$initView$3
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent event) {
                    OnBtnClickListener onCancelBtnClickListener$npUpdate_release;
                    Intrinsics.checkExpressionValueIsNotNull(event, "event");
                    if (event.getAction() == 1 && (onCancelBtnClickListener$npUpdate_release = UpdateAppUtils.INSTANCE.getOnCancelBtnClickListener$npUpdate_release()) != null) {
                        return onCancelBtnClickListener$npUpdate_release.onClick();
                    }
                    return false;
                }
            });
        }
        TextView textView7 = this.sureBtn;
        if (textView7 != null) {
            textView7.setOnTouchListener(new View.OnTouchListener() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$initView$4
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent event) {
                    OnBtnClickListener onUpdateBtnClickListener$npUpdate_release;
                    Intrinsics.checkExpressionValueIsNotNull(event, "event");
                    if (event.getAction() == 1 && (onUpdateBtnClickListener$npUpdate_release = UpdateAppUtils.INSTANCE.getOnUpdateBtnClickListener$npUpdate_release()) != null) {
                        return onUpdateBtnClickListener$npUpdate_release.onClick();
                    }
                    return false;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void preDownLoad() {
        boolean z = Build.VERSION.SDK_INT < 23;
        if (z) {
            download();
        }
        if (!(z)) {
            boolean z2 = ContextCompat.checkSelfPermission(this, permission) == 0;
            if (z2) {
                download();
            }
            if (!(z2)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, 1001);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"SetTextI18n"})
    public final void realDownload() {
        if ((getUpdateConfig().getForce() || getUpdateConfig().getAlwaysShowDownLoadDialog()) && (this.sureBtn instanceof TextView)) {
            DownloadAppUtils.INSTANCE.setOnError(new Function0<Unit>() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$realDownload$1
                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    super(0);
                }

                @Override // kotlin.jvm.functions.Function0
                public /* bridge */ /* synthetic */ Unit invoke() {
                    invoke2();
                    return Unit.INSTANCE;
                }

                /* renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2() {
                    TextView textView;
                    UiConfig uiConfig;
                    textView = UpdateAppActivity.this.sureBtn;
                    if (!(textView instanceof TextView)) {
                        textView = null;
                    }
                    if (textView != null) {
                        uiConfig = UpdateAppActivity.this.getUiConfig();
                        textView.setText(uiConfig.getDownloadFailText());
                    }
                }
            });
            DownloadAppUtils.INSTANCE.setOnReDownload(new Function0<Unit>() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$realDownload$2
                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    super(0);
                }

                @Override // kotlin.jvm.functions.Function0
                public /* bridge */ /* synthetic */ Unit invoke() {
                    invoke2();
                    return Unit.INSTANCE;
                }

                /* renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2() {
                    TextView textView;
                    UiConfig uiConfig;
                    textView = UpdateAppActivity.this.sureBtn;
                    if (!(textView instanceof TextView)) {
                        textView = null;
                    }
                    if (textView != null) {
                        uiConfig = UpdateAppActivity.this.getUiConfig();
                        textView.setText(uiConfig.getUpdateBtnText());
                    }
                }
            });
            DownloadAppUtils.INSTANCE.setOnProgress(new Function1<Integer, Unit>() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$realDownload$3
                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Unit invoke(Integer num) {
                    invoke(num.intValue());
                    return Unit.INSTANCE;
                }

                public final void invoke(int i) {
                    TextView textView;
                    UiConfig uiConfig;
                    TextView textView2;
                    boolean z = i == 100;
                    if (z) {
                        textView2 = UpdateAppActivity.this.sureBtn;
                        if (!(textView2 instanceof TextView)) {
                            textView2 = null;
                        }
                        if (textView2 != null) {
                            textView2.setText(UpdateAppActivity.this.getString(R.string.install));
                        }
                    }
                    if (!(z)) {
                        textView = UpdateAppActivity.this.sureBtn;
                        if (!(textView instanceof TextView)) {
                            textView = null;
                        }
                        if (textView != null) {
                            StringBuilder sb = new StringBuilder();
                            uiConfig = UpdateAppActivity.this.getUiConfig();
                            sb.append(uiConfig.getDownloadingBtnText());
                            sb.append(i);
                            sb.append('%');
                            textView.setText(sb.toString());
                        }
                    }
                }
            });
        }
        DownloadAppUtils.INSTANCE.download();
        boolean z = false;
        if (getUpdateConfig().getShowDownloadingToast()) {
            Toast.makeText(this, getUiConfig().getDownloadingToastText(), 0).show();
        }
        if (!getUpdateConfig().getForce() && !getUpdateConfig().getAlwaysShowDownLoadDialog()) {
            z = true;
        }
        if (z) {
            finish();
        }
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_out);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        String uiType = getUiConfig().getUiType();
        int hashCode = uiType.hashCode();
        if (hashCode == -1848957518) {
            if (uiType.equals(UiType.SIMPLE)) {
                i = R.layout.view_update_dialog_simple;
            }
            i = R.layout.view_update_dialog_simple;
        } else if (hashCode != -131730877) {
            if (hashCode == 1999208305 && uiType.equals(UiType.CUSTOM)) {
                Integer customLayoutId = getUiConfig().getCustomLayoutId();
                i = customLayoutId != null ? customLayoutId.intValue() : R.layout.view_update_dialog_simple;
            }
            i = R.layout.view_update_dialog_simple;
        } else {
            if (uiType.equals(UiType.PLENTIFUL)) {
                i = R.layout.view_update_dialog_plentiful;
            }
            i = R.layout.view_update_dialog_simple;
        }
        setContentView(i);
        refreshUI();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        Intrinsics.checkParameterIsNotNull(permissions, "permissions");
        Intrinsics.checkParameterIsNotNull(grantResults, "grantResults");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 1001) {
            return;
        }
        boolean z = grantResults[0] == 0;
        if (z) {
            download();
        }
        if (!(z)) {
            UpdateAppActivity updateAppActivity = this;
            if (!(ActivityCompat.shouldShowRequestPermissionRationale(updateAppActivity, permission))) {
                AlertDialogUtil alertDialogUtil = AlertDialogUtil.INSTANCE;
                String string = getString(R.string.no_storage_permission);
                Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.no_storage_permission)");
                AlertDialogUtil.show$default(alertDialogUtil, updateAppActivity, string, null, new Function0<Unit>() { // from class: npUpdate.nopointer.ui.UpdateAppActivity$onRequestPermissionsResult$$inlined$no$lambda$1
                    /* JADX INFO: Access modifiers changed from: package-private */
                    {
                        super(0);
                    }

                    @Override // kotlin.jvm.functions.Function0
                    public /* bridge */ /* synthetic */ Unit invoke() {
                        invoke2();
                        return Unit.INSTANCE;
                    }

                    /* renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2() {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.parse("package:" + UpdateAppActivity.this.getPackageName()));
                        UpdateAppActivity.this.startActivity(intent);
                    }
                }, false, null, null, null, 244, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    public final void refreshUI() {
        OnInitUiListener onInitUiListener$npUpdate_release = UpdateAppUtils.INSTANCE.getOnInitUiListener$npUpdate_release();
        if (onInitUiListener$npUpdate_release != null) {
            Window window = getWindow();
            Intrinsics.checkExpressionValueIsNotNull(window, "window");
            onInitUiListener$npUpdate_release.onInitUpdateUi(window.getDecorView().findViewById(android.R.id.content), getUpdateConfig(), getUiConfig());
        }
        initView();
        initUi();
    }
}
