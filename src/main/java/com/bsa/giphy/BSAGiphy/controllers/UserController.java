package com.bsa.giphy.BSAGiphy.controllers;

import com.bsa.giphy.BSAGiphy.dto.GifFileDto;
import com.bsa.giphy.BSAGiphy.dto.Query;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

@RestController
@RequestMapping("/user/")
public class UserController {
    private Environment environment; // this variable using to query data from application.properties

    @Autowired
    public UserController(Environment env){
        this.environment = env;
    }

    @PostMapping("{user_id}")
    public ResponseEntity<?> generateGif(@PathVariable String user_id, @RequestBody Query query) {

        System.out.println("Inputed user id: " + user_id);
        System.out.println(query.getQuery());

        if(user_id.isEmpty() || user_id.isBlank() || user_id.matches(".*[|*?<>:\"].*")){
            return new ResponseEntity<>(new HashMap<String, String>().put("message", "Impossible to use this id"), HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("api_key", environment.getProperty("giphy.api.key"));
        requestMap.put("q", query.getQuery());
        requestMap.put("limit", 1);
        requestMap.put("random_id", user_id);

        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(environment.getProperty("giphy.search.url"))
                .queryParam("api_key", environment.getProperty("giphy.api.key"))
                .queryParam("q", query.getQuery())
                .queryParam("limit", 1)
                .queryParam("random_id", user_id);

        GifFileDto gifFile = restTemplate.getForObject(builder.toUriString(), GifFileDto.class);

        JSONObject json = new JSONObject(gifFile);
        System.out.println(json.getJSONArray("data").getJSONObject(0).getString("url"));

        return new ResponseEntity<>("success", HttpStatus.OK);


    }

}
