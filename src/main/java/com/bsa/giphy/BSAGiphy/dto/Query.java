package com.bsa.giphy.BSAGiphy.dto;

public class Query {
    private String query;
    private Boolean force;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }
}
