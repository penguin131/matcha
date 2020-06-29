package com.matcha.client.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matcha.client.config.AppConfig;
import com.matcha.client.dto.UserProfileDto;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class UserService {

    //todo попробовать избавиться от шаблонного кода исполнения запроса в конце с помощью средств спринга(а это возможно?)
    private HttpPost prepareJsonPostRequest(String url, Object form) {
        HttpPost request = new HttpPost(String.format(
                "http://%s:%s/%s",
                AppConfig.getServerAddress(),
                AppConfig.getServerPort(),
                url));
        request.addHeader("content-type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(form);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            //todo log
            return null;
        }
        StringEntity params = new StringEntity(json, ContentType.APPLICATION_JSON);
        request.setEntity(params);
        return request;
    }

    public UserProfileDto getUserProfileForLogin(@NotNull String login) throws IOException, URISyntaxException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        URIBuilder builder = new URIBuilder(String.format(
                "http://%s:%s/getUserProfileForLogin",
                AppConfig.getServerAddress(),
                AppConfig.getServerPort()));
        builder.setParameter("login", login);
        HttpGet request = new HttpGet(builder.build());
        HttpResponse response = httpClient.execute(request);
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity.getContentLength() == 0) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(EntityUtils.toString(responseEntity), UserProfileDto.class);
    }

    public boolean registerNewUserAccount(@NotNull RegisterForm registerForm) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        registerForm.setPassword(encoder.encode(registerForm.getPassword()));
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = prepareJsonPostRequest("createUserProfile", registerForm);
        try {
            HttpResponse response = httpClient.execute(request);
            //todo log
            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateUserAccount(@NotNull UserForm userForm) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = prepareJsonPostRequest("updateUserProfile", userForm);
        try {
            HttpResponse response = httpClient.execute(request);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException ex) {
            ex.printStackTrace();
            //todo log
            return false;
        }
    }
}
