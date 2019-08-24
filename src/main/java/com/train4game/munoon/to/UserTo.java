package com.train4game.munoon.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserTo extends AbstractBaseTo {
    @Size(min = 2, max = 200)
    @NotBlank
    @SafeHtml
    String name;

    @Email
    @NotBlank
    @Size(min = 3, max = 200)
    @SafeHtml
    String email;

    @NotBlank
    @Size(min = 5, max = 200)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                '}';
    }
}
