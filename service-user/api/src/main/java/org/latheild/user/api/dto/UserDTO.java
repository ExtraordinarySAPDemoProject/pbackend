package org.latheild.user.api.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private String userId;

    private String email;

    private String[] trainings;

    private String[] comments;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
