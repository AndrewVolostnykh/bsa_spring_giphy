package com.bsa.giphy.BSAGiphy.services;

import com.bsa.giphy.BSAGiphy.dto.GifFileDto;
import com.bsa.giphy.BSAGiphy.dto.Query;
import com.bsa.giphy.BSAGiphy.entities.GifEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GiphyService {

    Environment environment;

    @Autowired
    public GiphyService(Environment environment){
        this.environment = environment;
    }

    public GifEntity searchGif(String user_id, Query query){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(environment.getProperty("giphy.search.url"))
                .queryParam("api_key", environment.getProperty("giphy.api.key"))
                .queryParam("q", query.getQuery())
                .queryParam("limit", 1)
                .queryParam("random_id", user_id);

        GifFileDto gifFile = restTemplate.getForObject(builder.toUriString(), GifFileDto.class);

        JSONObject json = new JSONObject(gifFile);
        json = json.getJSONArray("data").getJSONObject(0);

        GifEntity gifEntity = new GifEntity();
        gifEntity.setId(json.getString("id"));
        gifEntity.setUrl(json.getString("url"));
        gifEntity.setQuery(query.getQuery());

        return gifEntity;
    }
}
