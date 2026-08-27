package com.sun.mail.auth;

import java.security.Provider;
import java.security.Security;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslClientFactory;
import javax.security.sasl.SaslException;

/* loaded from: classes2.dex */
public class OAuth2SaslClientFactory implements SaslClientFactory {
    private static final String MECHANISM_NAME = "SaslClientFactory.XOAUTH2";
    private static final String PROVIDER_NAME = "JavaMail-OAuth2";

    /* loaded from: classes2.dex */
    static class OAuth2Provider extends Provider {
        private static final long serialVersionUID = -5371795551562287059L;

        public OAuth2Provider() {
            super(OAuth2SaslClientFactory.PROVIDER_NAME, 1.0d, "XOAUTH2 SASL Mechanism");
            put(OAuth2SaslClientFactory.MECHANISM_NAME, OAuth2SaslClientFactory.class.getName());
        }
    }

    public static void init() {
        try {
            if (Security.getProvider(PROVIDER_NAME) == null) {
                Security.addProvider(new OAuth2Provider());
            }
        } catch (SecurityException unused) {
        }
    }

    public SaslClient createSaslClient(String[] strArr, String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        for (String str4 : strArr) {
            if (str4.equals("XOAUTH2")) {
                return new OAuth2SaslClient(map, callbackHandler);
            }
        }
        return null;
    }

    public String[] getMechanismNames(Map<String, ?> map) {
        return new String[]{"XOAUTH2"};
    }
}
