package com.mail;

import com.helper.Config;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailService {
    private final static Logger logger = Logger.getLogger(MailService.class);

//    public static void sendConfirmationEmail(String to, String hash) throws MessagingException {
//        logger.info(String.format("sendConfirmationEmail() to: %s, hash: %s", to, hash));
//        String host = "smtp.gmail.com";
//        final String user="smight.matcha@gmail.com";
//        final String password="HBj41TFQ";
//        Properties props = new Properties();
//        props.put("mail.smtp.host",host);
//        props.setProperty("mail.transport.protocol", "smtp");
//        props.setProperty("mail.host", "smtp.gmail.com");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.ssl.enable", "true");
//        props.put("mail.smtp.port", "465");
//        Session session = Session.getDefaultInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(user,password);
//                    }
//                });
//        try {
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(user));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            message.setSubject("Account confirmation");
//            message.setText(getUrlToText() + hash);
//            Transport.send(message);
//            logger.info("message sent successfully...");
//        } catch (MessagingException e) {
//            logger.info("sendConfirmationEmail() exception:\n" + e.getMessage());
//            throw e;
//        }
//    }

    public static void sendConfirmationEmail(String to, String hash) throws MessagingException {
        final String fromEmail = "smight.matcha@rambler.ru"; //requires valid gmail id
        final String password = "HBj41TFQ"; // correct password for gmail id; // can be any email id

        System.out.println("SSLEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.rambler.ru"); //SMTP Host
        props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.port", "465"); //SMTP Port

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);
        EmailUtil.sendEmail(session, to,"SSLEmail Testing Subject", "SSLEmail Testing Body");

    }

    private static String getUrlToText() {
        if (!Config.isIsProd()) {
            return "To confirm the account click on the link:\nhttp://localhost:8080/tomeeapp/verification/";
        } else {
            return "To confirm the account click on the link:\nhttp://84.38.183.163:8080/spark-server-1.0/verification/";
        }
    }

    public static class EmailUtil {

        public static void sendEmail(Session session, String toEmail, String subject, String body){
            try
            {
                MimeMessage msg = new MimeMessage(session);
                //set message headers
                msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
                msg.addHeader("format", "flowed");
                msg.addHeader("Content-Transfer-Encoding", "8bit");

                msg.setFrom(new InternetAddress("smight.matcha@rambler.ru", "NoReply-JD"));

                msg.setReplyTo(InternetAddress.parse("smight.matcha@rambler.ru", false));

                msg.setSubject(subject, "UTF-8");

                msg.setText(body, "UTF-8");

                msg.setSentDate(new Date());

                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
                System.out.println("Message is ready");
                Transport.send(msg);

                System.out.println("EMail Sent Successfully!!");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
