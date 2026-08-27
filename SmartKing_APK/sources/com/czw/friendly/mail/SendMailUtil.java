package com.czw.friendly.mail;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.io.File;

/* loaded from: classes.dex */
public class SendMailUtil {
    private static final String FROM_ADD = "3343249301@qq.com";
    private static final String FROM_PSW = "davpgtmyazmbciij";
    private static final String HOST = "smtp.qq.com";
    private static final String PORT = "587";

    /* loaded from: classes.dex */
    public interface SendMailCallback {
        void onSend(boolean z);
    }

    @NonNull
    private static MailInfo creatMail(String str, String str2, String str3) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD);
        mailInfo.setPassword(FROM_PSW);
        mailInfo.setFromAddress(FROM_ADD);
        mailInfo.setToAddress(str);
        mailInfo.setSubject(str2 + "App Bug反馈");
        if (TextUtils.isEmpty(str3)) {
            str3 = "";
        }
        mailInfo.setContent("您好，这是我的联系方式" + str3 + "附件里面是日志文件！！！");
        return mailInfo;
    }

    public static void send(final File file, String str, String str2, String str3, final SendMailCallback sendMailCallback) {
        final MailInfo creatMail = creatMail(str, str2, str3);
        final MailSender mailSender = new MailSender();
        new Thread(new Runnable() { // from class: com.czw.friendly.mail.SendMailUtil.1
            @Override // java.lang.Runnable
            public void run() {
                boolean sendFileMail = MailSender.this.sendFileMail(creatMail, file);
                if (sendMailCallback != null) {
                    sendMailCallback.onSend(sendFileMail);
                }
            }
        }).start();
    }

    public static void send(String str, String str2, String str3) {
        final MailInfo creatMail = creatMail(str, str2, str3);
        final MailSender mailSender = new MailSender();
        new Thread(new Runnable() { // from class: com.czw.friendly.mail.SendMailUtil.2
            @Override // java.lang.Runnable
            public void run() {
                MailSender.this.sendTextMail(creatMail);
            }
        }).start();
    }
}
