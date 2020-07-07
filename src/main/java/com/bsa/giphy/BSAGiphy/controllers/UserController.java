package com.bsa.giphy.BSAGiphy.controllers;

import com.bsa.giphy.BSAGiphy.dto.HistoryDto;
import com.bsa.giphy.BSAGiphy.dto.Query;
import com.bsa.giphy.BSAGiphy.entities.Cache;
import com.bsa.giphy.BSAGiphy.processors.FileSystemProcessor;
import com.bsa.giphy.BSAGiphy.processors.Parser;
import com.bsa.giphy.BSAGiphy.services.GiphyService;
import com.bsa.giphy.BSAGiphy.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Cache cache; // this works because spring using reflection, i know this ;)
    UserUtil userUtil;
    Parser parser;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(GiphyService giphyServ,
                          FileSystemProcessor fileProcessor,
                          Cache cache,
                          UserUtil userUtil,
                          Parser parser){
        this.giphyService = giphyServ;
        this.fileSystemProcessor = fileProcessor;
        this.cache = cache;
        this.userUtil = userUtil;
        this.parser = parser;
    }

    @PostMapping("{user_id}")
    public ResponseEntity<?> generateGif(@RequestParam @PathVariable String user_id,
                                         @NotBlank @RequestBody Query query,
                                         @RequestHeader(name="X-BSA-GIPHY") boolean present) {
        if(validator(user_id) != null) {
            return validator(user_id);
        }

        System.out.println(present);

        logger.info("POST request by USER: " + user_id + ", CLASS: UserController, METHOD: generateGif(...); QUERY: " + query.getQuery());

        Map<String, String> response = new HashMap<>();

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
    public ResponseEntity<?> searchGif(@PathVariable String user_id,
                                       @NotBlank String query
                                       /*@RequestHeader(name="X-BSA-GIPHY") boolean present*/) {
        if(validator(user_id) != null) {
            return validator(user_id);
        }

        logger.info("GET request, USER: " + user_id + ", CLASS: UserController, METHOD: searchGif, QUERY: " + query);

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

    @GetMapping("{user_id}/all")
    public ResponseEntity<?> getAllGifs(@PathVariable String user_id) {
        if(validator(user_id) != null) {
            return validator(user_id);
        }

        logger.info("GET request, USER: " + user_id + ", CLASS: UserController, METHOD: getAllGifs(...)");

        var result = parser.parseFullCache(fileSystemProcessor.getFullUserCache(user_id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{user_id}/history")
    public ResponseEntity<?> getHistory(@PathVariable String user_id) {
        if(validator(user_id) != null) {
            return validator(user_id);
        }

        logger.info("GET request, USER: " + user_id + ", CLASS: UserController, METHOD: getHistory(...)");

        HistoryDto[] result = parser.parseHistory(fileSystemProcessor.getHistory(user_id));
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{user_id}/history/clean")
    public ResponseEntity<?> cleanHistory(@PathVariable String user_id) {
        if(validator(user_id) != null) {
            return validator(user_id);
        }

        logger.info("DELETE request, USER: " + user_id + ", CLASS: UserController, METHOD: clearHistory(...)");

        if(fileSystemProcessor.deleteHistory(user_id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("{user_id}/search")
    public ResponseEntity<?> searchGif(@PathVariable String user_id, String query, boolean force) {
        if(validator(user_id) != null) {
            return validator(user_id);
        }

        logger.info("GET request, USER: " + user_id + ", CLASS: UserController, METHOD: searchGif(...), QUERY: " + query + ", FORCE: " + force);

        if(!force) {
            var gif = cache.getGif(user_id, query);
            if(gif != null) {
                return new ResponseEntity<>(gif, HttpStatus.OK);
            }
        }

        var gif = fileSystemProcessor.getGifPath(query);

        if(gif == null) {
            return ResponseEntity.notFound().build();
        }

        cache.updateCache(user_id, query, gif.getAbsolutePath());

        return new ResponseEntity<>(gif.getAbsolutePath(), HttpStatus.OK);
    }

    @PostMapping("{user_id}/generate")
    public ResponseEntity<?> createGif(@PathVariable String user_id, @RequestBody Query query) {
        if(validator(user_id) != null) {
            return validator(user_id);
        }

        logger.info("POST request, USER: " + user_id + ", CLASS: UserController, METHOD: createGif(...), QUERY: " + query.getQuery() + ", FORCE: " + query.getForce());

        File gifFile;
        if(!query.getForce()) {
            System.out.println("Taking from a memory works");
            gifFile = fileSystemProcessor.getGifPath(query.getQuery());
            if(gifFile != null) {
                return new ResponseEntity<>(gifFile.getAbsolutePath(), HttpStatus.OK);
            }
        }

        var gifEntity = giphyService.searchGif(null, query);
        var resultFile = fileSystemProcessor.copyToUserFolder(user_id, query.getQuery(), fileSystemProcessor.downloadGifByUrl(gifEntity).getPath());

        if(resultFile == null) {
            return ResponseEntity.notFound().build();
        }

        gifFile = fileSystemProcessor.getGifPath(query.getQuery());

        return new ResponseEntity<>(gifFile, HttpStatus.OK);
    }

    @DeleteMapping("{user_id}/reset")
    public ResponseEntity<?> clearMemoryCache(@PathVariable String user_id, String query) {
        if(validator(user_id) != null) {
            return validator(user_id);
        }

        logger.info("DELETE request, USER: " + user_id + ", CLASS: UserController, METHOD: clearMemoryCache(...), QUERY:" + query);

        if(query == null) {
            cache.resetUser(user_id);
        } else {
            cache.resetUser(user_id, query);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{user_id}/clean")
    public ResponseEntity<?> deleteUser(@PathVariable String user_id) {
        if(validator(user_id) != null) {
            return validator(user_id);
        }

        logger.info("DELETE request, USER: " + user_id + ", CLASS: UserController, METHOD: deleteUser(...)");

        cache.resetUser(user_id);
        fileSystemProcessor.clearUserCache(user_id);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> validator(String user_id) {
        if (userUtil.invalid(user_id)) {
            Map<String, String> tempResponse = new HashMap<>();
            tempResponse.put("message", "Invalid request");
            return new ResponseEntity<>(tempResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
