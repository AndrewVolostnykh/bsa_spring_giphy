package com.bsa.giphy.BSAGiphy.dto;

import org.springframework.stereotype.Component;

@Component
public class User {

    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
