package com.matcha.client.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matcha.client.dto.UserDto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserService {


    public static User getUserProfileForLogin(String login) throws IOException, JSONException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:8080/getUserProfileForLogin");
        request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        StringEntity params = new StringEntity(String.format("{\"login\":\"%s\"} ", login));
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity == null) {
            return null;
        }
        JSONObject userProfile = new JSONObject(EntityUtils.toString(responseEntity));
        Set<GrantedAuthority> grantedAuths = new HashSet<>();
        grantedAuths.add(new SimpleGrantedAuthority("USER"));
        return new User(login, userProfile.getString("password"), grantedAuths);
    }

    //todo проверку существования почты
    public static boolean registerNewUserAccount(UserDto accountDto) throws IOException {
//        accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:8080/addUserProfile");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(accountDto);
        StringEntity params = new StringEntity(json);
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        return true;
    }
}