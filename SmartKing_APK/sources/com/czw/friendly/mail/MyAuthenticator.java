package com.czw.friendly.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/* loaded from: classes.dex */
public class MyAuthenticator extends Authenticator {
    String password;
    String userName;

    public MyAuthenticator() {
        this.userName = null;
        this.password = null;
    }

    public MyAuthenticator(String str, String str2) {
        this.userName = null;
        this.password = null;
        this.userName = str;
        this.password = str2;
    }

    @Override // javax.mail.Authenticator
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.userName, this.password);
    }
}
