package com.czw.friendly.mail;

import com.tencent.bugly.Bugly;
import java.util.Properties;

/* loaded from: classes.dex */
public class MailInfo {
    private String[] attachFileNames;
    private String content;
    private String fromAddress;
    private String mailServerHost;
    private String mailServerPort;
    private String password;
    private String subject;
    private String toAddress;
    private String userName;
    private boolean validate = true;

    public String[] getAttachFileNames() {
        return this.attachFileNames;
    }

    public String getContent() {
        return this.content;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    public String getMailServerHost() {
        return this.mailServerHost;
    }

    public String getMailServerPort() {
        return this.mailServerPort;
    }

    public String getPassword() {
        return this.password;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.mailServerHost);
        properties.put("mail.smtp.port", this.mailServerPort);
        properties.put("mail.smtp.auth", this.validate ? "true" : Bugly.SDK_IS_DEV);
        return properties;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getToAddress() {
        return this.toAddress;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean isValidate() {
        return this.validate;
    }

    public void setAttachFileNames(String[] strArr) {
        this.attachFileNames = strArr;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public void setFromAddress(String str) {
        this.fromAddress = str;
    }

    public void setMailServerHost(String str) {
        this.mailServerHost = str;
    }

    public void setMailServerPort(String str) {
        this.mailServerPort = str;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public void setSubject(String str) {
        this.subject = str;
    }

    public void setToAddress(String str) {
        this.toAddress = str;
    }

    public void setUserName(String str) {
        this.userName = str;
    }

    public void setValidate(boolean z) {
        this.validate = z;
    }
}
