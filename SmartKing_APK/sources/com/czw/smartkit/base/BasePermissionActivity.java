package com.czw.smartkit.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import npPermission.nopointer.core.NpPermissionRequester;
import npPermission.nopointer.core.RequestPermissionInfo;
import npPermission.nopointer.core.callback.PermissionCallback;

/* loaded from: classes.dex */
public abstract class BasePermissionActivity extends BaseActivity implements PermissionCallback {
    private NpPermissionRequester npPermissionRequester = null;

    public void onGetAllPermission() {
        LogUtil.e("所有权限得到");
    }

    @Override // npPermission.nopointer.core.callback.PermissionCallback
    public void onPermissionsDenied(int i, List<String> list) {
        RequestPermissionInfo permissionInfo;
        LogUtil.e("部分权限拒绝" + new Gson().toJson(list));
        if (this.npPermissionRequester == null || (permissionInfo = this.npPermissionRequester.getPermissionInfo()) == null || TextUtils.isEmpty(permissionInfo.getAgainPermissionMessage())) {
            return;
        }
        this.npPermissionRequester.checkDeniedPermissionsNeverAskAgain(this, Arrays.asList(permissionInfo.getPermissionArr()));
    }

    @Override // npPermission.nopointer.core.callback.PermissionCallback
    public void onPermissionsGranted(int i, List<String> list) {
        LogUtil.e("部分权限得到" + new Gson().toJson(list));
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (this.npPermissionRequester != null) {
            this.npPermissionRequester.onRequestPermissionsResult(this, i, strArr, iArr, this);
        }
    }

    public void requestPermission(RequestPermissionInfo requestPermissionInfo) {
        if (requestPermissionInfo == null) {
            return;
        }
        if (this.npPermissionRequester == null) {
            this.npPermissionRequester = new NpPermissionRequester(requestPermissionInfo);
        } else {
            this.npPermissionRequester.setPermissionInfo(requestPermissionInfo);
        }
        LogUtil.e("debug请求权限=====>" + requestPermissionInfo);
        this.npPermissionRequester.requestPermission(this, this);
    }
}
