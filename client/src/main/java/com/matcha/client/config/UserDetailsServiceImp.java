package com.matcha.client.config;

import com.matcha.client.services.UserService;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.io.IOException;

public class UserDetailsServiceImp implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = null;
        try {
            user = UserService.getUserProfileForLogin(username);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        User.UserBuilder builder;
        if (user != null) {
            builder = User.withUsername(username);
            builder.password(user.getPassword());
            builder.roles("USER");
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }
}
