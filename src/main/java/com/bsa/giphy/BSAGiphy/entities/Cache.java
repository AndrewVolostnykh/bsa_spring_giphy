package com.bsa.giphy.BSAGiphy.entities;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Cache {
    private Map<String, Map<String, ArrayList<String>>> map;
    private static Cache uniqInstance;

    public static final String PATH_TO_FILES = "D:\\Developing\\git_reposes\\bsa_giphy\\src\\main\\java\\com\\bsa\\giphy\\BSAGiphy\\giphy\\"; // ????

    private Cache(){
        map = new HashMap<>();
    }

    public static Cache getInstance() {
        if(uniqInstance == null){
            uniqInstance = new Cache();
        }

        return uniqInstance;
    }

    public Map<String, Map<String, ArrayList<String>>> getCacheMap() {
        return this.map;
    }

    public void updateCache(String user_id, String query, String gifId){

        var tempList = new ArrayList<String>();

        if(this.map.get(user_id) != null){
            if(this.map.get(user_id).get(query) != null){
                tempList = this.map.get(user_id).get(query);
                tempList.add(tempList.size(), gifId);
                this.map.get(user_id).put(query, tempList);
            } else {
                tempList.add(gifId);
                this.map.get(user_id).put(query, tempList);
            }
        } else {
            var userMap = new HashMap<String, ArrayList<String>>();
            tempList.add(gifId);
            userMap.put(query, tempList);
            this.map.put(user_id, userMap);
        }

    }

    public String getGif (String user_id, String query) {
        if(this.map.get(user_id) != null) {
            if(this.map.get(user_id).get(query) != null) {
                var queriedList = new ArrayList<String>();
                queriedList = this.map.get(user_id).get(query);
                return queriedList.get(new Random().nextInt(queriedList.size()));
            }
        }
        return null;
    }
}
