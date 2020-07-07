package com.mail;

import com.helper.Config;
import org.apache.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class MailService {
    private final static Logger logger = Logger.getLogger(MailService.class);

    public static void sendConfirmationEmail(String to, String hash) throws UnsupportedEncodingException, MessagingException {
        logger.info(String.format("sendConfirmationEmail() to: %s, hash: %s", to, hash));
        final String fromEmail = "smight.matcha@rambler.ru";
        final String password = "HBj41TFQ";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.rambler.ru");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getDefaultInstance(props, auth);
        sendEmail(session, to,"Account confirmation", getUrlToText() + hash);
    }

    private static String getUrlToText() {
        if (!Config.isIsProd()) {
            return "To confirm the account click on the link:\nhttp://localhost:8080/tomeeapp/verification/";
        } else {
            return "To confirm the account click on the link:\nhttp://84.38.183.163:8080/spark-server-1.0/verification/";
        }
    }

    public static void sendEmail(Session session, String toEmail, String subject, String body) throws UnsupportedEncodingException, MessagingException {
        try
        {
            logger.info("Message body: " + body);
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress("smight.matcha@rambler.ru", "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse("smight.matcha@rambler.ru", false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);
            logger.info("EMail Sent Successfully!");
        }
        catch (Exception e) {
            logger.info("Send mail error: " + e.getMessage());
            throw e;
        }
    }
}
