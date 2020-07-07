package com.bsa.giphy.BSAGiphy.controllers;

import com.bsa.giphy.BSAGiphy.dto.CacheDto;
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

@RestController
public class GeneralController {

    GiphyService giphyService;
    FileSystemProcessor fileSystemProcessor;
    Cache cache; // this works because spring using reflection, i know this ;)
    UserUtil userUtil;
    Parser parser;
    CacheDto cacheDto;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public GeneralController(GiphyService giphyServ,
                          FileSystemProcessor fileProcessor,
                          Cache cache,
                          UserUtil userUtil,
                          Parser parser,
                          CacheDto cacheDto){
        this.giphyService = giphyServ;
        this.fileSystemProcessor = fileProcessor;
        this.cache = cache;
        this.userUtil = userUtil;
        this.parser = parser;
        this.cacheDto = cacheDto;
    }

    @GetMapping("/gifs")
    public ResponseEntity<?> getAllGifs() {

        logger.info("GET request, CLASS: GeneralController, METHOD: getAllGifs(...)");

        var arrayOfAllGifs = parser.parseOnlyFiles(fileSystemProcessor.getFullCache(null));
        return new ResponseEntity<>(arrayOfAllGifs, HttpStatus.OK);
    }

    @GetMapping("/cache")
    public ResponseEntity<?> getDiskCache(String query) {

        logger.info("GET request, CLASS: GeneralController, METHOD: getDiskCache(...), QUERY: " + query);

        var fullCache = parser.parseFullCache(fileSystemProcessor.getFullCache(query));

        return new ResponseEntity<>(fullCache, HttpStatus.OK);
    }

    @DeleteMapping("/cache")
    public ResponseEntity<?> clearDiskCache() {
        logger.info("DELETE request, CLASS: GeneralController, METHOD: clearDickCache(...)");

        fileSystemProcessor.clearCache();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cache/generate")
    public ResponseEntity<?> generateGif(@RequestBody Query query) {
        logger.info("POST request, CLASS: GeneralController, METHOD: generateGif(...), QUERY:" + query.getQuery() + ", FORCE: " + query.getForce());

        var giphy = giphyService.searchGif(null, query);
        fileSystemProcessor.downloadGifByUrl(giphy);

        CacheDto cache = parser.parseFullCache(fileSystemProcessor.getFullCache(query.getQuery()))[0];

        return new ResponseEntity<>(cache, HttpStatus.OK);
    }


}
