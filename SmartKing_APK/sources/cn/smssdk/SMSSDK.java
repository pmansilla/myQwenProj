package cn.smssdk;

import android.app.Activity;
import android.telephony.SmsMessage;
import cn.smssdk.wrapper.TokenVerifyResult;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class SMSSDK {
    public static final int EVENT_GET_CONTACTS = 4;
    public static final int EVENT_GET_FRIENDS_IN_APP = 6;
    public static final int EVENT_GET_NEW_FRIENDS_COUNT = 7;
    public static final int EVENT_GET_SUPPORTED_COUNTRIES = 1;
    public static final int EVENT_GET_VERIFICATION_CODE = 2;
    public static final int EVENT_GET_VERIFY_TOKEN_CODE = 9;
    public static final int EVENT_GET_VOICE_VERIFICATION_CODE = 8;
    public static final int EVENT_SUBMIT_USER_INFO = 5;
    public static final int EVENT_SUBMIT_VERIFICATION_CODE = 3;
    public static final int EVENT_VERIFY_LOGIN = 10;
    public static final int RESULT_COMPLETE = -1;
    public static final int RESULT_ERROR = 0;
    private static b a;
    private static InitFlag b = InitFlag.DEFAULT;
    private static volatile boolean c = false;

    /* loaded from: classes.dex */
    public enum InitFlag {
        DEFAULT,
        WARNNING_READCONTACT,
        WARNNING_READCONTACT_DIALOG_MODE,
        DISABLE_CONTACT
    }

    /* loaded from: classes.dex */
    public interface VerifyCodeReadListener {
        void onReadVerifyCode(String str);
    }

    static {
        a();
    }

    private static void a() {
        if (c) {
            return;
        }
        synchronized (SMSSDK.class) {
            if (!c) {
                if (a == null) {
                    a = new b(b);
                    a.b();
                    a.e();
                }
                c = true;
            }
        }
    }

    @Deprecated
    public static void getContacts(boolean z) {
        a();
        a.a(4, Boolean.valueOf(z));
    }

    public static String[] getCountry(String str) {
        a();
        return a.a(str);
    }

    public static String[] getCountryByMCC(String str) {
        a();
        return a.b(str);
    }

    @Deprecated
    public static void getFriendsInApp() {
        a();
        a.a(6, (Object) null);
    }

    public static HashMap<Character, ArrayList<String[]>> getGroupedCountryList() {
        a();
        return a.a();
    }

    @Deprecated
    public static void getNewFriendsCount() {
        a();
        a.a(7, (Object) null);
    }

    public static void getSupportedCountries() {
        a();
        a.a(1, (Object) null);
    }

    public static void getToken() {
        a();
        a.c();
    }

    public static void getVerificationCode(String str, String str2) {
        getVerificationCode((String) null, str, str2);
    }

    public static void getVerificationCode(String str, String str2, OnSendMessageHandler onSendMessageHandler) {
        getVerificationCode(str, str2, null, onSendMessageHandler);
    }

    public static void getVerificationCode(String str, String str2, String str3) {
        getVerificationCode(str2, str3, str, null);
    }

    public static void getVerificationCode(String str, String str2, String str3, OnSendMessageHandler onSendMessageHandler) {
        getVerificationCode(str, str2, null, str3, onSendMessageHandler);
    }

    public static void getVerificationCode(String str, String str2, String str3, String str4, OnSendMessageHandler onSendMessageHandler) {
        a();
        a.a(2, new Object[]{str, str2, str3, str4, onSendMessageHandler});
    }

    public static String getVersion() {
        return "3.8.3";
    }

    public static void getVoiceVerifyCode(String str, String str2) {
        getVoiceVerifyCode(str, str2, null);
    }

    public static void getVoiceVerifyCode(String str, String str2, String str3) {
        a();
        a.a(8, new String[]{str2, str, str3});
    }

    public static void login(String str, TokenVerifyResult tokenVerifyResult) {
        a();
        a.a(str, tokenVerifyResult);
    }

    public static void readVerificationCode(SmsMessage smsMessage, VerifyCodeReadListener verifyCodeReadListener) {
        a();
        a.a(smsMessage, verifyCodeReadListener);
    }

    public static void registerEventHandler(EventHandler eventHandler) {
        a();
        a.a(eventHandler);
    }

    @Deprecated
    public static synchronized void setAskPermisionOnReadContact(boolean z) {
        synchronized (SMSSDK.class) {
        }
    }

    @Deprecated
    public static void setInitFlag(InitFlag initFlag) {
        b = initFlag;
    }

    @Deprecated
    public static void showAuthorizeDialog(Activity activity, OnDialogListener onDialogListener) {
        a();
        a.a(activity, onDialogListener);
    }

    @Deprecated
    public static void submitUserInfo(String str, String str2, String str3, String str4, String str5) {
        a();
        a.a(5, new String[]{str, str2, str3, str4, str5});
    }

    public static void submitVerificationCode(String str, String str2, String str3) {
        a();
        a.a(3, new String[]{str, str2, str3});
    }

    public static void unregisterAllEventHandler() {
        a();
        a.d();
    }

    public static void unregisterEventHandler(EventHandler eventHandler) {
        a();
        a.b(eventHandler);
    }
}
