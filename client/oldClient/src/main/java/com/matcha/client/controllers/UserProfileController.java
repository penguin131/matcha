package com.matcha.client.controllers;

import com.matcha.client.dto.UserProfileDto;
import com.matcha.client.form.UserForm;
import com.matcha.client.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class UserProfileController {

    private UserService service;

    // todo Мб не дергать постоянно сервер, а сохранять где то на форме всю инфу по юзеру?
    //  Так же можно запилить контекст в переменную, но я не знаю как его нормально проинициализировать.
    //   Но это уже больше про spring MVC который мало где юзается.
    @RequestMapping(method = RequestMethod.GET, value = "/profile")
    public String registerGet(Model model) throws IOException, URISyntaxException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        UserProfileDto userProfile = service.getUserProfileForLogin(username);
        model.addAttribute("userForm", new UserForm(
                username,
                userProfile.getConfirmed(),
                userProfile.getSexPreferences(),
                userProfile.getBiography()));
        return "profile";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/profile")
    public String registerPost(@Valid @ModelAttribute("userForm") UserForm userForm,
                                     BindingResult result) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        userForm.setLogin(username);
        if (result.hasErrors() || !service.updateUserAccount(userForm)) {
            return "profile";
        }
        return "index";
    }

    @Autowired
    public UserProfileController(UserService service) {
        this.service = service;
    }
}
