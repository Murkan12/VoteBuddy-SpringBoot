package com.ssi.votebuddy.controller;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

//    @GetMapping("/")
//    public String index() {
//        return "Hello";
//    }

    @GetMapping("/secured")
    public String secured() {
        return "Secured";
    }
}
