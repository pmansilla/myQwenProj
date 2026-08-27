package npPermission.nopointer.core;

import java.io.Serializable;
import java.util.Arrays;
import npPermission.nopointer.core.callback.PermissionDialogCallback;

/* loaded from: classes2.dex */
public class RequestPermissionInfo implements Serializable {
    private PermissionDialogCallback permissionDialogCallback;
    private String permissionTitle = "";
    private String permissionMessage = "";
    private String[] permissionArr = null;
    private String permissionCancelText = "";
    private String permissionSureText = "";
    private int requestCode = 666;
    private String againPermissionTitle = "";
    private String againPermissionMessage = "";
    private String againPermissionCancelText = "";
    private String againPermissionSureText = "";

    public String getAgainPermissionCancelText() {
        return this.againPermissionCancelText;
    }

    public String getAgainPermissionMessage() {
        return this.againPermissionMessage;
    }

    public String getAgainPermissionSureText() {
        return this.againPermissionSureText;
    }

    public String getAgainPermissionTitle() {
        return this.againPermissionTitle;
    }

    public String[] getPermissionArr() {
        return this.permissionArr;
    }

    public String getPermissionCancelText() {
        return this.permissionCancelText;
    }

    public PermissionDialogCallback getPermissionDialogCallback() {
        return this.permissionDialogCallback;
    }

    public String getPermissionMessage() {
        return this.permissionMessage;
    }

    public String getPermissionSureText() {
        return this.permissionSureText;
    }

    public String getPermissionTitle() {
        return this.permissionTitle;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public void setAgainPermissionCancelText(String str) {
        this.againPermissionCancelText = str;
    }

    public void setAgainPermissionMessage(String str) {
        this.againPermissionMessage = str;
    }

    public void setAgainPermissionSureText(String str) {
        this.againPermissionSureText = str;
    }

    public void setAgainPermissionTitle(String str) {
        this.againPermissionTitle = str;
    }

    public void setPermissionArr(String[] strArr) {
        this.permissionArr = strArr;
    }

    public void setPermissionCancelText(String str) {
        this.permissionCancelText = str;
    }

    public void setPermissionDialogCallback(PermissionDialogCallback permissionDialogCallback) {
        this.permissionDialogCallback = permissionDialogCallback;
    }

    public void setPermissionMessage(String str) {
        this.permissionMessage = str;
    }

    public void setPermissionSureText(String str) {
        this.permissionSureText = str;
    }

    public void setPermissionTitle(String str) {
        this.permissionTitle = str;
    }

    public void setRequestCode(int i) {
        this.requestCode = i;
    }

    public String toString() {
        return "RequestPermissionInfo{permissionTitle='" + this.permissionTitle + "', permissionMessage='" + this.permissionMessage + "', permissionArr=" + Arrays.toString(this.permissionArr) + ", permissionCancelText='" + this.permissionCancelText + "', permissionSureText='" + this.permissionSureText + "', requestCode=" + this.requestCode + ", againPermissionTitle='" + this.againPermissionTitle + "', againPermissionMessage='" + this.againPermissionMessage + "', againPermissionCancelText='" + this.againPermissionCancelText + "', againPermissionSureText='" + this.againPermissionSureText + "'}";
    }
}
