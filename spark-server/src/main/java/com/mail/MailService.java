package com.mail;

import org.apache.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {
    private final static Logger logger = Logger.getLogger(MailService.class);

    public static void sendConfirmationEmail(String to) throws MessagingException {
        logger.info("sendConfirmationEmail() to: " + to);
        String host = "smtp.gmail.com";
        final String user="smight.matcha@gmail.com";
        final String password="HBj41TFQ";

        Properties props = new Properties();
        props.put("mail.smtp.host",host);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("javatpoint");
            message.setText("This is simple program of sending email using JavaMail API");

            //send the message
            Transport.send(message);

            logger.info("message sent successfully...");

        } catch (MessagingException e) {
            logger.info(e.getMessage());
            throw e;
        }
    }
}
