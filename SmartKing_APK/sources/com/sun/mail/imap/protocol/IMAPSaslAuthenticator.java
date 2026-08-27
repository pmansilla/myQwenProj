package com.sun.mail.imap.protocol;

import com.sun.mail.auth.OAuth2SaslClientFactory;
import com.sun.mail.util.MailLogger;
import java.util.Properties;

/* loaded from: classes2.dex */
public class IMAPSaslAuthenticator implements SaslAuthenticator {
    private String host;
    private MailLogger logger;
    private String name;
    private IMAPProtocol pr;
    private Properties props;

    static {
        try {
            OAuth2SaslClientFactory.init();
        } catch (Throwable unused) {
        }
    }

    public IMAPSaslAuthenticator(IMAPProtocol iMAPProtocol, String str, Properties properties, MailLogger mailLogger, String str2) {
        this.pr = iMAPProtocol;
        this.name = str;
        this.props = properties;
        this.logger = mailLogger;
        this.host = str2;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0106 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.sun.mail.imap.protocol.SaslAuthenticator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean authenticate(java.lang.String[] r17, final java.lang.String r18, java.lang.String r19, final java.lang.String r20, final java.lang.String r21) throws com.sun.mail.iap.ProtocolException {
        /*
            Method dump skipped, instructions count: 671
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.protocol.IMAPSaslAuthenticator.authenticate(java.lang.String[], java.lang.String, java.lang.String, java.lang.String, java.lang.String):boolean");
    }
}
