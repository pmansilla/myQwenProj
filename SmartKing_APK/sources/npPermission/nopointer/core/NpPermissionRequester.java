package npPermission.nopointer.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import java.util.List;
import npPermission.nopointer.core.callback.PermissionCallback;
import npPermission.nopointer.log.NpPerLog;

/* loaded from: classes2.dex */
public class NpPermissionRequester extends AbsPermsRequester {
    public NpPermissionRequester(RequestPermissionInfo requestPermissionInfo) {
        super(requestPermissionInfo);
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    protected void cfgPermissionInfoDialog(final Activity activity, final RequestPermissionInfo requestPermissionInfo) {
        AlertDialog create = new AlertDialog.Builder(activity).setPositiveButton(requestPermissionInfo.getPermissionSureText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AbsPermsRequester.executePermissionsRequest(activity, requestPermissionInfo.getPermissionArr(), requestPermissionInfo.getRequestCode());
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onSure(false);
            }
        }).setNegativeButton(requestPermissionInfo.getPermissionCancelText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onCancel(false);
            }
        }).create();
        NpPerLog.log("fuck,firstDialog", create.toString());
        NpPerLog.log("fuck,permissionInfo", requestPermissionInfo.toString());
        create.setCancelable(false);
        create.setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(requestPermissionInfo.getPermissionTitle())) {
            create.setTitle(requestPermissionInfo.getPermissionTitle());
        }
        if (!TextUtils.isEmpty(requestPermissionInfo.getPermissionMessage())) {
            create.setMessage(requestPermissionInfo.getPermissionMessage());
        }
        create.show();
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    protected void cfgPermissionInfoDialog(final Fragment fragment, final RequestPermissionInfo requestPermissionInfo) {
        AlertDialog create = new AlertDialog.Builder(fragment.getActivity()).setPositiveButton(requestPermissionInfo.getPermissionSureText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AbsPermsRequester.executePermissionsRequest(fragment, requestPermissionInfo.getPermissionArr(), requestPermissionInfo.getRequestCode());
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onSure(false);
            }
        }).setNegativeButton(requestPermissionInfo.getPermissionCancelText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onCancel(false);
            }
        }).create();
        if (!TextUtils.isEmpty(requestPermissionInfo.getPermissionTitle())) {
            create.setTitle(requestPermissionInfo.getPermissionTitle());
        }
        if (!TextUtils.isEmpty(requestPermissionInfo.getPermissionMessage())) {
            create.setMessage(requestPermissionInfo.getPermissionMessage());
        }
        create.setCancelable(false);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    protected void cfgPermissionInfoDialog(final android.support.v4.app.Fragment fragment, final RequestPermissionInfo requestPermissionInfo) {
        AlertDialog create = new AlertDialog.Builder(fragment.getActivity()).setPositiveButton(requestPermissionInfo.getPermissionSureText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AbsPermsRequester.executePermissionsRequest(fragment, requestPermissionInfo.getPermissionArr(), requestPermissionInfo.getRequestCode());
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onSure(false);
            }
        }).setNegativeButton(requestPermissionInfo.getPermissionCancelText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onCancel(false);
            }
        }).create();
        if (!TextUtils.isEmpty(requestPermissionInfo.getPermissionTitle())) {
            create.setTitle(requestPermissionInfo.getPermissionTitle());
        }
        if (!TextUtils.isEmpty(requestPermissionInfo.getPermissionMessage())) {
            create.setMessage(requestPermissionInfo.getPermissionMessage());
        }
        create.setCancelable(false);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    protected void cfgPermissionInfoDialogForNeverAsk(final Activity activity, final RequestPermissionInfo requestPermissionInfo, List<String> list) {
        AlertDialog create = new AlertDialog.Builder(activity).setPositiveButton(requestPermissionInfo.getAgainPermissionSureText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                AbsPermsRequester.startAppSettingsScreen(activity, intent, requestPermissionInfo.getRequestCode());
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onSure(true);
            }
        }).setNegativeButton(requestPermissionInfo.getAgainPermissionCancelText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onCancel(true);
            }
        }).create();
        create.setCancelable(false);
        create.setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(requestPermissionInfo.getAgainPermissionTitle())) {
            create.setTitle(requestPermissionInfo.getAgainPermissionTitle());
        }
        if (!TextUtils.isEmpty(requestPermissionInfo.getAgainPermissionMessage())) {
            create.setMessage(requestPermissionInfo.getAgainPermissionMessage());
        }
        create.show();
        NpPerLog.log("显示再次询问对话框");
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    protected void cfgPermissionInfoDialogForNeverAsk(final Fragment fragment, final RequestPermissionInfo requestPermissionInfo, List<String> list) {
        AlertDialog create = new AlertDialog.Builder(fragment.getActivity()).setPositiveButton(requestPermissionInfo.getAgainPermissionSureText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", fragment.getActivity().getPackageName(), null));
                AbsPermsRequester.startAppSettingsScreen(fragment, intent, requestPermissionInfo.getRequestCode());
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onSure(true);
            }
        }).setNegativeButton(requestPermissionInfo.getAgainPermissionCancelText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onCancel(true);
            }
        }).create();
        if (!TextUtils.isEmpty(requestPermissionInfo.getAgainPermissionTitle())) {
            create.setTitle(requestPermissionInfo.getAgainPermissionTitle());
        }
        if (!TextUtils.isEmpty(requestPermissionInfo.getAgainPermissionMessage())) {
            create.setMessage(requestPermissionInfo.getAgainPermissionMessage());
        }
        create.setCancelable(false);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    protected void cfgPermissionInfoDialogForNeverAsk(final android.support.v4.app.Fragment fragment, final RequestPermissionInfo requestPermissionInfo, List<String> list) {
        AlertDialog create = new AlertDialog.Builder(fragment.getActivity()).setPositiveButton(requestPermissionInfo.getAgainPermissionSureText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", fragment.getActivity().getPackageName(), null));
                AbsPermsRequester.startAppSettingsScreen(fragment, intent, requestPermissionInfo.getRequestCode());
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onSure(true);
            }
        }).setNegativeButton(requestPermissionInfo.getAgainPermissionCancelText(), new DialogInterface.OnClickListener() { // from class: npPermission.nopointer.core.NpPermissionRequester.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (requestPermissionInfo == null || requestPermissionInfo.getPermissionDialogCallback() == null) {
                    return;
                }
                requestPermissionInfo.getPermissionDialogCallback().onCancel(true);
            }
        }).create();
        if (!TextUtils.isEmpty(requestPermissionInfo.getAgainPermissionTitle())) {
            create.setTitle(requestPermissionInfo.getAgainPermissionTitle());
        }
        if (!TextUtils.isEmpty(requestPermissionInfo.getAgainPermissionMessage())) {
            create.setMessage(requestPermissionInfo.getAgainPermissionMessage());
        }
        create.setCancelable(false);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    public /* bridge */ /* synthetic */ boolean checkDeniedPermissionsNeverAskAgain(Object obj, List list) {
        return super.checkDeniedPermissionsNeverAskAgain(obj, list);
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    public /* bridge */ /* synthetic */ RequestPermissionInfo getPermissionInfo() {
        return super.getPermissionInfo();
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    public /* bridge */ /* synthetic */ void onRequestPermissionsResult(Activity activity, int i, String[] strArr, int[] iArr, PermissionCallback permissionCallback) {
        super.onRequestPermissionsResult(activity, i, strArr, iArr, permissionCallback);
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    public /* bridge */ /* synthetic */ void onRequestPermissionsResult(android.support.v4.app.Fragment fragment, int i, String[] strArr, int[] iArr, PermissionCallback permissionCallback) {
        super.onRequestPermissionsResult(fragment, i, strArr, iArr, permissionCallback);
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    public /* bridge */ /* synthetic */ void requestPermission(Activity activity, PermissionCallback permissionCallback) {
        super.requestPermission((NpPermissionRequester) activity, permissionCallback);
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    public /* bridge */ /* synthetic */ void requestPermission(android.support.v4.app.Fragment fragment, PermissionCallback permissionCallback) {
        super.requestPermission((NpPermissionRequester) fragment, permissionCallback);
    }

    @Override // npPermission.nopointer.core.AbsPermsRequester
    public /* bridge */ /* synthetic */ void setPermissionInfo(RequestPermissionInfo requestPermissionInfo) {
        super.setPermissionInfo(requestPermissionInfo);
    }
}
