package com.matcha.client.controllers;

import com.matcha.client.form.UserForm;
import com.matcha.client.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class UserProfileController {

    private UserService service;

    @RequestMapping(method = RequestMethod.GET, value = "/profile")
    public String registerGet(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "profile";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/profile")
    public RedirectView registerPost(@ModelAttribute UserForm userForm) throws IOException {
        //todo валидацию + update user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userForm.setLogin(user.getUsername());
        service.updateUserAccount(userForm);
        return new RedirectView("/");
    }

    @Autowired
    public UserProfileController(UserService service) {
        this.service = service;
    }
}
