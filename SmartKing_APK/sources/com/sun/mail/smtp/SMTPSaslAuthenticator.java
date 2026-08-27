package com.sun.mail.smtp;

import com.litesuits.orm.db.assit.SQLBuilder;
import com.sun.mail.auth.OAuth2SaslClientFactory;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.MailLogger;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/* loaded from: classes2.dex */
public class SMTPSaslAuthenticator implements SaslAuthenticator {
    private String host;
    private MailLogger logger;
    private String name;
    private SMTPTransport pr;
    private Properties props;

    static {
        try {
            OAuth2SaslClientFactory.init();
        } catch (Throwable unused) {
        }
    }

    public SMTPSaslAuthenticator(SMTPTransport sMTPTransport, String str, Properties properties, MailLogger mailLogger, String str2) {
        this.pr = sMTPTransport;
        this.name = str;
        this.props = properties;
        this.logger = mailLogger;
        this.host = str2;
    }

    private static final String responseText(SMTPTransport sMTPTransport) {
        String trim = sMTPTransport.getLastServerResponse().trim();
        return trim.length() > 4 ? trim.substring(4) : "";
    }

    @Override // com.sun.mail.smtp.SaslAuthenticator
    public boolean authenticate(String[] strArr, final String str, String str2, final String str3, final String str4) throws MessagingException {
        String str5;
        String str6;
        byte[] bArr;
        int simpleCommand;
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("SASL Mechanisms:");
            for (String str7 : strArr) {
                this.logger.fine(SQLBuilder.BLANK + str7);
            }
            this.logger.fine("");
        }
        try {
            SaslClient createSaslClient = Sasl.createSaslClient(strArr, str2, this.name, this.host, this.props, new CallbackHandler() { // from class: com.sun.mail.smtp.SMTPSaslAuthenticator.1
                @Override // javax.security.auth.callback.CallbackHandler
                public void handle(Callback[] callbackArr) {
                    if (SMTPSaslAuthenticator.this.logger.isLoggable(Level.FINE)) {
                        SMTPSaslAuthenticator.this.logger.fine("SASL callback length: " + callbackArr.length);
                    }
                    for (int i = 0; i < callbackArr.length; i++) {
                        if (SMTPSaslAuthenticator.this.logger.isLoggable(Level.FINE)) {
                            SMTPSaslAuthenticator.this.logger.fine("SASL callback " + i + ": " + callbackArr[i]);
                        }
                        if (callbackArr[i] instanceof NameCallback) {
                            ((NameCallback) callbackArr[i]).setName(str3);
                        } else if (callbackArr[i] instanceof PasswordCallback) {
                            ((PasswordCallback) callbackArr[i]).setPassword(str4.toCharArray());
                        } else if (callbackArr[i] instanceof RealmCallback) {
                            RealmCallback realmCallback = (RealmCallback) callbackArr[i];
                            realmCallback.setText(str != null ? str : realmCallback.getDefaultText());
                        } else if (callbackArr[i] instanceof RealmChoiceCallback) {
                            RealmChoiceCallback realmChoiceCallback = (RealmChoiceCallback) callbackArr[i];
                            if (str == null) {
                                realmChoiceCallback.setSelectedIndex(realmChoiceCallback.getDefaultChoice());
                            } else {
                                String[] choices = realmChoiceCallback.getChoices();
                                int i2 = 0;
                                while (true) {
                                    if (i2 >= choices.length) {
                                        break;
                                    }
                                    if (choices[i2].equals(str)) {
                                        realmChoiceCallback.setSelectedIndex(i2);
                                        break;
                                    }
                                    i2++;
                                }
                            }
                        }
                    }
                }
            });
            if (createSaslClient == null) {
                this.logger.fine("No SASL support");
                throw new UnsupportedOperationException("No SASL support");
            }
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("SASL client " + createSaslClient.getMechanismName());
            }
            try {
                String mechanismName = createSaslClient.getMechanismName();
                if (createSaslClient.hasInitialResponse()) {
                    byte[] evaluateChallenge = createSaslClient.evaluateChallenge(new byte[0]);
                    if (evaluateChallenge.length > 0) {
                        byte[] encode = BASE64EncoderStream.encode(evaluateChallenge);
                        str5 = ASCIIUtility.toString(encode, 0, encode.length);
                    } else {
                        str5 = "=";
                    }
                } else {
                    str5 = null;
                }
                int simpleCommand2 = str5 != null ? this.pr.simpleCommand("AUTH " + mechanismName + SQLBuilder.BLANK + str5) : this.pr.simpleCommand("AUTH " + mechanismName);
                if (simpleCommand2 == 530) {
                    this.pr.startTLS();
                    simpleCommand2 = str5 != null ? this.pr.simpleCommand("AUTH " + mechanismName + SQLBuilder.BLANK + str5) : this.pr.simpleCommand("AUTH " + mechanismName);
                }
                if (simpleCommand2 == 235) {
                    return true;
                }
                if (simpleCommand2 != 334) {
                    return false;
                }
                int i = simpleCommand2;
                boolean z = false;
                while (!z) {
                    if (i == 334) {
                        try {
                            if (createSaslClient.isComplete()) {
                                bArr = null;
                            } else {
                                byte[] bytes = ASCIIUtility.getBytes(responseText(this.pr));
                                if (bytes.length > 0) {
                                    bytes = BASE64DecoderStream.decode(bytes);
                                }
                                if (this.logger.isLoggable(Level.FINE)) {
                                    this.logger.fine("SASL challenge: " + ASCIIUtility.toString(bytes, 0, bytes.length) + " :");
                                }
                                bArr = createSaslClient.evaluateChallenge(bytes);
                            }
                            if (bArr == null) {
                                this.logger.fine("SASL: no response");
                                simpleCommand = this.pr.simpleCommand("");
                            } else {
                                if (this.logger.isLoggable(Level.FINE)) {
                                    this.logger.fine("SASL response: " + ASCIIUtility.toString(bArr, 0, bArr.length) + " :");
                                }
                                simpleCommand = this.pr.simpleCommand(BASE64EncoderStream.encode(bArr));
                            }
                            i = simpleCommand;
                        } catch (Exception e) {
                            this.logger.log(Level.FINE, "SASL Exception", (Throwable) e);
                        }
                    }
                    z = true;
                }
                if (i != 235) {
                    return false;
                }
                if (!createSaslClient.isComplete() || (str6 = (String) createSaslClient.getNegotiatedProperty("javax.security.sasl.qop")) == null || (!str6.equalsIgnoreCase("auth-int") && !str6.equalsIgnoreCase("auth-conf"))) {
                    return true;
                }
                this.logger.fine("SASL Mechanism requires integrity or confidentiality");
                return false;
            } catch (Exception e2) {
                this.logger.log(Level.FINE, "SASL AUTHENTICATE Exception", (Throwable) e2);
                return false;
            }
        } catch (SaslException e3) {
            this.logger.log(Level.FINE, "Failed to create SASL client", e3);
            throw new UnsupportedOperationException(e3.getMessage(), e3);
        }
    }
}
