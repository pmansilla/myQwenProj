package npPermission.nopointer.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import npPermission.nopointer.core.callback.PermissionCallback;
import npPermission.nopointer.log.NpPerLog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class AbsPermsRequester {
    private RequestPermissionInfo permissionInfo;

    public AbsPermsRequester(RequestPermissionInfo requestPermissionInfo) {
        this.permissionInfo = null;
        this.permissionInfo = requestPermissionInfo;
    }

    protected static void checkCallingObjectSuitability(Object obj) {
        boolean z = obj instanceof Activity;
        boolean z2 = obj instanceof Fragment;
        boolean z3 = obj instanceof android.app.Fragment;
        boolean z4 = Build.VERSION.SDK_INT >= 23;
        if (z2 || z) {
            return;
        }
        if (z3 && z4) {
            return;
        }
        if (!z3) {
            throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
        }
        throw new IllegalArgumentException("Target SDK needs to be greater than 23 if caller is android.app.Fragment");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @TargetApi(23)
    public static void executePermissionsRequest(Object obj, String[] strArr, int i) {
        checkCallingObjectSuitability(obj);
        if (obj instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) obj, strArr, i);
        } else if (obj instanceof Fragment) {
            ((Fragment) obj).requestPermissions(strArr, i);
        } else if (obj instanceof android.app.Fragment) {
            ((android.app.Fragment) obj).requestPermissions(strArr, i);
        }
    }

    public static boolean hasPermissions(Context context, String... strArr) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (String str : strArr) {
            if (!(ContextCompat.checkSelfPermission(context, str) == 0)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermissions(Fragment fragment, String... strArr) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (String str : strArr) {
            if (!(ContextCompat.checkSelfPermission(fragment.getContext(), str) == 0)) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(23)
    protected static boolean shouldShowRequestPermissionRationale(Object obj, String str) {
        if (obj instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) obj, str);
        }
        if (obj instanceof Fragment) {
            return ((Fragment) obj).shouldShowRequestPermissionRationale(str);
        }
        if (obj instanceof android.app.Fragment) {
            return ((android.app.Fragment) obj).shouldShowRequestPermissionRationale(str);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @TargetApi(11)
    public static void startAppSettingsScreen(Object obj, Intent intent, int i) {
        if (obj instanceof Activity) {
            ((Activity) obj).startActivityForResult(intent, i);
        } else if (obj instanceof Fragment) {
            ((Fragment) obj).startActivityForResult(intent, i);
        } else if (obj instanceof android.app.Fragment) {
            ((android.app.Fragment) obj).startActivityForResult(intent, i);
        }
    }

    protected abstract void cfgPermissionInfoDialog(Activity activity, RequestPermissionInfo requestPermissionInfo);

    protected abstract void cfgPermissionInfoDialog(android.app.Fragment fragment, RequestPermissionInfo requestPermissionInfo);

    protected abstract void cfgPermissionInfoDialog(Fragment fragment, RequestPermissionInfo requestPermissionInfo);

    protected abstract void cfgPermissionInfoDialogForNeverAsk(Activity activity, RequestPermissionInfo requestPermissionInfo, List<String> list);

    protected abstract void cfgPermissionInfoDialogForNeverAsk(android.app.Fragment fragment, RequestPermissionInfo requestPermissionInfo, List<String> list);

    protected abstract void cfgPermissionInfoDialogForNeverAsk(Fragment fragment, RequestPermissionInfo requestPermissionInfo, List<String> list);

    public boolean checkDeniedPermissionsNeverAskAgain(Object obj, List<String> list) {
        if (obj == null) {
            return true;
        }
        for (String str : list) {
            boolean shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale(obj, str);
            NpPerLog.log(str + "///" + shouldShowRequestPermissionRationale);
            if (!shouldShowRequestPermissionRationale) {
                if (obj instanceof Activity) {
                    Activity activity = (Activity) obj;
                    if (!hasPermissions(activity, str)) {
                        cfgPermissionInfoDialogForNeverAsk(activity, this.permissionInfo, list);
                        return true;
                    }
                } else if (obj instanceof Fragment) {
                    Fragment fragment = (Fragment) obj;
                    if (!hasPermissions(fragment.getActivity(), str)) {
                        cfgPermissionInfoDialogForNeverAsk(fragment, this.permissionInfo, list);
                        return true;
                    }
                } else if (obj instanceof android.app.Fragment) {
                    android.app.Fragment fragment2 = (android.app.Fragment) obj;
                    if (!hasPermissions(fragment2.getActivity(), str)) {
                        cfgPermissionInfoDialogForNeverAsk(fragment2, this.permissionInfo, list);
                        return true;
                    }
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    public RequestPermissionInfo getPermissionInfo() {
        return this.permissionInfo;
    }

    public void onRequestPermissionsResult(Activity activity, int i, String[] strArr, int[] iArr, PermissionCallback permissionCallback) {
        checkCallingObjectSuitability(activity);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (iArr[i2] == 0) {
                arrayList.add(str);
            } else {
                arrayList2.add(str);
            }
        }
        if (!arrayList.isEmpty() && permissionCallback != null) {
            permissionCallback.onPermissionsGranted(i, arrayList);
        }
        if (!arrayList2.isEmpty() && permissionCallback != null) {
            permissionCallback.onPermissionsDenied(i, arrayList2);
        }
        if (arrayList.isEmpty() || !arrayList2.isEmpty() || permissionCallback == null) {
            return;
        }
        permissionCallback.onGetAllPermission();
    }

    public void onRequestPermissionsResult(Fragment fragment, int i, String[] strArr, int[] iArr, PermissionCallback permissionCallback) {
        checkCallingObjectSuitability(fragment);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (iArr[i2] == 0) {
                arrayList.add(str);
            } else {
                arrayList2.add(str);
            }
        }
        if (!arrayList.isEmpty() && permissionCallback != null) {
            permissionCallback.onPermissionsGranted(i, arrayList);
        }
        if (!arrayList2.isEmpty() && permissionCallback != null) {
            permissionCallback.onPermissionsDenied(i, arrayList2);
        }
        if (arrayList.isEmpty() || !arrayList2.isEmpty() || permissionCallback == null) {
            return;
        }
        permissionCallback.onGetAllPermission();
    }

    public <T extends Activity> void requestPermission(T t, PermissionCallback permissionCallback) {
        if (this.permissionInfo == null || this.permissionInfo.getPermissionArr() == null) {
            NpPerLog.log("权限请求的内容为空！！！");
        } else if (!hasPermissions(t, this.permissionInfo.getPermissionArr())) {
            requestPermissions(t, this.permissionInfo);
        } else if (permissionCallback != null) {
            permissionCallback.onGetAllPermission();
        }
    }

    public <T extends Fragment> void requestPermission(T t, PermissionCallback permissionCallback) {
        NpPerLog.log("permissionInfo==>" + this.permissionInfo.toString());
        if (this.permissionInfo == null || this.permissionInfo.getPermissionArr() == null) {
            NpPerLog.log("权限请求的内容为空！！！");
        } else if (!hasPermissions(t, this.permissionInfo.getPermissionArr())) {
            requestPermissions(t, this.permissionInfo);
        } else if (permissionCallback != null) {
            permissionCallback.onGetAllPermission();
        }
    }

    protected void requestPermissions(Object obj, RequestPermissionInfo requestPermissionInfo) {
        checkCallingObjectSuitability(obj);
        boolean z = false;
        for (String str : requestPermissionInfo.getPermissionArr()) {
            z = z || shouldShowRequestPermissionRationale(obj, str);
        }
        if (!z) {
            executePermissionsRequest(obj, requestPermissionInfo.getPermissionArr(), requestPermissionInfo.getRequestCode());
            return;
        }
        if (obj instanceof Activity) {
            cfgPermissionInfoDialog((Activity) obj, requestPermissionInfo);
        } else if (obj instanceof Fragment) {
            cfgPermissionInfoDialog((Fragment) obj, requestPermissionInfo);
        } else if (obj instanceof android.app.Fragment) {
            cfgPermissionInfoDialog((android.app.Fragment) obj, requestPermissionInfo);
        }
    }

    public void setPermissionInfo(RequestPermissionInfo requestPermissionInfo) {
        this.permissionInfo = requestPermissionInfo;
    }
}
