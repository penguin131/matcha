package com.matcha.client.config;

import com.matcha.client.services.UserService;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class UserDetailsServiceImp implements UserDetailsService {
    private UserService service = new UserService();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = null;
        try {
            user = service.getUserProfileForLogin(username);
        } catch (IOException | JSONException | URISyntaxException e) {
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
