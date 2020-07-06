package com.bsa.giphy.BSAGiphy.controllers;

import com.bsa.giphy.BSAGiphy.Entities.User;
import com.bsa.giphy.BSAGiphy.dto.SearchingResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/*Here will be logic for working with user requests
 * like DELETE/user/:id/clean
 * */
@RestController
@RequestMapping("/user/")
public class UserController {
    /*DESCRIPTION*/
    //TODO: Пользователь отправляет свой ид и ключевое слово сначала сюда, проверили есть ли такой файл в его папке,
    // если нет идем на giphy.com и генерируем этот файл

    @PostMapping("{user_id}/")
    public ResponseEntity<?> generateGif(@Valid @PathVariable User user_id, String query, BindingResult result) {

        if(result.hasErrors()) { // basic validation?
            Map<String, String> errors = new HashMap<>();
            for(FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("api_key", "FigBVm0dXD95M8LIRURK7vzLTF9il8jd");
        requestMap.put("q", query);
        requestMap.put("limit", 1);
        requestMap.put("random_id", user_id);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity("api.giphy.com/v1/gifs/search", SearchingResultDto.class, requestMap);

    }



}
