package com.mob.wrappers;

import android.os.Handler;
import android.os.Message;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.mob.MobSDK;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.UIHandler;
import java.lang.reflect.Field;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class ShareSDKWrapper extends SDKWrapper implements PublicMemberKeeper {
    private static int state;

    /* loaded from: classes2.dex */
    public interface CallbackWrapper {
        void onCancel(String str, int i);

        void onComplete(String str, int i, HashMap<String, Object> hashMap);

        void onError(String str, int i, Throwable th);
    }

    public static String getDbData(String str) {
        Platform platform;
        if (!isAvailable() || (platform = ShareSDK.getPlatform(str)) == null) {
            return null;
        }
        return platform.getDb().exportData();
    }

    public static String[] getPlatformList() {
        Platform[] platformList;
        if (!isAvailable() || (platformList = ShareSDK.getPlatformList()) == null) {
            return null;
        }
        String[] strArr = new String[platformList.length];
        for (int i = 0; i < platformList.length; i++) {
            strArr[i] = platformList[i].getName();
        }
        return strArr;
    }

    public static int getPlatformToId(String str) {
        if (isAvailable()) {
            return ShareSDK.platformNameToId(str);
        }
        return 0;
    }

    public static String getToken(String str) {
        Platform platform;
        if (!isAvailable() || (platform = ShareSDK.getPlatform(str)) == null) {
            return null;
        }
        return platform.getDb().getToken();
    }

    public static String getUserID(String str) {
        Platform platform;
        if (!isAvailable() || (platform = ShareSDK.getPlatform(str)) == null) {
            return null;
        }
        return platform.getDb().getUserId();
    }

    public static String getUserName(String str) {
        Platform platform;
        if (!isAvailable() || (platform = ShareSDK.getPlatform(str)) == null) {
            return null;
        }
        return platform.getDb().getUserName();
    }

    private static synchronized boolean isAvailable() {
        boolean z;
        synchronized (ShareSDKWrapper.class) {
            if (state == 0) {
                state = isAvailable(ShareSDK.SDK_TAG);
            }
            z = state == 1;
        }
        return z;
    }

    public static boolean isLogin(String str) {
        Platform platform;
        if (!isAvailable() || (platform = ShareSDK.getPlatform(str)) == null) {
            return false;
        }
        return platform.isAuthValid();
    }

    private static boolean isOneKeyShareAvailable() {
        try {
            new OnekeyShare();
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void login(final String str, final CallbackWrapper callbackWrapper) {
        if (!isAvailable()) {
            if (callbackWrapper != null) {
                UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.wrappers.ShareSDKWrapper.3
                    @Override // android.os.Handler.Callback
                    public boolean handleMessage(Message message) {
                        callbackWrapper.onError(str, 8, new Throwable("platform " + str + " is not exist or disable"));
                        return false;
                    }
                });
                return;
            }
            return;
        }
        Platform platform = ShareSDK.getPlatform(str);
        if (platform == null) {
            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.wrappers.ShareSDKWrapper.1
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    callbackWrapper.onError(str, 8, new Throwable("platform " + str + " is not exist or disable"));
                    return false;
                }
            });
            return;
        }
        platform.showUser(null);
        if (callbackWrapper != null) {
            platform.setPlatformActionListener(new PlatformActionListener() { // from class: com.mob.wrappers.ShareSDKWrapper.2
                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onCancel(Platform platform2, int i) {
                    CallbackWrapper.this.onCancel(platform2.getName(), i);
                }

                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onComplete(Platform platform2, int i, HashMap<String, Object> hashMap) {
                    CallbackWrapper.this.onComplete(platform2.getName(), i, hashMap);
                }

                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onError(Platform platform2, int i, Throwable th) {
                    CallbackWrapper.this.onError(platform2.getName(), i, th);
                }
            });
        }
    }

    public static void logout(String str) {
        Platform platform;
        if (!isAvailable() || (platform = ShareSDK.getPlatform(str)) == null) {
            return;
        }
        platform.removeAccount(true);
    }

    public static void oneKeyShare(String str, HashMap<String, Object> hashMap, final CallbackWrapper callbackWrapper) {
        if (isOneKeyShareAvailable()) {
            OnekeyShare onekeyShare = new OnekeyShare();
            if (str != null) {
                onekeyShare.setPlatform(str);
            }
            if (callbackWrapper != null) {
                onekeyShare.setCallback(new PlatformActionListener() { // from class: com.mob.wrappers.ShareSDKWrapper.7
                    @Override // cn.sharesdk.framework.PlatformActionListener
                    public void onCancel(Platform platform, int i) {
                        CallbackWrapper.this.onCancel(platform.getName(), i);
                    }

                    @Override // cn.sharesdk.framework.PlatformActionListener
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap2) {
                        CallbackWrapper.this.onComplete(platform.getName(), i, hashMap2);
                    }

                    @Override // cn.sharesdk.framework.PlatformActionListener
                    public void onError(Platform platform, int i, Throwable th) {
                        CallbackWrapper.this.onError(platform.getName(), i, th);
                    }
                });
            }
            for (Field field : onekeyShare.getClass().getDeclaredFields()) {
                if (HashMap.class.isAssignableFrom(field.getType())) {
                    try {
                        field.setAccessible(true);
                        ((HashMap) field.get(onekeyShare)).putAll(hashMap);
                        break;
                    } catch (Throwable unused) {
                        continue;
                    }
                }
            }
            onekeyShare.show(MobSDK.getContext());
        }
    }

    public static void oneKeyShare(HashMap<String, Object> hashMap, CallbackWrapper callbackWrapper) {
        oneKeyShare(null, hashMap, callbackWrapper);
    }

    public static void share(final String str, HashMap<String, Object> hashMap, final CallbackWrapper callbackWrapper) {
        if (!isAvailable()) {
            if (callbackWrapper != null) {
                UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.wrappers.ShareSDKWrapper.6
                    @Override // android.os.Handler.Callback
                    public boolean handleMessage(Message message) {
                        callbackWrapper.onError(str, 9, new Throwable("platform " + str + " is not exist or disable"));
                        return false;
                    }
                });
                return;
            }
            return;
        }
        Platform platform = ShareSDK.getPlatform(str);
        if (platform == null) {
            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.wrappers.ShareSDKWrapper.4
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    callbackWrapper.onError(str, 9, new Throwable("platform " + str + " is not exist or disable"));
                    return false;
                }
            });
            return;
        }
        Platform.ShareParams shareParams = new Platform.ShareParams(hashMap);
        if (callbackWrapper != null) {
            platform.setPlatformActionListener(new PlatformActionListener() { // from class: com.mob.wrappers.ShareSDKWrapper.5
                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onCancel(Platform platform2, int i) {
                    CallbackWrapper.this.onCancel(platform2.getName(), i);
                }

                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onComplete(Platform platform2, int i, HashMap<String, Object> hashMap2) {
                    CallbackWrapper.this.onComplete(platform2.getName(), i, hashMap2);
                }

                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onError(Platform platform2, int i, Throwable th) {
                    CallbackWrapper.this.onError(platform2.getName(), i, th);
                }
            });
        }
        platform.share(shareParams);
    }
}
