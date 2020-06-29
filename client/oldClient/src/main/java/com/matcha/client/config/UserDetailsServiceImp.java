package com.matcha.client.config;

import com.matcha.client.dto.UserProfileDto;
import com.matcha.client.services.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserDetailsServiceImp implements UserDetailsService {
    private UserService service = new UserService();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = null;
        try {
            UserProfileDto userProfile = service.getUserProfileForLogin(username);
            if (userProfile == null || StringUtils.isEmpty(userProfile.getPassword())) {
                return null;
            }
            Set<GrantedAuthority> grantedAuth = new HashSet<>();
            grantedAuth.add(new SimpleGrantedAuthority("USER"));
            user = new User(username, userProfile.getPassword(), grantedAuth);
        } catch (IOException | URISyntaxException e) {
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
