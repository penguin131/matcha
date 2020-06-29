package com.matcha.mailservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestServiceController {

    private JavaMailSender javaMailSender;

    @RequestMapping(value = "/test")
    @ResponseBody
    public String sendMail() {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("zininlol@rambler.ru");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");

        javaMailSender.send(msg);

        return "ss";
    }

    @Autowired
    public RestServiceController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
}
