package com.bsa.giphy.BSAGiphy.controllers;

import com.bsa.giphy.BSAGiphy.dto.Query;
import com.bsa.giphy.BSAGiphy.entities.Cache;
import com.bsa.giphy.BSAGiphy.processors.FileSystemProcessor;
import com.bsa.giphy.BSAGiphy.services.GiphyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/")
public class UserController {

    GiphyService giphyService;
    FileSystemProcessor fileSystemProcessor;
    Cache cache;

    @Autowired
    public UserController(GiphyService giphyServ, FileSystemProcessor fileProcessor, Cache cache){
        this.giphyService = giphyServ;
        this.fileSystemProcessor = fileProcessor;
        this.cache = cache;
    }

    @PostMapping("{user_id}")
    public ResponseEntity<?> generateGif(@PathVariable String user_id, @RequestBody Query query) {

        //TODO: add history about adding file to folder
        Map<String, String> response = new HashMap<>();


        if (user_id.isEmpty() || user_id.isBlank() || user_id.matches(".*[|*?<>:/\"].*")) { // validation have to be in other class
            Map<String, String> tempResponse = new HashMap<>();
            tempResponse.put("message", "Invalid name, dont use | \\ ? < > * : / \" ");
            return new ResponseEntity<>(tempResponse, HttpStatus.BAD_REQUEST);
        }

        File gifFile = fileSystemProcessor.getGifPath(query.getQuery());
        if (gifFile != null) {
            File result = fileSystemProcessor.copyToUserFolder(user_id, query.getQuery(), gifFile.getPath());
            response.put("gif", result.getAbsolutePath());
            response.put("query", query.getQuery());

            cache.updateCache(user_id, query.getQuery(), result.getName());
        } else {
            var gifEntity = giphyService.searchGif(user_id, query);
            fileSystemProcessor.addGifToUserFolder(user_id, gifEntity);
            gifFile = fileSystemProcessor.getGifPath(query.getQuery());
            response.put("gif", gifFile.getAbsolutePath());
            response.put("query", query.getQuery());

            cache.updateCache(user_id, query.getQuery(), gifEntity.getId());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{user_id}")
    public ResponseEntity<?> searchGif(@PathVariable String user_id, @NotNull @RequestBody Query query) {
        return null;
    }

}
