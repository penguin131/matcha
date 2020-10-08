package com.school21.server.controller;

import com.school21.server.security.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private JWTHelper helper;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
