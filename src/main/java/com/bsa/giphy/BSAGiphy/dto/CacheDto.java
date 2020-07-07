package com.bsa.giphy.BSAGiphy.dto;

import org.springframework.stereotype.Component;

@Component
public class CacheDto {
    private String query;
    private String[] gifs;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getGifs() {
        return gifs;
    }

    public void setGifs(String[] gifs) {
        this.gifs = gifs;
    }
}
