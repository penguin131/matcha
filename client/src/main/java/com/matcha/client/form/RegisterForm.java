package com.matcha.client.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matcha.client.validation.LoginExists;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class RegisterForm {
    @NotEmpty(message = "Login must be not empty!")
    @LoginExists
    private String login;
    @NotEmpty(message = "Password must be not empty!")
    private String password;
    @NotEmpty(message = "Email must be not empty!")
    @Email
    private String email;
    @NotEmpty(message = "Sex must be not empty!")
    private String sex;
    @NotEmpty(message = "First name must be not empty!")
    @JsonProperty("first_name")
    private String firstName;
    @NotEmpty(message = "Last name must be not empty!")
    @JsonProperty("last_name")
    private String lastName;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public RegisterForm(String login,
                        String password,
                        String email,
                        String sex,
                        String firstName,
                        String lastName) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public RegisterForm() {

    }
}
