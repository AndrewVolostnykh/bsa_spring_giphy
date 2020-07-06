package com.bsa.giphy.BSAGiphy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GifFileDto implements Serializable {
    private Object[] data;

    public Object getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

}
