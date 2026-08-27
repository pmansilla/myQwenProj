package npPermission.nopointer.core.callback;

import java.util.List;

/* loaded from: classes2.dex */
public interface PermissionCallback {
    void onGetAllPermission();

    void onPermissionsDenied(int i, List<String> list);

    void onPermissionsGranted(int i, List<String> list);
}
