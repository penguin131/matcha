package com.mail;

import com.helper.Config;
import org.apache.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {
    private final static Logger logger = Logger.getLogger(MailService.class);

    public static void sendConfirmationEmail(String to, String hash) throws MessagingException {
        logger.info(String.format("sendConfirmationEmail() to: %s, hash: %s", to, hash));
        String host = "smtp.gmail.com";
        final String user="smight.matcha@gmail.com";
        final String password="HBj41TFQ";
        Properties props = new Properties();
        props.put("mail.smtp.host",host);
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Account confirmation");
            message.setText(getUrlToText() + hash);
            Transport.send(message);
            logger.info("message sent successfully...");
        } catch (MessagingException e) {
            logger.info("sendConfirmationEmail() exception:\n" + e.getMessage());
            throw e;
        }
    }

    private static String getUrlToText() {
        if (!Config.isIsProd()) {
            return "To confirm the account click on the link:\nhttp://localhost:8080/tomeeapp/verification/";
        } else {
            return "To confirm the account click on the link:\nhttp://84.38.183.163:8080/spark-server-1.0/verification/";
        }
    }
}
