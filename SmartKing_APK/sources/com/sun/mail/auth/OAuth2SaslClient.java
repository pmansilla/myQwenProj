package com.sun.mail.auth;

import com.sun.mail.util.ASCIIUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/* loaded from: classes2.dex */
public class OAuth2SaslClient implements SaslClient {
    private CallbackHandler cbh;
    private boolean complete = false;

    public OAuth2SaslClient(Map<String, ?> map, CallbackHandler callbackHandler) {
        this.cbh = callbackHandler;
    }

    public void dispose() throws SaslException {
    }

    public byte[] evaluateChallenge(byte[] bArr) throws SaslException {
        byte[] bytes;
        if (this.complete) {
            return new byte[0];
        }
        Callback nameCallback = new NameCallback("User name:");
        PasswordCallback passwordCallback = new PasswordCallback("OAuth token:", false);
        try {
            this.cbh.handle(new Callback[]{nameCallback, passwordCallback});
            String name = nameCallback.getName();
            String str = new String(passwordCallback.getPassword());
            passwordCallback.clearPassword();
            String str2 = "user=" + name + "\u0001auth=Bearer " + str + "\u0001\u0001";
            try {
                bytes = str2.getBytes("utf-8");
            } catch (UnsupportedEncodingException unused) {
                bytes = ASCIIUtility.getBytes(str2);
            }
            this.complete = true;
            return bytes;
        } catch (IOException e) {
            throw new SaslException("Callback handler failed", e);
        } catch (UnsupportedCallbackException e2) {
            throw new SaslException("Unsupported callback", e2);
        }
    }

    public String getMechanismName() {
        return "XOAUTH2";
    }

    public Object getNegotiatedProperty(String str) {
        if (this.complete) {
            return null;
        }
        throw new IllegalStateException("OAUTH2 getNegotiatedProperty");
    }

    public boolean hasInitialResponse() {
        return true;
    }

    public boolean isComplete() {
        return this.complete;
    }

    public byte[] unwrap(byte[] bArr, int i, int i2) throws SaslException {
        throw new IllegalStateException("OAUTH2 unwrap not supported");
    }

    public byte[] wrap(byte[] bArr, int i, int i2) throws SaslException {
        throw new IllegalStateException("OAUTH2 wrap not supported");
    }
}
