package com.mohit.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<String> HomeController() {
        System.out.println("Home Controller");
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
}
