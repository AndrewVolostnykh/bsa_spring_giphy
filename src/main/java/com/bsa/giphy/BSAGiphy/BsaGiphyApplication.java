package com.bsa.giphy.BSAGiphy;

import com.bsa.giphy.BSAGiphy.exception.NoBsaGiphyHeaderException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@SpringBootApplication
public class BsaGiphyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsaGiphyApplication.class, args);
	}

}
