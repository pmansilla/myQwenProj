package com.mob.wrappers;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.UIHandler;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.gui.UMSGUI;

/* loaded from: classes2.dex */
public class UMSSDKWrapper extends SDKWrapper implements PublicMemberKeeper {
    private static int state;
    private static int stateGUI;

    /* loaded from: classes2.dex */
    public interface CallbackWrapper {
        void onError(Throwable th);

        void onSuccess(User user);
    }

    /* loaded from: classes2.dex */
    public static class User implements PublicMemberKeeper {
        private com.mob.ums.User user;

        private User(com.mob.ums.User user) {
            this.user = user;
        }

        public String getAvatar() {
            if (this.user == null || this.user.avatar.isNull()) {
                return null;
            }
            return ((String[]) this.user.avatar.get())[0];
        }

        public String getNickname() {
            if (this.user == null) {
                return null;
            }
            return (String) this.user.nickname.get();
        }

        public String getUserId() {
            if (this.user == null) {
                return null;
            }
            return (String) this.user.id.get();
        }
    }

    public static void getLoginUser(final CallbackWrapper callbackWrapper) {
        if (!isAvailable()) {
            unAvailable(callbackWrapper, 1);
            return;
        }
        try {
            UMSSDK.getLoginUser(new OperationCallback<com.mob.ums.User>() { // from class: com.mob.wrappers.UMSSDKWrapper.1
                public void onFailed(Throwable th) {
                    if (CallbackWrapper.this != null) {
                        CallbackWrapper.this.onError(th);
                    }
                }

                public void onSuccess(com.mob.ums.User user) {
                    if (CallbackWrapper.this != null) {
                        CallbackWrapper.this.onSuccess(new User(user));
                    }
                }
            });
        } catch (Throwable th) {
            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.wrappers.UMSSDKWrapper.2
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    if (CallbackWrapper.this == null) {
                        return false;
                    }
                    CallbackWrapper.this.onError(th);
                    return false;
                }
            });
        }
    }

    public static String getLoginUserId() {
        if (isAvailable()) {
            return UMSSDK.getLoginUserId();
        }
        return null;
    }

    public static String getLoginUserToken() {
        if (isAvailable()) {
            return UMSSDK.getLoginUserToken();
        }
        return null;
    }

    public static boolean hasLogin() {
        return !TextUtils.isEmpty(getLoginUserToken());
    }

    private static synchronized boolean isAvailable() {
        boolean z;
        synchronized (UMSSDKWrapper.class) {
            if (state == 0) {
                state = isAvailable("UMSSDK");
            }
            z = state == 1;
        }
        return z;
    }

    private static boolean isAvailableGUI() {
        if (stateGUI == 0) {
            stateGUI = -1;
            try {
                new UMSGUI();
                stateGUI = 1;
            } catch (Throwable unused) {
            }
        }
        return stateGUI == 1;
    }

    public static void logout() {
        if (isAvailable()) {
            UMSSDK.logout((OperationCallback) null);
        }
    }

    public static void showLogin(final CallbackWrapper callbackWrapper) {
        if (isAvailableGUI()) {
            UMSGUI.showLogin(new OperationCallback<com.mob.ums.User>() { // from class: com.mob.wrappers.UMSSDKWrapper.3
                public void onFailed(Throwable th) {
                    if (CallbackWrapper.this != null) {
                        CallbackWrapper.this.onError(th);
                    }
                }

                public void onSuccess(com.mob.ums.User user) {
                    if (CallbackWrapper.this != null) {
                        CallbackWrapper.this.onSuccess(new User(user));
                    }
                }
            });
        } else {
            unAvailable(callbackWrapper, 2);
        }
    }

    private static void unAvailable(final CallbackWrapper callbackWrapper, final int i) {
        UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.wrappers.UMSSDKWrapper.4
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                if (CallbackWrapper.this == null) {
                    return false;
                }
                CallbackWrapper.this.onError(i == 2 ? new Throwable("UMSSDKGUI is not available") : new Throwable("UMSSDK is not available"));
                return false;
            }
        });
    }
}
