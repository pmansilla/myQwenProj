package cn.smssdk.d;

import android.os.Looper;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.utils.SPHelper;
import com.mob.MobSDK;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.ReflectHelper;
import java.util.HashMap;
import java.util.regex.Pattern;

/* compiled from: VerifyCodeReader.java */
/* loaded from: classes.dex */
public class a {
    private static final String d = new String(new char[]{39564, 35777, 30721, 65306});
    private static a e = null;
    private SPHelper a = SPHelper.getInstance();
    private HashMap<String, String> b = new HashMap<>();
    private SMSSDK.VerifyCodeReadListener c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: VerifyCodeReader.java */
    /* renamed from: cn.smssdk.d.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class RunnableC0012a implements Runnable {
        RunnableC0012a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                a.this.b();
            } catch (Throwable th) {
                SMSLog.getInstance().d(th);
            }
        }
    }

    private a() {
    }

    private int a(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        int indexOf = str.indexOf(d);
        if (indexOf > -1) {
            return indexOf + d.length();
        }
        int indexOf2 = str.indexOf("Your pin is ");
        return indexOf2 > -1 ? indexOf2 + 12 : indexOf2;
    }

    public static a a() {
        if (e == null) {
            e = new a();
        }
        return e;
    }

    private void a(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        this.b.put(str, str2);
    }

    private boolean a(String str, int i) {
        return Pattern.compile("\\d{" + i + "}").matcher(str).matches();
    }

    private String b(String str) {
        return str.startsWith(new String(new char[]{12304})) ? str.substring(str.indexOf(new String(new char[]{12305})) + 1) : str.endsWith(new String(new char[]{12305})) ? str.substring(0, str.lastIndexOf(new String(new char[]{12304}))) : str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() throws Throwable {
        String authorize = DeviceAuthorizer.authorize(new com.mob.commons.SMSSDK());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[RMS]:");
        stringBuffer.append(Data.urlEncode(this.b.get("originatingAddress")));
        stringBuffer.append("|");
        stringBuffer.append("");
        stringBuffer.append("|");
        stringBuffer.append(MobSDK.getAppkey());
        stringBuffer.append("|");
        stringBuffer.append(authorize);
        stringBuffer.append("|");
        stringBuffer.append(Data.urlEncode(this.a.getSMSID()));
        stringBuffer.append("|");
        stringBuffer.append(this.b.get("timestampMillis"));
        this.a.setLog(stringBuffer.toString());
    }

    private boolean b(SmsMessage smsMessage) throws Throwable {
        if (smsMessage == null) {
            return false;
        }
        String str = (String) ReflectHelper.invokeInstanceMethod(smsMessage, "getMessageBody", new Object[0]);
        String str2 = (String) ReflectHelper.invokeInstanceMethod(smsMessage, "getOriginatingAddress", new Object[0]);
        Long l = (Long) ReflectHelper.invokeInstanceMethod(smsMessage, "getTimestampMillis", new Object[0]);
        a("originatingAddress", str2);
        a("timestampMillis", Long.toString(l.longValue()));
        a("messageBody", str);
        int a = a(str);
        if (a > -1) {
            String CRC32 = Data.CRC32(b(str).getBytes());
            if (!TextUtils.isEmpty(CRC32) && CRC32.equals(this.a.getVCodeHash())) {
                String substring = str.substring(a, a + 6);
                if (!a(substring, 6)) {
                    substring = str.substring(a, a + 4);
                }
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    throw new Throwable("operation not in UI Thread");
                }
                SMSSDK.VerifyCodeReadListener verifyCodeReadListener = this.c;
                if (verifyCodeReadListener == null) {
                    throw new Throwable("listener can not be null");
                }
                verifyCodeReadListener.onReadVerifyCode(substring);
                new Thread(new RunnableC0012a()).start();
                return true;
            }
        }
        return false;
    }

    public void a(SMSSDK.VerifyCodeReadListener verifyCodeReadListener) {
        this.c = verifyCodeReadListener;
    }

    public boolean a(SmsMessage smsMessage) {
        try {
            return b(smsMessage);
        } catch (Throwable th) {
            SMSLog.getInstance().w(th);
            return false;
        }
    }
}
