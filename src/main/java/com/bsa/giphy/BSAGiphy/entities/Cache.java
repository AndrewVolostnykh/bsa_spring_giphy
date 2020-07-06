package com.bsa.giphy.BSAGiphy.entities;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    private Map<String, Map<String, String[]>> map;
    private Cache uniqInstance;

    private Cache(){
        map = new HashMap<>();
    }

    public Cache getInstance() {
        if(uniqInstance == null){
            this.uniqInstance = new Cache();
        }

        return this.uniqInstance;
    }

    public Map<String, Map<String, String[]>> getCacheMap() {
        return this.map;
    }

    public void addUserGif(String user_id, String query, String gifId){
        //TODO: logic for adding gif, user and query to cache
    }
}
