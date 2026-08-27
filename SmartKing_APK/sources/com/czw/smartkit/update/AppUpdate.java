package com.czw.smartkit.update;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import com.czw.smartkit.netModule.NetManager;
import com.czw.smartkit.netModule.entity.AppVersionEntity;
import com.czw.smartkit.util.ToastHelper;
import com.czw.utils.LogUtil;
import npUpdate.nopointer.constacne.UiType;
import npUpdate.nopointer.listener.Md5CheckResultListener;
import npUpdate.nopointer.listener.UpdateDownloadListener;
import npUpdate.nopointer.model.UiConfig;
import npUpdate.nopointer.model.UpdateConfig;
import npUpdate.nopointer.update.UpdateAppUtils;
import ycnet.runchinaup.core.ycimpl.data.YCRespListData;
import ycnet.runchinaup.core.ycimpl.response.YCResponseListener;

/* loaded from: classes.dex */
public class AppUpdate {
    private static AppUpdate instance = new AppUpdate();
    private String updateContent;
    private String updateTitle;

    /* loaded from: classes.dex */
    public interface CheckCallback {
        void onCheckFinish();
    }

    private AppUpdate() {
        this.updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索";
        this.updateTitle = "发现新版本V2.0.0";
        UpdateAppUtils.getInstance().deleteInstalledApk();
        this.updateTitle = "";
        this.updateContent = getText(R.string.update_content);
    }

    public static boolean checkApkExist(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(str, 8192);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static AppUpdate getInstance() {
        return instance;
    }

    public static String getText(int i) {
        return MainApplication.getApp().getResources().getString(i);
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isANewVersionApp(AppVersionEntity appVersionEntity) {
        if (appVersionEntity == null || TextUtils.isEmpty(appVersionEntity.getDownload_url()) || Long.valueOf(appVersionEntity.getSize()).longValue() < 1) {
            return false;
        }
        try {
            return Integer.valueOf(appVersionEntity.getVersion().replace(".", "")).intValue() > Integer.valueOf(getVersionName(MainApplication.getApp()).replace(".", "")).intValue();
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isGoogleRom(Context context) {
        return checkApkExist(context, "com.google.android.music") && checkApkExist(context, "com.google.android.gm") && checkApkExist(context, "com.android.vending");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNewVersion(AppVersionEntity appVersionEntity) {
        UpdateConfig updateConfig = new UpdateConfig();
        updateConfig.setCheckWifi(true);
        updateConfig.setNeedCheckMd5(false);
        updateConfig.setApkSavePath(Environment.getExternalStorageDirectory() + "/SmartKing/update");
        updateConfig.setApkSaveName("SmartKing" + appVersionEntity.getVersion());
        updateConfig.setNotifyImgRes(R.mipmap.ic_launcher_round);
        UiConfig uiConfig = new UiConfig();
        uiConfig.setUpdateLogoImgRes(Integer.valueOf(R.drawable.update_icon));
        uiConfig.setUiType(UiType.PLENTIFUL);
        UpdateAppUtils.getInstance().apkUrl(appVersionEntity.getDownload_url()).updateTitle(this.updateTitle + "" + appVersionEntity.getVersion()).updateContent(this.updateContent).uiConfig(uiConfig).updateConfig(updateConfig).setMd5CheckResultListener(new Md5CheckResultListener() { // from class: com.czw.smartkit.update.AppUpdate.2
            @Override // npUpdate.nopointer.listener.Md5CheckResultListener
            public void onResult(boolean z) {
                LogUtil.e("onResult===>" + z);
            }
        }).setUpdateDownloadListener(new UpdateDownloadListener() { // from class: com.czw.smartkit.update.AppUpdate.1
            @Override // npUpdate.nopointer.listener.UpdateDownloadListener
            public void onDownload(int i) {
            }

            @Override // npUpdate.nopointer.listener.UpdateDownloadListener
            public void onError(Throwable th) {
                th.printStackTrace();
            }

            @Override // npUpdate.nopointer.listener.UpdateDownloadListener
            public void onFinish() {
            }

            @Override // npUpdate.nopointer.listener.UpdateDownloadListener
            public void onStart() {
            }
        }).update();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toastLast() {
        ToastHelper.getToastHelper().show(R.string.is_last_version);
    }

    public void checkUpdate(final boolean z, final CheckCallback checkCallback) {
        this.updateContent = getText(R.string.update_content);
        boolean isGoogleRom = isGoogleRom(MainApplication.getApp());
        LogUtil.e("isGoogleRom : " + isGoogleRom);
        if (!isGoogleRom) {
            NetManager.getNetManager().getLastAppVersion(new YCResponseListener<YCRespListData<AppVersionEntity>>() { // from class: com.czw.smartkit.update.AppUpdate.3
                @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                public void onError(int i, String str) {
                    super.onError(i, str);
                    if (checkCallback != null) {
                        checkCallback.onCheckFinish();
                    }
                }

                @Override // ycnet.runchinaup.core.abs.IResponseListener
                public void onSuccess(YCRespListData<AppVersionEntity> yCRespListData) {
                    if (checkCallback != null) {
                        checkCallback.onCheckFinish();
                    }
                    if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() <= 0 || TextUtils.isEmpty(yCRespListData.getData().get(0).getDownload_url())) {
                        if (z) {
                            AppUpdate.this.toastLast();
                        }
                    } else if (AppUpdate.isANewVersionApp(yCRespListData.getData().get(0))) {
                        AppUpdate.this.showNewVersion(yCRespListData.getData().get(0));
                    } else if (z) {
                        AppUpdate.this.toastLast();
                    }
                }
            });
            return;
        }
        if (checkCallback != null) {
            checkCallback.onCheckFinish();
        }
        if (z) {
            toastLast();
        }
    }
}
