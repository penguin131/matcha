package com.matcha.client.controllers;

import com.matcha.client.form.RegisterForm;
import com.matcha.client.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class RegisterController {

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public String registerGet(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public RedirectView registerPost(@ModelAttribute RegisterForm registerForm) throws IOException {
        //todo валидацию
        UserService.registerNewUserAccount(registerForm);
        return new RedirectView("/login");
    }
}
