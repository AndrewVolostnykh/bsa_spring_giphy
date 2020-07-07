package com.bsa.giphy.BSAGiphy.dto;

import java.time.LocalDate;

public class HistoryDto {
    LocalDate date;
    String query;
    String gif;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }
}
