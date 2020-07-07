package com.bsa.giphy.BSAGiphy.controllers;

import com.bsa.giphy.BSAGiphy.dto.Query;
import com.bsa.giphy.BSAGiphy.entities.Cache;
import com.bsa.giphy.BSAGiphy.processors.FileSystemProcessor;
import com.bsa.giphy.BSAGiphy.services.GiphyService;
import com.bsa.giphy.BSAGiphy.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/")
public class UserController {

    GiphyService giphyService;
    FileSystemProcessor fileSystemProcessor;
    Cache cache;
    UserUtil userUtil;

    @Autowired
    public UserController(GiphyService giphyServ,
                          FileSystemProcessor fileProcessor,
                          Cache cache,
                          UserUtil userUtil){
        this.giphyService = giphyServ;
        this.fileSystemProcessor = fileProcessor;
        this.cache = cache;
        this.userUtil = userUtil;
    }

    @PostMapping("{user_id}")
    public ResponseEntity<?> generateGif(@PathVariable String user_id, @NotBlank @RequestBody Query query) {

        Map<String, String> response = new HashMap<>();

        //TODO: query like an entity unnecessary

        if (userUtil.invalid(user_id)) {
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
    public ResponseEntity<?> searchGif(@PathVariable String user_id, @NotBlank String query) {

        var response = new HashMap<String, String>();

        if(userUtil.invalid(user_id) || query.isBlank() || query.isEmpty()) {
            response.put("message", "Invalid request ");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(cache.getGif(user_id, query) != null) {
            response.put("gif", fileSystemProcessor.getSTORAGE_PATH() + cache.getGif(user_id, query) + ".gif");
            response.put("query", query);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            if (fileSystemProcessor.getFromUserFolder(user_id, query) != null) {
                System.out.println("Taken from file system");
                File gif = fileSystemProcessor.getFromUserFolder(user_id, query);
                response.put("gif", gif.toPath().toString());
                response.put("query", query);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Cant find this file!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }

    }

}
