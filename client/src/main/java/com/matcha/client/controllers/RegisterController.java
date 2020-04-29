package com.matcha.client.controllers;

import com.matcha.client.form.RegisterForm;
import com.matcha.client.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class RegisterController {

    private UserService service;

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public String registerGet(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public String registerPost(@Valid @ModelAttribute("registerForm") RegisterForm registerForm,
                               BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "register";
        }
        service.registerNewUserAccount(registerForm);
        return "login";
    }

    @Autowired
    public RegisterController(UserService service) {
        this.service = service;
    }
}
