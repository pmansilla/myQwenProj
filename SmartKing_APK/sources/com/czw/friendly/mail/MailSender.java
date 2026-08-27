package com.czw.friendly.mail;

import android.util.Log;
import java.io.File;
import java.util.Date;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/* loaded from: classes.dex */
public class MailSender {
    private Message createAttachmentMail(final MailInfo mailInfo, File file) {
        MimeMessage mimeMessage;
        try {
            mimeMessage = new MimeMessage(Session.getInstance(mailInfo.getProperties(), new Authenticator() { // from class: com.czw.friendly.mail.MailSender.1
                @Override // javax.mail.Authenticator
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailInfo.getUserName(), mailInfo.getPassword());
                }
            }));
        } catch (Exception e) {
            e = e;
            mimeMessage = null;
        }
        try {
            mimeMessage.setFrom(new InternetAddress(mailInfo.getFromAddress()));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getToAddress()));
            mimeMessage.setSubject(mailInfo.getSubject());
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(mailInfo.getContent(), "text/html;charset=UTF-8");
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(mimeBodyPart);
            MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
            DataHandler dataHandler = new DataHandler(new FileDataSource(file));
            mimeBodyPart2.setDataHandler(dataHandler);
            mimeBodyPart2.setFileName(MimeUtility.encodeText(dataHandler.getName()));
            mimeMultipart.addBodyPart(mimeBodyPart2);
            mimeMultipart.setSubType("mixed");
            mimeMessage.setContent(mimeMultipart);
            mimeMessage.saveChanges();
            MailcapCommandMap mailcapCommandMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mailcapCommandMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mailcapCommandMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mailcapCommandMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mailcapCommandMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mailcapCommandMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mailcapCommandMap);
            Log.e("TAG", "创建带附件的邮件成功==>" + file.getPath());
        } catch (Exception e2) {
            e = e2;
            Log.e("TAG", "创建带附件的邮件失败");
            e.printStackTrace();
            return mimeMessage;
        }
        return mimeMessage;
    }

    public static boolean sendHtmlMail(MailInfo mailInfo) {
        try {
            MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(mailInfo.getProperties(), mailInfo.isValidate() ? new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword()) : null));
            mimeMessage.setFrom(new InternetAddress(mailInfo.getFromAddress()));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getToAddress()));
            mimeMessage.setSubject(mailInfo.getSubject());
            mimeMessage.setSentDate(new Date());
            MimeMultipart mimeMultipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mimeMultipart.addBodyPart(mimeBodyPart);
            mimeMessage.setContent(mimeMultipart);
            Transport.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendFileMail(MailInfo mailInfo, File file) {
        try {
            Transport.send(createAttachmentMail(mailInfo, file));
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendTextMail(MailInfo mailInfo) {
        Session defaultInstance = Session.getDefaultInstance(mailInfo.getProperties(), mailInfo.isValidate() ? new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword()) : null);
        try {
            defaultInstance.setDebug(true);
            MimeMessage mimeMessage = new MimeMessage(defaultInstance);
            mimeMessage.setFrom(new InternetAddress(mailInfo.getFromAddress()));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getToAddress()));
            mimeMessage.setSubject(mailInfo.getSubject());
            mimeMessage.setSentDate(new Date());
            mimeMessage.setText(mailInfo.getContent());
            Transport.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
