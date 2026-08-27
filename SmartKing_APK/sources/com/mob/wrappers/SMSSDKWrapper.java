package com.mob.wrappers;

import android.os.Handler;
import android.os.Message;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import com.mob.MobSDK;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.UIHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class SMSSDKWrapper extends SDKWrapper implements PublicMemberKeeper {
    private static int state;

    /* loaded from: classes2.dex */
    public interface BeforeSendMessage {
        boolean beforeSendMessage(String str, String str2);
    }

    /* loaded from: classes2.dex */
    public static abstract class CallbackWrapper implements PublicMemberKeeper {
        private static final HashSet<CallbackWrapper> CALLBACKS = new HashSet<>();
        private Object handler;

        public CallbackWrapper() {
            if (SMSSDKWrapper.access$400()) {
                this.handler = new EventHandler() { // from class: com.mob.wrappers.SMSSDKWrapper.CallbackWrapper.1
                    @Override // cn.smssdk.EventHandler
                    public void afterEvent(final int i, final int i2, final Object obj) {
                        UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.wrappers.SMSSDKWrapper.CallbackWrapper.1.1
                            @Override // android.os.Handler.Callback
                            public boolean handleMessage(Message message) {
                                CallbackWrapper.this.afterEvent(i, i2, obj);
                                return false;
                            }
                        });
                    }
                };
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void broadcastNotAvailable(int i) {
            Throwable th = new Throwable("SMSSDK is not available");
            synchronized (CALLBACKS) {
                Iterator<CallbackWrapper> it = CALLBACKS.iterator();
                while (it.hasNext()) {
                    it.next().afterEvent(i, 0, th);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void registerEventHandler() {
            if (SMSSDKWrapper.access$400()) {
                SMSSDK.registerEventHandler((EventHandler) this.handler);
                return;
            }
            synchronized (CALLBACKS) {
                CALLBACKS.add(this);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregisterEventHandler() {
            if (SMSSDKWrapper.access$400()) {
                SMSSDK.unregisterEventHandler((EventHandler) this.handler);
                return;
            }
            synchronized (CALLBACKS) {
                CALLBACKS.remove(this);
            }
        }

        protected abstract void afterEvent(int i, int i2, Object obj);
    }

    static /* synthetic */ boolean access$400() {
        return isAvailable();
    }

    public static synchronized HashMap<Character, ArrayList<String[]>> getGroupedCountryList() {
        synchronized (SMSSDKWrapper.class) {
            if (isAvailable()) {
                return SMSSDK.getGroupedCountryList();
            }
            return new HashMap<>();
        }
    }

    public static void getSupportedCountries() {
        if (isAvailable()) {
            SMSSDK.getSupportedCountries();
        } else {
            CallbackWrapper.broadcastNotAvailable(1);
        }
    }

    public static void getVerificationCode(String str, String str2) {
        getVerificationCode(str, str2, null);
    }

    public static void getVerificationCode(String str, String str2, final BeforeSendMessage beforeSendMessage) {
        if (isAvailable()) {
            SMSSDK.getVerificationCode(str, str2, beforeSendMessage != null ? new OnSendMessageHandler() { // from class: com.mob.wrappers.SMSSDKWrapper.1
                @Override // cn.smssdk.OnSendMessageHandler
                public boolean onSendMessage(String str3, String str4) {
                    return BeforeSendMessage.this.beforeSendMessage(str3, str4);
                }
            } : null);
        } else {
            CallbackWrapper.broadcastNotAvailable(2);
        }
    }

    public static synchronized void getVoiceVerifyCode(String str, String str2) {
        synchronized (SMSSDKWrapper.class) {
            if (isAvailable()) {
                SMSSDK.getVoiceVerifyCode(str, str2);
            } else {
                CallbackWrapper.broadcastNotAvailable(8);
            }
        }
    }

    private static synchronized boolean isAvailable() {
        boolean z;
        synchronized (SMSSDKWrapper.class) {
            if (state == 0) {
                state = isAvailable("SMSSDK");
            }
            z = state == 1;
        }
        return z;
    }

    public static void registerEventHandler(CallbackWrapper callbackWrapper) {
        callbackWrapper.registerEventHandler();
    }

    public static synchronized void setAskPermisionOnReadContact(boolean z) {
        synchronized (SMSSDKWrapper.class) {
            if (isAvailable()) {
                SMSSDK.setInitFlag(z ? SMSSDK.InitFlag.WARNNING_READCONTACT_DIALOG_MODE : SMSSDK.InitFlag.DEFAULT);
            }
        }
    }

    public static synchronized void showVerificationPage(final CallbackWrapper callbackWrapper) {
        synchronized (SMSSDKWrapper.class) {
            try {
                RegisterPage registerPage = new RegisterPage();
                if (callbackWrapper != null) {
                    registerPage.setRegisterCallback((EventHandler) callbackWrapper.handler);
                }
                registerPage.show(MobSDK.getContext());
            } catch (Throwable th) {
                if (callbackWrapper != null) {
                    UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.wrappers.SMSSDKWrapper.2
                        @Override // android.os.Handler.Callback
                        public boolean handleMessage(Message message) {
                            if (CallbackWrapper.this != null) {
                                CallbackWrapper.this.afterEvent(0, 0, th);
                            }
                            return false;
                        }
                    });
                }
            }
        }
    }

    public static synchronized void submitVerificationCode(String str, String str2, String str3) {
        synchronized (SMSSDKWrapper.class) {
            if (isAvailable()) {
                SMSSDK.submitVerificationCode(str, str2, str3);
            } else {
                CallbackWrapper.broadcastNotAvailable(3);
            }
        }
    }

    public static void unregisterEventHandler(CallbackWrapper callbackWrapper) {
        callbackWrapper.unregisterEventHandler();
    }
}
