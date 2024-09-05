package com.infovault.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to InfoVault!";
    }

    @GetMapping("/public/test")
    public String test() {
        return "This is a public test endpoint.";
    }
}
