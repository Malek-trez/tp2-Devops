package com.example.tp2devops.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    // GET /hello?username=malek
    @GetMapping
    public String getHello(@RequestParam(name = "username") String username) {
        return "Hello " + username + "!";
    }

    // POST /hello  with body { "username": "malek" }
    @PostMapping
    public String postHello(@RequestBody UserRequest request) {
        return "Hello " + request.getUsername() + "!";
    }

    // Simple DTO
    public static class UserRequest {
        private String username;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
    }
}
