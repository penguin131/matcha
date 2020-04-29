package com.matcha.client.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matcha.client.form.RegisterForm;
import com.matcha.client.form.UserForm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserService {

    private static final String serverPort = "8080";
    private static final String serverAddress = "localhost";

    public User getUserProfileForLogin(String login) throws IOException, JSONException, URISyntaxException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        URIBuilder builder = new URIBuilder(String.format("http://%s:%s/getUserProfileForLogin", serverAddress, serverPort));
        builder.setParameter("login", login);
        HttpGet request = new HttpGet(builder.build());
        HttpResponse response = httpClient.execute(request);
        HttpEntity responseEntity = response.getEntity();
        if(responseEntity.getContentLength() == 0) {
            return null;
        }
        JSONObject userProfile = new JSONObject(EntityUtils.toString(responseEntity));
        Set<GrantedAuthority> grantedAuth = new HashSet<>();
        grantedAuth.add(new SimpleGrantedAuthority("USER"));
        return new User(login, userProfile.getString("password"), grantedAuth);
    }

    public boolean registerNewUserAccount(RegisterForm registerForm) throws IOException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        registerForm.setPassword(encoder.encode(registerForm.getPassword()));
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(String.format("http://%s:%s/createUserProfile", serverAddress, serverPort));
        request.addHeader("content-type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(registerForm);
        StringEntity params = new StringEntity(json, ContentType.APPLICATION_JSON);
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        return response.getStatusLine().getStatusCode() == 200;
    }

    public boolean updateUserAccount(UserForm userForm) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(String.format("http://%s:%s/updateUserProfile", serverAddress, serverPort));
        request.addHeader("content-type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(userForm);
        StringEntity params = new StringEntity(json, ContentType.APPLICATION_JSON);
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        return response.getStatusLine().getStatusCode() == 200;
    }
}
