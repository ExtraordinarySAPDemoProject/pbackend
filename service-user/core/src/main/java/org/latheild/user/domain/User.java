package org.latheild.user.domain;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

public class User {
    @Id
    @NotNull
    private String id;

    private String email;

    private String password;

    private String access;

    private String[] trainings;

    private String[] comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String[] getTrainings() {
        return trainings;
    }

    public void setTrainings(String[] trainings) {
        this.trainings = trainings;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }
}
