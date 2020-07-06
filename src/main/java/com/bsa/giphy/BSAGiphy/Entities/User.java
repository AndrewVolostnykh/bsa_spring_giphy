package com.bsa.giphy.BSAGiphy.Entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class User {

    @NotBlank(message="User id cannot be blank")
    @Size(min=3) // idk what id will be ? String or integer ?
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
