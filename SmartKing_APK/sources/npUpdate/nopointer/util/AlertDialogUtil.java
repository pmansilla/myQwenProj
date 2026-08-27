package npUpdate.nopointer.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import npUpdate.nopointer.R;
import org.jetbrains.annotations.NotNull;

/* compiled from: AlertDialogUtil.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J^\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\n2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0010\u001a\u00020\b¨\u0006\u0011"}, d2 = {"LnpUpdate/nopointer/util/AlertDialogUtil;", "", "()V", "show", "", "activity", "Landroid/app/Activity;", "message", "", "onCancelClick", "Lkotlin/Function0;", "onSureClick", "cancelable", "", "title", "cancelText", "sureText", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class AlertDialogUtil {
    public static final AlertDialogUtil INSTANCE = new AlertDialogUtil();

    private AlertDialogUtil() {
    }

    public static /* synthetic */ void show$default(AlertDialogUtil alertDialogUtil, Activity activity, String str, Function0 function0, Function0 function02, boolean z, String str2, String str3, String str4, int i, Object obj) {
        String str5;
        String str6;
        String str7;
        Function0 function03 = (i & 4) != 0 ? new Function0<Unit>() { // from class: npUpdate.nopointer.util.AlertDialogUtil$show$1
            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
            }
        } : function0;
        Function0 function04 = (i & 8) != 0 ? new Function0<Unit>() { // from class: npUpdate.nopointer.util.AlertDialogUtil$show$2
            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
            }
        } : function02;
        boolean z2 = (i & 16) != 0 ? false : z;
        if ((i & 32) != 0) {
            String string = GlobalContextProvider.INSTANCE.getGlobalContext().getString(R.string.notice);
            Intrinsics.checkExpressionValueIsNotNull(string, "GlobalContextProvider.ge…etString(R.string.notice)");
            str5 = string;
        } else {
            str5 = str2;
        }
        if ((i & 64) != 0) {
            String string2 = GlobalContextProvider.INSTANCE.getGlobalContext().getString(R.string.cancel);
            Intrinsics.checkExpressionValueIsNotNull(string2, "GlobalContextProvider.ge…etString(R.string.cancel)");
            str6 = string2;
        } else {
            str6 = str3;
        }
        if ((i & 128) != 0) {
            String string3 = GlobalContextProvider.INSTANCE.getGlobalContext().getString(R.string.sure);
            Intrinsics.checkExpressionValueIsNotNull(string3, "GlobalContextProvider.ge….getString(R.string.sure)");
            str7 = string3;
        } else {
            str7 = str4;
        }
        alertDialogUtil.show(activity, str, function03, function04, z2, str5, str6, str7);
    }

    public final void show(@NotNull Activity activity, @NotNull String message, @NotNull final Function0<Unit> onCancelClick, @NotNull final Function0<Unit> onSureClick, boolean cancelable, @NotNull String title, @NotNull String cancelText, @NotNull String sureText) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(onCancelClick, "onCancelClick");
        Intrinsics.checkParameterIsNotNull(onSureClick, "onSureClick");
        Intrinsics.checkParameterIsNotNull(title, "title");
        Intrinsics.checkParameterIsNotNull(cancelText, "cancelText");
        Intrinsics.checkParameterIsNotNull(sureText, "sureText");
        new AlertDialog.Builder(activity, R.style.AlertDialog).setTitle(title).setMessage(message).setPositiveButton(sureText, new DialogInterface.OnClickListener() { // from class: npUpdate.nopointer.util.AlertDialogUtil$show$3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                Function0.this.invoke();
            }
        }).setNegativeButton(cancelText, new DialogInterface.OnClickListener() { // from class: npUpdate.nopointer.util.AlertDialogUtil$show$4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                Function0.this.invoke();
            }
        }).setCancelable(cancelable).create().show();
    }
}
